package net.lod.ducksdelights.worldgen.features;

import com.mojang.serialization.Codec;
import net.lod.ducksdelights.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.function.Predicate;

public class MonolithFeature extends Feature<NoneFeatureConfiguration> {
    public MonolithFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        BlockPos blockpos = featurePlaceContext.origin();
        RandomSource randomsource = featurePlaceContext.random();
        WorldGenLevel worldgenlevel = featurePlaceContext.level();
        int xScale = randomsource.nextIntBetweenInclusive(3, 6);
        int zScale = randomsource.nextIntBetweenInclusive(3, 6);
        int minHeight = worldgenlevel.getMinBuildHeight() + 1;
        int maxHeight = Math.max(minHeight + 50, worldgenlevel.getMaxBuildHeight() - (randomsource.nextIntBetweenInclusive(0, 50)));
        for (int xPos = -xScale; xPos <= xScale; ++xPos) {
            for (int zPos = -zScale; zPos <= zScale; ++zPos) {
                for (int yPos = minHeight; yPos <= maxHeight; ++yPos) {
                    BlockPos placementPos = new BlockPos(blockpos.getX() + xPos, yPos, blockpos.getZ() + zPos);
                    this.setBlock(worldgenlevel, placementPos, ModBlocks.MONOLITH.get().defaultBlockState());
                }
            }
        }
        return true;
    }
}
