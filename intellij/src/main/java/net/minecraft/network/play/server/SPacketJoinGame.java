package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldType;

public class SPacketJoinGame implements Packet<INetHandlerPlayClient>
{
    private int playerId;
    private boolean hardcoreMode;
    private GameType gameType;
    private int dimension;
    private EnumDifficulty field_149203_e;
    private int maxPlayers;
    private WorldType worldType;
    private boolean reducedDebugInfo;

    public SPacketJoinGame()
    {
    }

    public SPacketJoinGame(int p_i46938_1_, GameType p_i46938_2_, boolean p_i46938_3_, int p_i46938_4_, EnumDifficulty p_i46938_5_, int p_i46938_6_, WorldType p_i46938_7_, boolean p_i46938_8_)
    {
        this.playerId = p_i46938_1_;
        this.dimension = p_i46938_4_;
        this.field_149203_e = p_i46938_5_;
        this.gameType = p_i46938_2_;
        this.maxPlayers = p_i46938_6_;
        this.hardcoreMode = p_i46938_3_;
        this.worldType = p_i46938_7_;
        this.reducedDebugInfo = p_i46938_8_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.playerId = buf.readInt();
        int i = buf.readUnsignedByte();
        this.hardcoreMode = (i & 8) == 8;
        i = i & -9;
        this.gameType = GameType.getByID(i);
        this.dimension = buf.readInt();
        this.field_149203_e = EnumDifficulty.byId(buf.readUnsignedByte());
        this.maxPlayers = buf.readUnsignedByte();
        this.worldType = WorldType.byName(buf.readString(16));

        if (this.worldType == null)
        {
            this.worldType = WorldType.DEFAULT;
        }

        this.reducedDebugInfo = buf.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(this.playerId);
        int i = this.gameType.getID();

        if (this.hardcoreMode)
        {
            i |= 8;
        }

        buf.writeByte(i);
        buf.writeInt(this.dimension);
        buf.writeByte(this.field_149203_e.getId());
        buf.writeByte(this.maxPlayers);
        buf.writeString(this.worldType.func_77127_a());
        buf.writeBoolean(this.reducedDebugInfo);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleJoinGame(this);
    }

    public int getPlayerId()
    {
        return this.playerId;
    }

    public boolean isHardcoreMode()
    {
        return this.hardcoreMode;
    }

    public GameType getGameType()
    {
        return this.gameType;
    }

    public int func_149194_f()
    {
        return this.dimension;
    }

    public EnumDifficulty func_149192_g()
    {
        return this.field_149203_e;
    }

    public int func_149193_h()
    {
        return this.maxPlayers;
    }

    public WorldType getWorldType()
    {
        return this.worldType;
    }

    public boolean isReducedDebugInfo()
    {
        return this.reducedDebugInfo;
    }
}
