package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlockEntities;
import net.lod.ducksdelights.block.entity.PocketDimBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PocketDimBlock extends BaseEntityBlock {
    public static final BooleanProperty POWERED;
    public static final int RANGE = 5;

    public PocketDimBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false));
    }

    public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pSide) {
        return pAdjacentBlockState.is(this) ? true : super.skipRendering(pState, pAdjacentBlockState, pSide);
    }

    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PocketDimBlockEntity(pPos, pState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide ? createTickerHelper(pBlockEntityType, ModBlockEntities.POCKET_DIM_BE.get(), PocketDimBlock::tickEntity) : null;
    }

    private static void tickEntity(Level world, BlockPos blockPos, BlockState blockState, PocketDimBlockEntity blockEntity) {
        //if (world.getGameTime() % 5L == 0L) {
        getEntitiesInRange(world, blockPos, blockState);
        //}
    }

    public static void getEntitiesInRange(Level level, BlockPos pos, BlockState blockState) {
        int boxX = pos.getX();
        int boxY = pos.getY();
        int boxZ = pos.getZ();
        AABB box = (new AABB(boxX, boxY, boxZ, boxX + 1, boxY + 1, boxZ + 1)).inflate(PocketDimBlock.RANGE);
        List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, box, LivingEntity::attackable);
        if (!list.isEmpty()) {
            int targets = 0;
            for (LivingEntity livingEntity : list) {
                if (!livingEntity.hasEffect(MobEffects.INVISIBILITY)) {
                    if (livingEntity instanceof Player player) {
                        if (!player.isSpectator()) {
                            if (pos.getCenter().closerThan(livingEntity.position(), PocketDimBlock.RANGE) && livingEntity.isAlive()) {
                                targets++;
                            }
                        }
                    } else if (pos.getCenter().closerThan(livingEntity.position(), PocketDimBlock.RANGE) && livingEntity.isAlive()) {
                        targets++;
                    }
                }
            }
            if (targets > 15) {
                targets = 15;
            }
            boolean hasTarget = targets > 0;
            level.setBlock(pos, blockState.setValue(POWERED, hasTarget), 3);
        } else {
            if (level.getBlockState(pos).getValue(POWERED)) {
                level.setBlock(pos, blockState.setValue(POWERED, false), 3);
            }
        }
    }

    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return true;
    }

    public RenderShape getRenderShape(BlockState pState) {
        return !pState.getValue(POWERED) ? RenderShape.INVISIBLE : RenderShape.MODEL;
    }

    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1.0F;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWERED);
    }

    static {
        POWERED = BlockStateProperties.LIT;
    }
}
