package net.minecraft.block;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockNewLog extends BlockLog
{
    public static final PropertyEnum<BlockPlanks.EnumType> field_176300_b = PropertyEnum.<BlockPlanks.EnumType>create("variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>()
    {
        public boolean apply(@Nullable BlockPlanks.EnumType p_apply_1_)
        {
            return p_apply_1_.func_176839_a() >= 4;
        }
    });

    public BlockNewLog()
    {
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176300_b, BlockPlanks.EnumType.ACACIA).func_177226_a(field_176299_a, BlockLog.EnumAxis.Y));
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     * @deprecated call via {@link IBlockState#getMapColor(IBlockAccess,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public MapColor getMaterialColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        BlockPlanks.EnumType blockplanks$enumtype = (BlockPlanks.EnumType)state.get(field_176300_b);

        switch ((BlockLog.EnumAxis)state.get(field_176299_a))
        {
            case X:
            case Z:
            case NONE:
            default:
                switch (blockplanks$enumtype)
                {
                    case ACACIA:
                    default:
                        return MapColor.STONE;

                    case DARK_OAK:
                        return BlockPlanks.EnumType.DARK_OAK.func_181070_c();
                }

            case Y:
                return blockplanks$enumtype.func_181070_c();
        }
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        items.add(new ItemStack(this, 1, BlockPlanks.EnumType.ACACIA.func_176839_a() - 4));
        items.add(new ItemStack(this, 1, BlockPlanks.EnumType.DARK_OAK.func_176839_a() - 4));
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        IBlockState iblockstate = this.getDefaultState().func_177226_a(field_176300_b, BlockPlanks.EnumType.func_176837_a((p_176203_1_ & 3) + 4));

        switch (p_176203_1_ & 12)
        {
            case 0:
                iblockstate = iblockstate.func_177226_a(field_176299_a, BlockLog.EnumAxis.Y);
                break;

            case 4:
                iblockstate = iblockstate.func_177226_a(field_176299_a, BlockLog.EnumAxis.X);
                break;

            case 8:
                iblockstate = iblockstate.func_177226_a(field_176299_a, BlockLog.EnumAxis.Z);
                break;

            default:
                iblockstate = iblockstate.func_177226_a(field_176299_a, BlockLog.EnumAxis.NONE);
        }

        return iblockstate;
    }

    @SuppressWarnings("incomplete-switch")
    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;
        i = i | ((BlockPlanks.EnumType)p_176201_1_.get(field_176300_b)).func_176839_a() - 4;

        switch ((BlockLog.EnumAxis)p_176201_1_.get(field_176299_a))
        {
            case X:
                i |= 4;
                break;

            case Z:
                i |= 8;
                break;

            case NONE:
                i |= 12;
        }

        return i;
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176300_b, field_176299_a});
    }

    protected ItemStack func_180643_i(IBlockState p_180643_1_)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType)p_180643_1_.get(field_176300_b)).func_176839_a() - 4);
    }

    public int func_180651_a(IBlockState p_180651_1_)
    {
        return ((BlockPlanks.EnumType)p_180651_1_.get(field_176300_b)).func_176839_a() - 4;
    }
}
