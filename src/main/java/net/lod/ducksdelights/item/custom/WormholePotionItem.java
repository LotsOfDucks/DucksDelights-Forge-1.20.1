package net.lod.ducksdelights.item.custom;

import net.lod.ducksdelights.Config;
import net.lod.ducksdelights.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

public class WormholePotionItem extends Item {
    public WormholePotionItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 32;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.getItem() instanceof WormholePotionItem) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entityLiving) {
        if (!level.isClientSide && entityLiving instanceof ServerPlayer serverPlayer) {
            boolean targetFoundFlag = false;
            boolean interDimensional = Config.WORMHOLE_POTION_CROSS_DIMENSIONS.get();
            String targetName = stack.getDisplayName().toFlatList().get(1).getString();
            List<String> possibleTargetsList = Arrays.stream((Objects.requireNonNull(level.getServer())).getPlayerNames()).toList();

            for (int slot = 0; slot < possibleTargetsList.size(); slot ++) {
                if (!serverPlayer.isCreative()) {
                    if (!level.getServer().getPlayerList().getPlayers().get(slot).isCreative() && !level.getServer().getPlayerList().getPlayers().get(slot).isSpectator()) {
                        targetFoundFlag = Objects.equals(possibleTargetsList.get(slot), targetName);
                        if (targetFoundFlag) {
                            break;
                        }
                    }
                } else {
                    targetFoundFlag = Objects.equals(possibleTargetsList.get(slot), targetName);
                    if (targetFoundFlag) {
                        break;
                    }
                }
            }
            if (targetFoundFlag) {
                ServerPlayer targetPlayer = level.getServer().getPlayerList().getPlayerByName(targetName);
                ResourceKey<Level> currentDimension = level.dimension();
                ResourceKey<Level> targetDimension = Objects.requireNonNull(targetPlayer).level().dimension();
                ServerLevel serverWorld = interDimensional ? serverPlayer.server.getLevel(targetDimension) : (currentDimension == targetDimension ? Objects.requireNonNull(level.getServer()).getLevel(currentDimension) : null);
                if (serverWorld != null) {
                    try {
                        Vec3 respawnVec = targetPlayer.position();
                        if (!serverPlayer.isCreative()) {
                            stack.shrink(1);
                            serverPlayer.addItem(new ItemStack(Items.GLASS_BOTTLE));
                        }
                        serverPlayer.resetFallDistance();
                        serverPlayer.teleportTo(serverWorld, respawnVec.x, respawnVec.y, respawnVec.z, serverPlayer.getYRot(), serverPlayer.getXRot());
                        serverWorld.playSound(null, respawnVec.x, respawnVec.y, respawnVec.z, SoundEvents.CHORUS_FRUIT_TELEPORT, serverPlayer.getSoundSource(), 1.0F, 1.0F);
                        serverPlayer.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                        serverPlayer.hurt(serverPlayer.damageSources().fall(), 2);
                        serverPlayer.getCooldowns().addCooldown(ModItems.WORMHOLE_POTION.get(), 20 * Config.WORMHOLE_POTION_COOLDOWN.get()); // 20 ticks = 1 second
                    } catch (Exception e) {
                        serverPlayer.displayClientMessage(Component.translatable("item.ducksdelights.wormhole_potion.failed_teleport"), true);
                    }
                } else {
                    serverPlayer.displayClientMessage(Component.translatable("item.ducksdelights.wormhole_potion.failed_dimension"), true);
                }
            } else {
                serverPlayer.displayClientMessage(Component.translatable("item.ducksdelights.wormhole_potion.failed_teleport_no_player_present"), true);
            }
        }
        return stack;
    }

    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        pTooltip.add(Component.translatable("item.ducksdelights.wormhole_potion.desc1").withStyle(ChatFormatting.GRAY));
        if (Config.WORMHOLE_POTION_CROSS_DIMENSIONS.get()) {
            pTooltip.add(Component.translatable("item.ducksdelights.wormhole_potion.desc2").withStyle(ChatFormatting.BLUE));
        } else {
            pTooltip.add(Component.translatable("item.ducksdelights.wormhole_potion.desc3").withStyle(ChatFormatting.RED));
        }
    }
}
