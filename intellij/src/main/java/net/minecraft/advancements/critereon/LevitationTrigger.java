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
import net.minecraft.util.math.Vec3d;

public class LevitationTrigger implements ICriterionTrigger<LevitationTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("levitation");
    private final Map<PlayerAdvancements, LevitationTrigger.Listeners> field_193165_b = Maps.<PlayerAdvancements, LevitationTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<LevitationTrigger.Instance> listener)
    {
        LevitationTrigger.Listeners levitationtrigger$listeners = this.field_193165_b.get(playerAdvancementsIn);

        if (levitationtrigger$listeners == null)
        {
            levitationtrigger$listeners = new LevitationTrigger.Listeners(playerAdvancementsIn);
            this.field_193165_b.put(playerAdvancementsIn, levitationtrigger$listeners);
        }

        levitationtrigger$listeners.func_193449_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<LevitationTrigger.Instance> listener)
    {
        LevitationTrigger.Listeners levitationtrigger$listeners = this.field_193165_b.get(playerAdvancementsIn);

        if (levitationtrigger$listeners != null)
        {
            levitationtrigger$listeners.func_193446_b(listener);

            if (levitationtrigger$listeners.func_193447_a())
            {
                this.field_193165_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_193165_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public LevitationTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        DistancePredicate distancepredicate = DistancePredicate.deserialize(json.get("distance"));
        MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(json.get("duration"));
        return new LevitationTrigger.Instance(distancepredicate, minmaxbounds);
    }

    public void trigger(EntityPlayerMP player, Vec3d startPos, int duration)
    {
        LevitationTrigger.Listeners levitationtrigger$listeners = this.field_193165_b.get(player.getAdvancements());

        if (levitationtrigger$listeners != null)
        {
            levitationtrigger$listeners.func_193448_a(player, startPos, duration);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final DistancePredicate distance;
        private final MinMaxBounds duration;

        public Instance(DistancePredicate p_i47571_1_, MinMaxBounds p_i47571_2_)
        {
            super(LevitationTrigger.ID);
            this.distance = p_i47571_1_;
            this.duration = p_i47571_2_;
        }

        public boolean test(EntityPlayerMP player, Vec3d startPos, int durationIn)
        {
            if (!this.distance.test(startPos.x, startPos.y, startPos.z, player.posX, player.posY, player.posZ))
            {
                return false;
            }
            else
            {
                return this.duration.func_192514_a((float)durationIn);
            }
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_193450_a;
        private final Set<ICriterionTrigger.Listener<LevitationTrigger.Instance>> field_193451_b = Sets.<ICriterionTrigger.Listener<LevitationTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47572_1_)
        {
            this.field_193450_a = p_i47572_1_;
        }

        public boolean func_193447_a()
        {
            return this.field_193451_b.isEmpty();
        }

        public void func_193449_a(ICriterionTrigger.Listener<LevitationTrigger.Instance> p_193449_1_)
        {
            this.field_193451_b.add(p_193449_1_);
        }

        public void func_193446_b(ICriterionTrigger.Listener<LevitationTrigger.Instance> p_193446_1_)
        {
            this.field_193451_b.remove(p_193446_1_);
        }

        public void func_193448_a(EntityPlayerMP p_193448_1_, Vec3d p_193448_2_, int p_193448_3_)
        {
            List<ICriterionTrigger.Listener<LevitationTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<LevitationTrigger.Instance> listener : this.field_193451_b)
            {
                if (((LevitationTrigger.Instance)listener.getCriterionInstance()).test(p_193448_1_, p_193448_2_, p_193448_3_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<LevitationTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<LevitationTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_193450_a);
                }
            }
        }
    }
}
