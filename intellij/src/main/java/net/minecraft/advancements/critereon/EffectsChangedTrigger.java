package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class EffectsChangedTrigger implements ICriterionTrigger<EffectsChangedTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("effects_changed");
    private final Map<PlayerAdvancements, EffectsChangedTrigger.Listeners> field_193155_b = Maps.<PlayerAdvancements, EffectsChangedTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<EffectsChangedTrigger.Instance> listener)
    {
        EffectsChangedTrigger.Listeners effectschangedtrigger$listeners = this.field_193155_b.get(playerAdvancementsIn);

        if (effectschangedtrigger$listeners == null)
        {
            effectschangedtrigger$listeners = new EffectsChangedTrigger.Listeners(playerAdvancementsIn);
            this.field_193155_b.put(playerAdvancementsIn, effectschangedtrigger$listeners);
        }

        effectschangedtrigger$listeners.func_193431_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<EffectsChangedTrigger.Instance> listener)
    {
        EffectsChangedTrigger.Listeners effectschangedtrigger$listeners = this.field_193155_b.get(playerAdvancementsIn);

        if (effectschangedtrigger$listeners != null)
        {
            effectschangedtrigger$listeners.func_193429_b(listener);

            if (effectschangedtrigger$listeners.func_193430_a())
            {
                this.field_193155_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_193155_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public EffectsChangedTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        MobEffectsPredicate mobeffectspredicate = MobEffectsPredicate.deserialize(json.get("effects"));
        return new EffectsChangedTrigger.Instance(mobeffectspredicate);
    }

    public void trigger(EntityPlayerMP player)
    {
        EffectsChangedTrigger.Listeners effectschangedtrigger$listeners = this.field_193155_b.get(player.getAdvancements());

        if (effectschangedtrigger$listeners != null)
        {
            effectschangedtrigger$listeners.func_193432_a(player);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final MobEffectsPredicate effects;

        public Instance(MobEffectsPredicate effects)
        {
            super(EffectsChangedTrigger.ID);
            this.effects = effects;
        }

        public boolean test(EntityPlayerMP player)
        {
            return this.effects.test(player);
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_193433_a;
        private final Set<ICriterionTrigger.Listener<EffectsChangedTrigger.Instance>> field_193434_b = Sets.<ICriterionTrigger.Listener<EffectsChangedTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47546_1_)
        {
            this.field_193433_a = p_i47546_1_;
        }

        public boolean func_193430_a()
        {
            return this.field_193434_b.isEmpty();
        }

        public void func_193431_a(ICriterionTrigger.Listener<EffectsChangedTrigger.Instance> p_193431_1_)
        {
            this.field_193434_b.add(p_193431_1_);
        }

        public void func_193429_b(ICriterionTrigger.Listener<EffectsChangedTrigger.Instance> p_193429_1_)
        {
            this.field_193434_b.remove(p_193429_1_);
        }

        public void func_193432_a(EntityPlayerMP p_193432_1_)
        {
            List<ICriterionTrigger.Listener<EffectsChangedTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<EffectsChangedTrigger.Instance> listener : this.field_193434_b)
            {
                if (((EffectsChangedTrigger.Instance)listener.getCriterionInstance()).test(p_193432_1_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<EffectsChangedTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<EffectsChangedTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_193433_a);
                }
            }
        }
    }
}
