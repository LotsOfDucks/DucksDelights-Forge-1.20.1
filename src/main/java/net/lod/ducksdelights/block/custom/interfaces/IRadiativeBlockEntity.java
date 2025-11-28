package net.lod.ducksdelights.block.custom.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public interface IRadiativeBlockEntity {

    static void getEntitiesInRange(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity, Integer range, Float damageScale, Float xOffsetPos, Float xOffsetNeg, Float yOffsetPos, Float yOffsetNeg, Float zOffsetPos, Float zOffsetNeg, DamageSource damageSource) {
        int boxX = pos.getX();
        int boxY = pos.getY();
        int boxZ = pos.getZ();
        AABB box = (new AABB(boxX, boxY, boxZ, boxX + 1, boxY + 1, boxZ + 1)).inflate(range + 4);
        List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, box, LivingEntity::attackable);
        if (!list.isEmpty()) {
            irradiateEntities(level, list, pos, range, damageScale, xOffsetPos, xOffsetNeg, yOffsetPos, yOffsetNeg, zOffsetPos, zOffsetNeg, damageSource);
        }
    }

    static void irradiateEntities(Level world, List<LivingEntity> list, BlockPos pos, Integer range, Float damageScale, Float xOffsetPos, Float xOffsetNeg, Float yOffsetPos, Float yOffsetNeg, Float zOffsetPos, Float zOffsetNeg, DamageSource damageSource) {
        for (LivingEntity livingEntity : list) {
            if (pos.getCenter().closerThan(livingEntity.position(), range) && livingEntity.isAlive()) {
                if (livingEntity instanceof Player && livingEntity.tickCount <= 200) {
                    return;
                } else if (hasLos(world, livingEntity, pos, range, damageScale, xOffsetPos, xOffsetNeg, yOffsetPos, yOffsetNeg, zOffsetPos, zOffsetNeg)) {
                    applyDamage(livingEntity, damageSource, world, pos, range, damageScale);
                }
            }
        }
    }

    static boolean hasLos(Level world, LivingEntity livingEntity, BlockPos blockPos, Integer range, Float damageScale, Float xOffsetPos, Float xOffsetNeg, Float yOffsetPos, Float yOffsetNeg, Float zOffsetPos, Float zOffsetNeg) {
        Vec3 entityPositionFeet = livingEntity.position();
        Vec3 entityPositionEyes = livingEntity.getEyePosition();
        double blockCenterPosX = blockPos.getCenter().x();
        double blockCenterPosY = blockPos.getCenter().y();
        double blockCenterPosZ = blockPos.getCenter().z();
        float damageScaleFinal = Math.max(damageScale, 0.01F);
        long entityDistance = (long) Math.max(Math.min(range, Math.max(Math.ceil(Math.abs(blockPos.getCenter().distanceTo(entityPositionFeet))), 1)), 1);
        long modifiedEntityDistance;
        modifiedEntityDistance = (long) (entityDistance / damageScaleFinal);
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            modifiedEntityDistance = modifiedEntityDistance * 2;
        }
        modifiedEntityDistance = Math.max(modifiedEntityDistance, 1);
        if (world.getGameTime() % modifiedEntityDistance == 0L) {
            if (entityPositionEyes.x() > blockCenterPosX) {
                Vec3 eyeVectorX = new Vec3(blockPos.getCenter().x() +xOffsetPos, blockPos.getCenter().y(), blockPos.getCenter().z());
                boolean eyesX = livingEntity.level().clip(new ClipContext(eyeVectorX, entityPositionEyes, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!eyesX) {
                    return true;
                }
            } else {
                Vec3 eyeVectorX = new Vec3(blockPos.getCenter().x() -xOffsetNeg, blockPos.getCenter().y(), blockPos.getCenter().z());
                boolean eyesX = livingEntity.level().clip(new ClipContext(eyeVectorX, entityPositionEyes, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!eyesX) {
                    return true;
                }
            }
            if (entityPositionEyes.y() > blockCenterPosY) {
                Vec3 eyeVectorY = new Vec3(blockPos.getCenter().x(), blockPos.getCenter().y() +yOffsetPos, blockPos.getCenter().z());
                boolean eyesY = livingEntity.level().clip(new ClipContext(eyeVectorY, entityPositionEyes, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!eyesY) {
                    return true;
                }
            } else {
                Vec3 eyeVectorY = new Vec3(blockPos.getCenter().x(), blockPos.getCenter().y() -yOffsetNeg, blockPos.getCenter().z());
                boolean eyesY = livingEntity.level().clip(new ClipContext(eyeVectorY, entityPositionEyes, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!eyesY) {
                    return true;
                }
            }
            if (entityPositionEyes.z() > blockCenterPosZ) {
                Vec3 eyeVectorZ = new Vec3(blockPos.getCenter().x(), blockPos.getCenter().y(), blockPos.getCenter().z() +zOffsetPos);
                boolean eyesZ = livingEntity.level().clip(new ClipContext(eyeVectorZ, entityPositionEyes, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!eyesZ) {
                    return true;
                }
            } else {
                Vec3 eyeVectorZ = new Vec3(blockPos.getCenter().x(), blockPos.getCenter().y(), blockPos.getCenter().z() -zOffsetNeg);
                boolean eyesZ = livingEntity.level().clip(new ClipContext(eyeVectorZ, entityPositionEyes, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!eyesZ) {
                    return true;
                }
            }
            if (entityPositionFeet.x() > blockCenterPosX) {
                Vec3 footVectorX = new Vec3(blockPos.getCenter().x() +xOffsetPos, blockPos.getCenter().y(), blockPos.getCenter().z());
                boolean footX = livingEntity.level().clip(new ClipContext(footVectorX, entityPositionFeet, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!footX) {
                    return true;
                }
            } else {
                Vec3 footVectorX = new Vec3(blockPos.getCenter().x() -xOffsetNeg, blockPos.getCenter().y(), blockPos.getCenter().z());
                boolean footX = livingEntity.level().clip(new ClipContext(footVectorX, entityPositionFeet, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!footX) {
                    return true;
                }
            }
            if (entityPositionFeet.y() > blockCenterPosY) {
                Vec3 footVectorY = new Vec3(blockPos.getCenter().x(), blockPos.getCenter().y() +yOffsetPos, blockPos.getCenter().z());
                boolean footY = livingEntity.level().clip(new ClipContext(footVectorY, entityPositionFeet, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!footY) {
                    return true;
                }
            } else {
                Vec3 footVectorY = new Vec3(blockPos.getCenter().x(), blockPos.getCenter().y() -yOffsetNeg, blockPos.getCenter().z());
                boolean footY = livingEntity.level().clip(new ClipContext(footVectorY, entityPositionFeet, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!footY) {
                    return true;
                }
            }
            if (entityPositionFeet.z() > blockCenterPosZ) {
                Vec3 footVectorZ = new Vec3(blockPos.getCenter().x(), blockPos.getCenter().y(), blockPos.getCenter().z() +zOffsetPos);
                boolean footZ = livingEntity.level().clip(new ClipContext(footVectorZ, entityPositionFeet, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!footZ) {
                    return true;
                }
            } else {
                Vec3 footVectorZ = new Vec3(blockPos.getCenter().x(), blockPos.getCenter().y(), blockPos.getCenter().z() -zOffsetNeg);
                boolean footZ = livingEntity.level().clip(new ClipContext(footVectorZ, entityPositionFeet, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!footZ) {
                    return true;
                }
            }
        }
        return false;
    }

    static void applyDamage(LivingEntity livingEntity, DamageSource damageSource, Level world, BlockPos blockPos, int range, float damageScale) {
        double entityDistance = Math.max(Math.min(range, Math.max(Math.ceil(Math.abs(blockPos.getCenter().distanceTo(livingEntity.position()))), 1)), 1);
        float damageScaleFinal = Math.max(damageScale, 0.01F);
        double modifiedEntityDistance;
        modifiedEntityDistance = entityDistance / damageScaleFinal;
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            modifiedEntityDistance = modifiedEntityDistance * 2;
        }
        modifiedEntityDistance = Math.max(modifiedEntityDistance, 0.1);
        if (modifiedEntityDistance <= 1 && damageScaleFinal > 1.0F) {
            livingEntity.hurt(damageSource,  (float) (entityDistance / modifiedEntityDistance));
        } else {
            livingEntity.hurt(damageSource, 1);
        }
    }
}
