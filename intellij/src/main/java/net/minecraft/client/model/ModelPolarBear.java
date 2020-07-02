package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityPolarBear;

public class ModelPolarBear extends ModelQuadruped
{
    public ModelPolarBear()
    {
        super(12, 0.0F);
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.headModel = new ModelRenderer(this, 0, 0);
        this.headModel.func_78790_a(-3.5F, -3.0F, -3.0F, 7, 7, 7, 0.0F);
        this.headModel.setRotationPoint(0.0F, 10.0F, -16.0F);
        this.headModel.setTextureOffset(0, 44).func_78790_a(-2.5F, 1.0F, -6.0F, 5, 3, 3, 0.0F);
        this.headModel.setTextureOffset(26, 0).func_78790_a(-4.5F, -4.0F, -1.0F, 2, 2, 1, 0.0F);
        ModelRenderer modelrenderer = this.headModel.setTextureOffset(26, 0);
        modelrenderer.mirror = true;
        modelrenderer.func_78790_a(2.5F, -4.0F, -1.0F, 2, 2, 1, 0.0F);
        this.body = new ModelRenderer(this);
        this.body.setTextureOffset(0, 19).func_78790_a(-5.0F, -13.0F, -7.0F, 14, 14, 11, 0.0F);
        this.body.setTextureOffset(39, 0).func_78790_a(-4.0F, -25.0F, -7.0F, 12, 12, 10, 0.0F);
        this.body.setRotationPoint(-2.0F, 9.0F, 12.0F);
        int i = 10;
        this.legBackRight = new ModelRenderer(this, 50, 22);
        this.legBackRight.func_78790_a(-2.0F, 0.0F, -2.0F, 4, 10, 8, 0.0F);
        this.legBackRight.setRotationPoint(-3.5F, 14.0F, 6.0F);
        this.legBackLeft = new ModelRenderer(this, 50, 22);
        this.legBackLeft.func_78790_a(-2.0F, 0.0F, -2.0F, 4, 10, 8, 0.0F);
        this.legBackLeft.setRotationPoint(3.5F, 14.0F, 6.0F);
        this.legFrontRight = new ModelRenderer(this, 50, 40);
        this.legFrontRight.func_78790_a(-2.0F, 0.0F, -2.0F, 4, 10, 6, 0.0F);
        this.legFrontRight.setRotationPoint(-2.5F, 14.0F, -7.0F);
        this.legFrontLeft = new ModelRenderer(this, 50, 40);
        this.legFrontLeft.func_78790_a(-2.0F, 0.0F, -2.0F, 4, 10, 6, 0.0F);
        this.legFrontLeft.setRotationPoint(2.5F, 14.0F, -7.0F);
        --this.legBackRight.rotationPointX;
        ++this.legBackLeft.rotationPointX;
        this.legBackRight.rotationPointZ += 0.0F;
        this.legBackLeft.rotationPointZ += 0.0F;
        --this.legFrontRight.rotationPointX;
        ++this.legFrontLeft.rotationPointX;
        --this.legFrontRight.rotationPointZ;
        --this.legFrontLeft.rotationPointZ;
        this.field_78151_h += 2.0F;
    }

    public void func_78088_a(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        this.func_78087_a(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);

        if (this.field_78091_s)
        {
            float f = 2.0F;
            this.field_78145_g = 16.0F;
            this.field_78151_h = 4.0F;
            GlStateManager.func_179094_E();
            GlStateManager.func_179152_a(0.6666667F, 0.6666667F, 0.6666667F);
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
        super.func_78087_a(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
        float f = p_78087_3_ - (float)p_78087_7_.ticksExisted;
        float f1 = ((EntityPolarBear)p_78087_7_).getStandingAnimationScale(f);
        f1 = f1 * f1;
        float f2 = 1.0F - f1;
        this.body.rotateAngleX = ((float)Math.PI / 2F) - f1 * (float)Math.PI * 0.35F;
        this.body.rotationPointY = 9.0F * f2 + 11.0F * f1;
        this.legFrontRight.rotationPointY = 14.0F * f2 + -6.0F * f1;
        this.legFrontRight.rotationPointZ = -8.0F * f2 + -4.0F * f1;
        this.legFrontRight.rotateAngleX -= f1 * (float)Math.PI * 0.45F;
        this.legFrontLeft.rotationPointY = this.legFrontRight.rotationPointY;
        this.legFrontLeft.rotationPointZ = this.legFrontRight.rotationPointZ;
        this.legFrontLeft.rotateAngleX -= f1 * (float)Math.PI * 0.45F;
        this.headModel.rotationPointY = 10.0F * f2 + -12.0F * f1;
        this.headModel.rotationPointZ = -16.0F * f2 + -3.0F * f1;
        this.headModel.rotateAngleX += f1 * (float)Math.PI * 0.15F;
    }
}
