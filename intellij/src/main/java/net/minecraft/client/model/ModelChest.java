package net.minecraft.client.model;

public class ModelChest extends ModelBase
{
    public ModelRenderer field_78234_a = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
    public ModelRenderer field_78232_b;
    public ModelRenderer field_78233_c;

    public ModelChest()
    {
        this.field_78234_a.func_78790_a(0.0F, -5.0F, -14.0F, 14, 5, 14, 0.0F);
        this.field_78234_a.rotationPointX = 1.0F;
        this.field_78234_a.rotationPointY = 7.0F;
        this.field_78234_a.rotationPointZ = 15.0F;
        this.field_78233_c = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        this.field_78233_c.func_78790_a(-1.0F, -2.0F, -15.0F, 2, 4, 1, 0.0F);
        this.field_78233_c.rotationPointX = 8.0F;
        this.field_78233_c.rotationPointY = 7.0F;
        this.field_78233_c.rotationPointZ = 15.0F;
        this.field_78232_b = (new ModelRenderer(this, 0, 19)).setTextureSize(64, 64);
        this.field_78232_b.func_78790_a(0.0F, 0.0F, 0.0F, 14, 10, 14, 0.0F);
        this.field_78232_b.rotationPointX = 1.0F;
        this.field_78232_b.rotationPointY = 6.0F;
        this.field_78232_b.rotationPointZ = 1.0F;
    }

    public void func_78231_a()
    {
        this.field_78233_c.rotateAngleX = this.field_78234_a.rotateAngleX;
        this.field_78234_a.func_78785_a(0.0625F);
        this.field_78233_c.func_78785_a(0.0625F);
        this.field_78232_b.func_78785_a(0.0625F);
    }
}
