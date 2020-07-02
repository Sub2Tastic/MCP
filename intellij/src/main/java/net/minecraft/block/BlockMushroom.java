package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BlockMushroom extends BlockBush implements IGrowable
{
    protected static final AxisAlignedBB field_185518_a = new AxisAlignedBB(0.30000001192092896D, 0.0D, 0.30000001192092896D, 0.699999988079071D, 0.4000000059604645D, 0.699999988079071D);

    protected BlockMushroom()
    {
        this.func_149675_a(true);
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        return field_185518_a;
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        if (p_180650_4_.nextInt(25) == 0)
        {
            int i = 5;
            int j = 4;

            for (BlockPos blockpos : BlockPos.func_177975_b(p_180650_2_.add(-4, -1, -4), p_180650_2_.add(4, 1, 4)))
            {
                if (p_180650_1_.getBlockState(blockpos).getBlock() == this)
                {
                    --i;

                    if (i <= 0)
                    {
                        return;
                    }
                }
            }

            BlockPos blockpos1 = p_180650_2_.add(p_180650_4_.nextInt(3) - 1, p_180650_4_.nextInt(2) - p_180650_4_.nextInt(2), p_180650_4_.nextInt(3) - 1);

            for (int k = 0; k < 4; ++k)
            {
                if (p_180650_1_.isAirBlock(blockpos1) && this.func_180671_f(p_180650_1_, blockpos1, this.getDefaultState()))
                {
                    p_180650_2_ = blockpos1;
                }

                blockpos1 = p_180650_2_.add(p_180650_4_.nextInt(3) - 1, p_180650_4_.nextInt(2) - p_180650_4_.nextInt(2), p_180650_4_.nextInt(3) - 1);
            }

            if (p_180650_1_.isAirBlock(blockpos1) && this.func_180671_f(p_180650_1_, blockpos1, this.getDefaultState()))
            {
                p_180650_1_.setBlockState(blockpos1, this.getDefaultState(), 2);
            }
        }
    }

    public boolean func_176196_c(World p_176196_1_, BlockPos p_176196_2_)
    {
        return super.func_176196_c(p_176196_1_, p_176196_2_) && this.func_180671_f(p_176196_1_, p_176196_2_, this.getDefaultState());
    }

    protected boolean func_185514_i(IBlockState p_185514_1_)
    {
        return p_185514_1_.func_185913_b();
    }

    public boolean func_180671_f(World p_180671_1_, BlockPos p_180671_2_, IBlockState p_180671_3_)
    {
        if (p_180671_2_.getY() >= 0 && p_180671_2_.getY() < 256)
        {
            IBlockState iblockstate = p_180671_1_.getBlockState(p_180671_2_.down());

            if (iblockstate.getBlock() == Blocks.MYCELIUM)
            {
                return true;
            }
            else if (iblockstate.getBlock() == Blocks.DIRT && iblockstate.get(BlockDirt.field_176386_a) == BlockDirt.DirtType.PODZOL)
            {
                return true;
            }
            else
            {
                return p_180671_1_.func_175699_k(p_180671_2_) < 13 && this.func_185514_i(iblockstate);
            }
        }
        else
        {
            return false;
        }
    }

    public boolean func_176485_d(World p_176485_1_, BlockPos p_176485_2_, IBlockState p_176485_3_, Random p_176485_4_)
    {
        p_176485_1_.func_175698_g(p_176485_2_);
        WorldGenerator worldgenerator = null;

        if (this == Blocks.BROWN_MUSHROOM)
        {
            worldgenerator = new WorldGenBigMushroom(Blocks.BROWN_MUSHROOM_BLOCK);
        }
        else if (this == Blocks.RED_MUSHROOM)
        {
            worldgenerator = new WorldGenBigMushroom(Blocks.RED_MUSHROOM_BLOCK);
        }

        if (worldgenerator != null && worldgenerator.func_180709_b(p_176485_1_, p_176485_4_, p_176485_2_))
        {
            return true;
        }
        else
        {
            p_176485_1_.setBlockState(p_176485_2_, p_176485_3_, 3);
            return false;
        }
    }

    /**
     * Whether this IGrowable can grow
     */
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return true;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return (double)rand.nextFloat() < 0.4D;
    }

    public void func_176474_b(World p_176474_1_, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_)
    {
        this.func_176485_d(p_176474_1_, p_176474_3_, p_176474_4_, p_176474_2_);
    }
}
