package mchorse.mclib.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityUtils
{
    @OnlyIn(Dist.CLIENT)
    public static GameType getGameMode()
    {
        return getGameMode(Minecraft.getInstance().player);
    }

    @OnlyIn(Dist.CLIENT)
    public static GameType getGameMode(PlayerEntity player)
    {
        NetworkPlayerInfo networkplayerinfo = EntityUtils.getNetworkInfo(player);

        return networkplayerinfo == null ? GameType.SURVIVAL : networkplayerinfo.getGameType();
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean isAdventureMode(PlayerEntity player)
    {
        NetworkPlayerInfo info = getNetworkInfo(player);

        return info != null && info.getGameType() == GameType.ADVENTURE;
    }

    @OnlyIn(Dist.CLIENT)
    public static NetworkPlayerInfo getNetworkInfo(PlayerEntity player)
    {
        ClientPlayNetHandler connection = Minecraft.getInstance().getConnection();

        if (connection == null)
        {
            return null;
        }

        return (connection.getPlayerInfo(player.getGameProfile().getId()));
    }
}
