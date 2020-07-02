package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTaiga1 extends WorldGenAbstractTree
{
    private static final IBlockState field_181636_a = Blocks.field_150364_r.getDefaultState().func_177226_a(BlockOldLog.field_176301_b, BlockPlanks.EnumType.SPRUCE);
    private static final IBlockState field_181637_b = Blocks.field_150362_t.getDefaultState().func_177226_a(BlockOldLeaf.field_176239_P, BlockPlanks.EnumType.SPRUCE).func_177226_a(BlockLeaves.field_176236_b, Boolean.valueOf(false));

    public WorldGenTaiga1()
    {
        super(false);
    }

    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        int i = p_180709_2_.nextInt(5) + 7;
        int j = i - p_180709_2_.nextInt(2) - 3;
        int k = i - j;
        int l = 1 + p_180709_2_.nextInt(k + 1);

        if (p_180709_3_.getY() >= 1 && p_180709_3_.getY() + i + 1 <= 256)
        {
            boolean flag = true;

            for (int i1 = p_180709_3_.getY(); i1 <= p_180709_3_.getY() + 1 + i && flag; ++i1)
            {
                int j1 = 1;

                if (i1 - p_180709_3_.getY() < j)
                {
                    j1 = 0;
                }
                else
                {
                    j1 = l;
                }

                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for (int k1 = p_180709_3_.getX() - j1; k1 <= p_180709_3_.getX() + j1 && flag; ++k1)
                {
                    for (int l1 = p_180709_3_.getZ() - j1; l1 <= p_180709_3_.getZ() + j1 && flag; ++l1)
                    {
                        if (i1 >= 0 && i1 < 256)
                        {
                            if (!this.func_150523_a(p_180709_1_.getBlockState(blockpos$mutableblockpos.setPos(k1, i1, l1)).getBlock()))
                            {
                                flag = false;
                            }
                        }
                        else
                        {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag)
            {
                return false;
            }
            else
            {
                Block block = p_180709_1_.getBlockState(p_180709_3_.down()).getBlock();

                if ((block == Blocks.GRASS || block == Blocks.DIRT) && p_180709_3_.getY() < 256 - i - 1)
                {
                    this.func_175921_a(p_180709_1_, p_180709_3_.down());
                    int k2 = 0;

                    for (int l2 = p_180709_3_.getY() + i; l2 >= p_180709_3_.getY() + j; --l2)
                    {
                        for (int j3 = p_180709_3_.getX() - k2; j3 <= p_180709_3_.getX() + k2; ++j3)
                        {
                            int k3 = j3 - p_180709_3_.getX();

                            for (int i2 = p_180709_3_.getZ() - k2; i2 <= p_180709_3_.getZ() + k2; ++i2)
                            {
                                int j2 = i2 - p_180709_3_.getZ();

                                if (Math.abs(k3) != k2 || Math.abs(j2) != k2 || k2 <= 0)
                                {
                                    BlockPos blockpos = new BlockPos(j3, l2, i2);

                                    if (!p_180709_1_.getBlockState(blockpos).func_185913_b())
                                    {
                                        this.func_175903_a(p_180709_1_, blockpos, field_181637_b);
                                    }
                                }
                            }
                        }

                        if (k2 >= 1 && l2 == p_180709_3_.getY() + j + 1)
                        {
                            --k2;
                        }
                        else if (k2 < l)
                        {
                            ++k2;
                        }
                    }

                    for (int i3 = 0; i3 < i - 1; ++i3)
                    {
                        Material material = p_180709_1_.getBlockState(p_180709_3_.up(i3)).getMaterial();

                        if (material == Material.AIR || material == Material.LEAVES)
                        {
                            this.func_175903_a(p_180709_1_, p_180709_3_.up(i3), field_181636_a);
                        }
                    }

                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }
}
