package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelDragonHead extends ModelBase
{
    private final ModelRenderer head;
    private final ModelRenderer jaw;

    public ModelDragonHead(float p_i46588_1_)
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
    }

    public void func_78088_a(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        this.jaw.rotateAngleX = (float)(Math.sin((double)(p_78088_2_ * (float)Math.PI * 0.2F)) + 1.0D) * 0.2F;
        this.head.rotateAngleY = p_78088_5_ * 0.017453292F;
        this.head.rotateAngleX = p_78088_6_ * 0.017453292F;
        GlStateManager.func_179109_b(0.0F, -0.374375F, 0.0F);
        GlStateManager.func_179152_a(0.75F, 0.75F, 0.75F);
        this.head.func_78785_a(p_78088_7_);
    }
}
