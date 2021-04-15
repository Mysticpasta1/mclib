package mchorse.mclib.network.mclib.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import mchorse.mclib.network.IByteBufSerializable;
import mchorse.mclib.utils.ByteBuffUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketDropItem implements IByteBufSerializable
{
    public ItemStack stack = ItemStack.EMPTY;

    public PacketDropItem()
    {}

    public PacketDropItem(ItemStack stack)
    {
        this.stack = stack;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        CompoundNBT tagCompound = ByteBuffUtil.readNBT(buf);

        if (tagCompound != null)
        {
            this.stack = new ItemStack((IItemProvider) tagCompound);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        if (!this.stack.isEmpty())
        {
            ByteBuffUtil.writeNBT(buf, this.stack.write(new CompoundNBT()));
        }
    }
}