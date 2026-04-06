package net.lod.ducksdelights.block.entity;

import net.lod.ducksdelights.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PlayerDetectorBlockEntity extends BlockEntity {
    public PlayerDetectorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.PLAYER_DETECTOR_BE.get(), pPos, pBlockState);
    }
}
