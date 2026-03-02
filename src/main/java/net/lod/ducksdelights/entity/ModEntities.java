package net.lod.ducksdelights.entity;

import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.entity.custom.DynamicFallingBlockEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DucksDelights.MOD_ID);

    public static final RegistryObject<EntityType<DynamicFallingBlockEntity>> DYNAMIC_FALLING_BLOCK =
            ENTITY_TYPES.register("dynamic_falling_block", () -> EntityType.Builder.of(DynamicFallingBlockEntity::new, MobCategory.MISC)
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(10)
                    .updateInterval(20).build("dynamic_falling_block"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
