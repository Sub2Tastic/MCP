package net.minecraft.block;

import com.google.common.base.MoreObjects;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWireHook extends Block
{
    public static final PropertyDirection FACING = BlockHorizontal.HORIZONTAL_FACING;
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyBool ATTACHED = PropertyBool.create("attached");
    protected static final AxisAlignedBB HOOK_NORTH_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.625D, 0.6875D, 0.625D, 1.0D);
    protected static final AxisAlignedBB HOOK_SOUTH_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.0D, 0.6875D, 0.625D, 0.375D);
    protected static final AxisAlignedBB HOOK_WEST_AABB = new AxisAlignedBB(0.625D, 0.0D, 0.3125D, 1.0D, 0.625D, 0.6875D);
    protected static final AxisAlignedBB HOOK_EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.3125D, 0.375D, 0.625D, 0.6875D);

    public BlockTripWireHook()
    {
        super(Material.MISCELLANEOUS);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(FACING, EnumFacing.NORTH).func_177226_a(POWERED, Boolean.valueOf(false)).func_177226_a(ATTACHED, Boolean.valueOf(false)));
        this.func_149647_a(CreativeTabs.REDSTONE);
        this.func_149675_a(true);
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        switch ((EnumFacing)p_185496_1_.get(FACING))
        {
            case EAST:
            default:
                return HOOK_EAST_AABB;

            case WEST:
                return HOOK_WEST_AABB;

            case SOUTH:
                return HOOK_SOUTH_AABB;

            case NORTH:
                return HOOK_NORTH_AABB;
        }
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState p_180646_1_, IBlockAccess p_180646_2_, BlockPos p_180646_3_)
    {
        return field_185506_k;
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
        EnumFacing enumfacing = p_176198_3_.getOpposite();
        BlockPos blockpos = p_176198_2_.offset(enumfacing);
        IBlockState iblockstate = p_176198_1_.getBlockState(blockpos);
        boolean flag = func_193382_c(iblockstate.getBlock());
        return !flag && p_176198_3_.getAxis().isHorizontal() && iblockstate.func_193401_d(p_176198_1_, blockpos, p_176198_3_) == BlockFaceShape.SOLID && !iblockstate.canProvidePower();
    }

    public boolean func_176196_c(World p_176196_1_, BlockPos p_176196_2_)
    {
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            if (this.func_176198_a(p_176196_1_, p_176196_2_, enumfacing))
            {
                return true;
            }
        }

        return false;
    }

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        IBlockState iblockstate = this.getDefaultState().func_177226_a(POWERED, Boolean.valueOf(false)).func_177226_a(ATTACHED, Boolean.valueOf(false));

        if (p_180642_3_.getAxis().isHorizontal())
        {
            iblockstate = iblockstate.func_177226_a(FACING, p_180642_3_);
        }

        return iblockstate;
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        this.calculateState(worldIn, pos, state, false, false, -1, (IBlockState)null);
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        if (p_189540_4_ != this)
        {
            if (this.func_176261_e(p_189540_2_, p_189540_3_, p_189540_1_))
            {
                EnumFacing enumfacing = (EnumFacing)p_189540_1_.get(FACING);

                if (!this.func_176198_a(p_189540_2_, p_189540_3_, enumfacing))
                {
                    this.func_176226_b(p_189540_2_, p_189540_3_, p_189540_1_, 0);
                    p_189540_2_.func_175698_g(p_189540_3_);
                }
            }
        }
    }

    public void calculateState(World worldIn, BlockPos pos, IBlockState hookState, boolean p_176260_4_, boolean p_176260_5_, int p_176260_6_, @Nullable IBlockState state)
    {
        EnumFacing enumfacing = (EnumFacing)hookState.get(FACING);
        boolean flag = ((Boolean)hookState.get(ATTACHED)).booleanValue();
        boolean flag1 = ((Boolean)hookState.get(POWERED)).booleanValue();
        boolean flag2 = !p_176260_4_;
        boolean flag3 = false;
        int i = 0;
        IBlockState[] aiblockstate = new IBlockState[42];

        for (int j = 1; j < 42; ++j)
        {
            BlockPos blockpos = pos.offset(enumfacing, j);
            IBlockState iblockstate = worldIn.getBlockState(blockpos);

            if (iblockstate.getBlock() == Blocks.TRIPWIRE_HOOK)
            {
                if (iblockstate.get(FACING) == enumfacing.getOpposite())
                {
                    i = j;
                }

                break;
            }

            if (iblockstate.getBlock() != Blocks.TRIPWIRE && j != p_176260_6_)
            {
                aiblockstate[j] = null;
                flag2 = false;
            }
            else
            {
                if (j == p_176260_6_)
                {
                    iblockstate = (IBlockState)MoreObjects.firstNonNull(state, iblockstate);
                }

                boolean flag4 = !((Boolean)iblockstate.get(BlockTripWire.DISARMED)).booleanValue();
                boolean flag5 = ((Boolean)iblockstate.get(BlockTripWire.POWERED)).booleanValue();
                flag3 |= flag4 && flag5;
                aiblockstate[j] = iblockstate;

                if (j == p_176260_6_)
                {
                    worldIn.func_175684_a(pos, this, this.tickRate(worldIn));
                    flag2 &= flag4;
                }
            }
        }

        flag2 = flag2 & i > 1;
        flag3 = flag3 & flag2;
        IBlockState iblockstate1 = this.getDefaultState().func_177226_a(ATTACHED, Boolean.valueOf(flag2)).func_177226_a(POWERED, Boolean.valueOf(flag3));

        if (i > 0)
        {
            BlockPos blockpos1 = pos.offset(enumfacing, i);
            EnumFacing enumfacing1 = enumfacing.getOpposite();
            worldIn.setBlockState(blockpos1, iblockstate1.func_177226_a(FACING, enumfacing1), 3);
            this.notifyNeighbors(worldIn, blockpos1, enumfacing1);
            this.playSound(worldIn, blockpos1, flag2, flag3, flag, flag1);
        }

        this.playSound(worldIn, pos, flag2, flag3, flag, flag1);

        if (!p_176260_4_)
        {
            worldIn.setBlockState(pos, iblockstate1.func_177226_a(FACING, enumfacing), 3);

            if (p_176260_5_)
            {
                this.notifyNeighbors(worldIn, pos, enumfacing);
            }
        }

        if (flag != flag2)
        {
            for (int k = 1; k < i; ++k)
            {
                BlockPos blockpos2 = pos.offset(enumfacing, k);
                IBlockState iblockstate2 = aiblockstate[k];

                if (iblockstate2 != null && worldIn.getBlockState(blockpos2).getMaterial() != Material.AIR)
                {
                    worldIn.setBlockState(blockpos2, iblockstate2.func_177226_a(ATTACHED, Boolean.valueOf(flag2)), 3);
                }
            }
        }
    }

    public void func_180645_a(World p_180645_1_, BlockPos p_180645_2_, IBlockState p_180645_3_, Random p_180645_4_)
    {
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        this.calculateState(p_180650_1_, p_180650_2_, p_180650_3_, false, true, -1, (IBlockState)null);
    }

    private void playSound(World worldIn, BlockPos pos, boolean p_180694_3_, boolean p_180694_4_, boolean p_180694_5_, boolean p_180694_6_)
    {
        if (p_180694_4_ && !p_180694_6_)
        {
            worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_TRIPWIRE_CLICK_ON, SoundCategory.BLOCKS, 0.4F, 0.6F);
        }
        else if (!p_180694_4_ && p_180694_6_)
        {
            worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_TRIPWIRE_CLICK_OFF, SoundCategory.BLOCKS, 0.4F, 0.5F);
        }
        else if (p_180694_3_ && !p_180694_5_)
        {
            worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_TRIPWIRE_ATTACH, SoundCategory.BLOCKS, 0.4F, 0.7F);
        }
        else if (!p_180694_3_ && p_180694_5_)
        {
            worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_TRIPWIRE_DETACH, SoundCategory.BLOCKS, 0.4F, 1.2F / (worldIn.rand.nextFloat() * 0.2F + 0.9F));
        }
    }

    private void notifyNeighbors(World worldIn, BlockPos pos, EnumFacing side)
    {
        worldIn.func_175685_c(pos, this, false);
        worldIn.func_175685_c(pos.offset(side.getOpposite()), this, false);
    }

    private boolean func_176261_e(World p_176261_1_, BlockPos p_176261_2_, IBlockState p_176261_3_)
    {
        if (!this.func_176196_c(p_176261_1_, p_176261_2_))
        {
            this.func_176226_b(p_176261_1_, p_176261_2_, p_176261_3_, 0);
            p_176261_1_.func_175698_g(p_176261_2_);
            return false;
        }
        else
        {
            return true;
        }
    }

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
        boolean flag = ((Boolean)p_180663_3_.get(ATTACHED)).booleanValue();
        boolean flag1 = ((Boolean)p_180663_3_.get(POWERED)).booleanValue();

        if (flag || flag1)
        {
            this.calculateState(p_180663_1_, p_180663_2_, p_180663_3_, true, false, -1, (IBlockState)null);
        }

        if (flag1)
        {
            p_180663_1_.func_175685_c(p_180663_2_, this, false);
            p_180663_1_.func_175685_c(p_180663_2_.offset(((EnumFacing)p_180663_3_.get(FACING)).getOpposite()), this, false);
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

    public BlockRenderLayer func_180664_k()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(FACING, EnumFacing.byHorizontalIndex(p_176203_1_ & 3)).func_177226_a(POWERED, Boolean.valueOf((p_176203_1_ & 8) > 0)).func_177226_a(ATTACHED, Boolean.valueOf((p_176203_1_ & 4) > 0));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;
        i = i | ((EnumFacing)p_176201_1_.get(FACING)).getHorizontalIndex();

        if (((Boolean)p_176201_1_.get(POWERED)).booleanValue())
        {
            i |= 8;
        }

        if (((Boolean)p_176201_1_.get(ATTACHED)).booleanValue())
        {
            i |= 4;
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
        return new BlockStateContainer(this, new IProperty[] {FACING, POWERED, ATTACHED});
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
