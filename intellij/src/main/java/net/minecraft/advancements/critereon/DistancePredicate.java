package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;

public class DistancePredicate
{
    public static final DistancePredicate ANY = new DistancePredicate(MinMaxBounds.field_192516_a, MinMaxBounds.field_192516_a, MinMaxBounds.field_192516_a, MinMaxBounds.field_192516_a, MinMaxBounds.field_192516_a);
    private final MinMaxBounds x;
    private final MinMaxBounds y;
    private final MinMaxBounds z;
    private final MinMaxBounds horizontal;
    private final MinMaxBounds absolute;

    public DistancePredicate(MinMaxBounds p_i47542_1_, MinMaxBounds p_i47542_2_, MinMaxBounds p_i47542_3_, MinMaxBounds p_i47542_4_, MinMaxBounds p_i47542_5_)
    {
        this.x = p_i47542_1_;
        this.y = p_i47542_2_;
        this.z = p_i47542_3_;
        this.horizontal = p_i47542_4_;
        this.absolute = p_i47542_5_;
    }

    public boolean test(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        float f = (float)(x1 - x2);
        float f1 = (float)(y1 - y2);
        float f2 = (float)(z1 - z2);

        if (this.x.func_192514_a(MathHelper.abs(f)) && this.y.func_192514_a(MathHelper.abs(f1)) && this.z.func_192514_a(MathHelper.abs(f2)))
        {
            if (!this.horizontal.func_192513_a((double)(f * f + f2 * f2)))
            {
                return false;
            }
            else
            {
                return this.absolute.func_192513_a((double)(f * f + f1 * f1 + f2 * f2));
            }
        }
        else
        {
            return false;
        }
    }

    public static DistancePredicate deserialize(@Nullable JsonElement element)
    {
        if (element != null && !element.isJsonNull())
        {
            JsonObject jsonobject = JsonUtils.getJsonObject(element, "distance");
            MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(jsonobject.get("x"));
            MinMaxBounds minmaxbounds1 = MinMaxBounds.func_192515_a(jsonobject.get("y"));
            MinMaxBounds minmaxbounds2 = MinMaxBounds.func_192515_a(jsonobject.get("z"));
            MinMaxBounds minmaxbounds3 = MinMaxBounds.func_192515_a(jsonobject.get("horizontal"));
            MinMaxBounds minmaxbounds4 = MinMaxBounds.func_192515_a(jsonobject.get("absolute"));
            return new DistancePredicate(minmaxbounds, minmaxbounds1, minmaxbounds2, minmaxbounds3, minmaxbounds4);
        }
        else
        {
            return ANY;
        }
    }
}
