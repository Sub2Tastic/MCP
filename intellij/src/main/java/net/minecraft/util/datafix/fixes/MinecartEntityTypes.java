package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class MinecartEntityTypes implements IFixableData
{
    private static final List<String> MINECART_TYPE_LIST = Lists.newArrayList("MinecartRideable", "MinecartChest", "MinecartFurnace", "MinecartTNT", "MinecartSpawner", "MinecartHopper", "MinecartCommandBlock");

    public int func_188216_a()
    {
        return 106;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound p_188217_1_)
    {
        if ("Minecart".equals(p_188217_1_.getString("id")))
        {
            String s = "MinecartRideable";
            int i = p_188217_1_.getInt("Type");

            if (i > 0 && i < MINECART_TYPE_LIST.size())
            {
                s = MINECART_TYPE_LIST.get(i);
            }

            p_188217_1_.putString("id", s);
            p_188217_1_.remove("Type");
        }

        return p_188217_1_;
    }
}
