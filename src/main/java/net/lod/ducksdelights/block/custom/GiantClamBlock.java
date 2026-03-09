package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.custom.blockstate_properties.ModBlockStateProperties;
import net.lod.ducksdelights.block.custom.interfaces.ISimpleWaterAndLavaloggedBlock;
import net.lod.ducksdelights.block.entity.GiantClamBlockEntity;
import net.lod.ducksdelights.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
import org.jetbrains.annotations.Nullable;

public class GiantClamBlock extends BaseEntityBlock implements ISimpleWaterAndLavaloggedBlock {
    public static final BooleanProperty OPEN;
    public static final BooleanProperty PEARLING;
    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty LAVALOGGED;
    public static final BooleanProperty LOGGED;
    public static final EnumProperty<Direction> FACING;



    public GiantClamBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, false).setValue(PEARLING, false).setValue(WATERLOGGED, false).setValue(LAVALOGGED, false).setValue(LOGGED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GiantClamBlockEntity(blockPos, blockState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.GIANT_CLAM_BE.get(), pLevel.isClientSide ? GiantClamBlockEntity::clientTick : GiantClamBlockEntity::serverTick);
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public boolean isRandomlyTicking(BlockState pState) {
        return !this.isOpen(pState) && this.isLogged(pState);
    }

    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.random.nextInt(20) == 0) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof GiantClamBlockEntity) {
                GiantClamBlockEntity giantClamBlockEntity = (GiantClamBlockEntity) blockEntity;
                BlockState blockState = pLevel.getBlockState(pPos.below());
                if (blockState.is(BlockTags.SAND)) {
                    if (giantClamBlockEntity.getItem(0).isEmpty() && giantClamBlockEntity.getItem(1).isEmpty()) {
                        ItemStack sandItem = new ItemStack(blockState.getBlock());
                        if (giantClamBlockEntity.getPearlableRecipe(sandItem).isPresent()) {
                            SimpleContainer container = new SimpleContainer(sandItem);
                            ItemStack resultItem = giantClamBlockEntity.getResultItem(pLevel, container, sandItem);
                            if (resultItem.isItemEnabled(pLevel.enabledFeatures())) {
                                if (sandItem.hasTag()) {
                                    CompoundTag inputTag = sandItem.getTag();
                                    resultItem.setTag(inputTag);
                                }
                                giantClamBlockEntity.setItem(1, resultItem);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isSignalSource(BlockState pState) {
        return true;
    }

    public boolean isOpen(BlockState state) {
        return state.getValue(ModBlockStateProperties.OPEN);
    }

    public boolean isLogged(BlockState state) {
        return state.getValue(ModBlockStateProperties.LOGGED);
    }

    public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        return ((pBlockState.getValue(PEARLING) || pBlockState.getValue(OPEN)) && Direction.UP == pSide)  ? 15 : 0;
    }

    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {

    }

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isClientSide()){
            pLevel.setBlock(pPos, pState.cycle(ModBlockStateProperties.OPEN), 3);
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof GiantClamBlockEntity) {
                ((GiantClamBlockEntity) blockEntity).drops();
            }
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        FluidState fluidState = pContext.getLevel().getFluidState(pContext.getClickedPos());
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection()).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER).setValue(LAVALOGGED, fluidState.getType() == Fluids.LAVA).setValue(LOGGED, (fluidState.getType() == (Fluids.LAVA) || fluidState.getType() == (Fluids.WATER))).setValue(OPEN, true);
    }

    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : pState.getValue(LAVALOGGED) ? Fluids.LAVA.getSource(false) : super.getFluidState(pState);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(OPEN, PEARLING, WATERLOGGED, LAVALOGGED, LOGGED, FACING);
    }

    static {
        OPEN = ModBlockStateProperties.OPEN;
        PEARLING = ModBlockStateProperties.PEARLING;
        FACING = BlockStateProperties.HORIZONTAL_FACING;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        LAVALOGGED = ModBlockStateProperties.LAVALOGGED;
        LOGGED = ModBlockStateProperties.LOGGED;
    }
}
