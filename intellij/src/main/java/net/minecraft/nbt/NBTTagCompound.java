package net.minecraft.nbt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NBTTagCompound extends NBTBase
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");
    private final Map<String, NBTBase> tagMap = Maps.<String, NBTBase>newHashMap();

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException
    {
        for (String s : this.tagMap.keySet())
        {
            NBTBase nbtbase = this.tagMap.get(s);
            writeEntry(s, nbtbase, output);
        }

        output.writeByte(0);
    }

    void func_152446_a(DataInput p_152446_1_, int p_152446_2_, NBTSizeTracker p_152446_3_) throws IOException
    {
        p_152446_3_.read(384L);

        if (p_152446_2_ > 512)
        {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }
        else
        {
            this.tagMap.clear();
            byte b0;

            while ((b0 = readType(p_152446_1_, p_152446_3_)) != 0)
            {
                String s = readKey(p_152446_1_, p_152446_3_);
                p_152446_3_.read((long)(224 + 16 * s.length()));
                NBTBase nbtbase = func_152449_a(b0, s, p_152446_1_, p_152446_2_ + 1, p_152446_3_);

                if (this.tagMap.put(s, nbtbase) != null)
                {
                    p_152446_3_.read(288L);
                }
            }
        }
    }

    public Set<String> keySet()
    {
        return this.tagMap.keySet();
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId()
    {
        return 10;
    }

    public int size()
    {
        return this.tagMap.size();
    }

    public void func_74782_a(String p_74782_1_, NBTBase p_74782_2_)
    {
        this.tagMap.put(p_74782_1_, p_74782_2_);
    }

    /**
     * Stores a new NBTTagByte with the given byte value into the map with the given string key.
     */
    public void putByte(String key, byte value)
    {
        this.tagMap.put(key, new NBTTagByte(value));
    }

    /**
     * Stores a new NBTTagShort with the given short value into the map with the given string key.
     */
    public void putShort(String key, short value)
    {
        this.tagMap.put(key, new NBTTagShort(value));
    }

    /**
     * Stores a new NBTTagInt with the given integer value into the map with the given string key.
     */
    public void putInt(String key, int value)
    {
        this.tagMap.put(key, new NBTTagInt(value));
    }

    /**
     * Stores a new NBTTagLong with the given long value into the map with the given string key.
     */
    public void putLong(String key, long value)
    {
        this.tagMap.put(key, new NBTTagLong(value));
    }

    public void putUniqueId(String key, UUID value)
    {
        this.putLong(key + "Most", value.getMostSignificantBits());
        this.putLong(key + "Least", value.getLeastSignificantBits());
    }

    @Nullable
    public UUID getUniqueId(String key)
    {
        return new UUID(this.getLong(key + "Most"), this.getLong(key + "Least"));
    }

    public boolean hasUniqueId(String key)
    {
        return this.contains(key + "Most", 99) && this.contains(key + "Least", 99);
    }

    /**
     * Stores a new NBTTagFloat with the given float value into the map with the given string key.
     */
    public void putFloat(String key, float value)
    {
        this.tagMap.put(key, new NBTTagFloat(value));
    }

    /**
     * Stores a new NBTTagDouble with the given double value into the map with the given string key.
     */
    public void putDouble(String key, double value)
    {
        this.tagMap.put(key, new NBTTagDouble(value));
    }

    /**
     * Stores a new NBTTagString with the given string value into the map with the given string key.
     */
    public void putString(String key, String value)
    {
        this.tagMap.put(key, new NBTTagString(value));
    }

    /**
     * Stores a new NBTTagByteArray with the given array as data into the map with the given string key.
     */
    public void putByteArray(String key, byte[] value)
    {
        this.tagMap.put(key, new NBTTagByteArray(value));
    }

    /**
     * Stores a new NBTTagIntArray with the given array as data into the map with the given string key.
     */
    public void putIntArray(String key, int[] value)
    {
        this.tagMap.put(key, new NBTTagIntArray(value));
    }

    /**
     * Stores the given boolean value as a NBTTagByte, storing 1 for true and 0 for false, using the given string key.
     */
    public void putBoolean(String key, boolean value)
    {
        this.putByte(key, (byte)(value ? 1 : 0));
    }

    /**
     * gets a generic tag with the specified name
     */
    public NBTBase get(String key)
    {
        return this.tagMap.get(key);
    }

    /**
     * Gets the ID byte for the given tag key
     */
    public byte getTagId(String key)
    {
        NBTBase nbtbase = this.tagMap.get(key);
        return nbtbase == null ? 0 : nbtbase.getId();
    }

    /**
     * Returns whether the given string has been previously stored as a key in the map.
     */
    public boolean contains(String key)
    {
        return this.tagMap.containsKey(key);
    }

    /**
     * Returns whether the given string has been previously stored as a key in this tag compound as a particular type,
     * denoted by a parameter in the form of an ordinal. If the provided ordinal is 99, this method will match tag types
     * representing numbers.
     */
    public boolean contains(String key, int type)
    {
        int i = this.getTagId(key);

        if (i == type)
        {
            return true;
        }
        else if (type != 99)
        {
            return false;
        }
        else
        {
            return i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6;
        }
    }

    /**
     * Retrieves a byte value using the specified key, or 0 if no such key was stored.
     */
    public byte getByte(String key)
    {
        try
        {
            if (this.contains(key, 99))
            {
                return ((NBTPrimitive)this.tagMap.get(key)).getByte();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return 0;
    }

    /**
     * Retrieves a short value using the specified key, or 0 if no such key was stored.
     */
    public short getShort(String key)
    {
        try
        {
            if (this.contains(key, 99))
            {
                return ((NBTPrimitive)this.tagMap.get(key)).getShort();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return 0;
    }

    /**
     * Retrieves an integer value using the specified key, or 0 if no such key was stored.
     */
    public int getInt(String key)
    {
        try
        {
            if (this.contains(key, 99))
            {
                return ((NBTPrimitive)this.tagMap.get(key)).getInt();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return 0;
    }

    /**
     * Retrieves a long value using the specified key, or 0 if no such key was stored.
     */
    public long getLong(String key)
    {
        try
        {
            if (this.contains(key, 99))
            {
                return ((NBTPrimitive)this.tagMap.get(key)).getLong();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return 0L;
    }

    /**
     * Retrieves a float value using the specified key, or 0 if no such key was stored.
     */
    public float getFloat(String key)
    {
        try
        {
            if (this.contains(key, 99))
            {
                return ((NBTPrimitive)this.tagMap.get(key)).getFloat();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return 0.0F;
    }

    /**
     * Retrieves a double value using the specified key, or 0 if no such key was stored.
     */
    public double getDouble(String key)
    {
        try
        {
            if (this.contains(key, 99))
            {
                return ((NBTPrimitive)this.tagMap.get(key)).getDouble();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return 0.0D;
    }

    /**
     * Retrieves a string value using the specified key, or an empty string if no such key was stored.
     */
    public String getString(String key)
    {
        try
        {
            if (this.contains(key, 8))
            {
                return ((NBTBase)this.tagMap.get(key)).getString();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return "";
    }

    /**
     * Retrieves a byte array using the specified key, or a zero-length array if no such key was stored.
     */
    public byte[] getByteArray(String key)
    {
        try
        {
            if (this.contains(key, 7))
            {
                return ((NBTTagByteArray)this.tagMap.get(key)).getByteArray();
            }
        }
        catch (ClassCastException classcastexception)
        {
            throw new ReportedException(this.func_82581_a(key, 7, classcastexception));
        }

        return new byte[0];
    }

    /**
     * Retrieves an int array using the specified key, or a zero-length array if no such key was stored.
     */
    public int[] getIntArray(String key)
    {
        try
        {
            if (this.contains(key, 11))
            {
                return ((NBTTagIntArray)this.tagMap.get(key)).getIntArray();
            }
        }
        catch (ClassCastException classcastexception)
        {
            throw new ReportedException(this.func_82581_a(key, 11, classcastexception));
        }

        return new int[0];
    }

    /**
     * Retrieves a NBTTagCompound subtag matching the specified key, or a new empty NBTTagCompound if no such key was
     * stored.
     */
    public NBTTagCompound getCompound(String key)
    {
        try
        {
            if (this.contains(key, 10))
            {
                return (NBTTagCompound)this.tagMap.get(key);
            }
        }
        catch (ClassCastException classcastexception)
        {
            throw new ReportedException(this.func_82581_a(key, 10, classcastexception));
        }

        return new NBTTagCompound();
    }

    /**
     * Gets the NBTTagList object with the given name.
     */
    public NBTTagList getList(String key, int type)
    {
        try
        {
            if (this.getTagId(key) == 9)
            {
                NBTTagList nbttaglist = (NBTTagList)this.tagMap.get(key);

                if (!nbttaglist.func_82582_d() && nbttaglist.getTagType() != type)
                {
                    return new NBTTagList();
                }

                return nbttaglist;
            }
        }
        catch (ClassCastException classcastexception)
        {
            throw new ReportedException(this.func_82581_a(key, 9, classcastexception));
        }

        return new NBTTagList();
    }

    /**
     * Retrieves a boolean value using the specified key, or false if no such key was stored. This uses the getByte
     * method.
     */
    public boolean getBoolean(String key)
    {
        return this.getByte(key) != 0;
    }

    /**
     * Remove the specified tag.
     */
    public void remove(String key)
    {
        this.tagMap.remove(key);
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder("{");
        Collection<String> collection = this.tagMap.keySet();

        if (LOGGER.isDebugEnabled())
        {
            List<String> list = Lists.newArrayList(this.tagMap.keySet());
            Collections.sort(list);
            collection = list;
        }

        for (String s : collection)
        {
            if (stringbuilder.length() != 1)
            {
                stringbuilder.append(',');
            }

            stringbuilder.append(handleEscape(s)).append(':').append(this.tagMap.get(s));
        }

        return stringbuilder.append('}').toString();
    }

    public boolean func_82582_d()
    {
        return this.tagMap.isEmpty();
    }

    private CrashReport func_82581_a(final String p_82581_1_, final int p_82581_2_, ClassCastException p_82581_3_)
    {
        CrashReport crashreport = CrashReport.makeCrashReport(p_82581_3_, "Reading NBT data");
        CrashReportCategory crashreportcategory = crashreport.makeCategoryDepth("Corrupt NBT tag", 1);
        crashreportcategory.addDetail("Tag type found", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return NBTBase.field_82578_b[((NBTBase)NBTTagCompound.this.tagMap.get(p_82581_1_)).getId()];
            }
        });
        crashreportcategory.addDetail("Tag type expected", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return NBTBase.field_82578_b[p_82581_2_];
            }
        });
        crashreportcategory.addDetail("Tag name", p_82581_1_);
        return crashreport;
    }

    /**
     * Creates a clone of the tag.
     */
    public NBTTagCompound copy()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        for (String s : this.tagMap.keySet())
        {
            nbttagcompound.func_74782_a(s, ((NBTBase)this.tagMap.get(s)).copy());
        }

        return nbttagcompound;
    }

    public boolean equals(Object p_equals_1_)
    {
        return super.equals(p_equals_1_) && Objects.equals(this.tagMap.entrySet(), ((NBTTagCompound)p_equals_1_).tagMap.entrySet());
    }

    public int hashCode()
    {
        return super.hashCode() ^ this.tagMap.hashCode();
    }

    private static void writeEntry(String name, NBTBase data, DataOutput output) throws IOException
    {
        output.writeByte(data.getId());

        if (data.getId() != 0)
        {
            output.writeUTF(name);
            data.write(output);
        }
    }

    private static byte readType(DataInput input, NBTSizeTracker sizeTracker) throws IOException
    {
        return input.readByte();
    }

    private static String readKey(DataInput input, NBTSizeTracker sizeTracker) throws IOException
    {
        return input.readUTF();
    }

    static NBTBase func_152449_a(byte p_152449_0_, String p_152449_1_, DataInput p_152449_2_, int p_152449_3_, NBTSizeTracker p_152449_4_) throws IOException
    {
        NBTBase nbtbase = NBTBase.func_150284_a(p_152449_0_);

        try
        {
            nbtbase.func_152446_a(p_152449_2_, p_152449_3_, p_152449_4_);
            return nbtbase;
        }
        catch (IOException ioexception)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(ioexception, "Loading NBT data");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("NBT Tag");
            crashreportcategory.addDetail("Tag name", p_152449_1_);
            crashreportcategory.addDetail("Tag type", Byte.valueOf(p_152449_0_));
            throw new ReportedException(crashreport);
        }
    }

    public void func_179237_a(NBTTagCompound p_179237_1_)
    {
        for (String s : p_179237_1_.tagMap.keySet())
        {
            NBTBase nbtbase = p_179237_1_.tagMap.get(s);

            if (nbtbase.getId() == 10)
            {
                if (this.contains(s, 10))
                {
                    NBTTagCompound nbttagcompound = this.getCompound(s);
                    nbttagcompound.func_179237_a((NBTTagCompound)nbtbase);
                }
                else
                {
                    this.func_74782_a(s, nbtbase.copy());
                }
            }
            else
            {
                this.func_74782_a(s, nbtbase.copy());
            }
        }
    }

    protected static String handleEscape(String p_193582_0_)
    {
        return SIMPLE_VALUE.matcher(p_193582_0_).matches() ? p_193582_0_ : NBTTagString.func_193588_a(p_193582_0_);
    }
}
