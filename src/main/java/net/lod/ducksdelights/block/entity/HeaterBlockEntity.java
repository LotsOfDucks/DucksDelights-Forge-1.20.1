package net.lod.ducksdelights.block.entity;

import net.lod.ducksdelights.block.ModBlockEntities;
import net.lod.ducksdelights.block.custom.HeaterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HeaterBlockEntity extends BlockEntity {
    private int litDuration;

    public HeaterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.HEATER_BE.get(), pPos, pBlockState);
        this.litDuration = 0;
    }

    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.litDuration = pTag.getInt("LitDuration");
    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("LitDuration", this.litDuration);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, HeaterBlockEntity blockEntity) {
        boolean isLit = false;
        if (blockEntity.litDuration > 0) {
            --blockEntity.litDuration;
            if (level.getBlockEntity(blockPos.above()) instanceof AbstractFurnaceBlockEntity targetFurnace) {
                AbstractFurnaceBlockEntity.serverTick(level, blockPos.above(), level.getBlockState(blockPos.above()), targetFurnace);
            }
            isLit = true;

        }
        if (blockState.getValue(HeaterBlock.LIT) != isLit) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(HeaterBlock.LIT, isLit));
        }
        if (isLit) {
            setChanged(level, blockPos, blockState);
        }
    }

    public int getLitDuration() {
        return this.litDuration;
    }

    public void addFuelTime(Integer fuelValue, HeaterBlockEntity blockEntity) {
        blockEntity.litDuration += fuelValue;
    }
}
