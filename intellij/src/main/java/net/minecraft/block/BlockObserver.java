package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockObserver extends BlockDirectional
{
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public BlockObserver()
    {
        super(Material.ROCK);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(FACING, EnumFacing.SOUTH).func_177226_a(POWERED, Boolean.valueOf(false)));
        this.func_149647_a(CreativeTabs.REDSTONE);
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, POWERED});
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
     * fine.
     */
    public IBlockState rotate(IBlockState state, Rotation rot)
    {
        return state.func_177226_a(FACING, rot.rotate((EnumFacing)state.get(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.toRotation((EnumFacing)state.get(FACING)));
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        if (((Boolean)p_180650_3_.get(POWERED)).booleanValue())
        {
            p_180650_1_.setBlockState(p_180650_2_, p_180650_3_.func_177226_a(POWERED, Boolean.valueOf(false)), 2);
        }
        else
        {
            p_180650_1_.setBlockState(p_180650_2_, p_180650_3_.func_177226_a(POWERED, Boolean.valueOf(true)), 2);
            p_180650_1_.func_175684_a(p_180650_2_, this, 2);
        }

        this.updateNeighborsInFront(p_180650_1_, p_180650_2_, p_180650_3_);
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
    }

    public void func_190962_b(IBlockState p_190962_1_, World p_190962_2_, BlockPos p_190962_3_, Block p_190962_4_, BlockPos p_190962_5_)
    {
        if (!p_190962_2_.isRemote && p_190962_3_.offset((EnumFacing)p_190962_1_.get(FACING)).equals(p_190962_5_))
        {
            this.func_190960_d(p_190962_1_, p_190962_2_, p_190962_3_);
        }
    }

    private void func_190960_d(IBlockState p_190960_1_, World p_190960_2_, BlockPos p_190960_3_)
    {
        if (!((Boolean)p_190960_1_.get(POWERED)).booleanValue())
        {
            if (!p_190960_2_.func_184145_b(p_190960_3_, this))
            {
                p_190960_2_.func_175684_a(p_190960_3_, this, 2);
            }
        }
    }

    protected void updateNeighborsInFront(World worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing enumfacing = (EnumFacing)state.get(FACING);
        BlockPos blockpos = pos.offset(enumfacing.getOpposite());
        worldIn.neighborChanged(blockpos, this, pos);
        worldIn.notifyNeighborsOfStateExcept(blockpos, this, enumfacing);
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     * @deprecated call via {@link IBlockState#canProvidePower()} whenever possible. Implementing/overriding is fine.
     */
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    /**
     * @deprecated call via {@link IBlockState#getStrongPower(IBlockAccess,BlockPos,EnumFacing)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return blockState.getWeakPower(blockAccess, pos, side);
    }

    /**
     * @deprecated call via {@link IBlockState#getWeakPower(IBlockAccess,BlockPos,EnumFacing)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return ((Boolean)blockState.get(POWERED)).booleanValue() && blockState.get(FACING) == side ? 15 : 0;
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        if (!p_176213_1_.isRemote)
        {
            if (((Boolean)p_176213_3_.get(POWERED)).booleanValue())
            {
                this.func_180650_b(p_176213_1_, p_176213_2_, p_176213_3_, p_176213_1_.rand);
            }

            this.func_190960_d(p_176213_3_, p_176213_1_, p_176213_2_);
        }
    }

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
        if (((Boolean)p_180663_3_.get(POWERED)).booleanValue() && p_180663_1_.func_184145_b(p_180663_2_, this))
        {
            this.updateNeighborsInFront(p_180663_1_, p_180663_2_, p_180663_3_.func_177226_a(POWERED, Boolean.valueOf(false)));
        }
    }

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        return this.getDefaultState().func_177226_a(FACING, EnumFacing.func_190914_a(p_180642_2_, p_180642_8_).getOpposite());
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;
        i = i | ((EnumFacing)p_176201_1_.get(FACING)).getIndex();

        if (((Boolean)p_176201_1_.get(POWERED)).booleanValue())
        {
            i |= 8;
        }

        return i;
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(FACING, EnumFacing.byIndex(p_176203_1_ & 7));
    }
}
