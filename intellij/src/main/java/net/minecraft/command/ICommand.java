package net.minecraft.command;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public interface ICommand extends Comparable<ICommand>
{
    String func_71517_b();

    String func_71518_a(ICommandSender p_71518_1_);

    List<String> func_71514_a();

    void func_184881_a(MinecraftServer p_184881_1_, ICommandSender p_184881_2_, String[] p_184881_3_) throws CommandException;

    boolean func_184882_a(MinecraftServer p_184882_1_, ICommandSender p_184882_2_);

    List<String> func_184883_a(MinecraftServer p_184883_1_, ICommandSender p_184883_2_, String[] p_184883_3_, BlockPos p_184883_4_);

    boolean func_82358_a(String[] p_82358_1_, int p_82358_2_);
}
