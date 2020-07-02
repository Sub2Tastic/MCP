package net.minecraft.util.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.util.IObjectIntIterable;
import net.minecraft.util.IntIdentityHashBiMap;

public class RegistryNamespaced<K, V> extends RegistrySimple<K, V> implements IObjectIntIterable<V>
{
    protected final IntIdentityHashBiMap<V> underlyingIntegerMap = new IntIdentityHashBiMap<V>(256);
    protected final Map<V, K> field_148758_b;

    public RegistryNamespaced()
    {
        this.field_148758_b = ((BiMap)this.registryObjects).inverse();
    }

    public void func_177775_a(int p_177775_1_, K p_177775_2_, V p_177775_3_)
    {
        this.underlyingIntegerMap.put(p_177775_3_, p_177775_1_);
        this.func_82595_a(p_177775_2_, p_177775_3_);
    }

    protected Map<K, V> func_148740_a()
    {
        return HashBiMap.<K, V>create();
    }

    @Nullable
    public V getOrDefault(@Nullable K name)
    {
        return (V)super.getOrDefault(name);
    }

    @Nullable

    /**
     * Gets the name we use to identify the given object.
     */
    public K getKey(V value)
    {
        return this.field_148758_b.get(value);
    }

    public boolean func_148741_d(K p_148741_1_)
    {
        return super.func_148741_d(p_148741_1_);
    }

    /**
     * Gets the integer ID we use to identify the given object.
     */
    public int getId(@Nullable V value)
    {
        return this.underlyingIntegerMap.getId(value);
    }

    @Nullable
    public V func_148754_a(int p_148754_1_)
    {
        return this.underlyingIntegerMap.func_186813_a(p_148754_1_);
    }

    public Iterator<V> iterator()
    {
        return this.underlyingIntegerMap.iterator();
    }
}
