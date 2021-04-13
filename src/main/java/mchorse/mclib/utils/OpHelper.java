package mchorse.mclib.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.OpEntry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


public class OpHelper
{
    /**
     * Minimum OP level according to vanilla code
     */
    public static final int VANILLA_OP_LEVEL = 2;

    @OnlyIn(Dist.CLIENT)
    public static int getPlayerOpLevel()
    {
        assert Minecraft.getInstance().player != null;
        return Minecraft.getInstance().player.getPermissionLevel();
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean isPlayerOp()
    {
        return isOp(getPlayerOpLevel());
    }

    public static boolean isPlayerOp(ServerPlayerEntity player)
    {
        MinecraftServer server = player.server;

        if (server.getPlayerList().canSendCommands(player.getGameProfile()))
        {
            OpEntry userEntry = server.getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());

            if (userEntry != null)
            {
                return userEntry.getPermissionLevel() >= VANILLA_OP_LEVEL;
            }
            else
            {
                return server.getOpPermissionLevel() >= VANILLA_OP_LEVEL;
            }
        }

        return false;
    }

    public static boolean isOp(int opLevel)
    {
        return opLevel >= VANILLA_OP_LEVEL;
    }
}
