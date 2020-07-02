package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonMoving extends BlockContainer
{
    public static final PropertyDirection field_176426_a = BlockPistonExtension.FACING;
    public static final PropertyEnum<BlockPistonExtension.EnumPistonType> field_176425_b = BlockPistonExtension.TYPE;

    public BlockPistonMoving()
    {
        super(Material.PISTON);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176426_a, EnumFacing.NORTH).func_177226_a(field_176425_b, BlockPistonExtension.EnumPistonType.DEFAULT));
        this.func_149711_c(-1.0F);
    }

    @Nullable
    public TileEntity func_149915_a(World p_149915_1_, int p_149915_2_)
    {
        return null;
    }

    public static TileEntity func_185588_a(IBlockState p_185588_0_, EnumFacing p_185588_1_, boolean p_185588_2_, boolean p_185588_3_)
    {
        return new TileEntityPiston(p_185588_0_, p_185588_1_, p_185588_2_, p_185588_3_);
    }

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
        TileEntity tileentity = p_180663_1_.getTileEntity(p_180663_2_);

        if (tileentity instanceof TileEntityPiston)
        {
            ((TileEntityPiston)tileentity).clearPistonTileEntity();
        }
        else
        {
            super.func_180663_b(p_180663_1_, p_180663_2_, p_180663_3_);
        }
    }

    public boolean func_176196_c(World p_176196_1_, BlockPos p_176196_2_)
    {
        return false;
    }

    public boolean func_176198_a(World p_176198_1_, BlockPos p_176198_2_, EnumFacing p_176198_3_)
    {
        return false;
    }

    /**
     * Called after a player destroys this Block - the posiiton pos may no longer hold the state indicated.
     */
    public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state)
    {
        BlockPos blockpos = pos.offset(((EnumFacing)state.get(field_176426_a)).getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(blockpos);

        if (iblockstate.getBlock() instanceof BlockPistonBase && ((Boolean)iblockstate.get(BlockPistonBase.EXTENDED)).booleanValue())
        {
            worldIn.func_175698_g(blockpos);
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

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        if (!p_180639_1_.isRemote && p_180639_1_.getTileEntity(p_180639_2_) == null)
        {
            p_180639_1_.func_175698_g(p_180639_2_);
            return true;
        }
        else
        {
            return false;
        }
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Items.AIR;
    }

    public void func_180653_a(World p_180653_1_, BlockPos p_180653_2_, IBlockState p_180653_3_, float p_180653_4_, int p_180653_5_)
    {
        if (!p_180653_1_.isRemote)
        {
            TileEntityPiston tileentitypiston = this.func_185589_c(p_180653_1_, p_180653_2_);

            if (tileentitypiston != null)
            {
                IBlockState iblockstate = tileentitypiston.func_174927_b();
                iblockstate.getBlock().func_176226_b(p_180653_1_, p_180653_2_, iblockstate, 0);
            }
        }
    }

    @Nullable
    public RayTraceResult func_180636_a(IBlockState p_180636_1_, World p_180636_2_, BlockPos p_180636_3_, Vec3d p_180636_4_, Vec3d p_180636_5_)
    {
        return null;
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        if (!p_189540_2_.isRemote)
        {
            p_189540_2_.getTileEntity(p_189540_3_);
        }
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState p_180646_1_, IBlockAccess p_180646_2_, BlockPos p_180646_3_)
    {
        TileEntityPiston tileentitypiston = this.func_185589_c(p_180646_2_, p_180646_3_);
        return tileentitypiston == null ? null : tileentitypiston.func_184321_a(p_180646_2_, p_180646_3_);
    }

    public void func_185477_a(IBlockState p_185477_1_, World p_185477_2_, BlockPos p_185477_3_, AxisAlignedBB p_185477_4_, List<AxisAlignedBB> p_185477_5_, @Nullable Entity p_185477_6_, boolean p_185477_7_)
    {
        TileEntityPiston tileentitypiston = this.func_185589_c(p_185477_2_, p_185477_3_);

        if (tileentitypiston != null)
        {
            tileentitypiston.func_190609_a(p_185477_2_, p_185477_3_, p_185477_4_, p_185477_5_, p_185477_6_);
        }
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        TileEntityPiston tileentitypiston = this.func_185589_c(p_185496_2_, p_185496_3_);
        return tileentitypiston != null ? tileentitypiston.func_184321_a(p_185496_2_, p_185496_3_) : field_185505_j;
    }

    @Nullable
    private TileEntityPiston func_185589_c(IBlockAccess p_185589_1_, BlockPos p_185589_2_)
    {
        TileEntity tileentity = p_185589_1_.getTileEntity(p_185589_2_);
        return tileentity instanceof TileEntityPiston ? (TileEntityPiston)tileentity : null;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return ItemStack.EMPTY;
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(field_176426_a, BlockPistonExtension.func_176322_b(p_176203_1_)).func_177226_a(field_176425_b, (p_176203_1_ & 8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
     * fine.
     */
    public IBlockState rotate(IBlockState state, Rotation rot)
    {
        return state.func_177226_a(field_176426_a, rot.rotate((EnumFacing)state.get(field_176426_a)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.toRotation((EnumFacing)state.get(field_176426_a)));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;
        i = i | ((EnumFacing)p_176201_1_.get(field_176426_a)).getIndex();

        if (p_176201_1_.get(field_176425_b) == BlockPistonExtension.EnumPistonType.STICKY)
        {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176426_a, field_176425_b});
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
