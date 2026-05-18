package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlockEntities;
import net.lod.ducksdelights.block.entity.AbstractGiantClamEnderBlockEntity;
import net.lod.ducksdelights.block.entity.AbstractGiantClamNetherBlockEntity;
import net.lod.ducksdelights.block.entity.EnderGiantClamBlockEntity;
import net.lod.ducksdelights.block.entity.NetherGiantClamBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class NetherGiantClamBlock extends AbstractGiantNetherClamBlock{
    public NetherGiantClamBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new NetherGiantClamBlockEntity(blockPos, blockState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.GIANT_CLAM_NETHER_BE.get(), pLevel.isClientSide ? AbstractGiantClamNetherBlockEntity::clientTick : AbstractGiantClamNetherBlockEntity::serverTick);
    }
}
