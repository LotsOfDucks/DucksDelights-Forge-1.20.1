package net.lod.ducksdelights.block.custom.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lod.ducksdelights.block.ModBlockStateProperties;
import net.lod.ducksdelights.block.entity.BlightedSpawnerBlockEntity;
import net.lod.ducksdelights.block.entity.spawners.BlightedSpawner;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.Shapes;

public class BlightedSpawnerRenderer implements BlockEntityRenderer<BlightedSpawnerBlockEntity> {
    private final EntityRenderDispatcher entityRenderer;

    public BlightedSpawnerRenderer(BlockEntityRendererProvider.Context pContext) {
        this.entityRenderer = pContext.getEntityRenderer();
    }

    //spawns goober
    public void render(BlightedSpawnerBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        pPoseStack.translate(0.5F, 0.0F, 0.5F);
        BlightedSpawner spawner = pBlockEntity.getSpawner();
        Entity displayEntity = spawner.getOrCreateDisplayEntity(pBlockEntity.getLevel(), pBlockEntity.getLevel().getRandom(), pBlockEntity.getBlockPos());
        if (displayEntity != null) {
            float shrinkRate = 0.53125F;
            float largestSizeVal = Math.max(displayEntity.getBbWidth(), displayEntity.getBbHeight());
            if ((double)largestSizeVal > 1.0) {
                shrinkRate /= largestSizeVal;
            }

            pPoseStack.translate(0.0F, 0.4F, 0.0F);
            pPoseStack.mulPose(Axis.YP.rotationDegrees((float) Mth.lerp(pPartialTick, spawner.getoSpin(), spawner.getSpin()) * 10.0F));
            pPoseStack.translate(0.0F, -0.2F, 0.0F);
            pPoseStack.mulPose(Axis.XP.rotationDegrees(-30.0F));
            pPoseStack.scale(shrinkRate, shrinkRate, shrinkRate);
            this.entityRenderer.render(displayEntity, 0.0, 0.0, 0.0, 0.0F, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        }

        pPoseStack.popPose();
    }
}
