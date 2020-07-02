package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenBlockBlob extends WorldGenerator
{
    private final Block field_150545_a;
    private final int field_150544_b;

    public WorldGenBlockBlob(Block p_i45450_1_, int p_i45450_2_)
    {
        super(false);
        this.field_150545_a = p_i45450_1_;
        this.field_150544_b = p_i45450_2_;
    }

    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        while (true)
        {
            label50:
            {
                if (p_180709_3_.getY() > 3)
                {
                    if (p_180709_1_.isAirBlock(p_180709_3_.down()))
                    {
                        break label50;
                    }

                    Block block = p_180709_1_.getBlockState(p_180709_3_.down()).getBlock();

                    if (block != Blocks.GRASS && block != Blocks.DIRT && block != Blocks.STONE)
                    {
                        break label50;
                    }
                }

                if (p_180709_3_.getY() <= 3)
                {
                    return false;
                }

                int i1 = this.field_150544_b;

                for (int i = 0; i1 >= 0 && i < 3; ++i)
                {
                    int j = i1 + p_180709_2_.nextInt(2);
                    int k = i1 + p_180709_2_.nextInt(2);
                    int l = i1 + p_180709_2_.nextInt(2);
                    float f = (float)(j + k + l) * 0.333F + 0.5F;

                    for (BlockPos blockpos : BlockPos.func_177980_a(p_180709_3_.add(-j, -k, -l), p_180709_3_.add(j, k, l)))
                    {
                        if (blockpos.distanceSq(p_180709_3_) <= (double)(f * f))
                        {
                            p_180709_1_.setBlockState(blockpos, this.field_150545_a.getDefaultState(), 4);
                        }
                    }

                    p_180709_3_ = p_180709_3_.add(-(i1 + 1) + p_180709_2_.nextInt(2 + i1 * 2), 0 - p_180709_2_.nextInt(2), -(i1 + 1) + p_180709_2_.nextInt(2 + i1 * 2));
                }

                return true;
            }
            p_180709_3_ = p_180709_3_.down();
        }
    }
}
