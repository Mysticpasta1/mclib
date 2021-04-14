package mchorse.mclib.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Render brightness
 * 
 * This class is a workaround class which allows using lightmap methods 
 * without having to resort to straight copy-pasting the code.
 */
@OnlyIn(Dist.CLIENT)
public class RenderLightmap extends LivingRenderer
{
    /**
     * Private instance 
     */
    private static RenderLightmap instance;

    public static RenderLightmap getInstance()
    {
        if (instance == null)
        {
            instance = new RenderLightmap(Minecraft.getInstance().getRenderManager(), null, 0);
        }

        return instance;
    }

    public static boolean canRenderNamePlate(LivingEntity entity)
    {
        return getInstance().canRenderName(entity);
    }

    public static boolean set(LivingEntity entity, float partialTicks)
    {
        return getInstance().setBrightness(entity, partialTicks, true);
    }

    public static void unset()
    {
        getInstance().unsetBrightness();
    }

    public RenderLightmap(EntityRendererManager renderManagerIn, EntityModel<? extends Entity> modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    protected int getColorMultiplier(Entity entitylivingbaseIn, float lightBrightness, float partialTickTime)
    {
        return 0;
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity)
    {
        return null;
    }
}