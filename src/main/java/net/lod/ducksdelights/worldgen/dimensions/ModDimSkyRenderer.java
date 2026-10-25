package net.lod.ducksdelights.worldgen.dimensions;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.lod.ducksdelights.DucksDelights;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.ArrayList;

@OnlyIn(Dist.CLIENT)
public class ModDimSkyRenderer {
    @SuppressWarnings("removal")
    private static final ResourceLocation END_SKY_LOCATION = new ResourceLocation("textures/environment/end_sky.png");
    @SuppressWarnings("removal")
    private static final ResourceLocation EYE_LOCATION = new ResourceLocation(DucksDelights.MOD_ID, "textures/environment/pocket_dim_eye.png");

    @Nullable
    private static VertexBuffer skyBuffer;
    @Nullable
    private static VertexBuffer darkBuffer;
    private static VertexBuffer starBuffer;

    public ModDimSkyRenderer() {
        this.createStars();
    }

    public static boolean renderSky(PoseStack pPoseStack, Matrix4f pProjectionMatrix, float pPartialTick, Camera pCamera, boolean pIsFoggy, Runnable pSkyFogSetup) {
        //render background
        renderEndSky(pPoseStack, pProjectionMatrix, pSkyFogSetup);
        
        //define stuff
        Matrix4f matrix4f1 = pPoseStack.last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();

        //begin render setup
        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        
        //get sun info
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, EYE_LOCATION);
        float size = 30.0F;

        //define sun render location
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1, -size, 200.0F, -size).uv(0.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, size, 200.0F, -size).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, size, 200.0F, size).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, -size, 200.0F, size).uv(0.0F, 1.0F).endVertex();

        //finish rendering sun
        BufferUploader.drawWithShader(bufferbuilder.end());
        pPoseStack.pushPose();

        //try render stars
        if (GameRenderer.getPositionShader() != null) {
            RenderSystem.depthMask(true);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            FogRenderer.setupNoFog();
            starBuffer.bind();
            starBuffer.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());

            //finish rendering starts
            VertexBuffer.unbind();
            pSkyFogSetup.run();
            RenderSystem.depthMask(false);
        }

        //reset render after sky rendering
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        pPoseStack.popPose();

        //final render reset
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.depthMask(true);
        
        return true;
    }

    private static void renderEndSky(PoseStack pPoseStack, Matrix4f pProjectionMatrix, Runnable pSkyFogSetup) {
        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, END_SKY_LOCATION);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        for(int i = 0; i < 6; ++i) {
            pPoseStack.pushPose();
            if (i == 1) {
                pPoseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
            }

            if (i == 2) {
                pPoseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-90.0F));
            }

            if (i == 3) {
                pPoseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(180.0F));
            }

            if (i == 4) {
                pPoseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(90.0F));
            }

            if (i == 5) {
                pPoseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(-90.0F));
            }

            Matrix4f matrix4f = pPoseStack.last().pose();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferbuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).uv(0.0F, 0.0F).color(40, 40, 40, 255).endVertex();
            bufferbuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).uv(0.0F, 16.0F).color(40, 40, 40, 255).endVertex();
            bufferbuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).uv(16.0F, 16.0F).color(40, 40, 40, 255).endVertex();
            bufferbuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).uv(16.0F, 0.0F).color(40, 40, 40, 255).endVertex();
            tesselator.end();
            pPoseStack.popPose();
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

    // [VanillaCopy] LevelRenderer.createStars
    private void createStars() {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        if (starBuffer != null) {
            starBuffer.close();
        }

        starBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        BufferBuilder.RenderedBuffer renderedBuffer = this.drawStars(bufferbuilder);
        starBuffer.bind();
        starBuffer.upload(renderedBuffer);
        VertexBuffer.unbind();

    }

    // [VanillaCopy] of LevelRenderer.drawStars but with double the number of them
    private BufferBuilder.RenderedBuffer drawStars(BufferBuilder bufferBuilder) {
        RandomSource random = RandomSource.create(10842L);
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        // TF - 1500 -> 3000
        for (int i = 0; i < 6000; ++i) {
            double d0 = random.nextFloat() * 2.0F - 1.0F;
            double d1 = random.nextFloat() * 2.0F - 1.0F;
            double d2 = random.nextFloat() * 2.0F - 1.0F;
            double d3 = (0.15F + random.nextFloat() * 0.1F); // also scaling factor
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d4 < 1.0D && d4 > 0.01D) {
                d4 = 1.0D / Math.sqrt(d4);
                d0 *= d4;
                d1 *= d4;
                d2 *= d4;

                //star squash and stretch
                double startX = d0 * 100.0D;
                double startY = d1 * 100.0D;
                double startZ = d2 * 100.0D;

                double d8 = Math.atan2(d0, d2); //horizontal stretch
                double d9 = Math.sin(d8);
                double d10 = Math.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1); // vertical stretch from below
                double d12 = Math.sin(d11); // contextual vertical stretch at horizontal
                double d13 = Math.cos(d11);
                double d14 = random.nextDouble() * Math.PI * 2.0D; //rotation
                double d15 = Math.sin(d14);
                double d16 = Math.cos(d14);

                for (int j = 0; j < 4; ++j) {
                    double d18 = ((j & 2) - 1) * d3;
                    double d19 = ((j + 1 & 2) - 1) * d3;
                    double d21 = d18 * d16 - d19 * d15;
                    double d22 = d19 * d16 + d18 * d15;
                    double endY = d21 * d12 + 0.0D * d13; // universal vertical compression at horizontal
                    double d24 = 0.0D * d12 - d21 * d13;
                    double endX = d24 * d9 - d22 * d10;
                    double endZ = 0; //d22 * d9 + d24 * d10
                    bufferBuilder.vertex(startX + endX, startY + endY, startZ + endZ).endVertex();
                }
            }
        }

        return bufferBuilder.end();
    }
}
