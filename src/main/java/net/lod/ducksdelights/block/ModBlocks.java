package net.lod.ducksdelights.block;

import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.block.custom.*;
import net.lod.ducksdelights.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, DucksDelights.MOD_ID);

    public static final RegistryObject<SyncedRedstoneBlock> SYNCED_REDSTONE_BLOCK = registerBlock("synced_redstone_block",
            () -> new SyncedRedstoneBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)));

    public static final RegistryObject<DemonCoreBlock> DEMON_CORE_BLOCK = registerBlock("demon_core",
            () -> new DemonCoreBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK)));

    public static final RegistryObject<Block> CHALK = registerBlock("chalk",
            () -> new Block(BlockBehaviour.Properties.of()
                    .sound(SoundType.CALCITE)
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .instrument(NoteBlockInstrument.SNARE)
                    .requiresCorrectToolForDrops()
                    .strength(1.0F, 2.5F)));

    public static final RegistryObject<ChalkDustBlock> CHALK_DUST_BLOCK = registerBlock("chalk_dust_block",
            () -> new ChalkDustBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.SAND)
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(0.5F)));

    public static final RegistryObject<Block> CHALKY_DIRT = registerBlock("chalky_dirt",
            () -> new Block(BlockBehaviour.Properties.of()
                    .sound(SoundType.GRAVEL)
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(0.5F)));

    public static final RegistryObject<ChalkyGrassBlock> CHALKY_GRASS = registerBlock("chalky_grass",
            () -> new ChalkyGrassBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.GRASS)
                    .mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(0.6F)
                    .randomTicks()));

    public static final RegistryObject<TallGrassBlock> CHALKY_BRUSH = registerBlock("chalky_brush",
            () -> new TallGrassBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_YELLOW)
                    .replaceable()
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XYZ)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> GNEISS = registerBlock("gneiss",
            () -> new Block(BlockBehaviour.Properties.of()
                    .sound(SoundType.STONE)
                    .mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 6.5F)));

    public static final RegistryObject<Block> BLEEDING_STONE = registerBlock("bleeding_stone",
            () -> new BleedingStoneBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.STONE)
                    .mapColor(MapColor.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(1.0F, 3.0F)));

    public static final RegistryObject<Block> BLEEDING_DEEPSLATE = registerBlock("bleeding_deepslate",
            () -> new BleedingDeepslateBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.DEEPSLATE)
                    .mapColor(MapColor.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.5F, 3.0F)));

    public static final RegistryObject<Block> LUMEI_STEM = registerBlock("lumei_stem",
            () -> new CeilingStemBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)
                    .pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> LUMEI_FRUIT = registerBlock("lumei_fruit",
            () -> new HangingMelonBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .randomTicks()
                    .strength(2.0F)
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> LUMEI_MELON = registerBlock("lumei_melon",
            () -> new FallingMelonBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(1.0F)
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY)));




    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
