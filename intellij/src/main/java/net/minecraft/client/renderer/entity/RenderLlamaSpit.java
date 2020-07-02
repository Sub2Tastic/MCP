package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelLlamaSpit;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.util.ResourceLocation;

public class RenderLlamaSpit extends Render<EntityLlamaSpit>
{
    private static final ResourceLocation LLAMA_SPIT_TEXTURE = new ResourceLocation("textures/entity/llama/spit.png");
    private final ModelLlamaSpit model = new ModelLlamaSpit();

    public RenderLlamaSpit(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    public void func_76986_a(EntityLlamaSpit p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)p_76986_2_, (float)p_76986_4_ + 0.15F, (float)p_76986_6_);
        GlStateManager.func_179114_b(p_76986_1_.prevRotationYaw + (p_76986_1_.rotationYaw - p_76986_1_.prevRotationYaw) * p_76986_9_ - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.func_179114_b(p_76986_1_.prevRotationPitch + (p_76986_1_.rotationPitch - p_76986_1_.prevRotationPitch) * p_76986_9_, 0.0F, 0.0F, 1.0F);
        this.func_180548_c(p_76986_1_);

        if (this.field_188301_f)
        {
            GlStateManager.func_179142_g();
            GlStateManager.func_187431_e(this.func_188298_c(p_76986_1_));
        }

        this.model.func_78088_a(p_76986_1_, p_76986_9_, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        if (this.field_188301_f)
        {
            GlStateManager.func_187417_n();
            GlStateManager.func_179119_h();
        }

        GlStateManager.func_179121_F();
        super.func_76986_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityLlamaSpit entity)
    {
        return LLAMA_SPIT_TEXTURE;
    }
}
