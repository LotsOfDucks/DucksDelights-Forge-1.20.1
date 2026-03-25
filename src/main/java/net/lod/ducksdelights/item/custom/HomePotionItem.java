package net.lod.ducksdelights.item.custom;

import net.lod.ducksdelights.Config;
import net.lod.ducksdelights.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
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
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HomePotionItem extends Item {
    public HomePotionItem(Properties pProperties) {
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
        if (itemStack.getItem() instanceof HomePotionItem) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entityLiving) {
        if (!level.isClientSide && entityLiving instanceof ServerPlayer serverPlayer) {
            boolean interDimensional = Config.HOME_POTION_CROSS_DIMENSIONS.get();
            ResourceKey<net.minecraft.world.level.Level> currentDimension = level.dimension();
            ResourceKey<Level> respawnDimension = serverPlayer.getRespawnDimension();
            ServerLevel serverWorld = interDimensional ? serverPlayer.server.getLevel(respawnDimension) : (currentDimension == respawnDimension ? Objects.requireNonNull(level.getServer()).getLevel(currentDimension) : null);
            if (serverWorld != null) {
                try {
                    Optional<Vec3> respawnLocation = Player.findRespawnPositionAndUseSpawnBlock(serverWorld, Objects.requireNonNull(serverPlayer.getRespawnPosition()), serverPlayer.getRespawnAngle(), false, false);
                    if (respawnLocation.isPresent()) {
                        Vec3 respawnVec = respawnLocation.get();
                        if (!serverPlayer.isCreative()) {
                            stack.shrink(1);
                            serverPlayer.addItem(new ItemStack(Items.GLASS_BOTTLE));
                        }
                        serverPlayer.resetFallDistance();
                        serverPlayer.teleportTo(serverWorld, respawnVec.x, respawnVec.y, respawnVec.z, serverPlayer.getYRot(), serverPlayer.getXRot());
                        serverWorld.playSound(null, respawnVec.x, respawnVec.y, respawnVec.z, SoundEvents.CHORUS_FRUIT_TELEPORT, serverPlayer.getSoundSource(), 1.0F, 1.0F);
                        serverPlayer.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                        serverPlayer.hurt(serverPlayer.damageSources().fall(), 2);
                        serverPlayer.getCooldowns().addCooldown(ModItems.HOME_POTION.get(), 20 * Config.HOME_POTION_COOLDOWN.get()); // 20 ticks = 1 second
                    } else {
                        serverPlayer.displayClientMessage(Component.translatable("item.ducksdelights.home_potion.failed_respawn"), true);
                    }
                } catch (Exception e) {
                    serverPlayer.displayClientMessage(Component.translatable("item.ducksdelights.home_potion.failed_respawn"), true);
                }
            } else {
                serverPlayer.displayClientMessage(Component.translatable("item.ducksdelights.home_potion.failed_dimension"), true);
            }
        }
        return stack;
    }

    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        pTooltip.add(Component.translatable("item.ducksdelights.home_potion.desc1").withStyle(ChatFormatting.GRAY));
        if (Config.HOME_POTION_CROSS_DIMENSIONS.get()) {
            pTooltip.add(Component.translatable("item.ducksdelights.home_potion.desc2").withStyle(ChatFormatting.BLUE));
        } else {
            pTooltip.add(Component.translatable("item.ducksdelights.home_potion.desc3").withStyle(ChatFormatting.RED));
        }

    }

}
