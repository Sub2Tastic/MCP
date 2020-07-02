package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;

public class GuiIngameMenu extends GuiScreen
{
    private int field_146445_a;
    private int field_146444_f;

    public void func_73866_w_()
    {
        this.field_146445_a = 0;
        this.field_146292_n.clear();
        int i = -16;
        int j = 98;
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 120 + -16, I18n.format("menu.returnToMenu")));

        if (!this.field_146297_k.isIntegratedServerRunning())
        {
            (this.field_146292_n.get(0)).field_146126_j = I18n.format("menu.disconnect");
        }

        this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 24 + -16, I18n.format("menu.returnToGame")));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 96 + -16, 98, 20, I18n.format("menu.options")));
        GuiButton guibutton = this.func_189646_b(new GuiButton(7, this.field_146294_l / 2 + 2, this.field_146295_m / 4 + 96 + -16, 98, 20, I18n.format("menu.shareToLan")));
        guibutton.field_146124_l = this.field_146297_k.isSingleplayer() && !this.field_146297_k.getIntegratedServer().getPublic();
        this.field_146292_n.add(new GuiButton(5, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48 + -16, 98, 20, I18n.format("gui.advancements")));
        this.field_146292_n.add(new GuiButton(6, this.field_146294_l / 2 + 2, this.field_146295_m / 4 + 48 + -16, 98, 20, I18n.format("gui.stats")));
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        switch (p_146284_1_.field_146127_k)
        {
            case 0:
                this.field_146297_k.displayGuiScreen(new GuiOptions(this, this.field_146297_k.gameSettings));
                break;

            case 1:
                boolean flag = this.field_146297_k.isIntegratedServerRunning();
                boolean flag1 = this.field_146297_k.isConnectedToRealms();
                p_146284_1_.field_146124_l = false;
                this.field_146297_k.world.sendQuittingDisconnectingPacket();
                this.field_146297_k.loadWorld((WorldClient)null);

                if (flag)
                {
                    this.field_146297_k.displayGuiScreen(new GuiMainMenu());
                }
                else if (flag1)
                {
                    RealmsBridge realmsbridge = new RealmsBridge();
                    realmsbridge.switchToRealms(new GuiMainMenu());
                }
                else
                {
                    this.field_146297_k.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
                }

            case 2:
            case 3:
            default:
                break;

            case 4:
                this.field_146297_k.displayGuiScreen((GuiScreen)null);
                this.field_146297_k.func_71381_h();
                break;

            case 5:
                this.field_146297_k.displayGuiScreen(new GuiScreenAdvancements(this.field_146297_k.player.connection.getAdvancementManager()));
                break;

            case 6:
                this.field_146297_k.displayGuiScreen(new GuiStats(this, this.field_146297_k.player.getStats()));
                break;

            case 7:
                this.field_146297_k.displayGuiScreen(new GuiShareToLan(this));
        }
    }

    public void func_73876_c()
    {
        super.func_73876_c();
        ++this.field_146444_f;
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        this.func_73732_a(this.field_146289_q, I18n.format("menu.game"), this.field_146294_l / 2, 40, 16777215);
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
