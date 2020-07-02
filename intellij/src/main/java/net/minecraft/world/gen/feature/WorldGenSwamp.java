package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenSwamp extends WorldGenAbstractTree
{
    private static final IBlockState field_181648_a = Blocks.field_150364_r.getDefaultState().func_177226_a(BlockOldLog.field_176301_b, BlockPlanks.EnumType.OAK);
    private static final IBlockState field_181649_b = Blocks.field_150362_t.getDefaultState().func_177226_a(BlockOldLeaf.field_176239_P, BlockPlanks.EnumType.OAK).func_177226_a(BlockOldLeaf.field_176236_b, Boolean.valueOf(false));

    public WorldGenSwamp()
    {
        super(false);
    }

    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        int i;

        for (i = p_180709_2_.nextInt(4) + 5; p_180709_1_.getBlockState(p_180709_3_.down()).getMaterial() == Material.WATER; p_180709_3_ = p_180709_3_.down())
        {
            ;
        }

        boolean flag = true;

        if (p_180709_3_.getY() >= 1 && p_180709_3_.getY() + i + 1 <= 256)
        {
            for (int j = p_180709_3_.getY(); j <= p_180709_3_.getY() + 1 + i; ++j)
            {
                int k = 1;

                if (j == p_180709_3_.getY())
                {
                    k = 0;
                }

                if (j >= p_180709_3_.getY() + 1 + i - 2)
                {
                    k = 3;
                }

                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for (int l = p_180709_3_.getX() - k; l <= p_180709_3_.getX() + k && flag; ++l)
                {
                    for (int i1 = p_180709_3_.getZ() - k; i1 <= p_180709_3_.getZ() + k && flag; ++i1)
                    {
                        if (j >= 0 && j < 256)
                        {
                            IBlockState iblockstate = p_180709_1_.getBlockState(blockpos$mutableblockpos.setPos(l, j, i1));
                            Block block = iblockstate.getBlock();

                            if (iblockstate.getMaterial() != Material.AIR && iblockstate.getMaterial() != Material.LEAVES)
                            {
                                if (block != Blocks.WATER && block != Blocks.field_150358_i)
                                {
                                    flag = false;
                                }
                                else if (j > p_180709_3_.getY())
                                {
                                    flag = false;
                                }
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
                Block block1 = p_180709_1_.getBlockState(p_180709_3_.down()).getBlock();

                if ((block1 == Blocks.GRASS || block1 == Blocks.DIRT) && p_180709_3_.getY() < 256 - i - 1)
                {
                    this.func_175921_a(p_180709_1_, p_180709_3_.down());

                    for (int k1 = p_180709_3_.getY() - 3 + i; k1 <= p_180709_3_.getY() + i; ++k1)
                    {
                        int j2 = k1 - (p_180709_3_.getY() + i);
                        int l2 = 2 - j2 / 2;

                        for (int j3 = p_180709_3_.getX() - l2; j3 <= p_180709_3_.getX() + l2; ++j3)
                        {
                            int k3 = j3 - p_180709_3_.getX();

                            for (int i4 = p_180709_3_.getZ() - l2; i4 <= p_180709_3_.getZ() + l2; ++i4)
                            {
                                int j1 = i4 - p_180709_3_.getZ();

                                if (Math.abs(k3) != l2 || Math.abs(j1) != l2 || p_180709_2_.nextInt(2) != 0 && j2 != 0)
                                {
                                    BlockPos blockpos = new BlockPos(j3, k1, i4);

                                    if (!p_180709_1_.getBlockState(blockpos).func_185913_b())
                                    {
                                        this.func_175903_a(p_180709_1_, blockpos, field_181649_b);
                                    }
                                }
                            }
                        }
                    }

                    for (int l1 = 0; l1 < i; ++l1)
                    {
                        IBlockState iblockstate1 = p_180709_1_.getBlockState(p_180709_3_.up(l1));
                        Block block2 = iblockstate1.getBlock();

                        if (iblockstate1.getMaterial() == Material.AIR || iblockstate1.getMaterial() == Material.LEAVES || block2 == Blocks.field_150358_i || block2 == Blocks.WATER)
                        {
                            this.func_175903_a(p_180709_1_, p_180709_3_.up(l1), field_181648_a);
                        }
                    }

                    for (int i2 = p_180709_3_.getY() - 3 + i; i2 <= p_180709_3_.getY() + i; ++i2)
                    {
                        int k2 = i2 - (p_180709_3_.getY() + i);
                        int i3 = 2 - k2 / 2;
                        BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();

                        for (int l3 = p_180709_3_.getX() - i3; l3 <= p_180709_3_.getX() + i3; ++l3)
                        {
                            for (int j4 = p_180709_3_.getZ() - i3; j4 <= p_180709_3_.getZ() + i3; ++j4)
                            {
                                blockpos$mutableblockpos1.setPos(l3, i2, j4);

                                if (p_180709_1_.getBlockState(blockpos$mutableblockpos1).getMaterial() == Material.LEAVES)
                                {
                                    BlockPos blockpos3 = blockpos$mutableblockpos1.west();
                                    BlockPos blockpos4 = blockpos$mutableblockpos1.east();
                                    BlockPos blockpos1 = blockpos$mutableblockpos1.north();
                                    BlockPos blockpos2 = blockpos$mutableblockpos1.south();

                                    if (p_180709_2_.nextInt(4) == 0 && p_180709_1_.getBlockState(blockpos3).getMaterial() == Material.AIR)
                                    {
                                        this.func_181647_a(p_180709_1_, blockpos3, BlockVine.EAST);
                                    }

                                    if (p_180709_2_.nextInt(4) == 0 && p_180709_1_.getBlockState(blockpos4).getMaterial() == Material.AIR)
                                    {
                                        this.func_181647_a(p_180709_1_, blockpos4, BlockVine.WEST);
                                    }

                                    if (p_180709_2_.nextInt(4) == 0 && p_180709_1_.getBlockState(blockpos1).getMaterial() == Material.AIR)
                                    {
                                        this.func_181647_a(p_180709_1_, blockpos1, BlockVine.SOUTH);
                                    }

                                    if (p_180709_2_.nextInt(4) == 0 && p_180709_1_.getBlockState(blockpos2).getMaterial() == Material.AIR)
                                    {
                                        this.func_181647_a(p_180709_1_, blockpos2, BlockVine.NORTH);
                                    }
                                }
                            }
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

    private void func_181647_a(World p_181647_1_, BlockPos p_181647_2_, PropertyBool p_181647_3_)
    {
        IBlockState iblockstate = Blocks.VINE.getDefaultState().func_177226_a(p_181647_3_, Boolean.valueOf(true));
        this.func_175903_a(p_181647_1_, p_181647_2_, iblockstate);
        int i = 4;

        for (BlockPos blockpos = p_181647_2_.down(); p_181647_1_.getBlockState(blockpos).getMaterial() == Material.AIR && i > 0; --i)
        {
            this.func_175903_a(p_181647_1_, blockpos, iblockstate);
            blockpos = blockpos.down();
        }
    }
}
