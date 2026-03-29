package net.lod.ducksdelights.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SpongeBlock;

import javax.annotation.Nullable;
import java.util.List;

public class PearlPickaxeItem extends PickaxeItem {
    public PearlPickaxeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    public int getRemainingPotions(ItemStack stack) {
        int remainingUses = 0;
        if (stack.hasTag()) {
            remainingUses= stack.getTag().getInt("Uses");
        }
        return remainingUses;
    }

    public void setRemainingPotions(ItemStack stack, int remainingPotions) {
        stack.getOrCreateTag().putInt("Uses", remainingPotions);
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (!PotionUtils.getCustomEffects(pStack).isEmpty()) {
            List<MobEffectInstance> potionInstanceList = PotionUtils.getCustomEffects(pStack);
            for (MobEffectInstance mobEffectInstance : potionInstanceList) {
                pTarget.addEffect(new MobEffectInstance(mobEffectInstance.getEffect(), Math.max(1, (mobEffectInstance.getDuration() / 4)), mobEffectInstance.getAmplifier()));
            }
            if (this.getRemainingPotions(pStack) > 1) {
                this.setRemainingPotions(pStack, (this.getRemainingPotions(pStack) - 1));
            } else {
                pStack.removeTagKey("CustomPotionEffects");
                this.setRemainingPotions(pStack, 0);
            }
            if (pAttacker instanceof Player player) {
                player.displayClientMessage(Component.translatable("potion.ducksdelights.remaining_applications").append(" ").append(String.valueOf(this.getRemainingPotions(pStack))), true);
            }
        }
        pStack.hurtAndBreak(1, pAttacker, (entity) -> {
            Containers.dropContents(pAttacker.level(), pAttacker, new SimpleContainer(this.getRemainderItem(pStack)));
            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    public ItemStack getRemainderItem(ItemStack oldStack) {
        ItemStack remainderItem = new ItemStack(Items.GOLDEN_PICKAXE);
        remainderItem.setTag(oldStack.getTag());
        remainderItem.setDamageValue(0);
        return remainderItem;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        boolean success = false;
        if (pStack.getTag() != null) {
            if (pAction == ClickAction.SECONDARY && pSlot.allowModification(pPlayer)) {
                if (!pStack.getTag().contains("CustomPotionEffects")) {
                    if (pOther.getItem() instanceof PotionItem) {
                        Potion newPotion = PotionUtils.getPotion(pOther);
                        if (!newPotion.getEffects().isEmpty()) {
                            PotionUtils.setCustomEffects(pStack, PotionUtils.getPotion(pOther).getEffects());
                            this.playInsertSound(pPlayer);
                            this.setRemainingPotions(pStack, 4);
                            if (!pPlayer.isCreative()) {
                                if (pOther.getItem() instanceof ThrowablePotionItem) {
                                    this.playShatterInsertSound(pPlayer);
                                    pOther.shrink(1);
                                } else {
                                    pPlayer.addItem(new ItemStack(Items.GLASS_BOTTLE));
                                }
                                pOther.shrink(1);
                            }
                            success = true;
                        }
                    }
                } else {
                    if (pOther.getItem() instanceof BlockItem blockItem) {
                        if (blockItem.getBlock() instanceof SpongeBlock) {
                            pStack.removeTagKey("CustomPotionEffects");
                            this.setRemainingPotions(pStack, 0);
                            this.playScrubSound(pPlayer);
                            success = true;
                        }
                    }
                }
            }
        }
        return success;
    }

    private void playInsertSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BREWING_STAND_BREW, 1.0F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playShatterInsertSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.SPLASH_POTION_BREAK, 1.0F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playScrubSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.DOLPHIN_AMBIENT, 0.8F, 0.8F);
    }

    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        PotionUtils.addPotionTooltip(pStack, pTooltip, 0.25F);
        if (!PotionUtils.getCustomEffects(pStack).isEmpty()) {
            pTooltip.add(Component.translatable("potion.ducksdelights.remaining_applications").append(" ").append(String.valueOf(this.getRemainingPotions(pStack))).withStyle(ChatFormatting.GRAY));
        }
    }
}
