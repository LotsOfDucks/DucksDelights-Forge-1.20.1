package net.lod.ducksdelights.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.lod.ducksdelights.DucksDelights;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class BlazingRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;
    protected final int cookingTime;

    public BlazingRecipe(NonNullList<Ingredient> inputItems, ItemStack output, ResourceLocation id, int cookingTime) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
        this.cookingTime = cookingTime;
    }

    public int getCookingTime() {
        return this.cookingTime;
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        return inputItems.get(0).test(simpleContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer, RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<BlazingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "blazing";
    }

    public static class Serializer implements RecipeSerializer<BlazingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        @SuppressWarnings("removal")
        public static final ResourceLocation ID = new ResourceLocation(DucksDelights.MOD_ID, "blazing");

        @Override
        public BlazingRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));

            int cookingTime = GsonHelper.getAsInt(jsonObject, "cookingtime");
            JsonArray ingredients = GsonHelper.getAsJsonArray(jsonObject, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for (int listSlot = 0; listSlot < inputs.size(); listSlot++) {
                inputs.set(listSlot, Ingredient.fromJson(ingredients.get(listSlot)));
            }

            return new BlazingRecipe(inputs, output, resourceLocation, cookingTime);
        }

        @Override
        public @Nullable BlazingRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(friendlyByteBuf.readInt(), Ingredient.EMPTY);
            int cookingTime = friendlyByteBuf.readVarInt();

            for (int listSlot = 0; listSlot < inputs.size(); listSlot++) {
                inputs.set(listSlot, Ingredient.fromNetwork(friendlyByteBuf));
            }

            ItemStack output = friendlyByteBuf.readItem();
            return new BlazingRecipe(inputs, output, resourceLocation, cookingTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, BlazingRecipe blazingRecipe) {
            friendlyByteBuf.writeInt(blazingRecipe.inputItems.size());
            friendlyByteBuf.writeVarInt(blazingRecipe.cookingTime);

            for (Ingredient ingredient : blazingRecipe.getIngredients()) {
                ingredient.toNetwork(friendlyByteBuf);
            }

            friendlyByteBuf.writeItemStack(blazingRecipe.getResultItem(null), false);
        }
    }
}
