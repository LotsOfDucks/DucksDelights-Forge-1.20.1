package net.lod.ducksdelights.entity.mobeffects;

import net.lod.ducksdelights.Config;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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
            if (pLivingEntity.getAirSupply() > 1) {
                pLivingEntity.setAirSupply(pLivingEntity.getAirSupply() - (5 + pAmplifier));
            } else {
                if (!pLivingEntity.isUnderWater()) {
                    pLivingEntity.setAirSupply(pLivingEntity.getAirSupply() - (5));
                }
            }
        } else if (this == ModMobEffects.PROGENITOR.get()) {
            if (pLivingEntity.level().random.nextIntBetweenInclusive(1, 20) == 20) {
                pLivingEntity.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (pLivingEntity.level().random.nextFloat() - pLivingEntity.level().random.nextFloat()) * 0.2F + 1.0F);
                pLivingEntity.spawnAtLocation(Items.EGG);
                pLivingEntity.gameEvent(GameEvent.ENTITY_PLACE);
            }
        } else if (this == ModMobEffects.GRAVITATION.get()) {
            if (pLivingEntity instanceof Player player) {
                if (Config.GRAVITATION_ELYTRA_LOCK.get()) {
                    if (player.isFallFlying()) {
                        player.stopFallFlying();
                    }
                }
                if (player.getAbilities().flying && !player.isCreative()) {
                    player.move(MoverType.SELF, new Vec3(0, -1, 0));
                }
            }
            if (!pLivingEntity.onGround() && !pLivingEntity.onClimbable()) {
                pLivingEntity.fallDistance += 0.5F;
            }
            if (pLivingEntity instanceof FlyingMob flyingMob) {
                flyingMob.move(MoverType.SELF, new Vec3(0, -1, 0));
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
        } else if (this == ModMobEffects.BURNING.get()) {
            this.applyInstantenousEffect(null, null, pLivingEntity, pAmplifier, pLivingEntity.getHealth());
        } else if (this == ModMobEffects.FREEZING.get()) {
            if (pLivingEntity.canFreeze()) {
                pLivingEntity.setIsInPowderSnow(true);
                if (pLivingEntity.getTicksFrozen() < 60) {
                    pLivingEntity.setTicksFrozen(pLivingEntity.getTicksFrozen() + (60 + (20 * pAmplifier)));
                }
                pLivingEntity.setTicksFrozen(pLivingEntity.getTicksFrozen() + pAmplifier);
            }
        }
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity pSource, @Nullable Entity pIndirectSource, LivingEntity pLivingEntity, int pAmplifier, double pHealth) {
        if (this == ModMobEffects.LOVE.get()) {
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
                if (mob.getTarget() == pIndirectSource) {
                    mob.setAggressive(false);
                    mob.setTarget(null);
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
        } else if (this == ModMobEffects.BURNING.get()) {
            if (!pLivingEntity.fireImmune()) {
                pLivingEntity.setSecondsOnFire(6 * (pAmplifier + 1));
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
        } else if (this == ModMobEffects.PROGENITOR.get()) {
            rate = 20 >> pAmplifier;
            if (rate > 0) {
                return pDuration % rate == 0;
            } else {
                return true;
            }
        } else if (this == ModMobEffects.GRAVITATION.get()) {
            return true;
        } else if (this == ModMobEffects.TIME_BOMB.get()) {
            return pDuration == 1;
        } else if (this == ModMobEffects.PLAGUE.get()) {
            rate = 40 >> pAmplifier;
            if (rate > 0) {
                return pDuration % rate == 0;
            } else {
                return true;
            }
        } else if (this == ModMobEffects.FREEZING.get()) {
            return true;
        }
        return this == ModMobEffects.BEFOULING.get();
    }

    public void activateGamble(LivingEntity pLivingEntity, List<MobEffect> effectList) {
        int target = pLivingEntity.level().random.nextInt(0, effectList.size());
        int amplifier = pLivingEntity.level().random.nextIntBetweenInclusive(0, 2);
        if (effectList.get(target).isInstantenous()) {
            pLivingEntity.addEffect(new MobEffectInstance(effectList.get(target), 1, amplifier));
        } else {
            int duration = pLivingEntity.level().random.nextIntBetweenInclusive(200, 3600);
            pLivingEntity.addEffect(new MobEffectInstance(effectList.get(target), duration, amplifier));
        }
        effectList.remove(target);
    }

    public void performTeleportSwap (Entity pIndirectSource, LivingEntity pLivingEntity) {
        if (pIndirectSource != null) {
            Vec3 targetPosition = new Vec3(pLivingEntity.position().toVector3f());
            Vec3 throwerPosition = new Vec3(pIndirectSource.position().toVector3f());

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
            pIndirectSource.level().playSound(null, throwerPosition.x(), throwerPosition.y(), throwerPosition.z(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.NEUTRAL, 1F, 1F);

            pLivingEntity.teleportTo(throwerPosition.x(), throwerPosition.y(), throwerPosition.z());
            pLivingEntity.level().playSound(null, targetPosition.x(), targetPosition.y(), targetPosition.z(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.NEUTRAL, 1F, 1F);
        }
    }
}
