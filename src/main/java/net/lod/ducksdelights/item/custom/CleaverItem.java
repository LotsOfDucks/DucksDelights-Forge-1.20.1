package net.lod.ducksdelights.item.custom;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.lod.ducksdelights.entity.mobeffects.ModMobEffects;
import net.lod.ducksdelights.item.custom.enchantments.ModEnchantments;
import net.lod.ducksdelights.util.ModTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class CleaverItem extends SwordItem {

    public CleaverItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof LivingEntity) {
            if (((LivingEntity) entity).attackable()) {
                if (player.getLookAngle().normalize().dot(entity.getLookAngle().normalize()) >= 0.4 && player.getAttackStrengthScale(0.5F) > 0.9F) {
                    player.crit(entity);
                    player.sweepAttack();

                    int enchantLevel = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.BLOODLUST.get(), stack);

                    player.addEffect(new MobEffectInstance(ModMobEffects.VANITY.get(), 20 + (enchantLevel * 20)));
                }
            }
        }

        return super.onLeftClickEntity(stack, player, entity);
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
                }
            }
        }

        pStack.hurtAndBreak(1, pAttacker, (entity) -> {
            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    public SimpleContainer getCustomDrops(ServerLevel serverLevel, LivingEntity pTarget, ItemStack weapon) {
        LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(pTarget.getLootTable());
        LootParams.Builder lootparams$builder = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.THIS_ENTITY, pTarget).withParameter(LootContextParams.ORIGIN, pTarget.position()).withParameter(LootContextParams.DAMAGE_SOURCE, pTarget.getLastDamageSource()).withOptionalParameter(LootContextParams.KILLER_ENTITY, pTarget.getLastDamageSource().getEntity()).withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, pTarget.getLastDamageSource().getDirectEntity());
        if (pTarget.getLastHurtByMob() != null) {
            if (pTarget.getLastHurtByMob() instanceof Player player) {
                lootparams$builder = lootparams$builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player).withLuck(player.getLuck());
            }
        }

        LootParams lootparams = lootparams$builder.create(LootContextParamSets.ENTITY);

        ObjectArrayList<ItemStack> lootList = lootTable.getRandomItems(lootparams);
        SimpleContainer dropsContainer = new SimpleContainer(lootList.size() * 2);

        int enchantLevel = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.BONE_COLLECTION.get(), weapon);
        boolean hasBoneCollecting = enchantLevel > 0;

        for (ItemStack lootItemStack : lootList) {
            if (lootItemStack.isEdible()) {
                if (lootItemStack.getFoodProperties(null).isMeat()) {
                    dropsContainer.addItem(lootItemStack);

                    if (hasBoneCollecting) {
                        int count = serverLevel.random.nextInt(0, enchantLevel + 1);
                        dropsContainer.addItem(new ItemStack(Items.BONE, count));
                    }
                }
            }
        }
        return dropsContainer;
    }

    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        if (pState.is(Blocks.BONE_BLOCK)) {
            return 15.0F;
        } else {
            return pState.is(ModTags.Blocks.CLEAVER_EFFICIENT) ? 1.5F : 1.0F;
        }
    }

    public boolean isCorrectToolForDrops(BlockState pBlock) {
        return pBlock.is(ModTags.Blocks.CLEAVER_DROPS);
    }

    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return true;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return toolAction == ToolActions.SWORD_DIG;
    }
}
