package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockStoneSlab extends BlockSlab
{
    public static final PropertyBool field_176555_b = PropertyBool.create("seamless");
    public static final PropertyEnum<BlockStoneSlab.EnumType> field_176556_M = PropertyEnum.<BlockStoneSlab.EnumType>create("variant", BlockStoneSlab.EnumType.class);

    public BlockStoneSlab()
    {
        super(Material.ROCK);
        IBlockState iblockstate = this.stateContainer.getBaseState();

        if (this.func_176552_j())
        {
            iblockstate = iblockstate.func_177226_a(field_176555_b, Boolean.valueOf(false));
        }
        else
        {
            iblockstate = iblockstate.func_177226_a(field_176554_a, BlockSlab.EnumBlockHalf.BOTTOM);
        }

        this.setDefaultState(iblockstate.func_177226_a(field_176556_M, BlockStoneSlab.EnumType.STONE));
        this.func_149647_a(CreativeTabs.BUILDING_BLOCKS);
    }

    public Item func_180660_a(IBlockState p_180660_1_, Random p_180660_2_, int p_180660_3_)
    {
        return Item.getItemFromBlock(Blocks.STONE_SLAB);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Blocks.STONE_SLAB, 1, ((BlockStoneSlab.EnumType)state.get(field_176556_M)).func_176624_a());
    }

    public String func_150002_b(int p_150002_1_)
    {
        return super.getTranslationKey() + "." + BlockStoneSlab.EnumType.func_176625_a(p_150002_1_).func_176627_c();
    }

    public IProperty<?> func_176551_l()
    {
        return field_176556_M;
    }

    public Comparable<?> func_185674_a(ItemStack p_185674_1_)
    {
        return BlockStoneSlab.EnumType.func_176625_a(p_185674_1_.func_77960_j() & 7);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        for (BlockStoneSlab.EnumType blockstoneslab$enumtype : BlockStoneSlab.EnumType.values())
        {
            if (blockstoneslab$enumtype != BlockStoneSlab.EnumType.WOOD)
            {
                items.add(new ItemStack(this, 1, blockstoneslab$enumtype.func_176624_a()));
            }
        }
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        IBlockState iblockstate = this.getDefaultState().func_177226_a(field_176556_M, BlockStoneSlab.EnumType.func_176625_a(p_176203_1_ & 7));

        if (this.func_176552_j())
        {
            iblockstate = iblockstate.func_177226_a(field_176555_b, Boolean.valueOf((p_176203_1_ & 8) != 0));
        }
        else
        {
            iblockstate = iblockstate.func_177226_a(field_176554_a, (p_176203_1_ & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }

        return iblockstate;
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;
        i = i | ((BlockStoneSlab.EnumType)p_176201_1_.get(field_176556_M)).func_176624_a();

        if (this.func_176552_j())
        {
            if (((Boolean)p_176201_1_.get(field_176555_b)).booleanValue())
            {
                i |= 8;
            }
        }
        else if (p_176201_1_.get(field_176554_a) == BlockSlab.EnumBlockHalf.TOP)
        {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer func_180661_e()
    {
        return this.func_176552_j() ? new BlockStateContainer(this, new IProperty[] {field_176555_b, field_176556_M}) : new BlockStateContainer(this, new IProperty[] {field_176554_a, field_176556_M});
    }

    public int func_180651_a(IBlockState p_180651_1_)
    {
        return ((BlockStoneSlab.EnumType)p_180651_1_.get(field_176556_M)).func_176624_a();
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     * @deprecated call via {@link IBlockState#getMapColor(IBlockAccess,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public MapColor getMaterialColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return ((BlockStoneSlab.EnumType)state.get(field_176556_M)).func_181074_c();
    }

    public static enum EnumType implements IStringSerializable
    {
        STONE(0, MapColor.STONE, "stone"),
        SAND(1, MapColor.SAND, "sandstone", "sand"),
        WOOD(2, MapColor.WOOD, "wood_old", "wood"),
        COBBLESTONE(3, MapColor.STONE, "cobblestone", "cobble"),
        BRICK(4, MapColor.RED, "brick"),
        SMOOTHBRICK(5, MapColor.STONE, "stone_brick", "smoothStoneBrick"),
        NETHERBRICK(6, MapColor.NETHERRACK, "nether_brick", "netherBrick"),
        QUARTZ(7, MapColor.QUARTZ, "quartz");

        private static final BlockStoneSlab.EnumType[] field_176640_i = new BlockStoneSlab.EnumType[values().length];
        private final int field_176637_j;
        private final MapColor field_181075_k;
        private final String field_176638_k;
        private final String field_176635_l;

        private EnumType(int p_i46381_3_, MapColor p_i46381_4_, String p_i46381_5_)
        {
            this(p_i46381_3_, p_i46381_4_, p_i46381_5_, p_i46381_5_);
        }

        private EnumType(int p_i46382_3_, MapColor p_i46382_4_, String p_i46382_5_, String p_i46382_6_)
        {
            this.field_176637_j = p_i46382_3_;
            this.field_181075_k = p_i46382_4_;
            this.field_176638_k = p_i46382_5_;
            this.field_176635_l = p_i46382_6_;
        }

        public int func_176624_a()
        {
            return this.field_176637_j;
        }

        public MapColor func_181074_c()
        {
            return this.field_181075_k;
        }

        public String toString()
        {
            return this.field_176638_k;
        }

        public static BlockStoneSlab.EnumType func_176625_a(int p_176625_0_)
        {
            if (p_176625_0_ < 0 || p_176625_0_ >= field_176640_i.length)
            {
                p_176625_0_ = 0;
            }

            return field_176640_i[p_176625_0_];
        }

        public String getName()
        {
            return this.field_176638_k;
        }

        public String func_176627_c()
        {
            return this.field_176635_l;
        }

        static {
            for (BlockStoneSlab.EnumType blockstoneslab$enumtype : values())
            {
                field_176640_i[blockstoneslab$enumtype.func_176624_a()] = blockstoneslab$enumtype;
            }
        }
    }
}
