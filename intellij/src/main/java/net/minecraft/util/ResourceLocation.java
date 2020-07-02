package net.minecraft.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Locale;
import org.apache.commons.lang3.Validate;

public class ResourceLocation implements Comparable<ResourceLocation>
{
    protected final String namespace;
    protected final String path;

    protected ResourceLocation(int p_i45928_1_, String... p_i45928_2_)
    {
        this.namespace = org.apache.commons.lang3.StringUtils.isEmpty(p_i45928_2_[0]) ? "minecraft" : p_i45928_2_[0].toLowerCase(Locale.ROOT);
        this.path = p_i45928_2_[1].toLowerCase(Locale.ROOT);
        Validate.notNull(this.path);
    }

    public ResourceLocation(String resourceName)
    {
        this(0, func_177516_a(resourceName));
    }

    public ResourceLocation(String namespaceIn, String pathIn)
    {
        this(0, namespaceIn, pathIn);
    }

    protected static String[] func_177516_a(String p_177516_0_)
    {
        String[] astring = new String[] {"minecraft", p_177516_0_};
        int i = p_177516_0_.indexOf(58);

        if (i >= 0)
        {
            astring[1] = p_177516_0_.substring(i + 1, p_177516_0_.length());

            if (i > 1)
            {
                astring[0] = p_177516_0_.substring(0, i);
            }
        }

        return astring;
    }

    public String getPath()
    {
        return this.path;
    }

    public String getNamespace()
    {
        return this.namespace;
    }

    public String toString()
    {
        return this.namespace + ':' + this.path;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof ResourceLocation))
        {
            return false;
        }
        else
        {
            ResourceLocation resourcelocation = (ResourceLocation)p_equals_1_;
            return this.namespace.equals(resourcelocation.namespace) && this.path.equals(resourcelocation.path);
        }
    }

    public int hashCode()
    {
        return 31 * this.namespace.hashCode() + this.path.hashCode();
    }

    public int compareTo(ResourceLocation p_compareTo_1_)
    {
        int i = this.namespace.compareTo(p_compareTo_1_.namespace);

        if (i == 0)
        {
            i = this.path.compareTo(p_compareTo_1_.path);
        }

        return i;
    }

    public static class Serializer implements JsonDeserializer<ResourceLocation>, JsonSerializer<ResourceLocation>
    {
        public ResourceLocation deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
        {
            return new ResourceLocation(JsonUtils.getString(p_deserialize_1_, "location"));
        }

        public JsonElement serialize(ResourceLocation p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
        {
            return new JsonPrimitive(p_serialize_1_.toString());
        }
    }
}
