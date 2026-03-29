package net.lod.ducksdelights.block;

import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.block.entity.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DucksDelights.MOD_ID);

    public static final RegistryObject<BlockEntityType<DemonCoreBlockEntity>> DEMON_CORE_BE =
            BLOCK_ENTITIES.register("demon_core_block_entity", () ->
                    BlockEntityType.Builder.of(DemonCoreBlockEntity::new,
                            ModBlocks.DEMON_CORE.get()).build(null));

    public static final RegistryObject<BlockEntityType<MoonPhaseDetectorBlockEntity>> MOON_PHASE_DETECTOR_BE =
            BLOCK_ENTITIES.register("moon_phase_detector_block_entity", () ->
                    BlockEntityType.Builder.of(MoonPhaseDetectorBlockEntity::new,
                            ModBlocks.MOON_PHASE_DETECTOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlazingBarrelBlockEntity>> BLAZING_BARREL_DETECTOR_BE =
            BLOCK_ENTITIES.register("blazing_barrel_block_entity", () ->
                    BlockEntityType.Builder.of(BlazingBarrelBlockEntity::new,
                            ModBlocks.BLAZING_BARREL.get()).build(null));

    public static final RegistryObject<BlockEntityType<AdderBlockEntity>> ADDER_BE =
            BLOCK_ENTITIES.register("adder_block_entity", () ->
                    BlockEntityType.Builder.of(AdderBlockEntity::new,
                            ModBlocks.ADDER.get()).build(null));

    public static final RegistryObject<BlockEntityType<RandomizerBlockEntity>> RANDOMIZER_BE =
            BLOCK_ENTITIES.register("randomizer_block_entity", () ->
                    BlockEntityType.Builder.of(RandomizerBlockEntity::new,
                            ModBlocks.RANDOMIZER.get()).build(null));

    public static final RegistryObject<BlockEntityType<ResonatorBlockEntity>> RESONATOR_BE =
            BLOCK_ENTITIES.register("resonator_entity", () ->
                    BlockEntityType.Builder.of(ResonatorBlockEntity::new,
                            ModBlocks.RESONATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<SoulSpawnerBlockEntity>> SOUL_SPAWNER_BE =
            BLOCK_ENTITIES.register("soul_spawner_entity", () ->
                    BlockEntityType.Builder.of(SoulSpawnerBlockEntity::new,
                            ModBlocks.SOUL_SPAWNER_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<GiantClamBlockEntity>> GIANT_CLAM_BE =
            BLOCK_ENTITIES.register("giant_clam_block_entity", () ->
                    BlockEntityType.Builder.of(GiantClamBlockEntity::new,
                            ModBlocks.GIANT_CLAM_BROWN.get()).build(null));

    //public static final RegistryObject<BlockEntityType<GiantClamBlockEntity>> GIANT_CLAN_BE =
    //        BLOCK_ENTITIES.register("giant_clam", () ->
    //                BlockEntityType.Builder.of(GiantClamBlockEntity::new,
    //                        ModBlocks.GIANT_CLAM.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
