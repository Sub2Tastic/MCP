package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class ItemNameTag extends Item
{
    public ItemNameTag()
    {
        this.func_77637_a(CreativeTabs.TOOLS);
    }

    /**
     * Returns true if the item can be used on the given entity, e.g. shears on sheep.
     */
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
    {
        if (stack.hasDisplayName() && !(target instanceof EntityPlayer))
        {
            target.func_96094_a(stack.func_82833_r());

            if (target instanceof EntityLiving)
            {
                ((EntityLiving)target).enablePersistence();
            }

            stack.shrink(1);
            return true;
        }
        else
        {
            return false;
        }
    }
}
