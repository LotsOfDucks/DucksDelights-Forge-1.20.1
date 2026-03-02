package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.custom.interfaces.DynamicFallable;
import net.lod.ducksdelights.damage.ModDamageTypes;
import net.lod.ducksdelights.entity.custom.DynamicFallingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.ref.Reference;

public class DynamicFallingBlock extends Block implements DynamicFallable {
    public int DELAY_AFTER_PLACE;

    public DynamicFallingBlock(Properties pProperties, int fallDelay) {
        super(pProperties);
        this.DELAY_AFTER_PLACE = fallDelay;
        this.registerDefaultState(this.defaultBlockState());
    }

    //WIP




    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        pLevel.scheduleTick(pPos, this, this.getDelayAfterPlace());
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        pLevel.scheduleTick(pCurrentPos, this, this.getDelayAfterPlace());
        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    //BROKEN ATM
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (isFree(pLevel.getBlockState(pPos.below())) && pPos.getY() >= pLevel.getMinBuildHeight()) {
            Block thisBlock = pState.getBlock();
            DamageSource damageSource;
            damageSource = pLevel.damageSources().generic();
            if (thisBlock instanceof ShatteredBedrockBlock) {
                damageSource = new DamageSource(
                        pLevel.registryAccess()
                                .registryOrThrow(Registries.DAMAGE_TYPE)
                                .getHolder(ModDamageTypes.SHATTERED_BEDROCK).get()
                );
            }
            DynamicFallingBlockEntity dynamicFallingBlockEntity = DynamicFallingBlockEntity.fall(pLevel, pPos, pState, damageSource);
            this.newEntity(dynamicFallingBlockEntity);
        }
    }

    protected void newEntity(DynamicFallingBlockEntity pEntity) {
    }

    protected int getDelayAfterPlace() {
        return this.DELAY_AFTER_PLACE;
    }

    public static boolean isFree(BlockState pState) {
        return pState.isAir() || pState.is(BlockTags.FIRE) || pState.liquid() || pState.canBeReplaced();
    }

}
