package net.lod.ducksdelights.block.entity;

import net.lod.ducksdelights.block.ModBlockEntities;
import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.entity.mobeffects.ModMobEffects;
import net.lod.ducksdelights.recipe.FrigidHeartFreezingRecipe;
import net.lod.ducksdelights.recipe.ModRecipes;
import net.lod.ducksdelights.util.RecipeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.capability.ItemFluidContainer;

import java.util.List;
import java.util.Optional;

public class FrigidHeartBlockEntity extends BlockEntity {
    private final RecipeManager.CachedCheck<Container, FrigidHeartFreezingRecipe> quickCheck;
    public int range = 5 + 1;

    public FrigidHeartBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FRIGID_HEART_BE.get(), pPos, pBlockState);
        this.quickCheck = RecipeManager.createCheck(ModRecipes.FRIGID_HEART_FREEZING_RECIPE_TYPE.get());
    }

    public static void operationTick(Level pLevel, BlockPos pPos, BlockState pState, FrigidHeartBlockEntity pBlockEntity) {
        if (pLevel.getBlockState(pPos).is(ModBlocks.FRIGID_HEART.get())) {
            freezeEntitiesInRange(pLevel, pPos, pBlockEntity.range);
            //if (pLevel.getGameTime() % 5L == 0L) {
                freezeBlockInRange(pLevel, pPos, pBlockEntity, pBlockEntity.range);
            //}
        }
    }

    public static void particleTick(Level pLevel, BlockPos pPos, BlockState pState, FrigidHeartBlockEntity pBlockEntity) {
        RandomSource random = pLevel.random;
        int particleRange = pBlockEntity.range * 16;
        //for (int roll = 1; roll <= 5; roll++) {
            Vec3 targetPos = pPos.getCenter().add(Vec3.directionFromRotation(random.nextInt(0, 360), random.nextInt(0, 360)).scale(random.nextIntBetweenInclusive( 2, particleRange / 16)));
            pLevel.addParticle(ParticleTypes.SNOWFLAKE, false, targetPos.x(), targetPos.y(), targetPos.z(), 0, -0.025, 0);
        //}
    }

    public static void freezeEntitiesInRange(Level level, BlockPos pos, Integer range) {
        if (level.getGameTime() % 5L == 0L) {
            int boxX = pos.getX();
            int boxY = pos.getY();
            int boxZ = pos.getZ();
            AABB box = (new AABB(boxX, boxY, boxZ, boxX + 1, boxY + 1, boxZ + 1)).inflate(range + 3);
            List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, box, LivingEntity::attackable);
            if (!list.isEmpty()) {
                for (LivingEntity livingEntity : list) {
                    if (pos.getCenter().closerThan(livingEntity.position(), range) && livingEntity.isAlive()) {
                        if (livingEntity instanceof Player && livingEntity.tickCount <= 200) {
                            return;
                        } else if (livingEntity.canFreeze()) {
                            livingEntity.addEffect(new MobEffectInstance(ModMobEffects.FREEZING.get(), 20));
                        }
                    }
                }
            }
        }
    }

    public static void freezeBlockInRange(Level level, BlockPos pos, FrigidHeartBlockEntity blockEntity, Integer range) {
        RandomSource random = level.random;
        int particleRange = range * 16;
        Vec3 targetVec3 = new Vec3(pos.getX(), pos.getY(), pos.getZ()).add(Vec3.directionFromRotation(random.nextInt(0, 360), random.nextInt(0, 360)).scale(random.nextIntBetweenInclusive( 1, particleRange / 16)));
        BlockPos targetPos = new BlockPos( new Vec3i((int) Math.round(targetVec3.x()), (int) Math.round(targetVec3.y()), (int) Math.round(targetVec3.z())));
        ItemStack targetAsItem = new ItemStack(level.getBlockState(targetPos).getBlock().asItem());


        if (level.getBlockState(targetPos).is(BlockTags.REPLACEABLE) && !level.getFluidState(targetPos).isEmpty()) {
            targetAsItem = new ItemStack (level.getFluidState(targetPos).getType().getBucket());
        }


        Optional<FrigidHeartFreezingRecipe> optional = blockEntity.getFreezingRecipe(targetAsItem);
        if (optional.isPresent()) {
            FrigidHeartFreezingRecipe freezingRecipe = blockEntity.getFreezingRecipe(targetAsItem).get();
            SimpleContainer dummyContainer = new SimpleContainer(targetAsItem);
            ItemStack freezeStack = blockEntity.quickCheck.getRecipeFor(dummyContainer, level).map((recipe) -> recipe.assemble(dummyContainer, level.registryAccess())).orElse(targetAsItem);
            level.setBlockAndUpdate(targetPos, ((BlockItem) freezeStack.getItem()).getBlock().defaultBlockState());

            if (freezingRecipe.getShouldFreezeNeighbors()) {
                for (Direction checkDirection : Direction.values()) {
                    freezeNeighbor(level, targetPos.relative(checkDirection), blockEntity);
                }
            }
        }
    }

    public static void freezeNeighbor(Level level, BlockPos targetPos, FrigidHeartBlockEntity blockEntity) {
        ItemStack targetAsItem = new ItemStack(level.getBlockState(targetPos).getBlock().asItem());


        if (level.getBlockState(targetPos).is(BlockTags.REPLACEABLE) && !level.getFluidState(targetPos).isEmpty()) {
            targetAsItem = new ItemStack (level.getFluidState(targetPos).getType().getBucket());
        }


        Optional<FrigidHeartFreezingRecipe> optional = blockEntity.getFreezingRecipe(targetAsItem);
        if (optional.isPresent()) {
            SimpleContainer dummyContainer = new SimpleContainer(targetAsItem);
            ItemStack freezeStack = blockEntity.quickCheck.getRecipeFor(dummyContainer, level).map((recipe) -> recipe.assemble(dummyContainer, level.registryAccess())).orElse(targetAsItem);
            level.setBlockAndUpdate(targetPos, ((BlockItem) freezeStack.getItem()).getBlock().defaultBlockState());
        }
    }

    public Optional<FrigidHeartFreezingRecipe> getFreezingRecipe(ItemStack pStack) {
        return this.quickCheck.getRecipeFor(new SimpleContainer(pStack), this.level);
    }
}
