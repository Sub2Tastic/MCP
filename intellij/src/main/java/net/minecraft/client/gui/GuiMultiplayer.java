package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.network.LanServerInfo;
import net.minecraft.client.network.ServerPinger;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GuiMultiplayer extends GuiScreen
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final ServerPinger oldServerPinger = new ServerPinger();
    private final GuiScreen parentScreen;
    private ServerSelectionList serverListSelector;
    private ServerList savedServerList;
    private GuiButton btnEditServer;
    private GuiButton btnSelectServer;
    private GuiButton btnDeleteServer;
    private boolean field_146807_u;
    private boolean field_146806_v;
    private boolean field_146805_w;
    private boolean field_146813_x;

    /**
     * The text to be displayed when the player's cursor hovers over a server listing.
     */
    private String hoveringText;
    private ServerData selectedServer;
    private LanServerDetector.LanServerList lanServerList;
    private LanServerDetector.ThreadLanServerFind lanServerDetector;
    private boolean initialized;

    public GuiMultiplayer(GuiScreen parentScreen)
    {
        this.parentScreen = parentScreen;
    }

    public void func_73866_w_()
    {
        Keyboard.enableRepeatEvents(true);
        this.field_146292_n.clear();

        if (this.initialized)
        {
            this.serverListSelector.func_148122_a(this.field_146294_l, this.field_146295_m, 32, this.field_146295_m - 64);
        }
        else
        {
            this.initialized = true;
            this.savedServerList = new ServerList(this.field_146297_k);
            this.savedServerList.loadServerList();
            this.lanServerList = new LanServerDetector.LanServerList();

            try
            {
                this.lanServerDetector = new LanServerDetector.ThreadLanServerFind(this.lanServerList);
                this.lanServerDetector.start();
            }
            catch (Exception exception)
            {
                LOGGER.warn("Unable to start LAN server detection: {}", (Object)exception.getMessage());
            }

            this.serverListSelector = new ServerSelectionList(this, this.field_146297_k, this.field_146294_l, this.field_146295_m, 32, this.field_146295_m - 64, 36);
            this.serverListSelector.updateOnlineServers(this.savedServerList);
        }

        this.func_146794_g();
    }

    public void func_146274_d() throws IOException
    {
        super.func_146274_d();
        this.serverListSelector.func_178039_p();
    }

    public void func_146794_g()
    {
        this.btnEditServer = this.func_189646_b(new GuiButton(7, this.field_146294_l / 2 - 154, this.field_146295_m - 28, 70, 20, I18n.format("selectServer.edit")));
        this.btnDeleteServer = this.func_189646_b(new GuiButton(2, this.field_146294_l / 2 - 74, this.field_146295_m - 28, 70, 20, I18n.format("selectServer.delete")));
        this.btnSelectServer = this.func_189646_b(new GuiButton(1, this.field_146294_l / 2 - 154, this.field_146295_m - 52, 100, 20, I18n.format("selectServer.select")));
        this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 - 50, this.field_146295_m - 52, 100, 20, I18n.format("selectServer.direct")));
        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 + 4 + 50, this.field_146295_m - 52, 100, 20, I18n.format("selectServer.add")));
        this.field_146292_n.add(new GuiButton(8, this.field_146294_l / 2 + 4, this.field_146295_m - 28, 70, 20, I18n.format("selectServer.refresh")));
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 + 4 + 76, this.field_146295_m - 28, 75, 20, I18n.format("gui.cancel")));
        this.func_146790_a(this.serverListSelector.func_148193_k());
    }

    public void func_73876_c()
    {
        super.func_73876_c();

        if (this.lanServerList.getWasUpdated())
        {
            List<LanServerInfo> list = this.lanServerList.getLanServers();
            this.lanServerList.setWasNotUpdated();
            this.serverListSelector.updateNetworkServers(list);
        }

        this.oldServerPinger.pingPendingNetworks();
    }

    public void func_146281_b()
    {
        Keyboard.enableRepeatEvents(false);

        if (this.lanServerDetector != null)
        {
            this.lanServerDetector.interrupt();
            this.lanServerDetector = null;
        }

        this.oldServerPinger.clearPendingNetworks();
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146124_l)
        {
            GuiListExtended.IGuiListEntry guilistextended$iguilistentry = this.serverListSelector.func_148193_k() < 0 ? null : this.serverListSelector.func_148180_b(this.serverListSelector.func_148193_k());

            if (p_146284_1_.field_146127_k == 2 && guilistextended$iguilistentry instanceof ServerListEntryNormal)
            {
                String s4 = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData().serverName;

                if (s4 != null)
                {
                    this.field_146807_u = true;
                    String s = I18n.format("selectServer.deleteQuestion");
                    String s1 = "'" + s4 + "' " + I18n.format("selectServer.deleteWarning");
                    String s2 = I18n.format("selectServer.deleteButton");
                    String s3 = I18n.format("gui.cancel");
                    GuiYesNo guiyesno = new GuiYesNo(this, s, s1, s2, s3, this.serverListSelector.func_148193_k());
                    this.field_146297_k.displayGuiScreen(guiyesno);
                }
            }
            else if (p_146284_1_.field_146127_k == 1)
            {
                this.connectToSelected();
            }
            else if (p_146284_1_.field_146127_k == 4)
            {
                this.field_146813_x = true;
                this.selectedServer = new ServerData(I18n.format("selectServer.defaultName"), "", false);
                this.field_146297_k.displayGuiScreen(new GuiScreenServerList(this, this.selectedServer));
            }
            else if (p_146284_1_.field_146127_k == 3)
            {
                this.field_146806_v = true;
                this.selectedServer = new ServerData(I18n.format("selectServer.defaultName"), "", false);
                this.field_146297_k.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
            }
            else if (p_146284_1_.field_146127_k == 7 && guilistextended$iguilistentry instanceof ServerListEntryNormal)
            {
                this.field_146805_w = true;
                ServerData serverdata = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
                this.selectedServer = new ServerData(serverdata.serverName, serverdata.serverIP, false);
                this.selectedServer.copyFrom(serverdata);
                this.field_146297_k.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
            }
            else if (p_146284_1_.field_146127_k == 0)
            {
                this.field_146297_k.displayGuiScreen(this.parentScreen);
            }
            else if (p_146284_1_.field_146127_k == 8)
            {
                this.refreshServerList();
            }
        }
    }

    private void refreshServerList()
    {
        this.field_146297_k.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
    }

    public void func_73878_a(boolean p_73878_1_, int p_73878_2_)
    {
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = this.serverListSelector.func_148193_k() < 0 ? null : this.serverListSelector.func_148180_b(this.serverListSelector.func_148193_k());

        if (this.field_146807_u)
        {
            this.field_146807_u = false;

            if (p_73878_1_ && guilistextended$iguilistentry instanceof ServerListEntryNormal)
            {
                this.savedServerList.func_78851_b(this.serverListSelector.func_148193_k());
                this.savedServerList.saveServerList();
                this.serverListSelector.func_148192_c(-1);
                this.serverListSelector.updateOnlineServers(this.savedServerList);
            }

            this.field_146297_k.displayGuiScreen(this);
        }
        else if (this.field_146813_x)
        {
            this.field_146813_x = false;

            if (p_73878_1_)
            {
                this.connectToServer(this.selectedServer);
            }
            else
            {
                this.field_146297_k.displayGuiScreen(this);
            }
        }
        else if (this.field_146806_v)
        {
            this.field_146806_v = false;

            if (p_73878_1_)
            {
                this.savedServerList.addServerData(this.selectedServer);
                this.savedServerList.saveServerList();
                this.serverListSelector.func_148192_c(-1);
                this.serverListSelector.updateOnlineServers(this.savedServerList);
            }

            this.field_146297_k.displayGuiScreen(this);
        }
        else if (this.field_146805_w)
        {
            this.field_146805_w = false;

            if (p_73878_1_ && guilistextended$iguilistentry instanceof ServerListEntryNormal)
            {
                ServerData serverdata = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
                serverdata.serverName = this.selectedServer.serverName;
                serverdata.serverIP = this.selectedServer.serverIP;
                serverdata.copyFrom(this.selectedServer);
                this.savedServerList.saveServerList();
                this.serverListSelector.updateOnlineServers(this.savedServerList);
            }

            this.field_146297_k.displayGuiScreen(this);
        }
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
        int i = this.serverListSelector.func_148193_k();
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = i < 0 ? null : this.serverListSelector.func_148180_b(i);

        if (p_73869_2_ == 63)
        {
            this.refreshServerList();
        }
        else
        {
            if (i >= 0)
            {
                if (p_73869_2_ == 200)
                {
                    if (func_146272_n())
                    {
                        if (i > 0 && guilistextended$iguilistentry instanceof ServerListEntryNormal)
                        {
                            this.savedServerList.swapServers(i, i - 1);
                            this.func_146790_a(this.serverListSelector.func_148193_k() - 1);
                            this.serverListSelector.func_148145_f(-this.serverListSelector.func_148146_j());
                            this.serverListSelector.updateOnlineServers(this.savedServerList);
                        }
                    }
                    else if (i > 0)
                    {
                        this.func_146790_a(this.serverListSelector.func_148193_k() - 1);
                        this.serverListSelector.func_148145_f(-this.serverListSelector.func_148146_j());

                        if (this.serverListSelector.func_148180_b(this.serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan)
                        {
                            if (this.serverListSelector.func_148193_k() > 0)
                            {
                                this.func_146790_a(this.serverListSelector.func_148127_b() - 1);
                                this.serverListSelector.func_148145_f(-this.serverListSelector.func_148146_j());
                            }
                            else
                            {
                                this.func_146790_a(-1);
                            }
                        }
                    }
                    else
                    {
                        this.func_146790_a(-1);
                    }
                }
                else if (p_73869_2_ == 208)
                {
                    if (func_146272_n())
                    {
                        if (i < this.savedServerList.countServers() - 1)
                        {
                            this.savedServerList.swapServers(i, i + 1);
                            this.func_146790_a(i + 1);
                            this.serverListSelector.func_148145_f(this.serverListSelector.func_148146_j());
                            this.serverListSelector.updateOnlineServers(this.savedServerList);
                        }
                    }
                    else if (i < this.serverListSelector.func_148127_b())
                    {
                        this.func_146790_a(this.serverListSelector.func_148193_k() + 1);
                        this.serverListSelector.func_148145_f(this.serverListSelector.func_148146_j());

                        if (this.serverListSelector.func_148180_b(this.serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan)
                        {
                            if (this.serverListSelector.func_148193_k() < this.serverListSelector.func_148127_b() - 1)
                            {
                                this.func_146790_a(this.serverListSelector.func_148127_b() + 1);
                                this.serverListSelector.func_148145_f(this.serverListSelector.func_148146_j());
                            }
                            else
                            {
                                this.func_146790_a(-1);
                            }
                        }
                    }
                    else
                    {
                        this.func_146790_a(-1);
                    }
                }
                else if (p_73869_2_ != 28 && p_73869_2_ != 156)
                {
                    super.func_73869_a(p_73869_1_, p_73869_2_);
                }
                else
                {
                    this.func_146284_a(this.field_146292_n.get(2));
                }
            }
            else
            {
                super.func_73869_a(p_73869_1_, p_73869_2_);
            }
        }
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.hoveringText = null;
        this.func_146276_q_();
        this.serverListSelector.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
        this.func_73732_a(this.field_146289_q, I18n.format("multiplayer.title"), this.field_146294_l / 2, 20, 16777215);
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);

        if (this.hoveringText != null)
        {
            this.func_146283_a(Lists.newArrayList(Splitter.on("\n").split(this.hoveringText)), p_73863_1_, p_73863_2_);
        }
    }

    public void connectToSelected()
    {
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = this.serverListSelector.func_148193_k() < 0 ? null : this.serverListSelector.func_148180_b(this.serverListSelector.func_148193_k());

        if (guilistextended$iguilistentry instanceof ServerListEntryNormal)
        {
            this.connectToServer(((ServerListEntryNormal)guilistextended$iguilistentry).getServerData());
        }
        else if (guilistextended$iguilistentry instanceof ServerListEntryLanDetected)
        {
            LanServerInfo lanserverinfo = ((ServerListEntryLanDetected)guilistextended$iguilistentry).getServerData();
            this.connectToServer(new ServerData(lanserverinfo.getServerMotd(), lanserverinfo.getServerIpPort(), true));
        }
    }

    private void connectToServer(ServerData server)
    {
        this.field_146297_k.displayGuiScreen(new GuiConnecting(this, this.field_146297_k, server));
    }

    public void func_146790_a(int p_146790_1_)
    {
        this.serverListSelector.func_148192_c(p_146790_1_);
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = p_146790_1_ < 0 ? null : this.serverListSelector.func_148180_b(p_146790_1_);
        this.btnSelectServer.field_146124_l = false;
        this.btnEditServer.field_146124_l = false;
        this.btnDeleteServer.field_146124_l = false;

        if (guilistextended$iguilistentry != null && !(guilistextended$iguilistentry instanceof ServerListEntryLanScan))
        {
            this.btnSelectServer.field_146124_l = true;

            if (guilistextended$iguilistentry instanceof ServerListEntryNormal)
            {
                this.btnEditServer.field_146124_l = true;
                this.btnDeleteServer.field_146124_l = true;
            }
        }
    }

    public ServerPinger getOldServerPinger()
    {
        return this.oldServerPinger;
    }

    public void setHoveringText(String p_146793_1_)
    {
        this.hoveringText = p_146793_1_;
    }

    protected void func_73864_a(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
    {
        super.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);
        this.serverListSelector.func_148179_a(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    protected void func_146286_b(int p_146286_1_, int p_146286_2_, int p_146286_3_)
    {
        super.func_146286_b(p_146286_1_, p_146286_2_, p_146286_3_);
        this.serverListSelector.func_148181_b(p_146286_1_, p_146286_2_, p_146286_3_);
    }

    public ServerList getServerList()
    {
        return this.savedServerList;
    }

    public boolean func_175392_a(ServerListEntryNormal p_175392_1_, int p_175392_2_)
    {
        return p_175392_2_ > 0;
    }

    public boolean func_175394_b(ServerListEntryNormal p_175394_1_, int p_175394_2_)
    {
        return p_175394_2_ < this.savedServerList.countServers() - 1;
    }

    public void func_175391_a(ServerListEntryNormal p_175391_1_, int p_175391_2_, boolean p_175391_3_)
    {
        int i = p_175391_3_ ? 0 : p_175391_2_ - 1;
        this.savedServerList.swapServers(p_175391_2_, i);

        if (this.serverListSelector.func_148193_k() == p_175391_2_)
        {
            this.func_146790_a(i);
        }

        this.serverListSelector.updateOnlineServers(this.savedServerList);
    }

    public void func_175393_b(ServerListEntryNormal p_175393_1_, int p_175393_2_, boolean p_175393_3_)
    {
        int i = p_175393_3_ ? this.savedServerList.countServers() - 1 : p_175393_2_ + 1;
        this.savedServerList.swapServers(p_175393_2_, i);

        if (this.serverListSelector.func_148193_k() == p_175393_2_)
        {
            this.func_146790_a(i);
        }

        this.serverListSelector.updateOnlineServers(this.savedServerList);
    }
}
