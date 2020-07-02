package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockColored extends Block
{
    public static final PropertyEnum<EnumDyeColor> field_176581_a = PropertyEnum.<EnumDyeColor>create("color", EnumDyeColor.class);

    public BlockColored(Material p_i45398_1_)
    {
        super(p_i45398_1_);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176581_a, EnumDyeColor.WHITE));
        this.func_149647_a(CreativeTabs.BUILDING_BLOCKS);
    }

    public int func_180651_a(IBlockState p_180651_1_)
    {
        return ((EnumDyeColor)p_180651_1_.get(field_176581_a)).func_176765_a();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        for (EnumDyeColor enumdyecolor : EnumDyeColor.values())
        {
            items.add(new ItemStack(this, 1, enumdyecolor.func_176765_a()));
        }
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     * @deprecated call via {@link IBlockState#getMapColor(IBlockAccess,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public MapColor getMaterialColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return MapColor.func_193558_a((EnumDyeColor)state.get(field_176581_a));
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(field_176581_a, EnumDyeColor.func_176764_b(p_176203_1_));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((EnumDyeColor)p_176201_1_.get(field_176581_a)).func_176765_a();
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176581_a});
    }
}
