package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

public class GuiScreenServerList extends GuiScreen
{
    private final GuiScreen field_146303_a;
    private final ServerData serverData;
    private GuiTextField ipEdit;

    public GuiScreenServerList(GuiScreen p_i1031_1_, ServerData p_i1031_2_)
    {
        this.field_146303_a = p_i1031_1_;
        this.serverData = p_i1031_2_;
    }

    public void func_73876_c()
    {
        this.ipEdit.tick();
    }

    public void func_73866_w_()
    {
        Keyboard.enableRepeatEvents(true);
        this.field_146292_n.clear();
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 96 + 12, I18n.format("selectServer.select")));
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 120 + 12, I18n.format("gui.cancel")));
        this.ipEdit = new GuiTextField(2, this.field_146289_q, this.field_146294_l / 2 - 100, 116, 200, 20);
        this.ipEdit.setMaxStringLength(128);
        this.ipEdit.setFocused2(true);
        this.ipEdit.setText(this.field_146297_k.gameSettings.lastServer);
        (this.field_146292_n.get(0)).field_146124_l = !this.ipEdit.getText().isEmpty() && this.ipEdit.getText().split(":").length > 0;
    }

    public void func_146281_b()
    {
        Keyboard.enableRepeatEvents(false);
        this.field_146297_k.gameSettings.lastServer = this.ipEdit.getText();
        this.field_146297_k.gameSettings.saveOptions();
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146124_l)
        {
            if (p_146284_1_.field_146127_k == 1)
            {
                this.field_146303_a.func_73878_a(false, 0);
            }
            else if (p_146284_1_.field_146127_k == 0)
            {
                this.serverData.serverIP = this.ipEdit.getText();
                this.field_146303_a.func_73878_a(true, 0);
            }
        }
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
        if (this.ipEdit.func_146201_a(p_73869_1_, p_73869_2_))
        {
            (this.field_146292_n.get(0)).field_146124_l = !this.ipEdit.getText().isEmpty() && this.ipEdit.getText().split(":").length > 0;
        }
        else if (p_73869_2_ == 28 || p_73869_2_ == 156)
        {
            this.func_146284_a(this.field_146292_n.get(0));
        }
    }

    protected void func_73864_a(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
    {
        super.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);
        this.ipEdit.func_146192_a(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        this.func_73732_a(this.field_146289_q, I18n.format("selectServer.direct"), this.field_146294_l / 2, 20, 16777215);
        this.func_73731_b(this.field_146289_q, I18n.format("addServer.enterIp"), this.field_146294_l / 2 - 100, 100, 10526880);
        this.ipEdit.func_146194_f();
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
