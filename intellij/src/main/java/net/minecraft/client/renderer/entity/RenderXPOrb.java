package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderXPOrb extends Render<EntityXPOrb>
{
    private static final ResourceLocation EXPERIENCE_ORB_TEXTURES = new ResourceLocation("textures/entity/experience_orb.png");

    public RenderXPOrb(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    public void func_76986_a(EntityXPOrb p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        if (!this.field_188301_f)
        {
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
            this.func_180548_c(p_76986_1_);
            RenderHelper.func_74519_b();
            int i = p_76986_1_.getTextureByXP();
            float f = (float)(i % 4 * 16 + 0) / 64.0F;
            float f1 = (float)(i % 4 * 16 + 16) / 64.0F;
            float f2 = (float)(i / 4 * 16 + 0) / 64.0F;
            float f3 = (float)(i / 4 * 16 + 16) / 64.0F;
            float f4 = 1.0F;
            float f5 = 0.5F;
            float f6 = 0.25F;
            int j = p_76986_1_.func_70070_b();
            int k = j % 65536;
            int l = j / 65536;
            OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)k, (float)l);
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            float f8 = 255.0F;
            float f9 = ((float)p_76986_1_.xpColor + p_76986_9_) / 2.0F;
            l = (int)((MathHelper.sin(f9 + 0.0F) + 1.0F) * 0.5F * 255.0F);
            int i1 = 255;
            int j1 = (int)((MathHelper.sin(f9 + 4.1887903F) + 1.0F) * 0.1F * 255.0F);
            GlStateManager.func_179109_b(0.0F, 0.1F, 0.0F);
            GlStateManager.func_179114_b(180.0F - this.renderManager.field_78735_i, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179114_b((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.field_78732_j, 1.0F, 0.0F, 0.0F);
            float f7 = 0.3F;
            GlStateManager.func_179152_a(0.3F, 0.3F, 0.3F);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            bufferbuilder.func_181662_b(-0.5D, -0.25D, 0.0D).func_187315_a((double)f, (double)f3).func_181669_b(l, 255, j1, 128).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.func_181662_b(0.5D, -0.25D, 0.0D).func_187315_a((double)f1, (double)f3).func_181669_b(l, 255, j1, 128).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.func_181662_b(0.5D, 0.75D, 0.0D).func_187315_a((double)f1, (double)f2).func_181669_b(l, 255, j1, 128).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.func_181662_b(-0.5D, 0.75D, 0.0D).func_187315_a((double)f, (double)f2).func_181669_b(l, 255, j1, 128).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
            tessellator.draw();
            GlStateManager.func_179084_k();
            GlStateManager.func_179101_C();
            GlStateManager.func_179121_F();
            super.func_76986_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
        }
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityXPOrb entity)
    {
        return EXPERIENCE_ORB_TEXTURES;
    }
}
