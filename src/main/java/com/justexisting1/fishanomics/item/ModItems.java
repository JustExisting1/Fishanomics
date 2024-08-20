package com.justexisting1.fishanomics.item;

import com.justexisting1.fishanomics.Fishanomics;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Fishanomics.MOD_ID);
    public static final DeferredRegister.Items FISHING_RODS = DeferredRegister.createItems(Fishanomics.MOD_ID);


    public static final DeferredItem<Item> IRON_FISH = ITEMS.register("iron_fish", () -> new Item(new Item.Properties()));

    //register fishing rod
    public static final DeferredItem<FishingRodItem> WOODEN_FISHING_ROD = FISHING_RODS.register("wooden_fishing_rod", () -> new FishanomicFishingRodItem(
            Tiers.WOOD, new Item.Properties().durability(59)
    ));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        FISHING_RODS.register(eventBus);
    }
}
