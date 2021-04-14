package mchorse.mclib.config.values;

import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.config.gui.GuiConfigPanel;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public interface IConfigGuiProvider
{
    @OnlyIn(Dist.CLIENT)
    public List<GuiElement> getFields(Minecraft mc, GuiConfigPanel gui);
}