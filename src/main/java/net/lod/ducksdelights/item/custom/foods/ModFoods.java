package net.lod.ducksdelights.item.custom.foods;

import net.minecraft.world.food.FoodProperties;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModFoods {
    public static FoodProperties WHITE_RICE;
    public static FoodProperties GOLDEN_RICE;
    public static FoodProperties ONIGIRI;
    public static FoodProperties CHICKEN_ONIGIRI;
    public static FoodProperties BEEF_ONIGIRI;
    public static FoodProperties PORK_ONIGIRI;
    public static FoodProperties SALMON_ONIGIRI;
    public static FoodProperties GILDED_ONIGIRI;
    public static FoodProperties KIBBLESTONE;
    public static FoodProperties ROCK_CANDY;
    public static FoodProperties BLACKBERRY;


    public ModFoods() {
    }

    static {
        WHITE_RICE = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.4F).build();
        GOLDEN_RICE = (new FoodProperties.Builder()).nutrition(4).saturationMod(1.2F).build();
        ONIGIRI = (new FoodProperties.Builder()).nutrition(5).saturationMod(0.5F).build();
        CHICKEN_ONIGIRI = (new FoodProperties.Builder()).nutrition(8).saturationMod(0.6F).build();
        BEEF_ONIGIRI = (new FoodProperties.Builder()).nutrition(10).saturationMod(0.7F).build();
        PORK_ONIGIRI = (new FoodProperties.Builder()).nutrition(10).saturationMod(0.7F).build();
        SALMON_ONIGIRI = (new FoodProperties.Builder()).nutrition(8).saturationMod(0.6F).build();
        GILDED_ONIGIRI = (new FoodProperties.Builder()).nutrition(14).saturationMod(1.2F).build();
        KIBBLESTONE = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.1F).build();
        ROCK_CANDY = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.1F).build();
        BLACKBERRY = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.1F).build();
    }
}
