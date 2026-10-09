package net.lod.ducksdelights.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

import java.util.*;

public class BrewingHandler {

    public static void addBrewingRecipe(Potion basePotion, Ingredient ingredient, Potion resultPotion) {
        ItemStack basePotionStack = PotionUtils.setPotion(new ItemStack(Items.POTION), basePotion);
        ItemStack resultPotionStack = PotionUtils.setPotion(new ItemStack(Items.POTION), resultPotion);
        BrewingRecipeRegistry.addRecipe(new MiddlemanBrewingRecipe(
                Ingredient.of(basePotionStack),
                ingredient,
                resultPotionStack
        ));
    }

    public static void addBrewingRecipe(Ingredient base, Ingredient ingredient, Potion resultPotion) {
        ItemStack resultPotionStack = PotionUtils.setPotion(new ItemStack(Items.POTION), resultPotion);
        BrewingRecipeRegistry.addRecipe(new MiddlemanBrewingRecipe(
                base,
                ingredient,
                resultPotionStack
        ));
    }

    public static void addBrewingRecipe(Ingredient base, Ingredient ingredient, ItemStack result) {
        BrewingRecipeRegistry.addRecipe(new MiddlemanBrewingRecipe(
                base,
                ingredient,
                result
        ));
    }

    public static void addBrewingRecipe(Potion basePotion, Ingredient ingredient, ItemStack result) {
        ItemStack basePotionStack = PotionUtils.setPotion(new ItemStack(Items.POTION), basePotion);
        BrewingRecipeRegistry.addRecipe(new MiddlemanBrewingRecipe(
                Ingredient.of(basePotionStack),
                ingredient,
                result
        ));
    }

    public static int getPearledColor(Collection<MobEffectInstance> pEffects) {
        if (pEffects.isEmpty()) {
            return 3694022;
        } else {
            float red = 0.0F;
            float green = 0.0F;
            float blue = 0.0F;
            float totalEffects = 0;
            Iterator effectIterator = pEffects.iterator();

            while(effectIterator.hasNext()) {
                MobEffectInstance potionEffect = (MobEffectInstance)effectIterator.next();
                int effectColor = potionEffect.getEffect().getColor();
                int amplifierValue = (potionEffect.getAmplifier() + 1);
                red += ((amplifierValue * 0.8F) * (effectColor >> 16 & 255)) / 255.0F;
                green += ((amplifierValue * 0.8F) * (effectColor >> 8 & 255)) / 255.0F;
                blue += ((amplifierValue * 0.8F) * (effectColor >> 0 & 255)) / 255.0F;
                totalEffects += amplifierValue;

            }

            if (totalEffects < 1 && totalEffects != 0) {
                totalEffects = 1;
            }

            if (totalEffects == 0) {
                return 0;
            } else {
                red = ((red + 1) / (totalEffects + 1)) * 255.0F;
                green = ((green + 1) / (totalEffects + 1)) * 255.0F;
                blue = ((blue + 1) / (totalEffects + 1)) * 255.0F;
                return (int)red << 16 | (int)green << 8 | (int)blue;
            }
        }
    }

    public static ItemStack addCustomEffects(ItemStack pStack, Collection<MobEffectInstance> oldEffects, Collection<MobEffectInstance> newEffects) {
        if (newEffects.isEmpty()) {
            return pStack;
        } else {
            CompoundTag itemTag = pStack.getOrCreateTag();
            ListTag listTag = itemTag.getList("CustomPotionEffects", 9);

            //Setup Lists
            List<MobEffectInstance> oldList = new ArrayList<>(oldEffects.stream().toList());
            List<MobEffectInstance> finalList = new ArrayList<>(List.of());
            List<MobEffectInstance> effectInstancesToRemove = new ArrayList<>(List.of());

            //First Iterator Wave, Compare Effects.
            Iterator<MobEffectInstance> newEffectIterator = newEffects.iterator();
            Iterator<MobEffectInstance> oldEffectIterator = oldEffects.iterator();
            while(newEffectIterator.hasNext()) {
                MobEffectInstance nextInstance = newEffectIterator.next();
                boolean alreadyPresentAndStronger = false;
                while(oldEffectIterator.hasNext()) {
                    MobEffectInstance oldNextInstance = oldEffectIterator.next();
                    if (nextInstance.getEffect() == oldNextInstance.getEffect()) {
                        if (nextInstance.getAmplifier() < oldNextInstance.getAmplifier()) {
                            alreadyPresentAndStronger = true;
                        } else {
                            effectInstancesToRemove.add(oldNextInstance);
                        }
                    }

                }
                if (alreadyPresentAndStronger) {
                    continue;
                }

                finalList.add(nextInstance);
            }

            //Remove Flagged Effects From Original List
            oldList.removeAll(effectInstancesToRemove);
            finalList.addAll(oldList);

            finalList = finalList.stream().sorted().toList();
            Iterator<MobEffectInstance> finalListIterator = finalList.listIterator();
            while(finalListIterator.hasNext()) {
                MobEffectInstance nextInstance = finalListIterator.next();
                listTag.add(nextInstance.save(new CompoundTag()));
            }

            itemTag.put("CustomPotionEffects", listTag);
            return pStack;
        }
    }
}
