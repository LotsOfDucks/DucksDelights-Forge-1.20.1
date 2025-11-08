package net.lod.ducksdelights.block.custom.dispenser_behavior;

import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.block.custom.RopeLadderBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AntiRopeLadderDispenseBehavior extends OptionalDispenseItemBehavior {
    public AntiRopeLadderDispenseBehavior() {
    }

    private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

    protected ItemStack execute(BlockSource pSource, ItemStack pStack) {
        this.setSuccess(false);
        Item item = pStack.getItem();
        if (item instanceof BlockItem) {
            Direction dispenserFacing = pSource.getBlockState().getValue(DispenserBlock.FACING);
            BlockPos frontPos = pSource.getPos().relative(dispenserFacing);
            BlockState targetLadder = pSource.getLevel().getBlockState(frontPos);
            BlockPos.MutableBlockPos above = frontPos.mutable().move(Direction.UP);
            if (!pSource.getLevel().isClientSide) {
                if (!targetLadder.is(ModBlocks.ANTI_ROPE_LADDER.get())) {
                    this.setSuccess(((BlockItem) item).place(new DirectionalPlaceContext(pSource.getLevel(), frontPos, dispenserFacing, pStack, dispenserFacing)).consumesAction());
                } else {
                    if (targetLadder.getValue(RopeLadderBlock.ANCHORED)) {
                        while (pSource.getLevel().getBlockState(above.below()).is(ModBlocks.ANTI_ROPE_LADDER.get())) {
                            if (pSource.getLevel().getBlockState(above).is(Blocks.AIR)) {
                                pSource.getLevel().setBlock(above, pSource.getLevel().getBlockState(above.below()).setValue(RopeLadderBlock.ANCHORED, false).setValue(RopeLadderBlock.WATERLOGGED, false), 3);
                                pSource.getLevel().playSound(null, above.below(), SoundEvents.BAMBOO_WOOD_PLACE, SoundSource.BLOCKS);
                                pStack.shrink(1);
                                this.setSuccess(true);
                                break;
                            } else if (pSource.getLevel().getBlockState(above).is(Blocks.WATER)) {
                                if (pSource.getLevel().getFluidState(above).isSource()) {
                                    pSource.getLevel().setBlock(above, pSource.getLevel().getBlockState(above.below()).setValue(RopeLadderBlock.ANCHORED, false).setValue(RopeLadderBlock.WATERLOGGED, true), 3);
                                } else {
                                    pSource.getLevel().setBlock(above, pSource.getLevel().getBlockState(above.below()).setValue(RopeLadderBlock.ANCHORED, false).setValue(RopeLadderBlock.WATERLOGGED, false), 3);
                                }
                                pSource.getLevel().playSound(null, above.below(), SoundEvents.BAMBOO_WOOD_PLACE, SoundSource.BLOCKS);
                                pStack.shrink(1);
                                this.setSuccess(true);
                                break;
                            }
                            above.move(Direction.UP);
                        }
                    } else {
                        while (pSource.getLevel().getBlockState(above.below()).is(ModBlocks.ANTI_ROPE_LADDER.get())) {
                            if (!pSource.getLevel().getBlockState(above).is(ModBlocks.ANTI_ROPE_LADDER.get())) {
                                if (pSource.getLevel().getBlockState(above.below()).getValue(RopeLadderBlock.WATERLOGGED)) {
                                    pSource.getLevel().setBlock(above.below(), Blocks.WATER.defaultBlockState(), 3);
                                } else {
                                    pSource.getLevel().setBlock(above.below(), Blocks.AIR.defaultBlockState(), 3);
                                }
                                pSource.getLevel().playSound(null, above.below(), SoundEvents.BAMBOO_WOOD_BREAK, SoundSource.BLOCKS);
                                DispenserBlockEntity dispenser = pSource.getEntity();
                                if (pStack.getCount() < pStack.getMaxStackSize()) {
                                    pStack.grow(1);
                                } else {
                                    if (dispenser.addItem(new ItemStack(ModBlocks.ANTI_ROPE_LADDER.get()).copy()) < 0) {
                                        this.defaultDispenseItemBehavior.dispense(pSource, new ItemStack(ModBlocks.ANTI_ROPE_LADDER.get()));
                                    }
                                }
                                this.setSuccess(true);
                                break;
                            }
                            above.move(Direction.UP);
                        }
                    }
                }
            }
        }

        return pStack;
    }
}
