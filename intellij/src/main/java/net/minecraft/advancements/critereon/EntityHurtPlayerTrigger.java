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
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class EntityHurtPlayerTrigger implements ICriterionTrigger<EntityHurtPlayerTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("entity_hurt_player");
    private final Map<PlayerAdvancements, EntityHurtPlayerTrigger.Listeners> field_192202_b = Maps.<PlayerAdvancements, EntityHurtPlayerTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<EntityHurtPlayerTrigger.Instance> listener)
    {
        EntityHurtPlayerTrigger.Listeners entityhurtplayertrigger$listeners = this.field_192202_b.get(playerAdvancementsIn);

        if (entityhurtplayertrigger$listeners == null)
        {
            entityhurtplayertrigger$listeners = new EntityHurtPlayerTrigger.Listeners(playerAdvancementsIn);
            this.field_192202_b.put(playerAdvancementsIn, entityhurtplayertrigger$listeners);
        }

        entityhurtplayertrigger$listeners.func_192477_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<EntityHurtPlayerTrigger.Instance> listener)
    {
        EntityHurtPlayerTrigger.Listeners entityhurtplayertrigger$listeners = this.field_192202_b.get(playerAdvancementsIn);

        if (entityhurtplayertrigger$listeners != null)
        {
            entityhurtplayertrigger$listeners.func_192475_b(listener);

            if (entityhurtplayertrigger$listeners.func_192476_a())
            {
                this.field_192202_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_192202_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public EntityHurtPlayerTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        DamagePredicate damagepredicate = DamagePredicate.deserialize(json.get("damage"));
        return new EntityHurtPlayerTrigger.Instance(damagepredicate);
    }

    public void trigger(EntityPlayerMP player, DamageSource source, float amountDealt, float amountTaken, boolean wasBlocked)
    {
        EntityHurtPlayerTrigger.Listeners entityhurtplayertrigger$listeners = this.field_192202_b.get(player.getAdvancements());

        if (entityhurtplayertrigger$listeners != null)
        {
            entityhurtplayertrigger$listeners.func_192478_a(player, source, amountDealt, amountTaken, wasBlocked);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final DamagePredicate damage;

        public Instance(DamagePredicate damage)
        {
            super(EntityHurtPlayerTrigger.ID);
            this.damage = damage;
        }

        public boolean test(EntityPlayerMP player, DamageSource source, float amountDealt, float amountTaken, boolean wasBlocked)
        {
            return this.damage.test(player, source, amountDealt, amountTaken, wasBlocked);
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_192479_a;
        private final Set<ICriterionTrigger.Listener<EntityHurtPlayerTrigger.Instance>> field_192480_b = Sets.<ICriterionTrigger.Listener<EntityHurtPlayerTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47439_1_)
        {
            this.field_192479_a = p_i47439_1_;
        }

        public boolean func_192476_a()
        {
            return this.field_192480_b.isEmpty();
        }

        public void func_192477_a(ICriterionTrigger.Listener<EntityHurtPlayerTrigger.Instance> p_192477_1_)
        {
            this.field_192480_b.add(p_192477_1_);
        }

        public void func_192475_b(ICriterionTrigger.Listener<EntityHurtPlayerTrigger.Instance> p_192475_1_)
        {
            this.field_192480_b.remove(p_192475_1_);
        }

        public void func_192478_a(EntityPlayerMP p_192478_1_, DamageSource p_192478_2_, float p_192478_3_, float p_192478_4_, boolean p_192478_5_)
        {
            List<ICriterionTrigger.Listener<EntityHurtPlayerTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<EntityHurtPlayerTrigger.Instance> listener : this.field_192480_b)
            {
                if (((EntityHurtPlayerTrigger.Instance)listener.getCriterionInstance()).test(p_192478_1_, p_192478_2_, p_192478_3_, p_192478_4_, p_192478_5_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<EntityHurtPlayerTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<EntityHurtPlayerTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_192479_a);
                }
            }
        }
    }
}
