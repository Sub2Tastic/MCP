package net.minecraft.client.renderer.texture;

import net.minecraft.client.renderer.GlStateManager;

public abstract class AbstractTexture implements ITextureObject
{
    protected int glTextureId = -1;
    protected boolean blur;
    protected boolean mipmap;
    protected boolean field_174938_d;
    protected boolean field_174939_e;

    public void setBlurMipmapDirect(boolean blurIn, boolean mipmapIn)
    {
        this.blur = blurIn;
        this.mipmap = mipmapIn;
        int i;
        int j;

        if (blurIn)
        {
            i = mipmapIn ? 9987 : 9729;
            j = 9729;
        }
        else
        {
            i = mipmapIn ? 9986 : 9728;
            j = 9728;
        }

        GlStateManager.func_187421_b(3553, 10241, i);
        GlStateManager.func_187421_b(3553, 10240, j);
    }

    public void func_174936_b(boolean p_174936_1_, boolean p_174936_2_)
    {
        this.field_174938_d = this.blur;
        this.field_174939_e = this.mipmap;
        this.setBlurMipmapDirect(p_174936_1_, p_174936_2_);
    }

    public void func_174935_a()
    {
        this.setBlurMipmapDirect(this.field_174938_d, this.field_174939_e);
    }

    public int getGlTextureId()
    {
        if (this.glTextureId == -1)
        {
            this.glTextureId = TextureUtil.func_110996_a();
        }

        return this.glTextureId;
    }

    public void deleteGlTexture()
    {
        if (this.glTextureId != -1)
        {
            TextureUtil.func_147942_a(this.glTextureId);
            this.glTextureId = -1;
        }
    }
}
