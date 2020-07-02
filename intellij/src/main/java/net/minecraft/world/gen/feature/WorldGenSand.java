package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenSand extends WorldGenerator
{
    private final Block field_150517_a;
    private final int field_76539_b;

    public WorldGenSand(Block p_i45462_1_, int p_i45462_2_)
    {
        this.field_150517_a = p_i45462_1_;
        this.field_76539_b = p_i45462_2_;
    }

    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        if (p_180709_1_.getBlockState(p_180709_3_).getMaterial() != Material.WATER)
        {
            return false;
        }
        else
        {
            int i = p_180709_2_.nextInt(this.field_76539_b - 2) + 2;
            int j = 2;

            for (int k = p_180709_3_.getX() - i; k <= p_180709_3_.getX() + i; ++k)
            {
                for (int l = p_180709_3_.getZ() - i; l <= p_180709_3_.getZ() + i; ++l)
                {
                    int i1 = k - p_180709_3_.getX();
                    int j1 = l - p_180709_3_.getZ();

                    if (i1 * i1 + j1 * j1 <= i * i)
                    {
                        for (int k1 = p_180709_3_.getY() - 2; k1 <= p_180709_3_.getY() + 2; ++k1)
                        {
                            BlockPos blockpos = new BlockPos(k, k1, l);
                            Block block = p_180709_1_.getBlockState(blockpos).getBlock();

                            if (block == Blocks.DIRT || block == Blocks.GRASS)
                            {
                                p_180709_1_.setBlockState(blockpos, this.field_150517_a.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }
}
