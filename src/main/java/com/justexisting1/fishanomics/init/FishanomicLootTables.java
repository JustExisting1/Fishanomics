package com.justexisting1.fishanomics.init;

import com.justexisting1.fishanomics.Fishanomics;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;

public class FishanomicLootTables {

    public static final ResourceKey<LootTable> FISH = register("fishing/fish");
    public static final ResourceKey<LootTable> WOODEN_ROD = register("fishing/tiers/wooden_rod");


    private static ResourceKey<LootTable> register(String path){
        return BuiltInLootTables.register(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Fishanomics.MOD_ID, path)));
    }

}
