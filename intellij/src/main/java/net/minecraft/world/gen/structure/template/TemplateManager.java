package net.minecraft.world.gen.structure.template;

import com.google.common.collect.Maps;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import org.apache.commons.io.IOUtils;

public class TemplateManager
{
    private final Map<String, Template> templates = Maps.<String, Template>newHashMap();
    private final String field_186241_b;
    private final DataFixer fixer;

    public TemplateManager(String p_i47239_1_, DataFixer p_i47239_2_)
    {
        this.field_186241_b = p_i47239_1_;
        this.fixer = p_i47239_2_;
    }

    public Template func_186237_a(@Nullable MinecraftServer p_186237_1_, ResourceLocation p_186237_2_)
    {
        Template template = this.func_189942_b(p_186237_1_, p_186237_2_);

        if (template == null)
        {
            template = new Template();
            this.templates.put(p_186237_2_.getPath(), template);
        }

        return template;
    }

    @Nullable
    public Template func_189942_b(@Nullable MinecraftServer p_189942_1_, ResourceLocation p_189942_2_)
    {
        String s = p_189942_2_.getPath();

        if (this.templates.containsKey(s))
        {
            return this.templates.get(s);
        }
        else
        {
            if (p_189942_1_ == null)
            {
                this.func_186236_a(p_189942_2_);
            }
            else
            {
                this.func_186235_b(p_189942_2_);
            }

            return this.templates.containsKey(s) ? (Template)this.templates.get(s) : null;
        }
    }

    public boolean func_186235_b(ResourceLocation p_186235_1_)
    {
        String s = p_186235_1_.getPath();
        File file1 = new File(this.field_186241_b, s + ".nbt");

        if (!file1.exists())
        {
            return this.func_186236_a(p_186235_1_);
        }
        else
        {
            InputStream inputstream = null;
            boolean flag;

            try
            {
                inputstream = new FileInputStream(file1);
                this.func_186239_a(s, inputstream);
                return true;
            }
            catch (Throwable var10)
            {
                flag = false;
            }
            finally
            {
                IOUtils.closeQuietly(inputstream);
            }

            return flag;
        }
    }

    private boolean func_186236_a(ResourceLocation p_186236_1_)
    {
        String s = p_186236_1_.getNamespace();
        String s1 = p_186236_1_.getPath();
        InputStream inputstream = null;
        boolean flag;

        try
        {
            inputstream = MinecraftServer.class.getResourceAsStream("/assets/" + s + "/structures/" + s1 + ".nbt");
            this.func_186239_a(s1, inputstream);
            return true;
        }
        catch (Throwable var10)
        {
            flag = false;
        }
        finally
        {
            IOUtils.closeQuietly(inputstream);
        }

        return flag;
    }

    private void func_186239_a(String p_186239_1_, InputStream p_186239_2_) throws IOException
    {
        NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(p_186239_2_);

        if (!nbttagcompound.contains("DataVersion", 99))
        {
            nbttagcompound.putInt("DataVersion", 500);
        }

        Template template = new Template();
        template.read(this.fixer.func_188257_a(FixTypes.STRUCTURE, nbttagcompound));
        this.templates.put(p_186239_1_, template);
    }

    public boolean func_186238_c(@Nullable MinecraftServer p_186238_1_, ResourceLocation p_186238_2_)
    {
        String s = p_186238_2_.getPath();

        if (p_186238_1_ != null && this.templates.containsKey(s))
        {
            File file1 = new File(this.field_186241_b);

            if (!file1.exists())
            {
                if (!file1.mkdirs())
                {
                    return false;
                }
            }
            else if (!file1.isDirectory())
            {
                return false;
            }

            File file2 = new File(file1, s + ".nbt");
            Template template = this.templates.get(s);
            OutputStream outputstream = null;
            boolean flag;

            try
            {
                NBTTagCompound nbttagcompound = template.writeToNBT(new NBTTagCompound());
                outputstream = new FileOutputStream(file2);
                CompressedStreamTools.writeCompressed(nbttagcompound, outputstream);
                return true;
            }
            catch (Throwable var13)
            {
                flag = false;
            }
            finally
            {
                IOUtils.closeQuietly(outputstream);
            }

            return flag;
        }
        else
        {
            return false;
        }
    }

    public void remove(ResourceLocation templatePath)
    {
        this.templates.remove(templatePath.getPath());
    }
}
