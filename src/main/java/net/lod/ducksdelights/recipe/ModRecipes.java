package net.lod.ducksdelights.recipe;

import net.lod.ducksdelights.DucksDelights;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DucksDelights.MOD_ID);

    public static final RegistryObject<RecipeSerializer<BlazingRecipe>> BLAZING_RECIPE_SERIALIZER =
            SERIALIZERS.register("blazing", () -> BlazingRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }

}
