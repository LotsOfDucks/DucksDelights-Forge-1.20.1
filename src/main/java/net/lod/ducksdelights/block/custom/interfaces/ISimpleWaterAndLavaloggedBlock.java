package net.lod.ducksdelights.block.custom.interfaces;

import net.lod.ducksdelights.block.ModBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.Optional;

public interface ISimpleWaterAndLavaloggedBlock extends BucketPickup, LiquidBlockContainer {
    default boolean canPlaceLiquid(BlockGetter pLevel, BlockPos pPos, BlockState pState, Fluid pFluid) {
        return (pFluid == Fluids.WATER && !pState.getValue(ModBlockStateProperties.LAVALOGGED)) || (pFluid == Fluids.LAVA && !pState.getValue(BlockStateProperties.WATERLOGGED));
    }

    default boolean placeLiquid(LevelAccessor pLevel, BlockPos pPos, BlockState pState, FluidState pFluidState) {
        if (!(Boolean)pState.getValue(ModBlockStateProperties.LOGGED) && (pFluidState.getType() == Fluids.WATER || pFluidState.getType() == Fluids.LAVA)) {
            if (!pLevel.isClientSide()) {
                if (pFluidState.getType() == Fluids.WATER) {
                    pLevel.setBlock(pPos, pState.setValue(ModBlockStateProperties.LOGGED, true).setValue(BlockStateProperties.WATERLOGGED, true), 3);
                    pLevel.scheduleTick(pPos, pFluidState.getType(), pFluidState.getType().getTickDelay(pLevel));
                } else if (pFluidState.getType() == Fluids.LAVA) {
                    pLevel.setBlock(pPos, pState.setValue(ModBlockStateProperties.LOGGED, true).setValue(ModBlockStateProperties.LAVALOGGED, true), 3);
                    pLevel.scheduleTick(pPos, pFluidState.getType(), pFluidState.getType().getTickDelay(pLevel));
                }
            }
            return true;
        } else {
            return false;
        }
    }

    default ItemStack pickupBlock(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        if (pState.getValue(ModBlockStateProperties.LOGGED)) {
            if (pState.getValue(BlockStateProperties.WATERLOGGED)) {
                pLevel.setBlock(pPos, pState.setValue(ModBlockStateProperties.LOGGED, false).setValue(BlockStateProperties.WATERLOGGED, false), 3);
                pLevel.playSound(null, pPos ,SoundEvents.BUCKET_FILL, SoundSource.BLOCKS);
                return new ItemStack(Items.WATER_BUCKET);
            } else if (pState.getValue(ModBlockStateProperties.LAVALOGGED)) {
                pLevel.setBlock(pPos, pState.setValue(ModBlockStateProperties.LOGGED, false).setValue(ModBlockStateProperties.LAVALOGGED, false), 3);
                pLevel.playSound(null, pPos ,SoundEvents.BUCKET_FILL_LAVA, SoundSource.BLOCKS);
                return new ItemStack(Items.LAVA_BUCKET);
            }
            if (!pState.canSurvive(pLevel, pPos)) {
                pLevel.destroyBlock(pPos, true);
            }
        }
        return ItemStack.EMPTY;
    }

    default Optional<SoundEvent> getPickupSound() {
        return Optional.empty();
    }
}
