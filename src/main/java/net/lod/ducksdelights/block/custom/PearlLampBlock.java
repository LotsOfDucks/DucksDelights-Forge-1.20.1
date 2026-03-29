package net.lod.ducksdelights.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PearlLampBlock extends RedstoneLampBlock {
    public PearlLampBlock(Properties pProperties) {
        super(pProperties);
    }

    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (!pLevel.isClientSide) {
            boolean $$6 = pState.getValue(LIT);
            if ($$6 != pLevel.hasNeighborSignal(pPos)) {
                if ($$6) {
                    pLevel.scheduleTick(pPos, this, 1);
                } else {
                    pLevel.setBlock(pPos, pState.cycle(LIT), 2);
                }
            }

        }
    }

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(LIT) && !pLevel.hasNeighborSignal(pPos)) {
            pLevel.setBlock(pPos, pState.cycle(LIT), 2);
        }

    }
}
