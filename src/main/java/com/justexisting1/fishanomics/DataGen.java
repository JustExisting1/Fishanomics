package com.justexisting1.fishanomics;

import com.justexisting1.fishanomics.item.FishanomicItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class DataGen {


    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();

        gen.addProvider(event.includeServer(), new Recipes(gen.getPackOutput(), event.getLookupProvider()));
    }

    private static class Recipes extends RecipeProvider {
        public Recipes(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
            super(output, lookup);
        }

        @Override
        protected void buildRecipes(RecipeOutput consumer) {
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, FishanomicItems.STONE_HOOK.get())
                    .pattern("  s")
                    .pattern("b b")
                    .pattern(" b ")
                    .define('s', Ingredient.of(ItemTags.STONE_TOOL_MATERIALS))
                    .define('b', Ingredient.of(Items.COBBLESTONE_SLAB))
                    .unlockedBy("has_stick", has(Items.COBBLESTONE))
                    .save(consumer);
        }

    }
}
