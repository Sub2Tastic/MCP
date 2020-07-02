package net.minecraft.client.util;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.util.ResourceLocation;

public class SearchTree<T> implements ISearchTree<T>
{
    protected SuffixArray<T> byName = new SuffixArray<T>();
    protected SuffixArray<T> field_194045_b = new SuffixArray<T>();
    private final Function<T, Iterable<String>> nameFunc;
    private final Function<T, Iterable<ResourceLocation>> field_194047_d;
    private final List<T> field_194048_e = Lists.<T>newArrayList();
    private Object2IntMap<T> field_194049_f = new Object2IntOpenHashMap<T>();

    public SearchTree(Function<T, Iterable<String>> nameFuncIn, Function<T, Iterable<ResourceLocation>> idFuncIn)
    {
        this.nameFunc = nameFuncIn;
        this.field_194047_d = idFuncIn;
    }

    /**
     * Recalculates the contents of this search tree, reapplying {@link #nameFunc} and {@link #idFunc}. Should be called
     * whenever resources are reloaded (e.g. language changes).
     */
    public void recalculate()
    {
        this.byName = new SuffixArray<T>();
        this.field_194045_b = new SuffixArray<T>();

        for (T t : this.field_194048_e)
        {
            this.index(t);
        }

        this.byName.generate();
        this.field_194045_b.generate();
    }

    public void func_194043_a(T p_194043_1_)
    {
        this.field_194049_f.put(p_194043_1_, this.field_194048_e.size());
        this.field_194048_e.add(p_194043_1_);
        this.index(p_194043_1_);
    }

    /**
     * Directly puts the given item into {@link #byId} and {@link #byName}, applying {@link #nameFunc} and {@link
     * idFunc}.
     */
    private void index(T element)
    {
        (this.field_194047_d.apply(element)).forEach((p_194039_2_) ->
        {
            this.field_194045_b.add(element, p_194039_2_.toString().toLowerCase(Locale.ROOT));
        });
        (this.nameFunc.apply(element)).forEach((p_194041_2_) ->
        {
            this.byName.add(element, p_194041_2_.toLowerCase(Locale.ROOT));
        });
    }

    public List<T> search(String searchText)
    {
        List<T> list = this.byName.search(searchText);

        if (searchText.indexOf(58) < 0)
        {
            return list;
        }
        else
        {
            List<T> list1 = this.field_194045_b.search(searchText);
            return (List<T>)(list1.isEmpty() ? list : Lists.newArrayList(new SearchTree.MergingIterator(list.iterator(), list1.iterator(), this.field_194049_f)));
        }
    }

    static class MergingIterator<T> extends AbstractIterator<T>
    {
        private final Iterator<T> leftItr;
        private final Iterator<T> rightItr;
        private final Object2IntMap<T> numbers;
        private T field_194036_d;
        private T field_194037_e;

        public MergingIterator(Iterator<T> p_i47606_1_, Iterator<T> p_i47606_2_, Object2IntMap<T> p_i47606_3_)
        {
            this.leftItr = p_i47606_1_;
            this.rightItr = p_i47606_2_;
            this.numbers = p_i47606_3_;
            this.field_194036_d = (T)(p_i47606_1_.hasNext() ? p_i47606_1_.next() : null);
            this.field_194037_e = (T)(p_i47606_2_.hasNext() ? p_i47606_2_.next() : null);
        }

        protected T computeNext()
        {
            if (this.field_194036_d == null && this.field_194037_e == null)
            {
                return (T)this.endOfData();
            }
            else
            {
                int i;

                if (this.field_194036_d == this.field_194037_e)
                {
                    i = 0;
                }
                else if (this.field_194036_d == null)
                {
                    i = 1;
                }
                else if (this.field_194037_e == null)
                {
                    i = -1;
                }
                else
                {
                    i = Integer.compare(this.numbers.getInt(this.field_194036_d), this.numbers.getInt(this.field_194037_e));
                }

                T t = (T)(i <= 0 ? this.field_194036_d : this.field_194037_e);

                if (i <= 0)
                {
                    this.field_194036_d = (T)(this.leftItr.hasNext() ? this.leftItr.next() : null);
                }

                if (i >= 0)
                {
                    this.field_194037_e = (T)(this.rightItr.hasNext() ? this.rightItr.next() : null);
                }

                return t;
            }
        }
    }
}
