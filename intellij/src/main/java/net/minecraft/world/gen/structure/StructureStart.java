package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public abstract class StructureStart
{
    protected List<StructureComponent> components = Lists.<StructureComponent>newLinkedList();
    protected StructureBoundingBox bounds;
    private int chunkPosX;
    private int chunkPosZ;

    public StructureStart()
    {
    }

    public StructureStart(int p_i43002_1_, int p_i43002_2_)
    {
        this.chunkPosX = p_i43002_1_;
        this.chunkPosZ = p_i43002_2_;
    }

    public StructureBoundingBox getBoundingBox()
    {
        return this.bounds;
    }

    public List<StructureComponent> getComponents()
    {
        return this.components;
    }

    public void func_75068_a(World p_75068_1_, Random p_75068_2_, StructureBoundingBox p_75068_3_)
    {
        Iterator<StructureComponent> iterator = this.components.iterator();

        while (iterator.hasNext())
        {
            StructureComponent structurecomponent = iterator.next();

            if (structurecomponent.getBoundingBox().intersectsWith(p_75068_3_) && !structurecomponent.func_74875_a(p_75068_1_, p_75068_2_, p_75068_3_))
            {
                iterator.remove();
            }
        }
    }

    protected void func_75072_c()
    {
        this.bounds = StructureBoundingBox.getNewBoundingBox();

        for (StructureComponent structurecomponent : this.components)
        {
            this.bounds.expandTo(structurecomponent.getBoundingBox());
        }
    }

    public NBTTagCompound write(int chunkX, int chunkZ)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.putString("id", MapGenStructureIO.func_143033_a(this));
        nbttagcompound.putInt("ChunkX", chunkX);
        nbttagcompound.putInt("ChunkZ", chunkZ);
        nbttagcompound.func_74782_a("BB", this.bounds.toNBTTagIntArray());
        NBTTagList nbttaglist = new NBTTagList();

        for (StructureComponent structurecomponent : this.components)
        {
            nbttaglist.func_74742_a(structurecomponent.write());
        }

        nbttagcompound.func_74782_a("Children", nbttaglist);
        this.func_143022_a(nbttagcompound);
        return nbttagcompound;
    }

    public void func_143022_a(NBTTagCompound p_143022_1_)
    {
    }

    public void func_143020_a(World p_143020_1_, NBTTagCompound p_143020_2_)
    {
        this.chunkPosX = p_143020_2_.getInt("ChunkX");
        this.chunkPosZ = p_143020_2_.getInt("ChunkZ");

        if (p_143020_2_.contains("BB"))
        {
            this.bounds = new StructureBoundingBox(p_143020_2_.getIntArray("BB"));
        }

        NBTTagList nbttaglist = p_143020_2_.getList("Children", 10);

        for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
        {
            this.components.add(MapGenStructureIO.func_143032_b(nbttaglist.getCompound(i), p_143020_1_));
        }

        this.func_143017_b(p_143020_2_);
    }

    public void func_143017_b(NBTTagCompound p_143017_1_)
    {
    }

    protected void func_75067_a(World p_75067_1_, Random p_75067_2_, int p_75067_3_)
    {
        int i = p_75067_1_.getSeaLevel() - p_75067_3_;
        int j = this.bounds.getYSize() + 1;

        if (j < i)
        {
            j += p_75067_2_.nextInt(i - j);
        }

        int k = j - this.bounds.maxY;
        this.bounds.offset(0, k, 0);

        for (StructureComponent structurecomponent : this.components)
        {
            structurecomponent.offset(0, k, 0);
        }
    }

    protected void func_75070_a(World p_75070_1_, Random p_75070_2_, int p_75070_3_, int p_75070_4_)
    {
        int i = p_75070_4_ - p_75070_3_ + 1 - this.bounds.getYSize();
        int j;

        if (i > 1)
        {
            j = p_75070_3_ + p_75070_2_.nextInt(i);
        }
        else
        {
            j = p_75070_3_;
        }

        int k = j - this.bounds.minY;
        this.bounds.offset(0, k, 0);

        for (StructureComponent structurecomponent : this.components)
        {
            structurecomponent.offset(0, k, 0);
        }
    }

    /**
     * currently only defined for Villages, returns true if Village has more than 2 non-road components
     */
    public boolean isValid()
    {
        return true;
    }

    public boolean func_175788_a(ChunkPos p_175788_1_)
    {
        return true;
    }

    public void func_175787_b(ChunkPos p_175787_1_)
    {
    }

    public int getChunkPosX()
    {
        return this.chunkPosX;
    }

    public int getChunkPosZ()
    {
        return this.chunkPosZ;
    }
}
