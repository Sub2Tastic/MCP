package net.minecraft.enchantment;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;

public class EnchantmentProtection extends Enchantment
{
    public final EnchantmentProtection.Type protectionType;

    public EnchantmentProtection(Enchantment.Rarity rarityIn, EnchantmentProtection.Type protectionTypeIn, EntityEquipmentSlot... slots)
    {
        super(rarityIn, EnumEnchantmentType.ARMOR, slots);
        this.protectionType = protectionTypeIn;

        if (protectionTypeIn == EnchantmentProtection.Type.FALL)
        {
            this.type = EnumEnchantmentType.ARMOR_FEET;
        }
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int enchantmentLevel)
    {
        return this.protectionType.getMinimalEnchantability() + (enchantmentLevel - 1) * this.protectionType.getEnchantIncreasePerLevel();
    }

    public int func_77317_b(int p_77317_1_)
    {
        return this.getMinEnchantability(p_77317_1_) + this.protectionType.getEnchantIncreasePerLevel();
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel()
    {
        return 4;
    }

    /**
     * Calculates the damage protection of the enchantment based on level and damage source passed.
     */
    public int calcModifierDamage(int level, DamageSource source)
    {
        if (source.canHarmInCreative())
        {
            return 0;
        }
        else if (this.protectionType == EnchantmentProtection.Type.ALL)
        {
            return level;
        }
        else if (this.protectionType == EnchantmentProtection.Type.FIRE && source.isFireDamage())
        {
            return level * 2;
        }
        else if (this.protectionType == EnchantmentProtection.Type.FALL && source == DamageSource.FALL)
        {
            return level * 3;
        }
        else if (this.protectionType == EnchantmentProtection.Type.EXPLOSION && source.isExplosion())
        {
            return level * 2;
        }
        else
        {
            return this.protectionType == EnchantmentProtection.Type.PROJECTILE && source.isProjectile() ? level * 2 : 0;
        }
    }

    /**
     * Return the name of key in translation table of this enchantment.
     */
    public String getName()
    {
        return "enchantment.protect." + this.protectionType.func_185314_a();
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
    public boolean canApplyTogether(Enchantment ench)
    {
        if (ench instanceof EnchantmentProtection)
        {
            EnchantmentProtection enchantmentprotection = (EnchantmentProtection)ench;

            if (this.protectionType == enchantmentprotection.protectionType)
            {
                return false;
            }
            else
            {
                return this.protectionType == EnchantmentProtection.Type.FALL || enchantmentprotection.protectionType == EnchantmentProtection.Type.FALL;
            }
        }
        else
        {
            return super.canApplyTogether(ench);
        }
    }

    /**
     * Gets the amount of ticks an entity should be set fire, adjusted for fire protection.
     */
    public static int getFireTimeForEntity(EntityLivingBase p_92093_0_, int p_92093_1_)
    {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FIRE_PROTECTION, p_92093_0_);

        if (i > 0)
        {
            p_92093_1_ -= MathHelper.floor((float)p_92093_1_ * (float)i * 0.15F);
        }

        return p_92093_1_;
    }

    public static double getBlastDamageReduction(EntityLivingBase entityLivingBaseIn, double damage)
    {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.BLAST_PROTECTION, entityLivingBaseIn);

        if (i > 0)
        {
            damage -= (double)MathHelper.floor(damage * (double)((float)i * 0.15F));
        }

        return damage;
    }

    public static enum Type
    {
        ALL("all", 1, 11, 20),
        FIRE("fire", 10, 8, 12),
        FALL("fall", 5, 6, 10),
        EXPLOSION("explosion", 5, 8, 12),
        PROJECTILE("projectile", 3, 6, 15);

        private final String typeName;
        private final int minEnchantability;
        private final int levelCost;
        private final int field_185325_i;

        private Type(String p_i47051_3_, int p_i47051_4_, int p_i47051_5_, int p_i47051_6_)
        {
            this.typeName = p_i47051_3_;
            this.minEnchantability = p_i47051_4_;
            this.levelCost = p_i47051_5_;
            this.field_185325_i = p_i47051_6_;
        }

        public String func_185314_a()
        {
            return this.typeName;
        }

        public int getMinimalEnchantability()
        {
            return this.minEnchantability;
        }

        public int getEnchantIncreasePerLevel()
        {
            return this.levelCost;
        }
    }
}
