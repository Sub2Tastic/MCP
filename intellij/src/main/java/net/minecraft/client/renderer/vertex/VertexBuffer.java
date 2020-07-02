package net.minecraft.client.renderer.vertex;

import java.nio.ByteBuffer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;

public class VertexBuffer
{
    private int glBufferId;
    private final VertexFormat vertexFormat;
    private int count;

    public VertexBuffer(VertexFormat vertexFormatIn)
    {
        this.vertexFormat = vertexFormatIn;
        this.glBufferId = OpenGlHelper.func_176073_e();
    }

    public void bindBuffer()
    {
        OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, this.glBufferId);
    }

    public void func_181722_a(ByteBuffer p_181722_1_)
    {
        this.bindBuffer();
        OpenGlHelper.func_176071_a(OpenGlHelper.field_176089_P, p_181722_1_, 35044);
        this.unbindBuffer();
        this.count = p_181722_1_.limit() / this.vertexFormat.getSize();
    }

    public void func_177358_a(int p_177358_1_)
    {
        GlStateManager.func_187439_f(p_177358_1_, 0, this.count);
    }

    public void unbindBuffer()
    {
        OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, 0);
    }

    public void func_177362_c()
    {
        if (this.glBufferId >= 0)
        {
            OpenGlHelper.func_176074_g(this.glBufferId);
            this.glBufferId = -1;
        }
    }
}
