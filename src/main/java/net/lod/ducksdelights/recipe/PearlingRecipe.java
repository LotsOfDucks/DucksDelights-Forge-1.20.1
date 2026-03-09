package net.lod.ducksdelights.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.lod.ducksdelights.DucksDelights;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PearlingRecipe implements Recipe<Container> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;
    private final CookingBookCategory category;
    protected final String group;
    protected final int pearlingTime;

    public PearlingRecipe(NonNullList<Ingredient> inputItems, ItemStack output, ResourceLocation id, CookingBookCategory category, String group, int pearlingTime) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
        this.category = category;
        this.group = group;
        this.pearlingTime = pearlingTime;
    }

    public int getPearlingTime() {
        return this.pearlingTime;
    }

    @Override
    public boolean matches(Container container, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        return inputItems.get(0).test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    public String getGroup() {
        return this.group;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public CookingBookCategory category() {
        return this.category;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<PearlingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "pearling";
    }

    public static class Serializer implements RecipeSerializer<PearlingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        @SuppressWarnings("removal")
        public static final ResourceLocation ID = new ResourceLocation(DucksDelights.MOD_ID, "pearling");

        @Override
        public PearlingRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));

            String group = GsonHelper.getAsString(jsonObject, "group", "");
            CookingBookCategory cookingbookcategory = CookingBookCategory.CODEC.byName(GsonHelper.getAsString(jsonObject, "category", null), CookingBookCategory.MISC);
            int pearlingTime = GsonHelper.getAsInt(jsonObject, "pearlingtime");
            JsonArray ingredients = GsonHelper.getAsJsonArray(jsonObject, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for (int listSlot = 0; listSlot < inputs.size(); listSlot++) {
                inputs.set(listSlot, Ingredient.fromJson(ingredients.get(listSlot)));
            }

            return new PearlingRecipe(inputs, output, resourceLocation, cookingbookcategory, group, pearlingTime);
        }

        @Override
        public @Nullable PearlingRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(friendlyByteBuf.readInt(), Ingredient.EMPTY);

            String group = friendlyByteBuf.readUtf();
            CookingBookCategory cookingbookcategory = friendlyByteBuf.readEnum(CookingBookCategory.class);
            int pearlingTime = friendlyByteBuf.readVarInt();

            for (int listSlot = 0; listSlot < inputs.size(); listSlot++) {
                inputs.set(listSlot, Ingredient.fromNetwork(friendlyByteBuf));
            }

            ItemStack output = friendlyByteBuf.readItem();
            return new PearlingRecipe(inputs, output, resourceLocation, cookingbookcategory, group, pearlingTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, PearlingRecipe pearlingRecipe) {
            friendlyByteBuf.writeUtf(pearlingRecipe.group);
            friendlyByteBuf.writeEnum(pearlingRecipe.category());
            friendlyByteBuf.writeInt(pearlingRecipe.inputItems.size());
            friendlyByteBuf.writeVarInt(pearlingRecipe.pearlingTime);

            for (Ingredient ingredient : pearlingRecipe.getIngredients()) {
                ingredient.toNetwork(friendlyByteBuf);
            }

            friendlyByteBuf.writeItemStack(pearlingRecipe.getResultItem(null), false);
        }
    }
}
