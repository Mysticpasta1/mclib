package mchorse.mclib.client;

import mchorse.mclib.McLib;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.mclib.GuiDashboard;
import mchorse.mclib.config.values.ValueRL;
import mchorse.mclib.events.RemoveDashboardPanels;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class KeyboardHandler
{
    public KeyBinding dashboard;

    private int lastGuiScale = -1;

    public KeyboardHandler()
    {
        this.dashboard = new KeyBinding("key.mclib.dashboard", GLFW.GLFW_KEY_0, "key.mclib.category");

        ClientRegistry.registerKeyBinding(this.dashboard);
    }

    @SubscribeEvent
    public void onKeyboardInput(InputEvent.KeyInputEvent event, Screen screen)
    {
        if (this.dashboard.isPressed())
        {
            Minecraft.getInstance().displayGuiScreen(screen);

            if (GuiScreen.isCtrlKeyDown())
            {
                screen.panels.setPanel(screen.config);
            }
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event)
    {
        if (event.getGui() instanceof GuiBase)
        {
            if (this.lastGuiScale == -1)
            {
                this.lastGuiScale = Minecraft.getInstance().gameSettings.guiScale;

                int scale = McLib.userIntefaceScale.get();

                if (scale > 0)
                {
                    Minecraft.getInstance().gameSettings.guiScale = scale;
                }
            }
        }
        else
        {
            if (this.lastGuiScale != -1)
            {
                Minecraft.getInstance().gameSettings.guiScale = this.lastGuiScale;
                this.lastGuiScale = -1;
            }

            if (Minecraft.getInstance().world == null)
            {
                GuiDashboard.dashboard = null;
                ValueRL.picker = null;

                McLib.proxy.configs.resetServerValues();
                McLib.EVENT_BUS.post(new RemoveDashboardPanels());
            }
        }
    }
}