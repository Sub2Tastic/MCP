package net.minecraft.util.registry;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegistrySimple<K, V> implements IRegistry<K, V>
{
    private static final Logger LOGGER0 = LogManager.getLogger();
    protected final Map<K, V> registryObjects = this.func_148740_a();
    private Object[] values;

    protected Map<K, V> func_148740_a()
    {
        return Maps.<K, V>newHashMap();
    }

    @Nullable
    public V getOrDefault(@Nullable K name)
    {
        return this.registryObjects.get(name);
    }

    public void func_82595_a(K p_82595_1_, V p_82595_2_)
    {
        Validate.notNull(p_82595_1_);
        Validate.notNull(p_82595_2_);
        this.values = null;

        if (this.registryObjects.containsKey(p_82595_1_))
        {
            LOGGER0.debug("Adding duplicate key '{}' to registry", p_82595_1_);
        }

        this.registryObjects.put(p_82595_1_, p_82595_2_);
    }

    public Set<K> keySet()
    {
        return Collections.<K>unmodifiableSet(this.registryObjects.keySet());
    }

    @Nullable
    public V getRandom(Random random)
    {
        if (this.values == null)
        {
            Collection<?> collection = this.registryObjects.values();

            if (collection.isEmpty())
            {
                return (V)null;
            }

            this.values = collection.toArray(new Object[collection.size()]);
        }

        return (V)this.values[random.nextInt(this.values.length)];
    }

    public boolean func_148741_d(K p_148741_1_)
    {
        return this.registryObjects.containsKey(p_148741_1_);
    }

    public Iterator<V> iterator()
    {
        return this.registryObjects.values().iterator();
    }
}
