package net.lod.ducksdelights.entity.mobeffects;

import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class ModMobEffect extends MobEffect {
    protected ModMobEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
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
                if (player.isFallFlying()) {
                    player.stopFallFlying();
                }
            }
            if (!pLivingEntity.onGround() && !pLivingEntity.onClimbable()) {
                pLivingEntity.fallDistance += 0.5F;
            }
            if (pLivingEntity instanceof FlyingMob flyingMob) {
                flyingMob.move(MoverType.SELF, new Vec3(0, -1, 0));
            }
        } else if (this == ModMobEffects.ARE_YOU_READY_TO_MEET_GOD.get()) {
            pLivingEntity.move(MoverType.SELF, new Vec3(0, 10, 0));
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
                    mob.setTarget(null);
                }
            }
        } else if (this == ModMobEffects.GAMBLING.get()) {
            Iterator<MobEffect> effectIterator = ForgeRegistries.MOB_EFFECTS.getValues().iterator();
            List<MobEffect> effectList = new java.util.ArrayList<>(List.of());
            while (effectIterator.hasNext()) {
                effectList.add(effectIterator.next());
            }
            int target = pLivingEntity.level().random.nextInt(0, effectList.size());
            int amplifier = pLivingEntity.level().random.nextIntBetweenInclusive(0, 2);
            if (effectList.get(target).isInstantenous()) {
                pLivingEntity.addEffect(new MobEffectInstance(effectList.get(target), 1, amplifier));
            } else {
                int duration = pLivingEntity.level().random.nextIntBetweenInclusive(200, 3600);
                pLivingEntity.addEffect(new MobEffectInstance(effectList.get(target), duration, amplifier));
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
        } else if (this == ModMobEffects.ARE_YOU_READY_TO_MEET_GOD.get()) {
            return true;
        }
        return this == ModMobEffects.BEFOULING.get();
    }
}
