package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.custom.blockstate_properties.ModBlockStateProperties;
import net.lod.ducksdelights.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class MonolithBlock extends Block {
    public static final BooleanProperty LIT;
    public static final BooleanProperty AGITATED;

    public MonolithBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false).setValue(AGITATED, false));
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(LIT, pContext.getLevel().hasNeighborSignal(pContext.getClickedPos())).setValue(AGITATED, false);
    }

    public void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        if (!pLevel.isClientSide) {
            this.changeToLitBlock(pLevel, pPos, pState);
        }
    }

    public void onProjectileHit(Level pLevel, BlockState pState, BlockHitResult pHit, Projectile pProjectile) {
        this.changeToLitBlock(pLevel, pHit.getBlockPos(), pState);
        super.onProjectileHit(pLevel, pState, pHit, pProjectile);
    }

    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (!pLevel.isClientSide) {
            if (!(pBlock instanceof MonolithBlock) && pLevel.hasNeighborSignal(pPos)) {
                this.changeToLitBlock(pLevel, pPos, pState);
            }
        }
    }

    private void changeToLitBlock(Level level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof MonolithBlock) {
            level.setBlockAndUpdate(pos, state.setValue(LIT, true));
        }
        level.scheduleTick(pos, state.getBlock(), 1);
    }

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(AGITATED)) {
            if (pState.getValue(LIT)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(AGITATED, false).setValue(LIT, false));
            } else {
                this.changeToLitBlock(pLevel, pPos, pState);
            }
        } else {
            if (pState.getValue(LIT)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(AGITATED, true).setValue(LIT, false));
                pLevel.scheduleTick(pPos, this, 600 + pLevel.getRandom().nextInt(40));

                pLevel.playSound(null, pPos.getCenter().x(), pPos.getCenter().y(), pPos.getCenter().z(), SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS, 0.1F, 0.25F);

                for (Direction directions : Direction.values()) {
                    this.checkAndLightMonolith(pLevel, pPos.relative(directions));
                }
            }
        }
    }

    public void checkAndLightMonolith(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        if (state.getBlock() instanceof MonolithBlock && !state.getValue(AGITATED) && !state.getValue(LIT)) {
            this.changeToLitBlock(level, pos, state);
        }
    }


    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LIT, AGITATED);
    }

    static {
        LIT = RedstoneTorchBlock.LIT;
        AGITATED = ModBlockStateProperties.AGITATED;
    }
}
