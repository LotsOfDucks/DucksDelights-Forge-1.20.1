package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.item.ModItems;
import net.lod.ducksdelights.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlackberryCropBlock extends BushBlock implements BonemealableBlock {
    public static final IntegerProperty AGE;
    public static final BooleanProperty MAX_AGE;
    public static final IntegerProperty STAGE;
    public static final BooleanProperty HAS_FRUIT;
    public static final BooleanProperty IS_SPREADING;
    private static final VoxelShape[] AGE_TO_SHAPE;

    public BlackberryCropBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(MAX_AGE, false).setValue(STAGE, 0).setValue(HAS_FRUIT, false).setValue(IS_SPREADING, false));
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AGE_TO_SHAPE[this.getAge(pState)];
    }

    protected ItemLike getBaseSeedId() {
        return ModItems.BLACKBERRIES.get();
    }

    public boolean isRandomlyTicking(BlockState pState) {
        return !this.hasFruit(pState) || this.isSpreading(pState);
    }

    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        pLevel.setBlock(pPos, pState.setValue(IS_SPREADING, canSpread(pLevel, pPos)), 2);
        if (pState.getValue(IS_SPREADING)) {
            this.applySpread(pLevel, pPos, pState, pRandom);
        }
        if (pLevel.getRawBrightness(pPos, 0) >= 9) {
            int age = this.getAge(pState);
            int maxAge = this.getMaxAge(pState);
            int stage = this.getStage(pState);
            if (age < maxAge) {
                if (pRandom.nextInt(10) == 0) {
                    pLevel.setBlock(pPos, this.withAge(age + 1, pState), 2);
                }
            }
            if (age == maxAge) {
                pLevel.setBlock(pPos, pState.setValue(MAX_AGE, true), 2);
                if (stage < 2 && pLevel.getBlockState(pPos.above()).is(Blocks.AIR)) {
                    if (pRandom.nextInt(5) == 0) {
                        pLevel.setBlock(pPos.above(), this.withPropertiesOf(pState).setValue(STAGE, this.getStage(pState)+1).setValue(AGE, this.getAge(pState)+1).setValue(MAX_AGE, false), 2);
                    }
                }
                if (!pState.getValue(HAS_FRUIT)) {
                    if (pRandom.nextInt(20) == 0) {
                        pLevel.setBlock(pPos, this.withFruit(true, pState), 2);
                    }
                }
            }
        }
    }

    public int getAge(BlockState state) { return state.getValue(AGE); }

    public int getMaxAge(BlockState state) {
        return switch (this.getStage(state)) {
            case 1 -> 4;
            case 2 -> 5;
            default -> 2;
        };
    }

    public boolean isMaxAgeForStage(BlockState state) {
        return state.getValue(MAX_AGE);
    }

    public BlockState withAge(int age, BlockState state) {
        return this.withPropertiesOf(state).setValue(AGE, age);
    }

    public int getStage(BlockState state) {
        return state.getValue(STAGE);
    }

    public boolean hasFruit(BlockState state) {
        return state.getValue(HAS_FRUIT);
    }

    public BlockState withFruit(boolean hasfruit, BlockState state) {
        return this.withPropertiesOf(state).setValue(HAS_FRUIT, hasfruit);
    }

    protected boolean canPlantOnTop(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(BlockTags.DIRT);
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    public boolean mayPlaceOn(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockPos blockpos = pPos.below();
        return this.canPlantOnTop(pLevel.getBlockState(blockpos), pLevel, blockpos);
    }

    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        if (world.getBlockState(pos.below()).is(ModBlocks.BLACKBERRY_CROP.get())) {
            return world.getBlockState(pos.below()).getValue(STAGE) < 2 && world.getBlockState(pos.below()).getValue(MAX_AGE);
        } else return this.mayPlaceOn(world.getBlockState(pos), world, pos);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean b) {
        if (blockState.getValue(STAGE) == 0) {
            if (blockState.getValue(AGE) <= 2|| levelReader.getBlockState(blockPos.above()).is(Blocks.AIR)) {
                return true;
            }
        } else if (blockState.getValue(STAGE) == 1) {
            if (blockState.getValue(AGE) <= 4|| levelReader.getBlockState(blockPos.above()).is(Blocks.AIR)) {
                return true;
            }
        }
        return !blockState.getValue(HAS_FRUIT);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        this.applyGrowth(serverLevel, blockPos, blockState);
    }

    public void applyGrowth(Level world, BlockPos pos, BlockState state) {
        int i = Math.min(this.getMaxAge(state), this.getAge(state) + this.getGrowthAmount(world));
        world.setBlock(pos, this.withAge(i, state), 2);
        if (this.getAge(state) == this.getMaxAge(state)) {
            world.setBlock(pos, this.withFruit(true, state).setValue(MAX_AGE, true), 2);
            if (world.getBlockState(pos.above()).is(Blocks.AIR) && world.getBlockState(pos).getValue(STAGE) <= 1) {
                world.setBlock(pos.above(), this.withPropertiesOf(state).setValue(STAGE, this.getStage(state)+1).setValue(AGE, this.getAge(state)+1).setValue(MAX_AGE, false), 2);
            }
        }
    }

    protected int getGrowthAmount(Level world) {
        return Mth.nextInt(world.random, 0, 1);
    }

    public boolean canSpread(ServerLevel world, BlockPos pos) {
        if (!world.getGameRules().getBoolean(GameRules.RULE_DO_VINES_SPREAD)){
            return false;
        }
        return getValidLocations(world, pos) > 0;
    }

    public static float getValidLocations(ServerLevel world, BlockPos pos) {
        int validLocations = 0;
        for (int x = -1; x <= 1; ++x) {
            for (int y = -1; y <= 1; ++y) {
                for (int z = -1; z <= 1; ++z){
                    BlockState plantCheckState = world.getBlockState(pos.offset(x, y, z));
                    BlockState dirtCheckState = world.getBlockState(pos.offset(x, y-1, z));
                    if ((plantCheckState.is(Blocks.AIR) || plantCheckState.is(BlockTags.REPLACEABLE)) && dirtCheckState.is(BlockTags.DIRT)) {
                        validLocations += 1;
                    }
                }
            }
        }
        return validLocations;
    }

    public boolean isSpreading(BlockState state) {
        return state.getValue(IS_SPREADING);
    }

    public void applySpread(Level world, BlockPos pos, BlockState state, RandomSource random) {
        for (int x = -1; x <= 1; ++x) {
            for (int y = -1; y <= 1; ++y) {
                for (int z = -1; z <= 1; ++z){
                    BlockState plantCheckState = world.getBlockState(pos.offset(x, y, z));
                    BlockState dirtCheckState = world.getBlockState(pos.offset(x, y-1, z));
                    if ((plantCheckState.is(Blocks.AIR) || plantCheckState.is(BlockTags.REPLACEABLE)) && dirtCheckState.is(BlockTags.DIRT)) {
                        if (random.nextInt(100) <= 0) {
                            world.setBlock(pos.offset(x, y, z), ModBlocks.BLACKBERRY_CROP.get().defaultBlockState(), 2);
                        }
                    }
                }
            }
        }
    }

    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (pEntity instanceof LivingEntity && pEntity.getType() != EntityType.BEE) {
            pEntity.makeStuckInBlock(pState, new Vec3(0.800000011920929, 0.75, 0.800000011920929));
            if (!pLevel.isClientSide && (Integer)pState.getValue(AGE) > 0 && (pEntity.xOld != pEntity.getX() || pEntity.zOld != pEntity.getZ())) {
                double d0 = Math.abs(pEntity.getX() - pEntity.xOld);
                double d1 = Math.abs(pEntity.getZ() - pEntity.zOld);
                if (d0 >= 0.003000000026077032 || d1 >= 0.003000000026077032) {
                    pEntity.hurt(pLevel.damageSources().sweetBerryBush(), 1.0F);
                }
            }
        }

    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.getBlockState(pPos).getValue(HAS_FRUIT)) {
            int j = 1 + pLevel.random.nextInt(3);
            if (pLevel.getBlockState(pPos).getValue(STAGE) == 0) {
                pLevel.playSound(null, pPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + pLevel.random.nextFloat() * 0.4F);
                popResource(pLevel, pPos, new ItemStack(ModItems.BLACKBERRIES.get(), j));
                BlockState targetState = pState.setValue(HAS_FRUIT, false);
                pLevel.setBlock(pPos, targetState, 2);
                pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pPlayer, targetState));
                if (pLevel.getBlockState(pPos.above()).is(ModBlocks.BLACKBERRY_CROP.get())) {
                    BlockState targetState2 = pLevel.getBlockState(pPos.above()).setValue(HAS_FRUIT, false);
                    if (pLevel.getBlockState(pPos.above()).getValue(HAS_FRUIT)) {
                        popResource(pLevel, pPos, new ItemStack(ModItems.BLACKBERRIES.get(), j));
                        pLevel.setBlock(pPos.above(), targetState2, 2);
                    }
                }
                if (pLevel.getBlockState(pPos.above(2)).is(ModBlocks.BLACKBERRY_CROP.get())) {
                    BlockState targetState3 = pLevel.getBlockState(pPos.above(2)).setValue(HAS_FRUIT, false);
                    if (pLevel.getBlockState(pPos.above(2)).getValue(HAS_FRUIT)) {
                        popResource(pLevel, pPos, new ItemStack(ModItems.BLACKBERRIES.get(), j));
                        pLevel.setBlock(pPos.above(2), targetState3, 2);
                    }
                }
            }
            if (pLevel.getBlockState(pPos).getValue(STAGE) == 1) {
                pLevel.playSound(null, pPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + pLevel.random.nextFloat() * 0.4F);
                popResource(pLevel, pPos, new ItemStack(ModItems.BLACKBERRIES.get(), j));
                BlockState targetState = pState.setValue(HAS_FRUIT, false);
                BlockState targetState2 = pLevel.getBlockState(pPos.below()).setValue(HAS_FRUIT, false);
                pLevel.setBlock(pPos, targetState, 2);
                pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos.below(), GameEvent.Context.of(pPlayer, targetState));
                if (pLevel.getBlockState(pPos.below()).is(ModBlocks.BLACKBERRY_CROP.get())) {
                    if (pLevel.getBlockState(pPos.below()).getValue(HAS_FRUIT)) {
                        popResource(pLevel, pPos, new ItemStack(ModItems.BLACKBERRIES.get(), j));
                        pLevel.setBlock(pPos.below(), targetState2, 2);
                    }
                }
                if (pLevel.getBlockState(pPos.above()).is(ModBlocks.BLACKBERRY_CROP.get())) {
                    BlockState targetState3 = pLevel.getBlockState(pPos.above()).setValue(HAS_FRUIT, false);
                    if (pLevel.getBlockState(pPos.above()).getValue(HAS_FRUIT)) {
                        popResource(pLevel, pPos, new ItemStack(ModItems.BLACKBERRIES.get(), j));
                        pLevel.setBlock(pPos.above(), targetState3, 2);
                    }
                }
            }
            if (pLevel.getBlockState(pPos).getValue(STAGE) == 2) {
                pLevel.playSound(null, pPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + pLevel.random.nextFloat() * 0.4F);
                popResource(pLevel, pPos, new ItemStack(ModItems.BLACKBERRIES.get(), j));
                BlockState targetState = pState.setValue(HAS_FRUIT, false);
                BlockState targetState2 = pLevel.getBlockState(pPos.below()).setValue(HAS_FRUIT, false);
                BlockState targetState3 = pLevel.getBlockState(pPos.below(2)).setValue(HAS_FRUIT, false);
                pLevel.setBlock(pPos, targetState, 2);
                pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos.below(), GameEvent.Context.of(pPlayer, targetState));
                if (pLevel.getBlockState(pPos.below()).is(ModBlocks.BLACKBERRY_CROP.get())) {
                    if (pLevel.getBlockState(pPos.below()).getValue(HAS_FRUIT)) {
                        popResource(pLevel, pPos, new ItemStack(ModItems.BLACKBERRIES.get(), j));
                        pLevel.setBlock(pPos.below(), targetState2, 2);
                    }
                }
                if (pLevel.getBlockState(pPos.below(2)).is(ModBlocks.BLACKBERRY_CROP.get())) {
                    if (pLevel.getBlockState(pPos.below(2)).getValue(HAS_FRUIT)) {
                        popResource(pLevel, pPos, new ItemStack(ModItems.BLACKBERRIES.get(), j));
                        pLevel.setBlock(pPos.below(2), targetState3, 2);
                    }
                }
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE, MAX_AGE, STAGE, HAS_FRUIT, IS_SPREADING);
    }

    static {
        AGE = IntegerProperty.create("age", 0, 5);
        STAGE = IntegerProperty.create("stage", 0, 2);
        MAX_AGE = BooleanProperty.create("max_age");
        HAS_FRUIT = BooleanProperty.create("has_fruit");
        IS_SPREADING = BooleanProperty.create("is_spreading");
        AGE_TO_SHAPE = new VoxelShape[]{
                Block.box(4.0, 0.0, 4.0, 12.0, 6.0, 12.0),
                Block.box(3.0, 0.0, 3.0, 13.0, 12.0, 13.0),
                Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0),
                Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0),
                Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0),
                Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0)
        };
    }
}
