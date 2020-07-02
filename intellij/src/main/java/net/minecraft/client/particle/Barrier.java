package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class Barrier extends Particle
{
    protected Barrier(World p_i46286_1_, double p_i46286_2_, double p_i46286_4_, double p_i46286_6_, Item p_i46286_8_)
    {
        super(p_i46286_1_, p_i46286_2_, p_i46286_4_, p_i46286_6_, 0.0D, 0.0D, 0.0D);
        this.func_187117_a(Minecraft.getInstance().getItemRenderer().getItemModelMesher().func_178082_a(p_i46286_8_));
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.particleGravity = 0.0F;
        this.maxAge = 80;
    }

    public int func_70537_b()
    {
        return 1;
    }

    public void func_180434_a(BufferBuilder p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        float f = this.field_187119_C.getMinU();
        float f1 = this.field_187119_C.getMaxU();
        float f2 = this.field_187119_C.getMinV();
        float f3 = this.field_187119_C.getMaxV();
        float f4 = 0.5F;
        float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)p_180434_3_ - field_70556_an);
        float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)p_180434_3_ - field_70554_ao);
        float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)p_180434_3_ - field_70555_ap);
        int i = this.getBrightnessForRender(p_180434_3_);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        p_180434_1_.func_181662_b((double)(f5 - p_180434_4_ * 0.5F - p_180434_7_ * 0.5F), (double)(f6 - p_180434_5_ * 0.5F), (double)(f7 - p_180434_6_ * 0.5F - p_180434_8_ * 0.5F)).func_187315_a((double)f1, (double)f3).func_181666_a(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).func_187314_a(j, k).endVertex();
        p_180434_1_.func_181662_b((double)(f5 - p_180434_4_ * 0.5F + p_180434_7_ * 0.5F), (double)(f6 + p_180434_5_ * 0.5F), (double)(f7 - p_180434_6_ * 0.5F + p_180434_8_ * 0.5F)).func_187315_a((double)f1, (double)f2).func_181666_a(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).func_187314_a(j, k).endVertex();
        p_180434_1_.func_181662_b((double)(f5 + p_180434_4_ * 0.5F + p_180434_7_ * 0.5F), (double)(f6 + p_180434_5_ * 0.5F), (double)(f7 + p_180434_6_ * 0.5F + p_180434_8_ * 0.5F)).func_187315_a((double)f, (double)f2).func_181666_a(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).func_187314_a(j, k).endVertex();
        p_180434_1_.func_181662_b((double)(f5 + p_180434_4_ * 0.5F - p_180434_7_ * 0.5F), (double)(f6 - p_180434_5_ * 0.5F), (double)(f7 + p_180434_6_ * 0.5F - p_180434_8_ * 0.5F)).func_187315_a((double)f, (double)f3).func_181666_a(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).func_187314_a(j, k).endVertex();
    }

    public static class Factory implements IParticleFactory
    {
        public Particle func_178902_a(int p_178902_1_, World p_178902_2_, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_)
        {
            return new Barrier(p_178902_2_, p_178902_3_, p_178902_5_, p_178902_7_, Item.getItemFromBlock(Blocks.BARRIER));
        }
    }
}
