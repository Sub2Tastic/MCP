package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.AbstractIllager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class ModelIllager extends ModelBase
{
    public ModelRenderer head;
    public ModelRenderer hat;
    public ModelRenderer body;
    public ModelRenderer arms;
    public ModelRenderer field_191220_d;
    public ModelRenderer field_191221_e;
    public ModelRenderer field_191222_f;
    public ModelRenderer rightArm;
    public ModelRenderer leftArm;

    public ModelIllager(float scaleFactor, float p_i47227_2_, int textureWidthIn, int textureHeightIn)
    {
        this.head = (new ModelRenderer(this)).setTextureSize(textureWidthIn, textureHeightIn);
        this.head.setRotationPoint(0.0F, 0.0F + p_i47227_2_, 0.0F);
        this.head.setTextureOffset(0, 0).func_78790_a(-4.0F, -10.0F, -4.0F, 8, 10, 8, scaleFactor);
        this.hat = (new ModelRenderer(this, 32, 0)).setTextureSize(textureWidthIn, textureHeightIn);
        this.hat.func_78790_a(-4.0F, -10.0F, -4.0F, 8, 12, 8, scaleFactor + 0.45F);
        this.head.addChild(this.hat);
        this.hat.showModel = false;
        this.field_191222_f = (new ModelRenderer(this)).setTextureSize(textureWidthIn, textureHeightIn);
        this.field_191222_f.setRotationPoint(0.0F, p_i47227_2_ - 2.0F, 0.0F);
        this.field_191222_f.setTextureOffset(24, 0).func_78790_a(-1.0F, -1.0F, -6.0F, 2, 4, 2, scaleFactor);
        this.head.addChild(this.field_191222_f);
        this.body = (new ModelRenderer(this)).setTextureSize(textureWidthIn, textureHeightIn);
        this.body.setRotationPoint(0.0F, 0.0F + p_i47227_2_, 0.0F);
        this.body.setTextureOffset(16, 20).func_78790_a(-4.0F, 0.0F, -3.0F, 8, 12, 6, scaleFactor);
        this.body.setTextureOffset(0, 38).func_78790_a(-4.0F, 0.0F, -3.0F, 8, 18, 6, scaleFactor + 0.5F);
        this.arms = (new ModelRenderer(this)).setTextureSize(textureWidthIn, textureHeightIn);
        this.arms.setRotationPoint(0.0F, 0.0F + p_i47227_2_ + 2.0F, 0.0F);
        this.arms.setTextureOffset(44, 22).func_78790_a(-8.0F, -2.0F, -2.0F, 4, 8, 4, scaleFactor);
        ModelRenderer modelrenderer = (new ModelRenderer(this, 44, 22)).setTextureSize(textureWidthIn, textureHeightIn);
        modelrenderer.mirror = true;
        modelrenderer.func_78790_a(4.0F, -2.0F, -2.0F, 4, 8, 4, scaleFactor);
        this.arms.addChild(modelrenderer);
        this.arms.setTextureOffset(40, 38).func_78790_a(-4.0F, 2.0F, -2.0F, 8, 4, 4, scaleFactor);
        this.field_191220_d = (new ModelRenderer(this, 0, 22)).setTextureSize(textureWidthIn, textureHeightIn);
        this.field_191220_d.setRotationPoint(-2.0F, 12.0F + p_i47227_2_, 0.0F);
        this.field_191220_d.func_78790_a(-2.0F, 0.0F, -2.0F, 4, 12, 4, scaleFactor);
        this.field_191221_e = (new ModelRenderer(this, 0, 22)).setTextureSize(textureWidthIn, textureHeightIn);
        this.field_191221_e.mirror = true;
        this.field_191221_e.setRotationPoint(2.0F, 12.0F + p_i47227_2_, 0.0F);
        this.field_191221_e.func_78790_a(-2.0F, 0.0F, -2.0F, 4, 12, 4, scaleFactor);
        this.rightArm = (new ModelRenderer(this, 40, 46)).setTextureSize(textureWidthIn, textureHeightIn);
        this.rightArm.func_78790_a(-3.0F, -2.0F, -2.0F, 4, 12, 4, scaleFactor);
        this.rightArm.setRotationPoint(-5.0F, 2.0F + p_i47227_2_, 0.0F);
        this.leftArm = (new ModelRenderer(this, 40, 46)).setTextureSize(textureWidthIn, textureHeightIn);
        this.leftArm.mirror = true;
        this.leftArm.func_78790_a(-1.0F, -2.0F, -2.0F, 4, 12, 4, scaleFactor);
        this.leftArm.setRotationPoint(5.0F, 2.0F + p_i47227_2_, 0.0F);
    }

    public void func_78088_a(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        this.func_78087_a(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.head.func_78785_a(p_78088_7_);
        this.body.func_78785_a(p_78088_7_);
        this.field_191220_d.func_78785_a(p_78088_7_);
        this.field_191221_e.func_78785_a(p_78088_7_);
        AbstractIllager abstractillager = (AbstractIllager)p_78088_1_;

        if (abstractillager.getArmPose() == AbstractIllager.IllagerArmPose.CROSSED)
        {
            this.arms.func_78785_a(p_78088_7_);
        }
        else
        {
            this.rightArm.func_78785_a(p_78088_7_);
            this.leftArm.func_78785_a(p_78088_7_);
        }
    }

    public void func_78087_a(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        this.head.rotateAngleY = p_78087_4_ * 0.017453292F;
        this.head.rotateAngleX = p_78087_5_ * 0.017453292F;
        this.arms.rotationPointY = 3.0F;
        this.arms.rotationPointZ = -1.0F;
        this.arms.rotateAngleX = -0.75F;
        this.field_191220_d.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_ * 0.5F;
        this.field_191221_e.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float)Math.PI) * 1.4F * p_78087_2_ * 0.5F;
        this.field_191220_d.rotateAngleY = 0.0F;
        this.field_191221_e.rotateAngleY = 0.0F;
        AbstractIllager.IllagerArmPose abstractillager$illagerarmpose = ((AbstractIllager)p_78087_7_).getArmPose();

        if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.ATTACKING)
        {
            float f = MathHelper.sin(this.field_78095_p * (float)Math.PI);
            float f1 = MathHelper.sin((1.0F - (1.0F - this.field_78095_p) * (1.0F - this.field_78095_p)) * (float)Math.PI);
            this.rightArm.rotateAngleZ = 0.0F;
            this.leftArm.rotateAngleZ = 0.0F;
            this.rightArm.rotateAngleY = 0.15707964F;
            this.leftArm.rotateAngleY = -0.15707964F;

            if (((EntityLivingBase)p_78087_7_).getPrimaryHand() == EnumHandSide.RIGHT)
            {
                this.rightArm.rotateAngleX = -1.8849558F + MathHelper.cos(p_78087_3_ * 0.09F) * 0.15F;
                this.leftArm.rotateAngleX = -0.0F + MathHelper.cos(p_78087_3_ * 0.19F) * 0.5F;
                this.rightArm.rotateAngleX += f * 2.2F - f1 * 0.4F;
                this.leftArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
            }
            else
            {
                this.rightArm.rotateAngleX = -0.0F + MathHelper.cos(p_78087_3_ * 0.19F) * 0.5F;
                this.leftArm.rotateAngleX = -1.8849558F + MathHelper.cos(p_78087_3_ * 0.09F) * 0.15F;
                this.rightArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
                this.leftArm.rotateAngleX += f * 2.2F - f1 * 0.4F;
            }

            this.rightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
            this.leftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
            this.rightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
            this.leftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
        }
        else if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.SPELLCASTING)
        {
            this.rightArm.rotationPointZ = 0.0F;
            this.rightArm.rotationPointX = -5.0F;
            this.leftArm.rotationPointZ = 0.0F;
            this.leftArm.rotationPointX = 5.0F;
            this.rightArm.rotateAngleX = MathHelper.cos(p_78087_3_ * 0.6662F) * 0.25F;
            this.leftArm.rotateAngleX = MathHelper.cos(p_78087_3_ * 0.6662F) * 0.25F;
            this.rightArm.rotateAngleZ = 2.3561945F;
            this.leftArm.rotateAngleZ = -2.3561945F;
            this.rightArm.rotateAngleY = 0.0F;
            this.leftArm.rotateAngleY = 0.0F;
        }
        else if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.BOW_AND_ARROW)
        {
            this.rightArm.rotateAngleY = -0.1F + this.head.rotateAngleY;
            this.rightArm.rotateAngleX = -((float)Math.PI / 2F) + this.head.rotateAngleX;
            this.leftArm.rotateAngleX = -0.9424779F + this.head.rotateAngleX;
            this.leftArm.rotateAngleY = this.head.rotateAngleY - 0.4F;
            this.leftArm.rotateAngleZ = ((float)Math.PI / 2F);
        }
    }

    public ModelRenderer getArm(EnumHandSide p_191216_1_)
    {
        return p_191216_1_ == EnumHandSide.LEFT ? this.leftArm : this.rightArm;
    }
}
