package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerWolfCollar;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;

public class RenderWolf extends RenderLiving<EntityWolf>
{
    private static final ResourceLocation WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf.png");
    private static final ResourceLocation TAMED_WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf_tame.png");
    private static final ResourceLocation ANGRY_WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf_angry.png");

    public RenderWolf(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelWolf(), 0.5F);
        this.addLayer(new LayerWolfCollar(this));
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(EntityWolf livingBase, float partialTicks)
    {
        return livingBase.getTailRotation();
    }

    public void func_76986_a(EntityWolf p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        if (p_76986_1_.isWolfWet())
        {
            float f = p_76986_1_.getBrightness() * p_76986_1_.getShadingWhileWet(p_76986_9_);
            GlStateManager.func_179124_c(f, f, f);
        }

        super.func_76986_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityWolf entity)
    {
        if (entity.isTamed())
        {
            return TAMED_WOLF_TEXTURES;
        }
        else
        {
            return entity.isAngry() ? ANGRY_WOLF_TEXTURES : WOLF_TEXTURES;
        }
    }
}
