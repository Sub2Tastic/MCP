package net.minecraft.server.integrated;

import com.mojang.authlib.GameProfile;
import java.net.SocketAddress;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerList;

public class IntegratedPlayerList extends PlayerList
{
    private NBTTagCompound hostPlayerData;

    public IntegratedPlayerList(IntegratedServer server)
    {
        super(server);
        this.func_152611_a(10);
    }

    /**
     * also stores the NBTTags if this is an intergratedPlayerList
     */
    protected void writePlayerData(EntityPlayerMP playerIn)
    {
        if (playerIn.func_70005_c_().equals(this.getServer().getServerOwner()))
        {
            this.hostPlayerData = playerIn.writeWithoutTypeId(new NBTTagCompound());
        }

        super.writePlayerData(playerIn);
    }

    public String func_148542_a(SocketAddress p_148542_1_, GameProfile p_148542_2_)
    {
        return p_148542_2_.getName().equalsIgnoreCase(this.getServer().getServerOwner()) && this.getPlayerByUsername(p_148542_2_.getName()) != null ? "That name is already taken." : super.func_148542_a(p_148542_1_, p_148542_2_);
    }

    public IntegratedServer getServer()
    {
        return (IntegratedServer)super.getServer();
    }

    /**
     * On integrated servers, returns the host's player data to be written to level.dat.
     */
    public NBTTagCompound getHostPlayerData()
    {
        return this.hostPlayerData;
    }
}
