package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ElderGuardianSplit implements IFixableData
{
    public int func_188216_a()
    {
        return 700;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound p_188217_1_)
    {
        if ("Guardian".equals(p_188217_1_.getString("id")))
        {
            if (p_188217_1_.getBoolean("Elder"))
            {
                p_188217_1_.putString("id", "ElderGuardian");
            }

            p_188217_1_.remove("Elder");
        }

        return p_188217_1_;
    }
}
