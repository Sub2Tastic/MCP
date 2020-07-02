package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerInfo;

public class ServerSelectionList extends GuiListExtended
{
    private final GuiMultiplayer owner;
    private final List<ServerListEntryNormal> serverListInternet = Lists.<ServerListEntryNormal>newArrayList();
    private final List<ServerListEntryLanDetected> serverListLan = Lists.<ServerListEntryLanDetected>newArrayList();
    private final GuiListExtended.IGuiListEntry lanScanEntry = new ServerListEntryLanScan();
    private int field_148197_o = -1;

    public ServerSelectionList(GuiMultiplayer ownerIn, Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn)
    {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.owner = ownerIn;
    }

    public GuiListExtended.IGuiListEntry func_148180_b(int p_148180_1_)
    {
        if (p_148180_1_ < this.serverListInternet.size())
        {
            return this.serverListInternet.get(p_148180_1_);
        }
        else
        {
            p_148180_1_ = p_148180_1_ - this.serverListInternet.size();

            if (p_148180_1_ == 0)
            {
                return this.lanScanEntry;
            }
            else
            {
                --p_148180_1_;
                return this.serverListLan.get(p_148180_1_);
            }
        }
    }

    protected int func_148127_b()
    {
        return this.serverListInternet.size() + 1 + this.serverListLan.size();
    }

    public void func_148192_c(int p_148192_1_)
    {
        this.field_148197_o = p_148192_1_;
    }

    protected boolean func_148131_a(int p_148131_1_)
    {
        return p_148131_1_ == this.field_148197_o;
    }

    public int func_148193_k()
    {
        return this.field_148197_o;
    }

    public void updateOnlineServers(ServerList p_148195_1_)
    {
        this.serverListInternet.clear();

        for (int i = 0; i < p_148195_1_.countServers(); ++i)
        {
            this.serverListInternet.add(new ServerListEntryNormal(this.owner, p_148195_1_.getServerData(i)));
        }
    }

    public void updateNetworkServers(List<LanServerInfo> p_148194_1_)
    {
        this.serverListLan.clear();

        for (LanServerInfo lanserverinfo : p_148194_1_)
        {
            this.serverListLan.add(new ServerListEntryLanDetected(this.owner, lanserverinfo));
        }
    }

    protected int func_148137_d()
    {
        return super.func_148137_d() + 30;
    }

    public int func_148139_c()
    {
        return super.func_148139_c() + 85;
    }
}
