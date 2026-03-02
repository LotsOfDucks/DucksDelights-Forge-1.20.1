package net.lod.ducksdelights.block.entity.spawners;

import com.mojang.logging.LogUtils;
import net.lod.ducksdelights.block.entity.BlightedSpawnerBlockEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public class BlightedSpawner  extends BaseSpawner{
    private static final String SPAWN_DATA_TAG = "SpawnData";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int EVENT_SPAWN = 1;
    public int entityHealth = 20;
    public int spawnDelay = 20;
    public SimpleWeightedRandomList<SpawnData> spawnPotentials = SimpleWeightedRandomList.empty();
    @Nullable
    public SpawnData nextSpawnData;
    private double spin;
    private double oSpin;
    private int minSpawnDelay = 200;
    private int maxSpawnDelay = 400;
    private int spawnCount;
    @Nullable
    public LivingEntity displayEntity;
    private int maxNearbyEntities;
    private int requiredPlayerRange;
    private int spawnRange;

    public BlightedSpawner(int spawnCount, int maxNearbyEntities, int requiredPlayerRange, int spawnRange) {
        this.spawnCount = spawnCount;
        this.maxNearbyEntities = maxNearbyEntities;
        this.requiredPlayerRange = requiredPlayerRange;
        this.spawnRange = spawnRange;
    }

    //fuck learning spawner code with no tutorials. I reverse engineered this and I never want to do it again.


    public void setEntityHealth(CompoundTag pTag) {
        if (pTag.contains("EntityMaxHealth", 99)) {
            this.entityHealth = pTag.getShort("EntityMaxHealth");
            this.updateDelays(pTag.getShort("EntityMaxHealth"));
        }
    }

    public void updateDelays(int entityHealth) {
        int newSpawnDelay = 20;
        if (entityHealth > 0) {
            if (entityHealth < 20) {
                newSpawnDelay = entityHealth + ((20 - entityHealth) / 2);
            } else {
                newSpawnDelay = entityHealth + ((entityHealth - 20) / 2);
            }
        }
        this.spawnDelay = newSpawnDelay;
        this.maxSpawnDelay = newSpawnDelay * 20;
        this.minSpawnDelay = newSpawnDelay * 10;
    }

    public int getEntityHealth() {
        return this.entityHealth;
    }

    public void resetHealthAndDelays() {
        this.entityHealth = 20;
        this.spawnDelay = 20;
        this.maxSpawnDelay = 400;
        this.minSpawnDelay = 200;
    }

    public void setEntityId(EntityType<?> pType, @Nullable Level pLevel, RandomSource pRandom, BlockPos pPos) {
        this.getOrCreateNextSpawnData(pLevel, pRandom, pPos).getEntityToSpawn().putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(pType).toString());
    }

    public void getEntityId(@Nullable Level pLevel, RandomSource pRandom, BlockPos pPos) {
        this.getOrCreateNextSpawnData(pLevel, pRandom, pPos).getEntityToSpawn().getString("id");
    }

    public void clearEntityId(@Nullable Level pLevel, RandomSource pRandom, BlockPos pPos) {
        this.getOrCreateNextSpawnData(pLevel, pRandom, pPos).getEntityToSpawn().remove("id");
        this.displayEntity = null;
    }

    private boolean isNearPlayer(Level pLevel, BlockPos pPos) {
        return pLevel.hasNearbyAlivePlayer((double)pPos.getX() + 0.5, (double)pPos.getY() + 0.5, (double)pPos.getZ() + 0.5, (double)this.requiredPlayerRange);
    }

    public void clientTick(Level pLevel, BlockPos pPos) {
        if (!this.isNearPlayer(pLevel, pPos)) {
            this.oSpin = this.spin;
        } else if (this.displayEntity != null) {
            RandomSource randomsource = pLevel.getRandom();
            double d0 = (double)pPos.getX() + randomsource.nextDouble();
            double d1 = (double)pPos.getY() + randomsource.nextDouble();
            double d2 = (double)pPos.getZ() + randomsource.nextDouble();
            pLevel.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
            pLevel.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0, d1, d2, 0.0, 0.0, 0.0);
            if (this.spawnDelay > 0) {
                --this.spawnDelay;
            } else if (this.spawnDelay == 0){
                for(int k = 0; k < 3; ++k) {
                    double x = (double)pPos.getX() + 0.5 + (randomsource.nextDouble() - 0.5) * 2.0;
                    double y = (double)pPos.getY() + 0.5 + (randomsource.nextDouble() - 0.5) * 2.0;
                    double z = (double)pPos.getZ() + 0.5 + (randomsource.nextDouble() - 0.5) * 2.0;
                    pLevel.addParticle(ParticleTypes.SOUL, x, y, z, 0.0, 0.0, 0.0);
                }
            }


            this.oSpin = this.spin;
            this.spin = (this.spin + (double)(1000.0F / ((float)this.spawnDelay + 200.0F))) % 360.0;
        }

    }

    public void serverTick(ServerLevel pServerLevel, BlockPos pPos) {
        if (this.isNearPlayer(pServerLevel, pPos)) {
            if (this.spawnDelay == -1) {
                this.delay(pServerLevel, pPos);
            }

            if (this.spawnDelay > 0) {
                --this.spawnDelay;
            } else {
                boolean flag = false;
                RandomSource randomsource = pServerLevel.getRandom();
                SpawnData spawndata = this.getOrCreateNextSpawnData(pServerLevel, randomsource, pPos);
                int i = 0;

                while(true) {
                    if (i >= this.spawnCount) {
                        if (flag) {
                            this.delay(pServerLevel, pPos);
                        }
                        break;
                    }

                    CompoundTag compoundtag = spawndata.getEntityToSpawn();
                    Optional<EntityType<?>> optional = EntityType.by(compoundtag);
                    if (optional.isEmpty()) {
                        this.delay(pServerLevel, pPos);
                        return;
                    }

                    ListTag listtag = compoundtag.getList("Pos", 6);
                    int j = listtag.size();
                    double d0 = j >= 1 ? listtag.getDouble(0) : (double)pPos.getX() + (randomsource.nextDouble() - randomsource.nextDouble()) * (double)this.spawnRange + 0.5;
                    double d1 = j >= 2 ? listtag.getDouble(1) : (double)(pPos.getY() + randomsource.nextInt(3) - 1);
                    double d2 = j >= 3 ? listtag.getDouble(2) : (double)pPos.getZ() + (randomsource.nextDouble() - randomsource.nextDouble()) * (double)this.spawnRange + 0.5;
                    if (pServerLevel.noCollision(((EntityType)optional.get()).getAABB(d0, d1, d2))) {
                        label105: {
                            BlockPos blockpos = BlockPos.containing(d0, d1, d2);
                            if (spawndata.getCustomSpawnRules().isPresent()) {
                                if (!((EntityType)optional.get()).getCategory().isFriendly() && pServerLevel.getDifficulty() == Difficulty.PEACEFUL) {
                                    break label105;
                                }

                                SpawnData.CustomSpawnRules spawndata$customspawnrules = (SpawnData.CustomSpawnRules)spawndata.getCustomSpawnRules().get();
                                if (!spawndata$customspawnrules.blockLightLimit().isValueInRange(pServerLevel.getBrightness(LightLayer.BLOCK, blockpos)) || !spawndata$customspawnrules.skyLightLimit().isValueInRange(pServerLevel.getBrightness(LightLayer.SKY, blockpos))) {
                                    break label105;
                                }
                            } else if (!SpawnPlacements.checkSpawnRules((EntityType)optional.get(), pServerLevel, MobSpawnType.SPAWNER, blockpos, pServerLevel.getRandom())) {
                                break label105;
                            }

                            Entity entity = EntityType.loadEntityRecursive(compoundtag, pServerLevel, (p_151310_) -> {
                                p_151310_.moveTo(d0, d1, d2, p_151310_.getYRot(), p_151310_.getXRot());
                                return p_151310_;
                            });
                            if (entity == null) {
                                this.delay(pServerLevel, pPos);
                                return;
                            }

                            int k = pServerLevel.getEntitiesOfClass(entity.getClass(), (new AABB((double)pPos.getX(), (double)pPos.getY(), (double)pPos.getZ(), (double)(pPos.getX() + 1), (double)(pPos.getY() + 1), (double)(pPos.getZ() + 1))).inflate((double)this.spawnRange)).size();
                            if (k >= this.maxNearbyEntities) {
                                this.delay(pServerLevel, pPos);
                                return;
                            }

                            entity.moveTo(entity.getX(), entity.getY(), entity.getZ(), randomsource.nextFloat() * 360.0F, 0.0F);
                            if (entity instanceof Mob) {
                                Mob mob = (Mob)entity;
                                if (!ForgeEventFactory.checkSpawnPositionSpawner(mob, pServerLevel, MobSpawnType.SPAWNER, spawndata, this)) {
                                    break label105;
                                }

                                MobSpawnEvent.FinalizeSpawn event = ForgeEventFactory.onFinalizeSpawnSpawner(mob, pServerLevel, pServerLevel.getCurrentDifficultyAt(entity.blockPosition()), (SpawnGroupData)null, compoundtag, this);
                                if (event != null && spawndata.getEntityToSpawn().size() == 1 && spawndata.getEntityToSpawn().contains("id", 8)) {
                                    ((Mob)entity).finalizeSpawn(pServerLevel, event.getDifficulty(), event.getSpawnType(), event.getSpawnData(), event.getSpawnTag());
                                }
                            }

                            if (!pServerLevel.tryAddFreshEntityWithPassengers(entity)) {
                                this.delay(pServerLevel, pPos);
                                return;
                            }

                            pServerLevel.gameEvent(entity, GameEvent.ENTITY_PLACE, blockpos);
                            if (entity instanceof Mob) {
                                ((Mob)entity).spawnAnim();
                            }

                            flag = true;
                        }
                    }

                    ++i;
                }
            }
        }

    }

    private void delay(Level pLevel, BlockPos pPos) {
        RandomSource randomsource = pLevel.random;
        if (this.maxSpawnDelay <= this.minSpawnDelay) {
            this.spawnDelay = this.minSpawnDelay;
        } else {
            this.spawnDelay = this.minSpawnDelay + randomsource.nextInt(this.maxSpawnDelay - this.minSpawnDelay);
        }

        this.spawnPotentials.getRandom(randomsource).ifPresent((p_186386_) -> {
            this.setNextSpawnData(pLevel, pPos, (SpawnData)p_186386_.getData());
        });
        this.broadcastEvent(pLevel, pPos, 1);
    }

    public void load(@Nullable Level pLevel, BlockPos pPos, CompoundTag pTag) {
        this.spawnDelay = pTag.getShort("Delay");
        boolean flag = pTag.contains("SpawnData", 10);
        if (flag) {
            SpawnData spawndata = (SpawnData)SpawnData.CODEC.parse(NbtOps.INSTANCE, pTag.getCompound("SpawnData")).resultOrPartial((p_186391_) -> {
                LOGGER.warn("Invalid SpawnData: {}", p_186391_);
            }).orElseGet(SpawnData::new);
            this.setNextSpawnData(pLevel, pPos, spawndata);
        }

        boolean flag1 = pTag.contains("SpawnPotentials", 9);
        if (flag1) {
            ListTag listtag = pTag.getList("SpawnPotentials", 10);
            this.spawnPotentials = (SimpleWeightedRandomList)SpawnData.LIST_CODEC.parse(NbtOps.INSTANCE, listtag).resultOrPartial((p_186388_) -> {
                LOGGER.warn("Invalid SpawnPotentials list: {}", p_186388_);
            }).orElseGet(SimpleWeightedRandomList::empty);
        } else {
            this.spawnPotentials = SimpleWeightedRandomList.single(this.nextSpawnData != null ? this.nextSpawnData : new SpawnData());
        }

        if (pTag.contains("MinSpawnDelay", 99)) {
            this.minSpawnDelay = pTag.getShort("MinSpawnDelay");
            this.maxSpawnDelay = pTag.getShort("MaxSpawnDelay");
            this.spawnCount = pTag.getShort("SpawnCount");
        }

        if (pTag.contains("MaxNearbyEntities", 99)) {
            this.maxNearbyEntities = pTag.getShort("MaxNearbyEntities");
            this.requiredPlayerRange = pTag.getShort("RequiredPlayerRange");
        }

        if (pTag.contains("SpawnRange", 99)) {
            this.spawnRange = pTag.getShort("SpawnRange");
        }

        if (pTag.contains("EntityMaxHealth", 99)) {
            this.spawnRange = pTag.getShort("EntityMaxHealth");
        }

        this.displayEntity = null;
    }

    public CompoundTag save(CompoundTag pTag) {
        pTag.putShort("EntityMaxHealth", (short)this.entityHealth);
        pTag.putShort("Delay", (short)this.spawnDelay);
        pTag.putShort("MinSpawnDelay", (short)this.minSpawnDelay);
        pTag.putShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
        pTag.putShort("SpawnCount", (short)this.spawnCount);
        pTag.putShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
        pTag.putShort("RequiredPlayerRange", (short)this.requiredPlayerRange);
        pTag.putShort("SpawnRange", (short)this.spawnRange);
        if (this.nextSpawnData != null) {
            pTag.put("SpawnData", (Tag)SpawnData.CODEC.encodeStart(NbtOps.INSTANCE, this.nextSpawnData).result().orElseThrow(() -> {
                return new IllegalStateException("Invalid SpawnData");
            }));
        }

        pTag.put("SpawnPotentials", (Tag)SpawnData.LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.spawnPotentials).result().orElseThrow());
        return pTag;
    }

    @Nullable
    public Entity getOrCreateDisplayEntity(Level pLevel, RandomSource pRandom, BlockPos pPos) {
        if (this.displayEntity == null) {
            CompoundTag compoundtag = this.getOrCreateNextSpawnData(pLevel, pRandom, pPos).getEntityToSpawn();
            if (!compoundtag.contains("id", 8)) {
                return null;
            }

            this.displayEntity = (LivingEntity) EntityType.loadEntityRecursive(compoundtag, pLevel, Function.identity());
            if (compoundtag.size() == 1 && this.displayEntity instanceof Mob) {
            }
        }

        return this.displayEntity;
    }

    public boolean onEventTriggered(Level pLevel, int pId) {
        if (pId == 1) {
            if (pLevel.isClientSide) {
                this.spawnDelay = this.minSpawnDelay;
            }

            return true;
        } else {
            return false;
        }
    }

    protected void setNextSpawnData(@Nullable Level pLevel, BlockPos pPos, SpawnData pNextSpawnData) {
        this.nextSpawnData = pNextSpawnData;
    }

    public SpawnData getOrCreateNextSpawnData(@Nullable Level pLevel, RandomSource pRandom, BlockPos pPos) {
        if (this.nextSpawnData != null) {
            return this.nextSpawnData;
        } else {
            this.setNextSpawnData(pLevel, pPos, this.spawnPotentials.getRandom(pRandom).map(WeightedEntry.Wrapper::getData).orElseGet(SpawnData::new));
            return this.nextSpawnData;
        }
    }

    public void broadcastEvent(Level var1, BlockPos var2, int var3) {
    }

    public double getSpin() {
        return this.spin;
    }

    public double getoSpin() {
        return this.oSpin;
    }

    @Nullable
    public Entity getSpawnerEntity() {
        return null;
    }

    @Nullable
    public BlockEntity getSpawnerBlockEntity() {
        return null;
    }
}
