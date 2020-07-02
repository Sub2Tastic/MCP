package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagByteArray extends NBTBase
{
    private byte[] data;

    NBTTagByteArray()
    {
    }

    public NBTTagByteArray(byte[] data)
    {
        this.data = data;
    }

    public NBTTagByteArray(List<Byte> p_i47529_1_)
    {
        this(toArray(p_i47529_1_));
    }

    private static byte[] toArray(List<Byte> p_193589_0_)
    {
        byte[] abyte = new byte[p_193589_0_.size()];

        for (int i = 0; i < p_193589_0_.size(); ++i)
        {
            Byte obyte = p_193589_0_.get(i);
            abyte[i] = obyte == null ? 0 : obyte.byteValue();
        }

        return abyte;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException
    {
        output.writeInt(this.data.length);
        output.write(this.data);
    }

    void func_152446_a(DataInput p_152446_1_, int p_152446_2_, NBTSizeTracker p_152446_3_) throws IOException
    {
        p_152446_3_.read(192L);
        int i = p_152446_1_.readInt();
        p_152446_3_.read((long)(8 * i));
        this.data = new byte[i];
        p_152446_1_.readFully(this.data);
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId()
    {
        return 7;
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder("[B;");

        for (int i = 0; i < this.data.length; ++i)
        {
            if (i != 0)
            {
                stringbuilder.append(',');
            }

            stringbuilder.append((int)this.data[i]).append('B');
        }

        return stringbuilder.append(']').toString();
    }

    /**
     * Creates a clone of the tag.
     */
    public NBTBase copy()
    {
        byte[] abyte = new byte[this.data.length];
        System.arraycopy(this.data, 0, abyte, 0, this.data.length);
        return new NBTTagByteArray(abyte);
    }

    public boolean equals(Object p_equals_1_)
    {
        return super.equals(p_equals_1_) && Arrays.equals(this.data, ((NBTTagByteArray)p_equals_1_).data);
    }

    public int hashCode()
    {
        return super.hashCode() ^ Arrays.hashCode(this.data);
    }

    public byte[] getByteArray()
    {
        return this.data;
    }
}
