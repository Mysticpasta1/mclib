package mchorse.mclib.core;

import java.util.Map;

public class McLibCM
{
    public String[] getASMTransformerClass()
    {
        return new String[] {McLibCMClassTransformer.class.getName()};
    }

    public String getModContainerClass()
    {
        return McLibCMInfo.class.getName();
    }

    public String getSetupClass()
    {
        return null;
    }

    public void injectData(Map<String, Object> data)
    {}

    public String getAccessTransformerClass()
    {
        return null;
    }
}