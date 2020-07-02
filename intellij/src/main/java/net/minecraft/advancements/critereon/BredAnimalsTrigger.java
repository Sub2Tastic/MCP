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
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class BredAnimalsTrigger implements ICriterionTrigger<BredAnimalsTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("bred_animals");
    private final Map<PlayerAdvancements, BredAnimalsTrigger.Listeners> field_192172_b = Maps.<PlayerAdvancements, BredAnimalsTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<BredAnimalsTrigger.Instance> listener)
    {
        BredAnimalsTrigger.Listeners bredanimalstrigger$listeners = this.field_192172_b.get(playerAdvancementsIn);

        if (bredanimalstrigger$listeners == null)
        {
            bredanimalstrigger$listeners = new BredAnimalsTrigger.Listeners(playerAdvancementsIn);
            this.field_192172_b.put(playerAdvancementsIn, bredanimalstrigger$listeners);
        }

        bredanimalstrigger$listeners.func_192343_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<BredAnimalsTrigger.Instance> listener)
    {
        BredAnimalsTrigger.Listeners bredanimalstrigger$listeners = this.field_192172_b.get(playerAdvancementsIn);

        if (bredanimalstrigger$listeners != null)
        {
            bredanimalstrigger$listeners.func_192340_b(listener);

            if (bredanimalstrigger$listeners.func_192341_a())
            {
                this.field_192172_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_192172_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public BredAnimalsTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        EntityPredicate entitypredicate = EntityPredicate.deserialize(json.get("parent"));
        EntityPredicate entitypredicate1 = EntityPredicate.deserialize(json.get("partner"));
        EntityPredicate entitypredicate2 = EntityPredicate.deserialize(json.get("child"));
        return new BredAnimalsTrigger.Instance(entitypredicate, entitypredicate1, entitypredicate2);
    }

    public void trigger(EntityPlayerMP player, EntityAnimal parent1, EntityAnimal parent2, EntityAgeable child)
    {
        BredAnimalsTrigger.Listeners bredanimalstrigger$listeners = this.field_192172_b.get(player.getAdvancements());

        if (bredanimalstrigger$listeners != null)
        {
            bredanimalstrigger$listeners.func_192342_a(player, parent1, parent2, child);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final EntityPredicate parent;
        private final EntityPredicate partner;
        private final EntityPredicate child;

        public Instance(EntityPredicate parent, EntityPredicate partner, EntityPredicate child)
        {
            super(BredAnimalsTrigger.ID);
            this.parent = parent;
            this.partner = partner;
            this.child = child;
        }

        public boolean test(EntityPlayerMP player, EntityAnimal parent1In, EntityAnimal parent2In, EntityAgeable childIn)
        {
            if (!this.child.test(player, childIn))
            {
                return false;
            }
            else
            {
                return this.parent.test(player, parent1In) && this.partner.test(player, parent2In) || this.parent.test(player, parent2In) && this.partner.test(player, parent1In);
            }
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_192344_a;
        private final Set<ICriterionTrigger.Listener<BredAnimalsTrigger.Instance>> field_192345_b = Sets.<ICriterionTrigger.Listener<BredAnimalsTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47409_1_)
        {
            this.field_192344_a = p_i47409_1_;
        }

        public boolean func_192341_a()
        {
            return this.field_192345_b.isEmpty();
        }

        public void func_192343_a(ICriterionTrigger.Listener<BredAnimalsTrigger.Instance> p_192343_1_)
        {
            this.field_192345_b.add(p_192343_1_);
        }

        public void func_192340_b(ICriterionTrigger.Listener<BredAnimalsTrigger.Instance> p_192340_1_)
        {
            this.field_192345_b.remove(p_192340_1_);
        }

        public void func_192342_a(EntityPlayerMP p_192342_1_, EntityAnimal p_192342_2_, EntityAnimal p_192342_3_, EntityAgeable p_192342_4_)
        {
            List<ICriterionTrigger.Listener<BredAnimalsTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<BredAnimalsTrigger.Instance> listener : this.field_192345_b)
            {
                if (((BredAnimalsTrigger.Instance)listener.getCriterionInstance()).test(p_192342_1_, p_192342_2_, p_192342_3_, p_192342_4_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<BredAnimalsTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<BredAnimalsTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_192344_a);
                }
            }
        }
    }
}
