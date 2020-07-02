package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class EntityArmorAndHeld implements IFixableData
{
    public int func_188216_a()
    {
        return 100;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound p_188217_1_)
    {
        NBTTagList nbttaglist = p_188217_1_.getList("Equipment", 10);

        if (!nbttaglist.func_82582_d() && !p_188217_1_.contains("HandItems", 10))
        {
            NBTTagList nbttaglist1 = new NBTTagList();
            nbttaglist1.func_74742_a(nbttaglist.func_179238_g(0));
            nbttaglist1.func_74742_a(new NBTTagCompound());
            p_188217_1_.func_74782_a("HandItems", nbttaglist1);
        }

        if (nbttaglist.func_74745_c() > 1 && !p_188217_1_.contains("ArmorItem", 10))
        {
            NBTTagList nbttaglist3 = new NBTTagList();
            nbttaglist3.func_74742_a(nbttaglist.getCompound(1));
            nbttaglist3.func_74742_a(nbttaglist.getCompound(2));
            nbttaglist3.func_74742_a(nbttaglist.getCompound(3));
            nbttaglist3.func_74742_a(nbttaglist.getCompound(4));
            p_188217_1_.func_74782_a("ArmorItems", nbttaglist3);
        }

        p_188217_1_.remove("Equipment");

        if (p_188217_1_.contains("DropChances", 9))
        {
            NBTTagList nbttaglist4 = p_188217_1_.getList("DropChances", 5);

            if (!p_188217_1_.contains("HandDropChances", 10))
            {
                NBTTagList nbttaglist2 = new NBTTagList();
                nbttaglist2.func_74742_a(new NBTTagFloat(nbttaglist4.getFloat(0)));
                nbttaglist2.func_74742_a(new NBTTagFloat(0.0F));
                p_188217_1_.func_74782_a("HandDropChances", nbttaglist2);
            }

            if (!p_188217_1_.contains("ArmorDropChances", 10))
            {
                NBTTagList nbttaglist5 = new NBTTagList();
                nbttaglist5.func_74742_a(new NBTTagFloat(nbttaglist4.getFloat(1)));
                nbttaglist5.func_74742_a(new NBTTagFloat(nbttaglist4.getFloat(2)));
                nbttaglist5.func_74742_a(new NBTTagFloat(nbttaglist4.getFloat(3)));
                nbttaglist5.func_74742_a(new NBTTagFloat(nbttaglist4.getFloat(4)));
                p_188217_1_.func_74782_a("ArmorDropChances", nbttaglist5);
            }

            p_188217_1_.remove("DropChances");
        }

        return p_188217_1_;
    }
}
