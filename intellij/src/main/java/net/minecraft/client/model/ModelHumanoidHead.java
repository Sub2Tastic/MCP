package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelHumanoidHead extends ModelSkeletonHead
{
    private final ModelRenderer head = new ModelRenderer(this, 32, 0);

    public ModelHumanoidHead()
    {
        super(0, 0, 64, 64);
        this.head.func_78790_a(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.25F);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    public void func_78088_a(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        super.func_78088_a(p_78088_1_, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_);
        this.head.func_78785_a(p_78088_7_);
    }

    public void func_78087_a(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        super.func_78087_a(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
        this.head.rotateAngleY = this.field_82896_a.rotateAngleY;
        this.head.rotateAngleX = this.field_82896_a.rotateAngleX;
    }
}
