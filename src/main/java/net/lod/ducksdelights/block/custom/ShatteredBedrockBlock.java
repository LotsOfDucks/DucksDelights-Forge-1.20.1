package net.lod.ducksdelights.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class ShatteredBedrockBlock extends FallingBlock {
    private final int dustColor;

    public ShatteredBedrockBlock(int pDustColor, Properties pProperties) {
        super(pProperties);
        this.dustColor = pDustColor;
    }

    protected void falling(FallingBlockEntity pFallingEntity) {
        pFallingEntity.setHurtsEntities(4.0F, 80);
    }

    public void onLand(Level pLevel, BlockPos pPos, BlockState pState, BlockState pReplaceableState, FallingBlockEntity pFallingBlock) {
        if (!pFallingBlock.isSilent()) {
            pLevel.playSound(null, pPos, SoundEvents.DEEPSLATE_TILES_PLACE, SoundSource.BLOCKS);
        }
    }

    public int getDustColor(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return this.dustColor;
    }
}
