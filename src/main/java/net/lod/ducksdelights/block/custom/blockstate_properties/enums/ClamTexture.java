package net.lod.ducksdelights.block.custom.blockstate_properties.enums;

import net.minecraft.util.StringRepresentable;

public enum ClamTexture implements StringRepresentable {
    WHITE("white"),
    BROWN("brown"),
    BLUE("blue"),
    GREEN("green"),
    ENDER("ender")
    ;

    private final String name;

    ClamTexture(String pName) {
        this.name = pName;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
