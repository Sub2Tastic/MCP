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
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class TameAnimalTrigger implements ICriterionTrigger<TameAnimalTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("tame_animal");
    private final Map<PlayerAdvancements, TameAnimalTrigger.Listeners> field_193180_b = Maps.<PlayerAdvancements, TameAnimalTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<TameAnimalTrigger.Instance> listener)
    {
        TameAnimalTrigger.Listeners tameanimaltrigger$listeners = this.field_193180_b.get(playerAdvancementsIn);

        if (tameanimaltrigger$listeners == null)
        {
            tameanimaltrigger$listeners = new TameAnimalTrigger.Listeners(playerAdvancementsIn);
            this.field_193180_b.put(playerAdvancementsIn, tameanimaltrigger$listeners);
        }

        tameanimaltrigger$listeners.func_193496_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<TameAnimalTrigger.Instance> listener)
    {
        TameAnimalTrigger.Listeners tameanimaltrigger$listeners = this.field_193180_b.get(playerAdvancementsIn);

        if (tameanimaltrigger$listeners != null)
        {
            tameanimaltrigger$listeners.func_193494_b(listener);

            if (tameanimaltrigger$listeners.func_193495_a())
            {
                this.field_193180_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_193180_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public TameAnimalTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        EntityPredicate entitypredicate = EntityPredicate.deserialize(json.get("entity"));
        return new TameAnimalTrigger.Instance(entitypredicate);
    }

    public void trigger(EntityPlayerMP player, EntityAnimal entity)
    {
        TameAnimalTrigger.Listeners tameanimaltrigger$listeners = this.field_193180_b.get(player.getAdvancements());

        if (tameanimaltrigger$listeners != null)
        {
            tameanimaltrigger$listeners.func_193497_a(player, entity);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final EntityPredicate entity;

        public Instance(EntityPredicate entity)
        {
            super(TameAnimalTrigger.ID);
            this.entity = entity;
        }

        public boolean test(EntityPlayerMP player, EntityAnimal entity)
        {
            return this.entity.test(player, entity);
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_193498_a;
        private final Set<ICriterionTrigger.Listener<TameAnimalTrigger.Instance>> field_193499_b = Sets.<ICriterionTrigger.Listener<TameAnimalTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47514_1_)
        {
            this.field_193498_a = p_i47514_1_;
        }

        public boolean func_193495_a()
        {
            return this.field_193499_b.isEmpty();
        }

        public void func_193496_a(ICriterionTrigger.Listener<TameAnimalTrigger.Instance> p_193496_1_)
        {
            this.field_193499_b.add(p_193496_1_);
        }

        public void func_193494_b(ICriterionTrigger.Listener<TameAnimalTrigger.Instance> p_193494_1_)
        {
            this.field_193499_b.remove(p_193494_1_);
        }

        public void func_193497_a(EntityPlayerMP p_193497_1_, EntityAnimal p_193497_2_)
        {
            List<ICriterionTrigger.Listener<TameAnimalTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<TameAnimalTrigger.Instance> listener : this.field_193499_b)
            {
                if (((TameAnimalTrigger.Instance)listener.getCriterionInstance()).test(p_193497_1_, p_193497_2_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<TameAnimalTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<TameAnimalTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_193498_a);
                }
            }
        }
    }
}
