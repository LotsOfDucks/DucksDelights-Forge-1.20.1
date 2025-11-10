package net.lod.ducksdelights.block.entity;

import net.lod.ducksdelights.block.custom.DemonCoreBlock;
import net.lod.ducksdelights.damage.ModDamageTypes;
import net.lod.ducksdelights.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

public class DemonCoreBlockEntity extends BlockEntity {
    private boolean powered;
    private boolean waterlogged;
    private boolean lavalogged;
    private boolean logged;
    public int ticks;
    public int range = 20;

    public DemonCoreBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.DEMON_CORE_BE.get(), pPos, pBlockState);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    public static void clientTick(Level pLevel, BlockPos pPos, BlockState pState, DemonCoreBlockEntity pBlockEntity) {
        ++pBlockEntity.ticks;
        pBlockEntity.powered = pLevel.getBlockState(pPos).getValue(DemonCoreBlock.POWERED);
        pBlockEntity.waterlogged = pLevel.getBlockState(pPos).getValue(DemonCoreBlock.WATERLOGGED);
        if (pBlockEntity.waterlogged && pBlockEntity.powered) {
            double x = pPos.getCenter().x();
            double y = pPos.getCenter().y();
            double z = pPos.getCenter().z();
            pLevel.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, x + (0.5 *Math.random()), y + 0.02, z + (0.5 *Math.random()), Math.random(), 0.02, Math.random());
            pLevel.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, x + (-0.5 *Math.random()), y + 0.02, z + (-0.5 *Math.random()), (-1 *Math.random()), 0.02, (-1 * Math.random()));
            pLevel.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, x + (-0.5 *Math.random()), y + 0.02, z + (0.5 *Math.random()), (-1 *Math.random()), 0.02, Math.random());
            pLevel.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, x + (0.5 *Math.random()), y + 0.02, z + (-0.5 *Math.random()), Math.random(), 0.02, (-1 * Math.random()));
        }

    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, DemonCoreBlockEntity pBlockEntity) {
        ++pBlockEntity.ticks;
        pBlockEntity.powered = pLevel.getBlockState(pPos).getValue(DemonCoreBlock.POWERED);
        pBlockEntity.logged = pLevel.getBlockState(pPos).getValue(DemonCoreBlock.LOGGED);
        if (pBlockEntity.powered) {
            int range = pBlockEntity.range;
            int boxX = pPos.getX();
            int boxY = pPos.getY();
            int boxZ = pPos.getZ();
            AABB box = (new AABB(boxX, boxY, boxZ, boxX + 1, boxY + 1, boxZ + 1)).inflate(range + 4);
            List<LivingEntity> list = pLevel.getEntitiesOfClass(LivingEntity.class, box, LivingEntity::attackable);
            if (!list.isEmpty()) {
                irradiateEntities(pLevel, list, pPos, range);
            }
            if (pLevel.getGameTime() % 40L == 0L) {
                if (!pBlockEntity.logged) {
                    pLevel.playSound(null, pPos, ModSoundEvents.DEMON_CORE_AMBIENT.get(), SoundSource.BLOCKS, 4.0F, 1.0F);
                } else {
                    pLevel.playSound(null, pPos, ModSoundEvents.DEMON_CORE_AMBIENT.get(), SoundSource.BLOCKS, 1.0F, 0.5F);
                }
            }
        }
    }

    private static void irradiateEntities(Level world, List<LivingEntity> list, BlockPos pos, Integer val) {
        for (LivingEntity livingEntity : list) {
            if (pos.getCenter().closerThan(livingEntity.position(), val) && livingEntity.isAlive()) {
                if (livingEntity instanceof Player && livingEntity.tickCount <= 200) {
                    return;
                } else if (hasLos(world, livingEntity, pos)) {
                    applyDamage(world, pos, livingEntity);
                }
            }
        }
    }

    private static boolean hasLos(Level world, LivingEntity livingEntity, BlockPos blockPos) {
        Vec3 entityPositionFeet = livingEntity.position();
        Vec3 entityPositionEyes = livingEntity.getEyePosition();
        double blockCenterPosX = blockPos.getCenter().x();
        double blockCenterPosY = blockPos.getCenter().y();
        double blockCenterPosZ = blockPos.getCenter().z();
        long entityDistance = (long) Math.min(20, Math.max(Math.ceil(Math.abs(blockPos.getCenter().distanceTo(entityPositionFeet))), 1));
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            entityDistance = entityDistance * 2;
        }
        if (world.getGameTime() % entityDistance == 0L) {
            if (entityPositionEyes.x() > blockCenterPosX) {
                Vec3 eyeVectorX = new Vec3(blockPos.getCenter().x() +0.51, blockPos.getCenter().y(), blockPos.getCenter().z());
                boolean eyesX = livingEntity.level().clip(new ClipContext(eyeVectorX, entityPositionEyes, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!eyesX) {
                    return true;
                }
            } else {
                Vec3 eyeVectorX = new Vec3(blockPos.getCenter().x() -0.51, blockPos.getCenter().y(), blockPos.getCenter().z());
                boolean eyesX = livingEntity.level().clip(new ClipContext(eyeVectorX, entityPositionEyes, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!eyesX) {
                    return true;
                }
            }
            if (entityPositionEyes.y() > blockCenterPosY) {
                Vec3 eyeVectorY = new Vec3(blockPos.getCenter().x(), blockPos.getCenter().y() +0.49, blockPos.getCenter().z());
                boolean eyesY = livingEntity.level().clip(new ClipContext(eyeVectorY, entityPositionEyes, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!eyesY) {
                    return true;
                }
            } else {
                Vec3 eyeVectorY = new Vec3(blockPos.getCenter().x(), blockPos.getCenter().y() -0.51, blockPos.getCenter().z());
                boolean eyesY = livingEntity.level().clip(new ClipContext(eyeVectorY, entityPositionEyes, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!eyesY) {
                    return true;
                }
            }
            if (entityPositionEyes.z() > blockCenterPosZ) {
                Vec3 eyeVectorZ = new Vec3(blockPos.getCenter().x(), blockPos.getCenter().y(), blockPos.getCenter().z() +0.51);
                boolean eyesZ = livingEntity.level().clip(new ClipContext(eyeVectorZ, entityPositionEyes, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!eyesZ) {
                    return true;
                }
            } else {
                Vec3 eyeVectorZ = new Vec3(blockPos.getCenter().x(), blockPos.getCenter().y(), blockPos.getCenter().z() -0.51);
                boolean eyesZ = livingEntity.level().clip(new ClipContext(eyeVectorZ, entityPositionEyes, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!eyesZ) {
                    return true;
                }
            }
            if (entityPositionFeet.x() > blockCenterPosX) {
                Vec3 footVectorX = new Vec3(blockPos.getCenter().x() +0.51, blockPos.getCenter().y(), blockPos.getCenter().z());
                boolean footX = livingEntity.level().clip(new ClipContext(footVectorX, entityPositionFeet, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!footX) {
                    return true;
                }
            } else {
                Vec3 footVectorX = new Vec3(blockPos.getCenter().x() -0.51, blockPos.getCenter().y(), blockPos.getCenter().z());
                boolean footX = livingEntity.level().clip(new ClipContext(footVectorX, entityPositionFeet, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!footX) {
                    return true;
                }
            }
            if (entityPositionFeet.y() > blockCenterPosY) {
                Vec3 footVectorY = new Vec3(blockPos.getCenter().x(), blockPos.getCenter().y() +0.49, blockPos.getCenter().z());
                boolean footY = livingEntity.level().clip(new ClipContext(footVectorY, entityPositionFeet, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!footY) {
                    return true;
                }
            } else {
                Vec3 footVectorY = new Vec3(blockPos.getCenter().x(), blockPos.getCenter().y() -0.51, blockPos.getCenter().z());
                boolean footY = livingEntity.level().clip(new ClipContext(footVectorY, entityPositionFeet, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!footY) {
                    return true;
                }
            }
            if (entityPositionFeet.z() > blockCenterPosZ) {
                Vec3 footVectorZ = new Vec3(blockPos.getCenter().x(), blockPos.getCenter().y(), blockPos.getCenter().z() +0.51);
                boolean footZ = livingEntity.level().clip(new ClipContext(footVectorZ, entityPositionFeet, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!footZ) {
                    return true;
                }
            } else {
                Vec3 footVectorZ = new Vec3(blockPos.getCenter().x(), blockPos.getCenter().y(), blockPos.getCenter().z() -0.51);
                boolean footZ = livingEntity.level().clip(new ClipContext(footVectorZ, entityPositionFeet, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity)).getType() == HitResult.Type.BLOCK;
                if (!footZ) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void applyDamage(Level world, BlockPos pos, LivingEntity livingEntity) {
        DamageSource damageSource = new DamageSource(
                world.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolder(ModDamageTypes.FISSION).get()
        );
        livingEntity.hurt(damageSource, 1);
    }
}
