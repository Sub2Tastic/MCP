package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class HorseSaddle implements IFixableData
{
    public int func_188216_a()
    {
        return 110;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound p_188217_1_)
    {
        if ("EntityHorse".equals(p_188217_1_.getString("id")) && !p_188217_1_.contains("SaddleItem", 10) && p_188217_1_.getBoolean("Saddle"))
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.putString("id", "minecraft:saddle");
            nbttagcompound.putByte("Count", (byte)1);
            nbttagcompound.putShort("Damage", (short)0);
            p_188217_1_.func_74782_a("SaddleItem", nbttagcompound);
            p_188217_1_.remove("Saddle");
        }

        return p_188217_1_;
    }
}
