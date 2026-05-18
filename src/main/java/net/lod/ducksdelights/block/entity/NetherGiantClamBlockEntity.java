package net.lod.ducksdelights.block.entity;

import net.lod.ducksdelights.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class NetherGiantClamBlockEntity extends AbstractGiantClamNetherBlockEntity{
    public NetherGiantClamBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GIANT_CLAM_NETHER_BE.get(), pPos, pBlockState);
    }
}
