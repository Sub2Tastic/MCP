package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketTabComplete implements Packet<INetHandlerPlayClient>
{
    private String[] field_149632_a;

    public SPacketTabComplete()
    {
    }

    public SPacketTabComplete(String[] p_i46962_1_)
    {
        this.field_149632_a = p_i46962_1_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149632_a = new String[buf.readVarInt()];

        for (int i = 0; i < this.field_149632_a.length; ++i)
        {
            this.field_149632_a[i] = buf.readString(32767);
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarInt(this.field_149632_a.length);

        for (String s : this.field_149632_a)
        {
            buf.writeString(s);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.func_147274_a(this);
    }

    public String[] func_149630_c()
    {
        return this.field_149632_a;
    }
}
