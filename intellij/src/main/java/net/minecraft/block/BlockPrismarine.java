package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;

public class BlockPrismarine extends Block
{
    public static final PropertyEnum<BlockPrismarine.EnumType> field_176332_a = PropertyEnum.<BlockPrismarine.EnumType>create("variant", BlockPrismarine.EnumType.class);
    public static final int field_176331_b = BlockPrismarine.EnumType.ROUGH.func_176807_a();
    public static final int field_176333_M = BlockPrismarine.EnumType.BRICKS.func_176807_a();
    public static final int field_176334_N = BlockPrismarine.EnumType.DARK.func_176807_a();

    public BlockPrismarine()
    {
        super(Material.ROCK);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176332_a, BlockPrismarine.EnumType.ROUGH));
        this.func_149647_a(CreativeTabs.BUILDING_BLOCKS);
    }

    public String func_149732_F()
    {
        return I18n.func_74838_a(this.getTranslationKey() + "." + BlockPrismarine.EnumType.ROUGH.func_176809_c() + ".name");
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     * @deprecated call via {@link IBlockState#getMapColor(IBlockAccess,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public MapColor getMaterialColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.get(field_176332_a) == BlockPrismarine.EnumType.ROUGH ? MapColor.CYAN : MapColor.DIAMOND;
    }

    public int func_180651_a(IBlockState p_180651_1_)
    {
        return ((BlockPrismarine.EnumType)p_180651_1_.get(field_176332_a)).func_176807_a();
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((BlockPrismarine.EnumType)p_176201_1_.get(field_176332_a)).func_176807_a();
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176332_a});
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(field_176332_a, BlockPrismarine.EnumType.func_176810_a(p_176203_1_));
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        items.add(new ItemStack(this, 1, field_176331_b));
        items.add(new ItemStack(this, 1, field_176333_M));
        items.add(new ItemStack(this, 1, field_176334_N));
    }

    public static enum EnumType implements IStringSerializable
    {
        ROUGH(0, "prismarine", "rough"),
        BRICKS(1, "prismarine_bricks", "bricks"),
        DARK(2, "dark_prismarine", "dark");

        private static final BlockPrismarine.EnumType[] field_176813_d = new BlockPrismarine.EnumType[values().length];
        private final int field_176814_e;
        private final String field_176811_f;
        private final String field_176812_g;

        private EnumType(int p_i45692_3_, String p_i45692_4_, String p_i45692_5_)
        {
            this.field_176814_e = p_i45692_3_;
            this.field_176811_f = p_i45692_4_;
            this.field_176812_g = p_i45692_5_;
        }

        public int func_176807_a()
        {
            return this.field_176814_e;
        }

        public String toString()
        {
            return this.field_176811_f;
        }

        public static BlockPrismarine.EnumType func_176810_a(int p_176810_0_)
        {
            if (p_176810_0_ < 0 || p_176810_0_ >= field_176813_d.length)
            {
                p_176810_0_ = 0;
            }

            return field_176813_d[p_176810_0_];
        }

        public String getName()
        {
            return this.field_176811_f;
        }

        public String func_176809_c()
        {
            return this.field_176812_g;
        }

        static {
            for (BlockPrismarine.EnumType blockprismarine$enumtype : values())
            {
                field_176813_d[blockprismarine$enumtype.func_176807_a()] = blockprismarine$enumtype;
            }
        }
    }
}
