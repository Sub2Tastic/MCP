package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractChestHorse;

public class ModelLlama extends ModelQuadruped
{
    private final ModelRenderer chest1;
    private final ModelRenderer chest2;

    public ModelLlama(float p_i47226_1_)
    {
        super(15, p_i47226_1_);
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.headModel = new ModelRenderer(this, 0, 0);
        this.headModel.func_78790_a(-2.0F, -14.0F, -10.0F, 4, 4, 9, p_i47226_1_);
        this.headModel.setRotationPoint(0.0F, 7.0F, -6.0F);
        this.headModel.setTextureOffset(0, 14).func_78790_a(-4.0F, -16.0F, -6.0F, 8, 18, 6, p_i47226_1_);
        this.headModel.setTextureOffset(17, 0).func_78790_a(-4.0F, -19.0F, -4.0F, 3, 3, 2, p_i47226_1_);
        this.headModel.setTextureOffset(17, 0).func_78790_a(1.0F, -19.0F, -4.0F, 3, 3, 2, p_i47226_1_);
        this.body = new ModelRenderer(this, 29, 0);
        this.body.func_78790_a(-6.0F, -10.0F, -7.0F, 12, 18, 10, p_i47226_1_);
        this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
        this.chest1 = new ModelRenderer(this, 45, 28);
        this.chest1.func_78790_a(-3.0F, 0.0F, 0.0F, 8, 8, 3, p_i47226_1_);
        this.chest1.setRotationPoint(-8.5F, 3.0F, 3.0F);
        this.chest1.rotateAngleY = ((float)Math.PI / 2F);
        this.chest2 = new ModelRenderer(this, 45, 41);
        this.chest2.func_78790_a(-3.0F, 0.0F, 0.0F, 8, 8, 3, p_i47226_1_);
        this.chest2.setRotationPoint(5.5F, 3.0F, 3.0F);
        this.chest2.rotateAngleY = ((float)Math.PI / 2F);
        int i = 4;
        int j = 14;
        this.legBackRight = new ModelRenderer(this, 29, 29);
        this.legBackRight.func_78790_a(-2.0F, 0.0F, -2.0F, 4, 14, 4, p_i47226_1_);
        this.legBackRight.setRotationPoint(-2.5F, 10.0F, 6.0F);
        this.legBackLeft = new ModelRenderer(this, 29, 29);
        this.legBackLeft.func_78790_a(-2.0F, 0.0F, -2.0F, 4, 14, 4, p_i47226_1_);
        this.legBackLeft.setRotationPoint(2.5F, 10.0F, 6.0F);
        this.legFrontRight = new ModelRenderer(this, 29, 29);
        this.legFrontRight.func_78790_a(-2.0F, 0.0F, -2.0F, 4, 14, 4, p_i47226_1_);
        this.legFrontRight.setRotationPoint(-2.5F, 10.0F, -4.0F);
        this.legFrontLeft = new ModelRenderer(this, 29, 29);
        this.legFrontLeft.func_78790_a(-2.0F, 0.0F, -2.0F, 4, 14, 4, p_i47226_1_);
        this.legFrontLeft.setRotationPoint(2.5F, 10.0F, -4.0F);
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
        AbstractChestHorse abstractchesthorse = (AbstractChestHorse)p_78088_1_;
        boolean flag = !abstractchesthorse.isChild() && abstractchesthorse.hasChest();
        this.func_78087_a(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);

        if (this.field_78091_s)
        {
            float f = 2.0F;
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b(0.0F, this.field_78145_g * p_78088_7_, this.field_78151_h * p_78088_7_);
            GlStateManager.func_179121_F();
            GlStateManager.func_179094_E();
            float f1 = 0.7F;
            GlStateManager.func_179152_a(0.71428573F, 0.64935064F, 0.7936508F);
            GlStateManager.func_179109_b(0.0F, 21.0F * p_78088_7_, 0.22F);
            this.headModel.func_78785_a(p_78088_7_);
            GlStateManager.func_179121_F();
            GlStateManager.func_179094_E();
            float f2 = 1.1F;
            GlStateManager.func_179152_a(0.625F, 0.45454544F, 0.45454544F);
            GlStateManager.func_179109_b(0.0F, 33.0F * p_78088_7_, 0.0F);
            this.body.func_78785_a(p_78088_7_);
            GlStateManager.func_179121_F();
            GlStateManager.func_179094_E();
            GlStateManager.func_179152_a(0.45454544F, 0.41322312F, 0.45454544F);
            GlStateManager.func_179109_b(0.0F, 33.0F * p_78088_7_, 0.0F);
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

        if (flag)
        {
            this.chest1.func_78785_a(p_78088_7_);
            this.chest2.func_78785_a(p_78088_7_);
        }
    }
}
