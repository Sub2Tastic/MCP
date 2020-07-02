package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenIceSpike extends WorldGenerator
{
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
            p_180709_3_ = p_180709_3_.up(p_180709_2_.nextInt(4));
            int i = p_180709_2_.nextInt(4) + 7;
            int j = i / 4 + p_180709_2_.nextInt(2);

            if (j > 1 && p_180709_2_.nextInt(60) == 0)
            {
                p_180709_3_ = p_180709_3_.up(10 + p_180709_2_.nextInt(30));
            }

            for (int k = 0; k < i; ++k)
            {
                float f = (1.0F - (float)k / (float)i) * (float)j;
                int l = MathHelper.ceil(f);

                for (int i1 = -l; i1 <= l; ++i1)
                {
                    float f1 = (float)MathHelper.abs(i1) - 0.25F;

                    for (int j1 = -l; j1 <= l; ++j1)
                    {
                        float f2 = (float)MathHelper.abs(j1) - 0.25F;

                        if ((i1 == 0 && j1 == 0 || f1 * f1 + f2 * f2 <= f * f) && (i1 != -l && i1 != l && j1 != -l && j1 != l || p_180709_2_.nextFloat() <= 0.75F))
                        {
                            IBlockState iblockstate = p_180709_1_.getBlockState(p_180709_3_.add(i1, k, j1));
                            Block block = iblockstate.getBlock();

                            if (iblockstate.getMaterial() == Material.AIR || block == Blocks.DIRT || block == Blocks.SNOW || block == Blocks.ICE)
                            {
                                this.func_175903_a(p_180709_1_, p_180709_3_.add(i1, k, j1), Blocks.PACKED_ICE.getDefaultState());
                            }

                            if (k != 0 && l > 1)
                            {
                                iblockstate = p_180709_1_.getBlockState(p_180709_3_.add(i1, -k, j1));
                                block = iblockstate.getBlock();

                                if (iblockstate.getMaterial() == Material.AIR || block == Blocks.DIRT || block == Blocks.SNOW || block == Blocks.ICE)
                                {
                                    this.func_175903_a(p_180709_1_, p_180709_3_.add(i1, -k, j1), Blocks.PACKED_ICE.getDefaultState());
                                }
                            }
                        }
                    }
                }
            }

            int k1 = j - 1;

            if (k1 < 0)
            {
                k1 = 0;
            }
            else if (k1 > 1)
            {
                k1 = 1;
            }

            for (int l1 = -k1; l1 <= k1; ++l1)
            {
                for (int i2 = -k1; i2 <= k1; ++i2)
                {
                    BlockPos blockpos = p_180709_3_.add(l1, -1, i2);
                    int j2 = 50;

                    if (Math.abs(l1) == 1 && Math.abs(i2) == 1)
                    {
                        j2 = p_180709_2_.nextInt(5);
                    }

                    while (blockpos.getY() > 50)
                    {
                        IBlockState iblockstate1 = p_180709_1_.getBlockState(blockpos);
                        Block block1 = iblockstate1.getBlock();

                        if (iblockstate1.getMaterial() != Material.AIR && block1 != Blocks.DIRT && block1 != Blocks.SNOW && block1 != Blocks.ICE && block1 != Blocks.PACKED_ICE)
                        {
                            break;
                        }

                        this.func_175903_a(p_180709_1_, blockpos, Blocks.PACKED_ICE.getDefaultState());
                        blockpos = blockpos.down();
                        --j2;

                        if (j2 <= 0)
                        {
                            blockpos = blockpos.down(p_180709_2_.nextInt(5) + 1);
                            j2 = p_180709_2_.nextInt(5);
                        }
                    }
                }
            }

            return true;
        }
    }
}
