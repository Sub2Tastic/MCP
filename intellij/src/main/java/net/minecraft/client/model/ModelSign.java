package net.minecraft.client.model;

public class ModelSign extends ModelBase
{
    public ModelRenderer signBoard = new ModelRenderer(this, 0, 0);
    public ModelRenderer signStick;

    public ModelSign()
    {
        this.signBoard.func_78790_a(-12.0F, -14.0F, -1.0F, 24, 12, 2, 0.0F);
        this.signStick = new ModelRenderer(this, 0, 14);
        this.signStick.func_78790_a(-1.0F, -2.0F, -1.0F, 2, 14, 2, 0.0F);
    }

    public void func_78164_a()
    {
        this.signBoard.func_78785_a(0.0625F);
        this.signStick.func_78785_a(0.0625F);
    }
}
