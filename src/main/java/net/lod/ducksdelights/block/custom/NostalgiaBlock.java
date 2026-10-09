package net.lod.ducksdelights.block.custom;

import net.lod.ducksdelights.block.custom.blockstate_properties.ModBlockStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import javax.annotation.Nullable;

public class NostalgiaBlock extends Block {
    public static final IntegerProperty DIMENSION;

    public NostalgiaBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(DIMENSION, 3));
    }

    public int getDimensionState(Level level) {
        return switch (level.dimension().location().toString()) {
            case "minecraft:overworld" -> 0;
            case "minecraft:the_nether" -> 1;
            case "minecraft:the_end" -> 2;
            default -> 3;
        };
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(DIMENSION, getDimensionState(pContext.getLevel()));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(DIMENSION);
    }

    static {
        DIMENSION = ModBlockStateProperties.DIMENSION;
    }
}
