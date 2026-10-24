package net.lod.ducksdelights.item.custom;

import net.lod.ducksdelights.Config;
import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.item.ModItems;
import net.lod.ducksdelights.util.pocketDim.PocketDimSaveTransportHandler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class EnderKeyItem extends Item {
    public EnderKeyItem(Properties pProperties) {
        super(pProperties);
    }

    public int getUseDuration(ItemStack pStack) {
        return 80;
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pHand);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pLevel.isClientSide()) {
            for(int i = 0; i < 2; ++i) {
                pLevel.addParticle(ParticleTypes.PORTAL, pLivingEntity.getRandomX(0.5), pLivingEntity.getRandomY() - 0.25, pLivingEntity.getRandomZ(0.5), (pLevel.random.nextDouble() - 0.5) * 2.0, -pLevel.random.nextDouble(), (pLevel.random.nextDouble() - 0.5) * 2.0);
            }
        }
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
    }

    @SuppressWarnings("removal")
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        Player player = pEntityLiving instanceof Player ? (Player)pEntityLiving : null;

        if (pLevel instanceof ServerLevel && player != null) {
            if (!pLevel.dimension().location().equals(new ResourceLocation(DucksDelights.MOD_ID, "pocket"))) {
                ResourceKey<Level> resourceKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(DucksDelights.MOD_ID, "pocket"));
                ServerLevel targetServerLevel = ((ServerLevel)pLevel).getServer().getLevel(resourceKey);
                if (targetServerLevel != null) {
                    PocketDimSaveTransportHandler transportHandler = PocketDimSaveTransportHandler.checkData(pLevel.getServer().overworld());
                    transportHandler.teleportInPocketDim(targetServerLevel, player);
                }
            } else if (player.getPersistentData().contains("ducks_pocketDimOldDim")) {
                String oldDim = player.getPersistentData().getString("ducks_pocketDimOldDim");
                ResourceKey<Level> resourceKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(oldDim));
                ServerLevel targetServerLevel = ((ServerLevel)pLevel).getServer().getLevel(resourceKey);
                if (targetServerLevel != null) {
                    PocketDimSaveTransportHandler transportHandler = PocketDimSaveTransportHandler.checkData(pLevel.getServer().overworld());
                    transportHandler.teleportOutPocketDim(targetServerLevel, player);
                }
            }
            player.getCooldowns().addCooldown(ModItems.ENDER_KEY.get(), 20 * Config.ender_key_cooldown); // 20 ticks = 1 second
        }

        return pStack;
    }
}
