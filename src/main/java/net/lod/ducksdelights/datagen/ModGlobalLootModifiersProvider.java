package net.lod.ducksdelights.datagen;

import net.lod.ducksdelights.DucksDelights;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, DucksDelights.MOD_ID);
    }

    @Override
    protected void start() {

    }
}
