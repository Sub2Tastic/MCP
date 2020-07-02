package net.minecraft.client.shader;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonException;
import org.lwjgl.util.vector.Matrix4f;

public class Shader
{
    private final ShaderManager manager;
    public final Framebuffer framebufferIn;
    public final Framebuffer framebufferOut;
    private final List<Object> listAuxFramebuffers = Lists.<Object>newArrayList();
    private final List<String> listAuxNames = Lists.<String>newArrayList();
    private final List<Integer> listAuxWidths = Lists.<Integer>newArrayList();
    private final List<Integer> listAuxHeights = Lists.<Integer>newArrayList();
    private Matrix4f projectionMatrix;

    public Shader(IResourceManager resourceManager, String programName, Framebuffer framebufferInIn, Framebuffer framebufferOutIn) throws JsonException, IOException
    {
        this.manager = new ShaderManager(resourceManager, programName);
        this.framebufferIn = framebufferInIn;
        this.framebufferOut = framebufferOutIn;
    }

    public void func_148044_b()
    {
        this.manager.func_147988_a();
    }

    public void addAuxFramebuffer(String auxName, Object auxFramebufferIn, int width, int height)
    {
        this.listAuxNames.add(this.listAuxNames.size(), auxName);
        this.listAuxFramebuffers.add(this.listAuxFramebuffers.size(), auxFramebufferIn);
        this.listAuxWidths.add(this.listAuxWidths.size(), Integer.valueOf(width));
        this.listAuxHeights.add(this.listAuxHeights.size(), Integer.valueOf(height));
    }

    private void func_148040_d()
    {
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.func_179084_k();
        GlStateManager.func_179097_i();
        GlStateManager.func_179118_c();
        GlStateManager.func_179106_n();
        GlStateManager.func_179140_f();
        GlStateManager.func_179119_h();
        GlStateManager.func_179098_w();
        GlStateManager.func_179144_i(0);
    }

    public void func_148045_a(Matrix4f p_148045_1_)
    {
        this.projectionMatrix = p_148045_1_;
    }

    public void render(float partialTicks)
    {
        this.func_148040_d();
        this.framebufferIn.unbindFramebuffer();
        float f = (float)this.framebufferOut.framebufferTextureWidth;
        float f1 = (float)this.framebufferOut.framebufferTextureHeight;
        GlStateManager.func_179083_b(0, 0, (int)f, (int)f1);
        this.manager.func_147992_a("DiffuseSampler", this.framebufferIn);

        for (int i = 0; i < this.listAuxFramebuffers.size(); ++i)
        {
            this.manager.func_147992_a(this.listAuxNames.get(i), this.listAuxFramebuffers.get(i));
            this.manager.func_147984_b("AuxSize" + i).set((float)((Integer)this.listAuxWidths.get(i)).intValue(), (float)((Integer)this.listAuxHeights.get(i)).intValue());
        }

        this.manager.func_147984_b("ProjMat").func_148088_a(this.projectionMatrix);
        this.manager.func_147984_b("InSize").set((float)this.framebufferIn.framebufferTextureWidth, (float)this.framebufferIn.framebufferTextureHeight);
        this.manager.func_147984_b("OutSize").set(f, f1);
        this.manager.func_147984_b("Time").set(partialTicks);
        Minecraft minecraft = Minecraft.getInstance();
        this.manager.func_147984_b("ScreenSize").set((float)minecraft.field_71443_c, (float)minecraft.field_71440_d);
        this.manager.func_147995_c();
        this.framebufferOut.func_147614_f();
        this.framebufferOut.bindFramebuffer(false);
        GlStateManager.func_179132_a(false);
        GlStateManager.func_179135_a(true, true, true, true);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.func_181662_b(0.0D, (double)f1, 500.0D).func_181669_b(255, 255, 255, 255).endVertex();
        bufferbuilder.func_181662_b((double)f, (double)f1, 500.0D).func_181669_b(255, 255, 255, 255).endVertex();
        bufferbuilder.func_181662_b((double)f, 0.0D, 500.0D).func_181669_b(255, 255, 255, 255).endVertex();
        bufferbuilder.func_181662_b(0.0D, 0.0D, 500.0D).func_181669_b(255, 255, 255, 255).endVertex();
        tessellator.draw();
        GlStateManager.func_179132_a(true);
        GlStateManager.func_179135_a(true, true, true, true);
        this.manager.func_147993_b();
        this.framebufferOut.unbindFramebuffer();
        this.framebufferIn.unbindFramebufferTexture();

        for (Object object : this.listAuxFramebuffers)
        {
            if (object instanceof Framebuffer)
            {
                ((Framebuffer)object).unbindFramebufferTexture();
            }
        }
    }

    public ShaderManager func_148043_c()
    {
        return this.manager;
    }
}
