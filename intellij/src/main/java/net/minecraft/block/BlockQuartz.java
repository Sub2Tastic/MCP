package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockQuartz extends Block
{
    public static final PropertyEnum<BlockQuartz.EnumType> field_176335_a = PropertyEnum.<BlockQuartz.EnumType>create("variant", BlockQuartz.EnumType.class);

    public BlockQuartz()
    {
        super(Material.ROCK);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176335_a, BlockQuartz.EnumType.DEFAULT));
        this.func_149647_a(CreativeTabs.BUILDING_BLOCKS);
    }

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        if (p_180642_7_ == BlockQuartz.EnumType.LINES_Y.func_176796_a())
        {
            switch (p_180642_3_.getAxis())
            {
                case Z:
                    return this.getDefaultState().func_177226_a(field_176335_a, BlockQuartz.EnumType.LINES_Z);

                case X:
                    return this.getDefaultState().func_177226_a(field_176335_a, BlockQuartz.EnumType.LINES_X);

                case Y:
                    return this.getDefaultState().func_177226_a(field_176335_a, BlockQuartz.EnumType.LINES_Y);
            }
        }

        return p_180642_7_ == BlockQuartz.EnumType.CHISELED.func_176796_a() ? this.getDefaultState().func_177226_a(field_176335_a, BlockQuartz.EnumType.CHISELED) : this.getDefaultState().func_177226_a(field_176335_a, BlockQuartz.EnumType.DEFAULT);
    }

    public int func_180651_a(IBlockState p_180651_1_)
    {
        BlockQuartz.EnumType blockquartz$enumtype = (BlockQuartz.EnumType)p_180651_1_.get(field_176335_a);
        return blockquartz$enumtype != BlockQuartz.EnumType.LINES_X && blockquartz$enumtype != BlockQuartz.EnumType.LINES_Z ? blockquartz$enumtype.func_176796_a() : BlockQuartz.EnumType.LINES_Y.func_176796_a();
    }

    protected ItemStack func_180643_i(IBlockState p_180643_1_)
    {
        BlockQuartz.EnumType blockquartz$enumtype = (BlockQuartz.EnumType)p_180643_1_.get(field_176335_a);
        return blockquartz$enumtype != BlockQuartz.EnumType.LINES_X && blockquartz$enumtype != BlockQuartz.EnumType.LINES_Z ? super.func_180643_i(p_180643_1_) : new ItemStack(Item.getItemFromBlock(this), 1, BlockQuartz.EnumType.LINES_Y.func_176796_a());
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        items.add(new ItemStack(this, 1, BlockQuartz.EnumType.DEFAULT.func_176796_a()));
        items.add(new ItemStack(this, 1, BlockQuartz.EnumType.CHISELED.func_176796_a()));
        items.add(new ItemStack(this, 1, BlockQuartz.EnumType.LINES_Y.func_176796_a()));
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     * @deprecated call via {@link IBlockState#getMapColor(IBlockAccess,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public MapColor getMaterialColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return MapColor.QUARTZ;
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(field_176335_a, BlockQuartz.EnumType.func_176794_a(p_176203_1_));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((BlockQuartz.EnumType)p_176201_1_.get(field_176335_a)).func_176796_a();
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
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch ((BlockQuartz.EnumType)state.get(field_176335_a))
                {
                    case LINES_X:
                        return state.func_177226_a(field_176335_a, BlockQuartz.EnumType.LINES_Z);

                    case LINES_Z:
                        return state.func_177226_a(field_176335_a, BlockQuartz.EnumType.LINES_X);

                    default:
                        return state;
                }

            default:
                return state;
        }
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176335_a});
    }

    public static enum EnumType implements IStringSerializable
    {
        DEFAULT(0, "default", "default"),
        CHISELED(1, "chiseled", "chiseled"),
        LINES_Y(2, "lines_y", "lines"),
        LINES_X(3, "lines_x", "lines"),
        LINES_Z(4, "lines_z", "lines");

        private static final BlockQuartz.EnumType[] field_176797_f = new BlockQuartz.EnumType[values().length];
        private final int field_176798_g;
        private final String field_176805_h;
        private final String field_176806_i;

        private EnumType(int p_i45691_3_, String p_i45691_4_, String p_i45691_5_)
        {
            this.field_176798_g = p_i45691_3_;
            this.field_176805_h = p_i45691_4_;
            this.field_176806_i = p_i45691_5_;
        }

        public int func_176796_a()
        {
            return this.field_176798_g;
        }

        public String toString()
        {
            return this.field_176806_i;
        }

        public static BlockQuartz.EnumType func_176794_a(int p_176794_0_)
        {
            if (p_176794_0_ < 0 || p_176794_0_ >= field_176797_f.length)
            {
                p_176794_0_ = 0;
            }

            return field_176797_f[p_176794_0_];
        }

        public String getName()
        {
            return this.field_176805_h;
        }

        static {
            for (BlockQuartz.EnumType blockquartz$enumtype : values())
            {
                field_176797_f[blockquartz$enumtype.func_176796_a()] = blockquartz$enumtype;
            }
        }
    }
}
