package net.lod.ducksdelights.block;

import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.block.custom.*;
import net.lod.ducksdelights.block.custom.blockstate_properties.enums.ClamTexture;
import net.lod.ducksdelights.item.ModItems;
import net.lod.ducksdelights.item.custom.ArmorBlockItem;
import net.lod.ducksdelights.item.custom.ModArmorMaterials;
import net.lod.ducksdelights.item.custom.SimpleFurnaceFuelBlockItem;
import net.lod.ducksdelights.item.custom.foods.ModFoods;
import net.lod.ducksdelights.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, DucksDelights.MOD_ID);

    private static boolean always(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
    }

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    private static Boolean neverEntity(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return false;
    }

    private static boolean neverSpawn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {return false;}

    private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return (state) -> (Boolean)state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }

    private static ToIntFunction<BlockState> filledBlockEmission(int lightValue) {
        return (state) -> (Boolean)!state.getValue(FillableBarrelBlock.EXPLODING) ? lightValue : 0;
    }

    private static ToIntFunction<BlockState> explodingBlockEmission(int lightValue) {
        return (state) -> (Boolean)state.getValue(FillableBarrelBlock.EXPLODING) ? lightValue : 0;
    }

    public static final RegistryObject<EmptyBarrelBlock> EMPTY_BARREL = registerArmorBlock("empty_barrel", ModArmorMaterials.BARREL, ArmorItem.Type.HELMET, 200,
            () -> new EmptyBarrelBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)
                    .pushReaction(PushReaction.NORMAL)));

    public static final RegistryObject<FillableBarrelBlock> GLOWSTONE_BARREL = registerBlock("glowstone_barrel",
            () -> new FillableBarrelBlock(Items.GLOWSTONE_DUST ,BlockBehaviour.Properties.copy(ModBlocks.EMPTY_BARREL.get())
                    .lightLevel(((state) ->
                            switch (state.getValue(FillableBarrelBlock.FULLNESS)) {
                                case 1 -> 1;
                                case 2 -> 2;
                                case 3 -> 3;
                                case 4 -> 4;
                                case 5 -> 5;
                                case 6 -> 6;
                                case 7 -> 7;
                                case 8 -> 8;
                                case 9 -> 9;
                                case 10 -> 10;
                                case 11 -> 11;
                                case 12 -> 12;
                                case 13 -> 13;
                                case 14 -> 14;
                                case 15 -> 15;
                                default -> 1;

            }))));

    public static final RegistryObject<BlazingBarrelBlock> BLAZING_BARREL = registerBlock("blazing_barrel",
            () -> new BlazingBarrelBlock(Items.BLAZE_POWDER , true, 1,BlockBehaviour.Properties.copy(ModBlocks.EMPTY_BARREL.get())
                    .lightLevel(((state) ->
                            switch (state.getValue(FillableBarrelBlock.FULLNESS)) {
                                case 1 -> 1;
                                case 2 -> 2;
                                case 3 -> 3;
                                case 4 -> 4;
                                case 5 -> 5;
                                case 6 -> 6;
                                case 7 -> 7;
                                case 8 -> 8;
                                case 9 -> 9;
                                case 10 -> 10;
                                case 11 -> 11;
                                case 12 -> 12;
                                case 13 -> 13;
                                case 14 -> 14;
                                case 15 -> 15;
                                default -> 1;

                            }))));

    public static final RegistryObject<ExplodingBarrelBlock> GUNPOWDER_BARREL = registerBlock("gunpowder_barrel",
            () -> new ExplodingBarrelBlock(Items.GUNPOWDER ,BlockBehaviour.Properties.copy(Blocks.BARREL)
                    .ignitedByLava()
                    .lightLevel(explodingBlockEmission(15))
                    .pushReaction(PushReaction.NORMAL)));

    public static final RegistryObject<RopeLadderBlock> ROPE_LADDER = registerSimpleFurnaceFuelBlock("rope_ladder", 200,
            () -> new RopeLadderBlock(BlockBehaviour.Properties.copy(Blocks.LADDER)
                    .sound(SoundType.BAMBOO_WOOD)));

    public static final RegistryObject<AntiRopeLadderBlock> ANTI_ROPE_LADDER = registerSimpleFurnaceFuelBlock("anti_rope_ladder", 200,
            () -> new AntiRopeLadderBlock(BlockBehaviour.Properties.copy(ModBlocks.ROPE_LADDER.get())));

    public static final RegistryObject<LampSlabBlock> REDSTONE_LAMP_SLAB = registerBlock("redstone_lamp_slab",
            () -> new LampSlabBlock(BlockBehaviour.Properties.of()
                    .lightLevel(litBlockEmission(15))
                    .strength(0.3F)
                    .sound(SoundType.GLASS)));

    public static final RegistryObject<BlackberryCropBlock> BLACKBERRY_CROP = registerBlock("blackberry_crop",
            () -> new BlackberryCropBlock(BlockBehaviour.Properties.of()
                    .strength(1.0F)
                    .requiresCorrectToolForDrops()
                    .offsetType(BlockBehaviour.OffsetType.XYZ)
                    .noCollission()
                    .pushReaction(PushReaction.DESTROY)
                    .sound(SoundType.MANGROVE_ROOTS)
                    .mapColor(MapColor.COLOR_GREEN)));

    public static final RegistryObject<RiceCropBlock> RICE_CROP = registerBlock("rice_crop",
            () -> new RiceCropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)
                    .pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<PaddyFarmlandBlock> PADDY_FARMLAND = registerBlock("paddy_farmland",
            () -> new PaddyFarmlandBlock(BlockBehaviour.Properties.of()
                    .strength(0.6F)
                    .sound(SoundType.GRAVEL)
                    .mapColor(MapColor.DIRT)
                    .isSuffocating(ModBlocks::never)
                    .isViewBlocking(ModBlocks::never)
                    .randomTicks()));

    public static final RegistryObject<MoonPhaseDetectorBlock> MOON_PHASE_DETECTOR = registerBlock("moon_phase_detector",
            () -> new MoonPhaseDetectorBlock(BlockBehaviour.Properties.copy(Blocks.DAYLIGHT_DETECTOR)));

    public static final RegistryObject<SculkSpeakerBlock> SCULK_SPEAKER = registerBlock("sculk_speaker",
            () -> new SculkSpeakerBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.SCULK)
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(1.5F)
                    .noOcclusion()));

    public static final RegistryObject<ResonatorBlock> RESONATOR = registerBlock("resonator",
            () -> new ResonatorBlock(BlockBehaviour.Properties.of()
                    .strength(0.5F)
                    .sound(SoundType.POLISHED_DEEPSLATE)
                    .mapColor(MapColor.DEEPSLATE)));

    public static final RegistryObject<SandBlock> SHATTERED_BEDROCK = registerBlock("shattered_bedrock",
            () -> new SandBlock(5778454, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .sound(SoundType.DEEPSLATE_TILES)
                    .strength(-1.0F, 3600000.0F)));

    public static final RegistryObject<SandBlock> BEDROCK_SAND = registerBlock("bedrock_enriched_sand",
            () -> new SandBlock(14406560, BlockBehaviour.Properties.copy(Blocks.SAND)));

    public static final RegistryObject<GlassBlock> REINFORCED_GLASS = registerBlock("reinforced_glass",
            () -> new GlassBlock(BlockBehaviour.Properties.of()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(1.0F, 3600000.0F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(ModBlocks::neverEntity)
                    .isRedstoneConductor(ModBlocks::never)
                    .isSuffocating(ModBlocks::never)
                    .isViewBlocking(ModBlocks::never)));

    public static final RegistryObject<IronBarsBlock> REINFORCED_GLASS_PANE = registerBlock("reinforced_glass_pane",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(1.0F, 3600000.0F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()));



    public static final RegistryObject<Block> HAUNTED_STEEL_BLOCK = registerBlock("haunted_steel_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.NETHERITE_BLOCK)));

    public static final RegistryObject<SoulSpawnerBlock> SOUL_SPAWNER_BLOCK = registerBlock("soul_spawner",
            () -> new SoulSpawnerBlock(BlockBehaviour.Properties.copy(Blocks.SPAWNER)
                    .lightLevel((state) -> 1)));

    public static final RegistryObject<DemonCoreBlock> DEMON_CORE = registerFireproofBlock("demon_core",
            () -> new DemonCoreBlock(BlockBehaviour.Properties.of()
                    .strength(50.0F, 1200.0F)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .lightLevel((state) -> 3)
                    .emissiveRendering((state, world, pos) -> state.getValue(DemonCoreBlock.POWERED))
                    .mapColor(MapColor.COLOR_BLACK)));

    public static final RegistryObject<AdderBlock> ADDER = registerBlock("adder",
            () -> new AdderBlock(BlockBehaviour.Properties.copy(Blocks.REPEATER)));

    public static final RegistryObject<RandomizerBlock> RANDOMIZER = registerBlock("randomizer",
            () -> new RandomizerBlock(BlockBehaviour.Properties.copy(Blocks.REPEATER)));

    public static final RegistryObject<Block> PEARL_BLOCK = registerBlock("pearl_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));

    public static final RegistryObject<Block> CHISELED_PEARL_BLOCK = registerBlock("chiseled_pearl_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));

    public static final RegistryObject<Block> PEARL_BRICKS = registerBlock("pearl_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));

    public static final RegistryObject<Block> MOSSY_PEARL_BRICKS = registerBlock("mossy_pearl_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));

    public static final RegistryObject<Block> CRACKED_PEARL_BRICKS = registerBlock("cracked_pearl_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SANDSTONE)));

    public static final RegistryObject<PearlLampBlock> PEARL_LAMP = registerBlock("pearl_lamp",
            () -> new PearlLampBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_LAMP)));

    public static final RegistryObject<Block> STARBLIGHT_BRIDGE = registerBlock("starblight_bridge",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> GIANT_MARSHMALLOW = registerFoodBlock("giant_marshmallow", ModFoods.GIANT_MARSHMALLOW,
            () -> new GiantMarshmallowBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .sound(SoundType.WOOL)));

    public static final RegistryObject<Block> WHITE_GIANT_MARSHMALLOW = registerFoodBlock("white_giant_marshmallow", ModFoods.GIANT_MARSHMALLOW,
            () -> new GiantMarshmallowBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .sound(SoundType.WOOL)));

    public static final RegistryObject<Block> LIGHT_GRAY_GIANT_MARSHMALLOW = registerFoodBlock("light_gray_giant_marshmallow", ModFoods.GIANT_MARSHMALLOW,
            () -> new GiantMarshmallowBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .sound(SoundType.WOOL)));

    public static final RegistryObject<Block> GRAY_GIANT_MARSHMALLOW = registerFoodBlock("gray_giant_marshmallow", ModFoods.GIANT_MARSHMALLOW,
            () -> new GiantMarshmallowBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .sound(SoundType.WOOL)));

    public static final RegistryObject<Block> BLACK_GIANT_MARSHMALLOW = registerFoodBlock("black_giant_marshmallow", ModFoods.GIANT_MARSHMALLOW,
            () -> new GiantMarshmallowBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .sound(SoundType.WOOL)));

    public static final RegistryObject<Block> BROWN_GIANT_MARSHMALLOW = registerFoodBlock("brown_giant_marshmallow", ModFoods.GIANT_MARSHMALLOW,
            () -> new GiantMarshmallowBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .sound(SoundType.WOOL)));

    public static final RegistryObject<Block> RED_GIANT_MARSHMALLOW = registerFoodBlock("red_giant_marshmallow", ModFoods.GIANT_MARSHMALLOW,
            () -> new GiantMarshmallowBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .sound(SoundType.WOOL)));

    public static final RegistryObject<Block> ORANGE_GIANT_MARSHMALLOW = registerFoodBlock("orange_giant_marshmallow", ModFoods.GIANT_MARSHMALLOW,
            () -> new GiantMarshmallowBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .sound(SoundType.WOOL)));

    public static final RegistryObject<Block> YELLOW_GIANT_MARSHMALLOW = registerFoodBlock("yellow_giant_marshmallow", ModFoods.GIANT_MARSHMALLOW,
            () -> new GiantMarshmallowBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .sound(SoundType.WOOL)));

    public static final RegistryObject<Block> LIME_GIANT_MARSHMALLOW = registerFoodBlock("lime_giant_marshmallow", ModFoods.GIANT_MARSHMALLOW,
            () -> new GiantMarshmallowBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .sound(SoundType.WOOL)));

    public static final RegistryObject<Block> GREEN_GIANT_MARSHMALLOW = registerFoodBlock("green_giant_marshmallow", ModFoods.GIANT_MARSHMALLOW,
            () -> new GiantMarshmallowBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .sound(SoundType.WOOL)));

    public static final RegistryObject<Block> CYAN_GIANT_MARSHMALLOW = registerFoodBlock("cyan_giant_marshmallow", ModFoods.GIANT_MARSHMALLOW,
            () -> new GiantMarshmallowBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .sound(SoundType.WOOL)));

    public static final RegistryObject<Block> LIGHT_BLUE_GIANT_MARSHMALLOW = registerFoodBlock("light_blue_giant_marshmallow", ModFoods.GIANT_MARSHMALLOW,
            () -> new GiantMarshmallowBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .sound(SoundType.WOOL)));

    public static final RegistryObject<Block> BLUE_GIANT_MARSHMALLOW = registerFoodBlock("blue_giant_marshmallow", ModFoods.GIANT_MARSHMALLOW,
            () -> new GiantMarshmallowBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .sound(SoundType.WOOL)));

    public static final RegistryObject<Block> PURPLE_GIANT_MARSHMALLOW = registerFoodBlock("purple_giant_marshmallow", ModFoods.GIANT_MARSHMALLOW,
            () -> new GiantMarshmallowBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .sound(SoundType.WOOL)));

    public static final RegistryObject<Block> MAGENTA_GIANT_MARSHMALLOW = registerFoodBlock("magenta_giant_marshmallow", ModFoods.GIANT_MARSHMALLOW,
            () -> new GiantMarshmallowBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .sound(SoundType.WOOL)));

    public static final RegistryObject<Block> PINK_GIANT_MARSHMALLOW = registerFoodBlock("pink_giant_marshmallow", ModFoods.GIANT_MARSHMALLOW,
            () -> new GiantMarshmallowBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .sound(SoundType.WOOL)));

    public static final RegistryObject<GiantClamBlock> GIANT_CLAM_BROWN = registerBlock("giant_clam_brown",
            () -> new GiantClamBlock(BlockBehaviour.Properties.copy(Blocks.BRAIN_CORAL_BLOCK)
                    .noOcclusion()));






    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> registerSimpleFurnaceFuelBlock(String name , int burnTime, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerSimpleFurnaceFuelBlockItem(name , burnTime, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerSimpleFurnaceFuelBlockItem(String name, int burnTime, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new SimpleFurnaceFuelBlockItem(block.get(), burnTime, new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> registerFireproofBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerFireproofBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerFireproofBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()
                .fireResistant()
                .rarity(Rarity.EPIC)));
    }

    private static <T extends Block> RegistryObject<T> registerArmorBlock(String name, ArmorMaterial armorMaterial, ArmorItem.Type armorType, int burnTime ,Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerArmorBlockItem(name, armorMaterial, armorType, burnTime, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerArmorBlockItem(String name, ArmorMaterial armorMaterial, ArmorItem.Type armorType, int burnTime, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new ArmorBlockItem(block.get(), armorMaterial, armorType, ModSoundEvents.ARMOR_BARREL_EQUIP.get(), burnTime, new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> registerFoodBlock(String name, FoodProperties foodProperties, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerFoodBlockItem(name, foodProperties, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerFoodBlockItem(String name, FoodProperties foodProperties, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()
                .food(foodProperties)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
