package net.lod.ducksdelights.worldgen;

import com.google.common.collect.ImmutableList;
import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.block.custom.AbstractGiantNetherClamBlock;
import net.lod.ducksdelights.worldgen.features.ModFeatures;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import javax.annotation.Nullable;
import java.util.List;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> MARSHMALLOW_ROOT_KEY = registerKey("marshmallow_root_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GIANT_CLAM_KEY = registerKey("giant_clam_key");
    //public static final ResourceKey<ConfiguredFeature<?, ?>> GIANT_CLAM_NETHER_KEY = registerKey("giant_clam_nether_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GIANT_CLAM_END_KEY = registerKey("giant_clam_end_key");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        register(context, MARSHMALLOW_ROOT_KEY, Feature.FLOWER, new RandomPatchConfiguration(48, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_MARSHMALLOW_ROOT.get())))));
        register(context, GIANT_CLAM_KEY, ModFeatures.GIANT_CLAM_FEATURE.get(), new CountConfiguration(8));
        //register(context, GIANT_CLAM_NETHER_KEY, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.GIANT_CLAM_NETHER.get())), List.of(Blocks.SOUL_SOIL, Blocks.SOUL_SAND)));
        register(context, GIANT_CLAM_END_KEY, ModFeatures.GIANT_CLAM_END_FEATURE.get(), new RandomPatchConfiguration(24, 4, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.GIANT_CLAM_ENDER.get())))));
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
