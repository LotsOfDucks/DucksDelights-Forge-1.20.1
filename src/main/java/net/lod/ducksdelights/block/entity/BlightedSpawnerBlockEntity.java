package net.lod.ducksdelights.block.entity;

import net.lod.ducksdelights.block.ModBlockEntities;
import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.block.entity.spawners.BlightedSpawner;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BlightedSpawnerBlockEntity extends BlockEntity {

    public BlightedSpawnerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BLIGHTED_SPAWNER_BE.get(), pPos, pBlockState);
    }

    //refer to the BlightedSpawner comment.

    private final BlightedSpawner spawner = new BlightedSpawner(4, 12, 24, 4) {
        public void broadcastEvent(Level p_155767_, BlockPos p_155768_, int p_155769_) {
            p_155767_.blockEvent(p_155768_, ModBlocks.BLIGHTED_SPAWNER_BLOCK.get(), p_155769_, 0);
        }

        public void setNextSpawnData(@Nullable Level p_155771_, BlockPos p_155772_, SpawnData p_155773_) {
            super.setNextSpawnData(p_155771_, p_155772_, p_155773_);
            if (p_155771_ != null) {
                BlockState blockstate = p_155771_.getBlockState(p_155772_);
                p_155771_.sendBlockUpdated(p_155772_, blockstate, blockstate, 4);
            }

        }

        public @Nullable BlockEntity getSpawnerBlockEntity() {
            return BlightedSpawnerBlockEntity.this;
        }
    };

    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.spawner.load(this.level, this.worldPosition, pTag);
    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        this.spawner.save(pTag);
    }

    public static void clientTick(Level pLevel, BlockPos pPos, BlockState pState, BlightedSpawnerBlockEntity pBlockEntity) {
        pBlockEntity.spawner.clientTick(pLevel, pPos);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, BlightedSpawnerBlockEntity pBlockEntity) {
        pBlockEntity.spawner.serverTick((ServerLevel)pLevel, pPos);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag compoundtag = this.saveWithoutMetadata();
        compoundtag.remove("SpawnPotentials");
        return compoundtag;
    }

    public boolean triggerEvent(int pId, int pType) {
        return this.spawner.onEventTriggered(this.level, pId) ? true : super.triggerEvent(pId, pType);
    }

    public boolean onlyOpCanSetNbt() {
        return false;
    }

    public void setEntityId(EntityType<?> pType, RandomSource pRandom) {
        this.spawner.setEntityId(pType, this.level, pRandom, this.worldPosition);
    }

    public void clearEntityId(RandomSource pRandom) {
        this.spawner.clearEntityId(this.level, pRandom, this.worldPosition);
    }

    public BlightedSpawner getSpawner() {
        return this.spawner;
    }

    public void spawnParticles(BlockPos pos) {
        if (this.level instanceof ClientLevel) {
            for(int k = 0; k < 20; ++k) {
                double x = (double)pos.getX() + 0.5 + (this.getLevel().random.nextDouble() - 0.5) * 2.0;
                double y = (double)pos.getY() + 0.5 + (this.getLevel().random.nextDouble() - 0.5) * 2.0;
                double z = (double)pos.getZ() + 0.5 + (this.getLevel().random.nextDouble() - 0.5) * 2.0;
                assert this.level != null;
                this.level.addParticle(ParticleTypes.SOUL, x, y, z, 0.0, 0.0, 0.0);
            }
        }
    }
}

