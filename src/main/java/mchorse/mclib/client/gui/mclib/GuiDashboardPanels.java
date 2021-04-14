package mchorse.mclib.client.gui.mclib;

import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiPanelBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.Icon;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.Direction;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class GuiDashboardPanels extends GuiPanelBase<GuiDashboardPanel>
{
    public GuiDashboardPanels(Minecraft mc)
    {
        super(mc, Direction.LEFT);
    }

    public void open()
    {
        for (GuiDashboardPanel panel : this.panels)
        {
            panel.open();
        }
    }

    public void close()
    {
        for (GuiDashboardPanel panel : this.panels)
        {
            panel.close();
        }
    }

    @Override
    public void setPanel(GuiDashboardPanel panel)
    {
        if (this.view.delegate != null)
        {
            this.view.delegate.disappear();
        }

        super.setPanel(panel);

        if (this.view.delegate != null)
        {
            this.view.delegate.appear();
        }
    }

    @Override
    public GuiIconElement registerPanel(GuiDashboardPanel panel, IKey tooltip, Icon icon)
    {
        GuiIconElement element = super.registerPanel(panel, tooltip, icon);

        int key = this.getKeybind();

        if (key != -1)
        {
            element.keys()
                .register(IKey.comp(IKey.lang("mclib.gui.dashboard.open_panel"), tooltip), key, () -> element.clickItself(GuiBase.getCurrent()))
                .category(IKey.lang("mclib.gui.dashboard.category"));
        }

        return element;
    }

    protected int getKeybind()
    {
        int size = this.panels.size();

        switch (size)
        {
            case 1: return GLFW.GLFW_KEY_KP_0;
            case 2: return GLFW.GLFW_KEY_KP_1;
            case 3: return GLFW.GLFW_KEY_KP_2;
            case 4: return GLFW.GLFW_KEY_KP_3;
            case 5: return GLFW.GLFW_KEY_KP_4;
            case 6: return GLFW.GLFW_KEY_KP_5;
            case 7: return GLFW.GLFW_KEY_KP_6;
            case 8: return GLFW.GLFW_KEY_KP_7;
            case 9: return GLFW.GLFW_KEY_KP_8;
            case 10: return GLFW.GLFW_KEY_KP_9;
        }

        return -1;
    }

    @Override
    protected void drawBackground(GuiContext context, int x, int y, int w, int h)
    {
        Gui.drawRect(x, y, x + w, y + h, 0xff111111);
    }
}