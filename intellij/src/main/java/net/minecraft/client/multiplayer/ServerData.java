package net.minecraft.client.multiplayer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class ServerData
{
    public String serverName;
    public String serverIP;
    public String populationInfo;
    public String serverMOTD;
    public long pingToServer;
    public int version = 340;
    public String gameVersion = "1.12.2";
    public boolean pinged;
    public String playerList;
    private ServerData.ServerResourceMode resourceMode = ServerData.ServerResourceMode.PROMPT;
    private String serverIcon;

    /** True if the server is a LAN server */
    private boolean lanServer;

    public ServerData(String name, String ip, boolean isLan)
    {
        this.serverName = name;
        this.serverIP = ip;
        this.lanServer = isLan;
    }

    /**
     * Returns an NBTTagCompound with the server's name, IP and maybe acceptTextures.
     */
    public NBTTagCompound getNBTCompound()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.putString("name", this.serverName);
        nbttagcompound.putString("ip", this.serverIP);

        if (this.serverIcon != null)
        {
            nbttagcompound.putString("icon", this.serverIcon);
        }

        if (this.resourceMode == ServerData.ServerResourceMode.ENABLED)
        {
            nbttagcompound.putBoolean("acceptTextures", true);
        }
        else if (this.resourceMode == ServerData.ServerResourceMode.DISABLED)
        {
            nbttagcompound.putBoolean("acceptTextures", false);
        }

        return nbttagcompound;
    }

    public ServerData.ServerResourceMode getResourceMode()
    {
        return this.resourceMode;
    }

    public void setResourceMode(ServerData.ServerResourceMode mode)
    {
        this.resourceMode = mode;
    }

    /**
     * Takes an NBTTagCompound with 'name' and 'ip' keys, returns a ServerData instance.
     */
    public static ServerData getServerDataFromNBTCompound(NBTTagCompound nbtCompound)
    {
        ServerData serverdata = new ServerData(nbtCompound.getString("name"), nbtCompound.getString("ip"), false);

        if (nbtCompound.contains("icon", 8))
        {
            serverdata.setBase64EncodedIconData(nbtCompound.getString("icon"));
        }

        if (nbtCompound.contains("acceptTextures", 1))
        {
            if (nbtCompound.getBoolean("acceptTextures"))
            {
                serverdata.setResourceMode(ServerData.ServerResourceMode.ENABLED);
            }
            else
            {
                serverdata.setResourceMode(ServerData.ServerResourceMode.DISABLED);
            }
        }
        else
        {
            serverdata.setResourceMode(ServerData.ServerResourceMode.PROMPT);
        }

        return serverdata;
    }

    /**
     * Returns the base-64 encoded representation of the server's icon, or null if not available
     */
    public String getBase64EncodedIconData()
    {
        return this.serverIcon;
    }

    public void setBase64EncodedIconData(String icon)
    {
        this.serverIcon = icon;
    }

    /**
     * Return true if the server is a LAN server
     */
    public boolean isOnLAN()
    {
        return this.lanServer;
    }

    public void copyFrom(ServerData serverDataIn)
    {
        this.serverIP = serverDataIn.serverIP;
        this.serverName = serverDataIn.serverName;
        this.setResourceMode(serverDataIn.getResourceMode());
        this.serverIcon = serverDataIn.serverIcon;
        this.lanServer = serverDataIn.lanServer;
    }

    public static enum ServerResourceMode
    {
        ENABLED("enabled"),
        DISABLED("disabled"),
        PROMPT("prompt");

        private final ITextComponent motd;

        private ServerResourceMode(String name)
        {
            this.motd = new TextComponentTranslation("addServer.resourcePack." + name, new Object[0]);
        }

        public ITextComponent getMotd()
        {
            return this.motd;
        }
    }
}
