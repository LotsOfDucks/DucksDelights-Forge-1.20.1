package net.lod.ducksdelights.block.entity;

import net.lod.ducksdelights.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EnderGiantClamBlockEntity extends AbstractGiantClamEnderBlockEntity{
    public EnderGiantClamBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GIANT_CLAM_ENDER_BE.get(), pPos, pBlockState);
    }
}
