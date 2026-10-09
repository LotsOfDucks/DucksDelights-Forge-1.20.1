package net.lod.ducksdelights.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Blocks;

public class FrigidHeartFreezingRecipe extends AbstractFrigidHeartFreezingRecipe{
    public FrigidHeartFreezingRecipe(ResourceLocation pId, Ingredient pIngredient, ItemStack pResult, Boolean shouldFreeze) {
        super(ModRecipes.FRIGID_HEART_FREEZING_RECIPE_TYPE.get(), pId, pIngredient, pResult, shouldFreeze);
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(Blocks.ICE);
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FRIGID_HEART_FREEZING_RECIPE.get();
    }
}
