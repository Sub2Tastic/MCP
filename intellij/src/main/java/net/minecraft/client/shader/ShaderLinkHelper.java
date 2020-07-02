package net.minecraft.client.shader;

import java.io.IOException;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.util.JsonException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShaderLinkHelper
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static ShaderLinkHelper field_148079_b;

    public static void func_148076_a()
    {
        field_148079_b = new ShaderLinkHelper();
    }

    public static ShaderLinkHelper func_148074_b()
    {
        return field_148079_b;
    }

    public void deleteShader(ShaderManager p_148077_1_)
    {
        p_148077_1_.getFragmentShaderLoader().func_148054_b(p_148077_1_);
        p_148077_1_.getVertexShaderLoader().func_148054_b(p_148077_1_);
        OpenGlHelper.func_153187_e(p_148077_1_.getProgram());
    }

    public int createProgram() throws JsonException
    {
        int i = OpenGlHelper.func_153183_d();

        if (i <= 0)
        {
            throw new JsonException("Could not create shader program (returned program ID " + i + ")");
        }
        else
        {
            return i;
        }
    }

    public void linkProgram(ShaderManager p_148075_1_) throws IOException
    {
        p_148075_1_.getFragmentShaderLoader().attachShader(p_148075_1_);
        p_148075_1_.getVertexShaderLoader().attachShader(p_148075_1_);
        OpenGlHelper.func_153179_f(p_148075_1_.getProgram());
        int i = OpenGlHelper.func_153175_a(p_148075_1_.getProgram(), OpenGlHelper.field_153207_o);

        if (i == 0)
        {
            LOGGER.warn("Error encountered when linking program containing VS {} and FS {}. Log output:", p_148075_1_.getVertexShaderLoader().getShaderFilename(), p_148075_1_.getFragmentShaderLoader().getShaderFilename());
            LOGGER.warn(OpenGlHelper.func_153166_e(p_148075_1_.getProgram(), 32768));
        }
    }
}
