package net.lod.ducksdelights.recipe;

import net.lod.ducksdelights.block.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class PearlingRecipe extends AbstractCookingRecipe {
    public PearlingRecipe(ResourceLocation pId, String pGroup, CookingBookCategory pCategory, Ingredient pIngredient, ItemStack pResult, float pExperience, int pCookingTime) {
        super(ModRecipes.PEARLING_RECIPE_TYPE.get(), pId, pGroup, pCategory, pIngredient, pResult, pExperience, pCookingTime);
    }
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.GIANT_CLAM_BROWN.get());
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.PEARLING_RECIPE.get();
    }

    public int getPearlingTime() {
        return super.getCookingTime();
    }
}
