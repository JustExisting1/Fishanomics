package com.justexisting1.fishanomics.item;

import com.justexisting1.fishanomics.Fishanomics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;


public class FishanomicItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Fishanomics.MOD_ID);
    public static final Collection<DeferredItem<Item>> ITEMS_FOR_TABS = new ArrayList<>();

    //Fish
    public static final DeferredItem<Item> COAL_FISH = registerWithTab(SimpleItem::new, "coal_fish");
    public static final DeferredItem<Item> COPPER_FISH = registerWithTab(SimpleItem::new, "copper_fish");
    public static final DeferredItem<Item> IRON_FISH = registerWithTab(SimpleItem::new, "iron_fish");
    public static final DeferredItem<Item> GOLD_FISH = registerWithTab(SimpleItem::new, "gold_fish");
    public static final DeferredItem<Item> REDSTONE_FISH = registerWithTab(SimpleItem::new, "redstone_fish");
    public static final DeferredItem<Item> LAPIS_FISH = registerWithTab(SimpleItem::new, "lapis_fish");
    public static final DeferredItem<Item> DIAMOND_FISH = registerWithTab(SimpleItem::new, "diamond_fish");

    //Simple Items
    public static final DeferredItem<Item> WOODEN_HOOK = registerWithTab(SimpleItem::new, "wooden_hook");
    public static final DeferredItem<Item> STONE_HOOK = registerWithTab(SimpleItem::new, "stone_hook");
    public static final DeferredItem<Item> IRON_HOOK = registerWithTab(SimpleItem::new, "iron_hook");
    public static final DeferredItem<Item> GOLD_HOOK = registerWithTab(SimpleItem::new, "gold_hook");
    public static final DeferredItem<Item> DIAMOND_HOOK = registerWithTab(SimpleItem::new, "diamond_hook");

    //Fishing Rods
    public static final DeferredItem<Item> WOODEN_FISHING_ROD = registerWithTab(() -> new FishanomicFishingRodItem(Tiers.WOOD, new Item.Properties().durability(59)), "wooden_fishing_rod");
    public static final DeferredItem<Item> STONE_FISHING_ROD = registerWithTab(() -> new FishanomicFishingRodItem(Tiers.STONE, new Item.Properties().durability(131)), "stone_fishing_rod");
    public static final DeferredItem<Item> IRON_FISHING_ROD = registerWithTab(() -> new FishanomicFishingRodItem(Tiers.IRON, new Item.Properties().durability(250)), "iron_fishing_rod");
    public static final DeferredItem<Item> GOLD_FISHING_ROD = registerWithTab(() -> new FishanomicFishingRodItem(Tiers.GOLD, new Item.Properties().durability(32)), "gold_fishing_rod");
    public static final DeferredItem<Item> DIAMOND_FISHING_ROD = registerWithTab(() -> new FishanomicFishingRodItem(Tiers.DIAMOND, new Item.Properties().durability(1561)), "diamond_fishing_rod");
    public static final DeferredItem<Item> NETHERITE_FISHING_ROD = registerWithTab(() -> new FishanomicFishingRodItem(Tiers.NETHERITE, new Item.Properties().durability(2031)), "netherite_fishing_rod");

    public static DeferredItem<Item> register(Supplier<Item> initialiser, String name){
        return ITEMS.register(name, initialiser);
    }

    public static DeferredItem<Item> registerWithTab(Supplier<Item> initialiser, String name){
        DeferredItem<Item> itemToRegister = register(initialiser, name);
        ITEMS_FOR_TABS.add(itemToRegister);
        return itemToRegister;
    }
}
