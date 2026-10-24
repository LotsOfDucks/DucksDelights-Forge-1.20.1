package net.lod.ducksdelights.worldgen.dimensions;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class ModDimensionSpecialEffects extends DimensionSpecialEffects {
    public ModDimensionSpecialEffects(float pCloudLevel, boolean pHasGround, SkyType pSkyType, boolean pForceBrightLightmap, boolean pConstantAmbientLight) {
        super(pCloudLevel, pHasGround, pSkyType, pForceBrightLightmap, pConstantAmbientLight);
    }

    public Vec3 getBrightnessDependentFogColor(Vec3 colorVec, float daylightBrightness) {
        float[] dyeItemColors = DyeColor.CYAN.getTextureDiffuseColors();
        //return new Vec3(dyeItemColors[0], dyeItemColors[1], dyeItemColors[2]).scale(0.5).scale(daylightBrightness);
        return colorVec.scale(0.15000000596046448);
    }

    public boolean isFoggyAt(int p_108891_, int p_108892_) {
        return false;
    }

    @Nullable
    public float[] getSunriseColor(float p_108888_, float p_108889_) {
        return null;
    }

    @Override
    public boolean renderSky(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
        return ModDimSkyRenderer.renderSky(poseStack, projectionMatrix, partialTick, camera, isFoggy, setupFog);
    }
}
