package net.lod.ducksdelights.item.custom;

import net.lod.ducksdelights.util.BrewingHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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

public class EnchantedPearlAppleItem extends PearlAppleItem{
    public EnchantedPearlAppleItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        boolean success = false;
        if (pAction == ClickAction.SECONDARY && pSlot.allowModification(pPlayer)) {
            if (!pStack.hasTag()) {
                pStack.getOrCreateTagElement("Used").putBoolean("Used", false);
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
                        super.playInsertSound(pPlayer);
                        if (PotionUtils.getCustomEffects(newItem).size() > 5) {
                            this.setRemainingUse(newItem, true);
                        }
                        if (!pPlayer.isCreative()) {
                            if (pOther.getItem() instanceof ThrowablePotionItem) {
                                super.playShatterInsertSound(pPlayer);
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
                    super.playScrubSound(pPlayer);
                    success = true;
                }
            }
        }
        return success;
    }

    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        PotionUtils.addPotionTooltip(pStack, pTooltip, 1.0F);
        if (pStack.hasTag()) {
            if (pStack.getTag().contains("Used")) {
                if (pStack.getTag().getBoolean("Used")) {
                    pTooltip.add(Component.translatable("item.ducksdelights.pearled_apple_potion_applied").withStyle(ChatFormatting.RED));
                    return;
                }
                pTooltip.add(Component.translatable("item.ducksdelights.enchanted_pearled_apple_applicable").withStyle(ChatFormatting.BLUE));
            }
        } else {
            pTooltip.add(Component.translatable("item.ducksdelights.pearled_apple_applicable").withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }
}
