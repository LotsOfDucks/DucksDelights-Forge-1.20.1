package net.lod.ducksdelights.worldgen.features;

import net.lod.ducksdelights.DucksDelights;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, DucksDelights.MOD_ID);



    public static final RegistryObject<GiantClamFeature> GIANT_CLAM_FEATURE = registerFeature("giant_clam_feature",
            () -> new GiantClamFeature(CountConfiguration.CODEC));

    //public static final RegistryObject<GiantClamNetherFeature> GIANT_CLAM_NETHER_FEATURE = registerFeature("giant_clam_nether_feature",
    //        () -> new GiantClamNetherFeature(CountConfiguration.CODEC));

    public static final RegistryObject<GiantClamEndFeature> GIANT_CLAM_END_FEATURE = registerFeature("giant_clam_end_feature",
            () -> new GiantClamEndFeature(RandomPatchConfiguration.CODEC));


    private static <T extends Feature<?>> RegistryObject<T> registerFeature(String name, Supplier<T> feature) {
        RegistryObject<T> toReturn = FEATURES.register(name, feature);
        return toReturn;
    }

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
