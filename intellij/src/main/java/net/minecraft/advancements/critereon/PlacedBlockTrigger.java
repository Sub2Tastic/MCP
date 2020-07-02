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
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class PlacedBlockTrigger implements ICriterionTrigger<PlacedBlockTrigger.Instance>
{
    private static final ResourceLocation ID = new ResourceLocation("placed_block");
    private final Map<PlayerAdvancements, PlacedBlockTrigger.Listeners> field_193175_b = Maps.<PlayerAdvancements, PlacedBlockTrigger.Listeners>newHashMap();

    public ResourceLocation getId()
    {
        return ID;
    }

    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<PlacedBlockTrigger.Instance> listener)
    {
        PlacedBlockTrigger.Listeners placedblocktrigger$listeners = this.field_193175_b.get(playerAdvancementsIn);

        if (placedblocktrigger$listeners == null)
        {
            placedblocktrigger$listeners = new PlacedBlockTrigger.Listeners(playerAdvancementsIn);
            this.field_193175_b.put(playerAdvancementsIn, placedblocktrigger$listeners);
        }

        placedblocktrigger$listeners.func_193490_a(listener);
    }

    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<PlacedBlockTrigger.Instance> listener)
    {
        PlacedBlockTrigger.Listeners placedblocktrigger$listeners = this.field_193175_b.get(playerAdvancementsIn);

        if (placedblocktrigger$listeners != null)
        {
            placedblocktrigger$listeners.func_193487_b(listener);

            if (placedblocktrigger$listeners.func_193488_a())
            {
                this.field_193175_b.remove(playerAdvancementsIn);
            }
        }
    }

    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.field_193175_b.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    public PlacedBlockTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
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

        LocationPredicate locationpredicate = LocationPredicate.deserialize(json.get("location"));
        ItemPredicate itempredicate = ItemPredicate.deserialize(json.get("item"));
        return new PlacedBlockTrigger.Instance(block, map, locationpredicate, itempredicate);
    }

    public void trigger(EntityPlayerMP player, BlockPos pos, ItemStack item)
    {
        IBlockState iblockstate = player.world.getBlockState(pos);
        PlacedBlockTrigger.Listeners placedblocktrigger$listeners = this.field_193175_b.get(player.getAdvancements());

        if (placedblocktrigger$listeners != null)
        {
            placedblocktrigger$listeners.func_193489_a(iblockstate, pos, player.getServerWorld(), item);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final Block block;
        private final Map < IProperty<?>, Object > properties;
        private final LocationPredicate location;
        private final ItemPredicate item;

        public Instance(@Nullable Block p_i47566_1_, @Nullable Map < IProperty<?>, Object > p_i47566_2_, LocationPredicate p_i47566_3_, ItemPredicate p_i47566_4_)
        {
            super(PlacedBlockTrigger.ID);
            this.block = p_i47566_1_;
            this.properties = p_i47566_2_;
            this.location = p_i47566_3_;
            this.item = p_i47566_4_;
        }

        public boolean test(IBlockState state, BlockPos pos, WorldServer world, ItemStack item)
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

                if (!this.location.test(world, (float)pos.getX(), (float)pos.getY(), (float)pos.getZ()))
                {
                    return false;
                }
                else
                {
                    return this.item.test(item);
                }
            }
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements field_193491_a;
        private final Set<ICriterionTrigger.Listener<PlacedBlockTrigger.Instance>> field_193492_b = Sets.<ICriterionTrigger.Listener<PlacedBlockTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements p_i47567_1_)
        {
            this.field_193491_a = p_i47567_1_;
        }

        public boolean func_193488_a()
        {
            return this.field_193492_b.isEmpty();
        }

        public void func_193490_a(ICriterionTrigger.Listener<PlacedBlockTrigger.Instance> p_193490_1_)
        {
            this.field_193492_b.add(p_193490_1_);
        }

        public void func_193487_b(ICriterionTrigger.Listener<PlacedBlockTrigger.Instance> p_193487_1_)
        {
            this.field_193492_b.remove(p_193487_1_);
        }

        public void func_193489_a(IBlockState p_193489_1_, BlockPos p_193489_2_, WorldServer p_193489_3_, ItemStack p_193489_4_)
        {
            List<ICriterionTrigger.Listener<PlacedBlockTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<PlacedBlockTrigger.Instance> listener : this.field_193492_b)
            {
                if (((PlacedBlockTrigger.Instance)listener.getCriterionInstance()).test(p_193489_1_, p_193489_2_, p_193489_3_, p_193489_4_))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<PlacedBlockTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<PlacedBlockTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.field_193491_a);
                }
            }
        }
    }
}
