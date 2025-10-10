package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.entity.ModBlockEntities;
import net.lod.ducksdelights.block.entity.MoonPhaseDetectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DaylightDetectorBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DaylightDetectorBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class MoonPhaseDetectorBlock extends BaseEntityBlock {
    public static final IntegerProperty POWER;
    public static final IntegerProperty PHASE;
    public static final BooleanProperty SPECIFIC;
    protected static final VoxelShape SHAPE;

    public MoonPhaseDetectorBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWER, 0).setValue(PHASE, 0).setValue(SPECIFIC, true));
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public boolean useShapeForLightOcclusion(BlockState pState) {
        return true;
    }

    public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        return pBlockState.getValue(POWER);
    }

    private static void updateSignalStrength(BlockState pState, Level pLevel, BlockPos pPos) {
        int moonSize = (int) Math.floor(pLevel.getMoonPhase() * 15) + 1;
        int moonPhase = pLevel.getMoonPhase();
        boolean emitPower = true;
        float skyAngle = pLevel.getSunAngle(1.0F);
        if (moonSize > 0) {
            if (skyAngle < 1.5707963F || skyAngle > 4.7123890F) {
                emitPower = false;
            }
        }
        moonSize = Math.min(15, Math.max(moonSize, 0));
        if (pState.getValue(PHASE) != moonPhase) {
            pLevel.setBlock(pPos, pState.setValue(PHASE, moonPhase), 3);
        }
        int moonPhasePower = switch (moonPhase) {
            case 0 -> 6;
            case 1 -> 7;
            case 2 -> 8;
            case 3 -> 9;
            case 4 -> 1;
            case 5 -> 2;
            case 6 -> 3;
            case 7 -> 4;
            case 8 -> 5;
            default -> 0;
        };
        if (emitPower) {
            if (!pState.getValue(SPECIFIC) && pState.getValue(POWER) != moonSize) {
                pLevel.setBlock(pPos, pState.setValue(POWER, moonSize), 3);
            } else if (pState.getValue(SPECIFIC) && pState.getValue(POWER) != moonPhasePower) {
                pLevel.setBlock(pPos, pState.setValue(POWER, moonPhasePower), 3);
            }
        } else {
            pLevel.setBlock(pPos, pState.setValue(POWER, 0), 3);
        }
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.mayBuild()) {
            if (pLevel.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                BlockState $$6 = pState.cycle(SPECIFIC);
                pLevel.setBlock(pPos, $$6, 4);
                pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pPlayer, $$6));
                updateSignalStrength($$6, pLevel, pPos);
                return InteractionResult.CONSUME;
            }
        } else {
            return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public boolean isSignalSource(BlockState pState) {
        return true;
    }

    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MoonPhaseDetectorBlockEntity(pPos, pState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide && pLevel.dimensionType().hasSkyLight() ? createTickerHelper(pBlockEntityType, ModBlockEntities.MOON_PHASE_DETECTOR_BE.get(), MoonPhaseDetectorBlock::tickEntity) : null;
    }

    private static void tickEntity(Level world, BlockPos blockPos, BlockState blockState, MoonPhaseDetectorBlockEntity blockEntity) {
        if (world.getGameTime() % 20L == 0L) {
            updateSignalStrength(blockState, world, blockPos);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWER, PHASE, SPECIFIC);
    }

    static {
        POWER = BlockStateProperties.POWER;
        PHASE = IntegerProperty.create("phase", 0, 7);
        SPECIFIC = BooleanProperty.create("specific");
        SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0);
    }

}
