package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Collection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

public class SPacketMaps implements Packet<INetHandlerPlayClient>
{
    private int mapId;
    private byte mapScale;
    private boolean trackingPosition;
    private MapDecoration[] icons;
    private int minX;
    private int minZ;
    private int columns;
    private int rows;
    private byte[] mapDataBytes;

    public SPacketMaps()
    {
    }

    public SPacketMaps(int p_i46937_1_, byte p_i46937_2_, boolean p_i46937_3_, Collection<MapDecoration> p_i46937_4_, byte[] p_i46937_5_, int p_i46937_6_, int p_i46937_7_, int p_i46937_8_, int p_i46937_9_)
    {
        this.mapId = p_i46937_1_;
        this.mapScale = p_i46937_2_;
        this.trackingPosition = p_i46937_3_;
        this.icons = (MapDecoration[])p_i46937_4_.toArray(new MapDecoration[p_i46937_4_.size()]);
        this.minX = p_i46937_6_;
        this.minZ = p_i46937_7_;
        this.columns = p_i46937_8_;
        this.rows = p_i46937_9_;
        this.mapDataBytes = new byte[p_i46937_8_ * p_i46937_9_];

        for (int i = 0; i < p_i46937_8_; ++i)
        {
            for (int j = 0; j < p_i46937_9_; ++j)
            {
                this.mapDataBytes[i + j * p_i46937_8_] = p_i46937_5_[p_i46937_6_ + i + (p_i46937_7_ + j) * 128];
            }
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.mapId = buf.readVarInt();
        this.mapScale = buf.readByte();
        this.trackingPosition = buf.readBoolean();
        this.icons = new MapDecoration[buf.readVarInt()];

        for (int i = 0; i < this.icons.length; ++i)
        {
            short short1 = (short)buf.readByte();
            this.icons[i] = new MapDecoration(MapDecoration.Type.byIcon((byte)(short1 >> 4 & 15)), buf.readByte(), buf.readByte(), (byte)(short1 & 15));
        }

        this.columns = buf.readUnsignedByte();

        if (this.columns > 0)
        {
            this.rows = buf.readUnsignedByte();
            this.minX = buf.readUnsignedByte();
            this.minZ = buf.readUnsignedByte();
            this.mapDataBytes = buf.readByteArray();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarInt(this.mapId);
        buf.writeByte(this.mapScale);
        buf.writeBoolean(this.trackingPosition);
        buf.writeVarInt(this.icons.length);

        for (MapDecoration mapdecoration : this.icons)
        {
            buf.writeByte((mapdecoration.getImage() & 15) << 4 | mapdecoration.getRotation() & 15);
            buf.writeByte(mapdecoration.getX());
            buf.writeByte(mapdecoration.getY());
        }

        buf.writeByte(this.columns);

        if (this.columns > 0)
        {
            buf.writeByte(this.rows);
            buf.writeByte(this.minX);
            buf.writeByte(this.minZ);
            buf.writeByteArray(this.mapDataBytes);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleMaps(this);
    }

    public int getMapId()
    {
        return this.mapId;
    }

    /**
     * Sets new MapData from the packet to given MapData param
     */
    public void setMapdataTo(MapData mapdataIn)
    {
        mapdataIn.scale = this.mapScale;
        mapdataIn.trackingPosition = this.trackingPosition;
        mapdataIn.mapDecorations.clear();

        for (int i = 0; i < this.icons.length; ++i)
        {
            MapDecoration mapdecoration = this.icons[i];
            mapdataIn.mapDecorations.put("icon-" + i, mapdecoration);
        }

        for (int j = 0; j < this.columns; ++j)
        {
            for (int k = 0; k < this.rows; ++k)
            {
                mapdataIn.colors[this.minX + j + (this.minZ + k) * 128] = this.mapDataBytes[j + k * this.columns];
            }
        }
    }
}
