package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;

public class ChangeDimensionTrigger implements ICriterionTrigger<ChangeDimensionTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("changed_dimension");
    private final Map<PlayerAdvancements, ChangeDimensionTrigger.Listeners> field_193145_b = Maps.<PlayerAdvancements, ChangeDimensionTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<ChangeDimensionTrigger.Instance> listener)
    {
        ChangeDimensionTrigger.Listeners changedimensiontrigger$listeners = this.field_193145_b.get(playerAdvancementsIn);

        if (changedimensiontrigger$listeners == null)
        {
            changedimensiontrigger$listeners = new ChangeDimensionTrigger.Listeners(playerAdvancementsIn);
            this.field_193145_b.put(playerAdvancementsIn, changedimensiontrigger$listeners);
        }

        changedimensiontrigger$listeners.func_193233_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<ChangeDimensionTrigger.Instance> listener)
    {
        ChangeDimensionTrigger.Listeners changedimensiontrigger$listeners = this.field_193145_b.get(playerAdvancementsIn);

        if (changedimensiontrigger$listeners != null)
        {
            changedimensiontrigger$listeners.func_193231_b(listener);

            if (changedimensiontrigger$listeners.func_193232_a())
            {
                this.field_193145_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_193145_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public ChangeDimensionTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        DimensionType dimensiontype = json.has("from") ? DimensionType.byName(JsonUtils.getString(json, "from")) : null;
        DimensionType dimensiontype1 = json.has("to") ? DimensionType.byName(JsonUtils.getString(json, "to")) : null;
        return new ChangeDimensionTrigger.Instance(dimensiontype, dimensiontype1);
    }

    public void trigger(EntityPlayerMP player, DimensionType from, DimensionType to)
    {
        ChangeDimensionTrigger.Listeners changedimensiontrigger$listeners = this.field_193145_b.get(player.getAdvancements());

        if (changedimensiontrigger$listeners != null)
        {
            changedimensiontrigger$listeners.func_193234_a(from, to);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        @Nullable
        private final DimensionType from;
        @Nullable
        private final DimensionType to;

        public Instance(@Nullable DimensionType from, @Nullable DimensionType to)
        {
            super(ChangeDimensionTrigger.ID);
            this.from = from;
            this.to = to;
        }

        public boolean test(DimensionType from, DimensionType to)
        {
            if (this.from != null && this.from != from)
            {
                return false;
            }
            else
            {
                return this.to == null || this.to == to;
            }
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_193235_a;
        private final Set<ICriterionTrigger.Listener<ChangeDimensionTrigger.Instance>> field_193236_b = Sets.<ICriterionTrigger.Listener<ChangeDimensionTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47476_1_)
        {
            this.field_193235_a = p_i47476_1_;
        }

        public boolean func_193232_a()
        {
            return this.field_193236_b.isEmpty();
        }

        public void func_193233_a(ICriterionTrigger.Listener<ChangeDimensionTrigger.Instance> p_193233_1_)
        {
            this.field_193236_b.add(p_193233_1_);
        }

        public void func_193231_b(ICriterionTrigger.Listener<ChangeDimensionTrigger.Instance> p_193231_1_)
        {
            this.field_193236_b.remove(p_193231_1_);
        }

        public void func_193234_a(DimensionType p_193234_1_, DimensionType p_193234_2_)
        {
            List<ICriterionTrigger.Listener<ChangeDimensionTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<ChangeDimensionTrigger.Instance> listener : this.field_193236_b)
            {
                if (((ChangeDimensionTrigger.Instance)listener.getCriterionInstance()).test(p_193234_1_, p_193234_2_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<ChangeDimensionTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<ChangeDimensionTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_193235_a);
                }
            }
        }
    }
}
