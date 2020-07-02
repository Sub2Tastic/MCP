package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelCow;
import net.minecraft.client.renderer.entity.layers.LayerMooshroomMushroom;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.util.ResourceLocation;

public class RenderMooshroom extends RenderLiving<EntityMooshroom>
{
    private static final ResourceLocation field_110880_a = new ResourceLocation("textures/entity/cow/mooshroom.png");

    public RenderMooshroom(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelCow(), 0.7F);
        this.addLayer(new LayerMooshroomMushroom(this));
    }

    public ModelCow func_177087_b()
    {
        return (ModelCow)super.func_177087_b();
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityMooshroom entity)
    {
        return field_110880_a;
    }
}
