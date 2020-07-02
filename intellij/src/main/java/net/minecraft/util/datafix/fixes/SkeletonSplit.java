package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class SkeletonSplit implements IFixableData
{
    public int func_188216_a()
    {
        return 701;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound p_188217_1_)
    {
        String s = p_188217_1_.getString("id");

        if ("Skeleton".equals(s))
        {
            int i = p_188217_1_.getInt("SkeletonType");

            if (i == 1)
            {
                p_188217_1_.putString("id", "WitherSkeleton");
            }
            else if (i == 2)
            {
                p_188217_1_.putString("id", "Stray");
            }

            p_188217_1_.remove("SkeletonType");
        }

        return p_188217_1_;
    }
}
