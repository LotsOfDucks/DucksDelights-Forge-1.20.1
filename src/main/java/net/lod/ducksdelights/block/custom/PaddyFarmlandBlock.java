package net.lod.ducksdelights.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.FarmlandWaterManager;
import net.minecraftforge.common.IPlantable;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class PaddyFarmlandBlock extends Block implements SimpleWaterloggedBlock {
    public static final IntegerProperty MOISTURE;
    protected static final VoxelShape SHAPE;
    public static final BooleanProperty WATERLOGGED;
    public static final int MAX_MOISTURE = 7;

    public PaddyFarmlandBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false).setValue(MOISTURE, 0));
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pState.canSurvive(pLevel, pPos)) {
            turnToDirt(null, pState, pLevel, pPos);
        }

    }

    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        int moisture = pState.getValue(MOISTURE);
        if (!isNearWater(pLevel, pPos) && !pLevel.isRainingAt(pPos.above())) {
            if (moisture > 0) {
                pLevel.setBlock(pPos, pState.setValue(MOISTURE, moisture - 1), 2);
            } else if (!shouldMaintainFarmland(pLevel, pPos)) {
                turnToDirt(null, pState, pLevel, pPos);
            }
        } else if (!pState.getValue(WATERLOGGED)) {
            pLevel.setBlock(pPos, pState.setValue(MOISTURE, 6), 2);
        } else {
            pLevel.setBlock(pPos, pState.setValue(MOISTURE, 7), 2);
        }

    }

    public static void turnToDirt(@javax.annotation.Nullable Entity pEntity, BlockState pState, Level pLevel, BlockPos pPos) {
        BlockState blockstate = pushEntitiesUp(pState, Blocks.DIRT.defaultBlockState(), pLevel, pPos);
        pLevel.setBlockAndUpdate(pPos, blockstate);
        pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pEntity, blockstate));
    }

    private static boolean shouldMaintainFarmland(BlockGetter pLevel, BlockPos pPos) {
        return pLevel.getBlockState(pPos.above()).is(BlockTags.MAINTAINS_FARMLAND);
    }

    private static boolean isNearWater(LevelReader pLevel, BlockPos pPos) {
        Iterator iterator = BlockPos.betweenClosed(pPos.offset(-4, 0, -4), pPos.offset(4, 1, 4)).iterator();

        BlockPos blockpos;
        do {
            if (!iterator.hasNext()) {
                return false;
            }

            blockpos = (BlockPos)iterator.next();
        } while(pLevel.getFluidState(blockpos).is(FluidTags.WATER));

        return true;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        FluidState fluidState = pContext.getLevel().getFluidState(pContext.getClickedPos());
        return this.defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER).setValue(MOISTURE, fluidState.getType() == Fluids.WATER ? 7 : 0);
    }

    public boolean useShapeForLightOcclusion(BlockState pState) {
        return true;
    }

    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        switch (pType) {
            case LAND -> {
                return false;
            }
            case WATER -> {
                return pLevel.getFluidState(pPos).is(FluidTags.WATER);
            }
            case AIR -> {
                return false;
            }
            default -> {
                return false;
            }
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(MOISTURE, WATERLOGGED);
    }

    static {
        MOISTURE = BlockStateProperties.MOISTURE;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0);
    }
}
