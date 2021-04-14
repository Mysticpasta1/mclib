package mchorse.mclib.config.values;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.config.gui.GuiConfigPanel;
import mchorse.mclib.utils.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.List;

public class ValueDouble extends Value implements IServerValue, IConfigGuiProvider
{
    private double value;
    private double defaultValue;
    private double min = Double.NEGATIVE_INFINITY;
    private double max = Double.POSITIVE_INFINITY;

    private Double serverValue;

    public ValueDouble(String id)
    {
        super(id);
    }

    public ValueDouble(String id, double defaultValue)
    {
        super(id);

        this.defaultValue = defaultValue;

        this.reset();
    }

    public ValueDouble(String id, double defaultValue, double min, double max)
    {
        super(id);

        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;

        this.reset();
    }

    public double getMin()
    {
        return this.min;
    }

    public double getMax()
    {
        return this.max;
    }

    public double get()
    {
        return this.serverValue == null ? this.value : this.serverValue;
    }

    public void set(double value)
    {
        this.value = MathUtils.clamp(value, this.min, this.max);
        this.saveLater();
    }

    @Override
    public Object getValue()
    {
        return this.get();
    }

    @Override
    public void setValue(Object value)
    {
        if (value instanceof Double)
        {
            this.set((Double) value);
        }
    }

    @Override
    public void reset()
    {
        this.set(this.defaultValue);
    }

    @Override
    public void resetServer()
    {
        this.serverValue = null;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public List<GuiElement> getFields(Minecraft mc, GuiConfigPanel gui)
    {
        GuiElement element = new GuiElement(mc);
        GuiLabel label = Elements.label(IKey.lang(this.getLabelKey()), 0).anchor(0, 0.5F);
        GuiTrackpadElement trackpad = new GuiTrackpadElement(mc, this);

        trackpad.flex().w(90);

        element.flex().row(0).preferred(0).height(20);
        element.add(label, trackpad.removeTooltip());

        return Arrays.asList(element.tooltip(IKey.lang(this.getCommentKey())));
    }

    @Override
    public void valueFromJSON(JsonElement element)
    {
        this.set(element.getAsDouble());
    }

    @Override
    public JsonElement valueToJSON()
    {
        return new JsonPrimitive(this.value);
    }

    @Override
    public boolean parseFromCommand(String value)
    {
        try
        {
            this.set(Double.parseDouble(value));
        }
        catch (Exception e)
        {}

        return false;
    }

    @Override
    public void copy(Value value)
    {
        if (value instanceof ValueDouble)
        {
            this.value = ((ValueDouble) value).value;
        }
    }

    @Override
    public void copyServer(Value value)
    {
        if (value instanceof ValueDouble)
        {
            this.serverValue = ((ValueDouble) value).value;
        }
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        super.fromBytes(buffer);

        this.value = buffer.readDouble();
        this.defaultValue = buffer.readDouble();
        this.min = buffer.readDouble();
        this.max = buffer.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        super.toBytes(buffer);

        buffer.writeDouble(this.value);
        buffer.writeDouble(this.defaultValue);
        buffer.writeDouble(this.min);
        buffer.writeDouble(this.max);
    }

    @Override
    public String toString()
    {
        return Double.toString(this.value);
    }
}