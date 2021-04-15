package mchorse.mclib.network.mclib.server;

import mchorse.mclib.McLib;
import mchorse.mclib.config.Config;
import mchorse.mclib.config.ConfigManager;
import mchorse.mclib.network.ServerMessageHandler;
import mchorse.mclib.network.mclib.Dispatcher;
import mchorse.mclib.network.mclib.common.PacketConfig;
import mchorse.mclib.network.mclib.common.PacketRequestConfigs;
import mchorse.mclib.utils.OpHelper;
import net.minecraft.entity.player.ServerPlayerEntity;

public class ServerHandlerRequestConfigs extends ServerMessageHandler<PacketRequestConfigs>
{
    @Override
    public void run(ServerPlayerEntity player, PacketRequestConfigs message)
    {
        if (!OpHelper.isPlayerOp(player))
        {
            return;
        }

        ConfigManager manager = McLib.proxy.configs;

        for (Config config : manager.modules.values())
        {
            Config serverSide = config.filterServerDist();

            if (!serverSide.values.isEmpty())
            {
                Dispatcher.sendTo(new PacketConfig(serverSide), player);
            }
        }
    }
}