package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.block.custom.blockstate_properties.ModBlockStateProperties;
import net.lod.ducksdelights.sound.ModSoundEvents;
import net.lod.ducksdelights.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class MonolithBlock extends Block {
    public static final BooleanProperty SENSING;
    public static final BooleanProperty AGITATED;
    public static final BooleanProperty IS_SPREADING;

    public MonolithBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SENSING, false).setValue(AGITATED, false).setValue(IS_SPREADING, false));
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(SENSING, pContext.getLevel().hasNeighborSignal(pContext.getClickedPos())).setValue(AGITATED, false);
    }

    public void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        if (!pLevel.isClientSide) {
            if (this.isAlive(pLevel, pPos)) {
                this.changeToLitBlock(pLevel, pPos, pState);
                pPlayer.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200));
            }
        }
    }

    public void onProjectileHit(Level pLevel, BlockState pState, BlockHitResult pHit, Projectile pProjectile) {
        if (this.isAlive(pLevel, pHit.getBlockPos())) {
            this.changeToLitBlock(pLevel, pHit.getBlockPos(), pState);
            if (pProjectile.getOwner() != null) {
                if (pProjectile.getOwner() instanceof LivingEntity livingEntity && pProjectile.getOwner().isAlive()) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200));
                }
            }
        }
        super.onProjectileHit(pLevel, pState, pHit, pProjectile);
    }

    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        if (this.isAlive(level, pos)) {
            for (Direction directions : Direction.values()) {
                this.checkAndLightMonolith(level, pos.relative(directions));
                level.playSound(null, pos.getCenter().x(), pos.getCenter().y(), pos.getCenter().z(), ModSoundEvents.MONOLITH_ROAR.get(), SoundSource.BLOCKS, 0.1F, 0.1F);
                if (explosion.getExploder() instanceof LivingEntity livingEntity && explosion.getExploder().isAlive()) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200));
                }
            }
        }
        super.onBlockExploded(state, level, pos, explosion);
    }

    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (!pLevel.isClientSide) {
            if (this.isAlive(pLevel, pPos)) {
                this.updateSpreading(pLevel, pPos, pState);
            }
        }
    }

    public boolean isAlive(Level level, BlockPos pos) {
        for (int x = -1; x <= 1; ++x) {
            for (int y = -1; y <= 1; ++y) {
                for (int z = -1; z <= 1; ++z) {
                    if (this.obtainFlesh(level, pos.offset(x,y,z))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean obtainFlesh(Level level, BlockPos pos) {
        BlockState checkedState = level.getBlockState(pos);
        return checkedState.is(ModTags.Blocks.MONOLITH_ALIVE_GRANTING);
    }

    private void changeToLitBlock(Level level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof MonolithBlock) {
            level.setBlockAndUpdate(pos, state.setValue(SENSING, true));
        }
        level.scheduleTick(pos, state.getBlock(), 1);
    }

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(AGITATED)) {
            if (pState.getValue(SENSING)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(AGITATED, false).setValue(SENSING, false));
            } else {
                this.changeToLitBlock(pLevel, pPos, pState);
            }
        } else {
            if (pState.getValue(SENSING)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(AGITATED, true).setValue(SENSING, false));
                pLevel.scheduleTick(pPos, this, 600 + pLevel.getRandom().nextInt(40));

                pLevel.playSound(null, pPos.getCenter().x(), pPos.getCenter().y(), pPos.getCenter().z(), ModSoundEvents.MONOLITH_AGITATED.get(), SoundSource.BLOCKS, 0.1F, 0.25F);

                for (Direction directions : Direction.values()) {
                    this.checkAndLightMonolith(pLevel, pPos.relative(directions));
                }
            }
        }
    }

    public void checkAndLightMonolith(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof MonolithBlock && !state.getValue(AGITATED) && !state.getValue(SENSING)) {
            this.changeToLitBlock(level, pos, state);
        }
    }

    public void updateSpreading(Level level, BlockPos pos, BlockState blockState) {
        if (this.canSpread(level, pos)) {
            level.setBlockAndUpdate(pos, blockState.setValue(IS_SPREADING, true));
        }
    }

    public boolean isRandomlyTicking(BlockState pState) {
        return this.isSpreading(pState);
    }

    public boolean isSpreading(BlockState state) {
        return state.getValue(IS_SPREADING);
    }

    public boolean canSpread(Level level, BlockPos pos) {
        int nonMonolith = 0;
        for (Direction directions : Direction.values()) {
            nonMonolith += this.obtainNonMonolith(level, pos.relative(directions));
        }
        return (nonMonolith > 2);
    }

    public int obtainNonMonolith(Level level, BlockPos pos) {
        BlockState checkedState = level.getBlockState(pos);
        if (checkedState.is(ModTags.Blocks.MONOLITH_REPLACEABLE)) {
            return 1;
        } else {
            return 0;
        }
    }

    public Direction getSpreadDirection(Level level, BlockPos pos) {
        Direction resultDirection = null;
        for (Direction checkDirection : Direction.values()) {
            BlockState airCheckedState = level.getBlockState(pos.relative(checkDirection));
            if (airCheckedState.is(ModTags.Blocks.MONOLITH_REPLACEABLE)) {
                if (airCheckedState.is(ModBlocks.MONOLITH_GRAFT.get())) {
                    level.setBlockAndUpdate(pos.relative(checkDirection), ModBlocks.MONOLITH.get().defaultBlockState().setValue(IS_SPREADING, this.isAlive(level, pos.relative(checkDirection))));
                }
                int checkDistance = 5;
                if (checkDirection == Direction.UP || checkDirection == Direction.DOWN) {
                    checkDistance = 12;
                }
                for (int distance = checkDistance; distance >= 2; distance--) {
                    BlockState monolithFindState = level.getBlockState(pos.relative(checkDirection, distance));
                    if (monolithFindState.is(ModTags.Blocks.MONOLITH_ATTRACTABLE)) {
                        resultDirection = checkDirection;
                        break;
                    }
                }
            }
            if (resultDirection != null) {break;}
        }
        return resultDirection;
    }

    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pState.getValue(AGITATED)) {
            if (!this.canSpread(pLevel, pPos) || !this.isAlive(pLevel, pPos)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(IS_SPREADING, false));
            } else if (pRandom.nextIntBetweenInclusive(1, 5) == 5 && this.isAlive(pLevel, pPos)) {
                Direction spreadDirection = this.getSpreadDirection(pLevel, pPos);
                if (spreadDirection != null) {
                    pLevel.setBlockAndUpdate(pPos.relative(spreadDirection), ModBlocks.MONOLITH.get().defaultBlockState().setValue(IS_SPREADING, this.isAlive(pLevel, pPos.relative(spreadDirection))));
                }
            }
        }
    }


    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(SENSING, AGITATED, IS_SPREADING);
    }

    static {
        SENSING = ModBlockStateProperties.SENSING;
        AGITATED = ModBlockStateProperties.AGITATED;
        IS_SPREADING = ModBlockStateProperties.IS_SPREADING_FLESH;
    }
}
