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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPane extends Block
{
    public static final PropertyBool field_176241_b = PropertyBool.create("north");
    public static final PropertyBool field_176242_M = PropertyBool.create("east");
    public static final PropertyBool field_176243_N = PropertyBool.create("south");
    public static final PropertyBool field_176244_O = PropertyBool.create("west");
    protected static final AxisAlignedBB[] field_185730_f = new AxisAlignedBB[] {new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
    private final boolean field_150099_b;

    protected BlockPane(Material p_i45675_1_, boolean p_i45675_2_)
    {
        super(p_i45675_1_);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176241_b, Boolean.valueOf(false)).func_177226_a(field_176242_M, Boolean.valueOf(false)).func_177226_a(field_176243_N, Boolean.valueOf(false)).func_177226_a(field_176244_O, Boolean.valueOf(false)));
        this.field_150099_b = p_i45675_2_;
        this.func_149647_a(CreativeTabs.DECORATIONS);
    }

    public void func_185477_a(IBlockState p_185477_1_, World p_185477_2_, BlockPos p_185477_3_, AxisAlignedBB p_185477_4_, List<AxisAlignedBB> p_185477_5_, @Nullable Entity p_185477_6_, boolean p_185477_7_)
    {
        if (!p_185477_7_)
        {
            p_185477_1_ = this.func_176221_a(p_185477_1_, p_185477_2_, p_185477_3_);
        }

        func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, field_185730_f[0]);

        if (((Boolean)p_185477_1_.get(field_176241_b)).booleanValue())
        {
            func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, field_185730_f[func_185729_a(EnumFacing.NORTH)]);
        }

        if (((Boolean)p_185477_1_.get(field_176243_N)).booleanValue())
        {
            func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, field_185730_f[func_185729_a(EnumFacing.SOUTH)]);
        }

        if (((Boolean)p_185477_1_.get(field_176242_M)).booleanValue())
        {
            func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, field_185730_f[func_185729_a(EnumFacing.EAST)]);
        }

        if (((Boolean)p_185477_1_.get(field_176244_O)).booleanValue())
        {
            func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, field_185730_f[func_185729_a(EnumFacing.WEST)]);
        }
    }

    private static int func_185729_a(EnumFacing p_185729_0_)
    {
        return 1 << p_185729_0_.getHorizontalIndex();
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        p_185496_1_ = this.func_176221_a(p_185496_1_, p_185496_2_, p_185496_3_);
        return field_185730_f[func_185728_i(p_185496_1_)];
    }

    private static int func_185728_i(IBlockState p_185728_0_)
    {
        int i = 0;

        if (((Boolean)p_185728_0_.get(field_176241_b)).booleanValue())
        {
            i |= func_185729_a(EnumFacing.NORTH);
        }

        if (((Boolean)p_185728_0_.get(field_176242_M)).booleanValue())
        {
            i |= func_185729_a(EnumFacing.EAST);
        }

        if (((Boolean)p_185728_0_.get(field_176243_N)).booleanValue())
        {
            i |= func_185729_a(EnumFacing.SOUTH);
        }

        if (((Boolean)p_185728_0_.get(field_176244_O)).booleanValue())
        {
            i |= func_185729_a(EnumFacing.WEST);
        }

        return i;
    }

    public IBlockState func_176221_a(IBlockState p_176221_1_, IBlockAccess p_176221_2_, BlockPos p_176221_3_)
    {
        return p_176221_1_.func_177226_a(field_176241_b, Boolean.valueOf(this.func_193393_b(p_176221_2_, p_176221_2_.getBlockState(p_176221_3_.north()), p_176221_3_.north(), EnumFacing.SOUTH))).func_177226_a(field_176243_N, Boolean.valueOf(this.func_193393_b(p_176221_2_, p_176221_2_.getBlockState(p_176221_3_.south()), p_176221_3_.south(), EnumFacing.NORTH))).func_177226_a(field_176244_O, Boolean.valueOf(this.func_193393_b(p_176221_2_, p_176221_2_.getBlockState(p_176221_3_.west()), p_176221_3_.west(), EnumFacing.EAST))).func_177226_a(field_176242_M, Boolean.valueOf(this.func_193393_b(p_176221_2_, p_176221_2_.getBlockState(p_176221_3_.east()), p_176221_3_.east(), EnumFacing.WEST)));
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return !this.field_150099_b ? Items.AIR : super.func_180660_a(p_180660_1_, p_180660_2_, p_180660_3_);
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    /**
     * ""
     */
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing p_176225_4_)
    {
        return blockAccess.getBlockState(pos.offset(p_176225_4_)).getBlock() == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, p_176225_4_);
    }

    public final boolean func_193393_b(IBlockAccess p_193393_1_, IBlockState p_193393_2_, BlockPos p_193393_3_, EnumFacing p_193393_4_)
    {
        Block block = p_193393_2_.getBlock();
        BlockFaceShape blockfaceshape = p_193393_2_.func_193401_d(p_193393_1_, p_193393_3_, p_193393_4_);
        return !func_193394_e(block) && blockfaceshape == BlockFaceShape.SOLID || blockfaceshape == BlockFaceShape.MIDDLE_POLE_THIN;
    }

    protected static boolean func_193394_e(Block p_193394_0_)
    {
        return p_193394_0_ instanceof BlockShulkerBox || p_193394_0_ instanceof BlockLeaves || p_193394_0_ == Blocks.BEACON || p_193394_0_ == Blocks.CAULDRON || p_193394_0_ == Blocks.GLOWSTONE || p_193394_0_ == Blocks.ICE || p_193394_0_ == Blocks.SEA_LANTERN || p_193394_0_ == Blocks.PISTON || p_193394_0_ == Blocks.STICKY_PISTON || p_193394_0_ == Blocks.PISTON_HEAD || p_193394_0_ == Blocks.MELON || p_193394_0_ == Blocks.PUMPKIN || p_193394_0_ == Blocks.field_150428_aP || p_193394_0_ == Blocks.BARRIER;
    }

    protected boolean func_149700_E()
    {
        return true;
    }

    public BlockRenderLayer func_180664_k()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return 0;
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
                return state.func_177226_a(field_176241_b, state.get(field_176243_N)).func_177226_a(field_176242_M, state.get(field_176244_O)).func_177226_a(field_176243_N, state.get(field_176241_b)).func_177226_a(field_176244_O, state.get(field_176242_M));

            case COUNTERCLOCKWISE_90:
                return state.func_177226_a(field_176241_b, state.get(field_176242_M)).func_177226_a(field_176242_M, state.get(field_176243_N)).func_177226_a(field_176243_N, state.get(field_176244_O)).func_177226_a(field_176244_O, state.get(field_176241_b));

            case CLOCKWISE_90:
                return state.func_177226_a(field_176241_b, state.get(field_176244_O)).func_177226_a(field_176242_M, state.get(field_176241_b)).func_177226_a(field_176243_N, state.get(field_176242_M)).func_177226_a(field_176244_O, state.get(field_176243_N));

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
                return state.func_177226_a(field_176241_b, state.get(field_176243_N)).func_177226_a(field_176243_N, state.get(field_176241_b));

            case FRONT_BACK:
                return state.func_177226_a(field_176242_M, state.get(field_176244_O)).func_177226_a(field_176244_O, state.get(field_176242_M));

            default:
                return super.mirror(state, mirrorIn);
        }
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176241_b, field_176242_M, field_176244_O, field_176243_N});
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return p_193383_4_ != EnumFacing.UP && p_193383_4_ != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE_THIN : BlockFaceShape.CENTER_SMALL;
    }
}
