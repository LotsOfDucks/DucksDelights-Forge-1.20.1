package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.block.entity.AdderBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.ticks.TickPriority;
import org.jetbrains.annotations.Nullable;

public class AdderBlock extends DiodeBlock implements EntityBlock {
    public AdderBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AdderBlockEntity(blockPos, blockState);
    }

    protected int getDelay(BlockState blockState) {
        return 2;
    }

    protected int getOutputSignal(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        return blockentity instanceof AdderBlockEntity ? ((AdderBlockEntity)blockentity).getOutputSignal() : 0;
    }

    private int calculateOutputSignalUnreduced(Level pLevel, BlockPos pPos, BlockState pState) {
        int inputSignal = this.getInputSignal(pLevel, pPos, pState);
        int sideSignal = this.getAlternateSignal(pLevel, pPos, pState);
        int outputSignal = inputSignal + sideSignal;
        if (inputSignal + sideSignal > 15) {
        }

        return outputSignal;
    }

    private int calculateOutputSignal(Level pLevel, BlockPos pPos, BlockState pState) {
        int inputSignal = this.getInputSignal(pLevel, pPos, pState);
        int sideSignal = this.getAlternateSignal(pLevel, pPos, pState);
        int outputSignal = inputSignal + sideSignal;
        if (inputSignal + sideSignal > 15) {
            outputSignal = outputSignal - 16;
        }

        return outputSignal;
    }

    protected boolean shouldTurnOn(Level pLevel, BlockPos pPos, BlockState pState) {
        return this.calculateOutputSignalUnreduced(pLevel, pPos, pState) > 0;
    }

    protected int getInputSignal(Level pLevel, BlockPos pPos, BlockState pState) {
        int i = super.getInputSignal(pLevel, pPos, pState);
        Direction direction = pState.getValue(FACING);
        BlockPos blockpos = pPos.relative(direction);
        BlockState blockstate = pLevel.getBlockState(blockpos);
        if (blockstate.hasAnalogOutputSignal()) {
            i = blockstate.getAnalogOutputSignal(pLevel, blockpos);
        }

        return i;
    }

    protected void checkTickOnNeighbor(Level pLevel, BlockPos pPos, BlockState pState) {
        if (!pLevel.getBlockTicks().willTickThisTick(pPos, this)) {
            int outputSignal = this.calculateOutputSignal(pLevel, pPos, pState);
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            int blockEntityOutput = blockentity instanceof AdderBlockEntity ? ((AdderBlockEntity)blockentity).getOutputSignal() : 0;
            if (outputSignal != blockEntityOutput || (pState.getValue(POWERED) != this.shouldTurnOn(pLevel, pPos, pState))) {
                TickPriority tickpriority = this.shouldPrioritize(pLevel, pPos, pState) ? TickPriority.HIGH : TickPriority.NORMAL;
                pLevel.scheduleTick(pPos, this, 2, tickpriority);
            }
        }

    }

    private void refreshOutputState(Level pLevel, BlockPos pPos, BlockState pState) {
        int outputSignal = this.calculateOutputSignal(pLevel, pPos, pState);
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        int blockEntityOutput = 0;
        if (blockentity instanceof AdderBlockEntity adderBlockEntity) {
            blockEntityOutput = adderBlockEntity.getOutputSignal();
            adderBlockEntity.setOutputSignal(outputSignal);
        }

        if (blockEntityOutput != outputSignal || (pState.getValue(POWERED) != this.shouldTurnOn(pLevel, pPos, pState))) {
            boolean turnOn = this.shouldTurnOn(pLevel, pPos, pState);
            boolean isOn = pState.getValue(POWERED);
            if (isOn && !turnOn) {
                pLevel.setBlock(pPos, pState.setValue(POWERED, false), 2);
            } else if (!isOn && turnOn) {
                pLevel.setBlock(pPos, pState.setValue(POWERED, true), 2);
            }

            this.updateNeighborsInFront(pLevel, pPos, pState);
        }

    }

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        this.refreshOutputState(pLevel, pPos, pState);
    }

    public boolean triggerEvent(BlockState pState, Level pLevel, BlockPos pPos, int pId, int pParam) {
        super.triggerEvent(pState, pLevel, pPos, pId, pParam);
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        return blockentity != null && blockentity.triggerEvent(pId, pParam);
    }

    public boolean getWeakChanges(BlockState state, LevelReader world, BlockPos pos) {
        return state.is(ModBlocks.ADDER.get());
    }

    public void onNeighborChange(BlockState state, LevelReader world, BlockPos pos, BlockPos neighbor) {
        if (pos.getY() == neighbor.getY() && world instanceof Level && !world.isClientSide()) {
            state.neighborChanged((Level)world, pos, world.getBlockState(neighbor).getBlock(), neighbor, false);
        }

    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, POWERED);
    }

}
