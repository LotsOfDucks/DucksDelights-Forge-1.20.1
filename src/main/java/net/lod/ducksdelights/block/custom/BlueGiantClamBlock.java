package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlockEntities;
import net.lod.ducksdelights.block.entity.AbstractGiantClamBlockEntity;
import net.lod.ducksdelights.block.entity.BlueGiantClamBlockEntity;
import net.lod.ducksdelights.block.entity.BrownGiantClamBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlueGiantClamBlock extends AbstractGiantClamBlock{
    public BlueGiantClamBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlueGiantClamBlockEntity(blockPos, blockState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.GIANT_CLAM_BLUE_BE.get(), pLevel.isClientSide ? AbstractGiantClamBlockEntity::clientTick : AbstractGiantClamBlockEntity::serverTick);
    }
}
