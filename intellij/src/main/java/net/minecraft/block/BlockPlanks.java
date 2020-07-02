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
import net.minecraft.world.IBlockAccess;

public class BlockPlanks extends Block
{
    public static final PropertyEnum<BlockPlanks.EnumType> field_176383_a = PropertyEnum.<BlockPlanks.EnumType>create("variant", BlockPlanks.EnumType.class);

    public BlockPlanks()
    {
        super(Material.WOOD);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176383_a, BlockPlanks.EnumType.OAK));
        this.func_149647_a(CreativeTabs.BUILDING_BLOCKS);
    }

    public int func_180651_a(IBlockState p_180651_1_)
    {
        return ((BlockPlanks.EnumType)p_180651_1_.get(field_176383_a)).func_176839_a();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        for (BlockPlanks.EnumType blockplanks$enumtype : BlockPlanks.EnumType.values())
        {
            items.add(new ItemStack(this, 1, blockplanks$enumtype.func_176839_a()));
        }
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(field_176383_a, BlockPlanks.EnumType.func_176837_a(p_176203_1_));
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     * @deprecated call via {@link IBlockState#getMapColor(IBlockAccess,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public MapColor getMaterialColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return ((BlockPlanks.EnumType)state.get(field_176383_a)).func_181070_c();
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((BlockPlanks.EnumType)p_176201_1_.get(field_176383_a)).func_176839_a();
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176383_a});
    }

    public static enum EnumType implements IStringSerializable
    {
        OAK(0, "oak", MapColor.WOOD),
        SPRUCE(1, "spruce", MapColor.OBSIDIAN),
        BIRCH(2, "birch", MapColor.SAND),
        JUNGLE(3, "jungle", MapColor.DIRT),
        ACACIA(4, "acacia", MapColor.ADOBE),
        DARK_OAK(5, "dark_oak", "big_oak", MapColor.BROWN);

        private static final BlockPlanks.EnumType[] field_176842_g = new BlockPlanks.EnumType[values().length];
        private final int field_176850_h;
        private final String field_176851_i;
        private final String field_176848_j;
        private final MapColor field_181071_k;

        private EnumType(int p_i46388_3_, String p_i46388_4_, MapColor p_i46388_5_)
        {
            this(p_i46388_3_, p_i46388_4_, p_i46388_4_, p_i46388_5_);
        }

        private EnumType(int p_i46389_3_, String p_i46389_4_, String p_i46389_5_, MapColor p_i46389_6_)
        {
            this.field_176850_h = p_i46389_3_;
            this.field_176851_i = p_i46389_4_;
            this.field_176848_j = p_i46389_5_;
            this.field_181071_k = p_i46389_6_;
        }

        public int func_176839_a()
        {
            return this.field_176850_h;
        }

        public MapColor func_181070_c()
        {
            return this.field_181071_k;
        }

        public String toString()
        {
            return this.field_176851_i;
        }

        public static BlockPlanks.EnumType func_176837_a(int p_176837_0_)
        {
            if (p_176837_0_ < 0 || p_176837_0_ >= field_176842_g.length)
            {
                p_176837_0_ = 0;
            }

            return field_176842_g[p_176837_0_];
        }

        public String getName()
        {
            return this.field_176851_i;
        }

        public String func_176840_c()
        {
            return this.field_176848_j;
        }

        static {
            for (BlockPlanks.EnumType blockplanks$enumtype : values())
            {
                field_176842_g[blockplanks$enumtype.func_176839_a()] = blockplanks$enumtype;
            }
        }
    }
}
