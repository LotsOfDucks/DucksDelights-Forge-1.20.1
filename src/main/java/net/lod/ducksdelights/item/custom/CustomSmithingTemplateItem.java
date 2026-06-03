package net.lod.ducksdelights.item.custom;

import net.lod.ducksdelights.DucksDelights;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;
import org.intellij.lang.annotations.Identifier;

import java.util.List;

public class CustomSmithingTemplateItem extends SmithingTemplateItem {
    private static final ChatFormatting TITLE_FORMAT;
    private static final ChatFormatting DESCRIPTION_FORMAT;

    private static final ResourceLocation EMPTY_SLOT_HELMET;
    private static final ResourceLocation EMPTY_SLOT_CHESTPLATE;
    private static final ResourceLocation EMPTY_SLOT_LEGGINGS;
    private static final ResourceLocation EMPTY_SLOT_BOOTS;
    private static final ResourceLocation EMPTY_SLOT_HOE;
    private static final ResourceLocation EMPTY_SLOT_AXE;
    private static final ResourceLocation EMPTY_SLOT_SWORD;
    private static final ResourceLocation EMPTY_SLOT_SHOVEL;
    private static final ResourceLocation EMPTY_SLOT_PICKAXE;
    private static final ResourceLocation EMPTY_SLOT_INGOT;
    private static final ResourceLocation EMPTY_SLOT_REDSTONE_DUST;
    private static final ResourceLocation EMPTY_SLOT_QUARTZ;
    private static final ResourceLocation EMPTY_SLOT_EMERALD;
    private static final ResourceLocation EMPTY_SLOT_DIAMOND;
    private static final ResourceLocation EMPTY_SLOT_LAPIS_LAZULI;
    private static final ResourceLocation EMPTY_SLOT_AMETHYST_SHARD;

    private static final Component STEEL_UPGRADE;
    private static final Component STEEL_UPGRADE_APPLIES_TO_TEXT;
    private static final Component STEEL_UPGRADE_INGREDIENTS_TEXT;
    private static final Component STEEL_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT;
    private static final Component STEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT;

    public CustomSmithingTemplateItem(Component pAppliesTo, Component pIngredients, Component pUpdradeDescription, Component pBaseSlotDescription, Component pAdditionsSlotDescription, List<ResourceLocation> pBaseSlotEmptyIcons, List<ResourceLocation> pAdditonalSlotEmptyIcons) {
        super(pAppliesTo, pIngredients, pUpdradeDescription, pBaseSlotDescription, pAdditionsSlotDescription, pBaseSlotEmptyIcons, pAdditonalSlotEmptyIcons);
    }

    public static SmithingTemplateItem createHauntedSteelUpgrade() {
        return new SmithingTemplateItem(STEEL_UPGRADE_APPLIES_TO_TEXT, STEEL_UPGRADE_INGREDIENTS_TEXT, STEEL_UPGRADE, STEEL_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT, STEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT, getHauntedSteelUpgradeEmptyBaseSlotTextures(), getHauntedSteelUpgradeEmptyAdditionsSlotTextures());
    }

    private static List<ResourceLocation> getHauntedSteelUpgradeEmptyBaseSlotTextures() {
        return List.of(EMPTY_SLOT_HELMET, EMPTY_SLOT_SWORD, EMPTY_SLOT_CHESTPLATE, EMPTY_SLOT_PICKAXE, EMPTY_SLOT_LEGGINGS, EMPTY_SLOT_AXE, EMPTY_SLOT_BOOTS, EMPTY_SLOT_HOE, EMPTY_SLOT_SHOVEL);
    }

    private static List<ResourceLocation> getHauntedSteelUpgradeEmptyAdditionsSlotTextures() {
        return List.of(EMPTY_SLOT_INGOT);
    }


    static {
        TITLE_FORMAT = ChatFormatting.GRAY;
        DESCRIPTION_FORMAT = ChatFormatting.BLUE;
        EMPTY_SLOT_HELMET = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet");
        EMPTY_SLOT_CHESTPLATE = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate");
        EMPTY_SLOT_LEGGINGS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings");
        EMPTY_SLOT_BOOTS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots");
        EMPTY_SLOT_HOE = ResourceLocation.withDefaultNamespace("item/empty_slot_hoe");
        EMPTY_SLOT_AXE = ResourceLocation.withDefaultNamespace("item/empty_slot_axe");
        EMPTY_SLOT_SWORD = ResourceLocation.withDefaultNamespace("item/empty_slot_sword");
        EMPTY_SLOT_SHOVEL = ResourceLocation.withDefaultNamespace("item/empty_slot_shovel");
        EMPTY_SLOT_PICKAXE = ResourceLocation.withDefaultNamespace("item/empty_slot_pickaxe");
        EMPTY_SLOT_INGOT = ResourceLocation.withDefaultNamespace("item/empty_slot_ingot");
        EMPTY_SLOT_REDSTONE_DUST = ResourceLocation.withDefaultNamespace("item/empty_slot_redstone_dust");
        EMPTY_SLOT_QUARTZ = ResourceLocation.withDefaultNamespace("item/empty_slot_quartz");
        EMPTY_SLOT_EMERALD = ResourceLocation.withDefaultNamespace("item/empty_slot_emerald");
        EMPTY_SLOT_DIAMOND = ResourceLocation.withDefaultNamespace("item/empty_slot_diamond");
        EMPTY_SLOT_LAPIS_LAZULI = ResourceLocation.withDefaultNamespace("item/empty_slot_lapis_lazuli");
        EMPTY_SLOT_AMETHYST_SHARD = ResourceLocation.withDefaultNamespace("item/empty_slot_amethyst_shard");

        STEEL_UPGRADE = Component.translatable(Util.makeDescriptionId("upgrade", ResourceLocation.tryBuild(DucksDelights.MOD_ID, "haunted_steel_upgrade"))).withStyle(TITLE_FORMAT);
        STEEL_UPGRADE_APPLIES_TO_TEXT = Component.translatable(Util.makeDescriptionId("item", ResourceLocation.tryBuild(DucksDelights.MOD_ID, "smithing_template.haunted_steel_upgrade.applies_to"))).withStyle(DESCRIPTION_FORMAT);
        STEEL_UPGRADE_INGREDIENTS_TEXT = Component.translatable(Util.makeDescriptionId("item", ResourceLocation.tryBuild(DucksDelights.MOD_ID,"smithing_template.haunted_steel_upgrade.ingredients"))).withStyle(DESCRIPTION_FORMAT);
        STEEL_UPGRADE_BASE_SLOT_DESCRIPTION_TEXT = Component.translatable(Util.makeDescriptionId("item", ResourceLocation.tryBuild(DucksDelights.MOD_ID,"smithing_template.haunted_steel_upgrade.base_slot_description")));
        STEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_TEXT = Component.translatable(Util.makeDescriptionId("item", ResourceLocation.tryBuild(DucksDelights.MOD_ID,"smithing_template.haunted_steel_upgrade.additions_slot_description")));
    }
}
