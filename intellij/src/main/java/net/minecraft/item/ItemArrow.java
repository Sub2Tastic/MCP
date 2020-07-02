package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.world.World;

public class ItemArrow extends Item
{
    public ItemArrow()
    {
        this.func_77637_a(CreativeTabs.COMBAT);
    }

    public EntityArrow func_185052_a(World p_185052_1_, ItemStack p_185052_2_, EntityLivingBase p_185052_3_)
    {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(p_185052_1_, p_185052_3_);
        entitytippedarrow.setPotionEffect(p_185052_2_);
        return entitytippedarrow;
    }
}
