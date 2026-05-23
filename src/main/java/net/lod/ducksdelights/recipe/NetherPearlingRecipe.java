package net.lod.ducksdelights.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.block.ModBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NetherPearlingRecipe extends AbstractCookingRecipe {
    public NetherPearlingRecipe(ResourceLocation pId, String pGroup, CookingBookCategory pCategory, Ingredient pIngredient, ItemStack pResult, float pExperience, int pCookingTime) {
        super(ModRecipes.NETHER_PEARLING_RECIPE_TYPE.get(), pId, pGroup, pCategory, pIngredient, pResult, pExperience, pCookingTime);
    }
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.GIANT_CLAM_NETHER.get());
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.NETHER_PEARLING_RECIPE.get();
    }

    public int getPearlingTime() {
        return super.getCookingTime();
    }
}
