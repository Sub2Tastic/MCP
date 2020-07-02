package net.minecraft.world.chunk;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IntIdentityHashBiMap;

public class BlockStatePaletteHashMap implements IBlockStatePalette
{
    private final IntIdentityHashBiMap<IBlockState> statePaletteMap;
    private final IBlockStatePaletteResizer paletteResizer;
    private final int bits;

    public BlockStatePaletteHashMap(int p_i47089_1_, IBlockStatePaletteResizer p_i47089_2_)
    {
        this.bits = p_i47089_1_;
        this.paletteResizer = p_i47089_2_;
        this.statePaletteMap = new IntIdentityHashBiMap<IBlockState>(1 << p_i47089_1_);
    }

    public int idFor(IBlockState state)
    {
        int i = this.statePaletteMap.getId(state);

        if (i == -1)
        {
            i = this.statePaletteMap.add(state);

            if (i >= 1 << this.bits)
            {
                i = this.paletteResizer.func_186008_a(this.bits + 1, state);
            }
        }

        return i;
    }

    @Nullable

    /**
     * Gets the block state by the palette id.
     */
    public IBlockState get(int indexKey)
    {
        return this.statePaletteMap.func_186813_a(indexKey);
    }

    public void read(PacketBuffer buf)
    {
        this.statePaletteMap.clear();
        int i = buf.readVarInt();

        for (int j = 0; j < i; ++j)
        {
            this.statePaletteMap.add(Block.BLOCK_STATE_IDS.getByValue(buf.readVarInt()));
        }
    }

    public void write(PacketBuffer buf)
    {
        int i = this.statePaletteMap.size();
        buf.writeVarInt(i);

        for (int j = 0; j < i; ++j)
        {
            buf.writeVarInt(Block.BLOCK_STATE_IDS.get(this.statePaletteMap.func_186813_a(j)));
        }
    }

    public int getSerializedSize()
    {
        int i = PacketBuffer.getVarIntSize(this.statePaletteMap.size());

        for (int j = 0; j < this.statePaletteMap.size(); ++j)
        {
            i += PacketBuffer.getVarIntSize(Block.BLOCK_STATE_IDS.get(this.statePaletteMap.func_186813_a(j)));
        }

        return i;
    }
}
