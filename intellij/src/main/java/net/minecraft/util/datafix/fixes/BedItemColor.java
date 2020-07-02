package net.minecraft.util.datafix.fixes;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class BedItemColor implements IFixableData
{
    public int func_188216_a()
    {
        return 1125;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound p_188217_1_)
    {
        if ("minecraft:bed".equals(p_188217_1_.getString("id")) && p_188217_1_.getShort("Damage") == 0)
        {
            p_188217_1_.putShort("Damage", (short)EnumDyeColor.RED.func_176765_a());
        }

        return p_188217_1_;
    }
}
