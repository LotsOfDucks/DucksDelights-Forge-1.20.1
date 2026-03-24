package net.lod.ducksdelights.entity.mobeffects;

import net.lod.ducksdelights.item.ModItems;
import net.lod.ducksdelights.item.custom.ModPotions;
import net.lod.ducksdelights.util.BrewingHandler;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

public class ModPotionRecipes {

    public static void register() {
        BrewingHandler.addBrewingRecipe(Potions.STRONG_HEALING, Ingredient.of(Items.REDSTONE), ModPotions.LOVE.get());

        BrewingHandler.addBrewingRecipe(Ingredient.of(Items.HONEY_BOTTLE), Ingredient.of(Items.NETHER_WART), ModPotions.SWEET.get());

        BrewingHandler.addBrewingRecipe(ModPotions.SWEET.get(), Ingredient.of(ModItems.PEARL.get()), ModPotions.PURIFICATION.get());
        BrewingHandler.addBrewingRecipe(ModPotions.PURIFICATION.get(), Ingredient.of(Items.REDSTONE), ModPotions.LONG_PURIFICATION.get());

        BrewingHandler.addBrewingRecipe(ModPotions.PURIFICATION.get(), Ingredient.of(Items.FERMENTED_SPIDER_EYE), ModPotions.BEFOULING.get());
        BrewingHandler.addBrewingRecipe(ModPotions.LONG_PURIFICATION.get(), Ingredient.of(Items.FERMENTED_SPIDER_EYE), ModPotions.LONG_BEFOULING.get());
        BrewingHandler.addBrewingRecipe(ModPotions.BEFOULING.get(), Ingredient.of(Items.REDSTONE), ModPotions.LONG_BEFOULING.get());

        BrewingHandler.addBrewingRecipe(Potions.WATER_BREATHING, Ingredient.of(Items.FERMENTED_SPIDER_EYE), ModPotions.ASPHYXIATION.get());
        BrewingHandler.addBrewingRecipe(Potions.LONG_WATER_BREATHING, Ingredient.of(Items.FERMENTED_SPIDER_EYE), ModPotions.LONG_ASPHYXIATION.get());
        BrewingHandler.addBrewingRecipe(ModPotions.ASPHYXIATION.get(), Ingredient.of(Items.REDSTONE), ModPotions.LONG_ASPHYXIATION.get());

        BrewingHandler.addBrewingRecipe(ModPotions.SWEET.get(), Ingredient.of(Items.LEVER), ModPotions.GAMBLING.get());
        BrewingHandler.addBrewingRecipe(ModPotions.GAMBLING.get(), Ingredient.of(Items.GLOWSTONE), ModPotions.STRONG_GAMBLING.get());
        BrewingHandler.addBrewingRecipe(ModPotions.STRONG_GAMBLING.get(), Ingredient.of(Items.GLOWSTONE), ModPotions.STRONGER_GAMBLING.get());

        BrewingHandler.addBrewingRecipe(ModPotions.SWEET.get(), Ingredient.of(Items.IRON_BLOCK), ModPotions.BULWARK.get());
        BrewingHandler.addBrewingRecipe(ModPotions.BULWARK.get(), Ingredient.of(Items.REDSTONE), ModPotions.LONG_BULWARK.get());
        BrewingHandler.addBrewingRecipe(ModPotions.BULWARK.get(), Ingredient.of(Items.GLOWSTONE), ModPotions.GREATER_BULWARK.get());

        BrewingHandler.addBrewingRecipe(ModPotions.SWEET.get(), Ingredient.of(Items.SHULKER_SHELL), ModPotions.LEVITATION.get());
        BrewingHandler.addBrewingRecipe(ModPotions.LEVITATION.get(), Ingredient.of(Items.REDSTONE), ModPotions.LONG_LEVITATION.get());
        BrewingHandler.addBrewingRecipe(ModPotions.LEVITATION.get(), Ingredient.of(Items.GLOWSTONE), ModPotions.STRONG_LEVITATION.get());

        BrewingHandler.addBrewingRecipe(ModPotions.LEVITATION.get(), Ingredient.of(Items.FERMENTED_SPIDER_EYE), ModPotions.GRAVITATION.get());
        BrewingHandler.addBrewingRecipe(ModPotions.LONG_LEVITATION.get(), Ingredient.of(Items.FERMENTED_SPIDER_EYE), ModPotions.LONG_GRAVITATION.get());
        BrewingHandler.addBrewingRecipe(Potions.SLOW_FALLING, Ingredient.of(Items.FERMENTED_SPIDER_EYE), ModPotions.GRAVITATION.get());
        BrewingHandler.addBrewingRecipe(Potions.LONG_SLOW_FALLING, Ingredient.of(Items.FERMENTED_SPIDER_EYE), ModPotions.LONG_GRAVITATION.get());

        BrewingHandler.addBrewingRecipe(ModPotions.SWEET.get(), Ingredient.of(Items.TNT), ModPotions.EXPLODING.get());
        BrewingHandler.addBrewingRecipe(ModPotions.EXPLODING.get(), Ingredient.of(Items.GLOWSTONE), ModPotions.SHORT_EXPLODING.get());

        BrewingHandler.addBrewingRecipe(ModPotions.SWEET.get(), Ingredient.of(Items.ENDER_PEARL), ModPotions.ENDER_TRANSFERENCE.get());

        BrewingHandler.addBrewingRecipe(Potions.STRONG_POISON, Ingredient.of(Items.WITHER_SKELETON_SKULL), ModPotions.PLAGUE.get());

        BrewingHandler.addBrewingRecipe(ModPotions.SWEET.get(), Ingredient.of(Items.BLAZE_POWDER), ModPotions.BURNING.get());
        BrewingHandler.addBrewingRecipe(ModPotions.BURNING.get(), Ingredient.of(Items.REDSTONE), ModPotions.STRONG_BURNING.get());
        BrewingHandler.addBrewingRecipe(ModPotions.FREEZING.get(), Ingredient.of(Items.FERMENTED_SPIDER_EYE), ModPotions.BURNING.get());
        BrewingHandler.addBrewingRecipe(ModPotions.STRONG_FREEZING.get(), Ingredient.of(Items.FERMENTED_SPIDER_EYE), ModPotions.STRONG_BURNING.get());

        BrewingHandler.addBrewingRecipe(ModPotions.SWEET.get(), Ingredient.of(Items.ICE), ModPotions.FREEZING.get());
        BrewingHandler.addBrewingRecipe(ModPotions.FREEZING.get(), Ingredient.of(Items.REDSTONE), ModPotions.LONG_FREEZING.get());
        BrewingHandler.addBrewingRecipe(ModPotions.FREEZING.get(), Ingredient.of(Items.GLOWSTONE), ModPotions.STRONG_FREEZING.get());
        BrewingHandler.addBrewingRecipe(ModPotions.BURNING.get(), Ingredient.of(Items.FERMENTED_SPIDER_EYE), ModPotions.FREEZING.get());
        BrewingHandler.addBrewingRecipe(ModPotions.STRONG_BURNING.get(), Ingredient.of(Items.FERMENTED_SPIDER_EYE), ModPotions.STRONG_FREEZING.get());

        BrewingHandler.addBrewingRecipe(ModPotions.SWEET.get(), Ingredient.of(Items.ENDER_PEARL), ModPotions.ENDER_TRANSFERENCE.get());



    }
}
