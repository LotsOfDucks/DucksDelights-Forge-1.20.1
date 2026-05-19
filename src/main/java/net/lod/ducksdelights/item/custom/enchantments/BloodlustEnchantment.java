package net.lod.ducksdelights.item.custom.enchantments;

import net.lod.ducksdelights.item.custom.CleaverItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class BloodlustEnchantment extends Enchantment {
    public BloodlustEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public int getMinCost(int pEnchantmentLevel) {
        return 5 + (pEnchantmentLevel - 1) * 9;
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return super.getMinCost(pEnchantmentLevel) + 20;
    }

    public int getMaxLevel() {
        return 5;
    }

    public boolean canEnchant(ItemStack pStack) {
        return pStack.getItem() instanceof CleaverItem;
    }
}
