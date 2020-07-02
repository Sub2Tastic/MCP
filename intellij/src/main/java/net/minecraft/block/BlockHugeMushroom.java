package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHugeMushroom extends Block
{
    public static final PropertyEnum<BlockHugeMushroom.EnumType> field_176380_a = PropertyEnum.<BlockHugeMushroom.EnumType>create("variant", BlockHugeMushroom.EnumType.class);
    private final Block field_176379_b;

    public BlockHugeMushroom(Material p_i46392_1_, MapColor p_i46392_2_, Block p_i46392_3_)
    {
        super(p_i46392_1_, p_i46392_2_);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.ALL_OUTSIDE));
        this.field_176379_b = p_i46392_3_;
    }

    public int func_149745_a(Random p_149745_1_)
    {
        return Math.max(0, p_149745_1_.nextInt(10) - 7);
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     * @deprecated call via {@link IBlockState#getMapColor(IBlockAccess,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public MapColor getMaterialColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        switch ((BlockHugeMushroom.EnumType)state.get(field_176380_a))
        {
            case ALL_STEM:
                return MapColor.WOOL;

            case ALL_INSIDE:
                return MapColor.SAND;

            case STEM:
                return MapColor.SAND;

            default:
                return super.getMaterialColor(state, worldIn, pos);
        }
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Item.getItemFromBlock(this.field_176379_b);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this.field_176379_b);
    }

    public IBlockState func_180642_a(World p_180642_1_, BlockPos p_180642_2_, EnumFacing p_180642_3_, float p_180642_4_, float p_180642_5_, float p_180642_6_, int p_180642_7_, EntityLivingBase p_180642_8_)
    {
        return this.getDefaultState();
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.func_176895_a(p_176203_1_));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((BlockHugeMushroom.EnumType)p_176201_1_.get(field_176380_a)).func_176896_a();
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
                switch ((BlockHugeMushroom.EnumType)state.get(field_176380_a))
                {
                    case STEM:
                        break;

                    case NORTH_WEST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.SOUTH_EAST);

                    case NORTH:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.SOUTH);

                    case NORTH_EAST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.SOUTH_WEST);

                    case WEST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.EAST);

                    case EAST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.WEST);

                    case SOUTH_WEST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.NORTH_EAST);

                    case SOUTH:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.NORTH);

                    case SOUTH_EAST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.NORTH_WEST);

                    default:
                        return state;
                }

            case COUNTERCLOCKWISE_90:
                switch ((BlockHugeMushroom.EnumType)state.get(field_176380_a))
                {
                    case STEM:
                        break;

                    case NORTH_WEST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.SOUTH_WEST);

                    case NORTH:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.WEST);

                    case NORTH_EAST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.NORTH_WEST);

                    case WEST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.SOUTH);

                    case EAST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.NORTH);

                    case SOUTH_WEST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.SOUTH_EAST);

                    case SOUTH:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.EAST);

                    case SOUTH_EAST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.NORTH_EAST);

                    default:
                        return state;
                }

            case CLOCKWISE_90:
                switch ((BlockHugeMushroom.EnumType)state.get(field_176380_a))
                {
                    case STEM:
                        break;

                    case NORTH_WEST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.NORTH_EAST);

                    case NORTH:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.EAST);

                    case NORTH_EAST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.SOUTH_EAST);

                    case WEST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.NORTH);

                    case EAST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.SOUTH);

                    case SOUTH_WEST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.NORTH_WEST);

                    case SOUTH:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.WEST);

                    case SOUTH_EAST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.SOUTH_WEST);

                    default:
                        return state;
                }

            default:
                return state;
        }
    }

    @SuppressWarnings("incomplete-switch")

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
        BlockHugeMushroom.EnumType blockhugemushroom$enumtype = (BlockHugeMushroom.EnumType)state.get(field_176380_a);

        switch (mirrorIn)
        {
            case LEFT_RIGHT:
                switch (blockhugemushroom$enumtype)
                {
                    case NORTH_WEST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.SOUTH_WEST);

                    case NORTH:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.SOUTH);

                    case NORTH_EAST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.SOUTH_EAST);

                    case WEST:
                    case EAST:
                    default:
                        return super.mirror(state, mirrorIn);

                    case SOUTH_WEST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.NORTH_WEST);

                    case SOUTH:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.NORTH);

                    case SOUTH_EAST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.NORTH_EAST);
                }

            case FRONT_BACK:
                switch (blockhugemushroom$enumtype)
                {
                    case NORTH_WEST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.NORTH_EAST);

                    case NORTH:
                    case SOUTH:
                    default:
                        break;

                    case NORTH_EAST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.NORTH_WEST);

                    case WEST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.EAST);

                    case EAST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.WEST);

                    case SOUTH_WEST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.SOUTH_EAST);

                    case SOUTH_EAST:
                        return state.func_177226_a(field_176380_a, BlockHugeMushroom.EnumType.SOUTH_WEST);
                }
        }

        return super.mirror(state, mirrorIn);
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176380_a});
    }

    public static enum EnumType implements IStringSerializable
    {
        NORTH_WEST(1, "north_west"),
        NORTH(2, "north"),
        NORTH_EAST(3, "north_east"),
        WEST(4, "west"),
        CENTER(5, "center"),
        EAST(6, "east"),
        SOUTH_WEST(7, "south_west"),
        SOUTH(8, "south"),
        SOUTH_EAST(9, "south_east"),
        STEM(10, "stem"),
        ALL_INSIDE(0, "all_inside"),
        ALL_OUTSIDE(14, "all_outside"),
        ALL_STEM(15, "all_stem");

        private static final BlockHugeMushroom.EnumType[] field_176905_n = new BlockHugeMushroom.EnumType[16];
        private final int field_176906_o;
        private final String field_176914_p;

        private EnumType(int p_i45710_3_, String p_i45710_4_)
        {
            this.field_176906_o = p_i45710_3_;
            this.field_176914_p = p_i45710_4_;
        }

        public int func_176896_a()
        {
            return this.field_176906_o;
        }

        public String toString()
        {
            return this.field_176914_p;
        }

        public static BlockHugeMushroom.EnumType func_176895_a(int p_176895_0_)
        {
            if (p_176895_0_ < 0 || p_176895_0_ >= field_176905_n.length)
            {
                p_176895_0_ = 0;
            }

            BlockHugeMushroom.EnumType blockhugemushroom$enumtype = field_176905_n[p_176895_0_];
            return blockhugemushroom$enumtype == null ? field_176905_n[0] : blockhugemushroom$enumtype;
        }

        public String getName()
        {
            return this.field_176914_p;
        }

        static {
            for (BlockHugeMushroom.EnumType blockhugemushroom$enumtype : values())
            {
                field_176905_n[blockhugemushroom$enumtype.func_176896_a()] = blockhugemushroom$enumtype;
            }
        }
    }
}
