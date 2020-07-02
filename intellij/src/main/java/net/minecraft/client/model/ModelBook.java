package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelBook extends ModelBase
{
    public ModelRenderer coverRight = (new ModelRenderer(this)).setTextureOffset(0, 0).func_78789_a(-6.0F, -5.0F, 0.0F, 6, 10, 0);
    public ModelRenderer coverLeft = (new ModelRenderer(this)).setTextureOffset(16, 0).func_78789_a(0.0F, -5.0F, 0.0F, 6, 10, 0);
    public ModelRenderer pagesRight = (new ModelRenderer(this)).setTextureOffset(0, 10).func_78789_a(0.0F, -4.0F, -0.99F, 5, 8, 1);
    public ModelRenderer pagesLeft = (new ModelRenderer(this)).setTextureOffset(12, 10).func_78789_a(0.0F, -4.0F, -0.01F, 5, 8, 1);
    public ModelRenderer flippingPageRight = (new ModelRenderer(this)).setTextureOffset(24, 10).func_78789_a(0.0F, -4.0F, 0.0F, 5, 8, 0);
    public ModelRenderer flippingPageLeft = (new ModelRenderer(this)).setTextureOffset(24, 10).func_78789_a(0.0F, -4.0F, 0.0F, 5, 8, 0);
    public ModelRenderer bookSpine = (new ModelRenderer(this)).setTextureOffset(12, 0).func_78789_a(-1.0F, -5.0F, 0.0F, 2, 10, 0);

    public ModelBook()
    {
        this.coverRight.setRotationPoint(0.0F, 0.0F, -1.0F);
        this.coverLeft.setRotationPoint(0.0F, 0.0F, 1.0F);
        this.bookSpine.rotateAngleY = ((float)Math.PI / 2F);
    }

    public void func_78088_a(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        this.func_78087_a(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.coverRight.func_78785_a(p_78088_7_);
        this.coverLeft.func_78785_a(p_78088_7_);
        this.bookSpine.func_78785_a(p_78088_7_);
        this.pagesRight.func_78785_a(p_78088_7_);
        this.pagesLeft.func_78785_a(p_78088_7_);
        this.flippingPageRight.func_78785_a(p_78088_7_);
        this.flippingPageLeft.func_78785_a(p_78088_7_);
    }

    public void func_78087_a(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        float f = (MathHelper.sin(p_78087_1_ * 0.02F) * 0.1F + 1.25F) * p_78087_4_;
        this.coverRight.rotateAngleY = (float)Math.PI + f;
        this.coverLeft.rotateAngleY = -f;
        this.pagesRight.rotateAngleY = f;
        this.pagesLeft.rotateAngleY = -f;
        this.flippingPageRight.rotateAngleY = f - f * 2.0F * p_78087_2_;
        this.flippingPageLeft.rotateAngleY = f - f * 2.0F * p_78087_3_;
        this.pagesRight.rotationPointX = MathHelper.sin(f);
        this.pagesLeft.rotationPointX = MathHelper.sin(f);
        this.flippingPageRight.rotationPointX = MathHelper.sin(f);
        this.flippingPageLeft.rotationPointX = MathHelper.sin(f);
    }
}
