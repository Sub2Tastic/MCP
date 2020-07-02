package net.minecraft.network.play.server;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;

public class SPacketStatistics implements Packet<INetHandlerPlayClient>
{
    private Map<StatBase, Integer> statisticMap;

    public SPacketStatistics()
    {
    }

    public SPacketStatistics(Map<StatBase, Integer> p_i46969_1_)
    {
        this.statisticMap = p_i46969_1_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleStatistics(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        int i = buf.readVarInt();
        this.statisticMap = Maps.<StatBase, Integer>newHashMap();

        for (int j = 0; j < i; ++j)
        {
            StatBase statbase = StatList.func_151177_a(buf.readString(32767));
            int k = buf.readVarInt();

            if (statbase != null)
            {
                this.statisticMap.put(statbase, Integer.valueOf(k));
            }
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarInt(this.statisticMap.size());

        for (Entry<StatBase, Integer> entry : this.statisticMap.entrySet())
        {
            buf.writeString((entry.getKey()).field_75975_e);
            buf.writeVarInt(((Integer)entry.getValue()).intValue());
        }
    }

    public Map<StatBase, Integer> getStatisticMap()
    {
        return this.statisticMap;
    }
}
