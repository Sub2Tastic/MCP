package net.minecraft.init;

import javax.annotation.Nullable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;

public class Enchantments
{
    public static final Enchantment PROTECTION = func_185295_a("protection");
    public static final Enchantment FIRE_PROTECTION = func_185295_a("fire_protection");
    public static final Enchantment FEATHER_FALLING = func_185295_a("feather_falling");
    public static final Enchantment BLAST_PROTECTION = func_185295_a("blast_protection");
    public static final Enchantment PROJECTILE_PROTECTION = func_185295_a("projectile_protection");
    public static final Enchantment RESPIRATION = func_185295_a("respiration");
    public static final Enchantment AQUA_AFFINITY = func_185295_a("aqua_affinity");
    public static final Enchantment THORNS = func_185295_a("thorns");
    public static final Enchantment DEPTH_STRIDER = func_185295_a("depth_strider");
    public static final Enchantment FROST_WALKER = func_185295_a("frost_walker");
    public static final Enchantment BINDING_CURSE = func_185295_a("binding_curse");
    public static final Enchantment SHARPNESS = func_185295_a("sharpness");
    public static final Enchantment SMITE = func_185295_a("smite");
    public static final Enchantment BANE_OF_ARTHROPODS = func_185295_a("bane_of_arthropods");
    public static final Enchantment KNOCKBACK = func_185295_a("knockback");
    public static final Enchantment FIRE_ASPECT = func_185295_a("fire_aspect");
    public static final Enchantment LOOTING = func_185295_a("looting");
    public static final Enchantment SWEEPING = func_185295_a("sweeping");
    public static final Enchantment EFFICIENCY = func_185295_a("efficiency");
    public static final Enchantment SILK_TOUCH = func_185295_a("silk_touch");
    public static final Enchantment UNBREAKING = func_185295_a("unbreaking");
    public static final Enchantment FORTUNE = func_185295_a("fortune");
    public static final Enchantment POWER = func_185295_a("power");
    public static final Enchantment PUNCH = func_185295_a("punch");
    public static final Enchantment FLAME = func_185295_a("flame");
    public static final Enchantment INFINITY = func_185295_a("infinity");
    public static final Enchantment LUCK_OF_THE_SEA = func_185295_a("luck_of_the_sea");
    public static final Enchantment LURE = func_185295_a("lure");
    public static final Enchantment MENDING = func_185295_a("mending");
    public static final Enchantment VANISHING_CURSE = func_185295_a("vanishing_curse");

    @Nullable
    private static Enchantment func_185295_a(String p_185295_0_)
    {
        Enchantment enchantment = Enchantment.field_185264_b.getOrDefault(new ResourceLocation(p_185295_0_));

        if (enchantment == null)
        {
            throw new IllegalStateException("Invalid Enchantment requested: " + p_185295_0_);
        }
        else
        {
            return enchantment;
        }
    }

    static
    {
        if (!Bootstrap.func_179869_a())
        {
            throw new RuntimeException("Accessed Enchantments before Bootstrap!");
        }
    }
}
