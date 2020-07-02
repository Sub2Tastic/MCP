package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenReed extends WorldGenerator
{
    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        for (int i = 0; i < 20; ++i)
        {
            BlockPos blockpos = p_180709_3_.add(p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), 0, p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4));

            if (p_180709_1_.isAirBlock(blockpos))
            {
                BlockPos blockpos1 = blockpos.down();

                if (p_180709_1_.getBlockState(blockpos1.west()).getMaterial() == Material.WATER || p_180709_1_.getBlockState(blockpos1.east()).getMaterial() == Material.WATER || p_180709_1_.getBlockState(blockpos1.north()).getMaterial() == Material.WATER || p_180709_1_.getBlockState(blockpos1.south()).getMaterial() == Material.WATER)
                {
                    int j = 2 + p_180709_2_.nextInt(p_180709_2_.nextInt(3) + 1);

                    for (int k = 0; k < j; ++k)
                    {
                        if (Blocks.field_150436_aH.func_176354_d(p_180709_1_, blockpos))
                        {
                            p_180709_1_.setBlockState(blockpos.up(k), Blocks.field_150436_aH.getDefaultState(), 2);
                        }
                    }
                }
            }
        }

        return true;
    }
}
