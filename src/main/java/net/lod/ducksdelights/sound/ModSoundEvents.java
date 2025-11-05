package net.lod.ducksdelights.sound;

import net.lod.ducksdelights.DucksDelights;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DucksDelights.MOD_ID);

    public static final RegistryObject<SoundEvent> DEMON_CORE_TINK = registerSoundEvents("demon_core_tink");

    public static final RegistryObject<SoundEvent> DEMON_CORE_AMBIENT = registerSoundEvents("demon_core_ambient");

    public static final RegistryObject<SoundEvent> BARREL_FILL = registerSoundEvents("barrel_fill");

    public static final RegistryObject<SoundEvent> BLAZING_BARREL_CRACKLE = registerSoundEvents("blazing_barrel_crackle");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(DucksDelights.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
