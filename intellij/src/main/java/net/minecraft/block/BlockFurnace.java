package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFurnace extends BlockContainer
{
    public static final PropertyDirection field_176447_a = BlockHorizontal.HORIZONTAL_FACING;
    private final boolean field_149932_b;
    private static boolean field_149934_M;

    protected BlockFurnace(boolean p_i45407_1_)
    {
        super(Material.ROCK);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176447_a, EnumFacing.NORTH));
        this.field_149932_b = p_i45407_1_;
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Item.getItemFromBlock(Blocks.FURNACE);
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        this.func_176445_e(p_176213_1_, p_176213_2_, p_176213_3_);
    }

    private void func_176445_e(World p_176445_1_, BlockPos p_176445_2_, IBlockState p_176445_3_)
    {
        if (!p_176445_1_.isRemote)
        {
            IBlockState iblockstate = p_176445_1_.getBlockState(p_176445_2_.north());
            IBlockState iblockstate1 = p_176445_1_.getBlockState(p_176445_2_.south());
            IBlockState iblockstate2 = p_176445_1_.getBlockState(p_176445_2_.west());
            IBlockState iblockstate3 = p_176445_1_.getBlockState(p_176445_2_.east());
            EnumFacing enumfacing = (EnumFacing)p_176445_3_.get(field_176447_a);

            if (enumfacing == EnumFacing.NORTH && iblockstate.func_185913_b() && !iblockstate1.func_185913_b())
            {
                enumfacing = EnumFacing.SOUTH;
            }
            else if (enumfacing == EnumFacing.SOUTH && iblockstate1.func_185913_b() && !iblockstate.func_185913_b())
            {
                enumfacing = EnumFacing.NORTH;
            }
            else if (enumfacing == EnumFacing.WEST && iblockstate2.func_185913_b() && !iblockstate3.func_185913_b())
            {
                enumfacing = EnumFacing.EAST;
            }
            else if (enumfacing == EnumFacing.EAST && iblockstate3.func_185913_b() && !iblockstate2.func_185913_b())
            {
                enumfacing = EnumFacing.WEST;
            }

            p_176445_1_.setBlockState(p_176445_2_, p_176445_3_.func_177226_a(field_176447_a, enumfacing), 2);
        }
    }

    @SuppressWarnings("incomplete-switch")

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that
     * this method is unrelated to {@link randomTick} and {@link #needsRandomTick}, and will always be called regardless
     * of whether the block can receive random update ticks
     */
    public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (this.field_149932_b)
        {
            EnumFacing enumfacing = (EnumFacing)stateIn.get(field_176447_a);
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
            double d2 = (double)pos.getZ() + 0.5D;
            double d3 = 0.52D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;

            if (rand.nextDouble() < 0.1D)
            {
                worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            switch (enumfacing)
            {
                case WEST:
                    worldIn.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    worldIn.func_175688_a(EnumParticleTypes.FLAME, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    break;

                case EAST:
                    worldIn.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    worldIn.func_175688_a(EnumParticleTypes.FLAME, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    break;

                case NORTH:
                    worldIn.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                    worldIn.func_175688_a(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                    break;

                case SOUTH:
                    worldIn.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
                    worldIn.func_175688_a(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
            }
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

            if (tileentity instanceof TileEntityFurnace)
            {
                p_180639_4_.func_71007_a((TileEntityFurnace)tileentity);
                p_180639_4_.addStat(StatList.INTERACT_WITH_FURNACE);
            }

            return true;
        }
    }

    public static void func_176446_a(boolean p_176446_0_, World p_176446_1_, BlockPos p_176446_2_)
    {
        IBlockState iblockstate = p_176446_1_.getBlockState(p_176446_2_);
        TileEntity tileentity = p_176446_1_.getTileEntity(p_176446_2_);
        field_149934_M = true;

        if (p_176446_0_)
        {
            p_176446_1_.setBlockState(p_176446_2_, Blocks.field_150470_am.getDefaultState().func_177226_a(field_176447_a, iblockstate.get(field_176447_a)), 3);
            p_176446_1_.setBlockState(p_176446_2_, Blocks.field_150470_am.getDefaultState().func_177226_a(field_176447_a, iblockstate.get(field_176447_a)), 3);
        }
        else
        {
            p_176446_1_.setBlockState(p_176446_2_, Blocks.FURNACE.getDefaultState().func_177226_a(field_176447_a, iblockstate.get(field_176447_a)), 3);
            p_176446_1_.setBlockState(p_176446_2_, Blocks.FURNACE.getDefaultState().func_177226_a(field_176447_a, iblockstate.get(field_176447_a)), 3);
        }

        field_149934_M = false;

        if (tileentity != null)
        {
            tileentity.validate();
            p_176446_1_.setTileEntity(p_176446_2_, tileentity);
        }
    }

    public TileEntity func_149915_a(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityFurnace();
    }

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        return this.getDefaultState().func_177226_a(field_176447_a, p_180642_8_.getHorizontalFacing().getOpposite());
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.func_177226_a(field_176447_a, placer.getHorizontalFacing().getOpposite()), 2);

        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityFurnace)
            {
                ((TileEntityFurnace)tileentity).func_145951_a(stack.func_82833_r());
            }
        }
    }

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
        if (!field_149934_M)
        {
            TileEntity tileentity = p_180663_1_.getTileEntity(p_180663_2_);

            if (tileentity instanceof TileEntityFurnace)
            {
                InventoryHelper.dropInventoryItems(p_180663_1_, p_180663_2_, (TileEntityFurnace)tileentity);
                p_180663_1_.updateComparatorOutputLevel(p_180663_2_, this);
            }
        }

        super.func_180663_b(p_180663_1_, p_180663_2_, p_180663_3_);
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

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Blocks.FURNACE);
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
        EnumFacing enumfacing = EnumFacing.byIndex(p_176203_1_);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().func_177226_a(field_176447_a, enumfacing);
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((EnumFacing)p_176201_1_.get(field_176447_a)).getIndex();
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
     * fine.
     */
    public IBlockState rotate(IBlockState state, Rotation rot)
    {
        return state.func_177226_a(field_176447_a, rot.rotate((EnumFacing)state.get(field_176447_a)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.toRotation((EnumFacing)state.get(field_176447_a)));
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176447_a});
    }
}
