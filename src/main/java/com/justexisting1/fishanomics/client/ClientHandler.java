package com.justexisting1.fishanomics.client;

import com.justexisting1.fishanomics.Fishanomics;
import com.justexisting1.fishanomics.item.FishanomicItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public class ClientHandler {
    public static void setupClient(){
        registerFishingRodModel(FishanomicItems.WOODEN_FISHING_ROD.get());
        registerFishingRodModel(FishanomicItems.STONE_FISHING_ROD.get());
        registerFishingRodModel(FishanomicItems.IRON_FISHING_ROD.get());
        registerFishingRodModel(FishanomicItems.GOLD_FISHING_ROD.get());
        registerFishingRodModel(FishanomicItems.DIAMOND_FISHING_ROD.get());
        registerFishingRodModel(FishanomicItems.NETHERITE_FISHING_ROD.get());
    }

    public static void registerFishingRodModel(Item fishingRod){
        ItemProperties.register(fishingRod, ResourceLocation.fromNamespaceAndPath(Fishanomics.MOD_ID, "cast"), (stack, level, entity, seed) -> {
            if(entity == null){
                return 0.0F;
            } else {
                boolean isMainhand = entity.getMainHandItem() == stack;
                boolean isOffhand = entity.getOffhandItem() == stack;
                if(entity.getMainHandItem().getItem() instanceof FishingRodItem){
                    isOffhand = false;
                }
                return (isMainhand || isOffhand) && entity instanceof Player && ((Player) entity).fishing != null ? 1.0F : 0.0F;
            }
        });
    }
}
