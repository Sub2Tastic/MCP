package net.minecraft.block;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDynamicLiquid extends BlockLiquid
{
    int field_149815_a;

    protected BlockDynamicLiquid(Material p_i45403_1_)
    {
        super(p_i45403_1_);
    }

    private void func_180690_f(World p_180690_1_, BlockPos p_180690_2_, IBlockState p_180690_3_)
    {
        p_180690_1_.setBlockState(p_180690_2_, func_176363_b(this.material).getDefaultState().func_177226_a(LEVEL, p_180690_3_.get(LEVEL)), 2);
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        int i = ((Integer)p_180650_3_.get(LEVEL)).intValue();
        int j = 1;

        if (this.material == Material.LAVA && !p_180650_1_.dimension.doesWaterVaporize())
        {
            j = 2;
        }

        int k = this.tickRate(p_180650_1_);

        if (i > 0)
        {
            int l = -100;
            this.field_149815_a = 0;

            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
                l = this.func_176371_a(p_180650_1_, p_180650_2_.offset(enumfacing), l);
            }

            int i1 = l + j;

            if (i1 >= 8 || l < 0)
            {
                i1 = -1;
            }

            int j1 = this.func_189542_i(p_180650_1_.getBlockState(p_180650_2_.up()));

            if (j1 >= 0)
            {
                if (j1 >= 8)
                {
                    i1 = j1;
                }
                else
                {
                    i1 = j1 + 8;
                }
            }

            if (this.field_149815_a >= 2 && this.material == Material.WATER)
            {
                IBlockState iblockstate = p_180650_1_.getBlockState(p_180650_2_.down());

                if (iblockstate.getMaterial().isSolid())
                {
                    i1 = 0;
                }
                else if (iblockstate.getMaterial() == this.material && ((Integer)iblockstate.get(LEVEL)).intValue() == 0)
                {
                    i1 = 0;
                }
            }

            if (this.material == Material.LAVA && i < 8 && i1 < 8 && i1 > i && p_180650_4_.nextInt(4) != 0)
            {
                k *= 4;
            }

            if (i1 == i)
            {
                this.func_180690_f(p_180650_1_, p_180650_2_, p_180650_3_);
            }
            else
            {
                i = i1;

                if (i1 < 0)
                {
                    p_180650_1_.func_175698_g(p_180650_2_);
                }
                else
                {
                    p_180650_3_ = p_180650_3_.func_177226_a(LEVEL, Integer.valueOf(i1));
                    p_180650_1_.setBlockState(p_180650_2_, p_180650_3_, 2);
                    p_180650_1_.func_175684_a(p_180650_2_, this, k);
                    p_180650_1_.func_175685_c(p_180650_2_, this, false);
                }
            }
        }
        else
        {
            this.func_180690_f(p_180650_1_, p_180650_2_, p_180650_3_);
        }

        IBlockState iblockstate1 = p_180650_1_.getBlockState(p_180650_2_.down());

        if (this.func_176373_h(p_180650_1_, p_180650_2_.down(), iblockstate1))
        {
            if (this.material == Material.LAVA && p_180650_1_.getBlockState(p_180650_2_.down()).getMaterial() == Material.WATER)
            {
                p_180650_1_.setBlockState(p_180650_2_.down(), Blocks.STONE.getDefaultState());
                this.triggerMixEffects(p_180650_1_, p_180650_2_.down());
                return;
            }

            if (i >= 8)
            {
                this.func_176375_a(p_180650_1_, p_180650_2_.down(), iblockstate1, i);
            }
            else
            {
                this.func_176375_a(p_180650_1_, p_180650_2_.down(), iblockstate1, i + 8);
            }
        }
        else if (i >= 0 && (i == 0 || this.func_176372_g(p_180650_1_, p_180650_2_.down(), iblockstate1)))
        {
            Set<EnumFacing> set = this.func_176376_e(p_180650_1_, p_180650_2_);
            int k1 = i + j;

            if (i >= 8)
            {
                k1 = 1;
            }

            if (k1 >= 8)
            {
                return;
            }

            for (EnumFacing enumfacing1 : set)
            {
                this.func_176375_a(p_180650_1_, p_180650_2_.offset(enumfacing1), p_180650_1_.getBlockState(p_180650_2_.offset(enumfacing1)), k1);
            }
        }
    }

    private void func_176375_a(World p_176375_1_, BlockPos p_176375_2_, IBlockState p_176375_3_, int p_176375_4_)
    {
        if (this.func_176373_h(p_176375_1_, p_176375_2_, p_176375_3_))
        {
            if (p_176375_3_.getMaterial() != Material.AIR)
            {
                if (this.material == Material.LAVA)
                {
                    this.triggerMixEffects(p_176375_1_, p_176375_2_);
                }
                else
                {
                    p_176375_3_.getBlock().func_176226_b(p_176375_1_, p_176375_2_, p_176375_3_, 0);
                }
            }

            p_176375_1_.setBlockState(p_176375_2_, this.getDefaultState().func_177226_a(LEVEL, Integer.valueOf(p_176375_4_)), 3);
        }
    }

    private int func_176374_a(World p_176374_1_, BlockPos p_176374_2_, int p_176374_3_, EnumFacing p_176374_4_)
    {
        int i = 1000;

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            if (enumfacing != p_176374_4_)
            {
                BlockPos blockpos = p_176374_2_.offset(enumfacing);
                IBlockState iblockstate = p_176374_1_.getBlockState(blockpos);

                if (!this.func_176372_g(p_176374_1_, blockpos, iblockstate) && (iblockstate.getMaterial() != this.material || ((Integer)iblockstate.get(LEVEL)).intValue() > 0))
                {
                    if (!this.func_176372_g(p_176374_1_, blockpos.down(), iblockstate))
                    {
                        return p_176374_3_;
                    }

                    if (p_176374_3_ < this.getSlopeFindDistance(p_176374_1_))
                    {
                        int j = this.func_176374_a(p_176374_1_, blockpos, p_176374_3_ + 1, enumfacing.getOpposite());

                        if (j < i)
                        {
                            i = j;
                        }
                    }
                }
            }
        }

        return i;
    }

    private int getSlopeFindDistance(World worldIn)
    {
        return this.material == Material.LAVA && !worldIn.dimension.doesWaterVaporize() ? 2 : 4;
    }

    private Set<EnumFacing> func_176376_e(World p_176376_1_, BlockPos p_176376_2_)
    {
        int i = 1000;
        Set<EnumFacing> set = EnumSet.<EnumFacing>noneOf(EnumFacing.class);

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            BlockPos blockpos = p_176376_2_.offset(enumfacing);
            IBlockState iblockstate = p_176376_1_.getBlockState(blockpos);

            if (!this.func_176372_g(p_176376_1_, blockpos, iblockstate) && (iblockstate.getMaterial() != this.material || ((Integer)iblockstate.get(LEVEL)).intValue() > 0))
            {
                int j;

                if (this.func_176372_g(p_176376_1_, blockpos.down(), p_176376_1_.getBlockState(blockpos.down())))
                {
                    j = this.func_176374_a(p_176376_1_, blockpos, 1, enumfacing.getOpposite());
                }
                else
                {
                    j = 0;
                }

                if (j < i)
                {
                    set.clear();
                }

                if (j <= i)
                {
                    set.add(enumfacing);
                    i = j;
                }
            }
        }

        return set;
    }

    private boolean func_176372_g(World p_176372_1_, BlockPos p_176372_2_, IBlockState p_176372_3_)
    {
        Block block = p_176372_1_.getBlockState(p_176372_2_).getBlock();

        if (!(block instanceof BlockDoor) && block != Blocks.field_150472_an && block != Blocks.LADDER && block != Blocks.field_150436_aH)
        {
            return block.material != Material.PORTAL && block.material != Material.STRUCTURE_VOID ? block.material.blocksMovement() : true;
        }
        else
        {
            return true;
        }
    }

    protected int func_176371_a(World p_176371_1_, BlockPos p_176371_2_, int p_176371_3_)
    {
        int i = this.func_189542_i(p_176371_1_.getBlockState(p_176371_2_));

        if (i < 0)
        {
            return p_176371_3_;
        }
        else
        {
            if (i == 0)
            {
                ++this.field_149815_a;
            }

            if (i >= 8)
            {
                i = 0;
            }

            return p_176371_3_ >= 0 && i >= p_176371_3_ ? p_176371_3_ : i;
        }
    }

    private boolean func_176373_h(World p_176373_1_, BlockPos p_176373_2_, IBlockState p_176373_3_)
    {
        Material material = p_176373_3_.getMaterial();
        return material != this.material && material != Material.LAVA && !this.func_176372_g(p_176373_1_, p_176373_2_, p_176373_3_);
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        if (!this.func_176365_e(p_176213_1_, p_176213_2_, p_176213_3_))
        {
            p_176213_1_.func_175684_a(p_176213_2_, this, this.tickRate(p_176213_1_));
        }
    }
}
