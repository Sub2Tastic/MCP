package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;

public class ModelPlayer extends ModelBiped
{
    public ModelRenderer bipedLeftArmwear;
    public ModelRenderer bipedRightArmwear;
    public ModelRenderer bipedLeftLegwear;
    public ModelRenderer bipedRightLegwear;
    public ModelRenderer bipedBodyWear;
    private final ModelRenderer bipedCape;
    private final ModelRenderer bipedDeadmau5Head;
    private final boolean smallArms;

    public ModelPlayer(float modelSize, boolean smallArmsIn)
    {
        super(modelSize, 0.0F, 64, 64);
        this.smallArms = smallArmsIn;
        this.bipedDeadmau5Head = new ModelRenderer(this, 24, 0);
        this.bipedDeadmau5Head.func_78790_a(-3.0F, -6.0F, -1.0F, 6, 6, 1, modelSize);
        this.bipedCape = new ModelRenderer(this, 0, 0);
        this.bipedCape.setTextureSize(64, 32);
        this.bipedCape.func_78790_a(-5.0F, 0.0F, -1.0F, 10, 16, 1, modelSize);

        if (smallArmsIn)
        {
            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
            this.bipedLeftArm.func_78790_a(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
            this.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);
            this.bipedRightArm = new ModelRenderer(this, 40, 16);
            this.bipedRightArm.func_78790_a(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
            this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);
            this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
            this.bipedLeftArmwear.func_78790_a(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
            this.bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);
            this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
            this.bipedRightArmwear.func_78790_a(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
            this.bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);
        }
        else
        {
            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
            this.bipedLeftArm.func_78790_a(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
            this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
            this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
            this.bipedLeftArmwear.func_78790_a(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
            this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
            this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
            this.bipedRightArmwear.func_78790_a(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
            this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
        }

        this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
        this.bipedLeftLeg.func_78790_a(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
        this.bipedLeftLegwear.func_78790_a(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
        this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
        this.bipedRightLegwear.func_78790_a(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
        this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.bipedBodyWear = new ModelRenderer(this, 16, 32);
        this.bipedBodyWear.func_78790_a(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize + 0.25F);
        this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
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
            this.bipedLeftLegwear.func_78785_a(p_78088_7_);
            this.bipedRightLegwear.func_78785_a(p_78088_7_);
            this.bipedLeftArmwear.func_78785_a(p_78088_7_);
            this.bipedRightArmwear.func_78785_a(p_78088_7_);
            this.bipedBodyWear.func_78785_a(p_78088_7_);
        }
        else
        {
            if (p_78088_1_.func_70093_af())
            {
                GlStateManager.func_179109_b(0.0F, 0.2F, 0.0F);
            }

            this.bipedLeftLegwear.func_78785_a(p_78088_7_);
            this.bipedRightLegwear.func_78785_a(p_78088_7_);
            this.bipedLeftArmwear.func_78785_a(p_78088_7_);
            this.bipedRightArmwear.func_78785_a(p_78088_7_);
            this.bipedBodyWear.func_78785_a(p_78088_7_);
        }

        GlStateManager.func_179121_F();
    }

    public void func_178727_b(float p_178727_1_)
    {
        func_178685_a(this.bipedHead, this.bipedDeadmau5Head);
        this.bipedDeadmau5Head.rotationPointX = 0.0F;
        this.bipedDeadmau5Head.rotationPointY = 0.0F;
        this.bipedDeadmau5Head.func_78785_a(p_178727_1_);
    }

    public void func_178728_c(float p_178728_1_)
    {
        this.bipedCape.func_78785_a(p_178728_1_);
    }

    public void func_78087_a(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        super.func_78087_a(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
        func_178685_a(this.bipedLeftLeg, this.bipedLeftLegwear);
        func_178685_a(this.bipedRightLeg, this.bipedRightLegwear);
        func_178685_a(this.bipedLeftArm, this.bipedLeftArmwear);
        func_178685_a(this.bipedRightArm, this.bipedRightArmwear);
        func_178685_a(this.bipedBody, this.bipedBodyWear);

        if (p_78087_7_.func_70093_af())
        {
            this.bipedCape.rotationPointY = 2.0F;
        }
        else
        {
            this.bipedCape.rotationPointY = 0.0F;
        }
    }

    public void setVisible(boolean visible)
    {
        super.setVisible(visible);
        this.bipedLeftArmwear.showModel = visible;
        this.bipedRightArmwear.showModel = visible;
        this.bipedLeftLegwear.showModel = visible;
        this.bipedRightLegwear.showModel = visible;
        this.bipedBodyWear.showModel = visible;
        this.bipedCape.showModel = visible;
        this.bipedDeadmau5Head.showModel = visible;
    }

    public void func_187073_a(float p_187073_1_, EnumHandSide p_187073_2_)
    {
        ModelRenderer modelrenderer = this.getArmForSide(p_187073_2_);

        if (this.smallArms)
        {
            float f = 0.5F * (float)(p_187073_2_ == EnumHandSide.RIGHT ? 1 : -1);
            modelrenderer.rotationPointX += f;
            modelrenderer.func_78794_c(p_187073_1_);
            modelrenderer.rotationPointX -= f;
        }
        else
        {
            modelrenderer.func_78794_c(p_187073_1_);
        }
    }
}
