package net.lod.ducksdelights.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GiantMarshmallowBlock extends Block {
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);

    public GiantMarshmallowBlock(Properties pProperties) {
        super(pProperties);
    }

    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {
        if (pEntity.isSuppressingBounce()) {
            super.fallOn(pLevel, pState, pPos, pEntity, pFallDistance);
        } else {
            pEntity.causeFallDamage(pFallDistance, 0.0F, pLevel.damageSources().fall());
        }

    }

    public void updateEntityAfterFallOn(BlockGetter pLevel, Entity pEntity) {
        if (pEntity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(pLevel, pEntity);
        } else {
            this.launch(pEntity);
        }

    }

    private void launch(Entity pEntity) {
        Vec3 entityMovement = pEntity.getDeltaMovement();
        if (entityMovement.y < 0.0) {
            if (pEntity.level().getBlockState(pEntity.blockPosition()).getBlock() instanceof GiantMarshmallowBlock) {
                if (pEntity.level().hasNeighborSignal(pEntity.blockPosition())) {
                    float newSpeed = pEntity.level().getBestNeighborSignal(pEntity.blockPosition());
                    if (pEntity instanceof AbstractMinecart) {
                        pEntity.setDeltaMovement(entityMovement.x, (Math.log1p(newSpeed) + 1), entityMovement.z);
                    } else {
                        pEntity.setDeltaMovement(entityMovement.x, (Math.log1p(newSpeed) + 1) / 2, entityMovement.z);
                    }
                } else {
                    if (pEntity instanceof AbstractMinecart) {
                        pEntity.setDeltaMovement(entityMovement.x, 1.6F, entityMovement.z);
                    } else {
                        pEntity.setDeltaMovement(entityMovement.x, 0.8F, entityMovement.z);
                    }
                }
            } else {
                if (pEntity instanceof AbstractMinecart) {
                    pEntity.setDeltaMovement(entityMovement.x, 1.6F, entityMovement.z);
                } else {
                    pEntity.setDeltaMovement(entityMovement.x, 0.8F, entityMovement.z);
                }
            }
        }
    }
}
