package net.lod.ducksdelights.item.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class RemainderItem extends Item {
    public static ItemLike remainder;

    public RemainderItem(Properties pProperties, ItemLike remainderItem) {
        super(pProperties);
        remainder = remainderItem;
    }

    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        ItemStack noChange = super.finishUsingItem(pStack, pLevel, pEntityLiving);
        return pEntityLiving instanceof Player && ((Player)pEntityLiving).getAbilities().instabuild ? noChange : new ItemStack(remainder);
    }
}
