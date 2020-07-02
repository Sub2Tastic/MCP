package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SPacketUseBed implements Packet<INetHandlerPlayClient>
{
    private int field_149097_a;
    private BlockPos field_179799_b;

    public SPacketUseBed()
    {
    }

    public SPacketUseBed(EntityPlayer p_i46927_1_, BlockPos p_i46927_2_)
    {
        this.field_149097_a = p_i46927_1_.getEntityId();
        this.field_179799_b = p_i46927_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149097_a = buf.readVarInt();
        this.field_179799_b = buf.readBlockPos();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarInt(this.field_149097_a);
        buf.writeBlockPos(this.field_179799_b);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.func_147278_a(this);
    }

    public EntityPlayer func_149091_a(World p_149091_1_)
    {
        return (EntityPlayer)p_149091_1_.getEntityByID(this.field_149097_a);
    }

    public BlockPos func_179798_a()
    {
        return this.field_179799_b;
    }
}
