package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemMultiTexture extends ItemBlock
{
    protected final Block field_179227_b;
    protected final ItemMultiTexture.Mapper field_179228_c;

    public ItemMultiTexture(Block p_i47262_1_, Block p_i47262_2_, ItemMultiTexture.Mapper p_i47262_3_)
    {
        super(p_i47262_1_);
        this.field_179227_b = p_i47262_2_;
        this.field_179228_c = p_i47262_3_;
        this.func_77656_e(0);
        this.func_77627_a(true);
    }

    public ItemMultiTexture(Block p_i45346_1_, Block p_i45346_2_, final String[] p_i45346_3_)
    {
        this(p_i45346_1_, p_i45346_2_, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                int i = p_apply_1_.func_77960_j();

                if (i < 0 || i >= p_i45346_3_.length)
                {
                    i = 0;
                }

                return p_i45346_3_[i];
            }
        });
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
        return super.getTranslationKey() + "." + this.field_179228_c.apply(stack);
    }

    public interface Mapper
    {
        String apply(ItemStack var1);
    }
}
