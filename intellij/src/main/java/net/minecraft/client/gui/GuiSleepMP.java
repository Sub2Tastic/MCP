package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.CPacketEntityAction;

public class GuiSleepMP extends GuiChat
{
    public void func_73866_w_()
    {
        super.func_73866_w_();
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m - 40, I18n.format("multiplayer.stopSleeping")));
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
        if (p_73869_2_ == 1)
        {
            this.wakeFromSleep();
        }
        else if (p_73869_2_ != 28 && p_73869_2_ != 156)
        {
            super.func_73869_a(p_73869_1_, p_73869_2_);
        }
        else
        {
            String s = this.inputField.getText().trim();

            if (!s.isEmpty())
            {
                this.field_146297_k.player.sendChatMessage(s);
            }

            this.inputField.setText("");
            this.field_146297_k.ingameGUI.getChatGUI().resetScroll();
        }
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146127_k == 1)
        {
            this.wakeFromSleep();
        }
        else
        {
            super.func_146284_a(p_146284_1_);
        }
    }

    private void wakeFromSleep()
    {
        NetHandlerPlayClient nethandlerplayclient = this.field_146297_k.player.connection;
        nethandlerplayclient.sendPacket(new CPacketEntityAction(this.field_146297_k.player, CPacketEntityAction.Action.STOP_SLEEPING));
    }
}
