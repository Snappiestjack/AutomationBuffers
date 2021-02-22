package com.snappiestjack.automationbuffers;

import com.snappiestjack.automationbuffers.setup.ClientSetup;
import com.snappiestjack.automationbuffers.setup.Config;
import com.snappiestjack.automationbuffers.setup.ModSetup;
import com.snappiestjack.automationbuffers.setup.Registration;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("automationbuffers")
public class AutomationBuffers
{
    public static final String MODID = "automationbuffers";

    public AutomationBuffers() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);

        Registration.init();

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
    }
}
