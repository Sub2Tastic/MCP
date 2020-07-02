package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.ResourceLocation;

public class RenderElderGuardian extends RenderGuardian
{
    private static final ResourceLocation GUARDIAN_ELDER_TEXTURE = new ResourceLocation("textures/entity/guardian_elder.png");

    public RenderElderGuardian(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    protected void func_77041_b(EntityGuardian p_77041_1_, float p_77041_2_)
    {
        GlStateManager.func_179152_a(2.35F, 2.35F, 2.35F);
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityGuardian entity)
    {
        return GUARDIAN_ELDER_TEXTURE;
    }
}
