package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

public abstract class BlockStateBase implements IBlockState
{
    private static final Joiner field_177234_a = Joiner.on(',');
    private static final Function < Entry < IProperty<?>, Comparable<? >> , String > MAP_ENTRY_TO_STRING = new Function < Entry < IProperty<?>, Comparable<? >> , String > ()
    {
        @Nullable
        public String apply(@Nullable Entry < IProperty<?>, Comparable<? >> p_apply_1_)
        {
            if (p_apply_1_ == null)
            {
                return "<NULL>";
            }
            else
            {
                IProperty<?> iproperty = (IProperty)p_apply_1_.getKey();
                return iproperty.getName() + "=" + this.getPropertyName(iproperty, p_apply_1_.getValue());
            }
        }
        private <T extends Comparable<T>> String getPropertyName(IProperty<T> property, Comparable<?> entry)
        {
            return property.getName((T)entry);
        }
    };

    public <T extends Comparable<T>> IBlockState cycle(IProperty<T> property)
    {
        return this.func_177226_a(property, cyclePropertyValue(property.getAllowedValues(), this.get(property)));
    }

    protected static <T> T cyclePropertyValue(Collection<T> values, T currentValue)
    {
        Iterator<T> iterator = values.iterator();

        while (iterator.hasNext())
        {
            if (iterator.next().equals(currentValue))
            {
                if (iterator.hasNext())
                {
                    return iterator.next();
                }

                return values.iterator().next();
            }
        }

        return iterator.next();
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(Block.field_149771_c.getKey(this.getBlock()));

        if (!this.func_177228_b().isEmpty())
        {
            stringbuilder.append("[");
            field_177234_a.appendTo(stringbuilder, Iterables.transform(this.func_177228_b().entrySet(), MAP_ENTRY_TO_STRING));
            stringbuilder.append("]");
        }

        return stringbuilder.toString();
    }
}
