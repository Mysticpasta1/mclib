package mchorse.mclib.network.mclib.client;

import mchorse.mclib.McLib;
import mchorse.mclib.client.gui.mclib.GuiDashboard;
import mchorse.mclib.config.Config;
import mchorse.mclib.config.gui.GuiConfigPanel;
import mchorse.mclib.network.ClientMessageHandler;
import mchorse.mclib.network.mclib.common.PacketConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ClientHandlerConfig extends ClientMessageHandler<PacketConfig>
{
    @Override
    @OnlyIn(Dist.CLIENT)
    public void run(PlayerEntity player, PacketConfig message)
    {
        if (message.overwrite)
        {
            Config present = McLib.proxy.configs.modules.get(message.config.id);

            present.copyServer(message.config);
        }
        else
        {
            Screen screen = Minecraft.getInstance().currentScreen;

            if (screen instanceof GuiDashboard)
            {
                GuiConfigPanel panel = ((GuiDashboard) screen).config;

                panel.storeServerConfig(message.config);
            }
        }
    }
}
