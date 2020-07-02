package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.IMultipassModel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderBoat extends Render<EntityBoat>
{
    private static final ResourceLocation[] BOAT_TEXTURES = new ResourceLocation[] {new ResourceLocation("textures/entity/boat/boat_oak.png"), new ResourceLocation("textures/entity/boat/boat_spruce.png"), new ResourceLocation("textures/entity/boat/boat_birch.png"), new ResourceLocation("textures/entity/boat/boat_jungle.png"), new ResourceLocation("textures/entity/boat/boat_acacia.png"), new ResourceLocation("textures/entity/boat/boat_darkoak.png")};
    protected ModelBase modelBoat = new ModelBoat();

    public RenderBoat(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }

    public void func_76986_a(EntityBoat p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        GlStateManager.func_179094_E();
        this.func_188309_a(p_76986_2_, p_76986_4_, p_76986_6_);
        this.func_188311_a(p_76986_1_, p_76986_8_, p_76986_9_);
        this.func_180548_c(p_76986_1_);

        if (this.field_188301_f)
        {
            GlStateManager.func_179142_g();
            GlStateManager.func_187431_e(this.func_188298_c(p_76986_1_));
        }

        this.modelBoat.func_78088_a(p_76986_1_, p_76986_9_, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        if (this.field_188301_f)
        {
            GlStateManager.func_187417_n();
            GlStateManager.func_179119_h();
        }

        GlStateManager.func_179121_F();
        super.func_76986_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    public void func_188311_a(EntityBoat p_188311_1_, float p_188311_2_, float p_188311_3_)
    {
        GlStateManager.func_179114_b(180.0F - p_188311_2_, 0.0F, 1.0F, 0.0F);
        float f = (float)p_188311_1_.getTimeSinceHit() - p_188311_3_;
        float f1 = p_188311_1_.getDamageTaken() - p_188311_3_;

        if (f1 < 0.0F)
        {
            f1 = 0.0F;
        }

        if (f > 0.0F)
        {
            GlStateManager.func_179114_b(MathHelper.sin(f) * f * f1 / 10.0F * (float)p_188311_1_.getForwardDirection(), 1.0F, 0.0F, 0.0F);
        }

        GlStateManager.func_179152_a(-1.0F, -1.0F, 1.0F);
    }

    public void func_188309_a(double p_188309_1_, double p_188309_3_, double p_188309_5_)
    {
        GlStateManager.func_179109_b((float)p_188309_1_, (float)p_188309_3_ + 0.375F, (float)p_188309_5_);
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityBoat entity)
    {
        return BOAT_TEXTURES[entity.getBoatType().ordinal()];
    }

    public boolean func_188295_H_()
    {
        return true;
    }

    public void func_188300_b(EntityBoat p_188300_1_, double p_188300_2_, double p_188300_4_, double p_188300_6_, float p_188300_8_, float p_188300_9_)
    {
        GlStateManager.func_179094_E();
        this.func_188309_a(p_188300_2_, p_188300_4_, p_188300_6_);
        this.func_188311_a(p_188300_1_, p_188300_8_, p_188300_9_);
        this.func_180548_c(p_188300_1_);
        ((IMultipassModel)this.modelBoat).func_187054_b(p_188300_1_, p_188300_9_, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.func_179121_F();
    }
}
