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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class UsedEnderEyeTrigger implements ICriterionTrigger<UsedEnderEyeTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("used_ender_eye");
    private final Map<PlayerAdvancements, UsedEnderEyeTrigger.Listeners> field_192243_b = Maps.<PlayerAdvancements, UsedEnderEyeTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<UsedEnderEyeTrigger.Instance> listener)
    {
        UsedEnderEyeTrigger.Listeners usedendereyetrigger$listeners = this.field_192243_b.get(playerAdvancementsIn);

        if (usedendereyetrigger$listeners == null)
        {
            usedendereyetrigger$listeners = new UsedEnderEyeTrigger.Listeners(playerAdvancementsIn);
            this.field_192243_b.put(playerAdvancementsIn, usedendereyetrigger$listeners);
        }

        usedendereyetrigger$listeners.func_192546_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<UsedEnderEyeTrigger.Instance> listener)
    {
        UsedEnderEyeTrigger.Listeners usedendereyetrigger$listeners = this.field_192243_b.get(playerAdvancementsIn);

        if (usedendereyetrigger$listeners != null)
        {
            usedendereyetrigger$listeners.func_192544_b(listener);

            if (usedendereyetrigger$listeners.func_192545_a())
            {
                this.field_192243_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_192243_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public UsedEnderEyeTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(json.get("distance"));
        return new UsedEnderEyeTrigger.Instance(minmaxbounds);
    }

    public void trigger(EntityPlayerMP player, BlockPos pos)
    {
        UsedEnderEyeTrigger.Listeners usedendereyetrigger$listeners = this.field_192243_b.get(player.getAdvancements());

        if (usedendereyetrigger$listeners != null)
        {
            double d0 = player.posX - (double)pos.getX();
            double d1 = player.posZ - (double)pos.getZ();
            usedendereyetrigger$listeners.func_192543_a(d0 * d0 + d1 * d1);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final MinMaxBounds distance;

        public Instance(MinMaxBounds p_i47449_1_)
        {
            super(UsedEnderEyeTrigger.ID);
            this.distance = p_i47449_1_;
        }

        public boolean test(double distanceSq)
        {
            return this.distance.func_192513_a(distanceSq);
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_192547_a;
        private final Set<ICriterionTrigger.Listener<UsedEnderEyeTrigger.Instance>> field_192548_b = Sets.<ICriterionTrigger.Listener<UsedEnderEyeTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47450_1_)
        {
            this.field_192547_a = p_i47450_1_;
        }

        public boolean func_192545_a()
        {
            return this.field_192548_b.isEmpty();
        }

        public void func_192546_a(ICriterionTrigger.Listener<UsedEnderEyeTrigger.Instance> p_192546_1_)
        {
            this.field_192548_b.add(p_192546_1_);
        }

        public void func_192544_b(ICriterionTrigger.Listener<UsedEnderEyeTrigger.Instance> p_192544_1_)
        {
            this.field_192548_b.remove(p_192544_1_);
        }

        public void func_192543_a(double p_192543_1_)
        {
            List<ICriterionTrigger.Listener<UsedEnderEyeTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<UsedEnderEyeTrigger.Instance> listener : this.field_192548_b)
            {
                if (((UsedEnderEyeTrigger.Instance)listener.getCriterionInstance()).test(p_192543_1_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<UsedEnderEyeTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<UsedEnderEyeTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_192547_a);
                }
            }
        }
    }
}
