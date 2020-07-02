package net.minecraft.client.gui;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class GuiGameOver extends GuiScreen
{
    /**
     * The integer value containing the number of ticks that have passed since the player's death
     */
    private int enableButtonsTimer;
    private final ITextComponent causeOfDeath;

    public GuiGameOver(@Nullable ITextComponent p_i46598_1_)
    {
        this.causeOfDeath = p_i46598_1_;
    }

    public void func_73866_w_()
    {
        this.field_146292_n.clear();
        this.enableButtonsTimer = 0;

        if (this.field_146297_k.world.getWorldInfo().isHardcore())
        {
            this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 72, I18n.format("deathScreen.spectate")));
            this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 96, I18n.format("deathScreen." + (this.field_146297_k.isIntegratedServerRunning() ? "deleteWorld" : "leaveServer"))));
        }
        else
        {
            this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 72, I18n.format("deathScreen.respawn")));
            this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 96, I18n.format("deathScreen.titleScreen")));

            if (this.field_146297_k.getSession() == null)
            {
                (this.field_146292_n.get(1)).field_146124_l = false;
            }
        }

        for (GuiButton guibutton : this.field_146292_n)
        {
            guibutton.field_146124_l = false;
        }
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        switch (p_146284_1_.field_146127_k)
        {
            case 0:
                this.field_146297_k.player.respawnPlayer();
                this.field_146297_k.displayGuiScreen((GuiScreen)null);
                break;

            case 1:
                if (this.field_146297_k.world.getWorldInfo().isHardcore())
                {
                    this.field_146297_k.displayGuiScreen(new GuiMainMenu());
                }
                else
                {
                    GuiYesNo guiyesno = new GuiYesNo(this, I18n.format("deathScreen.quit.confirm"), "", I18n.format("deathScreen.titleScreen"), I18n.format("deathScreen.respawn"), 0);
                    this.field_146297_k.displayGuiScreen(guiyesno);
                    guiyesno.setButtonDelay(20);
                }
        }
    }

    public void func_73878_a(boolean p_73878_1_, int p_73878_2_)
    {
        if (p_73878_1_)
        {
            if (this.field_146297_k.world != null)
            {
                this.field_146297_k.world.sendQuittingDisconnectingPacket();
            }

            this.field_146297_k.loadWorld((WorldClient)null);
            this.field_146297_k.displayGuiScreen(new GuiMainMenu());
        }
        else
        {
            this.field_146297_k.player.respawnPlayer();
            this.field_146297_k.displayGuiScreen((GuiScreen)null);
        }
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        boolean flag = this.field_146297_k.world.getWorldInfo().isHardcore();
        this.func_73733_a(0, 0, this.field_146294_l, this.field_146295_m, 1615855616, -1602211792);
        GlStateManager.func_179094_E();
        GlStateManager.func_179152_a(2.0F, 2.0F, 2.0F);
        this.func_73732_a(this.field_146289_q, I18n.format(flag ? "deathScreen.title.hardcore" : "deathScreen.title"), this.field_146294_l / 2 / 2, 30, 16777215);
        GlStateManager.func_179121_F();

        if (this.causeOfDeath != null)
        {
            this.func_73732_a(this.field_146289_q, this.causeOfDeath.getFormattedText(), this.field_146294_l / 2, 85, 16777215);
        }

        this.func_73732_a(this.field_146289_q, I18n.format("deathScreen.score") + ": " + TextFormatting.YELLOW + this.field_146297_k.player.getScore(), this.field_146294_l / 2, 100, 16777215);

        if (this.causeOfDeath != null && p_73863_2_ > 85 && p_73863_2_ < 85 + this.field_146289_q.FONT_HEIGHT)
        {
            ITextComponent itextcomponent = this.getClickedComponentAt(p_73863_1_);

            if (itextcomponent != null && itextcomponent.getStyle().getHoverEvent() != null)
            {
                this.func_175272_a(itextcomponent, p_73863_1_, p_73863_2_);
            }
        }

        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    @Nullable
    public ITextComponent getClickedComponentAt(int p_184870_1_)
    {
        if (this.causeOfDeath == null)
        {
            return null;
        }
        else
        {
            int i = this.field_146297_k.fontRenderer.getStringWidth(this.causeOfDeath.getFormattedText());
            int j = this.field_146294_l / 2 - i / 2;
            int k = this.field_146294_l / 2 + i / 2;
            int l = j;

            if (p_184870_1_ >= j && p_184870_1_ <= k)
            {
                for (ITextComponent itextcomponent : this.causeOfDeath)
                {
                    l += this.field_146297_k.fontRenderer.getStringWidth(GuiUtilRenderComponents.removeTextColorsIfConfigured(itextcomponent.getUnformattedComponentText(), false));

                    if (l > p_184870_1_)
                    {
                        return itextcomponent;
                    }
                }

                return null;
            }
            else
            {
                return null;
            }
        }
    }

    public boolean func_73868_f()
    {
        return false;
    }

    public void func_73876_c()
    {
        super.func_73876_c();
        ++this.enableButtonsTimer;

        if (this.enableButtonsTimer == 20)
        {
            for (GuiButton guibutton : this.field_146292_n)
            {
                guibutton.field_146124_l = true;
            }
        }
    }
}
