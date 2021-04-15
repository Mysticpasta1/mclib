package mchorse.mclib.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * This class passes operation from Netty to Minecraft (Client) Thread. Also
 * prevents the server-side message handling method from appearing in client
 * message handler classes.
 *
 * @author Ernio (Ernest Sadowski)
 */
public abstract class ClientMessageHandler<T extends IByteBufSerializable> extends AbstractMessageHandler<T>
{
    @OnlyIn(Dist.CLIENT)
    public abstract void run(final PlayerEntity player, final T message);

    @Override
    @OnlyIn(Dist.CLIENT)
    public IByteBufSerializable handleClientMessage(final T message)
    {
        Minecraft.getInstance().enqueue(() -> ClientMessageHandler.this.run(Minecraft.getInstance().player, message));

        return null;
    }

    @Override
    public final IByteBufSerializable handleServerMessage(final ServerPlayerEntity player, final T message)
    {
        return null;
    }
}