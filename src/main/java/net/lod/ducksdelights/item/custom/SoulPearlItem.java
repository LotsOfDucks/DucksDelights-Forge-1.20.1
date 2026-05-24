package net.lod.ducksdelights.item.custom;

import net.lod.ducksdelights.entity.projectile.ThrownSoulpearl;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SoulPearlItem extends Item {
    public SoulPearlItem(Properties pProperties) {
        super(pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack $$3 = pPlayer.getItemInHand(pHand);
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
        pPlayer.getCooldowns().addCooldown(this, 20);
        if (!pLevel.isClientSide) {
            ThrownSoulpearl $$4 = new ThrownSoulpearl(pLevel, pPlayer);
            $$4.setItem($$3);
            $$4.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 1.0F);
            pLevel.addFreshEntity($$4);
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        if (!pPlayer.getAbilities().instabuild) {
            $$3.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess($$3, pLevel.isClientSide());
    }
}
