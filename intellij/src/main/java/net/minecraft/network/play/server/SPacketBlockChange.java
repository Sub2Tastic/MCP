package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SPacketBlockChange implements Packet<INetHandlerPlayClient>
{
    private BlockPos pos;
    private IBlockState field_148883_d;

    public SPacketBlockChange()
    {
    }

    public SPacketBlockChange(World p_i46965_1_, BlockPos p_i46965_2_)
    {
        this.pos = p_i46965_2_;
        this.field_148883_d = p_i46965_1_.getBlockState(p_i46965_2_);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.pos = buf.readBlockPos();
        this.field_148883_d = Block.BLOCK_STATE_IDS.getByValue(buf.readVarInt());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBlockPos(this.pos);
        buf.writeVarInt(Block.BLOCK_STATE_IDS.get(this.field_148883_d));
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleBlockChange(this);
    }

    public IBlockState func_180728_a()
    {
        return this.field_148883_d;
    }

    public BlockPos getPos()
    {
        return this.pos;
    }
}
