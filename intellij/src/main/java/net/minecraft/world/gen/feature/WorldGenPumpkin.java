package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenPumpkin extends WorldGenerator
{
    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        for (int i = 0; i < 64; ++i)
        {
            BlockPos blockpos = p_180709_3_.add(p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8), p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8));

            if (p_180709_1_.isAirBlock(blockpos) && p_180709_1_.getBlockState(blockpos.down()).getBlock() == Blocks.GRASS && Blocks.PUMPKIN.func_176196_c(p_180709_1_, blockpos))
            {
                p_180709_1_.setBlockState(blockpos, Blocks.PUMPKIN.getDefaultState().func_177226_a(BlockPumpkin.HORIZONTAL_FACING, EnumFacing.Plane.HORIZONTAL.random(p_180709_2_)), 2);
            }
        }

        return true;
    }
}
