package net.lod.ducksdelights.block.entity;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.lod.ducksdelights.block.ModBlockStateProperties;
import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.block.custom.ResonatorBlock;
import net.lod.ducksdelights.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Random;

public class ResonatorBlockEntity extends BlockEntity implements GameEventListener.Holder<VibrationSystem.Listener>, VibrationSystem {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int BREAKING_TIME = 20;
    private final VibrationSystem.User vibrationUser = new ResonatorBlockEntity.VibrationUser(this.getBlockPos());
    private VibrationSystem.Data vibrationData = new VibrationSystem.Data();
    private final VibrationSystem.Listener vibrationListener = new VibrationSystem.Listener(this);
    private int breakTicks;
    private int lastVibrationFrequency;

    public ResonatorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.RESONATOR_BE.get() ,pPos, pBlockState);
    }

    public VibrationSystem.Data getVibrationData() {
        return this.vibrationData;
    }

    public VibrationSystem.User getVibrationUser() {
        return this.vibrationUser;
    }

    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("last_vibration_frequency", 99)) {
            this.lastVibrationFrequency = pTag.getInt("last_vibration_frequency");
        }
        if (pTag.contains("break_ticks", 99)) {
            this.breakTicks = pTag.getInt("break_ticks");
        }
        if (pTag.contains("listener", 10)) {
            DataResult listenerData = Data.CODEC.parse(new Dynamic(NbtOps.INSTANCE, pTag.getCompound("listener")));
            listenerData.result().ifPresent((info) -> {
                this.vibrationData = (Data) info;
            });

        }

    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("last_vibration_frequency", this.lastVibrationFrequency);
        pTag.putInt("break_ticks", this.breakTicks);
        DataResult var10000 = Data.CODEC.encodeStart(NbtOps.INSTANCE, this.vibrationData);
        var10000.result().ifPresent((info) -> {
            pTag.put("listener", (Tag) info);
        });
    }

    public int getLastVibrationFrequency() {
        return this.lastVibrationFrequency;
    }

    public void setLastVibrationFrequency(int pLastVibrationFrequency) {
        this.lastVibrationFrequency = pLastVibrationFrequency;
    }

    public boolean canBreak(ServerLevel level, BlockPos targetPos) {
        if (!level.getBlockState(targetPos).is(BlockTags.REPLACEABLE)) {
            float targetDestroyTime = level.getBlockState(targetPos).getBlock().defaultDestroyTime();
            float resonanceThreshhold = switch (getLastVibrationFrequency()) {
                case (1) -> 0.0F;
                case (2) -> 0.3F;
                case (3) -> 0.65F;
                case (4) -> 0.8F;
                case (5) -> 1.0F;
                case (6) -> 1.4F;
                case (7) -> 1.5F;
                case (8) -> 2.0F;
                case (9) -> 3.0F;
                case (10) -> 3.5F;
                case (11) -> 4.5F;
                case (12) -> 5.0F;
                case (13) -> 30.0F;
                case (14) -> 50.0F;
                case (15) -> 99.0F;
                default -> 0.0F;
            };
            float resonanceLowerThreshhold = switch (getLastVibrationFrequency()) {
                case (1) -> -0.1F;
                case (2) -> 0.0F;
                case (3) -> 0.3F;
                case (4) -> 0.65F;
                case (5) -> 0.8F;
                case (6) -> 1.0F;
                case (7) -> 1.4F;
                case (8) -> 1.5F;
                case (9) -> 2.0F;
                case (10) -> 3.0F;
                case (11) -> 3.5F;
                case (12) -> 4.5F;
                case (13) -> 5.0F;
                case (14) -> 30.0F;
                case (15) -> 50.0F;
                default -> -0.1F;
            };
            if ((level.getBlockState(targetPos).is(Blocks.BEDROCK) || level.getBlockState(targetPos).is(ModBlocks.SHATTERED_BEDROCK.get())) && (getLastVibrationFrequency() == 15)) {
                return true;
            }
            if (getLastVibrationFrequency() < 15) {
                return (resonanceLowerThreshhold < targetDestroyTime && targetDestroyTime <= resonanceThreshhold);
            } else {
                return (50 < targetDestroyTime && targetDestroyTime < 100);
            }
        }
        return false;
    }

    public void breakingBlock(ServerLevel level, BlockState resontatorState ,BlockPos resonatorPos, BlockPos targetPos, ResonatorBlockEntity blockEntity) {
        if (canBreak(level, targetPos)) {
            BlockState targetState = level.getBlockState(targetPos);
            float targetDestroyTime = level.getBlockState(targetPos).getBlock().defaultDestroyTime();
            if (!resontatorState.getValue(ModBlockStateProperties.BREAKING)) {
                level.setBlock(resonatorPos, resontatorState.setValue(ModBlockStateProperties.BREAKING, true), 3);
            }
            if (targetDestroyTime >= 0.0) {
                if (this.breakTicks >= (BREAKING_TIME * (targetDestroyTime / 2))) {
                    this.breakBlock(level, resonatorPos, targetState, targetPos);
                    level.setBlock(resonatorPos, resontatorState.setValue(ModBlockStateProperties.BREAKING, false), 3);
                    blockEntity.breakTicks = 0;
                } else {
                    if (blockEntity.breakTicks % 5 == 0) {
                        spawnParticles(level, targetState, targetPos);
                    }
                    level.scheduleTick(resonatorPos, resontatorState.getBlock(), 1);
                }
            } else {
                if (this.breakTicks >= 1000) {
                    this.breakBlock(level, resonatorPos, targetState, targetPos);
                    level.setBlock(resonatorPos, resontatorState.setValue(ModBlockStateProperties.BREAKING, false), 3);
                    blockEntity.breakTicks = 0;
                } else {
                    if (blockEntity.breakTicks % 5 == 0) {
                        spawnParticles(level, targetState, targetPos);
                    }
                    level.scheduleTick(resonatorPos, resontatorState.getBlock(), 1);
                }
            }
            blockEntity.breakTicks++;
        } else {
            level.setBlock(resonatorPos, resontatorState.setValue(ModBlockStateProperties.BREAKING, false), 3);
            blockEntity.breakTicks = 0;
        }
    }

    public void breakBlock(ServerLevel level, BlockPos resonatorPos, BlockState targetState, BlockPos targetPos) {
        if (!targetState.is(Blocks.BEDROCK)) {
            level.destroyBlock(targetPos, true);
        } else {
            spawnParticles(level, targetState, targetPos);
            level.setBlock(targetPos, ModBlocks.SHATTERED_BEDROCK.get().defaultBlockState(), 3);
            ItemEntity itemEntity = new ItemEntity(level, resonatorPos.getX(), resonatorPos.getY() , resonatorPos.getZ(), new ItemStack(ModItems.BEDROCK_CHIPS.get(), 2));
            if (level.random.nextInt() % 20 == 0) {
                ItemEntity itemEntity2 = new ItemEntity(level, resonatorPos.getX(), resonatorPos.getY() , resonatorPos.getZ(), new ItemStack(ModItems.FISSILE_SHARD.get(), 1));
                level.addFreshEntity(itemEntity2);
            }
            level.addFreshEntity(itemEntity);
        }
    }

    public void spawnParticles(ServerLevel level, BlockState targetState ,BlockPos targetPos) {
        level.levelEvent(null, 2001, targetPos, Block.getId(targetState));
    }

    public VibrationSystem.Listener getListener() {
        return this.vibrationListener;
    }

    public class VibrationUser implements VibrationSystem.User {
        private static final int LISTENER_RADIUS = 16;
        protected final BlockPos blockPos;
        private final PositionSource positionSource;

        public VibrationUser(BlockPos blockPos) {
            this.blockPos = blockPos;
            this.positionSource = new BlockPositionSource(ResonatorBlockEntity.this.worldPosition);
        }

        public int getListenerRadius() {
            return 16;
        }

        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        public TagKey<GameEvent> getListenableEvents() {
            return GameEventTags.SHRIEKER_CAN_LISTEN;
        }

        public boolean canTriggerAvoidVibration() {
            return false;
        }

        public boolean canReceiveVibration(ServerLevel p_281256_, BlockPos p_281528_, GameEvent p_282632_, GameEvent.Context p_282914_) {
            BlockPos resonatorPos = this.blockPos;
            BlockState resonatorState = p_281256_.getBlockState(resonatorPos);
            Direction facing = resonatorState.getValue(ResonatorBlock.FACING);
            BlockPos targetPos = resonatorPos.relative(facing);
            return !(Boolean)ResonatorBlockEntity.this.getBlockState().getValue(ResonatorBlock.BREAKING) && !p_281256_.getBlockState(targetPos).is(BlockTags.REPLACEABLE);
        }

        public void onReceiveVibration(ServerLevel pLevel, BlockPos pPos, GameEvent pGameEvent, @Nullable Entity pEntity, @Nullable Entity pPlayerEntity, float pDistance) {
            BlockPos resonatorPos = this.blockPos;
            BlockState resonatorState = pLevel.getBlockState(resonatorPos);
            Direction facing = resonatorState.getValue(ResonatorBlock.FACING);
            BlockPos targetPos = resonatorPos.relative(facing);
            ResonatorBlockEntity.this.setLastVibrationFrequency(VibrationSystem.getGameEventFrequency(pGameEvent));
            breakingBlock(pLevel, resonatorState, resonatorPos, targetPos, (ResonatorBlockEntity) pLevel.getBlockEntity(resonatorPos));
        }

        public void onDataChanged() {
            ResonatorBlockEntity.this.setChanged();
        }

        public boolean requiresAdjacentChunksToBeTicking() {
            return true;
        }
    }
}
