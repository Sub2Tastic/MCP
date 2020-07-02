package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;

public class DynamicTexture extends AbstractTexture
{
    private final int[] dynamicTextureData;
    private final int field_94233_j;
    private final int field_94234_k;

    public DynamicTexture(BufferedImage p_i1270_1_)
    {
        this(p_i1270_1_.getWidth(), p_i1270_1_.getHeight());
        p_i1270_1_.getRGB(0, 0, p_i1270_1_.getWidth(), p_i1270_1_.getHeight(), this.dynamicTextureData, 0, p_i1270_1_.getWidth());
        this.updateDynamicTexture();
    }

    public DynamicTexture(int p_i1271_1_, int p_i1271_2_)
    {
        this.field_94233_j = p_i1271_1_;
        this.field_94234_k = p_i1271_2_;
        this.dynamicTextureData = new int[p_i1271_1_ * p_i1271_2_];
        TextureUtil.func_110991_a(this.getGlTextureId(), p_i1271_1_, p_i1271_2_);
    }

    public void func_110551_a(IResourceManager p_110551_1_) throws IOException
    {
    }

    public void updateDynamicTexture()
    {
        TextureUtil.func_110988_a(this.getGlTextureId(), this.dynamicTextureData, this.field_94233_j, this.field_94234_k);
    }

    public int[] func_110565_c()
    {
        return this.dynamicTextureData;
    }
}
