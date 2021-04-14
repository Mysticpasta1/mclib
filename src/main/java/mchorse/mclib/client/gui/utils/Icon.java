package mchorse.mclib.client.gui.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class Icon
{
    public final ResourceLocation location;
    public final int x;
    public final int y;
    public final int w;
    public final int h;
    public int textureW = 256;
    public int textureH = 256;

    public Icon(ResourceLocation location, int x, int y)
    {
        this(location, x, y, 16, 16);
    }

    public Icon(ResourceLocation location, int x, int y, int w, int h)
    {
        this.location = location;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public Icon(ResourceLocation location, int x, int y, int w, int h, int textureW, int textureH)
    {
        this(location, x, y, w, h);
        this.textureW = textureW;
        this.textureH = textureH;
    }

    public void render(int x, int y)
    {
        this.render(x, y, 0, 0);
    }

    public void render(int x, int y, float ax, float ay)
    {
        if (this.location == null)
        {
            return;
        }

        x -= ax * this.w;
        y -= ay * this.h;

        RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        Minecraft.getInstance().getTextureManager().bindTexture(this.location);
        GuiDraw.drawBillboard(x, y, this.x, this.y, this.w, this.h, this.textureW, this.textureH);
        RenderSystem.disableBlend();
        RenderSystem.disableAlphaTest();
    }

    public void renderArea(int x, int y, int w, int h)
    {
        RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        Minecraft.getInstance().getTextureManager().bindTexture(this.location);
        GuiDraw.drawRepeatBillboard(x, y, w, h, this.x, this.y, this.w, this.h, this.textureW, this.textureH);
        RenderSystem.disableBlend();
        RenderSystem.disableAlphaTest();
    }
}