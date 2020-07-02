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
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ConsumeItemTrigger implements ICriterionTrigger<ConsumeItemTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("consume_item");
    private final Map<PlayerAdvancements, ConsumeItemTrigger.Listeners> field_193150_b = Maps.<PlayerAdvancements, ConsumeItemTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<ConsumeItemTrigger.Instance> listener)
    {
        ConsumeItemTrigger.Listeners consumeitemtrigger$listeners = this.field_193150_b.get(playerAdvancementsIn);

        if (consumeitemtrigger$listeners == null)
        {
            consumeitemtrigger$listeners = new ConsumeItemTrigger.Listeners(playerAdvancementsIn);
            this.field_193150_b.put(playerAdvancementsIn, consumeitemtrigger$listeners);
        }

        consumeitemtrigger$listeners.func_193239_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<ConsumeItemTrigger.Instance> listener)
    {
        ConsumeItemTrigger.Listeners consumeitemtrigger$listeners = this.field_193150_b.get(playerAdvancementsIn);

        if (consumeitemtrigger$listeners != null)
        {
            consumeitemtrigger$listeners.func_193237_b(listener);

            if (consumeitemtrigger$listeners.func_193238_a())
            {
                this.field_193150_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_193150_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public ConsumeItemTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        ItemPredicate itempredicate = ItemPredicate.deserialize(json.get("item"));
        return new ConsumeItemTrigger.Instance(itempredicate);
    }

    public void trigger(EntityPlayerMP player, ItemStack item)
    {
        ConsumeItemTrigger.Listeners consumeitemtrigger$listeners = this.field_193150_b.get(player.getAdvancements());

        if (consumeitemtrigger$listeners != null)
        {
            consumeitemtrigger$listeners.func_193240_a(item);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final ItemPredicate item;

        public Instance(ItemPredicate item)
        {
            super(ConsumeItemTrigger.ID);
            this.item = item;
        }

        public boolean test(ItemStack item)
        {
            return this.item.test(item);
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_193241_a;
        private final Set<ICriterionTrigger.Listener<ConsumeItemTrigger.Instance>> field_193242_b = Sets.<ICriterionTrigger.Listener<ConsumeItemTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47563_1_)
        {
            this.field_193241_a = p_i47563_1_;
        }

        public boolean func_193238_a()
        {
            return this.field_193242_b.isEmpty();
        }

        public void func_193239_a(ICriterionTrigger.Listener<ConsumeItemTrigger.Instance> p_193239_1_)
        {
            this.field_193242_b.add(p_193239_1_);
        }

        public void func_193237_b(ICriterionTrigger.Listener<ConsumeItemTrigger.Instance> p_193237_1_)
        {
            this.field_193242_b.remove(p_193237_1_);
        }

        public void func_193240_a(ItemStack p_193240_1_)
        {
            List<ICriterionTrigger.Listener<ConsumeItemTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<ConsumeItemTrigger.Instance> listener : this.field_193242_b)
            {
                if (((ConsumeItemTrigger.Instance)listener.getCriterionInstance()).test(p_193240_1_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<ConsumeItemTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<ConsumeItemTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_193241_a);
                }
            }
        }
    }
}
