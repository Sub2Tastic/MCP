package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;

public class CommandSaveAll extends CommandBase
{
    public String func_71517_b()
    {
        return "save-all";
    }

    public String func_71518_a(ICommandSender p_71518_1_)
    {
        return "commands.save.usage";
    }

    public void func_184881_a(MinecraftServer p_184881_1_, ICommandSender p_184881_2_, String[] p_184881_3_) throws CommandException
    {
        p_184881_2_.sendMessage(new TextComponentTranslation("commands.save.start", new Object[0]));

        if (p_184881_1_.getPlayerList() != null)
        {
            p_184881_1_.getPlayerList().saveAllPlayerData();
        }

        try
        {
            for (int i = 0; i < p_184881_1_.worlds.length; ++i)
            {
                if (p_184881_1_.worlds[i] != null)
                {
                    WorldServer worldserver = p_184881_1_.worlds[i];
                    boolean flag = worldserver.disableLevelSaving;
                    worldserver.disableLevelSaving = false;
                    worldserver.func_73044_a(true, (IProgressUpdate)null);
                    worldserver.disableLevelSaving = flag;
                }
            }

            if (p_184881_3_.length > 0 && "flush".equals(p_184881_3_[0]))
            {
                p_184881_2_.sendMessage(new TextComponentTranslation("commands.save.flushStart", new Object[0]));

                for (int j = 0; j < p_184881_1_.worlds.length; ++j)
                {
                    if (p_184881_1_.worlds[j] != null)
                    {
                        WorldServer worldserver1 = p_184881_1_.worlds[j];
                        boolean flag1 = worldserver1.disableLevelSaving;
                        worldserver1.disableLevelSaving = false;
                        worldserver1.func_104140_m();
                        worldserver1.disableLevelSaving = flag1;
                    }
                }

                p_184881_2_.sendMessage(new TextComponentTranslation("commands.save.flushEnd", new Object[0]));
            }
        }
        catch (MinecraftException minecraftexception)
        {
            func_152373_a(p_184881_2_, this, "commands.save.failed", new Object[] {minecraftexception.getMessage()});
            return;
        }

        func_152373_a(p_184881_2_, this, "commands.save.success", new Object[0]);
    }

    public List<String> func_184883_a(MinecraftServer p_184883_1_, ICommandSender p_184883_2_, String[] p_184883_3_, @Nullable BlockPos p_184883_4_)
    {
        return p_184883_3_.length == 1 ? func_71530_a(p_184883_3_, new String[] {"flush"}) : Collections.emptyList();
    }
}
