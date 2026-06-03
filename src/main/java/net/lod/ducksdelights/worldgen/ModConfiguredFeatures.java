package net.lod.ducksdelights.worldgen;

import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.worldgen.features.DemonCoreRoomOverworldFeature;
import net.lod.ducksdelights.worldgen.features.ModFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> MARSHMALLOW_ROOT_KEY = registerKey("marshmallow_root_key");

    public static final ResourceKey<ConfiguredFeature<?, ?>> GIANT_CLAM_KEY = registerKey("giant_clam_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GIANT_CLAM_NETHER_KEY = registerKey("giant_clam_nether_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GIANT_CLAM_END_KEY = registerKey("giant_clam_end_key");

    public static final ResourceKey<ConfiguredFeature<?, ?>> DEMON_CORE_OVERWORLD_ROOM_KEY = registerKey("demon_core_overworld_room_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DEMON_CORE_OVERWORLD_DEEP_ROOM_KEY = registerKey("demon_core_overworld_deep_room_key");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        register(context, MARSHMALLOW_ROOT_KEY, Feature.FLOWER, new RandomPatchConfiguration(48, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_MARSHMALLOW_ROOT.get())))));
        register(context, GIANT_CLAM_KEY, ModFeatures.GIANT_CLAM_FEATURE.get(), new CountConfiguration(2));
        register(context, GIANT_CLAM_NETHER_KEY, ModFeatures.GIANT_CLAM_NETHER_FEATURE.get(), new RandomPatchConfiguration(24, 6, 3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.GIANT_CLAM_NETHER.get())))));
        register(context, GIANT_CLAM_END_KEY, ModFeatures.GIANT_CLAM_END_FEATURE.get(), new RandomPatchConfiguration(16, 4, 4, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.GIANT_CLAM_ENDER.get())))));
        register(context, DEMON_CORE_OVERWORLD_ROOM_KEY, ModFeatures.DEMON_CORE_OVERWORLD_ROOM_FEATURE.get(), new NoneFeatureConfiguration());
        register(context, DEMON_CORE_OVERWORLD_DEEP_ROOM_KEY, ModFeatures.DEMON_CORE_OVERWORLD_ROOM_FEATURE.get(), new NoneFeatureConfiguration());
    }

    @SuppressWarnings("removal")
    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(DucksDelights.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
