package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCocoa extends BlockHorizontal implements IGrowable
{
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 2);
    protected static final AxisAlignedBB[] COCOA_EAST_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.6875D, 0.4375D, 0.375D, 0.9375D, 0.75D, 0.625D), new AxisAlignedBB(0.5625D, 0.3125D, 0.3125D, 0.9375D, 0.75D, 0.6875D), new AxisAlignedBB(0.4375D, 0.1875D, 0.25D, 0.9375D, 0.75D, 0.75D)};
    protected static final AxisAlignedBB[] COCOA_WEST_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.0625D, 0.4375D, 0.375D, 0.3125D, 0.75D, 0.625D), new AxisAlignedBB(0.0625D, 0.3125D, 0.3125D, 0.4375D, 0.75D, 0.6875D), new AxisAlignedBB(0.0625D, 0.1875D, 0.25D, 0.5625D, 0.75D, 0.75D)};
    protected static final AxisAlignedBB[] COCOA_NORTH_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.375D, 0.4375D, 0.0625D, 0.625D, 0.75D, 0.3125D), new AxisAlignedBB(0.3125D, 0.3125D, 0.0625D, 0.6875D, 0.75D, 0.4375D), new AxisAlignedBB(0.25D, 0.1875D, 0.0625D, 0.75D, 0.75D, 0.5625D)};
    protected static final AxisAlignedBB[] COCOA_SOUTH_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.375D, 0.4375D, 0.6875D, 0.625D, 0.75D, 0.9375D), new AxisAlignedBB(0.3125D, 0.3125D, 0.5625D, 0.6875D, 0.75D, 0.9375D), new AxisAlignedBB(0.25D, 0.1875D, 0.4375D, 0.75D, 0.75D, 0.9375D)};

    public BlockCocoa()
    {
        super(Material.PLANTS);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(HORIZONTAL_FACING, EnumFacing.NORTH).func_177226_a(AGE, Integer.valueOf(0)));
        this.func_149675_a(true);
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        if (!this.func_176499_e(p_180650_1_, p_180650_2_, p_180650_3_))
        {
            this.func_176500_f(p_180650_1_, p_180650_2_, p_180650_3_);
        }
        else if (p_180650_1_.rand.nextInt(5) == 0)
        {
            int i = ((Integer)p_180650_3_.get(AGE)).intValue();

            if (i < 2)
            {
                p_180650_1_.setBlockState(p_180650_2_, p_180650_3_.func_177226_a(AGE, Integer.valueOf(i + 1)), 2);
            }
        }
    }

    public boolean func_176499_e(World p_176499_1_, BlockPos p_176499_2_, IBlockState p_176499_3_)
    {
        p_176499_2_ = p_176499_2_.offset((EnumFacing)p_176499_3_.get(HORIZONTAL_FACING));
        IBlockState iblockstate = p_176499_1_.getBlockState(p_176499_2_);
        return iblockstate.getBlock() == Blocks.field_150364_r && iblockstate.get(BlockOldLog.field_176301_b) == BlockPlanks.EnumType.JUNGLE;
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        int i = ((Integer)p_185496_1_.get(AGE)).intValue();

        switch ((EnumFacing)p_185496_1_.get(HORIZONTAL_FACING))
        {
            case SOUTH:
                return COCOA_SOUTH_AABB[i];

            case NORTH:
            default:
                return COCOA_NORTH_AABB[i];

            case WEST:
                return COCOA_WEST_AABB[i];

            case EAST:
                return COCOA_EAST_AABB[i];
        }
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
     * fine.
     */
    public IBlockState rotate(IBlockState state, Rotation rot)
    {
        return state.func_177226_a(HORIZONTAL_FACING, rot.rotate((EnumFacing)state.get(HORIZONTAL_FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.toRotation((EnumFacing)state.get(HORIZONTAL_FACING)));
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        EnumFacing enumfacing = EnumFacing.fromAngle((double)placer.rotationYaw);
        worldIn.setBlockState(pos, state.func_177226_a(HORIZONTAL_FACING, enumfacing), 2);
    }

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        if (!p_180642_3_.getAxis().isHorizontal())
        {
            p_180642_3_ = EnumFacing.NORTH;
        }

        return this.getDefaultState().func_177226_a(HORIZONTAL_FACING, p_180642_3_.getOpposite()).func_177226_a(AGE, Integer.valueOf(0));
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        if (!this.func_176499_e(p_189540_2_, p_189540_3_, p_189540_1_))
        {
            this.func_176500_f(p_189540_2_, p_189540_3_, p_189540_1_);
        }
    }

    private void func_176500_f(World p_176500_1_, BlockPos p_176500_2_, IBlockState p_176500_3_)
    {
        p_176500_1_.setBlockState(p_176500_2_, Blocks.AIR.getDefaultState(), 3);
        this.func_176226_b(p_176500_1_, p_176500_2_, p_176500_3_, 0);
    }

    public void func_180653_a(World p_180653_1_, BlockPos p_180653_2_, IBlockState p_180653_3_, float p_180653_4_, int p_180653_5_)
    {
        int i = ((Integer)p_180653_3_.get(AGE)).intValue();
        int j = 1;

        if (i >= 2)
        {
            j = 3;
        }

        for (int k = 0; k < j; ++k)
        {
            spawnAsEntity(p_180653_1_, p_180653_2_, new ItemStack(Items.field_151100_aR, 1, EnumDyeColor.BROWN.func_176767_b()));
        }
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Items.field_151100_aR, 1, EnumDyeColor.BROWN.func_176767_b());
    }

    /**
     * Whether this IGrowable can grow
     */
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return ((Integer)state.get(AGE)).intValue() < 2;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }

    public void func_176474_b(World p_176474_1_, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_)
    {
        p_176474_1_.setBlockState(p_176474_3_, p_176474_4_.func_177226_a(AGE, Integer.valueOf(((Integer)p_176474_4_.get(AGE)).intValue() + 1)), 2);
    }

    public BlockRenderLayer func_180664_k()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(HORIZONTAL_FACING, EnumFacing.byHorizontalIndex(p_176203_1_)).func_177226_a(AGE, Integer.valueOf((p_176203_1_ & 15) >> 2));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;
        i = i | ((EnumFacing)p_176201_1_.get(HORIZONTAL_FACING)).getHorizontalIndex();
        i = i | ((Integer)p_176201_1_.get(AGE)).intValue() << 2;
        return i;
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {HORIZONTAL_FACING, AGE});
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
