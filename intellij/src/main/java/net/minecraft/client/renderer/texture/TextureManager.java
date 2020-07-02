package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextureManager implements ITickable, IResourceManagerReloadListener
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ResourceLocation RESOURCE_LOCATION_EMPTY = new ResourceLocation("");
    private final Map<ResourceLocation, ITextureObject> mapTextureObjects = Maps.<ResourceLocation, ITextureObject>newHashMap();
    private final List<ITickable> listTickables = Lists.<ITickable>newArrayList();
    private final Map<String, Integer> mapTextureCounters = Maps.<String, Integer>newHashMap();
    private final IResourceManager resourceManager;

    public TextureManager(IResourceManager resourceManager)
    {
        this.resourceManager = resourceManager;
    }

    public void bindTexture(ResourceLocation resource)
    {
        ITextureObject itextureobject = this.mapTextureObjects.get(resource);

        if (itextureobject == null)
        {
            itextureobject = new SimpleTexture(resource);
            this.func_110579_a(resource, itextureobject);
        }

        TextureUtil.func_94277_a(itextureobject.getGlTextureId());
    }

    public boolean func_110580_a(ResourceLocation p_110580_1_, ITickableTextureObject p_110580_2_)
    {
        if (this.func_110579_a(p_110580_1_, p_110580_2_))
        {
            this.listTickables.add(p_110580_2_);
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean func_110579_a(ResourceLocation p_110579_1_, ITextureObject p_110579_2_)
    {
        boolean flag = true;

        try
        {
            p_110579_2_.func_110551_a(this.resourceManager);
        }
        catch (IOException ioexception)
        {
            if (p_110579_1_ != RESOURCE_LOCATION_EMPTY)
            {
                LOGGER.warn("Failed to load texture: {}", p_110579_1_, ioexception);
            }

            p_110579_2_ = TextureUtil.field_111001_a;
            this.mapTextureObjects.put(p_110579_1_, p_110579_2_);
            flag = false;
        }
        catch (Throwable throwable)
        {
            final ITextureObject p_110579_2_f = p_110579_2_;
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Registering texture");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
            crashreportcategory.addDetail("Resource location", p_110579_1_);
            crashreportcategory.addDetail("Texture object class", new ICrashReportDetail<String>()
            {
                public String call() throws Exception
                {
                    return p_110579_2_f.getClass().getName();
                }
            });
            throw new ReportedException(crashreport);
        }

        this.mapTextureObjects.put(p_110579_1_, p_110579_2_);
        return flag;
    }

    public ITextureObject func_110581_b(ResourceLocation p_110581_1_)
    {
        return this.mapTextureObjects.get(p_110581_1_);
    }

    public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture)
    {
        Integer integer = this.mapTextureCounters.get(name);

        if (integer == null)
        {
            integer = Integer.valueOf(1);
        }
        else
        {
            integer = integer.intValue() + 1;
        }

        this.mapTextureCounters.put(name, integer);
        ResourceLocation resourcelocation = new ResourceLocation(String.format("dynamic/%s_%d", name, integer));
        this.func_110579_a(resourcelocation, texture);
        return resourcelocation;
    }

    public void tick()
    {
        for (ITickable itickable : this.listTickables)
        {
            itickable.tick();
        }
    }

    public void deleteTexture(ResourceLocation textureLocation)
    {
        ITextureObject itextureobject = this.func_110581_b(textureLocation);

        if (itextureobject != null)
        {
            TextureUtil.func_147942_a(itextureobject.getGlTextureId());
        }
    }

    public void func_110549_a(IResourceManager p_110549_1_)
    {
        Iterator<Entry<ResourceLocation, ITextureObject>> iterator = this.mapTextureObjects.entrySet().iterator();

        while (iterator.hasNext())
        {
            Entry<ResourceLocation, ITextureObject> entry = (Entry)iterator.next();
            ITextureObject itextureobject = entry.getValue();

            if (itextureobject == TextureUtil.field_111001_a)
            {
                iterator.remove();
            }
            else
            {
                this.func_110579_a(entry.getKey(), itextureobject);
            }
        }
    }
}
