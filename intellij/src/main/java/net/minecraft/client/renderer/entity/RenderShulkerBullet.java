package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelShulkerBullet;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderShulkerBullet extends Render<EntityShulkerBullet>
{
    private static final ResourceLocation SHULKER_SPARK_TEXTURE = new ResourceLocation("textures/entity/shulker/spark.png");
    private final ModelShulkerBullet model = new ModelShulkerBullet();

    public RenderShulkerBullet(RenderManager manager)
    {
        super(manager);
    }

    private float func_188347_a(float p_188347_1_, float p_188347_2_, float p_188347_3_)
    {
        float f;

        for (f = p_188347_2_ - p_188347_1_; f < -180.0F; f += 360.0F)
        {
            ;
        }

        while (f >= 180.0F)
        {
            f -= 360.0F;
        }

        return p_188347_1_ + p_188347_3_ * f;
    }

    public void func_76986_a(EntityShulkerBullet p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        GlStateManager.func_179094_E();
        float f = this.func_188347_a(p_76986_1_.prevRotationYaw, p_76986_1_.rotationYaw, p_76986_9_);
        float f1 = p_76986_1_.prevRotationPitch + (p_76986_1_.rotationPitch - p_76986_1_.prevRotationPitch) * p_76986_9_;
        float f2 = (float)p_76986_1_.ticksExisted + p_76986_9_;
        GlStateManager.func_179109_b((float)p_76986_2_, (float)p_76986_4_ + 0.15F, (float)p_76986_6_);
        GlStateManager.func_179114_b(MathHelper.sin(f2 * 0.1F) * 180.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.func_179114_b(MathHelper.cos(f2 * 0.1F) * 180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.func_179114_b(MathHelper.sin(f2 * 0.15F) * 360.0F, 0.0F, 0.0F, 1.0F);
        float f3 = 0.03125F;
        GlStateManager.func_179091_B();
        GlStateManager.func_179152_a(-1.0F, -1.0F, 1.0F);
        this.func_180548_c(p_76986_1_);
        this.model.func_78088_a(p_76986_1_, 0.0F, 0.0F, 0.0F, f, f1, 0.03125F);
        GlStateManager.func_179147_l();
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.5F);
        GlStateManager.func_179152_a(1.5F, 1.5F, 1.5F);
        this.model.func_78088_a(p_76986_1_, 0.0F, 0.0F, 0.0F, f, f1, 0.03125F);
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
        super.func_76986_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityShulkerBullet entity)
    {
        return SHULKER_SPARK_TEXTURE;
    }
}
