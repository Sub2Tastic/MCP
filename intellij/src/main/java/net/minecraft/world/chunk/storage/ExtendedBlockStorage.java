package net.minecraft.world.chunk.storage;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraft.world.chunk.NibbleArray;

public class ExtendedBlockStorage
{
    private final int yBase;
    private int blockRefCount;
    private int blockTickRefCount;
    private final BlockStateContainer data;
    private NibbleArray field_76679_g;
    private NibbleArray field_76685_h;

    public ExtendedBlockStorage(int p_i1997_1_, boolean p_i1997_2_)
    {
        this.yBase = p_i1997_1_;
        this.data = new BlockStateContainer();
        this.field_76679_g = new NibbleArray();

        if (p_i1997_2_)
        {
            this.field_76685_h = new NibbleArray();
        }
    }

    public IBlockState getBlockState(int x, int y, int z)
    {
        return this.data.get(x, y, z);
    }

    public void setBlockState(int x, int y, int z, IBlockState state)
    {
        IBlockState iblockstate = this.getBlockState(x, y, z);
        Block block = iblockstate.getBlock();
        Block block1 = state.getBlock();

        if (block != Blocks.AIR)
        {
            --this.blockRefCount;

            if (block.ticksRandomly())
            {
                --this.blockTickRefCount;
            }
        }

        if (block1 != Blocks.AIR)
        {
            ++this.blockRefCount;

            if (block1.ticksRandomly())
            {
                ++this.blockTickRefCount;
            }
        }

        this.data.func_186013_a(x, y, z, state);
    }

    /**
     * Returns whether or not this block storage's Chunk is fully empty, based on its internal reference count.
     */
    public boolean isEmpty()
    {
        return this.blockRefCount == 0;
    }

    /**
     * Returns whether or not this block storage's Chunk will require random ticking, used to avoid looping through
     * random block ticks when there are no blocks that would randomly tick.
     */
    public boolean needsRandomTick()
    {
        return this.blockTickRefCount > 0;
    }

    public int func_76662_d()
    {
        return this.yBase;
    }

    public void func_76657_c(int p_76657_1_, int p_76657_2_, int p_76657_3_, int p_76657_4_)
    {
        this.field_76685_h.set(p_76657_1_, p_76657_2_, p_76657_3_, p_76657_4_);
    }

    public int func_76670_c(int p_76670_1_, int p_76670_2_, int p_76670_3_)
    {
        return this.field_76685_h.get(p_76670_1_, p_76670_2_, p_76670_3_);
    }

    public void func_76677_d(int p_76677_1_, int p_76677_2_, int p_76677_3_, int p_76677_4_)
    {
        this.field_76679_g.set(p_76677_1_, p_76677_2_, p_76677_3_, p_76677_4_);
    }

    public int func_76674_d(int p_76674_1_, int p_76674_2_, int p_76674_3_)
    {
        return this.field_76679_g.get(p_76674_1_, p_76674_2_, p_76674_3_);
    }

    public void recalculateRefCounts()
    {
        this.blockRefCount = 0;
        this.blockTickRefCount = 0;

        for (int i = 0; i < 16; ++i)
        {
            for (int j = 0; j < 16; ++j)
            {
                for (int k = 0; k < 16; ++k)
                {
                    Block block = this.getBlockState(i, j, k).getBlock();

                    if (block != Blocks.AIR)
                    {
                        ++this.blockRefCount;

                        if (block.ticksRandomly())
                        {
                            ++this.blockTickRefCount;
                        }
                    }
                }
            }
        }
    }

    public BlockStateContainer getData()
    {
        return this.data;
    }

    public NibbleArray func_76661_k()
    {
        return this.field_76679_g;
    }

    public NibbleArray func_76671_l()
    {
        return this.field_76685_h;
    }

    public void func_76659_c(NibbleArray p_76659_1_)
    {
        this.field_76679_g = p_76659_1_;
    }

    public void func_76666_d(NibbleArray p_76666_1_)
    {
        this.field_76685_h = p_76666_1_;
    }
}
