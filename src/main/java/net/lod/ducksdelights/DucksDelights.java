package net.lod.ducksdelights;

import com.mojang.logging.LogUtils;
import net.lod.ducksdelights.block.*;
import net.lod.ducksdelights.block.custom.dispenser_behavior.ModDispenserBehaviors;
import net.lod.ducksdelights.entity.ModEntities;
import net.lod.ducksdelights.entity.ModEntityRenderers;
import net.lod.ducksdelights.entity.mobeffects.ModMobEffects;
import net.lod.ducksdelights.entity.mobeffects.ModPotionRecipes;
import net.lod.ducksdelights.item.ModCreativeModeTabs;
import net.lod.ducksdelights.item.ModItems;
import net.lod.ducksdelights.item.custom.ModPotions;
import net.lod.ducksdelights.recipe.ModRecipes;
import net.lod.ducksdelights.sound.ModSoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DucksDelights.MOD_ID)
public class DucksDelights {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "ducksdelights";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public DucksDelights(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ModRecipes.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModMobEffects.register(modEventBus);
        ModPotions.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSoundEvents.register(modEventBus);



        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);


    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModEntityRenderers.register();
        ModBlockEntityRenderers.register();
        ModDispenserBehaviors.register();
        ModCompostables.register();
        ModPotionRecipes.register();
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        //if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
        //    event.accept(ModItems.VOID_BOTTLE);
        //}
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}