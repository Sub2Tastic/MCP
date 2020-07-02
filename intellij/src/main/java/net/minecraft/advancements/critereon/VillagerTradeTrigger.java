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
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class VillagerTradeTrigger implements ICriterionTrigger<VillagerTradeTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("villager_trade");
    private final Map<PlayerAdvancements, VillagerTradeTrigger.Listeners> field_192238_b = Maps.<PlayerAdvancements, VillagerTradeTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<VillagerTradeTrigger.Instance> listener)
    {
        VillagerTradeTrigger.Listeners villagertradetrigger$listeners = this.field_192238_b.get(playerAdvancementsIn);

        if (villagertradetrigger$listeners == null)
        {
            villagertradetrigger$listeners = new VillagerTradeTrigger.Listeners(playerAdvancementsIn);
            this.field_192238_b.put(playerAdvancementsIn, villagertradetrigger$listeners);
        }

        villagertradetrigger$listeners.func_192540_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<VillagerTradeTrigger.Instance> listener)
    {
        VillagerTradeTrigger.Listeners villagertradetrigger$listeners = this.field_192238_b.get(playerAdvancementsIn);

        if (villagertradetrigger$listeners != null)
        {
            villagertradetrigger$listeners.func_192538_b(listener);

            if (villagertradetrigger$listeners.func_192539_a())
            {
                this.field_192238_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_192238_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public VillagerTradeTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        EntityPredicate entitypredicate = EntityPredicate.deserialize(json.get("villager"));
        ItemPredicate itempredicate = ItemPredicate.deserialize(json.get("item"));
        return new VillagerTradeTrigger.Instance(entitypredicate, itempredicate);
    }

    public void func_192234_a(EntityPlayerMP p_192234_1_, EntityVillager p_192234_2_, ItemStack p_192234_3_)
    {
        VillagerTradeTrigger.Listeners villagertradetrigger$listeners = this.field_192238_b.get(p_192234_1_.getAdvancements());

        if (villagertradetrigger$listeners != null)
        {
            villagertradetrigger$listeners.func_192537_a(p_192234_1_, p_192234_2_, p_192234_3_);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final EntityPredicate villager;
        private final ItemPredicate item;

        public Instance(EntityPredicate villager, ItemPredicate item)
        {
            super(VillagerTradeTrigger.ID);
            this.villager = villager;
            this.item = item;
        }

        public boolean func_192285_a(EntityPlayerMP p_192285_1_, EntityVillager p_192285_2_, ItemStack p_192285_3_)
        {
            if (!this.villager.test(p_192285_1_, p_192285_2_))
            {
                return false;
            }
            else
            {
                return this.item.test(p_192285_3_);
            }
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_192541_a;
        private final Set<ICriterionTrigger.Listener<VillagerTradeTrigger.Instance>> field_192542_b = Sets.<ICriterionTrigger.Listener<VillagerTradeTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47458_1_)
        {
            this.field_192541_a = p_i47458_1_;
        }

        public boolean func_192539_a()
        {
            return this.field_192542_b.isEmpty();
        }

        public void func_192540_a(ICriterionTrigger.Listener<VillagerTradeTrigger.Instance> p_192540_1_)
        {
            this.field_192542_b.add(p_192540_1_);
        }

        public void func_192538_b(ICriterionTrigger.Listener<VillagerTradeTrigger.Instance> p_192538_1_)
        {
            this.field_192542_b.remove(p_192538_1_);
        }

        public void func_192537_a(EntityPlayerMP p_192537_1_, EntityVillager p_192537_2_, ItemStack p_192537_3_)
        {
            List<ICriterionTrigger.Listener<VillagerTradeTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<VillagerTradeTrigger.Instance> listener : this.field_192542_b)
            {
                if (((VillagerTradeTrigger.Instance)listener.getCriterionInstance()).func_192285_a(p_192537_1_, p_192537_2_, p_192537_3_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<VillagerTradeTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<VillagerTradeTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_192541_a);
                }
            }
        }
    }
}
