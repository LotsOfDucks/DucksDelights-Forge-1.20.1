package net.lod.ducksdelights.item;

import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.item.custom.PearlSwordItem;
import net.lod.ducksdelights.damage.ModDamageTypes;
import net.lod.ducksdelights.item.custom.*;
import net.lod.ducksdelights.item.custom.foods.ModFoods;
import net.lod.ducksdelights.item.custom.PearlShovelItem;
import net.lod.ducksdelights.item.custom.tiers.ModTiers;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DucksDelights.MOD_ID);

    public static final RegistryObject<Item> HAUNTED_METAL_SCRAP = ITEMS.register("haunted_metal_scrap",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> HAUNTED_STEEL_NUGGET = ITEMS.register("haunted_steel_nugget",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> HAUNTED_STEEL_INGOT = ITEMS.register("haunted_steel_ingot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> HAUNTED_STEEL_SWORD = ITEMS.register("haunted_steel_sword",
            () -> new SwordItem(ModTiers.HAUNTED_STEEL, 3, -2.4F, new Item.Properties()
                    .setNoRepair()));

    public static final RegistryObject<Item> HAUNTED_STEEL_SHOVEL = ITEMS.register("haunted_steel_shovel",
            () -> new ShovelItem(ModTiers.HAUNTED_STEEL, 2, -3.0F, new Item.Properties()));

    public static final RegistryObject<Item> HAUNTED_STEEL_PICKAXE = ITEMS.register("haunted_steel_pickaxe",
            () -> new PickaxeItem(ModTiers.HAUNTED_STEEL, 2, -2.8F, new Item.Properties()));

    public static final RegistryObject<Item> HAUNTED_STEEL_AXE = ITEMS.register("haunted_steel_axe",
            () -> new AxeItem(ModTiers.HAUNTED_STEEL, 6.5F, -3.0F, new Item.Properties()));

    public static final RegistryObject<Item> HAUNTED_STEEL_HOE = ITEMS.register("haunted_steel_hoe",
            () -> new HoeItem(ModTiers.HAUNTED_STEEL, -2, -3.0F, new Item.Properties()));



    public static final RegistryObject<Item> SOUL_CAGE = ITEMS.register("soul_cage",
            () -> new SoulCageItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<BedrockChipsItem> BEDROCK_CHIPS = ITEMS.register("bedrock_chips",
            () -> new BedrockChipsItem(new Item.Properties()
                    .food(ModFoods.BEDROCK_CHIPS)));

    public static final RegistryObject<RadioactiveItem> FISSILE_CHUNK = ITEMS.register("fissile_chunk",
            () -> new RadioactiveItem(new Item.Properties(), 200000, ModDamageTypes.FISSION));

    public static final RegistryObject<RadioactiveItem> STARBLIGHT_SHARD = ITEMS.register("starblight_shard",
            () -> new RadioactiveItem(new Item.Properties(), 200000, ModDamageTypes.FISSION));



    public static final RegistryObject<Item> BLACKBERRIES = ITEMS.register("blackberries",
            () -> new ItemNameBlockItem(ModBlocks.BLACKBERRY_CROP.get(),new Item.Properties()
                    .food(ModFoods.BLACKBERRY)));

    public static final RegistryObject<Item> BLACKBERRY_PIE = ITEMS.register("blackberry_pie",
            () -> new Item(new Item.Properties()
                    .food(ModFoods.BLACKBERRY_PIE)));

    public static final RegistryObject<Item> COLD_FROG_LEG = ITEMS.register("cold_frog_leg",
            () -> new Item(new Item.Properties()
                    .food(ModFoods.FROG_LEG)));

    public static final RegistryObject<Item> WARM_FROG_LEG = ITEMS.register("warm_frog_leg",
            () -> new Item(new Item.Properties()
                    .food(ModFoods.FROG_LEG)));

    public static final RegistryObject<Item> TEMPERATE_FROG_LEG = ITEMS.register("temperate_frog_leg",
            () -> new Item(new Item.Properties()
                    .food(ModFoods.FROG_LEG)));




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

    public static final RegistryObject<Item> MARSHMALLOW_ROOTS = ITEMS.register("marshmallow_roots",
            () -> new ItemNameBlockItem(ModBlocks.MARSHMALLOW_ROOT_CROP.get(),new Item.Properties()));

    public static final RegistryObject<Item> MARSHMALLOW = ITEMS.register("marshmallow",
            () -> new Item(new Item.Properties()
                    .food(ModFoods.MARSHMALLOW)));

    public static final RegistryObject<Item> MARSHMALLOW_STICK = ITEMS.register("marshmallow_stick",
            () -> new RemainderItem(new Item.Properties()
                    .food(ModFoods.MARSHMALLOW), Items.STICK));

    public static final RegistryObject<Item> MARSHMALLOW_CREME = ITEMS.register("marshmallow_creme",
            () -> new MarshmallowCremeItem(new Item.Properties()
                    .food(ModFoods.MARSHMALLOW).craftRemainder(Items.BOWL), Items.BOWL));

    public static final RegistryObject<Item> HOME_POTION = ITEMS.register("home_potion",
            () -> new HomePotionItem(new Item.Properties()
                    .stacksTo(1)));

    public static final RegistryObject<Item> RECOVERY_POTION = ITEMS.register("recovery_potion",
            () -> new RecoveryPotionItem(new Item.Properties()
                    .stacksTo(1)));

    public static final RegistryObject<Item> WORMHOLE_POTION = ITEMS.register("wormhole_potion",
            () -> new WormholePotionItem(new Item.Properties()
                    .stacksTo(16)));

    public static final RegistryObject<Item> PEARL = ITEMS.register("pearl",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PEARLED_APPLE = ITEMS.register("pearled_apple",
            () -> new Item(new Item.Properties()
                    .food(ModFoods.PEARLED_APPLE)
                    .rarity(Rarity.RARE)));

    public static final RegistryObject<Item> PEARL_SWORD = ITEMS.register("pearl_sword",
            () -> new PearlSwordItem(ModTiers.PEARL, 3, -2.4F, new Item.Properties()
                    .setNoRepair()));

    public static final RegistryObject<Item> PEARL_SHOVEL = ITEMS.register("pearl_shovel",
            () -> new PearlShovelItem(ModTiers.PEARL, 1.5F, -3.0F, new Item.Properties()));

    public static final RegistryObject<Item> PEARL_PICKAXE = ITEMS.register("pearl_pickaxe",
            () -> new PearlPickaxeItem(ModTiers.PEARL, 1, -2.8F, new Item.Properties()));

    public static final RegistryObject<Item> PEARL_AXE = ITEMS.register("pearl_axe",
            () -> new PearlAxeItem(ModTiers.PEARL, 6, -3.0F, new Item.Properties()));

    public static final RegistryObject<Item> PEARL_HOE = ITEMS.register("pearl_hoe",
            () -> new PearlHoeItem(ModTiers.PEARL, -2, -3.0F, new Item.Properties()));





    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
