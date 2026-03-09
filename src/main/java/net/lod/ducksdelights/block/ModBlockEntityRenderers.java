package net.lod.ducksdelights.block;

import net.lod.ducksdelights.block.custom.renderer.BlazingBarrelRenderer;
import net.lod.ducksdelights.block.custom.renderer.GiantClamRenderer;
import net.lod.ducksdelights.block.custom.renderer.SoulSpawnerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class ModBlockEntityRenderers {
    public static void register() {
        BlockEntityRenderers.register(ModBlockEntities.BLAZING_BARREL_DETECTOR_BE.get(), BlazingBarrelRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.SOUL_SPAWNER_BE.get(), SoulSpawnerRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.GIANT_CLAM_BE.get(), GiantClamRenderer::new);
    }
}
