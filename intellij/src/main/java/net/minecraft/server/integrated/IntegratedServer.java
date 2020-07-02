package net.minecraft.server.integrated;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.profiler.Snooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.CryptManager;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.Util;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.ServerWorldEventHandler;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerDemo;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntegratedServer extends MinecraftServer
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final Minecraft mc;
    private final WorldSettings worldSettings;
    private boolean isGamePaused;
    private boolean field_71346_p;
    private ThreadLanServerPing lanServerPing;

    public IntegratedServer(Minecraft p_i46523_1_, String p_i46523_2_, String p_i46523_3_, WorldSettings p_i46523_4_, YggdrasilAuthenticationService p_i46523_5_, MinecraftSessionService p_i46523_6_, GameProfileRepository p_i46523_7_, PlayerProfileCache p_i46523_8_)
    {
        super(new File(p_i46523_1_.gameDir, "saves"), p_i46523_1_.getProxy(), p_i46523_1_.getDataFixer(), p_i46523_5_, p_i46523_6_, p_i46523_7_, p_i46523_8_);
        this.setServerOwner(p_i46523_1_.getSession().getUsername());
        this.func_71261_m(p_i46523_2_);
        this.setWorldName(p_i46523_3_);
        this.setDemo(p_i46523_1_.isDemo());
        this.canCreateBonusChest(p_i46523_4_.isBonusChestEnabled());
        this.setBuildLimit(256);
        this.setPlayerList(new IntegratedPlayerList(this));
        this.mc = p_i46523_1_;
        this.worldSettings = this.isDemo() ? WorldServerDemo.field_73071_a : p_i46523_4_;
    }

    public ServerCommandManager func_175582_h()
    {
        return new IntegratedServerCommandManager(this);
    }

    public void loadAllWorlds(String saveName, String worldNameIn, long seed, WorldType type, String generatorOptions)
    {
        this.convertMapIfNeeded(saveName);
        this.worlds = new WorldServer[3];
        this.field_71312_k = new long[this.worlds.length][100];
        ISaveHandler isavehandler = this.getActiveAnvilConverter().func_75804_a(saveName, true);
        this.setResourcePackFromWorld(this.getFolderName(), isavehandler);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();

        if (worldinfo == null)
        {
            worldinfo = new WorldInfo(this.worldSettings, worldNameIn);
        }
        else
        {
            worldinfo.setWorldName(worldNameIn);
        }

        for (int i = 0; i < this.worlds.length; ++i)
        {
            int j = 0;

            if (i == 1)
            {
                j = -1;
            }

            if (i == 2)
            {
                j = 1;
            }

            if (i == 0)
            {
                if (this.isDemo())
                {
                    this.worlds[i] = (WorldServer)(new WorldServerDemo(this, isavehandler, worldinfo, j, this.profiler)).func_175643_b();
                }
                else
                {
                    this.worlds[i] = (WorldServer)(new WorldServer(this, isavehandler, worldinfo, j, this.profiler)).func_175643_b();
                }

                this.worlds[i].func_72963_a(this.worldSettings);
            }
            else
            {
                this.worlds[i] = (WorldServer)(new WorldServerMulti(this, isavehandler, j, this.worlds[0], this.profiler)).func_175643_b();
            }

            this.worlds[i].func_72954_a(new ServerWorldEventHandler(this, this.worlds[i]));
        }

        this.getPlayerList().func_72364_a(this.worlds);

        if (this.worlds[0].getWorldInfo().getDifficulty() == null)
        {
            this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
        }

        this.func_71222_d();
    }

    /**
     * Initialises the server and starts it.
     */
    public boolean init() throws IOException
    {
        LOGGER.info("Starting integrated minecraft server version 1.12.2");
        this.setOnlineMode(true);
        this.setCanSpawnAnimals(true);
        this.setCanSpawnNPCs(true);
        this.setAllowPvp(true);
        this.setAllowFlight(true);
        LOGGER.info("Generating keypair");
        this.setKeyPair(CryptManager.generateKeyPair());
        this.loadAllWorlds(this.getFolderName(), this.getWorldName(), this.worldSettings.getSeed(), this.worldSettings.getTerrainType(), this.worldSettings.func_82749_j());
        this.setMOTD(this.getServerOwner() + " - " + this.worlds[0].getWorldInfo().getWorldName());
        return true;
    }

    /**
     * Main function called by run() every loop.
     */
    public void tick()
    {
        boolean flag = this.isGamePaused;
        this.isGamePaused = Minecraft.getInstance().getConnection() != null && Minecraft.getInstance().isGamePaused();

        if (!flag && this.isGamePaused)
        {
            LOGGER.info("Saving and pausing game...");
            this.getPlayerList().saveAllPlayerData();
            this.func_71267_a(false);
        }

        if (this.isGamePaused)
        {
            synchronized (this.field_175589_i)
            {
                while (!this.field_175589_i.isEmpty())
                {
                    Util.func_181617_a(this.field_175589_i.poll(), LOGGER);
                }
            }
        }
        else
        {
            super.tick();

            if (this.mc.gameSettings.renderDistanceChunks != this.getPlayerList().getViewDistance())
            {
                LOGGER.info("Changing view distance to {}, from {}", Integer.valueOf(this.mc.gameSettings.renderDistanceChunks), Integer.valueOf(this.getPlayerList().getViewDistance()));
                this.getPlayerList().func_152611_a(this.mc.gameSettings.renderDistanceChunks);
            }

            if (this.mc.world != null)
            {
                WorldInfo worldinfo1 = this.worlds[0].getWorldInfo();
                WorldInfo worldinfo = this.mc.world.getWorldInfo();

                if (!worldinfo1.isDifficultyLocked() && worldinfo.getDifficulty() != worldinfo1.getDifficulty())
                {
                    LOGGER.info("Changing difficulty to {}, from {}", worldinfo.getDifficulty(), worldinfo1.getDifficulty());
                    this.setDifficultyForAllWorlds(worldinfo.getDifficulty());
                }
                else if (worldinfo.isDifficultyLocked() && !worldinfo1.isDifficultyLocked())
                {
                    LOGGER.info("Locking difficulty to {}", (Object)worldinfo.getDifficulty());

                    for (WorldServer worldserver : this.worlds)
                    {
                        if (worldserver != null)
                        {
                            worldserver.getWorldInfo().setDifficultyLocked(true);
                        }
                    }
                }
            }
        }
    }

    public boolean canStructuresSpawn()
    {
        return false;
    }

    public GameType getGameType()
    {
        return this.worldSettings.getGameType();
    }

    /**
     * Get the server's difficulty
     */
    public EnumDifficulty getDifficulty()
    {
        return this.mc.world.getWorldInfo().getDifficulty();
    }

    /**
     * Defaults to false.
     */
    public boolean isHardcore()
    {
        return this.worldSettings.getHardcoreEnabled();
    }

    public boolean func_181034_q()
    {
        return true;
    }

    public boolean func_183002_r()
    {
        return true;
    }

    public void func_71267_a(boolean p_71267_1_)
    {
        super.func_71267_a(p_71267_1_);
    }

    public File getDataDirectory()
    {
        return this.mc.gameDir;
    }

    public boolean isDedicatedServer()
    {
        return false;
    }

    /**
     * Get if native transport should be used. Native transport means linux server performance improvements and
     * optimized packet sending/receiving on linux
     */
    public boolean shouldUseNativeTransport()
    {
        return false;
    }

    /**
     * Called on exit from the main run() loop.
     */
    public void finalTick(CrashReport report)
    {
        this.mc.crashed(report);
    }

    /**
     * Adds the server info, including from theWorldServer, to the crash report.
     */
    public CrashReport addServerInfoToCrashReport(CrashReport report)
    {
        report = super.addServerInfoToCrashReport(report);
        report.getCategory().addDetail("Type", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return "Integrated Server (map_client.txt)";
            }
        });
        report.getCategory().addDetail("Is Modded", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                String s = ClientBrandRetriever.getClientModName();

                if (!s.equals("vanilla"))
                {
                    return "Definitely; Client brand changed to '" + s + "'";
                }
                else
                {
                    s = IntegratedServer.this.getServerModName();

                    if (!"vanilla".equals(s))
                    {
                        return "Definitely; Server brand changed to '" + s + "'";
                    }
                    else
                    {
                        return Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and both client + server brands are untouched.";
                    }
                }
            }
        });
        return report;
    }

    public void setDifficultyForAllWorlds(EnumDifficulty difficulty)
    {
        super.setDifficultyForAllWorlds(difficulty);

        if (this.mc.world != null)
        {
            this.mc.world.getWorldInfo().setDifficulty(difficulty);
        }
    }

    public void fillSnooper(Snooper snooper)
    {
        super.fillSnooper(snooper);
        snooper.addClientStat("snooper_partner", this.mc.getSnooper().getUniqueID());
    }

    public boolean func_70002_Q()
    {
        return Minecraft.getInstance().func_70002_Q();
    }

    public String func_71206_a(GameType p_71206_1_, boolean p_71206_2_)
    {
        try
        {
            int i = -1;

            try
            {
                i = HttpUtil.getSuitableLanPort();
            }
            catch (IOException var5)
            {
                ;
            }

            if (i <= 0)
            {
                i = 25564;
            }

            this.getNetworkSystem().addEndpoint((InetAddress)null, i);
            LOGGER.info("Started on {}", (int)i);
            this.field_71346_p = true;
            this.lanServerPing = new ThreadLanServerPing(this.getMOTD(), i + "");
            this.lanServerPing.start();
            this.getPlayerList().setGameType(p_71206_1_);
            this.getPlayerList().setCommandsAllowedForAll(p_71206_2_);
            this.mc.player.setPermissionLevel(p_71206_2_ ? 4 : 0);
            return i + "";
        }
        catch (IOException var6)
        {
            return null;
        }
    }

    /**
     * Saves all necessary data as preparation for stopping the server.
     */
    public void stopServer()
    {
        super.stopServer();

        if (this.lanServerPing != null)
        {
            this.lanServerPing.interrupt();
            this.lanServerPing = null;
        }
    }

    /**
     * Sets the serverRunning variable to false, in order to get the server to shut down.
     */
    public void initiateShutdown()
    {
        Futures.getUnchecked(this.func_152344_a(new Runnable()
        {
            public void run()
            {
                for (EntityPlayerMP entityplayermp : Lists.newArrayList(IntegratedServer.this.getPlayerList().getPlayers()))
                {
                    if (!entityplayermp.getUniqueID().equals(IntegratedServer.this.mc.player.getUniqueID()))
                    {
                        IntegratedServer.this.getPlayerList().playerLoggedOut(entityplayermp);
                    }
                }
            }
        }));
        super.initiateShutdown();

        if (this.lanServerPing != null)
        {
            this.lanServerPing.interrupt();
            this.lanServerPing = null;
        }
    }

    /**
     * Returns true if this integrated server is open to LAN
     */
    public boolean getPublic()
    {
        return this.field_71346_p;
    }

    /**
     * Sets the game type for all worlds.
     */
    public void setGameType(GameType gameMode)
    {
        super.setGameType(gameMode);
        this.getPlayerList().setGameType(gameMode);
    }

    /**
     * Return whether command blocks are enabled.
     */
    public boolean isCommandBlockEnabled()
    {
        return true;
    }

    public int getOpPermissionLevel()
    {
        return 4;
    }
}
