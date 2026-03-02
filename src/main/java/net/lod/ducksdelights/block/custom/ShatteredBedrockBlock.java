package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.damage.ModDamageTypes;
import net.lod.ducksdelights.entity.custom.DynamicFallingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ShatteredBedrockBlock extends DynamicFallingBlock {
    private final int dustColor;

    public ShatteredBedrockBlock(int pDustColor, Properties pProperties) {
        super(pProperties, 2);
        this.dustColor = pDustColor;
    }

    //bedrock gravel bedrock gravel

    protected void newEntity(DynamicFallingBlockEntity pFallingEntity) {
        pFallingEntity.setHurtsEntities(4.0F, 80);
    }

    public void onLand(Level pLevel, BlockPos pPos, BlockState pState, BlockState pReplaceableState, DynamicFallingBlockEntity pFallingBlock) {
        if (!pFallingBlock.isSilent()) {
            pLevel.playSound(null, pPos, SoundEvents.DEEPSLATE_TILES_PLACE, SoundSource.BLOCKS);
        }
    }

    public int getDustColor(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return this.dustColor;
    }
}
