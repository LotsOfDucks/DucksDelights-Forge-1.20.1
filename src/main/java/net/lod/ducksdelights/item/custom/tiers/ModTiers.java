package net.lod.ducksdelights.item.custom.tiers;

import net.lod.ducksdelights.item.ModItems;
import net.minecraft.tags.TagKey;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum ModTiers implements Tier {
    PEARL(3, 950, 12.0F, 2.0F, 10, () ->
            Ingredient.of(ModItems.PEARL.get())),
    HAUNTED_STEEL(3, 500, 7.0F, 2.0F, 14, () ->
            Ingredient.of(ModItems.HAUNTED_STEEL_INGOT.get()));

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    ModTiers(int pLevel, int pUses, float pSpeed, float pDamage, int pEnchantmentValue, Supplier pRepairIngredient) {
        this.level = pLevel;
        this.uses = pUses;
        this.speed = pSpeed;
        this.damage = pDamage;
        this.enchantmentValue = pEnchantmentValue;
        this.repairIngredient = new LazyLoadedValue(pRepairIngredient);
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    public int getLevel() {
        return this.level;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    public @Nullable TagKey<Block> getTag() {
        return getTagFromModTier(this);
    }

    public static TagKey<Block> getTagFromModTier(ModTiers tier) {
        TagKey tagKey;
        switch (tier) {
            case PEARL -> tagKey = Tags.Blocks.NEEDS_WOOD_TOOL;
            default -> throw new IncompatibleClassChangeError();
        }

        return tagKey;
    }
}
