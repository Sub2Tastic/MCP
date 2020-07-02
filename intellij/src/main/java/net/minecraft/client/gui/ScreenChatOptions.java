package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class ScreenChatOptions extends GuiScreen
{
    private static final GameSettings.Options[] CHAT_OPTIONS = new GameSettings.Options[] {GameSettings.Options.CHAT_VISIBILITY, GameSettings.Options.CHAT_COLOR, GameSettings.Options.CHAT_LINKS, GameSettings.Options.CHAT_OPACITY, GameSettings.Options.CHAT_LINKS_PROMPT, GameSettings.Options.CHAT_SCALE, GameSettings.Options.CHAT_HEIGHT_FOCUSED, GameSettings.Options.CHAT_HEIGHT_UNFOCUSED, GameSettings.Options.CHAT_WIDTH, GameSettings.Options.REDUCED_DEBUG_INFO, GameSettings.Options.NARRATOR};
    private final GuiScreen field_146396_g;
    private final GameSettings field_146400_h;
    private String field_146401_i;
    private GuiOptionButton narratorButton;

    public ScreenChatOptions(GuiScreen parentScreenIn, GameSettings gameSettingsIn)
    {
        this.field_146396_g = parentScreenIn;
        this.field_146400_h = gameSettingsIn;
    }

    public void func_73866_w_()
    {
        this.field_146401_i = I18n.format("options.chat.title");
        int i = 0;

        for (GameSettings.Options gamesettings$options : CHAT_OPTIONS)
        {
            if (gamesettings$options.func_74380_a())
            {
                this.field_146292_n.add(new GuiOptionSlider(gamesettings$options.func_74381_c(), this.field_146294_l / 2 - 155 + i % 2 * 160, this.field_146295_m / 6 + 24 * (i >> 1), gamesettings$options));
            }
            else
            {
                GuiOptionButton guioptionbutton = new GuiOptionButton(gamesettings$options.func_74381_c(), this.field_146294_l / 2 - 155 + i % 2 * 160, this.field_146295_m / 6 + 24 * (i >> 1), gamesettings$options, this.field_146400_h.func_74297_c(gamesettings$options));
                this.field_146292_n.add(guioptionbutton);

                if (gamesettings$options == GameSettings.Options.NARRATOR)
                {
                    this.narratorButton = guioptionbutton;
                    guioptionbutton.field_146124_l = NarratorChatListener.INSTANCE.isActive();
                }
            }

            ++i;
        }

        this.field_146292_n.add(new GuiButton(200, this.field_146294_l / 2 - 100, this.field_146295_m / 6 + 144, I18n.format("gui.done")));
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
        if (p_73869_2_ == 1)
        {
            this.field_146297_k.gameSettings.saveOptions();
        }

        super.func_73869_a(p_73869_1_, p_73869_2_);
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146124_l)
        {
            if (p_146284_1_.field_146127_k < 100 && p_146284_1_ instanceof GuiOptionButton)
            {
                this.field_146400_h.func_74306_a(((GuiOptionButton)p_146284_1_).func_146136_c(), 1);
                p_146284_1_.field_146126_j = this.field_146400_h.func_74297_c(GameSettings.Options.func_74379_a(p_146284_1_.field_146127_k));
            }

            if (p_146284_1_.field_146127_k == 200)
            {
                this.field_146297_k.gameSettings.saveOptions();
                this.field_146297_k.displayGuiScreen(this.field_146396_g);
            }
        }
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        this.func_73732_a(this.field_146289_q, this.field_146401_i, this.field_146294_l / 2, 20, 16777215);
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    public void updateNarratorButton()
    {
        this.narratorButton.field_146126_j = this.field_146400_h.func_74297_c(GameSettings.Options.func_74379_a(this.narratorButton.field_146127_k));
    }
}
