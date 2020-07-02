package net.minecraft.client.shader;

import java.nio.IntBuffer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class Framebuffer
{
    public int framebufferTextureWidth;
    public int framebufferTextureHeight;
    public int framebufferWidth;
    public int framebufferHeight;
    public boolean useDepth;
    public int framebufferObject;
    public int framebufferTexture;
    public int depthBuffer;
    public float[] framebufferColor;
    public int framebufferFilter;

    public Framebuffer(int p_i45078_1_, int p_i45078_2_, boolean p_i45078_3_)
    {
        this.useDepth = p_i45078_3_;
        this.framebufferObject = -1;
        this.framebufferTexture = -1;
        this.depthBuffer = -1;
        this.framebufferColor = new float[4];
        this.framebufferColor[0] = 1.0F;
        this.framebufferColor[1] = 1.0F;
        this.framebufferColor[2] = 1.0F;
        this.framebufferColor[3] = 0.0F;
        this.func_147613_a(p_i45078_1_, p_i45078_2_);
    }

    public void func_147613_a(int p_147613_1_, int p_147613_2_)
    {
        if (!OpenGlHelper.func_148822_b())
        {
            this.framebufferWidth = p_147613_1_;
            this.framebufferHeight = p_147613_2_;
        }
        else
        {
            GlStateManager.func_179126_j();

            if (this.framebufferObject >= 0)
            {
                this.deleteFramebuffer();
            }

            this.func_147605_b(p_147613_1_, p_147613_2_);
            this.checkFramebufferComplete();
            OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, 0);
        }
    }

    public void deleteFramebuffer()
    {
        if (OpenGlHelper.func_148822_b())
        {
            this.unbindFramebufferTexture();
            this.unbindFramebuffer();

            if (this.depthBuffer > -1)
            {
                OpenGlHelper.func_153184_g(this.depthBuffer);
                this.depthBuffer = -1;
            }

            if (this.framebufferTexture > -1)
            {
                TextureUtil.func_147942_a(this.framebufferTexture);
                this.framebufferTexture = -1;
            }

            if (this.framebufferObject > -1)
            {
                OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, 0);
                OpenGlHelper.func_153174_h(this.framebufferObject);
                this.framebufferObject = -1;
            }
        }
    }

    public void func_147605_b(int p_147605_1_, int p_147605_2_)
    {
        this.framebufferWidth = p_147605_1_;
        this.framebufferHeight = p_147605_2_;
        this.framebufferTextureWidth = p_147605_1_;
        this.framebufferTextureHeight = p_147605_2_;

        if (!OpenGlHelper.func_148822_b())
        {
            this.func_147614_f();
        }
        else
        {
            this.framebufferObject = OpenGlHelper.func_153165_e();
            this.framebufferTexture = TextureUtil.func_110996_a();

            if (this.useDepth)
            {
                this.depthBuffer = OpenGlHelper.func_153185_f();
            }

            this.setFramebufferFilter(9728);
            GlStateManager.func_179144_i(this.framebufferTexture);
            GlStateManager.func_187419_a(3553, 0, 32856, this.framebufferTextureWidth, this.framebufferTextureHeight, 0, 6408, 5121, (IntBuffer)null);
            OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, this.framebufferObject);
            OpenGlHelper.func_153188_a(OpenGlHelper.field_153198_e, OpenGlHelper.field_153200_g, 3553, this.framebufferTexture, 0);

            if (this.useDepth)
            {
                OpenGlHelper.func_153176_h(OpenGlHelper.field_153199_f, this.depthBuffer);
                OpenGlHelper.func_153186_a(OpenGlHelper.field_153199_f, 33190, this.framebufferTextureWidth, this.framebufferTextureHeight);
                OpenGlHelper.func_153190_b(OpenGlHelper.field_153198_e, OpenGlHelper.field_153201_h, OpenGlHelper.field_153199_f, this.depthBuffer);
            }

            this.func_147614_f();
            this.unbindFramebufferTexture();
        }
    }

    public void setFramebufferFilter(int framebufferFilterIn)
    {
        if (OpenGlHelper.func_148822_b())
        {
            this.framebufferFilter = framebufferFilterIn;
            GlStateManager.func_179144_i(this.framebufferTexture);
            GlStateManager.func_187421_b(3553, 10241, framebufferFilterIn);
            GlStateManager.func_187421_b(3553, 10240, framebufferFilterIn);
            GlStateManager.func_187421_b(3553, 10242, 10496);
            GlStateManager.func_187421_b(3553, 10243, 10496);
            GlStateManager.func_179144_i(0);
        }
    }

    public void checkFramebufferComplete()
    {
        int i = OpenGlHelper.func_153167_i(OpenGlHelper.field_153198_e);

        if (i != OpenGlHelper.field_153202_i)
        {
            if (i == OpenGlHelper.field_153203_j)
            {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
            }
            else if (i == OpenGlHelper.field_153204_k)
            {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
            }
            else if (i == OpenGlHelper.field_153205_l)
            {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
            }
            else if (i == OpenGlHelper.field_153206_m)
            {
                throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
            }
            else
            {
                throw new RuntimeException("glCheckFramebufferStatus returned unknown status:" + i);
            }
        }
    }

    public void bindFramebufferTexture()
    {
        if (OpenGlHelper.func_148822_b())
        {
            GlStateManager.func_179144_i(this.framebufferTexture);
        }
    }

    public void unbindFramebufferTexture()
    {
        if (OpenGlHelper.func_148822_b())
        {
            GlStateManager.func_179144_i(0);
        }
    }

    public void bindFramebuffer(boolean setViewportIn)
    {
        if (OpenGlHelper.func_148822_b())
        {
            OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, this.framebufferObject);

            if (setViewportIn)
            {
                GlStateManager.func_179083_b(0, 0, this.framebufferWidth, this.framebufferHeight);
            }
        }
    }

    public void unbindFramebuffer()
    {
        if (OpenGlHelper.func_148822_b())
        {
            OpenGlHelper.func_153171_g(OpenGlHelper.field_153198_e, 0);
        }
    }

    public void setFramebufferColor(float red, float green, float blue, float alpha)
    {
        this.framebufferColor[0] = red;
        this.framebufferColor[1] = green;
        this.framebufferColor[2] = blue;
        this.framebufferColor[3] = alpha;
    }

    public void framebufferRender(int width, int height)
    {
        this.framebufferRenderExt(width, height, true);
    }

    public void framebufferRenderExt(int width, int height, boolean p_178038_3_)
    {
        if (OpenGlHelper.func_148822_b())
        {
            GlStateManager.func_179135_a(true, true, true, false);
            GlStateManager.func_179097_i();
            GlStateManager.func_179132_a(false);
            GlStateManager.func_179128_n(5889);
            GlStateManager.func_179096_D();
            GlStateManager.func_179130_a(0.0D, (double)width, (double)height, 0.0D, 1000.0D, 3000.0D);
            GlStateManager.func_179128_n(5888);
            GlStateManager.func_179096_D();
            GlStateManager.func_179109_b(0.0F, 0.0F, -2000.0F);
            GlStateManager.func_179083_b(0, 0, width, height);
            GlStateManager.func_179098_w();
            GlStateManager.func_179140_f();
            GlStateManager.func_179118_c();

            if (p_178038_3_)
            {
                GlStateManager.func_179084_k();
                GlStateManager.func_179142_g();
            }

            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            this.bindFramebufferTexture();
            float f = (float)width;
            float f1 = (float)height;
            float f2 = (float)this.framebufferWidth / (float)this.framebufferTextureWidth;
            float f3 = (float)this.framebufferHeight / (float)this.framebufferTextureHeight;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.func_181662_b(0.0D, (double)f1, 0.0D).func_187315_a(0.0D, 0.0D).func_181669_b(255, 255, 255, 255).endVertex();
            bufferbuilder.func_181662_b((double)f, (double)f1, 0.0D).func_187315_a((double)f2, 0.0D).func_181669_b(255, 255, 255, 255).endVertex();
            bufferbuilder.func_181662_b((double)f, 0.0D, 0.0D).func_187315_a((double)f2, (double)f3).func_181669_b(255, 255, 255, 255).endVertex();
            bufferbuilder.func_181662_b(0.0D, 0.0D, 0.0D).func_187315_a(0.0D, (double)f3).func_181669_b(255, 255, 255, 255).endVertex();
            tessellator.draw();
            this.unbindFramebufferTexture();
            GlStateManager.func_179132_a(true);
            GlStateManager.func_179135_a(true, true, true, true);
        }
    }

    public void func_147614_f()
    {
        this.bindFramebuffer(true);
        GlStateManager.func_179082_a(this.framebufferColor[0], this.framebufferColor[1], this.framebufferColor[2], this.framebufferColor[3]);
        int i = 16384;

        if (this.useDepth)
        {
            GlStateManager.func_179151_a(1.0D);
            i |= 256;
        }

        GlStateManager.func_179086_m(i);
        this.unbindFramebuffer();
    }
}
