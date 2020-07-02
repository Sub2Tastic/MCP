package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;

public class GuiConfirmOpenLink extends GuiYesNo
{
    /** Text to warn players from opening unsafe links. */
    private final String openLinkWarning;

    /** Label for the Copy to Clipboard button. */
    private final String copyLinkButtonText;
    private final String linkText;
    private boolean showSecurityWarning = true;

    public GuiConfirmOpenLink(GuiYesNoCallback p_i1084_1_, String p_i1084_2_, int p_i1084_3_, boolean p_i1084_4_)
    {
        super(p_i1084_1_, I18n.format(p_i1084_4_ ? "chat.link.confirmTrusted" : "chat.link.confirm"), p_i1084_2_, p_i1084_3_);
        this.confirmButtonText = I18n.format(p_i1084_4_ ? "chat.link.open" : "gui.yes");
        this.cancelButtonText = I18n.format(p_i1084_4_ ? "gui.cancel" : "gui.no");
        this.copyLinkButtonText = I18n.format("chat.copy");
        this.openLinkWarning = I18n.format("chat.link.warning");
        this.linkText = p_i1084_2_;
    }

    public void func_73866_w_()
    {
        super.func_73866_w_();
        this.field_146292_n.clear();
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 50 - 105, this.field_146295_m / 6 + 96, 100, 20, this.confirmButtonText));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 50, this.field_146295_m / 6 + 96, 100, 20, this.copyLinkButtonText));
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 50 + 105, this.field_146295_m / 6 + 96, 100, 20, this.cancelButtonText));
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146127_k == 2)
        {
            this.copyLinkToClipboard();
        }

        this.field_146355_a.func_73878_a(p_146284_1_.field_146127_k == 0, this.field_146357_i);
    }

    /**
     * Copies the link to the system clipboard.
     */
    public void copyLinkToClipboard()
    {
        func_146275_d(this.linkText);
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);

        if (this.showSecurityWarning)
        {
            this.func_73732_a(this.field_146289_q, this.openLinkWarning, this.field_146294_l / 2, 110, 16764108);
        }
    }

    public void func_146358_g()
    {
        this.showSecurityWarning = false;
    }
}
