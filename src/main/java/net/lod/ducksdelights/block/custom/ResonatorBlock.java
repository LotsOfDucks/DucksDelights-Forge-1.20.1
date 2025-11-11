package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlockStateProperties;
import net.lod.ducksdelights.block.entity.ModBlockEntities;
import net.lod.ducksdelights.block.entity.ResonatorBlockEntity;
import net.lod.ducksdelights.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ResonatorBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING;
    public static final BooleanProperty BREAKING;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape NORTH;
    protected static final VoxelShape SOUTH;
    protected static final VoxelShape WEST;
    protected static final VoxelShape EAST;
    protected static final VoxelShape UP;
    protected static final VoxelShape DOWN;




    public ResonatorBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(BREAKING, false).setValue(WATERLOGGED, false));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ResonatorBlockEntity(blockPos, blockState);
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        FluidState fluidstate = pContext.getLevel().getFluidState(blockpos);
        return this.defaultBlockState().setValue(FACING, pContext.getClickedFace().getOpposite()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(FACING)) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
            case UP -> UP;
            case DOWN -> DOWN;
        };
    }

    public static boolean getBreaking(BlockState blockState) {
        return blockState.getValue(BREAKING);
    }

    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide ? createTickerHelper(pBlockEntityType, ModBlockEntities.RESONATOR_BE.get(), (level, blockPos, blockState, resonatorBlockEntity) -> {
            VibrationSystem.Ticker.tick(level, resonatorBlockEntity.getVibrationData(), resonatorBlockEntity.getVibrationUser());
        }) : null;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, WATERLOGGED, BREAKING);
    }

    static {
        FACING = BlockStateProperties.FACING;
        BREAKING = ModBlockStateProperties.BREAKING;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        NORTH = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
        SOUTH = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
        WEST = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
        EAST = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
        UP = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
        DOWN = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    }
}
