package net.lod.ducksdelights;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DucksDelights.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static Common COMMON_CONFIG;
    public static class Common {
        public Common(ForgeConfigSpec.Builder builder) {
            demon_core_range = builder.
                    translation("demon_core_range").
                    comment("Distance at which the Demon Core block will begin damaging entities (Blocks).").
                    define("Range DC(int)", 20);
            demon_core_damage_scale = builder.
                    translation("demon_core_damage_scale").
                    comment("Scaling Rate at which the Demon Core damage will scale up. (Higher = Faster).").
                    define("Scale DC(float)", 1.0F);
            resonator_mine_bedrock = builder.
                    translation("resonator_mine_bedrock").
                    comment("Whether the Resonator can break bedrock.").
                    define("Can Break Bedrock R(boolean)", true);
            resonator_mine_unmineables = builder.
                    translation("resonator_mine_unmineables").
                    comment("Whether the Resonator can break normally unbreakable blocks.").
                    define("Can Break Unbreakable Blocks R(boolean)", false);
            entity_detector_range = builder.
                    translation("entity_detector_range").
                    comment("Distance at which the Entity Detector block will detect entities (Blocks).").
                    define("Range ED(int)", 10);
            monster_detector_range = builder.
                    translation("monster_detector_range").
                    comment("Distance at which the Monster Detector block will detect monsters (Blocks).").
                    define("Range MD(int)", 10);
            player_detector_range = builder.
                    translation("player_detector_range").
                    comment("Distance at which the Player Detector block will detect players (Blocks).").
                    define("Range PD(int)", 7);
            gravitation_elytra_lock = builder.
                    translation("gravitation_elytra_lock").
                    comment("Whether the Gravitation Effect blocks elytra use.").
                    define("Interrupt Elytra? GP(boolean)", true);
            home_potion_cross_dimensions = builder.
                    translation("home_potion_cross_dimensions").
                    comment("Whether the Potion of Home can teleport user across dimensions.").
                    define("Can cross Dimensions? HP(boolean)", true);
            home_potion_cooldown = builder.
                    translation("home_potion_cross_dimensions").
                    comment("Cooldown of Potion of Home after use (seconds).").
                    define("Cooldown Duration HP(int)", 10);
            recovery_potion_cross_dimensions = builder.
                    translation("recovery_potion_cross_dimensions").
                    comment("Whether the Potion of Recovery can teleport user across dimensions.").
                    define("Can cross Dimensions? RP(boolean)", false);
            recovery_potion_cooldown = builder.
                    translation("recovery_potion_cross_dimensions").
                    comment("Cooldown of Potion of Recovery after use (seconds).").
                    define("Cooldown Duration RP(int)", 20);
            wormhole_potion_cross_dimensions = builder.
                    translation("wormhole_potion_cross_dimensions").
                    comment("Whether the Wormhole Potion can teleport user across dimensions.").
                    define("Can cross Dimensions? WP(boolean)", true);
            wormhole_potion_cooldown = builder.
                    translation("wormhole_potion_cooldown").
                    comment("Cooldown of Wormhole Potion after use (seconds).").
                    define("Cooldown Duration WP(int)", 10);
            ender_key_cooldown = builder.
                    translation("ender_key_cooldown").
                    comment("Cooldown of Ender Key after use (seconds).").
                    define("Cooldown Duration EK(int)", 3);
            monolith_should_spawn = builder.
                    translation("monolith_should_spawn").
                    comment("Whether of not you Acknowledge the Monoliths.").
                    define("Acknowledge?", true);
        }

        public final ForgeConfigSpec.ConfigValue<Integer> demon_core_range;
        public final ForgeConfigSpec.ConfigValue<Float> demon_core_damage_scale;
        public final ForgeConfigSpec.ConfigValue<Boolean> resonator_mine_bedrock;
        public final ForgeConfigSpec.ConfigValue<Boolean> resonator_mine_unmineables;
        public final ForgeConfigSpec.ConfigValue<Integer> entity_detector_range;
        public final ForgeConfigSpec.ConfigValue<Integer> monster_detector_range;
        public final ForgeConfigSpec.ConfigValue<Integer> player_detector_range;
        public final ForgeConfigSpec.ConfigValue<Boolean> gravitation_elytra_lock;
        public final ForgeConfigSpec.ConfigValue<Boolean> home_potion_cross_dimensions;
        public final ForgeConfigSpec.ConfigValue<Integer> home_potion_cooldown;
        public final ForgeConfigSpec.ConfigValue<Boolean> recovery_potion_cross_dimensions;
        public final ForgeConfigSpec.ConfigValue<Integer> recovery_potion_cooldown;
        public final ForgeConfigSpec.ConfigValue<Boolean> wormhole_potion_cross_dimensions;
        public final ForgeConfigSpec.ConfigValue<Integer> wormhole_potion_cooldown;
        public final ForgeConfigSpec.ConfigValue<Integer> ender_key_cooldown;
        public final ForgeConfigSpec.ConfigValue<Boolean> monolith_should_spawn;
    }
}
