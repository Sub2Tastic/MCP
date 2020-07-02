package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenGlowStone2 extends WorldGenerator
{
    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        if (!p_180709_1_.isAirBlock(p_180709_3_))
        {
            return false;
        }
        else if (p_180709_1_.getBlockState(p_180709_3_.up()).getBlock() != Blocks.NETHERRACK)
        {
            return false;
        }
        else
        {
            p_180709_1_.setBlockState(p_180709_3_, Blocks.GLOWSTONE.getDefaultState(), 2);

            for (int i = 0; i < 1500; ++i)
            {
                BlockPos blockpos = p_180709_3_.add(p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8), -p_180709_2_.nextInt(12), p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8));

                if (p_180709_1_.getBlockState(blockpos).getMaterial() == Material.AIR)
                {
                    int j = 0;

                    for (EnumFacing enumfacing : EnumFacing.values())
                    {
                        if (p_180709_1_.getBlockState(blockpos.offset(enumfacing)).getBlock() == Blocks.GLOWSTONE)
                        {
                            ++j;
                        }

                        if (j > 1)
                        {
                            break;
                        }
                    }

                    if (j == 1)
                    {
                        p_180709_1_.setBlockState(blockpos, Blocks.GLOWSTONE.getDefaultState(), 2);
                    }
                }
            }

            return true;
        }
    }
}
