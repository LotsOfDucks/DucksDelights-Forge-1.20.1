package net.lod.ducksdelights.block;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ModBlockStateProperties {
    public static final BooleanProperty LAVALOGGED = BooleanProperty.create("lavalogged");
    public static final BooleanProperty LOGGED = BooleanProperty.create("logged");

    public static final BooleanProperty BREAKING = BooleanProperty.create("breaking");

    public static final BooleanProperty ANCHORED = BooleanProperty.create("anchored");

    public static final IntegerProperty AGE_6 = IntegerProperty.create("age", 0, 5);
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 2);
    public static final BooleanProperty HAS_FRUIT = BooleanProperty.create("has_fruit");
    public static final BooleanProperty IS_SPREADING = BooleanProperty.create("is_spreading");

    public static final IntegerProperty FULLNESS = IntegerProperty.create("fullness", 1, 15);
    public static final BooleanProperty EXPLODING = BooleanProperty.create("exploding");

    public static final BooleanProperty FORCE_POWERED = BooleanProperty.create("force_powered");
    public static final BooleanProperty PLAYER_PLACED = BooleanProperty.create("player_placed");

    public static final IntegerProperty PHASE = IntegerProperty.create("phase", 0, 7);
    public static final BooleanProperty SPECIFIC = BooleanProperty.create("specific");

    public static final IntegerProperty AGE_8 = IntegerProperty.create("age", 0, 7);
    public static final BooleanProperty GOLDEN = BooleanProperty.create("golden");

    public static final IntegerProperty TUNE = IntegerProperty.create("tune", 1, 15);


    public ModBlockStateProperties() {
    }


}


