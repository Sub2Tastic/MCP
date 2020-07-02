package net.minecraft.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.server.MinecraftServer;

public class ServerScoreboard extends Scoreboard
{
    private final MinecraftServer server;
    private final Set<ScoreObjective> addedObjectives = Sets.<ScoreObjective>newHashSet();
    private Runnable[] dirtyRunnables = new Runnable[0];

    public ServerScoreboard(MinecraftServer mcServer)
    {
        this.server = mcServer;
    }

    public void onScoreChanged(Score scoreIn)
    {
        super.onScoreChanged(scoreIn);

        if (this.addedObjectives.contains(scoreIn.getObjective()))
        {
            this.server.getPlayerList().sendPacketToAllPlayers(new SPacketUpdateScore(scoreIn));
        }

        this.markSaveDataDirty();
    }

    public void onPlayerRemoved(String scoreName)
    {
        super.onPlayerRemoved(scoreName);
        this.server.getPlayerList().sendPacketToAllPlayers(new SPacketUpdateScore(scoreName));
        this.markSaveDataDirty();
    }

    public void onPlayerScoreRemoved(String scoreName, ScoreObjective objective)
    {
        super.onPlayerScoreRemoved(scoreName, objective);
        this.server.getPlayerList().sendPacketToAllPlayers(new SPacketUpdateScore(scoreName, objective));
        this.markSaveDataDirty();
    }

    /**
     * 0 is tab menu, 1 is sidebar, 2 is below name
     */
    public void setObjectiveInDisplaySlot(int objectiveSlot, ScoreObjective objective)
    {
        ScoreObjective scoreobjective = this.getObjectiveInDisplaySlot(objectiveSlot);
        super.setObjectiveInDisplaySlot(objectiveSlot, objective);

        if (scoreobjective != objective && scoreobjective != null)
        {
            if (this.getObjectiveDisplaySlotCount(scoreobjective) > 0)
            {
                this.server.getPlayerList().sendPacketToAllPlayers(new SPacketDisplayObjective(objectiveSlot, objective));
            }
            else
            {
                this.sendDisplaySlotRemovalPackets(scoreobjective);
            }
        }

        if (objective != null)
        {
            if (this.addedObjectives.contains(objective))
            {
                this.server.getPlayerList().sendPacketToAllPlayers(new SPacketDisplayObjective(objectiveSlot, objective));
            }
            else
            {
                this.addObjective(objective);
            }
        }

        this.markSaveDataDirty();
    }

    public boolean func_151392_a(String p_151392_1_, String p_151392_2_)
    {
        if (super.func_151392_a(p_151392_1_, p_151392_2_))
        {
            ScorePlayerTeam scoreplayerteam = this.getTeam(p_151392_2_);
            this.server.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(scoreplayerteam, Arrays.asList(p_151392_1_), 3));
            this.markSaveDataDirty();
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Removes the given username from the given ScorePlayerTeam. If the player is not on the team then an
     * IllegalStateException is thrown.
     */
    public void removePlayerFromTeam(String username, ScorePlayerTeam playerTeam)
    {
        super.removePlayerFromTeam(username, playerTeam);
        this.server.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(playerTeam, Arrays.asList(username), 4));
        this.markSaveDataDirty();
    }

    public void onObjectiveAdded(ScoreObjective objective)
    {
        super.onObjectiveAdded(objective);
        this.markSaveDataDirty();
    }

    public void func_96532_b(ScoreObjective p_96532_1_)
    {
        super.func_96532_b(p_96532_1_);

        if (this.addedObjectives.contains(p_96532_1_))
        {
            this.server.getPlayerList().sendPacketToAllPlayers(new SPacketScoreboardObjective(p_96532_1_, 2));
        }

        this.markSaveDataDirty();
    }

    public void onObjectiveRemoved(ScoreObjective objective)
    {
        super.onObjectiveRemoved(objective);

        if (this.addedObjectives.contains(objective))
        {
            this.sendDisplaySlotRemovalPackets(objective);
        }

        this.markSaveDataDirty();
    }

    public void onTeamAdded(ScorePlayerTeam playerTeam)
    {
        super.onTeamAdded(playerTeam);
        this.server.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(playerTeam, 0));
        this.markSaveDataDirty();
    }

    public void onTeamChanged(ScorePlayerTeam playerTeam)
    {
        super.onTeamChanged(playerTeam);
        this.server.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(playerTeam, 2));
        this.markSaveDataDirty();
    }

    public void onTeamRemoved(ScorePlayerTeam playerTeam)
    {
        super.onTeamRemoved(playerTeam);
        this.server.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(playerTeam, 1));
        this.markSaveDataDirty();
    }

    public void addDirtyRunnable(Runnable runnable)
    {
        this.dirtyRunnables = (Runnable[])Arrays.copyOf(this.dirtyRunnables, this.dirtyRunnables.length + 1);
        this.dirtyRunnables[this.dirtyRunnables.length - 1] = runnable;
    }

    protected void markSaveDataDirty()
    {
        for (Runnable runnable : this.dirtyRunnables)
        {
            runnable.run();
        }
    }

    public List < Packet<? >> getCreatePackets(ScoreObjective objective)
    {
        List < Packet<? >> list = Lists. < Packet<? >> newArrayList();
        list.add(new SPacketScoreboardObjective(objective, 0));

        for (int i = 0; i < 19; ++i)
        {
            if (this.getObjectiveInDisplaySlot(i) == objective)
            {
                list.add(new SPacketDisplayObjective(i, objective));
            }
        }

        for (Score score : this.getSortedScores(objective))
        {
            list.add(new SPacketUpdateScore(score));
        }

        return list;
    }

    public void addObjective(ScoreObjective objective)
    {
        List < Packet<? >> list = this.getCreatePackets(objective);

        for (EntityPlayerMP entityplayermp : this.server.getPlayerList().getPlayers())
        {
            for (Packet<?> packet : list)
            {
                entityplayermp.connection.sendPacket(packet);
            }
        }

        this.addedObjectives.add(objective);
    }

    public List < Packet<? >> getDestroyPackets(ScoreObjective p_96548_1_)
    {
        List < Packet<? >> list = Lists. < Packet<? >> newArrayList();
        list.add(new SPacketScoreboardObjective(p_96548_1_, 1));

        for (int i = 0; i < 19; ++i)
        {
            if (this.getObjectiveInDisplaySlot(i) == p_96548_1_)
            {
                list.add(new SPacketDisplayObjective(i, p_96548_1_));
            }
        }

        return list;
    }

    public void sendDisplaySlotRemovalPackets(ScoreObjective p_96546_1_)
    {
        List < Packet<? >> list = this.getDestroyPackets(p_96546_1_);

        for (EntityPlayerMP entityplayermp : this.server.getPlayerList().getPlayers())
        {
            for (Packet<?> packet : list)
            {
                entityplayermp.connection.sendPacket(packet);
            }
        }

        this.addedObjectives.remove(p_96546_1_);
    }

    public int getObjectiveDisplaySlotCount(ScoreObjective p_96552_1_)
    {
        int i = 0;

        for (int j = 0; j < 19; ++j)
        {
            if (this.getObjectiveInDisplaySlot(j) == p_96552_1_)
            {
                ++i;
            }
        }

        return i;
    }
}