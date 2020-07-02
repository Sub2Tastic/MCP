package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;

public class RenderWitherSkeleton extends RenderSkeleton
{
    private static final ResourceLocation WITHER_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");

    public RenderWitherSkeleton(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(AbstractSkeleton entity)
    {
        return WITHER_SKELETON_TEXTURES;
    }

    protected void func_77041_b(AbstractSkeleton p_77041_1_, float p_77041_2_)
    {
        GlStateManager.func_179152_a(1.2F, 1.2F, 1.2F);
    }
}
