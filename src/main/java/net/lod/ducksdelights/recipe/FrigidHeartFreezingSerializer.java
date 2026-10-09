package net.lod.ducksdelights.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.Arrays;

public class FrigidHeartFreezingSerializer<T extends AbstractFrigidHeartFreezingRecipe> implements RecipeSerializer<T> {
    public final CookieBaker<T> factory;

    public FrigidHeartFreezingSerializer(CookieBaker<T> pFactory) {
        this.factory = pFactory;
    }

    @SuppressWarnings("removal")
    public T fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
        JsonElement jsonelement = GsonHelper.isArrayNode(pJson, "ingredient") ? GsonHelper.getAsJsonArray(pJson, "ingredient") : GsonHelper.getAsJsonObject(pJson, "ingredient");
        Ingredient ingredient = Ingredient.fromJson(jsonelement, false);
        if (Arrays.stream(ingredient.getItems()).noneMatch(itemStack -> itemStack.getItem() instanceof BucketItem)) {
            if (Arrays.stream(ingredient.getItems()).noneMatch(itemStack -> itemStack.getItem() instanceof BlockItem)) {
                throw new IllegalStateException("Ingredient Item is not a block");
            }
        }
        if (!pJson.has("result")) {
            throw new JsonSyntaxException("Missing result, expected to find a string or object");
        } else {
            ItemStack itemstack;
            if (pJson.get("result").isJsonObject()) {
                itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pJson, "result"));
            } else {
                String s1 = GsonHelper.getAsString(pJson, "result");
                ResourceLocation resourcelocation = new ResourceLocation(s1);
                itemstack = new ItemStack(BuiltInRegistries.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
                    return new IllegalStateException("Item: " + s1 + " does not exist");
                }));
                if (!(itemstack.getItem() instanceof BlockItem)) {
                    throw new IllegalStateException("Item: " + s1 + " is not a block");
                }
            }
            boolean freezeNeighbors = false;
            if (pJson.has("freeze_neighbors")) {
                freezeNeighbors = GsonHelper.getAsBoolean(pJson, "freeze_neighbors");
            }

            return this.factory.create(pRecipeId, ingredient, itemstack, freezeNeighbors);
        }
    }

    public T fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
        ItemStack itemstack = pBuffer.readItem();
        Boolean freezeNeighbors = pBuffer.readBoolean();
        return this.factory.create(pRecipeId, ingredient, itemstack, freezeNeighbors);
    }

    public void toNetwork(FriendlyByteBuf pBuffer, T pRecipe) {
        for (int slot = 0; slot < pRecipe.getIngredients().size(); slot++) {
            pRecipe.getIngredients().get(slot ).toNetwork(pBuffer);
        }
        pBuffer.writeItem(pRecipe.getResultItem(null));
        pBuffer.writeBoolean(pRecipe.getShouldFreezeNeighbors());
    }

    interface CookieBaker<T extends AbstractFrigidHeartFreezingRecipe> {
        T create(ResourceLocation var1, Ingredient var4, ItemStack var5, Boolean bool);
    }


}
