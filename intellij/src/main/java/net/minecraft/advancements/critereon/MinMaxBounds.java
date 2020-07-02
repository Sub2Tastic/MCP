package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.util.JsonUtils;

public class MinMaxBounds
{
    public static final MinMaxBounds field_192516_a = new MinMaxBounds((Float)null, (Float)null);
    private final Float min;
    private final Float max;

    public MinMaxBounds(@Nullable Float p_i47431_1_, @Nullable Float p_i47431_2_)
    {
        this.min = p_i47431_1_;
        this.max = p_i47431_2_;
    }

    public boolean func_192514_a(float p_192514_1_)
    {
        if (this.min != null && this.min.floatValue() > p_192514_1_)
        {
            return false;
        }
        else
        {
            return this.max == null || this.max.floatValue() >= p_192514_1_;
        }
    }

    public boolean func_192513_a(double p_192513_1_)
    {
        if (this.min != null && (double)(this.min.floatValue() * this.min.floatValue()) > p_192513_1_)
        {
            return false;
        }
        else
        {
            return this.max == null || (double)(this.max.floatValue() * this.max.floatValue()) >= p_192513_1_;
        }
    }

    public static MinMaxBounds func_192515_a(@Nullable JsonElement p_192515_0_)
    {
        if (p_192515_0_ != null && !p_192515_0_.isJsonNull())
        {
            if (JsonUtils.isNumber(p_192515_0_))
            {
                float f2 = JsonUtils.getFloat(p_192515_0_, "value");
                return new MinMaxBounds(f2, f2);
            }
            else
            {
                JsonObject jsonobject = JsonUtils.getJsonObject(p_192515_0_, "value");
                Float f = jsonobject.has("min") ? JsonUtils.getFloat(jsonobject, "min") : null;
                Float f1 = jsonobject.has("max") ? JsonUtils.getFloat(jsonobject, "max") : null;
                return new MinMaxBounds(f, f1);
            }
        }
        else
        {
            return field_192516_a;
        }
    }
}
