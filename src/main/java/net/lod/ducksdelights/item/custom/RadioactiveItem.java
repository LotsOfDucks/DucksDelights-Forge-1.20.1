package net.lod.ducksdelights.item.custom;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class RadioactiveItem extends Item {
    private final ResourceKey<DamageType> damageType;
    public int burnTime;

    private boolean hasDamaged;

    public RadioactiveItem(Properties pProperties, int burnTime, ResourceKey<DamageType> damageType) {
        super(pProperties);
        this.burnTime = burnTime;
        this.damageType = damageType;
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return this.burnTime;
    }

    //this was a bitch to get working correct, having multiple stacks in the inventory cause so many problems
    //im free now *i hope*
    //just realized while typing that this only works for player inventories and not other entity inventories.
    //I'm not fixing that

    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        if (!level.isClientSide) {
            if (level.getGameTime() % getTicksUntilDamage(player) == 0L) {
                if (!hasDamaged) {
                    this.damageTrigger(level, player);
                    hasDamaged = true;
                }
            } else {
                if (hasDamaged) {
                    hasDamaged = false;
                }
            }
        }
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
    }

    public static long getTicksUntilDamage(Player player) {
        long ticksUntilDamage = 200;
        for (int slot = 0; slot <= player.getInventory().items.size(); slot++) {
            ItemStack targetItemStack = player.getInventory().getItem(slot);
            if (targetItemStack.getItem() instanceof RadioactiveItem) {
                long itemStackFullness = targetItemStack.getCount() / targetItemStack.getMaxStackSize();
                ticksUntilDamage *= Math.max(((1 - (itemStackFullness))), 1);
            }
        }
        return ticksUntilDamage;
    }

    public void damageTrigger(Level level, Player player) {
        DamageSource damageSource = new DamageSource(
                level.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolder(damageType).get()
        );
        player.hurt(damageSource, 1);
    }
}
