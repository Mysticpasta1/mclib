package mchorse.mclib.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Network dispatcher
 *
 * @author Ernio (Ernest Sadowski)
 */
public abstract class AbstractDispatcher
{
    private final SimpleChannel dispatcher;
    private byte nextPacketID;

    public AbstractDispatcher(ResourceLocation resourceLocation, String networkProtocolVersion, Predicate<String> clientAcceptedVersions, Predicate<String> serverAcceptedVersions)
    {
        this.dispatcher = NetworkRegistry.newSimpleChannel(resourceLocation, () -> networkProtocolVersion, clientAcceptedVersions, serverAcceptedVersions);
    }

    public SimpleChannel get()
    {
        return this.dispatcher;
    }

    /**
     * Here you supposed to register packets to handlers 
     */
    public abstract void register();

    /**
     * Send message to players who are tracking given entity
     */
    public void sendToTracked(Entity entity, IByteBufSerializable message)
    {
        EntityTracker tracker = ((ServerWorld) entity.world).getEntityTracker();

        for (PlayerEntity player : tracker.getTrackingPlayers(entity))
        {
            this.dispatcher.sendTo(message, (ServerPlayerEntity) player);
        }
    }

    /**
     * Send message to given player
     */
    public void sendTo(IByteBufSerializable message, ServerPlayerEntity player)
    {
        this.dispatcher.sendTo(message, player);
    }

    /**
     * Send message to the server
     */
    public void sendToServer(IByteBufSerializable message)
    {
        this.dispatcher.sendToServer(message);
    }

    /**
     * Register given message with given message handler on a given side
     */
    public <REQ extends IByteBufSerializable, REPLY extends IByteBufSerializable> void register(Class<REQ> message, Class<? extends IMessageHandler<REQ, REPLY>> handler, Dist side)
    {
        this.dispatcher.registerMessage(handler, message, this.nextPacketID++, side);
    }
}