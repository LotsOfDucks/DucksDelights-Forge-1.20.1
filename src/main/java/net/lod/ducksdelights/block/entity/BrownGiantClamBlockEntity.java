package net.lod.ducksdelights.block.entity;

import net.lod.ducksdelights.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BrownGiantClamBlockEntity extends AbstractGiantClamBlockEntity{
    public BrownGiantClamBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GIANT_CLAM_BROWN_BE.get(), pPos, pBlockState);
    }
}
