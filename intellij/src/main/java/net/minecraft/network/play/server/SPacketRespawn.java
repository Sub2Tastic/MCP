package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldType;

public class SPacketRespawn implements Packet<INetHandlerPlayClient>
{
    private int dimensionID;
    private EnumDifficulty field_149086_b;
    private GameType gameType;
    private WorldType worldType;

    public SPacketRespawn()
    {
    }

    public SPacketRespawn(int p_i46923_1_, EnumDifficulty p_i46923_2_, WorldType p_i46923_3_, GameType p_i46923_4_)
    {
        this.dimensionID = p_i46923_1_;
        this.field_149086_b = p_i46923_2_;
        this.gameType = p_i46923_4_;
        this.worldType = p_i46923_3_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleRespawn(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.dimensionID = buf.readInt();
        this.field_149086_b = EnumDifficulty.byId(buf.readUnsignedByte());
        this.gameType = GameType.getByID(buf.readUnsignedByte());
        this.worldType = WorldType.byName(buf.readString(16));

        if (this.worldType == null)
        {
            this.worldType = WorldType.DEFAULT;
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(this.dimensionID);
        buf.writeByte(this.field_149086_b.getId());
        buf.writeByte(this.gameType.getID());
        buf.writeString(this.worldType.func_77127_a());
    }

    public int func_149082_c()
    {
        return this.dimensionID;
    }

    public EnumDifficulty func_149081_d()
    {
        return this.field_149086_b;
    }

    public GameType getGameType()
    {
        return this.gameType;
    }

    public WorldType getWorldType()
    {
        return this.worldType;
    }
}
