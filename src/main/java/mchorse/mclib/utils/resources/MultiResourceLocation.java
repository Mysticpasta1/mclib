package mchorse.mclib.utils.resources;

import com.google.common.base.Objects;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.INBTType;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Multiple resource location class
 * 
 * This bad boy allows constructing a single texture out of several 
 * {@link ResourceLocation}s. It doesn't really make sense for other 
 * types of resources beside pictures.
 */
public class MultiResourceLocation extends ResourceLocation implements IWritableLocation
{
    public List<FilteredResourceLocation> children = new ArrayList<FilteredResourceLocation>();

    private int id = -1;

    public static MultiResourceLocation from(INBTType nbt)
    {
        ListNBT list = nbt instanceof ListNBT ? (ListNBT) nbt : null;

        if (list == null || list.isEmpty())
        {
            return null;
        }

        MultiResourceLocation multi = new MultiResourceLocation();

        try
        {
            multi.fromNbt(nbt);

            return multi;
        }
        catch (Exception e)
        {}

        return null;
    }

    public static MultiResourceLocation from(JsonElement element)
    {
        JsonArray list = element.isJsonArray() ? (JsonArray) element : null;

        if (list == null || list.size() == 0)
        {
            return null;
        }

        MultiResourceLocation multi = new MultiResourceLocation();

        try
        {
            multi.fromJson(element);

            return multi;
        }
        catch (Exception e)
        {}

        return null;
    }

    public MultiResourceLocation(String resourceName)
    {
        this();
        this.children.add(new FilteredResourceLocation(RLUtils.create(resourceName)));
    }

    public MultiResourceLocation(String resourceDomainIn, String resourcePathIn)
    {
        this();
        this.children.add(new FilteredResourceLocation(RLUtils.create(resourceDomainIn, resourcePathIn)));
    }

    public MultiResourceLocation()
    {
        /* This needed so there would less chances to match with an
         * actual ResourceLocation */
        super("it_would_be_very_ironic", "if_this_would_match_with_regular_rls");
    }

    public void recalculateId()
    {
        this.id = MultiResourceLocationManager.getId(this);
    }

    public String getResourceDomain()
    {
        return this.children.isEmpty() ? "" : this.children.get(0).path.getNamespace();
    }

    public String getResourcePath()
    {
        return this.children.isEmpty() ? "" : this.children.get(0).path.getPath();
    }

    /**
     * This is mostly for looks, but it doesn't really makes sense by  
     * itself
     */
    @Override
    public String toString()
    {
        return this.getResourceDomain() + ":" + this.getResourcePath();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof MultiResourceLocation)
        {
            MultiResourceLocation multi = (MultiResourceLocation) obj;

            return Objects.equal(this.children, multi.children);
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode()
    {
        if (this.id < 0)
        {
            this.recalculateId();
        }

        return this.id;
    }
    public void fromNbt(INBTType nbt) throws Exception
    {
        ListNBT list = (ListNBT) nbt;

        for (int i = 0; i < list.size(); i++)
        {
            FilteredResourceLocation location = FilteredResourceLocation.from((INBTType) list.get(i));

            if (location != null)
            {
                this.children.add(location);
            }
        }
    }

    @Override
    public void fromJson(JsonElement element) throws Exception
    {
        JsonArray array = (JsonArray) element;

        for (int i = 0; i < array.size(); i++)
        {
            FilteredResourceLocation location = FilteredResourceLocation.from(array.get(i));

            if (location != null)
            {
                this.children.add(location);
            }
        }
    }

    @Override
    public INBTType writeNbt()
    {
        ListNBT list = new ListNBT();

        for (FilteredResourceLocation child : this.children)
        {
            INBTType tag = child.writeNbt();

            if (tag != null)
            {
                list.add((INBT) tag);
            }
        }

        return (INBTType) list;
    }

    @Override
    public JsonElement writeJson()
    {
        JsonArray array = new JsonArray();

        for (FilteredResourceLocation child : this.children)
        {
            JsonElement element = child.writeJson();

            if (element != null)
            {
                array.add(element);
            }
        }

        return array;
    }

    @Override
    public ResourceLocation clone()
    {
        MultiResourceLocation newMulti = new MultiResourceLocation();

        for (FilteredResourceLocation child : this.children)
        {
            newMulti.children.add(child.copy());
        }

        return newMulti;
    }
}