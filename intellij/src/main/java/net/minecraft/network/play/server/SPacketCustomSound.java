package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.SoundCategory;
import org.apache.commons.lang3.Validate;

public class SPacketCustomSound implements Packet<INetHandlerPlayClient>
{
    private String soundName;
    private SoundCategory category;
    private int x;
    private int y = Integer.MAX_VALUE;
    private int z;
    private float volume;
    private float pitch;

    public SPacketCustomSound()
    {
    }

    public SPacketCustomSound(String p_i46948_1_, SoundCategory p_i46948_2_, double p_i46948_3_, double p_i46948_5_, double p_i46948_7_, float p_i46948_9_, float p_i46948_10_)
    {
        Validate.notNull(p_i46948_1_, "name");
        this.soundName = p_i46948_1_;
        this.category = p_i46948_2_;
        this.x = (int)(p_i46948_3_ * 8.0D);
        this.y = (int)(p_i46948_5_ * 8.0D);
        this.z = (int)(p_i46948_7_ * 8.0D);
        this.volume = p_i46948_9_;
        this.pitch = p_i46948_10_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.soundName = buf.readString(256);
        this.category = (SoundCategory)buf.readEnumValue(SoundCategory.class);
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.volume = buf.readFloat();
        this.pitch = buf.readFloat();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeString(this.soundName);
        buf.writeEnumValue(this.category);
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeFloat(this.volume);
        buf.writeFloat(this.pitch);
    }

    public String func_186930_a()
    {
        return this.soundName;
    }

    public SoundCategory getCategory()
    {
        return this.category;
    }

    public double getX()
    {
        return (double)((float)this.x / 8.0F);
    }

    public double getY()
    {
        return (double)((float)this.y / 8.0F);
    }

    public double getZ()
    {
        return (double)((float)this.z / 8.0F);
    }

    public float getVolume()
    {
        return this.volume;
    }

    public float getPitch()
    {
        return this.pitch;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleCustomSound(this);
    }
}
