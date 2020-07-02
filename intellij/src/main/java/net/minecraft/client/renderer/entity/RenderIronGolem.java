package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerIronGolemFlower;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;

public class RenderIronGolem extends RenderLiving<EntityIronGolem>
{
    private static final ResourceLocation IRON_GOLEM_TEXTURES = new ResourceLocation("textures/entity/iron_golem.png");

    public RenderIronGolem(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelIronGolem(), 0.5F);
        this.addLayer(new LayerIronGolemFlower(this));
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityIronGolem entity)
    {
        return IRON_GOLEM_TEXTURES;
    }

    protected void func_77043_a(EntityIronGolem p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_)
    {
        super.func_77043_a(p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);

        if ((double)p_77043_1_.limbSwingAmount >= 0.01D)
        {
            float f = 13.0F;
            float f1 = p_77043_1_.limbSwing - p_77043_1_.limbSwingAmount * (1.0F - p_77043_4_) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            GlStateManager.func_179114_b(6.5F * f2, 0.0F, 0.0F, 1.0F);
        }
    }
}
