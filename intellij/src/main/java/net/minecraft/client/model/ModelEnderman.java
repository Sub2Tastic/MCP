package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelEnderman extends ModelBiped
{
    public boolean isCarrying;
    public boolean isAttacking;

    public ModelEnderman(float scale)
    {
        super(0.0F, -14.0F, 64, 32);
        float f = -14.0F;
        this.bipedHeadwear = new ModelRenderer(this, 0, 16);
        this.bipedHeadwear.func_78790_a(-4.0F, -8.0F, -4.0F, 8, 8, 8, scale - 0.5F);
        this.bipedHeadwear.setRotationPoint(0.0F, -14.0F, 0.0F);
        this.bipedBody = new ModelRenderer(this, 32, 16);
        this.bipedBody.func_78790_a(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale);
        this.bipedBody.setRotationPoint(0.0F, -14.0F, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 56, 0);
        this.bipedRightArm.func_78790_a(-1.0F, -2.0F, -1.0F, 2, 30, 2, scale);
        this.bipedRightArm.setRotationPoint(-3.0F, -12.0F, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 56, 0);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.func_78790_a(-1.0F, -2.0F, -1.0F, 2, 30, 2, scale);
        this.bipedLeftArm.setRotationPoint(5.0F, -12.0F, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this, 56, 0);
        this.bipedRightLeg.func_78790_a(-1.0F, 0.0F, -1.0F, 2, 30, 2, scale);
        this.bipedRightLeg.setRotationPoint(-2.0F, -2.0F, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 56, 0);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.func_78790_a(-1.0F, 0.0F, -1.0F, 2, 30, 2, scale);
        this.bipedLeftLeg.setRotationPoint(2.0F, -2.0F, 0.0F);
    }

    public void func_78087_a(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        super.func_78087_a(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
        this.bipedHead.showModel = true;
        float f = -14.0F;
        this.bipedBody.rotateAngleX = 0.0F;
        this.bipedBody.rotationPointY = -14.0F;
        this.bipedBody.rotationPointZ = -0.0F;
        this.bipedRightLeg.rotateAngleX -= 0.0F;
        this.bipedLeftLeg.rotateAngleX -= 0.0F;
        this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX * 0.5D);
        this.bipedLeftArm.rotateAngleX = (float)((double)this.bipedLeftArm.rotateAngleX * 0.5D);
        this.bipedRightLeg.rotateAngleX = (float)((double)this.bipedRightLeg.rotateAngleX * 0.5D);
        this.bipedLeftLeg.rotateAngleX = (float)((double)this.bipedLeftLeg.rotateAngleX * 0.5D);
        float f1 = 0.4F;

        if (this.bipedRightArm.rotateAngleX > 0.4F)
        {
            this.bipedRightArm.rotateAngleX = 0.4F;
        }

        if (this.bipedLeftArm.rotateAngleX > 0.4F)
        {
            this.bipedLeftArm.rotateAngleX = 0.4F;
        }

        if (this.bipedRightArm.rotateAngleX < -0.4F)
        {
            this.bipedRightArm.rotateAngleX = -0.4F;
        }

        if (this.bipedLeftArm.rotateAngleX < -0.4F)
        {
            this.bipedLeftArm.rotateAngleX = -0.4F;
        }

        if (this.bipedRightLeg.rotateAngleX > 0.4F)
        {
            this.bipedRightLeg.rotateAngleX = 0.4F;
        }

        if (this.bipedLeftLeg.rotateAngleX > 0.4F)
        {
            this.bipedLeftLeg.rotateAngleX = 0.4F;
        }

        if (this.bipedRightLeg.rotateAngleX < -0.4F)
        {
            this.bipedRightLeg.rotateAngleX = -0.4F;
        }

        if (this.bipedLeftLeg.rotateAngleX < -0.4F)
        {
            this.bipedLeftLeg.rotateAngleX = -0.4F;
        }

        if (this.isCarrying)
        {
            this.bipedRightArm.rotateAngleX = -0.5F;
            this.bipedLeftArm.rotateAngleX = -0.5F;
            this.bipedRightArm.rotateAngleZ = 0.05F;
            this.bipedLeftArm.rotateAngleZ = -0.05F;
        }

        this.bipedRightArm.rotationPointZ = 0.0F;
        this.bipedLeftArm.rotationPointZ = 0.0F;
        this.bipedRightLeg.rotationPointZ = 0.0F;
        this.bipedLeftLeg.rotationPointZ = 0.0F;
        this.bipedRightLeg.rotationPointY = -5.0F;
        this.bipedLeftLeg.rotationPointY = -5.0F;
        this.bipedHead.rotationPointZ = -0.0F;
        this.bipedHead.rotationPointY = -13.0F;
        this.bipedHeadwear.rotationPointX = this.bipedHead.rotationPointX;
        this.bipedHeadwear.rotationPointY = this.bipedHead.rotationPointY;
        this.bipedHeadwear.rotationPointZ = this.bipedHead.rotationPointZ;
        this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
        this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
        this.bipedHeadwear.rotateAngleZ = this.bipedHead.rotateAngleZ;

        if (this.isAttacking)
        {
            float f2 = 1.0F;
            this.bipedHead.rotationPointY -= 5.0F;
        }
    }
}