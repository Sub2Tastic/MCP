package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelParrot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderParrot extends RenderLiving<EntityParrot>
{
    public static final ResourceLocation[] PARROT_TEXTURES = new ResourceLocation[] {new ResourceLocation("textures/entity/parrot/parrot_red_blue.png"), new ResourceLocation("textures/entity/parrot/parrot_blue.png"), new ResourceLocation("textures/entity/parrot/parrot_green.png"), new ResourceLocation("textures/entity/parrot/parrot_yellow_blue.png"), new ResourceLocation("textures/entity/parrot/parrot_grey.png")};

    public RenderParrot(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelParrot(), 0.3F);
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityParrot entity)
    {
        return PARROT_TEXTURES[entity.getVariant()];
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    public float handleRotationFloat(EntityParrot livingBase, float partialTicks)
    {
        return this.func_192861_b(livingBase, partialTicks);
    }

    private float func_192861_b(EntityParrot p_192861_1_, float p_192861_2_)
    {
        float f = p_192861_1_.oFlap + (p_192861_1_.flap - p_192861_1_.oFlap) * p_192861_2_;
        float f1 = p_192861_1_.oFlapSpeed + (p_192861_1_.flapSpeed - p_192861_1_.oFlapSpeed) * p_192861_2_;
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}
