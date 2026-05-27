package net.lod.ducksdelights.worldgen.features;

import com.mojang.serialization.Codec;
import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.block.custom.AbstractGiantClamBlock;
import net.lod.ducksdelights.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraftforge.common.Tags;

public class GiantClamNetherFeature extends Feature<RandomPatchConfiguration> {
    public GiantClamNetherFeature(Codec<RandomPatchConfiguration> pCodec) {
        super(pCodec);
    }

    public boolean place(FeaturePlaceContext<RandomPatchConfiguration> context) {
        RandomPatchConfiguration config = context.config();
        RandomSource random = context.random();
        BlockPos originPos = context.origin();
        WorldGenLevel level = context.level();
        int successes = 0;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int xzSpread = config.xzSpread() + 1;
        int ySpread = config.ySpread() + 1;

        for(int rolls = 0; rolls < config.tries(); ++rolls) {
            BlockState placementState = ModBlocks.GIANT_CLAM_NETHER.get().defaultBlockState().setValue(AbstractGiantClamBlock.FACING, getRandomDirection(random));
            mutableBlockPos.setWithOffset(originPos, random.nextInt(xzSpread) - random.nextInt(xzSpread), random.nextInt(ySpread) - random.nextInt(ySpread), random.nextInt(xzSpread) - random.nextInt(xzSpread));
            if (level.getBlockState(mutableBlockPos).is(BlockTags.REPLACEABLE) && level.getBlockState(mutableBlockPos.below()).is(ModTags.Blocks.GIANT_CLAM_NETHER_SPAWNABLE)) {
                level.setBlock(mutableBlockPos, placementState, 2);
                ++successes;
            }
        }
        return successes > 0;
    }

    public Direction getRandomDirection(RandomSource random) {
        return switch (random.nextIntBetweenInclusive(1,4)) {
            case 1 -> Direction.NORTH;
            case 2 -> Direction.SOUTH;
            case 3 -> Direction.WEST;
            case 4 -> Direction.EAST;
            default -> Direction.NORTH;
        };
    }
}
