package net.minecraft.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.properties.EntityProperty;
import net.minecraft.world.storage.loot.properties.EntityPropertyManager;

public class EntityHasProperty implements LootCondition
{
    private final EntityProperty[] field_186623_a;
    private final LootContext.EntityTarget target;

    public EntityHasProperty(EntityProperty[] p_i46617_1_, LootContext.EntityTarget p_i46617_2_)
    {
        this.field_186623_a = p_i46617_1_;
        this.target = p_i46617_2_;
    }

    public boolean func_186618_a(Random p_186618_1_, LootContext p_186618_2_)
    {
        Entity entity = p_186618_2_.func_186494_a(this.target);

        if (entity == null)
        {
            return false;
        }
        else
        {
            for (EntityProperty entityproperty : this.field_186623_a)
            {
                if (!entityproperty.func_186657_a(p_186618_1_, entity))
                {
                    return false;
                }
            }

            return true;
        }
    }

    public static class Serializer extends LootCondition.Serializer<EntityHasProperty>
    {
        protected Serializer()
        {
            super(new ResourceLocation("entity_properties"), EntityHasProperty.class);
        }

        public void serialize(JsonObject json, EntityHasProperty value, JsonSerializationContext context)
        {
            JsonObject jsonobject = new JsonObject();

            for (EntityProperty entityproperty : value.field_186623_a)
            {
                EntityProperty.Serializer<EntityProperty> serializer = EntityPropertyManager.<EntityProperty>func_186645_a(entityproperty);
                jsonobject.add(serializer.func_186649_a().toString(), serializer.func_186650_a(entityproperty, context));
            }

            json.add("properties", jsonobject);
            json.add("entity", context.serialize(value.target));
        }

        public EntityHasProperty deserialize(JsonObject json, JsonDeserializationContext context)
        {
            Set<Entry<String, JsonElement>> set = JsonUtils.getJsonObject(json, "properties").entrySet();
            EntityProperty[] aentityproperty = new EntityProperty[set.size()];
            int i = 0;

            for (Entry<String, JsonElement> entry : set)
            {
                aentityproperty[i++] = EntityPropertyManager.func_186646_a(new ResourceLocation(entry.getKey())).func_186652_a(entry.getValue(), context);
            }

            return new EntityHasProperty(aentityproperty, (LootContext.EntityTarget)JsonUtils.deserializeClass(json, "entity", context, LootContext.EntityTarget.class));
        }
    }
}
