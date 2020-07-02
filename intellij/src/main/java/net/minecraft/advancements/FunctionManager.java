package net.minecraft.advancements;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.command.FunctionObject;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FunctionManager implements ITickable
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final File field_193068_b;
    private final MinecraftServer server;
    private final Map<ResourceLocation, FunctionObject> functions = Maps.<ResourceLocation, FunctionObject>newHashMap();
    private String field_193071_e = "-";
    private FunctionObject field_193072_f;
    private final ArrayDeque<FunctionManager.QueuedCommand> commandQueue = new ArrayDeque<FunctionManager.QueuedCommand>();
    private boolean isExecuting = false;
    private final ICommandSender field_193073_g = new ICommandSender()
    {
        public String func_70005_c_()
        {
            return FunctionManager.this.field_193071_e;
        }
        public boolean func_70003_b(int p_70003_1_, String p_70003_2_)
        {
            return p_70003_1_ <= 2;
        }
        public World getEntityWorld()
        {
            return FunctionManager.this.server.worlds[0];
        }
        public MinecraftServer getServer()
        {
            return FunctionManager.this.server;
        }
    };

    public FunctionManager(@Nullable File p_i47517_1_, MinecraftServer p_i47517_2_)
    {
        this.field_193068_b = p_i47517_1_;
        this.server = p_i47517_2_;
        this.func_193059_f();
    }

    @Nullable
    public FunctionObject func_193058_a(ResourceLocation p_193058_1_)
    {
        return this.functions.get(p_193058_1_);
    }

    public ICommandManager func_193062_a()
    {
        return this.server.func_71187_D();
    }

    public int getMaxCommandChainLength()
    {
        return this.server.worlds[0].getGameRules().func_180263_c("maxCommandChainLength");
    }

    public Map<ResourceLocation, FunctionObject> getFunctions()
    {
        return this.functions;
    }

    public void tick()
    {
        String s = this.server.worlds[0].getGameRules().func_82767_a("gameLoopFunction");

        if (!s.equals(this.field_193071_e))
        {
            this.field_193071_e = s;
            this.field_193072_f = this.func_193058_a(new ResourceLocation(s));
        }

        if (this.field_193072_f != null)
        {
            this.func_194019_a(this.field_193072_f, this.field_193073_g);
        }
    }

    public int func_194019_a(FunctionObject p_194019_1_, ICommandSender p_194019_2_)
    {
        int i = this.getMaxCommandChainLength();

        if (this.isExecuting)
        {
            if (this.commandQueue.size() < i)
            {
                this.commandQueue.addFirst(new FunctionManager.QueuedCommand(this, p_194019_2_, new FunctionObject.FunctionEntry(p_194019_1_)));
            }

            return 0;
        }
        else
        {
            int l;

            try
            {
                this.isExecuting = true;
                int j = 0;
                FunctionObject.Entry[] afunctionobject$entry = p_194019_1_.getEntries();

                for (int k = afunctionobject$entry.length - 1; k >= 0; --k)
                {
                    this.commandQueue.push(new FunctionManager.QueuedCommand(this, p_194019_2_, afunctionobject$entry[k]));
                }

                while (true)
                {
                    if (this.commandQueue.isEmpty())
                    {
                        l = j;
                        return l;
                    }

                    (this.commandQueue.removeFirst()).execute(this.commandQueue, i);
                    ++j;

                    if (j >= i)
                    {
                        break;
                    }
                }

                l = j;
            }
            finally
            {
                this.commandQueue.clear();
                this.isExecuting = false;
            }

            return l;
        }
    }

    public void func_193059_f()
    {
        this.functions.clear();
        this.field_193072_f = null;
        this.field_193071_e = "-";
        this.func_193061_h();
    }

    private void func_193061_h()
    {
        if (this.field_193068_b != null)
        {
            this.field_193068_b.mkdirs();

            for (File file1 : FileUtils.listFiles(this.field_193068_b, new String[] {"mcfunction"}, true))
            {
                String s = FilenameUtils.removeExtension(this.field_193068_b.toURI().relativize(file1.toURI()).toString());
                String[] astring = s.split("/", 2);

                if (astring.length == 2)
                {
                    ResourceLocation resourcelocation = new ResourceLocation(astring[0], astring[1]);

                    try
                    {
                        this.functions.put(resourcelocation, FunctionObject.func_193527_a(this, Files.readLines(file1, StandardCharsets.UTF_8)));
                    }
                    catch (Throwable throwable)
                    {
                        LOGGER.error("Couldn't read custom function " + resourcelocation + " from " + file1, throwable);
                    }
                }
            }

            if (!this.functions.isEmpty())
            {
                LOGGER.info("Loaded " + this.functions.size() + " custom command functions");
            }
        }
    }

    public static class QueuedCommand
    {
        private final FunctionManager functionManager;
        private final ICommandSender sender;
        private final FunctionObject.Entry entry;

        public QueuedCommand(FunctionManager p_i47603_1_, ICommandSender p_i47603_2_, FunctionObject.Entry p_i47603_3_)
        {
            this.functionManager = p_i47603_1_;
            this.sender = p_i47603_2_;
            this.entry = p_i47603_3_;
        }

        public void execute(ArrayDeque<FunctionManager.QueuedCommand> commandQueue, int maxCommandChainLength)
        {
            this.entry.func_194145_a(this.functionManager, this.sender, commandQueue, maxCommandChainLength);
        }

        public String toString()
        {
            return this.entry.toString();
        }
    }
}
