package net.minecraft.util.registry;

import java.util.Set;
import javax.annotation.Nullable;

public interface IRegistry<K, V> extends Iterable<V>
{
    @Nullable
    V getOrDefault(K name);

    void func_82595_a(K p_82595_1_, V p_82595_2_);

    Set<K> keySet();
}
