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
    public static ForgeConfigSpec.ConfigValue<Boolean> GRAVITATION_ELYTRA_LOCK;
    public static boolean gravitation_elytra_lock;

    public Config() {
    }

    @SubscribeEvent
    static void onLoad(ModConfigEvent.Loading configEvent) {
        demon_core_range = DEMON_CORE_RANGE.get();
        demon_core_damage_scale = DEMON_CORE_DAMAGE_SCALE.get();
        resonator_mine_bedrock = RESONATOR_CAN_MINE_BEDROCK.get();
        resonator_mine_unmineables = RESONATOR_CAN_MINE_UNMINEABLES.get();
        gravitation_elytra_lock = GRAVITATION_ELYTRA_LOCK.get();
    }

    static {
        BUILDER.push("Duck's Delights");
        DEMON_CORE_RANGE = BUILDER.comment("Damaging Range in Blocks").define("Range", 20);
        DEMON_CORE_DAMAGE_SCALE = BUILDER.comment("Scaling Rate").define("Scale", 1.0F);
        RESONATOR_CAN_MINE_BEDROCK = BUILDER.comment("Can The Resonator Break Bedrock").define("Break Bedrock", true);
        RESONATOR_CAN_MINE_UNMINEABLES = BUILDER.comment("Can The Resonator Break Unmineables").define("Break Unbreakable", false);
        GRAVITATION_ELYTRA_LOCK =BUILDER.comment("Gravitation Potion Locks Elytra").define("Gravitation Elytra Lock", true);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
