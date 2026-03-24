package net.lod.ducksdelights.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

public class BrewingHandler {

    public static void addBrewingRecipe(Potion basePotion, Ingredient ingredient, Potion resultPotion) {
        ItemStack basePotionStack = PotionUtils.setPotion(new ItemStack(Items.POTION), basePotion);
        ItemStack resultPotionStack = PotionUtils.setPotion(new ItemStack(Items.POTION), resultPotion);
        BrewingRecipeRegistry.addRecipe(new MiddlemanBrewingRecipe(
                Ingredient.of(basePotionStack),
                ingredient,
                resultPotionStack
        ));
    }

    public static void addBrewingRecipe(Ingredient base, Ingredient ingredient, Potion resultPotion) {
        ItemStack resultPotionStack = PotionUtils.setPotion(new ItemStack(Items.POTION), resultPotion);
        BrewingRecipeRegistry.addRecipe(new MiddlemanBrewingRecipe(
                base,
                ingredient,
                resultPotionStack
        ));
    }
}
