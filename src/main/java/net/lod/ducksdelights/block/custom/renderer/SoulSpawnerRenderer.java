package net.lod.ducksdelights.block.custom.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lod.ducksdelights.block.custom.BlazingBarrelBlock;
import net.lod.ducksdelights.block.entity.BlazingBarrelBlockEntity;
import net.lod.ducksdelights.block.entity.SoulSpawnerBlockEntity;
import net.lod.ducksdelights.block.entity.spawners.SoulSpawner;
import net.lod.ducksdelights.item.ModItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SoulSpawnerRenderer implements BlockEntityRenderer<SoulSpawnerBlockEntity> {
    private final EntityRenderDispatcher entityRenderer;
    private final ItemRenderer itemRenderer;

    public SoulSpawnerRenderer(BlockEntityRendererProvider.Context pContext) {
        this.entityRenderer = pContext.getEntityRenderer();
        this.itemRenderer = pContext.getItemRenderer();
    }

    //spawns goober
    public void render(SoulSpawnerBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        pPoseStack.translate(0.5F, 0.0F, 0.5F);
        SoulSpawner spawner = pBlockEntity.getSpawner();
        Entity displayEntity = spawner.getOrCreateDisplayEntity(pBlockEntity.getLevel(), pBlockEntity.getLevel().getRandom(), pBlockEntity.getBlockPos());
        if (displayEntity != null) {
            float shrinkRate = 0.53125F;
            float largestSizeVal = Math.max(displayEntity.getBbWidth(), displayEntity.getBbHeight());
            if ((double)largestSizeVal > 1.0) {
                shrinkRate /= largestSizeVal;
            }
            float maxSpawnDelay = pBlockEntity.getSpawner().getMaxSpawnDelay();
            float spawnDelay = pBlockEntity.getSpawner().spawnDelay;
            shrinkRate = (float) (shrinkRate * Math.max(0.05 , (1 - (spawnDelay / maxSpawnDelay))));

            pPoseStack.translate(0.0F, 0.4F, 0.0F);
            pPoseStack.mulPose(Axis.YP.rotationDegrees((float) Mth.lerp(pPartialTick, spawner.getoSpin(), spawner.getSpin()) * 10.0F));
            pPoseStack.translate(0.0F, -0.2F, 0.0F);
            pPoseStack.mulPose(Axis.XP.rotationDegrees(-30.0F));
            pPoseStack.scale(shrinkRate, shrinkRate, shrinkRate);
            this.entityRenderer.render(displayEntity, 0.0, 0.0, 0.0, 0.0F, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        } else {
            ItemStack itemStack = new ItemStack(ModItems.SOUL_PEARL.get());
            pPoseStack.pushPose();
            pPoseStack.translate(0.0F, 0.4F, 0.0F);
            float gameTime = pBlockEntity.getLevel().getGameTime() % 360;
            pPoseStack.mulPose(Axis.YP.rotationDegrees((float) Mth.lerp(pPartialTick, gameTime, gameTime + 1) * 10.0F));
            pPoseStack.scale(0.4F, 0.4F, 0.4F);
            this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBuffer, pBlockEntity.getLevel(), (int) pBlockEntity.getBlockPos().asLong());
            pPoseStack.popPose();
        }

        pPoseStack.popPose();
    }
}
