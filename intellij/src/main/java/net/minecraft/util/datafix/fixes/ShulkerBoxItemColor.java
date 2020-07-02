package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ShulkerBoxItemColor implements IFixableData
{
    public static final String[] NAMES_BY_COLOR = new String[] {"minecraft:white_shulker_box", "minecraft:orange_shulker_box", "minecraft:magenta_shulker_box", "minecraft:light_blue_shulker_box", "minecraft:yellow_shulker_box", "minecraft:lime_shulker_box", "minecraft:pink_shulker_box", "minecraft:gray_shulker_box", "minecraft:silver_shulker_box", "minecraft:cyan_shulker_box", "minecraft:purple_shulker_box", "minecraft:blue_shulker_box", "minecraft:brown_shulker_box", "minecraft:green_shulker_box", "minecraft:red_shulker_box", "minecraft:black_shulker_box"};

    public int func_188216_a()
    {
        return 813;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound p_188217_1_)
    {
        if ("minecraft:shulker_box".equals(p_188217_1_.getString("id")) && p_188217_1_.contains("tag", 10))
        {
            NBTTagCompound nbttagcompound = p_188217_1_.getCompound("tag");

            if (nbttagcompound.contains("BlockEntityTag", 10))
            {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("BlockEntityTag");

                if (nbttagcompound1.getList("Items", 10).func_82582_d())
                {
                    nbttagcompound1.remove("Items");
                }

                int i = nbttagcompound1.getInt("Color");
                nbttagcompound1.remove("Color");

                if (nbttagcompound1.func_82582_d())
                {
                    nbttagcompound.remove("BlockEntityTag");
                }

                if (nbttagcompound.func_82582_d())
                {
                    p_188217_1_.remove("tag");
                }

                p_188217_1_.putString("id", NAMES_BY_COLOR[i % 16]);
            }
        }

        return p_188217_1_;
    }
}
