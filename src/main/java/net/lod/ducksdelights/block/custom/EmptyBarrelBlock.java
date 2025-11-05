package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class EmptyBarrelBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED;
    private static final VoxelShape SHAPE;

    public EmptyBarrelBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (!itemstack.isEmpty()) {
            if (itemstack.is(Items.GUNPOWDER)) {
                pLevel.setBlock(pPos, ModBlocks.GUNPOWDER_BARREL.get().defaultBlockState().setValue(FillableBarrelBlock.FULLNESS, 0), 3);
                pLevel.playSound(null, pPos, ModSoundEvents.BARREL_FILL.get(), SoundSource.BLOCKS, 5, 1);
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            }
            if (itemstack.is(Items.GLOWSTONE_DUST)) {
                pLevel.setBlock(pPos, ModBlocks.GLOWSTONE_BARREL.get().defaultBlockState().setValue(FillableBarrelBlock.FULLNESS, 0), 3);
                pLevel.playSound(null, pPos, ModSoundEvents.BARREL_FILL.get(), SoundSource.BLOCKS, 5, 1);
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            }
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        FluidState fluidState = pContext.getLevel().getFluidState(pContext.getClickedPos());
        return this.defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(WATERLOGGED);
    }

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        VoxelShape base = Block.box(1.0, 0.0, 1.0, 15.0, 0.25, 15.0);
        VoxelShape wall_north = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
        VoxelShape wall_south = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
        VoxelShape wall_east = Block.box(15.0, 0.0, 1.0, 16.0, 16.0, 15.0);
        VoxelShape wall_west = Block.box(0.0, 0.0, 1.0, 1.0, 16.0, 15.0);
        SHAPE = Shapes.or(base,wall_north,wall_south, wall_east, wall_west);
    }
}
