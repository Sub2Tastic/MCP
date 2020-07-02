package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Runnables;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServerDemo;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

public class GuiMainMenu extends GuiScreen
{
    private static final Logger field_146974_g = LogManager.getLogger();
    private static final Random field_175374_h = new Random();
    private final float field_73974_b;
    private String splashText;
    private GuiButton buttonResetDemo;
    private float field_73979_m;
    private DynamicTexture field_73977_n;
    private final Object field_104025_t = new Object();
    public static final String field_96138_a = "Please click " + TextFormatting.UNDERLINE + "here" + TextFormatting.RESET + " for more information.";
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private String field_92025_p;
    private String field_146972_A;
    private String field_104024_v;
    private static final ResourceLocation field_110353_x = new ResourceLocation("texts/splashes.txt");
    private static final ResourceLocation MINECRAFT_TITLE_TEXTURES = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation MINECRAFT_TITLE_EDITION = new ResourceLocation("textures/gui/title/edition.png");
    private static final ResourceLocation[] field_73978_o = new ResourceLocation[] {new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
    private ResourceLocation field_110351_G;
    private GuiButton field_175372_K;

    /** Has the check for a realms notification screen been performed? */
    private boolean hasCheckedForRealmsNotification;

    /**
     * A screen generated by realms for notifications; drawn in adition to the main menu (buttons and such from both are
     * drawn at the same time). May be null.
     */
    private GuiScreen realmsNotification;
    private int widthCopyright;
    private int widthCopyrightRest;

    public GuiMainMenu()
    {
        this.field_146972_A = field_96138_a;
        this.splashText = "missingno";
        IResource iresource = null;

        try
        {
            List<String> list = Lists.<String>newArrayList();
            iresource = Minecraft.getInstance().func_110442_L().func_110536_a(field_110353_x);
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(iresource.func_110527_b(), StandardCharsets.UTF_8));
            String s;

            while ((s = bufferedreader.readLine()) != null)
            {
                s = s.trim();

                if (!s.isEmpty())
                {
                    list.add(s);
                }
            }

            if (!list.isEmpty())
            {
                while (true)
                {
                    this.splashText = list.get(field_175374_h.nextInt(list.size()));

                    if (this.splashText.hashCode() != 125780783)
                    {
                        break;
                    }
                }
            }
        }
        catch (IOException var8)
        {
            ;
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)iresource);
        }

        this.field_73974_b = field_175374_h.nextFloat();
        this.field_92025_p = "";

        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.func_153193_b())
        {
            this.field_92025_p = I18n.format("title.oldgl1");
            this.field_146972_A = I18n.format("title.oldgl2");
            this.field_104024_v = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }

    /**
     * Is there currently a realms notification screen, and are realms notifications enabled?
     */
    private boolean areRealmsNotificationsEnabled()
    {
        return Minecraft.getInstance().gameSettings.func_74308_b(GameSettings.Options.REALMS_NOTIFICATIONS) && this.realmsNotification != null;
    }

    public void func_73876_c()
    {
        if (this.areRealmsNotificationsEnabled())
        {
            this.realmsNotification.func_73876_c();
        }
    }

    public boolean func_73868_f()
    {
        return false;
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
    }

    public void func_73866_w_()
    {
        this.field_73977_n = new DynamicTexture(256, 256);
        this.field_110351_G = this.field_146297_k.getTextureManager().getDynamicTextureLocation("background", this.field_73977_n);
        this.widthCopyright = this.field_146289_q.getStringWidth("Copyright Mojang AB. Do not distribute!");
        this.widthCopyrightRest = this.field_146294_l - this.widthCopyright - 2;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24)
        {
            this.splashText = "Merry X-mas!";
        }
        else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1)
        {
            this.splashText = "Happy new year!";
        }
        else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31)
        {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }

        int i = 24;
        int j = this.field_146295_m / 4 + 48;

        if (this.field_146297_k.isDemo())
        {
            this.addDemoButtons(j, 24);
        }
        else
        {
            this.addSingleplayerMultiplayerButtons(j, 24);
        }

        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, j + 72 + 12, 98, 20, I18n.format("menu.options")));
        this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 + 2, j + 72 + 12, 98, 20, I18n.format("menu.quit")));
        this.field_146292_n.add(new GuiButtonLanguage(5, this.field_146294_l / 2 - 124, j + 72 + 12));

        synchronized (this.field_104025_t)
        {
            this.field_92023_s = this.field_146289_q.getStringWidth(this.field_92025_p);
            this.field_92024_r = this.field_146289_q.getStringWidth(this.field_146972_A);
            int k = Math.max(this.field_92023_s, this.field_92024_r);
            this.field_92022_t = (this.field_146294_l - k) / 2;
            this.field_92021_u = (this.field_146292_n.get(0)).field_146129_i - 24;
            this.field_92020_v = this.field_92022_t + k;
            this.field_92019_w = this.field_92021_u + 24;
        }

        this.field_146297_k.setConnectedToRealms(false);

        if (Minecraft.getInstance().gameSettings.func_74308_b(GameSettings.Options.REALMS_NOTIFICATIONS) && !this.hasCheckedForRealmsNotification)
        {
            RealmsBridge realmsbridge = new RealmsBridge();
            this.realmsNotification = realmsbridge.getNotificationScreen(this);
            this.hasCheckedForRealmsNotification = true;
        }

        if (this.areRealmsNotificationsEnabled())
        {
            this.realmsNotification.func_183500_a(this.field_146294_l, this.field_146295_m);
            this.realmsNotification.func_73866_w_();
        }
    }

    /**
     * Adds Singleplayer and Multiplayer buttons on Main Menu for players who have bought the game.
     */
    private void addSingleplayerMultiplayerButtons(int yIn, int rowHeightIn)
    {
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, yIn, I18n.format("menu.singleplayer")));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, yIn + rowHeightIn * 1, I18n.format("menu.multiplayer")));
        this.field_175372_K = this.func_189646_b(new GuiButton(14, this.field_146294_l / 2 - 100, yIn + rowHeightIn * 2, I18n.format("menu.online")));
    }

    /**
     * Adds Demo buttons on Main Menu for players who are playing Demo.
     */
    private void addDemoButtons(int yIn, int rowHeightIn)
    {
        this.field_146292_n.add(new GuiButton(11, this.field_146294_l / 2 - 100, yIn, I18n.format("menu.playdemo")));
        this.buttonResetDemo = this.func_189646_b(new GuiButton(12, this.field_146294_l / 2 - 100, yIn + rowHeightIn * 1, I18n.format("menu.resetdemo")));
        ISaveFormat isaveformat = this.field_146297_k.getSaveLoader();
        WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

        if (worldinfo == null)
        {
            this.buttonResetDemo.field_146124_l = false;
        }
    }

    protected void func_146284_a(GuiButton p_146284_1_) throws IOException
    {
        if (p_146284_1_.field_146127_k == 0)
        {
            this.field_146297_k.displayGuiScreen(new GuiOptions(this, this.field_146297_k.gameSettings));
        }

        if (p_146284_1_.field_146127_k == 5)
        {
            this.field_146297_k.displayGuiScreen(new GuiLanguage(this, this.field_146297_k.gameSettings, this.field_146297_k.getLanguageManager()));
        }

        if (p_146284_1_.field_146127_k == 1)
        {
            this.field_146297_k.displayGuiScreen(new GuiWorldSelection(this));
        }

        if (p_146284_1_.field_146127_k == 2)
        {
            this.field_146297_k.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (p_146284_1_.field_146127_k == 14 && this.field_175372_K.field_146125_m)
        {
            this.switchToRealms();
        }

        if (p_146284_1_.field_146127_k == 4)
        {
            this.field_146297_k.shutdown();
        }

        if (p_146284_1_.field_146127_k == 11)
        {
            this.field_146297_k.launchIntegratedServer("Demo_World", "Demo_World", WorldServerDemo.field_73071_a);
        }

        if (p_146284_1_.field_146127_k == 12)
        {
            ISaveFormat isaveformat = this.field_146297_k.getSaveLoader();
            WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

            if (worldinfo != null)
            {
                this.field_146297_k.displayGuiScreen(new GuiYesNo(this, I18n.format("selectWorld.deleteQuestion"), "'" + worldinfo.getWorldName() + "' " + I18n.format("selectWorld.deleteWarning"), I18n.format("selectWorld.deleteButton"), I18n.format("gui.cancel"), 12));
            }
        }
    }

    private void switchToRealms()
    {
        RealmsBridge realmsbridge = new RealmsBridge();
        realmsbridge.switchToRealms(this);
    }

    public void func_73878_a(boolean p_73878_1_, int p_73878_2_)
    {
        if (p_73878_1_ && p_73878_2_ == 12)
        {
            ISaveFormat isaveformat = this.field_146297_k.getSaveLoader();
            isaveformat.func_75800_d();
            isaveformat.deleteWorldDirectory("Demo_World");
            this.field_146297_k.displayGuiScreen(this);
        }
        else if (p_73878_2_ == 12)
        {
            this.field_146297_k.displayGuiScreen(this);
        }
        else if (p_73878_2_ == 13)
        {
            if (p_73878_1_)
            {
                try
                {
                    Class<?> oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop").invoke((Object)null);
                    oclass.getMethod("browse", URI.class).invoke(object, new URI(this.field_104024_v));
                }
                catch (Throwable throwable)
                {
                    field_146974_g.error("Couldn't open link", throwable);
                }
            }

            this.field_146297_k.displayGuiScreen(this);
        }
    }

    private void func_73970_b(int p_73970_1_, int p_73970_2_, float p_73970_3_)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.func_179128_n(5889);
        GlStateManager.func_179094_E();
        GlStateManager.func_179096_D();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.func_179128_n(5888);
        GlStateManager.func_179094_E();
        GlStateManager.func_179096_D();
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.func_179114_b(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.func_179114_b(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179129_p();
        GlStateManager.func_179132_a(false);
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        int i = 8;

        for (int j = 0; j < 64; ++j)
        {
            GlStateManager.func_179094_E();
            float f = ((float)(j % 8) / 8.0F - 0.5F) / 64.0F;
            float f1 = ((float)(j / 8) / 8.0F - 0.5F) / 64.0F;
            float f2 = 0.0F;
            GlStateManager.func_179109_b(f, f1, 0.0F);
            GlStateManager.func_179114_b(MathHelper.sin(this.field_73979_m / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.func_179114_b(-this.field_73979_m * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int k = 0; k < 6; ++k)
            {
                GlStateManager.func_179094_E();

                if (k == 1)
                {
                    GlStateManager.func_179114_b(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 2)
                {
                    GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 3)
                {
                    GlStateManager.func_179114_b(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 4)
                {
                    GlStateManager.func_179114_b(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (k == 5)
                {
                    GlStateManager.func_179114_b(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                this.field_146297_k.getTextureManager().bindTexture(field_73978_o[k]);
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int l = 255 / (j + 1);
                float f3 = 0.0F;
                bufferbuilder.func_181662_b(-1.0D, -1.0D, 1.0D).func_187315_a(0.0D, 0.0D).func_181669_b(255, 255, 255, l).endVertex();
                bufferbuilder.func_181662_b(1.0D, -1.0D, 1.0D).func_187315_a(1.0D, 0.0D).func_181669_b(255, 255, 255, l).endVertex();
                bufferbuilder.func_181662_b(1.0D, 1.0D, 1.0D).func_187315_a(1.0D, 1.0D).func_181669_b(255, 255, 255, l).endVertex();
                bufferbuilder.func_181662_b(-1.0D, 1.0D, 1.0D).func_187315_a(0.0D, 1.0D).func_181669_b(255, 255, 255, l).endVertex();
                tessellator.draw();
                GlStateManager.func_179121_F();
            }

            GlStateManager.func_179121_F();
            GlStateManager.func_179135_a(true, true, true, false);
        }

        bufferbuilder.func_178969_c(0.0D, 0.0D, 0.0D);
        GlStateManager.func_179135_a(true, true, true, true);
        GlStateManager.func_179128_n(5889);
        GlStateManager.func_179121_F();
        GlStateManager.func_179128_n(5888);
        GlStateManager.func_179121_F();
        GlStateManager.func_179132_a(true);
        GlStateManager.func_179089_o();
        GlStateManager.func_179126_j();
    }

    private void func_73968_a()
    {
        this.field_146297_k.getTextureManager().bindTexture(this.field_110351_G);
        GlStateManager.func_187421_b(3553, 10241, 9729);
        GlStateManager.func_187421_b(3553, 10240, 9729);
        GlStateManager.func_187443_a(3553, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.func_179147_l();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.func_179135_a(true, true, true, false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.func_179118_c();
        int i = 3;

        for (int j = 0; j < 3; ++j)
        {
            float f = 1.0F / (float)(j + 1);
            int k = this.field_146294_l;
            int l = this.field_146295_m;
            float f1 = (float)(j - 1) / 256.0F;
            bufferbuilder.func_181662_b((double)k, (double)l, (double)this.field_73735_i).func_187315_a((double)(0.0F + f1), 1.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).endVertex();
            bufferbuilder.func_181662_b((double)k, 0.0D, (double)this.field_73735_i).func_187315_a((double)(1.0F + f1), 1.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).endVertex();
            bufferbuilder.func_181662_b(0.0D, 0.0D, (double)this.field_73735_i).func_187315_a((double)(1.0F + f1), 0.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).endVertex();
            bufferbuilder.func_181662_b(0.0D, (double)l, (double)this.field_73735_i).func_187315_a((double)(0.0F + f1), 0.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).endVertex();
        }

        tessellator.draw();
        GlStateManager.func_179141_d();
        GlStateManager.func_179135_a(true, true, true, true);
    }

    private void func_73971_c(int p_73971_1_, int p_73971_2_, float p_73971_3_)
    {
        this.field_146297_k.getFramebuffer().unbindFramebuffer();
        GlStateManager.func_179083_b(0, 0, 256, 256);
        this.func_73970_b(p_73971_1_, p_73971_2_, p_73971_3_);
        this.func_73968_a();
        this.func_73968_a();
        this.func_73968_a();
        this.func_73968_a();
        this.func_73968_a();
        this.func_73968_a();
        this.func_73968_a();
        this.field_146297_k.getFramebuffer().bindFramebuffer(true);
        GlStateManager.func_179083_b(0, 0, this.field_146297_k.field_71443_c, this.field_146297_k.field_71440_d);
        float f = 120.0F / (float)(this.field_146294_l > this.field_146295_m ? this.field_146294_l : this.field_146295_m);
        float f1 = (float)this.field_146295_m * f / 256.0F;
        float f2 = (float)this.field_146294_l * f / 256.0F;
        int i = this.field_146294_l;
        int j = this.field_146295_m;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.func_181662_b(0.0D, (double)j, (double)this.field_73735_i).func_187315_a((double)(0.5F - f1), (double)(0.5F + f2)).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        bufferbuilder.func_181662_b((double)i, (double)j, (double)this.field_73735_i).func_187315_a((double)(0.5F - f1), (double)(0.5F - f2)).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        bufferbuilder.func_181662_b((double)i, 0.0D, (double)this.field_73735_i).func_187315_a((double)(0.5F + f1), (double)(0.5F - f2)).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        bufferbuilder.func_181662_b(0.0D, 0.0D, (double)this.field_73735_i).func_187315_a((double)(0.5F + f1), (double)(0.5F + f2)).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.field_73979_m += p_73863_3_;
        GlStateManager.func_179118_c();
        this.func_73971_c(p_73863_1_, p_73863_2_, p_73863_3_);
        GlStateManager.func_179141_d();
        int i = 274;
        int j = this.field_146294_l / 2 - 137;
        int k = 30;
        this.func_73733_a(0, 0, this.field_146294_l, this.field_146295_m, -2130706433, 16777215);
        this.func_73733_a(0, 0, this.field_146294_l, this.field_146295_m, 0, Integer.MIN_VALUE);
        this.field_146297_k.getTextureManager().bindTexture(MINECRAFT_TITLE_TEXTURES);
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);

        if ((double)this.field_73974_b < 1.0E-4D)
        {
            this.func_73729_b(j + 0, 30, 0, 0, 99, 44);
            this.func_73729_b(j + 99, 30, 129, 0, 27, 44);
            this.func_73729_b(j + 99 + 26, 30, 126, 0, 3, 44);
            this.func_73729_b(j + 99 + 26 + 3, 30, 99, 0, 26, 44);
            this.func_73729_b(j + 155, 30, 0, 45, 155, 44);
        }
        else
        {
            this.func_73729_b(j + 0, 30, 0, 0, 155, 44);
            this.func_73729_b(j + 155, 30, 0, 45, 155, 44);
        }

        this.field_146297_k.getTextureManager().bindTexture(MINECRAFT_TITLE_EDITION);
        func_146110_a(j + 88, 67, 0.0F, 0.0F, 98, 14, 128.0F, 16.0F);
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)(this.field_146294_l / 2 + 90), 70.0F, 0.0F);
        GlStateManager.func_179114_b(-20.0F, 0.0F, 0.0F, 1.0F);
        float f = 1.8F - MathHelper.abs(MathHelper.sin((float)(Minecraft.func_71386_F() % 1000L) / 1000.0F * ((float)Math.PI * 2F)) * 0.1F);
        f = f * 100.0F / (float)(this.field_146289_q.getStringWidth(this.splashText) + 32);
        GlStateManager.func_179152_a(f, f, f);
        this.func_73732_a(this.field_146289_q, this.splashText, 0, -8, -256);
        GlStateManager.func_179121_F();
        String s = "Minecraft 1.12.2";

        if (this.field_146297_k.isDemo())
        {
            s = s + " Demo";
        }
        else
        {
            s = s + ("release".equalsIgnoreCase(this.field_146297_k.getVersionType()) ? "" : "/" + this.field_146297_k.getVersionType());
        }

        this.func_73731_b(this.field_146289_q, s, 2, this.field_146295_m - 10, -1);
        this.func_73731_b(this.field_146289_q, "Copyright Mojang AB. Do not distribute!", this.widthCopyrightRest, this.field_146295_m - 10, -1);

        if (p_73863_1_ > this.widthCopyrightRest && p_73863_1_ < this.widthCopyrightRest + this.widthCopyright && p_73863_2_ > this.field_146295_m - 10 && p_73863_2_ < this.field_146295_m && Mouse.isInsideWindow())
        {
            func_73734_a(this.widthCopyrightRest, this.field_146295_m - 1, this.widthCopyrightRest + this.widthCopyright, this.field_146295_m, -1);
        }

        if (this.field_92025_p != null && !this.field_92025_p.isEmpty())
        {
            func_73734_a(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
            this.func_73731_b(this.field_146289_q, this.field_92025_p, this.field_92022_t, this.field_92021_u, -1);
            this.func_73731_b(this.field_146289_q, this.field_146972_A, (this.field_146294_l - this.field_92024_r) / 2, (this.field_146292_n.get(0)).field_146129_i - 12, -1);
        }

        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);

        if (this.areRealmsNotificationsEnabled())
        {
            this.realmsNotification.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
        }
    }

    protected void func_73864_a(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
    {
        super.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);

        synchronized (this.field_104025_t)
        {
            if (!this.field_92025_p.isEmpty() && !StringUtils.isNullOrEmpty(this.field_104024_v) && p_73864_1_ >= this.field_92022_t && p_73864_1_ <= this.field_92020_v && p_73864_2_ >= this.field_92021_u && p_73864_2_ <= this.field_92019_w)
            {
                GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.field_104024_v, 13, true);
                guiconfirmopenlink.func_146358_g();
                this.field_146297_k.displayGuiScreen(guiconfirmopenlink);
            }
        }

        if (this.areRealmsNotificationsEnabled())
        {
            this.realmsNotification.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);
        }

        if (p_73864_1_ > this.widthCopyrightRest && p_73864_1_ < this.widthCopyrightRest + this.widthCopyright && p_73864_2_ > this.field_146295_m - 10 && p_73864_2_ < this.field_146295_m)
        {
            this.field_146297_k.displayGuiScreen(new GuiWinGame(false, Runnables.doNothing()));
        }
    }

    public void func_146281_b()
    {
        if (this.realmsNotification != null)
        {
            this.realmsNotification.func_146281_b();
        }
    }
}