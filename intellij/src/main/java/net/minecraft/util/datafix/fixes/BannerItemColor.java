package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class BannerItemColor implements IFixableData
{
    public int func_188216_a()
    {
        return 804;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound p_188217_1_)
    {
        if ("minecraft:banner".equals(p_188217_1_.getString("id")) && p_188217_1_.contains("tag", 10))
        {
            NBTTagCompound nbttagcompound = p_188217_1_.getCompound("tag");

            if (nbttagcompound.contains("BlockEntityTag", 10))
            {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("BlockEntityTag");

                if (nbttagcompound1.contains("Base", 99))
                {
                    p_188217_1_.putShort("Damage", (short)(nbttagcompound1.getShort("Base") & 15));

                    if (nbttagcompound.contains("display", 10))
                    {
                        NBTTagCompound nbttagcompound2 = nbttagcompound.getCompound("display");

                        if (nbttagcompound2.contains("Lore", 9))
                        {
                            NBTTagList nbttaglist = nbttagcompound2.getList("Lore", 8);

                            if (nbttaglist.func_74745_c() == 1 && "(+NBT)".equals(nbttaglist.getString(0)))
                            {
                                return p_188217_1_;
                            }
                        }
                    }

                    nbttagcompound1.remove("Base");

                    if (nbttagcompound1.func_82582_d())
                    {
                        nbttagcompound.remove("BlockEntityTag");
                    }

                    if (nbttagcompound.func_82582_d())
                    {
                        p_188217_1_.remove("tag");
                    }
                }
            }
        }

        return p_188217_1_;
    }
}
