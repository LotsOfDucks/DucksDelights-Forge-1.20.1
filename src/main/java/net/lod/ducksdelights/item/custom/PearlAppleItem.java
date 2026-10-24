package net.lod.ducksdelights.item.custom;

import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.util.BrewingHandler;
import net.lod.ducksdelights.util.pocketDim.PocketDimSaveTransportHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
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
import java.util.Iterator;
import java.util.List;

public class PearlAppleItem extends Item {
    public PearlAppleItem(Properties pProperties) {
        super(pProperties);
    }

    public void setRemainingUse(ItemStack stack, boolean used) {
        stack.getOrCreateTag().putBoolean("Used", used);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        boolean success = false;
        if (pAction == ClickAction.SECONDARY && pSlot.allowModification(pPlayer)) {
            if (!pStack.hasTag()) {
                pStack.getOrCreateTag().putBoolean("Used", false);
            }
            if (pOther.getItem() instanceof PotionItem) {
                ItemStack newItem;
                boolean createNewItem = false;
                if (pStack.getCount() > 1) {
                    newItem = pStack.copy();
                    newItem.setCount(1);
                    createNewItem = true;
                } else {
                    newItem = pStack;
                }
                if (!newItem.getTag().getBoolean("Used")) {
                    Potion newPotion = PotionUtils.getPotion(pOther);
                    if (!newPotion.getEffects().isEmpty()) {
                        if (newItem.getTag().contains("CustomPotionEffects")) {
                            BrewingHandler.addCustomEffects(newItem, PotionUtils.getCustomEffects(pStack), newPotion.getEffects());
                        } else {
                            BrewingHandler.addCustomEffects(newItem, List.of(), newPotion.getEffects());
                        }
                        this.playInsertSound(pPlayer);
                        if (PotionUtils.getCustomEffects(newItem).size() > 2) {
                            this.setRemainingUse(newItem, true);
                        }
                        if (!pPlayer.isCreative()) {
                            if (pOther.getItem() instanceof ThrowablePotionItem) {
                                this.playShatterInsertSound(pPlayer);
                                pOther.shrink(1);
                            } else {
                                pPlayer.addItem(new ItemStack(Items.GLASS_BOTTLE));
                            }
                            pOther.shrink(1);
                        }
                        if (createNewItem) {
                            pPlayer.addItem(newItem);
                            pStack.shrink(1);
                        }
                        success = true;
                    }
                }
            } else if (pOther.getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof SpongeBlock) {
                    pStack.removeTagKey("CustomPotionEffects");
                    this.setRemainingUse(pStack, false);
                    this.playScrubSound(pPlayer);
                    success = true;
                }
            }
        }
        return success;
    }

    public void playInsertSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.BREWING_STAND_BREW, 1.0F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    public void playShatterInsertSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.SPLASH_POTION_BREAK, 1.0F, 0.8F + pEntity.level().getRandom().nextFloat() * 0.4F);
    }

    public void playScrubSound(Entity pEntity) {
        pEntity.playSound(SoundEvents.DOLPHIN_AMBIENT, 0.8F, 0.8F);
    }

    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        PotionUtils.addPotionTooltip(pStack, pTooltip, 1.0F);
        if (pStack.hasTag()) {
            if (pStack.getTag().contains("Used")) {
                if (pStack.getTag().getBoolean("Used")) {
                    pTooltip.add(Component.translatable("item.ducksdelights.pearled_apple_potion_applied").withStyle(ChatFormatting.RED));
                    return;
                }
                pTooltip.add(Component.translatable("item.ducksdelights.pearled_apple_applicable").withStyle(ChatFormatting.BLUE));
            }
        } else {
            pTooltip.add(Component.translatable("item.ducksdelights.pearled_apple_applicable").withStyle(ChatFormatting.BLUE));
        }
    }

    public int getUseDuration(ItemStack pStack) {
        return 32;
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.EAT;
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pHand);
    }

    @SuppressWarnings("removal")
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        Player player = pEntityLiving instanceof Player ? (Player)pEntityLiving : null;

        if (!pLevel.isClientSide) {
            List<MobEffectInstance> mobEffectList = PotionUtils.getMobEffects(pStack);
            Iterator mobEffectIterator = mobEffectList.iterator();

            while(mobEffectIterator.hasNext()) {
                MobEffectInstance instance = (MobEffectInstance)mobEffectIterator.next();
                if (instance.getEffect().isInstantenous()) {
                    instance.getEffect().applyInstantenousEffect(player, player, pEntityLiving, instance.getAmplifier(), 1.0);
                } else {
                    pEntityLiving.addEffect(new MobEffectInstance(instance));
                }
            }
        }

        if (player != null) {
            player.eat(pLevel, pStack);
        }

        return pStack;
    }
}
