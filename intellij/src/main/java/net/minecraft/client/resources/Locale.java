package net.minecraft.client.resources;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

public class Locale
{
    private static final Splitter field_135030_b = Splitter.on('=').limit(2);
    private static final Pattern PATTERN = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    Map<String, String> properties = Maps.<String, String>newHashMap();
    private boolean field_135029_d;

    public synchronized void func_135022_a(IResourceManager p_135022_1_, List<String> p_135022_2_)
    {
        this.properties.clear();

        for (String s : p_135022_2_)
        {
            String s1 = String.format("lang/%s.lang", s);

            for (String s2 : p_135022_1_.func_135055_a())
            {
                try
                {
                    this.loadLocaleData(p_135022_1_.func_135056_b(new ResourceLocation(s2, s1)));
                }
                catch (IOException var9)
                {
                    ;
                }
            }
        }

        this.func_135024_b();
    }

    public boolean func_135025_a()
    {
        return this.field_135029_d;
    }

    private void func_135024_b()
    {
        this.field_135029_d = false;
        int i = 0;
        int j = 0;

        for (String s : this.properties.values())
        {
            int k = s.length();
            j += k;

            for (int l = 0; l < k; ++l)
            {
                if (s.charAt(l) >= 256)
                {
                    ++i;
                }
            }
        }

        float f = (float)i / (float)j;
        this.field_135029_d = (double)f > 0.1D;
    }

    /**
     * Loads the locale data for the list of resources.
     */
    private void loadLocaleData(List<IResource> resourcesList) throws IOException
    {
        for (IResource iresource : resourcesList)
        {
            InputStream inputstream = iresource.func_110527_b();

            try
            {
                this.loadLocaleData(inputstream);
            }
            finally
            {
                IOUtils.closeQuietly(inputstream);
            }
        }
    }

    private void loadLocaleData(InputStream inputStreamIn) throws IOException
    {
        for (String s : IOUtils.readLines(inputStreamIn, StandardCharsets.UTF_8))
        {
            if (!s.isEmpty() && s.charAt(0) != '#')
            {
                String[] astring = (String[])Iterables.toArray(field_135030_b.split(s), String.class);

                if (astring != null && astring.length == 2)
                {
                    String s1 = astring[0];
                    String s2 = PATTERN.matcher(astring[1]).replaceAll("%$1s");
                    this.properties.put(s1, s2);
                }
            }
        }
    }

    /**
     * Returns the translation, or the key itself if the key could not be translated.
     */
    private String translateKeyPrivate(String translateKey)
    {
        String s = this.properties.get(translateKey);
        return s == null ? translateKey : s;
    }

    /**
     * Calls String.format(translateKey(key), params)
     */
    public String formatMessage(String translateKey, Object[] parameters)
    {
        String s = this.translateKeyPrivate(translateKey);

        try
        {
            return String.format(s, parameters);
        }
        catch (IllegalFormatException var5)
        {
            return "Format error: " + s;
        }
    }

    public boolean hasKey(String key)
    {
        return this.properties.containsKey(key);
    }
}
