package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;

public class SPacketScoreboardObjective implements Packet<INetHandlerPlayClient>
{
    private String objectiveName;
    private String displayName;
    private IScoreCriteria.EnumRenderType field_179818_c;
    private int action;

    public SPacketScoreboardObjective()
    {
    }

    public SPacketScoreboardObjective(ScoreObjective objective, int actionIn)
    {
        this.objectiveName = objective.getName();
        this.displayName = objective.getDisplayName();
        this.field_179818_c = objective.getCriteria().getRenderType();
        this.action = actionIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.objectiveName = buf.readString(16);
        this.action = buf.readByte();

        if (this.action == 0 || this.action == 2)
        {
            this.displayName = buf.readString(32);
            this.field_179818_c = IScoreCriteria.EnumRenderType.func_178795_a(buf.readString(16));
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeString(this.objectiveName);
        buf.writeByte(this.action);

        if (this.action == 0 || this.action == 2)
        {
            buf.writeString(this.displayName);
            buf.writeString(this.field_179818_c.func_178796_a());
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleScoreboardObjective(this);
    }

    public String getObjectiveName()
    {
        return this.objectiveName;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public int getAction()
    {
        return this.action;
    }

    public IScoreCriteria.EnumRenderType func_179817_d()
    {
        return this.field_179818_c;
    }
}
