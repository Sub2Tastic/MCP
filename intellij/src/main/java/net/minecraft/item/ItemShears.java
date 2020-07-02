package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemShears extends Item
{
    public ItemShears()
    {
        this.func_77625_d(1);
        this.func_77656_e(238);
        this.func_77637_a(CreativeTabs.TOOLS);
    }

    /**
     * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
     */
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        if (!worldIn.isRemote)
        {
            stack.func_77972_a(1, entityLiving);
        }

        Block block = state.getBlock();
        return state.getMaterial() != Material.LEAVES && block != Blocks.field_150321_G && block != Blocks.field_150329_H && block != Blocks.VINE && block != Blocks.TRIPWIRE && block != Blocks.field_150325_L ? super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving) : true;
    }

    /**
     * Check whether this Item can harvest the given Block
     */
    public boolean canHarvestBlock(IBlockState blockIn)
    {
        Block block = blockIn.getBlock();
        return block == Blocks.field_150321_G || block == Blocks.REDSTONE_WIRE || block == Blocks.TRIPWIRE;
    }

    public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        Block block = state.getBlock();

        if (block != Blocks.field_150321_G && state.getMaterial() != Material.LEAVES)
        {
            return block == Blocks.field_150325_L ? 5.0F : super.getDestroySpeed(stack, state);
        }
        else
        {
            return 15.0F;
        }
    }
}
