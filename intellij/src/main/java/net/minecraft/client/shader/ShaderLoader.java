package net.minecraft.client.shader;

import com.google.common.collect.Maps;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.BufferUtils;

public class ShaderLoader
{
    private final ShaderLoader.ShaderType shaderType;
    private final String shaderFilename;
    private final int shader;
    private int shaderAttachCount;

    private ShaderLoader(ShaderLoader.ShaderType type, int shaderId, String filename)
    {
        this.shaderType = type;
        this.shader = shaderId;
        this.shaderFilename = filename;
    }

    public void attachShader(ShaderManager manager)
    {
        ++this.shaderAttachCount;
        OpenGlHelper.func_153178_b(manager.getProgram(), this.shader);
    }

    public void func_148054_b(ShaderManager p_148054_1_)
    {
        --this.shaderAttachCount;

        if (this.shaderAttachCount <= 0)
        {
            OpenGlHelper.func_153180_a(this.shader);
            this.shaderType.getLoadedShaders().remove(this.shaderFilename);
        }
    }

    public String getShaderFilename()
    {
        return this.shaderFilename;
    }

    public static ShaderLoader func_148057_a(IResourceManager p_148057_0_, ShaderLoader.ShaderType p_148057_1_, String p_148057_2_) throws IOException
    {
        ShaderLoader shaderloader = (ShaderLoader)p_148057_1_.getLoadedShaders().get(p_148057_2_);

        if (shaderloader == null)
        {
            ResourceLocation resourcelocation = new ResourceLocation("shaders/program/" + p_148057_2_ + p_148057_1_.getShaderExtension());
            IResource iresource = p_148057_0_.func_110536_a(resourcelocation);

            try
            {
                byte[] abyte = IOUtils.toByteArray(new BufferedInputStream(iresource.func_110527_b()));
                ByteBuffer bytebuffer = BufferUtils.createByteBuffer(abyte.length);
                bytebuffer.put(abyte);
                bytebuffer.position(0);
                int i = OpenGlHelper.func_153195_b(p_148057_1_.getShaderMode());
                OpenGlHelper.func_153169_a(i, bytebuffer);
                OpenGlHelper.func_153170_c(i);

                if (OpenGlHelper.func_153157_c(i, OpenGlHelper.field_153208_p) == 0)
                {
                    String s = StringUtils.trim(OpenGlHelper.func_153158_d(i, 32768));
                    JsonException jsonexception = new JsonException("Couldn't compile " + p_148057_1_.getShaderName() + " program: " + s);
                    jsonexception.setFilenameAndFlush(resourcelocation.getPath());
                    throw jsonexception;
                }

                shaderloader = new ShaderLoader(p_148057_1_, i, p_148057_2_);
                p_148057_1_.getLoadedShaders().put(p_148057_2_, shaderloader);
            }
            finally
            {
                IOUtils.closeQuietly((Closeable)iresource);
            }
        }

        return shaderloader;
    }

    public static enum ShaderType
    {
        VERTEX("vertex", ".vsh", OpenGlHelper.field_153209_q),
        FRAGMENT("fragment", ".fsh", OpenGlHelper.field_153210_r);

        private final String shaderName;
        private final String shaderExtension;
        private final int shaderMode;
        private final Map<String, ShaderLoader> loadedShaders = Maps.<String, ShaderLoader>newHashMap();

        private ShaderType(String shaderNameIn, String shaderExtensionIn, int shaderModeIn)
        {
            this.shaderName = shaderNameIn;
            this.shaderExtension = shaderExtensionIn;
            this.shaderMode = shaderModeIn;
        }

        public String getShaderName()
        {
            return this.shaderName;
        }

        private String getShaderExtension()
        {
            return this.shaderExtension;
        }

        private int getShaderMode()
        {
            return this.shaderMode;
        }

        private Map<String, ShaderLoader> getLoadedShaders()
        {
            return this.loadedShaders;
        }
    }
}
