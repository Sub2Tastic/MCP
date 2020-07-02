package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ParticleItemPickup extends Particle
{
    private final Entity item;
    private final Entity target;
    private int age;
    private final int field_70593_as;
    private final float field_174841_aA;
    private final RenderManager renderManager = Minecraft.getInstance().getRenderManager();

    public ParticleItemPickup(World p_i1233_1_, Entity p_i1233_2_, Entity p_i1233_3_, float p_i1233_4_)
    {
        super(p_i1233_1_, p_i1233_2_.posX, p_i1233_2_.posY, p_i1233_2_.posZ, p_i1233_2_.field_70159_w, p_i1233_2_.field_70181_x, p_i1233_2_.field_70179_y);
        this.item = p_i1233_2_;
        this.target = p_i1233_3_;
        this.field_70593_as = 3;
        this.field_174841_aA = p_i1233_4_;
    }

    public void func_180434_a(BufferBuilder p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        float f = ((float)this.age + p_180434_3_) / (float)this.field_70593_as;
        f = f * f;
        double d0 = this.item.posX;
        double d1 = this.item.posY;
        double d2 = this.item.posZ;
        double d3 = this.target.lastTickPosX + (this.target.posX - this.target.lastTickPosX) * (double)p_180434_3_;
        double d4 = this.target.lastTickPosY + (this.target.posY - this.target.lastTickPosY) * (double)p_180434_3_ + (double)this.field_174841_aA;
        double d5 = this.target.lastTickPosZ + (this.target.posZ - this.target.lastTickPosZ) * (double)p_180434_3_;
        double d6 = d0 + (d3 - d0) * (double)f;
        double d7 = d1 + (d4 - d1) * (double)f;
        double d8 = d2 + (d5 - d2) * (double)f;
        int i = this.getBrightnessForRender(p_180434_3_);
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)j, (float)k);
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        d6 = d6 - field_70556_an;
        d7 = d7 - field_70554_ao;
        d8 = d8 - field_70555_ap;
        GlStateManager.func_179145_e();
        this.renderManager.func_188391_a(this.item, d6, d7, d8, this.item.rotationYaw, p_180434_3_, false);
    }

    public void tick()
    {
        ++this.age;

        if (this.age == this.field_70593_as)
        {
            this.setExpired();
        }
    }

    public int func_70537_b()
    {
        return 3;
    }
}
