package net.minecraft.world;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class NextTickListEntry implements Comparable<NextTickListEntry>
{
    private static long nextTickEntryID;
    private final Block target;
    public final BlockPos position;
    public long scheduledTime;
    public int priority;
    private final long tickEntryID;

    public NextTickListEntry(BlockPos p_i45745_1_, Block p_i45745_2_)
    {
        this.tickEntryID = (long)(nextTickEntryID++);
        this.position = p_i45745_1_.toImmutable();
        this.target = p_i45745_2_;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (!(p_equals_1_ instanceof NextTickListEntry))
        {
            return false;
        }
        else
        {
            NextTickListEntry nextticklistentry = (NextTickListEntry)p_equals_1_;
            return this.position.equals(nextticklistentry.position) && Block.func_149680_a(this.target, nextticklistentry.target);
        }
    }

    public int hashCode()
    {
        return this.position.hashCode();
    }

    public NextTickListEntry func_77176_a(long p_77176_1_)
    {
        this.scheduledTime = p_77176_1_;
        return this;
    }

    public void func_82753_a(int p_82753_1_)
    {
        this.priority = p_82753_1_;
    }

    public int compareTo(NextTickListEntry p_compareTo_1_)
    {
        if (this.scheduledTime < p_compareTo_1_.scheduledTime)
        {
            return -1;
        }
        else if (this.scheduledTime > p_compareTo_1_.scheduledTime)
        {
            return 1;
        }
        else if (this.priority != p_compareTo_1_.priority)
        {
            return this.priority - p_compareTo_1_.priority;
        }
        else if (this.tickEntryID < p_compareTo_1_.tickEntryID)
        {
            return -1;
        }
        else
        {
            return this.tickEntryID > p_compareTo_1_.tickEntryID ? 1 : 0;
        }
    }

    public String toString()
    {
        return Block.func_149682_b(this.target) + ": " + this.position + ", " + this.scheduledTime + ", " + this.priority + ", " + this.tickEntryID;
    }

    public Block getTarget()
    {
        return this.target;
    }
}
