package net.lod.ducksdelights.block.custom.DispenserBehavior;

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

public class RopeLadderDispenseBehavior extends OptionalDispenseItemBehavior {
    public RopeLadderDispenseBehavior() {
    }

    private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

    protected ItemStack execute(BlockSource pSource, ItemStack pStack) {
        this.setSuccess(false);
        Item item = pStack.getItem();
        if (item instanceof BlockItem) {
            Direction dispenserFacing = pSource.getBlockState().getValue(DispenserBlock.FACING);
            BlockPos frontPos = pSource.getPos().relative(dispenserFacing);
            BlockState targetLadder = pSource.getLevel().getBlockState(frontPos);
            BlockPos.MutableBlockPos below = frontPos.mutable().move(Direction.DOWN);
            if (!pSource.getLevel().isClientSide) {
                if (!targetLadder.is(ModBlocks.ROPE_LADDER.get())) {
                    this.setSuccess(((BlockItem) item).place(new DirectionalPlaceContext(pSource.getLevel(), frontPos, dispenserFacing, pStack, dispenserFacing)).consumesAction());
                } else {
                    if (targetLadder.getValue(RopeLadderBlock.ANCHORED)) {
                        while (pSource.getLevel().getBlockState(below.above()).is(ModBlocks.ROPE_LADDER.get())) {
                            if (pSource.getLevel().getBlockState(below).is(Blocks.AIR)) {
                                pSource.getLevel().setBlock(below, pSource.getLevel().getBlockState(below.above()).setValue(RopeLadderBlock.ANCHORED, false).setValue(RopeLadderBlock.WATERLOGGED, false), 3);
                                pSource.getLevel().playSound(null, below.above(), SoundEvents.BAMBOO_WOOD_PLACE, SoundSource.BLOCKS);
                                pStack.shrink(1);
                                this.setSuccess(true);
                                break;
                            } else if (pSource.getLevel().getBlockState(below).is(Blocks.WATER)) {
                                if (pSource.getLevel().getFluidState(below).isSource()) {
                                    pSource.getLevel().setBlock(below, pSource.getLevel().getBlockState(below.above()).setValue(RopeLadderBlock.ANCHORED, false).setValue(RopeLadderBlock.WATERLOGGED, true), 3);
                                } else {
                                    pSource.getLevel().setBlock(below, pSource.getLevel().getBlockState(below.above()).setValue(RopeLadderBlock.ANCHORED, false).setValue(RopeLadderBlock.WATERLOGGED, false), 3);
                                }
                                pSource.getLevel().playSound(null, below.above(), SoundEvents.BAMBOO_WOOD_PLACE, SoundSource.BLOCKS);
                                pStack.shrink(1);
                                this.setSuccess(true);
                                break;
                            }
                            below.move(Direction.DOWN);
                        }
                    } else {
                        while (pSource.getLevel().getBlockState(below.above()).is(ModBlocks.ROPE_LADDER.get())) {
                            if (!pSource.getLevel().getBlockState(below).is(ModBlocks.ROPE_LADDER.get())) {
                                if (pSource.getLevel().getBlockState(below.above()).getValue(RopeLadderBlock.WATERLOGGED)) {
                                    pSource.getLevel().setBlock(below.above(), Blocks.WATER.defaultBlockState(), 3);
                                } else {
                                    pSource.getLevel().setBlock(below.above(), Blocks.AIR.defaultBlockState(), 3);
                                }
                                pSource.getLevel().playSound(null, below.above(), SoundEvents.BAMBOO_WOOD_BREAK, SoundSource.BLOCKS);
                                DispenserBlockEntity dispenser = pSource.getEntity();
                                if (pStack.getCount() < pStack.getMaxStackSize()) {
                                    pStack.grow(1);
                                } else {
                                    if (dispenser.addItem(new ItemStack(ModBlocks.ROPE_LADDER.get()).copy()) < 0) {
                                        this.defaultDispenseItemBehavior.dispense(pSource, new ItemStack(ModBlocks.ROPE_LADDER.get()));
                                    }
                                }
                                this.setSuccess(true);
                                break;
                            }
                            below.move(Direction.DOWN);
                        }
                    }
                }
            }
        }

        return pStack;
    }
}
