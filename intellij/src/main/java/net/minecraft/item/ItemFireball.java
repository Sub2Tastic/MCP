package net.minecraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFireball extends Item
{
    public ItemFireball()
    {
        this.func_77637_a(CreativeTabs.MISC);
    }

    public EnumActionResult func_180614_a(EntityPlayer p_180614_1_, World p_180614_2_, BlockPos p_180614_3_, EnumHand p_180614_4_, EnumFacing p_180614_5_, float p_180614_6_, float p_180614_7_, float p_180614_8_)
    {
        if (p_180614_2_.isRemote)
        {
            return EnumActionResult.SUCCESS;
        }
        else
        {
            p_180614_3_ = p_180614_3_.offset(p_180614_5_);
            ItemStack itemstack = p_180614_1_.getHeldItem(p_180614_4_);

            if (!p_180614_1_.canPlayerEdit(p_180614_3_, p_180614_5_, itemstack))
            {
                return EnumActionResult.FAIL;
            }
            else
            {
                if (p_180614_2_.getBlockState(p_180614_3_).getMaterial() == Material.AIR)
                {
                    p_180614_2_.playSound((EntityPlayer)null, p_180614_3_, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
                    p_180614_2_.setBlockState(p_180614_3_, Blocks.FIRE.getDefaultState());
                }

                if (!p_180614_1_.abilities.isCreativeMode)
                {
                    itemstack.shrink(1);
                }

                return EnumActionResult.SUCCESS;
            }
        }
    }
}
