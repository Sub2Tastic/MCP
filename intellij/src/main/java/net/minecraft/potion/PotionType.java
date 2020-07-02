package net.minecraft.potion;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;

public class PotionType
{
    private static final ResourceLocation field_185177_b = new ResourceLocation("empty");
    public static final RegistryNamespacedDefaultedByKey<ResourceLocation, PotionType> field_185176_a = new RegistryNamespacedDefaultedByKey<ResourceLocation, PotionType>(field_185177_b);
    private static int field_185178_c;
    private final String baseName;
    private final ImmutableList<PotionEffect> effects;

    @Nullable
    public static PotionType getPotionTypeForName(String name)
    {
        return field_185176_a.getOrDefault(new ResourceLocation(name));
    }

    public PotionType(PotionEffect... effectsIn)
    {
        this((String)null, effectsIn);
    }

    public PotionType(@Nullable String baseNameIn, PotionEffect... effectsIn)
    {
        this.baseName = baseNameIn;
        this.effects = ImmutableList.copyOf(effectsIn);
    }

    /**
     * Gets the name of this PotionType with a prefix (such as "Splash" or "Lingering") prepended
     */
    public String getNamePrefixed(String prefix)
    {
        return this.baseName == null ? prefix + ((ResourceLocation)field_185176_a.getKey(this)).getPath() : prefix + this.baseName;
    }

    public List<PotionEffect> getEffects()
    {
        return this.effects;
    }

    public static void func_185175_b()
    {
        func_185173_a("empty", new PotionType(new PotionEffect[0]));
        func_185173_a("water", new PotionType(new PotionEffect[0]));
        func_185173_a("mundane", new PotionType(new PotionEffect[0]));
        func_185173_a("thick", new PotionType(new PotionEffect[0]));
        func_185173_a("awkward", new PotionType(new PotionEffect[0]));
        func_185173_a("night_vision", new PotionType(new PotionEffect[] {new PotionEffect(MobEffects.NIGHT_VISION, 3600)}));
        func_185173_a("long_night_vision", new PotionType("night_vision", new PotionEffect[] {new PotionEffect(MobEffects.NIGHT_VISION, 9600)}));
        func_185173_a("invisibility", new PotionType(new PotionEffect[] {new PotionEffect(MobEffects.INVISIBILITY, 3600)}));
        func_185173_a("long_invisibility", new PotionType("invisibility", new PotionEffect[] {new PotionEffect(MobEffects.INVISIBILITY, 9600)}));
        func_185173_a("leaping", new PotionType(new PotionEffect[] {new PotionEffect(MobEffects.JUMP_BOOST, 3600)}));
        func_185173_a("long_leaping", new PotionType("leaping", new PotionEffect[] {new PotionEffect(MobEffects.JUMP_BOOST, 9600)}));
        func_185173_a("strong_leaping", new PotionType("leaping", new PotionEffect[] {new PotionEffect(MobEffects.JUMP_BOOST, 1800, 1)}));
        func_185173_a("fire_resistance", new PotionType(new PotionEffect[] {new PotionEffect(MobEffects.FIRE_RESISTANCE, 3600)}));
        func_185173_a("long_fire_resistance", new PotionType("fire_resistance", new PotionEffect[] {new PotionEffect(MobEffects.FIRE_RESISTANCE, 9600)}));
        func_185173_a("swiftness", new PotionType(new PotionEffect[] {new PotionEffect(MobEffects.SPEED, 3600)}));
        func_185173_a("long_swiftness", new PotionType("swiftness", new PotionEffect[] {new PotionEffect(MobEffects.SPEED, 9600)}));
        func_185173_a("strong_swiftness", new PotionType("swiftness", new PotionEffect[] {new PotionEffect(MobEffects.SPEED, 1800, 1)}));
        func_185173_a("slowness", new PotionType(new PotionEffect[] {new PotionEffect(MobEffects.SLOWNESS, 1800)}));
        func_185173_a("long_slowness", new PotionType("slowness", new PotionEffect[] {new PotionEffect(MobEffects.SLOWNESS, 4800)}));
        func_185173_a("water_breathing", new PotionType(new PotionEffect[] {new PotionEffect(MobEffects.WATER_BREATHING, 3600)}));
        func_185173_a("long_water_breathing", new PotionType("water_breathing", new PotionEffect[] {new PotionEffect(MobEffects.WATER_BREATHING, 9600)}));
        func_185173_a("healing", new PotionType(new PotionEffect[] {new PotionEffect(MobEffects.INSTANT_HEALTH, 1)}));
        func_185173_a("strong_healing", new PotionType("healing", new PotionEffect[] {new PotionEffect(MobEffects.INSTANT_HEALTH, 1, 1)}));
        func_185173_a("harming", new PotionType(new PotionEffect[] {new PotionEffect(MobEffects.INSTANT_DAMAGE, 1)}));
        func_185173_a("strong_harming", new PotionType("harming", new PotionEffect[] {new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 1)}));
        func_185173_a("poison", new PotionType(new PotionEffect[] {new PotionEffect(MobEffects.POISON, 900)}));
        func_185173_a("long_poison", new PotionType("poison", new PotionEffect[] {new PotionEffect(MobEffects.POISON, 1800)}));
        func_185173_a("strong_poison", new PotionType("poison", new PotionEffect[] {new PotionEffect(MobEffects.POISON, 432, 1)}));
        func_185173_a("regeneration", new PotionType(new PotionEffect[] {new PotionEffect(MobEffects.REGENERATION, 900)}));
        func_185173_a("long_regeneration", new PotionType("regeneration", new PotionEffect[] {new PotionEffect(MobEffects.REGENERATION, 1800)}));
        func_185173_a("strong_regeneration", new PotionType("regeneration", new PotionEffect[] {new PotionEffect(MobEffects.REGENERATION, 450, 1)}));
        func_185173_a("strength", new PotionType(new PotionEffect[] {new PotionEffect(MobEffects.STRENGTH, 3600)}));
        func_185173_a("long_strength", new PotionType("strength", new PotionEffect[] {new PotionEffect(MobEffects.STRENGTH, 9600)}));
        func_185173_a("strong_strength", new PotionType("strength", new PotionEffect[] {new PotionEffect(MobEffects.STRENGTH, 1800, 1)}));
        func_185173_a("weakness", new PotionType(new PotionEffect[] {new PotionEffect(MobEffects.WEAKNESS, 1800)}));
        func_185173_a("long_weakness", new PotionType("weakness", new PotionEffect[] {new PotionEffect(MobEffects.WEAKNESS, 4800)}));
        func_185173_a("luck", new PotionType("luck", new PotionEffect[] {new PotionEffect(MobEffects.LUCK, 6000)}));
        field_185176_a.func_177776_a();
    }

    protected static void func_185173_a(String p_185173_0_, PotionType p_185173_1_)
    {
        field_185176_a.func_177775_a(field_185178_c++, new ResourceLocation(p_185173_0_), p_185173_1_);
    }

    public boolean hasInstantEffect()
    {
        if (!this.effects.isEmpty())
        {
            UnmodifiableIterator unmodifiableiterator = this.effects.iterator();

            while (unmodifiableiterator.hasNext())
            {
                PotionEffect potioneffect = (PotionEffect)unmodifiableiterator.next();

                if (potioneffect.getPotion().isInstant())
                {
                    return true;
                }
            }
        }

        return false;
    }
}
