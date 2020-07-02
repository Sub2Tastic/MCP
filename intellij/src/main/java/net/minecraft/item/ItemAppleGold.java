package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemAppleGold extends ItemFood
{
    public ItemAppleGold(int p_i45341_1_, float p_i45341_2_, boolean p_i45341_3_)
    {
        super(p_i45341_1_, p_i45341_2_, p_i45341_3_);
        this.func_77627_a(true);
    }

    /**
     * Returns true if this item has an enchantment glint. By default, this returns
     * <code>stack.isItemEnchanted()</code>, but other items can override it (for instance, written books always return
     * true).
     *  
     * Note that if you override this method, you generally want to also call the super version (on {@link Item}) to get
     * the glint for enchanted items. Of course, that is unnecessary if the overwritten version always returns true.
     */
    public boolean hasEffect(ItemStack stack)
    {
        return super.hasEffect(stack) || stack.func_77960_j() > 0;
    }

    /**
     * Return an item rarity from EnumRarity
     */
    public EnumRarity getRarity(ItemStack stack)
    {
        return stack.func_77960_j() == 0 ? EnumRarity.RARE : EnumRarity.EPIC;
    }

    protected void func_77849_c(ItemStack p_77849_1_, World p_77849_2_, EntityPlayer p_77849_3_)
    {
        if (!p_77849_2_.isRemote)
        {
            if (p_77849_1_.func_77960_j() > 0)
            {
                p_77849_3_.func_70690_d(new PotionEffect(MobEffects.REGENERATION, 400, 1));
                p_77849_3_.func_70690_d(new PotionEffect(MobEffects.RESISTANCE, 6000, 0));
                p_77849_3_.func_70690_d(new PotionEffect(MobEffects.FIRE_RESISTANCE, 6000, 0));
                p_77849_3_.func_70690_d(new PotionEffect(MobEffects.ABSORPTION, 2400, 3));
            }
            else
            {
                p_77849_3_.func_70690_d(new PotionEffect(MobEffects.REGENERATION, 100, 1));
                p_77849_3_.func_70690_d(new PotionEffect(MobEffects.ABSORPTION, 2400, 0));
            }
        }
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        if (this.isInGroup(group))
        {
            items.add(new ItemStack(this));
            items.add(new ItemStack(this, 1, 1));
        }
    }
}
