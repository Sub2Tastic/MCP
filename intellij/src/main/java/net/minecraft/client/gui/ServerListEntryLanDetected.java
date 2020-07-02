package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.LanServerInfo;
import net.minecraft.client.resources.I18n;

public class ServerListEntryLanDetected implements GuiListExtended.IGuiListEntry
{
    private final GuiMultiplayer screen;
    protected final Minecraft mc;
    protected final LanServerInfo serverData;
    private long lastClickTime;

    protected ServerListEntryLanDetected(GuiMultiplayer p_i47141_1_, LanServerInfo p_i47141_2_)
    {
        this.screen = p_i47141_1_;
        this.serverData = p_i47141_2_;
        this.mc = Minecraft.getInstance();
    }

    public void func_192634_a(int p_192634_1_, int p_192634_2_, int p_192634_3_, int p_192634_4_, int p_192634_5_, int p_192634_6_, int p_192634_7_, boolean p_192634_8_, float p_192634_9_)
    {
        this.mc.fontRenderer.func_78276_b(I18n.format("lanServer.title"), p_192634_2_ + 32 + 3, p_192634_3_ + 1, 16777215);
        this.mc.fontRenderer.func_78276_b(this.serverData.getServerMotd(), p_192634_2_ + 32 + 3, p_192634_3_ + 12, 8421504);

        if (this.mc.gameSettings.hideServerAddress)
        {
            this.mc.fontRenderer.func_78276_b(I18n.format("selectServer.hiddenAddress"), p_192634_2_ + 32 + 3, p_192634_3_ + 12 + 11, 3158064);
        }
        else
        {
            this.mc.fontRenderer.func_78276_b(this.serverData.getServerIpPort(), p_192634_2_ + 32 + 3, p_192634_3_ + 12 + 11, 3158064);
        }
    }

    public boolean func_148278_a(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
    {
        this.screen.func_146790_a(p_148278_1_);

        if (Minecraft.func_71386_F() - this.lastClickTime < 250L)
        {
            this.screen.connectToSelected();
        }

        this.lastClickTime = Minecraft.func_71386_F();
        return false;
    }

    public void func_192633_a(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_)
    {
    }

    public void func_148277_b(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_)
    {
    }

    public LanServerInfo getServerData()
    {
        return this.serverData;
    }
}
