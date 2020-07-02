package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleTexture extends AbstractTexture
{
    private static final Logger LOGGER = LogManager.getLogger();
    protected final ResourceLocation textureLocation;

    public SimpleTexture(ResourceLocation textureResourceLocation)
    {
        this.textureLocation = textureResourceLocation;
    }

    public void func_110551_a(IResourceManager p_110551_1_) throws IOException
    {
        this.deleteGlTexture();
        IResource iresource = null;

        try
        {
            iresource = p_110551_1_.func_110536_a(this.textureLocation);
            BufferedImage bufferedimage = TextureUtil.func_177053_a(iresource.func_110527_b());
            boolean flag = false;
            boolean flag1 = false;

            if (iresource.func_110528_c())
            {
                try
                {
                    TextureMetadataSection texturemetadatasection = (TextureMetadataSection)iresource.func_110526_a("texture");

                    if (texturemetadatasection != null)
                    {
                        flag = texturemetadatasection.getTextureBlur();
                        flag1 = texturemetadatasection.getTextureClamp();
                    }
                }
                catch (RuntimeException runtimeexception)
                {
                    LOGGER.warn("Failed reading metadata of: {}", this.textureLocation, runtimeexception);
                }
            }

            TextureUtil.func_110989_a(this.getGlTextureId(), bufferedimage, flag, flag1);
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)iresource);
        }
    }
}
