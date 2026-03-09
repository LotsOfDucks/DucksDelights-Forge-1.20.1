package net.lod.ducksdelights.block.custom.dispenser_behavior;

import net.lod.ducksdelights.block.ModBlocks;
import net.minecraft.world.level.block.DispenserBlock;

public class ModDispenserBehaviors {
    public static void register() {
        DispenserBlock.registerBehavior(ModBlocks.ROPE_LADDER.get().asItem(), new RopeLadderDispenseBehavior());
        DispenserBlock.registerBehavior(ModBlocks.ANTI_ROPE_LADDER.get().asItem(), new AntiRopeLadderDispenseBehavior());

    }
}
