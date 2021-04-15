package mchorse.mclib.network.mclib;

import mchorse.mclib.McLib;
import mchorse.mclib.network.AbstractDispatcher;
import mchorse.mclib.network.IByteBufSerializable;
import mchorse.mclib.network.mclib.client.ClientHandlerConfig;
import mchorse.mclib.network.mclib.common.PacketConfig;
import mchorse.mclib.network.mclib.common.PacketDropItem;
import mchorse.mclib.network.mclib.common.PacketRequestConfigs;
import mchorse.mclib.network.mclib.server.ServerHandlerConfig;
import mchorse.mclib.network.mclib.server.ServerHandlerDropItem;
import mchorse.mclib.network.mclib.server.ServerHandlerRequestConfigs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;

public class Dispatcher
{
    public static final AbstractDispatcher DISPATCHER = new AbstractDispatcher(McLib.MOD_ID)
    {
        @Override
        public void register()
        {
            register(PacketDropItem.class, ServerHandlerDropItem.class, Dist.DEDICATED_SERVER);

            /* Config related packets */
            register(PacketRequestConfigs.class, ServerHandlerRequestConfigs.class, Dist.DEDICATED_SERVER);
            register(PacketConfig.class, ServerHandlerConfig.class, Dist.DEDICATED_SERVER);
            register(PacketConfig.class, ClientHandlerConfig.class, Dist.CLIENT);
        }
    };

    /**
     * Send message to players who are tracking given entity
     */
    public static void sendToTracked(Entity entity, IByteBufSerializable message)
    {
        DISPATCHER.sendToTracked(entity, message);
    }

    /**
     * Send message to given player
     */
    public static void sendTo(IByteBufSerializable message, ServerPlayerEntity player)
    {
        DISPATCHER.sendTo(message, player);
    }

    /**
     * Send message to the server
     */
    public static void sendToServer(IByteBufSerializable message)
    {
        DISPATCHER.sendToServer(message);
    }

    /**
     * Register all the networking messages and message handlers
     */
    public static void register()
    {
        DISPATCHER.register();
    }
}