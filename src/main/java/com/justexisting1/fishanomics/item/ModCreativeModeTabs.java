package com.justexisting1.fishanomics.item;

import com.justexisting1.fishanomics.Fishanomics;
import com.justexisting1.fishanomics.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Fishanomics.MOD_ID);

    public static final Supplier<CreativeModeTab> FISHANOMICS_ITEMS_TAB = CREATIVE_MODE_TAB.register("fishanomics_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.IRON_FISH.get()))
                    .title(Component.translatable("creativetab.fishanomics.fishanomics_items"))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(ModItems.IRON_FISH);
                        output.accept(ModItems.WOODEN_FISHING_ROD);
                    })).build());

    public static final Supplier<CreativeModeTab> FISHANOMICS_BLOCK_TAB = CREATIVE_MODE_TAB.register("fishanomics_blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.FISH_TANK.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(Fishanomics.MOD_ID, "fishanomics_items_tab"))
                    .title(Component.translatable("creativetab.fishanomics.fishanomics_blocks"))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.FISH_TANK);
                    })).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
