package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemTippedArrow extends ItemArrow
{
    public ItemStack getDefaultInstance()
    {
        return PotionUtils.addPotionToItemStack(super.getDefaultInstance(), PotionTypes.POISON);
    }

    public EntityArrow func_185052_a(World p_185052_1_, ItemStack p_185052_2_, EntityLivingBase p_185052_3_)
    {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(p_185052_1_, p_185052_3_);
        entitytippedarrow.setPotionEffect(p_185052_2_);
        return entitytippedarrow;
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void fillItemGroup(CreativeTabs group, NonNullList<ItemStack> items)
    {
        if (this.isInGroup(group))
        {
            for (PotionType potiontype : PotionType.field_185176_a)
            {
                if (!potiontype.getEffects().isEmpty())
                {
                    items.add(PotionUtils.addPotionToItemStack(new ItemStack(this), potiontype));
                }
            }
        }
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        PotionUtils.addPotionTooltip(stack, tooltip, 0.125F);
    }

    public String func_77653_i(ItemStack p_77653_1_)
    {
        return I18n.func_74838_a(PotionUtils.getPotionFromItem(p_77653_1_).getNamePrefixed("tipped_arrow.effect."));
    }
}
