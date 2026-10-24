package net.lod.ducksdelights.util.pocketDim;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.HashMap;

public class PocketDimSaveTransportHandler extends SavedData {
    int nextTargetCoordinate;
    HashMap<String, Integer> playerMap;

    public PocketDimSaveTransportHandler() {
        nextTargetCoordinate = 0;
        playerMap = new HashMap<>();
        this.setDirty();
    }

    //netherDataStorage.computeIfAbsent(this::load, this::create, "example");


    public static PocketDimSaveTransportHandler create() {
        return new PocketDimSaveTransportHandler();
    }

    public static PocketDimSaveTransportHandler load(CompoundTag compoundTag) {
        PocketDimSaveTransportHandler data = PocketDimSaveTransportHandler.create();
        data.nextTargetCoordinate = compoundTag.getInt("ducks_pocketDimNextCoord");
        ListTag tagList = compoundTag.getList("ducks_pocketDimPlayerList", 10);

        for (int i = 0; i < tagList.size(); ++i) {
            CompoundTag compound = tagList.getCompound(i);
            String playerName = compound.getString("ducks_pocketDimPlayerName");
            int position = compound.getInt("ducks_pocketDimPlayerPos");
            data.playerMap.put(playerName, position);
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putInt("ducks_pocketDimNextCoord", this.nextTargetCoordinate);
        ListTag tagList = new ListTag();

        for (String playerName : this.playerMap.keySet()) {
            CompoundTag compound = new CompoundTag();
            compound.putString("ducks_pocketDimPlayerName", playerName);
            int position = this.playerMap.get(playerName);
            compound.putInt("ducks_pocketDimPlayerPos", position);
            tagList.add(compound);
        }
        compoundTag.put("ducks_pocketDimPlayerList", tagList);
        return compoundTag;
    }

    public static PocketDimSaveTransportHandler checkData(ServerLevel serverLevel) {
        DimensionDataStorage storage = serverLevel.getServer().getLevel(Level.OVERWORLD).getDataStorage();
        return storage.computeIfAbsent(PocketDimSaveTransportHandler::load, PocketDimSaveTransportHandler::create, "ducks_pocketDimData");
    }

    @SuppressWarnings("removal")
    public void teleportInPocketDim(ServerLevel serverLevel, Entity entity) {
        String playerName = entity.getDisplayName().getString();
        int targetCoordinate;
        if (this.playerMap.containsKey(playerName)) {
            targetCoordinate = this.playerMap.get(playerName);
        } else {
            targetCoordinate = this.nextTargetCoordinate;
            this.nextTargetCoordinate++;
            this.playerMap.put(playerName, targetCoordinate);
            this.setDirty();
        }

        CompoundTag nbt = entity.getPersistentData();
        nbt.putString("ducks_pocketDimOldDim", entity.level().dimension().location().toString());
        nbt.putDouble("ducks_pocketDimOldX", entity.position().x());
        nbt.putDouble("ducks_pocketDimOldY", entity.position().y());
        nbt.putDouble("ducks_pocketDimOldZ", entity.position().z());

        entity.changeDimension(serverLevel, new PocketDimTeleporter(serverLevel, targetCoordinate));
    }

    @SuppressWarnings("removal")
    public void teleportOutPocketDim(ServerLevel serverLevel, Entity entity) {
        entity.changeDimension(serverLevel, new PocketDimTeleporter(serverLevel, 0));
        serverLevel.getProfiler().pop();
    }
}
