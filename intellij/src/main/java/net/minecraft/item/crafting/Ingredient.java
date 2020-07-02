package net.minecraft.item.crafting;

import com.google.common.base.Predicate;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import javax.annotation.Nullable;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Ingredient implements Predicate<ItemStack>
{
    public static final Ingredient EMPTY = new Ingredient(new ItemStack[0])
    {
        public boolean apply(@Nullable ItemStack p_apply_1_)
        {
            return p_apply_1_.isEmpty();
        }
    };
    private final ItemStack[] matchingStacks;
    private IntList matchingStacksPacked;

    private Ingredient(ItemStack... p_i47503_1_)
    {
        this.matchingStacks = p_i47503_1_;
    }

    public ItemStack[] getMatchingStacks()
    {
        return this.matchingStacks;
    }

    public boolean apply(@Nullable ItemStack p_apply_1_)
    {
        if (p_apply_1_ == null)
        {
            return false;
        }
        else
        {
            for (ItemStack itemstack : this.matchingStacks)
            {
                if (itemstack.getItem() == p_apply_1_.getItem())
                {
                    int i = itemstack.func_77960_j();

                    if (i == 32767 || i == p_apply_1_.func_77960_j())
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public IntList getValidItemStacksPacked()
    {
        if (this.matchingStacksPacked == null)
        {
            this.matchingStacksPacked = new IntArrayList(this.matchingStacks.length);

            for (ItemStack itemstack : this.matchingStacks)
            {
                this.matchingStacksPacked.add(RecipeItemHelper.pack(itemstack));
            }

            this.matchingStacksPacked.sort(IntComparators.NATURAL_COMPARATOR);
        }

        return this.matchingStacksPacked;
    }

    public static Ingredient func_193367_a(Item p_193367_0_)
    {
        return fromStacks(new ItemStack(p_193367_0_, 1, 32767));
    }

    public static Ingredient func_193368_a(Item... p_193368_0_)
    {
        ItemStack[] aitemstack = new ItemStack[p_193368_0_.length];

        for (int i = 0; i < p_193368_0_.length; ++i)
        {
            aitemstack[i] = new ItemStack(p_193368_0_[i]);
        }

        return fromStacks(aitemstack);
    }

    public static Ingredient fromStacks(ItemStack... stacks)
    {
        if (stacks.length > 0)
        {
            for (ItemStack itemstack : stacks)
            {
                if (!itemstack.isEmpty())
                {
                    return new Ingredient(stacks);
                }
            }
        }

        return EMPTY;
    }
}
