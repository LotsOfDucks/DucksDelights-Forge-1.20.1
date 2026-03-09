package net.lod.ducksdelights.item.custom;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class SimpleFurnaceFuelBlockItem extends BlockItem {
    public int burnTime;

    public SimpleFurnaceFuelBlockItem(Block pBlock, int burnTime, Properties pProperties) {
        super(pBlock, pProperties);
        this.burnTime = burnTime;
    }


    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return this.burnTime;
    }

}
