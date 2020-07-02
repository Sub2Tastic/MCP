package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.resources.I18n;

public class GuiYesNo extends GuiScreen
{
    protected GuiYesNoCallback field_146355_a;
    protected String field_146351_f;
    private final String messageLine2;
    private final List<String> listLines = Lists.<String>newArrayList();

    /** The text shown for the first button in GuiYesNo */
    protected String confirmButtonText;

    /** The text shown for the second button in GuiYesNo */
    protected String cancelButtonText;
    protected int field_146357_i;
    private int ticksUntilEnable;

    public GuiYesNo(GuiYesNoCallback p_i1082_1_, String p_i1082_2_, String p_i1082_3_, int p_i1082_4_)
    {
        this.field_146355_a = p_i1082_1_;
        this.field_146351_f = p_i1082_2_;
        this.messageLine2 = p_i1082_3_;
        this.field_146357_i = p_i1082_4_;
        this.confirmButtonText = I18n.format("gui.yes");
        this.cancelButtonText = I18n.format("gui.no");
    }

    public GuiYesNo(GuiYesNoCallback p_i1083_1_, String p_i1083_2_, String p_i1083_3_, String p_i1083_4_, String p_i1083_5_, int p_i1083_6_)
    {
        this.field_146355_a = p_i1083_1_;
        this.field_146351_f = p_i1083_2_;
        this.messageLine2 = p_i1083_3_;
        this.confirmButtonText = p_i1083_4_;
        this.cancelButtonText = p_i1083_5_;
        this.field_146357_i = p_i1083_6_;
    }

    public void func_73866_w_()
    {
        this.field_146292_n.add(new GuiOptionButton(0, this.field_146294_l / 2 - 155, this.field_146295_m / 6 + 96, this.confirmButtonText));
        this.field_146292_n.add(new GuiOptionButton(1, this.field_146294_l / 2 - 155 + 160, this.field_146295_m / 6 + 96, this.cancelButtonText));
        this.listLines.clear();
        this.listLines.addAll(this.field_146289_q.listFormattedStringToWidth(this.messageLine2, this.field_146294_l - 50));
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        this.field_146355_a.func_73878_a(p_146284_1_.field_146127_k == 0, this.field_146357_i);
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        this.func_73732_a(this.field_146289_q, this.field_146351_f, this.field_146294_l / 2, 70, 16777215);
        int i = 90;

        for (String s : this.listLines)
        {
            this.func_73732_a(this.field_146289_q, s, this.field_146294_l / 2, i, 16777215);
            i += this.field_146289_q.FONT_HEIGHT;
        }

        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    /**
     * Sets the number of ticks to wait before enabling the buttons.
     */
    public void setButtonDelay(int ticksUntilEnableIn)
    {
        this.ticksUntilEnable = ticksUntilEnableIn;

        for (GuiButton guibutton : this.field_146292_n)
        {
            guibutton.field_146124_l = false;
        }
    }

    public void func_73876_c()
    {
        super.func_73876_c();

        if (--this.ticksUntilEnable == 0)
        {
            for (GuiButton guibutton : this.field_146292_n)
            {
                guibutton.field_146124_l = true;
            }
        }
    }
}
