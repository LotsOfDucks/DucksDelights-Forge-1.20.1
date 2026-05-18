package net.lod.ducksdelights.block;

import net.lod.ducksdelights.block.custom.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class ModBlockEntityRenderers {
    public static void register() {
        BlockEntityRenderers.register(ModBlockEntities.BLAZING_BARREL_DETECTOR_BE.get(), BlazingBarrelRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.SOUL_SPAWNER_BE.get(), SoulSpawnerRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.GIANT_CLAM_BROWN_BE.get(), GiantClamRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.GIANT_CLAM_BLUE_BE.get(), GiantClamRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.GIANT_CLAM_GREEN_BE.get(), GiantClamRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.GIANT_CLAM_WHITE_BE.get(), GiantClamRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.GIANT_CLAM_NETHER_BE.get(), GiantClamNetherRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.GIANT_CLAM_ENDER_BE.get(), GiantClamEnderRenderer::new);
    }
}
