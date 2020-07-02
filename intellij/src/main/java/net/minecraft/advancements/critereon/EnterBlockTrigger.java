package net.minecraft.advancements.critereon;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class EnterBlockTrigger implements ICriterionTrigger<EnterBlockTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("enter_block");
    private final Map<PlayerAdvancements, EnterBlockTrigger.Listeners> field_192197_b = Maps.<PlayerAdvancements, EnterBlockTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<EnterBlockTrigger.Instance> listener)
    {
        EnterBlockTrigger.Listeners enterblocktrigger$listeners = this.field_192197_b.get(playerAdvancementsIn);

        if (enterblocktrigger$listeners == null)
        {
            enterblocktrigger$listeners = new EnterBlockTrigger.Listeners(playerAdvancementsIn);
            this.field_192197_b.put(playerAdvancementsIn, enterblocktrigger$listeners);
        }

        enterblocktrigger$listeners.func_192472_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<EnterBlockTrigger.Instance> listener)
    {
        EnterBlockTrigger.Listeners enterblocktrigger$listeners = this.field_192197_b.get(playerAdvancementsIn);

        if (enterblocktrigger$listeners != null)
        {
            enterblocktrigger$listeners.func_192469_b(listener);

            if (enterblocktrigger$listeners.func_192470_a())
            {
                this.field_192197_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_192197_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public EnterBlockTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        Block block = null;

        if (json.has("block"))
        {
            ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(json, "block"));

            if (!Block.field_149771_c.func_148741_d(resourcelocation))
            {
                throw new JsonSyntaxException("Unknown block type '" + resourcelocation + "'");
            }

            block = Block.field_149771_c.getOrDefault(resourcelocation);
        }

        Map < IProperty<?>, Object > map = null;

        if (json.has("state"))
        {
            if (block == null)
            {
                throw new JsonSyntaxException("Can't define block state without a specific block type");
            }

            BlockStateContainer blockstatecontainer = block.getStateContainer();

            for (Entry<String, JsonElement> entry : JsonUtils.getJsonObject(json, "state").entrySet())
            {
                IProperty<?> iproperty = blockstatecontainer.getProperty(entry.getKey());

                if (iproperty == null)
                {
                    throw new JsonSyntaxException("Unknown block state property '" + (String)entry.getKey() + "' for block '" + Block.field_149771_c.getKey(block) + "'");
                }

                String s = JsonUtils.getString(entry.getValue(), entry.getKey());
                Optional<?> optional = iproperty.parseValue(s);

                if (!optional.isPresent())
                {
                    throw new JsonSyntaxException("Invalid block state value '" + s + "' for property '" + (String)entry.getKey() + "' on block '" + Block.field_149771_c.getKey(block) + "'");
                }

                if (map == null)
                {
                    map = Maps. < IProperty<?>, Object > newHashMap();
                }

                map.put(iproperty, optional.get());
            }
        }

        return new EnterBlockTrigger.Instance(block, map);
    }

    public void trigger(EntityPlayerMP player, IBlockState state)
    {
        EnterBlockTrigger.Listeners enterblocktrigger$listeners = this.field_192197_b.get(player.getAdvancements());

        if (enterblocktrigger$listeners != null)
        {
            enterblocktrigger$listeners.func_192471_a(state);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final Block block;
        private final Map < IProperty<?>, Object > properties;

        public Instance(@Nullable Block p_i47451_1_, @Nullable Map < IProperty<?>, Object > p_i47451_2_)
        {
            super(EnterBlockTrigger.ID);
            this.block = p_i47451_1_;
            this.properties = p_i47451_2_;
        }

        public boolean test(IBlockState state)
        {
            if (this.block != null && state.getBlock() != this.block)
            {
                return false;
            }
            else
            {
                if (this.properties != null)
                {
                    for (Entry < IProperty<?>, Object > entry : this.properties.entrySet())
                    {
                        if (state.get(entry.getKey()) != entry.getValue())
                        {
                            return false;
                        }
                    }
                }

                return true;
            }
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_192473_a;
        private final Set<ICriterionTrigger.Listener<EnterBlockTrigger.Instance>> field_192474_b = Sets.<ICriterionTrigger.Listener<EnterBlockTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47452_1_)
        {
            this.field_192473_a = p_i47452_1_;
        }

        public boolean func_192470_a()
        {
            return this.field_192474_b.isEmpty();
        }

        public void func_192472_a(ICriterionTrigger.Listener<EnterBlockTrigger.Instance> p_192472_1_)
        {
            this.field_192474_b.add(p_192472_1_);
        }

        public void func_192469_b(ICriterionTrigger.Listener<EnterBlockTrigger.Instance> p_192469_1_)
        {
            this.field_192474_b.remove(p_192469_1_);
        }

        public void func_192471_a(IBlockState p_192471_1_)
        {
            List<ICriterionTrigger.Listener<EnterBlockTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<EnterBlockTrigger.Instance> listener : this.field_192474_b)
            {
                if (((EnterBlockTrigger.Instance)listener.getCriterionInstance()).test(p_192471_1_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<EnterBlockTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<EnterBlockTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_192473_a);
                }
            }
        }
    }
}
