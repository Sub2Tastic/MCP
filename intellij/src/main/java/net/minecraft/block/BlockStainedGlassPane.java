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
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStainedGlassPane extends BlockPane
{
    public static final PropertyEnum<EnumDyeColor> field_176245_a = PropertyEnum.<EnumDyeColor>create("color", EnumDyeColor.class);

    public BlockStainedGlassPane()
    {
        super(Material.GLASS, false);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176241_b, Boolean.valueOf(false)).func_177226_a(field_176242_M, Boolean.valueOf(false)).func_177226_a(field_176243_N, Boolean.valueOf(false)).func_177226_a(field_176244_O, Boolean.valueOf(false)).func_177226_a(field_176245_a, EnumDyeColor.WHITE));
        this.func_149647_a(CreativeTabs.DECORATIONS);
    }

    public int func_180651_a(IBlockState p_180651_1_)
    {
        return ((EnumDyeColor)p_180651_1_.get(field_176245_a)).func_176765_a();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        for (int i = 0; i < EnumDyeColor.values().length; ++i)
        {
            items.add(new ItemStack(this, 1, i));
        }
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     * @deprecated call via {@link IBlockState#getMapColor(IBlockAccess,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public MapColor getMaterialColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return MapColor.func_193558_a((EnumDyeColor)state.get(field_176245_a));
    }

    public BlockRenderLayer func_180664_k()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(field_176245_a, EnumDyeColor.func_176764_b(p_176203_1_));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((EnumDyeColor)p_176201_1_.get(field_176245_a)).func_176765_a();
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
                return state.func_177226_a(field_176241_b, state.get(field_176243_N)).func_177226_a(field_176242_M, state.get(field_176244_O)).func_177226_a(field_176243_N, state.get(field_176241_b)).func_177226_a(field_176244_O, state.get(field_176242_M));

            case COUNTERCLOCKWISE_90:
                return state.func_177226_a(field_176241_b, state.get(field_176242_M)).func_177226_a(field_176242_M, state.get(field_176243_N)).func_177226_a(field_176243_N, state.get(field_176244_O)).func_177226_a(field_176244_O, state.get(field_176241_b));

            case CLOCKWISE_90:
                return state.func_177226_a(field_176241_b, state.get(field_176244_O)).func_177226_a(field_176242_M, state.get(field_176241_b)).func_177226_a(field_176243_N, state.get(field_176242_M)).func_177226_a(field_176244_O, state.get(field_176243_N));

            default:
                return state;
        }
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
        switch (mirrorIn)
        {
            case LEFT_RIGHT:
                return state.func_177226_a(field_176241_b, state.get(field_176243_N)).func_177226_a(field_176243_N, state.get(field_176241_b));

            case FRONT_BACK:
                return state.func_177226_a(field_176242_M, state.get(field_176244_O)).func_177226_a(field_176244_O, state.get(field_176242_M));

            default:
                return super.mirror(state, mirrorIn);
        }
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176241_b, field_176242_M, field_176244_O, field_176243_N, field_176245_a});
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        if (!p_176213_1_.isRemote)
        {
            BlockBeacon.func_176450_d(p_176213_1_, p_176213_2_);
        }
    }

    public void func_180663_b(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_)
    {
        if (!p_180663_1_.isRemote)
        {
            BlockBeacon.func_176450_d(p_180663_1_, p_180663_2_);
        }
    }
}
