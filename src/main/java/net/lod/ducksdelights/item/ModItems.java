package net.lod.ducksdelights.item;

import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.damage.ModDamageTypes;
import net.lod.ducksdelights.item.custom.*;
import net.lod.ducksdelights.item.custom.foods.ModFoods;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DucksDelights.MOD_ID);

    public static final RegistryObject<Item> SOUL_CAGE_EMPTY = ITEMS.register("soul_cage_empty",
            () -> new EmptySoulCageItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> SOUL_CAGE = ITEMS.register("soul_cage",
            () -> new SoulCageItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<BedrockChipsItem> BEDROCK_CHIPS = ITEMS.register("bedrock_chips",
            () -> new BedrockChipsItem(new Item.Properties()
                    .food(ModFoods.BEDROCK_CHIPS)));

    public static final RegistryObject<RadioactiveItem> FISSILE_SHARD = ITEMS.register("fissile_shard",
            () -> new RadioactiveItem(new Item.Properties(), ModDamageTypes.FISSION));

    public static final RegistryObject<Item> BLACKBERRIES = ITEMS.register("blackberries",
            () -> new ItemNameBlockItem(ModBlocks.BLACKBERRY_CROP.get(),new Item.Properties()
                    .food(ModFoods.BLACKBERRY)));

    public static final RegistryObject<Item> BLACKBERRY_PIE = ITEMS.register("blackberry_pie",
            () -> new Item(new Item.Properties()
                    .food(ModFoods.BLACKBERRY_PIE)));

    public static final RegistryObject<Item> RAW_RICE = ITEMS.register("raw_rice",
            () -> new ItemNameBlockItem(ModBlocks.RICE_CROP.get(),new Item.Properties()));

    public static final RegistryObject<Item> RAW_GOLDEN_RICE = ITEMS.register("raw_golden_rice",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WHITE_RICE = ITEMS.register("white_rice",
            () -> new BowlFoodItem(new Item.Properties()
                    .food(ModFoods.WHITE_RICE)
                    .craftRemainder(Items.BOWL)));

    public static final RegistryObject<Item> GOLDEN_RICE = ITEMS.register("golden_rice",
            () -> new BowlFoodItem(new Item.Properties()
                    .food(ModFoods.GOLDEN_RICE)
                    .craftRemainder(Items.BOWL)));

    public static final RegistryObject<Item> ONIGIRI = ITEMS.register("onigiri",
            () -> new Item(new Item.Properties()
                    .food(ModFoods.ONIGIRI)));

    public static final RegistryObject<Item> CHICKEN_ONIGIRI = ITEMS.register("chicken_onigiri",
            () -> new Item(new Item.Properties()
                    .food(ModFoods.CHICKEN_ONIGIRI)));

    public static final RegistryObject<Item> BEEF_ONIGIRI = ITEMS.register("beef_onigiri",
            () -> new Item(new Item.Properties()
                    .food(ModFoods.BEEF_ONIGIRI)));

    public static final RegistryObject<Item> PORK_ONIGIRI = ITEMS.register("pork_onigiri",
            () -> new Item(new Item.Properties()
                    .food(ModFoods.PORK_ONIGIRI)));

    public static final RegistryObject<Item> SALMON_ONIGIRI = ITEMS.register("salmon_onigiri",
            () -> new Item(new Item.Properties()
                    .food(ModFoods.SALMON_ONIGIRI)));

    public static final RegistryObject<Item> GILDED_ONIGIRI = ITEMS.register("gilded_onigiri",
            () -> new Item(new Item.Properties()
                    .food(ModFoods.GILDED_ONIGIRI)));

    public static final RegistryObject<Item> KIBBLESTONE = ITEMS.register("kibblestone",
            () -> new Item(new Item.Properties()
                    .food(ModFoods.KIBBLESTONE)));

    public static final RegistryObject<Item> PLAIN_ROCK_CANDY = ITEMS.register("plain_rock_candy",
            () -> new RemainderItem(new Item.Properties()
                    .food(ModFoods.ROCK_CANDY), Items.STICK));

    public static final RegistryObject<Item> WHITE_ROCK_CANDY = ITEMS.register("white_rock_candy",
            () -> new RemainderItem(new Item.Properties()
                    .food(ModFoods.ROCK_CANDY), Items.STICK));

    public static final RegistryObject<Item> LIGHT_GRAY_ROCK_CANDY = ITEMS.register("light_gray_rock_candy",
            () -> new RemainderItem(new Item.Properties()
                    .food(ModFoods.ROCK_CANDY), Items.STICK));

    public static final RegistryObject<Item> GRAY_ROCK_CANDY = ITEMS.register("gray_rock_candy",
            () -> new RemainderItem(new Item.Properties()
                    .food(ModFoods.ROCK_CANDY), Items.STICK));

    public static final RegistryObject<Item> BLACK_ROCK_CANDY = ITEMS.register("black_rock_candy",
            () -> new RemainderItem(new Item.Properties()
                    .food(ModFoods.ROCK_CANDY), Items.STICK));

    public static final RegistryObject<Item> BROWN_ROCK_CANDY = ITEMS.register("brown_rock_candy",
            () -> new RemainderItem(new Item.Properties()
                    .food(ModFoods.ROCK_CANDY), Items.STICK));

    public static final RegistryObject<Item> RED_ROCK_CANDY = ITEMS.register("red_rock_candy",
            () -> new RemainderItem(new Item.Properties()
                    .food(ModFoods.ROCK_CANDY), Items.STICK));

    public static final RegistryObject<Item> ORANGE_ROCK_CANDY = ITEMS.register("orange_rock_candy",
            () -> new RemainderItem(new Item.Properties()
                    .food(ModFoods.ROCK_CANDY), Items.STICK));

    public static final RegistryObject<Item> YELLOW_ROCK_CANDY = ITEMS.register("yellow_rock_candy",
            () -> new RemainderItem(new Item.Properties()
                    .food(ModFoods.ROCK_CANDY), Items.STICK));

    public static final RegistryObject<Item> LIME_ROCK_CANDY = ITEMS.register("lime_rock_candy",
            () -> new RemainderItem(new Item.Properties()
                    .food(ModFoods.ROCK_CANDY), Items.STICK));

    public static final RegistryObject<Item> GREEN_ROCK_CANDY = ITEMS.register("green_rock_candy",
            () -> new RemainderItem(new Item.Properties()
                    .food(ModFoods.ROCK_CANDY), Items.STICK));

    public static final RegistryObject<Item> CYAN_ROCK_CANDY = ITEMS.register("cyan_rock_candy",
            () -> new RemainderItem(new Item.Properties()
                    .food(ModFoods.ROCK_CANDY), Items.STICK));

    public static final RegistryObject<Item> LIGHT_BLUE_ROCK_CANDY = ITEMS.register("light_blue_rock_candy",
            () -> new RemainderItem(new Item.Properties()
                    .food(ModFoods.ROCK_CANDY), Items.STICK));

    public static final RegistryObject<Item> BLUE_ROCK_CANDY = ITEMS.register("blue_rock_candy",
            () -> new RemainderItem(new Item.Properties()
                    .food(ModFoods.ROCK_CANDY), Items.STICK));

    public static final RegistryObject<Item> PURPLE_ROCK_CANDY = ITEMS.register("purple_rock_candy",
            () -> new RemainderItem(new Item.Properties()
                    .food(ModFoods.ROCK_CANDY), Items.STICK));

    public static final RegistryObject<Item> MAGENTA_ROCK_CANDY = ITEMS.register("magenta_rock_candy",
            () -> new RemainderItem(new Item.Properties()
                    .food(ModFoods.ROCK_CANDY), Items.STICK));

    public static final RegistryObject<Item> PINK_ROCK_CANDY = ITEMS.register("pink_rock_candy",
            () -> new RemainderItem(new Item.Properties()
                    .food(ModFoods.ROCK_CANDY), Items.STICK));





    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
