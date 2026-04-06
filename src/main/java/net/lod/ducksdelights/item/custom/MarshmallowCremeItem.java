package net.lod.ducksdelights.item.custom;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class MarshmallowCremeItem extends RemainderItem {
    public MarshmallowCremeItem(Properties pProperties, ItemLike remainderItem) {
        super(pProperties, remainderItem);
    }

    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        if (pEntityLiving.hasEffect(MobEffects.POISON)) {
            pEntityLiving.removeEffect(MobEffects.POISON);
        }
        return super.finishUsingItem(pStack, pLevel, pEntityLiving);
    }
}
