package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTrapDoor extends Block
{
    public static final PropertyDirection field_176284_a = BlockHorizontal.HORIZONTAL_FACING;
    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyEnum<BlockTrapDoor.DoorHalf> HALF = PropertyEnum.<BlockTrapDoor.DoorHalf>create("half", BlockTrapDoor.DoorHalf.class);
    protected static final AxisAlignedBB EAST_OPEN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_OPEN_AABB = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB SOUTH_OPEN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB NORTH_OPEN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB BOTTOM_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);
    protected static final AxisAlignedBB TOP_AABB = new AxisAlignedBB(0.0D, 0.8125D, 0.0D, 1.0D, 1.0D, 1.0D);

    protected BlockTrapDoor(Material p_i45434_1_)
    {
        super(p_i45434_1_);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176284_a, EnumFacing.NORTH).func_177226_a(OPEN, Boolean.valueOf(false)).func_177226_a(HALF, BlockTrapDoor.DoorHalf.BOTTOM));
        this.func_149647_a(CreativeTabs.REDSTONE);
    }

    public AxisAlignedBB func_185496_a(IBlockState p_185496_1_, IBlockAccess p_185496_2_, BlockPos p_185496_3_)
    {
        AxisAlignedBB axisalignedbb;

        if (((Boolean)p_185496_1_.get(OPEN)).booleanValue())
        {
            switch ((EnumFacing)p_185496_1_.get(field_176284_a))
            {
                case NORTH:
                default:
                    axisalignedbb = NORTH_OPEN_AABB;
                    break;

                case SOUTH:
                    axisalignedbb = SOUTH_OPEN_AABB;
                    break;

                case WEST:
                    axisalignedbb = WEST_OPEN_AABB;
                    break;

                case EAST:
                    axisalignedbb = EAST_OPEN_AABB;
            }
        }
        else if (p_185496_1_.get(HALF) == BlockTrapDoor.DoorHalf.TOP)
        {
            axisalignedbb = TOP_AABB;
        }
        else
        {
            axisalignedbb = BOTTOM_AABB;
        }

        return axisalignedbb;
    }

    public boolean func_149662_c(IBlockState p_149662_1_)
    {
        return false;
    }

    public boolean func_149686_d(IBlockState p_149686_1_)
    {
        return false;
    }

    public boolean func_176205_b(IBlockAccess p_176205_1_, BlockPos p_176205_2_)
    {
        return !((Boolean)p_176205_1_.getBlockState(p_176205_2_).get(OPEN)).booleanValue();
    }

    public boolean func_180639_a(World p_180639_1_, BlockPos p_180639_2_, IBlockState p_180639_3_, EntityPlayer p_180639_4_, EnumHand p_180639_5_, EnumFacing p_180639_6_, float p_180639_7_, float p_180639_8_, float p_180639_9_)
    {
        if (this.material == Material.IRON)
        {
            return false;
        }
        else
        {
            p_180639_3_ = p_180639_3_.cycle(OPEN);
            p_180639_1_.setBlockState(p_180639_2_, p_180639_3_, 2);
            this.playSound(p_180639_4_, p_180639_1_, p_180639_2_, ((Boolean)p_180639_3_.get(OPEN)).booleanValue());
            return true;
        }
    }

    protected void playSound(@Nullable EntityPlayer player, World worldIn, BlockPos pos, boolean p_185731_4_)
    {
        if (p_185731_4_)
        {
            int i = this.material == Material.IRON ? 1037 : 1007;
            worldIn.func_180498_a(player, i, pos, 0);
        }
        else
        {
            int j = this.material == Material.IRON ? 1036 : 1013;
            worldIn.func_180498_a(player, j, pos, 0);
        }
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        if (!p_189540_2_.isRemote)
        {
            boolean flag = p_189540_2_.isBlockPowered(p_189540_3_);

            if (flag || p_189540_4_.getDefaultState().canProvidePower())
            {
                boolean flag1 = ((Boolean)p_189540_1_.get(OPEN)).booleanValue();

                if (flag1 != flag)
                {
                    p_189540_2_.setBlockState(p_189540_3_, p_189540_1_.func_177226_a(OPEN, Boolean.valueOf(flag)), 2);
                    this.playSound((EntityPlayer)null, p_189540_2_, p_189540_3_, flag);
                }
            }
        }
    }

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        IBlockState iblockstate = this.getDefaultState();

        if (p_180642_3_.getAxis().isHorizontal())
        {
            iblockstate = iblockstate.func_177226_a(field_176284_a, p_180642_3_).func_177226_a(OPEN, Boolean.valueOf(false));
            iblockstate = iblockstate.func_177226_a(HALF, p_180642_5_ > 0.5F ? BlockTrapDoor.DoorHalf.TOP : BlockTrapDoor.DoorHalf.BOTTOM);
        }
        else
        {
            iblockstate = iblockstate.func_177226_a(field_176284_a, p_180642_8_.getHorizontalFacing().getOpposite()).func_177226_a(OPEN, Boolean.valueOf(false));
            iblockstate = iblockstate.func_177226_a(HALF, p_180642_3_ == EnumFacing.UP ? BlockTrapDoor.DoorHalf.BOTTOM : BlockTrapDoor.DoorHalf.TOP);
        }

        if (p_180642_1_.isBlockPowered(p_180642_2_))
        {
            iblockstate = iblockstate.func_177226_a(OPEN, Boolean.valueOf(true));
        }

        return iblockstate;
    }

    public boolean func_176198_a(World p_176198_1_, BlockPos p_176198_2_, EnumFacing p_176198_3_)
    {
        return true;
    }

    protected static EnumFacing func_176281_b(int p_176281_0_)
    {
        switch (p_176281_0_ & 3)
        {
            case 0:
                return EnumFacing.NORTH;

            case 1:
                return EnumFacing.SOUTH;

            case 2:
                return EnumFacing.WEST;

            case 3:
            default:
                return EnumFacing.EAST;
        }
    }

    protected static int func_176282_a(EnumFacing p_176282_0_)
    {
        switch (p_176282_0_)
        {
            case NORTH:
                return 0;

            case SOUTH:
                return 1;

            case WEST:
                return 2;

            case EAST:
            default:
                return 3;
        }
    }

    public BlockRenderLayer func_180664_k()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(field_176284_a, func_176281_b(p_176203_1_)).func_177226_a(OPEN, Boolean.valueOf((p_176203_1_ & 4) != 0)).func_177226_a(HALF, (p_176203_1_ & 8) == 0 ? BlockTrapDoor.DoorHalf.BOTTOM : BlockTrapDoor.DoorHalf.TOP);
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;
        i = i | func_176282_a((EnumFacing)p_176201_1_.get(field_176284_a));

        if (((Boolean)p_176201_1_.get(OPEN)).booleanValue())
        {
            i |= 4;
        }

        if (p_176201_1_.get(HALF) == BlockTrapDoor.DoorHalf.TOP)
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
        return state.func_177226_a(field_176284_a, rot.rotate((EnumFacing)state.get(field_176284_a)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.toRotation((EnumFacing)state.get(field_176284_a)));
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176284_a, OPEN, HALF});
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return (p_193383_4_ == EnumFacing.UP && p_193383_2_.get(HALF) == BlockTrapDoor.DoorHalf.TOP || p_193383_4_ == EnumFacing.DOWN && p_193383_2_.get(HALF) == BlockTrapDoor.DoorHalf.BOTTOM) && !((Boolean)p_193383_2_.get(OPEN)).booleanValue() ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    public static enum DoorHalf implements IStringSerializable
    {
        TOP("top"),
        BOTTOM("bottom");

        private final String field_176671_c;

        private DoorHalf(String p_i45674_3_)
        {
            this.field_176671_c = p_i45674_3_;
        }

        public String toString()
        {
            return this.field_176671_c;
        }

        public String getName()
        {
            return this.field_176671_c;
        }
    }
}
