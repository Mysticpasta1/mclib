package mchorse.mclib.core;

import net.minecraftforge.fml.ModContainer;

public class McLibCMInfo
{
    public String getName()
    {
        return "McLib core mod";
    }

    public String getModId()
    {
        return "mclib_core";
    }

    public Object getMod()
    {
        return null;
    }

    public String getVersion()
    {
        return "%VERSION%";
    }
}