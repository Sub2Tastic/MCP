package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayDeque;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.util.ResourceLocation;

public class FunctionObject
{
    private final FunctionObject.Entry[] entries;

    public FunctionObject(FunctionObject.Entry[] p_i47600_1_)
    {
        this.entries = p_i47600_1_;
    }

    public FunctionObject.Entry[] getEntries()
    {
        return this.entries;
    }

    public static FunctionObject func_193527_a(FunctionManager p_193527_0_, List<String> p_193527_1_)
    {
        List<FunctionObject.Entry> list = Lists.<FunctionObject.Entry>newArrayListWithCapacity(p_193527_1_.size());

        for (String s : p_193527_1_)
        {
            s = s.trim();

            if (!s.startsWith("#") && !s.isEmpty())
            {
                String[] astring = s.split(" ", 2);
                String s1 = astring[0];

                if (!p_193527_0_.func_193062_a().func_71555_a().containsKey(s1))
                {
                    if (s1.startsWith("//"))
                    {
                        throw new IllegalArgumentException("Unknown or invalid command '" + s1 + "' (if you intended to make a comment, use '#' not '//')");
                    }

                    if (s1.startsWith("/") && s1.length() > 1)
                    {
                        throw new IllegalArgumentException("Unknown or invalid command '" + s1 + "' (did you mean '" + s1.substring(1) + "'? Do not use a preceding forwards slash.)");
                    }

                    throw new IllegalArgumentException("Unknown or invalid command '" + s1 + "'");
                }

                list.add(new FunctionObject.CommandEntry(s));
            }
        }

        return new FunctionObject((FunctionObject.Entry[])list.toArray(new FunctionObject.Entry[list.size()]));
    }

    public static class CacheableFunction
    {
        public static final FunctionObject.CacheableFunction EMPTY = new FunctionObject.CacheableFunction((ResourceLocation)null);
        @Nullable
        private final ResourceLocation id;
        private boolean isValid;
        private FunctionObject function;

        public CacheableFunction(@Nullable ResourceLocation idIn)
        {
            this.id = idIn;
        }

        public CacheableFunction(FunctionObject functionIn)
        {
            this.id = null;
            this.function = functionIn;
        }

        @Nullable
        public FunctionObject func_193518_a(FunctionManager p_193518_1_)
        {
            if (!this.isValid)
            {
                if (this.id != null)
                {
                    this.function = p_193518_1_.func_193058_a(this.id);
                }

                this.isValid = true;
            }

            return this.function;
        }

        public String toString()
        {
            return String.valueOf((Object)this.id);
        }
    }

    public static class CommandEntry implements FunctionObject.Entry
    {
        private final String field_193525_a;

        public CommandEntry(String p_i47534_1_)
        {
            this.field_193525_a = p_i47534_1_;
        }

        public void func_194145_a(FunctionManager p_194145_1_, ICommandSender p_194145_2_, ArrayDeque<FunctionManager.QueuedCommand> p_194145_3_, int p_194145_4_)
        {
            p_194145_1_.func_193062_a().func_71556_a(p_194145_2_, this.field_193525_a);
        }

        public String toString()
        {
            return "/" + this.field_193525_a;
        }
    }

    public interface Entry
    {
        void func_194145_a(FunctionManager p_194145_1_, ICommandSender p_194145_2_, ArrayDeque<FunctionManager.QueuedCommand> p_194145_3_, int p_194145_4_);
    }

    public static class FunctionEntry implements FunctionObject.Entry
    {
        private final FunctionObject.CacheableFunction function;

        public FunctionEntry(FunctionObject functionIn)
        {
            this.function = new FunctionObject.CacheableFunction(functionIn);
        }

        public void func_194145_a(FunctionManager p_194145_1_, ICommandSender p_194145_2_, ArrayDeque<FunctionManager.QueuedCommand> p_194145_3_, int p_194145_4_)
        {
            FunctionObject functionobject = this.function.func_193518_a(p_194145_1_);

            if (functionobject != null)
            {
                FunctionObject.Entry[] afunctionobject$entry = functionobject.getEntries();
                int i = p_194145_4_ - p_194145_3_.size();
                int j = Math.min(afunctionobject$entry.length, i);

                for (int k = j - 1; k >= 0; --k)
                {
                    p_194145_3_.addFirst(new FunctionManager.QueuedCommand(p_194145_1_, p_194145_2_, afunctionobject$entry[k]));
                }
            }
        }

        public String toString()
        {
            return "/function " + this.function;
        }
    }
}
