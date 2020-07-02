package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockButton extends BlockDirectional
{
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    protected static final AxisAlignedBB field_185618_b = new AxisAlignedBB(0.3125D, 0.875D, 0.375D, 0.6875D, 1.0D, 0.625D);
    protected static final AxisAlignedBB field_185620_c = new AxisAlignedBB(0.3125D, 0.0D, 0.375D, 0.6875D, 0.125D, 0.625D);
    protected static final AxisAlignedBB AABB_NORTH_OFF = new AxisAlignedBB(0.3125D, 0.375D, 0.875D, 0.6875D, 0.625D, 1.0D);
    protected static final AxisAlignedBB AABB_SOUTH_OFF = new AxisAlignedBB(0.3125D, 0.375D, 0.0D, 0.6875D, 0.625D, 0.125D);
    protected static final AxisAlignedBB AABB_WEST_OFF = new AxisAlignedBB(0.875D, 0.375D, 0.3125D, 1.0D, 0.625D, 0.6875D);
    protected static final AxisAlignedBB AABB_EAST_OFF = new AxisAlignedBB(0.0D, 0.375D, 0.3125D, 0.125D, 0.625D, 0.6875D);
    protected static final AxisAlignedBB field_185619_B = new AxisAlignedBB(0.3125D, 0.9375D, 0.375D, 0.6875D, 1.0D, 0.625D);
    protected static final AxisAlignedBB field_185621_C = new AxisAlignedBB(0.3125D, 0.0D, 0.375D, 0.6875D, 0.0625D, 0.625D);
    protected static final AxisAlignedBB AABB_NORTH_ON = new AxisAlignedBB(0.3125D, 0.375D, 0.9375D, 0.6875D, 0.625D, 1.0D);
    protected static final AxisAlignedBB AABB_SOUTH_ON = new AxisAlignedBB(0.3125D, 0.375D, 0.0D, 0.6875D, 0.625D, 0.0625D);
    protected static final AxisAlignedBB AABB_WEST_ON = new AxisAlignedBB(0.9375D, 0.375D, 0.3125D, 1.0D, 0.625D, 0.6875D);
    protected static final AxisAlignedBB AABB_EAST_ON = new AxisAlignedBB(0.0D, 0.375D, 0.3125D, 0.0625D, 0.625D, 0.6875D);
    private final boolean wooden;

    protected BlockButton(boolean p_i45396_1_)
    {
        super(Material.MISCELLANEOUS);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(FACING, EnumFacing.NORTH).func_177226_a(POWERED, Boolean.valueOf(false)));
        this.func_149675_a(true);
        this.func_149647_a(CreativeTabs.REDSTONE);
        this.wooden = p_i45396_1_;
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState p_180646_1_, IBlockAccess p_180646_2_, BlockPos p_180646_3_)
    {
        return field_185506_k;
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return this.wooden ? 30 : 20;
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    public boolean func_176198_a(World p_176198_1_, BlockPos p_176198_2_, EnumFacing p_176198_3_)
    {
        return func_181088_a(p_176198_1_, p_176198_2_, p_176198_3_);
    }

    public boolean func_176196_c(World p_176196_1_, BlockPos p_176196_2_)
    {
        for (EnumFacing enumfacing : EnumFacing.values())
        {
            if (func_181088_a(p_176196_1_, p_176196_2_, enumfacing))
            {
                return true;
            }
        }

        return false;
    }

    protected static boolean func_181088_a(World p_181088_0_, BlockPos p_181088_1_, EnumFacing p_181088_2_)
    {
        BlockPos blockpos = p_181088_1_.offset(p_181088_2_.getOpposite());
        IBlockState iblockstate = p_181088_0_.getBlockState(blockpos);
        boolean flag = iblockstate.func_193401_d(p_181088_0_, blockpos, p_181088_2_) == BlockFaceShape.SOLID;
        Block block = iblockstate.getBlock();

        if (p_181088_2_ == EnumFacing.UP)
        {
            return block == Blocks.HOPPER || !func_193384_b(block) && flag;
        }
        else
        {
            return !func_193382_c(block) && flag;
        }
    }

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        return func_181088_a(p_180642_1_, p_180642_2_, p_180642_3_) ? this.getDefaultState().func_177226_a(FACING, p_180642_3_).func_177226_a(POWERED, Boolean.valueOf(false)) : this.getDefaultState().func_177226_a(FACING, EnumFacing.DOWN).func_177226_a(POWERED, Boolean.valueOf(false));
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        if (this.func_176583_e(p_189540_2_, p_189540_3_, p_189540_1_) && !func_181088_a(p_189540_2_, p_189540_3_, (EnumFacing)p_189540_1_.get(FACING)))
        {
            this.func_176226_b(p_189540_2_, p_189540_3_, p_189540_1_, 0);
            p_189540_2_.func_175698_g(p_189540_3_);
        }
    }

    private boolean func_176583_e(World p_176583_1_, BlockPos p_176583_2_, IBlockState p_176583_3_)
    {
        if (this.func_176196_c(p_176583_1_, p_176583_2_))
        {
            return true;
        }
        else
        {
            this.func_176226_b(p_176583_1_, p_176583_2_, p_176583_3_, 0);
            p_176583_1_.func_175698_g(p_176583_2_);
            return false;
        }
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        EnumFacing enumfacing = (EnumFacing)p_185496_1_.get(FACING);
        boolean flag = ((Boolean)p_185496_1_.get(POWERED)).booleanValue();

        switch (enumfacing)
        {
            case EAST:
                return flag ? AABB_EAST_ON : AABB_EAST_OFF;

            case WEST:
                return flag ? AABB_WEST_ON : AABB_WEST_OFF;

            case SOUTH:
                return flag ? AABB_SOUTH_ON : AABB_SOUTH_OFF;

            case NORTH:
            default:
                return flag ? AABB_NORTH_ON : AABB_NORTH_OFF;

            case UP:
                return flag ? field_185621_C : field_185620_c;

            case DOWN:
                return flag ? field_185619_B : field_185618_b;
        }
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        if (((Boolean)p_180639_3_.get(POWERED)).booleanValue())
        {
            return true;
        }
        else
        {
            p_180639_1_.setBlockState(p_180639_2_, p_180639_3_.func_177226_a(POWERED, Boolean.valueOf(true)), 3);
            p_180639_1_.func_175704_b(p_180639_2_, p_180639_2_);
            this.func_185615_a(p_180639_4_, p_180639_1_, p_180639_2_);
            this.func_176582_b(p_180639_1_, p_180639_2_, (EnumFacing)p_180639_3_.get(FACING));
            p_180639_1_.func_175684_a(p_180639_2_, this, this.tickRate(p_180639_1_));
            return true;
        }
    }

    protected abstract void func_185615_a(EntityPlayer p_185615_1_, World p_185615_2_, BlockPos p_185615_3_);

    protected abstract void func_185617_b(World p_185617_1_, BlockPos p_185617_2_);

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
        if (((Boolean)p_180663_3_.get(POWERED)).booleanValue())
        {
            this.func_176582_b(p_180663_1_, p_180663_2_, (EnumFacing)p_180663_3_.get(FACING));
        }

        super.func_180663_b(p_180663_1_, p_180663_2_, p_180663_3_);
    }

    /**
     * @deprecated call via {@link IBlockState#getWeakPower(IBlockAccess,BlockPos,EnumFacing)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return ((Boolean)blockState.get(POWERED)).booleanValue() ? 15 : 0;
    }

    /**
     * @deprecated call via {@link IBlockState#getStrongPower(IBlockAccess,BlockPos,EnumFacing)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        if (!((Boolean)blockState.get(POWERED)).booleanValue())
        {
            return 0;
        }
        else
        {
            return blockState.get(FACING) == side ? 15 : 0;
        }
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     * @deprecated call via {@link IBlockState#canProvidePower()} whenever possible. Implementing/overriding is fine.
     */
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    public void func_180645_a(World p_180645_1_, BlockPos p_180645_2_, IBlockState p_180645_3_, Random p_180645_4_)
    {
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        if (!p_180650_1_.isRemote)
        {
            if (((Boolean)p_180650_3_.get(POWERED)).booleanValue())
            {
                if (this.wooden)
                {
                    this.checkPressed(p_180650_3_, p_180650_1_, p_180650_2_);
                }
                else
                {
                    p_180650_1_.setBlockState(p_180650_2_, p_180650_3_.func_177226_a(POWERED, Boolean.valueOf(false)));
                    this.func_176582_b(p_180650_1_, p_180650_2_, (EnumFacing)p_180650_3_.get(FACING));
                    this.func_185617_b(p_180650_1_, p_180650_2_);
                    p_180650_1_.func_175704_b(p_180650_2_, p_180650_2_);
                }
            }
        }
    }

    public void func_180634_a(World p_180634_1_, BlockPos p_180634_2_, IBlockState p_180634_3_, Entity p_180634_4_)
    {
        if (!p_180634_1_.isRemote)
        {
            if (this.wooden)
            {
                if (!((Boolean)p_180634_3_.get(POWERED)).booleanValue())
                {
                    this.checkPressed(p_180634_3_, p_180634_1_, p_180634_2_);
                }
            }
        }
    }

    private void checkPressed(IBlockState state, World worldIn, BlockPos pos)
    {
        List <? extends Entity > list = worldIn.<Entity>func_72872_a(EntityArrow.class, state.func_185900_c(worldIn, pos).offset(pos));
        boolean flag = !list.isEmpty();
        boolean flag1 = ((Boolean)state.get(POWERED)).booleanValue();

        if (flag && !flag1)
        {
            worldIn.setBlockState(pos, state.func_177226_a(POWERED, Boolean.valueOf(true)));
            this.func_176582_b(worldIn, pos, (EnumFacing)state.get(FACING));
            worldIn.func_175704_b(pos, pos);
            this.func_185615_a((EntityPlayer)null, worldIn, pos);
        }

        if (!flag && flag1)
        {
            worldIn.setBlockState(pos, state.func_177226_a(POWERED, Boolean.valueOf(false)));
            this.func_176582_b(worldIn, pos, (EnumFacing)state.get(FACING));
            worldIn.func_175704_b(pos, pos);
            this.func_185617_b(worldIn, pos);
        }

        if (flag)
        {
            worldIn.func_175684_a(new BlockPos(pos), this, this.tickRate(worldIn));
        }
    }

    private void func_176582_b(World p_176582_1_, BlockPos p_176582_2_, EnumFacing p_176582_3_)
    {
        p_176582_1_.func_175685_c(p_176582_2_, this, false);
        p_176582_1_.func_175685_c(p_176582_2_.offset(p_176582_3_.getOpposite()), this, false);
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        EnumFacing enumfacing;

        switch (p_176203_1_ & 7)
        {
            case 0:
                enumfacing = EnumFacing.DOWN;
                break;

            case 1:
                enumfacing = EnumFacing.EAST;
                break;

            case 2:
                enumfacing = EnumFacing.WEST;
                break;

            case 3:
                enumfacing = EnumFacing.SOUTH;
                break;

            case 4:
                enumfacing = EnumFacing.NORTH;
                break;

            case 5:
            default:
                enumfacing = EnumFacing.UP;
        }

        return this.getDefaultState().func_177226_a(FACING, enumfacing).func_177226_a(POWERED, Boolean.valueOf((p_176203_1_ & 8) > 0));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i;

        switch ((EnumFacing)p_176201_1_.get(FACING))
        {
            case EAST:
                i = 1;
                break;

            case WEST:
                i = 2;
                break;

            case SOUTH:
                i = 3;
                break;

            case NORTH:
                i = 4;
                break;

            case UP:
            default:
                i = 5;
                break;

            case DOWN:
                i = 0;
        }

        if (((Boolean)p_176201_1_.get(POWERED)).booleanValue())
        {
            i |= 8;
        }

        return i;
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

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, POWERED});
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
