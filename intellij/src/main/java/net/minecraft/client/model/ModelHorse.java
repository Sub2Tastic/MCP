package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.util.math.MathHelper;

public class ModelHorse extends ModelBase
{
    private final ModelRenderer field_110709_a;
    private final ModelRenderer field_178711_b;
    private final ModelRenderer field_178712_c;
    private final ModelRenderer field_110705_d;
    private final ModelRenderer field_110706_e;
    private final ModelRenderer field_110703_f;
    private final ModelRenderer field_110704_g;
    private final ModelRenderer field_110716_h;
    private final ModelRenderer field_110717_i;
    private final ModelRenderer field_110714_j;
    private final ModelRenderer field_110715_k;
    private final ModelRenderer field_110712_l;
    private final ModelRenderer field_110713_m;
    private final ModelRenderer field_110710_n;
    private final ModelRenderer field_110711_o;
    private final ModelRenderer field_110719_v;
    private final ModelRenderer field_110718_w;
    private final ModelRenderer field_110722_x;
    private final ModelRenderer field_110721_y;
    private final ModelRenderer field_110720_z;
    private final ModelRenderer field_110688_A;
    private final ModelRenderer field_110689_B;
    private final ModelRenderer field_110690_C;
    private final ModelRenderer field_110684_D;
    private final ModelRenderer field_110685_E;
    private final ModelRenderer field_110686_F;
    private final ModelRenderer field_110687_G;
    private final ModelRenderer field_110695_H;
    private final ModelRenderer field_110696_I;
    private final ModelRenderer field_110697_J;
    private final ModelRenderer field_110698_K;
    private final ModelRenderer field_110691_L;
    private final ModelRenderer field_110692_M;
    private final ModelRenderer field_110693_N;
    private final ModelRenderer field_110694_O;
    private final ModelRenderer field_110700_P;
    private final ModelRenderer field_110699_Q;
    private final ModelRenderer field_110702_R;
    private final ModelRenderer field_110701_S;

    public ModelHorse()
    {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.field_110715_k = new ModelRenderer(this, 0, 34);
        this.field_110715_k.func_78789_a(-5.0F, -8.0F, -19.0F, 10, 10, 24);
        this.field_110715_k.setRotationPoint(0.0F, 11.0F, 9.0F);
        this.field_110712_l = new ModelRenderer(this, 44, 0);
        this.field_110712_l.func_78789_a(-1.0F, -1.0F, 0.0F, 2, 2, 3);
        this.field_110712_l.setRotationPoint(0.0F, 3.0F, 14.0F);
        this.field_110712_l.rotateAngleX = -1.134464F;
        this.field_110713_m = new ModelRenderer(this, 38, 7);
        this.field_110713_m.func_78789_a(-1.5F, -2.0F, 3.0F, 3, 4, 7);
        this.field_110713_m.setRotationPoint(0.0F, 3.0F, 14.0F);
        this.field_110713_m.rotateAngleX = -1.134464F;
        this.field_110710_n = new ModelRenderer(this, 24, 3);
        this.field_110710_n.func_78789_a(-1.5F, -4.5F, 9.0F, 3, 4, 7);
        this.field_110710_n.setRotationPoint(0.0F, 3.0F, 14.0F);
        this.field_110710_n.rotateAngleX = -1.3962634F;
        this.field_110711_o = new ModelRenderer(this, 78, 29);
        this.field_110711_o.func_78789_a(-2.5F, -2.0F, -2.5F, 4, 9, 5);
        this.field_110711_o.setRotationPoint(4.0F, 9.0F, 11.0F);
        this.field_110719_v = new ModelRenderer(this, 78, 43);
        this.field_110719_v.func_78789_a(-2.0F, 0.0F, -1.5F, 3, 5, 3);
        this.field_110719_v.setRotationPoint(4.0F, 16.0F, 11.0F);
        this.field_110718_w = new ModelRenderer(this, 78, 51);
        this.field_110718_w.func_78789_a(-2.5F, 5.1F, -2.0F, 4, 3, 4);
        this.field_110718_w.setRotationPoint(4.0F, 16.0F, 11.0F);
        this.field_110722_x = new ModelRenderer(this, 96, 29);
        this.field_110722_x.func_78789_a(-1.5F, -2.0F, -2.5F, 4, 9, 5);
        this.field_110722_x.setRotationPoint(-4.0F, 9.0F, 11.0F);
        this.field_110721_y = new ModelRenderer(this, 96, 43);
        this.field_110721_y.func_78789_a(-1.0F, 0.0F, -1.5F, 3, 5, 3);
        this.field_110721_y.setRotationPoint(-4.0F, 16.0F, 11.0F);
        this.field_110720_z = new ModelRenderer(this, 96, 51);
        this.field_110720_z.func_78789_a(-1.5F, 5.1F, -2.0F, 4, 3, 4);
        this.field_110720_z.setRotationPoint(-4.0F, 16.0F, 11.0F);
        this.field_110688_A = new ModelRenderer(this, 44, 29);
        this.field_110688_A.func_78789_a(-1.9F, -1.0F, -2.1F, 3, 8, 4);
        this.field_110688_A.setRotationPoint(4.0F, 9.0F, -8.0F);
        this.field_110689_B = new ModelRenderer(this, 44, 41);
        this.field_110689_B.func_78789_a(-1.9F, 0.0F, -1.6F, 3, 5, 3);
        this.field_110689_B.setRotationPoint(4.0F, 16.0F, -8.0F);
        this.field_110690_C = new ModelRenderer(this, 44, 51);
        this.field_110690_C.func_78789_a(-2.4F, 5.1F, -2.1F, 4, 3, 4);
        this.field_110690_C.setRotationPoint(4.0F, 16.0F, -8.0F);
        this.field_110684_D = new ModelRenderer(this, 60, 29);
        this.field_110684_D.func_78789_a(-1.1F, -1.0F, -2.1F, 3, 8, 4);
        this.field_110684_D.setRotationPoint(-4.0F, 9.0F, -8.0F);
        this.field_110685_E = new ModelRenderer(this, 60, 41);
        this.field_110685_E.func_78789_a(-1.1F, 0.0F, -1.6F, 3, 5, 3);
        this.field_110685_E.setRotationPoint(-4.0F, 16.0F, -8.0F);
        this.field_110686_F = new ModelRenderer(this, 60, 51);
        this.field_110686_F.func_78789_a(-1.6F, 5.1F, -2.1F, 4, 3, 4);
        this.field_110686_F.setRotationPoint(-4.0F, 16.0F, -8.0F);
        this.field_110709_a = new ModelRenderer(this, 0, 0);
        this.field_110709_a.func_78789_a(-2.5F, -10.0F, -1.5F, 5, 5, 7);
        this.field_110709_a.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.field_110709_a.rotateAngleX = 0.5235988F;
        this.field_178711_b = new ModelRenderer(this, 24, 18);
        this.field_178711_b.func_78789_a(-2.0F, -10.0F, -7.0F, 4, 3, 6);
        this.field_178711_b.setRotationPoint(0.0F, 3.95F, -10.0F);
        this.field_178711_b.rotateAngleX = 0.5235988F;
        this.field_178712_c = new ModelRenderer(this, 24, 27);
        this.field_178712_c.func_78789_a(-2.0F, -7.0F, -6.5F, 4, 2, 5);
        this.field_178712_c.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.field_178712_c.rotateAngleX = 0.5235988F;
        this.field_110709_a.addChild(this.field_178711_b);
        this.field_110709_a.addChild(this.field_178712_c);
        this.field_110705_d = new ModelRenderer(this, 0, 0);
        this.field_110705_d.func_78789_a(0.45F, -12.0F, 4.0F, 2, 3, 1);
        this.field_110705_d.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.field_110705_d.rotateAngleX = 0.5235988F;
        this.field_110706_e = new ModelRenderer(this, 0, 0);
        this.field_110706_e.func_78789_a(-2.45F, -12.0F, 4.0F, 2, 3, 1);
        this.field_110706_e.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.field_110706_e.rotateAngleX = 0.5235988F;
        this.field_110703_f = new ModelRenderer(this, 0, 12);
        this.field_110703_f.func_78789_a(-2.0F, -16.0F, 4.0F, 2, 7, 1);
        this.field_110703_f.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.field_110703_f.rotateAngleX = 0.5235988F;
        this.field_110703_f.rotateAngleZ = 0.2617994F;
        this.field_110704_g = new ModelRenderer(this, 0, 12);
        this.field_110704_g.func_78789_a(0.0F, -16.0F, 4.0F, 2, 7, 1);
        this.field_110704_g.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.field_110704_g.rotateAngleX = 0.5235988F;
        this.field_110704_g.rotateAngleZ = -0.2617994F;
        this.field_110716_h = new ModelRenderer(this, 0, 12);
        this.field_110716_h.func_78789_a(-2.05F, -9.8F, -2.0F, 4, 14, 8);
        this.field_110716_h.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.field_110716_h.rotateAngleX = 0.5235988F;
        this.field_110687_G = new ModelRenderer(this, 0, 34);
        this.field_110687_G.func_78789_a(-3.0F, 0.0F, 0.0F, 8, 8, 3);
        this.field_110687_G.setRotationPoint(-7.5F, 3.0F, 10.0F);
        this.field_110687_G.rotateAngleY = ((float)Math.PI / 2F);
        this.field_110695_H = new ModelRenderer(this, 0, 47);
        this.field_110695_H.func_78789_a(-3.0F, 0.0F, 0.0F, 8, 8, 3);
        this.field_110695_H.setRotationPoint(4.5F, 3.0F, 10.0F);
        this.field_110695_H.rotateAngleY = ((float)Math.PI / 2F);
        this.field_110696_I = new ModelRenderer(this, 80, 0);
        this.field_110696_I.func_78789_a(-5.0F, 0.0F, -3.0F, 10, 1, 8);
        this.field_110696_I.setRotationPoint(0.0F, 2.0F, 2.0F);
        this.field_110697_J = new ModelRenderer(this, 106, 9);
        this.field_110697_J.func_78789_a(-1.5F, -1.0F, -3.0F, 3, 1, 2);
        this.field_110697_J.setRotationPoint(0.0F, 2.0F, 2.0F);
        this.field_110698_K = new ModelRenderer(this, 80, 9);
        this.field_110698_K.func_78789_a(-4.0F, -1.0F, 3.0F, 8, 1, 2);
        this.field_110698_K.setRotationPoint(0.0F, 2.0F, 2.0F);
        this.field_110692_M = new ModelRenderer(this, 74, 0);
        this.field_110692_M.func_78789_a(-0.5F, 6.0F, -1.0F, 1, 2, 2);
        this.field_110692_M.setRotationPoint(5.0F, 3.0F, 2.0F);
        this.field_110691_L = new ModelRenderer(this, 70, 0);
        this.field_110691_L.func_78789_a(-0.5F, 0.0F, -0.5F, 1, 6, 1);
        this.field_110691_L.setRotationPoint(5.0F, 3.0F, 2.0F);
        this.field_110694_O = new ModelRenderer(this, 74, 4);
        this.field_110694_O.func_78789_a(-0.5F, 6.0F, -1.0F, 1, 2, 2);
        this.field_110694_O.setRotationPoint(-5.0F, 3.0F, 2.0F);
        this.field_110693_N = new ModelRenderer(this, 80, 0);
        this.field_110693_N.func_78789_a(-0.5F, 0.0F, -0.5F, 1, 6, 1);
        this.field_110693_N.setRotationPoint(-5.0F, 3.0F, 2.0F);
        this.field_110700_P = new ModelRenderer(this, 74, 13);
        this.field_110700_P.func_78789_a(1.5F, -8.0F, -4.0F, 1, 2, 2);
        this.field_110700_P.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.field_110700_P.rotateAngleX = 0.5235988F;
        this.field_110699_Q = new ModelRenderer(this, 74, 13);
        this.field_110699_Q.func_78789_a(-2.5F, -8.0F, -4.0F, 1, 2, 2);
        this.field_110699_Q.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.field_110699_Q.rotateAngleX = 0.5235988F;
        this.field_110702_R = new ModelRenderer(this, 44, 10);
        this.field_110702_R.func_78789_a(2.6F, -6.0F, -6.0F, 0, 3, 16);
        this.field_110702_R.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.field_110701_S = new ModelRenderer(this, 44, 5);
        this.field_110701_S.func_78789_a(-2.6F, -6.0F, -6.0F, 0, 3, 16);
        this.field_110701_S.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.field_110714_j = new ModelRenderer(this, 58, 0);
        this.field_110714_j.func_78789_a(-1.0F, -11.5F, 5.0F, 2, 16, 4);
        this.field_110714_j.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.field_110714_j.rotateAngleX = 0.5235988F;
        this.field_110717_i = new ModelRenderer(this, 80, 12);
        this.field_110717_i.func_78790_a(-2.5F, -10.1F, -7.0F, 5, 5, 12, 0.2F);
        this.field_110717_i.setRotationPoint(0.0F, 4.0F, -10.0F);
        this.field_110717_i.rotateAngleX = 0.5235988F;
    }

    public void func_78088_a(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        AbstractHorse abstracthorse = (AbstractHorse)p_78088_1_;
        float f = abstracthorse.getGrassEatingAmount(0.0F);
        boolean flag = abstracthorse.isChild();
        boolean flag1 = !flag && abstracthorse.isHorseSaddled();
        boolean flag2 = abstracthorse instanceof AbstractChestHorse;
        boolean flag3 = !flag && flag2 && ((AbstractChestHorse)abstracthorse).hasChest();
        float f1 = abstracthorse.func_110254_bY();
        boolean flag4 = abstracthorse.isBeingRidden();

        if (flag1)
        {
            this.field_110717_i.func_78785_a(p_78088_7_);
            this.field_110696_I.func_78785_a(p_78088_7_);
            this.field_110697_J.func_78785_a(p_78088_7_);
            this.field_110698_K.func_78785_a(p_78088_7_);
            this.field_110691_L.func_78785_a(p_78088_7_);
            this.field_110692_M.func_78785_a(p_78088_7_);
            this.field_110693_N.func_78785_a(p_78088_7_);
            this.field_110694_O.func_78785_a(p_78088_7_);
            this.field_110700_P.func_78785_a(p_78088_7_);
            this.field_110699_Q.func_78785_a(p_78088_7_);

            if (flag4)
            {
                this.field_110702_R.func_78785_a(p_78088_7_);
                this.field_110701_S.func_78785_a(p_78088_7_);
            }
        }

        if (flag)
        {
            GlStateManager.func_179094_E();
            GlStateManager.func_179152_a(f1, 0.5F + f1 * 0.5F, f1);
            GlStateManager.func_179109_b(0.0F, 0.95F * (1.0F - f1), 0.0F);
        }

        this.field_110711_o.func_78785_a(p_78088_7_);
        this.field_110719_v.func_78785_a(p_78088_7_);
        this.field_110718_w.func_78785_a(p_78088_7_);
        this.field_110722_x.func_78785_a(p_78088_7_);
        this.field_110721_y.func_78785_a(p_78088_7_);
        this.field_110720_z.func_78785_a(p_78088_7_);
        this.field_110688_A.func_78785_a(p_78088_7_);
        this.field_110689_B.func_78785_a(p_78088_7_);
        this.field_110690_C.func_78785_a(p_78088_7_);
        this.field_110684_D.func_78785_a(p_78088_7_);
        this.field_110685_E.func_78785_a(p_78088_7_);
        this.field_110686_F.func_78785_a(p_78088_7_);

        if (flag)
        {
            GlStateManager.func_179121_F();
            GlStateManager.func_179094_E();
            GlStateManager.func_179152_a(f1, f1, f1);
            GlStateManager.func_179109_b(0.0F, 1.35F * (1.0F - f1), 0.0F);
        }

        this.field_110715_k.func_78785_a(p_78088_7_);
        this.field_110712_l.func_78785_a(p_78088_7_);
        this.field_110713_m.func_78785_a(p_78088_7_);
        this.field_110710_n.func_78785_a(p_78088_7_);
        this.field_110716_h.func_78785_a(p_78088_7_);
        this.field_110714_j.func_78785_a(p_78088_7_);

        if (flag)
        {
            GlStateManager.func_179121_F();
            GlStateManager.func_179094_E();
            float f2 = 0.5F + f1 * f1 * 0.5F;
            GlStateManager.func_179152_a(f2, f2, f2);

            if (f <= 0.0F)
            {
                GlStateManager.func_179109_b(0.0F, 1.35F * (1.0F - f1), 0.0F);
            }
            else
            {
                GlStateManager.func_179109_b(0.0F, 0.9F * (1.0F - f1) * f + 1.35F * (1.0F - f1) * (1.0F - f), 0.15F * (1.0F - f1) * f);
            }
        }

        if (flag2)
        {
            this.field_110703_f.func_78785_a(p_78088_7_);
            this.field_110704_g.func_78785_a(p_78088_7_);
        }
        else
        {
            this.field_110705_d.func_78785_a(p_78088_7_);
            this.field_110706_e.func_78785_a(p_78088_7_);
        }

        this.field_110709_a.func_78785_a(p_78088_7_);

        if (flag)
        {
            GlStateManager.func_179121_F();
        }

        if (flag3)
        {
            this.field_110687_G.func_78785_a(p_78088_7_);
            this.field_110695_H.func_78785_a(p_78088_7_);
        }
    }

    private float func_110683_a(float p_110683_1_, float p_110683_2_, float p_110683_3_)
    {
        float f;

        for (f = p_110683_2_ - p_110683_1_; f < -180.0F; f += 360.0F)
        {
            ;
        }

        while (f >= 180.0F)
        {
            f -= 360.0F;
        }

        return p_110683_1_ + p_110683_3_ * f;
    }

    public void func_78086_a(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_)
    {
        super.func_78086_a(p_78086_1_, p_78086_2_, p_78086_3_, p_78086_4_);
        float f = this.func_110683_a(p_78086_1_.prevRenderYawOffset, p_78086_1_.renderYawOffset, p_78086_4_);
        float f1 = this.func_110683_a(p_78086_1_.prevRotationYawHead, p_78086_1_.rotationYawHead, p_78086_4_);
        float f2 = p_78086_1_.prevRotationPitch + (p_78086_1_.rotationPitch - p_78086_1_.prevRotationPitch) * p_78086_4_;
        float f3 = f1 - f;
        float f4 = f2 * 0.017453292F;

        if (f3 > 20.0F)
        {
            f3 = 20.0F;
        }

        if (f3 < -20.0F)
        {
            f3 = -20.0F;
        }

        if (p_78086_3_ > 0.2F)
        {
            f4 += MathHelper.cos(p_78086_2_ * 0.4F) * 0.15F * p_78086_3_;
        }

        AbstractHorse abstracthorse = (AbstractHorse)p_78086_1_;
        float f5 = abstracthorse.getGrassEatingAmount(p_78086_4_);
        float f6 = abstracthorse.getRearingAmount(p_78086_4_);
        float f7 = 1.0F - f6;
        float f8 = abstracthorse.getMouthOpennessAngle(p_78086_4_);
        boolean flag = abstracthorse.tailCounter != 0;
        boolean flag1 = abstracthorse.isHorseSaddled();
        boolean flag2 = abstracthorse.isBeingRidden();
        float f9 = (float)p_78086_1_.ticksExisted + p_78086_4_;
        float f10 = MathHelper.cos(p_78086_2_ * 0.6662F + (float)Math.PI);
        float f11 = f10 * 0.8F * p_78086_3_;
        this.field_110709_a.rotationPointY = 4.0F;
        this.field_110709_a.rotationPointZ = -10.0F;
        this.field_110712_l.rotationPointY = 3.0F;
        this.field_110713_m.rotationPointZ = 14.0F;
        this.field_110695_H.rotationPointY = 3.0F;
        this.field_110695_H.rotationPointZ = 10.0F;
        this.field_110715_k.rotateAngleX = 0.0F;
        this.field_110709_a.rotateAngleX = 0.5235988F + f4;
        this.field_110709_a.rotateAngleY = f3 * 0.017453292F;
        this.field_110709_a.rotateAngleX = f6 * (0.2617994F + f4) + f5 * 2.1816616F + (1.0F - Math.max(f6, f5)) * this.field_110709_a.rotateAngleX;
        this.field_110709_a.rotateAngleY = f6 * f3 * 0.017453292F + (1.0F - Math.max(f6, f5)) * this.field_110709_a.rotateAngleY;
        this.field_110709_a.rotationPointY = f6 * -6.0F + f5 * 11.0F + (1.0F - Math.max(f6, f5)) * this.field_110709_a.rotationPointY;
        this.field_110709_a.rotationPointZ = f6 * -1.0F + f5 * -10.0F + (1.0F - Math.max(f6, f5)) * this.field_110709_a.rotationPointZ;
        this.field_110712_l.rotationPointY = f6 * 9.0F + f7 * this.field_110712_l.rotationPointY;
        this.field_110713_m.rotationPointZ = f6 * 18.0F + f7 * this.field_110713_m.rotationPointZ;
        this.field_110695_H.rotationPointY = f6 * 5.5F + f7 * this.field_110695_H.rotationPointY;
        this.field_110695_H.rotationPointZ = f6 * 15.0F + f7 * this.field_110695_H.rotationPointZ;
        this.field_110715_k.rotateAngleX = f6 * -((float)Math.PI / 4F) + f7 * this.field_110715_k.rotateAngleX;
        this.field_110705_d.rotationPointY = this.field_110709_a.rotationPointY;
        this.field_110706_e.rotationPointY = this.field_110709_a.rotationPointY;
        this.field_110703_f.rotationPointY = this.field_110709_a.rotationPointY;
        this.field_110704_g.rotationPointY = this.field_110709_a.rotationPointY;
        this.field_110716_h.rotationPointY = this.field_110709_a.rotationPointY;
        this.field_178711_b.rotationPointY = 0.02F;
        this.field_178712_c.rotationPointY = 0.0F;
        this.field_110714_j.rotationPointY = this.field_110709_a.rotationPointY;
        this.field_110705_d.rotationPointZ = this.field_110709_a.rotationPointZ;
        this.field_110706_e.rotationPointZ = this.field_110709_a.rotationPointZ;
        this.field_110703_f.rotationPointZ = this.field_110709_a.rotationPointZ;
        this.field_110704_g.rotationPointZ = this.field_110709_a.rotationPointZ;
        this.field_110716_h.rotationPointZ = this.field_110709_a.rotationPointZ;
        this.field_178711_b.rotationPointZ = 0.02F - f8;
        this.field_178712_c.rotationPointZ = f8;
        this.field_110714_j.rotationPointZ = this.field_110709_a.rotationPointZ;
        this.field_110705_d.rotateAngleX = this.field_110709_a.rotateAngleX;
        this.field_110706_e.rotateAngleX = this.field_110709_a.rotateAngleX;
        this.field_110703_f.rotateAngleX = this.field_110709_a.rotateAngleX;
        this.field_110704_g.rotateAngleX = this.field_110709_a.rotateAngleX;
        this.field_110716_h.rotateAngleX = this.field_110709_a.rotateAngleX;
        this.field_178711_b.rotateAngleX = -0.09424778F * f8;
        this.field_178712_c.rotateAngleX = 0.15707964F * f8;
        this.field_110714_j.rotateAngleX = this.field_110709_a.rotateAngleX;
        this.field_110705_d.rotateAngleY = this.field_110709_a.rotateAngleY;
        this.field_110706_e.rotateAngleY = this.field_110709_a.rotateAngleY;
        this.field_110703_f.rotateAngleY = this.field_110709_a.rotateAngleY;
        this.field_110704_g.rotateAngleY = this.field_110709_a.rotateAngleY;
        this.field_110716_h.rotateAngleY = this.field_110709_a.rotateAngleY;
        this.field_178711_b.rotateAngleY = 0.0F;
        this.field_178712_c.rotateAngleY = 0.0F;
        this.field_110714_j.rotateAngleY = this.field_110709_a.rotateAngleY;
        this.field_110687_G.rotateAngleX = f11 / 5.0F;
        this.field_110695_H.rotateAngleX = -f11 / 5.0F;
        float f12 = 0.2617994F * f6;
        float f13 = MathHelper.cos(f9 * 0.6F + (float)Math.PI);
        this.field_110688_A.rotationPointY = -2.0F * f6 + 9.0F * f7;
        this.field_110688_A.rotationPointZ = -2.0F * f6 + -8.0F * f7;
        this.field_110684_D.rotationPointY = this.field_110688_A.rotationPointY;
        this.field_110684_D.rotationPointZ = this.field_110688_A.rotationPointZ;
        this.field_110719_v.rotationPointY = this.field_110711_o.rotationPointY + MathHelper.sin(((float)Math.PI / 2F) + f12 + f7 * -f10 * 0.5F * p_78086_3_) * 7.0F;
        this.field_110719_v.rotationPointZ = this.field_110711_o.rotationPointZ + MathHelper.cos(-((float)Math.PI / 2F) + f12 + f7 * -f10 * 0.5F * p_78086_3_) * 7.0F;
        this.field_110721_y.rotationPointY = this.field_110722_x.rotationPointY + MathHelper.sin(((float)Math.PI / 2F) + f12 + f7 * f10 * 0.5F * p_78086_3_) * 7.0F;
        this.field_110721_y.rotationPointZ = this.field_110722_x.rotationPointZ + MathHelper.cos(-((float)Math.PI / 2F) + f12 + f7 * f10 * 0.5F * p_78086_3_) * 7.0F;
        float f14 = (-1.0471976F + f13) * f6 + f11 * f7;
        float f15 = (-1.0471976F - f13) * f6 + -f11 * f7;
        this.field_110689_B.rotationPointY = this.field_110688_A.rotationPointY + MathHelper.sin(((float)Math.PI / 2F) + f14) * 7.0F;
        this.field_110689_B.rotationPointZ = this.field_110688_A.rotationPointZ + MathHelper.cos(-((float)Math.PI / 2F) + f14) * 7.0F;
        this.field_110685_E.rotationPointY = this.field_110684_D.rotationPointY + MathHelper.sin(((float)Math.PI / 2F) + f15) * 7.0F;
        this.field_110685_E.rotationPointZ = this.field_110684_D.rotationPointZ + MathHelper.cos(-((float)Math.PI / 2F) + f15) * 7.0F;
        this.field_110711_o.rotateAngleX = f12 + -f10 * 0.5F * p_78086_3_ * f7;
        this.field_110719_v.rotateAngleX = -0.08726646F * f6 + (-f10 * 0.5F * p_78086_3_ - Math.max(0.0F, f10 * 0.5F * p_78086_3_)) * f7;
        this.field_110718_w.rotateAngleX = this.field_110719_v.rotateAngleX;
        this.field_110722_x.rotateAngleX = f12 + f10 * 0.5F * p_78086_3_ * f7;
        this.field_110721_y.rotateAngleX = -0.08726646F * f6 + (f10 * 0.5F * p_78086_3_ - Math.max(0.0F, -f10 * 0.5F * p_78086_3_)) * f7;
        this.field_110720_z.rotateAngleX = this.field_110721_y.rotateAngleX;
        this.field_110688_A.rotateAngleX = f14;
        this.field_110689_B.rotateAngleX = (this.field_110688_A.rotateAngleX + (float)Math.PI * Math.max(0.0F, 0.2F + f13 * 0.2F)) * f6 + (f11 + Math.max(0.0F, f10 * 0.5F * p_78086_3_)) * f7;
        this.field_110690_C.rotateAngleX = this.field_110689_B.rotateAngleX;
        this.field_110684_D.rotateAngleX = f15;
        this.field_110685_E.rotateAngleX = (this.field_110684_D.rotateAngleX + (float)Math.PI * Math.max(0.0F, 0.2F - f13 * 0.2F)) * f6 + (-f11 + Math.max(0.0F, -f10 * 0.5F * p_78086_3_)) * f7;
        this.field_110686_F.rotateAngleX = this.field_110685_E.rotateAngleX;
        this.field_110718_w.rotationPointY = this.field_110719_v.rotationPointY;
        this.field_110718_w.rotationPointZ = this.field_110719_v.rotationPointZ;
        this.field_110720_z.rotationPointY = this.field_110721_y.rotationPointY;
        this.field_110720_z.rotationPointZ = this.field_110721_y.rotationPointZ;
        this.field_110690_C.rotationPointY = this.field_110689_B.rotationPointY;
        this.field_110690_C.rotationPointZ = this.field_110689_B.rotationPointZ;
        this.field_110686_F.rotationPointY = this.field_110685_E.rotationPointY;
        this.field_110686_F.rotationPointZ = this.field_110685_E.rotationPointZ;

        if (flag1)
        {
            this.field_110696_I.rotationPointY = f6 * 0.5F + f7 * 2.0F;
            this.field_110696_I.rotationPointZ = f6 * 11.0F + f7 * 2.0F;
            this.field_110697_J.rotationPointY = this.field_110696_I.rotationPointY;
            this.field_110698_K.rotationPointY = this.field_110696_I.rotationPointY;
            this.field_110691_L.rotationPointY = this.field_110696_I.rotationPointY;
            this.field_110693_N.rotationPointY = this.field_110696_I.rotationPointY;
            this.field_110692_M.rotationPointY = this.field_110696_I.rotationPointY;
            this.field_110694_O.rotationPointY = this.field_110696_I.rotationPointY;
            this.field_110687_G.rotationPointY = this.field_110695_H.rotationPointY;
            this.field_110697_J.rotationPointZ = this.field_110696_I.rotationPointZ;
            this.field_110698_K.rotationPointZ = this.field_110696_I.rotationPointZ;
            this.field_110691_L.rotationPointZ = this.field_110696_I.rotationPointZ;
            this.field_110693_N.rotationPointZ = this.field_110696_I.rotationPointZ;
            this.field_110692_M.rotationPointZ = this.field_110696_I.rotationPointZ;
            this.field_110694_O.rotationPointZ = this.field_110696_I.rotationPointZ;
            this.field_110687_G.rotationPointZ = this.field_110695_H.rotationPointZ;
            this.field_110696_I.rotateAngleX = this.field_110715_k.rotateAngleX;
            this.field_110697_J.rotateAngleX = this.field_110715_k.rotateAngleX;
            this.field_110698_K.rotateAngleX = this.field_110715_k.rotateAngleX;
            this.field_110702_R.rotationPointY = this.field_110709_a.rotationPointY;
            this.field_110701_S.rotationPointY = this.field_110709_a.rotationPointY;
            this.field_110717_i.rotationPointY = this.field_110709_a.rotationPointY;
            this.field_110700_P.rotationPointY = this.field_110709_a.rotationPointY;
            this.field_110699_Q.rotationPointY = this.field_110709_a.rotationPointY;
            this.field_110702_R.rotationPointZ = this.field_110709_a.rotationPointZ;
            this.field_110701_S.rotationPointZ = this.field_110709_a.rotationPointZ;
            this.field_110717_i.rotationPointZ = this.field_110709_a.rotationPointZ;
            this.field_110700_P.rotationPointZ = this.field_110709_a.rotationPointZ;
            this.field_110699_Q.rotationPointZ = this.field_110709_a.rotationPointZ;
            this.field_110702_R.rotateAngleX = f4;
            this.field_110701_S.rotateAngleX = f4;
            this.field_110717_i.rotateAngleX = this.field_110709_a.rotateAngleX;
            this.field_110700_P.rotateAngleX = this.field_110709_a.rotateAngleX;
            this.field_110699_Q.rotateAngleX = this.field_110709_a.rotateAngleX;
            this.field_110717_i.rotateAngleY = this.field_110709_a.rotateAngleY;
            this.field_110700_P.rotateAngleY = this.field_110709_a.rotateAngleY;
            this.field_110702_R.rotateAngleY = this.field_110709_a.rotateAngleY;
            this.field_110699_Q.rotateAngleY = this.field_110709_a.rotateAngleY;
            this.field_110701_S.rotateAngleY = this.field_110709_a.rotateAngleY;

            if (flag2)
            {
                this.field_110691_L.rotateAngleX = -1.0471976F;
                this.field_110692_M.rotateAngleX = -1.0471976F;
                this.field_110693_N.rotateAngleX = -1.0471976F;
                this.field_110694_O.rotateAngleX = -1.0471976F;
                this.field_110691_L.rotateAngleZ = 0.0F;
                this.field_110692_M.rotateAngleZ = 0.0F;
                this.field_110693_N.rotateAngleZ = 0.0F;
                this.field_110694_O.rotateAngleZ = 0.0F;
            }
            else
            {
                this.field_110691_L.rotateAngleX = f11 / 3.0F;
                this.field_110692_M.rotateAngleX = f11 / 3.0F;
                this.field_110693_N.rotateAngleX = f11 / 3.0F;
                this.field_110694_O.rotateAngleX = f11 / 3.0F;
                this.field_110691_L.rotateAngleZ = f11 / 5.0F;
                this.field_110692_M.rotateAngleZ = f11 / 5.0F;
                this.field_110693_N.rotateAngleZ = -f11 / 5.0F;
                this.field_110694_O.rotateAngleZ = -f11 / 5.0F;
            }
        }

        f12 = -1.3089969F + p_78086_3_ * 1.5F;

        if (f12 > 0.0F)
        {
            f12 = 0.0F;
        }

        if (flag)
        {
            this.field_110712_l.rotateAngleY = MathHelper.cos(f9 * 0.7F);
            f12 = 0.0F;
        }
        else
        {
            this.field_110712_l.rotateAngleY = 0.0F;
        }

        this.field_110713_m.rotateAngleY = this.field_110712_l.rotateAngleY;
        this.field_110710_n.rotateAngleY = this.field_110712_l.rotateAngleY;
        this.field_110713_m.rotationPointY = this.field_110712_l.rotationPointY;
        this.field_110710_n.rotationPointY = this.field_110712_l.rotationPointY;
        this.field_110713_m.rotationPointZ = this.field_110712_l.rotationPointZ;
        this.field_110710_n.rotationPointZ = this.field_110712_l.rotationPointZ;
        this.field_110712_l.rotateAngleX = f12;
        this.field_110713_m.rotateAngleX = f12;
        this.field_110710_n.rotateAngleX = -0.2617994F + f12;
    }
}
