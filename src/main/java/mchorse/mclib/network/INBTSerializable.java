package mchorse.mclib.network;

import net.minecraft.nbt.CompoundNBT;

public interface INBTSerializable
{
    public void fromNBT(CompoundNBT tag);

    public CompoundNBT toNBT(CompoundNBT tag);

    public default CompoundNBT toNBT()
    {
        return this.toNBT(new CompoundNBT());
    }
}
