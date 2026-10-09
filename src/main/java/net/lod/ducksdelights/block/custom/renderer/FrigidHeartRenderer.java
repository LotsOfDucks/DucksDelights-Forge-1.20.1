package net.lod.ducksdelights.block.custom.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lod.ducksdelights.block.entity.FrigidHeartBlockEntity;
import net.lod.ducksdelights.block.entity.SoulSpawnerBlockEntity;
import net.lod.ducksdelights.block.entity.spawners.SoulSpawner;
import net.lod.ducksdelights.item.ModItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FrigidHeartRenderer implements BlockEntityRenderer<FrigidHeartBlockEntity> {
    private final ItemRenderer itemRenderer;

    public FrigidHeartRenderer(BlockEntityRendererProvider.Context pContext) {
        this.itemRenderer = pContext.getItemRenderer();
    }

    //spawns goober
    public void render(FrigidHeartBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        ItemStack itemStack = new ItemStack(ModItems.FRIGID_HEART_ITEM.get());
        float gameTime = pBlockEntity.getLevel().getGameTime() % 360;

        pPoseStack.translate(0.5F, 0.95F, 0.5F);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTick, gameTime, gameTime + 1) * 10.0F));
        pPoseStack.scale(0.4F, 0.4F, 0.4F);
        this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBuffer, pBlockEntity.getLevel(), (int) pBlockEntity.getBlockPos().asLong());
        pPoseStack.popPose();
    }
}
