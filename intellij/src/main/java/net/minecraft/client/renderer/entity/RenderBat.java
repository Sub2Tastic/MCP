package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderBat extends RenderLiving<EntityBat>
{
    private static final ResourceLocation BAT_TEXTURES = new ResourceLocation("textures/entity/bat.png");

    public RenderBat(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelBat(), 0.25F);
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityBat entity)
    {
        return BAT_TEXTURES;
    }

    protected void func_77041_b(EntityBat p_77041_1_, float p_77041_2_)
    {
        GlStateManager.func_179152_a(0.35F, 0.35F, 0.35F);
    }

    protected void func_77043_a(EntityBat p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_)
    {
        if (p_77043_1_.getIsBatHanging())
        {
            GlStateManager.func_179109_b(0.0F, -0.1F, 0.0F);
        }
        else
        {
            GlStateManager.func_179109_b(0.0F, MathHelper.cos(p_77043_2_ * 0.3F) * 0.1F, 0.0F);
        }

        super.func_77043_a(p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
    }
}
