package net.minecraft.client.shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.renderer.OpenGlHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

public class ShaderUniform
{
    private static final Logger LOGGER = LogManager.getLogger();
    private int uniformLocation;
    private final int uniformCount;
    private final int uniformType;
    private final IntBuffer uniformIntBuffer;
    private final FloatBuffer uniformFloatBuffer;
    private final String shaderName;
    private boolean dirty;
    private final ShaderManager shaderManager;

    public ShaderUniform(String name, int type, int count, ShaderManager manager)
    {
        this.shaderName = name;
        this.uniformCount = count;
        this.uniformType = type;
        this.shaderManager = manager;

        if (type <= 3)
        {
            this.uniformIntBuffer = BufferUtils.createIntBuffer(count);
            this.uniformFloatBuffer = null;
        }
        else
        {
            this.uniformIntBuffer = null;
            this.uniformFloatBuffer = BufferUtils.createFloatBuffer(count);
        }

        this.uniformLocation = -1;
        this.markDirty();
    }

    private void markDirty()
    {
        this.dirty = true;

        if (this.shaderManager != null)
        {
            this.shaderManager.markDirty();
        }
    }

    public static int parseType(String typeName)
    {
        int i = -1;

        if ("int".equals(typeName))
        {
            i = 0;
        }
        else if ("float".equals(typeName))
        {
            i = 4;
        }
        else if (typeName.startsWith("matrix"))
        {
            if (typeName.endsWith("2x2"))
            {
                i = 8;
            }
            else if (typeName.endsWith("3x3"))
            {
                i = 9;
            }
            else if (typeName.endsWith("4x4"))
            {
                i = 10;
            }
        }

        return i;
    }

    public void setUniformLocation(int uniformLocationIn)
    {
        this.uniformLocation = uniformLocationIn;
    }

    public String getShaderName()
    {
        return this.shaderName;
    }

    public void set(float p_148090_1_)
    {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(0, p_148090_1_);
        this.markDirty();
    }

    public void set(float p_148087_1_, float p_148087_2_)
    {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(0, p_148087_1_);
        this.uniformFloatBuffer.put(1, p_148087_2_);
        this.markDirty();
    }

    public void set(float p_148095_1_, float p_148095_2_, float p_148095_3_)
    {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(0, p_148095_1_);
        this.uniformFloatBuffer.put(1, p_148095_2_);
        this.uniformFloatBuffer.put(2, p_148095_3_);
        this.markDirty();
    }

    public void set(float p_148081_1_, float p_148081_2_, float p_148081_3_, float p_148081_4_)
    {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(p_148081_1_);
        this.uniformFloatBuffer.put(p_148081_2_);
        this.uniformFloatBuffer.put(p_148081_3_);
        this.uniformFloatBuffer.put(p_148081_4_);
        this.uniformFloatBuffer.flip();
        this.markDirty();
    }

    public void setSafe(float p_148092_1_, float p_148092_2_, float p_148092_3_, float p_148092_4_)
    {
        this.uniformFloatBuffer.position(0);

        if (this.uniformType >= 4)
        {
            this.uniformFloatBuffer.put(0, p_148092_1_);
        }

        if (this.uniformType >= 5)
        {
            this.uniformFloatBuffer.put(1, p_148092_2_);
        }

        if (this.uniformType >= 6)
        {
            this.uniformFloatBuffer.put(2, p_148092_3_);
        }

        if (this.uniformType >= 7)
        {
            this.uniformFloatBuffer.put(3, p_148092_4_);
        }

        this.markDirty();
    }

    public void set(int p_148083_1_, int p_148083_2_, int p_148083_3_, int p_148083_4_)
    {
        this.uniformIntBuffer.position(0);

        if (this.uniformType >= 0)
        {
            this.uniformIntBuffer.put(0, p_148083_1_);
        }

        if (this.uniformType >= 1)
        {
            this.uniformIntBuffer.put(1, p_148083_2_);
        }

        if (this.uniformType >= 2)
        {
            this.uniformIntBuffer.put(2, p_148083_3_);
        }

        if (this.uniformType >= 3)
        {
            this.uniformIntBuffer.put(3, p_148083_4_);
        }

        this.markDirty();
    }

    public void set(float[] p_148097_1_)
    {
        if (p_148097_1_.length < this.uniformCount)
        {
            LOGGER.warn("Uniform.set called with a too-small value array (expected {}, got {}). Ignoring.", Integer.valueOf(this.uniformCount), Integer.valueOf(p_148097_1_.length));
        }
        else
        {
            this.uniformFloatBuffer.position(0);
            this.uniformFloatBuffer.put(p_148097_1_);
            this.uniformFloatBuffer.position(0);
            this.markDirty();
        }
    }

    public void func_148094_a(float p_148094_1_, float p_148094_2_, float p_148094_3_, float p_148094_4_, float p_148094_5_, float p_148094_6_, float p_148094_7_, float p_148094_8_, float p_148094_9_, float p_148094_10_, float p_148094_11_, float p_148094_12_, float p_148094_13_, float p_148094_14_, float p_148094_15_, float p_148094_16_)
    {
        this.uniformFloatBuffer.position(0);
        this.uniformFloatBuffer.put(0, p_148094_1_);
        this.uniformFloatBuffer.put(1, p_148094_2_);
        this.uniformFloatBuffer.put(2, p_148094_3_);
        this.uniformFloatBuffer.put(3, p_148094_4_);
        this.uniformFloatBuffer.put(4, p_148094_5_);
        this.uniformFloatBuffer.put(5, p_148094_6_);
        this.uniformFloatBuffer.put(6, p_148094_7_);
        this.uniformFloatBuffer.put(7, p_148094_8_);
        this.uniformFloatBuffer.put(8, p_148094_9_);
        this.uniformFloatBuffer.put(9, p_148094_10_);
        this.uniformFloatBuffer.put(10, p_148094_11_);
        this.uniformFloatBuffer.put(11, p_148094_12_);
        this.uniformFloatBuffer.put(12, p_148094_13_);
        this.uniformFloatBuffer.put(13, p_148094_14_);
        this.uniformFloatBuffer.put(14, p_148094_15_);
        this.uniformFloatBuffer.put(15, p_148094_16_);
        this.markDirty();
    }

    public void func_148088_a(Matrix4f p_148088_1_)
    {
        this.func_148094_a(p_148088_1_.m00, p_148088_1_.m01, p_148088_1_.m02, p_148088_1_.m03, p_148088_1_.m10, p_148088_1_.m11, p_148088_1_.m12, p_148088_1_.m13, p_148088_1_.m20, p_148088_1_.m21, p_148088_1_.m22, p_148088_1_.m23, p_148088_1_.m30, p_148088_1_.m31, p_148088_1_.m32, p_148088_1_.m33);
    }

    public void upload()
    {
        if (!this.dirty)
        {
            ;
        }

        this.dirty = false;

        if (this.uniformType <= 3)
        {
            this.uploadInt();
        }
        else if (this.uniformType <= 7)
        {
            this.uploadFloat();
        }
        else
        {
            if (this.uniformType > 10)
            {
                LOGGER.warn("Uniform.upload called, but type value ({}) is not a valid type. Ignoring.", (int)this.uniformType);
                return;
            }

            this.uploadFloatMatrix();
        }
    }

    private void uploadInt()
    {
        switch (this.uniformType)
        {
            case 0:
                OpenGlHelper.func_153181_a(this.uniformLocation, this.uniformIntBuffer);
                break;

            case 1:
                OpenGlHelper.func_153182_b(this.uniformLocation, this.uniformIntBuffer);
                break;

            case 2:
                OpenGlHelper.func_153192_c(this.uniformLocation, this.uniformIntBuffer);
                break;

            case 3:
                OpenGlHelper.func_153162_d(this.uniformLocation, this.uniformIntBuffer);
                break;

            default:
                LOGGER.warn("Uniform.upload called, but count value ({}) is  not in the range of 1 to 4. Ignoring.", (int)this.uniformCount);
        }
    }

    private void uploadFloat()
    {
        switch (this.uniformType)
        {
            case 4:
                OpenGlHelper.func_153168_a(this.uniformLocation, this.uniformFloatBuffer);
                break;

            case 5:
                OpenGlHelper.func_153177_b(this.uniformLocation, this.uniformFloatBuffer);
                break;

            case 6:
                OpenGlHelper.func_153191_c(this.uniformLocation, this.uniformFloatBuffer);
                break;

            case 7:
                OpenGlHelper.func_153159_d(this.uniformLocation, this.uniformFloatBuffer);
                break;

            default:
                LOGGER.warn("Uniform.upload called, but count value ({}) is not in the range of 1 to 4. Ignoring.", (int)this.uniformCount);
        }
    }

    private void uploadFloatMatrix()
    {
        switch (this.uniformType)
        {
            case 8:
                OpenGlHelper.func_153173_a(this.uniformLocation, true, this.uniformFloatBuffer);
                break;

            case 9:
                OpenGlHelper.func_153189_b(this.uniformLocation, true, this.uniformFloatBuffer);
                break;

            case 10:
                OpenGlHelper.func_153160_c(this.uniformLocation, true, this.uniformFloatBuffer);
        }
    }
}
