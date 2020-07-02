package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ArmorStandSilent implements IFixableData
{
    public int func_188216_a()
    {
        return 147;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound p_188217_1_)
    {
        if ("ArmorStand".equals(p_188217_1_.getString("id")) && p_188217_1_.getBoolean("Silent") && !p_188217_1_.getBoolean("Marker"))
        {
            p_188217_1_.remove("Silent");
        }

        return p_188217_1_;
    }
}
