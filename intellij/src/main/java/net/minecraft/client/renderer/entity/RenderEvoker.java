package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelIllager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;

public class RenderEvoker extends RenderLiving<EntityMob>
{
    private static final ResourceLocation EVOKER_ILLAGER = new ResourceLocation("textures/entity/illager/evoker.png");

    public RenderEvoker(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelIllager(0.0F, 0.0F, 64, 64), 0.5F);
        this.addLayer(new LayerHeldItem(this)
        {
            public void func_177141_a(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
            {
                if (((EntitySpellcasterIllager)p_177141_1_).isSpellcasting())
                {
                    super.func_177141_a(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
                }
            }
            protected void func_191361_a(EnumHandSide p_191361_1_)
            {
                ((ModelIllager)this.field_177206_a.func_177087_b()).getArm(p_191361_1_).func_78794_c(0.0625F);
            }
        });
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityMob entity)
    {
        return EVOKER_ILLAGER;
    }

    protected void func_77041_b(EntityMob p_77041_1_, float p_77041_2_)
    {
        float f = 0.9375F;
        GlStateManager.func_179152_a(0.9375F, 0.9375F, 0.9375F);
    }
}
