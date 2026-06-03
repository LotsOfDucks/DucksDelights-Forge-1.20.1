package net.lod.ducksdelights.item.custom;

import net.lod.ducksdelights.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
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
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.List;

public class HauntedSteelCleaverItem extends CleaverItem {
    public HauntedSteelCleaverItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pTarget.level() instanceof ServerLevel serverLevel) {
            if (pTarget.getHealth() <= 0) {
                SimpleContainer dropsContainer = getCustomDrops(serverLevel, pTarget, pStack);
                Containers.dropContents(pAttacker.level(), pTarget, dropsContainer);
                if (pTarget instanceof Player player) {
                    SimpleContainer skullContainer = new SimpleContainer(1);
                    ItemStack itemToDrop = new ItemStack(Items.PLAYER_HEAD);
                    CompoundTag dropTag = itemToDrop.getOrCreateTag();
                    dropTag.putString("SkullOwner", player.getGameProfile().getName());
                    itemToDrop.setTag(dropTag);
                    skullContainer.addItem(itemToDrop);
                    Containers.dropContents(pAttacker.level(), player, skullContainer);

                    if (pTarget.getExperienceReward() > 0) {
                        if (pTarget.shouldDropExperience() && serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                            int reward = ForgeEventFactory.getExperienceDrop(pTarget, player, pTarget.getExperienceReward());
                            ExperienceOrb.award(serverLevel, pTarget.position(), reward);
                        }
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
