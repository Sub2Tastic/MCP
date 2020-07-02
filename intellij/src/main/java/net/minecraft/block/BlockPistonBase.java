package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonBase extends BlockDirectional
{
    public static final PropertyBool EXTENDED = PropertyBool.create("extended");
    protected static final AxisAlignedBB PISTON_BASE_EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D);
    protected static final AxisAlignedBB PISTON_BASE_WEST_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB PISTON_BASE_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D);
    protected static final AxisAlignedBB PISTON_BASE_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB PISTON_BASE_UP_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
    protected static final AxisAlignedBB PISTON_BASE_DOWN_AABB = new AxisAlignedBB(0.0D, 0.25D, 0.0D, 1.0D, 1.0D, 1.0D);

    /** This piston is the sticky one? */
    private final boolean isSticky;

    public BlockPistonBase(boolean p_i45443_1_)
    {
        super(Material.PISTON);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(FACING, EnumFacing.NORTH).func_177226_a(EXTENDED, Boolean.valueOf(false)));
        this.isSticky = p_i45443_1_;
        this.func_149672_a(SoundType.STONE);
        this.func_149711_c(0.5F);
        this.func_149647_a(CreativeTabs.REDSTONE);
    }

    public boolean func_176214_u(IBlockState p_176214_1_)
    {
        return !((Boolean)p_176214_1_.get(EXTENDED)).booleanValue();
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        if (((Boolean)p_185496_1_.get(EXTENDED)).booleanValue())
        {
            switch ((EnumFacing)p_185496_1_.get(FACING))
            {
                case DOWN:
                    return PISTON_BASE_DOWN_AABB;

                case UP:
                default:
                    return PISTON_BASE_UP_AABB;

                case NORTH:
                    return PISTON_BASE_NORTH_AABB;

                case SOUTH:
                    return PISTON_BASE_SOUTH_AABB;

                case WEST:
                    return PISTON_BASE_WEST_AABB;

                case EAST:
                    return PISTON_BASE_EAST_AABB;
            }
        }
        else
        {
            return field_185505_j;
        }
    }

    public boolean func_185481_k(IBlockState p_185481_1_)
    {
        return !((Boolean)p_185481_1_.get(EXTENDED)).booleanValue() || p_185481_1_.get(FACING) == EnumFacing.DOWN;
    }

    public void func_185477_a(IBlockState p_185477_1_, World p_185477_2_, BlockPos p_185477_3_, AxisAlignedBB p_185477_4_, List<AxisAlignedBB> p_185477_5_, @Nullable Entity p_185477_6_, boolean p_185477_7_)
    {
        func_185492_a(p_185477_3_, p_185477_4_, p_185477_5_, p_185477_1_.func_185900_c(p_185477_2_, p_185477_3_));
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.func_177226_a(FACING, EnumFacing.func_190914_a(pos, placer)), 2);

        if (!worldIn.isRemote)
        {
            this.checkForMove(worldIn, pos, state);
        }
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        if (!p_189540_2_.isRemote)
        {
            this.checkForMove(p_189540_2_, p_189540_3_, p_189540_1_);
        }
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        if (!p_176213_1_.isRemote && p_176213_1_.getTileEntity(p_176213_2_) == null)
        {
            this.checkForMove(p_176213_1_, p_176213_2_, p_176213_3_);
        }
    }

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        return this.getDefaultState().func_177226_a(FACING, EnumFacing.func_190914_a(p_180642_2_, p_180642_8_)).func_177226_a(EXTENDED, Boolean.valueOf(false));
    }

    private void checkForMove(World worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing enumfacing = (EnumFacing)state.get(FACING);
        boolean flag = this.shouldBeExtended(worldIn, pos, enumfacing);

        if (flag && !((Boolean)state.get(EXTENDED)).booleanValue())
        {
            if ((new BlockPistonStructureHelper(worldIn, pos, enumfacing, true)).canMove())
            {
                worldIn.addBlockEvent(pos, this, 0, enumfacing.getIndex());
            }
        }
        else if (!flag && ((Boolean)state.get(EXTENDED)).booleanValue())
        {
            worldIn.addBlockEvent(pos, this, 1, enumfacing.getIndex());
        }
    }

    private boolean shouldBeExtended(World worldIn, BlockPos pos, EnumFacing facing)
    {
        for (EnumFacing enumfacing : EnumFacing.values())
        {
            if (enumfacing != facing && worldIn.isSidePowered(pos.offset(enumfacing), enumfacing))
            {
                return true;
            }
        }

        if (worldIn.isSidePowered(pos, EnumFacing.DOWN))
        {
            return true;
        }
        else
        {
            BlockPos blockpos = pos.up();

            for (EnumFacing enumfacing1 : EnumFacing.values())
            {
                if (enumfacing1 != EnumFacing.DOWN && worldIn.isSidePowered(blockpos.offset(enumfacing1), enumfacing1))
                {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * Called on server when World#addBlockEvent is called. If server returns true, then also called on the client. On
     * the Server, this may perform additional changes to the world, like pistons replacing the block with an extended
     * base. On the client, the update may involve replacing tile entities or effects such as sounds or particles
     * @deprecated call via {@link IBlockState#onBlockEventReceived(World,BlockPos,int,int)} whenever possible.
     * Implementing/overriding is fine.
     */
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        EnumFacing enumfacing = (EnumFacing)state.get(FACING);

        if (!worldIn.isRemote)
        {
            boolean flag = this.shouldBeExtended(worldIn, pos, enumfacing);

            if (flag && id == 1)
            {
                worldIn.setBlockState(pos, state.func_177226_a(EXTENDED, Boolean.valueOf(true)), 2);
                return false;
            }

            if (!flag && id == 0)
            {
                return false;
            }
        }

        if (id == 0)
        {
            if (!this.doMove(worldIn, pos, enumfacing, true))
            {
                return false;
            }

            worldIn.setBlockState(pos, state.func_177226_a(EXTENDED, Boolean.valueOf(true)), 3);
            worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, worldIn.rand.nextFloat() * 0.25F + 0.6F);
        }
        else if (id == 1)
        {
            TileEntity tileentity1 = worldIn.getTileEntity(pos.offset(enumfacing));

            if (tileentity1 instanceof TileEntityPiston)
            {
                ((TileEntityPiston)tileentity1).clearPistonTileEntity();
            }

            worldIn.setBlockState(pos, Blocks.field_180384_M.getDefaultState().func_177226_a(BlockPistonMoving.field_176426_a, enumfacing).func_177226_a(BlockPistonMoving.field_176425_b, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT), 3);
            worldIn.setTileEntity(pos, BlockPistonMoving.func_185588_a(this.func_176203_a(param), enumfacing, false, true));

            if (this.isSticky)
            {
                BlockPos blockpos = pos.add(enumfacing.getXOffset() * 2, enumfacing.getYOffset() * 2, enumfacing.getZOffset() * 2);
                IBlockState iblockstate = worldIn.getBlockState(blockpos);
                Block block = iblockstate.getBlock();
                boolean flag1 = false;

                if (block == Blocks.field_180384_M)
                {
                    TileEntity tileentity = worldIn.getTileEntity(blockpos);

                    if (tileentity instanceof TileEntityPiston)
                    {
                        TileEntityPiston tileentitypiston = (TileEntityPiston)tileentity;

                        if (tileentitypiston.func_174930_e() == enumfacing && tileentitypiston.isExtending())
                        {
                            tileentitypiston.clearPistonTileEntity();
                            flag1 = true;
                        }
                    }
                }

                if (!flag1 && iblockstate.getMaterial() != Material.AIR && canPush(iblockstate, worldIn, blockpos, enumfacing.getOpposite(), false, enumfacing) && (iblockstate.getPushReaction() == EnumPushReaction.NORMAL || block == Blocks.PISTON || block == Blocks.STICKY_PISTON))
                {
                    this.doMove(worldIn, pos, enumfacing, false);
                }
            }
            else
            {
                worldIn.func_175698_g(pos.offset(enumfacing));
            }

            worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, worldIn.rand.nextFloat() * 0.15F + 0.6F);
        }

        return true;
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    @Nullable
    public static EnumFacing func_176317_b(int p_176317_0_)
    {
        int i = p_176317_0_ & 7;
        return i > 5 ? null : EnumFacing.byIndex(i);
    }

    /**
     * Checks if the piston can push the given BlockState.
     */
    public static boolean canPush(IBlockState blockStateIn, World worldIn, BlockPos pos, EnumFacing facing, boolean destroyBlocks, EnumFacing directionIn)
    {
        Block block = blockStateIn.getBlock();

        if (block == Blocks.OBSIDIAN)
        {
            return false;
        }
        else if (!worldIn.getWorldBorder().contains(pos))
        {
            return false;
        }
        else if (pos.getY() >= 0 && (facing != EnumFacing.DOWN || pos.getY() != 0))
        {
            if (pos.getY() <= worldIn.func_72800_K() - 1 && (facing != EnumFacing.UP || pos.getY() != worldIn.func_72800_K() - 1))
            {
                if (block != Blocks.PISTON && block != Blocks.STICKY_PISTON)
                {
                    if (blockStateIn.getBlockHardness(worldIn, pos) == -1.0F)
                    {
                        return false;
                    }

                    switch (blockStateIn.getPushReaction())
                    {
                        case BLOCK:
                            return false;

                        case DESTROY:
                            return destroyBlocks;

                        case PUSH_ONLY:
                            return facing == directionIn;
                    }
                }
                else if (((Boolean)blockStateIn.get(EXTENDED)).booleanValue())
                {
                    return false;
                }

                return !block.hasTileEntity();
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    private boolean doMove(World worldIn, BlockPos pos, EnumFacing directionIn, boolean extending)
    {
        if (!extending)
        {
            worldIn.func_175698_g(pos.offset(directionIn));
        }

        BlockPistonStructureHelper blockpistonstructurehelper = new BlockPistonStructureHelper(worldIn, pos, directionIn, extending);

        if (!blockpistonstructurehelper.canMove())
        {
            return false;
        }
        else
        {
            List<BlockPos> list = blockpistonstructurehelper.getBlocksToMove();
            List<IBlockState> list1 = Lists.<IBlockState>newArrayList();

            for (int i = 0; i < list.size(); ++i)
            {
                BlockPos blockpos = list.get(i);
                list1.add(worldIn.getBlockState(blockpos).func_185899_b(worldIn, blockpos));
            }

            List<BlockPos> list2 = blockpistonstructurehelper.getBlocksToDestroy();
            int k = list.size() + list2.size();
            IBlockState[] aiblockstate = new IBlockState[k];
            EnumFacing enumfacing = extending ? directionIn : directionIn.getOpposite();

            for (int j = list2.size() - 1; j >= 0; --j)
            {
                BlockPos blockpos1 = list2.get(j);
                IBlockState iblockstate = worldIn.getBlockState(blockpos1);
                iblockstate.getBlock().func_176226_b(worldIn, blockpos1, iblockstate, 0);
                worldIn.setBlockState(blockpos1, Blocks.AIR.getDefaultState(), 4);
                --k;
                aiblockstate[k] = iblockstate;
            }

            for (int l = list.size() - 1; l >= 0; --l)
            {
                BlockPos blockpos3 = list.get(l);
                IBlockState iblockstate2 = worldIn.getBlockState(blockpos3);
                worldIn.setBlockState(blockpos3, Blocks.AIR.getDefaultState(), 2);
                blockpos3 = blockpos3.offset(enumfacing);
                worldIn.setBlockState(blockpos3, Blocks.field_180384_M.getDefaultState().func_177226_a(FACING, directionIn), 4);
                worldIn.setTileEntity(blockpos3, BlockPistonMoving.func_185588_a(list1.get(l), directionIn, extending, false));
                --k;
                aiblockstate[k] = iblockstate2;
            }

            BlockPos blockpos2 = pos.offset(directionIn);

            if (extending)
            {
                BlockPistonExtension.EnumPistonType blockpistonextension$enumpistontype = this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
                IBlockState iblockstate3 = Blocks.PISTON_HEAD.getDefaultState().func_177226_a(BlockPistonExtension.FACING, directionIn).func_177226_a(BlockPistonExtension.TYPE, blockpistonextension$enumpistontype);
                IBlockState iblockstate1 = Blocks.field_180384_M.getDefaultState().func_177226_a(BlockPistonMoving.field_176426_a, directionIn).func_177226_a(BlockPistonMoving.field_176425_b, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
                worldIn.setBlockState(blockpos2, iblockstate1, 4);
                worldIn.setTileEntity(blockpos2, BlockPistonMoving.func_185588_a(iblockstate3, directionIn, true, true));
            }

            for (int i1 = list2.size() - 1; i1 >= 0; --i1)
            {
                worldIn.func_175685_c(list2.get(i1), aiblockstate[k++].getBlock(), false);
            }

            for (int j1 = list.size() - 1; j1 >= 0; --j1)
            {
                worldIn.func_175685_c(list.get(j1), aiblockstate[k++].getBlock(), false);
            }

            if (extending)
            {
                worldIn.func_175685_c(blockpos2, Blocks.PISTON_HEAD, false);
            }

            return true;
        }
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(FACING, func_176317_b(p_176203_1_)).func_177226_a(EXTENDED, Boolean.valueOf((p_176203_1_ & 8) > 0));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;
        i = i | ((EnumFacing)p_176201_1_.get(FACING)).getIndex();

        if (((Boolean)p_176201_1_.get(EXTENDED)).booleanValue())
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
        return new BlockStateContainer(this, new IProperty[] {FACING, EXTENDED});
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        p_193383_2_ = this.func_176221_a(p_193383_2_, p_193383_1_, p_193383_3_);
        return p_193383_2_.get(FACING) != p_193383_4_.getOpposite() && ((Boolean)p_193383_2_.get(EXTENDED)).booleanValue() ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
    }
}
