package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.custom.blockstate_properties.ModBlockStateProperties;
import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.block.entity.RandomizerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.ticks.TickPriority;
import org.jetbrains.annotations.Nullable;

public class RandomizerBlock extends DiodeBlock implements EntityBlock {
    public static final BooleanProperty BINARY;

    public RandomizerBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false).setValue(BINARY, false));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new RandomizerBlockEntity(blockPos, blockState);
    }

    @Override
    protected boolean sideInputDiodesOnly() {
        return true;
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pPlayer.getAbilities().mayBuild) {
            return InteractionResult.PASS;
        } else {
            pState = pState.cycle(BINARY);
            float f = pState.getValue(BINARY) ? 0.55F : 0.5F;
            pLevel.playSound(pPlayer, pPos, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 0.3F, f);
            pLevel.setBlock(pPos, pState, 3);
            this.refreshOutputState(pLevel, pPos, pState);
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        }
    }

    protected int getDelay(BlockState blockState) {
        return 2;
    }

    protected int getOutputSignal(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        return blockentity instanceof RandomizerBlockEntity ? ((RandomizerBlockEntity)blockentity).getOutputSignal() : 0;
    }

    private int calculateOutputSignal(Level pLevel, BlockPos pPos, BlockState pState) {
        if (pState.getValue(POWERED)) {
            int currentOutput = this.getOutputSignal(pLevel, pPos, pState);
            if (pState.getValue(BINARY) && !(currentOutput == 0 || currentOutput == 15)) {
                return (pLevel.getRandom().nextIntBetweenInclusive(0,1) == 1) ? 15 : 0;
            } else if (!pState.getValue(BINARY) && currentOutput == 0) {
                return pLevel.getRandom().nextIntBetweenInclusive(1, 15);
            }
            return currentOutput;
        } else if (pState.getValue(BINARY)) {
            return (pLevel.getRandom().nextIntBetweenInclusive(0,1) == 1) ? 15 : 0;
        } else {
            return pLevel.getRandom().nextIntBetweenInclusive(1, 15);
        }
    }

    protected boolean shouldTurnOn(Level pLevel, BlockPos pPos, BlockState pState) {
        return this.getInputSignal(pLevel, pPos, pState) > 0;
    }

    protected void checkTickOnNeighbor(Level pLevel, BlockPos pPos, BlockState pState) {
        if (!pLevel.getBlockTicks().willTickThisTick(pPos, this)) {
            int outputSignal = this.calculateOutputSignal(pLevel, pPos, pState);
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            int blockEntityOutput = blockentity instanceof RandomizerBlockEntity ? ((RandomizerBlockEntity)blockentity).getOutputSignal() : 0;
            if (outputSignal != blockEntityOutput || pState.getValue(BINARY) || (pState.getValue(POWERED) != this.shouldTurnOn(pLevel, pPos, pState))) {
                TickPriority tickpriority = this.shouldPrioritize(pLevel, pPos, pState) ? TickPriority.HIGH : TickPriority.NORMAL;
                pLevel.scheduleTick(pPos, this, 2, tickpriority);
            }
        }

    }

    //make sure it updates accordingly, this sucked to code
    private void refreshOutputState(Level pLevel, BlockPos pPos, BlockState pState) {
        int outputSignal = this.calculateOutputSignal(pLevel, pPos, pState);
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        int blockEntityOutput = 0;
        if (blockentity instanceof RandomizerBlockEntity randomizerBlockEntity) {
            blockEntityOutput = randomizerBlockEntity.getOutputSignal();
            randomizerBlockEntity.setOutputSignal(outputSignal);
        }

        if (blockEntityOutput != outputSignal || pState.getValue(BINARY) || (pState.getValue(POWERED) != this.shouldTurnOn(pLevel, pPos, pState))) {
            boolean turnOn = this.shouldTurnOn(pLevel, pPos, pState);
            boolean isOn = pState.getValue(POWERED);
            if (isOn != turnOn) {
                pLevel.setBlock(pPos, pState.setValue(POWERED, turnOn), 2);
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
        return state.is(ModBlocks.RANDOMIZER.get());
    }

    public void onNeighborChange(BlockState state, LevelReader world, BlockPos pos, BlockPos neighbor) {
        if (pos.getY() == neighbor.getY() && world instanceof Level && !world.isClientSide()) {
            state.neighborChanged((Level)world, pos, world.getBlockState(neighbor).getBlock(), neighbor, false);
        }

    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, POWERED, BINARY);
    }

    static {
        BINARY = ModBlockStateProperties.BINARY;
    }
}
