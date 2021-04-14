package mchorse.mclib.client.gui.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.net.URI;

/**
 * GUI utilities
 */
@OnlyIn(Dist.CLIENT)
public class GuiUtils
{
    public static void drawModel(EntityModel model, PlayerEntity player, int x, int y, float scale)
    {
        drawModel(model, player, x, y, scale, 1.0F);
    }

    /**
     * Draw a {@link EntityModel} without using the {@link EntityRendererManager} (which
     * adds a lot of useless transformations and stuff to the screen rendering).
     */
    public static void drawModel(EntityModel model, PlayerEntity player, int x, int y, float scale, float alpha)
    {
        float factor = 0.0625F;

        RenderSystem.enableColorMaterial();
        RenderSystem.pushMatrix();
        RenderSystem.translatef(x, y, 50.0F);
        RenderSystem.scalef((-scale), scale, scale);
        RenderSystem.rotatef(45.0F, -1.0F, 0.0F, 0.0F);
        RenderSystem.rotatef(45.0F, 0.0F, -1.0F, 0.0F);
        RenderSystem.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
        RenderSystem.rotatef(180.0F, 0.0F, 1.0F, 0.0F);

        RenderHelper.enableStandardItemLighting();

        RenderSystem.pushMatrix();
        RenderSystem.disableCull();

        RenderSystem.enableRescaleNormal();
        RenderSystem.scalef(-1.0F, -1.0F, 1.0F);
        RenderSystem.translatef(0.0F, -1.501F, 0.0F);

        RenderSystem.enableAlphaTest();

        model.setLivingAnimations(player, 0, 0, 0);
        model.setRotationAngles(0, 0, player.ticksExisted, 0, 0, factor, player);

        RenderSystem.enableDepthTest();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);

        model.render(player, 0, 0, 0, 0, 0, factor);

        RenderSystem.disableDepthTest();

        RenderSystem.disableRescaleNormal();
        RenderSystem.disableAlphaTest();
        RenderSystem.popMatrix();

        RenderSystem.popMatrix();

        RenderHelper.disableStandardItemLighting();

        RenderSystem.disableRescaleNormal();
        RenderSystem.activeTexture(OpenGLHelper.lightmapTexUnit);
        RenderSystem.disableTexture();
        RenderSystem.activeTexture(OpenGlHelper.defaultTexUnit);
    }

    /**
     * Draw an entity on the screen.
     *
     * Taken <s>stolen</s> from minecraft's class GuiInventory. I wonder what's
     * the license of minecraft's decompiled code?
     * @param alpha 
     */
    public static void drawEntityOnScreen(int posX, int posY, float scale, LivingEntity ent, float alpha)
    {
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.enableColorMaterial();
        RenderSystem.pushMatrix();
        RenderSystem.translatef(posX, posY, 100.0F);
        RenderSystem.scalef((-scale), scale, scale);
        RenderSystem.rotatef(45.0F, -1.0F, 0.0F, 0.0F);
        RenderSystem.rotatef(45.0F, 0.0F, -1.0F, 0.0F);
        RenderSystem.rotatef(180.0F, 0.0F, 0.0F, 1.0F);

        boolean render = ent.getAlwaysRenderNameTagForRender();

        if (ent instanceof EnderDragonEntity)
        {
            RenderSystem.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
        }

        RenderHelper.enableStandardItemLighting();

        GlStateManager.enableRescaleNormal();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, alpha);

        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;

        ent.renderYawOffset = 0;
        ent.rotationYaw = 0;
        ent.rotationPitch = 0;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        ent.setAlwaysRenderNameTag(false);

        RenderSystem.translatef(0.0F, 0.0F, 0.0F);

        EntityRendererManager rendermanager = Minecraft.getInstance().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityStatic(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);

        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;

        ent.setAlwaysRenderNameTag(render);

        GlStateManager.popMatrix();

        RenderHelper.disableStandardItemLighting();

        RenderSystem.disableRescaleNormal();

        RenderSystem.disableBlend();
        RenderSystem.activeTexture(OpenGlHelper.lightmapTexUnit);
        RenderSystem.disableTexture();
        RenderSystem.activeTexture(OpenGlHelper.defaultTexUnit);
        RenderSystem.disableDepthTest();
    }

    /**
     * Draw an entity on the screen.
     *
     * Taken <s>stolen</s> from minecraft's class GuiInventory. I wonder what's
     * the license of minecraft's decompiled code?
     */
    public static void drawEntityOnScreen(int posX, int posY, int scale, int mouseX, int mouseY, LivingEntity ent)
    {
        RenderSystem.enableColorMaterial();
        RenderSystem.pushMatrix();
        RenderSystem.translatef(posX, posY, 100.0F);
        RenderSystem.scalef((-scale), scale, scale);
        RenderSystem.rotatef(180.0F, 0.0F, 0.0F, 1.0F);

        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;

        ent.renderYawOffset = (float) Math.atan(mouseX / 40.0F) * 20.0F;
        ent.rotationYaw = (float) Math.atan(mouseX / 40.0F) * 40.0F;
        ent.rotationPitch = -((float) Math.atan(mouseY / 40.0F)) * 20.0F;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;

        RenderSystem.translatef(0.0F, 0.0F, 0.0F);

        EntityRendererManager rendermanager = Minecraft.getInstance().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityStatic(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);

        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;

        RenderSystem.popMatrix();
        RenderHelper.disableStandardItemLighting();
        RenderSystem.disableRescaleNormal();
        RenderSystem.activeTexture(OpenGlHelper.lightmapTexUnit);
        RenderSystem.disableTexture();
        RenderSystem.activeTexture(OpenGlHelper.defaultTexUnit);
    }


    /**
     * Open web link
     */
    public static void openWebLink(String address)
    {
        try
        {
            openWebLink(new URI(address));
        }
        catch (Exception e)
        {}
    }

    /**
     * Open a URL
     */
    public static void openWebLink(URI uri)
    {
        try
        {
            Class<?> clazz = Class.forName("java.awt.Desktop");
            Object object = clazz.getMethod("getDesktop", new Class[0]).invoke(null);

            clazz.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {uri});
        }
        catch (Throwable t)
        {}
    }

    public static void playClick()
    {
        Minecraft.getInstance().getSoundHandler().play(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}