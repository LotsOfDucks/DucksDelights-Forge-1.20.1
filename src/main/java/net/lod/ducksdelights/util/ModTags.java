package net.lod.ducksdelights.util;

import net.lod.ducksdelights.DucksDelights;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Biomes {
        public static final TagKey<Biome> MARSHMALLOW_ROOT_SPAWNABLE = tag("marshmallow_root_spawnable");

        public static final TagKey<Biome> GIANT_CLAM_SPAWNABLE = tag("giant_clam_spawnable");
        public static final TagKey<Biome> GIANT_CLAM_NETHER_SPAWNABLE = tag("giant_clam_nether_spawnable");
        public static final TagKey<Biome> GIANT_CLAM_END_SPAWNABLE = tag("giant_clam_end_spawnable");

        @SuppressWarnings("removal")
        private static TagKey<Biome> tag(String name) {
            return TagKey.create(Registries.BIOME, new ResourceLocation(DucksDelights.MOD_ID, name));
        }
    }

    public static class Blocks {

        public static final TagKey<Block> RICE_CROP_GOLD_STRONG = tag("rice_crop_gold_strong");
        public static final TagKey<Block> RICE_CROP_GOLD_AVERAGE = tag("rice_crop_gold_average");
        public static final TagKey<Block> RICE_CROP_GOLD_WEAK = tag("rice_crop_gold_weak");

        public static final TagKey<Block> GIANT_MARSHMALLOWS = tag("giant_marshmallows");

        public static final TagKey<Block> GIANT_CLAMS = tag("giant_clams");
        public static final TagKey<Block> GIANT_CLAM_SPAWNABLE = tag("giant_clam_spawnable");
        public static final TagKey<Block> GIANT_CLAM_REPLACEABLE = tag("giant_clam_replaceable");
        public static final TagKey<Block> GIANT_CLAM_NETHER_SPAWNABLE = tag("giant_clam_nether_spawnable");
        public static final TagKey<Block> GIANT_CLAM_END_SPAWNABLE = tag("giant_clam_end_spawnable");

        public static final TagKey<Block> CLEAVER_EFFICIENT = tag("cleaver_efficient");
        public static final TagKey<Block> CLEAVER_DROPS = tag("cleaver_drops");

        @SuppressWarnings("removal")
        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(DucksDelights.MOD_ID, name));
        }
    }

    public static class Items {

        public static final TagKey<Item> CLEAVERS = tag("cleavers");
        public static final TagKey<Item> ONIGIRI = tag("onigiri");

        @SuppressWarnings("removal")
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(DucksDelights.MOD_ID, name));
        }
    }
}
