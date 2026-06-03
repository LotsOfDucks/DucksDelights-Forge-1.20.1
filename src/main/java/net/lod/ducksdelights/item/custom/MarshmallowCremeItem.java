package net.lod.ducksdelights.item.custom;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class MarshmallowCremeItem extends Item {
    public MarshmallowCremeItem(Properties pProperties, ItemLike remainderItem) {
        super(pProperties);
    }

    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        ItemStack noChange = super.finishUsingItem(pStack, pLevel, pEntityLiving);
        if (pEntityLiving.hasEffect(MobEffects.POISON)) {
            pEntityLiving.removeEffect(MobEffects.POISON);
        }
        return pEntityLiving instanceof Player && ((Player)pEntityLiving).getAbilities().instabuild ? noChange : new ItemStack(Items.BOWL);
    }
}
