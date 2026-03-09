package net.lod.ducksdelights.block.custom.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lod.ducksdelights.block.entity.GiantClamBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GiantClamRenderer implements BlockEntityRenderer<GiantClamBlockEntity> {
    private final ItemRenderer itemRenderer;

    public GiantClamRenderer(BlockEntityRendererProvider.Context pContext) {
        this.itemRenderer = pContext.getItemRenderer();
    }

    public void render(GiantClamBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        NonNullList<ItemStack> itemList = pBlockEntity.getItems();
        int blockPos = (int) pBlockEntity.getBlockPos().asLong();

        for (int slot = 0; slot < itemList.size(); ++slot) {
            ItemStack itemStack = itemList.get(slot);
            if (itemStack != ItemStack.EMPTY) {
                pPoseStack.pushPose();
                pPoseStack.translate(0.5F, 0.9F, 0.5F);
                pPoseStack.scale(0.375F, 0.375F, 0.375F);
                this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBuffer, pBlockEntity.getLevel(), blockPos + slot);
                pPoseStack.popPose();
            }
        }
    }
}
