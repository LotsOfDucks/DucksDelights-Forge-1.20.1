package net.lod.ducksdelights.block.entity;

import net.lod.ducksdelights.block.ModBlockEntities;
import net.lod.ducksdelights.block.custom.PearlLampBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class PearlLampBlockEntity extends BlockEntity {
    public boolean hasCustomColor = false;
    private float tintRed = 1;
    private float tintGreen = 1;
    private float tintBlue = 1;

    public PearlLampBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.PEARL_LAMP_BE.get(), pPos, pBlockState);
        this.hasCustomColor = false;
    }

    //mmmmm... data saving....

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putBoolean("HasColor", this.hasCustomColor);
        pTag.putFloat("TintRed", this.tintRed);
        pTag.putFloat("TintGreen", this.tintGreen);
        pTag.putFloat("TintBlue", this.tintBlue);
    }

    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.hasCustomColor = pTag.getBoolean("HasColor");
        this.tintRed = pTag.getFloat("TintRed");
        this.tintGreen = pTag.getFloat("TintGreen");
        this.tintBlue = pTag.getFloat("TintBlue");
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void dyeLamp(DyeItem dyeItem) {
        if (this.getBlockState().getBlock() instanceof PearlLampBlock) {
            int[] newColorArray = new int[3];
            float oldRed = this.tintRed;
            float oldGreen = this.tintGreen;
            float oldBlue = this.tintBlue;
            int colorsApplied = 1;
            float[] dyeItemColors = dyeItem.getDyeColor().getTextureDiffuseColors();

            int dyeItemRed = (int)(dyeItemColors[0] * 255.0F);
            int dyeItemGreen = (int)(dyeItemColors[1] * 255.0F);
            int dyeItemBlue = (int)(dyeItemColors[2] * 255.0F);
            int variable4 = 0;

            if (this.hasCustomColor) {
                variable4 += (int)(Math.max(oldRed, Math.max(oldGreen, oldBlue)) * 255.0F);
                newColorArray[0] += (int)(oldRed * 255.0F);
                newColorArray[1] += (int)(oldGreen * 255.0F);
                newColorArray[2] += (int)(oldBlue * 255.0F);
                ++colorsApplied;
            }

            variable4 += Math.max(dyeItemRed, Math.max(dyeItemGreen, dyeItemBlue));
            newColorArray[0] += dyeItemRed;
            newColorArray[1] += dyeItemGreen;
            newColorArray[2] += dyeItemBlue;


            int newRed = newColorArray[0] / colorsApplied;
            int newGreen = newColorArray[1] / colorsApplied;
            int newBlue = newColorArray[2] / colorsApplied;
            float variable20 = (float)variable4 / (float)colorsApplied;
            float variable4Again = (float)Math.max(newRed, Math.max(newGreen, newBlue));
            newRed = (int)((float)newRed * variable20 / variable4Again);
            newGreen = (int)((float)newGreen * variable20 / variable4Again);
            newBlue = (int)((float)newBlue * variable20 / variable4Again);
            this.tintRed = newRed / 255.0F;
            this.tintGreen = newGreen / 255.0F;
            this.tintBlue = newBlue / 255.0F;
            if (!this.hasCustomColor) {
                this.hasCustomColor = true;
            }
            this.setChanged();
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public int getDyeColor() {
        return Mth.color(this.tintRed, this.tintGreen, this.tintBlue);
    }

    public void clearDyes() {
        this.tintRed = 0;
        this.tintGreen = 0;
        this.tintBlue = 0;
        this.hasCustomColor = false;
        this.setChanged();
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }
}
