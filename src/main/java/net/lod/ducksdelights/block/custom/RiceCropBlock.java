package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RiceCropBlock extends CropBlock {
    public static final int MAX_AGE = 7;
    public static final IntegerProperty AGE;
    private static final VoxelShape[] AGE_TO_SHAPE;
    private static final BooleanProperty GOLDEN;

    public RiceCropBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(GOLDEN, false));
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AGE_TO_SHAPE[this.getAge(pState)];
    }

    public int getAge(BlockState state) { return state.getValue(AGE); }

    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(ModBlocks.PADDY_FARMLAND.get());
    }

    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockPos blockpos = pPos.below();
        return this.mayPlaceOn(pLevel.getBlockState(blockpos), pLevel, blockpos);
    }

    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return 7;
    }

    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.getRawBrightness(pPos, 0) >= 9) {
            int age = this.getAge(pState);
            if (age < this.getMaxAge()) {
                float f = getAvailableMoisture(this, pLevel, pPos);
                if (pRandom.nextInt((int)(25.0F / f) + 1) == 0) {
                    if (age == 5 && !pState.getValue(GOLDEN)) {
                        if (pRandom.nextInt(getAvailableGold(pLevel, pPos)) == 0) {
                            pLevel.setBlock(pPos, this.withAge(age + 1, pState.setValue(GOLDEN,true)), 3);
                        } else {
                            pLevel.setBlock(pPos, this.withAge(age + 1,pState), 3);
                        }
                    } else {
                        pLevel.setBlock(pPos, this.withAge(age + 1,pState), 3);
                    }
                }
            }
        }
    }

    public BlockState withAge(int age, BlockState state) {
        return this.withPropertiesOf(state).setValue(AGE, age);
    }

    protected static int getAvailableGold(ServerLevel world, BlockPos pos) {
        int baseChance = 5000;
        int gold = 1;
        for (int below = 2; below <= 6; ++below) {
            BlockState getGold = world.getBlockState(pos.below(below));
            if (getGold.is(Blocks.GOLD_BLOCK) || getGold.is(Blocks.RAW_GOLD_BLOCK)) {
                gold *= 3;
            }
            if (getGold.is(Blocks.GOLD_ORE) || getGold.is(Blocks.DEEPSLATE_GOLD_ORE)) {
                gold *= 3;
                gold -= 1;
            }
            if (getGold.is(Blocks.NETHER_GOLD_ORE)) {
                gold *= 2;
            }
        }
        return baseChance / gold;
    }

    protected static float getAvailableMoisture(Block block, ServerLevel world, BlockPos pos) {
        float f = 1.0F;
        BlockPos blockPos = pos.below();

        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                float g = 0.0F;
                BlockState blockState = world.getBlockState(blockPos.offset(i, 0, j));
                if (blockState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                    if (blockState.is(ModBlocks.PADDY_FARMLAND.get())) {
                        g = 1.0F;
                        if (blockState.getValue(PaddyFarmlandBlock.MOISTURE) > 0) {
                            g = 3.0F;
                        }
                    } else {
                        if (blockState.getFluidState().is(Fluids.WATER)) {
                            g = 3.0F;
                        } else {
                            g = 1.0F;
                        }
                    }
                }

                if (i != 0 || j != 0) {
                    g /= 4.0F;
                }

                f += g;
            }
        }
        if (world.getBlockState(blockPos).is(ModBlocks.PADDY_FARMLAND.get()) && !world.getBlockState(blockPos).getValue(BlockStateProperties.WATERLOGGED)) {
            f /= 2.0F;
        }
        return f / 2.0F;
    }

    protected ItemLike getBaseSeedId() {
        return ModItems.RAW_RICE.get();
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE, GOLDEN);
    }

    static {
        AGE = IntegerProperty.create("age", 0, 7);
        GOLDEN = BooleanProperty.create("golden");
        AGE_TO_SHAPE = new VoxelShape[]{
                Block.box(7.0, -6.0, 7.0, 9.0, 1.0, 9.0),
                Block.box(6.0, -6.0, 6.0, 10.0, 3.0, 10.0),
                Block.box(5.0, -6.0, 5.0, 11.0, 6.0, 11.0),
                Block.box(5.0, -6.0, 5.0, 11.0, 9.0, 11.0),
                Block.box(4.0, -6.0, 4.0, 12.0, 11.0, 12.0),
                Block.box(4.0, -6.0, 4.0, 12.0, 13.0, 12.0),
                Block.box(3.0, -6.0, 3.0, 13.0, 15.0, 13.0),
                Block.box(3.0, -6.0, 3.0, 13.0, 14.0, 13.0)
        };
    }
}
