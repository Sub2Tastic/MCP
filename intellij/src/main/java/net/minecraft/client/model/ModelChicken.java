package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelChicken extends ModelBase
{
    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;
    public ModelRenderer rightWing;
    public ModelRenderer leftWing;
    public ModelRenderer bill;
    public ModelRenderer chin;

    public ModelChicken()
    {
        int i = 16;
        this.head = new ModelRenderer(this, 0, 0);
        this.head.func_78790_a(-2.0F, -6.0F, -2.0F, 4, 6, 3, 0.0F);
        this.head.setRotationPoint(0.0F, 15.0F, -4.0F);
        this.bill = new ModelRenderer(this, 14, 0);
        this.bill.func_78790_a(-2.0F, -4.0F, -4.0F, 4, 2, 2, 0.0F);
        this.bill.setRotationPoint(0.0F, 15.0F, -4.0F);
        this.chin = new ModelRenderer(this, 14, 4);
        this.chin.func_78790_a(-1.0F, -2.0F, -3.0F, 2, 2, 2, 0.0F);
        this.chin.setRotationPoint(0.0F, 15.0F, -4.0F);
        this.body = new ModelRenderer(this, 0, 9);
        this.body.func_78790_a(-3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F);
        this.body.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.rightLeg = new ModelRenderer(this, 26, 0);
        this.rightLeg.func_78789_a(-1.0F, 0.0F, -3.0F, 3, 5, 3);
        this.rightLeg.setRotationPoint(-2.0F, 19.0F, 1.0F);
        this.leftLeg = new ModelRenderer(this, 26, 0);
        this.leftLeg.func_78789_a(-1.0F, 0.0F, -3.0F, 3, 5, 3);
        this.leftLeg.setRotationPoint(1.0F, 19.0F, 1.0F);
        this.rightWing = new ModelRenderer(this, 24, 13);
        this.rightWing.func_78789_a(0.0F, 0.0F, -3.0F, 1, 4, 6);
        this.rightWing.setRotationPoint(-4.0F, 13.0F, 0.0F);
        this.leftWing = new ModelRenderer(this, 24, 13);
        this.leftWing.func_78789_a(-1.0F, 0.0F, -3.0F, 1, 4, 6);
        this.leftWing.setRotationPoint(4.0F, 13.0F, 0.0F);
    }

    public void func_78088_a(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        this.func_78087_a(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);

        if (this.field_78091_s)
        {
            float f = 2.0F;
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b(0.0F, 5.0F * p_78088_7_, 2.0F * p_78088_7_);
            this.head.func_78785_a(p_78088_7_);
            this.bill.func_78785_a(p_78088_7_);
            this.chin.func_78785_a(p_78088_7_);
            GlStateManager.func_179121_F();
            GlStateManager.func_179094_E();
            GlStateManager.func_179152_a(0.5F, 0.5F, 0.5F);
            GlStateManager.func_179109_b(0.0F, 24.0F * p_78088_7_, 0.0F);
            this.body.func_78785_a(p_78088_7_);
            this.rightLeg.func_78785_a(p_78088_7_);
            this.leftLeg.func_78785_a(p_78088_7_);
            this.rightWing.func_78785_a(p_78088_7_);
            this.leftWing.func_78785_a(p_78088_7_);
            GlStateManager.func_179121_F();
        }
        else
        {
            this.head.func_78785_a(p_78088_7_);
            this.bill.func_78785_a(p_78088_7_);
            this.chin.func_78785_a(p_78088_7_);
            this.body.func_78785_a(p_78088_7_);
            this.rightLeg.func_78785_a(p_78088_7_);
            this.leftLeg.func_78785_a(p_78088_7_);
            this.rightWing.func_78785_a(p_78088_7_);
            this.leftWing.func_78785_a(p_78088_7_);
        }
    }

    public void func_78087_a(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        this.head.rotateAngleX = p_78087_5_ * 0.017453292F;
        this.head.rotateAngleY = p_78087_4_ * 0.017453292F;
        this.bill.rotateAngleX = this.head.rotateAngleX;
        this.bill.rotateAngleY = this.head.rotateAngleY;
        this.chin.rotateAngleX = this.head.rotateAngleX;
        this.chin.rotateAngleY = this.head.rotateAngleY;
        this.body.rotateAngleX = ((float)Math.PI / 2F);
        this.rightLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_;
        this.leftLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float)Math.PI) * 1.4F * p_78087_2_;
        this.rightWing.rotateAngleZ = p_78087_3_;
        this.leftWing.rotateAngleZ = -p_78087_3_;
    }
}
