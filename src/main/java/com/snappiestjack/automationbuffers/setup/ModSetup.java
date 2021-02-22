package com.snappiestjack.automationbuffers.setup;

import com.snappiestjack.automationbuffers.AutomationBuffers;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = AutomationBuffers.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static final ItemGroup ITEM_GROUP = new ItemGroup("automationbuffers") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Registration.MULTIBUFFER.get());
        }
    };

    public static void init(final FMLCommonSetupEvent event) {
    }

}