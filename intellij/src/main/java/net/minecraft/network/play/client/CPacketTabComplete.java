package net.minecraft.network.play.client;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.StringUtils;

public class CPacketTabComplete implements Packet<INetHandlerPlayServer>
{
    private String field_149420_a;
    private boolean field_186990_b;
    @Nullable
    private BlockPos field_179710_b;

    public CPacketTabComplete()
    {
    }

    public CPacketTabComplete(String p_i46888_1_, @Nullable BlockPos p_i46888_2_, boolean p_i46888_3_)
    {
        this.field_149420_a = p_i46888_1_;
        this.field_179710_b = p_i46888_2_;
        this.field_186990_b = p_i46888_3_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149420_a = buf.readString(32767);
        this.field_186990_b = buf.readBoolean();
        boolean flag = buf.readBoolean();

        if (flag)
        {
            this.field_179710_b = buf.readBlockPos();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeString(StringUtils.substring(this.field_149420_a, 0, 32767));
        buf.writeBoolean(this.field_186990_b);
        boolean flag = this.field_179710_b != null;
        buf.writeBoolean(flag);

        if (flag)
        {
            buf.writeBlockPos(this.field_179710_b);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.func_147341_a(this);
    }

    public String func_149419_c()
    {
        return this.field_149420_a;
    }

    @Nullable
    public BlockPos func_179709_b()
    {
        return this.field_179710_b;
    }

    public boolean func_186989_c()
    {
        return this.field_186990_b;
    }
}
