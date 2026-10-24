package net.lod.ducksdelights.worldgen.dimensions;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class ModDimensionSpecialEffects extends DimensionSpecialEffects {
    public ModDimensionSpecialEffects(float pCloudLevel, boolean pHasGround, SkyType pSkyType, boolean pForceBrightLightmap, boolean pConstantAmbientLight) {
        super(pCloudLevel, pHasGround, pSkyType, pForceBrightLightmap, pConstantAmbientLight);
    }

    public Vec3 getBrightnessDependentFogColor(Vec3 colorVec, float daylightBrightness) {
        float[] dyeItemColors = DyeColor.CYAN.getTextureDiffuseColors();
        return new Vec3(dyeItemColors[0], dyeItemColors[1], dyeItemColors[2]).scale(0.5).scale(daylightBrightness);
        //return colorVec.scale(1);
    }

    public boolean isFoggyAt(int p_108891_, int p_108892_) {
        return false;
    }

    @Nullable
    public float[] getSunriseColor(float p_108888_, float p_108889_) {
        return null;
    }
}
