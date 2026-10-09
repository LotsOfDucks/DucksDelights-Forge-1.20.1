package net.lod.ducksdelights.item;

import net.lod.ducksdelights.util.ModTags;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.alchemy.PotionUtils;

public class ModItemProperties {
    @SuppressWarnings("removal")
    public static void register() {
        ItemProperties.register(ModItems.PEARL_FISHING_ROD.get(), new ResourceLocation("cast"), (itemStack, client, player, integer) -> {
            if (player == null) {
                return 0.0F;
            } else {
                boolean isMainhand = player.getMainHandItem() == itemStack;
                boolean isOffhand = player.getOffhandItem() == itemStack;
                if (player.getMainHandItem().getItem() instanceof FishingRodItem) {
                    isOffhand = false;
                }

                return (isMainhand || isOffhand) && player instanceof Player && ((Player)player).fishing != null ? 1.0F : 0.0F;
            }
        });

        ItemProperties.registerGeneric(new ResourceLocation("potion_dipped_lod"), (itemStack, client, player, integer) -> {
            if (player == null) {
                return 0.0F;
            } else if (itemStack.is(ModTags.Items.POTION_DIPPABLE)) {
                boolean hasEffects = !PotionUtils.getCustomEffects(itemStack).isEmpty();
                return hasEffects ? 1.0F : 0.0F;
            } else {
                return 0.0F;
            }
        });
    }
}
