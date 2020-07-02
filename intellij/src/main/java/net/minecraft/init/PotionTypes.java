package net.minecraft.init;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;

public class PotionTypes
{
    private static final Set<PotionType> field_185228_K;
    public static final PotionType EMPTY;
    public static final PotionType WATER;
    public static final PotionType MUNDANE;
    public static final PotionType THICK;
    public static final PotionType AWKWARD;
    public static final PotionType NIGHT_VISION;
    public static final PotionType LONG_NIGHT_VISION;
    public static final PotionType INVISIBILITY;
    public static final PotionType LONG_INVISIBILITY;
    public static final PotionType LEAPING;
    public static final PotionType LONG_LEAPING;
    public static final PotionType STRONG_LEAPING;
    public static final PotionType FIRE_RESISTANCE;
    public static final PotionType LONG_FIRE_RESISTANCE;
    public static final PotionType SWIFTNESS;
    public static final PotionType LONG_SWIFTNESS;
    public static final PotionType STRONG_SWIFTNESS;
    public static final PotionType SLOWNESS;
    public static final PotionType LONG_SLOWNESS;
    public static final PotionType WATER_BREATHING;
    public static final PotionType LONG_WATER_BREATHING;
    public static final PotionType HEALING;
    public static final PotionType STRONG_HEALING;
    public static final PotionType HARMING;
    public static final PotionType STRONG_HARMING;
    public static final PotionType POISON;
    public static final PotionType LONG_POISON;
    public static final PotionType STRONG_POISON;
    public static final PotionType REGENERATION;
    public static final PotionType LONG_REGENERATION;
    public static final PotionType STRONG_REGENERATION;
    public static final PotionType STRENGTH;
    public static final PotionType LONG_STRENGTH;
    public static final PotionType STRONG_STRENGTH;
    public static final PotionType WEAKNESS;
    public static final PotionType LONG_WEAKNESS;

    private static PotionType func_185217_a(String p_185217_0_)
    {
        PotionType potiontype = PotionType.field_185176_a.getOrDefault(new ResourceLocation(p_185217_0_));

        if (!field_185228_K.add(potiontype))
        {
            throw new IllegalStateException("Invalid Potion requested: " + p_185217_0_);
        }
        else
        {
            return potiontype;
        }
    }

    static
    {
        if (!Bootstrap.func_179869_a())
        {
            throw new RuntimeException("Accessed Potions before Bootstrap!");
        }
        else
        {
            field_185228_K = Sets.<PotionType>newHashSet();
            EMPTY = func_185217_a("empty");
            WATER = func_185217_a("water");
            MUNDANE = func_185217_a("mundane");
            THICK = func_185217_a("thick");
            AWKWARD = func_185217_a("awkward");
            NIGHT_VISION = func_185217_a("night_vision");
            LONG_NIGHT_VISION = func_185217_a("long_night_vision");
            INVISIBILITY = func_185217_a("invisibility");
            LONG_INVISIBILITY = func_185217_a("long_invisibility");
            LEAPING = func_185217_a("leaping");
            LONG_LEAPING = func_185217_a("long_leaping");
            STRONG_LEAPING = func_185217_a("strong_leaping");
            FIRE_RESISTANCE = func_185217_a("fire_resistance");
            LONG_FIRE_RESISTANCE = func_185217_a("long_fire_resistance");
            SWIFTNESS = func_185217_a("swiftness");
            LONG_SWIFTNESS = func_185217_a("long_swiftness");
            STRONG_SWIFTNESS = func_185217_a("strong_swiftness");
            SLOWNESS = func_185217_a("slowness");
            LONG_SLOWNESS = func_185217_a("long_slowness");
            WATER_BREATHING = func_185217_a("water_breathing");
            LONG_WATER_BREATHING = func_185217_a("long_water_breathing");
            HEALING = func_185217_a("healing");
            STRONG_HEALING = func_185217_a("strong_healing");
            HARMING = func_185217_a("harming");
            STRONG_HARMING = func_185217_a("strong_harming");
            POISON = func_185217_a("poison");
            LONG_POISON = func_185217_a("long_poison");
            STRONG_POISON = func_185217_a("strong_poison");
            REGENERATION = func_185217_a("regeneration");
            LONG_REGENERATION = func_185217_a("long_regeneration");
            STRONG_REGENERATION = func_185217_a("strong_regeneration");
            STRENGTH = func_185217_a("strength");
            LONG_STRENGTH = func_185217_a("long_strength");
            STRONG_STRENGTH = func_185217_a("strong_strength");
            WEAKNESS = func_185217_a("weakness");
            LONG_WEAKNESS = func_185217_a("long_weakness");
            field_185228_K.clear();
        }
    }
}
