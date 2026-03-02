package net.lod.ducksdelights.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lod.ducksdelights.entity.custom.DynamicFallingBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.RenderTypeHelper;
import net.minecraftforge.client.model.data.ModelData;

import java.util.Iterator;

public class DynamicFallingBlockRenderer extends EntityRenderer<DynamicFallingBlockEntity> {
    private final BlockRenderDispatcher dispatcher;

    public DynamicFallingBlockRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.shadowRadius = 0.5F;
        this.dispatcher = pContext.getBlockRenderDispatcher();
    }

    public void render(DynamicFallingBlockEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        BlockState blockstate = pEntity.getBlockState();
        if (blockstate.getRenderShape() == RenderShape.MODEL) {
            Level level = pEntity.level();
            if (blockstate != level.getBlockState(pEntity.blockPosition()) && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                pPoseStack.pushPose();
                BlockPos blockpos = BlockPos.containing(pEntity.getX(), pEntity.getBoundingBox().maxY, pEntity.getZ());
                pPoseStack.translate(-0.5, 0.0, -0.5);
                BakedModel model = this.dispatcher.getBlockModel(blockstate);
                Iterator var11 = model.getRenderTypes(blockstate, RandomSource.create(blockstate.getSeed(pEntity.getStartPos())), ModelData.EMPTY).iterator();

                while(var11.hasNext()) {
                    RenderType renderType = (RenderType)var11.next();
                    renderType = RenderTypeHelper.getMovingBlockRenderType(renderType);
                    this.dispatcher.getModelRenderer().tesselateBlock(level, model, blockstate, blockpos, pPoseStack, pBuffer.getBuffer(renderType), false, RandomSource.create(), blockstate.getSeed(pEntity.getStartPos()), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
                }

                pPoseStack.popPose();
                super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
            }
        }

    }

    public ResourceLocation getTextureLocation(DynamicFallingBlockEntity pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
