package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class TickTrigger implements ICriterionTrigger<TickTrigger.Instance>
{
    public static final ResourceLocation ID = new ResourceLocation("tick");
    private final Map<PlayerAdvancements, TickTrigger.Listeners> field_193184_b = Maps.<PlayerAdvancements, TickTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<TickTrigger.Instance> listener)
    {
        TickTrigger.Listeners ticktrigger$listeners = this.field_193184_b.get(playerAdvancementsIn);

        if (ticktrigger$listeners == null)
        {
            ticktrigger$listeners = new TickTrigger.Listeners(playerAdvancementsIn);
            this.field_193184_b.put(playerAdvancementsIn, ticktrigger$listeners);
        }

        ticktrigger$listeners.func_193502_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<TickTrigger.Instance> listener)
    {
        TickTrigger.Listeners ticktrigger$listeners = this.field_193184_b.get(playerAdvancementsIn);

        if (ticktrigger$listeners != null)
        {
            ticktrigger$listeners.func_193500_b(listener);

            if (ticktrigger$listeners.func_193501_a())
            {
                this.field_193184_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_193184_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public TickTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        return new TickTrigger.Instance();
    }

    public void trigger(EntityPlayerMP player)
    {
        TickTrigger.Listeners ticktrigger$listeners = this.field_193184_b.get(player.getAdvancements());

        if (ticktrigger$listeners != null)
        {
            ticktrigger$listeners.func_193503_b();
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        public Instance()
        {
            super(TickTrigger.ID);
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_193504_a;
        private final Set<ICriterionTrigger.Listener<TickTrigger.Instance>> field_193505_b = Sets.<ICriterionTrigger.Listener<TickTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47496_1_)
        {
            this.field_193504_a = p_i47496_1_;
        }

        public boolean func_193501_a()
        {
            return this.field_193505_b.isEmpty();
        }

        public void func_193502_a(ICriterionTrigger.Listener<TickTrigger.Instance> p_193502_1_)
        {
            this.field_193505_b.add(p_193502_1_);
        }

        public void func_193500_b(ICriterionTrigger.Listener<TickTrigger.Instance> p_193500_1_)
        {
            this.field_193505_b.remove(p_193500_1_);
        }

        public void func_193503_b()
        {
            for (ICriterionTrigger.Listener<TickTrigger.Instance> listener : Lists.newArrayList(this.field_193505_b))
            {
                listener.grantCriterion(this.field_193504_a);
            }
        }
    }
}
