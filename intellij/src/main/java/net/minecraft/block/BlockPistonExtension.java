package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonExtension extends BlockDirectional
{
    public static final PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE = PropertyEnum.<BlockPistonExtension.EnumPistonType>create("type", BlockPistonExtension.EnumPistonType.class);
    public static final PropertyBool SHORT = PropertyBool.create("short");
    protected static final AxisAlignedBB PISTON_EXTENSION_EAST_AABB = new AxisAlignedBB(0.75D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB PISTON_EXTENSION_WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.25D, 1.0D, 1.0D);
    protected static final AxisAlignedBB PISTON_EXTENSION_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.75D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB PISTON_EXTENSION_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.25D);
    protected static final AxisAlignedBB PISTON_EXTENSION_UP_AABB = new AxisAlignedBB(0.0D, 0.75D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB PISTON_EXTENSION_DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
    protected static final AxisAlignedBB UP_ARM_AABB = new AxisAlignedBB(0.375D, -0.25D, 0.375D, 0.625D, 0.75D, 0.625D);
    protected static final AxisAlignedBB DOWN_ARM_AABB = new AxisAlignedBB(0.375D, 0.25D, 0.375D, 0.625D, 1.25D, 0.625D);
    protected static final AxisAlignedBB SOUTH_ARM_AABB = new AxisAlignedBB(0.375D, 0.375D, -0.25D, 0.625D, 0.625D, 0.75D);
    protected static final AxisAlignedBB NORTH_ARM_AABB = new AxisAlignedBB(0.375D, 0.375D, 0.25D, 0.625D, 0.625D, 1.25D);
    protected static final AxisAlignedBB EAST_ARM_AABB = new AxisAlignedBB(-0.25D, 0.375D, 0.375D, 0.75D, 0.625D, 0.625D);
    protected static final AxisAlignedBB WEST_ARM_AABB = new AxisAlignedBB(0.25D, 0.375D, 0.375D, 1.25D, 0.625D, 0.625D);
    protected static final AxisAlignedBB SHORT_UP_ARM_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.75D, 0.625D);
    protected static final AxisAlignedBB SHORT_DOWN_ARM_AABB = new AxisAlignedBB(0.375D, 0.25D, 0.375D, 0.625D, 1.0D, 0.625D);
    protected static final AxisAlignedBB SHORT_SOUTH_ARM_AABB = new AxisAlignedBB(0.375D, 0.375D, 0.0D, 0.625D, 0.625D, 0.75D);
    protected static final AxisAlignedBB SHORT_NORTH_ARM_AABB = new AxisAlignedBB(0.375D, 0.375D, 0.25D, 0.625D, 0.625D, 1.0D);
    protected static final AxisAlignedBB SHORT_EAST_ARM_AABB = new AxisAlignedBB(0.0D, 0.375D, 0.375D, 0.75D, 0.625D, 0.625D);
    protected static final AxisAlignedBB SHORT_WEST_ARM_AABB = new AxisAlignedBB(0.25D, 0.375D, 0.375D, 1.0D, 0.625D, 0.625D);

    public BlockPistonExtension()
    {
        super(Material.PISTON);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(FACING, EnumFacing.NORTH).func_177226_a(TYPE, BlockPistonExtension.EnumPistonType.DEFAULT).func_177226_a(SHORT, Boolean.valueOf(false)));
        this.func_149672_a(SoundType.STONE);
        this.func_149711_c(0.5F);
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        switch ((EnumFacing)p_185496_1_.get(FACING))
        {
            case DOWN:
            default:
                return PISTON_EXTENSION_DOWN_AABB;

            case UP:
                return PISTON_EXTENSION_UP_AABB;

            case NORTH:
                return PISTON_EXTENSION_NORTH_AABB;

            case SOUTH:
                return PISTON_EXTENSION_SOUTH_AABB;

            case WEST:
                return PISTON_EXTENSION_WEST_AABB;

            case EAST:
                return PISTON_EXTENSION_EAST_AABB;
        }
    }

    public void func_185477_a(IBlockState p_185477_1_, World p_185477_2_, BlockPos p_185477_3_, AxisAlignedBB p_185477_4_, List<AxisAlignedBB> p_185477_5_, @Nullable Entity p_185477_6_, boolean p_185477_7_)
    {
        func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, p_185477_1_.func_185900_c(p_185477_2_, p_185477_3_));
        func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, this.func_185633_i(p_185477_1_));
    }

    private AxisAlignedBB func_185633_i(IBlockState p_185633_1_)
    {
        boolean flag = ((Boolean)p_185633_1_.get(SHORT)).booleanValue();

        switch ((EnumFacing)p_185633_1_.get(FACING))
        {
            case DOWN:
            default:
                return flag ? SHORT_DOWN_ARM_AABB : DOWN_ARM_AABB;

            case UP:
                return flag ? SHORT_UP_ARM_AABB : UP_ARM_AABB;

            case NORTH:
                return flag ? SHORT_NORTH_ARM_AABB : NORTH_ARM_AABB;

            case SOUTH:
                return flag ? SHORT_SOUTH_ARM_AABB : SOUTH_ARM_AABB;

            case WEST:
                return flag ? SHORT_WEST_ARM_AABB : WEST_ARM_AABB;

            case EAST:
                return flag ? SHORT_EAST_ARM_AABB : EAST_ARM_AABB;
        }
    }

    public boolean func_185481_k(IBlockState p_185481_1_)
    {
        return p_185481_1_.get(FACING) == EnumFacing.UP;
    }

    /**
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually
     * collect this block
     */
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (player.abilities.isCreativeMode)
        {
            BlockPos blockpos = pos.offset(((EnumFacing)state.get(FACING)).getOpposite());
            Block block = worldIn.getBlockState(blockpos).getBlock();

            if (block == Blocks.PISTON || block == Blocks.STICKY_PISTON)
            {
                worldIn.func_175698_g(blockpos);
            }
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
        super.func_180663_b(p_180663_1_, p_180663_2_, p_180663_3_);
        EnumFacing enumfacing = ((EnumFacing)p_180663_3_.get(FACING)).getOpposite();
        p_180663_2_ = p_180663_2_.offset(enumfacing);
        IBlockState iblockstate = p_180663_1_.getBlockState(p_180663_2_);

        if ((iblockstate.getBlock() == Blocks.PISTON || iblockstate.getBlock() == Blocks.STICKY_PISTON) && ((Boolean)iblockstate.get(BlockPistonBase.EXTENDED)).booleanValue())
        {
            iblockstate.getBlock().func_176226_b(p_180663_1_, p_180663_2_, iblockstate, 0);
            p_180663_1_.func_175698_g(p_180663_2_);
        }
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    public boolean func_176196_c(World p_176196_1_, BlockPos p_176196_2_)
    {
        return false;
    }

    public boolean func_176198_a(World p_176198_1_, BlockPos p_176198_2_, EnumFacing p_176198_3_)
    {
        return false;
    }

    public int func_149745_a(Random p_149745_1_)
    {
        return 0;
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        EnumFacing enumfacing = (EnumFacing)p_189540_1_.get(FACING);
        BlockPos blockpos = p_189540_3_.offset(enumfacing.getOpposite());
        IBlockState iblockstate = p_189540_2_.getBlockState(blockpos);

        if (iblockstate.getBlock() != Blocks.PISTON && iblockstate.getBlock() != Blocks.STICKY_PISTON)
        {
            p_189540_2_.func_175698_g(p_189540_3_);
        }
        else
        {
            iblockstate.func_189546_a(p_189540_2_, blockpos, p_189540_4_, p_189540_5_);
        }
    }

    /**
     * ""
     */
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing p_176225_4_)
    {
        return true;
    }

    @Nullable
    public static EnumFacing func_176322_b(int p_176322_0_)
    {
        int i = p_176322_0_ & 7;
        return i > 5 ? null : EnumFacing.byIndex(i);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(state.get(TYPE) == BlockPistonExtension.EnumPistonType.STICKY ? Blocks.STICKY_PISTON : Blocks.PISTON);
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(FACING, func_176322_b(p_176203_1_)).func_177226_a(TYPE, (p_176203_1_ & 8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;
        i = i | ((EnumFacing)p_176201_1_.get(FACING)).getIndex();

        if (p_176201_1_.get(TYPE) == BlockPistonExtension.EnumPistonType.STICKY)
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
        return new BlockStateContainer(this, new IProperty[] {FACING, TYPE, SHORT});
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return p_193383_4_ == p_193383_2_.get(FACING) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    public static enum EnumPistonType implements IStringSerializable
    {
        DEFAULT("normal"),
        STICKY("sticky");

        private final String name;

        private EnumPistonType(String p_i45666_3_)
        {
            this.name = p_i45666_3_;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }
    }
}
