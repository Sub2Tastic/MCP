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
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class CuredZombieVillagerTrigger implements ICriterionTrigger<CuredZombieVillagerTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("cured_zombie_villager");
    private final Map<PlayerAdvancements, CuredZombieVillagerTrigger.Listeners> field_192187_b = Maps.<PlayerAdvancements, CuredZombieVillagerTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance> listener)
    {
        CuredZombieVillagerTrigger.Listeners curedzombievillagertrigger$listeners = this.field_192187_b.get(playerAdvancementsIn);

        if (curedzombievillagertrigger$listeners == null)
        {
            curedzombievillagertrigger$listeners = new CuredZombieVillagerTrigger.Listeners(playerAdvancementsIn);
            this.field_192187_b.put(playerAdvancementsIn, curedzombievillagertrigger$listeners);
        }

        curedzombievillagertrigger$listeners.func_192360_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance> listener)
    {
        CuredZombieVillagerTrigger.Listeners curedzombievillagertrigger$listeners = this.field_192187_b.get(playerAdvancementsIn);

        if (curedzombievillagertrigger$listeners != null)
        {
            curedzombievillagertrigger$listeners.func_192358_b(listener);

            if (curedzombievillagertrigger$listeners.func_192359_a())
            {
                this.field_192187_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_192187_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public CuredZombieVillagerTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        EntityPredicate entitypredicate = EntityPredicate.deserialize(json.get("zombie"));
        EntityPredicate entitypredicate1 = EntityPredicate.deserialize(json.get("villager"));
        return new CuredZombieVillagerTrigger.Instance(entitypredicate, entitypredicate1);
    }

    public void trigger(EntityPlayerMP player, EntityZombie zombie, EntityVillager villager)
    {
        CuredZombieVillagerTrigger.Listeners curedzombievillagertrigger$listeners = this.field_192187_b.get(player.getAdvancements());

        if (curedzombievillagertrigger$listeners != null)
        {
            curedzombievillagertrigger$listeners.func_192361_a(player, zombie, villager);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final EntityPredicate zombie;
        private final EntityPredicate villager;

        public Instance(EntityPredicate zombie, EntityPredicate villager)
        {
            super(CuredZombieVillagerTrigger.ID);
            this.zombie = zombie;
            this.villager = villager;
        }

        public boolean test(EntityPlayerMP player, EntityZombie zombie, EntityVillager villager)
        {
            if (!this.zombie.test(player, zombie))
            {
                return false;
            }
            else
            {
                return this.villager.test(player, villager);
            }
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_192362_a;
        private final Set<ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance>> field_192363_b = Sets.<ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47460_1_)
        {
            this.field_192362_a = p_i47460_1_;
        }

        public boolean func_192359_a()
        {
            return this.field_192363_b.isEmpty();
        }

        public void func_192360_a(ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance> p_192360_1_)
        {
            this.field_192363_b.add(p_192360_1_);
        }

        public void func_192358_b(ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance> p_192358_1_)
        {
            this.field_192363_b.remove(p_192358_1_);
        }

        public void func_192361_a(EntityPlayerMP p_192361_1_, EntityZombie p_192361_2_, EntityVillager p_192361_3_)
        {
            List<ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance> listener : this.field_192363_b)
            {
                if (((CuredZombieVillagerTrigger.Instance)listener.getCriterionInstance()).test(p_192361_1_, p_192361_2_, p_192361_3_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<CuredZombieVillagerTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_192362_a);
                }
            }
        }
    }
}
