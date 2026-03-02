package net.lod.ducksdelights.item.custom;

import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.block.entity.BlightedSpawnerBlockEntity;
import net.lod.ducksdelights.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.List;

public class SoulCageItem extends Item {
    public static final String TAG_SOUL_ID = "id";

    public SoulCageItem(Properties pProperties) {
        super(pProperties);
    }

    //I HATE THIS ITEM I TRIED CODING IT LIKE 5 SEPARATE TIMES
    //AUGH

    public static boolean isContainingSoul(ItemStack pStack) {
        CompoundTag itemTag = pStack.getTag();
        return itemTag != null && itemTag.contains("id");
    }

    public boolean isFoil(ItemStack pStack) {
        return isContainingSoul(pStack) || super.isFoil(pStack);
    }

    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity entity, InteractionHand hand) {
        Level level = entity.level();
        if (entity.getType().is(Tags.EntityTypes.BOSSES) || entity.getHealth() > 1) {
            return InteractionResult.PASS;
        } else {
            if (level instanceof ServerLevel) {
                ItemStack heldItem = playerIn.getItemInHand(hand);
                boolean isNotCreative = !playerIn.getAbilities().instabuild && heldItem.getCount() == 1;
                if (isNotCreative) {
                    this.addSoulTags(entity.getType(), heldItem.getOrCreateTag());
                    this.addHealthTags(entity, heldItem.getOrCreateTag());
                } else {
                    ItemStack newStack = new ItemStack(ModItems.SOUL_CAGE.get(), 1);
                    CompoundTag itemTag = heldItem.hasTag() ? heldItem.getTag().copy() : new CompoundTag();
                    newStack.setTag(itemTag);
                    if (!playerIn.getAbilities().instabuild) {
                        heldItem.shrink(1);
                    }
                    this.addSoulTags(entity.getType(), itemTag);
                    this.addHealthTags(entity, itemTag);
                    if (!playerIn.getInventory().add(newStack)) {
                        playerIn.drop(newStack, false);
                    }
                }
                level.playSound(null, playerIn, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.PLAYERS, 10.0F, 1.0F);
            } else {
                level.addParticle(ParticleTypes.SOUL, entity.getX(), entity.getY() + 0.5, entity.getZ(), 0.0, 0.075, 0.0);
            }
            entity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    private void addSoulTags(@Nullable EntityType<?> target, CompoundTag pCompoundTag) {
        if (!pCompoundTag.contains("id")) {
            pCompoundTag.putString("id", EntityType.getKey(target).toString());
        }
    }

    private void addHealthTags(LivingEntity target, CompoundTag pCompoundTag) {
        if (!pCompoundTag.contains("EntityMaxHealth")) {
            pCompoundTag.putShort("EntityMaxHealth", (short)target.getMaxHealth());
        }
    }

    private void addHealthTags(float healthValue, CompoundTag pCompoundTag) {
        if (!pCompoundTag.contains("EntityMaxHealth")) {
            pCompoundTag.putShort("EntityMaxHealth", (short)healthValue);
        }
    }

    public void clearTags(CompoundTag pCompoundTag) {
        if (pCompoundTag != null) {
            pCompoundTag.remove("id");
            pCompoundTag.remove("EntityMaxHealth");
        }
    }

    public EntityType<?> getType(@Nullable CompoundTag pNbt) {
        if (pNbt != null && pNbt.contains("id", 8)) {
            return EntityType.byString(pNbt.getString("id")).orElse(null);
        }
        return null;
    }

    public String getDescriptionId(ItemStack pStack) {
        return isContainingSoul(pStack) ? "item.ducksdelights.soul_cage_full" : super.getDescriptionId(pStack);
    }

    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        if (isContainingSoul(pStack)) {
            if (this.getType(pStack.getTag()) != null) {
                pTooltip.add(Component.translatable("item.ducksdelights.soul_cage_full.desc1").withStyle(ChatFormatting.GRAY));
                pTooltip.add(CommonComponents.space().append(this.getType(pStack.getTag()).toString()).withStyle(ChatFormatting.BLUE));
            } else {
                pTooltip.add(Component.translatable("item.ducksdelights.soul_cage_full.broken_desc").withStyle(ChatFormatting.GRAY));
            }
        } else {
            pTooltip.add(Component.translatable("item.ducksdelights.soul_cage.desc1").withStyle(ChatFormatting.GRAY));
            pTooltip.add(CommonComponents.space().append(Component.translatable("item.ducksdelights.soul_cage.desc2").withStyle(ChatFormatting.BLUE)));
        }

    }

    public InteractionResult useOn(UseOnContext pContext) {
        BlockPos clickedPos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        if (level.getBlockState(clickedPos).is(ModBlocks.BLIGHTED_SPAWNER_BLOCK.get())) {
            BlockEntity targetBlockEntityGrab = level.getBlockEntity(clickedPos);
            if (targetBlockEntityGrab instanceof BlightedSpawnerBlockEntity) {
                ItemStack heldItem = pContext.getItemInHand();
                BlightedSpawnerBlockEntity targetBlockEntity = (BlightedSpawnerBlockEntity) targetBlockEntityGrab;
                CompoundTag storedEntity = targetBlockEntity.getSpawner().getOrCreateNextSpawnData(level, level.getRandom(), clickedPos).getEntityToSpawn();
                if (isContainingSoul(heldItem)) {
                    if (!storedEntity.contains("id")) {
                        if (level instanceof ServerLevel) {
                            level.playSound(null, clickedPos, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 10.0F, 1.0F);
                            EntityType<?> newEntity = this.getType(heldItem.getTag());
                            targetBlockEntity.setEntityId(newEntity, level.getRandom());
                            targetBlockEntity.getSpawner().setEntityHealth(heldItem.getTag());
                            targetBlockEntity.setChanged();
                            level.sendBlockUpdated(clickedPos, targetBlockEntity.getBlockState(), targetBlockEntity.getBlockState(), 3);
                            level.gameEvent(pContext.getPlayer(), GameEvent.BLOCK_CHANGE, clickedPos);
                            clearTags(heldItem.getTag());
                        }
                        return InteractionResult.SUCCESS;
                    }
                    return super.useOn(pContext);
                } else {
                    if (storedEntity.contains("id")) {
                        if (level instanceof ServerLevel) {
                            level.playSound(null, clickedPos, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 10.0F, 1.0F);
                            addSoulTags(targetBlockEntity.getSpawner().getOrCreateDisplayEntity(level, level.getRandom(), clickedPos).getType(), heldItem.getOrCreateTag());
                            addHealthTags(targetBlockEntity.getSpawner().getEntityHealth(), heldItem.getOrCreateTag());
                            targetBlockEntity.clearEntityId(level.getRandom());
                            targetBlockEntity.getSpawner().resetHealthAndDelays();
                            targetBlockEntity.setChanged();
                            level.sendBlockUpdated(clickedPos, targetBlockEntity.getBlockState(), targetBlockEntity.getBlockState(), 3);
                            level.gameEvent(pContext.getPlayer(), GameEvent.BLOCK_CHANGE, clickedPos);
                        } else {
                            level.addParticle(ParticleTypes.SOUL, clickedPos.getX() + 0.5, clickedPos.getY() + 0.5, clickedPos.getZ() + 0.5, 0.0, 0.075, 0.0);
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
                return super.useOn(pContext);
            }
        }
        return super.useOn(pContext);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        if (isContainingSoul(itemstack)) {
            if (pPlayer.isCrouching()) {
                if (pLevel instanceof ServerLevel) {
                    pLevel.playSound(null, pPlayer, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 10.0F, 1.0F);
                    clearTags(itemstack.getTag());
                } else {
                    pLevel.addParticle(ParticleTypes.SOUL, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), 0.0, 0.075, 0.0);
                }
                return InteractionResultHolder.sidedSuccess(itemstack, true);
            }
        }
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
    }

}
