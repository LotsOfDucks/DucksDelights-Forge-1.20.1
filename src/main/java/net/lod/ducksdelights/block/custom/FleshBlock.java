package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.block.custom.blockstate_properties.ModBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FleshBlock extends Block {
    protected static final VoxelShape SQUISH_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);
    protected static final VoxelShape FULL_SHAPE = Shapes.block();
    public static final BooleanProperty IS_SPREADING;
    public static final BooleanProperty IS_FULL;


    public FleshBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(IS_SPREADING, false).setValue(IS_FULL, true));
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

    public boolean isRandomlyTicking(BlockState pState) {
        return this.isSpreading(pState);
    }

    public boolean isSpreading(BlockState state) {
        return state.getValue(IS_SPREADING);
    }

    public boolean canSpread(Level level, BlockPos pos) {
        int availableAir = 0;
        for (Direction directions : Direction.values()) {
            availableAir += this.obtainAir(level, pos.relative(directions));
        }
        return (availableAir == 1 || availableAir == 2);
    }

    public int obtainAir(Level level, BlockPos pos) {
        BlockState checkedState = level.getBlockState(pos);
        if (checkedState.is(BlockTags.REPLACEABLE)) {
            return 1;
        } else {
            return 0;
        }
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

            if (!pState.getValue(IS_SPREADING)) {
                if (this.canSpread(pLevel, pPos)) {
                    newState = newState.setValue(IS_SPREADING, true);
                }
            }
            if (newState != pState) {
                pLevel.setBlockAndUpdate(pPos, newState);
            }
        }
    }

    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(IS_SPREADING)) {
            if (!this.canSpread(pLevel, pPos)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(IS_SPREADING, false));
            } else if (pRandom.nextIntBetweenInclusive(1, 5) == 5) {
                for (Direction directions : Direction.values()) {
                    this.trySpread(pLevel, pPos.relative(directions), pRandom);
                }
            }
        }
    }

    public void trySpread(ServerLevel level, BlockPos relative, RandomSource randomSource) {
        BlockState checkedState = level.getBlockState(relative);
        if (checkedState.is(BlockTags.REPLACEABLE)) {
            if (randomSource.nextIntBetweenInclusive(1, 2) == 2) {
                level.setBlockAndUpdate(relative, ModBlocks.FLESH_BLOCK.get().defaultBlockState().setValue(IS_SPREADING, true));
            }
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(IS_SPREADING, IS_FULL);
    }

    static {
        IS_SPREADING = ModBlockStateProperties.IS_SPREADING_FLESH;
        IS_FULL = ModBlockStateProperties.IS_FULL;
    }
}
