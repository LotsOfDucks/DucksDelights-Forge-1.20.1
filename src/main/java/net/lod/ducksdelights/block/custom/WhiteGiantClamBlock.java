package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlockEntities;
import net.lod.ducksdelights.block.entity.AbstractGiantClamBlockEntity;
import net.lod.ducksdelights.block.entity.GreenGiantClamBlockEntity;
import net.lod.ducksdelights.block.entity.WhiteGiantClamBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class WhiteGiantClamBlock extends AbstractGiantClamBlock{
    public WhiteGiantClamBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WhiteGiantClamBlockEntity(blockPos, blockState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.GIANT_CLAM_WHITE_BE.get(), pLevel.isClientSide ? AbstractGiantClamBlockEntity::clientTick : AbstractGiantClamBlockEntity::serverTick);
    }
}
