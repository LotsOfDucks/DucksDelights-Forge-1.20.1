package net.lod.ducksdelights.item.custom;

import com.google.common.collect.Lists;
import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.entity.mobeffects.ModMobEffects;
import net.lod.ducksdelights.item.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, DucksDelights.MOD_ID);


    public static final RegistryObject<Potion> PURIFICATION =
            POTIONS.register("purification", () ->
                    new Potion(new MobEffectInstance(ModMobEffects.PURIFICATION.get(), 1200)));

    public static final RegistryObject<Potion> LONG_PURIFICATION =
            POTIONS.register("long_purification", () ->
                    new Potion("purification", new MobEffectInstance(ModMobEffects.PURIFICATION.get(), 3600)));

    public static final RegistryObject<Potion> BEFOULING =
            POTIONS.register("befouling", () ->
                    new Potion(new MobEffectInstance(ModMobEffects.BEFOULING.get(), 600)));

    public static final RegistryObject<Potion> LONG_BEFOULING =
            POTIONS.register("long_befouling", () ->
                    new Potion("befouling", new MobEffectInstance(ModMobEffects.BEFOULING.get(), 1800)));

    public static final RegistryObject<Potion> LOVE =
            POTIONS.register("love", () ->
                    new Potion(new MobEffectInstance(ModMobEffects.LOVE.get(), 1)));

    public static final RegistryObject<Potion> GAMBLING =
            POTIONS.register("gambling", () ->
                    new Potion(new MobEffectInstance(ModMobEffects.GAMBLING.get(), 1)));

    public static final RegistryObject<Potion> ASPHYXIATION =
            POTIONS.register("asphyxiation", () ->
                    new Potion(new MobEffectInstance(ModMobEffects.ASPHYXIATION.get(), 500)));

    public static final RegistryObject<Potion> LONG_ASPHYXIATION =
            POTIONS.register("long_asphyxiation", () ->
                    new Potion("asphyxiation", new MobEffectInstance(ModMobEffects.ASPHYXIATION.get(), 700)));

    public static final RegistryObject<Potion> STRONG_ASPHYXIATION =
            POTIONS.register("strong_asphyxiation", () ->
                    new Potion("asphyxiation", new MobEffectInstance(ModMobEffects.ASPHYXIATION.get(), 500, 1)));

    public static final RegistryObject<Potion> PROGENITOR =
            POTIONS.register("progenitor", () ->
                    new Potion(new MobEffectInstance(ModMobEffects.PROGENITOR.get(), 6000)));

    public static final RegistryObject<Potion> LONG_PROGENITOR =
            POTIONS.register("long_progenitor", () ->
                    new Potion("progenitor", new MobEffectInstance(ModMobEffects.PROGENITOR.get(), 12000)));

    public static final RegistryObject<Potion> STRONG_PROGENITOR =
            POTIONS.register("strong_progenitor", () ->
                    new Potion("progenitor", new MobEffectInstance(ModMobEffects.PROGENITOR.get(), 6000, 1)));

    public static final RegistryObject<Potion> BULWARK =
            POTIONS.register("bulwark", () ->
                    new Potion(new MobEffectInstance(ModMobEffects.BULWARK.get(), 1200)));

    public static final RegistryObject<Potion> LONG_BULWARK =
            POTIONS.register("long_bulwark", () ->
                    new Potion("bulwark", new MobEffectInstance(ModMobEffects.BULWARK.get(), 6000)));

    public static final RegistryObject<Potion> GREATER_BULWARK =
            POTIONS.register("greater_bulwark", () ->
                    new Potion(new MobEffectInstance(ModMobEffects.GREATER_BULWARK.get(), 1200)));

    public static final RegistryObject<Potion> GRAVITATION =
            POTIONS.register("gravitation", () ->
                    new Potion(new MobEffectInstance(ModMobEffects.GRAVITATION.get(), 2000)));

    public static final RegistryObject<Potion> LONG_GRAVITATION =
            POTIONS.register("long_gravitation", () ->
                    new Potion("gravitation", new MobEffectInstance(ModMobEffects.GRAVITATION.get(), 4000)));

    public static final RegistryObject<Potion> LEVITATION =
            POTIONS.register("levitation", () ->
                    new Potion(new MobEffectInstance(MobEffects.LEVITATION, 140)));

    public static final RegistryObject<Potion> LONG_LEVITATION =
            POTIONS.register("long_levitation", () ->
                    new Potion("levitation", new MobEffectInstance(MobEffects.LEVITATION, 280)));

    public static final RegistryObject<Potion> STRONG_LEVITATION =
            POTIONS.register("strong_levitation", () ->
                    new Potion("levitation", new MobEffectInstance(MobEffects.LEVITATION, 200, 1)));





    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
