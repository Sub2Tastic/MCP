package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.util.ResourceLocation;

public class RenderGiantZombie extends RenderLiving<EntityGiantZombie>
{
    private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
    private final float scale;

    public RenderGiantZombie(RenderManager renderManagerIn, float scaleIn)
    {
        super(renderManagerIn, new ModelZombie(), 0.5F * scaleIn);
        this.scale = scaleIn;
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerBipedArmor(this)
        {
            protected void func_177177_a()
            {
                this.modelLeggings = new ModelZombie(0.5F, true);
                this.modelArmor = new ModelZombie(1.0F, true);
            }
        });
    }

    public void func_82422_c()
    {
        GlStateManager.func_179109_b(0.0F, 0.1875F, 0.0F);
    }

    protected void func_77041_b(EntityGiantZombie p_77041_1_, float p_77041_2_)
    {
        GlStateManager.func_179152_a(this.scale, this.scale, this.scale);
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityGiantZombie entity)
    {
        return ZOMBIE_TEXTURES;
    }
}
