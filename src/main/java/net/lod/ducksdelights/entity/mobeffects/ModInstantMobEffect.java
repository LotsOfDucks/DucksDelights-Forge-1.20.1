package net.lod.ducksdelights.entity.mobeffects;

import net.minecraft.world.effect.MobEffectCategory;

public class ModInstantMobEffect extends ModMobEffect{
    protected ModInstantMobEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public boolean isInstantenous() {
        return true;
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration >= 1;
    }
}
