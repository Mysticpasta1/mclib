package mchorse.mclib.client.gui.framework.elements.list;

import net.minecraft.client.Minecraft;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;


public class GuiStringListElement extends GuiListElement<String>
{
    public GuiStringListElement(Minecraft mc, Consumer<List<String>> callback)
    {
        super(mc, callback);

        this.scroll.scrollItemSize = 16;
    }

    @Override
    protected boolean sortElements()
    {
        Collections.sort(this.list);

        return true;
    }

    @Override
    protected String elementToString(String element)
    {
        return element;
    }
}