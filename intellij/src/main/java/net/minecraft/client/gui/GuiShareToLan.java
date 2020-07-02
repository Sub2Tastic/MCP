package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;

public class GuiShareToLan extends GuiScreen
{
    private final GuiScreen lastScreen;
    private GuiButton allowCheatsButton;
    private GuiButton gameModeButton;
    private String gameMode = "survival";
    private boolean allowCheats;

    public GuiShareToLan(GuiScreen lastScreenIn)
    {
        this.lastScreen = lastScreenIn;
    }

    public void func_73866_w_()
    {
        this.field_146292_n.clear();
        this.field_146292_n.add(new GuiButton(101, this.field_146294_l / 2 - 155, this.field_146295_m - 28, 150, 20, I18n.format("lanServer.start")));
        this.field_146292_n.add(new GuiButton(102, this.field_146294_l / 2 + 5, this.field_146295_m - 28, 150, 20, I18n.format("gui.cancel")));
        this.gameModeButton = this.func_189646_b(new GuiButton(104, this.field_146294_l / 2 - 155, 100, 150, 20, I18n.format("selectWorld.gameMode")));
        this.allowCheatsButton = this.func_189646_b(new GuiButton(103, this.field_146294_l / 2 + 5, 100, 150, 20, I18n.format("selectWorld.allowCommands")));
        this.updateDisplayNames();
    }

    private void updateDisplayNames()
    {
        this.gameModeButton.field_146126_j = I18n.format("selectWorld.gameMode") + ": " + I18n.format("selectWorld.gameMode." + this.gameMode);
        this.allowCheatsButton.field_146126_j = I18n.format("selectWorld.allowCommands") + " ";

        if (this.allowCheats)
        {
            this.allowCheatsButton.field_146126_j = this.allowCheatsButton.field_146126_j + I18n.format("options.on");
        }
        else
        {
            this.allowCheatsButton.field_146126_j = this.allowCheatsButton.field_146126_j + I18n.format("options.off");
        }
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146127_k == 102)
        {
            this.field_146297_k.displayGuiScreen(this.lastScreen);
        }
        else if (p_146284_1_.field_146127_k == 104)
        {
            if ("spectator".equals(this.gameMode))
            {
                this.gameMode = "creative";
            }
            else if ("creative".equals(this.gameMode))
            {
                this.gameMode = "adventure";
            }
            else if ("adventure".equals(this.gameMode))
            {
                this.gameMode = "survival";
            }
            else
            {
                this.gameMode = "spectator";
            }

            this.updateDisplayNames();
        }
        else if (p_146284_1_.field_146127_k == 103)
        {
            this.allowCheats = !this.allowCheats;
            this.updateDisplayNames();
        }
        else if (p_146284_1_.field_146127_k == 101)
        {
            this.field_146297_k.displayGuiScreen((GuiScreen)null);
            String s = this.field_146297_k.getIntegratedServer().func_71206_a(GameType.getByName(this.gameMode), this.allowCheats);
            ITextComponent itextcomponent;

            if (s != null)
            {
                itextcomponent = new TextComponentTranslation("commands.publish.started", new Object[] {s});
            }
            else
            {
                itextcomponent = new TextComponentString("commands.publish.failed");
            }

            this.field_146297_k.ingameGUI.getChatGUI().printChatMessage(itextcomponent);
        }
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146276_q_();
        this.func_73732_a(this.field_146289_q, I18n.format("lanServer.title"), this.field_146294_l / 2, 50, 16777215);
        this.func_73732_a(this.field_146289_q, I18n.format("lanServer.otherPlayers"), this.field_146294_l / 2, 82, 16777215);
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
