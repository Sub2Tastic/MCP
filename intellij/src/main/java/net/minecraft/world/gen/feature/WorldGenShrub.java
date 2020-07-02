package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenShrub extends WorldGenTrees
{
    private final IBlockState field_150528_a;
    private final IBlockState field_150527_b;

    public WorldGenShrub(IBlockState p_i46450_1_, IBlockState p_i46450_2_)
    {
        super(false);
        this.field_150527_b = p_i46450_1_;
        this.field_150528_a = p_i46450_2_;
    }

    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        for (IBlockState iblockstate = p_180709_1_.getBlockState(p_180709_3_); (iblockstate.getMaterial() == Material.AIR || iblockstate.getMaterial() == Material.LEAVES) && p_180709_3_.getY() > 0; iblockstate = p_180709_1_.getBlockState(p_180709_3_))
        {
            p_180709_3_ = p_180709_3_.down();
        }

        Block block = p_180709_1_.getBlockState(p_180709_3_).getBlock();

        if (block == Blocks.DIRT || block == Blocks.GRASS)
        {
            p_180709_3_ = p_180709_3_.up();
            this.func_175903_a(p_180709_1_, p_180709_3_, this.field_150527_b);

            for (int i = p_180709_3_.getY(); i <= p_180709_3_.getY() + 2; ++i)
            {
                int j = i - p_180709_3_.getY();
                int k = 2 - j;

                for (int l = p_180709_3_.getX() - k; l <= p_180709_3_.getX() + k; ++l)
                {
                    int i1 = l - p_180709_3_.getX();

                    for (int j1 = p_180709_3_.getZ() - k; j1 <= p_180709_3_.getZ() + k; ++j1)
                    {
                        int k1 = j1 - p_180709_3_.getZ();

                        if (Math.abs(i1) != k || Math.abs(k1) != k || p_180709_2_.nextInt(2) != 0)
                        {
                            BlockPos blockpos = new BlockPos(l, i, j1);
                            Material material = p_180709_1_.getBlockState(blockpos).getMaterial();

                            if (material == Material.AIR || material == Material.LEAVES)
                            {
                                this.func_175903_a(p_180709_1_, blockpos, this.field_150528_a);
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
