package mchorse.mclib.network.mclib.common;

import io.netty.buffer.ByteBuf;
import mchorse.mclib.config.Config;
import mchorse.mclib.network.IByteBufSerializable;
import mchorse.mclib.utils.ByteBuffUtil;

public class PacketConfig implements IByteBufSerializable
{
    public Config config;
    public boolean overwrite;

    public PacketConfig()
    {}

    public PacketConfig(Config config)
    {
        this(config, false);
    }

    public PacketConfig(Config config, boolean overwrite)
    {
        this.config = config;
        this.overwrite = overwrite;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.config = new Config(ByteBuffUtil.readUTF8String(buf));
        this.config.fromBytes(buf);
        this.overwrite = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBuffUtil.writeUTF8String(buf, this.config.id);

        this.config.toBytes(buf);
        buf.writeBoolean(this.overwrite);
    }
}