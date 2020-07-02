package net.minecraft.client.model;

import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelGhast extends ModelBase
{
    ModelRenderer field_78128_a;
    ModelRenderer[] tentacles = new ModelRenderer[9];

    public ModelGhast()
    {
        int i = -16;
        this.field_78128_a = new ModelRenderer(this, 0, 0);
        this.field_78128_a.func_78789_a(-8.0F, -8.0F, -8.0F, 16, 16, 16);
        this.field_78128_a.rotationPointY += 8.0F;
        Random random = new Random(1660L);

        for (int j = 0; j < this.tentacles.length; ++j)
        {
            this.tentacles[j] = new ModelRenderer(this, 0, 0);
            float f = (((float)(j % 3) - (float)(j / 3 % 2) * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
            float f1 = ((float)(j / 3) / 2.0F * 2.0F - 1.0F) * 5.0F;
            int k = random.nextInt(7) + 8;
            this.tentacles[j].func_78789_a(-1.0F, 0.0F, -1.0F, 2, k, 2);
            this.tentacles[j].rotationPointX = f;
            this.tentacles[j].rotationPointZ = f1;
            this.tentacles[j].rotationPointY = 15.0F;
        }
    }

    public void func_78087_a(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        for (int i = 0; i < this.tentacles.length; ++i)
        {
            this.tentacles[i].rotateAngleX = 0.2F * MathHelper.sin(p_78087_3_ * 0.3F + (float)i) + 0.4F;
        }
    }

    public void func_78088_a(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        this.func_78087_a(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b(0.0F, 0.6F, 0.0F);
        this.field_78128_a.func_78785_a(p_78088_7_);

        for (ModelRenderer modelrenderer : this.tentacles)
        {
            modelrenderer.func_78785_a(p_78088_7_);
        }

        GlStateManager.func_179121_F();
    }
}
