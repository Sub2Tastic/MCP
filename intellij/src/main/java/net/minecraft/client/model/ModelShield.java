package net.minecraft.client.model;

public class ModelShield extends ModelBase
{
    public ModelRenderer plate;
    public ModelRenderer handle;

    public ModelShield()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.plate = new ModelRenderer(this, 0, 0);
        this.plate.func_78790_a(-6.0F, -11.0F, -2.0F, 12, 22, 1, 0.0F);
        this.handle = new ModelRenderer(this, 26, 0);
        this.handle.func_78790_a(-1.0F, -3.0F, -1.0F, 2, 6, 6, 0.0F);
    }

    public void func_187062_a()
    {
        this.plate.func_78785_a(0.0625F);
        this.handle.func_78785_a(0.0625F);
    }
}
