package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class SPacketSpawnObject implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private UUID uniqueId;
    private double x;
    private double y;
    private double z;
    private int speedX;
    private int speedY;
    private int speedZ;
    private int pitch;
    private int yaw;
    private int type;
    private int data;

    public SPacketSpawnObject()
    {
    }

    public SPacketSpawnObject(Entity entityIn, int typeIn)
    {
        this(entityIn, typeIn, 0);
    }

    public SPacketSpawnObject(Entity p_i46977_1_, int p_i46977_2_, int p_i46977_3_)
    {
        this.entityId = p_i46977_1_.getEntityId();
        this.uniqueId = p_i46977_1_.getUniqueID();
        this.x = p_i46977_1_.posX;
        this.y = p_i46977_1_.posY;
        this.z = p_i46977_1_.posZ;
        this.pitch = MathHelper.floor(p_i46977_1_.rotationPitch * 256.0F / 360.0F);
        this.yaw = MathHelper.floor(p_i46977_1_.rotationYaw * 256.0F / 360.0F);
        this.type = p_i46977_2_;
        this.data = p_i46977_3_;
        double d0 = 3.9D;
        this.speedX = (int)(MathHelper.clamp(p_i46977_1_.field_70159_w, -3.9D, 3.9D) * 8000.0D);
        this.speedY = (int)(MathHelper.clamp(p_i46977_1_.field_70181_x, -3.9D, 3.9D) * 8000.0D);
        this.speedZ = (int)(MathHelper.clamp(p_i46977_1_.field_70179_y, -3.9D, 3.9D) * 8000.0D);
    }

    public SPacketSpawnObject(Entity p_i46978_1_, int p_i46978_2_, int p_i46978_3_, BlockPos p_i46978_4_)
    {
        this(p_i46978_1_, p_i46978_2_, p_i46978_3_);
        this.x = (double)p_i46978_4_.getX();
        this.y = (double)p_i46978_4_.getY();
        this.z = (double)p_i46978_4_.getZ();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityId = buf.readVarInt();
        this.uniqueId = buf.readUniqueId();
        this.type = buf.readByte();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.pitch = buf.readByte();
        this.yaw = buf.readByte();
        this.data = buf.readInt();
        this.speedX = buf.readShort();
        this.speedY = buf.readShort();
        this.speedZ = buf.readShort();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarInt(this.entityId);
        buf.writeUniqueId(this.uniqueId);
        buf.writeByte(this.type);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeByte(this.pitch);
        buf.writeByte(this.yaw);
        buf.writeInt(this.data);
        buf.writeShort(this.speedX);
        buf.writeShort(this.speedY);
        buf.writeShort(this.speedZ);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSpawnObject(this);
    }

    public int getEntityID()
    {
        return this.entityId;
    }

    public UUID getUniqueId()
    {
        return this.uniqueId;
    }

    public double getX()
    {
        return this.x;
    }

    public double getY()
    {
        return this.y;
    }

    public double getZ()
    {
        return this.z;
    }

    public int func_149010_g()
    {
        return this.speedX;
    }

    public int func_149004_h()
    {
        return this.speedY;
    }

    public int func_148999_i()
    {
        return this.speedZ;
    }

    public int getPitch()
    {
        return this.pitch;
    }

    public int getYaw()
    {
        return this.yaw;
    }

    public int func_148993_l()
    {
        return this.type;
    }

    public int getData()
    {
        return this.data;
    }

    public void func_149003_d(int p_149003_1_)
    {
        this.speedX = p_149003_1_;
    }

    public void func_149000_e(int p_149000_1_)
    {
        this.speedY = p_149000_1_;
    }

    public void func_149007_f(int p_149007_1_)
    {
        this.speedZ = p_149007_1_;
    }

    public void func_149002_g(int p_149002_1_)
    {
        this.data = p_149002_1_;
    }
}
