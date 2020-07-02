package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionType;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class BrewedPotionTrigger implements ICriterionTrigger<BrewedPotionTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("brewed_potion");
    private final Map<PlayerAdvancements, BrewedPotionTrigger.Listeners> field_192177_b = Maps.<PlayerAdvancements, BrewedPotionTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<BrewedPotionTrigger.Instance> listener)
    {
        BrewedPotionTrigger.Listeners brewedpotiontrigger$listeners = this.field_192177_b.get(playerAdvancementsIn);

        if (brewedpotiontrigger$listeners == null)
        {
            brewedpotiontrigger$listeners = new BrewedPotionTrigger.Listeners(playerAdvancementsIn);
            this.field_192177_b.put(playerAdvancementsIn, brewedpotiontrigger$listeners);
        }

        brewedpotiontrigger$listeners.func_192349_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<BrewedPotionTrigger.Instance> listener)
    {
        BrewedPotionTrigger.Listeners brewedpotiontrigger$listeners = this.field_192177_b.get(playerAdvancementsIn);

        if (brewedpotiontrigger$listeners != null)
        {
            brewedpotiontrigger$listeners.func_192346_b(listener);

            if (brewedpotiontrigger$listeners.func_192347_a())
            {
                this.field_192177_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_192177_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public BrewedPotionTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        PotionType potiontype = null;

        if (json.has("potion"))
        {
            ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(json, "potion"));

            if (!PotionType.field_185176_a.func_148741_d(resourcelocation))
            {
                throw new JsonSyntaxException("Unknown potion '" + resourcelocation + "'");
            }

            potiontype = PotionType.field_185176_a.getOrDefault(resourcelocation);
        }

        return new BrewedPotionTrigger.Instance(potiontype);
    }

    public void trigger(EntityPlayerMP player, PotionType potionIn)
    {
        BrewedPotionTrigger.Listeners brewedpotiontrigger$listeners = this.field_192177_b.get(player.getAdvancements());

        if (brewedpotiontrigger$listeners != null)
        {
            brewedpotiontrigger$listeners.func_192348_a(potionIn);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final PotionType potion;

        public Instance(@Nullable PotionType potion)
        {
            super(BrewedPotionTrigger.ID);
            this.potion = potion;
        }

        public boolean test(PotionType potion)
        {
            return this.potion == null || this.potion == potion;
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_192350_a;
        private final Set<ICriterionTrigger.Listener<BrewedPotionTrigger.Instance>> field_192351_b = Sets.<ICriterionTrigger.Listener<BrewedPotionTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47399_1_)
        {
            this.field_192350_a = p_i47399_1_;
        }

        public boolean func_192347_a()
        {
            return this.field_192351_b.isEmpty();
        }

        public void func_192349_a(ICriterionTrigger.Listener<BrewedPotionTrigger.Instance> p_192349_1_)
        {
            this.field_192351_b.add(p_192349_1_);
        }

        public void func_192346_b(ICriterionTrigger.Listener<BrewedPotionTrigger.Instance> p_192346_1_)
        {
            this.field_192351_b.remove(p_192346_1_);
        }

        public void func_192348_a(PotionType p_192348_1_)
        {
            List<ICriterionTrigger.Listener<BrewedPotionTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<BrewedPotionTrigger.Instance> listener : this.field_192351_b)
            {
                if (((BrewedPotionTrigger.Instance)listener.getCriterionInstance()).test(p_192348_1_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<BrewedPotionTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<BrewedPotionTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_192350_a);
                }
            }
        }
    }
}
