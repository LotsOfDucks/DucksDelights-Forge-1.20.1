package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlockStateProperties;
import net.lod.ducksdelights.block.custom.interfaces.ISimpleWaterAndLavaloggedBlock;
import net.lod.ducksdelights.block.entity.ModBlockEntities;
import net.lod.ducksdelights.block.entity.ResonatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ResonatorBlock extends BaseEntityBlock implements ISimpleWaterAndLavaloggedBlock {
    public static final DirectionProperty FACING;
    public static final BooleanProperty BREAKING;
    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty LAVALOGGED;
    public static final BooleanProperty LOGGED;
    protected static final VoxelShape NORTH;
    protected static final VoxelShape SOUTH;
    protected static final VoxelShape WEST;
    protected static final VoxelShape EAST;
    protected static final VoxelShape UP;
    protected static final VoxelShape DOWN;




    public ResonatorBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(BREAKING, false).setValue(WATERLOGGED, false).setValue(LAVALOGGED, false).setValue(LOGGED, false));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ResonatorBlockEntity(blockPos, blockState);
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        FluidState fluidstate = pContext.getLevel().getFluidState(blockpos);
        return this.defaultBlockState().setValue(FACING, pContext.getClickedFace().getOpposite()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER).setValue(LAVALOGGED, fluidstate.getType() == Fluids.LAVA).setValue(LOGGED, (fluidstate.getType() == (Fluids.LAVA) || fluidstate.getType() == (Fluids.WATER)));
    }

    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : pState.getValue(LAVALOGGED) ? Fluids.LAVA.getSource(false) : super.getFluidState(pState);
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

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
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

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof ResonatorBlockEntity) {
            ((ResonatorBlockEntity) blockEntity).breakingBlock(pLevel, pState, pPos, pPos.relative(pState.getValue(FACING)), (ResonatorBlockEntity) blockEntity);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, WATERLOGGED, LAVALOGGED, LOGGED, BREAKING);
    }

    static {
        FACING = BlockStateProperties.FACING;
        BREAKING = ModBlockStateProperties.BREAKING;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        LAVALOGGED = ModBlockStateProperties.LAVALOGGED;
        LOGGED = ModBlockStateProperties.LOGGED;
        NORTH = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 8.0);
        SOUTH = Block.box(0.0, 0.0, 8.0, 16.0, 16.0, 16.0);
        WEST = Block.box(0.0, 0.0, 0.0, 8.0, 16.0, 16.0);
        EAST = Block.box(8.0, 0.0, 0.0, 16.0, 16.0, 16.0);
        UP = Block.box(0.0, 8.0, 0.0, 16.0, 16.0, 16.0);
        DOWN = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    }
}
