package net.lod.ducksdelights.worldgen;

import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.util.ModTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_MARSHMALLOW_ROOT_SWAMP = registerKey("add_marshmallow_root_swamp");
    public static final ResourceKey<BiomeModifier> ADD_GIANT_CLAM_BROWN_WARM_OCEAN = registerKey("add_giant_clam_brown_warm_ocean");
    public static final ResourceKey<BiomeModifier> ADD_GIANT_CLAM_NETHER_SOUL_VALLEY = registerKey("add_giant_clam_nether_soul_valley");
    public static final ResourceKey<BiomeModifier> ADD_GIANT_CLAM_END = registerKey("add_giant_clam_end");
    public static final ResourceKey<BiomeModifier> DEMON_CORE_OVERWORLD_ROOM = registerKey("add_demon_core_room_overworld");
    public static final ResourceKey<BiomeModifier> DEMON_CORE_OVERWORLD_ROOM_DEEP = registerKey("add_demon_core_room_overworld_deep");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);
        //Doin great :D
        context.register(ADD_MARSHMALLOW_ROOT_SWAMP, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.MARSHMALLOW_ROOT_SPAWNABLE),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.MARSHMALLOW_ROOT_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_GIANT_CLAM_BROWN_WARM_OCEAN, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.GIANT_CLAM_SPAWNABLE),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.GIANT_CLAM_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_GIANT_CLAM_NETHER_SOUL_VALLEY, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.GIANT_CLAM_NETHER_SPAWNABLE),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.GIANT_CLAM_NETHER_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_DECORATION));

        context.register(ADD_GIANT_CLAM_END, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.GIANT_CLAM_END_SPAWNABLE),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.GIANT_CLAM_END_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(DEMON_CORE_OVERWORLD_ROOM, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.DEMON_CORE_OVERWORLD_ROOM_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_STRUCTURES));

        context.register(DEMON_CORE_OVERWORLD_ROOM_DEEP, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.DEMON_CORE_OVERWORLD_ROOM_DEEP_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_STRUCTURES));
    }

    @SuppressWarnings("removal")
    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(DucksDelights.MOD_ID, name));
    }
}
