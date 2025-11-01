package net.lod.ducksdelights.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SoulCageItem extends Item {
    private static final String TAG_SOUL = "soul";

    public SoulCageItem(Properties pProperties) {
        super(pProperties);
    }

    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        CompoundTag compoundtag = pStack.getOrCreateTag();
        if (!compoundtag.contains("soul")) {
            compoundtag.put("soul", new CompoundTag());

            pStack.save(compoundtag);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

}
