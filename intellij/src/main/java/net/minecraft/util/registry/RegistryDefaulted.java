package net.minecraft.util.registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RegistryDefaulted<K, V> extends RegistrySimple<K, V>
{
    private final V field_82597_b;

    public RegistryDefaulted(V p_i1366_1_)
    {
        this.field_82597_b = p_i1366_1_;
    }

    @Nonnull
    public V getOrDefault(@Nullable K name)
    {
        V v = (V)super.getOrDefault(name);
        return (V)(v == null ? this.field_82597_b : v);
    }
}
