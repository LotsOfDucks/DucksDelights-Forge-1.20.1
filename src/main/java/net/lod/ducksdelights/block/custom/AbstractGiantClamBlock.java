package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.block.custom.blockstate_properties.ModBlockStateProperties;
import net.lod.ducksdelights.block.custom.interfaces.ISimpleWaterAndLavaloggedBlock;
import net.lod.ducksdelights.block.entity.AbstractGiantClamBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractGiantClamBlock extends BaseEntityBlock implements ISimpleWaterAndLavaloggedBlock {
    public static final BooleanProperty OPEN;
    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty LAVALOGGED;
    public static final BooleanProperty LOGGED;
    protected static final VoxelShape OPEN_SHAPE;
    protected static final VoxelShape NOUTH_SHAPE;
    protected static final VoxelShape WEAST_SHAPE;
    public static final EnumProperty<Direction> FACING;



    public AbstractGiantClamBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, true).setValue(WATERLOGGED, false).setValue(LAVALOGGED, false).setValue(LOGGED, false).setValue(FACING, Direction.NORTH));
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pState.getValue(OPEN)) {
            return OPEN_SHAPE;
        } else {
            if ((pState.getValue(FACING) == Direction.NORTH) || (pState.getValue(FACING) == Direction.SOUTH)) {
                return NOUTH_SHAPE;
            } else {
                return WEAST_SHAPE;
            }
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.getBlockEntity(pPos) instanceof AbstractGiantClamBlockEntity) {
            AbstractGiantClamBlockEntity blockEntity = (AbstractGiantClamBlockEntity) pLevel.getBlockEntity(pPos);
            if (pPlayer.isCrouching()) {
                if (pLevel.isClientSide()) {
                    this.emitParticle(pLevel, pPos, pState);
                    return InteractionResult.SUCCESS;
                } else {
                    this.toggleOpen(pLevel, pPos, pState);
                    return InteractionResult.CONSUME;
                }
            } else {
                if (pHand == InteractionHand.MAIN_HAND) {
                    boolean hasInputItem = !blockEntity.getItem(0).isEmpty();
                    boolean hasOutputItem = !blockEntity.getItem(1).isEmpty();
                    ItemStack heldItem = pPlayer.getItemInHand(pHand);
                    if (isOpen(pState)) {
                        if (hasInputItem || hasOutputItem) {
                            if (heldItem.isEmpty()) {
                                if (hasInputItem) {
                                    if (pLevel.isClientSide()) {
                                        return InteractionResult.SUCCESS;
                                    } else {
                                        pPlayer.addItem(blockEntity.getItem(0).copy()) ;
                                        blockEntity.removeItem(0, 1);
                                        pLevel.updateNeighbourForOutputSignal(pPos, this);
                                        return InteractionResult.CONSUME;
                                    }
                                } else {
                                    if (pLevel.isClientSide()) {
                                        return InteractionResult.SUCCESS;
                                    } else {
                                        pPlayer.addItem(blockEntity.getItem(1).copy());
                                        blockEntity.removeItem(1, 1);
                                        pLevel.updateNeighbourForOutputSignal(pPos, this);
                                        return InteractionResult.CONSUME;
                                    }
                                }
                            }
                        } else {
                            if (!heldItem.isEmpty()) {
                                if (pLevel.isClientSide()) {
                                    return InteractionResult.SUCCESS;
                                } else {
                                    blockEntity.setItem(0, heldItem.copyWithCount(1));
                                    heldItem.shrink(1);
                                    return InteractionResult.CONSUME;
                                }
                            }
                        }
                    }
                }
            }
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    public void onProjectileHit(Level pLevel, BlockState pState, BlockHitResult pHit, Projectile pProjectile) {
        if (this.isOpen(pState)) {
            this.toggleOpen(pLevel, pHit.getBlockPos(), pState);
            pLevel.scheduleTick(pHit.getBlockPos(), pLevel.getBlockState(pHit.getBlockPos()).getBlock(), 50);
        }
    }

    public boolean isRandomlyTicking(BlockState pState) {
        return this.isOpen(pState) && this.isLogged(pState);
    }

    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.random.nextInt(20) == 0) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof AbstractGiantClamBlockEntity) {
                AbstractGiantClamBlockEntity giantClamBlockEntity = (AbstractGiantClamBlockEntity) blockEntity;
                BlockState blockState = pLevel.getBlockState(pPos.below());
                if (blockState.is(BlockTags.SAND)) {
                    if (giantClamBlockEntity.isEmpty()) {
                        ItemStack sandItem = new ItemStack(blockState.getBlock());
                        if (giantClamBlockEntity.getPearlableRecipe(sandItem).isPresent()) {
                            ((AbstractGiantClamBlockEntity) blockEntity).setItem(0, sandItem);
                        }
                    }
                }
            }
        } else {
            //conversion is a temporary solution
            if (pLevel.getBlockState(pPos.below()).is(BlockTags.SOUL_SPEED_BLOCKS)) {
                BlockState newBlockstate = ModBlocks.GIANT_CLAM_NETHER.get().defaultBlockState().setValue(AbstractGiantClamBlock.FACING, pState.getValue(FACING)).setValue(AbstractGiantClamBlock.OPEN, pState.getValue(OPEN)).setValue(AbstractGiantClamBlock.WATERLOGGED, false).setValue(AbstractGiantClamBlock.LOGGED, false).setValue(AbstractGiantClamBlock.LAVALOGGED, false);
                pLevel.setBlockAndUpdate(pPos, newBlockstate);
            }
        }
    }

    public boolean isOpen(BlockState state) {
        return state.getValue(ModBlockStateProperties.OPEN);
    }

    public boolean isLogged(BlockState state) {
        return state.getValue(ModBlockStateProperties.LOGGED);
    }

    public void toggleOpen(Level level, BlockPos pos, BlockState state) {
        level.setBlockAndUpdate(pos, state.cycle(ModBlockStateProperties.OPEN));
        level.playSound(null, pos, SoundEvents.BONE_BLOCK_STEP, SoundSource.BLOCKS, 1, 0.75F);
    }

    public void emitParticle(Level level, BlockPos pos, BlockState state) {
        if (!state.getValue(OPEN) && state.getValue(WATERLOGGED)) {
            for (int bubl = 1; bubl <= 16; bubl++) {
                double x = pos.getCenter().x();
                double y = pos.getCenter().y() + 0.1;
                double z = pos.getCenter().z();
                if (level.random.nextIntBetweenInclusive(1, 2) == 1) {
                    level.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, x + (0.5 * (Math.random() - Math.random())), y + 0.02, z + (0.5 * (Math.random() - Math.random())), 0, 0.02, 0);
                }
            }
        }
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }
        if (pState.getValue(LAVALOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.LAVA, Fluids.LAVA.getTickDelay(pLevel));
        }

        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isClientSide()){
            this.toggleOpen(pLevel, pPos, pState);
        } else {
            this.emitParticle(pLevel, pPos, pState);
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof AbstractGiantClamBlockEntity) {
                ((AbstractGiantClamBlockEntity) blockEntity).drops();
            }
            pLevel.updateNeighbourForOutputSignal(pPos, this);
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState pBlockState, Level pLevel, BlockPos pPos) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof AbstractGiantClamBlockEntity abstractGiantClamBlockEntity) {
            if (!abstractGiantClamBlockEntity.getItem(0).isEmpty()) {
                return 1;
            } else if (!abstractGiantClamBlockEntity.getItem(1).isEmpty()) {
                return 3;
            }
        }
        return 0;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        FluidState fluidState = pContext.getLevel().getFluidState(pContext.getClickedPos());
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection()).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER).setValue(LAVALOGGED, fluidState.getType() == Fluids.LAVA).setValue(LOGGED, (fluidState.getType() == (Fluids.LAVA) || fluidState.getType() == (Fluids.WATER))).setValue(OPEN, true);
    }

    public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
        return Shapes.empty();
    }

    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1.0F;
    }

    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return true;
    }

    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : pState.getValue(LAVALOGGED) ? Fluids.LAVA.getSource(false) : super.getFluidState(pState);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(OPEN, WATERLOGGED, LAVALOGGED, LOGGED, FACING);
    }

    static {
        OPEN = ModBlockStateProperties.OPEN;
        FACING = BlockStateProperties.HORIZONTAL_FACING;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        LAVALOGGED = ModBlockStateProperties.LAVALOGGED;
        LOGGED = ModBlockStateProperties.LOGGED;
        WEAST_SHAPE = Block.box(3.0, 0.0, 0.0, 13.0, 11.0, 16.0);
        NOUTH_SHAPE = Block.box(0.0, 0.0, 3.0, 16.0, 11.0, 13.0);
        OPEN_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    }
}
