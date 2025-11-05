package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.block.entity.BlazingBarrelBlockEntity;
import net.lod.ducksdelights.block.entity.ModBlockEntities;
import net.lod.ducksdelights.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Optional;

public class BlazingBarrelBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
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
    private final boolean spawnParticles;
    private final int fireDamage;

    private static final VoxelShape[] SHAPE_BY_FULLNESS;

    public BlazingBarrelBlock(ItemLike fillItem ,boolean pSpawnParticles, int pFireDamage, BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.FILLITEM = fillItem;
        this.spawnParticles = pSpawnParticles;
        this.fireDamage = pFireDamage;
        this.registerDefaultState(this.stateDefinition.any().setValue(FULLNESS, 13).setValue(WATERLOGGED, false).setValue(EXPLODING, false));
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE_BY_FULLNESS[this.getFullness(pState)];
    }

    @org.jetbrains.annotations.Nullable
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
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        int fullness = this.getFullness(pState);
        if (!itemstack.is(this.FILLITEM.asItem()) && !itemstack.isEmpty()) {
            if (blockentity instanceof BlazingBarrelBlockEntity blazingBarrelBlockEntity) {
                Optional<CampfireCookingRecipe> optional = blazingBarrelBlockEntity.getCookableRecipe(itemstack);
                if (optional.isPresent()) {
                    if (!pLevel.isClientSide && blazingBarrelBlockEntity.placeFood(pPlayer, pPlayer.getAbilities().instabuild ? itemstack.copy() : itemstack, ((CampfireCookingRecipe)optional.get()).getCookingTime())) {
                        return InteractionResult.SUCCESS;
                    }

                    return InteractionResult.CONSUME;
                }
            }
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
            if (fullness > 1 && pHand.equals(InteractionHand.MAIN_HAND)) {
                pLevel.setBlock(pPos, pState.setValue(FULLNESS, fullness - 1), 3);
                pLevel.playSound(null, pPos, ModSoundEvents.BARREL_FILL.get(), SoundSource.BLOCKS, 5, 1);
                if (!pPlayer.isCreative()) {
                    pPlayer.addItem(new ItemStack(this.FILLITEM));
                }
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            }
            if (fullness == 1 && pHand.equals(InteractionHand.MAIN_HAND)) {
                pLevel.setBlock(pPos, ModBlocks.EMPTY_BARREL.get().defaultBlockState().setValue(WATERLOGGED, pState.getValue(WATERLOGGED)), 3);
                pLevel.playSound(null, pPos, ModSoundEvents.BARREL_FILL.get(), SoundSource.BLOCKS, 5, 1);
                if (!pPlayer.isCreative()) {
                    pPlayer.addItem(new ItemStack(this.FILLITEM));
                }
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            }
            return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }
    }

    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (pEntity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)pEntity)) {
            pEntity.hurt(pLevel.damageSources().inFire(), (float)this.fireDamage);
        }

        super.entityInside(pState, pLevel, pPos, pEntity);
    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof BlazingBarrelBlockEntity) {
                Containers.dropContents(pLevel, pPos, ((BlazingBarrelBlockEntity)blockentity).getItems());
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }

    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {


        if (pRandom.nextInt(10) == 0) {
            pLevel.playLocalSound((double)pPos.getX() + 0.5, (double)pPos.getY() + 0.5, (double)pPos.getZ() + 0.5, ModSoundEvents.BLAZING_BARREL_CRACKLE.get(), SoundSource.BLOCKS, 0.5F + pRandom.nextFloat(), pRandom.nextFloat() * 0.7F + 0.6F, false);
        }

        if (this.spawnParticles && !pState.getValue(WATERLOGGED)) {
            double yShift = switch (pState.getValue(BlazingBarrelBlock.FULLNESS)) {
                case 2 -> 0.18825F;
                case 3 -> 0.25075F;
                case 4 -> 0.31325F;
                case 5 -> 0.37575F;
                case 6 -> 0.43825F;
                case 7 -> 0.50075F;
                case 8 -> 0.56325F;
                case 9 -> 0.62575F;
                case 10 -> 0.68825F;
                case 11 -> 0.75075F;
                case 12 -> 0.81325F;
                case 13 -> 0.87575F;
                case 14 -> 0.93825F;
                case 15 -> 1.00075F;
                default -> 0.12575F;
            };
            double x = pPos.getCenter().x() + pRandom.nextDouble() * 0.6 - 0.3;
            double z = pPos.getCenter().z() + pRandom.nextDouble() * 0.6 - 0.3;

            pLevel.addParticle(ParticleTypes.SMOKE, x, pPos.getY() + yShift, z, 0.0, 0.1, 0.0);
            pLevel.addParticle(ParticleTypes.FLAME, x, pPos.getY() + yShift, z, 0.0, 0.1, 0.0);
            if (pRandom.nextInt(5) == 0) {
                for (int i = 0; i < pRandom.nextInt(1) + 1; ++i) {
                    pLevel.addParticle(ParticleTypes.LAVA, x, pPos.getY() + yShift, z, pRandom.nextFloat() / 2.0F, 5.0E-5, pRandom.nextFloat() / 2.0F);
                }
            }
        }
    }

    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlazingBarrelBlockEntity(pPos, pState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide) {
            return createTickerHelper(pBlockEntityType, ModBlockEntities.BLAZING_BARREL_DETECTOR_BE.get(), BlazingBarrelBlockEntity::particleTick);
        } else {
            return createTickerHelper(pBlockEntityType, ModBlockEntities.BLAZING_BARREL_DETECTOR_BE.get(), BlazingBarrelBlockEntity::cookTick);
        }
    }

    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }

    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
        return this.getFullness(pState);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FULLNESS, WATERLOGGED, EXPLODING);
    }

    static {
        FULLNESS = FillableBarrelBlock.FULLNESS;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        EXPLODING = FillableBarrelBlock.EXPLODING;
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
