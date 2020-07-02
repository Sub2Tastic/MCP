package net.minecraft.block;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockNewLeaf extends BlockLeaves
{
    public static final PropertyEnum<BlockPlanks.EnumType> field_176240_P = PropertyEnum.<BlockPlanks.EnumType>create("variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>()
    {
        public boolean apply(@Nullable BlockPlanks.EnumType p_apply_1_)
        {
            return p_apply_1_.func_176839_a() >= 4;
        }
    });

    public BlockNewLeaf()
    {
        this.setDefaultState(this.stateContainer.getBaseState().func_177226_a(field_176240_P, BlockPlanks.EnumType.ACACIA).func_177226_a(field_176236_b, Boolean.valueOf(true)).func_177226_a(field_176237_a, Boolean.valueOf(true)));
    }

    protected void func_176234_a(World p_176234_1_, BlockPos p_176234_2_, IBlockState p_176234_3_, int p_176234_4_)
    {
        if (p_176234_3_.get(field_176240_P) == BlockPlanks.EnumType.DARK_OAK && p_176234_1_.rand.nextInt(p_176234_4_) == 0)
        {
            spawnAsEntity(p_176234_1_, p_176234_2_, new ItemStack(Items.APPLE));
        }
    }

    public int func_180651_a(IBlockState p_180651_1_)
    {
        return ((BlockPlanks.EnumType)p_180651_1_.get(field_176240_P)).func_176839_a();
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this, 1, state.getBlock().func_176201_c(state) & 3);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
    }

    protected ItemStack func_180643_i(IBlockState p_180643_1_)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType)p_180643_1_.get(field_176240_P)).func_176839_a() - 4);
    }

    public IBlockState func_176203_a(int p_176203_1_)
    {
        return this.getDefaultState().func_177226_a(field_176240_P, this.func_176233_b(p_176203_1_)).func_177226_a(field_176237_a, Boolean.valueOf((p_176203_1_ & 4) == 0)).func_177226_a(field_176236_b, Boolean.valueOf((p_176203_1_ & 8) > 0));
    }

    public int func_176201_c(IBlockState p_176201_1_)
    {
        int i = 0;
        i = i | ((BlockPlanks.EnumType)p_176201_1_.get(field_176240_P)).func_176839_a() - 4;

        if (!((Boolean)p_176201_1_.get(field_176237_a)).booleanValue())
        {
            i |= 4;
        }

        if (((Boolean)p_176201_1_.get(field_176236_b)).booleanValue())
        {
            i |= 8;
        }

        return i;
    }

    public BlockPlanks.EnumType func_176233_b(int p_176233_1_)
    {
        return BlockPlanks.EnumType.func_176837_a((p_176233_1_ & 3) + 4);
    }

    protected BlockStateContainer func_180661_e()
    {
        return new BlockStateContainer(this, new IProperty[] {field_176240_P, field_176236_b, field_176237_a});
    }

    /**
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
     * Block.removedByPlayer
     */
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS)
        {
            player.addStat(StatList.func_188055_a(this));
            spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType)state.get(field_176240_P)).func_176839_a() - 4));
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }
}
