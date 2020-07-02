package net.minecraft.nbt;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.regex.Pattern;

public class JsonToNBT
{
    private static final Pattern DOUBLE_PATTERN_NOSUFFIX = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", 2);
    private static final Pattern DOUBLE_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", 2);
    private static final Pattern FLOAT_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", 2);
    private static final Pattern BYTE_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", 2);
    private static final Pattern LONG_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", 2);
    private static final Pattern SHORT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", 2);
    private static final Pattern INT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");
    private final String field_193622_h;
    private int field_193623_i;

    public static NBTTagCompound getTagFromJson(String jsonString) throws NBTException
    {
        return (new JsonToNBT(jsonString)).readSingleStruct();
    }

    @VisibleForTesting
    NBTTagCompound readSingleStruct() throws NBTException
    {
        NBTTagCompound nbttagcompound = this.readStruct();
        this.func_193607_l();

        if (this.func_193612_g())
        {
            ++this.field_193623_i;
            throw this.func_193602_b("Trailing data found");
        }
        else
        {
            return nbttagcompound;
        }
    }

    @VisibleForTesting
    JsonToNBT(String p_i47522_1_)
    {
        this.field_193622_h = p_i47522_1_;
    }

    protected String readKey() throws NBTException
    {
        this.func_193607_l();

        if (!this.func_193612_g())
        {
            throw this.func_193602_b("Expected key");
        }
        else
        {
            return this.func_193598_n() == '"' ? this.func_193595_h() : this.func_193614_i();
        }
    }

    private NBTException func_193602_b(String p_193602_1_)
    {
        return new NBTException(p_193602_1_, this.field_193622_h, this.field_193623_i);
    }

    protected NBTBase readTypedValue() throws NBTException
    {
        this.func_193607_l();

        if (this.func_193598_n() == '"')
        {
            return new NBTTagString(this.func_193595_h());
        }
        else
        {
            String s = this.func_193614_i();

            if (s.isEmpty())
            {
                throw this.func_193602_b("Expected value");
            }
            else
            {
                return this.type(s);
            }
        }
    }

    private NBTBase type(String stringIn)
    {
        try
        {
            if (FLOAT_PATTERN.matcher(stringIn).matches())
            {
                return new NBTTagFloat(Float.parseFloat(stringIn.substring(0, stringIn.length() - 1)));
            }

            if (BYTE_PATTERN.matcher(stringIn).matches())
            {
                return new NBTTagByte(Byte.parseByte(stringIn.substring(0, stringIn.length() - 1)));
            }

            if (LONG_PATTERN.matcher(stringIn).matches())
            {
                return new NBTTagLong(Long.parseLong(stringIn.substring(0, stringIn.length() - 1)));
            }

            if (SHORT_PATTERN.matcher(stringIn).matches())
            {
                return new NBTTagShort(Short.parseShort(stringIn.substring(0, stringIn.length() - 1)));
            }

            if (INT_PATTERN.matcher(stringIn).matches())
            {
                return new NBTTagInt(Integer.parseInt(stringIn));
            }

            if (DOUBLE_PATTERN.matcher(stringIn).matches())
            {
                return new NBTTagDouble(Double.parseDouble(stringIn.substring(0, stringIn.length() - 1)));
            }

            if (DOUBLE_PATTERN_NOSUFFIX.matcher(stringIn).matches())
            {
                return new NBTTagDouble(Double.parseDouble(stringIn));
            }

            if ("true".equalsIgnoreCase(stringIn))
            {
                return new NBTTagByte((byte)1);
            }

            if ("false".equalsIgnoreCase(stringIn))
            {
                return new NBTTagByte((byte)0);
            }
        }
        catch (NumberFormatException var3)
        {
            ;
        }

        return new NBTTagString(stringIn);
    }

    private String func_193595_h() throws NBTException
    {
        int i = ++this.field_193623_i;
        StringBuilder stringbuilder = null;
        boolean flag = false;

        while (this.func_193612_g())
        {
            char c0 = this.func_193594_o();

            if (flag)
            {
                if (c0 != '\\' && c0 != '"')
                {
                    throw this.func_193602_b("Invalid escape of '" + c0 + "'");
                }

                flag = false;
            }
            else
            {
                if (c0 == '\\')
                {
                    flag = true;

                    if (stringbuilder == null)
                    {
                        stringbuilder = new StringBuilder(this.field_193622_h.substring(i, this.field_193623_i - 1));
                    }

                    continue;
                }

                if (c0 == '"')
                {
                    return stringbuilder == null ? this.field_193622_h.substring(i, this.field_193623_i - 1) : stringbuilder.toString();
                }
            }

            if (stringbuilder != null)
            {
                stringbuilder.append(c0);
            }
        }

        throw this.func_193602_b("Missing termination quote");
    }

    private String func_193614_i()
    {
        int i;

        for (i = this.field_193623_i; this.func_193612_g() && this.func_193599_a(this.func_193598_n()); ++this.field_193623_i)
        {
            ;
        }

        return this.field_193622_h.substring(i, this.field_193623_i);
    }

    protected NBTBase readValue() throws NBTException
    {
        this.func_193607_l();

        if (!this.func_193612_g())
        {
            throw this.func_193602_b("Expected value");
        }
        else
        {
            char c0 = this.func_193598_n();

            if (c0 == '{')
            {
                return this.readStruct();
            }
            else
            {
                return c0 == '[' ? this.readList() : this.readTypedValue();
            }
        }
    }

    protected NBTBase readList() throws NBTException
    {
        return this.func_193608_a(2) && this.func_193597_b(1) != '"' && this.func_193597_b(2) == ';' ? this.readArrayTag() : this.readListTag();
    }

    protected NBTTagCompound readStruct() throws NBTException
    {
        this.expect('{');
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.func_193607_l();

        while (this.func_193612_g() && this.func_193598_n() != '}')
        {
            String s = this.readKey();

            if (s.isEmpty())
            {
                throw this.func_193602_b("Expected non-empty key");
            }

            this.expect(':');
            nbttagcompound.func_74782_a(s, this.readValue());

            if (!this.hasElementSeparator())
            {
                break;
            }

            if (!this.func_193612_g())
            {
                throw this.func_193602_b("Expected key");
            }
        }

        this.expect('}');
        return nbttagcompound;
    }

    private NBTBase readListTag() throws NBTException
    {
        this.expect('[');
        this.func_193607_l();

        if (!this.func_193612_g())
        {
            throw this.func_193602_b("Expected value");
        }
        else
        {
            NBTTagList nbttaglist = new NBTTagList();
            int i = -1;

            while (this.func_193598_n() != ']')
            {
                NBTBase nbtbase = this.readValue();
                int j = nbtbase.getId();

                if (i < 0)
                {
                    i = j;
                }
                else if (j != i)
                {
                    throw this.func_193602_b("Unable to insert " + NBTBase.func_193581_j(j) + " into ListTag of type " + NBTBase.func_193581_j(i));
                }

                nbttaglist.func_74742_a(nbtbase);

                if (!this.hasElementSeparator())
                {
                    break;
                }

                if (!this.func_193612_g())
                {
                    throw this.func_193602_b("Expected value");
                }
            }

            this.expect(']');
            return nbttaglist;
        }
    }

    private NBTBase readArrayTag() throws NBTException
    {
        this.expect('[');
        char c0 = this.func_193594_o();
        this.func_193594_o();
        this.func_193607_l();

        if (!this.func_193612_g())
        {
            throw this.func_193602_b("Expected value");
        }
        else if (c0 == 'B')
        {
            return new NBTTagByteArray(this.func_193603_a((byte)7, (byte)1));
        }
        else if (c0 == 'L')
        {
            return new NBTTagLongArray(this.func_193603_a((byte)12, (byte)4));
        }
        else if (c0 == 'I')
        {
            return new NBTTagIntArray(this.func_193603_a((byte)11, (byte)3));
        }
        else
        {
            throw this.func_193602_b("Invalid array type '" + c0 + "' found");
        }
    }

    private <T extends Number> List<T> func_193603_a(byte p_193603_1_, byte p_193603_2_) throws NBTException
    {
        List<T> list = Lists.<T>newArrayList();

        while (true)
        {
            if (this.func_193598_n() != ']')
            {
                NBTBase nbtbase = this.readValue();
                int i = nbtbase.getId();

                if (i != p_193603_2_)
                {
                    throw this.func_193602_b("Unable to insert " + NBTBase.func_193581_j(i) + " into " + NBTBase.func_193581_j(p_193603_1_));
                }

                if (p_193603_2_ == 1)
                {
                    list.add((T)Byte.valueOf(((NBTPrimitive)nbtbase).getByte()));
                }
                else if (p_193603_2_ == 4)
                {
                    list.add((T)Long.valueOf(((NBTPrimitive)nbtbase).getLong()));
                }
                else
                {
                    list.add((T)Integer.valueOf(((NBTPrimitive)nbtbase).getInt()));
                }

                if (this.hasElementSeparator())
                {
                    if (!this.func_193612_g())
                    {
                        throw this.func_193602_b("Expected value");
                    }

                    continue;
                }
            }

            this.expect(']');
            return list;
        }
    }

    private void func_193607_l()
    {
        while (this.func_193612_g() && Character.isWhitespace(this.func_193598_n()))
        {
            ++this.field_193623_i;
        }
    }

    private boolean hasElementSeparator()
    {
        this.func_193607_l();

        if (this.func_193612_g() && this.func_193598_n() == ',')
        {
            ++this.field_193623_i;
            this.func_193607_l();
            return true;
        }
        else
        {
            return false;
        }
    }

    private void expect(char expected) throws NBTException
    {
        this.func_193607_l();
        boolean flag = this.func_193612_g();

        if (flag && this.func_193598_n() == expected)
        {
            ++this.field_193623_i;
        }
        else
        {
            throw new NBTException("Expected '" + expected + "' but got '" + (flag ? this.func_193598_n() : "<EOF>") + "'", this.field_193622_h, this.field_193623_i + 1);
        }
    }

    protected boolean func_193599_a(char p_193599_1_)
    {
        return p_193599_1_ >= '0' && p_193599_1_ <= '9' || p_193599_1_ >= 'A' && p_193599_1_ <= 'Z' || p_193599_1_ >= 'a' && p_193599_1_ <= 'z' || p_193599_1_ == '_' || p_193599_1_ == '-' || p_193599_1_ == '.' || p_193599_1_ == '+';
    }

    private boolean func_193608_a(int p_193608_1_)
    {
        return this.field_193623_i + p_193608_1_ < this.field_193622_h.length();
    }

    boolean func_193612_g()
    {
        return this.func_193608_a(0);
    }

    private char func_193597_b(int p_193597_1_)
    {
        return this.field_193622_h.charAt(this.field_193623_i + p_193597_1_);
    }

    private char func_193598_n()
    {
        return this.func_193597_b(0);
    }

    private char func_193594_o()
    {
        return this.field_193622_h.charAt(this.field_193623_i++);
    }
}
