package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDoor extends Block
{
    public static final PropertyDirection FACING = BlockHorizontal.HORIZONTAL_FACING;
    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyEnum<BlockDoor.EnumHingePosition> HINGE = PropertyEnum.<BlockDoor.EnumHingePosition>create("hinge", BlockDoor.EnumHingePosition.class);
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyEnum<BlockDoor.EnumDoorHalf> HALF = PropertyEnum.<BlockDoor.EnumDoorHalf>create("half", BlockDoor.EnumDoorHalf.class);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);

    protected BlockDoor(Material p_i45402_1_)
    {
        super(p_i45402_1_);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(FACING, EnumFacing.NORTH).func_177226_a(OPEN, Boolean.valueOf(false)).func_177226_a(HINGE, BlockDoor.EnumHingePosition.LEFT).func_177226_a(POWERED, Boolean.valueOf(false)).func_177226_a(HALF, BlockDoor.EnumDoorHalf.LOWER));
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        p_185496_1_ = p_185496_1_.func_185899_b(p_185496_2_, p_185496_3_);
        EnumFacing enumfacing = (EnumFacing)p_185496_1_.get(FACING);
        boolean flag = !((Boolean)p_185496_1_.get(OPEN)).booleanValue();
        boolean flag1 = p_185496_1_.get(HINGE) == BlockDoor.EnumHingePosition.RIGHT;

        switch (enumfacing)
        {
            case EAST:
            default:
                return flag ? EAST_AABB : (flag1 ? NORTH_AABB : SOUTH_AABB);

            case SOUTH:
                return flag ? SOUTH_AABB : (flag1 ? EAST_AABB : WEST_AABB);

            case WEST:
                return flag ? WEST_AABB : (flag1 ? SOUTH_AABB : NORTH_AABB);

            case NORTH:
                return flag ? NORTH_AABB : (flag1 ? WEST_AABB : EAST_AABB);
        }
    }

    public String func_149732_F()
    {
        return I18n.func_74838_a((this.getTranslationKey() + ".name").replaceAll("tile", "item"));
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    public boolean func_176205_b(IBlockAccess p_176205_1_, BlockPos p_176205_2_)
    {
        return func_176516_g(func_176515_e(p_176205_1_, p_176205_2_));
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    private int getCloseSound()
    {
        return this.material == Material.IRON ? 1011 : 1012;
    }

    private int getOpenSound()
    {
        return this.material == Material.IRON ? 1005 : 1006;
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     * @deprecated call via {@link IBlockState#getMapColor(IBlockAccess,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public MapColor getMaterialColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        if (state.getBlock() == Blocks.IRON_DOOR)
        {
            return MapColor.IRON;
        }
        else if (state.getBlock() == Blocks.OAK_DOOR)
        {
            return BlockPlanks.EnumType.OAK.func_181070_c();
        }
        else if (state.getBlock() == Blocks.SPRUCE_DOOR)
        {
            return BlockPlanks.EnumType.SPRUCE.func_181070_c();
        }
        else if (state.getBlock() == Blocks.BIRCH_DOOR)
        {
            return BlockPlanks.EnumType.BIRCH.func_181070_c();
        }
        else if (state.getBlock() == Blocks.JUNGLE_DOOR)
        {
            return BlockPlanks.EnumType.JUNGLE.func_181070_c();
        }
        else if (state.getBlock() == Blocks.ACACIA_DOOR)
        {
            return BlockPlanks.EnumType.ACACIA.func_181070_c();
        }
        else
        {
            return state.getBlock() == Blocks.DARK_OAK_DOOR ? BlockPlanks.EnumType.DARK_OAK.func_181070_c() : super.getMaterialColor(state, worldIn, pos);
        }
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        if (this.material == Material.IRON)
        {
            return false;
        }
        else
        {
            BlockPos blockpos = p_180639_3_.get(HALF) == BlockDoor.EnumDoorHalf.LOWER ? p_180639_2_ : p_180639_2_.down();
            IBlockState iblockstate = p_180639_2_.equals(blockpos) ? p_180639_3_ : p_180639_1_.getBlockState(blockpos);

            if (iblockstate.getBlock() != this)
            {
                return false;
            }
            else
            {
                p_180639_3_ = iblockstate.cycle(OPEN);
                p_180639_1_.setBlockState(blockpos, p_180639_3_, 10);
                p_180639_1_.func_175704_b(blockpos, p_180639_2_);
                p_180639_1_.func_180498_a(p_180639_4_, ((Boolean)p_180639_3_.get(OPEN)).booleanValue() ? this.getOpenSound() : this.getCloseSound(), p_180639_2_, 0);
                return true;
            }
        }
    }

    public void toggleDoor(World worldIn, BlockPos pos, boolean open)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getBlock() == this)
        {
            BlockPos blockpos = iblockstate.get(HALF) == BlockDoor.EnumDoorHalf.LOWER ? pos : pos.down();
            IBlockState iblockstate1 = pos == blockpos ? iblockstate : worldIn.getBlockState(blockpos);

            if (iblockstate1.getBlock() == this && ((Boolean)iblockstate1.get(OPEN)).booleanValue() != open)
            {
                worldIn.setBlockState(blockpos, iblockstate1.func_177226_a(OPEN, Boolean.valueOf(open)), 10);
                worldIn.func_175704_b(blockpos, pos);
                worldIn.func_180498_a((EntityPlayer)null, open ? this.getOpenSound() : this.getCloseSound(), pos, 0);
            }
        }
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        if (p_189540_1_.get(HALF) == BlockDoor.EnumDoorHalf.UPPER)
        {
            BlockPos blockpos = p_189540_3_.down();
            IBlockState iblockstate = p_189540_2_.getBlockState(blockpos);

            if (iblockstate.getBlock() != this)
            {
                p_189540_2_.func_175698_g(p_189540_3_);
            }
            else if (p_189540_4_ != this)
            {
                iblockstate.func_189546_a(p_189540_2_, blockpos, p_189540_4_, p_189540_5_);
            }
        }
        else
        {
            boolean flag1 = false;
            BlockPos blockpos1 = p_189540_3_.up();
            IBlockState iblockstate1 = p_189540_2_.getBlockState(blockpos1);

            if (iblockstate1.getBlock() != this)
            {
                p_189540_2_.func_175698_g(p_189540_3_);
                flag1 = true;
            }

            if (!p_189540_2_.getBlockState(p_189540_3_.down()).func_185896_q())
            {
                p_189540_2_.func_175698_g(p_189540_3_);
                flag1 = true;

                if (iblockstate1.getBlock() == this)
                {
                    p_189540_2_.func_175698_g(blockpos1);
                }
            }

            if (flag1)
            {
                if (!p_189540_2_.isRemote)
                {
                    this.func_176226_b(p_189540_2_, p_189540_3_, p_189540_1_, 0);
                }
            }
            else
            {
                boolean flag = p_189540_2_.isBlockPowered(p_189540_3_) || p_189540_2_.isBlockPowered(blockpos1);

                if (p_189540_4_ != this && (flag || p_189540_4_.getDefaultState().canProvidePower()) && flag != ((Boolean)iblockstate1.get(POWERED)).booleanValue())
                {
                    p_189540_2_.setBlockState(blockpos1, iblockstate1.func_177226_a(POWERED, Boolean.valueOf(flag)), 2);

                    if (flag != ((Boolean)p_189540_1_.get(OPEN)).booleanValue())
                    {
                        p_189540_2_.setBlockState(p_189540_3_, p_189540_1_.func_177226_a(OPEN, Boolean.valueOf(flag)), 2);
                        p_189540_2_.func_175704_b(p_189540_3_, p_189540_3_);
                        p_189540_2_.func_180498_a((EntityPlayer)null, flag ? this.getOpenSound() : this.getCloseSound(), p_189540_3_, 0);
                    }
                }
            }
        }
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return p_180660_1_.get(HALF) == BlockDoor.EnumDoorHalf.UPPER ? Items.AIR : this.func_176509_j();
    }

    public boolean func_176196_c(World p_176196_1_, BlockPos p_176196_2_)
    {
        if (p_176196_2_.getY() >= 255)
        {
            return false;
        }
        else
        {
            return p_176196_1_.getBlockState(p_176196_2_.down()).func_185896_q() && super.func_176196_c(p_176196_1_, p_176196_2_) && super.func_176196_c(p_176196_1_, p_176196_2_.up());
        }
    }

    /**
     * @deprecated call via {@link IBlockState#getMobilityFlag()} whenever possible. Implementing/overriding is fine.
     */
    public EnumPushReaction getPushReaction(IBlockState state)
    {
        return EnumPushReaction.DESTROY;
    }

    public static int func_176515_e(IBlockAccess p_176515_0_, BlockPos p_176515_1_)
    {
        IBlockState iblockstate = p_176515_0_.getBlockState(p_176515_1_);
        int i = iblockstate.getBlock().func_176201_c(iblockstate);
        boolean flag = func_176518_i(i);
        IBlockState iblockstate1 = p_176515_0_.getBlockState(p_176515_1_.down());
        int j = iblockstate1.getBlock().func_176201_c(iblockstate1);
        int k = flag ? j : i;
        IBlockState iblockstate2 = p_176515_0_.getBlockState(p_176515_1_.up());
        int l = iblockstate2.getBlock().func_176201_c(iblockstate2);
        int i1 = flag ? i : l;
        boolean flag1 = (i1 & 1) != 0;
        boolean flag2 = (i1 & 2) != 0;
        return func_176510_b(k) | (flag ? 8 : 0) | (flag1 ? 16 : 0) | (flag2 ? 32 : 0);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this.func_176509_j());
    }

    private Item func_176509_j()
    {
        if (this == Blocks.IRON_DOOR)
        {
            return Items.field_151139_aw;
        }
        else if (this == Blocks.SPRUCE_DOOR)
        {
            return Items.field_179569_ar;
        }
        else if (this == Blocks.BIRCH_DOOR)
        {
            return Items.field_179568_as;
        }
        else if (this == Blocks.JUNGLE_DOOR)
        {
            return Items.field_179567_at;
        }
        else if (this == Blocks.ACACIA_DOOR)
        {
            return Items.field_179572_au;
        }
        else
        {
            return this == Blocks.DARK_OAK_DOOR ? Items.field_179571_av : Items.field_179570_aq;
        }
    }

    /**
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually
     * collect this block
     */
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        BlockPos blockpos = pos.down();
        BlockPos blockpos1 = pos.up();

        if (player.abilities.isCreativeMode && state.get(HALF) == BlockDoor.EnumDoorHalf.UPPER && worldIn.getBlockState(blockpos).getBlock() == this)
        {
            worldIn.func_175698_g(blockpos);
        }

        if (state.get(HALF) == BlockDoor.EnumDoorHalf.LOWER && worldIn.getBlockState(blockpos1).getBlock() == this)
        {
            if (player.abilities.isCreativeMode)
            {
                worldIn.func_175698_g(pos);
            }

            worldIn.func_175698_g(blockpos1);
        }
    }

    public BlockRenderLayer func_180664_k()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public IBlockState func_176221_a(IBlockState p_176221_1_, IBlockAccess p_176221_2_, BlockPos p_176221_3_)
    {
        if (p_176221_1_.get(HALF) == BlockDoor.EnumDoorHalf.LOWER)
        {
            IBlockState iblockstate = p_176221_2_.getBlockState(p_176221_3_.up());

            if (iblockstate.getBlock() == this)
            {
                p_176221_1_ = p_176221_1_.func_177226_a(HINGE, iblockstate.get(HINGE)).func_177226_a(POWERED, iblockstate.get(POWERED));
            }
        }
        else
        {
            IBlockState iblockstate1 = p_176221_2_.getBlockState(p_176221_3_.down());

            if (iblockstate1.getBlock() == this)
            {
                p_176221_1_ = p_176221_1_.func_177226_a(FACING, iblockstate1.get(FACING)).func_177226_a(OPEN, iblockstate1.get(OPEN));
            }
        }

        return p_176221_1_;
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
     * fine.
     */
    public IBlockState rotate(IBlockState state, Rotation rot)
    {
        return state.get(HALF) != BlockDoor.EnumDoorHalf.LOWER ? state : state.func_177226_a(FACING, rot.rotate((EnumFacing)state.get(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
        return mirrorIn == Mirror.NONE ? state : state.rotate(mirrorIn.toRotation((EnumFacing)state.get(FACING))).cycle(HINGE);
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return (p_176203_1_ & 8) > 0 ? this.getDefaultState().func_177226_a(HALF, BlockDoor.EnumDoorHalf.UPPER).func_177226_a(HINGE, (p_176203_1_ & 1) > 0 ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT).func_177226_a(POWERED, Boolean.valueOf((p_176203_1_ & 2) > 0)) : this.getDefaultState().func_177226_a(HALF, BlockDoor.EnumDoorHalf.LOWER).func_177226_a(FACING, EnumFacing.byHorizontalIndex(p_176203_1_ & 3).rotateYCCW()).func_177226_a(OPEN, Boolean.valueOf((p_176203_1_ & 4) > 0));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;

        if (p_176201_1_.get(HALF) == BlockDoor.EnumDoorHalf.UPPER)
        {
            i = i | 8;

            if (p_176201_1_.get(HINGE) == BlockDoor.EnumHingePosition.RIGHT)
            {
                i |= 1;
            }

            if (((Boolean)p_176201_1_.get(POWERED)).booleanValue())
            {
                i |= 2;
            }
        }
        else
        {
            i = i | ((EnumFacing)p_176201_1_.get(FACING)).rotateY().getHorizontalIndex();

            if (((Boolean)p_176201_1_.get(OPEN)).booleanValue())
            {
                i |= 4;
            }
        }

        return i;
    }

    protected static int func_176510_b(int p_176510_0_)
    {
        return p_176510_0_ & 7;
    }

    public static boolean func_176514_f(IBlockAccess p_176514_0_, BlockPos p_176514_1_)
    {
        return func_176516_g(func_176515_e(p_176514_0_, p_176514_1_));
    }

    public static EnumFacing func_176517_h(IBlockAccess p_176517_0_, BlockPos p_176517_1_)
    {
        return func_176511_f(func_176515_e(p_176517_0_, p_176517_1_));
    }

    public static EnumFacing func_176511_f(int p_176511_0_)
    {
        return EnumFacing.byHorizontalIndex(p_176511_0_ & 3).rotateYCCW();
    }

    protected static boolean func_176516_g(int p_176516_0_)
    {
        return (p_176516_0_ & 4) != 0;
    }

    protected static boolean func_176518_i(int p_176518_0_)
    {
        return (p_176518_0_ & 8) != 0;
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {HALF, FACING, OPEN, HINGE, POWERED});
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }

    public static enum EnumDoorHalf implements IStringSerializable
    {
        UPPER,
        LOWER;

        public String toString()
        {
            return this.getName();
        }

        public String getName()
        {
            return this == UPPER ? "upper" : "lower";
        }
    }

    public static enum EnumHingePosition implements IStringSerializable
    {
        LEFT,
        RIGHT;

        public String toString()
        {
            return this.getName();
        }

        public String getName()
        {
            return this == LEFT ? "left" : "right";
        }
    }
}
