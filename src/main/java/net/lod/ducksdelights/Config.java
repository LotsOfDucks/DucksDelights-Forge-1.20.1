package net.lod.ducksdelights;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = DucksDelights.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.ConfigValue<Integer> DEMON_CORE_RANGE;
    public static int demon_core_range;
    public static ForgeConfigSpec.ConfigValue<Float> DEMON_CORE_DAMAGE_SCALE;
    public static float demon_core_damage_scale;

    public static ForgeConfigSpec.ConfigValue<Boolean> RESONATOR_CAN_MINE_BEDROCK;
    public static boolean resonator_mine_bedrock;
    public static ForgeConfigSpec.ConfigValue<Boolean> RESONATOR_CAN_MINE_UNMINEABLES;
    public static boolean resonator_mine_unmineables;

    public static ForgeConfigSpec.ConfigValue<Integer> ENTITY_DETECTOR_RANGE;
    public static int entity_detector_range;
    public static ForgeConfigSpec.ConfigValue<Integer> MONSTER_DETECTOR_RANGE;
    public static int monster_detector_range;
    public static ForgeConfigSpec.ConfigValue<Integer> PLAYER_DETECTOR_RANGE;
    public static int player_detector_range;

    public static ForgeConfigSpec.ConfigValue<Boolean> GRAVITATION_ELYTRA_LOCK;
    public static boolean gravitation_elytra_lock;

    public static ForgeConfigSpec.ConfigValue<Boolean> HOME_POTION_CROSS_DIMENSIONS;
    public static boolean home_potion_cross_dimensions;
    public static ForgeConfigSpec.ConfigValue<Integer> HOME_POTION_COOLDOWN;
    public static int home_potion_cooldown;

    public static ForgeConfigSpec.ConfigValue<Boolean> RECOVERY_POTION_CROSS_DIMENSIONS;
    public static boolean recovery_potion_cross_dimensions;
    public static ForgeConfigSpec.ConfigValue<Integer> RECOVERY_POTION_COOLDOWN;
    public static int recovery_potion_cooldown;

    public static ForgeConfigSpec.ConfigValue<Boolean> WORMHOLE_POTION_CROSS_DIMENSIONS;
    public static boolean wormhole_potion_cross_dimensions;
    public static ForgeConfigSpec.ConfigValue<Integer> WORMHOLE_POTION_COOLDOWN;
    public static int wormhole_potion_cooldown;


    public Config() {
    }

    @SubscribeEvent
    static void onLoad(ModConfigEvent.Loading configEvent) {
        demon_core_range = DEMON_CORE_RANGE.get();
        demon_core_damage_scale = DEMON_CORE_DAMAGE_SCALE.get();

        resonator_mine_bedrock = RESONATOR_CAN_MINE_BEDROCK.get();
        resonator_mine_unmineables = RESONATOR_CAN_MINE_UNMINEABLES.get();

        entity_detector_range = ENTITY_DETECTOR_RANGE.get();
        monster_detector_range = MONSTER_DETECTOR_RANGE.get();
        player_detector_range = PLAYER_DETECTOR_RANGE.get();

        gravitation_elytra_lock = GRAVITATION_ELYTRA_LOCK.get();

        home_potion_cross_dimensions = HOME_POTION_CROSS_DIMENSIONS.get();
        home_potion_cooldown = HOME_POTION_COOLDOWN.get();

        recovery_potion_cross_dimensions = RECOVERY_POTION_CROSS_DIMENSIONS.get();
        recovery_potion_cooldown = RECOVERY_POTION_COOLDOWN.get();

        wormhole_potion_cross_dimensions = WORMHOLE_POTION_CROSS_DIMENSIONS.get();
        wormhole_potion_cooldown = WORMHOLE_POTION_COOLDOWN.get();

    }

    static {
        BUILDER.push("Duck's Delights");
        DEMON_CORE_RANGE = BUILDER.comment("Damaging Range in Blocks").define("Range", 20);
        DEMON_CORE_DAMAGE_SCALE = BUILDER.comment("Scaling Rate").define("Scale", 1.0F);

        RESONATOR_CAN_MINE_BEDROCK = BUILDER.comment("Can The Resonator Break Bedrock").define("Break Bedrock", true);
        RESONATOR_CAN_MINE_UNMINEABLES = BUILDER.comment("Can The Resonator Break Unmineables").define("Break Unbreakable", false);

        ENTITY_DETECTOR_RANGE = BUILDER.comment("Entity Detector Range in Blocks").define("Block Radius", 10);
        MONSTER_DETECTOR_RANGE = BUILDER.comment("Monster Detector Range in Blocks").define("Block Radius", 10);
        PLAYER_DETECTOR_RANGE = BUILDER.comment("Player Detector Range in Blocks").define("Block Radius", 7);

        GRAVITATION_ELYTRA_LOCK =BUILDER.comment("Gravitation Potion Locks Elytra").define("Gravitation Elytra Lock", true);

        HOME_POTION_CROSS_DIMENSIONS =BUILDER.comment("Home Potion can Cross Dimensions").define("Home Potion Dimension Crossing", true);
        HOME_POTION_COOLDOWN = BUILDER.comment("Home Potion Cooldown").define("Home Potion Cooldown in Seconds", 10);

        RECOVERY_POTION_CROSS_DIMENSIONS =BUILDER.comment("Recovery Potion can Cross Dimensions").define("Recovery Potion Dimension Crossing", false);
        RECOVERY_POTION_COOLDOWN = BUILDER.comment("Recovery Potion Cooldown").define("Recovery Potion in Seconds", 20);

        WORMHOLE_POTION_CROSS_DIMENSIONS =BUILDER.comment("Wormhole Potion can Cross Dimensions").define("Wormhole Potion Dimension Crossing", true);
        WORMHOLE_POTION_COOLDOWN = BUILDER.comment("Wormhole Potion Cooldown").define("Wormhole Potion Cooldown in Seconds", 10);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
