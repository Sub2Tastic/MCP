package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

public class BlockChest extends BlockContainer
{
    public static final PropertyDirection FACING = BlockHorizontal.HORIZONTAL_FACING;
    protected static final AxisAlignedBB field_185557_b = new AxisAlignedBB(0.0625D, 0.0D, 0.0D, 0.9375D, 0.875D, 0.9375D);
    protected static final AxisAlignedBB field_185558_c = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 1.0D);
    protected static final AxisAlignedBB field_185559_d = new AxisAlignedBB(0.0D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
    protected static final AxisAlignedBB field_185560_e = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 1.0D, 0.875D, 0.9375D);
    protected static final AxisAlignedBB field_185561_f = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
    public final BlockChest.Type field_149956_a;

    protected BlockChest(BlockChest.Type p_i46689_1_)
    {
        super(Material.WOOD);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(FACING, EnumFacing.NORTH));
        this.field_149956_a = p_i46689_1_;
        this.func_149647_a(p_i46689_1_ == BlockChest.Type.TRAP ? CreativeTabs.REDSTONE : CreativeTabs.DECORATIONS);
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    public boolean func_190946_v(IBlockState p_190946_1_)
    {
        return true;
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     * @deprecated call via {@link IBlockState#getRenderType()} whenever possible. Implementing/overriding is fine.
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        if (p_185496_2_.getBlockState(p_185496_3_.north()).getBlock() == this)
        {
            return field_185557_b;
        }
        else if (p_185496_2_.getBlockState(p_185496_3_.south()).getBlock() == this)
        {
            return field_185558_c;
        }
        else if (p_185496_2_.getBlockState(p_185496_3_.west()).getBlock() == this)
        {
            return field_185559_d;
        }
        else
        {
            return p_185496_2_.getBlockState(p_185496_3_.east()).getBlock() == this ? field_185560_e : field_185561_f;
        }
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        this.func_176455_e(p_176213_1_, p_176213_2_, p_176213_3_);

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            BlockPos blockpos = p_176213_2_.offset(enumfacing);
            IBlockState iblockstate = p_176213_1_.getBlockState(blockpos);

            if (iblockstate.getBlock() == this)
            {
                this.func_176455_e(p_176213_1_, blockpos, iblockstate);
            }
        }
    }

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        return this.getDefaultState().func_177226_a(FACING, p_180642_8_.getHorizontalFacing());
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        EnumFacing enumfacing = EnumFacing.byHorizontalIndex(MathHelper.floor((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
        state = state.func_177226_a(FACING, enumfacing);
        BlockPos blockpos = pos.north();
        BlockPos blockpos1 = pos.south();
        BlockPos blockpos2 = pos.west();
        BlockPos blockpos3 = pos.east();
        boolean flag = this == worldIn.getBlockState(blockpos).getBlock();
        boolean flag1 = this == worldIn.getBlockState(blockpos1).getBlock();
        boolean flag2 = this == worldIn.getBlockState(blockpos2).getBlock();
        boolean flag3 = this == worldIn.getBlockState(blockpos3).getBlock();

        if (!flag && !flag1 && !flag2 && !flag3)
        {
            worldIn.setBlockState(pos, state, 3);
        }
        else if (enumfacing.getAxis() != EnumFacing.Axis.X || !flag && !flag1)
        {
            if (enumfacing.getAxis() == EnumFacing.Axis.Z && (flag2 || flag3))
            {
                if (flag2)
                {
                    worldIn.setBlockState(blockpos2, state, 3);
                }
                else
                {
                    worldIn.setBlockState(blockpos3, state, 3);
                }

                worldIn.setBlockState(pos, state, 3);
            }
        }
        else
        {
            if (flag)
            {
                worldIn.setBlockState(blockpos, state, 3);
            }
            else
            {
                worldIn.setBlockState(blockpos1, state, 3);
            }

            worldIn.setBlockState(pos, state, 3);
        }

        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityChest)
            {
                ((TileEntityChest)tileentity).func_190575_a(stack.func_82833_r());
            }
        }
    }

    public IBlockState func_176455_e(World p_176455_1_, BlockPos p_176455_2_, IBlockState p_176455_3_)
    {
        if (p_176455_1_.isRemote)
        {
            return p_176455_3_;
        }
        else
        {
            IBlockState iblockstate = p_176455_1_.getBlockState(p_176455_2_.north());
            IBlockState iblockstate1 = p_176455_1_.getBlockState(p_176455_2_.south());
            IBlockState iblockstate2 = p_176455_1_.getBlockState(p_176455_2_.west());
            IBlockState iblockstate3 = p_176455_1_.getBlockState(p_176455_2_.east());
            EnumFacing enumfacing = (EnumFacing)p_176455_3_.get(FACING);

            if (iblockstate.getBlock() != this && iblockstate1.getBlock() != this)
            {
                boolean flag = iblockstate.func_185913_b();
                boolean flag1 = iblockstate1.func_185913_b();

                if (iblockstate2.getBlock() == this || iblockstate3.getBlock() == this)
                {
                    BlockPos blockpos1 = iblockstate2.getBlock() == this ? p_176455_2_.west() : p_176455_2_.east();
                    IBlockState iblockstate7 = p_176455_1_.getBlockState(blockpos1.north());
                    IBlockState iblockstate6 = p_176455_1_.getBlockState(blockpos1.south());
                    enumfacing = EnumFacing.SOUTH;
                    EnumFacing enumfacing2;

                    if (iblockstate2.getBlock() == this)
                    {
                        enumfacing2 = (EnumFacing)iblockstate2.get(FACING);
                    }
                    else
                    {
                        enumfacing2 = (EnumFacing)iblockstate3.get(FACING);
                    }

                    if (enumfacing2 == EnumFacing.NORTH)
                    {
                        enumfacing = EnumFacing.NORTH;
                    }

                    if ((flag || iblockstate7.func_185913_b()) && !flag1 && !iblockstate6.func_185913_b())
                    {
                        enumfacing = EnumFacing.SOUTH;
                    }

                    if ((flag1 || iblockstate6.func_185913_b()) && !flag && !iblockstate7.func_185913_b())
                    {
                        enumfacing = EnumFacing.NORTH;
                    }
                }
            }
            else
            {
                BlockPos blockpos = iblockstate.getBlock() == this ? p_176455_2_.north() : p_176455_2_.south();
                IBlockState iblockstate4 = p_176455_1_.getBlockState(blockpos.west());
                IBlockState iblockstate5 = p_176455_1_.getBlockState(blockpos.east());
                enumfacing = EnumFacing.EAST;
                EnumFacing enumfacing1;

                if (iblockstate.getBlock() == this)
                {
                    enumfacing1 = (EnumFacing)iblockstate.get(FACING);
                }
                else
                {
                    enumfacing1 = (EnumFacing)iblockstate1.get(FACING);
                }

                if (enumfacing1 == EnumFacing.WEST)
                {
                    enumfacing = EnumFacing.WEST;
                }

                if ((iblockstate2.func_185913_b() || iblockstate4.func_185913_b()) && !iblockstate3.func_185913_b() && !iblockstate5.func_185913_b())
                {
                    enumfacing = EnumFacing.EAST;
                }

                if ((iblockstate3.func_185913_b() || iblockstate5.func_185913_b()) && !iblockstate2.func_185913_b() && !iblockstate4.func_185913_b())
                {
                    enumfacing = EnumFacing.WEST;
                }
            }

            p_176455_3_ = p_176455_3_.func_177226_a(FACING, enumfacing);
            p_176455_1_.setBlockState(p_176455_2_, p_176455_3_, 3);
            return p_176455_3_;
        }
    }

    public IBlockState func_176458_f(World p_176458_1_, BlockPos p_176458_2_, IBlockState p_176458_3_)
    {
        EnumFacing enumfacing = null;

        for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
        {
            IBlockState iblockstate = p_176458_1_.getBlockState(p_176458_2_.offset(enumfacing1));

            if (iblockstate.getBlock() == this)
            {
                return p_176458_3_;
            }

            if (iblockstate.func_185913_b())
            {
                if (enumfacing != null)
                {
                    enumfacing = null;
                    break;
                }

                enumfacing = enumfacing1;
            }
        }

        if (enumfacing != null)
        {
            return p_176458_3_.func_177226_a(FACING, enumfacing.getOpposite());
        }
        else
        {
            EnumFacing enumfacing2 = (EnumFacing)p_176458_3_.get(FACING);

            if (p_176458_1_.getBlockState(p_176458_2_.offset(enumfacing2)).func_185913_b())
            {
                enumfacing2 = enumfacing2.getOpposite();
            }

            if (p_176458_1_.getBlockState(p_176458_2_.offset(enumfacing2)).func_185913_b())
            {
                enumfacing2 = enumfacing2.rotateY();
            }

            if (p_176458_1_.getBlockState(p_176458_2_.offset(enumfacing2)).func_185913_b())
            {
                enumfacing2 = enumfacing2.getOpposite();
            }

            return p_176458_3_.func_177226_a(FACING, enumfacing2);
        }
    }

    public boolean func_176196_c(World p_176196_1_, BlockPos p_176196_2_)
    {
        int i = 0;
        BlockPos blockpos = p_176196_2_.west();
        BlockPos blockpos1 = p_176196_2_.east();
        BlockPos blockpos2 = p_176196_2_.north();
        BlockPos blockpos3 = p_176196_2_.south();

        if (p_176196_1_.getBlockState(blockpos).getBlock() == this)
        {
            if (this.func_176454_e(p_176196_1_, blockpos))
            {
                return false;
            }

            ++i;
        }

        if (p_176196_1_.getBlockState(blockpos1).getBlock() == this)
        {
            if (this.func_176454_e(p_176196_1_, blockpos1))
            {
                return false;
            }

            ++i;
        }

        if (p_176196_1_.getBlockState(blockpos2).getBlock() == this)
        {
            if (this.func_176454_e(p_176196_1_, blockpos2))
            {
                return false;
            }

            ++i;
        }

        if (p_176196_1_.getBlockState(blockpos3).getBlock() == this)
        {
            if (this.func_176454_e(p_176196_1_, blockpos3))
            {
                return false;
            }

            ++i;
        }

        return i <= 1;
    }

    private boolean func_176454_e(World p_176454_1_, BlockPos p_176454_2_)
    {
        if (p_176454_1_.getBlockState(p_176454_2_).getBlock() != this)
        {
            return false;
        }
        else
        {
            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
                if (p_176454_1_.getBlockState(p_176454_2_.offset(enumfacing)).getBlock() == this)
                {
                    return true;
                }
            }

            return false;
        }
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        super.func_189540_a(p_189540_1_, p_189540_2_, p_189540_3_, p_189540_4_, p_189540_5_);
        TileEntity tileentity = p_189540_2_.getTileEntity(p_189540_3_);

        if (tileentity instanceof TileEntityChest)
        {
            tileentity.updateContainingBlockInfo();
        }
    }

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
        TileEntity tileentity = p_180663_1_.getTileEntity(p_180663_2_);

        if (tileentity instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(p_180663_1_, p_180663_2_, (IInventory)tileentity);
            p_180663_1_.updateComparatorOutputLevel(p_180663_2_, this);
        }

        super.func_180663_b(p_180663_1_, p_180663_2_, p_180663_3_);
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        if (p_180639_1_.isRemote)
        {
            return true;
        }
        else
        {
            ILockableContainer ilockablecontainer = this.func_180676_d(p_180639_1_, p_180639_2_);

            if (ilockablecontainer != null)
            {
                p_180639_4_.func_71007_a(ilockablecontainer);

                if (this.field_149956_a == BlockChest.Type.BASIC)
                {
                    p_180639_4_.addStat(StatList.OPEN_CHEST);
                }
                else if (this.field_149956_a == BlockChest.Type.TRAP)
                {
                    p_180639_4_.addStat(StatList.TRIGGER_TRAPPED_CHEST);
                }
            }

            return true;
        }
    }

    @Nullable
    public ILockableContainer func_180676_d(World p_180676_1_, BlockPos p_180676_2_)
    {
        return this.func_189418_a(p_180676_1_, p_180676_2_, false);
    }

    @Nullable
    public ILockableContainer func_189418_a(World p_189418_1_, BlockPos p_189418_2_, boolean p_189418_3_)
    {
        TileEntity tileentity = p_189418_1_.getTileEntity(p_189418_2_);

        if (!(tileentity instanceof TileEntityChest))
        {
            return null;
        }
        else
        {
            ILockableContainer ilockablecontainer = (TileEntityChest)tileentity;

            if (!p_189418_3_ && this.func_176457_m(p_189418_1_, p_189418_2_))
            {
                return null;
            }
            else
            {
                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                {
                    BlockPos blockpos = p_189418_2_.offset(enumfacing);
                    Block block = p_189418_1_.getBlockState(blockpos).getBlock();

                    if (block == this)
                    {
                        if (this.func_176457_m(p_189418_1_, blockpos))
                        {
                            return null;
                        }

                        TileEntity tileentity1 = p_189418_1_.getTileEntity(blockpos);

                        if (tileentity1 instanceof TileEntityChest)
                        {
                            if (enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH)
                            {
                                ilockablecontainer = new InventoryLargeChest("container.chestDouble", ilockablecontainer, (TileEntityChest)tileentity1);
                            }
                            else
                            {
                                ilockablecontainer = new InventoryLargeChest("container.chestDouble", (TileEntityChest)tileentity1, ilockablecontainer);
                            }
                        }
                    }
                }

                return ilockablecontainer;
            }
        }
    }

    public TileEntity func_149915_a(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityChest();
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     * @deprecated call via {@link IBlockState#canProvidePower()} whenever possible. Implementing/overriding is fine.
     */
    public boolean canProvidePower(IBlockState state)
    {
        return this.field_149956_a == BlockChest.Type.TRAP;
    }

    /**
     * @deprecated call via {@link IBlockState#getWeakPower(IBlockAccess,BlockPos,EnumFacing)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        if (!blockState.canProvidePower())
        {
            return 0;
        }
        else
        {
            int i = 0;
            TileEntity tileentity = blockAccess.getTileEntity(pos);

            if (tileentity instanceof TileEntityChest)
            {
                i = ((TileEntityChest)tileentity).numPlayersUsing;
            }

            return MathHelper.clamp(i, 0, 15);
        }
    }

    /**
     * @deprecated call via {@link IBlockState#getStrongPower(IBlockAccess,BlockPos,EnumFacing)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return side == EnumFacing.UP ? blockState.getWeakPower(blockAccess, pos, side) : 0;
    }

    private boolean func_176457_m(World p_176457_1_, BlockPos p_176457_2_)
    {
        return this.isBelowSolidBlock(p_176457_1_, p_176457_2_) || this.func_176453_o(p_176457_1_, p_176457_2_);
    }

    private boolean isBelowSolidBlock(World worldIn, BlockPos p_176456_2_)
    {
        return worldIn.getBlockState(p_176456_2_.up()).func_185915_l();
    }

    private boolean func_176453_o(World p_176453_1_, BlockPos p_176453_2_)
    {
        for (Entity entity : p_176453_1_.func_72872_a(EntityOcelot.class, new AxisAlignedBB((double)p_176453_2_.getX(), (double)(p_176453_2_.getY() + 1), (double)p_176453_2_.getZ(), (double)(p_176453_2_.getX() + 1), (double)(p_176453_2_.getY() + 2), (double)(p_176453_2_.getZ() + 1))))
        {
            EntityOcelot entityocelot = (EntityOcelot)entity;

            if (entityocelot.isSitting())
            {
                return true;
            }
        }

        return false;
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
        return Container.calcRedstoneFromInventory(this.func_180676_d(worldIn, pos));
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        EnumFacing enumfacing = EnumFacing.byIndex(p_176203_1_);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().func_177226_a(FACING, enumfacing);
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((EnumFacing)p_176201_1_.get(FACING)).getIndex();
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
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }

    public static enum Type
    {
        BASIC,
        TRAP;
    }
}
