package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockGravel extends BlockFalling
{
    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        if (p_180660_3_ > 3)
        {
            p_180660_3_ = 3;
        }

        return p_180660_2_.nextInt(10 - p_180660_3_ * 3) == 0 ? Items.FLINT : super.func_180660_a(p_180660_1_, p_180660_2_, p_180660_3_);
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     * @deprecated call via {@link IBlockState#getMapColor(IBlockAccess,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public MapColor getMaterialColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return MapColor.STONE;
    }

    public int getDustColor(IBlockState state)
    {
        return -8356741;
    }
}
