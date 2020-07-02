package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockPackedIce extends Block
{
    public BlockPackedIce()
    {
        super(Material.PACKED_ICE);
        this.slipperiness = 0.98F;
        this.func_149647_a(CreativeTabs.BUILDING_BLOCKS);
    }

    public int func_149745_a(Random p_149745_1_)
    {
        return 0;
    }
}
