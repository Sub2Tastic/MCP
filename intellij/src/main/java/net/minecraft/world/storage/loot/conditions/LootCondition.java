package net.minecraft.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;

public interface LootCondition
{
    boolean func_186618_a(Random p_186618_1_, LootContext p_186618_2_);

    public abstract static class Serializer<T extends LootCondition>
    {
        private final ResourceLocation lootTableLocation;
        private final Class<T> conditionClass;

        protected Serializer(ResourceLocation location, Class<T> clazz)
        {
            this.lootTableLocation = location;
            this.conditionClass = clazz;
        }

        public ResourceLocation getLootTableLocation()
        {
            return this.lootTableLocation;
        }

        public Class<T> getConditionClass()
        {
            return this.conditionClass;
        }

        public abstract void serialize(JsonObject json, T value, JsonSerializationContext context);

        public abstract T deserialize(JsonObject json, JsonDeserializationContext context);
    }
}
