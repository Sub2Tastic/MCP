package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityVelocity implements Packet<INetHandlerPlayClient>
{
    private int entityID;
    private int motionX;
    private int motionY;
    private int motionZ;

    public SPacketEntityVelocity()
    {
    }

    public SPacketEntityVelocity(Entity entityIn)
    {
        this(entityIn.getEntityId(), entityIn.field_70159_w, entityIn.field_70181_x, entityIn.field_70179_y);
    }

    public SPacketEntityVelocity(int p_i46915_1_, double p_i46915_2_, double p_i46915_4_, double p_i46915_6_)
    {
        this.entityID = p_i46915_1_;
        double d0 = 3.9D;

        if (p_i46915_2_ < -3.9D)
        {
            p_i46915_2_ = -3.9D;
        }

        if (p_i46915_4_ < -3.9D)
        {
            p_i46915_4_ = -3.9D;
        }

        if (p_i46915_6_ < -3.9D)
        {
            p_i46915_6_ = -3.9D;
        }

        if (p_i46915_2_ > 3.9D)
        {
            p_i46915_2_ = 3.9D;
        }

        if (p_i46915_4_ > 3.9D)
        {
            p_i46915_4_ = 3.9D;
        }

        if (p_i46915_6_ > 3.9D)
        {
            p_i46915_6_ = 3.9D;
        }

        this.motionX = (int)(p_i46915_2_ * 8000.0D);
        this.motionY = (int)(p_i46915_4_ * 8000.0D);
        this.motionZ = (int)(p_i46915_6_ * 8000.0D);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityID = buf.readVarInt();
        this.motionX = buf.readShort();
        this.motionY = buf.readShort();
        this.motionZ = buf.readShort();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarInt(this.entityID);
        buf.writeShort(this.motionX);
        buf.writeShort(this.motionY);
        buf.writeShort(this.motionZ);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityVelocity(this);
    }

    public int getEntityID()
    {
        return this.entityID;
    }

    public int getMotionX()
    {
        return this.motionX;
    }

    public int getMotionY()
    {
        return this.motionY;
    }

    public int getMotionZ()
    {
        return this.motionZ;
    }
}
