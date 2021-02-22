package com.snappiestjack.automationbuffers.datagen;

import com.snappiestjack.automationbuffers.setup.Registration;
import net.minecraft.data.DataGenerator;

public class LootTables extends BaseLootTableProvider {

    public LootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        lootTables.put(Registration.MULTIBUFFER.get(), createStandardTable("multibuffer", Registration.MULTIBUFFER.get()));
    }
}