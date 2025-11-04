package net.lod.ducksdelights.damage;

import net.lod.ducksdelights.DucksDelights;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

import java.util.Objects;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> FISSION = ResourceKey.create(Registries.DAMAGE_TYPE, Objects.requireNonNull(ResourceLocation.tryBuild(DucksDelights.MOD_ID, "fission")));
    public static final ResourceKey<DamageType> GUNPOWDER_BARREL = ResourceKey.create(Registries.DAMAGE_TYPE, Objects.requireNonNull(ResourceLocation.tryBuild(DucksDelights.MOD_ID, "gunpowder_barrel")));
}