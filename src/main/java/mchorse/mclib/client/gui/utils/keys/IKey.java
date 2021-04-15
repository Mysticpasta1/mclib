package mchorse.mclib.client.gui.utils.keys;

import net.minecraft.util.text.ITextProperties;

public interface IKey
{
    public static final IKey EMPTY = new StringKey("");

    public static IKey lang(String key)
    {
        return new LangKey(key);
    }

    public static IKey format(String key, Object... args)
    {
        return new LangKey(key).args(args);
    }

    public static IKey str(String key)
    {
        return new StringKey(key);
    }

    public static IKey comp(IKey... keys)
    {
        return new CompoundKey(keys);
    }

    public ITextProperties get();

    public void set(String string);
}