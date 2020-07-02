package net.minecraft.command.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandScoreboard extends CommandBase
{
    public String func_71517_b()
    {
        return "scoreboard";
    }

    public int func_82362_a()
    {
        return 2;
    }

    public String func_71518_a(ICommandSender p_71518_1_)
    {
        return "commands.scoreboard.usage";
    }

    public void func_184881_a(MinecraftServer p_184881_1_, ICommandSender p_184881_2_, String[] p_184881_3_) throws CommandException
    {
        if (!this.func_184909_b(p_184881_1_, p_184881_2_, p_184881_3_))
        {
            if (p_184881_3_.length < 1)
            {
                throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
            }
            else
            {
                if ("objectives".equalsIgnoreCase(p_184881_3_[0]))
                {
                    if (p_184881_3_.length == 1)
                    {
                        throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                    }

                    if ("list".equalsIgnoreCase(p_184881_3_[1]))
                    {
                        this.func_184925_a(p_184881_2_, p_184881_1_);
                    }
                    else if ("add".equalsIgnoreCase(p_184881_3_[1]))
                    {
                        if (p_184881_3_.length < 4)
                        {
                            throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
                        }

                        this.func_184908_a(p_184881_2_, p_184881_3_, 2, p_184881_1_);
                    }
                    else if ("remove".equalsIgnoreCase(p_184881_3_[1]))
                    {
                        if (p_184881_3_.length != 3)
                        {
                            throw new WrongUsageException("commands.scoreboard.objectives.remove.usage", new Object[0]);
                        }

                        this.func_184905_a(p_184881_2_, p_184881_3_[2], p_184881_1_);
                    }
                    else
                    {
                        if (!"setdisplay".equalsIgnoreCase(p_184881_3_[1]))
                        {
                            throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                        }

                        if (p_184881_3_.length != 3 && p_184881_3_.length != 4)
                        {
                            throw new WrongUsageException("commands.scoreboard.objectives.setdisplay.usage", new Object[0]);
                        }

                        this.func_184919_i(p_184881_2_, p_184881_3_, 2, p_184881_1_);
                    }
                }
                else if ("players".equalsIgnoreCase(p_184881_3_[0]))
                {
                    if (p_184881_3_.length == 1)
                    {
                        throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                    }

                    if ("list".equalsIgnoreCase(p_184881_3_[1]))
                    {
                        if (p_184881_3_.length > 3)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.list.usage", new Object[0]);
                        }

                        this.func_184920_j(p_184881_2_, p_184881_3_, 2, p_184881_1_);
                    }
                    else if ("add".equalsIgnoreCase(p_184881_3_[1]))
                    {
                        if (p_184881_3_.length < 5)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.add.usage", new Object[0]);
                        }

                        this.func_184918_k(p_184881_2_, p_184881_3_, 2, p_184881_1_);
                    }
                    else if ("remove".equalsIgnoreCase(p_184881_3_[1]))
                    {
                        if (p_184881_3_.length < 5)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.remove.usage", new Object[0]);
                        }

                        this.func_184918_k(p_184881_2_, p_184881_3_, 2, p_184881_1_);
                    }
                    else if ("set".equalsIgnoreCase(p_184881_3_[1]))
                    {
                        if (p_184881_3_.length < 5)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.set.usage", new Object[0]);
                        }

                        this.func_184918_k(p_184881_2_, p_184881_3_, 2, p_184881_1_);
                    }
                    else if ("reset".equalsIgnoreCase(p_184881_3_[1]))
                    {
                        if (p_184881_3_.length != 3 && p_184881_3_.length != 4)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.reset.usage", new Object[0]);
                        }

                        this.func_184912_l(p_184881_2_, p_184881_3_, 2, p_184881_1_);
                    }
                    else if ("enable".equalsIgnoreCase(p_184881_3_[1]))
                    {
                        if (p_184881_3_.length != 4)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.enable.usage", new Object[0]);
                        }

                        this.func_184914_m(p_184881_2_, p_184881_3_, 2, p_184881_1_);
                    }
                    else if ("test".equalsIgnoreCase(p_184881_3_[1]))
                    {
                        if (p_184881_3_.length != 5 && p_184881_3_.length != 6)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.test.usage", new Object[0]);
                        }

                        this.func_184907_n(p_184881_2_, p_184881_3_, 2, p_184881_1_);
                    }
                    else if ("operation".equalsIgnoreCase(p_184881_3_[1]))
                    {
                        if (p_184881_3_.length != 7)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.operation.usage", new Object[0]);
                        }

                        this.func_184906_o(p_184881_2_, p_184881_3_, 2, p_184881_1_);
                    }
                    else
                    {
                        if (!"tag".equalsIgnoreCase(p_184881_3_[1]))
                        {
                            throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                        }

                        if (p_184881_3_.length < 4)
                        {
                            throw new WrongUsageException("commands.scoreboard.players.tag.usage", new Object[0]);
                        }

                        this.func_184924_a(p_184881_1_, p_184881_2_, p_184881_3_, 2);
                    }
                }
                else
                {
                    if (!"teams".equalsIgnoreCase(p_184881_3_[0]))
                    {
                        throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
                    }

                    if (p_184881_3_.length == 1)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
                    }

                    if ("list".equalsIgnoreCase(p_184881_3_[1]))
                    {
                        if (p_184881_3_.length > 3)
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.list.usage", new Object[0]);
                        }

                        this.func_184922_e(p_184881_2_, p_184881_3_, 2, p_184881_1_);
                    }
                    else if ("add".equalsIgnoreCase(p_184881_3_[1]))
                    {
                        if (p_184881_3_.length < 3)
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
                        }

                        this.func_184910_b(p_184881_2_, p_184881_3_, 2, p_184881_1_);
                    }
                    else if ("remove".equalsIgnoreCase(p_184881_3_[1]))
                    {
                        if (p_184881_3_.length != 3)
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.remove.usage", new Object[0]);
                        }

                        this.func_184921_d(p_184881_2_, p_184881_3_, 2, p_184881_1_);
                    }
                    else if ("empty".equalsIgnoreCase(p_184881_3_[1]))
                    {
                        if (p_184881_3_.length != 3)
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.empty.usage", new Object[0]);
                        }

                        this.func_184917_h(p_184881_2_, p_184881_3_, 2, p_184881_1_);
                    }
                    else if ("join".equalsIgnoreCase(p_184881_3_[1]))
                    {
                        if (p_184881_3_.length < 4 && (p_184881_3_.length != 3 || !(p_184881_2_ instanceof EntityPlayer)))
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.join.usage", new Object[0]);
                        }

                        this.func_184916_f(p_184881_2_, p_184881_3_, 2, p_184881_1_);
                    }
                    else if ("leave".equalsIgnoreCase(p_184881_3_[1]))
                    {
                        if (p_184881_3_.length < 3 && !(p_184881_2_ instanceof EntityPlayer))
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.leave.usage", new Object[0]);
                        }

                        this.func_184911_g(p_184881_2_, p_184881_3_, 2, p_184881_1_);
                    }
                    else
                    {
                        if (!"option".equalsIgnoreCase(p_184881_3_[1]))
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
                        }

                        if (p_184881_3_.length != 4 && p_184881_3_.length != 5)
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
                        }

                        this.func_184923_c(p_184881_2_, p_184881_3_, 2, p_184881_1_);
                    }
                }
            }
        }
    }

    private boolean func_184909_b(MinecraftServer p_184909_1_, ICommandSender p_184909_2_, String[] p_184909_3_) throws CommandException
    {
        int i = -1;

        for (int j = 0; j < p_184909_3_.length; ++j)
        {
            if (this.func_82358_a(p_184909_3_, j) && "*".equals(p_184909_3_[j]))
            {
                if (i >= 0)
                {
                    throw new CommandException("commands.scoreboard.noMultiWildcard", new Object[0]);
                }

                i = j;
            }
        }

        if (i < 0)
        {
            return false;
        }
        else
        {
            List<String> list1 = Lists.newArrayList(this.func_184913_a(p_184909_1_).getObjectiveNames());
            String s = p_184909_3_[i];
            List<String> list = Lists.<String>newArrayList();

            for (String s1 : list1)
            {
                p_184909_3_[i] = s1;

                try
                {
                    this.func_184881_a(p_184909_1_, p_184909_2_, p_184909_3_);
                    list.add(s1);
                }
                catch (CommandException commandexception)
                {
                    TextComponentTranslation textcomponenttranslation = new TextComponentTranslation(commandexception.getMessage(), commandexception.func_74844_a());
                    textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
                    p_184909_2_.sendMessage(textcomponenttranslation);
                }
            }

            p_184909_3_[i] = s;
            p_184909_2_.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());

            if (list.isEmpty())
            {
                throw new WrongUsageException("commands.scoreboard.allMatchesFailed", new Object[0]);
            }
            else
            {
                return true;
            }
        }
    }

    protected Scoreboard func_184913_a(MinecraftServer p_184913_1_)
    {
        return p_184913_1_.getWorld(0).getScoreboard();
    }

    protected ScoreObjective func_184903_a(String p_184903_1_, boolean p_184903_2_, MinecraftServer p_184903_3_) throws CommandException
    {
        Scoreboard scoreboard = this.func_184913_a(p_184903_3_);
        ScoreObjective scoreobjective = scoreboard.getObjective(p_184903_1_);

        if (scoreobjective == null)
        {
            throw new CommandException("commands.scoreboard.objectiveNotFound", new Object[] {p_184903_1_});
        }
        else if (p_184903_2_ && scoreobjective.getCriteria().isReadOnly())
        {
            throw new CommandException("commands.scoreboard.objectiveReadOnly", new Object[] {p_184903_1_});
        }
        else
        {
            return scoreobjective;
        }
    }

    protected ScorePlayerTeam func_184915_a(String p_184915_1_, MinecraftServer p_184915_2_) throws CommandException
    {
        Scoreboard scoreboard = this.func_184913_a(p_184915_2_);
        ScorePlayerTeam scoreplayerteam = scoreboard.getTeam(p_184915_1_);

        if (scoreplayerteam == null)
        {
            throw new CommandException("commands.scoreboard.teamNotFound", new Object[] {p_184915_1_});
        }
        else
        {
            return scoreplayerteam;
        }
    }

    protected void func_184908_a(ICommandSender p_184908_1_, String[] p_184908_2_, int p_184908_3_, MinecraftServer p_184908_4_) throws CommandException
    {
        String s = p_184908_2_[p_184908_3_++];
        String s1 = p_184908_2_[p_184908_3_++];
        Scoreboard scoreboard = this.func_184913_a(p_184908_4_);
        IScoreCriteria iscorecriteria = IScoreCriteria.INSTANCES.get(s1);

        if (iscorecriteria == null)
        {
            throw new WrongUsageException("commands.scoreboard.objectives.add.wrongType", new Object[] {s1});
        }
        else if (scoreboard.getObjective(s) != null)
        {
            throw new CommandException("commands.scoreboard.objectives.add.alreadyExists", new Object[] {s});
        }
        else if (s.length() > 16)
        {
            throw new SyntaxErrorException("commands.scoreboard.objectives.add.tooLong", new Object[] {s, Integer.valueOf(16)});
        }
        else if (s.isEmpty())
        {
            throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
        }
        else
        {
            if (p_184908_2_.length > p_184908_3_)
            {
                String s2 = func_147178_a(p_184908_1_, p_184908_2_, p_184908_3_).func_150260_c();

                if (s2.length() > 32)
                {
                    throw new SyntaxErrorException("commands.scoreboard.objectives.add.displayTooLong", new Object[] {s2, Integer.valueOf(32)});
                }

                if (s2.isEmpty())
                {
                    scoreboard.func_96535_a(s, iscorecriteria);
                }
                else
                {
                    scoreboard.func_96535_a(s, iscorecriteria).func_96681_a(s2);
                }
            }
            else
            {
                scoreboard.func_96535_a(s, iscorecriteria);
            }

            func_152373_a(p_184908_1_, this, "commands.scoreboard.objectives.add.success", new Object[] {s});
        }
    }

    protected void func_184910_b(ICommandSender p_184910_1_, String[] p_184910_2_, int p_184910_3_, MinecraftServer p_184910_4_) throws CommandException
    {
        String s = p_184910_2_[p_184910_3_++];
        Scoreboard scoreboard = this.func_184913_a(p_184910_4_);

        if (scoreboard.getTeam(s) != null)
        {
            throw new CommandException("commands.scoreboard.teams.add.alreadyExists", new Object[] {s});
        }
        else if (s.length() > 16)
        {
            throw new SyntaxErrorException("commands.scoreboard.teams.add.tooLong", new Object[] {s, Integer.valueOf(16)});
        }
        else if (s.isEmpty())
        {
            throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
        }
        else
        {
            if (p_184910_2_.length > p_184910_3_)
            {
                String s1 = func_147178_a(p_184910_1_, p_184910_2_, p_184910_3_).func_150260_c();

                if (s1.length() > 32)
                {
                    throw new SyntaxErrorException("commands.scoreboard.teams.add.displayTooLong", new Object[] {s1, Integer.valueOf(32)});
                }

                if (s1.isEmpty())
                {
                    scoreboard.createTeam(s);
                }
                else
                {
                    scoreboard.createTeam(s).setDisplayName(s1);
                }
            }
            else
            {
                scoreboard.createTeam(s);
            }

            func_152373_a(p_184910_1_, this, "commands.scoreboard.teams.add.success", new Object[] {s});
        }
    }

    protected void func_184923_c(ICommandSender p_184923_1_, String[] p_184923_2_, int p_184923_3_, MinecraftServer p_184923_4_) throws CommandException
    {
        ScorePlayerTeam scoreplayerteam = this.func_184915_a(p_184923_2_[p_184923_3_++], p_184923_4_);

        if (scoreplayerteam != null)
        {
            String s = p_184923_2_[p_184923_3_++].toLowerCase(Locale.ROOT);

            if (!"color".equalsIgnoreCase(s) && !"friendlyfire".equalsIgnoreCase(s) && !"seeFriendlyInvisibles".equalsIgnoreCase(s) && !"nametagVisibility".equalsIgnoreCase(s) && !"deathMessageVisibility".equalsIgnoreCase(s) && !"collisionRule".equalsIgnoreCase(s))
            {
                throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
            }
            else if (p_184923_2_.length == 4)
            {
                if ("color".equalsIgnoreCase(s))
                {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_96333_a(TextFormatting.getValidValues(true, false))});
                }
                else if (!"friendlyfire".equalsIgnoreCase(s) && !"seeFriendlyInvisibles".equalsIgnoreCase(s))
                {
                    if (!"nametagVisibility".equalsIgnoreCase(s) && !"deathMessageVisibility".equalsIgnoreCase(s))
                    {
                        if ("collisionRule".equalsIgnoreCase(s))
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_71527_a(Team.CollisionRule.func_186687_a())});
                        }
                        else
                        {
                            throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
                        }
                    }
                    else
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_71527_a(Team.EnumVisible.func_178825_a())});
                    }
                }
                else
                {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_96333_a(Arrays.asList("true", "false"))});
                }
            }
            else
            {
                String s1 = p_184923_2_[p_184923_3_];

                if ("color".equalsIgnoreCase(s))
                {
                    TextFormatting textformatting = TextFormatting.getValueByName(s1);

                    if (textformatting == null || textformatting.isFancyStyling())
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_96333_a(TextFormatting.getValidValues(true, false))});
                    }

                    scoreplayerteam.setColor(textformatting);
                    scoreplayerteam.func_96666_b(textformatting.toString());
                    scoreplayerteam.func_96662_c(TextFormatting.RESET.toString());
                }
                else if ("friendlyfire".equalsIgnoreCase(s))
                {
                    if (!"true".equalsIgnoreCase(s1) && !"false".equalsIgnoreCase(s1))
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_96333_a(Arrays.asList("true", "false"))});
                    }

                    scoreplayerteam.setAllowFriendlyFire("true".equalsIgnoreCase(s1));
                }
                else if ("seeFriendlyInvisibles".equalsIgnoreCase(s))
                {
                    if (!"true".equalsIgnoreCase(s1) && !"false".equalsIgnoreCase(s1))
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_96333_a(Arrays.asList("true", "false"))});
                    }

                    scoreplayerteam.setSeeFriendlyInvisiblesEnabled("true".equalsIgnoreCase(s1));
                }
                else if ("nametagVisibility".equalsIgnoreCase(s))
                {
                    Team.EnumVisible team$enumvisible = Team.EnumVisible.getByName(s1);

                    if (team$enumvisible == null)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_71527_a(Team.EnumVisible.func_178825_a())});
                    }

                    scoreplayerteam.setNameTagVisibility(team$enumvisible);
                }
                else if ("deathMessageVisibility".equalsIgnoreCase(s))
                {
                    Team.EnumVisible team$enumvisible1 = Team.EnumVisible.getByName(s1);

                    if (team$enumvisible1 == null)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_71527_a(Team.EnumVisible.func_178825_a())});
                    }

                    scoreplayerteam.setDeathMessageVisibility(team$enumvisible1);
                }
                else if ("collisionRule".equalsIgnoreCase(s))
                {
                    Team.CollisionRule team$collisionrule = Team.CollisionRule.getByName(s1);

                    if (team$collisionrule == null)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_71527_a(Team.CollisionRule.func_186687_a())});
                    }

                    scoreplayerteam.setCollisionRule(team$collisionrule);
                }

                func_152373_a(p_184923_1_, this, "commands.scoreboard.teams.option.success", new Object[] {s, scoreplayerteam.getName(), s1});
            }
        }
    }

    protected void func_184921_d(ICommandSender p_184921_1_, String[] p_184921_2_, int p_184921_3_, MinecraftServer p_184921_4_) throws CommandException
    {
        Scoreboard scoreboard = this.func_184913_a(p_184921_4_);
        ScorePlayerTeam scoreplayerteam = this.func_184915_a(p_184921_2_[p_184921_3_], p_184921_4_);

        if (scoreplayerteam != null)
        {
            scoreboard.removeTeam(scoreplayerteam);
            func_152373_a(p_184921_1_, this, "commands.scoreboard.teams.remove.success", new Object[] {scoreplayerteam.getName()});
        }
    }

    protected void func_184922_e(ICommandSender p_184922_1_, String[] p_184922_2_, int p_184922_3_, MinecraftServer p_184922_4_) throws CommandException
    {
        Scoreboard scoreboard = this.func_184913_a(p_184922_4_);

        if (p_184922_2_.length > p_184922_3_)
        {
            ScorePlayerTeam scoreplayerteam = this.func_184915_a(p_184922_2_[p_184922_3_], p_184922_4_);

            if (scoreplayerteam == null)
            {
                return;
            }

            Collection<String> collection = scoreplayerteam.getMembershipCollection();
            p_184922_1_.func_174794_a(CommandResultStats.Type.QUERY_RESULT, collection.size());

            if (collection.isEmpty())
            {
                throw new CommandException("commands.scoreboard.teams.list.player.empty", new Object[] {scoreplayerteam.getName()});
            }

            TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.scoreboard.teams.list.player.count", new Object[] {collection.size(), scoreplayerteam.getName()});
            textcomponenttranslation.getStyle().setColor(TextFormatting.DARK_GREEN);
            p_184922_1_.sendMessage(textcomponenttranslation);
            p_184922_1_.sendMessage(new TextComponentString(func_71527_a(collection.toArray())));
        }
        else
        {
            Collection<ScorePlayerTeam> collection1 = scoreboard.getTeams();
            p_184922_1_.func_174794_a(CommandResultStats.Type.QUERY_RESULT, collection1.size());

            if (collection1.isEmpty())
            {
                throw new CommandException("commands.scoreboard.teams.list.empty", new Object[0]);
            }

            TextComponentTranslation textcomponenttranslation1 = new TextComponentTranslation("commands.scoreboard.teams.list.count", new Object[] {collection1.size()});
            textcomponenttranslation1.getStyle().setColor(TextFormatting.DARK_GREEN);
            p_184922_1_.sendMessage(textcomponenttranslation1);

            for (ScorePlayerTeam scoreplayerteam1 : collection1)
            {
                p_184922_1_.sendMessage(new TextComponentTranslation("commands.scoreboard.teams.list.entry", new Object[] {scoreplayerteam1.getName(), scoreplayerteam1.getDisplayName(), scoreplayerteam1.getMembershipCollection().size()}));
            }
        }
    }

    protected void func_184916_f(ICommandSender p_184916_1_, String[] p_184916_2_, int p_184916_3_, MinecraftServer p_184916_4_) throws CommandException
    {
        Scoreboard scoreboard = this.func_184913_a(p_184916_4_);
        String s = p_184916_2_[p_184916_3_++];
        Set<String> set = Sets.<String>newHashSet();
        Set<String> set1 = Sets.<String>newHashSet();

        if (p_184916_1_ instanceof EntityPlayer && p_184916_3_ == p_184916_2_.length)
        {
            String s4 = func_71521_c(p_184916_1_).func_70005_c_();

            if (scoreboard.func_151392_a(s4, s))
            {
                set.add(s4);
            }
            else
            {
                set1.add(s4);
            }
        }
        else
        {
            while (p_184916_3_ < p_184916_2_.length)
            {
                String s1 = p_184916_2_[p_184916_3_++];

                if (EntitySelector.func_82378_b(s1))
                {
                    for (Entity entity : func_184890_c(p_184916_4_, p_184916_1_, s1))
                    {
                        String s3 = func_184891_e(p_184916_4_, p_184916_1_, entity.getCachedUniqueIdString());

                        if (scoreboard.func_151392_a(s3, s))
                        {
                            set.add(s3);
                        }
                        else
                        {
                            set1.add(s3);
                        }
                    }
                }
                else
                {
                    String s2 = func_184891_e(p_184916_4_, p_184916_1_, s1);

                    if (scoreboard.func_151392_a(s2, s))
                    {
                        set.add(s2);
                    }
                    else
                    {
                        set1.add(s2);
                    }
                }
            }
        }

        if (!set.isEmpty())
        {
            p_184916_1_.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, set.size());
            func_152373_a(p_184916_1_, this, "commands.scoreboard.teams.join.success", new Object[] {set.size(), s, func_71527_a(set.toArray(new String[set.size()]))});
        }

        if (!set1.isEmpty())
        {
            throw new CommandException("commands.scoreboard.teams.join.failure", new Object[] {set1.size(), s, func_71527_a(set1.toArray(new String[set1.size()]))});
        }
    }

    protected void func_184911_g(ICommandSender p_184911_1_, String[] p_184911_2_, int p_184911_3_, MinecraftServer p_184911_4_) throws CommandException
    {
        Scoreboard scoreboard = this.func_184913_a(p_184911_4_);
        Set<String> set = Sets.<String>newHashSet();
        Set<String> set1 = Sets.<String>newHashSet();

        if (p_184911_1_ instanceof EntityPlayer && p_184911_3_ == p_184911_2_.length)
        {
            String s3 = func_71521_c(p_184911_1_).func_70005_c_();

            if (scoreboard.removePlayerFromTeams(s3))
            {
                set.add(s3);
            }
            else
            {
                set1.add(s3);
            }
        }
        else
        {
            while (p_184911_3_ < p_184911_2_.length)
            {
                String s = p_184911_2_[p_184911_3_++];

                if (EntitySelector.func_82378_b(s))
                {
                    for (Entity entity : func_184890_c(p_184911_4_, p_184911_1_, s))
                    {
                        String s2 = func_184891_e(p_184911_4_, p_184911_1_, entity.getCachedUniqueIdString());

                        if (scoreboard.removePlayerFromTeams(s2))
                        {
                            set.add(s2);
                        }
                        else
                        {
                            set1.add(s2);
                        }
                    }
                }
                else
                {
                    String s1 = func_184891_e(p_184911_4_, p_184911_1_, s);

                    if (scoreboard.removePlayerFromTeams(s1))
                    {
                        set.add(s1);
                    }
                    else
                    {
                        set1.add(s1);
                    }
                }
            }
        }

        if (!set.isEmpty())
        {
            p_184911_1_.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, set.size());
            func_152373_a(p_184911_1_, this, "commands.scoreboard.teams.leave.success", new Object[] {set.size(), func_71527_a(set.toArray(new String[set.size()]))});
        }

        if (!set1.isEmpty())
        {
            throw new CommandException("commands.scoreboard.teams.leave.failure", new Object[] {set1.size(), func_71527_a(set1.toArray(new String[set1.size()]))});
        }
    }

    protected void func_184917_h(ICommandSender p_184917_1_, String[] p_184917_2_, int p_184917_3_, MinecraftServer p_184917_4_) throws CommandException
    {
        Scoreboard scoreboard = this.func_184913_a(p_184917_4_);
        ScorePlayerTeam scoreplayerteam = this.func_184915_a(p_184917_2_[p_184917_3_], p_184917_4_);

        if (scoreplayerteam != null)
        {
            Collection<String> collection = Lists.newArrayList(scoreplayerteam.getMembershipCollection());
            p_184917_1_.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, collection.size());

            if (collection.isEmpty())
            {
                throw new CommandException("commands.scoreboard.teams.empty.alreadyEmpty", new Object[] {scoreplayerteam.getName()});
            }
            else
            {
                for (String s : collection)
                {
                    scoreboard.removePlayerFromTeam(s, scoreplayerteam);
                }

                func_152373_a(p_184917_1_, this, "commands.scoreboard.teams.empty.success", new Object[] {collection.size(), scoreplayerteam.getName()});
            }
        }
    }

    protected void func_184905_a(ICommandSender p_184905_1_, String p_184905_2_, MinecraftServer p_184905_3_) throws CommandException
    {
        Scoreboard scoreboard = this.func_184913_a(p_184905_3_);
        ScoreObjective scoreobjective = this.func_184903_a(p_184905_2_, false, p_184905_3_);
        scoreboard.removeObjective(scoreobjective);
        func_152373_a(p_184905_1_, this, "commands.scoreboard.objectives.remove.success", new Object[] {p_184905_2_});
    }

    protected void func_184925_a(ICommandSender p_184925_1_, MinecraftServer p_184925_2_) throws CommandException
    {
        Scoreboard scoreboard = this.func_184913_a(p_184925_2_);
        Collection<ScoreObjective> collection = scoreboard.getScoreObjectives();

        if (collection.isEmpty())
        {
            throw new CommandException("commands.scoreboard.objectives.list.empty", new Object[0]);
        }
        else
        {
            TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.scoreboard.objectives.list.count", new Object[] {collection.size()});
            textcomponenttranslation.getStyle().setColor(TextFormatting.DARK_GREEN);
            p_184925_1_.sendMessage(textcomponenttranslation);

            for (ScoreObjective scoreobjective : collection)
            {
                p_184925_1_.sendMessage(new TextComponentTranslation("commands.scoreboard.objectives.list.entry", new Object[] {scoreobjective.getName(), scoreobjective.getDisplayName(), scoreobjective.getCriteria().getName()}));
            }
        }
    }

    protected void func_184919_i(ICommandSender p_184919_1_, String[] p_184919_2_, int p_184919_3_, MinecraftServer p_184919_4_) throws CommandException
    {
        Scoreboard scoreboard = this.func_184913_a(p_184919_4_);
        String s = p_184919_2_[p_184919_3_++];
        int i = Scoreboard.getObjectiveDisplaySlotNumber(s);
        ScoreObjective scoreobjective = null;

        if (p_184919_2_.length == 4)
        {
            scoreobjective = this.func_184903_a(p_184919_2_[p_184919_3_], false, p_184919_4_);
        }

        if (i < 0)
        {
            throw new CommandException("commands.scoreboard.objectives.setdisplay.invalidSlot", new Object[] {s});
        }
        else
        {
            scoreboard.setObjectiveInDisplaySlot(i, scoreobjective);

            if (scoreobjective != null)
            {
                func_152373_a(p_184919_1_, this, "commands.scoreboard.objectives.setdisplay.successSet", new Object[] {Scoreboard.getObjectiveDisplaySlot(i), scoreobjective.getName()});
            }
            else
            {
                func_152373_a(p_184919_1_, this, "commands.scoreboard.objectives.setdisplay.successCleared", new Object[] {Scoreboard.getObjectiveDisplaySlot(i)});
            }
        }
    }

    protected void func_184920_j(ICommandSender p_184920_1_, String[] p_184920_2_, int p_184920_3_, MinecraftServer p_184920_4_) throws CommandException
    {
        Scoreboard scoreboard = this.func_184913_a(p_184920_4_);

        if (p_184920_2_.length > p_184920_3_)
        {
            String s = func_184891_e(p_184920_4_, p_184920_1_, p_184920_2_[p_184920_3_]);
            Map<ScoreObjective, Score> map = scoreboard.getObjectivesForEntity(s);
            p_184920_1_.func_174794_a(CommandResultStats.Type.QUERY_RESULT, map.size());

            if (map.isEmpty())
            {
                throw new CommandException("commands.scoreboard.players.list.player.empty", new Object[] {s});
            }

            TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.scoreboard.players.list.player.count", new Object[] {map.size(), s});
            textcomponenttranslation.getStyle().setColor(TextFormatting.DARK_GREEN);
            p_184920_1_.sendMessage(textcomponenttranslation);

            for (Score score : map.values())
            {
                p_184920_1_.sendMessage(new TextComponentTranslation("commands.scoreboard.players.list.player.entry", new Object[] {score.getScorePoints(), score.getObjective().getDisplayName(), score.getObjective().getName()}));
            }
        }
        else
        {
            Collection<String> collection = scoreboard.getObjectiveNames();
            p_184920_1_.func_174794_a(CommandResultStats.Type.QUERY_RESULT, collection.size());

            if (collection.isEmpty())
            {
                throw new CommandException("commands.scoreboard.players.list.empty", new Object[0]);
            }

            TextComponentTranslation textcomponenttranslation1 = new TextComponentTranslation("commands.scoreboard.players.list.count", new Object[] {collection.size()});
            textcomponenttranslation1.getStyle().setColor(TextFormatting.DARK_GREEN);
            p_184920_1_.sendMessage(textcomponenttranslation1);
            p_184920_1_.sendMessage(new TextComponentString(func_71527_a(collection.toArray())));
        }
    }

    protected void func_184918_k(ICommandSender p_184918_1_, String[] p_184918_2_, int p_184918_3_, MinecraftServer p_184918_4_) throws CommandException
    {
        String s = p_184918_2_[p_184918_3_ - 1];
        int i = p_184918_3_;
        String s1 = func_184891_e(p_184918_4_, p_184918_1_, p_184918_2_[p_184918_3_++]);

        if (s1.length() > 40)
        {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] {s1, Integer.valueOf(40)});
        }
        else
        {
            ScoreObjective scoreobjective = this.func_184903_a(p_184918_2_[p_184918_3_++], true, p_184918_4_);
            int j = "set".equalsIgnoreCase(s) ? func_175755_a(p_184918_2_[p_184918_3_++]) : func_180528_a(p_184918_2_[p_184918_3_++], 0);

            if (p_184918_2_.length > p_184918_3_)
            {
                Entity entity = func_184885_b(p_184918_4_, p_184918_1_, p_184918_2_[i]);

                try
                {
                    NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(func_180529_a(p_184918_2_, p_184918_3_));
                    NBTTagCompound nbttagcompound1 = func_184887_a(entity);

                    if (!NBTUtil.areNBTEquals(nbttagcompound, nbttagcompound1, true))
                    {
                        throw new CommandException("commands.scoreboard.players.set.tagMismatch", new Object[] {s1});
                    }
                }
                catch (NBTException nbtexception)
                {
                    throw new CommandException("commands.scoreboard.players.set.tagError", new Object[] {nbtexception.getMessage()});
                }
            }

            Scoreboard scoreboard = this.func_184913_a(p_184918_4_);
            Score score = scoreboard.getOrCreateScore(s1, scoreobjective);

            if ("set".equalsIgnoreCase(s))
            {
                score.setScorePoints(j);
            }
            else if ("add".equalsIgnoreCase(s))
            {
                score.increaseScore(j);
            }
            else
            {
                score.func_96646_b(j);
            }

            func_152373_a(p_184918_1_, this, "commands.scoreboard.players.set.success", new Object[] {scoreobjective.getName(), s1, score.getScorePoints()});
        }
    }

    protected void func_184912_l(ICommandSender p_184912_1_, String[] p_184912_2_, int p_184912_3_, MinecraftServer p_184912_4_) throws CommandException
    {
        Scoreboard scoreboard = this.func_184913_a(p_184912_4_);
        String s = func_184891_e(p_184912_4_, p_184912_1_, p_184912_2_[p_184912_3_++]);

        if (p_184912_2_.length > p_184912_3_)
        {
            ScoreObjective scoreobjective = this.func_184903_a(p_184912_2_[p_184912_3_++], false, p_184912_4_);
            scoreboard.removeObjectiveFromEntity(s, scoreobjective);
            func_152373_a(p_184912_1_, this, "commands.scoreboard.players.resetscore.success", new Object[] {scoreobjective.getName(), s});
        }
        else
        {
            scoreboard.removeObjectiveFromEntity(s, (ScoreObjective)null);
            func_152373_a(p_184912_1_, this, "commands.scoreboard.players.reset.success", new Object[] {s});
        }
    }

    protected void func_184914_m(ICommandSender p_184914_1_, String[] p_184914_2_, int p_184914_3_, MinecraftServer p_184914_4_) throws CommandException
    {
        Scoreboard scoreboard = this.func_184913_a(p_184914_4_);
        String s = func_184886_d(p_184914_4_, p_184914_1_, p_184914_2_[p_184914_3_++]);

        if (s.length() > 40)
        {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] {s, Integer.valueOf(40)});
        }
        else
        {
            ScoreObjective scoreobjective = this.func_184903_a(p_184914_2_[p_184914_3_], false, p_184914_4_);

            if (scoreobjective.getCriteria() != IScoreCriteria.TRIGGER)
            {
                throw new CommandException("commands.scoreboard.players.enable.noTrigger", new Object[] {scoreobjective.getName()});
            }
            else
            {
                Score score = scoreboard.getOrCreateScore(s, scoreobjective);
                score.setLocked(false);
                func_152373_a(p_184914_1_, this, "commands.scoreboard.players.enable.success", new Object[] {scoreobjective.getName(), s});
            }
        }
    }

    protected void func_184907_n(ICommandSender p_184907_1_, String[] p_184907_2_, int p_184907_3_, MinecraftServer p_184907_4_) throws CommandException
    {
        Scoreboard scoreboard = this.func_184913_a(p_184907_4_);
        String s = func_184891_e(p_184907_4_, p_184907_1_, p_184907_2_[p_184907_3_++]);

        if (s.length() > 40)
        {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] {s, Integer.valueOf(40)});
        }
        else
        {
            ScoreObjective scoreobjective = this.func_184903_a(p_184907_2_[p_184907_3_++], false, p_184907_4_);

            if (!scoreboard.entityHasObjective(s, scoreobjective))
            {
                throw new CommandException("commands.scoreboard.players.test.notFound", new Object[] {scoreobjective.getName(), s});
            }
            else
            {
                int i = p_184907_2_[p_184907_3_].equals("*") ? Integer.MIN_VALUE : func_175755_a(p_184907_2_[p_184907_3_]);
                ++p_184907_3_;
                int j = p_184907_3_ < p_184907_2_.length && !p_184907_2_[p_184907_3_].equals("*") ? func_180528_a(p_184907_2_[p_184907_3_], i) : Integer.MAX_VALUE;
                Score score = scoreboard.getOrCreateScore(s, scoreobjective);

                if (score.getScorePoints() >= i && score.getScorePoints() <= j)
                {
                    func_152373_a(p_184907_1_, this, "commands.scoreboard.players.test.success", new Object[] {score.getScorePoints(), i, j});
                }
                else
                {
                    throw new CommandException("commands.scoreboard.players.test.failed", new Object[] {score.getScorePoints(), i, j});
                }
            }
        }
    }

    protected void func_184906_o(ICommandSender p_184906_1_, String[] p_184906_2_, int p_184906_3_, MinecraftServer p_184906_4_) throws CommandException
    {
        Scoreboard scoreboard = this.func_184913_a(p_184906_4_);
        String s = func_184891_e(p_184906_4_, p_184906_1_, p_184906_2_[p_184906_3_++]);
        ScoreObjective scoreobjective = this.func_184903_a(p_184906_2_[p_184906_3_++], true, p_184906_4_);
        String s1 = p_184906_2_[p_184906_3_++];
        String s2 = func_184891_e(p_184906_4_, p_184906_1_, p_184906_2_[p_184906_3_++]);
        ScoreObjective scoreobjective1 = this.func_184903_a(p_184906_2_[p_184906_3_], false, p_184906_4_);

        if (s.length() > 40)
        {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] {s, Integer.valueOf(40)});
        }
        else if (s2.length() > 40)
        {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] {s2, Integer.valueOf(40)});
        }
        else
        {
            Score score = scoreboard.getOrCreateScore(s, scoreobjective);

            if (!scoreboard.entityHasObjective(s2, scoreobjective1))
            {
                throw new CommandException("commands.scoreboard.players.operation.notFound", new Object[] {scoreobjective1.getName(), s2});
            }
            else
            {
                Score score1 = scoreboard.getOrCreateScore(s2, scoreobjective1);

                if ("+=".equals(s1))
                {
                    score.setScorePoints(score.getScorePoints() + score1.getScorePoints());
                }
                else if ("-=".equals(s1))
                {
                    score.setScorePoints(score.getScorePoints() - score1.getScorePoints());
                }
                else if ("*=".equals(s1))
                {
                    score.setScorePoints(score.getScorePoints() * score1.getScorePoints());
                }
                else if ("/=".equals(s1))
                {
                    if (score1.getScorePoints() != 0)
                    {
                        score.setScorePoints(score.getScorePoints() / score1.getScorePoints());
                    }
                }
                else if ("%=".equals(s1))
                {
                    if (score1.getScorePoints() != 0)
                    {
                        score.setScorePoints(score.getScorePoints() % score1.getScorePoints());
                    }
                }
                else if ("=".equals(s1))
                {
                    score.setScorePoints(score1.getScorePoints());
                }
                else if ("<".equals(s1))
                {
                    score.setScorePoints(Math.min(score.getScorePoints(), score1.getScorePoints()));
                }
                else if (">".equals(s1))
                {
                    score.setScorePoints(Math.max(score.getScorePoints(), score1.getScorePoints()));
                }
                else
                {
                    if (!"><".equals(s1))
                    {
                        throw new CommandException("commands.scoreboard.players.operation.invalidOperation", new Object[] {s1});
                    }

                    int i = score.getScorePoints();
                    score.setScorePoints(score1.getScorePoints());
                    score1.setScorePoints(i);
                }

                func_152373_a(p_184906_1_, this, "commands.scoreboard.players.operation.success", new Object[0]);
            }
        }
    }

    protected void func_184924_a(MinecraftServer p_184924_1_, ICommandSender p_184924_2_, String[] p_184924_3_, int p_184924_4_) throws CommandException
    {
        String s = func_184891_e(p_184924_1_, p_184924_2_, p_184924_3_[p_184924_4_]);
        Entity entity = func_184885_b(p_184924_1_, p_184924_2_, p_184924_3_[p_184924_4_++]);
        String s1 = p_184924_3_[p_184924_4_++];
        Set<String> set = entity.getTags();

        if ("list".equals(s1))
        {
            if (!set.isEmpty())
            {
                TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.scoreboard.players.tag.list", new Object[] {s});
                textcomponenttranslation.getStyle().setColor(TextFormatting.DARK_GREEN);
                p_184924_2_.sendMessage(textcomponenttranslation);
                p_184924_2_.sendMessage(new TextComponentString(func_71527_a(set.toArray())));
            }

            p_184924_2_.func_174794_a(CommandResultStats.Type.QUERY_RESULT, set.size());
        }
        else if (p_184924_3_.length < 5)
        {
            throw new WrongUsageException("commands.scoreboard.players.tag.usage", new Object[0]);
        }
        else
        {
            String s2 = p_184924_3_[p_184924_4_++];

            if (p_184924_3_.length > p_184924_4_)
            {
                try
                {
                    NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(func_180529_a(p_184924_3_, p_184924_4_));
                    NBTTagCompound nbttagcompound1 = func_184887_a(entity);

                    if (!NBTUtil.areNBTEquals(nbttagcompound, nbttagcompound1, true))
                    {
                        throw new CommandException("commands.scoreboard.players.tag.tagMismatch", new Object[] {s});
                    }
                }
                catch (NBTException nbtexception)
                {
                    throw new CommandException("commands.scoreboard.players.tag.tagError", new Object[] {nbtexception.getMessage()});
                }
            }

            if ("add".equals(s1))
            {
                if (!entity.addTag(s2))
                {
                    throw new CommandException("commands.scoreboard.players.tag.tooMany", new Object[] {Integer.valueOf(1024)});
                }

                func_152373_a(p_184924_2_, this, "commands.scoreboard.players.tag.success.add", new Object[] {s2});
            }
            else
            {
                if (!"remove".equals(s1))
                {
                    throw new WrongUsageException("commands.scoreboard.players.tag.usage", new Object[0]);
                }

                if (!entity.removeTag(s2))
                {
                    throw new CommandException("commands.scoreboard.players.tag.notFound", new Object[] {s2});
                }

                func_152373_a(p_184924_2_, this, "commands.scoreboard.players.tag.success.remove", new Object[] {s2});
            }
        }
    }

    public List<String> func_184883_a(MinecraftServer p_184883_1_, ICommandSender p_184883_2_, String[] p_184883_3_, @Nullable BlockPos p_184883_4_)
    {
        if (p_184883_3_.length == 1)
        {
            return func_71530_a(p_184883_3_, new String[] {"objectives", "players", "teams"});
        }
        else
        {
            if ("objectives".equalsIgnoreCase(p_184883_3_[0]))
            {
                if (p_184883_3_.length == 2)
                {
                    return func_71530_a(p_184883_3_, new String[] {"list", "add", "remove", "setdisplay"});
                }

                if ("add".equalsIgnoreCase(p_184883_3_[1]))
                {
                    if (p_184883_3_.length == 4)
                    {
                        Set<String> set = IScoreCriteria.INSTANCES.keySet();
                        return func_175762_a(p_184883_3_, set);
                    }
                }
                else if ("remove".equalsIgnoreCase(p_184883_3_[1]))
                {
                    if (p_184883_3_.length == 3)
                    {
                        return func_175762_a(p_184883_3_, this.func_184926_a(false, p_184883_1_));
                    }
                }
                else if ("setdisplay".equalsIgnoreCase(p_184883_3_[1]))
                {
                    if (p_184883_3_.length == 3)
                    {
                        return func_71530_a(p_184883_3_, Scoreboard.getDisplaySlotStrings());
                    }

                    if (p_184883_3_.length == 4)
                    {
                        return func_175762_a(p_184883_3_, this.func_184926_a(false, p_184883_1_));
                    }
                }
            }
            else if ("players".equalsIgnoreCase(p_184883_3_[0]))
            {
                if (p_184883_3_.length == 2)
                {
                    return func_71530_a(p_184883_3_, new String[] {"set", "add", "remove", "reset", "list", "enable", "test", "operation", "tag"});
                }

                if (!"set".equalsIgnoreCase(p_184883_3_[1]) && !"add".equalsIgnoreCase(p_184883_3_[1]) && !"remove".equalsIgnoreCase(p_184883_3_[1]) && !"reset".equalsIgnoreCase(p_184883_3_[1]))
                {
                    if ("enable".equalsIgnoreCase(p_184883_3_[1]))
                    {
                        if (p_184883_3_.length == 3)
                        {
                            return func_71530_a(p_184883_3_, p_184883_1_.getOnlinePlayerNames());
                        }

                        if (p_184883_3_.length == 4)
                        {
                            return func_175762_a(p_184883_3_, this.func_184904_b(p_184883_1_));
                        }
                    }
                    else if (!"list".equalsIgnoreCase(p_184883_3_[1]) && !"test".equalsIgnoreCase(p_184883_3_[1]))
                    {
                        if ("operation".equalsIgnoreCase(p_184883_3_[1]))
                        {
                            if (p_184883_3_.length == 3)
                            {
                                return func_175762_a(p_184883_3_, this.func_184913_a(p_184883_1_).getObjectiveNames());
                            }

                            if (p_184883_3_.length == 4)
                            {
                                return func_175762_a(p_184883_3_, this.func_184926_a(true, p_184883_1_));
                            }

                            if (p_184883_3_.length == 5)
                            {
                                return func_71530_a(p_184883_3_, new String[] {"+=", "-=", "*=", "/=", "%=", "=", "<", ">", "><"});
                            }

                            if (p_184883_3_.length == 6)
                            {
                                return func_71530_a(p_184883_3_, p_184883_1_.getOnlinePlayerNames());
                            }

                            if (p_184883_3_.length == 7)
                            {
                                return func_175762_a(p_184883_3_, this.func_184926_a(false, p_184883_1_));
                            }
                        }
                        else if ("tag".equalsIgnoreCase(p_184883_3_[1]))
                        {
                            if (p_184883_3_.length == 3)
                            {
                                return func_175762_a(p_184883_3_, this.func_184913_a(p_184883_1_).getObjectiveNames());
                            }

                            if (p_184883_3_.length == 4)
                            {
                                return func_71530_a(p_184883_3_, new String[] {"add", "remove", "list"});
                            }
                        }
                    }
                    else
                    {
                        if (p_184883_3_.length == 3)
                        {
                            return func_175762_a(p_184883_3_, this.func_184913_a(p_184883_1_).getObjectiveNames());
                        }

                        if (p_184883_3_.length == 4 && "test".equalsIgnoreCase(p_184883_3_[1]))
                        {
                            return func_175762_a(p_184883_3_, this.func_184926_a(false, p_184883_1_));
                        }
                    }
                }
                else
                {
                    if (p_184883_3_.length == 3)
                    {
                        return func_71530_a(p_184883_3_, p_184883_1_.getOnlinePlayerNames());
                    }

                    if (p_184883_3_.length == 4)
                    {
                        return func_175762_a(p_184883_3_, this.func_184926_a(true, p_184883_1_));
                    }
                }
            }
            else if ("teams".equalsIgnoreCase(p_184883_3_[0]))
            {
                if (p_184883_3_.length == 2)
                {
                    return func_71530_a(p_184883_3_, new String[] {"add", "remove", "join", "leave", "empty", "list", "option"});
                }

                if ("join".equalsIgnoreCase(p_184883_3_[1]))
                {
                    if (p_184883_3_.length == 3)
                    {
                        return func_175762_a(p_184883_3_, this.func_184913_a(p_184883_1_).getTeamNames());
                    }

                    if (p_184883_3_.length >= 4)
                    {
                        return func_71530_a(p_184883_3_, p_184883_1_.getOnlinePlayerNames());
                    }
                }
                else
                {
                    if ("leave".equalsIgnoreCase(p_184883_3_[1]))
                    {
                        return func_71530_a(p_184883_3_, p_184883_1_.getOnlinePlayerNames());
                    }

                    if (!"empty".equalsIgnoreCase(p_184883_3_[1]) && !"list".equalsIgnoreCase(p_184883_3_[1]) && !"remove".equalsIgnoreCase(p_184883_3_[1]))
                    {
                        if ("option".equalsIgnoreCase(p_184883_3_[1]))
                        {
                            if (p_184883_3_.length == 3)
                            {
                                return func_175762_a(p_184883_3_, this.func_184913_a(p_184883_1_).getTeamNames());
                            }

                            if (p_184883_3_.length == 4)
                            {
                                return func_71530_a(p_184883_3_, new String[] {"color", "friendlyfire", "seeFriendlyInvisibles", "nametagVisibility", "deathMessageVisibility", "collisionRule"});
                            }

                            if (p_184883_3_.length == 5)
                            {
                                if ("color".equalsIgnoreCase(p_184883_3_[3]))
                                {
                                    return func_175762_a(p_184883_3_, TextFormatting.getValidValues(true, false));
                                }

                                if ("nametagVisibility".equalsIgnoreCase(p_184883_3_[3]) || "deathMessageVisibility".equalsIgnoreCase(p_184883_3_[3]))
                                {
                                    return func_71530_a(p_184883_3_, Team.EnumVisible.func_178825_a());
                                }

                                if ("collisionRule".equalsIgnoreCase(p_184883_3_[3]))
                                {
                                    return func_71530_a(p_184883_3_, Team.CollisionRule.func_186687_a());
                                }

                                if ("friendlyfire".equalsIgnoreCase(p_184883_3_[3]) || "seeFriendlyInvisibles".equalsIgnoreCase(p_184883_3_[3]))
                                {
                                    return func_71530_a(p_184883_3_, new String[] {"true", "false"});
                                }
                            }
                        }
                    }
                    else if (p_184883_3_.length == 3)
                    {
                        return func_175762_a(p_184883_3_, this.func_184913_a(p_184883_1_).getTeamNames());
                    }
                }
            }

            return Collections.<String>emptyList();
        }
    }

    protected List<String> func_184926_a(boolean p_184926_1_, MinecraftServer p_184926_2_)
    {
        Collection<ScoreObjective> collection = this.func_184913_a(p_184926_2_).getScoreObjectives();
        List<String> list = Lists.<String>newArrayList();

        for (ScoreObjective scoreobjective : collection)
        {
            if (!p_184926_1_ || !scoreobjective.getCriteria().isReadOnly())
            {
                list.add(scoreobjective.getName());
            }
        }

        return list;
    }

    protected List<String> func_184904_b(MinecraftServer p_184904_1_)
    {
        Collection<ScoreObjective> collection = this.func_184913_a(p_184904_1_).getScoreObjectives();
        List<String> list = Lists.<String>newArrayList();

        for (ScoreObjective scoreobjective : collection)
        {
            if (scoreobjective.getCriteria() == IScoreCriteria.TRIGGER)
            {
                list.add(scoreobjective.getName());
            }
        }

        return list;
    }

    public boolean func_82358_a(String[] p_82358_1_, int p_82358_2_)
    {
        if (!"players".equalsIgnoreCase(p_82358_1_[0]))
        {
            if ("teams".equalsIgnoreCase(p_82358_1_[0]))
            {
                return p_82358_2_ == 2;
            }
            else
            {
                return false;
            }
        }
        else if (p_82358_1_.length > 1 && "operation".equalsIgnoreCase(p_82358_1_[1]))
        {
            return p_82358_2_ == 2 || p_82358_2_ == 5;
        }
        else
        {
            return p_82358_2_ == 2;
        }
    }
}
