package net.lod.ducksdelights.entity.projectile;

import net.lod.ducksdelights.entity.ModEntities;
import net.lod.ducksdelights.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nullable;

public class ThrownSoulpearl extends ThrowableItemProjectile {
    public ThrownSoulpearl(EntityType<? extends ThrownSoulpearl> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ThrownSoulpearl(Level pLevel, LivingEntity pShooter) {
        super(ModEntities.THROWN_SOULPEARL.get(), pShooter, pLevel);
    }

    protected Item getDefaultItem() {
        return ModItems.SOUL_PEARL.get();
    }

    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        //this.getOwner().sendSystemMessage(Component.literal("Stop that."));
    }

    protected void onHit(HitResult pResult) {
        super.onHit(pResult);

        //this.getOwner().sendSystemMessage(Component.literal("Stop that."));
        this.discard();
    }

    public void tick() {
        Entity entity = this.getOwner();
        if (entity instanceof Player && !entity.isAlive()) {
            this.discard();
        } else {
            super.tick();
        }

    }

    @Nullable
    public Entity changeDimension(ServerLevel p_37506_, ITeleporter teleporter) {
        Entity entity = this.getOwner();
        if (entity != null && entity.level().dimension() != p_37506_.dimension()) {
            this.setOwner(null);
        }

        return super.changeDimension(p_37506_, teleporter);
    }
}
