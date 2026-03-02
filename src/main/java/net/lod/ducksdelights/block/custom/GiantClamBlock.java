package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlockStateProperties;
import net.lod.ducksdelights.block.custom.interfaces.ISimpleWaterAndLavaloggedBlock;
import net.lod.ducksdelights.block.entity.DemonCoreBlockEntity;
import net.lod.ducksdelights.block.entity.GiantClamBlockEntity;
import net.lod.ducksdelights.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

public class GiantClamBlock extends BaseEntityBlock implements ISimpleWaterAndLavaloggedBlock, WorldlyContainerHolder {
    public static final BooleanProperty OPEN;
    public static final BooleanProperty HAS_PEARL;
    public static final BooleanProperty PLAYER_PLACED;
    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty LAVALOGGED;
    public static final BooleanProperty LOGGED;
    public static final EnumProperty<Direction> FACING;

    protected GiantClamBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, false).setValue(PLAYER_PLACED, true).setValue(PLAYER_PLACED, false).setValue(WATERLOGGED, false).setValue(LAVALOGGED, false).setValue(LOGGED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GiantClamBlockEntity(blockPos, blockState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.DEMON_CORE_BE.get(), pLevel.isClientSide ? DemonCoreBlockEntity::clientTick : DemonCoreBlockEntity::serverTick);
    }

    @Override
    public WorldlyContainer getContainer(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos) {
        return null;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(OPEN, PLAYER_PLACED, WATERLOGGED, LAVALOGGED, LOGGED, FACING);
    }

    static {
        OPEN = ModBlockStateProperties.OPEN;
        HAS_PEARL = ModBlockStateProperties.HAS_PEARL;
        PLAYER_PLACED = ModBlockStateProperties.PLAYER_PLACED;
        FACING = BlockStateProperties.HORIZONTAL_FACING;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        LAVALOGGED = ModBlockStateProperties.LAVALOGGED;
        LOGGED = ModBlockStateProperties.LOGGED;
    }
}
