package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.client.model.ModelArmorStandArmor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderArmorStand extends RenderLivingBase<EntityArmorStand>
{
    /**
     * A constant instance of the armor stand texture, wrapped inside a ResourceLocation wrapper.
     */
    public static final ResourceLocation TEXTURE_ARMOR_STAND = new ResourceLocation("textures/entity/armorstand/wood.png");

    public RenderArmorStand(RenderManager manager)
    {
        super(manager, new ModelArmorStand(), 0.0F);
        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this)
        {
            protected void func_177177_a()
            {
                this.modelLeggings = new ModelArmorStandArmor(0.5F);
                this.modelArmor = new ModelArmorStandArmor(1.0F);
            }
        };
        this.addLayer(layerbipedarmor);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerElytra(this));
        this.addLayer(new LayerCustomHead(this.func_177087_b().bipedHead));
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityArmorStand entity)
    {
        return TEXTURE_ARMOR_STAND;
    }

    public ModelArmorStand func_177087_b()
    {
        return (ModelArmorStand)super.func_177087_b();
    }

    protected void func_77043_a(EntityArmorStand p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_)
    {
        GlStateManager.func_179114_b(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
        float f = (float)(p_77043_1_.world.getGameTime() - p_77043_1_.punchCooldown) + p_77043_4_;

        if (f < 5.0F)
        {
            GlStateManager.func_179114_b(MathHelper.sin(f / 1.5F * (float)Math.PI) * 3.0F, 0.0F, 1.0F, 0.0F);
        }
    }

    protected boolean canRenderName(EntityArmorStand entity)
    {
        return entity.isCustomNameVisible();
    }

    public void func_76986_a(EntityArmorStand p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        if (p_76986_1_.hasMarker())
        {
            this.field_188323_j = true;
        }

        super.func_76986_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);

        if (p_76986_1_.hasMarker())
        {
            this.field_188323_j = false;
        }
    }
}
