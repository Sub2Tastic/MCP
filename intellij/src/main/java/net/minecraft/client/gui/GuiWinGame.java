package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiWinGame extends GuiScreen
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation MINECRAFT_LOGO = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation MINECRAFT_EDITION = new ResourceLocation("textures/gui/title/edition.png");
    private static final ResourceLocation VIGNETTE_TEXTURE = new ResourceLocation("textures/misc/vignette.png");
    private final boolean poem;
    private final Runnable onFinished;
    private float time;
    private List<String> lines;
    private int totalScrollLength;
    private float scrollSpeed = 0.5F;

    public GuiWinGame(boolean poemIn, Runnable onFinishedIn)
    {
        this.poem = poemIn;
        this.onFinished = onFinishedIn;

        if (!poemIn)
        {
            this.scrollSpeed = 0.75F;
        }
    }

    public void func_73876_c()
    {
        this.field_146297_k.getMusicTicker().tick();
        this.field_146297_k.getSoundHandler().tick();
        float f = (float)(this.totalScrollLength + this.field_146295_m + this.field_146295_m + 24) / this.scrollSpeed;

        if (this.time > f)
        {
            this.sendRespawnPacket();
        }
    }

    protected void func_73869_a(char p_73869_1_, int p_73869_2_) throws IOException
    {
        if (p_73869_2_ == 1)
        {
            this.sendRespawnPacket();
        }
    }

    private void sendRespawnPacket()
    {
        this.onFinished.run();
        this.field_146297_k.displayGuiScreen((GuiScreen)null);
    }

    public boolean func_73868_f()
    {
        return true;
    }

    public void func_73866_w_()
    {
        if (this.lines == null)
        {
            this.lines = Lists.<String>newArrayList();
            IResource iresource = null;

            try
            {
                String s = "" + TextFormatting.WHITE + TextFormatting.OBFUSCATED + TextFormatting.GREEN + TextFormatting.AQUA;
                int i = 274;

                if (this.poem)
                {
                    iresource = this.field_146297_k.func_110442_L().func_110536_a(new ResourceLocation("texts/end.txt"));
                    InputStream inputstream = iresource.func_110527_b();
                    BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8));
                    Random random = new Random(8124371L);
                    String s1;

                    while ((s1 = bufferedreader.readLine()) != null)
                    {
                        String s2;
                        String s3;

                        for (s1 = s1.replaceAll("PLAYERNAME", this.field_146297_k.getSession().getUsername()); s1.contains(s); s1 = s2 + TextFormatting.WHITE + TextFormatting.OBFUSCATED + "XXXXXXXX".substring(0, random.nextInt(4) + 3) + s3)
                        {
                            int j = s1.indexOf(s);
                            s2 = s1.substring(0, j);
                            s3 = s1.substring(j + s.length());
                        }

                        this.lines.addAll(this.field_146297_k.fontRenderer.listFormattedStringToWidth(s1, 274));
                        this.lines.add("");
                    }

                    inputstream.close();

                    for (int k = 0; k < 8; ++k)
                    {
                        this.lines.add("");
                    }
                }

                InputStream inputstream1 = this.field_146297_k.func_110442_L().func_110536_a(new ResourceLocation("texts/credits.txt")).func_110527_b();
                BufferedReader bufferedreader1 = new BufferedReader(new InputStreamReader(inputstream1, StandardCharsets.UTF_8));
                String s4;

                while ((s4 = bufferedreader1.readLine()) != null)
                {
                    s4 = s4.replaceAll("PLAYERNAME", this.field_146297_k.getSession().getUsername());
                    s4 = s4.replaceAll("\t", "    ");
                    this.lines.addAll(this.field_146297_k.fontRenderer.listFormattedStringToWidth(s4, 274));
                    this.lines.add("");
                }

                inputstream1.close();
                this.totalScrollLength = this.lines.size() * 12;
            }
            catch (Exception exception)
            {
                LOGGER.error("Couldn't load credits", (Throwable)exception);
            }
            finally
            {
                IOUtils.closeQuietly((Closeable)iresource);
            }
        }
    }

    private void drawWinGameScreen(int mouseX, int mouseY, float partialTicks)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        this.field_146297_k.getTextureManager().bindTexture(Gui.field_110325_k);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        int i = this.field_146294_l;
        float f = -this.time * 0.5F * this.scrollSpeed;
        float f1 = (float)this.field_146295_m - this.time * 0.5F * this.scrollSpeed;
        float f2 = 0.015625F;
        float f3 = this.time * 0.02F;
        float f4 = (float)(this.totalScrollLength + this.field_146295_m + this.field_146295_m + 24) / this.scrollSpeed;
        float f5 = (f4 - 20.0F - this.time) * 0.005F;

        if (f5 < f3)
        {
            f3 = f5;
        }

        if (f3 > 1.0F)
        {
            f3 = 1.0F;
        }

        f3 = f3 * f3;
        f3 = f3 * 96.0F / 255.0F;
        bufferbuilder.func_181662_b(0.0D, (double)this.field_146295_m, (double)this.field_73735_i).func_187315_a(0.0D, (double)(f * 0.015625F)).func_181666_a(f3, f3, f3, 1.0F).endVertex();
        bufferbuilder.func_181662_b((double)i, (double)this.field_146295_m, (double)this.field_73735_i).func_187315_a((double)((float)i * 0.015625F), (double)(f * 0.015625F)).func_181666_a(f3, f3, f3, 1.0F).endVertex();
        bufferbuilder.func_181662_b((double)i, 0.0D, (double)this.field_73735_i).func_187315_a((double)((float)i * 0.015625F), (double)(f1 * 0.015625F)).func_181666_a(f3, f3, f3, 1.0F).endVertex();
        bufferbuilder.func_181662_b(0.0D, 0.0D, (double)this.field_73735_i).func_187315_a(0.0D, (double)(f1 * 0.015625F)).func_181666_a(f3, f3, f3, 1.0F).endVertex();
        tessellator.draw();
    }

    public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.drawWinGameScreen(p_73863_1_, p_73863_2_, p_73863_3_);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        int i = 274;
        int j = this.field_146294_l / 2 - 137;
        int k = this.field_146295_m + 50;
        this.time += p_73863_3_;
        float f = -this.time * this.scrollSpeed;
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b(0.0F, f, 0.0F);
        this.field_146297_k.getTextureManager().bindTexture(MINECRAFT_LOGO);
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.func_179141_d();
        this.func_73729_b(j, k, 0, 0, 155, 44);
        this.func_73729_b(j + 155, k, 0, 45, 155, 44);
        this.field_146297_k.getTextureManager().bindTexture(MINECRAFT_EDITION);
        func_146110_a(j + 88, k + 37, 0.0F, 0.0F, 98, 14, 128.0F, 16.0F);
        GlStateManager.func_179118_c();
        int l = k + 100;

        for (int i1 = 0; i1 < this.lines.size(); ++i1)
        {
            if (i1 == this.lines.size() - 1)
            {
                float f1 = (float)l + f - (float)(this.field_146295_m / 2 - 6);

                if (f1 < 0.0F)
                {
                    GlStateManager.func_179109_b(0.0F, -f1, 0.0F);
                }
            }

            if ((float)l + f + 12.0F + 8.0F > 0.0F && (float)l + f < (float)this.field_146295_m)
            {
                String s = this.lines.get(i1);

                if (s.startsWith("[C]"))
                {
                    this.field_146289_q.drawStringWithShadow(s.substring(3), (float)(j + (274 - this.field_146289_q.getStringWidth(s.substring(3))) / 2), (float)l, 16777215);
                }
                else
                {
                    this.field_146289_q.random.setSeed((long)((float)((long)i1 * 4238972211L) + this.time / 4.0F));
                    this.field_146289_q.drawStringWithShadow(s, (float)j, (float)l, 16777215);
                }
            }

            l += 12;
        }

        GlStateManager.func_179121_F();
        this.field_146297_k.getTextureManager().bindTexture(VIGNETTE_TEXTURE);
        GlStateManager.func_179147_l();
        GlStateManager.func_187401_a(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
        int j1 = this.field_146294_l;
        int k1 = this.field_146295_m;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.func_181662_b(0.0D, (double)k1, (double)this.field_73735_i).func_187315_a(0.0D, 1.0D).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        bufferbuilder.func_181662_b((double)j1, (double)k1, (double)this.field_73735_i).func_187315_a(1.0D, 1.0D).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        bufferbuilder.func_181662_b((double)j1, 0.0D, (double)this.field_73735_i).func_187315_a(1.0D, 0.0D).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        bufferbuilder.func_181662_b(0.0D, 0.0D, (double)this.field_73735_i).func_187315_a(0.0D, 0.0D).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
        GlStateManager.func_179084_k();
        super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
