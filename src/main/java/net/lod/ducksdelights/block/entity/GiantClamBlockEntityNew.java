package net.lod.ducksdelights.block.entity;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.lod.ducksdelights.block.ModBlockEntities;
import net.lod.ducksdelights.recipe.PearlingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.Nullable;

public class GiantClamBlockEntityNew extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder {
    protected final ContainerData dataAccess;
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed;
    private final RecipeManager.CachedCheck<Container, ? extends PearlingRecipe> quickCheck;
    LazyOptional<? extends IItemHandler>[] handlers;
    private final RecipeType<? extends PearlingRecipe> recipeType;
    protected NonNullList<ItemStack> items;

    int pearlingProgress;
    int pearlingTotalTime;

    protected GiantClamBlockEntityNew(BlockPos pPos, BlockState pBlockState, RecipeType<? extends PearlingRecipe> pRecipeType) {
        super(ModBlockEntities.GIANT_CLAM_BE.get(), pPos, pBlockState);
        this.items = NonNullList.withSize(2, ItemStack.EMPTY);
        this.dataAccess = new ContainerData() {
            public int get(int dataValue) {
                switch (dataValue) {
                    case 0 -> {
                        return GiantClamBlockEntityNew.this.pearlingProgress;
                    }
                    case 1 -> {
                        return GiantClamBlockEntityNew.this.pearlingTotalTime;
                    }
                    default -> {
                        return 0;
                    }
                }
            }

            public void set(int dataValue, int integer) {
                switch (dataValue) {
                    case 0 -> GiantClamBlockEntityNew.this.pearlingProgress = integer;
                    case 1 -> GiantClamBlockEntityNew.this.pearlingTotalTime = integer;
                }

            }

            public int getCount() {
                return 2;
            }
        };
        this.recipesUsed = new Object2IntOpenHashMap();
        this.handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN);
        this.quickCheck = RecipeManager.createCheck(pRecipeType);
        this.recipeType = pRecipeType;
    }



    @Override
    protected Component getDefaultName() {
        return null;
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int i) {
        return null;
    }

    @Override
    public ItemStack removeItem(int i, int i1) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return null;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {

    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {

    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return false;
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe) {

    }

    @Override
    public @Nullable Recipe<?> getRecipeUsed() {
        return null;
    }
}
