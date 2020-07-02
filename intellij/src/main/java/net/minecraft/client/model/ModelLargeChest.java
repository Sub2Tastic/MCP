package net.minecraft.client.model;

public class ModelLargeChest extends ModelChest
{
    public ModelLargeChest()
    {
        this.field_78234_a = (new ModelRenderer(this, 0, 0)).setTextureSize(128, 64);
        this.field_78234_a.func_78790_a(0.0F, -5.0F, -14.0F, 30, 5, 14, 0.0F);
        this.field_78234_a.rotationPointX = 1.0F;
        this.field_78234_a.rotationPointY = 7.0F;
        this.field_78234_a.rotationPointZ = 15.0F;
        this.field_78233_c = (new ModelRenderer(this, 0, 0)).setTextureSize(128, 64);
        this.field_78233_c.func_78790_a(-1.0F, -2.0F, -15.0F, 2, 4, 1, 0.0F);
        this.field_78233_c.rotationPointX = 16.0F;
        this.field_78233_c.rotationPointY = 7.0F;
        this.field_78233_c.rotationPointZ = 15.0F;
        this.field_78232_b = (new ModelRenderer(this, 0, 19)).setTextureSize(128, 64);
        this.field_78232_b.func_78790_a(0.0F, 0.0F, 0.0F, 30, 10, 14, 0.0F);
        this.field_78232_b.rotationPointX = 1.0F;
        this.field_78232_b.rotationPointY = 6.0F;
        this.field_78232_b.rotationPointZ = 1.0F;
    }
}
