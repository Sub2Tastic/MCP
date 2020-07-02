package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockClay extends Block
{
    public BlockClay()
    {
        super(Material.CLAY);
        this.func_149647_a(CreativeTabs.BUILDING_BLOCKS);
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Items.CLAY_BALL;
    }

    public int func_149745_a(Random p_149745_1_)
    {
        return 4;
    }
}
