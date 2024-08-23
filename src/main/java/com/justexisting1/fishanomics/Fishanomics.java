package com.justexisting1.fishanomics;

import com.justexisting1.fishanomics.block.ModBlocks;
import com.justexisting1.fishanomics.client.ClientHandler;
import com.justexisting1.fishanomics.item.FishanomicFishingRodItem;
import com.justexisting1.fishanomics.item.ModCreativeModeTabs;
import com.justexisting1.fishanomics.item.FishanomicItems;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Fishanomics.MOD_ID)
public class Fishanomics {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "fishanomics";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Fishanomics(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::setupClient);
        this.registerDeferredRegistries(modEventBus);
        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);



//        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::gatherData);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void setupClient(FMLClientSetupEvent event){
        event.enqueueWork(ClientHandler::setupClient);
    }

    public void registerDeferredRegistries(IEventBus eventBus){
        //Pulls list of mod items -> registers them
        FishanomicItems.ITEMS.register(eventBus);
        ModBlocks.register(eventBus);

        // Register the item to a creative tab
        ModCreativeModeTabs.register(eventBus);
    }

    // Add the example block item to the building blocks tab
//    private void addCreative(BuildCreativeModeTabContentsEvent event) {
//        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
//            event.accept(ModItems.IRON_FISH);
//        }
//
//        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
//            event.accept(ModBlocks.FISH_TANK);
//        }
//    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    public void gatherData(GatherDataEvent event)
    {
        DataGen.gatherData(event);
    }

}
