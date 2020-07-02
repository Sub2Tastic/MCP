package net.minecraft.client.settings;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.tutorial.TutorialSteps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GameSettings
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new Gson();
    private static final Type TYPE_LIST_STRING = new ParameterizedType()
    {
        public Type[] getActualTypeArguments()
        {
            return new Type[] {String.class};
        }
        public Type getRawType()
        {
            return List.class;
        }
        public Type getOwnerType()
        {
            return null;
        }
    };
    public static final Splitter field_189990_a = Splitter.on(':');
    private static final String[] field_74367_ae = new String[] {"options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"};
    private static final String[] field_74364_ag = new String[] {"options.particles.all", "options.particles.decreased", "options.particles.minimal"};
    private static final String[] field_98303_au = new String[] {"options.ao.off", "options.ao.min", "options.ao.max"};
    private static final String[] field_181149_aW = new String[] {"options.off", "options.clouds.fast", "options.clouds.fancy"};
    private static final String[] field_186713_aK = new String[] {"options.off", "options.attack.crosshair", "options.attack.hotbar"};
    public static final String[] field_193632_b = new String[] {"options.narrator.off", "options.narrator.all", "options.narrator.chat", "options.narrator.system"};
    public float mouseSensitivity = 0.5F;
    public boolean invertMouse;
    public int renderDistanceChunks = -1;
    public boolean viewBobbing = true;
    public boolean field_74337_g;
    public boolean field_151448_g = true;
    public int framerateLimit = 120;
    public int cloudOption = 2;
    public boolean fancyGraphics = true;
    public int ambientOcclusionStatus = 2;
    public List<String> resourcePacks = Lists.<String>newArrayList();
    public List<String> incompatibleResourcePacks = Lists.<String>newArrayList();
    public EntityPlayer.EnumChatVisibility chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
    public boolean chatColor = true;
    public boolean chatLinks = true;
    public boolean chatLinksPrompt = true;
    public float chatOpacity = 1.0F;
    public boolean snooper = true;
    public boolean fullscreen;
    public boolean vsync = true;
    public boolean field_178881_t = true;
    public boolean reducedDebugInfo;
    public boolean hideServerAddress;
    public boolean advancedItemTooltips;
    public boolean pauseOnLostFocus = true;
    private final Set<EnumPlayerModelParts> setModelParts = Sets.newHashSet(EnumPlayerModelParts.values());
    public boolean touchscreen;
    public EnumHandSide mainHand = EnumHandSide.RIGHT;
    public int overrideWidth;
    public int overrideHeight;
    public boolean heldItemTooltips = true;
    public float chatScale = 1.0F;
    public float chatWidth = 1.0F;
    public float chatHeightUnfocused = 0.44366196F;
    public float chatHeightFocused = 1.0F;
    public int mipmapLevels = 4;
    private final Map<SoundCategory, Float> soundLevels = Maps.newEnumMap(SoundCategory.class);
    public boolean useNativeTransport = true;
    public boolean entityShadows = true;
    public int attackIndicator = 1;
    public boolean field_189422_N;
    public boolean showSubtitles;
    public boolean realmsNotifications = true;
    public boolean autoJump = true;
    public TutorialSteps tutorialStep = TutorialSteps.MOVEMENT;
    public KeyBinding keyBindForward = new KeyBinding("key.forward", 17, "key.categories.movement");
    public KeyBinding keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
    public KeyBinding keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
    public KeyBinding keyBindRight = new KeyBinding("key.right", 32, "key.categories.movement");
    public KeyBinding keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
    public KeyBinding field_74311_E = new KeyBinding("key.sneak", 42, "key.categories.movement");
    public KeyBinding keyBindSprint = new KeyBinding("key.sprint", 29, "key.categories.movement");
    public KeyBinding keyBindInventory = new KeyBinding("key.inventory", 18, "key.categories.inventory");
    public KeyBinding keyBindSwapHands = new KeyBinding("key.swapHands", 33, "key.categories.inventory");
    public KeyBinding keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.inventory");
    public KeyBinding keyBindUseItem = new KeyBinding("key.use", -99, "key.categories.gameplay");
    public KeyBinding keyBindAttack = new KeyBinding("key.attack", -100, "key.categories.gameplay");
    public KeyBinding keyBindPickBlock = new KeyBinding("key.pickItem", -98, "key.categories.gameplay");
    public KeyBinding keyBindChat = new KeyBinding("key.chat", 20, "key.categories.multiplayer");
    public KeyBinding keyBindPlayerList = new KeyBinding("key.playerlist", 15, "key.categories.multiplayer");
    public KeyBinding keyBindCommand = new KeyBinding("key.command", 53, "key.categories.multiplayer");
    public KeyBinding keyBindScreenshot = new KeyBinding("key.screenshot", 60, "key.categories.misc");
    public KeyBinding keyBindTogglePerspective = new KeyBinding("key.togglePerspective", 63, "key.categories.misc");
    public KeyBinding keyBindSmoothCamera = new KeyBinding("key.smoothCamera", 0, "key.categories.misc");
    public KeyBinding keyBindFullscreen = new KeyBinding("key.fullscreen", 87, "key.categories.misc");
    public KeyBinding keyBindSpectatorOutlines = new KeyBinding("key.spectatorOutlines", 0, "key.categories.misc");
    public KeyBinding keyBindAdvancements = new KeyBinding("key.advancements", 38, "key.categories.misc");
    public KeyBinding[] keyBindsHotbar = new KeyBinding[] {new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory")};
    public KeyBinding keyBindSaveToolbar = new KeyBinding("key.saveToolbarActivator", 46, "key.categories.creative");
    public KeyBinding keyBindLoadToolbar = new KeyBinding("key.loadToolbarActivator", 45, "key.categories.creative");
    public KeyBinding[] keyBindings;
    protected Minecraft mc;
    private File optionsFile;
    public EnumDifficulty difficulty;
    public boolean hideGUI;
    public int thirdPersonView;
    public boolean showDebugInfo;
    public boolean showDebugProfilerChart;
    public boolean showLagometer;
    public String lastServer;
    public boolean smoothCamera;
    public boolean field_74325_U;
    public float fov;
    public float gamma;
    public float field_151452_as;
    public int guiScale;
    public int particles;
    public int narrator;
    public String language;
    public boolean field_151455_aw;

    public GameSettings(Minecraft mcIn, File mcDataDir)
    {
        this.keyBindings = (KeyBinding[])ArrayUtils.addAll(new KeyBinding[] {this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.field_74311_E, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindFullscreen, this.keyBindSpectatorOutlines, this.keyBindSwapHands, this.keyBindSaveToolbar, this.keyBindLoadToolbar, this.keyBindAdvancements}, this.keyBindsHotbar);
        this.difficulty = EnumDifficulty.NORMAL;
        this.lastServer = "";
        this.fov = 70.0F;
        this.language = "en_us";
        this.mc = mcIn;
        this.optionsFile = new File(mcDataDir, "options.txt");

        if (mcIn.isJava64bit() && Runtime.getRuntime().maxMemory() >= 1000000000L)
        {
            GameSettings.Options.RENDER_DISTANCE.func_148263_a(32.0F);
        }
        else
        {
            GameSettings.Options.RENDER_DISTANCE.func_148263_a(16.0F);
        }

        this.renderDistanceChunks = mcIn.isJava64bit() ? 12 : 8;
        this.loadOptions();
    }

    public GameSettings()
    {
        this.keyBindings = (KeyBinding[])ArrayUtils.addAll(new KeyBinding[] {this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.field_74311_E, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindFullscreen, this.keyBindSpectatorOutlines, this.keyBindSwapHands, this.keyBindSaveToolbar, this.keyBindLoadToolbar, this.keyBindAdvancements}, this.keyBindsHotbar);
        this.difficulty = EnumDifficulty.NORMAL;
        this.lastServer = "";
        this.fov = 70.0F;
        this.language = "en_us";
    }

    public static String func_74298_c(int p_74298_0_)
    {
        if (p_74298_0_ < 0)
        {
            switch (p_74298_0_)
            {
                case -100:
                    return I18n.format("key.mouse.left");

                case -99:
                    return I18n.format("key.mouse.right");

                case -98:
                    return I18n.format("key.mouse.middle");

                default:
                    return I18n.format("key.mouseButton", p_74298_0_ + 101);
            }
        }
        else
        {
            return p_74298_0_ < 256 ? Keyboard.getKeyName(p_74298_0_) : String.format("%c", (char)(p_74298_0_ - 256)).toUpperCase();
        }
    }

    public static boolean func_100015_a(KeyBinding p_100015_0_)
    {
        int i = p_100015_0_.func_151463_i();

        if (i != 0 && i < 256)
        {
            return i < 0 ? Mouse.isButtonDown(i + 100) : Keyboard.isKeyDown(i);
        }
        else
        {
            return false;
        }
    }

    public void func_151440_a(KeyBinding p_151440_1_, int p_151440_2_)
    {
        p_151440_1_.func_151462_b(p_151440_2_);
        this.saveOptions();
    }

    public void func_74304_a(GameSettings.Options p_74304_1_, float p_74304_2_)
    {
        if (p_74304_1_ == GameSettings.Options.SENSITIVITY)
        {
            this.mouseSensitivity = p_74304_2_;
        }

        if (p_74304_1_ == GameSettings.Options.FOV)
        {
            this.fov = p_74304_2_;
        }

        if (p_74304_1_ == GameSettings.Options.GAMMA)
        {
            this.gamma = p_74304_2_;
        }

        if (p_74304_1_ == GameSettings.Options.FRAMERATE_LIMIT)
        {
            this.framerateLimit = (int)p_74304_2_;
        }

        if (p_74304_1_ == GameSettings.Options.CHAT_OPACITY)
        {
            this.chatOpacity = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }

        if (p_74304_1_ == GameSettings.Options.CHAT_HEIGHT_FOCUSED)
        {
            this.chatHeightFocused = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }

        if (p_74304_1_ == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED)
        {
            this.chatHeightUnfocused = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }

        if (p_74304_1_ == GameSettings.Options.CHAT_WIDTH)
        {
            this.chatWidth = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }

        if (p_74304_1_ == GameSettings.Options.CHAT_SCALE)
        {
            this.chatScale = p_74304_2_;
            this.mc.ingameGUI.getChatGUI().refreshChat();
        }

        if (p_74304_1_ == GameSettings.Options.MIPMAP_LEVELS)
        {
            int i = this.mipmapLevels;
            this.mipmapLevels = (int)p_74304_2_;

            if ((float)i != p_74304_2_)
            {
                this.mc.func_147117_R().func_147633_a(this.mipmapLevels);
                this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                this.mc.func_147117_R().setBlurMipmapDirect(false, this.mipmapLevels > 0);
                this.mc.func_175603_A();
            }
        }

        if (p_74304_1_ == GameSettings.Options.RENDER_DISTANCE)
        {
            this.renderDistanceChunks = (int)p_74304_2_;
            this.mc.worldRenderer.setDisplayListEntitiesDirty();
        }
    }

    public void func_74306_a(GameSettings.Options p_74306_1_, int p_74306_2_)
    {
        if (p_74306_1_ == GameSettings.Options.RENDER_DISTANCE)
        {
            this.func_74304_a(p_74306_1_, MathHelper.clamp((float)(this.renderDistanceChunks + p_74306_2_), p_74306_1_.func_186707_e(), p_74306_1_.func_148267_f()));
        }

        if (p_74306_1_ == GameSettings.Options.MAIN_HAND)
        {
            this.mainHand = this.mainHand.opposite();
        }

        if (p_74306_1_ == GameSettings.Options.INVERT_MOUSE)
        {
            this.invertMouse = !this.invertMouse;
        }

        if (p_74306_1_ == GameSettings.Options.GUI_SCALE)
        {
            this.guiScale = this.guiScale + p_74306_2_ & 3;
        }

        if (p_74306_1_ == GameSettings.Options.PARTICLES)
        {
            this.particles = (this.particles + p_74306_2_) % 3;
        }

        if (p_74306_1_ == GameSettings.Options.VIEW_BOBBING)
        {
            this.viewBobbing = !this.viewBobbing;
        }

        if (p_74306_1_ == GameSettings.Options.RENDER_CLOUDS)
        {
            this.cloudOption = (this.cloudOption + p_74306_2_) % 3;
        }

        if (p_74306_1_ == GameSettings.Options.FORCE_UNICODE_FONT)
        {
            this.field_151455_aw = !this.field_151455_aw;
            this.mc.fontRenderer.func_78264_a(this.mc.getLanguageManager().func_135042_a() || this.field_151455_aw);
        }

        if (p_74306_1_ == GameSettings.Options.FBO_ENABLE)
        {
            this.field_151448_g = !this.field_151448_g;
        }

        if (p_74306_1_ == GameSettings.Options.ANAGLYPH)
        {
            this.field_74337_g = !this.field_74337_g;
            this.mc.func_110436_a();
        }

        if (p_74306_1_ == GameSettings.Options.GRAPHICS)
        {
            this.fancyGraphics = !this.fancyGraphics;
            this.mc.worldRenderer.loadRenderers();
        }

        if (p_74306_1_ == GameSettings.Options.AMBIENT_OCCLUSION)
        {
            this.ambientOcclusionStatus = (this.ambientOcclusionStatus + p_74306_2_) % 3;
            this.mc.worldRenderer.loadRenderers();
        }

        if (p_74306_1_ == GameSettings.Options.CHAT_VISIBILITY)
        {
            this.chatVisibility = EntityPlayer.EnumChatVisibility.func_151426_a((this.chatVisibility.func_151428_a() + p_74306_2_) % 3);
        }

        if (p_74306_1_ == GameSettings.Options.CHAT_COLOR)
        {
            this.chatColor = !this.chatColor;
        }

        if (p_74306_1_ == GameSettings.Options.CHAT_LINKS)
        {
            this.chatLinks = !this.chatLinks;
        }

        if (p_74306_1_ == GameSettings.Options.CHAT_LINKS_PROMPT)
        {
            this.chatLinksPrompt = !this.chatLinksPrompt;
        }

        if (p_74306_1_ == GameSettings.Options.SNOOPER_ENABLED)
        {
            this.snooper = !this.snooper;
        }

        if (p_74306_1_ == GameSettings.Options.TOUCHSCREEN)
        {
            this.touchscreen = !this.touchscreen;
        }

        if (p_74306_1_ == GameSettings.Options.USE_FULLSCREEN)
        {
            this.fullscreen = !this.fullscreen;

            if (this.mc.func_71372_G() != this.fullscreen)
            {
                this.mc.func_71352_k();
            }
        }

        if (p_74306_1_ == GameSettings.Options.ENABLE_VSYNC)
        {
            this.vsync = !this.vsync;
            Display.setVSyncEnabled(this.vsync);
        }

        if (p_74306_1_ == GameSettings.Options.USE_VBO)
        {
            this.field_178881_t = !this.field_178881_t;
            this.mc.worldRenderer.loadRenderers();
        }

        if (p_74306_1_ == GameSettings.Options.REDUCED_DEBUG_INFO)
        {
            this.reducedDebugInfo = !this.reducedDebugInfo;
        }

        if (p_74306_1_ == GameSettings.Options.ENTITY_SHADOWS)
        {
            this.entityShadows = !this.entityShadows;
        }

        if (p_74306_1_ == GameSettings.Options.ATTACK_INDICATOR)
        {
            this.attackIndicator = (this.attackIndicator + p_74306_2_) % 3;
        }

        if (p_74306_1_ == GameSettings.Options.SHOW_SUBTITLES)
        {
            this.showSubtitles = !this.showSubtitles;
        }

        if (p_74306_1_ == GameSettings.Options.REALMS_NOTIFICATIONS)
        {
            this.realmsNotifications = !this.realmsNotifications;
        }

        if (p_74306_1_ == GameSettings.Options.AUTO_JUMP)
        {
            this.autoJump = !this.autoJump;
        }

        if (p_74306_1_ == GameSettings.Options.NARRATOR)
        {
            if (NarratorChatListener.INSTANCE.isActive())
            {
                this.narrator = (this.narrator + p_74306_2_) % field_193632_b.length;
            }
            else
            {
                this.narrator = 0;
            }

            NarratorChatListener.INSTANCE.func_193641_a(this.narrator);
        }

        this.saveOptions();
    }

    public float func_74296_a(GameSettings.Options p_74296_1_)
    {
        if (p_74296_1_ == GameSettings.Options.FOV)
        {
            return this.fov;
        }
        else if (p_74296_1_ == GameSettings.Options.GAMMA)
        {
            return this.gamma;
        }
        else if (p_74296_1_ == GameSettings.Options.SATURATION)
        {
            return this.field_151452_as;
        }
        else if (p_74296_1_ == GameSettings.Options.SENSITIVITY)
        {
            return this.mouseSensitivity;
        }
        else if (p_74296_1_ == GameSettings.Options.CHAT_OPACITY)
        {
            return this.chatOpacity;
        }
        else if (p_74296_1_ == GameSettings.Options.CHAT_HEIGHT_FOCUSED)
        {
            return this.chatHeightFocused;
        }
        else if (p_74296_1_ == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED)
        {
            return this.chatHeightUnfocused;
        }
        else if (p_74296_1_ == GameSettings.Options.CHAT_SCALE)
        {
            return this.chatScale;
        }
        else if (p_74296_1_ == GameSettings.Options.CHAT_WIDTH)
        {
            return this.chatWidth;
        }
        else if (p_74296_1_ == GameSettings.Options.FRAMERATE_LIMIT)
        {
            return (float)this.framerateLimit;
        }
        else if (p_74296_1_ == GameSettings.Options.MIPMAP_LEVELS)
        {
            return (float)this.mipmapLevels;
        }
        else
        {
            return p_74296_1_ == GameSettings.Options.RENDER_DISTANCE ? (float)this.renderDistanceChunks : 0.0F;
        }
    }

    public boolean func_74308_b(GameSettings.Options p_74308_1_)
    {
        switch (p_74308_1_)
        {
            case INVERT_MOUSE:
                return this.invertMouse;

            case VIEW_BOBBING:
                return this.viewBobbing;

            case ANAGLYPH:
                return this.field_74337_g;

            case FBO_ENABLE:
                return this.field_151448_g;

            case CHAT_COLOR:
                return this.chatColor;

            case CHAT_LINKS:
                return this.chatLinks;

            case CHAT_LINKS_PROMPT:
                return this.chatLinksPrompt;

            case SNOOPER_ENABLED:
                return this.snooper;

            case USE_FULLSCREEN:
                return this.fullscreen;

            case ENABLE_VSYNC:
                return this.vsync;

            case USE_VBO:
                return this.field_178881_t;

            case TOUCHSCREEN:
                return this.touchscreen;

            case FORCE_UNICODE_FONT:
                return this.field_151455_aw;

            case REDUCED_DEBUG_INFO:
                return this.reducedDebugInfo;

            case ENTITY_SHADOWS:
                return this.entityShadows;

            case SHOW_SUBTITLES:
                return this.showSubtitles;

            case REALMS_NOTIFICATIONS:
                return this.realmsNotifications;

            case ENABLE_WEAK_ATTACKS:
                return this.field_189422_N;

            case AUTO_JUMP:
                return this.autoJump;

            default:
                return false;
        }
    }

    private static String func_74299_a(String[] p_74299_0_, int p_74299_1_)
    {
        if (p_74299_1_ < 0 || p_74299_1_ >= p_74299_0_.length)
        {
            p_74299_1_ = 0;
        }

        return I18n.format(p_74299_0_[p_74299_1_]);
    }

    public String func_74297_c(GameSettings.Options p_74297_1_)
    {
        String s = I18n.format(p_74297_1_.func_74378_d()) + ": ";

        if (p_74297_1_.func_74380_a())
        {
            float f1 = this.func_74296_a(p_74297_1_);
            float f = p_74297_1_.func_148266_c(f1);

            if (p_74297_1_ == GameSettings.Options.SENSITIVITY)
            {
                if (f == 0.0F)
                {
                    return s + I18n.format("options.sensitivity.min");
                }
                else
                {
                    return f == 1.0F ? s + I18n.format("options.sensitivity.max") : s + (int)(f * 200.0F) + "%";
                }
            }
            else if (p_74297_1_ == GameSettings.Options.FOV)
            {
                if (f1 == 70.0F)
                {
                    return s + I18n.format("options.fov.min");
                }
                else
                {
                    return f1 == 110.0F ? s + I18n.format("options.fov.max") : s + (int)f1;
                }
            }
            else if (p_74297_1_ == GameSettings.Options.FRAMERATE_LIMIT)
            {
                return f1 == p_74297_1_.field_148272_O ? s + I18n.format("options.framerateLimit.max") : s + I18n.format("options.framerate", (int)f1);
            }
            else if (p_74297_1_ == GameSettings.Options.RENDER_CLOUDS)
            {
                return f1 == p_74297_1_.field_148271_N ? s + I18n.format("options.cloudHeight.min") : s + ((int)f1 + 128);
            }
            else if (p_74297_1_ == GameSettings.Options.GAMMA)
            {
                if (f == 0.0F)
                {
                    return s + I18n.format("options.gamma.min");
                }
                else
                {
                    return f == 1.0F ? s + I18n.format("options.gamma.max") : s + "+" + (int)(f * 100.0F) + "%";
                }
            }
            else if (p_74297_1_ == GameSettings.Options.SATURATION)
            {
                return s + (int)(f * 400.0F) + "%";
            }
            else if (p_74297_1_ == GameSettings.Options.CHAT_OPACITY)
            {
                return s + (int)(f * 90.0F + 10.0F) + "%";
            }
            else if (p_74297_1_ == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED)
            {
                return s + GuiNewChat.func_146243_b(f) + "px";
            }
            else if (p_74297_1_ == GameSettings.Options.CHAT_HEIGHT_FOCUSED)
            {
                return s + GuiNewChat.func_146243_b(f) + "px";
            }
            else if (p_74297_1_ == GameSettings.Options.CHAT_WIDTH)
            {
                return s + GuiNewChat.func_146233_a(f) + "px";
            }
            else if (p_74297_1_ == GameSettings.Options.RENDER_DISTANCE)
            {
                return s + I18n.format("options.chunks", (int)f1);
            }
            else if (p_74297_1_ == GameSettings.Options.MIPMAP_LEVELS)
            {
                return f1 == 0.0F ? s + I18n.format("options.off") : s + (int)f1;
            }
            else
            {
                return f == 0.0F ? s + I18n.format("options.off") : s + (int)(f * 100.0F) + "%";
            }
        }
        else if (p_74297_1_.func_74382_b())
        {
            boolean flag = this.func_74308_b(p_74297_1_);
            return flag ? s + I18n.format("options.on") : s + I18n.format("options.off");
        }
        else if (p_74297_1_ == GameSettings.Options.MAIN_HAND)
        {
            return s + this.mainHand;
        }
        else if (p_74297_1_ == GameSettings.Options.GUI_SCALE)
        {
            return s + func_74299_a(field_74367_ae, this.guiScale);
        }
        else if (p_74297_1_ == GameSettings.Options.CHAT_VISIBILITY)
        {
            return s + I18n.format(this.chatVisibility.func_151429_b());
        }
        else if (p_74297_1_ == GameSettings.Options.PARTICLES)
        {
            return s + func_74299_a(field_74364_ag, this.particles);
        }
        else if (p_74297_1_ == GameSettings.Options.AMBIENT_OCCLUSION)
        {
            return s + func_74299_a(field_98303_au, this.ambientOcclusionStatus);
        }
        else if (p_74297_1_ == GameSettings.Options.RENDER_CLOUDS)
        {
            return s + func_74299_a(field_181149_aW, this.cloudOption);
        }
        else if (p_74297_1_ == GameSettings.Options.GRAPHICS)
        {
            if (this.fancyGraphics)
            {
                return s + I18n.format("options.graphics.fancy");
            }
            else
            {
                String s1 = "options.graphics.fast";
                return s + I18n.format("options.graphics.fast");
            }
        }
        else if (p_74297_1_ == GameSettings.Options.ATTACK_INDICATOR)
        {
            return s + func_74299_a(field_186713_aK, this.attackIndicator);
        }
        else if (p_74297_1_ == GameSettings.Options.NARRATOR)
        {
            return NarratorChatListener.INSTANCE.isActive() ? s + func_74299_a(field_193632_b, this.narrator) : s + I18n.format("options.narrator.notavailable");
        }
        else
        {
            return s;
        }
    }

    /**
     * Loads the options from the options file. It appears that this has replaced the previous 'loadOptions'
     */
    public void loadOptions()
    {
        try
        {
            if (!this.optionsFile.exists())
            {
                return;
            }

            this.soundLevels.clear();
            List<String> list = IOUtils.readLines(new FileInputStream(this.optionsFile));
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            for (String s : list)
            {
                try
                {
                    Iterator<String> iterator = field_189990_a.omitEmptyStrings().limit(2).split(s).iterator();
                    nbttagcompound.putString(iterator.next(), iterator.next());
                }
                catch (Exception var10)
                {
                    LOGGER.warn("Skipping bad option: {}", (Object)s);
                }
            }

            nbttagcompound = this.dataFix(nbttagcompound);

            for (String s1 : nbttagcompound.keySet())
            {
                String s2 = nbttagcompound.getString(s1);

                try
                {
                    if ("mouseSensitivity".equals(s1))
                    {
                        this.mouseSensitivity = this.parseFloat(s2);
                    }

                    if ("fov".equals(s1))
                    {
                        this.fov = this.parseFloat(s2) * 40.0F + 70.0F;
                    }

                    if ("gamma".equals(s1))
                    {
                        this.gamma = this.parseFloat(s2);
                    }

                    if ("saturation".equals(s1))
                    {
                        this.field_151452_as = this.parseFloat(s2);
                    }

                    if ("invertYMouse".equals(s1))
                    {
                        this.invertMouse = "true".equals(s2);
                    }

                    if ("renderDistance".equals(s1))
                    {
                        this.renderDistanceChunks = Integer.parseInt(s2);
                    }

                    if ("guiScale".equals(s1))
                    {
                        this.guiScale = Integer.parseInt(s2);
                    }

                    if ("particles".equals(s1))
                    {
                        this.particles = Integer.parseInt(s2);
                    }

                    if ("bobView".equals(s1))
                    {
                        this.viewBobbing = "true".equals(s2);
                    }

                    if ("anaglyph3d".equals(s1))
                    {
                        this.field_74337_g = "true".equals(s2);
                    }

                    if ("maxFps".equals(s1))
                    {
                        this.framerateLimit = Integer.parseInt(s2);
                    }

                    if ("fboEnable".equals(s1))
                    {
                        this.field_151448_g = "true".equals(s2);
                    }

                    if ("difficulty".equals(s1))
                    {
                        this.difficulty = EnumDifficulty.byId(Integer.parseInt(s2));
                    }

                    if ("fancyGraphics".equals(s1))
                    {
                        this.fancyGraphics = "true".equals(s2);
                    }

                    if ("tutorialStep".equals(s1))
                    {
                        this.tutorialStep = TutorialSteps.byName(s2);
                    }

                    if ("ao".equals(s1))
                    {
                        if ("true".equals(s2))
                        {
                            this.ambientOcclusionStatus = 2;
                        }
                        else if ("false".equals(s2))
                        {
                            this.ambientOcclusionStatus = 0;
                        }
                        else
                        {
                            this.ambientOcclusionStatus = Integer.parseInt(s2);
                        }
                    }

                    if ("renderClouds".equals(s1))
                    {
                        if ("true".equals(s2))
                        {
                            this.cloudOption = 2;
                        }
                        else if ("false".equals(s2))
                        {
                            this.cloudOption = 0;
                        }
                        else if ("fast".equals(s2))
                        {
                            this.cloudOption = 1;
                        }
                    }

                    if ("attackIndicator".equals(s1))
                    {
                        if ("0".equals(s2))
                        {
                            this.attackIndicator = 0;
                        }
                        else if ("1".equals(s2))
                        {
                            this.attackIndicator = 1;
                        }
                        else if ("2".equals(s2))
                        {
                            this.attackIndicator = 2;
                        }
                    }

                    if ("resourcePacks".equals(s1))
                    {
                        this.resourcePacks = (List)JsonUtils.fromJson(GSON, s2, TYPE_LIST_STRING);

                        if (this.resourcePacks == null)
                        {
                            this.resourcePacks = Lists.<String>newArrayList();
                        }
                    }

                    if ("incompatibleResourcePacks".equals(s1))
                    {
                        this.incompatibleResourcePacks = (List)JsonUtils.fromJson(GSON, s2, TYPE_LIST_STRING);

                        if (this.incompatibleResourcePacks == null)
                        {
                            this.incompatibleResourcePacks = Lists.<String>newArrayList();
                        }
                    }

                    if ("lastServer".equals(s1))
                    {
                        this.lastServer = s2;
                    }

                    if ("lang".equals(s1))
                    {
                        this.language = s2;
                    }

                    if ("chatVisibility".equals(s1))
                    {
                        this.chatVisibility = EntityPlayer.EnumChatVisibility.func_151426_a(Integer.parseInt(s2));
                    }

                    if ("chatColors".equals(s1))
                    {
                        this.chatColor = "true".equals(s2);
                    }

                    if ("chatLinks".equals(s1))
                    {
                        this.chatLinks = "true".equals(s2);
                    }

                    if ("chatLinksPrompt".equals(s1))
                    {
                        this.chatLinksPrompt = "true".equals(s2);
                    }

                    if ("chatOpacity".equals(s1))
                    {
                        this.chatOpacity = this.parseFloat(s2);
                    }

                    if ("snooperEnabled".equals(s1))
                    {
                        this.snooper = "true".equals(s2);
                    }

                    if ("fullscreen".equals(s1))
                    {
                        this.fullscreen = "true".equals(s2);
                    }

                    if ("enableVsync".equals(s1))
                    {
                        this.vsync = "true".equals(s2);
                    }

                    if ("useVbo".equals(s1))
                    {
                        this.field_178881_t = "true".equals(s2);
                    }

                    if ("hideServerAddress".equals(s1))
                    {
                        this.hideServerAddress = "true".equals(s2);
                    }

                    if ("advancedItemTooltips".equals(s1))
                    {
                        this.advancedItemTooltips = "true".equals(s2);
                    }

                    if ("pauseOnLostFocus".equals(s1))
                    {
                        this.pauseOnLostFocus = "true".equals(s2);
                    }

                    if ("touchscreen".equals(s1))
                    {
                        this.touchscreen = "true".equals(s2);
                    }

                    if ("overrideHeight".equals(s1))
                    {
                        this.overrideHeight = Integer.parseInt(s2);
                    }

                    if ("overrideWidth".equals(s1))
                    {
                        this.overrideWidth = Integer.parseInt(s2);
                    }

                    if ("heldItemTooltips".equals(s1))
                    {
                        this.heldItemTooltips = "true".equals(s2);
                    }

                    if ("chatHeightFocused".equals(s1))
                    {
                        this.chatHeightFocused = this.parseFloat(s2);
                    }

                    if ("chatHeightUnfocused".equals(s1))
                    {
                        this.chatHeightUnfocused = this.parseFloat(s2);
                    }

                    if ("chatScale".equals(s1))
                    {
                        this.chatScale = this.parseFloat(s2);
                    }

                    if ("chatWidth".equals(s1))
                    {
                        this.chatWidth = this.parseFloat(s2);
                    }

                    if ("mipmapLevels".equals(s1))
                    {
                        this.mipmapLevels = Integer.parseInt(s2);
                    }

                    if ("forceUnicodeFont".equals(s1))
                    {
                        this.field_151455_aw = "true".equals(s2);
                    }

                    if ("reducedDebugInfo".equals(s1))
                    {
                        this.reducedDebugInfo = "true".equals(s2);
                    }

                    if ("useNativeTransport".equals(s1))
                    {
                        this.useNativeTransport = "true".equals(s2);
                    }

                    if ("entityShadows".equals(s1))
                    {
                        this.entityShadows = "true".equals(s2);
                    }

                    if ("mainHand".equals(s1))
                    {
                        this.mainHand = "left".equals(s2) ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
                    }

                    if ("showSubtitles".equals(s1))
                    {
                        this.showSubtitles = "true".equals(s2);
                    }

                    if ("realmsNotifications".equals(s1))
                    {
                        this.realmsNotifications = "true".equals(s2);
                    }

                    if ("enableWeakAttacks".equals(s1))
                    {
                        this.field_189422_N = "true".equals(s2);
                    }

                    if ("autoJump".equals(s1))
                    {
                        this.autoJump = "true".equals(s2);
                    }

                    if ("narrator".equals(s1))
                    {
                        this.narrator = Integer.parseInt(s2);
                    }

                    for (KeyBinding keybinding : this.keyBindings)
                    {
                        if (s1.equals("key_" + keybinding.getKeyDescription()))
                        {
                            keybinding.func_151462_b(Integer.parseInt(s2));
                        }
                    }

                    for (SoundCategory soundcategory : SoundCategory.values())
                    {
                        if (s1.equals("soundCategory_" + soundcategory.getName()))
                        {
                            this.soundLevels.put(soundcategory, Float.valueOf(this.parseFloat(s2)));
                        }
                    }

                    for (EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values())
                    {
                        if (s1.equals("modelPart_" + enumplayermodelparts.getPartName()))
                        {
                            this.setModelPartEnabled(enumplayermodelparts, "true".equals(s2));
                        }
                    }
                }
                catch (Exception var11)
                {
                    LOGGER.warn("Skipping bad option: {}:{}", s1, s2);
                }
            }

            KeyBinding.resetKeyBindingArrayAndHash();
        }
        catch (Exception exception)
        {
            LOGGER.error("Failed to load options", (Throwable)exception);
        }
    }

    private NBTTagCompound dataFix(NBTTagCompound nbt)
    {
        int i = 0;

        try
        {
            i = Integer.parseInt(nbt.getString("version"));
        }
        catch (RuntimeException var4)
        {
            ;
        }

        return this.mc.getDataFixer().func_188251_a(FixTypes.OPTIONS, nbt, i);
    }

    /**
     * Parses a string into a float.
     */
    private float parseFloat(String p_74305_1_)
    {
        if ("true".equals(p_74305_1_))
        {
            return 1.0F;
        }
        else
        {
            return "false".equals(p_74305_1_) ? 0.0F : Float.parseFloat(p_74305_1_);
        }
    }

    /**
     * Saves the options to the options file.
     */
    public void saveOptions()
    {
        PrintWriter printwriter = null;

        try
        {
            printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFile), StandardCharsets.UTF_8));
            printwriter.println("version:1343");
            printwriter.println("invertYMouse:" + this.invertMouse);
            printwriter.println("mouseSensitivity:" + this.mouseSensitivity);
            printwriter.println("fov:" + (this.fov - 70.0F) / 40.0F);
            printwriter.println("gamma:" + this.gamma);
            printwriter.println("saturation:" + this.field_151452_as);
            printwriter.println("renderDistance:" + this.renderDistanceChunks);
            printwriter.println("guiScale:" + this.guiScale);
            printwriter.println("particles:" + this.particles);
            printwriter.println("bobView:" + this.viewBobbing);
            printwriter.println("anaglyph3d:" + this.field_74337_g);
            printwriter.println("maxFps:" + this.framerateLimit);
            printwriter.println("fboEnable:" + this.field_151448_g);
            printwriter.println("difficulty:" + this.difficulty.getId());
            printwriter.println("fancyGraphics:" + this.fancyGraphics);
            printwriter.println("ao:" + this.ambientOcclusionStatus);

            switch (this.cloudOption)
            {
                case 0:
                    printwriter.println("renderClouds:false");
                    break;

                case 1:
                    printwriter.println("renderClouds:fast");
                    break;

                case 2:
                    printwriter.println("renderClouds:true");
            }

            printwriter.println("resourcePacks:" + GSON.toJson(this.resourcePacks));
            printwriter.println("incompatibleResourcePacks:" + GSON.toJson(this.incompatibleResourcePacks));
            printwriter.println("lastServer:" + this.lastServer);
            printwriter.println("lang:" + this.language);
            printwriter.println("chatVisibility:" + this.chatVisibility.func_151428_a());
            printwriter.println("chatColors:" + this.chatColor);
            printwriter.println("chatLinks:" + this.chatLinks);
            printwriter.println("chatLinksPrompt:" + this.chatLinksPrompt);
            printwriter.println("chatOpacity:" + this.chatOpacity);
            printwriter.println("snooperEnabled:" + this.snooper);
            printwriter.println("fullscreen:" + this.fullscreen);
            printwriter.println("enableVsync:" + this.vsync);
            printwriter.println("useVbo:" + this.field_178881_t);
            printwriter.println("hideServerAddress:" + this.hideServerAddress);
            printwriter.println("advancedItemTooltips:" + this.advancedItemTooltips);
            printwriter.println("pauseOnLostFocus:" + this.pauseOnLostFocus);
            printwriter.println("touchscreen:" + this.touchscreen);
            printwriter.println("overrideWidth:" + this.overrideWidth);
            printwriter.println("overrideHeight:" + this.overrideHeight);
            printwriter.println("heldItemTooltips:" + this.heldItemTooltips);
            printwriter.println("chatHeightFocused:" + this.chatHeightFocused);
            printwriter.println("chatHeightUnfocused:" + this.chatHeightUnfocused);
            printwriter.println("chatScale:" + this.chatScale);
            printwriter.println("chatWidth:" + this.chatWidth);
            printwriter.println("mipmapLevels:" + this.mipmapLevels);
            printwriter.println("forceUnicodeFont:" + this.field_151455_aw);
            printwriter.println("reducedDebugInfo:" + this.reducedDebugInfo);
            printwriter.println("useNativeTransport:" + this.useNativeTransport);
            printwriter.println("entityShadows:" + this.entityShadows);
            printwriter.println("mainHand:" + (this.mainHand == EnumHandSide.LEFT ? "left" : "right"));
            printwriter.println("attackIndicator:" + this.attackIndicator);
            printwriter.println("showSubtitles:" + this.showSubtitles);
            printwriter.println("realmsNotifications:" + this.realmsNotifications);
            printwriter.println("enableWeakAttacks:" + this.field_189422_N);
            printwriter.println("autoJump:" + this.autoJump);
            printwriter.println("narrator:" + this.narrator);
            printwriter.println("tutorialStep:" + this.tutorialStep.getName());

            for (KeyBinding keybinding : this.keyBindings)
            {
                printwriter.println("key_" + keybinding.getKeyDescription() + ":" + keybinding.func_151463_i());
            }

            for (SoundCategory soundcategory : SoundCategory.values())
            {
                printwriter.println("soundCategory_" + soundcategory.getName() + ":" + this.getSoundLevel(soundcategory));
            }

            for (EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values())
            {
                printwriter.println("modelPart_" + enumplayermodelparts.getPartName() + ":" + this.setModelParts.contains(enumplayermodelparts));
            }
        }
        catch (Exception exception)
        {
            LOGGER.error("Failed to save options", (Throwable)exception);
        }
        finally
        {
            IOUtils.closeQuietly((Writer)printwriter);
        }

        this.sendSettingsToServer();
    }

    public float getSoundLevel(SoundCategory category)
    {
        return this.soundLevels.containsKey(category) ? ((Float)this.soundLevels.get(category)).floatValue() : 1.0F;
    }

    public void setSoundLevel(SoundCategory category, float volume)
    {
        this.mc.getSoundHandler().setSoundLevel(category, volume);
        this.soundLevels.put(category, Float.valueOf(volume));
    }

    /**
     * Send a client info packet with settings information to the server
     */
    public void sendSettingsToServer()
    {
        if (this.mc.player != null)
        {
            int i = 0;

            for (EnumPlayerModelParts enumplayermodelparts : this.setModelParts)
            {
                i |= enumplayermodelparts.getPartMask();
            }

            this.mc.player.connection.sendPacket(new CPacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColor, i, this.mainHand));
        }
    }

    public Set<EnumPlayerModelParts> getModelParts()
    {
        return ImmutableSet.copyOf(this.setModelParts);
    }

    public void setModelPartEnabled(EnumPlayerModelParts modelPart, boolean enable)
    {
        if (enable)
        {
            this.setModelParts.add(modelPart);
        }
        else
        {
            this.setModelParts.remove(modelPart);
        }

        this.sendSettingsToServer();
    }

    public void switchModelPartEnabled(EnumPlayerModelParts modelPart)
    {
        if (this.getModelParts().contains(modelPart))
        {
            this.setModelParts.remove(modelPart);
        }
        else
        {
            this.setModelParts.add(modelPart);
        }

        this.sendSettingsToServer();
    }

    public int func_181147_e()
    {
        return this.renderDistanceChunks >= 4 ? this.cloudOption : 0;
    }

    /**
     * Return true if the client connect to a server using the native transport system
     */
    public boolean isUsingNativeTransport()
    {
        return this.useNativeTransport;
    }

    public static enum Options
    {
        INVERT_MOUSE("options.invertMouse", false, true),
        SENSITIVITY("options.sensitivity", true, false),
        FOV("options.fov", true, false, 30.0F, 110.0F, 1.0F),
        GAMMA("options.gamma", true, false),
        SATURATION("options.saturation", true, false),
        RENDER_DISTANCE("options.renderDistance", true, false, 2.0F, 16.0F, 1.0F),
        VIEW_BOBBING("options.viewBobbing", false, true),
        ANAGLYPH("options.anaglyph", false, true),
        FRAMERATE_LIMIT("options.framerateLimit", true, false, 10.0F, 260.0F, 10.0F),
        FBO_ENABLE("options.fboEnable", false, true),
        RENDER_CLOUDS("options.renderClouds", false, false),
        GRAPHICS("options.graphics", false, false),
        AMBIENT_OCCLUSION("options.ao", false, false),
        GUI_SCALE("options.guiScale", false, false),
        PARTICLES("options.particles", false, false),
        CHAT_VISIBILITY("options.chat.visibility", false, false),
        CHAT_COLOR("options.chat.color", false, true),
        CHAT_LINKS("options.chat.links", false, true),
        CHAT_OPACITY("options.chat.opacity", true, false),
        CHAT_LINKS_PROMPT("options.chat.links.prompt", false, true),
        SNOOPER_ENABLED("options.snooper", false, true),
        USE_FULLSCREEN("options.fullscreen", false, true),
        ENABLE_VSYNC("options.vsync", false, true),
        USE_VBO("options.vbo", false, true),
        TOUCHSCREEN("options.touchscreen", false, true),
        CHAT_SCALE("options.chat.scale", true, false),
        CHAT_WIDTH("options.chat.width", true, false),
        CHAT_HEIGHT_FOCUSED("options.chat.height.focused", true, false),
        CHAT_HEIGHT_UNFOCUSED("options.chat.height.unfocused", true, false),
        MIPMAP_LEVELS("options.mipmapLevels", true, false, 0.0F, 4.0F, 1.0F),
        FORCE_UNICODE_FONT("options.forceUnicodeFont", false, true),
        REDUCED_DEBUG_INFO("options.reducedDebugInfo", false, true),
        ENTITY_SHADOWS("options.entityShadows", false, true),
        MAIN_HAND("options.mainHand", false, false),
        ATTACK_INDICATOR("options.attackIndicator", false, false),
        ENABLE_WEAK_ATTACKS("options.enableWeakAttacks", false, true),
        SHOW_SUBTITLES("options.showSubtitles", false, true),
        REALMS_NOTIFICATIONS("options.realmsNotifications", false, true),
        AUTO_JUMP("options.autoJump", false, true),
        NARRATOR("options.narrator", false, false);

        private final boolean field_74385_A;
        private final boolean field_74386_B;
        private final String field_74387_C;
        private final float field_148270_M;
        private float field_148271_N;
        private float field_148272_O;

        public static GameSettings.Options func_74379_a(int p_74379_0_)
        {
            for (GameSettings.Options gamesettings$options : values())
            {
                if (gamesettings$options.func_74381_c() == p_74379_0_)
                {
                    return gamesettings$options;
                }
            }

            return null;
        }

        private Options(String p_i1015_3_, boolean p_i1015_4_, boolean p_i1015_5_)
        {
            this(p_i1015_3_, p_i1015_4_, p_i1015_5_, 0.0F, 1.0F, 0.0F);
        }

        private Options(String p_i45004_3_, boolean p_i45004_4_, boolean p_i45004_5_, float p_i45004_6_, float p_i45004_7_, float p_i45004_8_)
        {
            this.field_74387_C = p_i45004_3_;
            this.field_74385_A = p_i45004_4_;
            this.field_74386_B = p_i45004_5_;
            this.field_148271_N = p_i45004_6_;
            this.field_148272_O = p_i45004_7_;
            this.field_148270_M = p_i45004_8_;
        }

        public boolean func_74380_a()
        {
            return this.field_74385_A;
        }

        public boolean func_74382_b()
        {
            return this.field_74386_B;
        }

        public int func_74381_c()
        {
            return this.ordinal();
        }

        public String func_74378_d()
        {
            return this.field_74387_C;
        }

        public float func_186707_e()
        {
            return this.field_148271_N;
        }

        public float func_148267_f()
        {
            return this.field_148272_O;
        }

        public void func_148263_a(float p_148263_1_)
        {
            this.field_148272_O = p_148263_1_;
        }

        public float func_148266_c(float p_148266_1_)
        {
            return MathHelper.clamp((this.func_148268_e(p_148266_1_) - this.field_148271_N) / (this.field_148272_O - this.field_148271_N), 0.0F, 1.0F);
        }

        public float func_148262_d(float p_148262_1_)
        {
            return this.func_148268_e(this.field_148271_N + (this.field_148272_O - this.field_148271_N) * MathHelper.clamp(p_148262_1_, 0.0F, 1.0F));
        }

        public float func_148268_e(float p_148268_1_)
        {
            p_148268_1_ = this.func_148264_f(p_148268_1_);
            return MathHelper.clamp(p_148268_1_, this.field_148271_N, this.field_148272_O);
        }

        private float func_148264_f(float p_148264_1_)
        {
            if (this.field_148270_M > 0.0F)
            {
                p_148264_1_ = this.field_148270_M * (float)Math.round(p_148264_1_ / this.field_148270_M);
            }

            return p_148264_1_;
        }
    }
}
