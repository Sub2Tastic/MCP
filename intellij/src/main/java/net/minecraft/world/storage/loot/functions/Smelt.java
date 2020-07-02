package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Smelt extends LootFunction
{
    private static final Logger LOGGER = LogManager.getLogger();

    public Smelt(LootCondition[] conditionsIn)
    {
        super(conditionsIn);
    }

    public ItemStack func_186553_a(ItemStack p_186553_1_, Random p_186553_2_, LootContext p_186553_3_)
    {
        if (p_186553_1_.isEmpty())
        {
            return p_186553_1_;
        }
        else
        {
            ItemStack itemstack = FurnaceRecipes.func_77602_a().func_151395_a(p_186553_1_);

            if (itemstack.isEmpty())
            {
                LOGGER.warn("Couldn't smelt {} because there is no smelting recipe", (Object)p_186553_1_);
                return p_186553_1_;
            }
            else
            {
                ItemStack itemstack1 = itemstack.copy();
                itemstack1.setCount(p_186553_1_.getCount());
                return itemstack1;
            }
        }
    }

    public static class Serializer extends LootFunction.Serializer<Smelt>
    {
        protected Serializer()
        {
            super(new ResourceLocation("furnace_smelt"), Smelt.class);
        }

        public void serialize(JsonObject object, Smelt functionClazz, JsonSerializationContext serializationContext)
        {
        }

        public Smelt deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn)
        {
            return new Smelt(conditionsIn);
        }
    }
}
