package net.lod.ducksdelights.item.custom;

import net.lod.ducksdelights.damage.ModDamageTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BedrockChipsItem extends Item {
    public BedrockChipsItem(Properties pProperties) {
        super(pProperties);
    }

    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        ItemStack noChange = super.finishUsingItem(pStack, pLevel, pEntityLiving);
        DamageSource damageSource = new DamageSource(
                pLevel.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolder(ModDamageTypes.BEDROCK_CHIPS).get()
        );
        pEntityLiving.hurt(damageSource, 6);
        return noChange;
    }
}
