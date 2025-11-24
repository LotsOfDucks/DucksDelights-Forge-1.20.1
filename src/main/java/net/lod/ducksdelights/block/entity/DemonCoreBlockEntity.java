package net.lod.ducksdelights.block.entity;

import net.lod.ducksdelights.block.custom.DemonCoreBlock;
import net.lod.ducksdelights.block.custom.interfaces.IRadiativeBlockEntity;
import net.lod.ducksdelights.damage.ModDamageTypes;
import net.lod.ducksdelights.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DemonCoreBlockEntity extends BlockEntity implements IRadiativeBlockEntity {
    private boolean powered;
    private boolean waterlogged;
    private boolean lavalogged;
    private boolean logged;
    public int ticks;
    public int range = 20;
    public float damageScale = 1.0F;

    public DemonCoreBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.DEMON_CORE_BE.get(), pPos, pBlockState);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    public static void clientTick(Level pLevel, BlockPos pPos, BlockState pState, DemonCoreBlockEntity pBlockEntity) {
        ++pBlockEntity.ticks;
        pBlockEntity.powered = pLevel.getBlockState(pPos).getValue(DemonCoreBlock.POWERED);
        pBlockEntity.waterlogged = pLevel.getBlockState(pPos).getValue(DemonCoreBlock.WATERLOGGED);
        if (pBlockEntity.waterlogged && pBlockEntity.powered) {
            double x = pPos.getCenter().x();
            double y = pPos.getCenter().y();
            double z = pPos.getCenter().z();
            pLevel.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, x + (0.5 *Math.random()), y + 0.02, z + (0.5 *Math.random()), Math.random(), 0.02, Math.random());
            pLevel.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, x + (-0.5 *Math.random()), y + 0.02, z + (-0.5 *Math.random()), (-1 *Math.random()), 0.02, (-1 * Math.random()));
            pLevel.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, x + (-0.5 *Math.random()), y + 0.02, z + (0.5 *Math.random()), (-1 *Math.random()), 0.02, Math.random());
            pLevel.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, x + (0.5 *Math.random()), y + 0.02, z + (-0.5 *Math.random()), Math.random(), 0.02, (-1 * Math.random()));
        }

    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, DemonCoreBlockEntity pBlockEntity) {
        ++pBlockEntity.ticks;
        pBlockEntity.powered = pLevel.getBlockState(pPos).getValue(DemonCoreBlock.POWERED);
        pBlockEntity.logged = pLevel.getBlockState(pPos).getValue(DemonCoreBlock.LOGGED);
        if (pBlockEntity.powered) {
            DamageSource damageSource = new DamageSource(
                    pLevel.registryAccess()
                            .registryOrThrow(Registries.DAMAGE_TYPE)
                            .getHolder(ModDamageTypes.FISSION).get()
            );
            IRadiativeBlockEntity.getEntitiesInRange(pLevel, pPos, pState, pBlockEntity, pBlockEntity.range, pBlockEntity.damageScale, 0.51F, 0.51F, 0.49F, 0.51F, 0.51F, 0.51F, damageSource);
            if (pLevel.getGameTime() % 40L == 0L) {
                if (!pBlockEntity.logged) {
                    pLevel.playSound(null, pPos, ModSoundEvents.DEMON_CORE_AMBIENT.get(), SoundSource.BLOCKS, 4.0F, 1.0F);
                } else {
                    pLevel.playSound(null, pPos, ModSoundEvents.DEMON_CORE_AMBIENT.get(), SoundSource.BLOCKS, 1.0F, 0.5F);
                }
            }
        }
    }
}
