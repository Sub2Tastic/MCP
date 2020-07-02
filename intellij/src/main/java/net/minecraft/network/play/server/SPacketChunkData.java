package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class SPacketChunkData implements Packet<INetHandlerPlayClient>
{
    private int chunkX;
    private int chunkZ;
    private int availableSections;
    private byte[] buffer;
    private List<NBTTagCompound> tileEntityTags;
    private boolean fullChunk;

    public SPacketChunkData()
    {
    }

    public SPacketChunkData(Chunk chunkIn, int changedSectionFilter)
    {
        this.chunkX = chunkIn.field_76635_g;
        this.chunkZ = chunkIn.field_76647_h;
        this.fullChunk = changedSectionFilter == 65535;
        boolean flag = chunkIn.getWorld().dimension.hasSkyLight();
        this.buffer = new byte[this.func_189556_a(chunkIn, flag, changedSectionFilter)];
        this.availableSections = this.func_189555_a(new PacketBuffer(this.getWriteBuffer()), chunkIn, flag, changedSectionFilter);
        this.tileEntityTags = Lists.<NBTTagCompound>newArrayList();

        for (Entry<BlockPos, TileEntity> entry : chunkIn.getTileEntityMap().entrySet())
        {
            BlockPos blockpos = entry.getKey();
            TileEntity tileentity = entry.getValue();
            int i = blockpos.getY() >> 4;

            if (this.isFullChunk() || (changedSectionFilter & 1 << i) != 0)
            {
                NBTTagCompound nbttagcompound = tileentity.getUpdateTag();
                this.tileEntityTags.add(nbttagcompound);
            }
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.chunkX = buf.readInt();
        this.chunkZ = buf.readInt();
        this.fullChunk = buf.readBoolean();
        this.availableSections = buf.readVarInt();
        int i = buf.readVarInt();

        if (i > 2097152)
        {
            throw new RuntimeException("Chunk Packet trying to allocate too much memory on read.");
        }
        else
        {
            this.buffer = new byte[i];
            buf.readBytes(this.buffer);
            int j = buf.readVarInt();
            this.tileEntityTags = Lists.<NBTTagCompound>newArrayList();

            for (int k = 0; k < j; ++k)
            {
                this.tileEntityTags.add(buf.readCompoundTag());
            }
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(this.chunkX);
        buf.writeInt(this.chunkZ);
        buf.writeBoolean(this.fullChunk);
        buf.writeVarInt(this.availableSections);
        buf.writeVarInt(this.buffer.length);
        buf.writeBytes(this.buffer);
        buf.writeVarInt(this.tileEntityTags.size());

        for (NBTTagCompound nbttagcompound : this.tileEntityTags)
        {
            buf.writeCompoundTag(nbttagcompound);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleChunkData(this);
    }

    public PacketBuffer getReadBuffer()
    {
        return new PacketBuffer(Unpooled.wrappedBuffer(this.buffer));
    }

    private ByteBuf getWriteBuffer()
    {
        ByteBuf bytebuf = Unpooled.wrappedBuffer(this.buffer);
        bytebuf.writerIndex(0);
        return bytebuf;
    }

    public int func_189555_a(PacketBuffer p_189555_1_, Chunk p_189555_2_, boolean p_189555_3_, int p_189555_4_)
    {
        int i = 0;
        ExtendedBlockStorage[] aextendedblockstorage = p_189555_2_.getSections();
        int j = 0;

        for (int k = aextendedblockstorage.length; j < k; ++j)
        {
            ExtendedBlockStorage extendedblockstorage = aextendedblockstorage[j];

            if (extendedblockstorage != Chunk.EMPTY_SECTION && (!this.isFullChunk() || !extendedblockstorage.isEmpty()) && (p_189555_4_ & 1 << j) != 0)
            {
                i |= 1 << j;
                extendedblockstorage.getData().write(p_189555_1_);
                p_189555_1_.writeBytes(extendedblockstorage.func_76661_k().getData());

                if (p_189555_3_)
                {
                    p_189555_1_.writeBytes(extendedblockstorage.func_76671_l().getData());
                }
            }
        }

        if (this.isFullChunk())
        {
            p_189555_1_.writeBytes(p_189555_2_.func_76605_m());
        }

        return i;
    }

    protected int func_189556_a(Chunk p_189556_1_, boolean p_189556_2_, int p_189556_3_)
    {
        int i = 0;
        ExtendedBlockStorage[] aextendedblockstorage = p_189556_1_.getSections();
        int j = 0;

        for (int k = aextendedblockstorage.length; j < k; ++j)
        {
            ExtendedBlockStorage extendedblockstorage = aextendedblockstorage[j];

            if (extendedblockstorage != Chunk.EMPTY_SECTION && (!this.isFullChunk() || !extendedblockstorage.isEmpty()) && (p_189556_3_ & 1 << j) != 0)
            {
                i = i + extendedblockstorage.getData().getSerializedSize();
                i = i + extendedblockstorage.func_76661_k().getData().length;

                if (p_189556_2_)
                {
                    i += extendedblockstorage.func_76671_l().getData().length;
                }
            }
        }

        if (this.isFullChunk())
        {
            i += p_189556_1_.func_76605_m().length;
        }

        return i;
    }

    public int getChunkX()
    {
        return this.chunkX;
    }

    public int getChunkZ()
    {
        return this.chunkZ;
    }

    public int getAvailableSections()
    {
        return this.availableSections;
    }

    public boolean isFullChunk()
    {
        return this.fullChunk;
    }

    public List<NBTTagCompound> getTileEntityTags()
    {
        return this.tileEntityTags;
    }
}
