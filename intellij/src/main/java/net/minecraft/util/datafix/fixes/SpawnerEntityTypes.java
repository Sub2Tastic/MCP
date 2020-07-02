package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class SpawnerEntityTypes implements IFixableData
{
    public int func_188216_a()
    {
        return 107;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound p_188217_1_)
    {
        if (!"MobSpawner".equals(p_188217_1_.getString("id")))
        {
            return p_188217_1_;
        }
        else
        {
            if (p_188217_1_.contains("EntityId", 8))
            {
                String s = p_188217_1_.getString("EntityId");
                NBTTagCompound nbttagcompound = p_188217_1_.getCompound("SpawnData");
                nbttagcompound.putString("id", s.isEmpty() ? "Pig" : s);
                p_188217_1_.func_74782_a("SpawnData", nbttagcompound);
                p_188217_1_.remove("EntityId");
            }

            if (p_188217_1_.contains("SpawnPotentials", 9))
            {
                NBTTagList nbttaglist = p_188217_1_.getList("SpawnPotentials", 10);

                for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
                {
                    NBTTagCompound nbttagcompound1 = nbttaglist.getCompound(i);

                    if (nbttagcompound1.contains("Type", 8))
                    {
                        NBTTagCompound nbttagcompound2 = nbttagcompound1.getCompound("Properties");
                        nbttagcompound2.putString("id", nbttagcompound1.getString("Type"));
                        nbttagcompound1.func_74782_a("Entity", nbttagcompound2);
                        nbttagcompound1.remove("Type");
                        nbttagcompound1.remove("Properties");
                    }
                }
            }

            return p_188217_1_;
        }
    }
}
