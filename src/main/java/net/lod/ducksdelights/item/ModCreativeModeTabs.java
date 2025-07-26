package net.lod.ducksdelights.item;

import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DucksDelights.MOD_ID);

    public static final RegistryObject<CreativeModeTab> VOIDBREAK_ITEMS = CREATIVE_MODE_TABS.register("voidbreak_items", () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.VOID_BOTTLE.get()))
            .title(Component.translatable("creativetab.voidbreak_items"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ModItems.VOID_BOTTLE.get());
                output.accept(ModItems.CHALK_DUST.get());
                output.accept(ModItems.LUMEI_BUD.get());
            })
            .build());

    public static final RegistryObject<CreativeModeTab> VOIDBREAK_NATURAL_BLOCKS = CREATIVE_MODE_TABS.register("voidbreak_natural_blocks", () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.DEEPSLATE))
            .title(Component.translatable("creativetab.voidbreak_nat_blocks"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ModBlocks.DEMON_CORE_BLOCK.get());
                output.accept(ModBlocks.GNEISS.get());
                output.accept(ModBlocks.CHALK.get());
                output.accept(ModBlocks.CHALKY_DIRT.get());
                output.accept(ModBlocks.CHALKY_GRASS.get());
                output.accept(ModBlocks.CHALKY_BRUSH.get());
                output.accept(ModBlocks.LUMEI_MELON.get());
                output.accept(ModBlocks.BLEEDING_STONE.get());
                output.accept(ModBlocks.BLEEDING_DEEPSLATE.get());
            })
            .build());

    public static final RegistryObject<CreativeModeTab> VOIDBREAK_MISC = CREATIVE_MODE_TABS.register("voidbreak_misc", () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.BEDROCK))
            .title(Component.translatable("creativetab.voidbreak_misc"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ModBlocks.CHALK_DUST_BLOCK.get());
            })
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
