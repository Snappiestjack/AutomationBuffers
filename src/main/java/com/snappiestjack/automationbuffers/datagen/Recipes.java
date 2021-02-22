package com.snappiestjack.automationbuffers.datagen;

import com.snappiestjack.automationbuffers.setup.Registration;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraftforge.common.Tags;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(Registration.MULTIBUFFER.get())
                .patternLine("sbs")
                .patternLine("i#i")
                .patternLine("iii")
                .key('s', Items.SMOOTH_STONE)
                .key('i', Tags.Items.INGOTS_IRON)
                .key('b', Items.BARREL )
                .key('#', Items.BUCKET)
                .setGroup("automationbuffers")
                .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
                .build(consumer);
    }
}
