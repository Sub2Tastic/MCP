package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddBedTileEntity implements IFixableData
{
    private static final Logger field_193842_a = LogManager.getLogger();

    public int func_188216_a()
    {
        return 1125;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound p_188217_1_)
    {
        int i = 416;

        try
        {
            NBTTagCompound nbttagcompound = p_188217_1_.getCompound("Level");
            int j = nbttagcompound.getInt("xPos");
            int k = nbttagcompound.getInt("zPos");
            NBTTagList nbttaglist = nbttagcompound.getList("TileEntities", 10);
            NBTTagList nbttaglist1 = nbttagcompound.getList("Sections", 10);

            for (int l = 0; l < nbttaglist1.func_74745_c(); ++l)
            {
                NBTTagCompound nbttagcompound1 = nbttaglist1.getCompound(l);
                int i1 = nbttagcompound1.getByte("Y");
                byte[] abyte = nbttagcompound1.getByteArray("Blocks");

                for (int j1 = 0; j1 < abyte.length; ++j1)
                {
                    if (416 == (abyte[j1] & 255) << 4)
                    {
                        int k1 = j1 & 15;
                        int l1 = j1 >> 8 & 15;
                        int i2 = j1 >> 4 & 15;
                        NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                        nbttagcompound2.putString("id", "bed");
                        nbttagcompound2.putInt("x", k1 + (j << 4));
                        nbttagcompound2.putInt("y", l1 + (i1 << 4));
                        nbttagcompound2.putInt("z", i2 + (k << 4));
                        nbttaglist.func_74742_a(nbttagcompound2);
                    }
                }
            }
        }
        catch (Exception var17)
        {
            field_193842_a.warn("Unable to datafix Bed blocks, level format may be missing tags.");
        }

        return p_188217_1_;
    }
}
