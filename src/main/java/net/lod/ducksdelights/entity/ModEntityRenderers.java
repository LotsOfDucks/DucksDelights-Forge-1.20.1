package net.lod.ducksdelights.entity;

import net.lod.ducksdelights.entity.client.DynamicFallingBlockRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class ModEntityRenderers {
    public static void register() {
        EntityRenderers.register(ModEntities.DYNAMIC_FALLING_BLOCK.get(), DynamicFallingBlockRenderer::new);
    }
}
