package mchorse.mclib.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.INetHandler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

public class ReflectionUtils
{
    /**
     * Minecraft texture manager's field to the texture map (a map of 
     * {@link ITextComponent} which is used to cache references to
     * OpenGL textures). 
     */
    public static Field TEXTURE_MAP;

    /**
     * Get texture map from texture manager using reflection API
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<ResourceLocation, ITextComponent> getTextures(TextureManager manager)
    {
        if (TEXTURE_MAP == null)
        {
            setupTextureMapField(manager);
        }

        try
        {
            return (Map<ResourceLocation, ITextComponent>) TEXTURE_MAP.get(manager);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Setup texture map field which is looked up using the reflection API
     */
    @SuppressWarnings("rawtypes")
    public static void setupTextureMapField(TextureManager manager)
    {
        /* Finding the field which has holds the texture cache */
        for (Field field : manager.getClass().getDeclaredFields())
        {
            if (Modifier.isStatic(field.getModifiers()))
            {
                continue;
            }

            field.setAccessible(true);

            try
            {
                Object value = field.get(manager);

                if (value instanceof Map && ((Map) value).keySet().iterator().next() instanceof ResourceLocation)
                {
                    TEXTURE_MAP = field;

                    break;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean registerResourcePack(IResourcePack pack)
    {
        try
        {
            Field field = INetHandler.class.getDeclaredField("resourcePackList");
            field.setAccessible(true);

            List<IResourcePack> packs = (List<IResourcePack>) field.get(INetHandler.class);
            packs.add(pack);
            IResourceManager manager = Minecraft.getInstance().getResourceManager();

            if (manager instanceof SimpleReloadableResourceManager)
            {
                ((SimpleReloadableResourceManager) manager).addResourcePack(pack);
            }

            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}