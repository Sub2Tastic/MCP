package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;

public class RenderHusk extends RenderZombie
{
    private static final ResourceLocation HUSK_ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/husk.png");

    public RenderHusk(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    protected void func_77041_b(EntityZombie p_77041_1_, float p_77041_2_)
    {
        float f = 1.0625F;
        GlStateManager.func_179152_a(1.0625F, 1.0625F, 1.0625F);
        super.func_77041_b(p_77041_1_, p_77041_2_);
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityZombie entity)
    {
        return HUSK_ZOMBIE_TEXTURES;
    }
}
