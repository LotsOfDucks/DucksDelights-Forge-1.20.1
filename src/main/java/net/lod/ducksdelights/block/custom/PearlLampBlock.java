package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.entity.PearlLampBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class PearlLampBlock extends BaseEntityBlock {
    public static final BooleanProperty LIT;

    public PearlLampBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, false));
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(LIT, pContext.getLevel().hasNeighborSignal(pContext.getClickedPos()));
    }

    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (!pLevel.isClientSide) {
            boolean wasLit = pState.getValue(LIT);
            if (wasLit != pLevel.hasNeighborSignal(pPos)) {
                pLevel.scheduleTick(pPos, this, 1);
            }
        }
    }

    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
        if (pLevel.getBlockEntity(pPos) instanceof PearlLampBlockEntity blockEntity) {
            if (pLevel.getBlockState(pPos).getValue(LIT)) {
                if (blockEntity.hasCustomColor) {
                    int red = Math.round(blockEntity.tintRed * 5);
                    int green = Math.round(blockEntity.tintGreen * 5);
                    int blue = Math.round(blockEntity.tintBlue * 5);
                    return Math.min((red + green + blue), 15);
                }
                return 15;
            }
            return 0;

        }
        return pLevel.getBlockState(pPos).getValue(LIT) ? 15 : 0;
    }

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        boolean wasLit = pState.getValue(LIT);
        if (wasLit != pLevel.hasNeighborSignal(pPos)) {
            pLevel.setBlock(pPos, pState.cycle(LIT), 3);
        }
    }

    @Override
    public boolean isConduitFrame(BlockState state, LevelReader level, BlockPos pos, BlockPos conduit) {
        return true;
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PearlLampBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (pLevel.getBlockEntity(pPos) instanceof PearlLampBlockEntity blockEntity) {
            if (itemstack.getItem() instanceof DyeItem dyeItem) {
                blockEntity.dyeLamp(dyeItem);
                if (!pPlayer.isCreative()) {
                    itemstack.shrink(1);
                }
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            } else if (itemstack.is(Items.SPONGE) && blockEntity.hasCustomColor) {
                blockEntity.clearDyes();
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            }
            return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LIT);
    }

    static {
        LIT = RedstoneTorchBlock.LIT;
    }


}
