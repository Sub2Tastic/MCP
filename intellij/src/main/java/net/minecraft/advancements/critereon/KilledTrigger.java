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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class KilledTrigger implements ICriterionTrigger<KilledTrigger.Instance>
{
    private final Map<PlayerAdvancements, KilledTrigger.Listeners> field_192213_a = Maps.<PlayerAdvancements, KilledTrigger.Listeners>newHashMap();
    private final ResourceLocation id;

    public KilledTrigger(ResourceLocation id)
    {
        this.id = id;
    }

    public ResourceLocation getId()
    {
        return this.id;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<KilledTrigger.Instance> listener)
    {
        KilledTrigger.Listeners killedtrigger$listeners = this.field_192213_a.get(playerAdvancementsIn);

        if (killedtrigger$listeners == null)
        {
            killedtrigger$listeners = new KilledTrigger.Listeners(playerAdvancementsIn);
            this.field_192213_a.put(playerAdvancementsIn, killedtrigger$listeners);
        }

        killedtrigger$listeners.func_192504_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<KilledTrigger.Instance> listener)
    {
        KilledTrigger.Listeners killedtrigger$listeners = this.field_192213_a.get(playerAdvancementsIn);

        if (killedtrigger$listeners != null)
        {
            killedtrigger$listeners.func_192501_b(listener);

            if (killedtrigger$listeners.func_192502_a())
            {
                this.field_192213_a.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_192213_a.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public KilledTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        return new KilledTrigger.Instance(this.id, EntityPredicate.deserialize(json.get("entity")), DamageSourcePredicate.deserialize(json.get("killing_blow")));
    }

    public void trigger(EntityPlayerMP player, Entity entity, DamageSource source)
    {
        KilledTrigger.Listeners killedtrigger$listeners = this.field_192213_a.get(player.getAdvancements());

        if (killedtrigger$listeners != null)
        {
            killedtrigger$listeners.func_192503_a(player, entity, source);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final EntityPredicate entity;
        private final DamageSourcePredicate killingBlow;

        public Instance(ResourceLocation criterionIn, EntityPredicate entity, DamageSourcePredicate killingBlow)
        {
            super(criterionIn);
            this.entity = entity;
            this.killingBlow = killingBlow;
        }

        public boolean test(EntityPlayerMP player, Entity entity, DamageSource source)
        {
            return !this.killingBlow.test(player, source) ? false : this.entity.test(player, entity);
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_192505_a;
        private final Set<ICriterionTrigger.Listener<KilledTrigger.Instance>> field_192506_b = Sets.<ICriterionTrigger.Listener<KilledTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47455_1_)
        {
            this.field_192505_a = p_i47455_1_;
        }

        public boolean func_192502_a()
        {
            return this.field_192506_b.isEmpty();
        }

        public void func_192504_a(ICriterionTrigger.Listener<KilledTrigger.Instance> p_192504_1_)
        {
            this.field_192506_b.add(p_192504_1_);
        }

        public void func_192501_b(ICriterionTrigger.Listener<KilledTrigger.Instance> p_192501_1_)
        {
            this.field_192506_b.remove(p_192501_1_);
        }

        public void func_192503_a(EntityPlayerMP p_192503_1_, Entity p_192503_2_, DamageSource p_192503_3_)
        {
            List<ICriterionTrigger.Listener<KilledTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<KilledTrigger.Instance> listener : this.field_192506_b)
            {
                if (((KilledTrigger.Instance)listener.getCriterionInstance()).test(p_192503_1_, p_192503_2_, p_192503_3_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<KilledTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<KilledTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_192505_a);
                }
            }
        }
    }
}
