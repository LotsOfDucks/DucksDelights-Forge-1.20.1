package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.NoteBlockEvent;
import org.jetbrains.annotations.Nullable;

public class SculkSpeakerBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty POWERED;
    public static final IntegerProperty TUNE;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape UP_SHAPE;
    protected static final VoxelShape LOW_SHAPE;
    protected static final VoxelShape SHAPE;
    public static final EnumProperty<Direction> FACING;

    public SculkSpeakerBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(TUNE, 1).setValue(POWERED, false).setValue(WATERLOGGED, false).setValue(FACING, Direction.NORTH));
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public boolean isSignalSource(BlockState pState) {
        return true;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        FluidState fluidState = pContext.getLevel().getFluidState(pContext.getClickedPos());
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection()).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER).setValue(POWERED, pContext.getLevel().hasNeighborSignal(pContext.getClickedPos()));
    }

    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        boolean isPowered = pLevel.hasNeighborSignal(pPos);
        int a = pLevel.getSignal(pPos.relative(pState.getValue(FACING).getOpposite()), pState.getValue(FACING).getOpposite());
        if (isPowered != pState.getValue(POWERED)) {
            if (a > 0) {
                pLevel.setBlock(pPos, pState.setValue(POWERED, isPowered).setValue(TUNE, a), 2);
            }else {
                pLevel.setBlock(pPos, pState.setValue(POWERED, isPowered), 2);
            }
            if (!pState.getValue(POWERED)) {
                this.playResonance(null, pState, pLevel, pPos);
            }
        }
    }

    private void playResonance(@Nullable Entity entity, BlockState state, Level world, BlockPos pos) {
        world.blockEvent(pos, this, 0, 0);
        int tune = state.getValue(TUNE);
        switch(tune){
            case 1:
                world.gameEvent(entity, GameEvent.RESONATE_1, pos);
                break;
            case 2:
                world.gameEvent(entity, GameEvent.RESONATE_2, pos);
                break;
            case 3:
                world.gameEvent(entity, GameEvent.RESONATE_3, pos);
                break;
            case 4:
                world.gameEvent(entity, GameEvent.RESONATE_4, pos);
                break;
            case 5:
                world.gameEvent(entity, GameEvent.RESONATE_5, pos);
                break;
            case 6:
                world.gameEvent(entity, GameEvent.RESONATE_6, pos);
                break;
            case 7:
                world.gameEvent(entity, GameEvent.RESONATE_7, pos);
                break;
            case 8:
                world.gameEvent(entity, GameEvent.RESONATE_8, pos);
                break;
            case 9:
                world.gameEvent(entity, GameEvent.RESONATE_9, pos);
                break;
            case 10:
                world.gameEvent(entity, GameEvent.RESONATE_10, pos);
                break;
            case 11:
                world.gameEvent(entity, GameEvent.RESONATE_11, pos);
                break;
            case 12:
                world.gameEvent(entity, GameEvent.RESONATE_12, pos);
                break;
            case 13:
                world.gameEvent(entity, GameEvent.RESONATE_13, pos);
                break;
            case 14:
                world.gameEvent(entity, GameEvent.RESONATE_14, pos);
                break;
            case 15:
                world.gameEvent(entity, GameEvent.RESONATE_15, pos);
        }
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide) {
            pState = pState.cycle(TUNE);
            pLevel.setBlockAndUpdate(pPos, pState);
            pPlayer.awardStat(Stats.TUNE_NOTEBLOCK);
        }

        return InteractionResult.SUCCESS;
    }

    public void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        if (!pLevel.isClientSide) {
            this.playResonance(pPlayer, pState, pLevel, pPos);
            pPlayer.awardStat(Stats.PLAY_NOTEBLOCK);
        }
    }

    public static float getNotePitch(int pNote) {
        return (float)Math.pow(2.0, (double)(pNote - 12) / 12.0);
    }

    public boolean triggerEvent(BlockState pState, Level pLevel, BlockPos pPos, int pId, int pParam) {
        NoteBlockEvent.Play i = new NoteBlockEvent.Play(pLevel, pPos, pState, pState.getValue(TUNE), NoteBlockInstrument.BASEDRUM);
        if (MinecraftForge.EVENT_BUS.post(i)) {
            return false;
        } else {
            NoteBlockInstrument noteBlockInstrument = NoteBlockInstrument.BASEDRUM;
            float f;
            if (noteBlockInstrument.isTunable()) {
                int tune = pState.getValue(TUNE);
                f = getNotePitch(tune);
                pLevel.addParticle(ParticleTypes.NOTE, (double) pPos.getX() + 0.5, (double) pPos.getY() + 1.2, (double) pPos.getZ() + 0.5, (double) tune / 24.0, 0.0, 0.0);
            } else {
                f = 1.0F;
            }
            Holder holder = noteBlockInstrument.getSoundEvent();
            if (!(Boolean) pState.getValue(WATERLOGGED)) {
                pLevel.playSeededSound(null, (double) pPos.getX() + 0.5, (double) pPos.getY() + 0.5, (double) pPos.getZ() + 0.5, holder, SoundSource.RECORDS, 0.5F, f, pLevel.random.nextLong());
            } else {
                double d = pPos.getX();
                double e = pPos.getY();
                double g = pPos.getZ();
                pLevel.addAlwaysVisibleParticle(ParticleTypes.BUBBLE, d + 0.5, e + 0.7, g + 0.5, 0.0, 0.04, 0.0);
                pLevel.addAlwaysVisibleParticle(ParticleTypes.BUBBLE, d + Math.random(), e + 0.7, g + Math.random(), 0.0, 0.04, 0.0);
                pLevel.addAlwaysVisibleParticle(ParticleTypes.BUBBLE_COLUMN_UP, d + Math.random(), e + 0.7, g + Math.random(), 0.0, 0.04, 0.0);
                pLevel.addAlwaysVisibleParticle(ParticleTypes.BUBBLE_COLUMN_UP, d + Math.random(), e + 0.7, g + Math.random(), 0.0, 0.04, 0.0);
            }
            return true;
        }
    }

    public void spawnAfterBreak(BlockState pState, ServerLevel pLevel, BlockPos pPos, ItemStack pStack, boolean pDropExperience) {
        super.spawnAfterBreak(pState, pLevel, pPos, pStack, pDropExperience);
    }

    public int getExpDrop(BlockState state, LevelReader level, RandomSource randomSource, BlockPos pos, int fortuneLevel, int silkTouchLevel) {
        return silkTouchLevel == 0 ? 5 : 0;
    }


    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWERED, WATERLOGGED, TUNE, FACING);
    }

    static {
        POWERED = BlockStateProperties.POWERED;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        TUNE = ModBlockStateProperties.TUNE;
        UP_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
        LOW_SHAPE = Block.box(2.0, 8.0, 2.0, 14.0, 12.0, 14.0);
        SHAPE = Shapes.or(UP_SHAPE, LOW_SHAPE);
        FACING = BlockStateProperties.HORIZONTAL_FACING;
    }
}
