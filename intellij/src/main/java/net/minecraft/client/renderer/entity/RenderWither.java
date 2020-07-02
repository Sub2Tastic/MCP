package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelWither;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerWitherAura;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.ResourceLocation;

public class RenderWither extends RenderLiving<EntityWither>
{
    private static final ResourceLocation INVULNERABLE_WITHER_TEXTURES = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
    private static final ResourceLocation WITHER_TEXTURES = new ResourceLocation("textures/entity/wither/wither.png");

    public RenderWither(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelWither(0.0F), 1.0F);
        this.addLayer(new LayerWitherAura(this));
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityWither entity)
    {
        int i = entity.getInvulTime();
        return i > 0 && (i > 80 || i / 5 % 2 != 1) ? INVULNERABLE_WITHER_TEXTURES : WITHER_TEXTURES;
    }

    protected void func_77041_b(EntityWither p_77041_1_, float p_77041_2_)
    {
        float f = 2.0F;
        int i = p_77041_1_.getInvulTime();

        if (i > 0)
        {
            f -= ((float)i - p_77041_2_) / 220.0F * 0.5F;
        }

        GlStateManager.func_179152_a(f, f, f);
    }
}
