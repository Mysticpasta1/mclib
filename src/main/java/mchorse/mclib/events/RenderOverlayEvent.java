package mchorse.mclib.events;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.awt.*;

@Cancelable
@OnlyIn(Dist.CLIENT)
public abstract class RenderOverlayEvent extends Event
{
    public final Minecraft mc;
    public final MainWindow resolution;

    public RenderOverlayEvent(Minecraft mc, MainWindow resolution)
    {
        this.mc = mc;
        this.resolution = resolution;
    }

    public static class Pre extends RenderOverlayEvent
    {
        public Pre(Minecraft mc, MainWindow resolution)
        {
            super(mc, resolution);
        }
    }

    public static class Post extends RenderOverlayEvent
    {
        public Post(Minecraft mc, MainWindow resolution)
        {
            super(mc, resolution);
        }
    }
}