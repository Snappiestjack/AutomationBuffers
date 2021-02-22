package com.snappiestjack.automationbuffers.setup;

import com.snappiestjack.automationbuffers.AutomationBuffers;
import com.snappiestjack.automationbuffers.blocks.multibuffer.MultiBufferScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = AutomationBuffers.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(Registration.MULTIBUFFER_CONTAINER.get(), MultiBufferScreen::new);
    }
}