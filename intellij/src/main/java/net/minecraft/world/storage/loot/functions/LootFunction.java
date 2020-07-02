package net.minecraft.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public abstract class LootFunction
{
    private final LootCondition[] field_186555_a;

    protected LootFunction(LootCondition[] p_i46626_1_)
    {
        this.field_186555_a = p_i46626_1_;
    }

    public abstract ItemStack func_186553_a(ItemStack p_186553_1_, Random p_186553_2_, LootContext p_186553_3_);

    public LootCondition[] func_186554_a()
    {
        return this.field_186555_a;
    }

    public abstract static class Serializer<T extends LootFunction>
    {
        private final ResourceLocation lootTableLocation;
        private final Class<T> functionClass;

        protected Serializer(ResourceLocation location, Class<T> clazz)
        {
            this.lootTableLocation = location;
            this.functionClass = clazz;
        }

        public ResourceLocation getFunctionName()
        {
            return this.lootTableLocation;
        }

        public Class<T> getFunctionClass()
        {
            return this.functionClass;
        }

        public abstract void serialize(JsonObject object, T functionClazz, JsonSerializationContext serializationContext);

        public abstract T deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn);
    }
}
