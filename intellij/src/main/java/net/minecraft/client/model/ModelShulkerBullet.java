package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelShulkerBullet extends ModelBase
{
    public ModelRenderer renderer;

    public ModelShulkerBullet()
    {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.renderer = new ModelRenderer(this);
        this.renderer.setTextureOffset(0, 0).func_78790_a(-4.0F, -4.0F, -1.0F, 8, 8, 2, 0.0F);
        this.renderer.setTextureOffset(0, 10).func_78790_a(-1.0F, -4.0F, -4.0F, 2, 8, 8, 0.0F);
        this.renderer.setTextureOffset(20, 0).func_78790_a(-4.0F, -1.0F, -4.0F, 8, 2, 8, 0.0F);
        this.renderer.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    public void func_78088_a(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        this.func_78087_a(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.renderer.func_78785_a(p_78088_7_);
    }

    public void func_78087_a(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        super.func_78087_a(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
        this.renderer.rotateAngleY = p_78087_4_ * 0.017453292F;
        this.renderer.rotateAngleX = p_78087_5_ * 0.017453292F;
    }
}
