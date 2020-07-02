package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.border.WorldBorder;

public class CommandWorldBorder extends CommandBase
{
    public String func_71517_b()
    {
        return "worldborder";
    }

    public int func_82362_a()
    {
        return 2;
    }

    public String func_71518_a(ICommandSender p_71518_1_)
    {
        return "commands.worldborder.usage";
    }

    public void func_184881_a(MinecraftServer p_184881_1_, ICommandSender p_184881_2_, String[] p_184881_3_) throws CommandException
    {
        if (p_184881_3_.length < 1)
        {
            throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
        }
        else
        {
            WorldBorder worldborder = this.func_184931_a(p_184881_1_);

            if ("set".equals(p_184881_3_[0]))
            {
                if (p_184881_3_.length != 2 && p_184881_3_.length != 3)
                {
                    throw new WrongUsageException("commands.worldborder.set.usage", new Object[0]);
                }

                double d0 = worldborder.getTargetSize();
                double d2 = func_175756_a(p_184881_3_[1], 1.0D, 6.0E7D);
                long i = p_184881_3_.length > 2 ? func_175760_a(p_184881_3_[2], 0L, 9223372036854775L) * 1000L : 0L;

                if (i > 0L)
                {
                    worldborder.setTransition(d0, d2, i);

                    if (d0 > d2)
                    {
                        func_152373_a(p_184881_2_, this, "commands.worldborder.setSlowly.shrink.success", new Object[] {String.format("%.1f", d2), String.format("%.1f", d0), Long.toString(i / 1000L)});
                    }
                    else
                    {
                        func_152373_a(p_184881_2_, this, "commands.worldborder.setSlowly.grow.success", new Object[] {String.format("%.1f", d2), String.format("%.1f", d0), Long.toString(i / 1000L)});
                    }
                }
                else
                {
                    worldborder.setTransition(d2);
                    func_152373_a(p_184881_2_, this, "commands.worldborder.set.success", new Object[] {String.format("%.1f", d2), String.format("%.1f", d0)});
                }
            }
            else if ("add".equals(p_184881_3_[0]))
            {
                if (p_184881_3_.length != 2 && p_184881_3_.length != 3)
                {
                    throw new WrongUsageException("commands.worldborder.add.usage", new Object[0]);
                }

                double d4 = worldborder.getDiameter();
                double d8 = d4 + func_175756_a(p_184881_3_[1], -d4, 6.0E7D - d4);
                long j1 = worldborder.getTimeUntilTarget() + (p_184881_3_.length > 2 ? func_175760_a(p_184881_3_[2], 0L, 9223372036854775L) * 1000L : 0L);

                if (j1 > 0L)
                {
                    worldborder.setTransition(d4, d8, j1);

                    if (d4 > d8)
                    {
                        func_152373_a(p_184881_2_, this, "commands.worldborder.setSlowly.shrink.success", new Object[] {String.format("%.1f", d8), String.format("%.1f", d4), Long.toString(j1 / 1000L)});
                    }
                    else
                    {
                        func_152373_a(p_184881_2_, this, "commands.worldborder.setSlowly.grow.success", new Object[] {String.format("%.1f", d8), String.format("%.1f", d4), Long.toString(j1 / 1000L)});
                    }
                }
                else
                {
                    worldborder.setTransition(d8);
                    func_152373_a(p_184881_2_, this, "commands.worldborder.set.success", new Object[] {String.format("%.1f", d8), String.format("%.1f", d4)});
                }
            }
            else if ("center".equals(p_184881_3_[0]))
            {
                if (p_184881_3_.length != 3)
                {
                    throw new WrongUsageException("commands.worldborder.center.usage", new Object[0]);
                }

                BlockPos blockpos = p_184881_2_.getPosition();
                double d1 = func_175761_b((double)blockpos.getX() + 0.5D, p_184881_3_[1], true);
                double d3 = func_175761_b((double)blockpos.getZ() + 0.5D, p_184881_3_[2], true);
                worldborder.setCenter(d1, d3);
                func_152373_a(p_184881_2_, this, "commands.worldborder.center.success", new Object[] {d1, d3});
            }
            else if ("damage".equals(p_184881_3_[0]))
            {
                if (p_184881_3_.length < 2)
                {
                    throw new WrongUsageException("commands.worldborder.damage.usage", new Object[0]);
                }

                if ("buffer".equals(p_184881_3_[1]))
                {
                    if (p_184881_3_.length != 3)
                    {
                        throw new WrongUsageException("commands.worldborder.damage.buffer.usage", new Object[0]);
                    }

                    double d5 = func_180526_a(p_184881_3_[2], 0.0D);
                    double d9 = worldborder.getDamageBuffer();
                    worldborder.setDamageBuffer(d5);
                    func_152373_a(p_184881_2_, this, "commands.worldborder.damage.buffer.success", new Object[] {String.format("%.1f", d5), String.format("%.1f", d9)});
                }
                else if ("amount".equals(p_184881_3_[1]))
                {
                    if (p_184881_3_.length != 3)
                    {
                        throw new WrongUsageException("commands.worldborder.damage.amount.usage", new Object[0]);
                    }

                    double d6 = func_180526_a(p_184881_3_[2], 0.0D);
                    double d10 = worldborder.getDamagePerBlock();
                    worldborder.setDamagePerBlock(d6);
                    func_152373_a(p_184881_2_, this, "commands.worldborder.damage.amount.success", new Object[] {String.format("%.2f", d6), String.format("%.2f", d10)});
                }
            }
            else if ("warning".equals(p_184881_3_[0]))
            {
                if (p_184881_3_.length < 2)
                {
                    throw new WrongUsageException("commands.worldborder.warning.usage", new Object[0]);
                }

                if ("time".equals(p_184881_3_[1]))
                {
                    if (p_184881_3_.length != 3)
                    {
                        throw new WrongUsageException("commands.worldborder.warning.time.usage", new Object[0]);
                    }

                    int j = func_180528_a(p_184881_3_[2], 0);
                    int l = worldborder.getWarningTime();
                    worldborder.setWarningTime(j);
                    func_152373_a(p_184881_2_, this, "commands.worldborder.warning.time.success", new Object[] {j, l});
                }
                else if ("distance".equals(p_184881_3_[1]))
                {
                    if (p_184881_3_.length != 3)
                    {
                        throw new WrongUsageException("commands.worldborder.warning.distance.usage", new Object[0]);
                    }

                    int k = func_180528_a(p_184881_3_[2], 0);
                    int i1 = worldborder.getWarningDistance();
                    worldborder.setWarningDistance(k);
                    func_152373_a(p_184881_2_, this, "commands.worldborder.warning.distance.success", new Object[] {k, i1});
                }
            }
            else
            {
                if (!"get".equals(p_184881_3_[0]))
                {
                    throw new WrongUsageException("commands.worldborder.usage", new Object[0]);
                }

                double d7 = worldborder.getDiameter();
                p_184881_2_.func_174794_a(CommandResultStats.Type.QUERY_RESULT, MathHelper.floor(d7 + 0.5D));
                p_184881_2_.sendMessage(new TextComponentTranslation("commands.worldborder.get.success", new Object[] {String.format("%.0f", d7)}));
            }
        }
    }

    protected WorldBorder func_184931_a(MinecraftServer p_184931_1_)
    {
        return p_184931_1_.worlds[0].getWorldBorder();
    }

    public List<String> func_184883_a(MinecraftServer p_184883_1_, ICommandSender p_184883_2_, String[] p_184883_3_, @Nullable BlockPos p_184883_4_)
    {
        if (p_184883_3_.length == 1)
        {
            return func_71530_a(p_184883_3_, new String[] {"set", "center", "damage", "warning", "add", "get"});
        }
        else if (p_184883_3_.length == 2 && "damage".equals(p_184883_3_[0]))
        {
            return func_71530_a(p_184883_3_, new String[] {"buffer", "amount"});
        }
        else if (p_184883_3_.length >= 2 && p_184883_3_.length <= 3 && "center".equals(p_184883_3_[0]))
        {
            return func_181043_b(p_184883_3_, 1, p_184883_4_);
        }
        else
        {
            return p_184883_3_.length == 2 && "warning".equals(p_184883_3_[0]) ? func_71530_a(p_184883_3_, new String[] {"time", "distance"}) : Collections.emptyList();
        }
    }
}
