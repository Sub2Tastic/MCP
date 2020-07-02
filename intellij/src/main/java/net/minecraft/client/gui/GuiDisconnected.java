package net.minecraft.client.gui;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;

public class GuiDisconnected extends GuiScreen
{
    private final String field_146306_a;
    private final ITextComponent message;
    private List<String> multilineMessage;
    private final GuiScreen nextScreen;
    private int textHeight;

    public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, ITextComponent chatComp)
    {
        this.nextScreen = screen;
        this.field_146306_a = I18n.format(reasonLocalizationKey);
        this.message = chatComp;
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
    }

    public void func_73866_w_()
    {
        this.field_146292_n.clear();
        this.multilineMessage = this.field_146289_q.listFormattedStringToWidth(this.message.getFormattedText(), this.field_146294_l - 50);
        this.textHeight = this.multilineMessage.size() * this.field_146289_q.FONT_HEIGHT;
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, Math.min(this.field_146295_m / 2 + this.textHeight / 2 + this.field_146289_q.FONT_HEIGHT, this.field_146295_m - 30), I18n.format("gui.toMenu")));
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146127_k == 0)
        {
            this.field_146297_k.displayGuiScreen(this.nextScreen);
        }
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        this.func_73732_a(this.field_146289_q, this.field_146306_a, this.field_146294_l / 2, this.field_146295_m / 2 - this.textHeight / 2 - this.field_146289_q.FONT_HEIGHT * 2, 11184810);
        int i = this.field_146295_m / 2 - this.textHeight / 2;

        if (this.multilineMessage != null)
        {
            for (String s : this.multilineMessage)
            {
                this.func_73732_a(this.field_146289_q, s, this.field_146294_l / 2, i, 16777215);
                i += this.field_146289_q.FONT_HEIGHT;
            }
        }

        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
