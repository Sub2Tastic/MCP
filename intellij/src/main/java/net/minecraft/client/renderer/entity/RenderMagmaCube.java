package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelMagmaCube;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.util.ResourceLocation;

public class RenderMagmaCube extends RenderLiving<EntityMagmaCube>
{
    private static final ResourceLocation MAGMA_CUBE_TEXTURES = new ResourceLocation("textures/entity/slime/magmacube.png");

    public RenderMagmaCube(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelMagmaCube(), 0.25F);
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityMagmaCube entity)
    {
        return MAGMA_CUBE_TEXTURES;
    }

    protected void func_77041_b(EntityMagmaCube p_77041_1_, float p_77041_2_)
    {
        int i = p_77041_1_.getSlimeSize();
        float f = (p_77041_1_.prevSquishFactor + (p_77041_1_.squishFactor - p_77041_1_.prevSquishFactor) * p_77041_2_) / ((float)i * 0.5F + 1.0F);
        float f1 = 1.0F / (f + 1.0F);
        GlStateManager.func_179152_a(f1 * (float)i, 1.0F / f1 * (float)i, f1 * (float)i);
    }
}
