package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import javax.annotation.Nullable;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;

public class LocationPredicate
{
    public static LocationPredicate ANY = new LocationPredicate(MinMaxBounds.field_192516_a, MinMaxBounds.field_192516_a, MinMaxBounds.field_192516_a, (Biome)null, (String)null, (DimensionType)null);
    private final MinMaxBounds x;
    private final MinMaxBounds y;
    private final MinMaxBounds z;
    @Nullable
    final Biome biome;
    @Nullable
    private final String feature;
    @Nullable
    private final DimensionType dimension;

    public LocationPredicate(MinMaxBounds p_i47539_1_, MinMaxBounds p_i47539_2_, MinMaxBounds p_i47539_3_, @Nullable Biome p_i47539_4_, @Nullable String p_i47539_5_, @Nullable DimensionType p_i47539_6_)
    {
        this.x = p_i47539_1_;
        this.y = p_i47539_2_;
        this.z = p_i47539_3_;
        this.biome = p_i47539_4_;
        this.feature = p_i47539_5_;
        this.dimension = p_i47539_6_;
    }

    public boolean test(WorldServer world, double x, double y, double z)
    {
        return this.test(world, (float)x, (float)y, (float)z);
    }

    public boolean test(WorldServer world, float x, float y, float z)
    {
        if (!this.x.func_192514_a(x))
        {
            return false;
        }
        else if (!this.y.func_192514_a(y))
        {
            return false;
        }
        else if (!this.z.func_192514_a(z))
        {
            return false;
        }
        else if (this.dimension != null && this.dimension != world.dimension.getType())
        {
            return false;
        }
        else
        {
            BlockPos blockpos = new BlockPos((double)x, (double)y, (double)z);

            if (this.biome != null && this.biome != world.func_180494_b(blockpos))
            {
                return false;
            }
            else
            {
                return this.feature == null || world.getChunkProvider().func_193413_a(world, this.feature, blockpos);
            }
        }
    }

    public static LocationPredicate deserialize(@Nullable JsonElement element)
    {
        if (element != null && !element.isJsonNull())
        {
            JsonObject jsonobject = JsonUtils.getJsonObject(element, "location");
            JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonobject, "position", new JsonObject());
            MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(jsonobject1.get("x"));
            MinMaxBounds minmaxbounds1 = MinMaxBounds.func_192515_a(jsonobject1.get("y"));
            MinMaxBounds minmaxbounds2 = MinMaxBounds.func_192515_a(jsonobject1.get("z"));
            DimensionType dimensiontype = jsonobject.has("dimension") ? DimensionType.byName(JsonUtils.getString(jsonobject, "dimension")) : null;
            String s = jsonobject.has("feature") ? JsonUtils.getString(jsonobject, "feature") : null;
            Biome biome = null;

            if (jsonobject.has("biome"))
            {
                ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(jsonobject, "biome"));
                biome = Biome.field_185377_q.getOrDefault(resourcelocation);

                if (biome == null)
                {
                    throw new JsonSyntaxException("Unknown biome '" + resourcelocation + "'");
                }
            }

            return new LocationPredicate(minmaxbounds, minmaxbounds1, minmaxbounds2, biome, s, dimensiontype);
        }
        else
        {
            return ANY;
        }
    }
}
