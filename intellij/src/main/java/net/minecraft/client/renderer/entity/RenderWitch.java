package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItemWitch;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.util.ResourceLocation;

public class RenderWitch extends RenderLiving<EntityWitch>
{
    private static final ResourceLocation WITCH_TEXTURES = new ResourceLocation("textures/entity/witch.png");

    public RenderWitch(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelWitch(0.0F), 0.5F);
        this.addLayer(new LayerHeldItemWitch(this));
    }

    public ModelWitch func_177087_b()
    {
        return (ModelWitch)super.func_177087_b();
    }

    public void func_76986_a(EntityWitch p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        ((ModelWitch)this.entityModel).holdingItem = !p_76986_1_.getHeldItemMainhand().isEmpty();
        super.func_76986_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityWitch entity)
    {
        return WITCH_TEXTURES;
    }

    public void func_82422_c()
    {
        GlStateManager.func_179109_b(0.0F, 0.1875F, 0.0F);
    }

    protected void func_77041_b(EntityWitch p_77041_1_, float p_77041_2_)
    {
        float f = 0.9375F;
        GlStateManager.func_179152_a(0.9375F, 0.9375F, 0.9375F);
    }
}
