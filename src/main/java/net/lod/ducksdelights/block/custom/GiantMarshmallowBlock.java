package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GiantMarshmallowBlock extends Block {
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);

    public GiantMarshmallowBlock(Properties pProperties) {
        super(pProperties);
    }

    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return true;
    }

    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {
        if (pEntity.isSuppressingBounce()) {
            super.fallOn(pLevel, pState, pPos, pEntity, pFallDistance);
        } else {
            pEntity.causeFallDamage(pFallDistance, 0.0F, pLevel.damageSources().fall());
        }

    }

    public void updateEntityAfterFallOn(BlockGetter pLevel, Entity pEntity) {
        if (pEntity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(pLevel, pEntity);
        } else {
            this.launch(pEntity);
            if (pEntity instanceof LivingEntity) {
                pEntity.level().playSound(null, pEntity, ModSoundEvents.GIANT_MARSHMALLOW_BOOWOOP.get(), SoundSource.BLOCKS, 1, 1);
            }
        }
    }

    private void launch(Entity pEntity) {
        Vec3 entityMovement = pEntity.getDeltaMovement();
        if (pEntity.getFeetBlockState().getBlock() instanceof GiantMarshmallowBlock) {
            if (pEntity.level().hasNeighborSignal(pEntity.blockPosition())) {
                float newSpeed = pEntity.level().getBestNeighborSignal(pEntity.blockPosition());
                if (pEntity instanceof AbstractMinecart) {
                    pEntity.setDeltaMovement(entityMovement.x, (Math.log1p(newSpeed) + 1), entityMovement.z);
                } else {
                    pEntity.setDeltaMovement(entityMovement.x, (Math.log1p(newSpeed) + 1) / 2, entityMovement.z);
                }
            } else {
                if (pEntity instanceof AbstractMinecart) {
                    pEntity.setDeltaMovement(entityMovement.x, 1.6F, entityMovement.z);
                } else {
                    pEntity.setDeltaMovement(entityMovement.x, 0.8F, entityMovement.z);
                }
            }
        } else {
            if (pEntity instanceof AbstractMinecart) {
                pEntity.setDeltaMovement(entityMovement.x, 1.6F, entityMovement.z);
            } else {
                pEntity.setDeltaMovement(entityMovement.x, 0.8F, entityMovement.z);
            }
        }
    }
}
