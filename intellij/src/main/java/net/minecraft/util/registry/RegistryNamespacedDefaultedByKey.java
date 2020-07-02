package net.minecraft.util.registry;

import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;

public class RegistryNamespacedDefaultedByKey<K, V> extends RegistryNamespaced<K, V>
{
    /** The key of the default value. */
    private final K defaultValueKey;

    /**
     * The default value for this registry, retrurned in the place of a null value.
     */
    private V defaultValue;

    public RegistryNamespacedDefaultedByKey(K p_i46017_1_)
    {
        this.defaultValueKey = p_i46017_1_;
    }

    public void func_177775_a(int p_177775_1_, K p_177775_2_, V p_177775_3_)
    {
        if (this.defaultValueKey.equals(p_177775_2_))
        {
            this.defaultValue = p_177775_3_;
        }

        super.func_177775_a(p_177775_1_, p_177775_2_, p_177775_3_);
    }

    public void func_177776_a()
    {
        Validate.notNull(this.defaultValue, "Missing default of DefaultedMappedRegistry: " + this.defaultValueKey);
    }

    /**
     * Gets the integer ID we use to identify the given object.
     */
    public int getId(V value)
    {
        int i = super.getId(value);
        return i == -1 ? super.getId(this.defaultValue) : i;
    }

    @Nonnull

    /**
     * Gets the name we use to identify the given object.
     */
    public K getKey(V value)
    {
        K k = (K)super.getKey(value);
        return (K)(k == null ? this.defaultValueKey : k);
    }

    @Nonnull
    public V getOrDefault(@Nullable K name)
    {
        V v = (V)super.getOrDefault(name);
        return (V)(v == null ? this.defaultValue : v);
    }

    @Nonnull
    public V func_148754_a(int p_148754_1_)
    {
        V v = (V)super.func_148754_a(p_148754_1_);
        return (V)(v == null ? this.defaultValue : v);
    }

    @Nonnull
    public V getRandom(Random random)
    {
        V v = (V)super.getRandom(random);
        return (V)(v == null ? this.defaultValue : v);
    }
}
