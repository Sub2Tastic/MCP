package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemCloth extends ItemBlock
{
    public ItemCloth(Block p_i45358_1_)
    {
        super(p_i45358_1_);
        this.func_77656_e(0);
        this.func_77627_a(true);
    }

    public int func_77647_b(int p_77647_1_)
    {
        return p_77647_1_;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getTranslationKey(ItemStack stack)
    {
        return super.getTranslationKey() + "." + EnumDyeColor.func_176764_b(stack.func_77960_j()).getTranslationKey();
    }
}
