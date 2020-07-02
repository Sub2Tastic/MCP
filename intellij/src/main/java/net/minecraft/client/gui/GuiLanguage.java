package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;

public class GuiLanguage extends GuiScreen
{
    protected GuiScreen field_146453_a;

    /** The List GuiSlot object reference. */
    private GuiLanguage.List list;
    private final GameSettings field_146451_g;

    /** Reference to the LanguageManager object. */
    private final LanguageManager languageManager;
    private GuiOptionButton field_146455_i;

    /** The button to confirm the current settings. */
    private GuiOptionButton confirmSettingsBtn;

    public GuiLanguage(GuiScreen screen, GameSettings gameSettingsObj, LanguageManager manager)
    {
        this.field_146453_a = screen;
        this.field_146451_g = gameSettingsObj;
        this.languageManager = manager;
    }

    public void func_73866_w_()
    {
        this.field_146455_i = (GuiOptionButton)this.func_189646_b(new GuiOptionButton(100, this.field_146294_l / 2 - 155, this.field_146295_m - 38, GameSettings.Options.FORCE_UNICODE_FONT, this.field_146451_g.func_74297_c(GameSettings.Options.FORCE_UNICODE_FONT)));
        this.confirmSettingsBtn = (GuiOptionButton)this.func_189646_b(new GuiOptionButton(6, this.field_146294_l / 2 - 155 + 160, this.field_146295_m - 38, I18n.format("gui.done")));
        this.list = new GuiLanguage.List(this.field_146297_k);
        this.list.func_148134_d(7, 8);
    }

    public void func_146274_d() throws IOException
    {
        super.func_146274_d();
        this.list.func_178039_p();
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146124_l)
        {
            switch (p_146284_1_.field_146127_k)
            {
                case 5:
                    break;

                case 6:
                    this.field_146297_k.displayGuiScreen(this.field_146453_a);
                    break;

                case 100:
                    if (p_146284_1_ instanceof GuiOptionButton)
                    {
                        this.field_146451_g.func_74306_a(((GuiOptionButton)p_146284_1_).func_146136_c(), 1);
                        p_146284_1_.field_146126_j = this.field_146451_g.func_74297_c(GameSettings.Options.FORCE_UNICODE_FONT);
                        ScaledResolution scaledresolution = new ScaledResolution(this.field_146297_k);
                        int i = scaledresolution.func_78326_a();
                        int j = scaledresolution.func_78328_b();
                        this.func_146280_a(this.field_146297_k, i, j);
                    }

                    break;

                default:
                    this.list.func_148147_a(p_146284_1_);
            }
        }
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.list.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
        this.func_73732_a(this.field_146289_q, I18n.format("options.language"), this.field_146294_l / 2, 16, 16777215);
        this.func_73732_a(this.field_146289_q, "(" + I18n.format("options.languageWarning") + ")", this.field_146294_l / 2, this.field_146295_m - 56, 8421504);
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    class List extends GuiSlot
    {
        private final java.util.List<String> field_148176_l = Lists.<String>newArrayList();
        private final Map<String, Language> field_148177_m = Maps.<String, Language>newHashMap();

        public List(Minecraft mcIn)
        {
            super(mcIn, GuiLanguage.this.field_146294_l, GuiLanguage.this.field_146295_m, 32, GuiLanguage.this.field_146295_m - 65 + 4, 18);

            for (Language language : GuiLanguage.this.languageManager.getLanguages())
            {
                this.field_148177_m.put(language.func_135034_a(), language);
                this.field_148176_l.add(language.func_135034_a());
            }
        }

        protected int func_148127_b()
        {
            return this.field_148176_l.size();
        }

        protected void func_148144_a(int p_148144_1_, boolean p_148144_2_, int p_148144_3_, int p_148144_4_)
        {
            Language language = this.field_148177_m.get(this.field_148176_l.get(p_148144_1_));
            GuiLanguage.this.languageManager.setCurrentLanguage(language);
            GuiLanguage.this.field_146451_g.language = language.func_135034_a();
            this.field_148161_k.func_110436_a();
            GuiLanguage.this.field_146289_q.func_78264_a(GuiLanguage.this.languageManager.func_135042_a() || GuiLanguage.this.field_146451_g.field_151455_aw);
            GuiLanguage.this.field_146289_q.setBidiFlag(GuiLanguage.this.languageManager.isCurrentLanguageBidirectional());
            GuiLanguage.this.confirmSettingsBtn.field_146126_j = I18n.format("gui.done");
            GuiLanguage.this.field_146455_i.field_146126_j = GuiLanguage.this.field_146451_g.func_74297_c(GameSettings.Options.FORCE_UNICODE_FONT);
            GuiLanguage.this.field_146451_g.saveOptions();
        }

        protected boolean func_148131_a(int p_148131_1_)
        {
            return ((String)this.field_148176_l.get(p_148131_1_)).equals(GuiLanguage.this.languageManager.getCurrentLanguage().func_135034_a());
        }

        protected int func_148138_e()
        {
            return this.func_148127_b() * 18;
        }

        protected void func_148123_a()
        {
            GuiLanguage.this.func_146276_q_();
        }

        protected void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_)
        {
            GuiLanguage.this.field_146289_q.setBidiFlag(true);
            GuiLanguage.this.func_73732_a(GuiLanguage.this.field_146289_q, ((Language)this.field_148177_m.get(this.field_148176_l.get(p_192637_1_))).toString(), this.field_148155_a / 2, p_192637_3_ + 1, 16777215);
            GuiLanguage.this.field_146289_q.setBidiFlag(GuiLanguage.this.languageManager.getCurrentLanguage().isBidirectional());
        }
    }
}
