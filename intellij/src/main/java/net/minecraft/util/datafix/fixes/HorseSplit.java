package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class HorseSplit implements IFixableData
{
    public int func_188216_a()
    {
        return 703;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound p_188217_1_)
    {
        if ("EntityHorse".equals(p_188217_1_.getString("id")))
        {
            int i = p_188217_1_.getInt("Type");

            switch (i)
            {
                case 0:
                default:
                    p_188217_1_.putString("id", "Horse");
                    break;

                case 1:
                    p_188217_1_.putString("id", "Donkey");
                    break;

                case 2:
                    p_188217_1_.putString("id", "Mule");
                    break;

                case 3:
                    p_188217_1_.putString("id", "ZombieHorse");
                    break;

                case 4:
                    p_188217_1_.putString("id", "SkeletonHorse");
            }

            p_188217_1_.remove("Type");
        }

        return p_188217_1_;
    }
}
