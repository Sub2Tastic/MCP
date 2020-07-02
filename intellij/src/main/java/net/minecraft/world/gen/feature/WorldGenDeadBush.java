package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenDeadBush extends WorldGenerator
{
    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        for (IBlockState iblockstate = p_180709_1_.getBlockState(p_180709_3_); (iblockstate.getMaterial() == Material.AIR || iblockstate.getMaterial() == Material.LEAVES) && p_180709_3_.getY() > 0; iblockstate = p_180709_1_.getBlockState(p_180709_3_))
        {
            p_180709_3_ = p_180709_3_.down();
        }

        for (int i = 0; i < 4; ++i)
        {
            BlockPos blockpos = p_180709_3_.add(p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8), p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8));

            if (p_180709_1_.isAirBlock(blockpos) && Blocks.field_150330_I.func_180671_f(p_180709_1_, blockpos, Blocks.field_150330_I.getDefaultState()))
            {
                p_180709_1_.setBlockState(blockpos, Blocks.field_150330_I.getDefaultState(), 2);
            }
        }

        return true;
    }
}
