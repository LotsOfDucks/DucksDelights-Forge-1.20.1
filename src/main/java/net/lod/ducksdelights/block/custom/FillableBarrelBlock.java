package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FillableBarrelBlock extends Block implements SimpleWaterloggedBlock {
    private static final VoxelShape BASE;
    private static final VoxelShape FULL1;
    private static final VoxelShape FULL2;
    private static final VoxelShape FULL3;
    private static final VoxelShape FULL4;
    private static final VoxelShape FULL5;
    private static final VoxelShape FULL6;
    private static final VoxelShape FULL7;
    private static final VoxelShape FULL8;
    private static final VoxelShape FULL9;
    private static final VoxelShape FULL10;
    private static final VoxelShape FULL11;
    private static final VoxelShape FULL12;
    private static final VoxelShape FULL13;
    private static final VoxelShape FULL14;
    private static final VoxelShape FULL15;

    public static final IntegerProperty FULLNESS;
    public final ItemLike FILLITEM;
    public static final BooleanProperty WATERLOGGED;
    public static BooleanProperty EXPLODING;

    private static final VoxelShape[] SHAPE_BY_FULLNESS;

    public FillableBarrelBlock(ItemLike fillItem, Properties pProperties) {
        super(pProperties);
        this.FILLITEM = fillItem;
        this.registerDefaultState(this.stateDefinition.any().setValue(FULLNESS, 13).setValue(WATERLOGGED, false).setValue(EXPLODING, false));
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE_BY_FULLNESS[this.getFullness(pState)];
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        FluidState fluidState = pContext.getLevel().getFluidState(pContext.getClickedPos());
        return this.defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    protected IntegerProperty getFullnessProperty() {
        return FULLNESS;
    }

    public int getFullness(BlockState pState) {
        return pState.getValue(this.getFullnessProperty());
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        int fullness = this.getFullness(pState);
        if (!itemstack.is(this.FILLITEM.asItem()) && !itemstack.isEmpty()) {
            return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        } else if (itemstack.is(this.FILLITEM.asItem())) {
            if (fullness < 15) {
                pLevel.setBlock(pPos, pState.setValue(FULLNESS, fullness + 1), 3);
                pLevel.playSound(null, pPos, ModSoundEvents.BARREL_FILL.get(), SoundSource.BLOCKS, 5, 1);
                if (!pPlayer.isCreative()) {
                    itemstack.shrink(1);
                }
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            }
            return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        } else {
            if (fullness > 0 && pHand.equals(InteractionHand.MAIN_HAND)) {
                pLevel.setBlock(pPos, pState.setValue(FULLNESS, fullness - 1), 3);
                pLevel.playSound(null, pPos, ModSoundEvents.BARREL_FILL.get(), SoundSource.BLOCKS, 5, 1);
                if (!pPlayer.isCreative()) {
                    pPlayer.addItem(new ItemStack(this.FILLITEM));
                }
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            }
            if (fullness == 0 && pHand.equals(InteractionHand.MAIN_HAND)) {
                pLevel.setBlock(pPos, ModBlocks.EMPTY_BARREL.get().defaultBlockState(), 3);
                pLevel.playSound(null, pPos, ModSoundEvents.BARREL_FILL.get(), SoundSource.BLOCKS, 5, 1);
                if (!pPlayer.isCreative()) {
                    pPlayer.addItem(new ItemStack(this.FILLITEM));
                }
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            }
            return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }
    }

    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
        return this.getFullness(pState);
    }

    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FULLNESS, WATERLOGGED, EXPLODING);
    }

    static {
        FULLNESS = IntegerProperty.create("fullness", 0, 15);
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        EXPLODING = BooleanProperty.create("exploding");
        VoxelShape wall_north = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
        VoxelShape wall_south = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
        VoxelShape wall_east = Block.box(15.0, 0.0, 1.0, 16.0, 16.0, 15.0);
        VoxelShape wall_west = Block.box(0.0, 0.0, 1.0, 1.0, 16.0, 15.0);
        VoxelShape full0 = Block.box(1.0, 0.0, 1.0, 15.0, 0.5, 15.0);
        VoxelShape full1 = Block.box(1.0, 0.0, 1.0, 15.0, 1.5, 15.0);
        VoxelShape full2 = Block.box(1.0, 0.0, 1.0, 15.0, 2.5, 15.0);
        VoxelShape full3 = Block.box(1.0, 0.0, 1.0, 15.0, 3.5, 15.0);
        VoxelShape full4 = Block.box(1.0, 0.0, 1.0, 15.0, 4.5, 15.0);
        VoxelShape full5 = Block.box(1.0, 0.0, 1.0, 15.0, 5.5, 15.0);
        VoxelShape full6 = Block.box(1.0, 0.0, 1.0, 15.0, 6.5, 15.0);
        VoxelShape full7 = Block.box(1.0, 0.0, 1.0, 15.0, 7.5, 15.0);
        VoxelShape full8 = Block.box(1.0, 0.0, 1.0, 15.0, 8.5, 15.0);
        VoxelShape full9 = Block.box(1.0, 0.0, 1.0, 15.0, 9.5, 15.0);
        VoxelShape full10 = Block.box(1.0, 0.0, 1.0, 15.0, 10.5, 15.0);
        VoxelShape full11 = Block.box(1.0, 0.0, 1.0, 15.0, 11.5, 15.0);
        VoxelShape full12 = Block.box(1.0, 0.0, 1.0, 15.0, 12.5, 15.0);
        VoxelShape full13 = Block.box(1.0, 0.0, 1.0, 15.0, 13.5, 15.0);
        VoxelShape full14 = Block.box(1.0, 0.0, 1.0, 15.0, 14.5, 15.0);
        VoxelShape full15 = Block.box(1.0, 0.0, 1.0, 15.0, 15.5, 15.0);
        BASE = Shapes.or(wall_north,wall_south, wall_east, wall_west, full0);
        FULL1 = Shapes.or(wall_north,wall_south, wall_east, wall_west, full1);
        FULL2 = Shapes.or(wall_north,wall_south, wall_east, wall_west, full2);
        FULL3 = Shapes.or(wall_north,wall_south, wall_east, wall_west, full3);
        FULL4 = Shapes.or(wall_north,wall_south, wall_east, wall_west, full4);
        FULL5 = Shapes.or(wall_north,wall_south, wall_east, wall_west, full5);
        FULL6 = Shapes.or(wall_north,wall_south, wall_east, wall_west, full6);
        FULL7 = Shapes.or(wall_north,wall_south, wall_east, wall_west, full7);
        FULL8 = Shapes.or(wall_north,wall_south, wall_east, wall_west, full8);
        FULL9 = Shapes.or(wall_north,wall_south, wall_east, wall_west, full9);
        FULL10 = Shapes.or(wall_north,wall_south, wall_east, wall_west, full10);
        FULL11 = Shapes.or(wall_north,wall_south, wall_east, wall_west, full11);
        FULL12 = Shapes.or(wall_north,wall_south, wall_east, wall_west, full12);
        FULL13 = Shapes.or(wall_north,wall_south, wall_east, wall_west, full13);
        FULL14 = Shapes.or(wall_north,wall_south, wall_east, wall_west, full14);
        FULL15 = Shapes.or(wall_north,wall_south, wall_east, wall_west, full15);
        SHAPE_BY_FULLNESS = new VoxelShape[]{
                BASE,
                FULL1,
                FULL2,
                FULL3,
                FULL4,
                FULL5,
                FULL6,
                FULL7,
                FULL8,
                FULL9,
                FULL10,
                FULL11,
                FULL12,
                FULL13,
                FULL14,
                FULL15,};
    }
}
