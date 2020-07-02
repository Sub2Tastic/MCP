package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSeeds extends Item
{
    private final Block field_150925_a;
    private final Block field_77838_b;

    public ItemSeeds(Block p_i45352_1_, Block p_i45352_2_)
    {
        this.field_150925_a = p_i45352_1_;
        this.field_77838_b = p_i45352_2_;
        this.func_77637_a(CreativeTabs.MATERIALS);
    }

    public EnumActionResult func_180614_a(EntityPlayer p_180614_1_, World p_180614_2_, BlockPos p_180614_3_, EnumHand p_180614_4_, EnumFacing p_180614_5_, float p_180614_6_, float p_180614_7_, float p_180614_8_)
    {
        ItemStack itemstack = p_180614_1_.getHeldItem(p_180614_4_);

        if (p_180614_5_ == EnumFacing.UP && p_180614_1_.canPlayerEdit(p_180614_3_.offset(p_180614_5_), p_180614_5_, itemstack) && p_180614_2_.getBlockState(p_180614_3_).getBlock() == this.field_77838_b && p_180614_2_.isAirBlock(p_180614_3_.up()))
        {
            p_180614_2_.setBlockState(p_180614_3_.up(), this.field_150925_a.getDefaultState());

            if (p_180614_1_ instanceof EntityPlayerMP)
            {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)p_180614_1_, p_180614_3_.up(), itemstack);
            }

            itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }
}
