package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.util.List;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;

public class WorldVertexBufferUploader
{
    public void draw(BufferBuilder p_181679_1_)
    {
        if (p_181679_1_.func_178989_h() > 0)
        {
            VertexFormat vertexformat = p_181679_1_.func_178973_g();
            int i = vertexformat.getSize();
            ByteBuffer bytebuffer = p_181679_1_.func_178966_f();
            List<VertexFormatElement> list = vertexformat.func_177343_g();

            for (int j = 0; j < list.size(); ++j)
            {
                VertexFormatElement vertexformatelement = list.get(j);
                VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
                int k = vertexformatelement.getType().getGlConstant();
                int l = vertexformatelement.getIndex();
                bytebuffer.position(vertexformat.func_181720_d(j));

                switch (vertexformatelement$enumusage)
                {
                    case POSITION:
                        GlStateManager.func_187427_b(vertexformatelement.getElementCount(), k, i, bytebuffer);
                        GlStateManager.func_187410_q(32884);
                        break;

                    case UV:
                        OpenGlHelper.func_77472_b(OpenGlHelper.field_77478_a + l);
                        GlStateManager.func_187404_a(vertexformatelement.getElementCount(), k, i, bytebuffer);
                        GlStateManager.func_187410_q(32888);
                        OpenGlHelper.func_77472_b(OpenGlHelper.field_77478_a);
                        break;

                    case COLOR:
                        GlStateManager.func_187400_c(vertexformatelement.getElementCount(), k, i, bytebuffer);
                        GlStateManager.func_187410_q(32886);
                        break;

                    case NORMAL:
                        GlStateManager.func_187446_a(k, i, bytebuffer);
                        GlStateManager.func_187410_q(32885);
                }
            }

            GlStateManager.func_187439_f(p_181679_1_.func_178979_i(), 0, p_181679_1_.func_178989_h());
            int i1 = 0;

            for (int j1 = list.size(); i1 < j1; ++i1)
            {
                VertexFormatElement vertexformatelement1 = list.get(i1);
                VertexFormatElement.EnumUsage vertexformatelement$enumusage1 = vertexformatelement1.getUsage();
                int k1 = vertexformatelement1.getIndex();

                switch (vertexformatelement$enumusage1)
                {
                    case POSITION:
                        GlStateManager.func_187429_p(32884);
                        break;

                    case UV:
                        OpenGlHelper.func_77472_b(OpenGlHelper.field_77478_a + k1);
                        GlStateManager.func_187429_p(32888);
                        OpenGlHelper.func_77472_b(OpenGlHelper.field_77478_a);
                        break;

                    case COLOR:
                        GlStateManager.func_187429_p(32886);
                        GlStateManager.func_179117_G();
                        break;

                    case NORMAL:
                        GlStateManager.func_187429_p(32885);
                }
            }
        }

        p_181679_1_.reset();
    }
}
