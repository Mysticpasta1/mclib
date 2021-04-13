package mchorse.mclib.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.nbt.*;
import net.minecraft.util.math.vector.Vector3f;

import java.io.IOException;

/**
 * NBT utils 
 */
public class NBTUtils
{
    public static void readFloatList(ListNBT list, float[] array)
    {
        int count = Math.min(array.length, list.size());

        for (int i = 0; i < count; i++)
        {
            array[i] = list.getFloat(i);
        }
    }

    public static ListNBT writeFloatList(ListNBT list, float[] array)
    {
        for (int i = 0; i < array.length; i++)
        {
            list.add(FloatNBT.valueOf(array[i]));
        }

        return list;
    }

    public static void readFloatList(ListNBT list, Vector3f vector)
    {
        if (list.size() != 3)
        {
            return;
        }

        float x = vector.getX();
        x = list.getFloat(0);
        float y = vector.getY();
        y = list.getFloat(1);
        float z = vector.getZ();
        z = list.getFloat(2);
    }

    public static ListNBT writeFloatList(ListNBT list, Vector3f vector)
    {
        list.add(FloatNBT.valueOf(vector.getX()));
        list.add(FloatNBT.valueOf(vector.getY()));
        list.add(FloatNBT.valueOf(vector.getZ()));

        return list;
    }

    public static CompoundNBT readInfiniteTag(ByteBuf buf)
    {
        int i = buf.readerIndex();
        byte b0 = buf.readByte();

        if (b0 == 0)
        {
            return null;
        }
        else
        {
            buf.readerIndex(i);

            try
            {
                return CompressedStreamTools.read(new ByteBufInputStream(buf), NBTSizeTracker.INFINITE);
            }
            catch (IOException ioexception)
            {
                throw new EncoderException(ioexception);
            }
        }
    }
}