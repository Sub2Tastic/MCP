package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.util.math.MathHelper;

public class ModelRabbit extends ModelBase
{
    /** The Rabbit's Left Foot */
    private final ModelRenderer rabbitLeftFoot;

    /** The Rabbit's Right Foot */
    private final ModelRenderer rabbitRightFoot;

    /** The Rabbit's Left Thigh */
    private final ModelRenderer rabbitLeftThigh;

    /** The Rabbit's Right Thigh */
    private final ModelRenderer rabbitRightThigh;

    /** The Rabbit's Body */
    private final ModelRenderer rabbitBody;

    /** The Rabbit's Left Arm */
    private final ModelRenderer rabbitLeftArm;

    /** The Rabbit's Right Arm */
    private final ModelRenderer rabbitRightArm;

    /** The Rabbit's Head */
    private final ModelRenderer rabbitHead;

    /** The Rabbit's Right Ear */
    private final ModelRenderer rabbitRightEar;

    /** The Rabbit's Left Ear */
    private final ModelRenderer rabbitLeftEar;

    /** The Rabbit's Tail */
    private final ModelRenderer rabbitTail;

    /** The Rabbit's Nose */
    private final ModelRenderer rabbitNose;
    private float jumpRotation;

    public ModelRabbit()
    {
        this.func_78085_a("head.main", 0, 0);
        this.func_78085_a("head.nose", 0, 24);
        this.func_78085_a("head.ear1", 0, 10);
        this.func_78085_a("head.ear2", 6, 10);
        this.rabbitLeftFoot = new ModelRenderer(this, 26, 24);
        this.rabbitLeftFoot.func_78789_a(-1.0F, 5.5F, -3.7F, 2, 1, 7);
        this.rabbitLeftFoot.setRotationPoint(3.0F, 17.5F, 3.7F);
        this.rabbitLeftFoot.mirror = true;
        this.setRotationOffset(this.rabbitLeftFoot, 0.0F, 0.0F, 0.0F);
        this.rabbitRightFoot = new ModelRenderer(this, 8, 24);
        this.rabbitRightFoot.func_78789_a(-1.0F, 5.5F, -3.7F, 2, 1, 7);
        this.rabbitRightFoot.setRotationPoint(-3.0F, 17.5F, 3.7F);
        this.rabbitRightFoot.mirror = true;
        this.setRotationOffset(this.rabbitRightFoot, 0.0F, 0.0F, 0.0F);
        this.rabbitLeftThigh = new ModelRenderer(this, 30, 15);
        this.rabbitLeftThigh.func_78789_a(-1.0F, 0.0F, 0.0F, 2, 4, 5);
        this.rabbitLeftThigh.setRotationPoint(3.0F, 17.5F, 3.7F);
        this.rabbitLeftThigh.mirror = true;
        this.setRotationOffset(this.rabbitLeftThigh, -0.34906584F, 0.0F, 0.0F);
        this.rabbitRightThigh = new ModelRenderer(this, 16, 15);
        this.rabbitRightThigh.func_78789_a(-1.0F, 0.0F, 0.0F, 2, 4, 5);
        this.rabbitRightThigh.setRotationPoint(-3.0F, 17.5F, 3.7F);
        this.rabbitRightThigh.mirror = true;
        this.setRotationOffset(this.rabbitRightThigh, -0.34906584F, 0.0F, 0.0F);
        this.rabbitBody = new ModelRenderer(this, 0, 0);
        this.rabbitBody.func_78789_a(-3.0F, -2.0F, -10.0F, 6, 5, 10);
        this.rabbitBody.setRotationPoint(0.0F, 19.0F, 8.0F);
        this.rabbitBody.mirror = true;
        this.setRotationOffset(this.rabbitBody, -0.34906584F, 0.0F, 0.0F);
        this.rabbitLeftArm = new ModelRenderer(this, 8, 15);
        this.rabbitLeftArm.func_78789_a(-1.0F, 0.0F, -1.0F, 2, 7, 2);
        this.rabbitLeftArm.setRotationPoint(3.0F, 17.0F, -1.0F);
        this.rabbitLeftArm.mirror = true;
        this.setRotationOffset(this.rabbitLeftArm, -0.17453292F, 0.0F, 0.0F);
        this.rabbitRightArm = new ModelRenderer(this, 0, 15);
        this.rabbitRightArm.func_78789_a(-1.0F, 0.0F, -1.0F, 2, 7, 2);
        this.rabbitRightArm.setRotationPoint(-3.0F, 17.0F, -1.0F);
        this.rabbitRightArm.mirror = true;
        this.setRotationOffset(this.rabbitRightArm, -0.17453292F, 0.0F, 0.0F);
        this.rabbitHead = new ModelRenderer(this, 32, 0);
        this.rabbitHead.func_78789_a(-2.5F, -4.0F, -5.0F, 5, 4, 5);
        this.rabbitHead.setRotationPoint(0.0F, 16.0F, -1.0F);
        this.rabbitHead.mirror = true;
        this.setRotationOffset(this.rabbitHead, 0.0F, 0.0F, 0.0F);
        this.rabbitRightEar = new ModelRenderer(this, 52, 0);
        this.rabbitRightEar.func_78789_a(-2.5F, -9.0F, -1.0F, 2, 5, 1);
        this.rabbitRightEar.setRotationPoint(0.0F, 16.0F, -1.0F);
        this.rabbitRightEar.mirror = true;
        this.setRotationOffset(this.rabbitRightEar, 0.0F, -0.2617994F, 0.0F);
        this.rabbitLeftEar = new ModelRenderer(this, 58, 0);
        this.rabbitLeftEar.func_78789_a(0.5F, -9.0F, -1.0F, 2, 5, 1);
        this.rabbitLeftEar.setRotationPoint(0.0F, 16.0F, -1.0F);
        this.rabbitLeftEar.mirror = true;
        this.setRotationOffset(this.rabbitLeftEar, 0.0F, 0.2617994F, 0.0F);
        this.rabbitTail = new ModelRenderer(this, 52, 6);
        this.rabbitTail.func_78789_a(-1.5F, -1.5F, 0.0F, 3, 3, 2);
        this.rabbitTail.setRotationPoint(0.0F, 20.0F, 7.0F);
        this.rabbitTail.mirror = true;
        this.setRotationOffset(this.rabbitTail, -0.3490659F, 0.0F, 0.0F);
        this.rabbitNose = new ModelRenderer(this, 32, 9);
        this.rabbitNose.func_78789_a(-0.5F, -2.5F, -5.5F, 1, 1, 1);
        this.rabbitNose.setRotationPoint(0.0F, 16.0F, -1.0F);
        this.rabbitNose.mirror = true;
        this.setRotationOffset(this.rabbitNose, 0.0F, 0.0F, 0.0F);
    }

    private void setRotationOffset(ModelRenderer renderer, float x, float y, float z)
    {
        renderer.rotateAngleX = x;
        renderer.rotateAngleY = y;
        renderer.rotateAngleZ = z;
    }

    public void func_78088_a(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        this.func_78087_a(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);

        if (this.field_78091_s)
        {
            float f = 1.5F;
            GlStateManager.func_179094_E();
            GlStateManager.func_179152_a(0.56666666F, 0.56666666F, 0.56666666F);
            GlStateManager.func_179109_b(0.0F, 22.0F * p_78088_7_, 2.0F * p_78088_7_);
            this.rabbitHead.func_78785_a(p_78088_7_);
            this.rabbitLeftEar.func_78785_a(p_78088_7_);
            this.rabbitRightEar.func_78785_a(p_78088_7_);
            this.rabbitNose.func_78785_a(p_78088_7_);
            GlStateManager.func_179121_F();
            GlStateManager.func_179094_E();
            GlStateManager.func_179152_a(0.4F, 0.4F, 0.4F);
            GlStateManager.func_179109_b(0.0F, 36.0F * p_78088_7_, 0.0F);
            this.rabbitLeftFoot.func_78785_a(p_78088_7_);
            this.rabbitRightFoot.func_78785_a(p_78088_7_);
            this.rabbitLeftThigh.func_78785_a(p_78088_7_);
            this.rabbitRightThigh.func_78785_a(p_78088_7_);
            this.rabbitBody.func_78785_a(p_78088_7_);
            this.rabbitLeftArm.func_78785_a(p_78088_7_);
            this.rabbitRightArm.func_78785_a(p_78088_7_);
            this.rabbitTail.func_78785_a(p_78088_7_);
            GlStateManager.func_179121_F();
        }
        else
        {
            GlStateManager.func_179094_E();
            GlStateManager.func_179152_a(0.6F, 0.6F, 0.6F);
            GlStateManager.func_179109_b(0.0F, 16.0F * p_78088_7_, 0.0F);
            this.rabbitLeftFoot.func_78785_a(p_78088_7_);
            this.rabbitRightFoot.func_78785_a(p_78088_7_);
            this.rabbitLeftThigh.func_78785_a(p_78088_7_);
            this.rabbitRightThigh.func_78785_a(p_78088_7_);
            this.rabbitBody.func_78785_a(p_78088_7_);
            this.rabbitLeftArm.func_78785_a(p_78088_7_);
            this.rabbitRightArm.func_78785_a(p_78088_7_);
            this.rabbitHead.func_78785_a(p_78088_7_);
            this.rabbitRightEar.func_78785_a(p_78088_7_);
            this.rabbitLeftEar.func_78785_a(p_78088_7_);
            this.rabbitTail.func_78785_a(p_78088_7_);
            this.rabbitNose.func_78785_a(p_78088_7_);
            GlStateManager.func_179121_F();
        }
    }

    public void func_78087_a(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        float f = p_78087_3_ - (float)p_78087_7_.ticksExisted;
        EntityRabbit entityrabbit = (EntityRabbit)p_78087_7_;
        this.rabbitNose.rotateAngleX = p_78087_5_ * 0.017453292F;
        this.rabbitHead.rotateAngleX = p_78087_5_ * 0.017453292F;
        this.rabbitRightEar.rotateAngleX = p_78087_5_ * 0.017453292F;
        this.rabbitLeftEar.rotateAngleX = p_78087_5_ * 0.017453292F;
        this.rabbitNose.rotateAngleY = p_78087_4_ * 0.017453292F;
        this.rabbitHead.rotateAngleY = p_78087_4_ * 0.017453292F;
        this.rabbitRightEar.rotateAngleY = this.rabbitNose.rotateAngleY - 0.2617994F;
        this.rabbitLeftEar.rotateAngleY = this.rabbitNose.rotateAngleY + 0.2617994F;
        this.jumpRotation = MathHelper.sin(entityrabbit.getJumpCompletion(f) * (float)Math.PI);
        this.rabbitLeftThigh.rotateAngleX = (this.jumpRotation * 50.0F - 21.0F) * 0.017453292F;
        this.rabbitRightThigh.rotateAngleX = (this.jumpRotation * 50.0F - 21.0F) * 0.017453292F;
        this.rabbitLeftFoot.rotateAngleX = this.jumpRotation * 50.0F * 0.017453292F;
        this.rabbitRightFoot.rotateAngleX = this.jumpRotation * 50.0F * 0.017453292F;
        this.rabbitLeftArm.rotateAngleX = (this.jumpRotation * -40.0F - 11.0F) * 0.017453292F;
        this.rabbitRightArm.rotateAngleX = (this.jumpRotation * -40.0F - 11.0F) * 0.017453292F;
    }

    public void func_78086_a(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_)
    {
        super.func_78086_a(p_78086_1_, p_78086_2_, p_78086_3_, p_78086_4_);
        this.jumpRotation = MathHelper.sin(((EntityRabbit)p_78086_1_).getJumpCompletion(p_78086_4_) * (float)Math.PI);
    }
}
