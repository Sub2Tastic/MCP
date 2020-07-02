package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadDownloadImageData extends SimpleTexture
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final AtomicInteger field_147643_d = new AtomicInteger(0);
    @Nullable
    private final File cacheFile;
    private final String imageUrl;
    @Nullable
    private final IImageBuffer field_110563_c;
    @Nullable
    private BufferedImage field_110560_d;
    @Nullable
    private Thread field_110561_e;
    private boolean textureUploaded;

    public ThreadDownloadImageData(@Nullable File p_i1049_1_, String p_i1049_2_, ResourceLocation p_i1049_3_, @Nullable IImageBuffer p_i1049_4_)
    {
        super(p_i1049_3_);
        this.cacheFile = p_i1049_1_;
        this.imageUrl = p_i1049_2_;
        this.field_110563_c = p_i1049_4_;
    }

    private void func_147640_e()
    {
        if (!this.textureUploaded)
        {
            if (this.field_110560_d != null)
            {
                if (this.textureLocation != null)
                {
                    this.deleteGlTexture();
                }

                TextureUtil.func_110987_a(super.getGlTextureId(), this.field_110560_d);
                this.textureUploaded = true;
            }
        }
    }

    public int getGlTextureId()
    {
        this.func_147640_e();
        return super.getGlTextureId();
    }

    public void func_147641_a(BufferedImage p_147641_1_)
    {
        this.field_110560_d = p_147641_1_;

        if (this.field_110563_c != null)
        {
            this.field_110563_c.func_152634_a();
        }
    }

    public void func_110551_a(IResourceManager p_110551_1_) throws IOException
    {
        if (this.field_110560_d == null && this.textureLocation != null)
        {
            super.func_110551_a(p_110551_1_);
        }

        if (this.field_110561_e == null)
        {
            if (this.cacheFile != null && this.cacheFile.isFile())
            {
                LOGGER.debug("Loading http texture from local cache ({})", (Object)this.cacheFile);

                try
                {
                    this.field_110560_d = ImageIO.read(this.cacheFile);

                    if (this.field_110563_c != null)
                    {
                        this.func_147641_a(this.field_110563_c.func_78432_a(this.field_110560_d));
                    }
                }
                catch (IOException ioexception)
                {
                    LOGGER.error("Couldn't load skin {}", this.cacheFile, ioexception);
                    this.func_152433_a();
                }
            }
            else
            {
                this.func_152433_a();
            }
        }
    }

    protected void func_152433_a()
    {
        this.field_110561_e = new Thread("Texture Downloader #" + field_147643_d.incrementAndGet())
        {
            public void run()
            {
                HttpURLConnection httpurlconnection = null;
                ThreadDownloadImageData.LOGGER.debug("Downloading http texture from {} to {}", ThreadDownloadImageData.this.imageUrl, ThreadDownloadImageData.this.cacheFile);

                try
                {
                    httpurlconnection = (HttpURLConnection)(new URL(ThreadDownloadImageData.this.imageUrl)).openConnection(Minecraft.getInstance().getProxy());
                    httpurlconnection.setDoInput(true);
                    httpurlconnection.setDoOutput(false);
                    httpurlconnection.connect();

                    if (httpurlconnection.getResponseCode() / 100 == 2)
                    {
                        BufferedImage bufferedimage;

                        if (ThreadDownloadImageData.this.cacheFile != null)
                        {
                            FileUtils.copyInputStreamToFile(httpurlconnection.getInputStream(), ThreadDownloadImageData.this.cacheFile);
                            bufferedimage = ImageIO.read(ThreadDownloadImageData.this.cacheFile);
                        }
                        else
                        {
                            bufferedimage = TextureUtil.func_177053_a(httpurlconnection.getInputStream());
                        }

                        if (ThreadDownloadImageData.this.field_110563_c != null)
                        {
                            bufferedimage = ThreadDownloadImageData.this.field_110563_c.func_78432_a(bufferedimage);
                        }

                        ThreadDownloadImageData.this.func_147641_a(bufferedimage);
                        return;
                    }
                }
                catch (Exception exception)
                {
                    ThreadDownloadImageData.LOGGER.error("Couldn't download http texture", (Throwable)exception);
                    return;
                }
                finally
                {
                    if (httpurlconnection != null)
                    {
                        httpurlconnection.disconnect();
                    }
                }
            }
        };
        this.field_110561_e.setDaemon(true);
        this.field_110561_e.start();
    }
}
