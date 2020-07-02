package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockConcretePowder extends BlockFalling
{
    public static final PropertyEnum<EnumDyeColor> field_192426_a = PropertyEnum.<EnumDyeColor>create("color", EnumDyeColor.class);

    public BlockConcretePowder()
    {
        super(Material.SAND);
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_192426_a, EnumDyeColor.WHITE));
        this.func_149647_a(CreativeTabs.BUILDING_BLOCKS);
    }

    public void onEndFalling(World worldIn, BlockPos pos, IBlockState fallingState, IBlockState hitState)
    {
        if (hitState.getMaterial().isLiquid())
        {
            worldIn.setBlockState(pos, Blocks.field_192443_dR.getDefaultState().func_177226_a(BlockColored.field_176581_a, fallingState.get(field_192426_a)), 3);
        }
    }

    protected boolean func_192425_e(World p_192425_1_, BlockPos p_192425_2_, IBlockState p_192425_3_)
    {
        boolean flag = false;

        for (EnumFacing enumfacing : EnumFacing.values())
        {
            if (enumfacing != EnumFacing.DOWN)
            {
                BlockPos blockpos = p_192425_2_.offset(enumfacing);

                if (p_192425_1_.getBlockState(blockpos).getMaterial() == Material.WATER)
                {
                    flag = true;
                    break;
                }
            }
        }

        if (flag)
        {
            p_192425_1_.setBlockState(p_192425_2_, Blocks.field_192443_dR.getDefaultState().func_177226_a(BlockColored.field_176581_a, p_192425_3_.get(field_192426_a)), 3);
        }

        return flag;
    }

    public void func_189540_a(IBlockState p_189540_1_, World p_189540_2_, BlockPos p_189540_3_, Block p_189540_4_, BlockPos p_189540_5_)
    {
        if (!this.func_192425_e(p_189540_2_, p_189540_3_, p_189540_1_))
        {
            super.func_189540_a(p_189540_1_, p_189540_2_, p_189540_3_, p_189540_4_, p_189540_5_);
        }
    }

    public void func_176213_c(World p_176213_1_, BlockPos p_176213_2_, IBlockState p_176213_3_)
    {
        if (!this.func_192425_e(p_176213_1_, p_176213_2_, p_176213_3_))
        {
            super.func_176213_c(p_176213_1_, p_176213_2_, p_176213_3_);
        }
    }

    public int func_180651_a(IBlockState p_180651_1_)
    {
        return ((EnumDyeColor)p_180651_1_.get(field_192426_a)).func_176765_a();
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
        return MapColor.func_193558_a((EnumDyeColor)state.get(field_192426_a));
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(field_192426_a, EnumDyeColor.func_176764_b(p_176203_1_));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        return ((EnumDyeColor)p_176201_1_.get(field_192426_a)).func_176765_a();
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_192426_a});
    }
}
