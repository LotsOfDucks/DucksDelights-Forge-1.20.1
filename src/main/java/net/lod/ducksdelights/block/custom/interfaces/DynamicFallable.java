package net.lod.ducksdelights.block.custom.interfaces;

import net.lod.ducksdelights.entity.custom.DynamicFallingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface DynamicFallable {
    default void onLand(Level pLevel, BlockPos pPos, BlockState pState, BlockState pReplaceableState, DynamicFallingBlockEntity pFallingBlock) {
    }

    default void onBrokenAfterFall(Level pLevel, BlockPos pPos, DynamicFallingBlockEntity pFallingBlock) {
    }
}
