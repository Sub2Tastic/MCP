package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelMinecart extends ModelBase
{
    public ModelRenderer[] sideModels = new ModelRenderer[7];

    public ModelMinecart()
    {
        this.sideModels[0] = new ModelRenderer(this, 0, 10);
        this.sideModels[1] = new ModelRenderer(this, 0, 0);
        this.sideModels[2] = new ModelRenderer(this, 0, 0);
        this.sideModels[3] = new ModelRenderer(this, 0, 0);
        this.sideModels[4] = new ModelRenderer(this, 0, 0);
        this.sideModels[5] = new ModelRenderer(this, 44, 10);
        int i = 20;
        int j = 8;
        int k = 16;
        int l = 4;
        this.sideModels[0].func_78790_a(-10.0F, -8.0F, -1.0F, 20, 16, 2, 0.0F);
        this.sideModels[0].setRotationPoint(0.0F, 4.0F, 0.0F);
        this.sideModels[5].func_78790_a(-9.0F, -7.0F, -1.0F, 18, 14, 1, 0.0F);
        this.sideModels[5].setRotationPoint(0.0F, 4.0F, 0.0F);
        this.sideModels[1].func_78790_a(-8.0F, -9.0F, -1.0F, 16, 8, 2, 0.0F);
        this.sideModels[1].setRotationPoint(-9.0F, 4.0F, 0.0F);
        this.sideModels[2].func_78790_a(-8.0F, -9.0F, -1.0F, 16, 8, 2, 0.0F);
        this.sideModels[2].setRotationPoint(9.0F, 4.0F, 0.0F);
        this.sideModels[3].func_78790_a(-8.0F, -9.0F, -1.0F, 16, 8, 2, 0.0F);
        this.sideModels[3].setRotationPoint(0.0F, 4.0F, -7.0F);
        this.sideModels[4].func_78790_a(-8.0F, -9.0F, -1.0F, 16, 8, 2, 0.0F);
        this.sideModels[4].setRotationPoint(0.0F, 4.0F, 7.0F);
        this.sideModels[0].rotateAngleX = ((float)Math.PI / 2F);
        this.sideModels[1].rotateAngleY = ((float)Math.PI * 3F / 2F);
        this.sideModels[2].rotateAngleY = ((float)Math.PI / 2F);
        this.sideModels[3].rotateAngleY = (float)Math.PI;
        this.sideModels[5].rotateAngleX = -((float)Math.PI / 2F);
    }

    public void func_78088_a(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        this.sideModels[5].rotationPointY = 4.0F - p_78088_4_;

        for (int i = 0; i < 6; ++i)
        {
            this.sideModels[i].func_78785_a(p_78088_7_);
        }
    }
}
