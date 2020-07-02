package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class CPacketPlayerTryUseItemOnBlock implements Packet<INetHandlerPlayServer>
{
    private BlockPos field_179725_b;
    private EnumFacing field_149579_d;
    private EnumHand hand;
    private float field_149577_f;
    private float field_149578_g;
    private float field_149584_h;

    public CPacketPlayerTryUseItemOnBlock()
    {
    }

    public CPacketPlayerTryUseItemOnBlock(BlockPos p_i46858_1_, EnumFacing p_i46858_2_, EnumHand p_i46858_3_, float p_i46858_4_, float p_i46858_5_, float p_i46858_6_)
    {
        this.field_179725_b = p_i46858_1_;
        this.field_149579_d = p_i46858_2_;
        this.hand = p_i46858_3_;
        this.field_149577_f = p_i46858_4_;
        this.field_149578_g = p_i46858_5_;
        this.field_149584_h = p_i46858_6_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_179725_b = buf.readBlockPos();
        this.field_149579_d = (EnumFacing)buf.readEnumValue(EnumFacing.class);
        this.hand = (EnumHand)buf.readEnumValue(EnumHand.class);
        this.field_149577_f = buf.readFloat();
        this.field_149578_g = buf.readFloat();
        this.field_149584_h = buf.readFloat();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBlockPos(this.field_179725_b);
        buf.writeEnumValue(this.field_149579_d);
        buf.writeEnumValue(this.hand);
        buf.writeFloat(this.field_149577_f);
        buf.writeFloat(this.field_149578_g);
        buf.writeFloat(this.field_149584_h);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processTryUseItemOnBlock(this);
    }

    public BlockPos func_187023_a()
    {
        return this.field_179725_b;
    }

    public EnumFacing func_187024_b()
    {
        return this.field_149579_d;
    }

    public EnumHand getHand()
    {
        return this.hand;
    }

    public float func_187026_d()
    {
        return this.field_149577_f;
    }

    public float func_187025_e()
    {
        return this.field_149578_g;
    }

    public float func_187020_f()
    {
        return this.field_149584_h;
    }
}
