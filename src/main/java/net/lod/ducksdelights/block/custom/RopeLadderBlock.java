package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class RopeLadderBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING;
    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty ANCHORED;
    protected static final VoxelShape EAST_ANCHORED;
    protected static final VoxelShape WEST_ANCHORED;
    protected static final VoxelShape SOUTH_ANCHORED;
    protected static final VoxelShape NORTH_ANCHORED;
    protected static final VoxelShape WEAST;
    protected static final VoxelShape NOUTH;


    public RopeLadderBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false).setValue(ANCHORED, false));
    }

    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (pEntity instanceof LivingEntity) {

        }
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pState.getValue(ANCHORED)) {
            switch (pState.getValue(FACING)) {
                case NORTH:
                    return NORTH_ANCHORED;
                case SOUTH:
                    return SOUTH_ANCHORED;
                case WEST:
                    return WEST_ANCHORED;
                case EAST:
                default:
                    return EAST_ANCHORED;
            }
        } else {
            switch (pState.getValue(FACING)) {
                case NORTH, SOUTH:
                    return NOUTH;
                case WEST:
                    return WEAST;
                case EAST:
                default:
                    return WEAST;
            }
        }
    }

    public boolean isBelowRopeLadder(BlockGetter level, BlockPos blockPos) {
        BlockState aboveBlock = level.getBlockState(blockPos.above());
        return aboveBlock.is(ModBlocks.ROPE_LADDER.get());
    }

    public boolean doesMatchAboveRopeLadder(BlockGetter level, BlockPos blockPos) {
        BlockState aboveBlock = level.getBlockState(blockPos.above());
        BlockState thisBlock = level.getBlockState(blockPos);
        return aboveBlock.getValue(FACING) == thisBlock.getValue(FACING);
    }

    private boolean canAttachTo(BlockGetter pBlockReader, BlockPos pPos, Direction pDirection) {
        BlockState attachedBlockState = pBlockReader.getBlockState(pPos);
        return attachedBlockState.isFaceSturdy(pBlockReader, pPos, pDirection);
    }

    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        Direction facing = pState.getValue(FACING);
        if (pState.getValue(ANCHORED)) {
            return this.canAttachTo(pLevel, pPos.relative(facing.getOpposite()), facing);
        } else {
            return false;
        }
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (!pState.canSurvive(pLevel, pCurrentPos) && !this.isBelowRopeLadder(pLevel, pCurrentPos)) {
            if (!pLevel.isClientSide()) {
                pLevel.scheduleTick(pCurrentPos, this, 1);
            }
            return pState;
        } else if (!pState.canSurvive(pLevel, pCurrentPos) && this.isBelowRopeLadder(pLevel, pCurrentPos)) {
            pLevel.scheduleTick(pCurrentPos, this, 1);
            return pState.setValue(ANCHORED, false);
        } else {
            if (pState.getValue(WATERLOGGED)) {
                pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
            }

            return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
        }
    }

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pState.canSurvive(pLevel, pPos) && !this.isBelowRopeLadder(pLevel, pPos)) {
            pLevel.destroyBlock(pPos, true);
        } else if (!pState.canSurvive(pLevel, pPos) && this.isBelowRopeLadder(pLevel, pPos)) {
            pLevel.setBlock(pPos, pLevel.getBlockState(pPos).setValue(FACING, pLevel.getBlockState(pPos.above()).getValue(FACING)), 2);
        }
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack heldItem = pPlayer.getItemInHand(pHand);
        boolean flag = false;
        if (heldItem.is(ModBlocks.ROPE_LADDER.get().asItem())) {
            BlockPos.MutableBlockPos below = pPos.mutable().move(Direction.DOWN);
            while (isBelowRopeLadder(pLevel, below)) {
                if (pLevel.getBlockState(below).is(Blocks.AIR)) {
                    pLevel.setBlock(below, pLevel.getBlockState(below.above()).setValue(ANCHORED, false), 2);
                    pLevel.playSound(null, below, SoundEvents.BAMBOO_WOOD_PLACE, SoundSource.BLOCKS);
                    if (!pPlayer.getAbilities().instabuild) {
                        heldItem.shrink(1);
                    }
                    flag = true;
                    break;
                } else if (pLevel.getBlockState(below).is(Blocks.WATER)) {
                    pLevel.setBlock(below, pLevel.getBlockState(below.above()).setValue(ANCHORED, false).setValue(WATERLOGGED, true), 2);
                    pLevel.playSound(null, below, SoundEvents.BAMBOO_WOOD_PLACE, SoundSource.BLOCKS);
                    if (!pPlayer.getAbilities().instabuild) {
                        heldItem.shrink(1);
                    }
                    flag = true;
                    break;
                }
                below.move(Direction.DOWN);
            }
        } else if (heldItem.isEmpty()) {
            BlockPos.MutableBlockPos below = pPos.mutable().move(Direction.DOWN);
            while (isBelowRopeLadder(pLevel, below)) {
                if (!pLevel.getBlockState(below).is(ModBlocks.ROPE_LADDER.get())) {
                    if (pLevel.getBlockState(below.above()).is(ModBlocks.ROPE_LADDER.get())) {
                        if (pLevel.getBlockState(below.above()).getValue(WATERLOGGED)) {
                            pLevel.setBlock(below.above(), Blocks.WATER.defaultBlockState(), 2);
                        } else {
                            pLevel.setBlock(below.above(), Blocks.AIR.defaultBlockState(), 2);
                        }
                    }
                    pLevel.playSound(null, below, SoundEvents.BAMBOO_WOOD_BREAK, SoundSource.BLOCKS);
                    if (!pPlayer.getAbilities().instabuild) {
                        pPlayer.addItem(new ItemStack(ModBlocks.ROPE_LADDER.get().asItem(), 1));
                    }
                    flag = true;
                    break;
                }
                below.move(Direction.DOWN);
            }
        }

        if (flag) {
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState newLadder;
        if (!pContext.replacingClickedOnBlock()) {
            newLadder = pContext.getLevel().getBlockState(pContext.getClickedPos().relative(pContext.getClickedFace().getOpposite()));
            if (newLadder.is(this) && newLadder.getValue(FACING) == pContext.getClickedFace()) {
                return null;
            }
        }

        newLadder = this.defaultBlockState();
        LevelReader level = pContext.getLevel();
        BlockPos blockPos = pContext.getClickedPos();
        FluidState fluidState = pContext.getLevel().getFluidState(pContext.getClickedPos());
        Direction[] direction = pContext.getNearestLookingDirections();
        int length = direction.length;

        for(int checkLength = 0; checkLength < length; ++checkLength) {
            Direction findPos = direction[checkLength];
            if (findPos.getAxis().isHorizontal()) {
                newLadder = newLadder.setValue(FACING, findPos.getOpposite()).setValue(ANCHORED, true);
                if (newLadder.canSurvive(level, blockPos) || this.isBelowRopeLadder(level, blockPos)) {
                    return newLadder.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
                }
            }
        }

        return null;
    }

    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, WATERLOGGED, ANCHORED);
    }

    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        ANCHORED = BooleanProperty.create("anchored");
        WEAST = Block.box(7.0, 0.0, 0.0, 9.0, 16.0, 16.0);
        NOUTH = Block.box(0.0, 0.0, 7.0, 16.0, 16.0, 9.0);
        VoxelShape spool_north = Block.box(0.0, 8.0, 9.0, 16.0, 16.0, 16.0);
        VoxelShape spool_south = Block.box(0.0, 8.0, 0.0, 16.0, 16.0, 7.0);
        VoxelShape spool_east = Block.box(0.0, 8.0, 0.0, 7.0, 16.0, 16.0);
        VoxelShape spool_west = Block.box(9.0, 8.0, 0.0, 16.0, 16.0, 16.0);
        NORTH_ANCHORED = Shapes.or(NOUTH, new VoxelShape[]{spool_north});
        SOUTH_ANCHORED = Shapes.or(NOUTH, new VoxelShape[]{spool_south});
        EAST_ANCHORED = Shapes.or(WEAST, new VoxelShape[]{spool_east});
        WEST_ANCHORED = Shapes.or(WEAST, new VoxelShape[]{spool_west});
    }
}
