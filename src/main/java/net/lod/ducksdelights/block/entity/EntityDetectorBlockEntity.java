package net.lod.ducksdelights.block.entity;

import net.lod.ducksdelights.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EntityDetectorBlockEntity extends BlockEntity {
    public EntityDetectorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ENTITY_DETECTOR_BE.get(), pPos, pBlockState);
    }


}
