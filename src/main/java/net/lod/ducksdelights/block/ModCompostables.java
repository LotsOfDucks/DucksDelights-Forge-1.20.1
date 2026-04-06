package net.lod.ducksdelights.block;

import net.lod.ducksdelights.item.ModItems;
import net.minecraft.world.level.block.ComposterBlock;

public class ModCompostables {
    public static void register() {
        ComposterBlock.COMPOSTABLES.put(ModItems.RAW_RICE.get().asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModItems.ONIGIRI.get().asItem(), 0.85F);
        ComposterBlock.COMPOSTABLES.put(ModItems.RAW_GOLDEN_RICE.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ModItems.GILDED_ONIGIRI.get().asItem(), 1.0F);

        ComposterBlock.COMPOSTABLES.put(ModItems.BLACKBERRIES.get().asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModItems.BLACKBERRY_PIE.get().asItem(), 1.0F);

        ComposterBlock.COMPOSTABLES.put(ModItems.MARSHMALLOW_ROOTS.get().asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModItems.MARSHMALLOW.get().asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.GIANT_MARSHMALLOW.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.WHITE_GIANT_MARSHMALLOW.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.LIGHT_GRAY_GIANT_MARSHMALLOW.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.GRAY_GIANT_MARSHMALLOW.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.BLACK_GIANT_MARSHMALLOW.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.BROWN_GIANT_MARSHMALLOW.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.RED_GIANT_MARSHMALLOW.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.ORANGE_GIANT_MARSHMALLOW.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.YELLOW_GIANT_MARSHMALLOW.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.LIME_GIANT_MARSHMALLOW.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.GREEN_GIANT_MARSHMALLOW.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.CYAN_GIANT_MARSHMALLOW.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.LIGHT_BLUE_GIANT_MARSHMALLOW.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.BLUE_GIANT_MARSHMALLOW.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.PURPLE_GIANT_MARSHMALLOW.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.MAGENTA_GIANT_MARSHMALLOW.get().asItem(), 1.0F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.PINK_GIANT_MARSHMALLOW.get().asItem(), 1.0F);
    }
}
