package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBanner extends BlockContainer
{
    public static final PropertyDirection field_176449_a = BlockHorizontal.HORIZONTAL_FACING;
    public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);
    protected static final AxisAlignedBB field_185550_c = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);

    protected BlockBanner()
    {
        super(Material.WOOD);
    }

    public String func_149732_F()
    {
        return I18n.func_74838_a("item.banner.white.name");
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState p_180646_1_, IBlockAccess p_180646_2_, BlockPos p_180646_3_)
    {
        return field_185506_k;
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    public boolean func_176205_b(IBlockAccess p_176205_1_, BlockPos p_176205_2_)
    {
        return true;
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    /**
     * Return true if an entity can be spawned inside the block (used to get the player's bed spawn location)
     */
    public boolean canSpawnInBlock()
    {
        return true;
    }

    public TileEntity func_149915_a(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityBanner();
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Items.field_179564_cE;
    }

    private ItemStack func_185549_e(World p_185549_1_, BlockPos p_185549_2_)
    {
        TileEntity tileentity = p_185549_1_.getTileEntity(p_185549_2_);
        return tileentity instanceof TileEntityBanner ? ((TileEntityBanner)tileentity).getItem() : ItemStack.EMPTY;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        ItemStack itemstack = this.func_185549_e(worldIn, pos);
        return itemstack.isEmpty() ? new ItemStack(Items.field_179564_cE) : itemstack;
    }

    public void func_180653_a(World p_180653_1_, BlockPos p_180653_2_, IBlockState p_180653_3_, float p_180653_4_, int p_180653_5_)
    {
        ItemStack itemstack = this.func_185549_e(p_180653_1_, p_180653_2_);

        if (itemstack.isEmpty())
        {
            super.func_180653_a(p_180653_1_, p_180653_2_, p_180653_3_, p_180653_4_, p_180653_5_);
        }
        else
        {
            spawnAsEntity(p_180653_1_, p_180653_2_, itemstack);
        }
    }

    public boolean func_176196_c(World p_176196_1_, BlockPos p_176196_2_)
    {
        return !this.func_181087_e(p_176196_1_, p_176196_2_) && super.func_176196_c(p_176196_1_, p_176196_2_);
    }

    /**
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
     * Block.removedByPlayer
     */
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (te instanceof TileEntityBanner)
        {
            TileEntityBanner tileentitybanner = (TileEntityBanner)te;
            ItemStack itemstack = tileentitybanner.getItem();
            spawnAsEntity(worldIn, pos, itemstack);
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, (TileEntity)null, stack);
        }
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }

    public static class BlockBannerHanging extends BlockBanner
    {
        protected static final AxisAlignedBB field_185551_d = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 0.78125D, 1.0D);
        protected static final AxisAlignedBB field_185552_e = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.78125D, 0.125D);
        protected static final AxisAlignedBB field_185553_f = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 0.78125D, 1.0D);
        protected static final AxisAlignedBB field_185554_g = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 0.78125D, 1.0D);

        public BlockBannerHanging()
        {
            this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176449_a, EnumFacing.NORTH));
        }

        public IBlockState rotate(IBlockState state, Rotation rot)
        {
            return state.func_177226_a(field_176449_a, rot.rotate((EnumFacing)state.get(field_176449_a)));
        }

        public IBlockState mirror(IBlockState state, Mirror mirrorIn)
        {
            return state.rotate(mirrorIn.toRotation((EnumFacing)state.get(field_176449_a)));
        }

        public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
        {
            switch ((EnumFacing)p_185496_1_.get(field_176449_a))
            {
                case NORTH:
                default:
                    return field_185551_d;

                case SOUTH:
                    return field_185552_e;

                case WEST:
                    return field_185553_f;

                case EAST:
                    return field_185554_g;
            }
        }

        public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
        {
            EnumFacing enumfacing = (EnumFacing)p_189540_1_.get(field_176449_a);

            if (!p_189540_2_.getBlockState(p_189540_3_.offset(enumfacing.getOpposite())).getMaterial().isSolid())
            {
                this.func_176226_b(p_189540_2_, p_189540_3_, p_189540_1_, 0);
                p_189540_2_.func_175698_g(p_189540_3_);
            }

            super.func_189540_a(p_189540_1_, p_189540_2_, p_189540_3_, p_189540_4_, p_189540_5_);
        }

        public IBlockState func_176203_a(int p_176203_1_)
        {
            EnumFacing enumfacing = EnumFacing.byIndex(p_176203_1_);

            if (enumfacing.getAxis() == EnumFacing.Axis.Y)
            {
                enumfacing = EnumFacing.NORTH;
            }

            return this.getDefaultState().func_177226_a(field_176449_a, enumfacing);
        }

        public int func_176201_c(IBlockState p_176201_1_)
        {
            return ((EnumFacing)p_176201_1_.get(field_176449_a)).getIndex();
        }

        protected BlockStateContainer func_180661_e()
        {
            return new BlockStateContainer(this, new IProperty[] {field_176449_a});
        }
    }

    public static class BlockBannerStanding extends BlockBanner
    {
        public BlockBannerStanding()
        {
            this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(ROTATION, Integer.valueOf(0)));
        }

        public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
        {
            return field_185550_c;
        }

        public IBlockState rotate(IBlockState state, Rotation rot)
        {
            return state.func_177226_a(ROTATION, Integer.valueOf(rot.rotate(((Integer)state.get(ROTATION)).intValue(), 16)));
        }

        public IBlockState mirror(IBlockState state, Mirror mirrorIn)
        {
            return state.func_177226_a(ROTATION, Integer.valueOf(mirrorIn.mirrorRotation(((Integer)state.get(ROTATION)).intValue(), 16)));
        }

        public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
        {
            if (!p_189540_2_.getBlockState(p_189540_3_.down()).getMaterial().isSolid())
            {
                this.func_176226_b(p_189540_2_, p_189540_3_, p_189540_1_, 0);
                p_189540_2_.func_175698_g(p_189540_3_);
            }

            super.func_189540_a(p_189540_1_, p_189540_2_, p_189540_3_, p_189540_4_, p_189540_5_);
        }

        public IBlockState func_176203_a(int p_176203_1_)
        {
            return this.getDefaultState().func_177226_a(ROTATION, Integer.valueOf(p_176203_1_));
        }

        public int func_176201_c(IBlockState p_176201_1_)
        {
            return ((Integer)p_176201_1_.get(ROTATION)).intValue();
        }

        protected BlockStateContainer func_180661_e()
        {
            return new BlockStateContainer(this, new IProperty[] {ROTATION});
        }
    }
}
