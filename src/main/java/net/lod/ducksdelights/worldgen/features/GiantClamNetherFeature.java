package net.lod.ducksdelights.worldgen.features;

import com.mojang.serialization.Codec;
import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.block.custom.AbstractGiantClamBlock;
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

public class GiantClamNetherFeature extends Feature<CountConfiguration> {
    public GiantClamNetherFeature(Codec<CountConfiguration> configurationCodec) {
        super(configurationCodec);
    }

    public boolean place(FeaturePlaceContext<CountConfiguration> context) {
        int successes = 0;
        RandomSource random = context.random();
        WorldGenLevel level = context.level();
        BlockPos originPos = context.origin();
        BlockPos belowPos = context.origin().below();

        if (!level.getBlockState(belowPos).is(BlockTags.SOUL_SPEED_BLOCKS)) {
            return false;
        } else {
            int count = context.config().count().sample(random);

            for (int roll = 0; roll < count; ++roll) {
                int xOffset = random.nextInt(12) - random.nextInt(12);
                int yOffset = random.nextInt(12) - random.nextInt(12);
                int zOffset = random.nextInt(12) - random.nextInt(12);
                int yHeightMod = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, originPos.getX() + xOffset, originPos.getZ() + zOffset);
                BlockPos placementPos = new BlockPos(originPos.getX() + xOffset, yHeightMod + yOffset, originPos.getZ() + zOffset);
                BlockState placementState = ModBlocks.GIANT_CLAM_NETHER.get().defaultBlockState().setValue(AbstractGiantClamBlock.OPEN, true).setValue(AbstractGiantClamBlock.WATERLOGGED, true).setValue(AbstractGiantClamBlock.LOGGED, true).setValue(AbstractGiantClamBlock.FACING, getRandomDirection(random));
                if ((level.getBlockState(placementPos).is(Blocks.AIR) || level.getBlockState(placementPos).is(Blocks.CAVE_AIR)) && (level.getBlockState(placementPos.below()).is(BlockTags.SOUL_SPEED_BLOCKS) || level.getBlockState(placementPos.below()).is(Blocks.BEDROCK))) {
                    level.setBlock(placementPos, placementState, 2);
                    ++successes;
                }
            }

            return successes > 0;
        }
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
