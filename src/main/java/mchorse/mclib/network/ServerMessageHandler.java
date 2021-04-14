package mchorse.mclib.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.Objects;

/**
 * This class passes operation from Netty to Minecraft (Server) Thread. This
 * class will prevent the client-side message handling method from appearing in
 * server message handler classes.
 *
 * @author Ernio (Ernest Sadowski)
 */
public abstract class ServerMessageHandler<T extends IMessage> extends AbstractMessageHandler<T>
{
    public abstract void run(final ServerPlayerEntity player, final T message);

    @Override
    public IMessage handleServerMessage(final ServerPlayerEntity player, final T message)
    {
        Objects.requireNonNull(player.getServer()).runAsync(new Runnable()
        {
            @Override
            public void run()
            {
                ServerMessageHandler.this.run(player, message);
            }
        });

        return null;
    }

    @Override
    public final IMessage handleClientMessage(final T message)
    {
        return null;
    }

    /**
     * Safe way to get a tile entity on the server without exposing code 
     * to ACG (Arbitrary Chunk Generation) exploit (thanks to Paul Fulham)
     */
    protected TileEntity getTE(ServerPlayerEntity player, BlockPos pos)
    {
        World world = player.getEntityWorld();

        if (world.isBlockLoaded(pos))
        {
            return world.getTileEntity(pos);
        }

        return null;
    }
}