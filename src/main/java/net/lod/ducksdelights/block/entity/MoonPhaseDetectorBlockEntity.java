package net.lod.ducksdelights.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MoonPhaseDetectorBlockEntity extends BlockEntity {

    public MoonPhaseDetectorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.MOON_PHASE_DETECTOR_BE.get(), pPos, pBlockState);
    }
}
