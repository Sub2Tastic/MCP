package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelQuadruped extends ModelBase
{
    public ModelRenderer headModel = new ModelRenderer(this, 0, 0);
    public ModelRenderer body;
    public ModelRenderer legBackRight;
    public ModelRenderer legBackLeft;
    public ModelRenderer legFrontRight;
    public ModelRenderer legFrontLeft;
    protected float field_78145_g = 8.0F;
    protected float field_78151_h = 4.0F;

    public ModelQuadruped(int p_i1154_1_, float p_i1154_2_)
    {
        this.headModel.func_78790_a(-4.0F, -4.0F, -8.0F, 8, 8, 8, p_i1154_2_);
        this.headModel.setRotationPoint(0.0F, (float)(18 - p_i1154_1_), -6.0F);
        this.body = new ModelRenderer(this, 28, 8);
        this.body.func_78790_a(-5.0F, -10.0F, -7.0F, 10, 16, 8, p_i1154_2_);
        this.body.setRotationPoint(0.0F, (float)(17 - p_i1154_1_), 2.0F);
        this.legBackRight = new ModelRenderer(this, 0, 16);
        this.legBackRight.func_78790_a(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
        this.legBackRight.setRotationPoint(-3.0F, (float)(24 - p_i1154_1_), 7.0F);
        this.legBackLeft = new ModelRenderer(this, 0, 16);
        this.legBackLeft.func_78790_a(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
        this.legBackLeft.setRotationPoint(3.0F, (float)(24 - p_i1154_1_), 7.0F);
        this.legFrontRight = new ModelRenderer(this, 0, 16);
        this.legFrontRight.func_78790_a(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
        this.legFrontRight.setRotationPoint(-3.0F, (float)(24 - p_i1154_1_), -5.0F);
        this.legFrontLeft = new ModelRenderer(this, 0, 16);
        this.legFrontLeft.func_78790_a(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
        this.legFrontLeft.setRotationPoint(3.0F, (float)(24 - p_i1154_1_), -5.0F);
    }

    public void func_78088_a(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        this.func_78087_a(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);

        if (this.field_78091_s)
        {
            float f = 2.0F;
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b(0.0F, this.field_78145_g * p_78088_7_, this.field_78151_h * p_78088_7_);
            this.headModel.func_78785_a(p_78088_7_);
            GlStateManager.func_179121_F();
            GlStateManager.func_179094_E();
            GlStateManager.func_179152_a(0.5F, 0.5F, 0.5F);
            GlStateManager.func_179109_b(0.0F, 24.0F * p_78088_7_, 0.0F);
            this.body.func_78785_a(p_78088_7_);
            this.legBackRight.func_78785_a(p_78088_7_);
            this.legBackLeft.func_78785_a(p_78088_7_);
            this.legFrontRight.func_78785_a(p_78088_7_);
            this.legFrontLeft.func_78785_a(p_78088_7_);
            GlStateManager.func_179121_F();
        }
        else
        {
            this.headModel.func_78785_a(p_78088_7_);
            this.body.func_78785_a(p_78088_7_);
            this.legBackRight.func_78785_a(p_78088_7_);
            this.legBackLeft.func_78785_a(p_78088_7_);
            this.legFrontRight.func_78785_a(p_78088_7_);
            this.legFrontLeft.func_78785_a(p_78088_7_);
        }
    }

    public void func_78087_a(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        this.headModel.rotateAngleX = p_78087_5_ * 0.017453292F;
        this.headModel.rotateAngleY = p_78087_4_ * 0.017453292F;
        this.body.rotateAngleX = ((float)Math.PI / 2F);
        this.legBackRight.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_;
        this.legBackLeft.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float)Math.PI) * 1.4F * p_78087_2_;
        this.legFrontRight.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float)Math.PI) * 1.4F * p_78087_2_;
        this.legFrontLeft.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_;
    }
}
