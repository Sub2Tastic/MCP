package net.minecraft.advancements.critereon;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class MobEffectsPredicate
{
    public static final MobEffectsPredicate ANY = new MobEffectsPredicate(Collections.emptyMap());
    private final Map<Potion, MobEffectsPredicate.InstancePredicate> effects;

    public MobEffectsPredicate(Map<Potion, MobEffectsPredicate.InstancePredicate> effects)
    {
        this.effects = effects;
    }

    public boolean test(Entity entityIn)
    {
        if (this == ANY)
        {
            return true;
        }
        else
        {
            return entityIn instanceof EntityLivingBase ? this.test(((EntityLivingBase)entityIn).getActivePotionMap()) : false;
        }
    }

    public boolean test(EntityLivingBase entityIn)
    {
        return this == ANY ? true : this.test(entityIn.getActivePotionMap());
    }

    public boolean test(Map<Potion, PotionEffect> potions)
    {
        if (this == ANY)
        {
            return true;
        }
        else
        {
            for (Entry<Potion, MobEffectsPredicate.InstancePredicate> entry : this.effects.entrySet())
            {
                PotionEffect potioneffect = potions.get(entry.getKey());

                if (!((MobEffectsPredicate.InstancePredicate)entry.getValue()).test(potioneffect))
                {
                    return false;
                }
            }

            return true;
        }
    }

    public static MobEffectsPredicate deserialize(@Nullable JsonElement element)
    {
        if (element != null && !element.isJsonNull())
        {
            JsonObject jsonobject = JsonUtils.getJsonObject(element, "effects");
            Map<Potion, MobEffectsPredicate.InstancePredicate> map = Maps.<Potion, MobEffectsPredicate.InstancePredicate>newHashMap();

            for (Entry<String, JsonElement> entry : jsonobject.entrySet())
            {
                ResourceLocation resourcelocation = new ResourceLocation(entry.getKey());
                Potion potion = Potion.field_188414_b.getOrDefault(resourcelocation);

                if (potion == null)
                {
                    throw new JsonSyntaxException("Unknown effect '" + resourcelocation + "'");
                }

                MobEffectsPredicate.InstancePredicate mobeffectspredicate$instancepredicate = MobEffectsPredicate.InstancePredicate.deserialize(JsonUtils.getJsonObject(entry.getValue(), entry.getKey()));
                map.put(potion, mobeffectspredicate$instancepredicate);
            }

            return new MobEffectsPredicate(map);
        }
        else
        {
            return ANY;
        }
    }

    public static class InstancePredicate
    {
        private final MinMaxBounds amplifier;
        private final MinMaxBounds duration;
        @Nullable
        private final Boolean ambient;
        @Nullable
        private final Boolean visible;

        public InstancePredicate(MinMaxBounds p_i47497_1_, MinMaxBounds p_i47497_2_, @Nullable Boolean p_i47497_3_, @Nullable Boolean p_i47497_4_)
        {
            this.amplifier = p_i47497_1_;
            this.duration = p_i47497_2_;
            this.ambient = p_i47497_3_;
            this.visible = p_i47497_4_;
        }

        public boolean test(@Nullable PotionEffect effect)
        {
            if (effect == null)
            {
                return false;
            }
            else if (!this.amplifier.func_192514_a((float)effect.getAmplifier()))
            {
                return false;
            }
            else if (!this.duration.func_192514_a((float)effect.getDuration()))
            {
                return false;
            }
            else if (this.ambient != null && this.ambient.booleanValue() != effect.isAmbient())
            {
                return false;
            }
            else
            {
                return this.visible == null || this.visible.booleanValue() == effect.doesShowParticles();
            }
        }

        public static MobEffectsPredicate.InstancePredicate deserialize(JsonObject object)
        {
            MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(object.get("amplifier"));
            MinMaxBounds minmaxbounds1 = MinMaxBounds.func_192515_a(object.get("duration"));
            Boolean obool = object.has("ambient") ? JsonUtils.getBoolean(object, "ambient") : null;
            Boolean obool1 = object.has("visible") ? JsonUtils.getBoolean(object, "visible") : null;
            return new MobEffectsPredicate.InstancePredicate(minmaxbounds, minmaxbounds1, obool, obool1);
        }
    }
}
