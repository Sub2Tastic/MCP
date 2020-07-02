package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class SetCount extends LootFunction
{
    private final RandomValueRange countRange;

    public SetCount(LootCondition[] p_i46623_1_, RandomValueRange p_i46623_2_)
    {
        super(p_i46623_1_);
        this.countRange = p_i46623_2_;
    }

    public ItemStack func_186553_a(ItemStack p_186553_1_, Random p_186553_2_, LootContext p_186553_3_)
    {
        p_186553_1_.setCount(this.countRange.generateInt(p_186553_2_));
        return p_186553_1_;
    }

    public static class Serializer extends LootFunction.Serializer<SetCount>
    {
        protected Serializer()
        {
            super(new ResourceLocation("set_count"), SetCount.class);
        }

        public void serialize(JsonObject object, SetCount functionClazz, JsonSerializationContext serializationContext)
        {
            object.add("count", serializationContext.serialize(functionClazz.countRange));
        }

        public SetCount deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn)
        {
            return new SetCount(conditionsIn, (RandomValueRange)JsonUtils.deserializeClass(object, "count", deserializationContext, RandomValueRange.class));
        }
    }
}
