package net.lod.ducksdelights.entity.mobeffects;

import net.lod.ducksdelights.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class ModMobEffect extends MobEffect {
    protected ModMobEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Entity source = pLivingEntity.getLastAttacker();
        if (pLivingEntity.hasEffect(ModMobEffects.BULWARK.get()) && pLivingEntity.hasEffect(ModMobEffects.GREATER_BULWARK.get())) {
            pLivingEntity.removeEffect(ModMobEffects.BULWARK.get());
        }
        if (pLivingEntity.hasEffect(ModMobEffects.BEFOULING.get()) && pLivingEntity.hasEffect(ModMobEffects.PURIFICATION.get())) {
            pLivingEntity.removeEffect(ModMobEffects.BEFOULING.get());
            pLivingEntity.removeEffect(ModMobEffects.PURIFICATION.get());
        }
        if (pLivingEntity.hasEffect(MobEffects.LEVITATION) && pLivingEntity.hasEffect(ModMobEffects.GRAVITATION.get())) {
            pLivingEntity.removeEffect(MobEffects.LEVITATION);
            pLivingEntity.removeEffect(ModMobEffects.GRAVITATION.get());
        }
        if (pLivingEntity.isOnFire() && pLivingEntity.hasEffect(ModMobEffects.FREEZING.get())) {
            pLivingEntity.removeEffect(ModMobEffects.FREEZING.get());
        }
        if (this == ModMobEffects.PURIFICATION.get()) {
            for (MobEffectInstance mobEffectInstance : pLivingEntity.getActiveEffects()) {
                MobEffect effect = mobEffectInstance.getEffect();
                if (effect.getCategory() == MobEffectCategory.HARMFUL) {
                    pLivingEntity.removeEffect(effect);
                }
            }
        } else if (this == ModMobEffects.BEFOULING.get()) {
            for (MobEffectInstance mobEffectInstance : pLivingEntity.getActiveEffects()) {
                MobEffect effect = mobEffectInstance.getEffect();
                if (effect.getCategory() == MobEffectCategory.BENEFICIAL) {
                    pLivingEntity.removeEffect(effect);
                }
            }
        } else if (this == ModMobEffects.ASPHYXIATION.get()) {
            if (pLivingEntity.getMobType() != MobType.UNDEAD) {
                if (pLivingEntity.getAirSupply() > 1) {
                    pLivingEntity.setAirSupply(pLivingEntity.getAirSupply() - (5 + pAmplifier));
                } else {
                    if (!pLivingEntity.isUnderWater()) {
                        pLivingEntity.setAirSupply(pLivingEntity.getAirSupply() - (5));
                    }
                }
            } else {
                if (pLivingEntity instanceof Zombie zombie) {
                    if (!(zombie instanceof Drowned)) {
                        if (pLivingEntity.getEffect(this).endsWithin(1)) {
                            zombie.convertTo(EntityType.DROWNED, true);
                            if (!zombie.isSilent()) {
                                zombie.level().levelEvent(null, 1040, zombie.blockPosition(), 0);
                            }
                        }
                    } else {
                        pLivingEntity.removeEffect(this);
                    }
                } else {
                    pLivingEntity.removeEffect(this);
                }
            }
        } else if (this == ModMobEffects.GRAVITATION.get()) {
            if (pLivingEntity instanceof Player player) {
                if (Config.GRAVITATION_ELYTRA_LOCK.get()) {
                    if (player.isFallFlying()) {
                        player.stopFallFlying();
                    }
                }
            }
            if (!pLivingEntity.onGround() && !pLivingEntity.onClimbable()) {
                pLivingEntity.fallDistance += 0.5F;
            }
            if (pLivingEntity instanceof FlyingMob flyingMob) {
                flyingMob.push(0, -0.4, 0);
            }
        } else if (this == ModMobEffects.TIME_BOMB.get()) {
            pLivingEntity.level().explode(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), pAmplifier + 2, Level.ExplosionInteraction.MOB);
        } else if (this == ModMobEffects.PLAGUE.get()) {
            pLivingEntity.hurt(pLivingEntity.damageSources().wither(), 1.0F);
            AABB box = (pLivingEntity.getBoundingBox().inflate(2));
            List<LivingEntity> nearbyEntities = pLivingEntity.level().getEntitiesOfClass(LivingEntity.class, box, LivingEntity::attackable);
            if (!nearbyEntities.isEmpty()) {
                for (LivingEntity targetEntity : nearbyEntities) {
                    if (targetEntity != pLivingEntity) {
                        if (targetEntity.level().getRandom().nextInt(0, 5) < 4) {
                            if (targetEntity instanceof Player player) {
                                if (!player.isCreative()) {
                                    if (!targetEntity.hasEffect(ModMobEffects.PLAGUE.get())) {
                                        targetEntity.addEffect(new MobEffectInstance(ModMobEffects.PLAGUE.get(), -1, pAmplifier));
                                    }
                                }
                            } else {
                                if (!targetEntity.hasEffect(ModMobEffects.PLAGUE.get())) {
                                    targetEntity.addEffect(new MobEffectInstance(ModMobEffects.PLAGUE.get(), -1, pAmplifier));
                                }
                            }
                        }
                    }
                }
            }
        } else if (this == ModMobEffects.GAMBLING.get()) {
            this.applyInstantenousEffect(null, null, pLivingEntity, pAmplifier, pLivingEntity.getHealth());
        } else if (this == ModMobEffects.LOVE.get()) {
            this.applyInstantenousEffect(null, source, pLivingEntity, pAmplifier, pLivingEntity.getHealth());
        } else if (this == ModMobEffects.ENDER_TRANSFERENCE.get()) {
            this.applyInstantenousEffect(null, source, pLivingEntity, pAmplifier, pLivingEntity.getHealth());
        } else if (this == ModMobEffects.LAUNCHING.get()) {
            this.applyInstantenousEffect(null, source, pLivingEntity, pAmplifier, pLivingEntity.getHealth());
        } else if (this == ModMobEffects.BURNING.get()) {
            this.applyInstantenousEffect(null, null, pLivingEntity, pAmplifier, pLivingEntity.getHealth());
        } else if (this == ModMobEffects.FREEZING.get()) {
            if (pLivingEntity.getType() != EntityType.STRAY) {
                if (pLivingEntity.canFreeze()) {
                    pLivingEntity.setIsInPowderSnow(true);
                    if (pLivingEntity.getTicksFrozen() < 60) {
                        pLivingEntity.setTicksFrozen(pLivingEntity.getTicksFrozen() + (60 + (20 * pAmplifier)));
                    }
                    pLivingEntity.setTicksFrozen(pLivingEntity.getTicksFrozen() + pAmplifier);
                }
                if (pLivingEntity.getType() == EntityType.SKELETON) {
                    pLivingEntity.setIsInPowderSnow(true);
                    if (pLivingEntity.getTicksFrozen() < 60) {
                        pLivingEntity.setTicksFrozen(pLivingEntity.getTicksFrozen() + (60 + (20 * pAmplifier)));
                    }
                    pLivingEntity.setTicksFrozen(pLivingEntity.getTicksFrozen() + pAmplifier);
                }
            } else {
                pLivingEntity.removeEffect(this);
            }
        } else if (this == ModMobEffects.GREEN_THUMB.get()) {
            this.applyInstantenousEffect(null, source, pLivingEntity, pAmplifier, pLivingEntity.getHealth());
        }
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity pSource, @Nullable Entity pIndirectSource, @NotNull LivingEntity pLivingEntity, int pAmplifier, double pHealth) {
        if (this == ModMobEffects.LOVE.get()) {
            if (pIndirectSource != null) {
                if (pLivingEntity instanceof Animal animal) {
                    if (animal.canFallInLove() && !animal.isBaby()) {
                        if (pIndirectSource instanceof Player player) {
                            animal.setInLove(player);
                        } else {
                            animal.setInLove(null);
                        }
                    }
                } else if (pLivingEntity instanceof Villager villager) {
                    if (pIndirectSource instanceof Player player) {
                        villager.getGossips().add(player.getUUID(), GossipType.MINOR_POSITIVE, 25);
                    }
                } else if (pLivingEntity instanceof Mob mob) {
                    if (mob instanceof NeutralMob neutralMob) {
                        neutralMob.forgetCurrentTargetAndRefreshUniversalAnger();
                    } else if (mob.getTarget() == pIndirectSource) {
                        mob.setAggressive(false);
                        mob.setTarget(null);
                    }
                }
            }
        } else if (this == ModMobEffects.GAMBLING.get()) {
            Iterator<MobEffect> effectIterator = ForgeRegistries.MOB_EFFECTS.getValues().iterator();
            List<MobEffect> effectList = new java.util.ArrayList<>(List.of());
            while (effectIterator.hasNext()) {
                effectList.add(effectIterator.next());
            }
            for (int rolls = 0; rolls <= pAmplifier; rolls++ ) {
                this.activateGamble(pLivingEntity, effectList);
            }
        } else if (this == ModMobEffects.ENDER_TRANSFERENCE.get()) {
            if (!pLivingEntity.level().isClientSide()) {
                this.performTeleportSwap(pIndirectSource, pLivingEntity);
            }
        } else if (this == ModMobEffects.LAUNCHING.get()) {
            if (!pLivingEntity.level().isClientSide()) {
                this.performLaunch(pIndirectSource, pLivingEntity, pAmplifier);
            }
        } else if (this == ModMobEffects.GREEN_THUMB.get()) {
            if (!pLivingEntity.level().isClientSide()) {
                this.applyGreenThumb(pLivingEntity, pAmplifier);
            }
        } else if (this == ModMobEffects.BURNING.get()) {
            if (!pLivingEntity.fireImmune()) {
                pLivingEntity.setSecondsOnFire(6 * (pAmplifier + 1));
            }
            if (pLivingEntity.hasEffect(ModMobEffects.FREEZING.get())) {
                    pLivingEntity.removeEffect(ModMobEffects.FREEZING.get());
                    pLivingEntity.clearFire();
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int rate;
        if (this == ModMobEffects.PURIFICATION.get()) {
            return true;
        } else if (this == ModMobEffects.ASPHYXIATION.get()) {
            return true;
        } else if (this == ModMobEffects.GREATER_BULWARK.get()) {
            return true;
        } else if (this == ModMobEffects.GRAVITATION.get()) {
            return true;
        } else if (this == ModMobEffects.STEP_UP.get()) {
            return true;
        } else if (this == ModMobEffects.TIME_BOMB.get()) {
            return pDuration == 1;
        } else if (this == ModMobEffects.GREEN_THUMB.get()) {
            return true;
        } else if (this == ModMobEffects.PLAGUE.get()) {
            rate = 40 >> pAmplifier;
            if (rate > 0) {
                return pDuration % rate == 0;
            } else {
                return true;
            }
        } else if (this == ModMobEffects.FREEZING.get()) {
            return true;
        } else
        return this == ModMobEffects.BEFOULING.get();
    }

    public void activateGamble(LivingEntity pLivingEntity, List<MobEffect> effectList) {
        int target = pLivingEntity.level().getRandom().nextInt(0, effectList.size());
        MobEffect targetEffect = effectList.get(target);
        int amplifier = pLivingEntity.level().getRandom().nextIntBetweenInclusive(0, 2);
        if (targetEffect.isInstantenous()) {
            pLivingEntity.addEffect(new MobEffectInstance(targetEffect, 1, amplifier));
        } else {
            if (pLivingEntity.hasEffect(targetEffect)) {
                int presentAmplifier = pLivingEntity.getEffect(targetEffect).getAmplifier();
                if (presentAmplifier < amplifier) {
                    pLivingEntity.removeEffect(targetEffect);
                }
            }
            int duration = pLivingEntity.level().getRandom().nextIntBetweenInclusive(200, 3600);
            pLivingEntity.addEffect(new MobEffectInstance(targetEffect, duration, amplifier));
        }
        effectList.remove(target);
    }

    public void performLaunch(Entity pIndirectSource, LivingEntity pLivingEntity, int amplifier) {
        if (pIndirectSource != null) {
            if (pIndirectSource == pLivingEntity) {
                pIndirectSource.hurt(pIndirectSource.damageSources().fall(), 1);
                pIndirectSource.push(0, 1 + amplifier, 0);
            } else {
                pLivingEntity.hurt(pLivingEntity.damageSources().fall(), 1);
                pLivingEntity.push(0, 1 + amplifier, 0);
            }
        } else {
            pLivingEntity.hurt(pLivingEntity.damageSources().fall(), 1);
            pLivingEntity.push(0, 1 + amplifier, 0);
        }
    }

    public void performTeleportSwap(Entity pIndirectSource, LivingEntity pLivingEntity) {
        if (pIndirectSource != null) {
            Vec3 targetPosition = new Vec3(pLivingEntity.position().toVector3f());
            Vec3 throwerPosition = new Vec3(pIndirectSource.position().toVector3f());
            
            if (pIndirectSource == pLivingEntity) {
                pIndirectSource.resetFallDistance();
                pIndirectSource.level().playSound(null, throwerPosition.x(), throwerPosition.y(), throwerPosition.z(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.NEUTRAL, 1F, 1F);
                pIndirectSource.hurt(pIndirectSource.damageSources().fall(), 2);
            } else {
                if (pIndirectSource.isPassenger()) {
                    Entity vehicle = pIndirectSource.getVehicle();
                    vehicle.ejectPassengers();
                }
                if (pLivingEntity.isPassenger()) {
                    Entity vehicle = pLivingEntity.getVehicle();
                    vehicle.ejectPassengers();
                }
                if (pIndirectSource.isVehicle()) {
                    pIndirectSource.ejectPassengers();
                }
                if (pLivingEntity.isVehicle()) {
                    pLivingEntity.ejectPassengers();
                }

                pIndirectSource.teleportTo(targetPosition.x(), targetPosition.y(), targetPosition.z());
                pIndirectSource.resetFallDistance();
                pIndirectSource.level().playSound(null, throwerPosition.x(), throwerPosition.y(), throwerPosition.z(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.NEUTRAL, 1F, 1F);
                pIndirectSource.hurt(pIndirectSource.damageSources().fall(), 2);


                pLivingEntity.teleportTo(throwerPosition.x(), throwerPosition.y(), throwerPosition.z());
                pLivingEntity.resetFallDistance();
                pLivingEntity.level().playSound(null, targetPosition.x(), targetPosition.y(), targetPosition.z(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.NEUTRAL, 1F, 1F);
                pLivingEntity.hurt(pIndirectSource.damageSources().fall(), 2);
            }
        }
    }

    public void applyGreenThumb(LivingEntity livingEntity, int amplifier) {
        Vec3 entityPos = livingEntity.position();
        Level level = livingEntity.level();
        if (level instanceof ServerLevel serverLevel) {
            if (amplifier >= 9) {
                amplifier = 8;
            }
            if (serverLevel.getRandom().nextInt(0, 10) >= (9 - amplifier)) {
                for (int roll = 1; roll <= 4; roll++) {
                    BlockPos targetPos = new BlockPos((int) Math.floor(entityPos.x()) + serverLevel.getRandom().nextIntBetweenInclusive(-4, 4), (int) Math.floor(entityPos.y()) + serverLevel.getRandom().nextIntBetweenInclusive(-2, 2), (int) Math.floor(entityPos.z()) + serverLevel.getRandom().nextIntBetweenInclusive(-4, 4));
                    BlockState targetState = serverLevel.getBlockState(targetPos);
                    Block targetBlock = targetState.getBlock();
                    if (targetBlock instanceof BonemealableBlock bonemealableBlock) {
                        if (bonemealableBlock.isValidBonemealTarget(serverLevel, targetPos, targetState, false)) {
                            bonemealableBlock.performBonemeal(serverLevel, serverLevel.getRandom(), targetPos, targetState);
                            level.levelEvent(1505, targetPos, 0);
                        }
                    }
                }
            }
        }
    }
}
