package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkSystem;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.profiler.Profiler;
import net.minecraft.profiler.Snooper;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ITickable;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.ServerWorldEventHandler;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerDemo;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class MinecraftServer implements ICommandSender, Runnable, IThreadListener, ISnooperInfo
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final File USER_CACHE_FILE = new File("usercache.json");
    private final ISaveFormat anvilConverterForAnvilFile;
    private final Snooper snooper = new Snooper("server", this, func_130071_aq());
    private final File anvilFile;
    private final List<ITickable> tickables = Lists.<ITickable>newArrayList();
    public final ICommandManager field_71321_q;
    public final Profiler profiler = new Profiler();
    private final NetworkSystem networkSystem;
    private final ServerStatusResponse statusResponse = new ServerStatusResponse();
    private final Random random = new Random();
    private final DataFixer dataFixer;
    private int serverPort = -1;
    public WorldServer[] worlds;
    private PlayerList playerList;
    private boolean serverRunning = true;
    private boolean serverStopped;
    private int tickCounter;
    protected final Proxy serverProxy;
    public String field_71302_d;
    public int field_71303_e;
    private boolean onlineMode;
    private boolean preventProxyConnections;
    private boolean canSpawnAnimals;
    private boolean canSpawnNPCs;
    private boolean pvpEnabled;
    private boolean allowFlight;
    private String motd;
    private int buildLimit;
    private int maxPlayerIdleMinutes;
    public final long[] tickTimeArray = new long[100];
    public long[][] field_71312_k;
    private KeyPair serverKeyPair;
    private String serverOwner;
    private String folderName;
    private String worldName;
    private boolean isDemo;
    private boolean enableBonusChest;

    /** The texture pack for the server */
    private String resourcePackUrl = "";
    private String resourcePackHash = "";
    private boolean serverIsRunning;
    private long timeOfLastWarning;
    private String userMessage;
    private boolean startProfiling;
    private boolean isGamemodeForced;
    private final YggdrasilAuthenticationService authService;
    private final MinecraftSessionService sessionService;
    private final GameProfileRepository profileRepo;
    private final PlayerProfileCache profileCache;
    private long nanoTimeSinceStatusRefresh;
    public final Queue < FutureTask<? >> field_175589_i = Queues. < FutureTask<? >> newArrayDeque();
    private Thread serverThread;
    private long field_175591_ab = func_130071_aq();
    private boolean worldIconSet;

    public MinecraftServer(File p_i47054_1_, Proxy p_i47054_2_, DataFixer p_i47054_3_, YggdrasilAuthenticationService p_i47054_4_, MinecraftSessionService p_i47054_5_, GameProfileRepository p_i47054_6_, PlayerProfileCache p_i47054_7_)
    {
        this.serverProxy = p_i47054_2_;
        this.authService = p_i47054_4_;
        this.sessionService = p_i47054_5_;
        this.profileRepo = p_i47054_6_;
        this.profileCache = p_i47054_7_;
        this.anvilFile = p_i47054_1_;
        this.networkSystem = new NetworkSystem(this);
        this.field_71321_q = this.func_175582_h();
        this.anvilConverterForAnvilFile = new AnvilSaveConverter(p_i47054_1_, p_i47054_3_);
        this.dataFixer = p_i47054_3_;
    }

    public ServerCommandManager func_175582_h()
    {
        return new ServerCommandManager(this);
    }

    /**
     * Initialises the server and starts it.
     */
    public abstract boolean init() throws IOException;

    public void convertMapIfNeeded(String worldNameIn)
    {
        if (this.getActiveAnvilConverter().isOldMapFormat(worldNameIn))
        {
            LOGGER.info("Converting map!");
            this.func_71192_d("menu.convertingLevel");
            this.getActiveAnvilConverter().convertMapFormat(worldNameIn, new IProgressUpdate()
            {
                private long startTime = System.currentTimeMillis();
                public void func_73720_a(String p_73720_1_)
                {
                }
                public void func_73721_b(String p_73721_1_)
                {
                }
                public void setLoadingProgress(int progress)
                {
                    if (System.currentTimeMillis() - this.startTime >= 1000L)
                    {
                        this.startTime = System.currentTimeMillis();
                        MinecraftServer.LOGGER.info("Converting... {}%", (int)progress);
                    }
                }
                public void setDoneWorking()
                {
                }
                public void func_73719_c(String p_73719_1_)
                {
                }
            });
        }
    }

    protected synchronized void func_71192_d(String p_71192_1_)
    {
        this.userMessage = p_71192_1_;
    }

    @Nullable

    public synchronized String func_71195_b_()
    {
        return this.userMessage;
    }

    public void loadAllWorlds(String saveName, String worldNameIn, long seed, WorldType type, String generatorOptions)
    {
        this.convertMapIfNeeded(saveName);
        this.func_71192_d("menu.loadingLevel");
        this.worlds = new WorldServer[3];
        this.field_71312_k = new long[this.worlds.length][100];
        ISaveHandler isavehandler = this.anvilConverterForAnvilFile.func_75804_a(saveName, true);
        this.setResourcePackFromWorld(this.getFolderName(), isavehandler);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        WorldSettings worldsettings;

        if (worldinfo == null)
        {
            if (this.isDemo())
            {
                worldsettings = WorldServerDemo.field_73071_a;
            }
            else
            {
                worldsettings = new WorldSettings(seed, this.getGameType(), this.canStructuresSpawn(), this.isHardcore(), type);
                worldsettings.func_82750_a(generatorOptions);

                if (this.enableBonusChest)
                {
                    worldsettings.enableBonusChest();
                }
            }

            worldinfo = new WorldInfo(worldsettings, worldNameIn);
        }
        else
        {
            worldinfo.setWorldName(worldNameIn);
            worldsettings = new WorldSettings(worldinfo);
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

                this.worlds[i].func_72963_a(worldsettings);
            }
            else
            {
                this.worlds[i] = (WorldServer)(new WorldServerMulti(this, isavehandler, j, this.worlds[0], this.profiler)).func_175643_b();
            }

            this.worlds[i].func_72954_a(new ServerWorldEventHandler(this, this.worlds[i]));

            if (!this.isSinglePlayer())
            {
                this.worlds[i].getWorldInfo().setGameType(this.getGameType());
            }
        }

        this.playerList.func_72364_a(this.worlds);
        this.setDifficultyForAllWorlds(this.getDifficulty());
        this.func_71222_d();
    }

    public void func_71222_d()
    {
        int i = 16;
        int j = 4;
        int k = 192;
        int l = 625;
        int i1 = 0;
        this.func_71192_d("menu.generatingTerrain");
        int j1 = 0;
        LOGGER.info("Preparing start region for level 0");
        WorldServer worldserver = this.worlds[0];
        BlockPos blockpos = worldserver.getSpawnPoint();
        long k1 = func_130071_aq();

        for (int l1 = -192; l1 <= 192 && this.isServerRunning(); l1 += 16)
        {
            for (int i2 = -192; i2 <= 192 && this.isServerRunning(); i2 += 16)
            {
                long j2 = func_130071_aq();

                if (j2 - k1 > 1000L)
                {
                    this.func_71216_a_("Preparing spawn area", i1 * 100 / 625);
                    k1 = j2;
                }

                ++i1;
                worldserver.getChunkProvider().func_186025_d(blockpos.getX() + l1 >> 4, blockpos.getZ() + i2 >> 4);
            }
        }

        this.func_71243_i();
    }

    public void setResourcePackFromWorld(String worldNameIn, ISaveHandler saveHandlerIn)
    {
        File file1 = new File(saveHandlerIn.getWorldDirectory(), "resources.zip");

        if (file1.isFile())
        {
            try
            {
                this.setResourcePack("level://" + URLEncoder.encode(worldNameIn, StandardCharsets.UTF_8.toString()) + "/" + "resources.zip", "");
            }
            catch (UnsupportedEncodingException var5)
            {
                LOGGER.warn("Something went wrong url encoding {}", (Object)worldNameIn);
            }
        }
    }

    public abstract boolean canStructuresSpawn();

    public abstract GameType getGameType();

    /**
     * Get the server's difficulty
     */
    public abstract EnumDifficulty getDifficulty();

    /**
     * Defaults to false.
     */
    public abstract boolean isHardcore();

    public abstract int getOpPermissionLevel();

    public abstract boolean func_181034_q();

    public abstract boolean func_183002_r();

    protected void func_71216_a_(String p_71216_1_, int p_71216_2_)
    {
        this.field_71302_d = p_71216_1_;
        this.field_71303_e = p_71216_2_;
        LOGGER.info("{}: {}%", p_71216_1_, Integer.valueOf(p_71216_2_));
    }

    protected void func_71243_i()
    {
        this.field_71302_d = null;
        this.field_71303_e = 0;
    }

    public void func_71267_a(boolean p_71267_1_)
    {
        for (WorldServer worldserver : this.worlds)
        {
            if (worldserver != null)
            {
                if (!p_71267_1_)
                {
                    LOGGER.info("Saving chunks for level '{}'/{}", worldserver.getWorldInfo().getWorldName(), worldserver.dimension.getType().func_186065_b());
                }

                try
                {
                    worldserver.func_73044_a(true, (IProgressUpdate)null);
                }
                catch (MinecraftException minecraftexception)
                {
                    LOGGER.warn(minecraftexception.getMessage());
                }
            }
        }
    }

    /**
     * Saves all necessary data as preparation for stopping the server.
     */
    public void stopServer()
    {
        LOGGER.info("Stopping server");

        if (this.getNetworkSystem() != null)
        {
            this.getNetworkSystem().terminateEndpoints();
        }

        if (this.playerList != null)
        {
            LOGGER.info("Saving players");
            this.playerList.saveAllPlayerData();
            this.playerList.removeAllPlayers();
        }

        if (this.worlds != null)
        {
            LOGGER.info("Saving worlds");

            for (WorldServer worldserver : this.worlds)
            {
                if (worldserver != null)
                {
                    worldserver.disableLevelSaving = false;
                }
            }

            this.func_71267_a(false);

            for (WorldServer worldserver1 : this.worlds)
            {
                if (worldserver1 != null)
                {
                    worldserver1.func_73041_k();
                }
            }
        }

        if (this.snooper.isSnooperRunning())
        {
            this.snooper.stop();
        }
    }

    public boolean isServerRunning()
    {
        return this.serverRunning;
    }

    /**
     * Sets the serverRunning variable to false, in order to get the server to shut down.
     */
    public void initiateShutdown()
    {
        this.serverRunning = false;
    }

    public void run()
    {
        try
        {
            if (this.init())
            {
                this.field_175591_ab = func_130071_aq();
                long i = 0L;
                this.statusResponse.setServerDescription(new TextComponentString(this.motd));
                this.statusResponse.setVersion(new ServerStatusResponse.Version("1.12.2", 340));
                this.applyServerIconToResponse(this.statusResponse);

                while (this.serverRunning)
                {
                    long k = func_130071_aq();
                    long j = k - this.field_175591_ab;

                    if (j > 2000L && this.field_175591_ab - this.timeOfLastWarning >= 15000L)
                    {
                        LOGGER.warn("Can't keep up! Did the system time change, or is the server overloaded? Running {}ms behind, skipping {} tick(s)", Long.valueOf(j), Long.valueOf(j / 50L));
                        j = 2000L;
                        this.timeOfLastWarning = this.field_175591_ab;
                    }

                    if (j < 0L)
                    {
                        LOGGER.warn("Time ran backwards! Did the system time change?");
                        j = 0L;
                    }

                    i += j;
                    this.field_175591_ab = k;

                    if (this.worlds[0].func_73056_e())
                    {
                        this.tick();
                        i = 0L;
                    }
                    else
                    {
                        while (i > 50L)
                        {
                            i -= 50L;
                            this.tick();
                        }
                    }

                    Thread.sleep(Math.max(1L, 50L - i));
                    this.serverIsRunning = true;
                }
            }
            else
            {
                this.finalTick((CrashReport)null);
            }
        }
        catch (Throwable throwable1)
        {
            LOGGER.error("Encountered an unexpected exception", throwable1);
            CrashReport crashreport = null;

            if (throwable1 instanceof ReportedException)
            {
                crashreport = this.addServerInfoToCrashReport(((ReportedException)throwable1).getCrashReport());
            }
            else
            {
                crashreport = this.addServerInfoToCrashReport(new CrashReport("Exception in server tick loop", throwable1));
            }

            File file1 = new File(new File(this.getDataDirectory(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

            if (crashreport.saveToFile(file1))
            {
                LOGGER.error("This crash report has been saved to: {}", (Object)file1.getAbsolutePath());
            }
            else
            {
                LOGGER.error("We were unable to save this crash report to disk.");
            }

            this.finalTick(crashreport);
        }
        finally
        {
            try
            {
                this.serverStopped = true;
                this.stopServer();
            }
            catch (Throwable throwable)
            {
                LOGGER.error("Exception stopping the server", throwable);
            }
            finally
            {
                this.systemExitNow();
            }
        }
    }

    public void applyServerIconToResponse(ServerStatusResponse response)
    {
        File file1 = this.getFile("server-icon.png");

        if (!file1.exists())
        {
            file1 = this.getActiveAnvilConverter().getFile(this.getFolderName(), "icon.png");
        }

        if (file1.isFile())
        {
            ByteBuf bytebuf = Unpooled.buffer();

            try
            {
                BufferedImage bufferedimage = ImageIO.read(file1);
                Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide");
                Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high");
                ImageIO.write(bufferedimage, "PNG", new ByteBufOutputStream(bytebuf));
                ByteBuf bytebuf1 = Base64.encode(bytebuf);
                response.setFavicon("data:image/png;base64," + bytebuf1.toString(StandardCharsets.UTF_8));
            }
            catch (Exception exception)
            {
                LOGGER.error("Couldn't load server icon", (Throwable)exception);
            }
            finally
            {
                bytebuf.release();
            }
        }
    }

    public boolean isWorldIconSet()
    {
        this.worldIconSet = this.worldIconSet || this.getWorldIconFile().isFile();
        return this.worldIconSet;
    }

    public File getWorldIconFile()
    {
        return this.getActiveAnvilConverter().getFile(this.getFolderName(), "icon.png");
    }

    public File getDataDirectory()
    {
        return new File(".");
    }

    /**
     * Called on exit from the main run() loop.
     */
    public void finalTick(CrashReport report)
    {
    }

    /**
     * Directly calls System.exit(0), instantly killing the program.
     */
    public void systemExitNow()
    {
    }

    /**
     * Main function called by run() every loop.
     */
    public void tick()
    {
        long i = System.nanoTime();
        ++this.tickCounter;

        if (this.startProfiling)
        {
            this.startProfiling = false;
            this.profiler.field_76327_a = true;
            this.profiler.func_76317_a();
        }

        this.profiler.startSection("root");
        this.updateTimeLightAndEntities();

        if (i - this.nanoTimeSinceStatusRefresh >= 5000000000L)
        {
            this.nanoTimeSinceStatusRefresh = i;
            this.statusResponse.setPlayers(new ServerStatusResponse.Players(this.getMaxPlayers(), this.getCurrentPlayerCount()));
            GameProfile[] agameprofile = new GameProfile[Math.min(this.getCurrentPlayerCount(), 12)];
            int j = MathHelper.nextInt(this.random, 0, this.getCurrentPlayerCount() - agameprofile.length);

            for (int k = 0; k < agameprofile.length; ++k)
            {
                agameprofile[k] = ((EntityPlayerMP)this.playerList.getPlayers().get(j + k)).getGameProfile();
            }

            Collections.shuffle(Arrays.asList(agameprofile));
            this.statusResponse.getPlayers().setPlayers(agameprofile);
        }

        if (this.tickCounter % 900 == 0)
        {
            this.profiler.startSection("save");
            this.playerList.saveAllPlayerData();
            this.func_71267_a(true);
            this.profiler.endSection();
        }

        this.profiler.startSection("tallying");
        this.tickTimeArray[this.tickCounter % 100] = System.nanoTime() - i;
        this.profiler.endSection();
        this.profiler.startSection("snooper");

        if (!this.snooper.isSnooperRunning() && this.tickCounter > 100)
        {
            this.snooper.start();
        }

        if (this.tickCounter % 6000 == 0)
        {
            this.snooper.addMemoryStatsToSnooper();
        }

        this.profiler.endSection();
        this.profiler.endSection();
    }

    public void updateTimeLightAndEntities()
    {
        this.profiler.startSection("jobs");

        synchronized (this.field_175589_i)
        {
            while (!this.field_175589_i.isEmpty())
            {
                Util.func_181617_a(this.field_175589_i.poll(), LOGGER);
            }
        }

        this.profiler.func_76318_c("levels");

        for (int j = 0; j < this.worlds.length; ++j)
        {
            long i = System.nanoTime();

            if (j == 0 || this.getAllowNether())
            {
                WorldServer worldserver = this.worlds[j];
                this.profiler.startSection(() ->
                {
                    return worldserver.getWorldInfo().getWorldName();
                });

                if (this.tickCounter % 20 == 0)
                {
                    this.profiler.startSection("timeSync");
                    this.playerList.sendPacketToAllPlayersInDimension(new SPacketTimeUpdate(worldserver.getGameTime(), worldserver.getDayTime(), worldserver.getGameRules().func_82766_b("doDaylightCycle")), worldserver.dimension.getType().getId());
                    this.profiler.endSection();
                }

                this.profiler.startSection("tick");

                try
                {
                    worldserver.tick();
                }
                catch (Throwable throwable1)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Exception ticking world");
                    worldserver.fillCrashReport(crashreport);
                    throw new ReportedException(crashreport);
                }

                try
                {
                    worldserver.func_72939_s();
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Exception ticking world entities");
                    worldserver.fillCrashReport(crashreport1);
                    throw new ReportedException(crashreport1);
                }

                this.profiler.endSection();
                this.profiler.startSection("tracker");
                worldserver.func_73039_n().func_72788_a();
                this.profiler.endSection();
                this.profiler.endSection();
            }

            this.field_71312_k[j][this.tickCounter % 100] = System.nanoTime() - i;
        }

        this.profiler.func_76318_c("connection");
        this.getNetworkSystem().tick();
        this.profiler.func_76318_c("players");
        this.playerList.tick();
        this.profiler.func_76318_c("commandFunctions");
        this.getFunctionManager().tick();
        this.profiler.func_76318_c("tickables");

        for (int k = 0; k < this.tickables.size(); ++k)
        {
            ((ITickable)this.tickables.get(k)).tick();
        }

        this.profiler.endSection();
    }

    public boolean getAllowNether()
    {
        return true;
    }

    public void startServerThread()
    {
        this.serverThread = new Thread(this, "Server thread");
        this.serverThread.start();
    }

    /**
     * Returns a File object from the specified string.
     */
    public File getFile(String fileName)
    {
        return new File(this.getDataDirectory(), fileName);
    }

    /**
     * Logs the message with a level of WARN.
     */
    public void logWarning(String msg)
    {
        LOGGER.warn(msg);
    }

    /**
     * Gets the worldServer by the given dimension.
     */
    public WorldServer getWorld(int dimension)
    {
        if (dimension == -1)
        {
            return this.worlds[1];
        }
        else
        {
            return dimension == 1 ? this.worlds[2] : this.worlds[0];
        }
    }

    /**
     * Returns the server's Minecraft version as string.
     */
    public String getMinecraftVersion()
    {
        return "1.12.2";
    }

    /**
     * Returns the number of players currently on the server.
     */
    public int getCurrentPlayerCount()
    {
        return this.playerList.getCurrentPlayerCount();
    }

    /**
     * Returns the maximum number of players allowed on the server.
     */
    public int getMaxPlayers()
    {
        return this.playerList.getMaxPlayers();
    }

    /**
     * Returns an array of the usernames of all the connected players.
     */
    public String[] getOnlinePlayerNames()
    {
        return this.playerList.getOnlinePlayerNames();
    }

    public GameProfile[] func_152357_F()
    {
        return this.playerList.func_152600_g();
    }

    public String getServerModName()
    {
        return "vanilla";
    }

    /**
     * Adds the server info, including from theWorldServer, to the crash report.
     */
    public CrashReport addServerInfoToCrashReport(CrashReport report)
    {
        report.getCategory().addDetail("Profiler Position", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return MinecraftServer.this.profiler.field_76327_a ? MinecraftServer.this.profiler.func_76322_c() : "N/A (disabled)";
            }
        });

        if (this.playerList != null)
        {
            report.getCategory().addDetail("Player Count", new ICrashReportDetail<String>()
            {
                public String call()
                {
                    return MinecraftServer.this.playerList.getCurrentPlayerCount() + " / " + MinecraftServer.this.playerList.getMaxPlayers() + "; " + MinecraftServer.this.playerList.getPlayers();
                }
            });
        }

        return report;
    }

    public List<String> func_184104_a(ICommandSender p_184104_1_, String p_184104_2_, @Nullable BlockPos p_184104_3_, boolean p_184104_4_)
    {
        List<String> list = Lists.<String>newArrayList();
        boolean flag = p_184104_2_.startsWith("/");

        if (flag)
        {
            p_184104_2_ = p_184104_2_.substring(1);
        }

        if (!flag && !p_184104_4_)
        {
            String[] astring = p_184104_2_.split(" ", -1);
            String s2 = astring[astring.length - 1];

            for (String s1 : this.playerList.getOnlinePlayerNames())
            {
                if (CommandBase.func_71523_a(s2, s1))
                {
                    list.add(s1);
                }
            }

            return list;
        }
        else
        {
            boolean flag1 = !p_184104_2_.contains(" ");
            List<String> list1 = this.field_71321_q.func_180524_a(p_184104_1_, p_184104_2_, p_184104_3_);

            if (!list1.isEmpty())
            {
                for (String s : list1)
                {
                    if (flag1 && !p_184104_4_)
                    {
                        list.add("/" + s);
                    }
                    else
                    {
                        list.add(s);
                    }
                }
            }

            return list;
        }
    }

    public boolean isAnvilFileSet()
    {
        return this.anvilFile != null;
    }

    public String func_70005_c_()
    {
        return "Server";
    }

    /**
     * Send a chat message to the CommandSender
     */
    public void sendMessage(ITextComponent component)
    {
        LOGGER.info(component.func_150260_c());
    }

    public boolean func_70003_b(int p_70003_1_, String p_70003_2_)
    {
        return true;
    }

    public ICommandManager func_71187_D()
    {
        return this.field_71321_q;
    }

    /**
     * Gets KeyPair instanced in MinecraftServer.
     */
    public KeyPair getKeyPair()
    {
        return this.serverKeyPair;
    }

    /**
     * Returns the username of the server owner (for integrated servers)
     */
    public String getServerOwner()
    {
        return this.serverOwner;
    }

    /**
     * Sets the username of the owner of this server (in the case of an integrated server)
     */
    public void setServerOwner(String owner)
    {
        this.serverOwner = owner;
    }

    public boolean isSinglePlayer()
    {
        return this.serverOwner != null;
    }

    public String getFolderName()
    {
        return this.folderName;
    }

    public void func_71261_m(String p_71261_1_)
    {
        this.folderName = p_71261_1_;
    }

    public void setWorldName(String worldNameIn)
    {
        this.worldName = worldNameIn;
    }

    public String getWorldName()
    {
        return this.worldName;
    }

    public void setKeyPair(KeyPair keyPair)
    {
        this.serverKeyPair = keyPair;
    }

    public void setDifficultyForAllWorlds(EnumDifficulty difficulty)
    {
        for (WorldServer worldserver1 : this.worlds)
        {
            if (worldserver1 != null)
            {
                if (worldserver1.getWorldInfo().isHardcore())
                {
                    worldserver1.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
                    worldserver1.setAllowedSpawnTypes(true, true);
                }
                else if (this.isSinglePlayer())
                {
                    worldserver1.getWorldInfo().setDifficulty(difficulty);
                    worldserver1.setAllowedSpawnTypes(worldserver1.getDifficulty() != EnumDifficulty.PEACEFUL, true);
                }
                else
                {
                    worldserver1.getWorldInfo().setDifficulty(difficulty);
                    worldserver1.setAllowedSpawnTypes(this.allowSpawnMonsters(), this.canSpawnAnimals);
                }
            }
        }
    }

    public boolean allowSpawnMonsters()
    {
        return true;
    }

    /**
     * Gets whether this is a demo or not.
     */
    public boolean isDemo()
    {
        return this.isDemo;
    }

    /**
     * Sets whether this is a demo or not.
     */
    public void setDemo(boolean demo)
    {
        this.isDemo = demo;
    }

    public void canCreateBonusChest(boolean enable)
    {
        this.enableBonusChest = enable;
    }

    public ISaveFormat getActiveAnvilConverter()
    {
        return this.anvilConverterForAnvilFile;
    }

    public String getResourcePackUrl()
    {
        return this.resourcePackUrl;
    }

    public String getResourcePackHash()
    {
        return this.resourcePackHash;
    }

    public void setResourcePack(String url, String hash)
    {
        this.resourcePackUrl = url;
        this.resourcePackHash = hash;
    }

    public void fillSnooper(Snooper snooper)
    {
        snooper.addClientStat("whitelist_enabled", Boolean.valueOf(false));
        snooper.addClientStat("whitelist_count", Integer.valueOf(0));

        if (this.playerList != null)
        {
            snooper.addClientStat("players_current", Integer.valueOf(this.getCurrentPlayerCount()));
            snooper.addClientStat("players_max", Integer.valueOf(this.getMaxPlayers()));
            snooper.addClientStat("players_seen", Integer.valueOf(this.playerList.func_72373_m().length));
        }

        snooper.addClientStat("uses_auth", Boolean.valueOf(this.onlineMode));
        snooper.addClientStat("gui_state", this.getGuiEnabled() ? "enabled" : "disabled");
        snooper.addClientStat("run_time", Long.valueOf((func_130071_aq() - snooper.getMinecraftStartTimeMillis()) / 60L * 1000L));
        snooper.addClientStat("avg_tick_ms", Integer.valueOf((int)(MathHelper.average(this.tickTimeArray) * 1.0E-6D)));
        int l = 0;

        if (this.worlds != null)
        {
            for (WorldServer worldserver1 : this.worlds)
            {
                if (worldserver1 != null)
                {
                    WorldInfo worldinfo = worldserver1.getWorldInfo();
                    snooper.addClientStat("world[" + l + "][dimension]", Integer.valueOf(worldserver1.dimension.getType().getId()));
                    snooper.addClientStat("world[" + l + "][mode]", worldinfo.getGameType());
                    snooper.addClientStat("world[" + l + "][difficulty]", worldserver1.getDifficulty());
                    snooper.addClientStat("world[" + l + "][hardcore]", Boolean.valueOf(worldinfo.isHardcore()));
                    snooper.addClientStat("world[" + l + "][generator_name]", worldinfo.getGenerator().func_77127_a());
                    snooper.addClientStat("world[" + l + "][generator_version]", Integer.valueOf(worldinfo.getGenerator().getVersion()));
                    snooper.addClientStat("world[" + l + "][height]", Integer.valueOf(this.buildLimit));
                    snooper.addClientStat("world[" + l + "][chunks_loaded]", Integer.valueOf(worldserver1.getChunkProvider().getLoadedChunkCount()));
                    ++l;
                }
            }
        }

        snooper.addClientStat("worlds", Integer.valueOf(l));
    }

    public void func_70001_b(Snooper p_70001_1_)
    {
        p_70001_1_.addStatToSnooper("singleplayer", Boolean.valueOf(this.isSinglePlayer()));
        p_70001_1_.addStatToSnooper("server_brand", this.getServerModName());
        p_70001_1_.addStatToSnooper("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
        p_70001_1_.addStatToSnooper("dedicated", Boolean.valueOf(this.isDedicatedServer()));
    }

    public boolean func_70002_Q()
    {
        return true;
    }

    public abstract boolean isDedicatedServer();

    public boolean isServerInOnlineMode()
    {
        return this.onlineMode;
    }

    public void setOnlineMode(boolean online)
    {
        this.onlineMode = online;
    }

    public boolean getPreventProxyConnections()
    {
        return this.preventProxyConnections;
    }

    public boolean getCanSpawnAnimals()
    {
        return this.canSpawnAnimals;
    }

    public void setCanSpawnAnimals(boolean spawnAnimals)
    {
        this.canSpawnAnimals = spawnAnimals;
    }

    public boolean getCanSpawnNPCs()
    {
        return this.canSpawnNPCs;
    }

    /**
     * Get if native transport should be used. Native transport means linux server performance improvements and
     * optimized packet sending/receiving on linux
     */
    public abstract boolean shouldUseNativeTransport();

    public void setCanSpawnNPCs(boolean spawnNpcs)
    {
        this.canSpawnNPCs = spawnNpcs;
    }

    public boolean isPVPEnabled()
    {
        return this.pvpEnabled;
    }

    public void setAllowPvp(boolean allowPvp)
    {
        this.pvpEnabled = allowPvp;
    }

    public boolean isFlightAllowed()
    {
        return this.allowFlight;
    }

    public void setAllowFlight(boolean allow)
    {
        this.allowFlight = allow;
    }

    /**
     * Return whether command blocks are enabled.
     */
    public abstract boolean isCommandBlockEnabled();

    public String getMOTD()
    {
        return this.motd;
    }

    public void setMOTD(String motdIn)
    {
        this.motd = motdIn;
    }

    public int getBuildLimit()
    {
        return this.buildLimit;
    }

    public void setBuildLimit(int maxBuildHeight)
    {
        this.buildLimit = maxBuildHeight;
    }

    public boolean isServerStopped()
    {
        return this.serverStopped;
    }

    public PlayerList getPlayerList()
    {
        return this.playerList;
    }

    public void setPlayerList(PlayerList list)
    {
        this.playerList = list;
    }

    /**
     * Sets the game type for all worlds.
     */
    public void setGameType(GameType gameMode)
    {
        for (WorldServer worldserver1 : this.worlds)
        {
            worldserver1.getWorldInfo().setGameType(gameMode);
        }
    }

    public NetworkSystem getNetworkSystem()
    {
        return this.networkSystem;
    }

    public boolean serverIsInRunLoop()
    {
        return this.serverIsRunning;
    }

    public boolean getGuiEnabled()
    {
        return false;
    }

    public abstract String func_71206_a(GameType p_71206_1_, boolean p_71206_2_);

    public int getTickCounter()
    {
        return this.tickCounter;
    }

    public void enableProfiling()
    {
        this.startProfiling = true;
    }

    public Snooper getSnooper()
    {
        return this.snooper;
    }

    /**
     * Get the world, if available. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the overworld
     */
    public World getEntityWorld()
    {
        return this.worlds[0];
    }

    public boolean isBlockProtected(World worldIn, BlockPos pos, EntityPlayer playerIn)
    {
        return false;
    }

    /**
     * Get the forceGamemode field (whether joining players will be put in their old gamemode or the default one)
     */
    public boolean getForceGamemode()
    {
        return this.isGamemodeForced;
    }

    public Proxy func_110454_ao()
    {
        return this.serverProxy;
    }

    public static long func_130071_aq()
    {
        return System.currentTimeMillis();
    }

    public int getMaxPlayerIdleMinutes()
    {
        return this.maxPlayerIdleMinutes;
    }

    public void setPlayerIdleTimeout(int idleTimeout)
    {
        this.maxPlayerIdleMinutes = idleTimeout;
    }

    public MinecraftSessionService getMinecraftSessionService()
    {
        return this.sessionService;
    }

    public GameProfileRepository getGameProfileRepository()
    {
        return this.profileRepo;
    }

    public PlayerProfileCache getPlayerProfileCache()
    {
        return this.profileCache;
    }

    public ServerStatusResponse getServerStatusResponse()
    {
        return this.statusResponse;
    }

    public void refreshStatusNextTick()
    {
        this.nanoTimeSinceStatusRefresh = 0L;
    }

    @Nullable
    public Entity func_175576_a(UUID p_175576_1_)
    {
        for (WorldServer worldserver1 : this.worlds)
        {
            if (worldserver1 != null)
            {
                Entity entity = worldserver1.func_175733_a(p_175576_1_);

                if (entity != null)
                {
                    return entity;
                }
            }
        }

        return null;
    }

    public boolean func_174792_t_()
    {
        return this.worlds[0].getGameRules().func_82766_b("sendCommandFeedback");
    }

    /**
     * Get the Minecraft server instance
     */
    public MinecraftServer getServer()
    {
        return this;
    }

    public int getMaxWorldSize()
    {
        return 29999984;
    }

    public <V> ListenableFuture<V> func_175586_a(Callable<V> p_175586_1_)
    {
        Validate.notNull(p_175586_1_);

        if (!this.func_152345_ab() && !this.isServerStopped())
        {
            ListenableFutureTask<V> listenablefuturetask = ListenableFutureTask.<V>create(p_175586_1_);

            synchronized (this.field_175589_i)
            {
                this.field_175589_i.add(listenablefuturetask);
                return listenablefuturetask;
            }
        }
        else
        {
            try
            {
                return Futures.<V>immediateFuture(p_175586_1_.call());
            }
            catch (Exception exception)
            {
                return Futures.immediateFailedCheckedFuture(exception);
            }
        }
    }

    public ListenableFuture<Object> func_152344_a(Runnable p_152344_1_)
    {
        Validate.notNull(p_152344_1_);
        return this.<Object>func_175586_a(Executors.callable(p_152344_1_));
    }

    public boolean func_152345_ab()
    {
        return Thread.currentThread() == this.serverThread;
    }

    /**
     * The compression treshold. If the packet is larger than the specified amount of bytes, it will be compressed
     */
    public int getNetworkCompressionThreshold()
    {
        return 256;
    }

    public int getSpawnRadius(@Nullable WorldServer worldIn)
    {
        return worldIn != null ? worldIn.getGameRules().func_180263_c("spawnRadius") : 10;
    }

    public AdvancementManager getAdvancementManager()
    {
        return this.worlds[0].func_191952_z();
    }

    public FunctionManager getFunctionManager()
    {
        return this.worlds[0].func_193037_A();
    }

    public void reload()
    {
        if (this.func_152345_ab())
        {
            this.getPlayerList().saveAllPlayerData();
            this.worlds[0].func_184146_ak().func_186522_a();
            this.getAdvancementManager().func_192779_a();
            this.getFunctionManager().func_193059_f();
            this.getPlayerList().reloadResources();
        }
        else
        {
            this.func_152344_a(this::reload);
        }
    }
}
