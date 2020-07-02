package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryDefaulted;
import net.minecraft.world.World;

public class BlockDispenser extends BlockContainer
{
    public static final PropertyDirection FACING = BlockDirectional.FACING;
    public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");
    public static final RegistryDefaulted<Item, IBehaviorDispenseItem> DISPENSE_BEHAVIOR_REGISTRY = new RegistryDefaulted<Item, IBehaviorDispenseItem>(new BehaviorDefaultDispenseItem());
    protected Random field_149942_b = new Random();

    protected BlockDispenser()
    {
        super(Material.ROCK);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(FACING, EnumFacing.NORTH).func_177226_a(TRIGGERED, Boolean.valueOf(false)));
        this.func_149647_a(CreativeTabs.REDSTONE);
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 4;
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        super.func_176213_c(p_176213_1_, p_176213_2_, p_176213_3_);
        this.func_176438_e(p_176213_1_, p_176213_2_, p_176213_3_);
    }

    private void func_176438_e(World p_176438_1_, BlockPos p_176438_2_, IBlockState p_176438_3_)
    {
        if (!p_176438_1_.isRemote)
        {
            EnumFacing enumfacing = (EnumFacing)p_176438_3_.get(FACING);
            boolean flag = p_176438_1_.getBlockState(p_176438_2_.north()).func_185913_b();
            boolean flag1 = p_176438_1_.getBlockState(p_176438_2_.south()).func_185913_b();

            if (enumfacing == EnumFacing.NORTH && flag && !flag1)
            {
                enumfacing = EnumFacing.SOUTH;
            }
            else if (enumfacing == EnumFacing.SOUTH && flag1 && !flag)
            {
                enumfacing = EnumFacing.NORTH;
            }
            else
            {
                boolean flag2 = p_176438_1_.getBlockState(p_176438_2_.west()).func_185913_b();
                boolean flag3 = p_176438_1_.getBlockState(p_176438_2_.east()).func_185913_b();

                if (enumfacing == EnumFacing.WEST && flag2 && !flag3)
                {
                    enumfacing = EnumFacing.EAST;
                }
                else if (enumfacing == EnumFacing.EAST && flag3 && !flag2)
                {
                    enumfacing = EnumFacing.WEST;
                }
            }

            p_176438_1_.setBlockState(p_176438_2_, p_176438_3_.func_177226_a(FACING, enumfacing).func_177226_a(TRIGGERED, Boolean.valueOf(false)), 2);
        }
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        if (p_180639_1_.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity tileentity = p_180639_1_.getTileEntity(p_180639_2_);

            if (tileentity instanceof TileEntityDispenser)
            {
                p_180639_4_.func_71007_a((TileEntityDispenser)tileentity);

                if (tileentity instanceof TileEntityDropper)
                {
                    p_180639_4_.addStat(StatList.INSPECT_DROPPER);
                }
                else
                {
                    p_180639_4_.addStat(StatList.INSPECT_DISPENSER);
                }
            }

            return true;
        }
    }

    protected void dispense(World worldIn, BlockPos pos)
    {
        BlockSourceImpl blocksourceimpl = new BlockSourceImpl(worldIn, pos);
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser)blocksourceimpl.getBlockTileEntity();

        if (tileentitydispenser != null)
        {
            int i = tileentitydispenser.getDispenseSlot();

            if (i < 0)
            {
                worldIn.func_175718_b(1001, pos, 0);
            }
            else
            {
                ItemStack itemstack = tileentitydispenser.getStackInSlot(i);
                IBehaviorDispenseItem ibehaviordispenseitem = this.getBehavior(itemstack);

                if (ibehaviordispenseitem != IBehaviorDispenseItem.field_82483_a)
                {
                    tileentitydispenser.setInventorySlotContents(i, ibehaviordispenseitem.func_82482_a(blocksourceimpl, itemstack));
                }
            }
        }
    }

    protected IBehaviorDispenseItem getBehavior(ItemStack stack)
    {
        return DISPENSE_BEHAVIOR_REGISTRY.getOrDefault(stack.getItem());
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        boolean flag = p_189540_2_.isBlockPowered(p_189540_3_) || p_189540_2_.isBlockPowered(p_189540_3_.up());
        boolean flag1 = ((Boolean)p_189540_1_.get(TRIGGERED)).booleanValue();

        if (flag && !flag1)
        {
            p_189540_2_.func_175684_a(p_189540_3_, this, this.tickRate(p_189540_2_));
            p_189540_2_.setBlockState(p_189540_3_, p_189540_1_.func_177226_a(TRIGGERED, Boolean.valueOf(true)), 4);
        }
        else if (!flag && flag1)
        {
            p_189540_2_.setBlockState(p_189540_3_, p_189540_1_.func_177226_a(TRIGGERED, Boolean.valueOf(false)), 4);
        }
    }

    public void func_180650_b(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_)
    {
        if (!p_180650_1_.isRemote)
        {
            this.dispense(p_180650_1_, p_180650_2_);
        }
    }

    public TileEntity func_149915_a(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityDispenser();
    }

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        return this.getDefaultState().func_177226_a(FACING, EnumFacing.func_190914_a(p_180642_2_, p_180642_8_)).func_177226_a(TRIGGERED, Boolean.valueOf(false));
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.func_177226_a(FACING, EnumFacing.func_190914_a(pos, placer)), 2);

        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityDispenser)
            {
                ((TileEntityDispenser)tileentity).func_190575_a(stack.func_82833_r());
            }
        }
    }

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
        TileEntity tileentity = p_180663_1_.getTileEntity(p_180663_2_);

        if (tileentity instanceof TileEntityDispenser)
        {
            InventoryHelper.dropInventoryItems(p_180663_1_, p_180663_2_, (TileEntityDispenser)tileentity);
            p_180663_1_.updateComparatorOutputLevel(p_180663_2_, this);
        }

        super.func_180663_b(p_180663_1_, p_180663_2_, p_180663_3_);
    }

    /**
     * Get the position where the dispenser at the given Coordinates should dispense to.
     */
    public static IPosition getDispensePosition(IBlockSource coords)
    {
        EnumFacing enumfacing = (EnumFacing)coords.getBlockState().get(FACING);
        double d0 = coords.getX() + 0.7D * (double)enumfacing.getXOffset();
        double d1 = coords.getY() + 0.7D * (double)enumfacing.getYOffset();
        double d2 = coords.getZ() + 0.7D * (double)enumfacing.getZOffset();
        return new PositionImpl(d0, d1, d2);
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
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     * @deprecated call via {@link IBlockState#getRenderType()} whenever possible. Implementing/overriding is fine.
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(FACING, EnumFacing.byIndex(p_176203_1_ & 7)).func_177226_a(TRIGGERED, Boolean.valueOf((p_176203_1_ & 8) > 0));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;
        i = i | ((EnumFacing)p_176201_1_.get(FACING)).getIndex();

        if (((Boolean)p_176201_1_.get(TRIGGERED)).booleanValue())
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
        return new BlockStateContainer(this, new IProperty[] {FACING, TRIGGERED});
    }
}
