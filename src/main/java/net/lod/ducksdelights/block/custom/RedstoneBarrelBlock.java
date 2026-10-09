package net.lod.ducksdelights.block.custom;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RedstoneBarrelBlock extends FillableBarrelBlock{
    private static final Vec3[] COLORS;

    public RedstoneBarrelBlock(ItemLike fillItem, Properties pProperties) {
        super(fillItem, pProperties);
    }

    public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        return pBlockState.getValue(FULLNESS);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return true;
    }

    public static int getColorForPower(int pPower) {
        Vec3 vec3 = COLORS[pPower];
        return Mth.color((float)vec3.x(), (float)vec3.y(), (float)vec3.z());
    }

    static {
        COLORS = Util.make(new Vec3[16], (vec3s) -> {
            for(int i = 0; i <= 15; ++i) {
                float f = (float)i / 15.0F;
                float f1 = f * 0.6F + (f > 0.0F ? 0.4F : 0.3F);
                float f2 = Mth.clamp(f * f * 0.7F - 0.5F, 0.0F, 1.0F);
                float f3 = Mth.clamp(f * f * 0.6F - 0.7F, 0.0F, 1.0F);
                vec3s[i] = new Vec3(f1, f2, f3);
            }

        });
    }
}
