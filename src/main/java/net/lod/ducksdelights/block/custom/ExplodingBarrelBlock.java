package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.damage.ModDamageTypes;
import net.lod.ducksdelights.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class ExplodingBarrelBlock extends FillableBarrelBlock{
    private LivingEntity igniter = null;

    public ExplodingBarrelBlock(ItemLike fillItem, Properties pProperties) {
        super(fillItem, pProperties);
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        int fullness = this.getFullness(pState);
        if (!itemstack.is(this.FILLITEM.asItem()) && !itemstack.isEmpty() && !itemstack.is(Items.FLINT_AND_STEEL) && !itemstack.is(Items.FIRE_CHARGE)) {
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
            return InteractionResult.FAIL;
        } else if (itemstack.is(Items.FLINT_AND_STEEL) || itemstack.is(Items.FIRE_CHARGE)) {
            if (!pState.getValue(WATERLOGGED)) {
                startExplode(pLevel, pState, pPos);
                this.igniter = pPlayer;
                Item item = itemstack.getItem();
                if (!pPlayer.isCreative()) {
                    if (itemstack.is(Items.FLINT_AND_STEEL)) {
                        itemstack.hurtAndBreak(1, pPlayer, (p_57425_) -> {
                            p_57425_.broadcastBreakEvent(pHand);
                        });
                    } else {
                        itemstack.shrink(1);
                    }
                }

                pPlayer.awardStat(Stats.ITEM_USED.get(item));
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            if (fullness > 0 && pHand.equals(InteractionHand.MAIN_HAND)) {
                pLevel.setBlock(pPos, pState.setValue(FULLNESS, fullness - 1), 3);
                pLevel.playSound(null, pPos, ModSoundEvents.BARREL_FILL.get(), SoundSource.BLOCKS, 5, 1);
                if (!pPlayer.isCreative()) {
                    pPlayer.addItem(new ItemStack(this.FILLITEM));
                }
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            }
            return InteractionResult.FAIL;
        }
    }

    public void onCaughtFire(BlockState state, Level world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (!state.getValue(WATERLOGGED)) {
            startExplode(world, state, pos);
        }
    }

    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (pLevel.hasNeighborSignal(pPos)) {
            startExplode(pLevel, pState, pPos);
        }
    }

    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (!pOldState.is(pState.getBlock()) && pLevel.hasNeighborSignal(pPos)) {
            startExplode(pLevel, pState, pPos);
        }
    }

    public void onProjectileHit(Level pLevel, BlockState pState, BlockHitResult pHit, Projectile pProjectile) {
        if (!pLevel.isClientSide) {
            BlockPos blockpos = pHit.getBlockPos();
            if (pProjectile.isOnFire() && pProjectile.mayInteract(pLevel, blockpos)) {
                this.igniter = (LivingEntity) pProjectile.getOwner();
                startExplode(pLevel, pState, blockpos);
            }
        }
    }

    public boolean dropFromExplosion(Explosion pExplosion) {
        return false;
    }

    public float getExplosionRadiusValues (Level level, BlockPos blockPos) {
         return switch (level.getBlockState(blockPos).getValue(FULLNESS)) {
            case 0 -> 1;
            case 1 -> 2;
            case 2 -> 3;
            case 3 -> 4;
            case 4 -> 4.5F;
            case 5 -> 5;
            case 6 -> 5.5F;
            case 7 -> 6;
            case 8 -> 6.5F;
            case 9 -> 7;
            case 10 -> 7.5F;
            case 11 -> 8;
            case 12 -> 8.5F;
            case 13 -> 9;
            case 14 -> 9.5F;
            case 15 -> 10;
            default ->
                    throw new IllegalStateException("Unexpected value: " + level.getBlockState(blockPos).getValue(FULLNESS));
        };
    }

    private float getExplosionRadius(Level level, BlockPos blockPos) {
        float radius = this.getExplosionRadiusValues(level, blockPos);
        for (int x = -1; x <= 1; ++x) {
            for (int y = -1; y <= 1; ++y) {
                for (int z = -1; z <= 1; ++z){
                    BlockState checkNearbyBarrel = level.getBlockState(blockPos.offset(x, y, z));
                    if (checkNearbyBarrel.is(ModBlocks.GUNPOWDER_BARREL.get())) {
                        if (!checkNearbyBarrel.getValue(WATERLOGGED)) {
                            float adjacent = getExplosionRadiusValues(level, blockPos.offset(x, y, z));
                            radius += (adjacent / 4);
                        }
                    }
                }
            }
        }

        return radius;
    }

    private void startExplode(Level level, BlockState state, BlockPos pos) {
        for (int x = -1; x <= 1; ++x) {
            for (int y = -1; y <= 1; ++y) {
                for (int z = -1; z <= 1; ++z) {
                    BlockPos bombPos = pos.offset(x, y, z);
                    BlockState isBomb = level.getBlockState(bombPos);
                    if (isBomb.is(ModBlocks.GUNPOWDER_BARREL.get())) {
                        level.setBlock(bombPos, isBomb.setValue(EXPLODING, true), 3);
                    }
                }
            }
        }
        level.scheduleTick(pos, this, 20);
    }

    private void setExplode(Level level, BlockState state, BlockPos pos) {
        int range = level.getBlockState(pos).getValue(FULLNESS) + 2;
        for (int x = -range; x <= range; ++x) {
            for (int y = -range; y <= range; ++y) {
                for (int z = -range; z <= range; ++z){
                    BlockPos bombPos = pos.offset(x, y, z);
                    BlockState isBomb = level.getBlockState(bombPos);
                    if (isBomb.is(ModBlocks.GUNPOWDER_BARREL.get()) || isBomb.is(Blocks.TNT)) {
                        if (isBomb.is(ModBlocks.GUNPOWDER_BARREL.get())) {
                            level.setBlock(bombPos, isBomb.setValue(EXPLODING, true), 3);
                            level.scheduleTick(bombPos, isBomb.getBlock(), 20);
                        }
                    }
                }
            }
        }
    }

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pState.getValue(WATERLOGGED)) {
            if (pState.getValue(EXPLODING)) {
                setExplode(pLevel, pState, pPos);
                explode(pLevel, pPos, this.igniter);
                pLevel.removeBlock(pPos, false);
            } else {
                setExplode(pLevel, pState, pPos);
            }
        }
    }

    private void explode(Level pLevel, BlockPos pPos, LivingEntity entity) {
        if (!pLevel.isClientSide) {
            float radius = this.getExplosionRadius(pLevel, pPos);
            DamageSource damageSource = new DamageSource(
                    pLevel.registryAccess()
                            .registryOrThrow(Registries.DAMAGE_TYPE)
                            .getHolder(ModDamageTypes.FISSION).get()
            );
            pLevel.explode(entity, damageSource , new ExplosionDamageCalculator() ,pPos.getCenter().x(), pPos.getCenter().y() + 0.5, pPos.getCenter().z(), radius, true, Level.ExplosionInteraction.TNT);
        }
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pState.getValue(EXPLODING)) {
            return Block.box(7.99, 0.0, 7.99, 8.01, 0.01, 8.01);
        }
        return super.getShape(pState, pLevel, pPos, pContext);
    }
}
