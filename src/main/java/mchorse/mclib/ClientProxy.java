package mchorse.mclib;

import mchorse.mclib.client.InputRenderer;
import mchorse.mclib.client.KeyboardHandler;
import mchorse.mclib.client.gui.utils.keys.LangKey;
import mchorse.mclib.utils.ReflectionUtils;
import mchorse.mclib.utils.resources.MultiResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy
{
    @SubscribeEvent
    public void doClientStuff(FMLClientSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new KeyboardHandler());
        MinecraftForge.EVENT_BUS.register(new InputRenderer());
    }
    
    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event)
    {


        Minecraft mc = Minecraft.getInstance();

        /* OMG, thank you very much Forge! */
        if (!mc.getFramebuffer().isStencilEnabled())
        {
            mc.getFramebuffer().enableStencil();
        }

        ((IReloadableResourceManager) mc.getResourceManager()).addReloadListener((manager) ->
        {
            LangKey.lastTime = System.currentTimeMillis();

            if (McLib.multiskinClear.get())
            {
                new ClientProxy();
                mc.enqueue(clearMultiTextures());
            }

            return mc.runAsync(() -> {
                ???
            });
        });


    }

    private static Runnable clearMultiTextures()
    {
        return () -> {
            Minecraft mc = Minecraft.getInstance();
            Map<ResourceLocation, ITextComponent> map = ReflectionUtils.getTextures(mc.getTextureManager());
            List<ResourceLocation> toClear = new ArrayList<ResourceLocation>();

            assert map != null;
            for (ResourceLocation location : map.keySet()) {
                if (location instanceof MultiResourceLocation) {
                    toClear.add(location);
                }
            }

            for (ResourceLocation location : toClear) {
                mc.getTextureManager().deleteTexture(location);
            }
        };
    }
}