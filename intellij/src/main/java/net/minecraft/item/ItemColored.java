package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemColored extends ItemBlock
{
    private String[] field_150945_c;

    public ItemColored(Block p_i45332_1_, boolean p_i45332_2_)
    {
        super(p_i45332_1_);

        if (p_i45332_2_)
        {
            this.func_77656_e(0);
            this.func_77627_a(true);
        }
    }

    public int func_77647_b(int p_77647_1_)
    {
        return p_77647_1_;
    }

    public ItemColored func_150943_a(String[] p_150943_1_)
    {
        this.field_150945_c = p_150943_1_;
        return this;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getTranslationKey(ItemStack stack)
    {
        if (this.field_150945_c == null)
        {
            return super.getTranslationKey(stack);
        }
        else
        {
            int i = stack.func_77960_j();
            return i >= 0 && i < this.field_150945_c.length ? super.getTranslationKey(stack) + "." + this.field_150945_c[i] : super.getTranslationKey(stack);
        }
    }
}
