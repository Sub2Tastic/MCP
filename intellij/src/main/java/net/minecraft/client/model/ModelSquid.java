package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelSquid extends ModelBase
{
    ModelRenderer body;
    ModelRenderer[] legs = new ModelRenderer[8];

    public ModelSquid()
    {
        int i = -16;
        this.body = new ModelRenderer(this, 0, 0);
        this.body.func_78789_a(-6.0F, -8.0F, -6.0F, 12, 16, 12);
        this.body.rotationPointY += 8.0F;

        for (int j = 0; j < this.legs.length; ++j)
        {
            this.legs[j] = new ModelRenderer(this, 48, 0);
            double d0 = (double)j * Math.PI * 2.0D / (double)this.legs.length;
            float f = (float)Math.cos(d0) * 5.0F;
            float f1 = (float)Math.sin(d0) * 5.0F;
            this.legs[j].func_78789_a(-1.0F, 0.0F, -1.0F, 2, 18, 2);
            this.legs[j].rotationPointX = f;
            this.legs[j].rotationPointZ = f1;
            this.legs[j].rotationPointY = 15.0F;
            d0 = (double)j * Math.PI * -2.0D / (double)this.legs.length + (Math.PI / 2D);
            this.legs[j].rotateAngleY = (float)d0;
        }
    }

    public void func_78087_a(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        for (ModelRenderer modelrenderer : this.legs)
        {
            modelrenderer.rotateAngleX = p_78087_3_;
        }
    }

    public void func_78088_a(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        this.func_78087_a(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.body.func_78785_a(p_78088_7_);

        for (ModelRenderer modelrenderer : this.legs)
        {
            modelrenderer.func_78785_a(p_78088_7_);
        }
    }
}
