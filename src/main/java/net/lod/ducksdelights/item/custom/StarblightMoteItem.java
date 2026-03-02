package net.lod.ducksdelights.item.custom;

import net.lod.ducksdelights.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class StarblightMoteItem extends RadioactiveItem {
    public StarblightMoteItem(Properties pProperties, ResourceKey<DamageType> damageType) {
        super(pProperties, damageType);
    }

    //pretty!

    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        if (level.getBlockState(blockpos).is(Blocks.SPAWNER)) {
            if (!level.isClientSide) {
                level.setBlock(blockpos, ModBlocks.BLIGHTED_SPAWNER_BLOCK.get().defaultBlockState(), 3);
                level.playSound(null, blockpos, SoundEvents.SCULK_BLOCK_CHARGE, SoundSource.BLOCKS, 1F, 1F);
                pContext.getItemInHand().shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }
}
