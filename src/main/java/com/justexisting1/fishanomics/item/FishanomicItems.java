package com.justexisting1.fishanomics.item;

import com.justexisting1.fishanomics.Fishanomics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;


public class FishanomicItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Fishanomics.MOD_ID);
    public static final Collection<DeferredItem<Item>> ITEMS_FOR_TABS = new ArrayList<>();

    //Simple Items
    public static final DeferredItem<Item> IRON_FISH = registerWithTab(SimpleItem::new, "iron_fish");
    public static final DeferredItem<Item> WOODEN_HOOK = registerWithTab(SimpleItem::new, "wooden_hook");
    public static final DeferredItem<Item> STONE_HOOK = registerWithTab(SimpleItem::new, "stone_hook");

    //Fishing Rods
    public static final DeferredItem<Item> WOODEN_FISHING_ROD = registerWithTab(() -> new FishanomicFishingRodItem(Tiers.WOOD, new Item.Properties().durability(59)), "wooden_fishing_rod");

    public static DeferredItem<Item> register(Supplier<Item> initialiser, String name){
        return ITEMS.register(name, initialiser);
    }

    public static DeferredItem<Item> registerWithTab(Supplier<Item> initialiser, String name){
        DeferredItem<Item> itemToRegister = register(initialiser, name);
        ITEMS_FOR_TABS.add(itemToRegister);
        return itemToRegister;
    }
}
