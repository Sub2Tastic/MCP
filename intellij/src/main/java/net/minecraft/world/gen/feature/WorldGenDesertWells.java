package net.minecraft.world.gen.feature;

import com.google.common.base.Predicates;
import java.util.Random;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenDesertWells extends WorldGenerator
{
    private static final BlockStateMatcher IS_SAND = BlockStateMatcher.forBlock(Blocks.SAND).func_177637_a(BlockSand.field_176504_a, Predicates.equalTo(BlockSand.EnumType.SAND));
    private final IBlockState sandSlab = Blocks.STONE_SLAB.getDefaultState().func_177226_a(BlockStoneSlab.field_176556_M, BlockStoneSlab.EnumType.SAND).func_177226_a(BlockSlab.field_176554_a, BlockSlab.EnumBlockHalf.BOTTOM);
    private final IBlockState sandstone = Blocks.SANDSTONE.getDefaultState();
    private final IBlockState water = Blocks.field_150358_i.getDefaultState();

    public boolean func_180709_b(World p_180709_1_, Random p_180709_2_, BlockPos p_180709_3_)
    {
        while (p_180709_1_.isAirBlock(p_180709_3_) && p_180709_3_.getY() > 2)
        {
            p_180709_3_ = p_180709_3_.down();
        }

        if (!IS_SAND.apply(p_180709_1_.getBlockState(p_180709_3_)))
        {
            return false;
        }
        else
        {
            for (int i = -2; i <= 2; ++i)
            {
                for (int j = -2; j <= 2; ++j)
                {
                    if (p_180709_1_.isAirBlock(p_180709_3_.add(i, -1, j)) && p_180709_1_.isAirBlock(p_180709_3_.add(i, -2, j)))
                    {
                        return false;
                    }
                }
            }

            for (int l = -1; l <= 0; ++l)
            {
                for (int l1 = -2; l1 <= 2; ++l1)
                {
                    for (int k = -2; k <= 2; ++k)
                    {
                        p_180709_1_.setBlockState(p_180709_3_.add(l1, l, k), this.sandstone, 2);
                    }
                }
            }

            p_180709_1_.setBlockState(p_180709_3_, this.water, 2);

            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
                p_180709_1_.setBlockState(p_180709_3_.offset(enumfacing), this.water, 2);
            }

            for (int i1 = -2; i1 <= 2; ++i1)
            {
                for (int i2 = -2; i2 <= 2; ++i2)
                {
                    if (i1 == -2 || i1 == 2 || i2 == -2 || i2 == 2)
                    {
                        p_180709_1_.setBlockState(p_180709_3_.add(i1, 1, i2), this.sandstone, 2);
                    }
                }
            }

            p_180709_1_.setBlockState(p_180709_3_.add(2, 1, 0), this.sandSlab, 2);
            p_180709_1_.setBlockState(p_180709_3_.add(-2, 1, 0), this.sandSlab, 2);
            p_180709_1_.setBlockState(p_180709_3_.add(0, 1, 2), this.sandSlab, 2);
            p_180709_1_.setBlockState(p_180709_3_.add(0, 1, -2), this.sandSlab, 2);

            for (int j1 = -1; j1 <= 1; ++j1)
            {
                for (int j2 = -1; j2 <= 1; ++j2)
                {
                    if (j1 == 0 && j2 == 0)
                    {
                        p_180709_1_.setBlockState(p_180709_3_.add(j1, 4, j2), this.sandstone, 2);
                    }
                    else
                    {
                        p_180709_1_.setBlockState(p_180709_3_.add(j1, 4, j2), this.sandSlab, 2);
                    }
                }
            }

            for (int k1 = 1; k1 <= 3; ++k1)
            {
                p_180709_1_.setBlockState(p_180709_3_.add(-1, k1, -1), this.sandstone, 2);
                p_180709_1_.setBlockState(p_180709_3_.add(-1, k1, 1), this.sandstone, 2);
                p_180709_1_.setBlockState(p_180709_3_.add(1, k1, -1), this.sandstone, 2);
                p_180709_1_.setBlockState(p_180709_3_.add(1, k1, 1), this.sandstone, 2);
            }

            return true;
        }
    }
}
