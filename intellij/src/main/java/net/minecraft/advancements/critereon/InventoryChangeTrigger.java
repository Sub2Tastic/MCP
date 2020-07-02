package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class InventoryChangeTrigger implements ICriterionTrigger<InventoryChangeTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("inventory_changed");
    private final Map<PlayerAdvancements, InventoryChangeTrigger.Listeners> field_192210_b = Maps.<PlayerAdvancements, InventoryChangeTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<InventoryChangeTrigger.Instance> listener)
    {
        InventoryChangeTrigger.Listeners inventorychangetrigger$listeners = this.field_192210_b.get(playerAdvancementsIn);

        if (inventorychangetrigger$listeners == null)
        {
            inventorychangetrigger$listeners = new InventoryChangeTrigger.Listeners(playerAdvancementsIn);
            this.field_192210_b.put(playerAdvancementsIn, inventorychangetrigger$listeners);
        }

        inventorychangetrigger$listeners.func_192489_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<InventoryChangeTrigger.Instance> listener)
    {
        InventoryChangeTrigger.Listeners inventorychangetrigger$listeners = this.field_192210_b.get(playerAdvancementsIn);

        if (inventorychangetrigger$listeners != null)
        {
            inventorychangetrigger$listeners.func_192487_b(listener);

            if (inventorychangetrigger$listeners.func_192488_a())
            {
                this.field_192210_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_192210_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public InventoryChangeTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        JsonObject jsonobject = JsonUtils.getJsonObject(json, "slots", new JsonObject());
        MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(jsonobject.get("occupied"));
        MinMaxBounds minmaxbounds1 = MinMaxBounds.func_192515_a(jsonobject.get("full"));
        MinMaxBounds minmaxbounds2 = MinMaxBounds.func_192515_a(jsonobject.get("empty"));
        ItemPredicate[] aitempredicate = ItemPredicate.deserializeArray(json.get("items"));
        return new InventoryChangeTrigger.Instance(minmaxbounds, minmaxbounds1, minmaxbounds2, aitempredicate);
    }

    public void trigger(EntityPlayerMP player, InventoryPlayer inventory)
    {
        InventoryChangeTrigger.Listeners inventorychangetrigger$listeners = this.field_192210_b.get(player.getAdvancements());

        if (inventorychangetrigger$listeners != null)
        {
            inventorychangetrigger$listeners.func_192486_a(inventory);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final MinMaxBounds occupied;
        private final MinMaxBounds full;
        private final MinMaxBounds empty;
        private final ItemPredicate[] items;

        public Instance(MinMaxBounds p_i47390_1_, MinMaxBounds p_i47390_2_, MinMaxBounds p_i47390_3_, ItemPredicate[] p_i47390_4_)
        {
            super(InventoryChangeTrigger.ID);
            this.occupied = p_i47390_1_;
            this.full = p_i47390_2_;
            this.empty = p_i47390_3_;
            this.items = p_i47390_4_;
        }

        public boolean test(InventoryPlayer inventory)
        {
            int i = 0;
            int j = 0;
            int k = 0;
            List<ItemPredicate> list = Lists.newArrayList(this.items);

            for (int l = 0; l < inventory.getSizeInventory(); ++l)
            {
                ItemStack itemstack = inventory.getStackInSlot(l);

                if (itemstack.isEmpty())
                {
                    ++j;
                }
                else
                {
                    ++k;

                    if (itemstack.getCount() >= itemstack.getMaxStackSize())
                    {
                        ++i;
                    }

                    Iterator<ItemPredicate> iterator = list.iterator();

                    while (iterator.hasNext())
                    {
                        ItemPredicate itempredicate = iterator.next();

                        if (itempredicate.test(itemstack))
                        {
                            iterator.remove();
                        }
                    }
                }
            }

            if (!this.full.func_192514_a((float)i))
            {
                return false;
            }
            else if (!this.empty.func_192514_a((float)j))
            {
                return false;
            }
            else if (!this.occupied.func_192514_a((float)k))
            {
                return false;
            }
            else if (!list.isEmpty())
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_192490_a;
        private final Set<ICriterionTrigger.Listener<InventoryChangeTrigger.Instance>> field_192491_b = Sets.<ICriterionTrigger.Listener<InventoryChangeTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47391_1_)
        {
            this.field_192490_a = p_i47391_1_;
        }

        public boolean func_192488_a()
        {
            return this.field_192491_b.isEmpty();
        }

        public void func_192489_a(ICriterionTrigger.Listener<InventoryChangeTrigger.Instance> p_192489_1_)
        {
            this.field_192491_b.add(p_192489_1_);
        }

        public void func_192487_b(ICriterionTrigger.Listener<InventoryChangeTrigger.Instance> p_192487_1_)
        {
            this.field_192491_b.remove(p_192487_1_);
        }

        public void func_192486_a(InventoryPlayer p_192486_1_)
        {
            List<ICriterionTrigger.Listener<InventoryChangeTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<InventoryChangeTrigger.Instance> listener : this.field_192491_b)
            {
                if (((InventoryChangeTrigger.Instance)listener.getCriterionInstance()).test(p_192486_1_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<InventoryChangeTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<InventoryChangeTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_192490_a);
                }
            }
        }
    }
}
