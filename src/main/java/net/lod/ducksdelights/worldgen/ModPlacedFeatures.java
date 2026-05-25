package net.lod.ducksdelights.worldgen;

import com.google.common.collect.ImmutableList;
import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.block.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.RandomPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> MARSHMALLOW_ROOT_PLACED_KEY = registerKey("marshmallow_root_placed");
    public static final ResourceKey<PlacedFeature> GIANT_CLAM_PLACED_KEY = registerKey("giant_clam_placed");
    //public static final ResourceKey<PlacedFeature> GIANT_CLAM_NETHER_PLACED_KEY = registerKey("giant_clam_nether_placed");
    public static final ResourceKey<PlacedFeature> GIANT_CLAM_END_PLACED_KEY = registerKey("giant_clam_end_placed");



    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, MARSHMALLOW_ROOT_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.MARSHMALLOW_ROOT_KEY),
                List.of(new PlacementModifier[]{RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()}));

        register(context, GIANT_CLAM_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GIANT_CLAM_KEY),
                List.of(new PlacementModifier[]{RarityFilter.onAverageOnceEvery(64), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()}));

        //register(context, GIANT_CLAM_NETHER_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GIANT_CLAM_NETHER_KEY), getNetherClamPlacement(256, null));

        register(context, GIANT_CLAM_END_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GIANT_CLAM_END_KEY),
                List.of(new PlacementModifier[]{RarityFilter.onAverageOnceEvery(512), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()}));
    }

    @SuppressWarnings("removal")
    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(DucksDelights.MOD_ID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    private static List<PlacementModifier> getNetherClamPlacement(int pRarity, @Nullable PlacementModifier pPlacement) {
        ImmutableList.Builder<PlacementModifier> builder = ImmutableList.builder();
        if (pPlacement != null) {
            builder.add(pPlacement);
        }

        if (pRarity != 0) {
            builder.add(RarityFilter.onAverageOnceEvery(pRarity));
        }

        builder.add(InSquarePlacement.spread());
        builder.add(PlacementUtils.HEIGHTMAP);
        builder.add(BiomeFilter.biome());
        return builder.build();
    }
}
