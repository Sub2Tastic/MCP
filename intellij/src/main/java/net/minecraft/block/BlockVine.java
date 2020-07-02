package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVine extends Block
{
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool[] field_176274_P = new PropertyBool[] {UP, NORTH, SOUTH, WEST, EAST};
    protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);

    public BlockVine()
    {
        super(Material.TALL_PLANTS);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(UP, Boolean.valueOf(false)).func_177226_a(NORTH, Boolean.valueOf(false)).func_177226_a(EAST, Boolean.valueOf(false)).func_177226_a(SOUTH, Boolean.valueOf(false)).func_177226_a(WEST, Boolean.valueOf(false)));
        this.func_149675_a(true);
        this.func_149647_a(CreativeTabs.DECORATIONS);
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState p_180646_1_, IBlockAccess p_180646_2_, BlockPos p_180646_3_)
    {
        return field_185506_k;
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        p_185496_1_ = p_185496_1_.func_185899_b(p_185496_2_, p_185496_3_);
        int i = 0;
        AxisAlignedBB axisalignedbb = field_185505_j;

        if (((Boolean)p_185496_1_.get(UP)).booleanValue())
        {
            axisalignedbb = UP_AABB;
            ++i;
        }

        if (((Boolean)p_185496_1_.get(NORTH)).booleanValue())
        {
            axisalignedbb = NORTH_AABB;
            ++i;
        }

        if (((Boolean)p_185496_1_.get(EAST)).booleanValue())
        {
            axisalignedbb = EAST_AABB;
            ++i;
        }

        if (((Boolean)p_185496_1_.get(SOUTH)).booleanValue())
        {
            axisalignedbb = SOUTH_AABB;
            ++i;
        }

        if (((Boolean)p_185496_1_.get(WEST)).booleanValue())
        {
            axisalignedbb = WEST_AABB;
            ++i;
        }

        return i == 1 ? axisalignedbb : field_185505_j;
    }

    public IBlockState func_176221_a(IBlockState p_176221_1_, IBlockAccess p_176221_2_, BlockPos p_176221_3_)
    {
        BlockPos blockpos = p_176221_3_.up();
        return p_176221_1_.func_177226_a(UP, Boolean.valueOf(p_176221_2_.getBlockState(blockpos).func_193401_d(p_176221_2_, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID));
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    public boolean func_176200_f(IBlockAccess p_176200_1_, BlockPos p_176200_2_)
    {
        return true;
    }

    public boolean func_176198_a(World p_176198_1_, BlockPos p_176198_2_, EnumFacing p_176198_3_)
    {
        return p_176198_3_ != EnumFacing.DOWN && p_176198_3_ != EnumFacing.UP && this.func_193395_a(p_176198_1_, p_176198_2_, p_176198_3_);
    }

    public boolean func_193395_a(World p_193395_1_, BlockPos p_193395_2_, EnumFacing p_193395_3_)
    {
        Block block = p_193395_1_.getBlockState(p_193395_2_.up()).getBlock();
        return this.func_193396_c(p_193395_1_, p_193395_2_.offset(p_193395_3_.getOpposite()), p_193395_3_) && (block == Blocks.AIR || block == Blocks.VINE || this.func_193396_c(p_193395_1_, p_193395_2_.up(), EnumFacing.UP));
    }

    private boolean func_193396_c(World p_193396_1_, BlockPos p_193396_2_, EnumFacing p_193396_3_)
    {
        IBlockState iblockstate = p_193396_1_.getBlockState(p_193396_2_);
        return iblockstate.func_193401_d(p_193396_1_, p_193396_2_, p_193396_3_) == BlockFaceShape.SOLID && !func_193397_e(iblockstate.getBlock());
    }

    protected static boolean func_193397_e(Block p_193397_0_)
    {
        return p_193397_0_ instanceof BlockShulkerBox || p_193397_0_ == Blocks.BEACON || p_193397_0_ == Blocks.CAULDRON || p_193397_0_ == Blocks.GLASS || p_193397_0_ == Blocks.field_150399_cn || p_193397_0_ == Blocks.PISTON || p_193397_0_ == Blocks.STICKY_PISTON || p_193397_0_ == Blocks.PISTON_HEAD || p_193397_0_ == Blocks.field_150415_aT;
    }

    private boolean func_176269_e(World p_176269_1_, BlockPos p_176269_2_, IBlockState p_176269_3_)
    {
        IBlockState iblockstate = p_176269_3_;

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            PropertyBool propertybool = getPropertyFor(enumfacing);

            if (((Boolean)p_176269_3_.get(propertybool)).booleanValue() && !this.func_193395_a(p_176269_1_, p_176269_2_, enumfacing.getOpposite()))
            {
                IBlockState iblockstate1 = p_176269_1_.getBlockState(p_176269_2_.up());

                if (iblockstate1.getBlock() != this || !((Boolean)iblockstate1.get(propertybool)).booleanValue())
                {
                    p_176269_3_ = p_176269_3_.func_177226_a(propertybool, Boolean.valueOf(false));
                }
            }
        }

        if (func_176268_d(p_176269_3_) == 0)
        {
            return false;
        }
        else
        {
            if (iblockstate != p_176269_3_)
            {
                p_176269_1_.setBlockState(p_176269_2_, p_176269_3_, 2);
            }

            return true;
        }
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        if (!p_189540_2_.isRemote && !this.func_176269_e(p_189540_2_, p_189540_3_, p_189540_1_))
        {
            this.func_176226_b(p_189540_2_, p_189540_3_, p_189540_1_, 0);
            p_189540_2_.func_175698_g(p_189540_3_);
        }
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        if (!p_180650_1_.isRemote)
        {
            if (p_180650_1_.rand.nextInt(4) == 0)
            {
                int i = 4;
                int j = 5;
                boolean flag = false;
                label181:

                for (int k = -4; k <= 4; ++k)
                {
                    for (int l = -4; l <= 4; ++l)
                    {
                        for (int i1 = -1; i1 <= 1; ++i1)
                        {
                            if (p_180650_1_.getBlockState(p_180650_2_.add(k, i1, l)).getBlock() == this)
                            {
                                --j;

                                if (j <= 0)
                                {
                                    flag = true;
                                    break label181;
                                }
                            }
                        }
                    }
                }

                EnumFacing enumfacing1 = EnumFacing.random(p_180650_4_);
                BlockPos blockpos2 = p_180650_2_.up();

                if (enumfacing1 == EnumFacing.UP && p_180650_2_.getY() < 255 && p_180650_1_.isAirBlock(blockpos2))
                {
                    IBlockState iblockstate2 = p_180650_3_;

                    for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL)
                    {
                        if (p_180650_4_.nextBoolean() && this.func_193395_a(p_180650_1_, blockpos2, enumfacing2.getOpposite()))
                        {
                            iblockstate2 = iblockstate2.func_177226_a(getPropertyFor(enumfacing2), Boolean.valueOf(true));
                        }
                        else
                        {
                            iblockstate2 = iblockstate2.func_177226_a(getPropertyFor(enumfacing2), Boolean.valueOf(false));
                        }
                    }

                    if (((Boolean)iblockstate2.get(NORTH)).booleanValue() || ((Boolean)iblockstate2.get(EAST)).booleanValue() || ((Boolean)iblockstate2.get(SOUTH)).booleanValue() || ((Boolean)iblockstate2.get(WEST)).booleanValue())
                    {
                        p_180650_1_.setBlockState(blockpos2, iblockstate2, 2);
                    }
                }
                else if (enumfacing1.getAxis().isHorizontal() && !((Boolean)p_180650_3_.get(getPropertyFor(enumfacing1))).booleanValue())
                {
                    if (!flag)
                    {
                        BlockPos blockpos4 = p_180650_2_.offset(enumfacing1);
                        IBlockState iblockstate3 = p_180650_1_.getBlockState(blockpos4);
                        Block block1 = iblockstate3.getBlock();

                        if (block1.material == Material.AIR)
                        {
                            EnumFacing enumfacing3 = enumfacing1.rotateY();
                            EnumFacing enumfacing4 = enumfacing1.rotateYCCW();
                            boolean flag1 = ((Boolean)p_180650_3_.get(getPropertyFor(enumfacing3))).booleanValue();
                            boolean flag2 = ((Boolean)p_180650_3_.get(getPropertyFor(enumfacing4))).booleanValue();
                            BlockPos blockpos = blockpos4.offset(enumfacing3);
                            BlockPos blockpos1 = blockpos4.offset(enumfacing4);

                            if (flag1 && this.func_193395_a(p_180650_1_, blockpos.offset(enumfacing3), enumfacing3))
                            {
                                p_180650_1_.setBlockState(blockpos4, this.getDefaultState().func_177226_a(getPropertyFor(enumfacing3), Boolean.valueOf(true)), 2);
                            }
                            else if (flag2 && this.func_193395_a(p_180650_1_, blockpos1.offset(enumfacing4), enumfacing4))
                            {
                                p_180650_1_.setBlockState(blockpos4, this.getDefaultState().func_177226_a(getPropertyFor(enumfacing4), Boolean.valueOf(true)), 2);
                            }
                            else if (flag1 && p_180650_1_.isAirBlock(blockpos) && this.func_193395_a(p_180650_1_, blockpos, enumfacing1))
                            {
                                p_180650_1_.setBlockState(blockpos, this.getDefaultState().func_177226_a(getPropertyFor(enumfacing1.getOpposite()), Boolean.valueOf(true)), 2);
                            }
                            else if (flag2 && p_180650_1_.isAirBlock(blockpos1) && this.func_193395_a(p_180650_1_, blockpos1, enumfacing1))
                            {
                                p_180650_1_.setBlockState(blockpos1, this.getDefaultState().func_177226_a(getPropertyFor(enumfacing1.getOpposite()), Boolean.valueOf(true)), 2);
                            }
                        }
                        else if (iblockstate3.func_193401_d(p_180650_1_, blockpos4, enumfacing1) == BlockFaceShape.SOLID)
                        {
                            p_180650_1_.setBlockState(p_180650_2_, p_180650_3_.func_177226_a(getPropertyFor(enumfacing1), Boolean.valueOf(true)), 2);
                        }
                    }
                }
                else
                {
                    if (p_180650_2_.getY() > 1)
                    {
                        BlockPos blockpos3 = p_180650_2_.down();
                        IBlockState iblockstate = p_180650_1_.getBlockState(blockpos3);
                        Block block = iblockstate.getBlock();

                        if (block.material == Material.AIR)
                        {
                            IBlockState iblockstate1 = p_180650_3_;

                            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                            {
                                if (p_180650_4_.nextBoolean())
                                {
                                    iblockstate1 = iblockstate1.func_177226_a(getPropertyFor(enumfacing), Boolean.valueOf(false));
                                }
                            }

                            if (((Boolean)iblockstate1.get(NORTH)).booleanValue() || ((Boolean)iblockstate1.get(EAST)).booleanValue() || ((Boolean)iblockstate1.get(SOUTH)).booleanValue() || ((Boolean)iblockstate1.get(WEST)).booleanValue())
                            {
                                p_180650_1_.setBlockState(blockpos3, iblockstate1, 2);
                            }
                        }
                        else if (block == this)
                        {
                            IBlockState iblockstate4 = iblockstate;

                            for (EnumFacing enumfacing5 : EnumFacing.Plane.HORIZONTAL)
                            {
                                PropertyBool propertybool = getPropertyFor(enumfacing5);

                                if (p_180650_4_.nextBoolean() && ((Boolean)p_180650_3_.get(propertybool)).booleanValue())
                                {
                                    iblockstate4 = iblockstate4.func_177226_a(propertybool, Boolean.valueOf(true));
                                }
                            }

                            if (((Boolean)iblockstate4.get(NORTH)).booleanValue() || ((Boolean)iblockstate4.get(EAST)).booleanValue() || ((Boolean)iblockstate4.get(SOUTH)).booleanValue() || ((Boolean)iblockstate4.get(WEST)).booleanValue())
                            {
                                p_180650_1_.setBlockState(blockpos3, iblockstate4, 2);
                            }
                        }
                    }
                }
            }
        }
    }

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        IBlockState iblockstate = this.getDefaultState().func_177226_a(UP, Boolean.valueOf(false)).func_177226_a(NORTH, Boolean.valueOf(false)).func_177226_a(EAST, Boolean.valueOf(false)).func_177226_a(SOUTH, Boolean.valueOf(false)).func_177226_a(WEST, Boolean.valueOf(false));
        return p_180642_3_.getAxis().isHorizontal() ? iblockstate.func_177226_a(getPropertyFor(p_180642_3_.getOpposite()), Boolean.valueOf(true)) : iblockstate;
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Items.AIR;
    }

    public int func_149745_a(Random p_149745_1_)
    {
        return 0;
    }

    /**
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
     * Block.removedByPlayer
     */
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS)
        {
            player.addStat(StatList.func_188055_a(this));
            spawnAsEntity(worldIn, pos, new ItemStack(Blocks.VINE, 1, 0));
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }

    public BlockRenderLayer func_180664_k()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(SOUTH, Boolean.valueOf((p_176203_1_ & 1) > 0)).func_177226_a(WEST, Boolean.valueOf((p_176203_1_ & 2) > 0)).func_177226_a(NORTH, Boolean.valueOf((p_176203_1_ & 4) > 0)).func_177226_a(EAST, Boolean.valueOf((p_176203_1_ & 8) > 0));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;

        if (((Boolean)p_176201_1_.get(SOUTH)).booleanValue())
        {
            i |= 1;
        }

        if (((Boolean)p_176201_1_.get(WEST)).booleanValue())
        {
            i |= 2;
        }

        if (((Boolean)p_176201_1_.get(NORTH)).booleanValue())
        {
            i |= 4;
        }

        if (((Boolean)p_176201_1_.get(EAST)).booleanValue())
        {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {UP, NORTH, EAST, SOUTH, WEST});
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
                return state.func_177226_a(NORTH, state.get(SOUTH)).func_177226_a(EAST, state.get(WEST)).func_177226_a(SOUTH, state.get(NORTH)).func_177226_a(WEST, state.get(EAST));

            case COUNTERCLOCKWISE_90:
                return state.func_177226_a(NORTH, state.get(EAST)).func_177226_a(EAST, state.get(SOUTH)).func_177226_a(SOUTH, state.get(WEST)).func_177226_a(WEST, state.get(NORTH));

            case CLOCKWISE_90:
                return state.func_177226_a(NORTH, state.get(WEST)).func_177226_a(EAST, state.get(NORTH)).func_177226_a(SOUTH, state.get(EAST)).func_177226_a(WEST, state.get(SOUTH));

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
                return state.func_177226_a(NORTH, state.get(SOUTH)).func_177226_a(SOUTH, state.get(NORTH));

            case FRONT_BACK:
                return state.func_177226_a(EAST, state.get(WEST)).func_177226_a(WEST, state.get(EAST));

            default:
                return super.mirror(state, mirrorIn);
        }
    }

    public static PropertyBool getPropertyFor(EnumFacing side)
    {
        switch (side)
        {
            case UP:
                return UP;

            case NORTH:
                return NORTH;

            case SOUTH:
                return SOUTH;

            case WEST:
                return WEST;

            case EAST:
                return EAST;

            default:
                throw new IllegalArgumentException(side + " is an invalid choice");
        }
    }

    public static int func_176268_d(IBlockState p_176268_0_)
    {
        int i = 0;

        for (PropertyBool propertybool : field_176274_P)
        {
            if (((Boolean)p_176268_0_.get(propertybool)).booleanValue())
            {
                ++i;
            }
        }

        return i;
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
