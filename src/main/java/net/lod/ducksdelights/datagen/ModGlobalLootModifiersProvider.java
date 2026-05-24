package net.lod.ducksdelights.datagen;

import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.item.ModItems;
import net.lod.ducksdelights.loot.AddItemModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, DucksDelights.MOD_ID);
    }

    @Override
    @SuppressWarnings("removal")
    protected void start() {
        add("raw_rice_from_tall_grass", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS).build(),
                LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.1F, 2F).build()}, ModItems.RAW_RICE.get()));

        add("blackberries_from_taiga_village", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/village/village_taiga_house")).build(),
                LootItemRandomChanceCondition.randomChance(0.75F).build()}, ModItems.BLACKBERRIES.get()));

        add("kibblestone_from_silverfish", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("entities/silverfish")).build(),
                LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.25F, 2F).build()}, ModItems.KIBBLESTONE.get()));

        add("haunted_metal_scrap_from_dungeon_loot", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/simple_dungeon")).build(),
                LootItemRandomChanceCondition.randomChance(0.5F).build()}, ModItems.HAUNTED_METAL_SCRAP.get()));

        add("haunted_metal_scrap_from_stronghold_crossing_loot", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/stronghold_crossing")).build(),
                LootItemRandomChanceCondition.randomChance(0.75F).build()}, ModItems.HAUNTED_METAL_SCRAP.get()));

        add("pearl_from_archaeology", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("archaeology/ocean_ruin_warm")).build(),
                LootItemRandomChanceCondition.randomChance(0.5F).build()}, ModItems.PEARL.get()));
    }
}
