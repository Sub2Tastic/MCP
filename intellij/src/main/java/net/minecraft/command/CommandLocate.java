package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandLocate extends CommandBase
{
    public String func_71517_b()
    {
        return "locate";
    }

    public int func_82362_a()
    {
        return 2;
    }

    public String func_71518_a(ICommandSender p_71518_1_)
    {
        return "commands.locate.usage";
    }

    public void func_184881_a(MinecraftServer p_184881_1_, ICommandSender p_184881_2_, String[] p_184881_3_) throws CommandException
    {
        if (p_184881_3_.length != 1)
        {
            throw new WrongUsageException("commands.locate.usage", new Object[0]);
        }
        else
        {
            String s = p_184881_3_[0];
            BlockPos blockpos = p_184881_2_.getEntityWorld().func_190528_a(s, p_184881_2_.getPosition(), false);

            if (blockpos != null)
            {
                p_184881_2_.sendMessage(new TextComponentTranslation("commands.locate.success", new Object[] {s, blockpos.getX(), blockpos.getZ()}));
            }
            else
            {
                throw new CommandException("commands.locate.failure", new Object[] {s});
            }
        }
    }

    public List<String> func_184883_a(MinecraftServer p_184883_1_, ICommandSender p_184883_2_, String[] p_184883_3_, @Nullable BlockPos p_184883_4_)
    {
        return p_184883_3_.length == 1 ? func_71530_a(p_184883_3_, new String[] {"Stronghold", "Monument", "Village", "Mansion", "EndCity", "Fortress", "Temple", "Mineshaft"}) : Collections.emptyList();
    }
}
