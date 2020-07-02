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

public class ItemDurabilityTrigger implements ICriterionTrigger<ItemDurabilityTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("item_durability_changed");
    private final Map<PlayerAdvancements, ItemDurabilityTrigger.Listeners> field_193160_b = Maps.<PlayerAdvancements, ItemDurabilityTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<ItemDurabilityTrigger.Instance> listener)
    {
        ItemDurabilityTrigger.Listeners itemdurabilitytrigger$listeners = this.field_193160_b.get(playerAdvancementsIn);

        if (itemdurabilitytrigger$listeners == null)
        {
            itemdurabilitytrigger$listeners = new ItemDurabilityTrigger.Listeners(playerAdvancementsIn);
            this.field_193160_b.put(playerAdvancementsIn, itemdurabilitytrigger$listeners);
        }

        itemdurabilitytrigger$listeners.func_193440_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<ItemDurabilityTrigger.Instance> listener)
    {
        ItemDurabilityTrigger.Listeners itemdurabilitytrigger$listeners = this.field_193160_b.get(playerAdvancementsIn);

        if (itemdurabilitytrigger$listeners != null)
        {
            itemdurabilitytrigger$listeners.func_193438_b(listener);

            if (itemdurabilitytrigger$listeners.func_193439_a())
            {
                this.field_193160_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_193160_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public ItemDurabilityTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        ItemPredicate itempredicate = ItemPredicate.deserialize(json.get("item"));
        MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(json.get("durability"));
        MinMaxBounds minmaxbounds1 = MinMaxBounds.func_192515_a(json.get("delta"));
        return new ItemDurabilityTrigger.Instance(itempredicate, minmaxbounds, minmaxbounds1);
    }

    public void trigger(EntityPlayerMP player, ItemStack itemIn, int newDurability)
    {
        ItemDurabilityTrigger.Listeners itemdurabilitytrigger$listeners = this.field_193160_b.get(player.getAdvancements());

        if (itemdurabilitytrigger$listeners != null)
        {
            itemdurabilitytrigger$listeners.func_193441_a(itemIn, newDurability);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final ItemPredicate item;
        private final MinMaxBounds durability;
        private final MinMaxBounds delta;

        public Instance(ItemPredicate p_i47511_1_, MinMaxBounds p_i47511_2_, MinMaxBounds p_i47511_3_)
        {
            super(ItemDurabilityTrigger.ID);
            this.item = p_i47511_1_;
            this.durability = p_i47511_2_;
            this.delta = p_i47511_3_;
        }

        public boolean test(ItemStack item, int p_193197_2_)
        {
            if (!this.item.test(item))
            {
                return false;
            }
            else if (!this.durability.func_192514_a((float)(item.getMaxDamage() - p_193197_2_)))
            {
                return false;
            }
            else
            {
                return this.delta.func_192514_a((float)(item.getDamage() - p_193197_2_));
            }
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_193442_a;
        private final Set<ICriterionTrigger.Listener<ItemDurabilityTrigger.Instance>> field_193443_b = Sets.<ICriterionTrigger.Listener<ItemDurabilityTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47512_1_)
        {
            this.field_193442_a = p_i47512_1_;
        }

        public boolean func_193439_a()
        {
            return this.field_193443_b.isEmpty();
        }

        public void func_193440_a(ICriterionTrigger.Listener<ItemDurabilityTrigger.Instance> p_193440_1_)
        {
            this.field_193443_b.add(p_193440_1_);
        }

        public void func_193438_b(ICriterionTrigger.Listener<ItemDurabilityTrigger.Instance> p_193438_1_)
        {
            this.field_193443_b.remove(p_193438_1_);
        }

        public void func_193441_a(ItemStack p_193441_1_, int p_193441_2_)
        {
            List<ICriterionTrigger.Listener<ItemDurabilityTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<ItemDurabilityTrigger.Instance> listener : this.field_193443_b)
            {
                if (((ItemDurabilityTrigger.Instance)listener.getCriterionInstance()).test(p_193441_1_, p_193441_2_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<ItemDurabilityTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<ItemDurabilityTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_193442_a);
                }
            }
        }
    }
}
