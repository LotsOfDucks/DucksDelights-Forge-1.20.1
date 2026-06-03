package net.lod.ducksdelights.item.custom;

import net.lod.ducksdelights.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SpongeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.List;

public class HauntedSteelFishingRodItem extends FishingRodItem {
    public HauntedSteelFishingRodItem(Properties pProperties) {
        super(pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        int i;
        if (pPlayer.fishing != null) {
            if (!pLevel.isClientSide) {
                i = pPlayer.fishing.retrieve(itemstack);
                itemstack.hurtAndBreak(i, pPlayer, (p_41288_) -> {
                    p_41288_.broadcastBreakEvent(pHand);
                });
            }

            pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
            pPlayer.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        } else {
            pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!pLevel.isClientSide) {
                i = EnchantmentHelper.getFishingSpeedBonus(itemstack);
                int j = EnchantmentHelper.getFishingLuckBonus(itemstack);
                pLevel.addFreshEntity(new FishingHook(pPlayer, pLevel, j + 3, i + 2));
            }

            pPlayer.awardStat(Stats.ITEM_USED.get(this));
            pPlayer.gameEvent(GameEvent.ITEM_INTERACT_START);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pTarget.level() instanceof ServerLevel serverLevel && pAttacker instanceof Player player) {
            if (pTarget.getHealth() <= 0) {
                if (pTarget.getExperienceReward() > 0) {
                    if (pTarget.shouldDropExperience() && serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                        int reward = ForgeEventFactory.getExperienceDrop(pTarget, player, pTarget.getExperienceReward());
                        ExperienceOrb.award(serverLevel, pTarget.position(), reward);
                    }
                }
            }
        }

        pStack.hurtAndBreak(1, pAttacker, (entity) -> {
            Containers.dropContents(pAttacker.level(), pAttacker, new SimpleContainer(this.getRemainderItem(pTarget.getRandom())));
            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide && pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            int expDrop = pState.getExpDrop(pLevel, pEntityLiving.getRandom(), pPos, EnchantmentHelper.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, pEntityLiving), EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, pEntityLiving));
            if (pLevel instanceof ServerLevel serverLevel) {
                ExperienceOrb.award(serverLevel, pPos.getCenter(), expDrop);
            }

            pStack.hurtAndBreak(1, pEntityLiving, (p_40992_) -> {
                Containers.dropContents(pLevel, pEntityLiving, new SimpleContainer(this.getRemainderItem(pEntityLiving.getRandom())));
                p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }

    public ItemStack getRemainderItem(RandomSource random) {
        ItemStack remainderItem = new ItemStack(ModItems.HAUNTED_METAL_SCRAP.get());
        remainderItem.setCount(random.nextIntBetweenInclusive(1, 3));
        return remainderItem;
    }
}
