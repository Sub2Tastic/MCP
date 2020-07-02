package net.minecraft.world;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum DimensionType
{
    OVERWORLD(0, "overworld", "", WorldProviderSurface.class),
    NETHER(-1, "the_nether", "_nether", WorldProviderHell.class),
    THE_END(1, "the_end", "_end", WorldProviderEnd.class);

    private final int id;
    private final String field_186075_e;
    private final String suffix;
    private final Class <? extends WorldProvider > field_186077_g;

    private DimensionType(int p_i46672_3_, String p_i46672_4_, String p_i46672_5_, Class <? extends WorldProvider > p_i46672_6_)
    {
        this.id = p_i46672_3_;
        this.field_186075_e = p_i46672_4_;
        this.suffix = p_i46672_5_;
        this.field_186077_g = p_i46672_6_;
    }

    public int getId()
    {
        return this.id;
    }

    public String func_186065_b()
    {
        return this.field_186075_e;
    }

    public String getSuffix()
    {
        return this.suffix;
    }

    public WorldProvider func_186070_d()
    {
        try
        {
            Constructor <? extends WorldProvider > constructor = this.field_186077_g.getConstructor();
            return constructor.newInstance();
        }
        catch (NoSuchMethodException nosuchmethodexception)
        {
            throw new Error("Could not create new dimension", nosuchmethodexception);
        }
        catch (InvocationTargetException invocationtargetexception)
        {
            throw new Error("Could not create new dimension", invocationtargetexception);
        }
        catch (InstantiationException instantiationexception)
        {
            throw new Error("Could not create new dimension", instantiationexception);
        }
        catch (IllegalAccessException illegalaccessexception)
        {
            throw new Error("Could not create new dimension", illegalaccessexception);
        }
    }

    public static DimensionType getById(int id)
    {
        for (DimensionType dimensiontype : values())
        {
            if (dimensiontype.getId() == id)
            {
                return dimensiontype;
            }
        }

        throw new IllegalArgumentException("Invalid dimension id " + id);
    }

    public static DimensionType byName(String nameIn)
    {
        for (DimensionType dimensiontype : values())
        {
            if (dimensiontype.func_186065_b().equals(nameIn))
            {
                return dimensiontype;
            }
        }

        throw new IllegalArgumentException("Invalid dimension " + nameIn);
    }
}
