package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class RedundantChanceTags implements IFixableData
{
    public int func_188216_a()
    {
        return 113;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound p_188217_1_)
    {
        if (p_188217_1_.contains("HandDropChances", 9))
        {
            NBTTagList nbttaglist = p_188217_1_.getList("HandDropChances", 5);

            if (nbttaglist.func_74745_c() == 2 && nbttaglist.getFloat(0) == 0.0F && nbttaglist.getFloat(1) == 0.0F)
            {
                p_188217_1_.remove("HandDropChances");
            }
        }

        if (p_188217_1_.contains("ArmorDropChances", 9))
        {
            NBTTagList nbttaglist1 = p_188217_1_.getList("ArmorDropChances", 5);

            if (nbttaglist1.func_74745_c() == 4 && nbttaglist1.getFloat(0) == 0.0F && nbttaglist1.getFloat(1) == 0.0F && nbttaglist1.getFloat(2) == 0.0F && nbttaglist1.getFloat(3) == 0.0F)
            {
                p_188217_1_.remove("ArmorDropChances");
            }
        }

        return p_188217_1_;
    }
}
