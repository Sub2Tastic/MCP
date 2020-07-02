package net.minecraft.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneWire extends Block
{
    public static final PropertyEnum<BlockRedstoneWire.EnumAttachPosition> NORTH = PropertyEnum.<BlockRedstoneWire.EnumAttachPosition>create("north", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyEnum<BlockRedstoneWire.EnumAttachPosition> EAST = PropertyEnum.<BlockRedstoneWire.EnumAttachPosition>create("east", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyEnum<BlockRedstoneWire.EnumAttachPosition> SOUTH = PropertyEnum.<BlockRedstoneWire.EnumAttachPosition>create("south", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyEnum<BlockRedstoneWire.EnumAttachPosition> WEST = PropertyEnum.<BlockRedstoneWire.EnumAttachPosition>create("west", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    protected static final AxisAlignedBB[] field_185700_f = new AxisAlignedBB[] {new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D)};
    private boolean canProvidePower = true;
    private final Set<BlockPos> blocksNeedingUpdate = Sets.<BlockPos>newHashSet();

    public BlockRedstoneWire()
    {
        super(Material.MISCELLANEOUS);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(NORTH, BlockRedstoneWire.EnumAttachPosition.NONE).func_177226_a(EAST, BlockRedstoneWire.EnumAttachPosition.NONE).func_177226_a(SOUTH, BlockRedstoneWire.EnumAttachPosition.NONE).func_177226_a(WEST, BlockRedstoneWire.EnumAttachPosition.NONE).func_177226_a(POWER, Integer.valueOf(0)));
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        return field_185700_f[getAABBIndex(p_185496_1_.func_185899_b(p_185496_2_, p_185496_3_))];
    }

    private static int getAABBIndex(IBlockState state)
    {
        int i = 0;
        boolean flag = state.get(NORTH) != BlockRedstoneWire.EnumAttachPosition.NONE;
        boolean flag1 = state.get(EAST) != BlockRedstoneWire.EnumAttachPosition.NONE;
        boolean flag2 = state.get(SOUTH) != BlockRedstoneWire.EnumAttachPosition.NONE;
        boolean flag3 = state.get(WEST) != BlockRedstoneWire.EnumAttachPosition.NONE;

        if (flag || flag2 && !flag && !flag1 && !flag3)
        {
            i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
        }

        if (flag1 || flag3 && !flag && !flag1 && !flag2)
        {
            i |= 1 << EnumFacing.EAST.getHorizontalIndex();
        }

        if (flag2 || flag && !flag1 && !flag2 && !flag3)
        {
            i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
        }

        if (flag3 || flag1 && !flag && !flag2 && !flag3)
        {
            i |= 1 << EnumFacing.WEST.getHorizontalIndex();
        }

        return i;
    }

    public IBlockState func_176221_a(IBlockState p_176221_1_, IBlockAccess p_176221_2_, BlockPos p_176221_3_)
    {
        p_176221_1_ = p_176221_1_.func_177226_a(WEST, this.func_176341_c(p_176221_2_, p_176221_3_, EnumFacing.WEST));
        p_176221_1_ = p_176221_1_.func_177226_a(EAST, this.func_176341_c(p_176221_2_, p_176221_3_, EnumFacing.EAST));
        p_176221_1_ = p_176221_1_.func_177226_a(NORTH, this.func_176341_c(p_176221_2_, p_176221_3_, EnumFacing.NORTH));
        p_176221_1_ = p_176221_1_.func_177226_a(SOUTH, this.func_176341_c(p_176221_2_, p_176221_3_, EnumFacing.SOUTH));
        return p_176221_1_;
    }

    private BlockRedstoneWire.EnumAttachPosition func_176341_c(IBlockAccess p_176341_1_, BlockPos p_176341_2_, EnumFacing p_176341_3_)
    {
        BlockPos blockpos = p_176341_2_.offset(p_176341_3_);
        IBlockState iblockstate = p_176341_1_.getBlockState(p_176341_2_.offset(p_176341_3_));

        if (!canConnectTo(p_176341_1_.getBlockState(blockpos), p_176341_3_) && (iblockstate.func_185915_l() || !canConnectUpwardsTo(p_176341_1_.getBlockState(blockpos.down()))))
        {
            IBlockState iblockstate1 = p_176341_1_.getBlockState(p_176341_2_.up());

            if (!iblockstate1.func_185915_l())
            {
                boolean flag = p_176341_1_.getBlockState(blockpos).func_185896_q() || p_176341_1_.getBlockState(blockpos).getBlock() == Blocks.GLOWSTONE;

                if (flag && canConnectUpwardsTo(p_176341_1_.getBlockState(blockpos.up())))
                {
                    if (iblockstate.func_185898_k())
                    {
                        return BlockRedstoneWire.EnumAttachPosition.UP;
                    }

                    return BlockRedstoneWire.EnumAttachPosition.SIDE;
                }
            }

            return BlockRedstoneWire.EnumAttachPosition.NONE;
        }
        else
        {
            return BlockRedstoneWire.EnumAttachPosition.SIDE;
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

    public boolean func_176196_c(World p_176196_1_, BlockPos p_176196_2_)
    {
        return p_176196_1_.getBlockState(p_176196_2_.down()).func_185896_q() || p_176196_1_.getBlockState(p_176196_2_.down()).getBlock() == Blocks.GLOWSTONE;
    }

    private IBlockState updateSurroundingRedstone(World worldIn, BlockPos pos, IBlockState state)
    {
        state = this.func_176345_a(worldIn, pos, pos, state);
        List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();

        for (BlockPos blockpos : list)
        {
            worldIn.func_175685_c(blockpos, this, false);
        }

        return state;
    }

    private IBlockState func_176345_a(World p_176345_1_, BlockPos p_176345_2_, BlockPos p_176345_3_, IBlockState p_176345_4_)
    {
        IBlockState iblockstate = p_176345_4_;
        int i = ((Integer)p_176345_4_.get(POWER)).intValue();
        int j = 0;
        j = this.func_176342_a(p_176345_1_, p_176345_3_, j);
        this.canProvidePower = false;
        int k = p_176345_1_.getRedstonePowerFromNeighbors(p_176345_2_);
        this.canProvidePower = true;

        if (k > 0 && k > j - 1)
        {
            j = k;
        }

        int l = 0;

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            BlockPos blockpos = p_176345_2_.offset(enumfacing);
            boolean flag = blockpos.getX() != p_176345_3_.getX() || blockpos.getZ() != p_176345_3_.getZ();

            if (flag)
            {
                l = this.func_176342_a(p_176345_1_, blockpos, l);
            }

            if (p_176345_1_.getBlockState(blockpos).func_185915_l() && !p_176345_1_.getBlockState(p_176345_2_.up()).func_185915_l())
            {
                if (flag && p_176345_2_.getY() >= p_176345_3_.getY())
                {
                    l = this.func_176342_a(p_176345_1_, blockpos.up(), l);
                }
            }
            else if (!p_176345_1_.getBlockState(blockpos).func_185915_l() && flag && p_176345_2_.getY() <= p_176345_3_.getY())
            {
                l = this.func_176342_a(p_176345_1_, blockpos.down(), l);
            }
        }

        if (l > j)
        {
            j = l - 1;
        }
        else if (j > 0)
        {
            --j;
        }
        else
        {
            j = 0;
        }

        if (k > j - 1)
        {
            j = k;
        }

        if (i != j)
        {
            p_176345_4_ = p_176345_4_.func_177226_a(POWER, Integer.valueOf(j));

            if (p_176345_1_.getBlockState(p_176345_2_) == iblockstate)
            {
                p_176345_1_.setBlockState(p_176345_2_, p_176345_4_, 2);
            }

            this.blocksNeedingUpdate.add(p_176345_2_);

            for (EnumFacing enumfacing1 : EnumFacing.values())
            {
                this.blocksNeedingUpdate.add(p_176345_2_.offset(enumfacing1));
            }
        }

        return p_176345_4_;
    }

    /**
     * Calls World.notifyNeighborsOfStateChange() for all neighboring blocks, but only if the given block is a redstone
     * wire.
     */
    private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos)
    {
        if (worldIn.getBlockState(pos).getBlock() == this)
        {
            worldIn.func_175685_c(pos, this, false);

            for (EnumFacing enumfacing : EnumFacing.values())
            {
                worldIn.func_175685_c(pos.offset(enumfacing), this, false);
            }
        }
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        if (!p_176213_1_.isRemote)
        {
            this.updateSurroundingRedstone(p_176213_1_, p_176213_2_, p_176213_3_);

            for (EnumFacing enumfacing : EnumFacing.Plane.VERTICAL)
            {
                p_176213_1_.func_175685_c(p_176213_2_.offset(enumfacing), this, false);
            }

            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
            {
                this.notifyWireNeighborsOfStateChange(p_176213_1_, p_176213_2_.offset(enumfacing1));
            }

            for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL)
            {
                BlockPos blockpos = p_176213_2_.offset(enumfacing2);

                if (p_176213_1_.getBlockState(blockpos).func_185915_l())
                {
                    this.notifyWireNeighborsOfStateChange(p_176213_1_, blockpos.up());
                }
                else
                {
                    this.notifyWireNeighborsOfStateChange(p_176213_1_, blockpos.down());
                }
            }
        }
    }

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
        super.func_180663_b(p_180663_1_, p_180663_2_, p_180663_3_);

        if (!p_180663_1_.isRemote)
        {
            for (EnumFacing enumfacing : EnumFacing.values())
            {
                p_180663_1_.func_175685_c(p_180663_2_.offset(enumfacing), this, false);
            }

            this.updateSurroundingRedstone(p_180663_1_, p_180663_2_, p_180663_3_);

            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
            {
                this.notifyWireNeighborsOfStateChange(p_180663_1_, p_180663_2_.offset(enumfacing1));
            }

            for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL)
            {
                BlockPos blockpos = p_180663_2_.offset(enumfacing2);

                if (p_180663_1_.getBlockState(blockpos).func_185915_l())
                {
                    this.notifyWireNeighborsOfStateChange(p_180663_1_, blockpos.up());
                }
                else
                {
                    this.notifyWireNeighborsOfStateChange(p_180663_1_, blockpos.down());
                }
            }
        }
    }

    private int func_176342_a(World p_176342_1_, BlockPos p_176342_2_, int p_176342_3_)
    {
        if (p_176342_1_.getBlockState(p_176342_2_).getBlock() != this)
        {
            return p_176342_3_;
        }
        else
        {
            int i = ((Integer)p_176342_1_.getBlockState(p_176342_2_).get(POWER)).intValue();
            return i > p_176342_3_ ? i : p_176342_3_;
        }
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        if (!p_189540_2_.isRemote)
        {
            if (this.func_176196_c(p_189540_2_, p_189540_3_))
            {
                this.updateSurroundingRedstone(p_189540_2_, p_189540_3_, p_189540_1_);
            }
            else
            {
                this.func_176226_b(p_189540_2_, p_189540_3_, p_189540_1_, 0);
                p_189540_2_.func_175698_g(p_189540_3_);
            }
        }
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Items.REDSTONE;
    }

    /**
     * @deprecated call via {@link IBlockState#getStrongPower(IBlockAccess,BlockPos,EnumFacing)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return !this.canProvidePower ? 0 : blockState.getWeakPower(blockAccess, pos, side);
    }

    /**
     * @deprecated call via {@link IBlockState#getWeakPower(IBlockAccess,BlockPos,EnumFacing)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        if (!this.canProvidePower)
        {
            return 0;
        }
        else
        {
            int i = ((Integer)blockState.get(POWER)).intValue();

            if (i == 0)
            {
                return 0;
            }
            else if (side == EnumFacing.UP)
            {
                return i;
            }
            else
            {
                EnumSet<EnumFacing> enumset = EnumSet.<EnumFacing>noneOf(EnumFacing.class);

                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                {
                    if (this.isPowerSourceAt(blockAccess, pos, enumfacing))
                    {
                        enumset.add(enumfacing);
                    }
                }

                if (side.getAxis().isHorizontal() && enumset.isEmpty())
                {
                    return i;
                }
                else if (enumset.contains(side) && !enumset.contains(side.rotateYCCW()) && !enumset.contains(side.rotateY()))
                {
                    return i;
                }
                else
                {
                    return 0;
                }
            }
        }
    }

    private boolean isPowerSourceAt(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        BlockPos blockpos = pos.offset(side);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        boolean flag = iblockstate.func_185915_l();
        boolean flag1 = worldIn.getBlockState(pos.up()).func_185915_l();

        if (!flag1 && flag && canConnectUpwardsTo(worldIn, blockpos.up()))
        {
            return true;
        }
        else if (canConnectTo(iblockstate, side))
        {
            return true;
        }
        else if (iblockstate.getBlock() == Blocks.field_150416_aS && iblockstate.get(BlockRedstoneDiode.HORIZONTAL_FACING) == side)
        {
            return true;
        }
        else
        {
            return !flag && canConnectUpwardsTo(worldIn, blockpos.down());
        }
    }

    protected static boolean canConnectUpwardsTo(IBlockAccess worldIn, BlockPos pos)
    {
        return canConnectUpwardsTo(worldIn.getBlockState(pos));
    }

    protected static boolean canConnectUpwardsTo(IBlockState state)
    {
        return canConnectTo(state, (EnumFacing)null);
    }

    protected static boolean canConnectTo(IBlockState blockState, @Nullable EnumFacing side)
    {
        Block block = blockState.getBlock();

        if (block == Blocks.REDSTONE_WIRE)
        {
            return true;
        }
        else if (Blocks.field_150413_aR.func_185547_C(blockState))
        {
            EnumFacing enumfacing = (EnumFacing)blockState.get(BlockRedstoneRepeater.HORIZONTAL_FACING);
            return enumfacing == side || enumfacing.getOpposite() == side;
        }
        else if (Blocks.OBSERVER == blockState.getBlock())
        {
            return side == blockState.get(BlockObserver.FACING);
        }
        else
        {
            return blockState.canProvidePower() && side != null;
        }
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     * @deprecated call via {@link IBlockState#canProvidePower()} whenever possible. Implementing/overriding is fine.
     */
    public boolean canProvidePower(IBlockState state)
    {
        return this.canProvidePower;
    }

    public static int colorMultiplier(int p_176337_0_)
    {
        float f = (float)p_176337_0_ / 15.0F;
        float f1 = f * 0.6F + 0.4F;

        if (p_176337_0_ == 0)
        {
            f1 = 0.3F;
        }

        float f2 = f * f * 0.7F - 0.5F;
        float f3 = f * f * 0.6F - 0.7F;

        if (f2 < 0.0F)
        {
            f2 = 0.0F;
        }

        if (f3 < 0.0F)
        {
            f3 = 0.0F;
        }

        int i = MathHelper.clamp((int)(f1 * 255.0F), 0, 255);
        int j = MathHelper.clamp((int)(f2 * 255.0F), 0, 255);
        int k = MathHelper.clamp((int)(f3 * 255.0F), 0, 255);
        return -16777216 | i << 16 | j << 8 | k;
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that
     * this method is unrelated to {@link randomTick} and {@link #needsRandomTick}, and will always be called regardless
     * of whether the block can receive random update ticks
     */
    public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        int i = ((Integer)stateIn.get(POWER)).intValue();

        if (i != 0)
        {
            double d0 = (double)pos.getX() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            double d1 = (double)((float)pos.getY() + 0.0625F);
            double d2 = (double)pos.getZ() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            float f = (float)i / 15.0F;
            float f1 = f * 0.6F + 0.4F;
            float f2 = Math.max(0.0F, f * f * 0.7F - 0.5F);
            float f3 = Math.max(0.0F, f * f * 0.6F - 0.7F);
            worldIn.func_175688_a(EnumParticleTypes.REDSTONE, d0, d1, d2, (double)f1, (double)f2, (double)f3);
        }
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Items.REDSTONE);
    }

    public BlockRenderLayer func_180664_k()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(POWER, Integer.valueOf(p_176203_1_));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((Integer)p_176201_1_.get(POWER)).intValue();
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

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {NORTH, EAST, SOUTH, WEST, POWER});
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }

    static enum EnumAttachPosition implements IStringSerializable
    {
        UP("up"),
        SIDE("side"),
        NONE("none");

        private final String name;

        private EnumAttachPosition(String p_i45689_3_)
        {
            this.name = p_i45689_3_;
        }

        public String toString()
        {
            return this.getName();
        }

        public String getName()
        {
            return this.name;
        }
    }
}
