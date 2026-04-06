package net.lod.ducksdelights.item;

import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DucksDelights.MOD_ID);

    public static final RegistryObject<CreativeModeTab> DUCKSDELIGHTS_BLOCKS = CREATIVE_MODE_TABS.register("ducksdelights_blocks", () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.DEMON_CORE.get()))
            .title(Component.translatable("itemgroup.ducksdelights.blocks"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ModBlocks.HAUNTED_STEEL_BLOCK.get());
                output.accept(ModBlocks.DEMON_CORE.get());

                output.accept(ModBlocks.PEARL_BLOCK.get());
                output.accept(ModBlocks.CHISELED_PEARL_BLOCK.get());
                output.accept(ModBlocks.PEARL_BRICKS.get());
                output.accept(ModBlocks.MOSSY_PEARL_BRICKS.get());
                output.accept(ModBlocks.CRACKED_PEARL_BRICKS.get());
                output.accept(ModBlocks.PEARL_LAMP.get());

                output.accept(ModBlocks.GIANT_MARSHMALLOW.get());

                output.accept(ModBlocks.ROPE_LADDER.get());
                output.accept(ModBlocks.ANTI_ROPE_LADDER.get());

                output.accept(ModBlocks.EMPTY_BARREL.get());
                output.accept(ModBlocks.BLAZING_BARREL.get());
                output.accept(ModBlocks.GLOWSTONE_BARREL.get());
                output.accept(ModBlocks.GUNPOWDER_BARREL.get());

                output.accept(ModBlocks.SHATTERED_BEDROCK.get());
                output.accept(ModBlocks.BEDROCK_SAND.get());
                output.accept(ModBlocks.REINFORCED_GLASS.get());
                output.accept(ModBlocks.REINFORCED_GLASS_PANE.get());
                output.accept(ModBlocks.SOUL_SPAWNER_BLOCK.get());

                output.accept(ModBlocks.PADDY_FARMLAND.get());

                output.accept(ModBlocks.GIANT_MARSHMALLOW.get());
                output.accept(ModBlocks.WHITE_GIANT_MARSHMALLOW.get());
                output.accept(ModBlocks.LIGHT_GRAY_GIANT_MARSHMALLOW.get());
                output.accept(ModBlocks.GRAY_GIANT_MARSHMALLOW.get());
                output.accept(ModBlocks.BLACK_GIANT_MARSHMALLOW.get());
                output.accept(ModBlocks.BROWN_GIANT_MARSHMALLOW.get());
                output.accept(ModBlocks.RED_GIANT_MARSHMALLOW.get());
                output.accept(ModBlocks.ORANGE_GIANT_MARSHMALLOW.get());
                output.accept(ModBlocks.YELLOW_GIANT_MARSHMALLOW.get());
                output.accept(ModBlocks.LIME_GIANT_MARSHMALLOW.get());
                output.accept(ModBlocks.GREEN_GIANT_MARSHMALLOW.get());
                output.accept(ModBlocks.CYAN_GIANT_MARSHMALLOW.get());
                output.accept(ModBlocks.LIGHT_BLUE_GIANT_MARSHMALLOW.get());
                output.accept(ModBlocks.BLUE_GIANT_MARSHMALLOW.get());
                output.accept(ModBlocks.PURPLE_GIANT_MARSHMALLOW.get());
                output.accept(ModBlocks.MAGENTA_GIANT_MARSHMALLOW.get());
                output.accept(ModBlocks.PINK_GIANT_MARSHMALLOW.get());

            })
            .build());

    public static final RegistryObject<CreativeModeTab> DUCKSDELIGHTS_ITEMS = CREATIVE_MODE_TABS.register("ducksdelights_items", () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.PEARL.get()))
            .title(Component.translatable("itemgroup.ducksdelights.items"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ModItems.HOME_POTION.get());
                output.accept(ModItems.RECOVERY_POTION.get());
                output.accept(ModItems.WORMHOLE_POTION.get());

                output.accept(ModItems.PEARL.get());
                output.accept(ModItems.PEARL_SWORD.get());
                output.accept(ModItems.PEARL_PICKAXE.get());
                output.accept(ModItems.PEARL_AXE.get());
                output.accept(ModItems.PEARL_SHOVEL.get());
                output.accept(ModItems.PEARL_HOE.get());
                output.accept(ModItems.PEARLED_APPLE.get());

                output.accept(ModItems.HAUNTED_METAL_SCRAP.get());
                output.accept(ModItems.HAUNTED_STEEL_NUGGET.get());
                output.accept(ModItems.HAUNTED_STEEL_INGOT.get());
                output.accept(ModItems.HAUNTED_STEEL_SWORD.get());
                output.accept(ModItems.HAUNTED_STEEL_PICKAXE.get());
                output.accept(ModItems.HAUNTED_STEEL_AXE.get());
                output.accept(ModItems.HAUNTED_STEEL_SHOVEL.get());
                output.accept(ModItems.HAUNTED_STEEL_HOE.get());
                output.accept(ModItems.SOUL_CAGE.get());

                output.accept(ModItems.BEDROCK_CHIPS.get());
                output.accept(ModItems.FISSILE_CHUNK.get());


            })
            .build());

    public static final RegistryObject<CreativeModeTab> DUCKSDELIGHTS_FOOD = CREATIVE_MODE_TABS.register("ducksdelights_food", () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.RAW_RICE.get()))
            .title(Component.translatable("itemgroup.ducksdelights.food"))
            .displayItems((itemDisplayParameters, output) -> {
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

                output.accept(ModBlocks.WILD_MARSHMALLOW_ROOT.get());
                output.accept(ModItems.MARSHMALLOW_ROOTS.get());
                output.accept(ModBlocks.ALTHAEA.get());
                output.accept(ModItems.MARSHMALLOW.get());
                output.accept(ModItems.MARSHMALLOW_STICK.get());
                output.accept(ModItems.MARSHMALLOW_CREME.get());
                output.accept(ModBlocks.GIANT_MARSHMALLOW.get());

                output.accept(ModItems.BLACKBERRIES.get());
                output.accept(ModItems.BLACKBERRY_PIE.get());


                output.accept(ModItems.COLD_FROG_LEG.get());
                output.accept(ModItems.WARM_FROG_LEG.get());
                output.accept(ModItems.TEMPERATE_FROG_LEG.get());

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

    public static final RegistryObject<CreativeModeTab> DUCKSDELIGHTS_REDSTONE = CREATIVE_MODE_TABS.register("ducksdelights_redstone", () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.ADDER.get()))
            .title(Component.translatable("itemgroup.ducksdelights.redstone"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ModBlocks.ADDER.get());
                output.accept(ModBlocks.RANDOMIZER.get());
                output.accept(ModBlocks.RESONATOR.get());

                output.accept(ModBlocks.ENTITY_DETECTOR.get());
                output.accept(ModBlocks.MOB_DETECTOR.get());
                output.accept(ModBlocks.PLAYER_DETECTOR.get());

                output.accept(ModBlocks.SCULK_SPEAKER.get());
                output.accept(ModBlocks.MOON_PHASE_DETECTOR.get());
                output.accept(ModBlocks.REDSTONE_LAMP_SLAB.get());

                output.accept(ModBlocks.PEARL_LAMP.get());

                output.accept(ModBlocks.EMPTY_BARREL.get());
                output.accept(ModBlocks.BLAZING_BARREL.get());
                output.accept(ModBlocks.GLOWSTONE_BARREL.get());
                output.accept(ModBlocks.GUNPOWDER_BARREL.get());

                output.accept(ModBlocks.DEMON_CORE.get());

                output.accept(ModBlocks.GIANT_MARSHMALLOW.get());

                output.accept(ModBlocks.ROPE_LADDER.get());
                output.accept(ModBlocks.ANTI_ROPE_LADDER.get());


            })
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
