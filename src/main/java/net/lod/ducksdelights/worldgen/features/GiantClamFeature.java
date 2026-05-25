package net.lod.ducksdelights.worldgen.features;

import com.mojang.serialization.Codec;
import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.block.custom.AbstractGiantClamBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;

public class GiantClamFeature extends Feature<CountConfiguration> {
    public GiantClamFeature(Codec<CountConfiguration> configurationCodec) {
        super(configurationCodec);
    }

    public boolean place(FeaturePlaceContext<CountConfiguration> context) {
        int successes = 0;
        RandomSource random = context.random();
        WorldGenLevel level = context.level();
        BlockPos originPos = context.origin();
        int count = context.config().count().sample(random);

        for(int roll = 0; roll < count; ++roll) {
            int xOffset = random.nextInt(8) - random.nextInt(8);
            int zOffset = random.nextInt(8) - random.nextInt(8);
            int yHeight = level.getHeight(Heightmap.Types.OCEAN_FLOOR, originPos.getX() + xOffset, originPos.getZ() + zOffset);
            BlockPos placementPos = new BlockPos(originPos.getX() + xOffset, yHeight, originPos.getZ() + zOffset);
            BlockState placementState = getRandomClam(random).setValue(AbstractGiantClamBlock.WATERLOGGED, true).setValue(AbstractGiantClamBlock.LOGGED, true).setValue(AbstractGiantClamBlock.FACING, getRandomDirection(random));
            if (level.getBlockState(placementPos).is(Blocks.WATER) && level.getBlockState(placementPos.below()).is(BlockTags.SAND)) {
                level.setBlock(placementPos, placementState, 2);
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

    public BlockState getRandomClam(RandomSource random) {
        return switch (random.nextIntBetweenInclusive(1,4)) {
            case 1 -> ModBlocks.GIANT_CLAM_BROWN.get().defaultBlockState();
            case 2 -> ModBlocks.GIANT_CLAM_GREEN.get().defaultBlockState();
            case 3 -> ModBlocks.GIANT_CLAM_BLUE.get().defaultBlockState();
            case 4 -> ModBlocks.GIANT_CLAM_WHITE.get().defaultBlockState();
            default -> ModBlocks.GIANT_CLAM_BROWN.get().defaultBlockState();
        };
    }
}
