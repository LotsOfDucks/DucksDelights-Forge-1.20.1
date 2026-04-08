package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.Config;
import net.lod.ducksdelights.block.ModBlockEntities;
import net.lod.ducksdelights.block.entity.MobDetectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;

public class MobDetectorBlock extends BaseEntityBlock {
    public static final IntegerProperty POWER;
    
    public MobDetectorBlock(Properties pProperties) {
        super(pProperties);
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MobDetectorBlockEntity(pPos, pState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide && pLevel.dimensionType().hasSkyLight() ? createTickerHelper(pBlockEntityType, ModBlockEntities.MOB_DETECTOR_BE.get(), MobDetectorBlock::tickEntity) : null;
    }

    private static void tickEntity(Level world, BlockPos blockPos, BlockState blockState, MobDetectorBlockEntity blockEntity) {
        if (world.getGameTime() % 5L == 0L) {
            getEntitiesInRange(world, blockPos, blockState);
        }
    }

    public static void getEntitiesInRange(Level level, BlockPos pos, BlockState blockState) {
        int boxX = pos.getX();
        int boxY = pos.getY();
        int boxZ = pos.getZ();
        AABB box = (new AABB(boxX, boxY, boxZ, boxX + 1, boxY + 1, boxZ + 1)).inflate(Config.monster_detector_range);
        List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, box, LivingEntity::attackable);
        if (!list.isEmpty()) {
            int targets = 0;
            for (LivingEntity livingEntity : list) {
                if (livingEntity instanceof Enemy) {
                    if (!livingEntity.hasEffect(MobEffects.INVISIBILITY)) {
                        if (pos.getCenter().closerThan(livingEntity.position(), Config.monster_detector_range) && livingEntity.isAlive()) {
                            targets++;
                        }
                    }
                }
            }
            if (targets > 15) {
                targets = 15;
            }
            level.setBlock(pos, blockState.setValue(POWER, targets), 3);
        } else {
            if (level.getBlockState(pos).getValue(POWER) != 0) {
                level.setBlock(pos, blockState.setValue(POWER, 0), 3);
            }
        }
    }

    public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        return pBlockState.getValue(POWER);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @org.jetbrains.annotations.Nullable Direction direction) {
        return true;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWER);
    }

    static {
        POWER = BlockStateProperties.POWER;
    }
}
