package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemFireworkCharge extends Item
{
    public static NBTBase func_150903_a(ItemStack p_150903_0_, String p_150903_1_)
    {
        if (p_150903_0_.hasTag())
        {
            NBTTagCompound nbttagcompound = p_150903_0_.getTag().getCompound("Explosion");

            if (nbttagcompound != null)
            {
                return nbttagcompound.get(p_150903_1_);
            }
        }

        return null;
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if (stack.hasTag())
        {
            NBTTagCompound nbttagcompound = stack.getTag().getCompound("Explosion");

            if (nbttagcompound != null)
            {
                func_150902_a(nbttagcompound, tooltip);
            }
        }
    }

    public static void func_150902_a(NBTTagCompound p_150902_0_, List<String> p_150902_1_)
    {
        byte b0 = p_150902_0_.getByte("Type");

        if (b0 >= 0 && b0 <= 4)
        {
            p_150902_1_.add(I18n.func_74838_a("item.fireworksCharge.type." + b0).trim());
        }
        else
        {
            p_150902_1_.add(I18n.func_74838_a("item.fireworksCharge.type").trim());
        }

        int[] aint = p_150902_0_.getIntArray("Colors");

        if (aint.length > 0)
        {
            boolean flag = true;
            String s = "";

            for (int i : aint)
            {
                if (!flag)
                {
                    s = s + ", ";
                }

                flag = false;
                boolean flag1 = false;

                for (int j = 0; j < ItemDye.field_150922_c.length; ++j)
                {
                    if (i == ItemDye.field_150922_c[j])
                    {
                        flag1 = true;
                        s = s + I18n.func_74838_a("item.fireworksCharge." + EnumDyeColor.func_176766_a(j).getTranslationKey());
                        break;
                    }
                }

                if (!flag1)
                {
                    s = s + I18n.func_74838_a("item.fireworksCharge.customColor");
                }
            }

            p_150902_1_.add(s);
        }

        int[] aint1 = p_150902_0_.getIntArray("FadeColors");

        if (aint1.length > 0)
        {
            boolean flag2 = true;
            String s1 = I18n.func_74838_a("item.fireworksCharge.fadeTo") + " ";

            for (int l : aint1)
            {
                if (!flag2)
                {
                    s1 = s1 + ", ";
                }

                flag2 = false;
                boolean flag5 = false;

                for (int k = 0; k < 16; ++k)
                {
                    if (l == ItemDye.field_150922_c[k])
                    {
                        flag5 = true;
                        s1 = s1 + I18n.func_74838_a("item.fireworksCharge." + EnumDyeColor.func_176766_a(k).getTranslationKey());
                        break;
                    }
                }

                if (!flag5)
                {
                    s1 = s1 + I18n.func_74838_a("item.fireworksCharge.customColor");
                }
            }

            p_150902_1_.add(s1);
        }

        boolean flag3 = p_150902_0_.getBoolean("Trail");

        if (flag3)
        {
            p_150902_1_.add(I18n.func_74838_a("item.fireworksCharge.trail"));
        }

        boolean flag4 = p_150902_0_.getBoolean("Flicker");

        if (flag4)
        {
            p_150902_1_.add(I18n.func_74838_a("item.fireworksCharge.flicker"));
        }
    }
}
