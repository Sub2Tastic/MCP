package net.minecraft.command;

import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;

public interface ICommandManager
{
    int func_71556_a(ICommandSender p_71556_1_, String p_71556_2_);

    List<String> func_180524_a(ICommandSender p_180524_1_, String p_180524_2_, BlockPos p_180524_3_);

    List<ICommand> func_71557_a(ICommandSender p_71557_1_);

    Map<String, ICommand> func_71555_a();
}
