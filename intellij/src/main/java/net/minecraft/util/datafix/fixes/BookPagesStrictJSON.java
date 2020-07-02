package net.minecraft.util.datafix.fixes;

import com.google.gson.JsonParseException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.StringUtils;
import net.minecraft.util.datafix.IFixableData;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class BookPagesStrictJSON implements IFixableData
{
    public int func_188216_a()
    {
        return 165;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound p_188217_1_)
    {
        if ("minecraft:written_book".equals(p_188217_1_.getString("id")))
        {
            NBTTagCompound nbttagcompound = p_188217_1_.getCompound("tag");

            if (nbttagcompound.contains("pages", 9))
            {
                NBTTagList nbttaglist = nbttagcompound.getList("pages", 8);

                for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
                {
                    String s = nbttaglist.getString(i);
                    ITextComponent itextcomponent = null;

                    if (!"null".equals(s) && !StringUtils.isNullOrEmpty(s))
                    {
                        if (s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"' || s.charAt(0) == '{' && s.charAt(s.length() - 1) == '}')
                        {
                            try
                            {
                                itextcomponent = (ITextComponent)JsonUtils.fromJson(SignStrictJSON.GSON, s, ITextComponent.class, true);

                                if (itextcomponent == null)
                                {
                                    itextcomponent = new TextComponentString("");
                                }
                            }
                            catch (JsonParseException var10)
                            {
                                ;
                            }

                            if (itextcomponent == null)
                            {
                                try
                                {
                                    itextcomponent = ITextComponent.Serializer.fromJson(s);
                                }
                                catch (JsonParseException var9)
                                {
                                    ;
                                }
                            }

                            if (itextcomponent == null)
                            {
                                try
                                {
                                    itextcomponent = ITextComponent.Serializer.fromJsonLenient(s);
                                }
                                catch (JsonParseException var8)
                                {
                                    ;
                                }
                            }

                            if (itextcomponent == null)
                            {
                                itextcomponent = new TextComponentString(s);
                            }
                        }
                        else
                        {
                            itextcomponent = new TextComponentString(s);
                        }
                    }
                    else
                    {
                        itextcomponent = new TextComponentString("");
                    }

                    nbttaglist.func_150304_a(i, new NBTTagString(ITextComponent.Serializer.toJson(itextcomponent)));
                }

                nbttagcompound.func_74782_a("pages", nbttaglist);
            }
        }

        return p_188217_1_;
    }
}
