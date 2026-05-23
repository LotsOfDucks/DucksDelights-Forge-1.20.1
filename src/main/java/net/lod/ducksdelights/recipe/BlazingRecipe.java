package net.lod.ducksdelights.recipe;

import net.lod.ducksdelights.block.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class BlazingRecipe extends AbstractCookingRecipe {
    public BlazingRecipe(ResourceLocation name, String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(ModRecipes.BLAZING_RECIPE_TYPE.get(), name, group, category, ingredient, result, experience, cookingTime);
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.BLAZING_BARREL.get());
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.BLAZING_RECIPE.get();
    }
}
