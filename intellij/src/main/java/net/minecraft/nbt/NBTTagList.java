package net.minecraft.nbt;

import com.google.common.collect.Lists;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NBTTagList extends NBTBase
{
    private static final Logger field_179239_b = LogManager.getLogger();
    private List<NBTBase> tagList = Lists.<NBTBase>newArrayList();
    private byte tagType = 0;

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException
    {
        if (this.tagList.isEmpty())
        {
            this.tagType = 0;
        }
        else
        {
            this.tagType = ((NBTBase)this.tagList.get(0)).getId();
        }

        output.writeByte(this.tagType);
        output.writeInt(this.tagList.size());

        for (int i = 0; i < this.tagList.size(); ++i)
        {
            ((NBTBase)this.tagList.get(i)).write(output);
        }
    }

    void func_152446_a(DataInput p_152446_1_, int p_152446_2_, NBTSizeTracker p_152446_3_) throws IOException
    {
        p_152446_3_.read(296L);

        if (p_152446_2_ > 512)
        {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }
        else
        {
            this.tagType = p_152446_1_.readByte();
            int i = p_152446_1_.readInt();

            if (this.tagType == 0 && i > 0)
            {
                throw new RuntimeException("Missing type on ListTag");
            }
            else
            {
                p_152446_3_.read(32L * (long)i);
                this.tagList = Lists.<NBTBase>newArrayListWithCapacity(i);

                for (int j = 0; j < i; ++j)
                {
                    NBTBase nbtbase = NBTBase.func_150284_a(this.tagType);
                    nbtbase.func_152446_a(p_152446_1_, p_152446_2_ + 1, p_152446_3_);
                    this.tagList.add(nbtbase);
                }
            }
        }
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId()
    {
        return 9;
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder("[");

        for (int i = 0; i < this.tagList.size(); ++i)
        {
            if (i != 0)
            {
                stringbuilder.append(',');
            }

            stringbuilder.append(this.tagList.get(i));
        }

        return stringbuilder.append(']').toString();
    }

    public void func_74742_a(NBTBase p_74742_1_)
    {
        if (p_74742_1_.getId() == 0)
        {
            field_179239_b.warn("Invalid TagEnd added to ListTag");
        }
        else
        {
            if (this.tagType == 0)
            {
                this.tagType = p_74742_1_.getId();
            }
            else if (this.tagType != p_74742_1_.getId())
            {
                field_179239_b.warn("Adding mismatching tag types to tag list");
                return;
            }

            this.tagList.add(p_74742_1_);
        }
    }

    public void func_150304_a(int p_150304_1_, NBTBase p_150304_2_)
    {
        if (p_150304_2_.getId() == 0)
        {
            field_179239_b.warn("Invalid TagEnd added to ListTag");
        }
        else if (p_150304_1_ >= 0 && p_150304_1_ < this.tagList.size())
        {
            if (this.tagType == 0)
            {
                this.tagType = p_150304_2_.getId();
            }
            else if (this.tagType != p_150304_2_.getId())
            {
                field_179239_b.warn("Adding mismatching tag types to tag list");
                return;
            }

            this.tagList.set(p_150304_1_, p_150304_2_);
        }
        else
        {
            field_179239_b.warn("index out of bounds to set tag in tag list");
        }
    }

    public NBTBase func_74744_a(int p_74744_1_)
    {
        return this.tagList.remove(p_74744_1_);
    }

    public boolean func_82582_d()
    {
        return this.tagList.isEmpty();
    }

    /**
     * Retrieves the NBTTagCompound at the specified index in the list
     */
    public NBTTagCompound getCompound(int i)
    {
        if (i >= 0 && i < this.tagList.size())
        {
            NBTBase nbtbase = this.tagList.get(i);

            if (nbtbase.getId() == 10)
            {
                return (NBTTagCompound)nbtbase;
            }
        }

        return new NBTTagCompound();
    }

    public int getInt(int iIn)
    {
        if (iIn >= 0 && iIn < this.tagList.size())
        {
            NBTBase nbtbase = this.tagList.get(iIn);

            if (nbtbase.getId() == 3)
            {
                return ((NBTTagInt)nbtbase).getInt();
            }
        }

        return 0;
    }

    public int[] getIntArray(int i)
    {
        if (i >= 0 && i < this.tagList.size())
        {
            NBTBase nbtbase = this.tagList.get(i);

            if (nbtbase.getId() == 11)
            {
                return ((NBTTagIntArray)nbtbase).getIntArray();
            }
        }

        return new int[0];
    }

    public double getDouble(int i)
    {
        if (i >= 0 && i < this.tagList.size())
        {
            NBTBase nbtbase = this.tagList.get(i);

            if (nbtbase.getId() == 6)
            {
                return ((NBTTagDouble)nbtbase).getDouble();
            }
        }

        return 0.0D;
    }

    public float getFloat(int i)
    {
        if (i >= 0 && i < this.tagList.size())
        {
            NBTBase nbtbase = this.tagList.get(i);

            if (nbtbase.getId() == 5)
            {
                return ((NBTTagFloat)nbtbase).getFloat();
            }
        }

        return 0.0F;
    }

    /**
     * Retrieves the tag String value at the specified index in the list
     */
    public String getString(int i)
    {
        if (i >= 0 && i < this.tagList.size())
        {
            NBTBase nbtbase = this.tagList.get(i);
            return nbtbase.getId() == 8 ? nbtbase.getString() : nbtbase.toString();
        }
        else
        {
            return "";
        }
    }

    public NBTBase func_179238_g(int p_179238_1_)
    {
        return (NBTBase)(p_179238_1_ >= 0 && p_179238_1_ < this.tagList.size() ? (NBTBase)this.tagList.get(p_179238_1_) : new NBTTagEnd());
    }

    public int func_74745_c()
    {
        return this.tagList.size();
    }

    /**
     * Creates a clone of the tag.
     */
    public NBTTagList copy()
    {
        NBTTagList nbttaglist = new NBTTagList();
        nbttaglist.tagType = this.tagType;

        for (NBTBase nbtbase : this.tagList)
        {
            NBTBase nbtbase1 = nbtbase.copy();
            nbttaglist.tagList.add(nbtbase1);
        }

        return nbttaglist;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (!super.equals(p_equals_1_))
        {
            return false;
        }
        else
        {
            NBTTagList nbttaglist = (NBTTagList)p_equals_1_;
            return this.tagType == nbttaglist.tagType && Objects.equals(this.tagList, nbttaglist.tagList);
        }
    }

    public int hashCode()
    {
        return super.hashCode() ^ this.tagList.hashCode();
    }

    public int getTagType()
    {
        return this.tagType;
    }
}
