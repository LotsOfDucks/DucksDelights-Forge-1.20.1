package net.lod.ducksdelights.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AdderBlockEntity extends BlockEntity {
    private int output;

    public AdderBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ADDER_BE.get(), pPos, pBlockState);
    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("OutputSignal", this.output);
    }

    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.output = pTag.getInt("OutputSignal");
    }

    public int getOutputSignal() {
        return this.output;
    }

    public void setOutputSignal(int pOutput) {
        this.output = pOutput;
    }
}
