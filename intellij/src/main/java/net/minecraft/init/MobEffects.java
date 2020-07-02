package net.minecraft.init;

import javax.annotation.Nullable;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class MobEffects
{
    public static final Potion SPEED;
    public static final Potion SLOWNESS;
    public static final Potion HASTE;
    public static final Potion MINING_FATIGUE;
    public static final Potion STRENGTH;
    public static final Potion INSTANT_HEALTH;
    public static final Potion INSTANT_DAMAGE;
    public static final Potion JUMP_BOOST;
    public static final Potion NAUSEA;
    public static final Potion REGENERATION;
    public static final Potion RESISTANCE;
    public static final Potion FIRE_RESISTANCE;
    public static final Potion WATER_BREATHING;
    public static final Potion INVISIBILITY;
    public static final Potion BLINDNESS;
    public static final Potion NIGHT_VISION;
    public static final Potion HUNGER;
    public static final Potion WEAKNESS;
    public static final Potion POISON;
    public static final Potion WITHER;
    public static final Potion HEALTH_BOOST;
    public static final Potion ABSORPTION;
    public static final Potion SATURATION;
    public static final Potion GLOWING;
    public static final Potion LEVITATION;
    public static final Potion LUCK;
    public static final Potion UNLUCK;

    @Nullable
    private static Potion func_188422_a(String p_188422_0_)
    {
        Potion potion = Potion.field_188414_b.getOrDefault(new ResourceLocation(p_188422_0_));

        if (potion == null)
        {
            throw new IllegalStateException("Invalid MobEffect requested: " + p_188422_0_);
        }
        else
        {
            return potion;
        }
    }

    static
    {
        if (!Bootstrap.func_179869_a())
        {
            throw new RuntimeException("Accessed MobEffects before Bootstrap!");
        }
        else
        {
            SPEED = func_188422_a("speed");
            SLOWNESS = func_188422_a("slowness");
            HASTE = func_188422_a("haste");
            MINING_FATIGUE = func_188422_a("mining_fatigue");
            STRENGTH = func_188422_a("strength");
            INSTANT_HEALTH = func_188422_a("instant_health");
            INSTANT_DAMAGE = func_188422_a("instant_damage");
            JUMP_BOOST = func_188422_a("jump_boost");
            NAUSEA = func_188422_a("nausea");
            REGENERATION = func_188422_a("regeneration");
            RESISTANCE = func_188422_a("resistance");
            FIRE_RESISTANCE = func_188422_a("fire_resistance");
            WATER_BREATHING = func_188422_a("water_breathing");
            INVISIBILITY = func_188422_a("invisibility");
            BLINDNESS = func_188422_a("blindness");
            NIGHT_VISION = func_188422_a("night_vision");
            HUNGER = func_188422_a("hunger");
            WEAKNESS = func_188422_a("weakness");
            POISON = func_188422_a("poison");
            WITHER = func_188422_a("wither");
            HEALTH_BOOST = func_188422_a("health_boost");
            ABSORPTION = func_188422_a("absorption");
            SATURATION = func_188422_a("saturation");
            GLOWING = func_188422_a("glowing");
            LEVITATION = func_188422_a("levitation");
            LUCK = func_188422_a("luck");
            UNLUCK = func_188422_a("unluck");
        }
    }
}
