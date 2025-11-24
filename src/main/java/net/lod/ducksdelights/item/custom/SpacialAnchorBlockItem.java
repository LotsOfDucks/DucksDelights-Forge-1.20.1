package net.lod.ducksdelights.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SpacialAnchorBlockItem extends BlockItem {
    public SpacialAnchorBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack heldItem = pPlayer.getItemInHand(pHand);
        if (pLevel.isClientSide) {
            return InteractionResultHolder.pass(heldItem);
        } else {

            pPlayer.getLookAngle();
            Vec3 playerEyePosition = pPlayer.getEyePosition();
            Vec3i playerLookDirection = new Vec3i((int) pPlayer.getLookAngle().x, (int) pPlayer.getLookAngle().y, (int) pPlayer.getLookAngle().z);
            BlockState targetPlaceState = pLevel.getBlockState(new BlockPos((int) playerEyePosition.z, (int) playerEyePosition.y, (int) playerEyePosition.z).offset(playerLookDirection.relative(Direction.NORTH, 4)));
            return InteractionResultHolder.pass(heldItem);
        }
    }
}
