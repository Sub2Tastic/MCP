package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ParticleSweepAttack extends Particle
{
    private static final ResourceLocation field_187137_a = new ResourceLocation("textures/entity/sweep.png");
    private static final VertexFormat field_187138_G = (new VertexFormat()).func_181721_a(DefaultVertexFormats.POSITION_3F).func_181721_a(DefaultVertexFormats.TEX_2F).func_181721_a(DefaultVertexFormats.COLOR_4UB).func_181721_a(DefaultVertexFormats.TEX_2S).func_181721_a(DefaultVertexFormats.NORMAL_3B).func_181721_a(DefaultVertexFormats.PADDING_1B);
    private int field_187139_H;
    private final int field_187140_I;
    private final TextureManager field_187141_J;
    private final float field_187142_K;

    protected ParticleSweepAttack(TextureManager p_i46582_1_, World p_i46582_2_, double p_i46582_3_, double p_i46582_5_, double p_i46582_7_, double p_i46582_9_, double p_i46582_11_, double p_i46582_13_)
    {
        super(p_i46582_2_, p_i46582_3_, p_i46582_5_, p_i46582_7_, 0.0D, 0.0D, 0.0D);
        this.field_187141_J = p_i46582_1_;
        this.field_187140_I = 4;
        float f = this.rand.nextFloat() * 0.6F + 0.4F;
        this.particleRed = f;
        this.particleGreen = f;
        this.particleBlue = f;
        this.field_187142_K = 1.0F - (float)p_i46582_9_ * 0.5F;
    }

    public void func_180434_a(BufferBuilder p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        int i = (int)(((float)this.field_187139_H + p_180434_3_) * 3.0F / (float)this.field_187140_I);

        if (i <= 7)
        {
            this.field_187141_J.bindTexture(field_187137_a);
            float f = (float)(i % 4) / 4.0F;
            float f1 = f + 0.24975F;
            float f2 = (float)(i / 2) / 2.0F;
            float f3 = f2 + 0.4995F;
            float f4 = 1.0F * this.field_187142_K;
            float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)p_180434_3_ - field_70556_an);
            float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)p_180434_3_ - field_70554_ao);
            float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)p_180434_3_ - field_70555_ap);
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.func_179140_f();
            RenderHelper.disableStandardItemLighting();
            p_180434_1_.begin(7, field_187138_G);
            p_180434_1_.func_181662_b((double)(f5 - p_180434_4_ * f4 - p_180434_7_ * f4), (double)(f6 - p_180434_5_ * f4 * 0.5F), (double)(f7 - p_180434_6_ * f4 - p_180434_8_ * f4)).func_187315_a((double)f1, (double)f3).func_181666_a(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).func_187314_a(0, 240).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
            p_180434_1_.func_181662_b((double)(f5 - p_180434_4_ * f4 + p_180434_7_ * f4), (double)(f6 + p_180434_5_ * f4 * 0.5F), (double)(f7 - p_180434_6_ * f4 + p_180434_8_ * f4)).func_187315_a((double)f1, (double)f2).func_181666_a(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).func_187314_a(0, 240).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
            p_180434_1_.func_181662_b((double)(f5 + p_180434_4_ * f4 + p_180434_7_ * f4), (double)(f6 + p_180434_5_ * f4 * 0.5F), (double)(f7 + p_180434_6_ * f4 + p_180434_8_ * f4)).func_187315_a((double)f, (double)f2).func_181666_a(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).func_187314_a(0, 240).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
            p_180434_1_.func_181662_b((double)(f5 + p_180434_4_ * f4 - p_180434_7_ * f4), (double)(f6 - p_180434_5_ * f4 * 0.5F), (double)(f7 + p_180434_6_ * f4 - p_180434_8_ * f4)).func_187315_a((double)f, (double)f3).func_181666_a(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).func_187314_a(0, 240).func_181663_c(0.0F, 1.0F, 0.0F).endVertex();
            Tessellator.getInstance().draw();
            GlStateManager.func_179145_e();
        }
    }

    public int getBrightnessForRender(float partialTick)
    {
        return 61680;
    }

    public void tick()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        ++this.field_187139_H;

        if (this.field_187139_H == this.field_187140_I)
        {
            this.setExpired();
        }
    }

    public int func_70537_b()
    {
        return 3;
    }

    public static class Factory implements IParticleFactory
    {
        public Particle func_178902_a(int p_178902_1_, World p_178902_2_, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_)
        {
            return new ParticleSweepAttack(Minecraft.getInstance().getTextureManager(), p_178902_2_, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}
