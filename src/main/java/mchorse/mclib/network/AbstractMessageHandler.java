package mchorse.mclib.network;

import com.ibm.icu.impl.SimpleFormatterImpl;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Base of all MessageHandlers.
 *
 * @author Ernio (Ernest Sadowski)
 */
public abstract class AbstractMessageHandler<T extends IByteBufSerializable> implements IMessageHandler<T, IByteBufSerializable>
{
    /**
     * Handle a message received on the client side
     *
     * @return a message to send back to the Server, or null if no reply is
     *         necessary
     */
    @OnlyIn(Dist.CLIENT)
    public abstract IByteBufSerializable handleClientMessage(final T message);

    /**
     * Handle a message received on the server side
     *
     * @return a message to send back to the Client, or null if no reply is
     *         necessary
     */
    public abstract IByteBufSerializable handleServerMessage(final ServerPlayerEntity player, final T message);

    @Override
    public IByteBufSerializable onMessage(T message, MessageContext ctx)
    {
        if (ctx.side.isClient())
        {
            return this.handleClientMessage(message);
        }
        else
        {
            return this.handleServerMessage(ctx.getServerHandler().player, message);
        }
    }
}