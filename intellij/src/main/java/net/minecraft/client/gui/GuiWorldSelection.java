package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiWorldSelection extends GuiScreen
{
    private static final Logger field_184868_g = LogManager.getLogger();
    protected GuiScreen prevScreen;
    protected String field_184867_f = "Select world";
    private String worldVersTooltip;
    private GuiButton deleteButton;
    private GuiButton selectButton;
    private GuiButton renameButton;
    private GuiButton copyButton;
    private GuiListWorldSelection selectionList;

    public GuiWorldSelection(GuiScreen screenIn)
    {
        this.prevScreen = screenIn;
    }

    public void func_73866_w_()
    {
        this.field_184867_f = I18n.format("selectWorld.title");
        this.selectionList = new GuiListWorldSelection(this, this.field_146297_k, this.field_146294_l, this.field_146295_m, 32, this.field_146295_m - 64, 36);
        this.func_184862_a();
    }

    public void func_146274_d() throws IOException
    {
        super.func_146274_d();
        this.selectionList.func_178039_p();
    }

    public void func_184862_a()
    {
        this.selectButton = this.func_189646_b(new GuiButton(1, this.field_146294_l / 2 - 154, this.field_146295_m - 52, 150, 20, I18n.format("selectWorld.select")));
        this.func_189646_b(new GuiButton(3, this.field_146294_l / 2 + 4, this.field_146295_m - 52, 150, 20, I18n.format("selectWorld.create")));
        this.renameButton = this.func_189646_b(new GuiButton(4, this.field_146294_l / 2 - 154, this.field_146295_m - 28, 72, 20, I18n.format("selectWorld.edit")));
        this.deleteButton = this.func_189646_b(new GuiButton(2, this.field_146294_l / 2 - 76, this.field_146295_m - 28, 72, 20, I18n.format("selectWorld.delete")));
        this.copyButton = this.func_189646_b(new GuiButton(5, this.field_146294_l / 2 + 4, this.field_146295_m - 28, 72, 20, I18n.format("selectWorld.recreate")));
        this.func_189646_b(new GuiButton(0, this.field_146294_l / 2 + 82, this.field_146295_m - 28, 72, 20, I18n.format("gui.cancel")));
        this.selectButton.field_146124_l = false;
        this.deleteButton.field_146124_l = false;
        this.renameButton.field_146124_l = false;
        this.copyButton.field_146124_l = false;
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146124_l)
        {
            GuiListWorldSelectionEntry guilistworldselectionentry = this.selectionList.func_186794_f();

            if (p_146284_1_.field_146127_k == 2)
            {
                if (guilistworldselectionentry != null)
                {
                    guilistworldselectionentry.func_186776_b();
                }
            }
            else if (p_146284_1_.field_146127_k == 1)
            {
                if (guilistworldselectionentry != null)
                {
                    guilistworldselectionentry.func_186774_a();
                }
            }
            else if (p_146284_1_.field_146127_k == 3)
            {
                this.field_146297_k.displayGuiScreen(new GuiCreateWorld(this));
            }
            else if (p_146284_1_.field_146127_k == 4)
            {
                if (guilistworldselectionentry != null)
                {
                    guilistworldselectionentry.func_186778_c();
                }
            }
            else if (p_146284_1_.field_146127_k == 0)
            {
                this.field_146297_k.displayGuiScreen(this.prevScreen);
            }
            else if (p_146284_1_.field_146127_k == 5 && guilistworldselectionentry != null)
            {
                guilistworldselectionentry.func_186779_d();
            }
        }
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.worldVersTooltip = null;
        this.selectionList.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
        this.func_73732_a(this.field_146289_q, this.field_184867_f, this.field_146294_l / 2, 20, 16777215);
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);

        if (this.worldVersTooltip != null)
        {
            this.func_146283_a(Lists.newArrayList(Splitter.on("\n").split(this.worldVersTooltip)), p_73863_1_, p_73863_2_);
        }
    }

    protected void func_73864_a(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
    {
        super.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);
        this.selectionList.func_148179_a(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    protected void func_146286_b(int p_146286_1_, int p_146286_2_, int p_146286_3_)
    {
        super.func_146286_b(p_146286_1_, p_146286_2_, p_146286_3_);
        this.selectionList.func_148181_b(p_146286_1_, p_146286_2_, p_146286_3_);
    }

    /**
     * Called back by selectionList when we call its drawScreen method, from ours.
     */
    public void setVersionTooltip(String p_184861_1_)
    {
        this.worldVersTooltip = p_184861_1_;
    }

    public void func_184863_a(@Nullable GuiListWorldSelectionEntry p_184863_1_)
    {
        boolean flag = p_184863_1_ != null;
        this.selectButton.field_146124_l = flag;
        this.deleteButton.field_146124_l = flag;
        this.renameButton.field_146124_l = flag;
        this.copyButton.field_146124_l = flag;
    }
}
