package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GiantMarshmallowBlock extends Block {
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);

    public GiantMarshmallowBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.block();
    }

    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return true;
    }

    @Override
    public boolean isSlimeBlock(BlockState state) {
        return true;
    }

    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {
        pEntity.resetFallDistance();
    }

    public void updateEntityAfterFallOn(BlockGetter pLevel, Entity pEntity) {
        if (pEntity.isSuppressingBounce()) {
            this.bounceUp(pEntity);
        } else {
            this.launch(pEntity);
            this.doSound(pEntity);
        }
    }

    public void launch(Entity pEntity) {
        Vec3 entityMovement = pEntity.getDeltaMovement();
        double xSpeed = entityMovement.x;
        double zSpeed = entityMovement.z;
        float newYSpeed = 0.8F;
        if (pEntity instanceof AbstractMinecart) {
            xSpeed *= 10;
            newYSpeed *= 2;
            zSpeed *= 10;
        }
        if (pEntity.getFeetBlockState().getBlock() instanceof GiantMarshmallowBlock) {
            if (pEntity.level().hasNeighborSignal(pEntity.blockPosition())) {
                double newYSpeedPowered = Math.log1p(pEntity.level().getBestNeighborSignal(pEntity.blockPosition())) + 1;
                if (!(pEntity instanceof AbstractMinecart)) {
                    newYSpeedPowered /= 2;
                }
                pEntity.setDeltaMovement(xSpeed, newYSpeedPowered, zSpeed);
            } else {
                pEntity.setDeltaMovement(xSpeed, newYSpeed, zSpeed);
            }
        } else {
            pEntity.setDeltaMovement(xSpeed, newYSpeed, zSpeed);
        }
    }

    public void bounceUp(Entity pEntity) {
        Vec3 motion = pEntity.getDeltaMovement();
        if (motion.y < 0.0) {
            double motionChange = pEntity instanceof LivingEntity ? 1.0 : 0.8;
            pEntity.setDeltaMovement(motion.x, -motion.y * motionChange, motion.z);

        }
        if (motion.y >= 0.1) {
            this.doSound(pEntity);
        } else if (motion.y <= -0.1) {
            this.doSound(pEntity);
        }
    }

    public void doSound(Entity pEntity) {
        if (pEntity instanceof LivingEntity) {
            pEntity.level().playSound(null, pEntity, ModSoundEvents.GIANT_MARSHMALLOW_BOOWOOP.get(), SoundSource.BLOCKS, 1, 1);
        }
        if (pEntity instanceof AbstractMinecart) {
            pEntity.level().playSound(null, pEntity, ModSoundEvents.GIANT_MARSHMALLOW_BOOWOOP.get(), SoundSource.BLOCKS, 1, 1);
        }
        if (pEntity instanceof Boat) {
            pEntity.level().playSound(null, pEntity, ModSoundEvents.GIANT_MARSHMALLOW_BOOWOOP.get(), SoundSource.BLOCKS, 1, 1);
        }
    }
}
