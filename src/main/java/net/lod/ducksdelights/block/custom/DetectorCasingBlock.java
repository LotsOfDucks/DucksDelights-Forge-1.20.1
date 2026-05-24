package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.custom.blockstate_properties.ModBlockStateProperties;
import net.lod.ducksdelights.block.custom.interfaces.ISimpleWaterAndLavaloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DetectorCasingBlock extends Block implements ISimpleWaterAndLavaloggedBlock {
    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty LAVALOGGED;
    public static final BooleanProperty LOGGED;
    protected static final VoxelShape BASE1;
    protected static final VoxelShape BASE2;
    protected static final VoxelShape BASE3;
    protected static final VoxelShape BASE4;
    protected static final VoxelShape SIDE1;
    protected static final VoxelShape SIDE2;
    protected static final VoxelShape SIDE3;
    protected static final VoxelShape SIDE4;
    protected static final VoxelShape TOP1;
    protected static final VoxelShape TOP2;
    protected static final VoxelShape TOP3;
    protected static final VoxelShape TOP4;
    protected static final VoxelShape SHAPE;

    public DetectorCasingBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(LAVALOGGED, false).setValue(LOGGED, false));
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return SHAPE;
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        FluidState fluidState = pContext.getLevel().getFluidState(pContext.getClickedPos());
        return this.defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER).setValue(LAVALOGGED, fluidState.getType() == Fluids.LAVA).setValue(LOGGED, (fluidState.getType() == (Fluids.LAVA) || fluidState.getType() == (Fluids.WATER)));
    }

    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : pState.getValue(LAVALOGGED) ? Fluids.LAVA.getSource(false) : super.getFluidState(pState);
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }
        if (pState.getValue(LAVALOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.LAVA, Fluids.LAVA.getTickDelay(pLevel));
        }

        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(WATERLOGGED, LAVALOGGED, LOGGED);
    }

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        LAVALOGGED = ModBlockStateProperties.LAVALOGGED;
        LOGGED = ModBlockStateProperties.LOGGED;
        BASE1 = Block.box(0.0, 0.0, 0.0, 4.0, 4.0, 16.0);
        BASE2 = Block.box(12.0, 0.0, 0.0, 16.0, 4.0, 16.0);
        BASE3 = Block.box(4.0, 0.0, 0.0, 12.0, 4.0, 4.0);
        BASE4 = Block.box(4.0, 0.0, 12.0, 12.0, 4.0, 16.0);

        SIDE1 = Block.box(0.0, 4.0, 0.0, 4.0, 12.0, 4.0);
        SIDE2 = Block.box(12.0, 4.0, 12.0, 16.0, 12.0, 16.0);
        SIDE3 = Block.box(0.0, 4.0, 12.0, 4.0, 12.0, 16.0);
        SIDE4 = Block.box(12.0, 4.0, 0.0, 16.0, 12.0, 4.0);

        TOP1 = Block.box(0.0, 12.0, 0.0, 4.0, 16.0, 16.0);
        TOP2 = Block.box(12.0, 12.0, 0.0, 16.0, 16.0, 16.0);
        TOP3 = Block.box(4.0, 12.0, 0.0, 12.0, 16.0, 4.0);
        TOP4 = Block.box(4.0, 12.0, 12.0, 12.0, 16.0, 16.0);
        SHAPE = Shapes.or(BASE1,BASE2,BASE3,BASE4,SIDE1,SIDE2,SIDE3,SIDE4,TOP1,TOP2,TOP3,TOP4);
    }
}
