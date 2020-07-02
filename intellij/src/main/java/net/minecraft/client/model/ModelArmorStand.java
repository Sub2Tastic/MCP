package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.EnumHandSide;

public class ModelArmorStand extends ModelArmorStandArmor
{
    public ModelRenderer standRightSide;
    public ModelRenderer standLeftSide;
    public ModelRenderer standWaist;
    public ModelRenderer standBase;

    public ModelArmorStand()
    {
        this(0.0F);
    }

    public ModelArmorStand(float modelSize)
    {
        super(modelSize, 64, 64);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.func_78790_a(-1.0F, -7.0F, -1.0F, 2, 7, 2, modelSize);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody = new ModelRenderer(this, 0, 26);
        this.bipedBody.func_78790_a(-6.0F, 0.0F, -1.5F, 12, 3, 3, modelSize);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 24, 0);
        this.bipedRightArm.func_78790_a(-2.0F, -2.0F, -1.0F, 2, 12, 2, modelSize);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 32, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.func_78790_a(0.0F, -2.0F, -1.0F, 2, 12, 2, modelSize);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this, 8, 0);
        this.bipedRightLeg.func_78790_a(-1.0F, 0.0F, -1.0F, 2, 11, 2, modelSize);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 40, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.func_78790_a(-1.0F, 0.0F, -1.0F, 2, 11, 2, modelSize);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.standRightSide = new ModelRenderer(this, 16, 0);
        this.standRightSide.func_78790_a(-3.0F, 3.0F, -1.0F, 2, 7, 2, modelSize);
        this.standRightSide.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.standRightSide.showModel = true;
        this.standLeftSide = new ModelRenderer(this, 48, 16);
        this.standLeftSide.func_78790_a(1.0F, 3.0F, -1.0F, 2, 7, 2, modelSize);
        this.standLeftSide.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.standWaist = new ModelRenderer(this, 0, 48);
        this.standWaist.func_78790_a(-4.0F, 10.0F, -1.0F, 8, 2, 2, modelSize);
        this.standWaist.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.standBase = new ModelRenderer(this, 0, 32);
        this.standBase.func_78790_a(-6.0F, 11.0F, -6.0F, 12, 1, 12, modelSize);
        this.standBase.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.bipedHeadwear.showModel = false;
    }

    public void func_78087_a(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        super.func_78087_a(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);

        if (p_78087_7_ instanceof EntityArmorStand)
        {
            EntityArmorStand entityarmorstand = (EntityArmorStand)p_78087_7_;
            this.bipedLeftArm.showModel = entityarmorstand.getShowArms();
            this.bipedRightArm.showModel = entityarmorstand.getShowArms();
            this.standBase.showModel = !entityarmorstand.hasNoBasePlate();
            this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
            this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
            this.standRightSide.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
            this.standRightSide.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
            this.standRightSide.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
            this.standLeftSide.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
            this.standLeftSide.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
            this.standLeftSide.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
            this.standWaist.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
            this.standWaist.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
            this.standWaist.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
            this.standBase.rotateAngleX = 0.0F;
            this.standBase.rotateAngleY = 0.017453292F * -p_78087_7_.rotationYaw;
            this.standBase.rotateAngleZ = 0.0F;
        }
    }

    public void func_78088_a(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        super.func_78088_a(p_78088_1_, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_);
        GlStateManager.func_179094_E();

        if (this.field_78091_s)
        {
            float f = 2.0F;
            GlStateManager.func_179152_a(0.5F, 0.5F, 0.5F);
            GlStateManager.func_179109_b(0.0F, 24.0F * p_78088_7_, 0.0F);
            this.standRightSide.func_78785_a(p_78088_7_);
            this.standLeftSide.func_78785_a(p_78088_7_);
            this.standWaist.func_78785_a(p_78088_7_);
            this.standBase.func_78785_a(p_78088_7_);
        }
        else
        {
            if (p_78088_1_.func_70093_af())
            {
                GlStateManager.func_179109_b(0.0F, 0.2F, 0.0F);
            }

            this.standRightSide.func_78785_a(p_78088_7_);
            this.standLeftSide.func_78785_a(p_78088_7_);
            this.standWaist.func_78785_a(p_78088_7_);
            this.standBase.func_78785_a(p_78088_7_);
        }

        GlStateManager.func_179121_F();
    }

    public void func_187073_a(float p_187073_1_, EnumHandSide p_187073_2_)
    {
        ModelRenderer modelrenderer = this.getArmForSide(p_187073_2_);
        boolean flag = modelrenderer.showModel;
        modelrenderer.showModel = true;
        super.func_187073_a(p_187073_1_, p_187073_2_);
        modelrenderer.showModel = flag;
    }
}
