package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.math.MathHelper;

public class ModelWolf extends ModelBase
{
    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer legBackRight;
    public ModelRenderer legBackLeft;
    public ModelRenderer legFrontRight;
    public ModelRenderer legFrontLeft;
    ModelRenderer tail;
    ModelRenderer mane;

    public ModelWolf()
    {
        float f = 0.0F;
        float f1 = 13.5F;
        this.head = new ModelRenderer(this, 0, 0);
        this.head.func_78790_a(-2.0F, -3.0F, -2.0F, 6, 6, 4, 0.0F);
        this.head.setRotationPoint(-1.0F, 13.5F, -7.0F);
        this.body = new ModelRenderer(this, 18, 14);
        this.body.func_78790_a(-3.0F, -2.0F, -3.0F, 6, 9, 6, 0.0F);
        this.body.setRotationPoint(0.0F, 14.0F, 2.0F);
        this.mane = new ModelRenderer(this, 21, 0);
        this.mane.func_78790_a(-3.0F, -3.0F, -3.0F, 8, 6, 7, 0.0F);
        this.mane.setRotationPoint(-1.0F, 14.0F, 2.0F);
        this.legBackRight = new ModelRenderer(this, 0, 18);
        this.legBackRight.func_78790_a(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.legBackRight.setRotationPoint(-2.5F, 16.0F, 7.0F);
        this.legBackLeft = new ModelRenderer(this, 0, 18);
        this.legBackLeft.func_78790_a(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.legBackLeft.setRotationPoint(0.5F, 16.0F, 7.0F);
        this.legFrontRight = new ModelRenderer(this, 0, 18);
        this.legFrontRight.func_78790_a(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.legFrontRight.setRotationPoint(-2.5F, 16.0F, -4.0F);
        this.legFrontLeft = new ModelRenderer(this, 0, 18);
        this.legFrontLeft.func_78790_a(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.legFrontLeft.setRotationPoint(0.5F, 16.0F, -4.0F);
        this.tail = new ModelRenderer(this, 9, 18);
        this.tail.func_78790_a(0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F);
        this.tail.setRotationPoint(-1.0F, 12.0F, 8.0F);
        this.head.setTextureOffset(16, 14).func_78790_a(-2.0F, -5.0F, 0.0F, 2, 2, 1, 0.0F);
        this.head.setTextureOffset(16, 14).func_78790_a(2.0F, -5.0F, 0.0F, 2, 2, 1, 0.0F);
        this.head.setTextureOffset(0, 10).func_78790_a(-0.5F, 0.0F, -5.0F, 3, 3, 4, 0.0F);
    }

    public void func_78088_a(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        super.func_78088_a(p_78088_1_, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_);
        this.func_78087_a(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);

        if (this.field_78091_s)
        {
            float f = 2.0F;
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b(0.0F, 5.0F * p_78088_7_, 2.0F * p_78088_7_);
            this.head.func_78791_b(p_78088_7_);
            GlStateManager.func_179121_F();
            GlStateManager.func_179094_E();
            GlStateManager.func_179152_a(0.5F, 0.5F, 0.5F);
            GlStateManager.func_179109_b(0.0F, 24.0F * p_78088_7_, 0.0F);
            this.body.func_78785_a(p_78088_7_);
            this.legBackRight.func_78785_a(p_78088_7_);
            this.legBackLeft.func_78785_a(p_78088_7_);
            this.legFrontRight.func_78785_a(p_78088_7_);
            this.legFrontLeft.func_78785_a(p_78088_7_);
            this.tail.func_78791_b(p_78088_7_);
            this.mane.func_78785_a(p_78088_7_);
            GlStateManager.func_179121_F();
        }
        else
        {
            this.head.func_78791_b(p_78088_7_);
            this.body.func_78785_a(p_78088_7_);
            this.legBackRight.func_78785_a(p_78088_7_);
            this.legBackLeft.func_78785_a(p_78088_7_);
            this.legFrontRight.func_78785_a(p_78088_7_);
            this.legFrontLeft.func_78785_a(p_78088_7_);
            this.tail.func_78791_b(p_78088_7_);
            this.mane.func_78785_a(p_78088_7_);
        }
    }

    public void func_78086_a(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_)
    {
        EntityWolf entitywolf = (EntityWolf)p_78086_1_;

        if (entitywolf.isAngry())
        {
            this.tail.rotateAngleY = 0.0F;
        }
        else
        {
            this.tail.rotateAngleY = MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_;
        }

        if (entitywolf.isSitting())
        {
            this.mane.setRotationPoint(-1.0F, 16.0F, -3.0F);
            this.mane.rotateAngleX = ((float)Math.PI * 2F / 5F);
            this.mane.rotateAngleY = 0.0F;
            this.body.setRotationPoint(0.0F, 18.0F, 0.0F);
            this.body.rotateAngleX = ((float)Math.PI / 4F);
            this.tail.setRotationPoint(-1.0F, 21.0F, 6.0F);
            this.legBackRight.setRotationPoint(-2.5F, 22.0F, 2.0F);
            this.legBackRight.rotateAngleX = ((float)Math.PI * 3F / 2F);
            this.legBackLeft.setRotationPoint(0.5F, 22.0F, 2.0F);
            this.legBackLeft.rotateAngleX = ((float)Math.PI * 3F / 2F);
            this.legFrontRight.rotateAngleX = 5.811947F;
            this.legFrontRight.setRotationPoint(-2.49F, 17.0F, -4.0F);
            this.legFrontLeft.rotateAngleX = 5.811947F;
            this.legFrontLeft.setRotationPoint(0.51F, 17.0F, -4.0F);
        }
        else
        {
            this.body.setRotationPoint(0.0F, 14.0F, 2.0F);
            this.body.rotateAngleX = ((float)Math.PI / 2F);
            this.mane.setRotationPoint(-1.0F, 14.0F, -3.0F);
            this.mane.rotateAngleX = this.body.rotateAngleX;
            this.tail.setRotationPoint(-1.0F, 12.0F, 8.0F);
            this.legBackRight.setRotationPoint(-2.5F, 16.0F, 7.0F);
            this.legBackLeft.setRotationPoint(0.5F, 16.0F, 7.0F);
            this.legFrontRight.setRotationPoint(-2.5F, 16.0F, -4.0F);
            this.legFrontLeft.setRotationPoint(0.5F, 16.0F, -4.0F);
            this.legBackRight.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_;
            this.legBackLeft.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F + (float)Math.PI) * 1.4F * p_78086_3_;
            this.legFrontRight.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F + (float)Math.PI) * 1.4F * p_78086_3_;
            this.legFrontLeft.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_;
        }

        this.head.rotateAngleZ = entitywolf.getInterestedAngle(p_78086_4_) + entitywolf.getShakeAngle(p_78086_4_, 0.0F);
        this.mane.rotateAngleZ = entitywolf.getShakeAngle(p_78086_4_, -0.08F);
        this.body.rotateAngleZ = entitywolf.getShakeAngle(p_78086_4_, -0.16F);
        this.tail.rotateAngleZ = entitywolf.getShakeAngle(p_78086_4_, -0.2F);
    }

    public void func_78087_a(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        super.func_78087_a(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
        this.head.rotateAngleX = p_78087_5_ * 0.017453292F;
        this.head.rotateAngleY = p_78087_4_ * 0.017453292F;
        this.tail.rotateAngleX = p_78087_3_;
    }
}
