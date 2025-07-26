package net.lod.ducksdelights.item;

import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.block.ModBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DucksDelights.MOD_ID);

    public static final RegistryObject<Item> VOID_BOTTLE = ITEMS.register("void_bottle",
            () -> new Item(new Item.Properties()
                    .craftRemainder(Items.GLASS_BOTTLE)
                    .stacksTo(16)));

    public static final RegistryObject<Item> CHALK_DUST = ITEMS.register("chalk_dust",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LUMEI_BUD = ITEMS.register("lumei_bud",
            () -> new ItemNameBlockItem(ModBlocks.LUMEI_STEM.get(),new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
