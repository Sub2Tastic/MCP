package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;

public class ModelSheep2 extends ModelQuadruped
{
    private float headRotationAngleX;

    public ModelSheep2()
    {
        super(12, 0.0F);
        this.headModel = new ModelRenderer(this, 0, 0);
        this.headModel.func_78790_a(-3.0F, -4.0F, -6.0F, 6, 6, 8, 0.0F);
        this.headModel.setRotationPoint(0.0F, 6.0F, -8.0F);
        this.body = new ModelRenderer(this, 28, 8);
        this.body.func_78790_a(-4.0F, -10.0F, -7.0F, 8, 16, 6, 0.0F);
        this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
    }

    public void func_78086_a(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_)
    {
        super.func_78086_a(p_78086_1_, p_78086_2_, p_78086_3_, p_78086_4_);
        this.headModel.rotationPointY = 6.0F + ((EntitySheep)p_78086_1_).getHeadRotationPointY(p_78086_4_) * 9.0F;
        this.headRotationAngleX = ((EntitySheep)p_78086_1_).getHeadRotationAngleX(p_78086_4_);
    }

    public void func_78087_a(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        super.func_78087_a(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
        this.headModel.rotateAngleX = this.headRotationAngleX;
    }
}
