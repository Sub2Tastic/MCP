package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRailDetector extends BlockRailBase
{
    public static final PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE = PropertyEnum.<BlockRailBase.EnumRailDirection>create("shape", BlockRailBase.EnumRailDirection.class, new Predicate<BlockRailBase.EnumRailDirection>()
    {
        public boolean apply(@Nullable BlockRailBase.EnumRailDirection p_apply_1_)
        {
            return p_apply_1_ != BlockRailBase.EnumRailDirection.NORTH_EAST && p_apply_1_ != BlockRailBase.EnumRailDirection.NORTH_WEST && p_apply_1_ != BlockRailBase.EnumRailDirection.SOUTH_EAST && p_apply_1_ != BlockRailBase.EnumRailDirection.SOUTH_WEST;
        }
    });
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public BlockRailDetector()
    {
        super(true);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(POWERED, Boolean.valueOf(false)).func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
        this.func_149675_a(true);
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 20;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     * @deprecated call via {@link IBlockState#canProvidePower()} whenever possible. Implementing/overriding is fine.
     */
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    public void func_180634_a(World p_180634_1_, BlockPos p_180634_2_, IBlockState p_180634_3_, Entity p_180634_4_)
    {
        if (!p_180634_1_.isRemote)
        {
            if (!((Boolean)p_180634_3_.get(POWERED)).booleanValue())
            {
                this.updatePoweredState(p_180634_1_, p_180634_2_, p_180634_3_);
            }
        }
    }

    public void func_180645_a(World p_180645_1_, BlockPos p_180645_2_, IBlockState p_180645_3_, Random p_180645_4_)
    {
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        if (!p_180650_1_.isRemote && ((Boolean)p_180650_3_.get(POWERED)).booleanValue())
        {
            this.updatePoweredState(p_180650_1_, p_180650_2_, p_180650_3_);
        }
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
            return side == EnumFacing.UP ? 15 : 0;
        }
    }

    private void updatePoweredState(World worldIn, BlockPos pos, IBlockState state)
    {
        boolean flag = ((Boolean)state.get(POWERED)).booleanValue();
        boolean flag1 = false;
        List<EntityMinecart> list = this.<EntityMinecart>func_176571_a(worldIn, pos, EntityMinecart.class);

        if (!list.isEmpty())
        {
            flag1 = true;
        }

        if (flag1 && !flag)
        {
            worldIn.setBlockState(pos, state.func_177226_a(POWERED, Boolean.valueOf(true)), 3);
            this.updateConnectedRails(worldIn, pos, state, true);
            worldIn.func_175685_c(pos, this, false);
            worldIn.func_175685_c(pos.down(), this, false);
            worldIn.func_175704_b(pos, pos);
        }

        if (!flag1 && flag)
        {
            worldIn.setBlockState(pos, state.func_177226_a(POWERED, Boolean.valueOf(false)), 3);
            this.updateConnectedRails(worldIn, pos, state, false);
            worldIn.func_175685_c(pos, this, false);
            worldIn.func_175685_c(pos.down(), this, false);
            worldIn.func_175704_b(pos, pos);
        }

        if (flag1)
        {
            worldIn.func_175684_a(new BlockPos(pos), this, this.tickRate(worldIn));
        }

        worldIn.updateComparatorOutputLevel(pos, this);
    }

    protected void updateConnectedRails(World worldIn, BlockPos pos, IBlockState state, boolean powered)
    {
        BlockRailBase.Rail blockrailbase$rail = new BlockRailBase.Rail(worldIn, pos, state);

        for (BlockPos blockpos : blockrailbase$rail.func_185763_a())
        {
            IBlockState iblockstate = worldIn.getBlockState(blockpos);

            if (iblockstate != null)
            {
                iblockstate.func_189546_a(worldIn, blockpos, iblockstate.getBlock(), pos);
            }
        }
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        super.func_176213_c(p_176213_1_, p_176213_2_, p_176213_3_);
        this.updatePoweredState(p_176213_1_, p_176213_2_, p_176213_3_);
    }

    public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty()
    {
        return SHAPE;
    }

    /**
     * @deprecated call via {@link IBlockState#hasComparatorInputOverride()} whenever possible. Implementing/overriding
     * is fine.
     */
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    /**
     * @deprecated call via {@link IBlockState#getComparatorInputOverride(World,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        if (((Boolean)blockState.get(POWERED)).booleanValue())
        {
            List<EntityMinecartCommandBlock> list = this.<EntityMinecartCommandBlock>func_176571_a(worldIn, pos, EntityMinecartCommandBlock.class);

            if (!list.isEmpty())
            {
                return ((EntityMinecartCommandBlock)list.get(0)).getCommandBlockLogic().getSuccessCount();
            }

            List<EntityMinecart> list1 = this.<EntityMinecart>func_176571_a(worldIn, pos, EntityMinecart.class, EntitySelectors.HAS_INVENTORY);

            if (!list1.isEmpty())
            {
                return Container.calcRedstoneFromInventory((IInventory)list1.get(0));
            }
        }

        return 0;
    }

    protected <T extends EntityMinecart> List<T> func_176571_a(World p_176571_1_, BlockPos p_176571_2_, Class<T> p_176571_3_, Predicate<Entity>... p_176571_4_)
    {
        AxisAlignedBB axisalignedbb = this.getDectectionBox(p_176571_2_);
        return p_176571_4_.length != 1 ? p_176571_1_.func_72872_a(p_176571_3_, axisalignedbb) : p_176571_1_.getEntitiesWithinAABB(p_176571_3_, axisalignedbb, p_176571_4_[0]);
    }

    private AxisAlignedBB getDectectionBox(BlockPos pos)
    {
        float f = 0.2F;
        return new AxisAlignedBB((double)((float)pos.getX() + 0.2F), (double)pos.getY(), (double)((float)pos.getZ() + 0.2F), (double)((float)(pos.getX() + 1) - 0.2F), (double)((float)(pos.getY() + 1) - 0.2F), (double)((float)(pos.getZ() + 1) - 0.2F));
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.func_177016_a(p_176203_1_ & 7)).func_177226_a(POWERED, Boolean.valueOf((p_176203_1_ & 8) > 0));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;
        i = i | ((BlockRailBase.EnumRailDirection)p_176201_1_.get(SHAPE)).func_177015_a();

        if (((Boolean)p_176201_1_.get(POWERED)).booleanValue())
        {
            i |= 8;
        }

        return i;
    }

    @SuppressWarnings("incomplete-switch")

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
                switch ((BlockRailBase.EnumRailDirection)state.get(SHAPE))
                {
                    case ASCENDING_EAST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

                    case ASCENDING_WEST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

                    case ASCENDING_NORTH:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

                    case ASCENDING_SOUTH:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

                    case SOUTH_EAST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);

                    case SOUTH_WEST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

                    case NORTH_WEST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);

                    case NORTH_EAST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);
                }

            case COUNTERCLOCKWISE_90:
                switch ((BlockRailBase.EnumRailDirection)state.get(SHAPE))
                {
                    case ASCENDING_EAST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

                    case ASCENDING_WEST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

                    case ASCENDING_NORTH:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

                    case ASCENDING_SOUTH:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

                    case SOUTH_EAST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

                    case SOUTH_WEST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);

                    case NORTH_WEST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);

                    case NORTH_EAST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);

                    case NORTH_SOUTH:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);

                    case EAST_WEST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
                }

            case CLOCKWISE_90:
                switch ((BlockRailBase.EnumRailDirection)state.get(SHAPE))
                {
                    case ASCENDING_EAST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

                    case ASCENDING_WEST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

                    case ASCENDING_NORTH:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

                    case ASCENDING_SOUTH:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

                    case SOUTH_EAST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);

                    case SOUTH_WEST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);

                    case NORTH_WEST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

                    case NORTH_EAST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);

                    case NORTH_SOUTH:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST);

                    case EAST_WEST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH);
                }

            default:
                return state;
        }
    }

    @SuppressWarnings("incomplete-switch")

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)state.get(SHAPE);

        switch (mirrorIn)
        {
            case LEFT_RIGHT:
                switch (blockrailbase$enumraildirection)
                {
                    case ASCENDING_NORTH:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_SOUTH);

                    case ASCENDING_SOUTH:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_NORTH);

                    case SOUTH_EAST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

                    case SOUTH_WEST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);

                    case NORTH_WEST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);

                    case NORTH_EAST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);

                    default:
                        return super.mirror(state, mirrorIn);
                }

            case FRONT_BACK:
                switch (blockrailbase$enumraildirection)
                {
                    case ASCENDING_EAST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_WEST);

                    case ASCENDING_WEST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.ASCENDING_EAST);

                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;

                    case SOUTH_EAST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_WEST);

                    case SOUTH_WEST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.SOUTH_EAST);

                    case NORTH_WEST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.NORTH_EAST);

                    case NORTH_EAST:
                        return state.func_177226_a(SHAPE, BlockRailBase.EnumRailDirection.NORTH_WEST);
                }
        }

        return super.mirror(state, mirrorIn);
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {SHAPE, POWERED});
    }
}
