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
    private float tintRed = 1.0F;
    private float tintGreen = 1.0F;
    private float tintBlue = 1.0F;

    public PearlLampBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.PEARL_LAMP_BE.get(), pPos, pBlockState);
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

    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    public void dyeLamp(DyeItem dyeItem) {
        if (this.getBlockState().getBlock() instanceof PearlLampBlock) {

            int[] newColorArray = new int[3];
            float oldRed = this.tintRed * 2;
            float oldGreen = this.tintGreen * 2;
            float oldBlue = this.tintBlue * 2;
            int colorsApplied = 0;

            float[] dyeItemColors = dyeItem.getDyeColor().getTextureDiffuseColors();
            int dyeItemRed = (int)(dyeItemColors[0] * 255.0F);
            int dyeItemGreen = (int)(dyeItemColors[1] * 255.0F);
            int dyeItemBlue = (int)(dyeItemColors[2] * 255.0F);
            int brightestValue = 0;

            if (this.hasCustomColor) {
                brightestValue += (int)(Math.max(oldRed, Math.max(oldGreen, oldBlue)) * 255.0F);
                newColorArray[0] += (int)(oldRed * 255.0F);
                newColorArray[1] += (int)(oldGreen * 255.0F);
                newColorArray[2] += (int)(oldBlue * 255.0F);
                colorsApplied += 2;
            }

            brightestValue += Math.max(dyeItemRed, Math.max(dyeItemGreen, dyeItemBlue));
            newColorArray[0] += dyeItemRed;
            newColorArray[1] += dyeItemGreen;
            newColorArray[2] += dyeItemBlue;
            ++colorsApplied;


            int newRed = newColorArray[0] / colorsApplied;
            int newGreen = newColorArray[1] / colorsApplied;
            int newBlue = newColorArray[2] / colorsApplied;
            float brightestVsApplied = (float)brightestValue / (float)colorsApplied;

            float newBrightestValue = (float)Math.max(newRed, Math.max(newGreen, newBlue));
            newRed = (int)((float)newRed * brightestVsApplied / newBrightestValue);
            newGreen = (int)((float)newGreen * brightestVsApplied / newBrightestValue);
            newBlue = (int)((float)newBlue * brightestVsApplied / newBrightestValue);

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

    public float getTintRed() {
        return this.tintRed;
    }

    public float getTintGreen() {
        return this.tintGreen;
    }

    public float getTintBlue() {
        return this.tintBlue;
    }

    public void clearDyes() {
        this.tintRed = 1.0F;
        this.tintGreen = 1.0F;
        this.tintBlue = 1.0F;
        this.hasCustomColor = false;
        this.setChanged();
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }
}
