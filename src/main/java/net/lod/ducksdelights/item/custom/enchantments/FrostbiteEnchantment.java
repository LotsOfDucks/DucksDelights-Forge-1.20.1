package net.lod.ducksdelights.item.custom.enchantments;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Map;

public class FrostbiteEnchantment extends Enchantment {
    protected FrostbiteEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public int getMinCost(int pEnchantmentLevel) {
        return 10 + 20 * (pEnchantmentLevel - 1);
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return super.getMinCost(pEnchantmentLevel) + 50;
    }

    public int getMaxLevel() {
        return 3;
    }

    public boolean canEnchant(ItemStack pStack) {
        return pStack.getItem() instanceof ArmorItem || super.canEnchant(pStack);
    }

    public void doPostHurt(LivingEntity pUser, Entity pAttacker, int pLevel) {
        RandomSource random = pUser.getRandom();
        Map.Entry<EquipmentSlot, ItemStack> armorItem = EnchantmentHelper.getRandomItemWith(ModEnchantments.FROSTBITE.get(), pUser);
        if (shouldHit(pLevel, random)) {
            int ticksToFreeze = 0;
            if (pAttacker.getTicksFrozen() <= 200) {
                ticksToFreeze = getFreezeTicks(pLevel, random);
            }
            pAttacker.setTicksFrozen(pAttacker.getTicksFrozen() + ticksToFreeze);

            if (pAttacker.level() instanceof ServerLevel serverLevel) {
                spawnFreezeParticles(serverLevel, pAttacker);
            }

            if (armorItem != null) {
                armorItem.getValue().hurtAndBreak(1, pUser, (p_45208_) -> {
                    p_45208_.broadcastBreakEvent(armorItem.getKey());
                });
            }
        }

    }

    public static void spawnFreezeParticles(ServerLevel serverLevel, Entity pAttacker) {
        double xWidth = pAttacker.getBoundingBox().getXsize() / 2;
        double yWidth = pAttacker.getBoundingBox().getYsize() / 4;
        double zWidth = pAttacker.getBoundingBox().getZsize() / 2;
        double x = pAttacker.position().x();
        double y = pAttacker.position().y() + yWidth;
        double z = pAttacker.position().z();

        for (int partNum = 0; partNum <= 16; partNum++) {
            serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, x, y + yWidth, z, 1, (pAttacker.level().random.nextIntBetweenInclusive(-1, 1) * xWidth), (pAttacker.level().random.nextIntBetweenInclusive(-1, 1) * 1.5 * yWidth), (pAttacker.level().random.nextIntBetweenInclusive(-1, 1) * zWidth), 0);
        }
    }

    public static boolean shouldHit(int pLevel, RandomSource pRandom) {
        if (pLevel <= 0) {
            return false;
        } else {
            return pRandom.nextFloat() < 0.2F * (float)pLevel;
        }
    }

    public static int getFreezeTicks(int pLevel, RandomSource pRandom) {
        int ticksToFreeze = pLevel > 10 ? 420 : 20 + pRandom.nextInt(pLevel * 20);
        return Math.min(ticksToFreeze, 420);
    }
}
