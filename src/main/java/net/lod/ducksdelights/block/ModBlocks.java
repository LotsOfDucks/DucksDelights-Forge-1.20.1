package net.lod.ducksdelights.block;

import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.block.custom.*;
import net.lod.ducksdelights.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ToFloatFunction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
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

    private static boolean neverSpawn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {return false;}

    private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return (state) -> (Boolean)state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }

    private static ToIntFunction<BlockState> explodingBlockEmission(int lightValue) {
        return (state) -> (Boolean)state.getValue(FillableBarrelBlock.EXPLODING) ? lightValue : 0;
    }

    public static final RegistryObject<EmptyBarrelBlock> EMPTY_BARREL = registerBlock("empty_barrel",
            () -> new EmptyBarrelBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)
                    .ignitedByLava()
                    .pushReaction(PushReaction.NORMAL)));

    public static final RegistryObject<ExplodingBarrelBlock> GUNPOWDER_BARREL = registerBlock("gunpowder_barrel",
            () -> new ExplodingBarrelBlock(Items.GUNPOWDER ,BlockBehaviour.Properties.copy(Blocks.BARREL)
                    .ignitedByLava()
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

    public static final RegistryObject<DemonCoreBlock> DEMON_CORE = registerBlock("demon_core",
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
