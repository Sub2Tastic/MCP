package net.minecraft.block;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemLead;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFence extends Block
{
    public static final PropertyBool field_176526_a = PropertyBool.create("north");
    public static final PropertyBool field_176525_b = PropertyBool.create("east");
    public static final PropertyBool field_176527_M = PropertyBool.create("south");
    public static final PropertyBool field_176528_N = PropertyBool.create("west");
    protected static final AxisAlignedBB[] field_185670_e = new AxisAlignedBB[] {new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D), new AxisAlignedBB(0.0D, 0.0D, 0.375D, 0.625D, 1.0D, 1.0D), new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.625D, 1.0D, 0.625D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D), new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.375D, 0.0D, 0.0D, 1.0D, 1.0D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.625D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
    public static final AxisAlignedBB field_185671_f = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.5D, 0.625D);
    public static final AxisAlignedBB field_185672_g = new AxisAlignedBB(0.375D, 0.0D, 0.625D, 0.625D, 1.5D, 1.0D);
    public static final AxisAlignedBB field_185667_B = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 0.375D, 1.5D, 0.625D);
    public static final AxisAlignedBB field_185668_C = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.5D, 0.375D);
    public static final AxisAlignedBB field_185669_D = new AxisAlignedBB(0.625D, 0.0D, 0.375D, 1.0D, 1.5D, 0.625D);

    public BlockFence(Material p_i46395_1_, MapColor p_i46395_2_)
    {
        super(p_i46395_1_, p_i46395_2_);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176526_a, Boolean.valueOf(false)).func_177226_a(field_176525_b, Boolean.valueOf(false)).func_177226_a(field_176527_M, Boolean.valueOf(false)).func_177226_a(field_176528_N, Boolean.valueOf(false)));
        this.func_149647_a(CreativeTabs.DECORATIONS);
    }

    public void func_185477_a(IBlockState p_185477_1_, World p_185477_2_, BlockPos p_185477_3_, AxisAlignedBB p_185477_4_, List<AxisAlignedBB> p_185477_5_, @Nullable Entity p_185477_6_, boolean p_185477_7_)
    {
        if (!p_185477_7_)
        {
            p_185477_1_ = p_185477_1_.func_185899_b(p_185477_2_, p_185477_3_);
        }

        func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, field_185671_f);

        if (((Boolean)p_185477_1_.get(field_176526_a)).booleanValue())
        {
            func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, field_185668_C);
        }

        if (((Boolean)p_185477_1_.get(field_176525_b)).booleanValue())
        {
            func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, field_185669_D);
        }

        if (((Boolean)p_185477_1_.get(field_176527_M)).booleanValue())
        {
            func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, field_185672_g);
        }

        if (((Boolean)p_185477_1_.get(field_176528_N)).booleanValue())
        {
            func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, field_185667_B);
        }
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        p_185496_1_ = this.func_176221_a(p_185496_1_, p_185496_2_, p_185496_3_);
        return field_185670_e[func_185666_i(p_185496_1_)];
    }

    private static int func_185666_i(IBlockState p_185666_0_)
    {
        int i = 0;

        if (((Boolean)p_185666_0_.get(field_176526_a)).booleanValue())
        {
            i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
        }

        if (((Boolean)p_185666_0_.get(field_176525_b)).booleanValue())
        {
            i |= 1 << EnumFacing.EAST.getHorizontalIndex();
        }

        if (((Boolean)p_185666_0_.get(field_176527_M)).booleanValue())
        {
            i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
        }

        if (((Boolean)p_185666_0_.get(field_176528_N)).booleanValue())
        {
            i |= 1 << EnumFacing.WEST.getHorizontalIndex();
        }

        return i;
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    public boolean func_176205_b(IBlockAccess p_176205_1_, BlockPos p_176205_2_)
    {
        return false;
    }

    public boolean func_176524_e(IBlockAccess p_176524_1_, BlockPos p_176524_2_, EnumFacing p_176524_3_)
    {
        IBlockState iblockstate = p_176524_1_.getBlockState(p_176524_2_);
        BlockFaceShape blockfaceshape = iblockstate.func_193401_d(p_176524_1_, p_176524_2_, p_176524_3_);
        Block block = iblockstate.getBlock();
        boolean flag = blockfaceshape == BlockFaceShape.MIDDLE_POLE && (iblockstate.getMaterial() == this.material || block instanceof BlockFenceGate);
        return !func_194142_e(block) && blockfaceshape == BlockFaceShape.SOLID || flag;
    }

    protected static boolean func_194142_e(Block p_194142_0_)
    {
        return Block.func_193382_c(p_194142_0_) || p_194142_0_ == Blocks.BARRIER || p_194142_0_ == Blocks.MELON || p_194142_0_ == Blocks.PUMPKIN || p_194142_0_ == Blocks.field_150428_aP;
    }

    /**
     * ""
     */
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing p_176225_4_)
    {
        return true;
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        if (!p_180639_1_.isRemote)
        {
            return ItemLead.func_180618_a(p_180639_4_, p_180639_1_, p_180639_2_);
        }
        else
        {
            ItemStack itemstack = p_180639_4_.getHeldItem(p_180639_5_);
            return itemstack.getItem() == Items.LEAD || itemstack.isEmpty();
        }
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return 0;
    }

    public IBlockState func_176221_a(IBlockState p_176221_1_, IBlockAccess p_176221_2_, BlockPos p_176221_3_)
    {
        return p_176221_1_.func_177226_a(field_176526_a, Boolean.valueOf(this.func_176524_e(p_176221_2_, p_176221_3_.north(), EnumFacing.SOUTH))).func_177226_a(field_176525_b, Boolean.valueOf(this.func_176524_e(p_176221_2_, p_176221_3_.east(), EnumFacing.WEST))).func_177226_a(field_176527_M, Boolean.valueOf(this.func_176524_e(p_176221_2_, p_176221_3_.south(), EnumFacing.NORTH))).func_177226_a(field_176528_N, Boolean.valueOf(this.func_176524_e(p_176221_2_, p_176221_3_.west(), EnumFacing.EAST)));
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
     * fine.
     */
    public IBlockState rotate(IBlockState state, Rotation rot)
    {
        switch (rot)
        {
            case CLOCKWISE_180:
                return state.func_177226_a(field_176526_a, state.get(field_176527_M)).func_177226_a(field_176525_b, state.get(field_176528_N)).func_177226_a(field_176527_M, state.get(field_176526_a)).func_177226_a(field_176528_N, state.get(field_176525_b));

            case COUNTERCLOCKWISE_90:
                return state.func_177226_a(field_176526_a, state.get(field_176525_b)).func_177226_a(field_176525_b, state.get(field_176527_M)).func_177226_a(field_176527_M, state.get(field_176528_N)).func_177226_a(field_176528_N, state.get(field_176526_a));

            case CLOCKWISE_90:
                return state.func_177226_a(field_176526_a, state.get(field_176528_N)).func_177226_a(field_176525_b, state.get(field_176526_a)).func_177226_a(field_176527_M, state.get(field_176525_b)).func_177226_a(field_176528_N, state.get(field_176527_M));

            default:
                return state;
        }
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
        switch (mirrorIn)
        {
            case LEFT_RIGHT:
                return state.func_177226_a(field_176526_a, state.get(field_176527_M)).func_177226_a(field_176527_M, state.get(field_176526_a));

            case FRONT_BACK:
                return state.func_177226_a(field_176525_b, state.get(field_176528_N)).func_177226_a(field_176528_N, state.get(field_176525_b));

            default:
                return super.mirror(state, mirrorIn);
        }
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176526_a, field_176525_b, field_176528_N, field_176527_M});
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return p_193383_4_ != EnumFacing.UP && p_193383_4_ != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE : BlockFaceShape.CENTER;
    }
}
