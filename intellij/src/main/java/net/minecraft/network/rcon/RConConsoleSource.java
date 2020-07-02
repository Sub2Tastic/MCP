package net.minecraft.network.rcon;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class RConConsoleSource implements ICommandSender
{
    private final StringBuffer buffer = new StringBuffer();
    private final MinecraftServer server;

    public RConConsoleSource(MinecraftServer serverIn)
    {
        this.server = serverIn;
    }

    public String func_70005_c_()
    {
        return "Rcon";
    }

    /**
     * Send a chat message to the CommandSender
     */
    public void sendMessage(ITextComponent component)
    {
        this.buffer.append(component.func_150260_c());
    }

    public boolean func_70003_b(int p_70003_1_, String p_70003_2_)
    {
        return true;
    }

    /**
     * Get the world, if available. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the overworld
     */
    public World getEntityWorld()
    {
        return this.server.getEntityWorld();
    }

    public boolean func_174792_t_()
    {
        return true;
    }

    /**
     * Get the Minecraft server instance
     */
    public MinecraftServer getServer()
    {
        return this.server;
    }
}
