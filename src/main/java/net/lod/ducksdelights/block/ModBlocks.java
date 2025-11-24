package net.lod.ducksdelights.block;

import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.block.custom.*;
import net.lod.ducksdelights.item.ModItems;
import net.lod.ducksdelights.item.custom.ArmorBlockItem;
import net.lod.ducksdelights.item.custom.ModArmorMaterials;
import net.lod.ducksdelights.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
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

    public static final RegistryObject<EmptyBarrelBlock> EMPTY_BARREL = registerArmorBlock("empty_barrel", ModArmorMaterials.BARREL, ArmorItem.Type.HELMET,
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

    public static final RegistryObject<RopeLadderBlock> ROPE_LADDER = registerBlock("rope_ladder",
            () -> new RopeLadderBlock(BlockBehaviour.Properties.copy(Blocks.LADDER)
                    .sound(SoundType.BAMBOO_WOOD)));

    public static final RegistryObject<AntiRopeLadderBlock> ANTI_ROPE_LADDER = registerBlock("anti_rope_ladder",
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

    public static final RegistryObject<DemonCoreBlock> DEMON_CORE = registerFireproofBlock("demon_core",
            () -> new DemonCoreBlock(BlockBehaviour.Properties.of()
                    .strength(50.0F, 1200.0F)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .lightLevel((state) -> 3)
                    .emissiveRendering((state, world, pos) -> state.getValue(DemonCoreBlock.POWERED))
                    .mapColor(MapColor.COLOR_BLACK)));

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

    public static final RegistryObject<ShatteredBedrockBlock> SHATTERED_BEDROCK = registerBlock("shattered_bedrock",
            () -> new ShatteredBedrockBlock(5778454, BlockBehaviour.Properties.of()
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

    public static final RegistryObject<AdderBlock> ADDER = registerBlock("adder",
            () -> new AdderBlock(BlockBehaviour.Properties.copy(Blocks.REPEATER)));






    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
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

    private static <T extends Block> RegistryObject<T> registerArmorBlock(String name, ArmorMaterial armorMaterial, ArmorItem.Type armorType,Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerArmorBlockItem(name, armorMaterial, armorType,toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerArmorBlockItem(String name, ArmorMaterial armorMaterial, ArmorItem.Type armorType,RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new ArmorBlockItem(block.get(), armorMaterial, armorType, ModSoundEvents.ARMOR_BARREL_EQUIP.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
