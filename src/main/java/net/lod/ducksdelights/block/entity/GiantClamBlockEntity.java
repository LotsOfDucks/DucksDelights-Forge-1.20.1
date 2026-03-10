package net.lod.ducksdelights.block.entity;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.lod.ducksdelights.block.ModBlockEntities;
import net.lod.ducksdelights.block.custom.GiantClamBlock;
import net.lod.ducksdelights.block.custom.blockstate_properties.ModBlockStateProperties;
import net.lod.ducksdelights.recipe.PearlingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.*;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Optional;

public class GiantClamBlockEntity extends BlockEntity implements WorldlyContainer {
    protected static final int SLOT_INPUT = 0;
    protected static final int SLOT_RESULT = 1;
    public static final int[] SLOTS_FOR_UP = new int[]{0};
    public static final int[] SLOTS_FOR_DOWN = new int[]{0,1};
    @Nullable
    public Component name;
    public LockCode lockKey;
    public NonNullList<ItemStack> items;
    int pearlingProgress;
    int pearlingTotalTime;
    public final RecipeType<? extends PearlingRecipe> recipeType;
    public final Object2IntOpenHashMap<ResourceLocation> recipesUsed;
    public final RecipeManager.CachedCheck<Container, PearlingRecipe> quickCheck;
    LazyOptional<? extends IItemHandler>[] itemHandler;
    protected final ContainerData dataAccess;





    public GiantClamBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GIANT_CLAM_BE.get(), pPos, pBlockState);
        this.items = NonNullList.withSize(2, ItemStack.EMPTY);
        this.recipeType = PearlingRecipe.Type.INSTANCE;
        this.quickCheck = RecipeManager.createCheck(PearlingRecipe.Type.INSTANCE);
        this.lockKey = LockCode.NO_LOCK;
        this.recipesUsed = new Object2IntOpenHashMap();
        this.dataAccess = new ContainerData() {
            public int get(int p_58431_) {
                switch (p_58431_) {
                    case 0 -> {
                        return GiantClamBlockEntity.this.pearlingProgress;
                    }
                    case 1 -> {
                        return GiantClamBlockEntity.this.pearlingTotalTime;
                    }
                    default -> {
                        return 0;
                    }
                }
            }

            public void set(int p_58433_, int p_58434_) {
                switch (p_58433_) {
                    case 0 -> GiantClamBlockEntity.this.pearlingProgress = p_58434_;
                    case 1 -> GiantClamBlockEntity.this.pearlingTotalTime = p_58434_;
                }

            }

            public int getCount() {
                return 2;
            }
        };
        this.itemHandler = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN);
    }

    private boolean isPearling() {
        return (this.pearlingProgress > 0);
    }

    @SuppressWarnings("removal")
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.lockKey = LockCode.fromTag(pTag);
        this.items.clear();
        ContainerHelper.loadAllItems(pTag, this.items);
        this.pearlingProgress = pTag.getInt("PearlingTime");
        this.pearlingTotalTime = pTag.getInt("PearlingTimeTotal");
        if (pTag.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(pTag.getString("CustomName"));
        }
        CompoundTag compoundtag = pTag.getCompound("RecipesUsed");
        Iterator compoundTag = compoundtag.getAllKeys().iterator();
        while(compoundTag.hasNext()) {
            String s = (String)compoundTag.next();
            this.recipesUsed.put(new ResourceLocation(s), compoundtag.getInt(s));
        }

    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        this.lockKey.addToTag(pTag);
        ContainerHelper.saveAllItems(pTag, this.items, true);
        pTag.putInt("PearlingTime", this.pearlingProgress);
        pTag.putInt("PearlingTimeTotal", this.pearlingTotalTime);
        if (this.name != null) {
            pTag.putString("CustomName", Component.Serializer.toJson(this.name));
        }
        CompoundTag compoundtag = new CompoundTag();
        this.recipesUsed.forEach((p_187449_, p_187450_) -> {
            compoundtag.putInt(p_187449_.toString(), p_187450_);
        });
        pTag.put("RecipesUsed", compoundtag);

    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = new CompoundTag();
        ContainerHelper.saveAllItems(compoundTag, this.items, true);
        return compoundTag;
    }

    public boolean itemHasRecipe(int index) {
        if (!this.getItem(0).isEmpty()) {
            Optional<PearlingRecipe> hasRecipe = this.getPearlableRecipe(getItem(index));
            return hasRecipe.isPresent();
        } else return false;
    }

    public ItemStack getResultItem(Level level, Container container, ItemStack itemStack) {
        return this.quickCheck.getRecipeFor(container, level).map((p_270054_) -> p_270054_.assemble(container, level.registryAccess())).orElse(itemStack);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, GiantClamBlockEntity pBlockEntity) {
        boolean hasItemInput = !pBlockEntity.getItem(0).isEmpty();
        ItemStack itemInput = pBlockEntity.getItem(0);
        boolean hasItemOutput = !pBlockEntity.getItem(1).isEmpty();

        boolean isOpen = pState.getValue(GiantClamBlock.OPEN);
        boolean isLogged = pState.getValue(GiantClamBlock.LOGGED);

        boolean blockHasBeenChanged = false;
        boolean blockShouldClose = false;

        if (pLevel.dimension() == Level.END) {
            if (isLogged) {
                if (pLevel.random.nextInt(20) == 0) {
                    pLevel.setBlock(pPos, Blocks.GRASS_BLOCK.defaultBlockState(), 3);
                }
            }
        }
        if (!isOpen) {
            if (isLogged) {
                if (hasItemInput && !hasItemOutput) {
                    if (pBlockEntity.itemHasRecipe(0)) {
                        blockHasBeenChanged = true;
                        pBlockEntity.pearlingProgress++;
                        if (pBlockEntity.pearlingProgress >= pBlockEntity.pearlingTotalTime) {
                            SimpleContainer container = new SimpleContainer(itemInput);
                            ItemStack resultItem = pBlockEntity.getResultItem(pLevel, container, itemInput);
                            if (resultItem.isItemEnabled(pLevel.enabledFeatures())) {
                                if (itemInput.hasTag()) {
                                    CompoundTag inputTag = itemInput.getTag();
                                    resultItem.setTag(inputTag);
                                }
                                pBlockEntity.setItem(1, resultItem);
                                pBlockEntity.removeItem(0, 1);
                                pLevel.scheduleTick(pPos, pLevel.getBlockState(pPos).getBlock(), 20);
                            }
                        }
                    } else {
                        pBlockEntity.resetPearlingProgress();
                    }
                }
            }
        } else {
            if (isLogged) {
                if (hasItemInput && !hasItemOutput) {
                    if (pBlockEntity.itemHasRecipe(0)) {
                        blockShouldClose = true;
                    }
                }
            } else {
                if (hasItemInput || hasItemOutput) {
                    blockHasBeenChanged = true;
                }
            }
        }


        if (blockShouldClose) {
            if (blockShouldClose) {
                pState = pState.setValue(GiantClamBlock.OPEN, false);
            }
            pLevel.setBlock(pPos, pState, 3);
            blockHasBeenChanged = true;
        }

        if (blockHasBeenChanged) {
            setChanged(pLevel, pPos, pState);
        }
    }

    public static void clientTick(Level pLevel, BlockPos pPos, BlockState pState, GiantClamBlockEntity pBlockEntity) {

    }

    public void resetPearlingProgress() {
        this.pearlingProgress = 0;
    }

    public Optional<PearlingRecipe> getPearlableRecipe(ItemStack pStack) {
        return this.items.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.quickCheck.getRecipeFor(new SimpleContainer(pStack), this.level);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    public void drops() {
        SimpleContainer container = new SimpleContainer(items.size());
        for (int slots = 0; slots < items.size(); slots++) {
            container.setItem(slots, items.get(slots));
        }
        BlockPos blockPos = new BlockPos((int) this.worldPosition.getCenter().x, (int) this.worldPosition.getCenter().y, (int) this.worldPosition.getCenter().z);
        Containers.dropContents(this.level, blockPos, container);
    }

    public int[] getSlotsForFace(Direction pSide) {
        if (pSide == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        } else {
            return SLOTS_FOR_UP;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        if (this.getItem(0).isEmpty() && this.getItem(1).isEmpty()) {
            if (this.getBlockState().getValue(ModBlockStateProperties.OPEN)) {
                return pIndex == 0;
            }
        }
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        if (pDirection == Direction.DOWN && pIndex == 0) {
            return isDryAndClosed();
        }
        return true;
    }

    public boolean isDryAndClosed() {
        return (!this.getBlockState().getValue(ModBlockStateProperties.OPEN) && !this.getBlockState().getValue(ModBlockStateProperties.LOGGED));
    }

    @Override
    public boolean isEmpty() {
        return (this.getItem(0).isEmpty() && this.getItem(1).isEmpty());
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        markUpdated();
        return ContainerHelper.removeItem(this.items, slot, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        if (itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }
        this.items.set(slot, itemStack);
        this.resetPearlingProgress();
        if (slot == 0 && this.getPearlableRecipe(itemStack).isPresent()) {
            this.pearlingTotalTime = getTotalPearlingTime(this.level, this);
        }
        this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(), GameEvent.Context.of(this.getBlockState()));
        markUpdated();
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    private static int getTotalPearlingTime(Level pLevel, GiantClamBlockEntity pBlockEntity) {
        return pBlockEntity.quickCheck.getRecipeFor(pBlockEntity, pLevel).map(PearlingRecipe::getPearlingTime).orElse(200);
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER && facing != null && !this.remove) {
            LazyOptional var10000;
            switch (facing) {
                case UP -> var10000 = this.itemHandler[0].cast();
                case DOWN -> var10000 = this.itemHandler[1].cast();
                default -> var10000 = this.itemHandler[0].cast();
            }

            return var10000;
        } else {
            return super.getCapability(capability, facing);
        }
    }

    public void invalidateCaps() {
        super.invalidateCaps();

        for(int x = 0; x < this.itemHandler.length; ++x) {
            this.itemHandler[x].invalidate();
        }
    }

    public void reviveCaps() {
        super.reviveCaps();
        this.itemHandler = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN);
    }
}
