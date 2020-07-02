package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;

public class ModelDragon extends ModelBase
{
    private final ModelRenderer head;
    private final ModelRenderer spine;
    private final ModelRenderer jaw;
    private final ModelRenderer body;
    private final ModelRenderer field_78218_e;
    private final ModelRenderer field_78215_f;
    private final ModelRenderer field_78216_g;
    private final ModelRenderer field_78226_h;
    private final ModelRenderer field_78227_i;
    private final ModelRenderer field_78224_j;
    private final ModelRenderer field_78225_k;
    private final ModelRenderer field_78222_l;
    private float partialTicks;

    public ModelDragon(float p_i46360_1_)
    {
        this.textureWidth = 256;
        this.textureHeight = 256;
        this.func_78085_a("body.body", 0, 0);
        this.func_78085_a("wing.skin", -56, 88);
        this.func_78085_a("wingtip.skin", -56, 144);
        this.func_78085_a("rearleg.main", 0, 0);
        this.func_78085_a("rearfoot.main", 112, 0);
        this.func_78085_a("rearlegtip.main", 196, 0);
        this.func_78085_a("head.upperhead", 112, 30);
        this.func_78085_a("wing.bone", 112, 88);
        this.func_78085_a("head.upperlip", 176, 44);
        this.func_78085_a("jaw.jaw", 176, 65);
        this.func_78085_a("frontleg.main", 112, 104);
        this.func_78085_a("wingtip.bone", 112, 136);
        this.func_78085_a("frontfoot.main", 144, 104);
        this.func_78085_a("neck.box", 192, 104);
        this.func_78085_a("frontlegtip.main", 226, 138);
        this.func_78085_a("body.scale", 220, 53);
        this.func_78085_a("head.scale", 0, 0);
        this.func_78085_a("neck.scale", 48, 0);
        this.func_78085_a("head.nostril", 112, 0);
        float f = -16.0F;
        this.head = new ModelRenderer(this, "head");
        this.head.func_78786_a("upperlip", -6.0F, -1.0F, -24.0F, 12, 5, 16);
        this.head.func_78786_a("upperhead", -8.0F, -8.0F, -10.0F, 16, 16, 16);
        this.head.mirror = true;
        this.head.func_78786_a("scale", -5.0F, -12.0F, -4.0F, 2, 4, 6);
        this.head.func_78786_a("nostril", -5.0F, -3.0F, -22.0F, 2, 2, 4);
        this.head.mirror = false;
        this.head.func_78786_a("scale", 3.0F, -12.0F, -4.0F, 2, 4, 6);
        this.head.func_78786_a("nostril", 3.0F, -3.0F, -22.0F, 2, 2, 4);
        this.jaw = new ModelRenderer(this, "jaw");
        this.jaw.setRotationPoint(0.0F, 4.0F, -8.0F);
        this.jaw.func_78786_a("jaw", -6.0F, 0.0F, -16.0F, 12, 4, 16);
        this.head.addChild(this.jaw);
        this.spine = new ModelRenderer(this, "neck");
        this.spine.func_78786_a("box", -5.0F, -5.0F, -5.0F, 10, 10, 10);
        this.spine.func_78786_a("scale", -1.0F, -9.0F, -3.0F, 2, 4, 6);
        this.body = new ModelRenderer(this, "body");
        this.body.setRotationPoint(0.0F, 4.0F, 8.0F);
        this.body.func_78786_a("body", -12.0F, 0.0F, -16.0F, 24, 24, 64);
        this.body.func_78786_a("scale", -1.0F, -6.0F, -10.0F, 2, 6, 12);
        this.body.func_78786_a("scale", -1.0F, -6.0F, 10.0F, 2, 6, 12);
        this.body.func_78786_a("scale", -1.0F, -6.0F, 30.0F, 2, 6, 12);
        this.field_78225_k = new ModelRenderer(this, "wing");
        this.field_78225_k.setRotationPoint(-12.0F, 5.0F, 2.0F);
        this.field_78225_k.func_78786_a("bone", -56.0F, -4.0F, -4.0F, 56, 8, 8);
        this.field_78225_k.func_78786_a("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
        this.field_78222_l = new ModelRenderer(this, "wingtip");
        this.field_78222_l.setRotationPoint(-56.0F, 0.0F, 0.0F);
        this.field_78222_l.func_78786_a("bone", -56.0F, -2.0F, -2.0F, 56, 4, 4);
        this.field_78222_l.func_78786_a("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
        this.field_78225_k.addChild(this.field_78222_l);
        this.field_78215_f = new ModelRenderer(this, "frontleg");
        this.field_78215_f.setRotationPoint(-12.0F, 20.0F, 2.0F);
        this.field_78215_f.func_78786_a("main", -4.0F, -4.0F, -4.0F, 8, 24, 8);
        this.field_78226_h = new ModelRenderer(this, "frontlegtip");
        this.field_78226_h.setRotationPoint(0.0F, 20.0F, -1.0F);
        this.field_78226_h.func_78786_a("main", -3.0F, -1.0F, -3.0F, 6, 24, 6);
        this.field_78215_f.addChild(this.field_78226_h);
        this.field_78224_j = new ModelRenderer(this, "frontfoot");
        this.field_78224_j.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.field_78224_j.func_78786_a("main", -4.0F, 0.0F, -12.0F, 8, 4, 16);
        this.field_78226_h.addChild(this.field_78224_j);
        this.field_78218_e = new ModelRenderer(this, "rearleg");
        this.field_78218_e.setRotationPoint(-16.0F, 16.0F, 42.0F);
        this.field_78218_e.func_78786_a("main", -8.0F, -4.0F, -8.0F, 16, 32, 16);
        this.field_78216_g = new ModelRenderer(this, "rearlegtip");
        this.field_78216_g.setRotationPoint(0.0F, 32.0F, -4.0F);
        this.field_78216_g.func_78786_a("main", -6.0F, -2.0F, 0.0F, 12, 32, 12);
        this.field_78218_e.addChild(this.field_78216_g);
        this.field_78227_i = new ModelRenderer(this, "rearfoot");
        this.field_78227_i.setRotationPoint(0.0F, 31.0F, 4.0F);
        this.field_78227_i.func_78786_a("main", -9.0F, 0.0F, -20.0F, 18, 6, 24);
        this.field_78216_g.addChild(this.field_78227_i);
    }

    public void func_78086_a(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_)
    {
        this.partialTicks = p_78086_4_;
    }

    public void func_78088_a(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        GlStateManager.func_179094_E();
        EntityDragon entitydragon = (EntityDragon)p_78088_1_;
        float f = entitydragon.prevAnimTime + (entitydragon.animTime - entitydragon.prevAnimTime) * this.partialTicks;
        this.jaw.rotateAngleX = (float)(Math.sin((double)(f * ((float)Math.PI * 2F))) + 1.0D) * 0.2F;
        float f1 = (float)(Math.sin((double)(f * ((float)Math.PI * 2F) - 1.0F)) + 1.0D);
        f1 = (f1 * f1 + f1 * 2.0F) * 0.05F;
        GlStateManager.func_179109_b(0.0F, f1 - 2.0F, -3.0F);
        GlStateManager.func_179114_b(f1 * 2.0F, 1.0F, 0.0F, 0.0F);
        float f2 = -30.0F;
        float f4 = 0.0F;
        float f5 = 1.5F;
        double[] adouble = entitydragon.getMovementOffsets(6, this.partialTicks);
        float f6 = this.func_78214_a(entitydragon.getMovementOffsets(5, this.partialTicks)[0] - entitydragon.getMovementOffsets(10, this.partialTicks)[0]);
        float f7 = this.func_78214_a(entitydragon.getMovementOffsets(5, this.partialTicks)[0] + (double)(f6 / 2.0F));
        float f8 = f * ((float)Math.PI * 2F);
        f2 = 20.0F;
        float f3 = -12.0F;

        for (int i = 0; i < 5; ++i)
        {
            double[] adouble1 = entitydragon.getMovementOffsets(5 - i, this.partialTicks);
            float f9 = (float)Math.cos((double)((float)i * 0.45F + f8)) * 0.15F;
            this.spine.rotateAngleY = this.func_78214_a(adouble1[0] - adouble[0]) * 0.017453292F * 1.5F;
            this.spine.rotateAngleX = f9 + entitydragon.getHeadPartYOffset(i, adouble, adouble1) * 0.017453292F * 1.5F * 5.0F;
            this.spine.rotateAngleZ = -this.func_78214_a(adouble1[0] - (double)f7) * 0.017453292F * 1.5F;
            this.spine.rotationPointY = f2;
            this.spine.rotationPointZ = f3;
            this.spine.rotationPointX = f4;
            f2 = (float)((double)f2 + Math.sin((double)this.spine.rotateAngleX) * 10.0D);
            f3 = (float)((double)f3 - Math.cos((double)this.spine.rotateAngleY) * Math.cos((double)this.spine.rotateAngleX) * 10.0D);
            f4 = (float)((double)f4 - Math.sin((double)this.spine.rotateAngleY) * Math.cos((double)this.spine.rotateAngleX) * 10.0D);
            this.spine.func_78785_a(p_78088_7_);
        }

        this.head.rotationPointY = f2;
        this.head.rotationPointZ = f3;
        this.head.rotationPointX = f4;
        double[] adouble2 = entitydragon.getMovementOffsets(0, this.partialTicks);
        this.head.rotateAngleY = this.func_78214_a(adouble2[0] - adouble[0]) * 0.017453292F;
        this.head.rotateAngleX = this.func_78214_a((double)entitydragon.getHeadPartYOffset(6, adouble, adouble2)) * 0.017453292F * 1.5F * 5.0F;
        this.head.rotateAngleZ = -this.func_78214_a(adouble2[0] - (double)f7) * 0.017453292F;
        this.head.func_78785_a(p_78088_7_);
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b(0.0F, 1.0F, 0.0F);
        GlStateManager.func_179114_b(-f6 * 1.5F, 0.0F, 0.0F, 1.0F);
        GlStateManager.func_179109_b(0.0F, -1.0F, 0.0F);
        this.body.rotateAngleZ = 0.0F;
        this.body.func_78785_a(p_78088_7_);

        for (int j = 0; j < 2; ++j)
        {
            GlStateManager.func_179089_o();
            float f11 = f * ((float)Math.PI * 2F);
            this.field_78225_k.rotateAngleX = 0.125F - (float)Math.cos((double)f11) * 0.2F;
            this.field_78225_k.rotateAngleY = 0.25F;
            this.field_78225_k.rotateAngleZ = (float)(Math.sin((double)f11) + 0.125D) * 0.8F;
            this.field_78222_l.rotateAngleZ = -((float)(Math.sin((double)(f11 + 2.0F)) + 0.5D)) * 0.75F;
            this.field_78218_e.rotateAngleX = 1.0F + f1 * 0.1F;
            this.field_78216_g.rotateAngleX = 0.5F + f1 * 0.1F;
            this.field_78227_i.rotateAngleX = 0.75F + f1 * 0.1F;
            this.field_78215_f.rotateAngleX = 1.3F + f1 * 0.1F;
            this.field_78226_h.rotateAngleX = -0.5F - f1 * 0.1F;
            this.field_78224_j.rotateAngleX = 0.75F + f1 * 0.1F;
            this.field_78225_k.func_78785_a(p_78088_7_);
            this.field_78215_f.func_78785_a(p_78088_7_);
            this.field_78218_e.func_78785_a(p_78088_7_);
            GlStateManager.func_179152_a(-1.0F, 1.0F, 1.0F);

            if (j == 0)
            {
                GlStateManager.func_187407_a(GlStateManager.CullFace.FRONT);
            }
        }

        GlStateManager.func_179121_F();
        GlStateManager.func_187407_a(GlStateManager.CullFace.BACK);
        GlStateManager.func_179129_p();
        float f10 = -((float)Math.sin((double)(f * ((float)Math.PI * 2F)))) * 0.0F;
        f8 = f * ((float)Math.PI * 2F);
        f2 = 10.0F;
        f3 = 60.0F;
        f4 = 0.0F;
        adouble = entitydragon.getMovementOffsets(11, this.partialTicks);

        for (int k = 0; k < 12; ++k)
        {
            adouble2 = entitydragon.getMovementOffsets(12 + k, this.partialTicks);
            f10 = (float)((double)f10 + Math.sin((double)((float)k * 0.45F + f8)) * 0.05000000074505806D);
            this.spine.rotateAngleY = (this.func_78214_a(adouble2[0] - adouble[0]) * 1.5F + 180.0F) * 0.017453292F;
            this.spine.rotateAngleX = f10 + (float)(adouble2[1] - adouble[1]) * 0.017453292F * 1.5F * 5.0F;
            this.spine.rotateAngleZ = this.func_78214_a(adouble2[0] - (double)f7) * 0.017453292F * 1.5F;
            this.spine.rotationPointY = f2;
            this.spine.rotationPointZ = f3;
            this.spine.rotationPointX = f4;
            f2 = (float)((double)f2 + Math.sin((double)this.spine.rotateAngleX) * 10.0D);
            f3 = (float)((double)f3 - Math.cos((double)this.spine.rotateAngleY) * Math.cos((double)this.spine.rotateAngleX) * 10.0D);
            f4 = (float)((double)f4 - Math.sin((double)this.spine.rotateAngleY) * Math.cos((double)this.spine.rotateAngleX) * 10.0D);
            this.spine.func_78785_a(p_78088_7_);
        }

        GlStateManager.func_179121_F();
    }

    private float func_78214_a(double p_78214_1_)
    {
        while (p_78214_1_ >= 180.0D)
        {
            p_78214_1_ -= 360.0D;
        }

        while (p_78214_1_ < -180.0D)
        {
            p_78214_1_ += 360.0D;
        }

        return (float)p_78214_1_;
    }
}
