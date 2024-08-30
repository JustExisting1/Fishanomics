package com.justexisting1.fishanomics;

import com.justexisting1.fishanomics.item.FishanomicItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.extensions.IHolderExtension;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class DataGen {


    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();

        gen.addProvider(event.includeServer(), new Recipes(gen.getPackOutput(), event.getLookupProvider()));
        gen.addProvider(event.includeClient(), new MyItemModelProvider(gen.getPackOutput(), event.getExistingFileHelper()));
    }

    public static class MyItemModelProvider extends ItemModelProvider {
        public MyItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
            super(output, "examplemod", existingFileHelper);
        }

        @Override
        protected void registerModels() {
            //NOTE, THE TEXTURE MUST BE PRESENT FOR THIS TO DATA GEN

            // Block items generally use their corresponding block models as parent.
//                withExistingParent(FishanomicItems.EXAMPLE_BLOCK_ITEM.get(), modLoc("block/example_block"));
            // Items generally use a simple parent and one texture. The most common parents are item/generated and item/handheld.
            // In this example, the item texture would be located at assets/examplemod/textures/item/example_item.png.
            // If you want a more complex model, you can use getBuilder() and then work from that, like you would with block models.
//            withExistingParent(String.valueOf(FishanomicItems.WOODEN_HOOK.get()), mcLoc("item/generated")).texture("layer0", "item/example_item");
            // The above line is so common that there is a shortcut for it. Note that the item registry name and the
            // texture path, relative to textures/item, must match.
//            FishanomicItems.ITEMS.getEntries().forEach((item) -> {
//                try{
//                    basicItem(item.get());
//                }
//                catch (Exception err){
//                    return;
//                }
//            });
//            basicItem(FishanomicItems.COAL_FISH.get());


        }
    }

    private static class Recipes extends RecipeProvider {
        public Recipes(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
            super(output, lookup);
        }

        @Override
        protected void buildRecipes(RecipeOutput consumer) {
//            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, FishanomicItems.STONE_HOOK.get())
//                    .pattern("  s")
//                    .pattern("c c")
//                    .pattern(" c ")
//                    .define('s', Ingredient.of(Items.STONE, Items.DEEPSLATE, Items.BLACKSTONE))
//                    .define('c', Ingredient.of(ItemTags.STONE_TOOL_MATERIALS))
//                    .unlockedBy("has_stick", has(Items.COBBLESTONE))
//                    .save(consumer);
//
//            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, FishanomicItems.STONE_FISHING_ROD.get())
//                    .pattern("  s")
//                    .pattern(" st")
//                    .pattern("sht")
//                    .define('s', Ingredient.of(Items.STICK))
//                    .define('t', Ingredient.of(Items.STRING))
//                    .define('h', Ingredient.of(FishanomicItems.STONE_HOOK))
//                    .unlockedBy("has_stick", has(Items.STONE))
//                    .save(consumer);
//
//            SmithingTransformRecipeBuilder.smithing(
//                            // The template ingredient.
//                            Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
//                            // The base ingredient.
//                            Ingredient.of(FishanomicItems.DIAMOND_FISHING_ROD),
//                            // The addition ingredient.
//                            Ingredient.of(Items.NETHERITE_INGOT),
//                            // The recipe book category.
//                            RecipeCategory.TOOLS,
//                            // The result item. Note that while the recipe codec accepts an item stack here, the builder does not.
//                            // If you need an item stack output, you need to use your own builder.
//                            FishanomicItems.NETHERITE_FISHING_ROD.asItem()
//                    )
//                    // The recipe advancement, like with the other recipes above.
//                    .unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
//                    // This overload of #save allows us to specify a name.
//                    .save(consumer, "netherite_fishing_rod_smithing");
        }

    }
}
