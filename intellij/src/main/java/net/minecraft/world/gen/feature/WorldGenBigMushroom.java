package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenBigMushroom extends WorldGenerator
{
    private final Block field_76523_a;

    public WorldGenBigMushroom(Block p_i46449_1_)
    {
        super(true);
        this.field_76523_a = p_i46449_1_;
    }

    public WorldGenBigMushroom()
    {
        super(false);
        this.field_76523_a = null;
    }

    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        Block block = this.field_76523_a;

        if (block == null)
        {
            block = p_180709_2_.nextBoolean() ? Blocks.BROWN_MUSHROOM_BLOCK : Blocks.RED_MUSHROOM_BLOCK;
        }

        int i = p_180709_2_.nextInt(3) + 4;

        if (p_180709_2_.nextInt(12) == 0)
        {
            i *= 2;
        }

        boolean flag = true;

        if (p_180709_3_.getY() >= 1 && p_180709_3_.getY() + i + 1 < 256)
        {
            for (int j = p_180709_3_.getY(); j <= p_180709_3_.getY() + 1 + i; ++j)
            {
                int k = 3;

                if (j <= p_180709_3_.getY() + 3)
                {
                    k = 0;
                }

                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for (int l = p_180709_3_.getX() - k; l <= p_180709_3_.getX() + k && flag; ++l)
                {
                    for (int i1 = p_180709_3_.getZ() - k; i1 <= p_180709_3_.getZ() + k && flag; ++i1)
                    {
                        if (j >= 0 && j < 256)
                        {
                            Material material = p_180709_1_.getBlockState(blockpos$mutableblockpos.setPos(l, j, i1)).getMaterial();

                            if (material != Material.AIR && material != Material.LEAVES)
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
                Block block1 = p_180709_1_.getBlockState(p_180709_3_.down()).getBlock();

                if (block1 != Blocks.DIRT && block1 != Blocks.GRASS && block1 != Blocks.MYCELIUM)
                {
                    return false;
                }
                else
                {
                    int k2 = p_180709_3_.getY() + i;

                    if (block == Blocks.RED_MUSHROOM_BLOCK)
                    {
                        k2 = p_180709_3_.getY() + i - 3;
                    }

                    for (int l2 = k2; l2 <= p_180709_3_.getY() + i; ++l2)
                    {
                        int j3 = 1;

                        if (l2 < p_180709_3_.getY() + i)
                        {
                            ++j3;
                        }

                        if (block == Blocks.BROWN_MUSHROOM_BLOCK)
                        {
                            j3 = 3;
                        }

                        int k3 = p_180709_3_.getX() - j3;
                        int l3 = p_180709_3_.getX() + j3;
                        int j1 = p_180709_3_.getZ() - j3;
                        int k1 = p_180709_3_.getZ() + j3;

                        for (int l1 = k3; l1 <= l3; ++l1)
                        {
                            for (int i2 = j1; i2 <= k1; ++i2)
                            {
                                int j2 = 5;

                                if (l1 == k3)
                                {
                                    --j2;
                                }
                                else if (l1 == l3)
                                {
                                    ++j2;
                                }

                                if (i2 == j1)
                                {
                                    j2 -= 3;
                                }
                                else if (i2 == k1)
                                {
                                    j2 += 3;
                                }

                                BlockHugeMushroom.EnumType blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.func_176895_a(j2);

                                if (block == Blocks.BROWN_MUSHROOM_BLOCK || l2 < p_180709_3_.getY() + i)
                                {
                                    if ((l1 == k3 || l1 == l3) && (i2 == j1 || i2 == k1))
                                    {
                                        continue;
                                    }

                                    if (l1 == p_180709_3_.getX() - (j3 - 1) && i2 == j1)
                                    {
                                        blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_WEST;
                                    }

                                    if (l1 == k3 && i2 == p_180709_3_.getZ() - (j3 - 1))
                                    {
                                        blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_WEST;
                                    }

                                    if (l1 == p_180709_3_.getX() + (j3 - 1) && i2 == j1)
                                    {
                                        blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_EAST;
                                    }

                                    if (l1 == l3 && i2 == p_180709_3_.getZ() - (j3 - 1))
                                    {
                                        blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_EAST;
                                    }

                                    if (l1 == p_180709_3_.getX() - (j3 - 1) && i2 == k1)
                                    {
                                        blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_WEST;
                                    }

                                    if (l1 == k3 && i2 == p_180709_3_.getZ() + (j3 - 1))
                                    {
                                        blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_WEST;
                                    }

                                    if (l1 == p_180709_3_.getX() + (j3 - 1) && i2 == k1)
                                    {
                                        blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_EAST;
                                    }

                                    if (l1 == l3 && i2 == p_180709_3_.getZ() + (j3 - 1))
                                    {
                                        blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_EAST;
                                    }
                                }

                                if (blockhugemushroom$enumtype == BlockHugeMushroom.EnumType.CENTER && l2 < p_180709_3_.getY() + i)
                                {
                                    blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.ALL_INSIDE;
                                }

                                if (p_180709_3_.getY() >= p_180709_3_.getY() + i - 1 || blockhugemushroom$enumtype != BlockHugeMushroom.EnumType.ALL_INSIDE)
                                {
                                    BlockPos blockpos = new BlockPos(l1, l2, i2);

                                    if (!p_180709_1_.getBlockState(blockpos).func_185913_b())
                                    {
                                        this.func_175903_a(p_180709_1_, blockpos, block.getDefaultState().func_177226_a(BlockHugeMushroom.field_176380_a, blockhugemushroom$enumtype));
                                    }
                                }
                            }
                        }
                    }

                    for (int i3 = 0; i3 < i; ++i3)
                    {
                        IBlockState iblockstate = p_180709_1_.getBlockState(p_180709_3_.up(i3));

                        if (!iblockstate.func_185913_b())
                        {
                            this.func_175903_a(p_180709_1_, p_180709_3_.up(i3), block.getDefaultState().func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.STEM));
                        }
                    }

                    return true;
                }
            }
        }
        else
        {
            return false;
        }
    }
}
