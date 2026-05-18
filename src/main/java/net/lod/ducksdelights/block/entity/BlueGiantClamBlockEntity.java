package net.lod.ducksdelights.block.entity;

import net.lod.ducksdelights.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlueGiantClamBlockEntity extends AbstractGiantClamBlockEntity{
    public BlueGiantClamBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GIANT_CLAM_BLUE_BE.get(), pPos, pBlockState);
    }
}
