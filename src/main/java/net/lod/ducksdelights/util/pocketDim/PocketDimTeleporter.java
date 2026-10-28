package net.lod.ducksdelights.util.pocketDim;

import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nullable;
import java.util.function.Function;

public class PocketDimTeleporter implements ITeleporter {

    private final ServerLevel level;
    private final int coord;

    public PocketDimTeleporter(ServerLevel level, int coord) {
        this.level = level;
        this.coord = coord;
    }

    @Override
    public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
        return false;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        return ITeleporter.super.placeEntity(entity, currentWorld, destWorld, yaw, repositionEntity);
    }

    @Override
    @SuppressWarnings("removal")
    public @Nullable PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        Vec3 dimVec;
        if (destWorld.dimension().location().equals(new ResourceLocation(DucksDelights.MOD_ID, "pocket"))) {
            //position entity and facing
            dimVec = new Vec3((coord * 32) + 9 - 1, 42, 2 - 0.5);
            BlockPos dimSpawnPos = new BlockPos((int) dimVec.x(), (int) dimVec.y() - 1, (int) dimVec.z());
            if (destWorld.getBlockState(dimSpawnPos).getBlock() == Blocks.AIR) {
                this.placePortalStructure();
            }
            dimVec = dimVec.add(0, 0, 6.5);
        } else if (entity.getPersistentData().contains("ducks_pocketDimOldDim")) {
            double xPos = entity.getPersistentData().getDouble("ducks_pocketDimOldX");
            double yPos = entity.getPersistentData().getDouble("ducks_pocketDimOldY");
            double zPos = entity.getPersistentData().getDouble("ducks_pocketDimOldZ");
            dimVec = new Vec3(xPos, yPos, zPos);
        } else {
            dimVec = destWorld.getSharedSpawnPos().getCenter();
        }

        return new PortalInfo(dimVec, Vec3.ZERO,  entity.getYRot(), entity.getXRot());
    }

    public void placePortalStructure() {
        final int minX = Math.min(coord * 32, coord * 32 + 15);
        final int minY = 40;
        final int minZ = 0;

        final int maxX = Math.max(coord * 32, coord * 32 + 15);
        final int maxY = 53;
        final int maxZ = 15;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (x == minX || y == minY || z == minZ || x == maxX || y == maxY || z == maxZ) {
                        if (this.level.getBlockState(new BlockPos(x, y, z)) != ModBlocks.HAUNTED_ENDER_BLOCK.get().defaultBlockState()) {
                            this.level.setBlockAndUpdate(new BlockPos(x, y, z), ModBlocks.HAUNTED_ENDER_BLOCK.get().defaultBlockState());
                        }
                    }
                }
            }
        }
    }
}
