package net.lod.ducksdelights.block.entity;

import net.lod.ducksdelights.block.custom.BlazingBarrelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.Optional;

public class BlazingBarrelBlockEntity extends BlockEntity implements Clearable {
    private static final int BURN_COOL_SPEED = 2;
    private static final int NUM_SLOTS = 4;
    private final NonNullList<ItemStack> items;
    private final int[] cookingProgress;
    private final int[] cookingTime;
    private final RecipeManager.CachedCheck<Container, CampfireCookingRecipe> quickCheck;

    public BlazingBarrelBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BLAZING_BARREL_DETECTOR_BE.get(), pPos, pBlockState);
        this.items = NonNullList.withSize(4, ItemStack.EMPTY);
        this.cookingProgress = new int[4];
        this.cookingTime = new int[4];
        this.quickCheck = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);
    }

    public static void cookTick(Level pLevel, BlockPos pPos, BlockState pState, BlazingBarrelBlockEntity pBlockEntity) {
        boolean cook = false;

        for(int slot = 0; slot < pBlockEntity.items.size(); ++slot) {
            ItemStack cookingItem = pBlockEntity.items.get(slot);
            if (!cookingItem.isEmpty()) {
                cook = true;
                pBlockEntity.cookingProgress[slot]++;
                if (pBlockEntity.cookingProgress[slot] >= (pBlockEntity.cookingTime[slot] * 0.75)) {
                    Container container = new SimpleContainer(cookingItem);
                    ItemStack finishStack = pBlockEntity.quickCheck.getRecipeFor(container, pLevel).map((p_270054_) -> p_270054_.assemble(container, pLevel.registryAccess())).orElse(cookingItem);
                    if (finishStack.isItemEnabled(pLevel.enabledFeatures())) {
                        Containers.dropItemStack(pLevel, pPos.getCenter().x(), pPos.getCenter().y(), pPos.getCenter().z(), finishStack);
                        pBlockEntity.items.set(slot, ItemStack.EMPTY);
                        pLevel.sendBlockUpdated(pPos, pState, pState, 3);
                        pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pState));
                    }
                }
            }
        }

        if (cook) {
            setChanged(pLevel, pPos, pState);
        }

    }

    public static void particleTick(Level pLevel, BlockPos pPos, BlockState pState, BlazingBarrelBlockEntity pBlockEntity) {
        RandomSource random = pLevel.random;
        for(int slot = 0; slot < pBlockEntity.items.size(); ++slot) {
            if (!pBlockEntity.items.get(slot).isEmpty() && random.nextFloat() < 0.2F) {
                Direction itemPos = Direction.from2DDataValue(Math.floorMod(slot, 4));
                float yShift = switch (pState.getValue(BlazingBarrelBlock.FULLNESS)) {
                    case 2 -> 0.18825F;
                    case 3 -> 0.25075F;
                    case 4 -> 0.31325F;
                    case 5 -> 0.37575F;
                    case 6 -> 0.43825F;
                    case 7 -> 0.50075F;
                    case 8 -> 0.56325F;
                    case 9 -> 0.62575F;
                    case 10 -> 0.68825F;
                    case 11 -> 0.75075F;
                    case 12 -> 0.81325F;
                    case 13 -> 0.87575F;
                    case 14 -> 0.93825F;
                    case 15 -> 1.00075F;
                    default -> 0.12575F;
                };
                double x = (double)pPos.getX() + 0.5 - (double)((float)itemPos.getStepX() * 0.2F) + (double)((float)itemPos.getClockWise().getStepX() * 0.2F);
                double y = (double)pPos.getY() + yShift;
                double z = (double)pPos.getZ() + 0.5 - (double)((float)itemPos.getStepZ() * 0.2F) + (double)((float)itemPos.getClockWise().getStepZ() * 0.2F);

                for(int slotNum = 0; slotNum < 4; ++slotNum) {
                    pLevel.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 5.0E-4, 0.0);
                }
            }
        }

    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.items.clear();
        ContainerHelper.loadAllItems(pTag, this.items);
        int[] cookingTimes;
        if (pTag.contains("CookingTimes", 11)) {
            cookingTimes = pTag.getIntArray("CookingTimes");
            System.arraycopy(cookingTimes, 0, this.cookingProgress, 0, Math.min(this.cookingTime.length, cookingTimes.length));
        }

        if (pTag.contains("CookingTotalTimes", 11)) {
            cookingTimes = pTag.getIntArray("CookingTotalTimes");
            System.arraycopy(cookingTimes, 0, this.cookingTime, 0, Math.min(this.cookingTime.length, cookingTimes.length));
        }

    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        ContainerHelper.saveAllItems(pTag, this.items, true);
        pTag.putIntArray("CookingTimes", this.cookingProgress);
        pTag.putIntArray("CookingTotalTimes", this.cookingTime);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = new CompoundTag();
        ContainerHelper.saveAllItems(compoundTag, this.items, true);
        return compoundTag;
    }

    public Optional<CampfireCookingRecipe> getCookableRecipe(ItemStack pStack) {
        return this.items.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.quickCheck.getRecipeFor(new SimpleContainer(new ItemStack[]{pStack}), this.level);
    }

    public boolean placeFood(@Nullable Entity pEntity, ItemStack pStack, int pCookTime) {
        for(int slot = 0; slot < this.items.size(); ++slot) {
            ItemStack itemStack = this.items.get(slot);
            if (itemStack.isEmpty()) {
                this.cookingTime[slot] = pCookTime;
                this.cookingProgress[slot] = 0;
                this.items.set(slot, pStack.split(1));
                this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(), GameEvent.Context.of(pEntity, this.getBlockState()));
                this.markUpdated();
                return true;
            }
        }

        return false;
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public void clearContent() {
        this.items.clear();
    }
}
