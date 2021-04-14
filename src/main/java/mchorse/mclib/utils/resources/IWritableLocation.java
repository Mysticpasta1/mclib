package mchorse.mclib.utils.resources;

import com.google.gson.JsonElement;
import net.minecraft.nbt.INBTType;
import net.minecraft.util.ResourceLocation;

public interface IWritableLocation
{
    public void fromNbt(INBTType nbt) throws Exception;

    public void fromJson(JsonElement element) throws Exception;

    public INBTType writeNbt();

    public JsonElement writeJson();

    public ResourceLocation clone();
}