package net.minecraft.client.gui;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerListEntryNormal implements GuiListExtended.IGuiListEntry
{
    private static final Logger field_148304_a = LogManager.getLogger();
    private static final ThreadPoolExecutor field_148302_b = new ScheduledThreadPoolExecutor(5, (new ThreadFactoryBuilder()).setNameFormat("Server Pinger #%d").setDaemon(true).build());
    private static final ResourceLocation field_178015_c = new ResourceLocation("textures/misc/unknown_server.png");
    private static final ResourceLocation field_178014_d = new ResourceLocation("textures/gui/server_selection.png");
    private final GuiMultiplayer owner;
    private final Minecraft mc;
    private final ServerData server;
    private final ResourceLocation serverIcon;
    private String lastIconB64;
    private DynamicTexture icon;
    private long lastClickTime;

    protected ServerListEntryNormal(GuiMultiplayer p_i45048_1_, ServerData p_i45048_2_)
    {
        this.owner = p_i45048_1_;
        this.server = p_i45048_2_;
        this.mc = Minecraft.getInstance();
        this.serverIcon = new ResourceLocation("servers/" + p_i45048_2_.serverIP + "/icon");
        this.icon = (DynamicTexture)this.mc.getTextureManager().func_110581_b(this.serverIcon);
    }

    public void func_192634_a(int p_192634_1_, int p_192634_2_, int p_192634_3_, int p_192634_4_, int p_192634_5_, int p_192634_6_, int p_192634_7_, boolean p_192634_8_, float p_192634_9_)
    {
        if (!this.server.pinged)
        {
            this.server.pinged = true;
            this.server.pingToServer = -2L;
            this.server.serverMOTD = "";
            this.server.populationInfo = "";
            field_148302_b.submit(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        ServerListEntryNormal.this.owner.getOldServerPinger().ping(ServerListEntryNormal.this.server);
                    }
                    catch (UnknownHostException var2)
                    {
                        ServerListEntryNormal.this.server.pingToServer = -1L;
                        ServerListEntryNormal.this.server.serverMOTD = TextFormatting.DARK_RED + I18n.format("multiplayer.status.cannot_resolve");
                    }
                    catch (Exception var3)
                    {
                        ServerListEntryNormal.this.server.pingToServer = -1L;
                        ServerListEntryNormal.this.server.serverMOTD = TextFormatting.DARK_RED + I18n.format("multiplayer.status.cannot_connect");
                    }
                }
            });
        }

        boolean flag = this.server.version > 340;
        boolean flag1 = this.server.version < 340;
        boolean flag2 = flag || flag1;
        this.mc.fontRenderer.func_78276_b(this.server.serverName, p_192634_2_ + 32 + 3, p_192634_3_ + 1, 16777215);
        List<String> list = this.mc.fontRenderer.listFormattedStringToWidth(this.server.serverMOTD, p_192634_4_ - 32 - 2);

        for (int i = 0; i < Math.min(list.size(), 2); ++i)
        {
            this.mc.fontRenderer.func_78276_b(list.get(i), p_192634_2_ + 32 + 3, p_192634_3_ + 12 + this.mc.fontRenderer.FONT_HEIGHT * i, 8421504);
        }

        String s2 = flag2 ? TextFormatting.DARK_RED + this.server.gameVersion : this.server.populationInfo;
        int j = this.mc.fontRenderer.getStringWidth(s2);
        this.mc.fontRenderer.func_78276_b(s2, p_192634_2_ + p_192634_4_ - j - 15 - 2, p_192634_3_ + 1, 8421504);
        int k = 0;
        String s = null;
        int l;
        String s1;

        if (flag2)
        {
            l = 5;
            s1 = I18n.format(flag ? "multiplayer.status.client_out_of_date" : "multiplayer.status.server_out_of_date");
            s = this.server.playerList;
        }
        else if (this.server.pinged && this.server.pingToServer != -2L)
        {
            if (this.server.pingToServer < 0L)
            {
                l = 5;
            }
            else if (this.server.pingToServer < 150L)
            {
                l = 0;
            }
            else if (this.server.pingToServer < 300L)
            {
                l = 1;
            }
            else if (this.server.pingToServer < 600L)
            {
                l = 2;
            }
            else if (this.server.pingToServer < 1000L)
            {
                l = 3;
            }
            else
            {
                l = 4;
            }

            if (this.server.pingToServer < 0L)
            {
                s1 = I18n.format("multiplayer.status.no_connection");
            }
            else
            {
                s1 = this.server.pingToServer + "ms";
                s = this.server.playerList;
            }
        }
        else
        {
            k = 1;
            l = (int)(Minecraft.func_71386_F() / 100L + (long)(p_192634_1_ * 2) & 7L);

            if (l > 4)
            {
                l = 8 - l;
            }

            s1 = I18n.format("multiplayer.status.pinging");
        }

        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Gui.field_110324_m);
        Gui.func_146110_a(p_192634_2_ + p_192634_4_ - 15, p_192634_3_, (float)(k * 10), (float)(176 + l * 8), 10, 8, 256.0F, 256.0F);

        if (this.server.getBase64EncodedIconData() != null && !this.server.getBase64EncodedIconData().equals(this.lastIconB64))
        {
            this.lastIconB64 = this.server.getBase64EncodedIconData();
            this.prepareServerIcon();
            this.owner.getServerList().saveServerList();
        }

        if (this.icon != null)
        {
            this.drawTextureAt(p_192634_2_, p_192634_3_, this.serverIcon);
        }
        else
        {
            this.drawTextureAt(p_192634_2_, p_192634_3_, field_178015_c);
        }

        int i1 = p_192634_6_ - p_192634_2_;
        int j1 = p_192634_7_ - p_192634_3_;

        if (i1 >= p_192634_4_ - 15 && i1 <= p_192634_4_ - 5 && j1 >= 0 && j1 <= 8)
        {
            this.owner.setHoveringText(s1);
        }
        else if (i1 >= p_192634_4_ - j - 15 - 2 && i1 <= p_192634_4_ - 15 - 2 && j1 >= 0 && j1 <= 8)
        {
            this.owner.setHoveringText(s);
        }

        if (this.mc.gameSettings.touchscreen || p_192634_8_)
        {
            this.mc.getTextureManager().bindTexture(field_178014_d);
            Gui.func_73734_a(p_192634_2_, p_192634_3_, p_192634_2_ + 32, p_192634_3_ + 32, -1601138544);
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            int k1 = p_192634_6_ - p_192634_2_;
            int l1 = p_192634_7_ - p_192634_3_;

            if (this.canJoin())
            {
                if (k1 < 32 && k1 > 16)
                {
                    Gui.func_146110_a(p_192634_2_, p_192634_3_, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                }
                else
                {
                    Gui.func_146110_a(p_192634_2_, p_192634_3_, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }

            if (this.owner.func_175392_a(this, p_192634_1_))
            {
                if (k1 < 16 && l1 < 16)
                {
                    Gui.func_146110_a(p_192634_2_, p_192634_3_, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                }
                else
                {
                    Gui.func_146110_a(p_192634_2_, p_192634_3_, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }

            if (this.owner.func_175394_b(this, p_192634_1_))
            {
                if (k1 < 16 && l1 > 16)
                {
                    Gui.func_146110_a(p_192634_2_, p_192634_3_, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                }
                else
                {
                    Gui.func_146110_a(p_192634_2_, p_192634_3_, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }
        }
    }

    protected void drawTextureAt(int p_178012_1_, int p_178012_2_, ResourceLocation p_178012_3_)
    {
        this.mc.getTextureManager().bindTexture(p_178012_3_);
        GlStateManager.func_179147_l();
        Gui.func_146110_a(p_178012_1_, p_178012_2_, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
        GlStateManager.func_179084_k();
    }

    private boolean canJoin()
    {
        return true;
    }

    private void prepareServerIcon()
    {
        if (this.server.getBase64EncodedIconData() == null)
        {
            this.mc.getTextureManager().deleteTexture(this.serverIcon);
            this.icon = null;
        }
        else
        {
            ByteBuf bytebuf = Unpooled.copiedBuffer((CharSequence)this.server.getBase64EncodedIconData(), StandardCharsets.UTF_8);
            ByteBuf bytebuf1 = null;
            BufferedImage bufferedimage;
            label99:
            {
                try
                {
                    bytebuf1 = Base64.decode(bytebuf);
                    bufferedimage = TextureUtil.func_177053_a(new ByteBufInputStream(bytebuf1));
                    Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide");
                    Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high");
                    break label99;
                }
                catch (Throwable throwable)
                {
                    field_148304_a.error("Invalid icon for server {} ({})", this.server.serverName, this.server.serverIP, throwable);
                    this.server.setBase64EncodedIconData((String)null);
                }
                finally
                {
                    bytebuf.release();

                    if (bytebuf1 != null)
                    {
                        bytebuf1.release();
                    }
                }

                return;
            }

            if (this.icon == null)
            {
                this.icon = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
                this.mc.getTextureManager().func_110579_a(this.serverIcon, this.icon);
            }

            bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), this.icon.func_110565_c(), 0, bufferedimage.getWidth());
            this.icon.updateDynamicTexture();
        }
    }

    public boolean func_148278_a(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
    {
        if (p_148278_5_ <= 32)
        {
            if (p_148278_5_ < 32 && p_148278_5_ > 16 && this.canJoin())
            {
                this.owner.func_146790_a(p_148278_1_);
                this.owner.connectToSelected();
                return true;
            }

            if (p_148278_5_ < 16 && p_148278_6_ < 16 && this.owner.func_175392_a(this, p_148278_1_))
            {
                this.owner.func_175391_a(this, p_148278_1_, GuiScreen.func_146272_n());
                return true;
            }

            if (p_148278_5_ < 16 && p_148278_6_ > 16 && this.owner.func_175394_b(this, p_148278_1_))
            {
                this.owner.func_175393_b(this, p_148278_1_, GuiScreen.func_146272_n());
                return true;
            }
        }

        this.owner.func_146790_a(p_148278_1_);

        if (Minecraft.func_71386_F() - this.lastClickTime < 250L)
        {
            this.owner.connectToSelected();
        }

        this.lastClickTime = Minecraft.func_71386_F();
        return false;
    }

    public void func_192633_a(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_)
    {
    }

    public void func_148277_b(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_)
    {
    }

    public ServerData getServerData()
    {
        return this.server;
    }
}
