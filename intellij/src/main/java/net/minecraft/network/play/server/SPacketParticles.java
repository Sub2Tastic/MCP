package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.EnumParticleTypes;

public class SPacketParticles implements Packet<INetHandlerPlayClient>
{
    private EnumParticleTypes field_179751_a;
    private float xCoord;
    private float yCoord;
    private float zCoord;
    private float xOffset;
    private float yOffset;
    private float zOffset;
    private float particleSpeed;
    private int particleCount;
    private boolean longDistance;
    private int[] field_179753_k;

    public SPacketParticles()
    {
    }

    public SPacketParticles(EnumParticleTypes p_i46939_1_, boolean p_i46939_2_, float p_i46939_3_, float p_i46939_4_, float p_i46939_5_, float p_i46939_6_, float p_i46939_7_, float p_i46939_8_, float p_i46939_9_, int p_i46939_10_, int... p_i46939_11_)
    {
        this.field_179751_a = p_i46939_1_;
        this.longDistance = p_i46939_2_;
        this.xCoord = p_i46939_3_;
        this.yCoord = p_i46939_4_;
        this.zCoord = p_i46939_5_;
        this.xOffset = p_i46939_6_;
        this.yOffset = p_i46939_7_;
        this.zOffset = p_i46939_8_;
        this.particleSpeed = p_i46939_9_;
        this.particleCount = p_i46939_10_;
        this.field_179753_k = p_i46939_11_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_179751_a = EnumParticleTypes.func_179342_a(buf.readInt());

        if (this.field_179751_a == null)
        {
            this.field_179751_a = EnumParticleTypes.BARRIER;
        }

        this.longDistance = buf.readBoolean();
        this.xCoord = buf.readFloat();
        this.yCoord = buf.readFloat();
        this.zCoord = buf.readFloat();
        this.xOffset = buf.readFloat();
        this.yOffset = buf.readFloat();
        this.zOffset = buf.readFloat();
        this.particleSpeed = buf.readFloat();
        this.particleCount = buf.readInt();
        int i = this.field_179751_a.func_179345_d();
        this.field_179753_k = new int[i];

        for (int j = 0; j < i; ++j)
        {
            this.field_179753_k[j] = buf.readVarInt();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(this.field_179751_a.func_179348_c());
        buf.writeBoolean(this.longDistance);
        buf.writeFloat(this.xCoord);
        buf.writeFloat(this.yCoord);
        buf.writeFloat(this.zCoord);
        buf.writeFloat(this.xOffset);
        buf.writeFloat(this.yOffset);
        buf.writeFloat(this.zOffset);
        buf.writeFloat(this.particleSpeed);
        buf.writeInt(this.particleCount);
        int i = this.field_179751_a.func_179345_d();

        for (int j = 0; j < i; ++j)
        {
            buf.writeVarInt(this.field_179753_k[j]);
        }
    }

    public EnumParticleTypes func_179749_a()
    {
        return this.field_179751_a;
    }

    public boolean isLongDistance()
    {
        return this.longDistance;
    }

    /**
     * Gets the x coordinate to spawn the particle.
     */
    public double getXCoordinate()
    {
        return (double)this.xCoord;
    }

    /**
     * Gets the y coordinate to spawn the particle.
     */
    public double getYCoordinate()
    {
        return (double)this.yCoord;
    }

    /**
     * Gets the z coordinate to spawn the particle.
     */
    public double getZCoordinate()
    {
        return (double)this.zCoord;
    }

    /**
     * Gets the x coordinate offset for the particle. The particle may use the offset for particle spread.
     */
    public float getXOffset()
    {
        return this.xOffset;
    }

    /**
     * Gets the y coordinate offset for the particle. The particle may use the offset for particle spread.
     */
    public float getYOffset()
    {
        return this.yOffset;
    }

    /**
     * Gets the z coordinate offset for the particle. The particle may use the offset for particle spread.
     */
    public float getZOffset()
    {
        return this.zOffset;
    }

    /**
     * Gets the speed of the particle animation (used in client side rendering).
     */
    public float getParticleSpeed()
    {
        return this.particleSpeed;
    }

    /**
     * Gets the amount of particles to spawn
     */
    public int getParticleCount()
    {
        return this.particleCount;
    }

    public int[] func_179748_k()
    {
        return this.field_179753_k;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleParticles(this);
    }
}
