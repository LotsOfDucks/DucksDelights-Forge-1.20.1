package net.lod.ducksdelights.recipe;

import net.lod.ducksdelights.DucksDelights;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, DucksDelights.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, DucksDelights.MOD_ID);


    public static final RegistryObject<RecipeType<BlazingRecipe>> BLAZING_RECIPE_TYPE = RECIPE_TYPES.register("blazing", () -> new RecipeType<>() {
    });
    public static final RegistryObject<RecipeType<PearlingRecipe>> PEARLING_RECIPE_TYPE = RECIPE_TYPES.register("pearling", () -> new RecipeType<>() {
    });
    public static final RegistryObject<RecipeType<NetherPearlingRecipe>> NETHER_PEARLING_RECIPE_TYPE = RECIPE_TYPES.register("nether_pearling", () -> new RecipeType<>() {
    });
    public static final RegistryObject<RecipeType<EndPearlingRecipe>> END_PEARLING_RECIPE_TYPE = RECIPE_TYPES.register("end_pearling", () -> new RecipeType<>() {
    });


    public static final RegistryObject<RecipeSerializer<?>> BLAZING_RECIPE = SERIALIZERS.register("blazing", () -> new BlazingCookingSerializer<>(BlazingRecipe::new, 100));
    public static final RegistryObject<RecipeSerializer<?>> PEARLING_RECIPE = SERIALIZERS.register("pearling", () -> new PearlingSerializer<>(PearlingRecipe::new, 100));
    public static final RegistryObject<RecipeSerializer<?>> NETHER_PEARLING_RECIPE = SERIALIZERS.register("nether_pearling", () -> new PearlingSerializer<>(NetherPearlingRecipe::new, 100));
    public static final RegistryObject<RecipeSerializer<?>> END_PEARLING_RECIPE = SERIALIZERS.register("end_pearling", () -> new PearlingSerializer<>(EndPearlingRecipe::new, 100));


    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        SERIALIZERS.register(eventBus);
    }

}
