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

public class UsedTotemTrigger implements ICriterionTrigger<UsedTotemTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("used_totem");
    private final Map<PlayerAdvancements, UsedTotemTrigger.Listeners> field_193189_b = Maps.<PlayerAdvancements, UsedTotemTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<UsedTotemTrigger.Instance> listener)
    {
        UsedTotemTrigger.Listeners usedtotemtrigger$listeners = this.field_193189_b.get(playerAdvancementsIn);

        if (usedtotemtrigger$listeners == null)
        {
            usedtotemtrigger$listeners = new UsedTotemTrigger.Listeners(playerAdvancementsIn);
            this.field_193189_b.put(playerAdvancementsIn, usedtotemtrigger$listeners);
        }

        usedtotemtrigger$listeners.func_193508_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<UsedTotemTrigger.Instance> listener)
    {
        UsedTotemTrigger.Listeners usedtotemtrigger$listeners = this.field_193189_b.get(playerAdvancementsIn);

        if (usedtotemtrigger$listeners != null)
        {
            usedtotemtrigger$listeners.func_193506_b(listener);

            if (usedtotemtrigger$listeners.func_193507_a())
            {
                this.field_193189_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_193189_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public UsedTotemTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        ItemPredicate itempredicate = ItemPredicate.deserialize(json.get("item"));
        return new UsedTotemTrigger.Instance(itempredicate);
    }

    public void trigger(EntityPlayerMP player, ItemStack item)
    {
        UsedTotemTrigger.Listeners usedtotemtrigger$listeners = this.field_193189_b.get(player.getAdvancements());

        if (usedtotemtrigger$listeners != null)
        {
            usedtotemtrigger$listeners.func_193509_a(item);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final ItemPredicate item;

        public Instance(ItemPredicate item)
        {
            super(UsedTotemTrigger.ID);
            this.item = item;
        }

        public boolean test(ItemStack item)
        {
            return this.item.test(item);
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_193510_a;
        private final Set<ICriterionTrigger.Listener<UsedTotemTrigger.Instance>> field_193511_b = Sets.<ICriterionTrigger.Listener<UsedTotemTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47565_1_)
        {
            this.field_193510_a = p_i47565_1_;
        }

        public boolean func_193507_a()
        {
            return this.field_193511_b.isEmpty();
        }

        public void func_193508_a(ICriterionTrigger.Listener<UsedTotemTrigger.Instance> p_193508_1_)
        {
            this.field_193511_b.add(p_193508_1_);
        }

        public void func_193506_b(ICriterionTrigger.Listener<UsedTotemTrigger.Instance> p_193506_1_)
        {
            this.field_193511_b.remove(p_193506_1_);
        }

        public void func_193509_a(ItemStack p_193509_1_)
        {
            List<ICriterionTrigger.Listener<UsedTotemTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<UsedTotemTrigger.Instance> listener : this.field_193511_b)
            {
                if (((UsedTotemTrigger.Instance)listener.getCriterionInstance()).test(p_193509_1_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<UsedTotemTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<UsedTotemTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_193510_a);
                }
            }
        }
    }
}
