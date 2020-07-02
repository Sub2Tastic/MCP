package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenMegaJungle extends WorldGenHugeTrees
{
    public WorldGenMegaJungle(boolean p_i46448_1_, int p_i46448_2_, int p_i46448_3_, IBlockState p_i46448_4_, IBlockState p_i46448_5_)
    {
        super(p_i46448_1_, p_i46448_2_, p_i46448_3_, p_i46448_4_, p_i46448_5_);
    }

    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        int i = this.func_150533_a(p_180709_2_);

        if (!this.func_175929_a(p_180709_1_, p_180709_2_, p_180709_3_, i))
        {
            return false;
        }
        else
        {
            this.func_175930_c(p_180709_1_, p_180709_3_.up(i), 2);

            for (int j = p_180709_3_.getY() + i - 2 - p_180709_2_.nextInt(4); j > p_180709_3_.getY() + i / 2; j -= 2 + p_180709_2_.nextInt(4))
            {
                float f = p_180709_2_.nextFloat() * ((float)Math.PI * 2F);
                int k = p_180709_3_.getX() + (int)(0.5F + MathHelper.cos(f) * 4.0F);
                int l = p_180709_3_.getZ() + (int)(0.5F + MathHelper.sin(f) * 4.0F);

                for (int i1 = 0; i1 < 5; ++i1)
                {
                    k = p_180709_3_.getX() + (int)(1.5F + MathHelper.cos(f) * (float)i1);
                    l = p_180709_3_.getZ() + (int)(1.5F + MathHelper.sin(f) * (float)i1);
                    this.func_175903_a(p_180709_1_, new BlockPos(k, j - 3 + i1 / 2, l), this.field_76520_b);
                }

                int j2 = 1 + p_180709_2_.nextInt(2);
                int j1 = j;

                for (int k1 = j - j2; k1 <= j1; ++k1)
                {
                    int l1 = k1 - j1;
                    this.func_175928_b(p_180709_1_, new BlockPos(k, k1, l), 1 - l1);
                }
            }

            for (int i2 = 0; i2 < i; ++i2)
            {
                BlockPos blockpos = p_180709_3_.up(i2);

                if (this.func_150523_a(p_180709_1_.getBlockState(blockpos).getBlock()))
                {
                    this.func_175903_a(p_180709_1_, blockpos, this.field_76520_b);

                    if (i2 > 0)
                    {
                        this.func_181632_a(p_180709_1_, p_180709_2_, blockpos.west(), BlockVine.EAST);
                        this.func_181632_a(p_180709_1_, p_180709_2_, blockpos.north(), BlockVine.SOUTH);
                    }
                }

                if (i2 < i - 1)
                {
                    BlockPos blockpos1 = blockpos.east();

                    if (this.func_150523_a(p_180709_1_.getBlockState(blockpos1).getBlock()))
                    {
                        this.func_175903_a(p_180709_1_, blockpos1, this.field_76520_b);

                        if (i2 > 0)
                        {
                            this.func_181632_a(p_180709_1_, p_180709_2_, blockpos1.east(), BlockVine.WEST);
                            this.func_181632_a(p_180709_1_, p_180709_2_, blockpos1.north(), BlockVine.SOUTH);
                        }
                    }

                    BlockPos blockpos2 = blockpos.south().east();

                    if (this.func_150523_a(p_180709_1_.getBlockState(blockpos2).getBlock()))
                    {
                        this.func_175903_a(p_180709_1_, blockpos2, this.field_76520_b);

                        if (i2 > 0)
                        {
                            this.func_181632_a(p_180709_1_, p_180709_2_, blockpos2.east(), BlockVine.WEST);
                            this.func_181632_a(p_180709_1_, p_180709_2_, blockpos2.south(), BlockVine.NORTH);
                        }
                    }

                    BlockPos blockpos3 = blockpos.south();

                    if (this.func_150523_a(p_180709_1_.getBlockState(blockpos3).getBlock()))
                    {
                        this.func_175903_a(p_180709_1_, blockpos3, this.field_76520_b);

                        if (i2 > 0)
                        {
                            this.func_181632_a(p_180709_1_, p_180709_2_, blockpos3.west(), BlockVine.EAST);
                            this.func_181632_a(p_180709_1_, p_180709_2_, blockpos3.south(), BlockVine.NORTH);
                        }
                    }
                }
            }

            return true;
        }
    }

    private void func_181632_a(World p_181632_1_, Random p_181632_2_, BlockPos p_181632_3_, PropertyBool p_181632_4_)
    {
        if (p_181632_2_.nextInt(3) > 0 && p_181632_1_.isAirBlock(p_181632_3_))
        {
            this.func_175903_a(p_181632_1_, p_181632_3_, Blocks.VINE.getDefaultState().func_177226_a(p_181632_4_, Boolean.valueOf(true)));
        }
    }

    private void func_175930_c(World p_175930_1_, BlockPos p_175930_2_, int p_175930_3_)
    {
        int i = 2;

        for (int j = -2; j <= 0; ++j)
        {
            this.func_175925_a(p_175930_1_, p_175930_2_.up(j), p_175930_3_ + 1 - j);
        }
    }
}
