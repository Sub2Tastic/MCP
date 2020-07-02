package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelIllager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RenderIllusionIllager extends RenderLiving<EntityMob>
{
    private static final ResourceLocation ILLUSIONIST = new ResourceLocation("textures/entity/illager/illusionist.png");

    public RenderIllusionIllager(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelIllager(0.0F, 0.0F, 64, 64), 0.5F);
        this.addLayer(new LayerHeldItem(this)
        {
            public void func_177141_a(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
            {
                if (((EntityIllusionIllager)p_177141_1_).isSpellcasting() || ((EntityIllusionIllager)p_177141_1_).func_193096_dj())
                {
                    super.func_177141_a(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
                }
            }
            protected void func_191361_a(EnumHandSide p_191361_1_)
            {
                ((ModelIllager)this.field_177206_a.func_177087_b()).getArm(p_191361_1_).func_78794_c(0.0625F);
            }
        });
        ((ModelIllager)this.func_177087_b()).hat.showModel = true;
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityMob entity)
    {
        return ILLUSIONIST;
    }

    protected void func_77041_b(EntityMob p_77041_1_, float p_77041_2_)
    {
        float f = 0.9375F;
        GlStateManager.func_179152_a(0.9375F, 0.9375F, 0.9375F);
    }

    public void func_76986_a(EntityMob p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        if (p_76986_1_.isInvisible())
        {
            Vec3d[] avec3d = ((EntityIllusionIllager)p_76986_1_).getRenderLocations(p_76986_9_);
            float f = this.handleRotationFloat(p_76986_1_, p_76986_9_);

            for (int i = 0; i < avec3d.length; ++i)
            {
                super.func_76986_a(p_76986_1_, p_76986_2_ + avec3d[i].x + (double)MathHelper.cos((float)i + f * 0.5F) * 0.025D, p_76986_4_ + avec3d[i].y + (double)MathHelper.cos((float)i + f * 0.75F) * 0.0125D, p_76986_6_ + avec3d[i].z + (double)MathHelper.cos((float)i + f * 0.7F) * 0.025D, p_76986_8_, p_76986_9_);
            }
        }
        else
        {
            super.func_76986_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
        }
    }

    public void func_177067_a(EntityMob p_177067_1_, double p_177067_2_, double p_177067_4_, double p_177067_6_)
    {
        super.func_177067_a(p_177067_1_, p_177067_2_, p_177067_4_, p_177067_6_);
    }

    protected boolean func_193115_c(EntityMob p_193115_1_)
    {
        return true;
    }
}
