package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemEnchantedBook extends Item
{
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
        return true;
    }

    /**
     * Checks isDamagable and if it cannot be stacked
     */
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

    /**
     * Return an item rarity from EnumRarity
     */
    public EnumRarity getRarity(ItemStack stack)
    {
        return getEnchantments(stack).func_82582_d() ? super.getRarity(stack) : EnumRarity.UNCOMMON;
    }

    public static NBTTagList getEnchantments(ItemStack stack)
    {
        NBTTagCompound nbttagcompound = stack.getTag();
        return nbttagcompound != null ? nbttagcompound.getList("StoredEnchantments", 10) : new NBTTagList();
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        NBTTagList nbttaglist = getEnchantments(stack);

        for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompound(i);
            int j = nbttagcompound.getShort("id");
            Enchantment enchantment = Enchantment.getEnchantmentByID(j);

            if (enchantment != null)
            {
                tooltip.add(enchantment.func_77316_c(nbttagcompound.getShort("lvl")));
            }
        }
    }

    /**
     * Adds an stored enchantment to an enchanted book ItemStack
     */
    public static void addEnchantment(ItemStack p_92115_0_, EnchantmentData stack)
    {
        NBTTagList nbttaglist = getEnchantments(p_92115_0_);
        boolean flag = true;

        for (int i = 0; i < nbttaglist.func_74745_c(); ++i)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompound(i);

            if (Enchantment.getEnchantmentByID(nbttagcompound.getShort("id")) == stack.enchantment)
            {
                if (nbttagcompound.getShort("lvl") < stack.enchantmentLevel)
                {
                    nbttagcompound.putShort("lvl", (short)stack.enchantmentLevel);
                }

                flag = false;
                break;
            }
        }

        if (flag)
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.putShort("id", (short)Enchantment.func_185258_b(stack.enchantment));
            nbttagcompound1.putShort("lvl", (short)stack.enchantmentLevel);
            nbttaglist.func_74742_a(nbttagcompound1);
        }

        if (!p_92115_0_.hasTag())
        {
            p_92115_0_.setTag(new NBTTagCompound());
        }

        p_92115_0_.getTag().func_74782_a("StoredEnchantments", nbttaglist);
    }

    /**
     * Returns the ItemStack of an enchanted version of this item.
     */
    public static ItemStack getEnchantedItemStack(EnchantmentData enchantData)
    {
        ItemStack itemstack = new ItemStack(Items.ENCHANTED_BOOK);
        addEnchantment(itemstack, enchantData);
        return itemstack;
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        if (group == CreativeTabs.SEARCH)
        {
            for (Enchantment enchantment : Enchantment.field_185264_b)
            {
                if (enchantment.type != null)
                {
                    for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); ++i)
                    {
                        items.add(getEnchantedItemStack(new EnchantmentData(enchantment, i)));
                    }
                }
            }
        }
        else if (group.getRelevantEnchantmentTypes().length != 0)
        {
            for (Enchantment enchantment1 : Enchantment.field_185264_b)
            {
                if (group.hasRelevantEnchantmentType(enchantment1.type))
                {
                    items.add(getEnchantedItemStack(new EnchantmentData(enchantment1, enchantment1.getMaxLevel())));
                }
            }
        }
    }
}
