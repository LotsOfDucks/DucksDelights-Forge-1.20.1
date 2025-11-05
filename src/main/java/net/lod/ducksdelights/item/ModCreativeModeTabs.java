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

    public static final RegistryObject<CreativeModeTab> DUCKSDELIGHTS_ITEMS = CREATIVE_MODE_TABS.register("ducksdelights_items", () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.WHITE_RICE.get()))
            .title(Component.translatable("itemgroup.ducksdelights.items"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ModItems.BLACKBERRIES.get());
                output.accept(ModItems.BLACKBERRY_PIE.get());
                output.accept(ModItems.RAW_RICE.get());
                output.accept(ModItems.RAW_GOLDEN_RICE.get());
                output.accept(ModItems.WHITE_RICE.get());
                output.accept(ModItems.GOLDEN_RICE.get());
                output.accept(ModItems.ONIGIRI.get());
                output.accept(ModItems.CHICKEN_ONIGIRI.get());
                output.accept(ModItems.BEEF_ONIGIRI.get());
                output.accept(ModItems.PORK_ONIGIRI.get());
                output.accept(ModItems.SALMON_ONIGIRI.get());
                output.accept(ModItems.GILDED_ONIGIRI.get());
                output.accept(ModItems.KIBBLESTONE.get());
                output.accept(ModItems.PLAIN_ROCK_CANDY.get());
                output.accept(ModItems.WHITE_ROCK_CANDY.get());
                output.accept(ModItems.LIGHT_GRAY_ROCK_CANDY.get());
                output.accept(ModItems.GRAY_ROCK_CANDY.get());
                output.accept(ModItems.BLACK_ROCK_CANDY.get());
                output.accept(ModItems.BROWN_ROCK_CANDY.get());
                output.accept(ModItems.RED_ROCK_CANDY.get());
                output.accept(ModItems.ORANGE_ROCK_CANDY.get());
                output.accept(ModItems.YELLOW_ROCK_CANDY.get());
                output.accept(ModItems.LIME_ROCK_CANDY.get());
                output.accept(ModItems.GREEN_ROCK_CANDY.get());
                output.accept(ModItems.CYAN_ROCK_CANDY.get());
                output.accept(ModItems.LIGHT_BLUE_ROCK_CANDY.get());
                output.accept(ModItems.BLUE_ROCK_CANDY.get());
                output.accept(ModItems.PURPLE_ROCK_CANDY.get());
                output.accept(ModItems.MAGENTA_ROCK_CANDY.get());
                output.accept(ModItems.PINK_ROCK_CANDY.get());
            })
            .build());

    public static final RegistryObject<CreativeModeTab> DUCKSDELIGHTS_BLOCKS = CREATIVE_MODE_TABS.register("ducksdelights_blocks", () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.DEMON_CORE.get()))
            .title(Component.translatable("itemgroup.ducksdelights.blocks"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ModBlocks.EMPTY_BARREL.get());
                output.accept(ModBlocks.BLAZING_BARREL.get());
                output.accept(ModBlocks.GLOWSTONE_BARREL.get());
                output.accept(ModBlocks.GUNPOWDER_BARREL.get());
                output.accept(ModBlocks.DEMON_CORE.get());
                output.accept(ModBlocks.ROPE_LADDER.get());
                output.accept(ModBlocks.ANTI_ROPE_LADDER.get());
                output.accept(ModBlocks.SCULK_SPEAKER.get());
                output.accept(ModBlocks.MOON_PHASE_DETECTOR.get());
                output.accept(ModBlocks.PADDY_FARMLAND.get());
            })
            .build());

    public static final RegistryObject<CreativeModeTab> DUCKSDELIGHTS_MISC = CREATIVE_MODE_TABS.register("ducksdelights_misc", () -> CreativeModeTab.builder().icon(() -> new ItemStack(Blocks.BEDROCK))
            .title(Component.translatable("itemgroup.ducksdelights.misc"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ModBlocks.DEMON_CORE.get());
                output.accept(ModBlocks.SCULK_SPEAKER.get());
                output.accept(ModBlocks.MOON_PHASE_DETECTOR.get());
                output.accept(ModBlocks.PADDY_FARMLAND.get());
            })
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
