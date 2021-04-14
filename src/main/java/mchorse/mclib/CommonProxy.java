package mchorse.mclib;

import mchorse.mclib.config.ConfigHandler;
import mchorse.mclib.config.ConfigManager;
import mchorse.mclib.network.mclib.Dispatcher;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class CommonProxy
{

    public ConfigManager configs = new ConfigManager();
    public static Path configFolder;

    @SubscribeEvent
    public static void serverLoad(FMLServerStartingEvent event)
    {
        new CommonProxy();
        configFolder = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath());

        Dispatcher.register();

        MinecraftForge.EVENT_BUS.register(new ConfigHandler());
    }

    @SubscribeEvent
    public static void init(final FMLCommonSetupEvent event)
    {

    }
}