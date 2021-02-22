package com.snappiestjack.automationbuffers.datagen;

import com.snappiestjack.automationbuffers.AutomationBuffers;
import com.snappiestjack.automationbuffers.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class Items extends ItemModelProvider {

    public Items(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, AutomationBuffers.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(Registration.MULTIBUFFER_ITEM.get().getRegistryName().getPath(), new ResourceLocation(AutomationBuffers.MODID, "block/multibuffer"));
    }
}