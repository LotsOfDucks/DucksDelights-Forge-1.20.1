package net.lod.ducksdelights.block.custom.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lod.ducksdelights.block.custom.BlazingBarrelBlock;
import net.lod.ducksdelights.block.entity.BlazingBarrelBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlazingBarrelRenderer implements BlockEntityRenderer<BlazingBarrelBlockEntity> {
    private static final float SIZE = 0.375F;
    private final ItemRenderer itemRenderer;

    public BlazingBarrelRenderer(BlockEntityRendererProvider.Context pContext) {
        this.itemRenderer = pContext.getItemRenderer();
    }

    public void render(BlazingBarrelBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        NonNullList<ItemStack> itemList = pBlockEntity.getItems();
        int blockPos = (int)pBlockEntity.getBlockPos().asLong();
        float yShift = switch (pBlockEntity.getBlockState().getValue(BlazingBarrelBlock.FULLNESS)) {
            case 2 -> 0.18825F;
            case 3 -> 0.25075F;
            case 4 -> 0.31325F;
            case 5 -> 0.37575F;
            case 6 -> 0.43825F;
            case 7 -> 0.50075F;
            case 8 -> 0.56325F;
            case 9 -> 0.62575F;
            case 10 -> 0.68825F;
            case 11 -> 0.75075F;
            case 12 -> 0.81325F;
            case 13 -> 0.87575F;
            case 14 -> 0.93825F;
            case 15 -> 1.00075F;
            default -> 0.12575F;
        };


        for(int slot = 0; slot < itemList.size(); ++slot) {
            ItemStack $$10 = itemList.get(slot);
            if ($$10 != ItemStack.EMPTY) {
                pPoseStack.pushPose();
                pPoseStack.translate(0.5F, yShift, 0.5F);
                Direction directionRotation = Direction.from2DDataValue((slot) % 4);
                float rotDegree = -directionRotation.toYRot();
                pPoseStack.mulPose(Axis.YP.rotationDegrees(rotDegree));
                pPoseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                pPoseStack.translate(-0.2F, -0.2F, 0.0F);
                pPoseStack.scale(0.375F, 0.375F, 0.375F);
                this.itemRenderer.renderStatic($$10, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBuffer, pBlockEntity.getLevel(), blockPos + slot);
                pPoseStack.popPose();
            }
        }

    }
}
