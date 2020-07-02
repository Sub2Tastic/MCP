package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenIcePath extends WorldGenerator
{
    private final Block block = Blocks.PACKED_ICE;
    private final int field_150554_b;

    public WorldGenIcePath(int p_i45454_1_)
    {
        this.field_150554_b = p_i45454_1_;
    }

    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        while (p_180709_1_.isAirBlock(p_180709_3_) && p_180709_3_.getY() > 2)
        {
            p_180709_3_ = p_180709_3_.down();
        }

        if (p_180709_1_.getBlockState(p_180709_3_).getBlock() != Blocks.SNOW)
        {
            return false;
        }
        else
        {
            int i = p_180709_2_.nextInt(this.field_150554_b - 2) + 2;
            int j = 1;

            for (int k = p_180709_3_.getX() - i; k <= p_180709_3_.getX() + i; ++k)
            {
                for (int l = p_180709_3_.getZ() - i; l <= p_180709_3_.getZ() + i; ++l)
                {
                    int i1 = k - p_180709_3_.getX();
                    int j1 = l - p_180709_3_.getZ();

                    if (i1 * i1 + j1 * j1 <= i * i)
                    {
                        for (int k1 = p_180709_3_.getY() - 1; k1 <= p_180709_3_.getY() + 1; ++k1)
                        {
                            BlockPos blockpos = new BlockPos(k, k1, l);
                            Block block = p_180709_1_.getBlockState(blockpos).getBlock();

                            if (block == Blocks.DIRT || block == Blocks.SNOW || block == Blocks.ICE)
                            {
                                p_180709_1_.setBlockState(blockpos, this.block.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }
}
