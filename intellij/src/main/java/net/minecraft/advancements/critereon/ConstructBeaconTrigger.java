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
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;

public class ConstructBeaconTrigger implements ICriterionTrigger<ConstructBeaconTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("construct_beacon");
    private final Map<PlayerAdvancements, ConstructBeaconTrigger.Listeners> field_192182_b = Maps.<PlayerAdvancements, ConstructBeaconTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<ConstructBeaconTrigger.Instance> listener)
    {
        ConstructBeaconTrigger.Listeners constructbeacontrigger$listeners = this.field_192182_b.get(playerAdvancementsIn);

        if (constructbeacontrigger$listeners == null)
        {
            constructbeacontrigger$listeners = new ConstructBeaconTrigger.Listeners(playerAdvancementsIn);
            this.field_192182_b.put(playerAdvancementsIn, constructbeacontrigger$listeners);
        }

        constructbeacontrigger$listeners.func_192355_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<ConstructBeaconTrigger.Instance> listener)
    {
        ConstructBeaconTrigger.Listeners constructbeacontrigger$listeners = this.field_192182_b.get(playerAdvancementsIn);

        if (constructbeacontrigger$listeners != null)
        {
            constructbeacontrigger$listeners.func_192353_b(listener);

            if (constructbeacontrigger$listeners.func_192354_a())
            {
                this.field_192182_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_192182_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public ConstructBeaconTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(json.get("level"));
        return new ConstructBeaconTrigger.Instance(minmaxbounds);
    }

    public void trigger(EntityPlayerMP player, TileEntityBeacon beacon)
    {
        ConstructBeaconTrigger.Listeners constructbeacontrigger$listeners = this.field_192182_b.get(player.getAdvancements());

        if (constructbeacontrigger$listeners != null)
        {
            constructbeacontrigger$listeners.func_192352_a(beacon);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final MinMaxBounds level;

        public Instance(MinMaxBounds p_i47373_1_)
        {
            super(ConstructBeaconTrigger.ID);
            this.level = p_i47373_1_;
        }

        public boolean test(TileEntityBeacon beacon)
        {
            return this.level.func_192514_a((float)beacon.getLevels());
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_192356_a;
        private final Set<ICriterionTrigger.Listener<ConstructBeaconTrigger.Instance>> field_192357_b = Sets.<ICriterionTrigger.Listener<ConstructBeaconTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47374_1_)
        {
            this.field_192356_a = p_i47374_1_;
        }

        public boolean func_192354_a()
        {
            return this.field_192357_b.isEmpty();
        }

        public void func_192355_a(ICriterionTrigger.Listener<ConstructBeaconTrigger.Instance> p_192355_1_)
        {
            this.field_192357_b.add(p_192355_1_);
        }

        public void func_192353_b(ICriterionTrigger.Listener<ConstructBeaconTrigger.Instance> p_192353_1_)
        {
            this.field_192357_b.remove(p_192353_1_);
        }

        public void func_192352_a(TileEntityBeacon p_192352_1_)
        {
            List<ICriterionTrigger.Listener<ConstructBeaconTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<ConstructBeaconTrigger.Instance> listener : this.field_192357_b)
            {
                if (((ConstructBeaconTrigger.Instance)listener.getCriterionInstance()).test(p_192352_1_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<ConstructBeaconTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<ConstructBeaconTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_192356_a);
                }
            }
        }
    }
}
