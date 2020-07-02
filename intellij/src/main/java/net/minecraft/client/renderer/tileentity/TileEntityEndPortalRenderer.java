package net.minecraft.client.renderer.tileentity;

import java.nio.FloatBuffer;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntityEndPortalRenderer extends TileEntitySpecialRenderer<TileEntityEndPortal>
{
    private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
    private static final Random RANDOM = new Random(31100L);
    private static final FloatBuffer field_188201_h = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer field_188202_i = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer field_147528_b = GLAllocation.createDirectFloatBuffer(16);

    public void func_192841_a(TileEntityEndPortal p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_)
    {
        GlStateManager.func_179140_f();
        RANDOM.setSeed(31100L);
        GlStateManager.func_179111_a(2982, field_188201_h);
        GlStateManager.func_179111_a(2983, field_188202_i);
        double d0 = p_192841_2_ * p_192841_2_ + p_192841_4_ * p_192841_4_ + p_192841_6_ * p_192841_6_;
        int i = this.getPasses(d0);
        float f = this.getOffset();
        boolean flag = false;

        for (int j = 0; j < i; ++j)
        {
            GlStateManager.func_179094_E();
            float f1 = 2.0F / (float)(18 - j);

            if (j == 0)
            {
                this.func_147499_a(END_SKY_TEXTURE);
                f1 = 0.15F;
                GlStateManager.func_179147_l();
                GlStateManager.func_187401_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            }

            if (j >= 1)
            {
                this.func_147499_a(END_PORTAL_TEXTURE);
                flag = true;
                Minecraft.getInstance().gameRenderer.func_191514_d(true);
            }

            if (j == 1)
            {
                GlStateManager.func_179147_l();
                GlStateManager.func_187401_a(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            }

            GlStateManager.func_179149_a(GlStateManager.TexGen.S, 9216);
            GlStateManager.func_179149_a(GlStateManager.TexGen.T, 9216);
            GlStateManager.func_179149_a(GlStateManager.TexGen.R, 9216);
            GlStateManager.func_179105_a(GlStateManager.TexGen.S, 9474, this.func_147525_a(1.0F, 0.0F, 0.0F, 0.0F));
            GlStateManager.func_179105_a(GlStateManager.TexGen.T, 9474, this.func_147525_a(0.0F, 1.0F, 0.0F, 0.0F));
            GlStateManager.func_179105_a(GlStateManager.TexGen.R, 9474, this.func_147525_a(0.0F, 0.0F, 1.0F, 0.0F));
            GlStateManager.func_179087_a(GlStateManager.TexGen.S);
            GlStateManager.func_179087_a(GlStateManager.TexGen.T);
            GlStateManager.func_179087_a(GlStateManager.TexGen.R);
            GlStateManager.func_179121_F();
            GlStateManager.func_179128_n(5890);
            GlStateManager.func_179094_E();
            GlStateManager.func_179096_D();
            GlStateManager.func_179109_b(0.5F, 0.5F, 0.0F);
            GlStateManager.func_179152_a(0.5F, 0.5F, 1.0F);
            float f2 = (float)(j + 1);
            GlStateManager.func_179109_b(17.0F / f2, (2.0F + f2 / 1.5F) * ((float)Minecraft.func_71386_F() % 800000.0F / 800000.0F), 0.0F);
            GlStateManager.func_179114_b((f2 * f2 * 4321.0F + f2 * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.func_179152_a(4.5F - f2 / 4.0F, 4.5F - f2 / 4.0F, 1.0F);
            GlStateManager.func_179110_a(field_188202_i);
            GlStateManager.func_179110_a(field_188201_h);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            float f3 = (RANDOM.nextFloat() * 0.5F + 0.1F) * f1;
            float f4 = (RANDOM.nextFloat() * 0.5F + 0.4F) * f1;
            float f5 = (RANDOM.nextFloat() * 0.5F + 0.5F) * f1;

            if (p_192841_1_.shouldRenderFace(EnumFacing.SOUTH))
            {
                bufferbuilder.func_181662_b(p_192841_2_, p_192841_4_, p_192841_6_ + 1.0D).func_181666_a(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.func_181662_b(p_192841_2_ + 1.0D, p_192841_4_, p_192841_6_ + 1.0D).func_181666_a(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.func_181662_b(p_192841_2_ + 1.0D, p_192841_4_ + 1.0D, p_192841_6_ + 1.0D).func_181666_a(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.func_181662_b(p_192841_2_, p_192841_4_ + 1.0D, p_192841_6_ + 1.0D).func_181666_a(f3, f4, f5, 1.0F).endVertex();
            }

            if (p_192841_1_.shouldRenderFace(EnumFacing.NORTH))
            {
                bufferbuilder.func_181662_b(p_192841_2_, p_192841_4_ + 1.0D, p_192841_6_).func_181666_a(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.func_181662_b(p_192841_2_ + 1.0D, p_192841_4_ + 1.0D, p_192841_6_).func_181666_a(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.func_181662_b(p_192841_2_ + 1.0D, p_192841_4_, p_192841_6_).func_181666_a(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.func_181662_b(p_192841_2_, p_192841_4_, p_192841_6_).func_181666_a(f3, f4, f5, 1.0F).endVertex();
            }

            if (p_192841_1_.shouldRenderFace(EnumFacing.EAST))
            {
                bufferbuilder.func_181662_b(p_192841_2_ + 1.0D, p_192841_4_ + 1.0D, p_192841_6_).func_181666_a(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.func_181662_b(p_192841_2_ + 1.0D, p_192841_4_ + 1.0D, p_192841_6_ + 1.0D).func_181666_a(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.func_181662_b(p_192841_2_ + 1.0D, p_192841_4_, p_192841_6_ + 1.0D).func_181666_a(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.func_181662_b(p_192841_2_ + 1.0D, p_192841_4_, p_192841_6_).func_181666_a(f3, f4, f5, 1.0F).endVertex();
            }

            if (p_192841_1_.shouldRenderFace(EnumFacing.WEST))
            {
                bufferbuilder.func_181662_b(p_192841_2_, p_192841_4_, p_192841_6_).func_181666_a(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.func_181662_b(p_192841_2_, p_192841_4_, p_192841_6_ + 1.0D).func_181666_a(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.func_181662_b(p_192841_2_, p_192841_4_ + 1.0D, p_192841_6_ + 1.0D).func_181666_a(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.func_181662_b(p_192841_2_, p_192841_4_ + 1.0D, p_192841_6_).func_181666_a(f3, f4, f5, 1.0F).endVertex();
            }

            if (p_192841_1_.shouldRenderFace(EnumFacing.DOWN))
            {
                bufferbuilder.func_181662_b(p_192841_2_, p_192841_4_, p_192841_6_).func_181666_a(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.func_181662_b(p_192841_2_ + 1.0D, p_192841_4_, p_192841_6_).func_181666_a(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.func_181662_b(p_192841_2_ + 1.0D, p_192841_4_, p_192841_6_ + 1.0D).func_181666_a(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.func_181662_b(p_192841_2_, p_192841_4_, p_192841_6_ + 1.0D).func_181666_a(f3, f4, f5, 1.0F).endVertex();
            }

            if (p_192841_1_.shouldRenderFace(EnumFacing.UP))
            {
                bufferbuilder.func_181662_b(p_192841_2_, p_192841_4_ + (double)f, p_192841_6_ + 1.0D).func_181666_a(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.func_181662_b(p_192841_2_ + 1.0D, p_192841_4_ + (double)f, p_192841_6_ + 1.0D).func_181666_a(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.func_181662_b(p_192841_2_ + 1.0D, p_192841_4_ + (double)f, p_192841_6_).func_181666_a(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.func_181662_b(p_192841_2_, p_192841_4_ + (double)f, p_192841_6_).func_181666_a(f3, f4, f5, 1.0F).endVertex();
            }

            tessellator.draw();
            GlStateManager.func_179121_F();
            GlStateManager.func_179128_n(5888);
            this.func_147499_a(END_SKY_TEXTURE);
        }

        GlStateManager.func_179084_k();
        GlStateManager.func_179100_b(GlStateManager.TexGen.S);
        GlStateManager.func_179100_b(GlStateManager.TexGen.T);
        GlStateManager.func_179100_b(GlStateManager.TexGen.R);
        GlStateManager.func_179145_e();

        if (flag)
        {
            Minecraft.getInstance().gameRenderer.func_191514_d(false);
        }
    }

    protected int getPasses(double p_191286_1_)
    {
        int i;

        if (p_191286_1_ > 36864.0D)
        {
            i = 1;
        }
        else if (p_191286_1_ > 25600.0D)
        {
            i = 3;
        }
        else if (p_191286_1_ > 16384.0D)
        {
            i = 5;
        }
        else if (p_191286_1_ > 9216.0D)
        {
            i = 7;
        }
        else if (p_191286_1_ > 4096.0D)
        {
            i = 9;
        }
        else if (p_191286_1_ > 1024.0D)
        {
            i = 11;
        }
        else if (p_191286_1_ > 576.0D)
        {
            i = 13;
        }
        else if (p_191286_1_ > 256.0D)
        {
            i = 14;
        }
        else
        {
            i = 15;
        }

        return i;
    }

    protected float getOffset()
    {
        return 0.75F;
    }

    private FloatBuffer func_147525_a(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_)
    {
        this.field_147528_b.clear();
        this.field_147528_b.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
        this.field_147528_b.flip();
        return this.field_147528_b;
    }
}
