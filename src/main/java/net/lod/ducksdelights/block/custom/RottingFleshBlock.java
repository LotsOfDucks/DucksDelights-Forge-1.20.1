package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.block.custom.blockstate_properties.ModBlockStateProperties;
import net.lod.ducksdelights.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RottingFleshBlock extends Block {
    protected static final VoxelShape SQUISH_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);
    protected static final VoxelShape FULL_SHAPE = Shapes.block();
    public static final BooleanProperty IS_FULL;

    public RottingFleshBlock(Properties pProperties) {
        super(pProperties);
    }

    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (!pState.getValue(IS_FULL)) {
            return SQUISH_SHAPE;
        } else {
            return FULL_SHAPE;
        }
    }

    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return Shapes.block();
    }

    public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
        return Shapes.block();
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(IS_FULL, pContext.getLevel().getBlockState(pContext.getClickedPos().above()).isSolid());
    }

    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (!pLevel.isClientSide) {
            BlockState newState = pState;
            if (!pState.getValue(IS_FULL)) {
                if (pLevel.getBlockState(pPos.above()).isSolid()) {
                    newState = newState.setValue(IS_FULL, true);
                }
            } else {
                if (!pLevel.getBlockState(pPos.above()).isSolid()) {
                    newState = newState.setValue(IS_FULL, false);
                }
            }
            if (newState != pState) {
                pLevel.setBlockAndUpdate(pPos, newState);
            }
        }
    }

    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        for (Direction directions : Direction.values()) {
            this.trySpread(pLevel, pPos.relative(directions), pRandom);
            pLevel.scheduleTick(pPos, this, 600 + pLevel.getRandom().nextInt(40));
        }
    }

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        for (Direction directions : Direction.values()) {
            this.trySpread(pLevel, pPos.relative(directions), pRandom);
        }
        pLevel.destroyBlock(pPos, true);
    }

    public void trySpread(ServerLevel level, BlockPos relative, RandomSource randomSource) {
        BlockState checkedState = level.getBlockState(relative);
        if (checkedState.is(ModBlocks.FLESH_BLOCK.get())) {
            level.setBlockAndUpdate(relative, ModBlocks.ROTTING_FLESH_BLOCK.get().defaultBlockState());
            level.levelEvent(null, 2001, relative, Block.getId(ModBlocks.ROTTING_FLESH_BLOCK.get().defaultBlockState()));
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(IS_FULL);
    }

    static {
        IS_FULL = ModBlockStateProperties.IS_FULL;
    }
}
