package net.lod.ducksdelights.item.custom;

import net.lod.ducksdelights.damage.ModDamageTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RadioactiveItem extends Item {
    private final ResourceKey<DamageType> damageType;

    public RadioactiveItem(Properties pProperties, ResourceKey<DamageType> damageType) {
        super(pProperties);
        this.damageType = damageType;
    }

    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        if (!level.isClientSide) {
            if (level.getGameTime() % 20L == 0L) {
                DamageSource damageSource = new DamageSource(
                        level.registryAccess()
                                .registryOrThrow(Registries.DAMAGE_TYPE)
                                .getHolder(damageType).get()
                );
                player.hurt(damageSource, 1);
            }
        }
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
    }
}
