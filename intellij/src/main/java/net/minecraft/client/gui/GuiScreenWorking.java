package net.minecraft.client.gui;

import net.minecraft.util.IProgressUpdate;

public class GuiScreenWorking extends GuiScreen implements IProgressUpdate
{
    private String title = "";
    private String stage = "";
    private int progress;
    private boolean doneWorking;

    public void func_73720_a(String p_73720_1_)
    {
        this.func_73721_b(p_73720_1_);
    }

    public void func_73721_b(String p_73721_1_)
    {
        this.title = p_73721_1_;
        this.func_73719_c("Working...");
    }

    public void func_73719_c(String p_73719_1_)
    {
        this.stage = p_73719_1_;
        this.setLoadingProgress(0);
    }

    /**
     * Updates the progress bar on the loading screen to the specified amount.
     */
    public void setLoadingProgress(int progress)
    {
        this.progress = progress;
    }

    public void setDoneWorking()
    {
        this.doneWorking = true;
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        if (this.doneWorking)
        {
            if (!this.field_146297_k.isConnectedToRealms())
            {
                this.field_146297_k.displayGuiScreen((GuiScreen)null);
            }
        }
        else
        {
            this.func_146276_q_();
            this.func_73732_a(this.field_146289_q, this.title, this.field_146294_l / 2, 70, 16777215);
            this.func_73732_a(this.field_146289_q, this.stage + " " + this.progress + "%", this.field_146294_l / 2, 90, 16777215);
            super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
        }
    }
}
