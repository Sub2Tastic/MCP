package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenWaterlily extends WorldGenerator
{
    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        for (int i = 0; i < 10; ++i)
        {
            int j = p_180709_3_.getX() + p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8);
            int k = p_180709_3_.getY() + p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4);
            int l = p_180709_3_.getZ() + p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8);

            if (p_180709_1_.isAirBlock(new BlockPos(j, k, l)) && Blocks.field_150392_bi.func_176196_c(p_180709_1_, new BlockPos(j, k, l)))
            {
                p_180709_1_.setBlockState(new BlockPos(j, k, l), Blocks.field_150392_bi.getDefaultState(), 2);
            }
        }

        return true;
    }
}
