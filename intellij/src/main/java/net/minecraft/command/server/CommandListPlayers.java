package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandListPlayers extends CommandBase
{
    public String func_71517_b()
    {
        return "list";
    }

    public int func_82362_a()
    {
        return 0;
    }

    public String func_71518_a(ICommandSender p_71518_1_)
    {
        return "commands.players.usage";
    }

    public void func_184881_a(MinecraftServer p_184881_1_, ICommandSender p_184881_2_, String[] p_184881_3_) throws CommandException
    {
        int i = p_184881_1_.getCurrentPlayerCount();
        p_184881_2_.sendMessage(new TextComponentTranslation("commands.players.list", new Object[] {i, p_184881_1_.getMaxPlayers()}));
        p_184881_2_.sendMessage(new TextComponentString(p_184881_1_.getPlayerList().func_181058_b(p_184881_3_.length > 0 && "uuids".equalsIgnoreCase(p_184881_3_[0]))));
        p_184881_2_.func_174794_a(CommandResultStats.Type.QUERY_RESULT, i);
    }
}
