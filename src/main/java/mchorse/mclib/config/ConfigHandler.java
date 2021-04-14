package mchorse.mclib.config;

import mchorse.mclib.McLib;
import mchorse.mclib.network.mclib.Dispatcher;
import mchorse.mclib.network.mclib.common.PacketConfig;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ConfigHandler
{
    @SubscribeEvent
    public void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        MinecraftServer server = event.getPlayer().getServer();

        if (server == null || server.isSinglePlayer() || !(event.getPlayer() instanceof ServerPlayerEntity))
        {
            return;
        }

        for (Config config : McLib.proxy.configs.modules.values())
        {
            if (config.hasSyncable())
            {
                Dispatcher.sendTo(new PacketConfig(config.filterSyncable(), true), (ServerPlayerEntity) event.getPlayer());
            }
        }
    }
}
