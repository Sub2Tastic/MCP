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

public class EnchantedItemTrigger implements ICriterionTrigger<EnchantedItemTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("enchanted_item");
    private final Map<PlayerAdvancements, EnchantedItemTrigger.Listeners> field_192192_b = Maps.<PlayerAdvancements, EnchantedItemTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<EnchantedItemTrigger.Instance> listener)
    {
        EnchantedItemTrigger.Listeners enchanteditemtrigger$listeners = this.field_192192_b.get(playerAdvancementsIn);

        if (enchanteditemtrigger$listeners == null)
        {
            enchanteditemtrigger$listeners = new EnchantedItemTrigger.Listeners(playerAdvancementsIn);
            this.field_192192_b.put(playerAdvancementsIn, enchanteditemtrigger$listeners);
        }

        enchanteditemtrigger$listeners.func_192460_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<EnchantedItemTrigger.Instance> listener)
    {
        EnchantedItemTrigger.Listeners enchanteditemtrigger$listeners = this.field_192192_b.get(playerAdvancementsIn);

        if (enchanteditemtrigger$listeners != null)
        {
            enchanteditemtrigger$listeners.func_192457_b(listener);

            if (enchanteditemtrigger$listeners.func_192458_a())
            {
                this.field_192192_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_192192_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public EnchantedItemTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        ItemPredicate itempredicate = ItemPredicate.deserialize(json.get("item"));
        MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(json.get("levels"));
        return new EnchantedItemTrigger.Instance(itempredicate, minmaxbounds);
    }

    public void trigger(EntityPlayerMP player, ItemStack item, int levelsSpent)
    {
        EnchantedItemTrigger.Listeners enchanteditemtrigger$listeners = this.field_192192_b.get(player.getAdvancements());

        if (enchanteditemtrigger$listeners != null)
        {
            enchanteditemtrigger$listeners.func_192459_a(item, levelsSpent);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final ItemPredicate item;
        private final MinMaxBounds levels;

        public Instance(ItemPredicate p_i47376_1_, MinMaxBounds p_i47376_2_)
        {
            super(EnchantedItemTrigger.ID);
            this.item = p_i47376_1_;
            this.levels = p_i47376_2_;
        }

        public boolean test(ItemStack item, int levelsIn)
        {
            if (!this.item.test(item))
            {
                return false;
            }
            else
            {
                return this.levels.func_192514_a((float)levelsIn);
            }
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_192461_a;
        private final Set<ICriterionTrigger.Listener<EnchantedItemTrigger.Instance>> field_192462_b = Sets.<ICriterionTrigger.Listener<EnchantedItemTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47377_1_)
        {
            this.field_192461_a = p_i47377_1_;
        }

        public boolean func_192458_a()
        {
            return this.field_192462_b.isEmpty();
        }

        public void func_192460_a(ICriterionTrigger.Listener<EnchantedItemTrigger.Instance> p_192460_1_)
        {
            this.field_192462_b.add(p_192460_1_);
        }

        public void func_192457_b(ICriterionTrigger.Listener<EnchantedItemTrigger.Instance> p_192457_1_)
        {
            this.field_192462_b.remove(p_192457_1_);
        }

        public void func_192459_a(ItemStack p_192459_1_, int p_192459_2_)
        {
            List<ICriterionTrigger.Listener<EnchantedItemTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<EnchantedItemTrigger.Instance> listener : this.field_192462_b)
            {
                if (((EnchantedItemTrigger.Instance)listener.getCriterionInstance()).test(p_192459_1_, p_192459_2_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<EnchantedItemTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<EnchantedItemTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_192461_a);
                }
            }
        }
    }
}
