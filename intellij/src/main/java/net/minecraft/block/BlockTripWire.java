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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWire extends Block
{
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyBool ATTACHED = PropertyBool.create("attached");
    public static final PropertyBool DISARMED = PropertyBool.create("disarmed");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0625D, 0.0D, 1.0D, 0.15625D, 1.0D);
    protected static final AxisAlignedBB TRIP_WRITE_ATTACHED_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

    public BlockTripWire()
    {
        super(Material.MISCELLANEOUS);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(POWERED, Boolean.valueOf(false)).func_177226_a(ATTACHED, Boolean.valueOf(false)).func_177226_a(DISARMED, Boolean.valueOf(false)).func_177226_a(NORTH, Boolean.valueOf(false)).func_177226_a(EAST, Boolean.valueOf(false)).func_177226_a(SOUTH, Boolean.valueOf(false)).func_177226_a(WEST, Boolean.valueOf(false)));
        this.func_149675_a(true);
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        return !((Boolean)p_185496_1_.get(ATTACHED)).booleanValue() ? TRIP_WRITE_ATTACHED_AABB : AABB;
    }

    public IBlockState func_176221_a(IBlockState p_176221_1_, IBlockAccess p_176221_2_, BlockPos p_176221_3_)
    {
        return p_176221_1_.func_177226_a(NORTH, Boolean.valueOf(func_176287_c(p_176221_2_, p_176221_3_, p_176221_1_, EnumFacing.NORTH))).func_177226_a(EAST, Boolean.valueOf(func_176287_c(p_176221_2_, p_176221_3_, p_176221_1_, EnumFacing.EAST))).func_177226_a(SOUTH, Boolean.valueOf(func_176287_c(p_176221_2_, p_176221_3_, p_176221_1_, EnumFacing.SOUTH))).func_177226_a(WEST, Boolean.valueOf(func_176287_c(p_176221_2_, p_176221_3_, p_176221_1_, EnumFacing.WEST)));
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

    public BlockRenderLayer func_180664_k()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Items.STRING;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Items.STRING);
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        p_176213_1_.setBlockState(p_176213_2_, p_176213_3_, 3);
        this.notifyHook(p_176213_1_, p_176213_2_, p_176213_3_);
    }

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
        this.notifyHook(p_180663_1_, p_180663_2_, p_180663_3_.func_177226_a(POWERED, Boolean.valueOf(true)));
    }

    /**
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually
     * collect this block
     */
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (!worldIn.isRemote)
        {
            if (!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() == Items.SHEARS)
            {
                worldIn.setBlockState(pos, state.func_177226_a(DISARMED, Boolean.valueOf(true)), 4);
            }
        }
    }

    private void notifyHook(World worldIn, BlockPos pos, IBlockState state)
    {
        for (EnumFacing enumfacing : new EnumFacing[] {EnumFacing.SOUTH, EnumFacing.WEST})
        {
            for (int i = 1; i < 42; ++i)
            {
                BlockPos blockpos = pos.offset(enumfacing, i);
                IBlockState iblockstate = worldIn.getBlockState(blockpos);

                if (iblockstate.getBlock() == Blocks.TRIPWIRE_HOOK)
                {
                    if (iblockstate.get(BlockTripWireHook.FACING) == enumfacing.getOpposite())
                    {
                        Blocks.TRIPWIRE_HOOK.calculateState(worldIn, blockpos, iblockstate, false, true, i, state);
                    }

                    break;
                }

                if (iblockstate.getBlock() != Blocks.TRIPWIRE)
                {
                    break;
                }
            }
        }
    }

    public void func_180634_a(World p_180634_1_, BlockPos p_180634_2_, IBlockState p_180634_3_, Entity p_180634_4_)
    {
        if (!p_180634_1_.isRemote)
        {
            if (!((Boolean)p_180634_3_.get(POWERED)).booleanValue())
            {
                this.updateState(p_180634_1_, p_180634_2_);
            }
        }
    }

    public void func_180645_a(World p_180645_1_, BlockPos p_180645_2_, IBlockState p_180645_3_, Random p_180645_4_)
    {
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        if (!p_180650_1_.isRemote)
        {
            if (((Boolean)p_180650_1_.getBlockState(p_180650_2_).get(POWERED)).booleanValue())
            {
                this.updateState(p_180650_1_, p_180650_2_);
            }
        }
    }

    private void updateState(World worldIn, BlockPos pos)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        boolean flag = ((Boolean)iblockstate.get(POWERED)).booleanValue();
        boolean flag1 = false;
        List <? extends Entity > list = worldIn.getEntitiesWithinAABBExcludingEntity((Entity)null, iblockstate.func_185900_c(worldIn, pos).offset(pos));

        if (!list.isEmpty())
        {
            for (Entity entity : list)
            {
                if (!entity.doesEntityNotTriggerPressurePlate())
                {
                    flag1 = true;
                    break;
                }
            }
        }

        if (flag1 != flag)
        {
            iblockstate = iblockstate.func_177226_a(POWERED, Boolean.valueOf(flag1));
            worldIn.setBlockState(pos, iblockstate, 3);
            this.notifyHook(worldIn, pos, iblockstate);
        }

        if (flag1)
        {
            worldIn.func_175684_a(new BlockPos(pos), this, this.tickRate(worldIn));
        }
    }

    public static boolean func_176287_c(IBlockAccess p_176287_0_, BlockPos p_176287_1_, IBlockState p_176287_2_, EnumFacing p_176287_3_)
    {
        BlockPos blockpos = p_176287_1_.offset(p_176287_3_);
        IBlockState iblockstate = p_176287_0_.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (block == Blocks.TRIPWIRE_HOOK)
        {
            EnumFacing enumfacing = p_176287_3_.getOpposite();
            return iblockstate.get(BlockTripWireHook.FACING) == enumfacing;
        }
        else
        {
            return block == Blocks.TRIPWIRE;
        }
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(POWERED, Boolean.valueOf((p_176203_1_ & 1) > 0)).func_177226_a(ATTACHED, Boolean.valueOf((p_176203_1_ & 4) > 0)).func_177226_a(DISARMED, Boolean.valueOf((p_176203_1_ & 8) > 0));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;

        if (((Boolean)p_176201_1_.get(POWERED)).booleanValue())
        {
            i |= 1;
        }

        if (((Boolean)p_176201_1_.get(ATTACHED)).booleanValue())
        {
            i |= 4;
        }

        if (((Boolean)p_176201_1_.get(DISARMED)).booleanValue())
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
        return new BlockStateContainer(this, new IProperty[] {POWERED, ATTACHED, DISARMED, NORTH, EAST, WEST, SOUTH});
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
