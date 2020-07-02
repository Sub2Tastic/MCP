package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ShulkerBoxTileColor implements IFixableData
{
    public int func_188216_a()
    {
        return 813;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound p_188217_1_)
    {
        if ("minecraft:shulker".equals(p_188217_1_.getString("id")))
        {
            p_188217_1_.remove("Color");
        }

        return p_188217_1_;
    }
}
