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
import net.minecraft.world.WorldServer;

public class PositionTrigger implements ICriterionTrigger<PositionTrigger.Instance>
{
    private final ResourceLocation id;
    private final Map<PlayerAdvancements, PositionTrigger.Listeners> field_192218_b = Maps.<PlayerAdvancements, PositionTrigger.Listeners>newHashMap();

    public PositionTrigger(ResourceLocation id)
    {
        this.id = id;
    }

    public ResourceLocation getId()
    {
        return this.id;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<PositionTrigger.Instance> listener)
    {
        PositionTrigger.Listeners positiontrigger$listeners = this.field_192218_b.get(playerAdvancementsIn);

        if (positiontrigger$listeners == null)
        {
            positiontrigger$listeners = new PositionTrigger.Listeners(playerAdvancementsIn);
            this.field_192218_b.put(playerAdvancementsIn, positiontrigger$listeners);
        }

        positiontrigger$listeners.func_192510_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<PositionTrigger.Instance> listener)
    {
        PositionTrigger.Listeners positiontrigger$listeners = this.field_192218_b.get(playerAdvancementsIn);

        if (positiontrigger$listeners != null)
        {
            positiontrigger$listeners.func_192507_b(listener);

            if (positiontrigger$listeners.func_192508_a())
            {
                this.field_192218_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_192218_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public PositionTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        LocationPredicate locationpredicate = LocationPredicate.deserialize(json);
        return new PositionTrigger.Instance(this.id, locationpredicate);
    }

    public void trigger(EntityPlayerMP player)
    {
        PositionTrigger.Listeners positiontrigger$listeners = this.field_192218_b.get(player.getAdvancements());

        if (positiontrigger$listeners != null)
        {
            positiontrigger$listeners.func_193462_a(player.getServerWorld(), player.posX, player.posY, player.posZ);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final LocationPredicate location;

        public Instance(ResourceLocation criterionIn, LocationPredicate location)
        {
            super(criterionIn);
            this.location = location;
        }

        public boolean test(WorldServer world, double x, double y, double z)
        {
            return this.location.test(world, x, y, z);
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_192511_a;
        private final Set<ICriterionTrigger.Listener<PositionTrigger.Instance>> field_192512_b = Sets.<ICriterionTrigger.Listener<PositionTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47442_1_)
        {
            this.field_192511_a = p_i47442_1_;
        }

        public boolean func_192508_a()
        {
            return this.field_192512_b.isEmpty();
        }

        public void func_192510_a(ICriterionTrigger.Listener<PositionTrigger.Instance> p_192510_1_)
        {
            this.field_192512_b.add(p_192510_1_);
        }

        public void func_192507_b(ICriterionTrigger.Listener<PositionTrigger.Instance> p_192507_1_)
        {
            this.field_192512_b.remove(p_192507_1_);
        }

        public void func_193462_a(WorldServer p_193462_1_, double p_193462_2_, double p_193462_4_, double p_193462_6_)
        {
            List<ICriterionTrigger.Listener<PositionTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<PositionTrigger.Instance> listener : this.field_192512_b)
            {
                if (((PositionTrigger.Instance)listener.getCriterionInstance()).test(p_193462_1_, p_193462_2_, p_193462_4_, p_193462_6_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<PositionTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<PositionTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_192511_a);
                }
            }
        }
    }
}
