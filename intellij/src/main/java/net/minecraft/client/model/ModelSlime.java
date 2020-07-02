package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelSlime extends ModelBase
{
    ModelRenderer slimeBodies;
    ModelRenderer slimeRightEye;
    ModelRenderer slimeLeftEye;
    ModelRenderer slimeMouth;

    public ModelSlime(int slimeBodyTexOffY)
    {
        if (slimeBodyTexOffY > 0)
        {
            this.slimeBodies = new ModelRenderer(this, 0, slimeBodyTexOffY);
            this.slimeBodies.func_78789_a(-3.0F, 17.0F, -3.0F, 6, 6, 6);
            this.slimeRightEye = new ModelRenderer(this, 32, 0);
            this.slimeRightEye.func_78789_a(-3.25F, 18.0F, -3.5F, 2, 2, 2);
            this.slimeLeftEye = new ModelRenderer(this, 32, 4);
            this.slimeLeftEye.func_78789_a(1.25F, 18.0F, -3.5F, 2, 2, 2);
            this.slimeMouth = new ModelRenderer(this, 32, 8);
            this.slimeMouth.func_78789_a(0.0F, 21.0F, -3.5F, 1, 1, 1);
        }
        else
        {
            this.slimeBodies = new ModelRenderer(this, 0, slimeBodyTexOffY);
            this.slimeBodies.func_78789_a(-4.0F, 16.0F, -4.0F, 8, 8, 8);
        }
    }

    public void func_78088_a(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        this.func_78087_a(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        GlStateManager.func_179109_b(0.0F, 0.001F, 0.0F);
        this.slimeBodies.func_78785_a(p_78088_7_);

        if (this.slimeRightEye != null)
        {
            this.slimeRightEye.func_78785_a(p_78088_7_);
            this.slimeLeftEye.func_78785_a(p_78088_7_);
            this.slimeMouth.func_78785_a(p_78088_7_);
        }
    }
}
