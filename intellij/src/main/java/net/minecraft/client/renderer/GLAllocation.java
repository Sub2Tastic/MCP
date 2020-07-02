package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.util.glu.GLU;

public class GLAllocation
{

    public static synchronized int func_74526_a(int p_74526_0_)
    {
        int i = GlStateManager.func_187442_t(p_74526_0_);

        if (i == 0)
        {
            int j = GlStateManager.func_187434_L();
            String s = "No error code reported";

            if (j != 0)
            {
                s = GLU.gluErrorString(j);
            }

            throw new IllegalStateException("glGenLists returned an ID of 0 for a count of " + p_74526_0_ + ", GL error (" + j + "): " + s);
        }
        else
        {
            return i;
        }
    }

    public static synchronized void func_178874_a(int p_178874_0_, int p_178874_1_)
    {
        GlStateManager.func_187449_e(p_178874_0_, p_178874_1_);
    }

    public static synchronized void func_74523_b(int p_74523_0_)
    {
        func_178874_a(p_74523_0_, 1);
    }

    /**
     * Creates and returns a direct byte buffer with the specified capacity. Applies native ordering to speed up access.
     */
    public static synchronized ByteBuffer createDirectByteBuffer(int capacity)
    {
        return ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
    }

    public static IntBuffer func_74527_f(int p_74527_0_)
    {
        return createDirectByteBuffer(p_74527_0_ << 2).asIntBuffer();
    }

    /**
     * Creates and returns a direct float buffer with the specified capacity. Applies native ordering to speed up
     * access.
     */
    public static FloatBuffer createDirectFloatBuffer(int capacity)
    {
        return createDirectByteBuffer(capacity << 2).asFloatBuffer();
    }
}
