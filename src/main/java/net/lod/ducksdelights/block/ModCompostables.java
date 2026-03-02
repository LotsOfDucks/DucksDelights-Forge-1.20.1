package net.lod.ducksdelights.block;

import net.lod.ducksdelights.item.ModItems;
import net.minecraft.world.level.block.ComposterBlock;

public class ModCompostables {
    public static void register() {
        ComposterBlock.COMPOSTABLES.put(ModItems.RAW_RICE.get().asItem(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModItems.RAW_GOLDEN_RICE.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ModItems.BLACKBERRIES.get().asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModItems.BLACKBERRY_PIE.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ModItems.GILDED_ONIGIRI.get().asItem(), 1.0F);
    }
}
