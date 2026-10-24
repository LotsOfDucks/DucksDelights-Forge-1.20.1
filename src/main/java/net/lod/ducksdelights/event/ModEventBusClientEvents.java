package net.lod.ducksdelights.event;

import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.block.ModBlocks;
import net.lod.ducksdelights.block.custom.FillableBarrelBlock;
import net.lod.ducksdelights.block.custom.RedstoneBarrelBlock;
import net.lod.ducksdelights.block.entity.PearlLampBlockEntity;
import net.lod.ducksdelights.item.ModItemProperties;
import net.lod.ducksdelights.item.ModItems;
import net.lod.ducksdelights.item.custom.PearlFishingRodItem;
import net.lod.ducksdelights.util.BrewingHandler;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = DucksDelights.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {

    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            if (tintIndex == 1) {
                Collection<MobEffectInstance> mobEffectInstances = PotionUtils.getCustomEffects(stack);
                if (!mobEffectInstances.isEmpty()) {
                    return BrewingHandler.getPearledColor(PotionUtils.getCustomEffects(stack));
                }
            }
            return -1;
        }, ModItems.PEARL_AXE.get(), ModItems.PEARLED_APPLE.get(), ModItems.ENCHANTED_PEARLED_APPLE.get());
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((blockState, blockAndTintGetter, blockPos, tintIndex) -> {
            if (tintIndex == 1) {
                return RedstoneBarrelBlock.getColorForPower(blockState.getValue(FillableBarrelBlock.FULLNESS));
            } else {
                return -1;
            }
        }, ModBlocks.REDSTONE_BARREL.get());
        //event.register((blockState, blockAndTintGetter, blockPos, tintIndex) -> {
        //    if (blockPos != null) {
        //        Vec3 pos = blockPos.getCenter();
        //        long scalar = Mth.getSeed((int) pos.x(), (int) pos.y(), (int) pos.z());
        //        double posX = (1 / (1 + Math.exp(Math.sin(pos.x() * scalar))));
        //        double posY = (1 / (1 + Math.exp(Math.sin(pos.y() * scalar))));
        //        double posZ = (1 / (1 + Math.exp(Math.sin(pos.z() * scalar))));
        //        return Mth.color((float)posX, (float)posY, (float)posZ);
        //    } else {
        //        return -1;
        //    }
        //}, ModBlocks.PEARL_BLOCK.get());
        event.register((blockState, blockAndTintGetter, blockPos, tintIndex) -> {
            if (blockAndTintGetter.getBlockEntity(blockPos) instanceof PearlLampBlockEntity pearlLampBlockEntity) {
                if (pearlLampBlockEntity.hasCustomColor) {
                    return pearlLampBlockEntity.getDyeColor();
                }
            }
            return -1;
        }, ModBlocks.PEARL_LAMP.get());
    }
}
