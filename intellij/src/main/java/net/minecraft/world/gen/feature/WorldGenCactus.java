package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenCactus extends WorldGenerator
{
    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        for (int i = 0; i < 10; ++i)
        {
            BlockPos blockpos = p_180709_3_.add(p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8), p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8));

            if (p_180709_1_.isAirBlock(blockpos))
            {
                int j = 1 + p_180709_2_.nextInt(p_180709_2_.nextInt(3) + 1);

                for (int k = 0; k < j; ++k)
                {
                    if (Blocks.CACTUS.func_176586_d(p_180709_1_, blockpos))
                    {
                        p_180709_1_.setBlockState(blockpos.up(k), Blocks.CACTUS.getDefaultState(), 2);
                    }
                }
            }
        }

        return true;
    }
}
