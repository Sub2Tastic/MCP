package net.minecraft.potion;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryNamespaced;

public class Potion
{
    public static final RegistryNamespaced<ResourceLocation, Potion> field_188414_b = new RegistryNamespaced<ResourceLocation, Potion>();
    private final Map<IAttribute, AttributeModifier> attributeModifierMap = Maps.<IAttribute, AttributeModifier>newHashMap();
    private final boolean field_76418_K;
    private final int liquidColor;
    private String name = "";
    private int field_76417_J = -1;
    private double field_76412_L;
    private boolean field_188415_h;

    @Nullable

    /**
     * Gets a Potion from the potion registry using a numeric Id.
     */
    public static Potion get(int potionID)
    {
        return field_188414_b.func_148754_a(potionID);
    }

    /**
     * Gets the numeric Id associated with a potion.
     */
    public static int getId(Potion potionIn)
    {
        return field_188414_b.getId(potionIn);
    }

    @Nullable
    public static Potion func_180142_b(String p_180142_0_)
    {
        return field_188414_b.getOrDefault(new ResourceLocation(p_180142_0_));
    }

    protected Potion(boolean p_i46815_1_, int p_i46815_2_)
    {
        this.field_76418_K = p_i46815_1_;

        if (p_i46815_1_)
        {
            this.field_76412_L = 0.5D;
        }
        else
        {
            this.field_76412_L = 1.0D;
        }

        this.liquidColor = p_i46815_2_;
    }

    protected Potion func_76399_b(int p_76399_1_, int p_76399_2_)
    {
        this.field_76417_J = p_76399_1_ + p_76399_2_ * 8;
        return this;
    }

    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
    {
        if (this == MobEffects.REGENERATION)
        {
            if (entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth())
            {
                entityLivingBaseIn.heal(1.0F);
            }
        }
        else if (this == MobEffects.POISON)
        {
            if (entityLivingBaseIn.getHealth() > 1.0F)
            {
                entityLivingBaseIn.attackEntityFrom(DamageSource.MAGIC, 1.0F);
            }
        }
        else if (this == MobEffects.WITHER)
        {
            entityLivingBaseIn.attackEntityFrom(DamageSource.WITHER, 1.0F);
        }
        else if (this == MobEffects.HUNGER && entityLivingBaseIn instanceof EntityPlayer)
        {
            ((EntityPlayer)entityLivingBaseIn).addExhaustion(0.005F * (float)(amplifier + 1));
        }
        else if (this == MobEffects.SATURATION && entityLivingBaseIn instanceof EntityPlayer)
        {
            if (!entityLivingBaseIn.world.isRemote)
            {
                ((EntityPlayer)entityLivingBaseIn).getFoodStats().addStats(amplifier + 1, 1.0F);
            }
        }
        else if ((this != MobEffects.INSTANT_HEALTH || entityLivingBaseIn.isEntityUndead()) && (this != MobEffects.INSTANT_DAMAGE || !entityLivingBaseIn.isEntityUndead()))
        {
            if (this == MobEffects.INSTANT_DAMAGE && !entityLivingBaseIn.isEntityUndead() || this == MobEffects.INSTANT_HEALTH && entityLivingBaseIn.isEntityUndead())
            {
                entityLivingBaseIn.attackEntityFrom(DamageSource.MAGIC, (float)(6 << amplifier));
            }
        }
        else
        {
            entityLivingBaseIn.heal((float)Math.max(4 << amplifier, 0));
        }
    }

    public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, EntityLivingBase entityLivingBaseIn, int amplifier, double health)
    {
        if ((this != MobEffects.INSTANT_HEALTH || entityLivingBaseIn.isEntityUndead()) && (this != MobEffects.INSTANT_DAMAGE || !entityLivingBaseIn.isEntityUndead()))
        {
            if (this == MobEffects.INSTANT_DAMAGE && !entityLivingBaseIn.isEntityUndead() || this == MobEffects.INSTANT_HEALTH && entityLivingBaseIn.isEntityUndead())
            {
                int j = (int)(health * (double)(6 << amplifier) + 0.5D);

                if (source == null)
                {
                    entityLivingBaseIn.attackEntityFrom(DamageSource.MAGIC, (float)j);
                }
                else
                {
                    entityLivingBaseIn.attackEntityFrom(DamageSource.causeIndirectMagicDamage(source, indirectSource), (float)j);
                }
            }
        }
        else
        {
            int i = (int)(health * (double)(4 << amplifier) + 0.5D);
            entityLivingBaseIn.heal((float)i);
        }
    }

    /**
     * checks if Potion effect is ready to be applied this tick.
     */
    public boolean isReady(int duration, int amplifier)
    {
        if (this == MobEffects.REGENERATION)
        {
            int k = 50 >> amplifier;

            if (k > 0)
            {
                return duration % k == 0;
            }
            else
            {
                return true;
            }
        }
        else if (this == MobEffects.POISON)
        {
            int j = 25 >> amplifier;

            if (j > 0)
            {
                return duration % j == 0;
            }
            else
            {
                return true;
            }
        }
        else if (this == MobEffects.WITHER)
        {
            int i = 40 >> amplifier;

            if (i > 0)
            {
                return duration % i == 0;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return this == MobEffects.HUNGER;
        }
    }

    /**
     * Returns true if the potion has an instant effect instead of a continuous one (eg Harming)
     */
    public boolean isInstant()
    {
        return false;
    }

    public Potion func_76390_b(String p_76390_1_)
    {
        this.name = p_76390_1_;
        return this;
    }

    /**
     * returns the name of the potion
     */
    public String getName()
    {
        return this.name;
    }

    public boolean func_76400_d()
    {
        return this.field_76417_J >= 0;
    }

    public int func_76392_e()
    {
        return this.field_76417_J;
    }

    public boolean func_76398_f()
    {
        return this.field_76418_K;
    }

    public static String getPotionDurationString(PotionEffect effect, float durationFactor)
    {
        if (effect.getIsPotionDurationMax())
        {
            return "**:**";
        }
        else
        {
            int i = MathHelper.floor((float)effect.getDuration() * durationFactor);
            return StringUtils.ticksToElapsedTime(i);
        }
    }

    protected Potion func_76404_a(double p_76404_1_)
    {
        this.field_76412_L = p_76404_1_;
        return this;
    }

    /**
     * Returns the color of the potion liquid.
     */
    public int getLiquidColor()
    {
        return this.liquidColor;
    }

    public Potion func_111184_a(IAttribute p_111184_1_, String p_111184_2_, double p_111184_3_, int p_111184_5_)
    {
        AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString(p_111184_2_), this.getName(), p_111184_3_, p_111184_5_);
        this.attributeModifierMap.put(p_111184_1_, attributemodifier);
        return this;
    }

    public Map<IAttribute, AttributeModifier> getAttributeModifierMap()
    {
        return this.attributeModifierMap;
    }

    public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier)
    {
        for (Entry<IAttribute, AttributeModifier> entry : this.attributeModifierMap.entrySet())
        {
            IAttributeInstance iattributeinstance = attributeMapIn.getAttributeInstance(entry.getKey());

            if (iattributeinstance != null)
            {
                iattributeinstance.removeModifier(entry.getValue());
            }
        }
    }

    public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier)
    {
        for (Entry<IAttribute, AttributeModifier> entry : this.attributeModifierMap.entrySet())
        {
            IAttributeInstance iattributeinstance = attributeMapIn.getAttributeInstance(entry.getKey());

            if (iattributeinstance != null)
            {
                AttributeModifier attributemodifier = entry.getValue();
                iattributeinstance.removeModifier(attributemodifier);
                iattributeinstance.applyModifier(new AttributeModifier(attributemodifier.getID(), this.getName() + " " + amplifier, this.getAttributeModifierAmount(amplifier, attributemodifier), attributemodifier.func_111169_c()));
            }
        }
    }

    public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier)
    {
        return modifier.getAmount() * (double)(amplifier + 1);
    }

    /**
     * Get if the potion is beneficial to the player. Beneficial potions are shown on the first row of the HUD
     */
    public boolean isBeneficial()
    {
        return this.field_188415_h;
    }

    public Potion func_188413_j()
    {
        this.field_188415_h = true;
        return this;
    }

    public static void func_188411_k()
    {
        field_188414_b.func_177775_a(1, new ResourceLocation("speed"), (new Potion(false, 8171462)).func_76390_b("effect.moveSpeed").func_76399_b(0, 0).func_111184_a(SharedMonsterAttributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635", 0.20000000298023224D, 2).func_188413_j());
        field_188414_b.func_177775_a(2, new ResourceLocation("slowness"), (new Potion(true, 5926017)).func_76390_b("effect.moveSlowdown").func_76399_b(1, 0).func_111184_a(SharedMonsterAttributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15000000596046448D, 2));
        field_188414_b.func_177775_a(3, new ResourceLocation("haste"), (new Potion(false, 14270531)).func_76390_b("effect.digSpeed").func_76399_b(2, 0).func_76404_a(1.5D).func_188413_j().func_111184_a(SharedMonsterAttributes.ATTACK_SPEED, "AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3", 0.10000000149011612D, 2));
        field_188414_b.func_177775_a(4, new ResourceLocation("mining_fatigue"), (new Potion(true, 4866583)).func_76390_b("effect.digSlowDown").func_76399_b(3, 0).func_111184_a(SharedMonsterAttributes.ATTACK_SPEED, "55FCED67-E92A-486E-9800-B47F202C4386", -0.10000000149011612D, 2));
        field_188414_b.func_177775_a(5, new ResourceLocation("strength"), (new PotionAttackDamage(false, 9643043, 3.0D)).func_76390_b("effect.damageBoost").func_76399_b(4, 0).func_111184_a(SharedMonsterAttributes.ATTACK_DAMAGE, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 0.0D, 0).func_188413_j());
        field_188414_b.func_177775_a(6, new ResourceLocation("instant_health"), (new PotionHealth(false, 16262179)).func_76390_b("effect.heal").func_188413_j());
        field_188414_b.func_177775_a(7, new ResourceLocation("instant_damage"), (new PotionHealth(true, 4393481)).func_76390_b("effect.harm").func_188413_j());
        field_188414_b.func_177775_a(8, new ResourceLocation("jump_boost"), (new Potion(false, 2293580)).func_76390_b("effect.jump").func_76399_b(2, 1).func_188413_j());
        field_188414_b.func_177775_a(9, new ResourceLocation("nausea"), (new Potion(true, 5578058)).func_76390_b("effect.confusion").func_76399_b(3, 1).func_76404_a(0.25D));
        field_188414_b.func_177775_a(10, new ResourceLocation("regeneration"), (new Potion(false, 13458603)).func_76390_b("effect.regeneration").func_76399_b(7, 0).func_76404_a(0.25D).func_188413_j());
        field_188414_b.func_177775_a(11, new ResourceLocation("resistance"), (new Potion(false, 10044730)).func_76390_b("effect.resistance").func_76399_b(6, 1).func_188413_j());
        field_188414_b.func_177775_a(12, new ResourceLocation("fire_resistance"), (new Potion(false, 14981690)).func_76390_b("effect.fireResistance").func_76399_b(7, 1).func_188413_j());
        field_188414_b.func_177775_a(13, new ResourceLocation("water_breathing"), (new Potion(false, 3035801)).func_76390_b("effect.waterBreathing").func_76399_b(0, 2).func_188413_j());
        field_188414_b.func_177775_a(14, new ResourceLocation("invisibility"), (new Potion(false, 8356754)).func_76390_b("effect.invisibility").func_76399_b(0, 1).func_188413_j());
        field_188414_b.func_177775_a(15, new ResourceLocation("blindness"), (new Potion(true, 2039587)).func_76390_b("effect.blindness").func_76399_b(5, 1).func_76404_a(0.25D));
        field_188414_b.func_177775_a(16, new ResourceLocation("night_vision"), (new Potion(false, 2039713)).func_76390_b("effect.nightVision").func_76399_b(4, 1).func_188413_j());
        field_188414_b.func_177775_a(17, new ResourceLocation("hunger"), (new Potion(true, 5797459)).func_76390_b("effect.hunger").func_76399_b(1, 1));
        field_188414_b.func_177775_a(18, new ResourceLocation("weakness"), (new PotionAttackDamage(true, 4738376, -4.0D)).func_76390_b("effect.weakness").func_76399_b(5, 0).func_111184_a(SharedMonsterAttributes.ATTACK_DAMAGE, "22653B89-116E-49DC-9B6B-9971489B5BE5", 0.0D, 0));
        field_188414_b.func_177775_a(19, new ResourceLocation("poison"), (new Potion(true, 5149489)).func_76390_b("effect.poison").func_76399_b(6, 0).func_76404_a(0.25D));
        field_188414_b.func_177775_a(20, new ResourceLocation("wither"), (new Potion(true, 3484199)).func_76390_b("effect.wither").func_76399_b(1, 2).func_76404_a(0.25D));
        field_188414_b.func_177775_a(21, new ResourceLocation("health_boost"), (new PotionHealthBoost(false, 16284963)).func_76390_b("effect.healthBoost").func_76399_b(7, 2).func_111184_a(SharedMonsterAttributes.MAX_HEALTH, "5D6F0BA2-1186-46AC-B896-C61C5CEE99CC", 4.0D, 0).func_188413_j());
        field_188414_b.func_177775_a(22, new ResourceLocation("absorption"), (new PotionAbsorption(false, 2445989)).func_76390_b("effect.absorption").func_76399_b(2, 2).func_188413_j());
        field_188414_b.func_177775_a(23, new ResourceLocation("saturation"), (new PotionHealth(false, 16262179)).func_76390_b("effect.saturation").func_188413_j());
        field_188414_b.func_177775_a(24, new ResourceLocation("glowing"), (new Potion(false, 9740385)).func_76390_b("effect.glowing").func_76399_b(4, 2));
        field_188414_b.func_177775_a(25, new ResourceLocation("levitation"), (new Potion(true, 13565951)).func_76390_b("effect.levitation").func_76399_b(3, 2));
        field_188414_b.func_177775_a(26, new ResourceLocation("luck"), (new Potion(false, 3381504)).func_76390_b("effect.luck").func_76399_b(5, 2).func_188413_j().func_111184_a(SharedMonsterAttributes.LUCK, "03C3C89D-7037-4B42-869F-B146BCB64D2E", 1.0D, 0));
        field_188414_b.func_177775_a(27, new ResourceLocation("unluck"), (new Potion(true, 12624973)).func_76390_b("effect.unluck").func_76399_b(6, 2).func_111184_a(SharedMonsterAttributes.LUCK, "CC5AF142-2BD2-4215-B636-2605AED11727", -1.0D, 0));
    }
}
