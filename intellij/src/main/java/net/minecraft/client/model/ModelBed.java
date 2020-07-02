package net.minecraft.client.model;

public class ModelBed extends ModelBase
{
    public ModelRenderer field_193772_a;
    public ModelRenderer field_193773_b;
    public ModelRenderer[] field_193774_c = new ModelRenderer[4];

    public ModelBed()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.field_193772_a = new ModelRenderer(this, 0, 0);
        this.field_193772_a.func_78790_a(0.0F, 0.0F, 0.0F, 16, 16, 6, 0.0F);
        this.field_193773_b = new ModelRenderer(this, 0, 22);
        this.field_193773_b.func_78790_a(0.0F, 0.0F, 0.0F, 16, 16, 6, 0.0F);
        this.field_193774_c[0] = new ModelRenderer(this, 50, 0);
        this.field_193774_c[1] = new ModelRenderer(this, 50, 6);
        this.field_193774_c[2] = new ModelRenderer(this, 50, 12);
        this.field_193774_c[3] = new ModelRenderer(this, 50, 18);
        this.field_193774_c[0].func_78789_a(0.0F, 6.0F, -16.0F, 3, 3, 3);
        this.field_193774_c[1].func_78789_a(0.0F, 6.0F, 0.0F, 3, 3, 3);
        this.field_193774_c[2].func_78789_a(-16.0F, 6.0F, -16.0F, 3, 3, 3);
        this.field_193774_c[3].func_78789_a(-16.0F, 6.0F, 0.0F, 3, 3, 3);
        this.field_193774_c[0].rotateAngleX = ((float)Math.PI / 2F);
        this.field_193774_c[1].rotateAngleX = ((float)Math.PI / 2F);
        this.field_193774_c[2].rotateAngleX = ((float)Math.PI / 2F);
        this.field_193774_c[3].rotateAngleX = ((float)Math.PI / 2F);
        this.field_193774_c[0].rotateAngleZ = 0.0F;
        this.field_193774_c[1].rotateAngleZ = ((float)Math.PI / 2F);
        this.field_193774_c[2].rotateAngleZ = ((float)Math.PI * 3F / 2F);
        this.field_193774_c[3].rotateAngleZ = (float)Math.PI;
    }

    public int func_193770_a()
    {
        return 51;
    }

    public void func_193771_b()
    {
        this.field_193772_a.func_78785_a(0.0625F);
        this.field_193773_b.func_78785_a(0.0625F);
        this.field_193774_c[0].func_78785_a(0.0625F);
        this.field_193774_c[1].func_78785_a(0.0625F);
        this.field_193774_c[2].func_78785_a(0.0625F);
        this.field_193774_c[3].func_78785_a(0.0625F);
    }

    public void func_193769_a(boolean p_193769_1_)
    {
        this.field_193772_a.showModel = p_193769_1_;
        this.field_193773_b.showModel = !p_193769_1_;
        this.field_193774_c[0].showModel = !p_193769_1_;
        this.field_193774_c[1].showModel = p_193769_1_;
        this.field_193774_c[2].showModel = !p_193769_1_;
        this.field_193774_c[3].showModel = p_193769_1_;
    }
}
