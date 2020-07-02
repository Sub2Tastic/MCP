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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class PlayerHurtEntityTrigger implements ICriterionTrigger<PlayerHurtEntityTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("player_hurt_entity");
    private final Map<PlayerAdvancements, PlayerHurtEntityTrigger.Listeners> field_192223_b = Maps.<PlayerAdvancements, PlayerHurtEntityTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance> listener)
    {
        PlayerHurtEntityTrigger.Listeners playerhurtentitytrigger$listeners = this.field_192223_b.get(playerAdvancementsIn);

        if (playerhurtentitytrigger$listeners == null)
        {
            playerhurtentitytrigger$listeners = new PlayerHurtEntityTrigger.Listeners(playerAdvancementsIn);
            this.field_192223_b.put(playerAdvancementsIn, playerhurtentitytrigger$listeners);
        }

        playerhurtentitytrigger$listeners.func_192522_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance> listener)
    {
        PlayerHurtEntityTrigger.Listeners playerhurtentitytrigger$listeners = this.field_192223_b.get(playerAdvancementsIn);

        if (playerhurtentitytrigger$listeners != null)
        {
            playerhurtentitytrigger$listeners.func_192519_b(listener);

            if (playerhurtentitytrigger$listeners.func_192520_a())
            {
                this.field_192223_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_192223_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public PlayerHurtEntityTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        DamagePredicate damagepredicate = DamagePredicate.deserialize(json.get("damage"));
        EntityPredicate entitypredicate = EntityPredicate.deserialize(json.get("entity"));
        return new PlayerHurtEntityTrigger.Instance(damagepredicate, entitypredicate);
    }

    public void trigger(EntityPlayerMP player, Entity entityIn, DamageSource source, float amountDealt, float amountTaken, boolean blocked)
    {
        PlayerHurtEntityTrigger.Listeners playerhurtentitytrigger$listeners = this.field_192223_b.get(player.getAdvancements());

        if (playerhurtentitytrigger$listeners != null)
        {
            playerhurtentitytrigger$listeners.func_192521_a(player, entityIn, source, amountDealt, amountTaken, blocked);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final DamagePredicate damage;
        private final EntityPredicate entity;

        public Instance(DamagePredicate damage, EntityPredicate entity)
        {
            super(PlayerHurtEntityTrigger.ID);
            this.damage = damage;
            this.entity = entity;
        }

        public boolean test(EntityPlayerMP player, Entity entity, DamageSource source, float dealt, float taken, boolean blocked)
        {
            if (!this.damage.test(player, source, dealt, taken, blocked))
            {
                return false;
            }
            else
            {
                return this.entity.test(player, entity);
            }
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_192523_a;
        private final Set<ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance>> field_192524_b = Sets.<ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47407_1_)
        {
            this.field_192523_a = p_i47407_1_;
        }

        public boolean func_192520_a()
        {
            return this.field_192524_b.isEmpty();
        }

        public void func_192522_a(ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance> p_192522_1_)
        {
            this.field_192524_b.add(p_192522_1_);
        }

        public void func_192519_b(ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance> p_192519_1_)
        {
            this.field_192524_b.remove(p_192519_1_);
        }

        public void func_192521_a(EntityPlayerMP p_192521_1_, Entity p_192521_2_, DamageSource p_192521_3_, float p_192521_4_, float p_192521_5_, boolean p_192521_6_)
        {
            List<ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance> listener : this.field_192524_b)
            {
                if (((PlayerHurtEntityTrigger.Instance)listener.getCriterionInstance()).test(p_192521_1_, p_192521_2_, p_192521_3_, p_192521_4_, p_192521_5_, p_192521_6_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<PlayerHurtEntityTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_192523_a);
                }
            }
        }
    }
}
