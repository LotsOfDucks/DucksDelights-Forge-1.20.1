package net.lod.ducksdelights.block.entity;

import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.block.ModBlocks;
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

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
