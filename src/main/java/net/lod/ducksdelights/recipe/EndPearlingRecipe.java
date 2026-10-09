package net.lod.ducksdelights.recipe;

import net.lod.ducksdelights.block.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class EndPearlingRecipe extends AbstractCookingRecipe {
    public EndPearlingRecipe(ResourceLocation pId, String pGroup, CookingBookCategory pCategory, Ingredient pIngredient, ItemStack pResult, float pExperience, int pCookingTime) {
        super(ModRecipes.END_PEARLING_RECIPE_TYPE.get(), pId, pGroup, pCategory, pIngredient, pResult, pExperience, pCookingTime);
    }
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.GIANT_CLAM_ENDER.get());
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.END_PEARLING_RECIPE.get();
    }

    public int getPearlingTime() {
        return super.getCookingTime();
    }
}
