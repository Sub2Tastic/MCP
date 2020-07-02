package net.minecraft.client.resources;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.text.translation.LanguageMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LanguageManager implements IResourceManagerReloadListener
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final MetadataSerializer field_135047_b;
    private String currentLanguage;
    protected static final Locale CURRENT_LOCALE = new Locale();
    private final Map<String, Language> languageMap = Maps.<String, Language>newHashMap();

    public LanguageManager(MetadataSerializer p_i1304_1_, String p_i1304_2_)
    {
        this.field_135047_b = p_i1304_1_;
        this.currentLanguage = p_i1304_2_;
        I18n.setLocale(CURRENT_LOCALE);
    }

    public void parseLanguageMetadata(List<IResourcePack> resourcesPacks)
    {
        this.languageMap.clear();

        for (IResourcePack iresourcepack : resourcesPacks)
        {
            try
            {
                LanguageMetadataSection languagemetadatasection = (LanguageMetadataSection)iresourcepack.func_135058_a(this.field_135047_b, "language");

                if (languagemetadatasection != null)
                {
                    for (Language language : languagemetadatasection.getLanguages())
                    {
                        if (!this.languageMap.containsKey(language.func_135034_a()))
                        {
                            this.languageMap.put(language.func_135034_a(), language);
                        }
                    }
                }
            }
            catch (RuntimeException runtimeexception)
            {
                LOGGER.warn("Unable to parse language metadata section of resourcepack: {}", iresourcepack.func_130077_b(), runtimeexception);
            }
            catch (IOException ioexception)
            {
                LOGGER.warn("Unable to parse language metadata section of resourcepack: {}", iresourcepack.func_130077_b(), ioexception);
            }
        }
    }

    public void func_110549_a(IResourceManager p_110549_1_)
    {
        List<String> list = Lists.newArrayList("en_us");

        if (!"en_us".equals(this.currentLanguage))
        {
            list.add(this.currentLanguage);
        }

        CURRENT_LOCALE.func_135022_a(p_110549_1_, list);
        LanguageMap.replaceWith(CURRENT_LOCALE.properties);
    }

    public boolean func_135042_a()
    {
        return CURRENT_LOCALE.func_135025_a();
    }

    public boolean isCurrentLanguageBidirectional()
    {
        return this.getCurrentLanguage() != null && this.getCurrentLanguage().isBidirectional();
    }

    public void setCurrentLanguage(Language currentLanguageIn)
    {
        this.currentLanguage = currentLanguageIn.func_135034_a();
    }

    public Language getCurrentLanguage()
    {
        String s = this.languageMap.containsKey(this.currentLanguage) ? this.currentLanguage : "en_us";
        return this.languageMap.get(s);
    }

    public SortedSet<Language> getLanguages()
    {
        return Sets.newTreeSet(this.languageMap.values());
    }

    public Language getLanguage(String p_191960_1_)
    {
        return this.languageMap.get(p_191960_1_);
    }
}
