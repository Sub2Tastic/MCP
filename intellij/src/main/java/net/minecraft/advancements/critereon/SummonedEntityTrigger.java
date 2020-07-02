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
import net.minecraft.util.ResourceLocation;

public class SummonedEntityTrigger implements ICriterionTrigger<SummonedEntityTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("summoned_entity");
    private final Map<PlayerAdvancements, SummonedEntityTrigger.Listeners> field_192233_b = Maps.<PlayerAdvancements, SummonedEntityTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<SummonedEntityTrigger.Instance> listener)
    {
        SummonedEntityTrigger.Listeners summonedentitytrigger$listeners = this.field_192233_b.get(playerAdvancementsIn);

        if (summonedentitytrigger$listeners == null)
        {
            summonedentitytrigger$listeners = new SummonedEntityTrigger.Listeners(playerAdvancementsIn);
            this.field_192233_b.put(playerAdvancementsIn, summonedentitytrigger$listeners);
        }

        summonedentitytrigger$listeners.func_192534_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<SummonedEntityTrigger.Instance> listener)
    {
        SummonedEntityTrigger.Listeners summonedentitytrigger$listeners = this.field_192233_b.get(playerAdvancementsIn);

        if (summonedentitytrigger$listeners != null)
        {
            summonedentitytrigger$listeners.func_192531_b(listener);

            if (summonedentitytrigger$listeners.func_192532_a())
            {
                this.field_192233_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_192233_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public SummonedEntityTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        EntityPredicate entitypredicate = EntityPredicate.deserialize(json.get("entity"));
        return new SummonedEntityTrigger.Instance(entitypredicate);
    }

    public void trigger(EntityPlayerMP player, Entity entity)
    {
        SummonedEntityTrigger.Listeners summonedentitytrigger$listeners = this.field_192233_b.get(player.getAdvancements());

        if (summonedentitytrigger$listeners != null)
        {
            summonedentitytrigger$listeners.func_192533_a(player, entity);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final EntityPredicate entity;

        public Instance(EntityPredicate entity)
        {
            super(SummonedEntityTrigger.ID);
            this.entity = entity;
        }

        public boolean test(EntityPlayerMP player, Entity entity)
        {
            return this.entity.test(player, entity);
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_192535_a;
        private final Set<ICriterionTrigger.Listener<SummonedEntityTrigger.Instance>> field_192536_b = Sets.<ICriterionTrigger.Listener<SummonedEntityTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47372_1_)
        {
            this.field_192535_a = p_i47372_1_;
        }

        public boolean func_192532_a()
        {
            return this.field_192536_b.isEmpty();
        }

        public void func_192534_a(ICriterionTrigger.Listener<SummonedEntityTrigger.Instance> p_192534_1_)
        {
            this.field_192536_b.add(p_192534_1_);
        }

        public void func_192531_b(ICriterionTrigger.Listener<SummonedEntityTrigger.Instance> p_192531_1_)
        {
            this.field_192536_b.remove(p_192531_1_);
        }

        public void func_192533_a(EntityPlayerMP p_192533_1_, Entity p_192533_2_)
        {
            List<ICriterionTrigger.Listener<SummonedEntityTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<SummonedEntityTrigger.Instance> listener : this.field_192536_b)
            {
                if (((SummonedEntityTrigger.Instance)listener.getCriterionInstance()).test(p_192533_1_, p_192533_2_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<SummonedEntityTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<SummonedEntityTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_192535_a);
                }
            }
        }
    }
}
