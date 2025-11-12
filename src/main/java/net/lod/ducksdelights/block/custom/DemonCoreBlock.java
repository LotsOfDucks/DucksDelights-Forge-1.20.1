package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlockStateProperties;
import net.lod.ducksdelights.block.custom.interfaces.SimpleWaterAndLavaloggedBlock;
import net.lod.ducksdelights.block.entity.DemonCoreBlockEntity;
import net.lod.ducksdelights.block.entity.ModBlockEntities;
import net.lod.ducksdelights.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DemonCoreBlock extends BaseEntityBlock implements SimpleWaterAndLavaloggedBlock {
    public static final BooleanProperty POWERED;
    public static final BooleanProperty FORCE_POWERED;
    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty LAVALOGGED;
    public static final BooleanProperty LOGGED;
    public static final BooleanProperty PLAYER_PLACED;
    protected static final VoxelShape UP_SHAPE;
    protected static final VoxelShape LOW_SHAPE;
    protected static final VoxelShape SHAPE;
    public static final EnumProperty<Direction> FACING;

    public DemonCoreBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false).setValue(FORCE_POWERED, false).setValue(WATERLOGGED, false).setValue(LAVALOGGED, false).setValue(LOGGED, false).setValue(PLAYER_PLACED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DemonCoreBlockEntity(blockPos, blockState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.DEMON_CORE_BE.get(), pLevel.isClientSide ? DemonCoreBlockEntity::clientTick : DemonCoreBlockEntity::serverTick);
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        FluidState fluidState = pContext.getLevel().getFluidState(pContext.getClickedPos());
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection()).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER).setValue(LAVALOGGED, fluidState.getType() == Fluids.LAVA).setValue(LOGGED, (fluidState.getType() == (Fluids.LAVA) || fluidState.getType() == (Fluids.WATER))).setValue(POWERED, pContext.getLevel().hasNeighborSignal(pContext.getClickedPos())).setValue(PLAYER_PLACED, true);
    }

    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : pState.getValue(LAVALOGGED) ? Fluids.LAVA.getSource(false) : super.getFluidState(pState);
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pState.getValue(PLAYER_PLACED)) {
            return InteractionResult.PASS;
        } else if (pLevel.hasNeighborSignal(pPos)){
            return InteractionResult.PASS;
        } else {
            BlockState blockState = pState.cycle(POWERED).cycle(FORCE_POWERED);
            pLevel.setBlock(pPos, blockState, 3);
            pLevel.playSound(null, pPos, ModSoundEvents.DEMON_CORE_TINK.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }
    }

    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        boolean isPowered = pLevel.hasNeighborSignal(pPos);
        if (pState.getValue(PLAYER_PLACED) && !pState.getValue(FORCE_POWERED) && isPowered != pState.getValue(POWERED)) {
            pLevel.playSound(null, pPos, ModSoundEvents.DEMON_CORE_TINK.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!pState.getValue(POWERED)) {
                if (!pState.getValue(LOGGED)) {
                    pLevel.playSound(null, pPos, ModSoundEvents.DEMON_CORE_AMBIENT.get(), SoundSource.BLOCKS, 4.0F, 1.0F);
                } else {
                    pLevel.playSound(null, pPos, ModSoundEvents.DEMON_CORE_AMBIENT.get(), SoundSource.BLOCKS, 1.0F, 0.5F);
                }
            }
            pLevel.setBlockAndUpdate(pPos, pState.setValue(POWERED, isPowered));
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWERED, FORCE_POWERED, WATERLOGGED, LAVALOGGED, LOGGED, FACING, PLAYER_PLACED);
    }

    static {
        POWERED = BlockStateProperties.POWERED;
        FORCE_POWERED = BooleanProperty.create("force_powered");
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        LAVALOGGED = ModBlockStateProperties.LAVALOGGED;
        LOGGED = ModBlockStateProperties.LOGGED;
        PLAYER_PLACED = BooleanProperty.create("player_placed");
        LOW_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
        UP_SHAPE = Block.box(1.0, 8.0, 1.0, 15.0, 14.0, 15.0);
        SHAPE = Shapes.or(UP_SHAPE, LOW_SHAPE);
        FACING = BlockStateProperties.HORIZONTAL_FACING;
    }
}
