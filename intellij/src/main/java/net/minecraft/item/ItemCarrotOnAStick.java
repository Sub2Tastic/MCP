package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemCarrotOnAStick extends Item
{
    public ItemCarrotOnAStick()
    {
        this.func_77637_a(CreativeTabs.TRANSPORTATION);
        this.func_77625_d(1);
        this.func_77656_e(25);
    }

    public boolean func_77662_d()
    {
        return true;
    }

    public boolean func_77629_n_()
    {
        return true;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (worldIn.isRemote)
        {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        else
        {
            if (playerIn.isPassenger() && playerIn.getRidingEntity() instanceof EntityPig)
            {
                EntityPig entitypig = (EntityPig)playerIn.getRidingEntity();

                if (itemstack.getMaxDamage() - itemstack.func_77960_j() >= 7 && entitypig.boost())
                {
                    itemstack.func_77972_a(7, playerIn);

                    if (itemstack.isEmpty())
                    {
                        ItemStack itemstack1 = new ItemStack(Items.FISHING_ROD);
                        itemstack1.setTag(itemstack.getTag());
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack1);
                    }

                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
                }
            }

            playerIn.addStat(StatList.func_188057_b(this));
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
    }
}
