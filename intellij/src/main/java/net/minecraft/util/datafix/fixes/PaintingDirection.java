package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.datafix.IFixableData;

public class PaintingDirection implements IFixableData
{
    public int func_188216_a()
    {
        return 111;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound p_188217_1_)
    {
        String s = p_188217_1_.getString("id");
        boolean flag = "Painting".equals(s);
        boolean flag1 = "ItemFrame".equals(s);

        if ((flag || flag1) && !p_188217_1_.contains("Facing", 99))
        {
            EnumFacing enumfacing;

            if (p_188217_1_.contains("Direction", 99))
            {
                enumfacing = EnumFacing.byHorizontalIndex(p_188217_1_.getByte("Direction"));
                p_188217_1_.putInt("TileX", p_188217_1_.getInt("TileX") + enumfacing.getXOffset());
                p_188217_1_.putInt("TileY", p_188217_1_.getInt("TileY") + enumfacing.getYOffset());
                p_188217_1_.putInt("TileZ", p_188217_1_.getInt("TileZ") + enumfacing.getZOffset());
                p_188217_1_.remove("Direction");

                if (flag1 && p_188217_1_.contains("ItemRotation", 99))
                {
                    p_188217_1_.putByte("ItemRotation", (byte)(p_188217_1_.getByte("ItemRotation") * 2));
                }
            }
            else
            {
                enumfacing = EnumFacing.byHorizontalIndex(p_188217_1_.getByte("Dir"));
                p_188217_1_.remove("Dir");
            }

            p_188217_1_.putByte("Facing", (byte)enumfacing.getHorizontalIndex());
        }

        return p_188217_1_;
    }
}
