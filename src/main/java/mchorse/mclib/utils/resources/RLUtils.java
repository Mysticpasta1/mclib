package mchorse.mclib.utils.resources;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import mchorse.mclib.McLib;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.*;
import net.minecraft.resources.IResource;
import net.minecraft.resources.SimpleResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link ResourceLocation} utility methods
 *
 * This class has utils for saving and reading {@link ResourceLocation} from
 * actor model and skin.
 */
public class RLUtils
{
    private static List<IResourceTransformer> transformers = new ArrayList<IResourceTransformer>();
    private static ResourceLocation pixel = new ResourceLocation("mclib:textures/pixel.png");

    /**
     * Get stream for multi resource location 
     */
    @OnlyIn(Dist.CLIENT)
    public static IResource getStreamForMultiskin(MultiResourceLocation multi, InputStream inputStream) throws IOException
    {
        if (multi.children.isEmpty())
        {
            throw new IOException("Multi-skin is empty!");
        }

        try
        {
            if (McLib.multiskinMultiThreaded.get())
            {
                MultiskinThread.add(multi);

                return Minecraft.getInstance().getResourceManager().getResource(pixel);
            }
            else
            {
                MultiskinThread.clear();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ImageIO.write(TextureProcessor.postProcess(multi), "png", stream);

                return new SimpleResource("McLib multiskin handler", multi.clone(), inputStream , inputStream);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new IOException(e);
        }
    }

    public static void register(IResourceTransformer transformer)
    {
        transformers.add(transformer);
    }

    public static ResourceLocation create(String path)
    {
        for (IResourceTransformer transformer : transformers)
        {
            path = transformer.transform(path);
        }

        return new TextureLocation(path);
    }

    public static ResourceLocation create(String domain, String path)
    {
        for (IResourceTransformer transformer : transformers)
        {
            String newDomain = transformer.transformDomain(domain, path);
            String newPath = transformer.transformPath(domain, path);

            domain = newDomain;
            path = newPath;
        }

        return new TextureLocation(domain, path);
    }

    public static ResourceLocation create(INBTType base)
    {
        ResourceLocation location = MultiResourceLocation.from(base);

        if (location != null)
        {
            return location;
        }

        if (base instanceof StringNBT)
        {
            return create(((StringNBT) base).getString());
        }

        return null;
    }

    public static ResourceLocation create(JsonElement element)
    {
        ResourceLocation location = MultiResourceLocation.from(element);

        if (location != null)
        {
            return location;
        }

        if (element.isJsonPrimitive())
        {
            return create(element.getAsString());
        }

        return null;
    }

    public static INBTType writeNbt(ResourceLocation location, StringNBT stringNBT)
    {
        if (location instanceof IWritableLocation)
        {
            return ((IWritableLocation) location).writeNbt();
        }
        else if (location != null)
        {
            return (INBTType) stringNBT;
        }

        return null;
    }

    public static JsonElement writeJson(ResourceLocation location)
    {
        if (location instanceof IWritableLocation)
        {
            return ((IWritableLocation) location).writeJson();
        }
        else if (location != null)
        {
            return new JsonPrimitive(location.toString());
        }

        return JsonNull.INSTANCE;
    }

    public static ResourceLocation clone(ResourceLocation location)
    {
        if (location instanceof IWritableLocation)
        {
            return ((IWritableLocation) location).clone();
        }
        else if (location != null)
        {
            return create(location.toString());
        }

        return null;
    }
}