package net.lod.ducksdelights.entity.mobeffects;

import net.lod.ducksdelights.DucksDelights;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.UUID;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, DucksDelights.MOD_ID);

    public static final RegistryObject<MobEffect> PURIFICATION =
            MOB_EFFECTS.register("purification", () -> new ModMobEffect(
                    MobEffectCategory.BENEFICIAL, 16116445));

    public static final RegistryObject<MobEffect> BEFOULING =
            MOB_EFFECTS.register("befouling", () -> new ModMobEffect(
                    MobEffectCategory.HARMFUL, 3543591));

    public static final RegistryObject<MobEffect> ASPHYXIATION =
            MOB_EFFECTS.register("asphyxiation", () -> new ModMobEffect(
                    MobEffectCategory.HARMFUL, 2675));

    public static final RegistryObject<MobEffect> LOVE =
            MOB_EFFECTS.register("love", () -> new ModInstantMobEffect(
                    MobEffectCategory.NEUTRAL, 16262179));

    public static final RegistryObject<MobEffect> GAMBLING =
            MOB_EFFECTS.register("gambling", () -> new ModInstantMobEffect(
                    MobEffectCategory.NEUTRAL, 16766720));

    public static final RegistryObject<MobEffect> BULWARK =
            MOB_EFFECTS.register("bulwark", () -> new ModMobEffect(
                    MobEffectCategory.BENEFICIAL, 3442654)
                    .addAttributeModifier(Attributes.ARMOR, "f798dc57-2a44-46d8-8522-fe8eff98e5d5", 4, AttributeModifier.Operation.ADDITION)
                    .addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "f5e1946c-9a4b-4eef-8178-d27dd311754f", 1, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<MobEffect> GREATER_BULWARK =
            MOB_EFFECTS.register("greater_bulwark", () -> new ModMobEffect(
                    MobEffectCategory.BENEFICIAL, 3442654)
                    .addAttributeModifier(Attributes.ARMOR, "30484c45-83fc-4e9e-aad3-b92e91f55229", 10, AttributeModifier.Operation.ADDITION)
                    .addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "72dca167-ba0f-4d3e-9d16-b15eff6f1fd0", 2, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<MobEffect> GRAVITATION =
            MOB_EFFECTS.register("gravitation", () -> new ModMobEffect(
                    MobEffectCategory.HARMFUL, 4069984)
                    .addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "1b69802a-2305-43a1-b70b-bc5fdfc1f10a", 0.75, AttributeModifier.Operation.MULTIPLY_BASE));

    public static final RegistryObject<MobEffect> STEP_UP =
            MOB_EFFECTS.register("step_up", () -> new ModMobEffect(
                    MobEffectCategory.BENEFICIAL, 3134362)
                    .addAttributeModifier(ForgeMod.STEP_HEIGHT_ADDITION.get(), "3f3e4532-4a4c-4883-abac-20d8b3bef55d", 0.5, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<MobEffect> TIME_BOMB =
            MOB_EFFECTS.register("time_bomb", () -> new ModMobEffect(
                    MobEffectCategory.HARMFUL, 15715670));

    public static final RegistryObject<MobEffect> PLAGUE =
            MOB_EFFECTS.register("plague", () -> new ModMobEffect(
                    MobEffectCategory.HARMFUL, 4526678));

    public static final RegistryObject<MobEffect> ENDER_TRANSFERENCE =
            MOB_EFFECTS.register("ender_transference", () -> new ModInstantMobEffect(
                    MobEffectCategory.HARMFUL, 1072721));

    public static final RegistryObject<MobEffect> BURNING =
            MOB_EFFECTS.register("burning", () -> new ModInstantMobEffect(
                    MobEffectCategory.HARMFUL, 16750848));

    public static final RegistryObject<MobEffect> FREEZING =
            MOB_EFFECTS.register("freezing", () -> new ModMobEffect(
                    MobEffectCategory.HARMFUL, 8826621));

    public static final RegistryObject<MobEffect> GREEN_THUMB =
            MOB_EFFECTS.register("green_thumb", () -> new ModMobEffect(
                    MobEffectCategory.BENEFICIAL, 4895570));



    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
