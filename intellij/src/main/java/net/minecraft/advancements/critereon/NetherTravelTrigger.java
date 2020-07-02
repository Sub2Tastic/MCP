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
import net.minecraft.world.WorldServer;

public class NetherTravelTrigger implements ICriterionTrigger<NetherTravelTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("nether_travel");
    private final Map<PlayerAdvancements, NetherTravelTrigger.Listeners> field_193170_b = Maps.<PlayerAdvancements, NetherTravelTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<NetherTravelTrigger.Instance> listener)
    {
        NetherTravelTrigger.Listeners nethertraveltrigger$listeners = this.field_193170_b.get(playerAdvancementsIn);

        if (nethertraveltrigger$listeners == null)
        {
            nethertraveltrigger$listeners = new NetherTravelTrigger.Listeners(playerAdvancementsIn);
            this.field_193170_b.put(playerAdvancementsIn, nethertraveltrigger$listeners);
        }

        nethertraveltrigger$listeners.func_193484_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<NetherTravelTrigger.Instance> listener)
    {
        NetherTravelTrigger.Listeners nethertraveltrigger$listeners = this.field_193170_b.get(playerAdvancementsIn);

        if (nethertraveltrigger$listeners != null)
        {
            nethertraveltrigger$listeners.func_193481_b(listener);

            if (nethertraveltrigger$listeners.func_193482_a())
            {
                this.field_193170_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_193170_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public NetherTravelTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        LocationPredicate locationpredicate = LocationPredicate.deserialize(json.get("entered"));
        LocationPredicate locationpredicate1 = LocationPredicate.deserialize(json.get("exited"));
        DistancePredicate distancepredicate = DistancePredicate.deserialize(json.get("distance"));
        return new NetherTravelTrigger.Instance(locationpredicate, locationpredicate1, distancepredicate);
    }

    public void trigger(EntityPlayerMP player, Vec3d enteredNetherPosition)
    {
        NetherTravelTrigger.Listeners nethertraveltrigger$listeners = this.field_193170_b.get(player.getAdvancements());

        if (nethertraveltrigger$listeners != null)
        {
            nethertraveltrigger$listeners.func_193483_a(player.getServerWorld(), enteredNetherPosition, player.posX, player.posY, player.posZ);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final LocationPredicate entered;
        private final LocationPredicate exited;
        private final DistancePredicate distance;

        public Instance(LocationPredicate enteredIn, LocationPredicate exitedIn, DistancePredicate distanceIn)
        {
            super(NetherTravelTrigger.ID);
            this.entered = enteredIn;
            this.exited = exitedIn;
            this.distance = distanceIn;
        }

        public boolean test(WorldServer world, Vec3d enteredNetherPosition, double x, double y, double z)
        {
            if (!this.entered.test(world, enteredNetherPosition.x, enteredNetherPosition.y, enteredNetherPosition.z))
            {
                return false;
            }
            else if (!this.exited.test(world, x, y, z))
            {
                return false;
            }
            else
            {
                return this.distance.test(enteredNetherPosition.x, enteredNetherPosition.y, enteredNetherPosition.z, x, y, z);
            }
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_193485_a;
        private final Set<ICriterionTrigger.Listener<NetherTravelTrigger.Instance>> field_193486_b = Sets.<ICriterionTrigger.Listener<NetherTravelTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47575_1_)
        {
            this.field_193485_a = p_i47575_1_;
        }

        public boolean func_193482_a()
        {
            return this.field_193486_b.isEmpty();
        }

        public void func_193484_a(ICriterionTrigger.Listener<NetherTravelTrigger.Instance> p_193484_1_)
        {
            this.field_193486_b.add(p_193484_1_);
        }

        public void func_193481_b(ICriterionTrigger.Listener<NetherTravelTrigger.Instance> p_193481_1_)
        {
            this.field_193486_b.remove(p_193481_1_);
        }

        public void func_193483_a(WorldServer p_193483_1_, Vec3d p_193483_2_, double p_193483_3_, double p_193483_5_, double p_193483_7_)
        {
            List<ICriterionTrigger.Listener<NetherTravelTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<NetherTravelTrigger.Instance> listener : this.field_193486_b)
            {
                if (((NetherTravelTrigger.Instance)listener.getCriterionInstance()).test(p_193483_1_, p_193483_2_, p_193483_3_, p_193483_5_, p_193483_7_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<NetherTravelTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<NetherTravelTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_193485_a);
                }
            }
        }
    }
}
