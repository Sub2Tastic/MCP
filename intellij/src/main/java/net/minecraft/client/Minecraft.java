package net.minecraft.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMemoryErrorScreen;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.ScreenChatOptions;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import net.minecraft.client.settings.CreativeSettings;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.client.util.ISearchTree;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.client.util.SearchTree;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.profiler.Profiler;
import net.minecraft.profiler.Snooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.MinecraftError;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentKeybind;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

public class Minecraft implements IThreadListener, ISnooperInfo
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation field_110444_H = new ResourceLocation("textures/gui/title/mojang.png");
    public static final boolean IS_RUNNING_ON_MAC = Util.getOSType() == Util.EnumOS.OSX;
    public static byte[] memoryReserve = new byte[10485760];
    private static final List<DisplayMode> field_110445_I = Lists.newArrayList(new DisplayMode(2560, 1600), new DisplayMode(2880, 1800));
    private final File fileResourcepacks;
    private final PropertyMap field_152356_J;

    /** The player's GameProfile properties */
    private final PropertyMap profileProperties;
    private ServerData currentServerData;
    private TextureManager textureManager;
    private static Minecraft instance;
    private final DataFixer dataFixer;
    public PlayerControllerMP playerController;
    private boolean field_71431_Q;
    private final boolean field_175619_R = true;
    private boolean field_71434_R;
    private CrashReport crashReporter;
    public int field_71443_c;
    public int field_71440_d;

    /** True if the player is connected to a realms server */
    private boolean connectedToRealms;
    private final Timer timer = new Timer(20.0F);
    private final Snooper snooper = new Snooper("client", this, MinecraftServer.func_130071_aq());
    public WorldClient world;
    public RenderGlobal worldRenderer;
    private RenderManager renderManager;
    private RenderItem itemRenderer;
    private ItemRenderer firstPersonRenderer;
    public EntityPlayerSP player;
    @Nullable
    private Entity renderViewEntity;
    public Entity pointedEntity;
    public ParticleManager particles;
    private SearchTreeManager searchTreeManager = new SearchTreeManager();
    private final Session session;
    private boolean isGamePaused;
    private float renderPartialTicksPaused;
    public FontRenderer fontRenderer;
    public FontRenderer standardGalacticFontRenderer;
    @Nullable
    public GuiScreen currentScreen;
    public LoadingScreenRenderer field_71461_s;
    public EntityRenderer gameRenderer;
    public DebugRenderer debugRenderer;
    private int leftClickCounter;
    private final int field_71436_X;
    private final int field_71435_Y;
    @Nullable
    private IntegratedServer integratedServer;
    public GuiIngame ingameGUI;
    public boolean skipRenderWorld;
    public RayTraceResult objectMouseOver;
    public GameSettings gameSettings;
    public CreativeSettings creativeSettings;
    public MouseHelper mouseHelper;
    public final File gameDir;
    private final File field_110446_Y;
    private final String launchedVersion;
    private final String versionType;
    private final Proxy proxy;
    private ISaveFormat saveFormat;
    private static int debugFPS;
    private int rightClickDelayTimer;
    private String field_71475_ae;
    private int field_71477_af;
    public boolean field_71415_G;
    long field_71423_H = func_71386_F();
    private int field_71457_ai;

    /** The FrameTimer's instance */
    public final FrameTimer frameTimer = new FrameTimer();

    /** Time in nanoseconds of when the class is loaded */
    long startNanoTime = System.nanoTime();
    private final boolean jvm64bit;
    private final boolean isDemo;
    @Nullable
    private NetworkManager networkManager;
    private boolean integratedServerIsRunning;
    public final Profiler profiler = new Profiler();
    private long field_83002_am = -1L;
    private IReloadableResourceManager resourceManager;
    private final MetadataSerializer field_110452_an = new MetadataSerializer();
    private final List<IResourcePack> field_110449_ao = Lists.<IResourcePack>newArrayList();
    private final DefaultResourcePack field_110450_ap;
    private ResourcePackRepository resourcePackRepository;
    private LanguageManager languageManager;
    private BlockColors blockColors;
    private ItemColors itemColors;
    private Framebuffer framebuffer;
    private TextureMap field_147128_au;
    private SoundHandler soundHandler;
    private MusicTicker musicTicker;
    private ResourceLocation field_152354_ay;
    private final MinecraftSessionService sessionService;
    private SkinManager skinManager;
    private final Queue < FutureTask<? >> field_152351_aB = Queues. < FutureTask<? >> newArrayDeque();
    private final Thread thread = Thread.currentThread();
    private ModelManager modelManager;

    /**
     * The BlockRenderDispatcher instance that will be used based off gamesettings
     */
    private BlockRendererDispatcher blockRenderDispatcher;
    private final GuiToast toastGui;
    volatile boolean running = true;
    public String debug = "";
    public boolean renderChunksMany = true;
    private long debugUpdateTime = func_71386_F();
    private int fpsCounter;
    private boolean field_184129_aV;
    private final Tutorial tutorial;
    long field_71421_N = -1L;
    private String debugProfilerName = "root";

    public Minecraft(GameConfiguration gameConfig)
    {
        instance = this;
        this.gameDir = gameConfig.folderInfo.gameDir;
        this.field_110446_Y = gameConfig.folderInfo.assetsDir;
        this.fileResourcepacks = gameConfig.folderInfo.resourcePacksDir;
        this.launchedVersion = gameConfig.gameInfo.version;
        this.versionType = gameConfig.gameInfo.versionType;
        this.field_152356_J = gameConfig.userInfo.userProperties;
        this.profileProperties = gameConfig.userInfo.profileProperties;
        this.field_110450_ap = new DefaultResourcePack(gameConfig.folderInfo.getAssetsIndex());
        this.proxy = gameConfig.userInfo.proxy == null ? Proxy.NO_PROXY : gameConfig.userInfo.proxy;
        this.sessionService = (new YggdrasilAuthenticationService(this.proxy, UUID.randomUUID().toString())).createMinecraftSessionService();
        this.session = gameConfig.userInfo.session;
        LOGGER.info("Setting user: {}", (Object)this.session.getUsername());
        LOGGER.debug("(Session ID is {})", (Object)this.session.getSessionID());
        this.isDemo = gameConfig.gameInfo.isDemo;
        this.field_71443_c = gameConfig.displayInfo.field_178764_a > 0 ? gameConfig.displayInfo.field_178764_a : 1;
        this.field_71440_d = gameConfig.displayInfo.field_178762_b > 0 ? gameConfig.displayInfo.field_178762_b : 1;
        this.field_71436_X = gameConfig.displayInfo.field_178764_a;
        this.field_71435_Y = gameConfig.displayInfo.field_178762_b;
        this.field_71431_Q = gameConfig.displayInfo.field_178763_c;
        this.jvm64bit = isJvm64bit();
        this.integratedServer = null;

        if (gameConfig.serverInfo.serverName != null)
        {
            this.field_71475_ae = gameConfig.serverInfo.serverName;
            this.field_71477_af = gameConfig.serverInfo.serverPort;
        }

        ImageIO.setUseCache(false);
        Locale.setDefault(Locale.ROOT);
        Bootstrap.register();
        TextComponentKeybind.displaySupplierFunction = KeyBinding::getDisplayString;
        this.dataFixer = DataFixesManager.createFixer();
        this.toastGui = new GuiToast(this);
        this.tutorial = new Tutorial(this);
    }

    public void run()
    {
        this.running = true;

        try
        {
            this.func_71384_a();
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Initializing game");
            crashreport.makeCategory("Initialization");
            this.displayCrashReport(this.addGraphicsAndWorldToCrashReport(crashreport));
            return;
        }

        while (true)
        {
            try
            {
                while (this.running)
                {
                    if (!this.field_71434_R || this.crashReporter == null)
                    {
                        try
                        {
                            this.func_71411_J();
                        }
                        catch (OutOfMemoryError var10)
                        {
                            this.freeMemory();
                            this.displayGuiScreen(new GuiMemoryErrorScreen());
                            System.gc();
                        }
                    }
                    else
                    {
                        this.displayCrashReport(this.crashReporter);
                    }
                }
            }
            catch (MinecraftError var12)
            {
                break;
            }
            catch (ReportedException reportedexception)
            {
                this.addGraphicsAndWorldToCrashReport(reportedexception.getCrashReport());
                this.freeMemory();
                LOGGER.fatal("Reported exception thrown!", (Throwable)reportedexception);
                this.displayCrashReport(reportedexception.getCrashReport());
                break;
            }
            catch (Throwable throwable1)
            {
                CrashReport crashreport1 = this.addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", throwable1));
                this.freeMemory();
                LOGGER.fatal("Unreported exception thrown!", throwable1);
                this.displayCrashReport(crashreport1);
                break;
            }
            finally
            {
                this.shutdownMinecraftApplet();
            }

            return;
        }
    }

    private void func_71384_a() throws LWJGLException, IOException
    {
        this.gameSettings = new GameSettings(this, this.gameDir);
        this.creativeSettings = new CreativeSettings(this, this.gameDir);
        this.field_110449_ao.add(this.field_110450_ap);
        this.startTimerHackThread();

        if (this.gameSettings.overrideHeight > 0 && this.gameSettings.overrideWidth > 0)
        {
            this.field_71443_c = this.gameSettings.overrideWidth;
            this.field_71440_d = this.gameSettings.overrideHeight;
        }

        LOGGER.info("LWJGL Version: {}", (Object)Sys.getVersion());
        this.func_175594_ao();
        this.func_175605_an();
        this.func_175609_am();
        OpenGlHelper.func_77474_a();
        this.framebuffer = new Framebuffer(this.field_71443_c, this.field_71440_d, true);
        this.framebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
        this.func_175608_ak();
        this.resourcePackRepository = new ResourcePackRepository(this.fileResourcepacks, new File(this.gameDir, "server-resource-packs"), this.field_110450_ap, this.field_110452_an, this.gameSettings);
        this.resourceManager = new SimpleReloadableResourceManager(this.field_110452_an);
        this.languageManager = new LanguageManager(this.field_110452_an, this.gameSettings.language);
        this.resourceManager.func_110542_a(this.languageManager);
        this.func_110436_a();
        this.textureManager = new TextureManager(this.resourceManager);
        this.resourceManager.func_110542_a(this.textureManager);
        this.func_180510_a(this.textureManager);
        this.skinManager = new SkinManager(this.textureManager, new File(this.field_110446_Y, "skins"), this.sessionService);
        this.saveFormat = new AnvilSaveConverter(new File(this.gameDir, "saves"), this.dataFixer);
        this.soundHandler = new SoundHandler(this.resourceManager, this.gameSettings);
        this.resourceManager.func_110542_a(this.soundHandler);
        this.musicTicker = new MusicTicker(this);
        this.fontRenderer = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii.png"), this.textureManager, false);

        if (this.gameSettings.language != null)
        {
            this.fontRenderer.func_78264_a(this.func_152349_b());
            this.fontRenderer.setBidiFlag(this.languageManager.isCurrentLanguageBidirectional());
        }

        this.standardGalacticFontRenderer = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii_sga.png"), this.textureManager, false);
        this.resourceManager.func_110542_a(this.fontRenderer);
        this.resourceManager.func_110542_a(this.standardGalacticFontRenderer);
        this.resourceManager.func_110542_a(new GrassColorReloadListener());
        this.resourceManager.func_110542_a(new FoliageColorReloadListener());
        this.mouseHelper = new MouseHelper();
        this.func_71361_d("Pre startup");
        GlStateManager.func_179098_w();
        GlStateManager.func_179103_j(7425);
        GlStateManager.func_179151_a(1.0D);
        GlStateManager.func_179126_j();
        GlStateManager.func_179143_c(515);
        GlStateManager.func_179141_d();
        GlStateManager.func_179092_a(516, 0.1F);
        GlStateManager.func_187407_a(GlStateManager.CullFace.BACK);
        GlStateManager.func_179128_n(5889);
        GlStateManager.func_179096_D();
        GlStateManager.func_179128_n(5888);
        this.func_71361_d("Startup");
        this.field_147128_au = new TextureMap("textures");
        this.field_147128_au.func_147633_a(this.gameSettings.mipmapLevels);
        this.textureManager.func_110580_a(TextureMap.LOCATION_BLOCKS_TEXTURE, this.field_147128_au);
        this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        this.field_147128_au.setBlurMipmapDirect(false, this.gameSettings.mipmapLevels > 0);
        this.modelManager = new ModelManager(this.field_147128_au);
        this.resourceManager.func_110542_a(this.modelManager);
        this.blockColors = BlockColors.init();
        this.itemColors = ItemColors.init(this.blockColors);
        this.itemRenderer = new RenderItem(this.textureManager, this.modelManager, this.itemColors);
        this.renderManager = new RenderManager(this.textureManager, this.itemRenderer);
        this.firstPersonRenderer = new ItemRenderer(this);
        this.resourceManager.func_110542_a(this.itemRenderer);
        this.gameRenderer = new EntityRenderer(this, this.resourceManager);
        this.resourceManager.func_110542_a(this.gameRenderer);
        this.blockRenderDispatcher = new BlockRendererDispatcher(this.modelManager.getBlockModelShapes(), this.blockColors);
        this.resourceManager.func_110542_a(this.blockRenderDispatcher);
        this.worldRenderer = new RenderGlobal(this);
        this.resourceManager.func_110542_a(this.worldRenderer);
        this.populateSearchTreeManager();
        this.resourceManager.func_110542_a(this.searchTreeManager);
        GlStateManager.func_179083_b(0, 0, this.field_71443_c, this.field_71440_d);
        this.particles = new ParticleManager(this.world, this.textureManager);
        this.func_71361_d("Post startup");
        this.ingameGUI = new GuiIngame(this);

        if (this.field_71475_ae != null)
        {
            this.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), this, this.field_71475_ae, this.field_71477_af));
        }
        else
        {
            this.displayGuiScreen(new GuiMainMenu());
        }

        this.textureManager.deleteTexture(this.field_152354_ay);
        this.field_152354_ay = null;
        this.field_71461_s = new LoadingScreenRenderer(this);
        this.debugRenderer = new DebugRenderer(this);

        if (this.gameSettings.fullscreen && !this.field_71431_Q)
        {
            this.func_71352_k();
        }

        try
        {
            Display.setVSyncEnabled(this.gameSettings.vsync);
        }
        catch (OpenGLException var2)
        {
            this.gameSettings.vsync = false;
            this.gameSettings.saveOptions();
        }

        this.worldRenderer.makeEntityOutlineShader();
    }

    /**
     * Fills {@link #searchTreeManager} with the current item and recipe registry contents.
     */
    private void populateSearchTreeManager()
    {
        SearchTree<ItemStack> searchtree = new SearchTree<ItemStack>((p_193988_0_) ->
        {
            return (List)p_193988_0_.getTooltip((EntityPlayer)null, ITooltipFlag.TooltipFlags.NORMAL).stream().map(TextFormatting::getTextWithoutFormattingCodes).map(String::trim).filter((p_193984_0_) -> {
                return !p_193984_0_.isEmpty();
            }).collect(Collectors.toList());
        }, (p_193985_0_) ->
        {
            return Collections.singleton(Item.field_150901_e.getKey(p_193985_0_.getItem()));
        });
        NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>create();

        for (Item item : Item.field_150901_e)
        {
            item.fillItemGroup(CreativeTabs.SEARCH, nonnulllist);
        }

        nonnulllist.forEach(searchtree::func_194043_a);
        SearchTree<RecipeList> searchtree1 = new SearchTree<RecipeList>((p_193990_0_) ->
        {
            return (List)p_193990_0_.getRecipes().stream().flatMap((p_193993_0_) -> {
                return p_193993_0_.getRecipeOutput().getTooltip((EntityPlayer)null, ITooltipFlag.TooltipFlags.NORMAL).stream();
            }).map(TextFormatting::getTextWithoutFormattingCodes).map(String::trim).filter((p_193994_0_) -> {
                return !p_193994_0_.isEmpty();
            }).collect(Collectors.toList());
        }, (p_193991_0_) ->
        {
            return (List)p_193991_0_.getRecipes().stream().map((p_193992_0_) -> {
                return Item.field_150901_e.getKey(p_193992_0_.getRecipeOutput().getItem());
            }).collect(Collectors.toList());
        });
        RecipeBookClient.field_194087_f.forEach(searchtree1::func_194043_a);
        this.searchTreeManager.func_194009_a(SearchTreeManager.field_194011_a, searchtree);
        this.searchTreeManager.func_194009_a(SearchTreeManager.RECIPES, searchtree1);
    }

    private void func_175608_ak()
    {
        this.field_110452_an.func_110504_a(new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
        this.field_110452_an.func_110504_a(new FontMetadataSectionSerializer(), FontMetadataSection.class);
        this.field_110452_an.func_110504_a(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
        this.field_110452_an.func_110504_a(new PackMetadataSectionSerializer(), PackMetadataSection.class);
        this.field_110452_an.func_110504_a(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
    }

    private void func_175609_am() throws LWJGLException
    {
        Display.setResizable(true);
        Display.setTitle("Minecraft 1.12.2");

        try
        {
            Display.create((new PixelFormat()).withDepthBits(24));
        }
        catch (LWJGLException lwjglexception)
        {
            LOGGER.error("Couldn't set pixel format", (Throwable)lwjglexception);

            try
            {
                Thread.sleep(1000L);
            }
            catch (InterruptedException var3)
            {
                ;
            }

            if (this.field_71431_Q)
            {
                this.func_110441_Q();
            }

            Display.create();
        }
    }

    private void func_175605_an() throws LWJGLException
    {
        if (this.field_71431_Q)
        {
            Display.setFullscreen(true);
            DisplayMode displaymode = Display.getDisplayMode();
            this.field_71443_c = Math.max(1, displaymode.getWidth());
            this.field_71440_d = Math.max(1, displaymode.getHeight());
        }
        else
        {
            Display.setDisplayMode(new DisplayMode(this.field_71443_c, this.field_71440_d));
        }
    }

    private void func_175594_ao()
    {
        Util.EnumOS util$enumos = Util.getOSType();

        if (util$enumos != Util.EnumOS.OSX)
        {
            InputStream inputstream = null;
            InputStream inputstream1 = null;

            try
            {
                inputstream = this.field_110450_ap.func_152780_c(new ResourceLocation("icons/icon_16x16.png"));
                inputstream1 = this.field_110450_ap.func_152780_c(new ResourceLocation("icons/icon_32x32.png"));

                if (inputstream != null && inputstream1 != null)
                {
                    Display.setIcon(new ByteBuffer[] {this.func_152340_a(inputstream), this.func_152340_a(inputstream1)});
                }
            }
            catch (IOException ioexception)
            {
                LOGGER.error("Couldn't set icon", (Throwable)ioexception);
            }
            finally
            {
                IOUtils.closeQuietly(inputstream);
                IOUtils.closeQuietly(inputstream1);
            }
        }
    }

    private static boolean isJvm64bit()
    {
        String[] astring = new String[] {"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};

        for (String s : astring)
        {
            String s1 = System.getProperty(s);

            if (s1 != null && s1.contains("64"))
            {
                return true;
            }
        }

        return false;
    }

    public Framebuffer getFramebuffer()
    {
        return this.framebuffer;
    }

    /**
     * Gets the version that Minecraft was launched under (the name of a version JSON). Specified via the
     * <code>--version</code> flag.
     */
    public String getVersion()
    {
        return this.launchedVersion;
    }

    /**
     * Gets the type of version that Minecraft was launched under (as specified in the version JSON). Specified via the
     * <code>--versionType</code> flag.
     */
    public String getVersionType()
    {
        return this.versionType;
    }

    private void startTimerHackThread()
    {
        Thread thread = new Thread("Timer hack thread")
        {
            public void run()
            {
                while (Minecraft.this.running)
                {
                    try
                    {
                        Thread.sleep(2147483647L);
                    }
                    catch (InterruptedException var2)
                    {
                        ;
                    }
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    public void crashed(CrashReport crash)
    {
        this.field_71434_R = true;
        this.crashReporter = crash;
    }

    /**
     * Wrapper around displayCrashReportInternal
     */
    public void displayCrashReport(CrashReport p_71377_1_)
    {
        File file1 = new File(getInstance().gameDir, "crash-reports");
        File file2 = new File(file1, "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-client.txt");
        Bootstrap.printToSYSOUT(p_71377_1_.getCompleteReport());

        if (p_71377_1_.getFile() != null)
        {
            Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + p_71377_1_.getFile());
            System.exit(-1);
        }
        else if (p_71377_1_.saveToFile(file2))
        {
            Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + file2.getAbsolutePath());
            System.exit(-1);
        }
        else
        {
            Bootstrap.printToSYSOUT("#@?@# Game crashed! Crash report could not be saved. #@?@#");
            System.exit(-2);
        }
    }

    public boolean func_152349_b()
    {
        return this.languageManager.func_135042_a() || this.gameSettings.field_151455_aw;
    }

    public void func_110436_a()
    {
        List<IResourcePack> list = Lists.newArrayList(this.field_110449_ao);

        if (this.integratedServer != null)
        {
            this.integratedServer.reload();
        }

        for (ResourcePackRepository.Entry resourcepackrepository$entry : this.resourcePackRepository.func_110613_c())
        {
            list.add(resourcepackrepository$entry.func_110514_c());
        }

        if (this.resourcePackRepository.func_148530_e() != null)
        {
            list.add(this.resourcePackRepository.func_148530_e());
        }

        try
        {
            this.resourceManager.func_110541_a(list);
        }
        catch (RuntimeException runtimeexception)
        {
            LOGGER.info("Caught error stitching, removing all assigned resourcepacks", (Throwable)runtimeexception);
            list.clear();
            list.addAll(this.field_110449_ao);
            this.resourcePackRepository.func_148527_a(Collections.emptyList());
            this.resourceManager.func_110541_a(list);
            this.gameSettings.resourcePacks.clear();
            this.gameSettings.incompatibleResourcePacks.clear();
            this.gameSettings.saveOptions();
        }

        this.languageManager.parseLanguageMetadata(list);

        if (this.worldRenderer != null)
        {
            this.worldRenderer.loadRenderers();
        }
    }

    private ByteBuffer func_152340_a(InputStream p_152340_1_) throws IOException
    {
        BufferedImage bufferedimage = ImageIO.read(p_152340_1_);
        int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), (int[])null, 0, bufferedimage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);

        for (int i : aint)
        {
            bytebuffer.putInt(i << 8 | i >> 24 & 255);
        }

        bytebuffer.flip();
        return bytebuffer;
    }

    private void func_110441_Q() throws LWJGLException
    {
        Set<DisplayMode> set = Sets.<DisplayMode>newHashSet();
        Collections.addAll(set, Display.getAvailableDisplayModes());
        DisplayMode displaymode = Display.getDesktopDisplayMode();

        if (!set.contains(displaymode) && Util.getOSType() == Util.EnumOS.OSX)
        {
            label52:

            for (DisplayMode displaymode1 : field_110445_I)
            {
                boolean flag = true;

                for (DisplayMode displaymode2 : set)
                {
                    if (displaymode2.getBitsPerPixel() == 32 && displaymode2.getWidth() == displaymode1.getWidth() && displaymode2.getHeight() == displaymode1.getHeight())
                    {
                        flag = false;
                        break;
                    }
                }

                if (!flag)
                {
                    Iterator iterator = set.iterator();
                    DisplayMode displaymode3;

                    while (true)
                    {
                        if (!iterator.hasNext())
                        {
                            continue label52;
                        }

                        displaymode3 = (DisplayMode)iterator.next();

                        if (displaymode3.getBitsPerPixel() == 32 && displaymode3.getWidth() == displaymode1.getWidth() / 2 && displaymode3.getHeight() == displaymode1.getHeight() / 2)
                        {
                            break;
                        }
                    }

                    displaymode = displaymode3;
                }
            }
        }

        Display.setDisplayMode(displaymode);
        this.field_71443_c = displaymode.getWidth();
        this.field_71440_d = displaymode.getHeight();
    }

    private void func_180510_a(TextureManager p_180510_1_) throws LWJGLException
    {
        ScaledResolution scaledresolution = new ScaledResolution(this);
        int i = scaledresolution.func_78325_e();
        Framebuffer framebuffer = new Framebuffer(scaledresolution.func_78326_a() * i, scaledresolution.func_78328_b() * i, true);
        framebuffer.bindFramebuffer(false);
        GlStateManager.func_179128_n(5889);
        GlStateManager.func_179096_D();
        GlStateManager.func_179130_a(0.0D, (double)scaledresolution.func_78326_a(), (double)scaledresolution.func_78328_b(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.func_179128_n(5888);
        GlStateManager.func_179096_D();
        GlStateManager.func_179109_b(0.0F, 0.0F, -2000.0F);
        GlStateManager.func_179140_f();
        GlStateManager.func_179106_n();
        GlStateManager.func_179097_i();
        GlStateManager.func_179098_w();
        InputStream inputstream = null;

        try
        {
            inputstream = this.field_110450_ap.func_110590_a(field_110444_H);
            this.field_152354_ay = p_180510_1_.getDynamicTextureLocation("logo", new DynamicTexture(ImageIO.read(inputstream)));
            p_180510_1_.bindTexture(this.field_152354_ay);
        }
        catch (IOException ioexception)
        {
            LOGGER.error("Unable to load logo: {}", field_110444_H, ioexception);
        }
        finally
        {
            IOUtils.closeQuietly(inputstream);
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.func_181662_b(0.0D, (double)this.field_71440_d, 0.0D).func_187315_a(0.0D, 0.0D).func_181669_b(255, 255, 255, 255).endVertex();
        bufferbuilder.func_181662_b((double)this.field_71443_c, (double)this.field_71440_d, 0.0D).func_187315_a(0.0D, 0.0D).func_181669_b(255, 255, 255, 255).endVertex();
        bufferbuilder.func_181662_b((double)this.field_71443_c, 0.0D, 0.0D).func_187315_a(0.0D, 0.0D).func_181669_b(255, 255, 255, 255).endVertex();
        bufferbuilder.func_181662_b(0.0D, 0.0D, 0.0D).func_187315_a(0.0D, 0.0D).func_181669_b(255, 255, 255, 255).endVertex();
        tessellator.draw();
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        int j = 256;
        int k = 256;
        this.func_181536_a((scaledresolution.func_78326_a() - 256) / 2, (scaledresolution.func_78328_b() - 256) / 2, 0, 0, 256, 256, 255, 255, 255, 255);
        GlStateManager.func_179140_f();
        GlStateManager.func_179106_n();
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(scaledresolution.func_78326_a() * i, scaledresolution.func_78328_b() * i);
        GlStateManager.func_179141_d();
        GlStateManager.func_179092_a(516, 0.1F);
        this.func_175601_h();
    }

    public void func_181536_a(int p_181536_1_, int p_181536_2_, int p_181536_3_, int p_181536_4_, int p_181536_5_, int p_181536_6_, int p_181536_7_, int p_181536_8_, int p_181536_9_, int p_181536_10_)
    {
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        bufferbuilder.func_181662_b((double)p_181536_1_, (double)(p_181536_2_ + p_181536_6_), 0.0D).func_187315_a((double)((float)p_181536_3_ * 0.00390625F), (double)((float)(p_181536_4_ + p_181536_6_) * 0.00390625F)).func_181669_b(p_181536_7_, p_181536_8_, p_181536_9_, p_181536_10_).endVertex();
        bufferbuilder.func_181662_b((double)(p_181536_1_ + p_181536_5_), (double)(p_181536_2_ + p_181536_6_), 0.0D).func_187315_a((double)((float)(p_181536_3_ + p_181536_5_) * 0.00390625F), (double)((float)(p_181536_4_ + p_181536_6_) * 0.00390625F)).func_181669_b(p_181536_7_, p_181536_8_, p_181536_9_, p_181536_10_).endVertex();
        bufferbuilder.func_181662_b((double)(p_181536_1_ + p_181536_5_), (double)p_181536_2_, 0.0D).func_187315_a((double)((float)(p_181536_3_ + p_181536_5_) * 0.00390625F), (double)((float)p_181536_4_ * 0.00390625F)).func_181669_b(p_181536_7_, p_181536_8_, p_181536_9_, p_181536_10_).endVertex();
        bufferbuilder.func_181662_b((double)p_181536_1_, (double)p_181536_2_, 0.0D).func_187315_a((double)((float)p_181536_3_ * 0.00390625F), (double)((float)p_181536_4_ * 0.00390625F)).func_181669_b(p_181536_7_, p_181536_8_, p_181536_9_, p_181536_10_).endVertex();
        Tessellator.getInstance().draw();
    }

    /**
     * Returns the save loader that is currently being used
     */
    public ISaveFormat getSaveLoader()
    {
        return this.saveFormat;
    }

    /**
     * Sets the argument GuiScreen as the main (topmost visible) screen.
     *  
     * <p><strong>WARNING</strong>: This method is not thread-safe. Opening GUIs from a thread other than the main
     * thread may cause many different issues, including the GUI being rendered before it has initialized (leading to
     * unusual crashes). If on a thread other than the main thread, use {@link #execute}:
     *  
     * <pre>
     * minecraft.execute(() -> minecraft.displayGuiScreen(gui));
     * </pre>
     */
    public void displayGuiScreen(@Nullable GuiScreen guiScreenIn)
    {
        if (this.currentScreen != null)
        {
            this.currentScreen.func_146281_b();
        }

        if (guiScreenIn == null && this.world == null)
        {
            guiScreenIn = new GuiMainMenu();
        }
        else if (guiScreenIn == null && this.player.getHealth() <= 0.0F)
        {
            guiScreenIn = new GuiGameOver((ITextComponent)null);
        }

        if (guiScreenIn instanceof GuiMainMenu || guiScreenIn instanceof GuiMultiplayer)
        {
            this.gameSettings.showDebugInfo = false;
            this.ingameGUI.getChatGUI().clearChatMessages(true);
        }

        this.currentScreen = guiScreenIn;

        if (guiScreenIn != null)
        {
            this.func_71364_i();
            KeyBinding.unPressAllKeys();

            while (Mouse.next())
            {
                ;
            }

            while (Keyboard.next())
            {
                ;
            }

            ScaledResolution scaledresolution = new ScaledResolution(this);
            int i = scaledresolution.func_78326_a();
            int j = scaledresolution.func_78328_b();
            guiScreenIn.func_146280_a(this, i, j);
            this.skipRenderWorld = false;
        }
        else
        {
            this.soundHandler.resume();
            this.func_71381_h();
        }
    }

    private void func_71361_d(String p_71361_1_)
    {
        int i = GlStateManager.func_187434_L();

        if (i != 0)
        {
            String s = GLU.gluErrorString(i);
            LOGGER.error("########## GL ERROR ##########");
            LOGGER.error("@ {}", (Object)p_71361_1_);
            LOGGER.error("{}: {}", Integer.valueOf(i), s);
        }
    }

    /**
     * Shuts down the minecraft applet by stopping the resource downloads, and clearing up GL stuff; called when the
     * application (or web page) is exited.
     */
    public void shutdownMinecraftApplet()
    {
        try
        {
            LOGGER.info("Stopping!");

            try
            {
                this.loadWorld((WorldClient)null);
            }
            catch (Throwable var5)
            {
                ;
            }

            this.soundHandler.unloadSounds();
        }
        finally
        {
            Display.destroy();

            if (!this.field_71434_R)
            {
                System.exit(0);
            }
        }

        System.gc();
    }

    private void func_71411_J() throws IOException
    {
        long i = System.nanoTime();
        this.profiler.startSection("root");

        if (Display.isCreated() && Display.isCloseRequested())
        {
            this.shutdown();
        }

        this.timer.updateTimer();
        this.profiler.startSection("scheduledExecutables");

        synchronized (this.field_152351_aB)
        {
            while (!this.field_152351_aB.isEmpty())
            {
                Util.func_181617_a(this.field_152351_aB.poll(), LOGGER);
            }
        }

        this.profiler.endSection();
        long l = System.nanoTime();
        this.profiler.startSection("tick");

        for (int j = 0; j < Math.min(10, this.timer.elapsedTicks); ++j)
        {
            this.runTick();
        }

        this.profiler.func_76318_c("preRenderErrors");
        long i1 = System.nanoTime() - l;
        this.func_71361_d("Pre render");
        this.profiler.func_76318_c("sound");
        this.soundHandler.func_147691_a(this.player, this.timer.renderPartialTicks);
        this.profiler.endSection();
        this.profiler.startSection("render");
        GlStateManager.func_179094_E();
        GlStateManager.func_179086_m(16640);
        this.framebuffer.bindFramebuffer(true);
        this.profiler.startSection("display");
        GlStateManager.func_179098_w();
        this.profiler.endSection();

        if (!this.skipRenderWorld)
        {
            this.profiler.func_76318_c("gameRenderer");
            this.gameRenderer.func_181560_a(this.isGamePaused ? this.renderPartialTicksPaused : this.timer.renderPartialTicks, i);
            this.profiler.func_76318_c("toasts");
            this.toastGui.func_191783_a(new ScaledResolution(this));
            this.profiler.endSection();
        }

        this.profiler.endSection();

        if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart && !this.gameSettings.hideGUI)
        {
            if (!this.profiler.field_76327_a)
            {
                this.profiler.func_76317_a();
            }

            this.profiler.field_76327_a = true;
            this.func_71366_a(i1);
        }
        else
        {
            this.profiler.field_76327_a = false;
            this.field_71421_N = System.nanoTime();
        }

        this.framebuffer.unbindFramebuffer();
        GlStateManager.func_179121_F();
        GlStateManager.func_179094_E();
        this.framebuffer.framebufferRender(this.field_71443_c, this.field_71440_d);
        GlStateManager.func_179121_F();
        GlStateManager.func_179094_E();
        this.gameRenderer.func_152430_c(this.timer.renderPartialTicks);
        GlStateManager.func_179121_F();
        this.profiler.startSection("root");
        this.func_175601_h();
        Thread.yield();
        this.func_71361_d("Post render");
        ++this.fpsCounter;
        boolean flag = this.isSingleplayer() && this.currentScreen != null && this.currentScreen.func_73868_f() && !this.integratedServer.getPublic();

        if (this.isGamePaused != flag)
        {
            if (this.isGamePaused)
            {
                this.renderPartialTicksPaused = this.timer.renderPartialTicks;
            }
            else
            {
                this.timer.renderPartialTicks = this.renderPartialTicksPaused;
            }

            this.isGamePaused = flag;
        }

        long k = System.nanoTime();
        this.frameTimer.addFrame(k - this.startNanoTime);
        this.startNanoTime = k;

        while (func_71386_F() >= this.debugUpdateTime + 1000L)
        {
            debugFPS = this.fpsCounter;
            this.debug = String.format("%d fps (%d chunk update%s) T: %s%s%s%s%s", debugFPS, RenderChunk.field_178592_a, RenderChunk.field_178592_a == 1 ? "" : "s", (float)this.gameSettings.framerateLimit == GameSettings.Options.FRAMERATE_LIMIT.func_148267_f() ? "inf" : this.gameSettings.framerateLimit, this.gameSettings.vsync ? " vsync" : "", this.gameSettings.fancyGraphics ? "" : " fast", this.gameSettings.cloudOption == 0 ? "" : (this.gameSettings.cloudOption == 1 ? " fast-clouds" : " fancy-clouds"), OpenGlHelper.func_176075_f() ? " vbo" : "");
            RenderChunk.field_178592_a = 0;
            this.debugUpdateTime += 1000L;
            this.fpsCounter = 0;
            this.snooper.addMemoryStatsToSnooper();

            if (!this.snooper.isSnooperRunning())
            {
                this.snooper.start();
            }
        }

        if (this.func_147107_h())
        {
            this.profiler.startSection("fpslimit_wait");
            Display.sync(this.func_90020_K());
            this.profiler.endSection();
        }

        this.profiler.endSection();
    }

    public void func_175601_h()
    {
        this.profiler.startSection("display_update");
        Display.update();
        this.profiler.endSection();
        this.func_175604_i();
    }

    protected void func_175604_i()
    {
        if (!this.field_71431_Q && Display.wasResized())
        {
            int i = this.field_71443_c;
            int j = this.field_71440_d;
            this.field_71443_c = Display.getWidth();
            this.field_71440_d = Display.getHeight();

            if (this.field_71443_c != i || this.field_71440_d != j)
            {
                if (this.field_71443_c <= 0)
                {
                    this.field_71443_c = 1;
                }

                if (this.field_71440_d <= 0)
                {
                    this.field_71440_d = 1;
                }

                this.func_71370_a(this.field_71443_c, this.field_71440_d);
            }
        }
    }

    public int func_90020_K()
    {
        return this.world == null && this.currentScreen != null ? 30 : this.gameSettings.framerateLimit;
    }

    public boolean func_147107_h()
    {
        return (float)this.func_90020_K() < GameSettings.Options.FRAMERATE_LIMIT.func_148267_f();
    }

    /**
     * Attempts to free as much memory as possible, including leaving the world and running the garbage collector.
     */
    public void freeMemory()
    {
        try
        {
            memoryReserve = new byte[0];
            this.worldRenderer.deleteAllDisplayLists();
        }
        catch (Throwable var3)
        {
            ;
        }

        try
        {
            System.gc();
            this.loadWorld((WorldClient)null);
        }
        catch (Throwable var2)
        {
            ;
        }

        System.gc();
    }

    /**
     * Update debugProfilerName in response to number keys in debug screen
     */
    private void updateDebugProfilerName(int keyCount)
    {
        List<Profiler.Result> list = this.profiler.func_76321_b(this.debugProfilerName);

        if (!list.isEmpty())
        {
            Profiler.Result profiler$result = list.remove(0);

            if (keyCount == 0)
            {
                if (!profiler$result.field_76331_c.isEmpty())
                {
                    int i = this.debugProfilerName.lastIndexOf(46);

                    if (i >= 0)
                    {
                        this.debugProfilerName = this.debugProfilerName.substring(0, i);
                    }
                }
            }
            else
            {
                --keyCount;

                if (keyCount < list.size() && !"unspecified".equals((list.get(keyCount)).field_76331_c))
                {
                    if (!this.debugProfilerName.isEmpty())
                    {
                        this.debugProfilerName = this.debugProfilerName + ".";
                    }

                    this.debugProfilerName = this.debugProfilerName + (list.get(keyCount)).field_76331_c;
                }
            }
        }
    }

    private void func_71366_a(long p_71366_1_)
    {
        if (this.profiler.field_76327_a)
        {
            List<Profiler.Result> list = this.profiler.func_76321_b(this.debugProfilerName);
            Profiler.Result profiler$result = list.remove(0);
            GlStateManager.func_179086_m(256);
            GlStateManager.func_179128_n(5889);
            GlStateManager.func_179142_g();
            GlStateManager.func_179096_D();
            GlStateManager.func_179130_a(0.0D, (double)this.field_71443_c, (double)this.field_71440_d, 0.0D, 1000.0D, 3000.0D);
            GlStateManager.func_179128_n(5888);
            GlStateManager.func_179096_D();
            GlStateManager.func_179109_b(0.0F, 0.0F, -2000.0F);
            GlStateManager.func_187441_d(1.0F);
            GlStateManager.func_179090_x();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            int i = 160;
            int j = this.field_71443_c - 160 - 10;
            int k = this.field_71440_d - 320;
            GlStateManager.func_179147_l();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.func_181662_b((double)((float)j - 176.0F), (double)((float)k - 96.0F - 16.0F), 0.0D).func_181669_b(200, 0, 0, 0).endVertex();
            bufferbuilder.func_181662_b((double)((float)j - 176.0F), (double)(k + 320), 0.0D).func_181669_b(200, 0, 0, 0).endVertex();
            bufferbuilder.func_181662_b((double)((float)j + 176.0F), (double)(k + 320), 0.0D).func_181669_b(200, 0, 0, 0).endVertex();
            bufferbuilder.func_181662_b((double)((float)j + 176.0F), (double)((float)k - 96.0F - 16.0F), 0.0D).func_181669_b(200, 0, 0, 0).endVertex();
            tessellator.draw();
            GlStateManager.func_179084_k();
            double d0 = 0.0D;

            for (int l = 0; l < list.size(); ++l)
            {
                Profiler.Result profiler$result1 = list.get(l);
                int i1 = MathHelper.floor(profiler$result1.field_76332_a / 4.0D) + 1;
                bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
                int j1 = profiler$result1.func_76329_a();
                int k1 = j1 >> 16 & 255;
                int l1 = j1 >> 8 & 255;
                int i2 = j1 & 255;
                bufferbuilder.func_181662_b((double)j, (double)k, 0.0D).func_181669_b(k1, l1, i2, 255).endVertex();

                for (int j2 = i1; j2 >= 0; --j2)
                {
                    float f = (float)((d0 + profiler$result1.field_76332_a * (double)j2 / (double)i1) * (Math.PI * 2D) / 100.0D);
                    float f1 = MathHelper.sin(f) * 160.0F;
                    float f2 = MathHelper.cos(f) * 160.0F * 0.5F;
                    bufferbuilder.func_181662_b((double)((float)j + f1), (double)((float)k - f2), 0.0D).func_181669_b(k1, l1, i2, 255).endVertex();
                }

                tessellator.draw();
                bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);

                for (int i3 = i1; i3 >= 0; --i3)
                {
                    float f3 = (float)((d0 + profiler$result1.field_76332_a * (double)i3 / (double)i1) * (Math.PI * 2D) / 100.0D);
                    float f4 = MathHelper.sin(f3) * 160.0F;
                    float f5 = MathHelper.cos(f3) * 160.0F * 0.5F;
                    bufferbuilder.func_181662_b((double)((float)j + f4), (double)((float)k - f5), 0.0D).func_181669_b(k1 >> 1, l1 >> 1, i2 >> 1, 255).endVertex();
                    bufferbuilder.func_181662_b((double)((float)j + f4), (double)((float)k - f5 + 10.0F), 0.0D).func_181669_b(k1 >> 1, l1 >> 1, i2 >> 1, 255).endVertex();
                }

                tessellator.draw();
                d0 += profiler$result1.field_76332_a;
            }

            DecimalFormat decimalformat = new DecimalFormat("##0.00");
            GlStateManager.func_179098_w();
            String s = "";

            if (!"unspecified".equals(profiler$result.field_76331_c))
            {
                s = s + "[0] ";
            }

            if (profiler$result.field_76331_c.isEmpty())
            {
                s = s + "ROOT ";
            }
            else
            {
                s = s + profiler$result.field_76331_c + ' ';
            }

            int l2 = 16777215;
            this.fontRenderer.drawStringWithShadow(s, (float)(j - 160), (float)(k - 80 - 16), 16777215);
            s = decimalformat.format(profiler$result.field_76330_b) + "%";
            this.fontRenderer.drawStringWithShadow(s, (float)(j + 160 - this.fontRenderer.getStringWidth(s)), (float)(k - 80 - 16), 16777215);

            for (int k2 = 0; k2 < list.size(); ++k2)
            {
                Profiler.Result profiler$result2 = list.get(k2);
                StringBuilder stringbuilder = new StringBuilder();

                if ("unspecified".equals(profiler$result2.field_76331_c))
                {
                    stringbuilder.append("[?] ");
                }
                else
                {
                    stringbuilder.append("[").append(k2 + 1).append("] ");
                }

                String s1 = stringbuilder.append(profiler$result2.field_76331_c).toString();
                this.fontRenderer.drawStringWithShadow(s1, (float)(j - 160), (float)(k + 80 + k2 * 8 + 20), profiler$result2.func_76329_a());
                s1 = decimalformat.format(profiler$result2.field_76332_a) + "%";
                this.fontRenderer.drawStringWithShadow(s1, (float)(j + 160 - 50 - this.fontRenderer.getStringWidth(s1)), (float)(k + 80 + k2 * 8 + 20), profiler$result2.func_76329_a());
                s1 = decimalformat.format(profiler$result2.field_76330_b) + "%";
                this.fontRenderer.drawStringWithShadow(s1, (float)(j + 160 - this.fontRenderer.getStringWidth(s1)), (float)(k + 80 + k2 * 8 + 20), profiler$result2.func_76329_a());
            }
        }
    }

    /**
     * Called when the window is closing. Sets 'running' to false which allows the game loop to exit cleanly.
     */
    public void shutdown()
    {
        this.running = false;
    }

    public void func_71381_h()
    {
        if (Display.isActive())
        {
            if (!this.field_71415_G)
            {
                if (!IS_RUNNING_ON_MAC)
                {
                    KeyBinding.updateKeyBindState();
                }

                this.field_71415_G = true;
                this.mouseHelper.func_74372_a();
                this.displayGuiScreen((GuiScreen)null);
                this.leftClickCounter = 10000;
            }
        }
    }

    public void func_71364_i()
    {
        if (this.field_71415_G)
        {
            this.field_71415_G = false;
            this.mouseHelper.func_74373_b();
        }
    }

    /**
     * Displays the ingame menu
     */
    public void displayInGameMenu()
    {
        if (this.currentScreen == null)
        {
            this.displayGuiScreen(new GuiIngameMenu());

            if (this.isSingleplayer() && !this.integratedServer.getPublic())
            {
                this.soundHandler.pause();
            }
        }
    }

    private void sendClickBlockToController(boolean leftClick)
    {
        if (!leftClick)
        {
            this.leftClickCounter = 0;
        }

        if (this.leftClickCounter <= 0 && !this.player.isHandActive())
        {
            if (leftClick && this.objectMouseOver != null && this.objectMouseOver.field_72313_a == RayTraceResult.Type.BLOCK)
            {
                BlockPos blockpos = this.objectMouseOver.func_178782_a();

                if (this.world.getBlockState(blockpos).getMaterial() != Material.AIR && this.playerController.onPlayerDamageBlock(blockpos, this.objectMouseOver.field_178784_b))
                {
                    this.particles.addBlockHitEffects(blockpos, this.objectMouseOver.field_178784_b);
                    this.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
            else
            {
                this.playerController.resetBlockRemoving();
            }
        }
    }

    private void clickMouse()
    {
        if (this.leftClickCounter <= 0)
        {
            if (this.objectMouseOver == null)
            {
                LOGGER.error("Null returned as 'hitResult', this shouldn't happen!");

                if (this.playerController.isNotCreative())
                {
                    this.leftClickCounter = 10;
                }
            }
            else if (!this.player.isRowingBoat())
            {
                switch (this.objectMouseOver.field_72313_a)
                {
                    case ENTITY:
                        this.playerController.attackEntity(this.player, this.objectMouseOver.field_72308_g);
                        break;

                    case BLOCK:
                        BlockPos blockpos = this.objectMouseOver.func_178782_a();

                        if (this.world.getBlockState(blockpos).getMaterial() != Material.AIR)
                        {
                            this.playerController.clickBlock(blockpos, this.objectMouseOver.field_178784_b);
                            break;
                        }

                    case MISS:
                        if (this.playerController.isNotCreative())
                        {
                            this.leftClickCounter = 10;
                        }

                        this.player.resetCooldown();
                }

                this.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }

    @SuppressWarnings("incomplete-switch")

    /**
     * Called when user clicked he's mouse right button (place)
     */
    private void rightClickMouse()
    {
        if (!this.playerController.getIsHittingBlock())
        {
            this.rightClickDelayTimer = 4;

            if (!this.player.isRowingBoat())
            {
                if (this.objectMouseOver == null)
                {
                    LOGGER.warn("Null returned as 'hitResult', this shouldn't happen!");
                }

                for (EnumHand enumhand : EnumHand.values())
                {
                    ItemStack itemstack = this.player.getHeldItem(enumhand);

                    if (this.objectMouseOver != null)
                    {
                        switch (this.objectMouseOver.field_72313_a)
                        {
                            case ENTITY:
                                if (this.playerController.interactWithEntity(this.player, this.objectMouseOver.field_72308_g, this.objectMouseOver, enumhand) == EnumActionResult.SUCCESS)
                                {
                                    return;
                                }

                                if (this.playerController.interactWithEntity(this.player, this.objectMouseOver.field_72308_g, enumhand) == EnumActionResult.SUCCESS)
                                {
                                    return;
                                }

                                break;

                            case BLOCK:
                                BlockPos blockpos = this.objectMouseOver.func_178782_a();

                                if (this.world.getBlockState(blockpos).getMaterial() != Material.AIR)
                                {
                                    int i = itemstack.getCount();
                                    EnumActionResult enumactionresult = this.playerController.func_187099_a(this.player, this.world, blockpos, this.objectMouseOver.field_178784_b, this.objectMouseOver.hitResult, enumhand);

                                    if (enumactionresult == EnumActionResult.SUCCESS)
                                    {
                                        this.player.swingArm(enumhand);

                                        if (!itemstack.isEmpty() && (itemstack.getCount() != i || this.playerController.isInCreativeMode()))
                                        {
                                            this.gameRenderer.itemRenderer.resetEquippedProgress(enumhand);
                                        }

                                        return;
                                    }
                                }
                        }
                    }

                    if (!itemstack.isEmpty() && this.playerController.processRightClick(this.player, this.world, enumhand) == EnumActionResult.SUCCESS)
                    {
                        this.gameRenderer.itemRenderer.resetEquippedProgress(enumhand);
                        return;
                    }
                }
            }
        }
    }

    public void func_71352_k()
    {
        try
        {
            this.field_71431_Q = !this.field_71431_Q;
            this.gameSettings.fullscreen = this.field_71431_Q;

            if (this.field_71431_Q)
            {
                this.func_110441_Q();
                this.field_71443_c = Display.getDisplayMode().getWidth();
                this.field_71440_d = Display.getDisplayMode().getHeight();

                if (this.field_71443_c <= 0)
                {
                    this.field_71443_c = 1;
                }

                if (this.field_71440_d <= 0)
                {
                    this.field_71440_d = 1;
                }
            }
            else
            {
                Display.setDisplayMode(new DisplayMode(this.field_71436_X, this.field_71435_Y));
                this.field_71443_c = this.field_71436_X;
                this.field_71440_d = this.field_71435_Y;

                if (this.field_71443_c <= 0)
                {
                    this.field_71443_c = 1;
                }

                if (this.field_71440_d <= 0)
                {
                    this.field_71440_d = 1;
                }
            }

            if (this.currentScreen != null)
            {
                this.func_71370_a(this.field_71443_c, this.field_71440_d);
            }
            else
            {
                this.func_147119_ah();
            }

            Display.setFullscreen(this.field_71431_Q);
            Display.setVSyncEnabled(this.gameSettings.vsync);
            this.func_175601_h();
        }
        catch (Exception exception)
        {
            LOGGER.error("Couldn't toggle fullscreen", (Throwable)exception);
        }
    }

    private void func_71370_a(int p_71370_1_, int p_71370_2_)
    {
        this.field_71443_c = Math.max(1, p_71370_1_);
        this.field_71440_d = Math.max(1, p_71370_2_);

        if (this.currentScreen != null)
        {
            ScaledResolution scaledresolution = new ScaledResolution(this);
            this.currentScreen.func_175273_b(this, scaledresolution.func_78326_a(), scaledresolution.func_78328_b());
        }

        this.field_71461_s = new LoadingScreenRenderer(this);
        this.func_147119_ah();
    }

    private void func_147119_ah()
    {
        this.framebuffer.func_147613_a(this.field_71443_c, this.field_71440_d);

        if (this.gameRenderer != null)
        {
            this.gameRenderer.updateShaderGroupSize(this.field_71443_c, this.field_71440_d);
        }
    }

    /**
     * Return the musicTicker's instance
     */
    public MusicTicker getMusicTicker()
    {
        return this.musicTicker;
    }

    /**
     * Runs the current tick.
     */
    public void runTick() throws IOException
    {
        if (this.rightClickDelayTimer > 0)
        {
            --this.rightClickDelayTimer;
        }

        this.profiler.startSection("gui");

        if (!this.isGamePaused)
        {
            this.ingameGUI.tick();
        }

        this.profiler.endSection();
        this.gameRenderer.getMouseOver(1.0F);
        this.tutorial.onMouseHover(this.world, this.objectMouseOver);
        this.profiler.startSection("gameMode");

        if (!this.isGamePaused && this.world != null)
        {
            this.playerController.tick();
        }

        this.profiler.func_76318_c("textures");

        if (this.world != null)
        {
            this.textureManager.tick();
        }

        if (this.currentScreen == null && this.player != null)
        {
            if (this.player.getHealth() <= 0.0F && !(this.currentScreen instanceof GuiGameOver))
            {
                this.displayGuiScreen((GuiScreen)null);
            }
            else if (this.player.isSleeping() && this.world != null)
            {
                this.displayGuiScreen(new GuiSleepMP());
            }
        }
        else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.player.isSleeping())
        {
            this.displayGuiScreen((GuiScreen)null);
        }

        if (this.currentScreen != null)
        {
            this.leftClickCounter = 10000;
        }

        if (this.currentScreen != null)
        {
            try
            {
                this.currentScreen.func_146269_k();
            }
            catch (Throwable throwable1)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Updating screen events");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Affected screen");
                crashreportcategory.addDetail("Screen name", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return Minecraft.this.currentScreen.getClass().getCanonicalName();
                    }
                });
                throw new ReportedException(crashreport);
            }

            if (this.currentScreen != null)
            {
                try
                {
                    this.currentScreen.func_73876_c();
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Ticking screen");
                    CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Affected screen");
                    crashreportcategory1.addDetail("Screen name", new ICrashReportDetail<String>()
                    {
                        public String call() throws Exception
                        {
                            return Minecraft.this.currentScreen.getClass().getCanonicalName();
                        }
                    });
                    throw new ReportedException(crashreport1);
                }
            }
        }

        if (this.currentScreen == null || this.currentScreen.field_146291_p)
        {
            this.profiler.func_76318_c("mouse");
            this.func_184124_aB();

            if (this.leftClickCounter > 0)
            {
                --this.leftClickCounter;
            }

            this.profiler.func_76318_c("keyboard");
            this.func_184118_az();
        }

        if (this.world != null)
        {
            if (this.player != null)
            {
                ++this.field_71457_ai;

                if (this.field_71457_ai == 30)
                {
                    this.field_71457_ai = 0;
                    this.world.func_72897_h(this.player);
                }
            }

            this.profiler.func_76318_c("gameRenderer");

            if (!this.isGamePaused)
            {
                this.gameRenderer.tick();
            }

            this.profiler.func_76318_c("levelRenderer");

            if (!this.isGamePaused)
            {
                this.worldRenderer.tick();
            }

            this.profiler.func_76318_c("level");

            if (!this.isGamePaused)
            {
                if (this.world.func_175658_ac() > 0)
                {
                    this.world.func_175702_c(this.world.func_175658_ac() - 1);
                }

                this.world.func_72939_s();
            }
        }
        else if (this.gameRenderer.func_147702_a())
        {
            this.gameRenderer.stopUseShader();
        }

        if (!this.isGamePaused)
        {
            this.musicTicker.tick();
            this.soundHandler.tick();
        }

        if (this.world != null)
        {
            if (!this.isGamePaused)
            {
                this.world.setAllowedSpawnTypes(this.world.getDifficulty() != EnumDifficulty.PEACEFUL, true);
                this.tutorial.tick();

                try
                {
                    this.world.tick();
                }
                catch (Throwable throwable2)
                {
                    CrashReport crashreport2 = CrashReport.makeCrashReport(throwable2, "Exception in world tick");

                    if (this.world == null)
                    {
                        CrashReportCategory crashreportcategory2 = crashreport2.makeCategory("Affected level");
                        crashreportcategory2.addDetail("Problem", "Level is null!");
                    }
                    else
                    {
                        this.world.fillCrashReport(crashreport2);
                    }

                    throw new ReportedException(crashreport2);
                }
            }

            this.profiler.func_76318_c("animateTick");

            if (!this.isGamePaused && this.world != null)
            {
                this.world.animateTick(MathHelper.floor(this.player.posX), MathHelper.floor(this.player.posY), MathHelper.floor(this.player.posZ));
            }

            this.profiler.func_76318_c("particles");

            if (!this.isGamePaused)
            {
                this.particles.tick();
            }
        }
        else if (this.networkManager != null)
        {
            this.profiler.func_76318_c("pendingConnection");
            this.networkManager.tick();
        }

        this.profiler.endSection();
        this.field_71423_H = func_71386_F();
    }

    private void func_184118_az() throws IOException
    {
        while (Keyboard.next())
        {
            int i = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();

            if (this.field_83002_am > 0L)
            {
                if (func_71386_F() - this.field_83002_am >= 6000L)
                {
                    throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
                }

                if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61))
                {
                    this.field_83002_am = -1L;
                }
            }
            else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61))
            {
                this.field_184129_aV = true;
                this.field_83002_am = func_71386_F();
            }

            this.func_152348_aa();

            if (this.currentScreen != null)
            {
                this.currentScreen.func_146282_l();
            }

            boolean flag = Keyboard.getEventKeyState();

            if (flag)
            {
                if (i == 62 && this.gameRenderer != null)
                {
                    this.gameRenderer.switchUseShader();
                }

                boolean flag1 = false;

                if (this.currentScreen == null)
                {
                    if (i == 1)
                    {
                        this.displayInGameMenu();
                    }

                    flag1 = Keyboard.isKeyDown(61) && this.func_184122_c(i);
                    this.field_184129_aV |= flag1;

                    if (i == 59)
                    {
                        this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
                    }
                }

                if (flag1)
                {
                    KeyBinding.func_74510_a(i, false);
                }
                else
                {
                    KeyBinding.func_74510_a(i, true);
                    KeyBinding.func_74507_a(i);
                }

                if (this.gameSettings.showDebugProfilerChart)
                {
                    if (i == 11)
                    {
                        this.updateDebugProfilerName(0);
                    }

                    for (int j = 0; j < 9; ++j)
                    {
                        if (i == 2 + j)
                        {
                            this.updateDebugProfilerName(j + 1);
                        }
                    }
                }
            }
            else
            {
                KeyBinding.func_74510_a(i, false);

                if (i == 61)
                {
                    if (this.field_184129_aV)
                    {
                        this.field_184129_aV = false;
                    }
                    else
                    {
                        this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
                        this.gameSettings.showDebugProfilerChart = this.gameSettings.showDebugInfo && GuiScreen.func_146272_n();
                        this.gameSettings.showLagometer = this.gameSettings.showDebugInfo && GuiScreen.func_175283_s();
                    }
                }
            }
        }

        this.processKeyBinds();
    }

    private boolean func_184122_c(int p_184122_1_)
    {
        if (p_184122_1_ == 30)
        {
            this.worldRenderer.loadRenderers();
            this.func_190521_a("debug.reload_chunks.message");
            return true;
        }
        else if (p_184122_1_ == 48)
        {
            boolean flag1 = !this.renderManager.isDebugBoundingBox();
            this.renderManager.setDebugBoundingBox(flag1);
            this.func_190521_a(flag1 ? "debug.show_hitboxes.on" : "debug.show_hitboxes.off");
            return true;
        }
        else if (p_184122_1_ == 32)
        {
            if (this.ingameGUI != null)
            {
                this.ingameGUI.getChatGUI().clearChatMessages(false);
            }

            return true;
        }
        else if (p_184122_1_ == 33)
        {
            this.gameSettings.func_74306_a(GameSettings.Options.RENDER_DISTANCE, GuiScreen.func_146272_n() ? -1 : 1);
            this.func_190521_a("debug.cycle_renderdistance.message", this.gameSettings.renderDistanceChunks);
            return true;
        }
        else if (p_184122_1_ == 34)
        {
            boolean flag = this.debugRenderer.toggleChunkBorders();
            this.func_190521_a(flag ? "debug.chunk_boundaries.on" : "debug.chunk_boundaries.off");
            return true;
        }
        else if (p_184122_1_ == 35)
        {
            this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
            this.func_190521_a(this.gameSettings.advancedItemTooltips ? "debug.advanced_tooltips.on" : "debug.advanced_tooltips.off");
            this.gameSettings.saveOptions();
            return true;
        }
        else if (p_184122_1_ == 49)
        {
            if (!this.player.func_70003_b(2, ""))
            {
                this.func_190521_a("debug.creative_spectator.error");
            }
            else if (this.player.isCreative())
            {
                this.player.sendChatMessage("/gamemode spectator");
            }
            else if (this.player.isSpectator())
            {
                this.player.sendChatMessage("/gamemode creative");
            }

            return true;
        }
        else if (p_184122_1_ == 25)
        {
            this.gameSettings.pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
            this.gameSettings.saveOptions();
            this.func_190521_a(this.gameSettings.pauseOnLostFocus ? "debug.pause_focus.on" : "debug.pause_focus.off");
            return true;
        }
        else if (p_184122_1_ == 16)
        {
            this.func_190521_a("debug.help.message");
            GuiNewChat guinewchat = this.ingameGUI.getChatGUI();
            guinewchat.printChatMessage(new TextComponentTranslation("debug.reload_chunks.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.show_hitboxes.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.clear_chat.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.cycle_renderdistance.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.chunk_boundaries.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.advanced_tooltips.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.creative_spectator.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.pause_focus.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.help.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.reload_resourcepacks.help", new Object[0]));
            return true;
        }
        else if (p_184122_1_ == 20)
        {
            this.func_190521_a("debug.reload_resourcepacks.message");
            this.func_110436_a();
            return true;
        }
        else
        {
            return false;
        }
    }

    private void processKeyBinds()
    {
        for (; this.gameSettings.keyBindTogglePerspective.isPressed(); this.worldRenderer.setDisplayListEntitiesDirty())
        {
            ++this.gameSettings.thirdPersonView;

            if (this.gameSettings.thirdPersonView > 2)
            {
                this.gameSettings.thirdPersonView = 0;
            }

            if (this.gameSettings.thirdPersonView == 0)
            {
                this.gameRenderer.loadEntityShader(this.getRenderViewEntity());
            }
            else if (this.gameSettings.thirdPersonView == 1)
            {
                this.gameRenderer.loadEntityShader((Entity)null);
            }
        }

        while (this.gameSettings.keyBindSmoothCamera.isPressed())
        {
            this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
        }

        for (int i = 0; i < 9; ++i)
        {
            boolean flag = this.gameSettings.keyBindSaveToolbar.isKeyDown();
            boolean flag1 = this.gameSettings.keyBindLoadToolbar.isKeyDown();

            if (this.gameSettings.keyBindsHotbar[i].isPressed())
            {
                if (this.player.isSpectator())
                {
                    this.ingameGUI.getSpectatorGui().onHotbarSelected(i);
                }
                else if (!this.player.isCreative() || this.currentScreen != null || !flag1 && !flag)
                {
                    this.player.inventory.currentItem = i;
                }
                else
                {
                    GuiContainerCreative.handleHotbarSnapshots(this, i, flag1, flag);
                }
            }
        }

        while (this.gameSettings.keyBindInventory.isPressed())
        {
            if (this.playerController.isRidingHorse())
            {
                this.player.sendHorseInventory();
            }
            else
            {
                this.tutorial.openInventory();
                this.displayGuiScreen(new GuiInventory(this.player));
            }
        }

        while (this.gameSettings.keyBindAdvancements.isPressed())
        {
            this.displayGuiScreen(new GuiScreenAdvancements(this.player.connection.getAdvancementManager()));
        }

        while (this.gameSettings.keyBindSwapHands.isPressed())
        {
            if (!this.player.isSpectator())
            {
                this.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, BlockPos.ZERO, EnumFacing.DOWN));
            }
        }

        while (this.gameSettings.keyBindDrop.isPressed())
        {
            if (!this.player.isSpectator())
            {
                this.player.func_71040_bB(GuiScreen.func_146271_m());
            }
        }

        boolean flag2 = this.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN;

        if (flag2)
        {
            while (this.gameSettings.keyBindChat.isPressed())
            {
                this.displayGuiScreen(new GuiChat());
            }

            if (this.currentScreen == null && this.gameSettings.keyBindCommand.isPressed())
            {
                this.displayGuiScreen(new GuiChat("/"));
            }
        }

        if (this.player.isHandActive())
        {
            if (!this.gameSettings.keyBindUseItem.isKeyDown())
            {
                this.playerController.onStoppedUsingItem(this.player);
            }

            label109:

            while (true)
            {
                if (!this.gameSettings.keyBindAttack.isPressed())
                {
                    while (this.gameSettings.keyBindUseItem.isPressed())
                    {
                        ;
                    }

                    while (true)
                    {
                        if (this.gameSettings.keyBindPickBlock.isPressed())
                        {
                            continue;
                        }

                        break label109;
                    }
                }
            }
        }
        else
        {
            while (this.gameSettings.keyBindAttack.isPressed())
            {
                this.clickMouse();
            }

            while (this.gameSettings.keyBindUseItem.isPressed())
            {
                this.rightClickMouse();
            }

            while (this.gameSettings.keyBindPickBlock.isPressed())
            {
                this.middleClickMouse();
            }
        }

        if (this.gameSettings.keyBindUseItem.isKeyDown() && this.rightClickDelayTimer == 0 && !this.player.isHandActive())
        {
            this.rightClickMouse();
        }

        this.sendClickBlockToController(this.currentScreen == null && this.gameSettings.keyBindAttack.isKeyDown() && this.field_71415_G);
    }

    private void func_184124_aB() throws IOException
    {
        while (Mouse.next())
        {
            int i = Mouse.getEventButton();
            KeyBinding.func_74510_a(i - 100, Mouse.getEventButtonState());

            if (Mouse.getEventButtonState())
            {
                if (this.player.isSpectator() && i == 2)
                {
                    this.ingameGUI.getSpectatorGui().onMiddleClick();
                }
                else
                {
                    KeyBinding.func_74507_a(i - 100);
                }
            }

            long j = func_71386_F() - this.field_71423_H;

            if (j <= 200L)
            {
                int k = Mouse.getEventDWheel();

                if (k != 0)
                {
                    if (this.player.isSpectator())
                    {
                        k = k < 0 ? -1 : 1;

                        if (this.ingameGUI.getSpectatorGui().isMenuActive())
                        {
                            this.ingameGUI.getSpectatorGui().func_175259_b(-k);
                        }
                        else
                        {
                            float f = MathHelper.clamp(this.player.abilities.getFlySpeed() + (float)k * 0.005F, 0.0F, 0.2F);
                            this.player.abilities.func_75092_a(f);
                        }
                    }
                    else
                    {
                        this.player.inventory.func_70453_c(k);
                    }
                }

                if (this.currentScreen == null)
                {
                    if (!this.field_71415_G && Mouse.getEventButtonState())
                    {
                        this.func_71381_h();
                    }
                }
                else if (this.currentScreen != null)
                {
                    this.currentScreen.func_146274_d();
                }
            }
        }
    }

    private void func_190521_a(String p_190521_1_, Object... p_190521_2_)
    {
        this.ingameGUI.getChatGUI().printChatMessage((new TextComponentString("")).appendSibling((new TextComponentTranslation("debug.prefix", new Object[0])).setStyle((new Style()).setColor(TextFormatting.YELLOW).setBold(Boolean.valueOf(true)))).appendText(" ").appendSibling(new TextComponentTranslation(p_190521_1_, p_190521_2_)));
    }

    /**
     * Arguments: World foldername,  World ingame name, WorldSettings
     */
    public void launchIntegratedServer(String folderName, String worldName, @Nullable WorldSettings worldSettingsIn)
    {
        this.loadWorld((WorldClient)null);
        System.gc();
        ISaveHandler isavehandler = this.saveFormat.func_75804_a(folderName, false);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();

        if (worldinfo == null && worldSettingsIn != null)
        {
            worldinfo = new WorldInfo(worldSettingsIn, folderName);
            isavehandler.saveWorldInfo(worldinfo);
        }

        if (worldSettingsIn == null)
        {
            worldSettingsIn = new WorldSettings(worldinfo);
        }

        try
        {
            YggdrasilAuthenticationService yggdrasilauthenticationservice = new YggdrasilAuthenticationService(this.proxy, UUID.randomUUID().toString());
            MinecraftSessionService minecraftsessionservice = yggdrasilauthenticationservice.createMinecraftSessionService();
            GameProfileRepository gameprofilerepository = yggdrasilauthenticationservice.createProfileRepository();
            PlayerProfileCache playerprofilecache = new PlayerProfileCache(gameprofilerepository, new File(this.gameDir, MinecraftServer.USER_CACHE_FILE.getName()));
            TileEntitySkull.setProfileCache(playerprofilecache);
            TileEntitySkull.setSessionService(minecraftsessionservice);
            PlayerProfileCache.setOnlineMode(false);
            this.integratedServer = new IntegratedServer(this, folderName, worldName, worldSettingsIn, yggdrasilauthenticationservice, minecraftsessionservice, gameprofilerepository, playerprofilecache);
            this.integratedServer.startServerThread();
            this.integratedServerIsRunning = true;
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Starting integrated server");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Starting integrated server");
            crashreportcategory.addDetail("Level ID", folderName);
            crashreportcategory.addDetail("Level Name", worldName);
            throw new ReportedException(crashreport);
        }

        this.field_71461_s.func_73720_a(I18n.format("menu.loadingLevel"));

        while (!this.integratedServer.serverIsInRunLoop())
        {
            String s = this.integratedServer.func_71195_b_();

            if (s != null)
            {
                this.field_71461_s.func_73719_c(I18n.format(s));
            }
            else
            {
                this.field_71461_s.func_73719_c("");
            }

            try
            {
                Thread.sleep(200L);
            }
            catch (InterruptedException var10)
            {
                ;
            }
        }

        this.displayGuiScreen(new GuiScreenWorking());
        SocketAddress socketaddress = this.integratedServer.getNetworkSystem().addLocalEndpoint();
        NetworkManager networkmanager = NetworkManager.provideLocalClient(socketaddress);
        networkmanager.setNetHandler(new NetHandlerLoginClient(networkmanager, this, (GuiScreen)null));
        networkmanager.sendPacket(new C00Handshake(socketaddress.toString(), 0, EnumConnectionState.LOGIN));
        networkmanager.sendPacket(new CPacketLoginStart(this.getSession().getProfile()));
        this.networkManager = networkmanager;
    }

    /**
     * unloads the current world first
     */
    public void loadWorld(@Nullable WorldClient worldClientIn)
    {
        this.func_71353_a(worldClientIn, "");
    }

    public void func_71353_a(@Nullable WorldClient p_71353_1_, String p_71353_2_)
    {
        if (p_71353_1_ == null)
        {
            NetHandlerPlayClient nethandlerplayclient = this.getConnection();

            if (nethandlerplayclient != null)
            {
                nethandlerplayclient.cleanup();
            }

            if (this.integratedServer != null && this.integratedServer.isAnvilFileSet())
            {
                this.integratedServer.initiateShutdown();
            }

            this.integratedServer = null;
            this.gameRenderer.resetData();
            this.playerController = null;
            NarratorChatListener.INSTANCE.clear();
        }

        this.renderViewEntity = null;
        this.networkManager = null;

        if (this.field_71461_s != null)
        {
            this.field_71461_s.func_73721_b(p_71353_2_);
            this.field_71461_s.func_73719_c("");
        }

        if (p_71353_1_ == null && this.world != null)
        {
            this.resourcePackRepository.func_148529_f();
            this.ingameGUI.resetPlayersOverlayFooterHeader();
            this.setServerData((ServerData)null);
            this.integratedServerIsRunning = false;
        }

        this.soundHandler.stop();
        this.world = p_71353_1_;

        if (this.worldRenderer != null)
        {
            this.worldRenderer.setWorldAndLoadRenderers(p_71353_1_);
        }

        if (this.particles != null)
        {
            this.particles.clearEffects(p_71353_1_);
        }

        TileEntityRendererDispatcher.instance.setWorld(p_71353_1_);

        if (p_71353_1_ != null)
        {
            if (!this.integratedServerIsRunning)
            {
                AuthenticationService authenticationservice = new YggdrasilAuthenticationService(this.proxy, UUID.randomUUID().toString());
                MinecraftSessionService minecraftsessionservice = authenticationservice.createMinecraftSessionService();
                GameProfileRepository gameprofilerepository = authenticationservice.createProfileRepository();
                PlayerProfileCache playerprofilecache = new PlayerProfileCache(gameprofilerepository, new File(this.gameDir, MinecraftServer.USER_CACHE_FILE.getName()));
                TileEntitySkull.setProfileCache(playerprofilecache);
                TileEntitySkull.setSessionService(minecraftsessionservice);
                PlayerProfileCache.setOnlineMode(false);
            }

            if (this.player == null)
            {
                this.player = this.playerController.func_192830_a(p_71353_1_, new StatisticsManager(), new RecipeBookClient());
                this.playerController.func_78745_b(this.player);
            }

            this.player.preparePlayerToSpawn();
            p_71353_1_.addEntity0(this.player);
            this.player.movementInput = new MovementInputFromOptions(this.gameSettings);
            this.playerController.setPlayerCapabilities(this.player);
            this.renderViewEntity = this.player;
        }
        else
        {
            this.saveFormat.func_75800_d();
            this.player = null;
        }

        System.gc();
        this.field_71423_H = 0L;
    }

    public void func_71354_a(int p_71354_1_)
    {
        this.world.setInitialSpawnLocation();
        this.world.removeAllEntities();
        int i = 0;
        String s = null;

        if (this.player != null)
        {
            i = this.player.getEntityId();
            this.world.func_72900_e(this.player);
            s = this.player.getServerBrand();
        }

        this.renderViewEntity = null;
        EntityPlayerSP entityplayersp = this.player;
        this.player = this.playerController.func_192830_a(this.world, this.player == null ? new StatisticsManager() : this.player.getStats(), this.player == null ? new RecipeBook() : this.player.func_192035_E());
        this.player.getDataManager().setEntryValues(entityplayersp.getDataManager().getAll());
        this.player.dimension = p_71354_1_;
        this.renderViewEntity = this.player;
        this.player.preparePlayerToSpawn();
        this.player.setServerBrand(s);
        this.world.addEntity0(this.player);
        this.playerController.func_78745_b(this.player);
        this.player.movementInput = new MovementInputFromOptions(this.gameSettings);
        this.player.setEntityId(i);
        this.playerController.setPlayerCapabilities(this.player);
        this.player.setReducedDebug(entityplayersp.hasReducedDebug());

        if (this.currentScreen instanceof GuiGameOver)
        {
            this.displayGuiScreen((GuiScreen)null);
        }
    }

    /**
     * Gets whether this is a demo or not.
     */
    public final boolean isDemo()
    {
        return this.isDemo;
    }

    @Nullable
    public NetHandlerPlayClient getConnection()
    {
        return this.player == null ? null : this.player.connection;
    }

    public static boolean isGuiEnabled()
    {
        return instance == null || !instance.gameSettings.hideGUI;
    }

    public static boolean isFancyGraphicsEnabled()
    {
        return instance != null && instance.gameSettings.fancyGraphics;
    }

    /**
     * Returns if ambient occlusion is enabled
     */
    public static boolean isAmbientOcclusionEnabled()
    {
        return instance != null && instance.gameSettings.ambientOcclusionStatus != 0;
    }

    /**
     * Called when user clicked he's mouse middle button (pick block)
     */
    private void middleClickMouse()
    {
        if (this.objectMouseOver != null && this.objectMouseOver.field_72313_a != RayTraceResult.Type.MISS)
        {
            boolean flag = this.player.abilities.isCreativeMode;
            TileEntity tileentity = null;
            ItemStack itemstack;

            if (this.objectMouseOver.field_72313_a == RayTraceResult.Type.BLOCK)
            {
                BlockPos blockpos = this.objectMouseOver.func_178782_a();
                IBlockState iblockstate = this.world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (iblockstate.getMaterial() == Material.AIR)
                {
                    return;
                }

                itemstack = block.getItem(this.world, blockpos, iblockstate);

                if (itemstack.isEmpty())
                {
                    return;
                }

                if (flag && GuiScreen.func_146271_m() && block.hasTileEntity())
                {
                    tileentity = this.world.getTileEntity(blockpos);
                }
            }
            else
            {
                if (this.objectMouseOver.field_72313_a != RayTraceResult.Type.ENTITY || this.objectMouseOver.field_72308_g == null || !flag)
                {
                    return;
                }

                if (this.objectMouseOver.field_72308_g instanceof EntityPainting)
                {
                    itemstack = new ItemStack(Items.PAINTING);
                }
                else if (this.objectMouseOver.field_72308_g instanceof EntityLeashKnot)
                {
                    itemstack = new ItemStack(Items.LEAD);
                }
                else if (this.objectMouseOver.field_72308_g instanceof EntityItemFrame)
                {
                    EntityItemFrame entityitemframe = (EntityItemFrame)this.objectMouseOver.field_72308_g;
                    ItemStack itemstack1 = entityitemframe.getDisplayedItem();

                    if (itemstack1.isEmpty())
                    {
                        itemstack = new ItemStack(Items.ITEM_FRAME);
                    }
                    else
                    {
                        itemstack = itemstack1.copy();
                    }
                }
                else if (this.objectMouseOver.field_72308_g instanceof EntityMinecart)
                {
                    EntityMinecart entityminecart = (EntityMinecart)this.objectMouseOver.field_72308_g;
                    Item item1;

                    switch (entityminecart.getMinecartType())
                    {
                        case FURNACE:
                            item1 = Items.FURNACE_MINECART;
                            break;

                        case CHEST:
                            item1 = Items.CHEST_MINECART;
                            break;

                        case TNT:
                            item1 = Items.TNT_MINECART;
                            break;

                        case HOPPER:
                            item1 = Items.HOPPER_MINECART;
                            break;

                        case COMMAND_BLOCK:
                            item1 = Items.COMMAND_BLOCK_MINECART;
                            break;

                        default:
                            item1 = Items.MINECART;
                    }

                    itemstack = new ItemStack(item1);
                }
                else if (this.objectMouseOver.field_72308_g instanceof EntityBoat)
                {
                    itemstack = new ItemStack(((EntityBoat)this.objectMouseOver.field_72308_g).getItemBoat());
                }
                else if (this.objectMouseOver.field_72308_g instanceof EntityArmorStand)
                {
                    itemstack = new ItemStack(Items.ARMOR_STAND);
                }
                else if (this.objectMouseOver.field_72308_g instanceof EntityEnderCrystal)
                {
                    itemstack = new ItemStack(Items.END_CRYSTAL);
                }
                else
                {
                    ResourceLocation resourcelocation = EntityList.func_191301_a(this.objectMouseOver.field_72308_g);

                    if (resourcelocation == null || !EntityList.field_75627_a.containsKey(resourcelocation))
                    {
                        return;
                    }

                    itemstack = new ItemStack(Items.field_151063_bx);
                    ItemMonsterPlacer.func_185078_a(itemstack, resourcelocation);
                }
            }

            if (itemstack.isEmpty())
            {
                String s = "";

                if (this.objectMouseOver.field_72313_a == RayTraceResult.Type.BLOCK)
                {
                    s = ((ResourceLocation)Block.field_149771_c.getKey(this.world.getBlockState(this.objectMouseOver.func_178782_a()).getBlock())).toString();
                }
                else if (this.objectMouseOver.field_72313_a == RayTraceResult.Type.ENTITY)
                {
                    s = EntityList.func_191301_a(this.objectMouseOver.field_72308_g).toString();
                }

                LOGGER.warn("Picking on: [{}] {} gave null item", this.objectMouseOver.field_72313_a, s);
            }
            else
            {
                InventoryPlayer inventoryplayer = this.player.inventory;

                if (tileentity != null)
                {
                    this.storeTEInStack(itemstack, tileentity);
                }

                int i = inventoryplayer.getSlotFor(itemstack);

                if (flag)
                {
                    inventoryplayer.setPickedItemStack(itemstack);
                    this.playerController.sendSlotPacket(this.player.getHeldItem(EnumHand.MAIN_HAND), 36 + inventoryplayer.currentItem);
                }
                else if (i != -1)
                {
                    if (InventoryPlayer.isHotbar(i))
                    {
                        inventoryplayer.currentItem = i;
                    }
                    else
                    {
                        this.playerController.pickItem(i);
                    }
                }
            }
        }
    }

    private ItemStack storeTEInStack(ItemStack stack, TileEntity te)
    {
        NBTTagCompound nbttagcompound = te.write(new NBTTagCompound());

        if (stack.getItem() == Items.field_151144_bL && nbttagcompound.contains("Owner"))
        {
            NBTTagCompound nbttagcompound2 = nbttagcompound.getCompound("Owner");
            NBTTagCompound nbttagcompound3 = new NBTTagCompound();
            nbttagcompound3.func_74782_a("SkullOwner", nbttagcompound2);
            stack.setTag(nbttagcompound3);
            return stack;
        }
        else
        {
            stack.setTagInfo("BlockEntityTag", nbttagcompound);
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            NBTTagList nbttaglist = new NBTTagList();
            nbttaglist.func_74742_a(new NBTTagString("(+NBT)"));
            nbttagcompound1.func_74782_a("Lore", nbttaglist);
            stack.setTagInfo("display", nbttagcompound1);
            return stack;
        }
    }

    /**
     * adds core server Info (GL version , Texture pack, isModded, type), and the worldInfo to the crash report
     */
    public CrashReport addGraphicsAndWorldToCrashReport(CrashReport theCrash)
    {
        theCrash.getCategory().addDetail("Launched Version", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return Minecraft.this.launchedVersion;
            }
        });
        theCrash.getCategory().addDetail("LWJGL", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return Sys.getVersion();
            }
        });
        theCrash.getCategory().addDetail("OpenGL", new ICrashReportDetail<String>()
        {
            public String call()
            {
                return GlStateManager.func_187416_u(7937) + " GL version " + GlStateManager.func_187416_u(7938) + ", " + GlStateManager.func_187416_u(7936);
            }
        });
        theCrash.getCategory().addDetail("GL Caps", new ICrashReportDetail<String>()
        {
            public String call()
            {
                return OpenGlHelper.func_153172_c();
            }
        });
        theCrash.getCategory().addDetail("Using VBOs", new ICrashReportDetail<String>()
        {
            public String call()
            {
                return Minecraft.this.gameSettings.field_178881_t ? "Yes" : "No";
            }
        });
        theCrash.getCategory().addDetail("Is Modded", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                String s = ClientBrandRetriever.getClientModName();

                if (!"vanilla".equals(s))
                {
                    return "Definitely; Client brand changed to '" + s + "'";
                }
                else
                {
                    return Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and client brand is untouched.";
                }
            }
        });
        theCrash.getCategory().addDetail("Type", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return "Client (map_client.txt)";
            }
        });
        theCrash.getCategory().addDetail("Resource Packs", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                StringBuilder stringbuilder = new StringBuilder();

                for (String s : Minecraft.this.gameSettings.resourcePacks)
                {
                    if (stringbuilder.length() > 0)
                    {
                        stringbuilder.append(", ");
                    }

                    stringbuilder.append(s);

                    if (Minecraft.this.gameSettings.incompatibleResourcePacks.contains(s))
                    {
                        stringbuilder.append(" (incompatible)");
                    }
                }

                return stringbuilder.toString();
            }
        });
        theCrash.getCategory().addDetail("Current Language", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return Minecraft.this.languageManager.getCurrentLanguage().toString();
            }
        });
        theCrash.getCategory().addDetail("Profiler Position", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return Minecraft.this.profiler.field_76327_a ? Minecraft.this.profiler.func_76322_c() : "N/A (disabled)";
            }
        });
        theCrash.getCategory().addDetail("CPU", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return OpenGlHelper.func_183029_j();
            }
        });

        if (this.world != null)
        {
            this.world.fillCrashReport(theCrash);
        }

        return theCrash;
    }

    /**
     * Return the singleton Minecraft instance for the game
     */
    public static Minecraft getInstance()
    {
        return instance;
    }

    public ListenableFuture<Object> func_175603_A()
    {
        return this.func_152344_a(new Runnable()
        {
            public void run()
            {
                Minecraft.this.func_110436_a();
            }
        });
    }

    public void fillSnooper(Snooper snooper)
    {
        snooper.addClientStat("fps", Integer.valueOf(debugFPS));
        snooper.addClientStat("vsync_enabled", Boolean.valueOf(this.gameSettings.vsync));
        snooper.addClientStat("display_frequency", Integer.valueOf(Display.getDisplayMode().getFrequency()));
        snooper.addClientStat("display_type", this.field_71431_Q ? "fullscreen" : "windowed");
        snooper.addClientStat("run_time", Long.valueOf((MinecraftServer.func_130071_aq() - snooper.getMinecraftStartTimeMillis()) / 60L * 1000L));
        snooper.addClientStat("current_action", this.getCurrentAction());
        snooper.addClientStat("language", this.gameSettings.language == null ? "en_us" : this.gameSettings.language);
        String s = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN ? "little" : "big";
        snooper.addClientStat("endianness", s);
        snooper.addClientStat("subtitles", Boolean.valueOf(this.gameSettings.showSubtitles));
        snooper.addClientStat("touch", this.gameSettings.touchscreen ? "touch" : "mouse");
        snooper.addClientStat("resource_packs", Integer.valueOf(this.resourcePackRepository.func_110613_c().size()));
        int i = 0;

        for (ResourcePackRepository.Entry resourcepackrepository$entry : this.resourcePackRepository.func_110613_c())
        {
            snooper.addClientStat("resource_pack[" + i++ + "]", resourcepackrepository$entry.func_110515_d());
        }

        if (this.integratedServer != null && this.integratedServer.getSnooper() != null)
        {
            snooper.addClientStat("snooper_partner", this.integratedServer.getSnooper().getUniqueID());
        }
    }

    /**
     * Return the current action's name
     */
    private String getCurrentAction()
    {
        if (this.integratedServer != null)
        {
            return this.integratedServer.getPublic() ? "hosting_lan" : "singleplayer";
        }
        else if (this.currentServerData != null)
        {
            return this.currentServerData.isOnLAN() ? "playing_lan" : "multiplayer";
        }
        else
        {
            return "out_of_game";
        }
    }

    public void func_70001_b(Snooper p_70001_1_)
    {
        p_70001_1_.addStatToSnooper("opengl_version", GlStateManager.func_187416_u(7938));
        p_70001_1_.addStatToSnooper("opengl_vendor", GlStateManager.func_187416_u(7936));
        p_70001_1_.addStatToSnooper("client_brand", ClientBrandRetriever.getClientModName());
        p_70001_1_.addStatToSnooper("launched_version", this.launchedVersion);
        ContextCapabilities contextcapabilities = GLContext.getCapabilities();
        p_70001_1_.addStatToSnooper("gl_caps[ARB_arrays_of_arrays]", Boolean.valueOf(contextcapabilities.GL_ARB_arrays_of_arrays));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_base_instance]", Boolean.valueOf(contextcapabilities.GL_ARB_base_instance));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_blend_func_extended]", Boolean.valueOf(contextcapabilities.GL_ARB_blend_func_extended));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_clear_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_clear_buffer_object));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_color_buffer_float]", Boolean.valueOf(contextcapabilities.GL_ARB_color_buffer_float));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_compatibility]", Boolean.valueOf(contextcapabilities.GL_ARB_compatibility));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_compressed_texture_pixel_storage]", Boolean.valueOf(contextcapabilities.GL_ARB_compressed_texture_pixel_storage));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_compute_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_compute_shader));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_copy_buffer]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_buffer));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_copy_image]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_image));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", Boolean.valueOf(contextcapabilities.GL_ARB_depth_buffer_float));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_compute_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_compute_shader));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_copy_buffer]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_buffer));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_copy_image]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_image));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", Boolean.valueOf(contextcapabilities.GL_ARB_depth_buffer_float));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_depth_clamp]", Boolean.valueOf(contextcapabilities.GL_ARB_depth_clamp));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_depth_texture]", Boolean.valueOf(contextcapabilities.GL_ARB_depth_texture));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_draw_buffers]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_buffers));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_draw_buffers_blend]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_buffers_blend));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_draw_elements_base_vertex]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_elements_base_vertex));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_draw_indirect]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_indirect));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_draw_instanced]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_instanced));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_explicit_attrib_location]", Boolean.valueOf(contextcapabilities.GL_ARB_explicit_attrib_location));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_explicit_uniform_location]", Boolean.valueOf(contextcapabilities.GL_ARB_explicit_uniform_location));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_fragment_layer_viewport]", Boolean.valueOf(contextcapabilities.GL_ARB_fragment_layer_viewport));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_fragment_program]", Boolean.valueOf(contextcapabilities.GL_ARB_fragment_program));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_fragment_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_fragment_shader));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_fragment_program_shadow]", Boolean.valueOf(contextcapabilities.GL_ARB_fragment_program_shadow));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_framebuffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_framebuffer_object));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_framebuffer_sRGB]", Boolean.valueOf(contextcapabilities.GL_ARB_framebuffer_sRGB));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_geometry_shader4]", Boolean.valueOf(contextcapabilities.GL_ARB_geometry_shader4));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_gpu_shader5]", Boolean.valueOf(contextcapabilities.GL_ARB_gpu_shader5));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_half_float_pixel]", Boolean.valueOf(contextcapabilities.GL_ARB_half_float_pixel));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_half_float_vertex]", Boolean.valueOf(contextcapabilities.GL_ARB_half_float_vertex));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_instanced_arrays]", Boolean.valueOf(contextcapabilities.GL_ARB_instanced_arrays));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_map_buffer_alignment]", Boolean.valueOf(contextcapabilities.GL_ARB_map_buffer_alignment));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_map_buffer_range]", Boolean.valueOf(contextcapabilities.GL_ARB_map_buffer_range));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_multisample]", Boolean.valueOf(contextcapabilities.GL_ARB_multisample));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_multitexture]", Boolean.valueOf(contextcapabilities.GL_ARB_multitexture));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_occlusion_query2]", Boolean.valueOf(contextcapabilities.GL_ARB_occlusion_query2));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_pixel_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_pixel_buffer_object));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_seamless_cube_map]", Boolean.valueOf(contextcapabilities.GL_ARB_seamless_cube_map));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_shader_objects]", Boolean.valueOf(contextcapabilities.GL_ARB_shader_objects));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_shader_stencil_export]", Boolean.valueOf(contextcapabilities.GL_ARB_shader_stencil_export));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_shader_texture_lod]", Boolean.valueOf(contextcapabilities.GL_ARB_shader_texture_lod));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_shadow]", Boolean.valueOf(contextcapabilities.GL_ARB_shadow));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_shadow_ambient]", Boolean.valueOf(contextcapabilities.GL_ARB_shadow_ambient));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_stencil_texturing]", Boolean.valueOf(contextcapabilities.GL_ARB_stencil_texturing));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_sync]", Boolean.valueOf(contextcapabilities.GL_ARB_sync));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_tessellation_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_tessellation_shader));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_texture_border_clamp]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_border_clamp));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_texture_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_buffer_object));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_texture_cube_map]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_cube_map));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_texture_cube_map_array]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_cube_map_array));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_texture_non_power_of_two]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_non_power_of_two));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_uniform_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_uniform_buffer_object));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_vertex_blend]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_blend));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_vertex_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_buffer_object));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_vertex_program]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_program));
        p_70001_1_.addStatToSnooper("gl_caps[ARB_vertex_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_shader));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_bindable_uniform]", Boolean.valueOf(contextcapabilities.GL_EXT_bindable_uniform));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_blend_equation_separate]", Boolean.valueOf(contextcapabilities.GL_EXT_blend_equation_separate));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_blend_func_separate]", Boolean.valueOf(contextcapabilities.GL_EXT_blend_func_separate));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_blend_minmax]", Boolean.valueOf(contextcapabilities.GL_EXT_blend_minmax));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_blend_subtract]", Boolean.valueOf(contextcapabilities.GL_EXT_blend_subtract));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_draw_instanced]", Boolean.valueOf(contextcapabilities.GL_EXT_draw_instanced));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_framebuffer_multisample]", Boolean.valueOf(contextcapabilities.GL_EXT_framebuffer_multisample));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_framebuffer_object]", Boolean.valueOf(contextcapabilities.GL_EXT_framebuffer_object));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_framebuffer_sRGB]", Boolean.valueOf(contextcapabilities.GL_EXT_framebuffer_sRGB));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_geometry_shader4]", Boolean.valueOf(contextcapabilities.GL_EXT_geometry_shader4));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_gpu_program_parameters]", Boolean.valueOf(contextcapabilities.GL_EXT_gpu_program_parameters));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_gpu_shader4]", Boolean.valueOf(contextcapabilities.GL_EXT_gpu_shader4));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_multi_draw_arrays]", Boolean.valueOf(contextcapabilities.GL_EXT_multi_draw_arrays));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_packed_depth_stencil]", Boolean.valueOf(contextcapabilities.GL_EXT_packed_depth_stencil));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_paletted_texture]", Boolean.valueOf(contextcapabilities.GL_EXT_paletted_texture));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_rescale_normal]", Boolean.valueOf(contextcapabilities.GL_EXT_rescale_normal));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_separate_shader_objects]", Boolean.valueOf(contextcapabilities.GL_EXT_separate_shader_objects));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_shader_image_load_store]", Boolean.valueOf(contextcapabilities.GL_EXT_shader_image_load_store));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_shadow_funcs]", Boolean.valueOf(contextcapabilities.GL_EXT_shadow_funcs));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_shared_texture_palette]", Boolean.valueOf(contextcapabilities.GL_EXT_shared_texture_palette));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_stencil_clear_tag]", Boolean.valueOf(contextcapabilities.GL_EXT_stencil_clear_tag));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_stencil_two_side]", Boolean.valueOf(contextcapabilities.GL_EXT_stencil_two_side));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_stencil_wrap]", Boolean.valueOf(contextcapabilities.GL_EXT_stencil_wrap));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_texture_3d]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_3d));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_texture_array]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_array));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_texture_buffer_object]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_buffer_object));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_texture_integer]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_integer));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_texture_lod_bias]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_lod_bias));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_texture_sRGB]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_sRGB));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_vertex_shader]", Boolean.valueOf(contextcapabilities.GL_EXT_vertex_shader));
        p_70001_1_.addStatToSnooper("gl_caps[EXT_vertex_weighting]", Boolean.valueOf(contextcapabilities.GL_EXT_vertex_weighting));
        p_70001_1_.addStatToSnooper("gl_caps[gl_max_vertex_uniforms]", Integer.valueOf(GlStateManager.func_187397_v(35658)));
        GlStateManager.func_187434_L();
        p_70001_1_.addStatToSnooper("gl_caps[gl_max_fragment_uniforms]", Integer.valueOf(GlStateManager.func_187397_v(35657)));
        GlStateManager.func_187434_L();
        p_70001_1_.addStatToSnooper("gl_caps[gl_max_vertex_attribs]", Integer.valueOf(GlStateManager.func_187397_v(34921)));
        GlStateManager.func_187434_L();
        p_70001_1_.addStatToSnooper("gl_caps[gl_max_vertex_texture_image_units]", Integer.valueOf(GlStateManager.func_187397_v(35660)));
        GlStateManager.func_187434_L();
        p_70001_1_.addStatToSnooper("gl_caps[gl_max_texture_image_units]", Integer.valueOf(GlStateManager.func_187397_v(34930)));
        GlStateManager.func_187434_L();
        p_70001_1_.addStatToSnooper("gl_caps[gl_max_array_texture_layers]", Integer.valueOf(GlStateManager.func_187397_v(35071)));
        GlStateManager.func_187434_L();
        p_70001_1_.addStatToSnooper("gl_max_texture_size", Integer.valueOf(func_71369_N()));
        GameProfile gameprofile = this.session.getProfile();

        if (gameprofile != null && gameprofile.getId() != null)
        {
            p_70001_1_.addStatToSnooper("uuid", Hashing.sha1().hashBytes(gameprofile.getId().toString().getBytes(Charsets.ISO_8859_1)).toString());
        }
    }

    public static int func_71369_N()
    {
        for (int i = 16384; i > 0; i >>= 1)
        {
            GlStateManager.func_187419_a(32868, 0, 6408, i, i, 0, 6408, 5121, (IntBuffer)null);
            int j = GlStateManager.func_187411_c(32868, 0, 4096);

            if (j != 0)
            {
                return i;
            }
        }

        return -1;
    }

    public boolean func_70002_Q()
    {
        return this.gameSettings.snooper;
    }

    /**
     * Set the current ServerData instance.
     */
    public void setServerData(ServerData serverDataIn)
    {
        this.currentServerData = serverDataIn;
    }

    @Nullable
    public ServerData getCurrentServerData()
    {
        return this.currentServerData;
    }

    public boolean isIntegratedServerRunning()
    {
        return this.integratedServerIsRunning;
    }

    /**
     * Returns true if there is only one player playing, and the current server is the integrated one.
     */
    public boolean isSingleplayer()
    {
        return this.integratedServerIsRunning && this.integratedServer != null;
    }

    @Nullable

    /**
     * Returns the currently running integrated server
     */
    public IntegratedServer getIntegratedServer()
    {
        return this.integratedServer;
    }

    public static void func_71363_D()
    {
        if (instance != null)
        {
            IntegratedServer integratedserver = instance.getIntegratedServer();

            if (integratedserver != null)
            {
                integratedserver.stopServer();
            }
        }
    }

    /**
     * Returns the PlayerUsageSnooper instance.
     */
    public Snooper getSnooper()
    {
        return this.snooper;
    }

    public static long func_71386_F()
    {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    public boolean func_71372_G()
    {
        return this.field_71431_Q;
    }

    public Session getSession()
    {
        return this.session;
    }

    /**
     * Return the player's GameProfile properties
     */
    public PropertyMap getProfileProperties()
    {
        if (this.profileProperties.isEmpty())
        {
            GameProfile gameprofile = this.getSessionService().fillProfileProperties(this.session.getProfile(), false);
            this.profileProperties.putAll(gameprofile.getProperties());
        }

        return this.profileProperties;
    }

    public Proxy getProxy()
    {
        return this.proxy;
    }

    public TextureManager getTextureManager()
    {
        return this.textureManager;
    }

    public IResourceManager func_110442_L()
    {
        return this.resourceManager;
    }

    public ResourcePackRepository func_110438_M()
    {
        return this.resourcePackRepository;
    }

    public LanguageManager getLanguageManager()
    {
        return this.languageManager;
    }

    public TextureMap func_147117_R()
    {
        return this.field_147128_au;
    }

    public boolean isJava64bit()
    {
        return this.jvm64bit;
    }

    public boolean isGamePaused()
    {
        return this.isGamePaused;
    }

    public SoundHandler getSoundHandler()
    {
        return this.soundHandler;
    }

    public MusicTicker.MusicType getAmbientMusicType()
    {
        if (this.currentScreen instanceof GuiWinGame)
        {
            return MusicTicker.MusicType.CREDITS;
        }
        else if (this.player != null)
        {
            if (this.player.world.dimension instanceof WorldProviderHell)
            {
                return MusicTicker.MusicType.NETHER;
            }
            else if (this.player.world.dimension instanceof WorldProviderEnd)
            {
                return this.ingameGUI.getBossOverlay().shouldPlayEndBossMusic() ? MusicTicker.MusicType.END_BOSS : MusicTicker.MusicType.END;
            }
            else
            {
                return this.player.abilities.isCreativeMode && this.player.abilities.allowFlying ? MusicTicker.MusicType.CREATIVE : MusicTicker.MusicType.GAME;
            }
        }
        else
        {
            return MusicTicker.MusicType.MENU;
        }
    }

    public void func_152348_aa()
    {
        int i = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();

        if (i != 0 && !Keyboard.isRepeatEvent())
        {
            if (!(this.currentScreen instanceof GuiControls) || ((GuiControls)this.currentScreen).time <= func_71386_F() - 20L)
            {
                if (Keyboard.getEventKeyState())
                {
                    if (i == this.gameSettings.keyBindFullscreen.func_151463_i())
                    {
                        this.func_71352_k();
                    }
                    else if (i == this.gameSettings.keyBindScreenshot.func_151463_i())
                    {
                        this.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(this.gameDir, this.field_71443_c, this.field_71440_d, this.framebuffer));
                    }
                    else if (i == 48 && GuiScreen.func_146271_m() && (this.currentScreen == null || this.currentScreen != null && !this.currentScreen.func_193976_p()))
                    {
                        this.gameSettings.func_74306_a(GameSettings.Options.NARRATOR, 1);

                        if (this.currentScreen instanceof ScreenChatOptions)
                        {
                            ((ScreenChatOptions)this.currentScreen).updateNarratorButton();
                        }
                    }
                }
            }
        }
    }

    public MinecraftSessionService getSessionService()
    {
        return this.sessionService;
    }

    public SkinManager getSkinManager()
    {
        return this.skinManager;
    }

    @Nullable
    public Entity getRenderViewEntity()
    {
        return this.renderViewEntity;
    }

    public void setRenderViewEntity(Entity viewingEntity)
    {
        this.renderViewEntity = viewingEntity;
        this.gameRenderer.loadEntityShader(viewingEntity);
    }

    public <V> ListenableFuture<V> func_152343_a(Callable<V> p_152343_1_)
    {
        Validate.notNull(p_152343_1_);

        if (this.func_152345_ab())
        {
            try
            {
                return Futures.<V>immediateFuture(p_152343_1_.call());
            }
            catch (Exception exception)
            {
                return Futures.immediateFailedCheckedFuture(exception);
            }
        }
        else
        {
            ListenableFutureTask<V> listenablefuturetask = ListenableFutureTask.<V>create(p_152343_1_);

            synchronized (this.field_152351_aB)
            {
                this.field_152351_aB.add(listenablefuturetask);
                return listenablefuturetask;
            }
        }
    }

    public ListenableFuture<Object> func_152344_a(Runnable p_152344_1_)
    {
        Validate.notNull(p_152344_1_);
        return this.<Object>func_152343_a(Executors.callable(p_152344_1_));
    }

    public boolean func_152345_ab()
    {
        return Thread.currentThread() == this.thread;
    }

    public BlockRendererDispatcher getBlockRendererDispatcher()
    {
        return this.blockRenderDispatcher;
    }

    public RenderManager getRenderManager()
    {
        return this.renderManager;
    }

    public RenderItem getItemRenderer()
    {
        return this.itemRenderer;
    }

    public ItemRenderer getFirstPersonRenderer()
    {
        return this.firstPersonRenderer;
    }

    public <T> ISearchTree<T> func_193987_a(SearchTreeManager.Key<T> p_193987_1_)
    {
        return this.searchTreeManager.<T>func_194010_a(p_193987_1_);
    }

    public static int func_175610_ah()
    {
        return debugFPS;
    }

    /**
     * Return the FrameTimer's instance
     */
    public FrameTimer getFrameTimer()
    {
        return this.frameTimer;
    }

    /**
     * Return true if the player is connected to a realms server
     */
    public boolean isConnectedToRealms()
    {
        return this.connectedToRealms;
    }

    /**
     * Set if the player is connected to a realms server
     */
    public void setConnectedToRealms(boolean isConnected)
    {
        this.connectedToRealms = isConnected;
    }

    public DataFixer getDataFixer()
    {
        return this.dataFixer;
    }

    public float getRenderPartialTicks()
    {
        return this.timer.renderPartialTicks;
    }

    public float getTickLength()
    {
        return this.timer.elapsedPartialTicks;
    }

    public BlockColors getBlockColors()
    {
        return this.blockColors;
    }

    /**
     * Whether to use reduced debug info
     */
    public boolean isReducedDebug()
    {
        return this.player != null && this.player.hasReducedDebug() || this.gameSettings.reducedDebugInfo;
    }

    public GuiToast getToastGui()
    {
        return this.toastGui;
    }

    public Tutorial getTutorial()
    {
        return this.tutorial;
    }
}
