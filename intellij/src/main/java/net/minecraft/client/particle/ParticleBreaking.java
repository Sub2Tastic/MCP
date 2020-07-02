package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class ParticleBreaking extends Particle
{
    protected ParticleBreaking(World p_i1195_1_, double p_i1195_2_, double p_i1195_4_, double p_i1195_6_, Item p_i1195_8_)
    {
        this(p_i1195_1_, p_i1195_2_, p_i1195_4_, p_i1195_6_, p_i1195_8_, 0);
    }

    protected ParticleBreaking(World p_i1197_1_, double p_i1197_2_, double p_i1197_4_, double p_i1197_6_, double p_i1197_8_, double p_i1197_10_, double p_i1197_12_, Item p_i1197_14_, int p_i1197_15_)
    {
        this(p_i1197_1_, p_i1197_2_, p_i1197_4_, p_i1197_6_, p_i1197_14_, p_i1197_15_);
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.motionX += p_i1197_8_;
        this.motionY += p_i1197_10_;
        this.motionZ += p_i1197_12_;
    }

    protected ParticleBreaking(World p_i1196_1_, double p_i1196_2_, double p_i1196_4_, double p_i1196_6_, Item p_i1196_8_, int p_i1196_9_)
    {
        super(p_i1196_1_, p_i1196_2_, p_i1196_4_, p_i1196_6_, 0.0D, 0.0D, 0.0D);
        this.func_187117_a(Minecraft.getInstance().getItemRenderer().getItemModelMesher().func_178087_a(p_i1196_8_, p_i1196_9_));
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.particleGravity = Blocks.SNOW.field_149763_I;
        this.particleScale /= 2.0F;
    }

    public int func_70537_b()
    {
        return 1;
    }

    public void func_180434_a(BufferBuilder p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        float f = ((float)this.field_94054_b + this.field_70548_b / 4.0F) / 16.0F;
        float f1 = f + 0.015609375F;
        float f2 = ((float)this.field_94055_c + this.field_70549_c / 4.0F) / 16.0F;
        float f3 = f2 + 0.015609375F;
        float f4 = 0.1F * this.particleScale;

        if (this.field_187119_C != null)
        {
            f = this.field_187119_C.getInterpolatedU((double)(this.field_70548_b / 4.0F * 16.0F));
            f1 = this.field_187119_C.getInterpolatedU((double)((this.field_70548_b + 1.0F) / 4.0F * 16.0F));
            f2 = this.field_187119_C.getInterpolatedV((double)(this.field_70549_c / 4.0F * 16.0F));
            f3 = this.field_187119_C.getInterpolatedV((double)((this.field_70549_c + 1.0F) / 4.0F * 16.0F));
        }

        float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)p_180434_3_ - field_70556_an);
        float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)p_180434_3_ - field_70554_ao);
        float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)p_180434_3_ - field_70555_ap);
        int i = this.getBrightnessForRender(p_180434_3_);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        p_180434_1_.func_181662_b((double)(f5 - p_180434_4_ * f4 - p_180434_7_ * f4), (double)(f6 - p_180434_5_ * f4), (double)(f7 - p_180434_6_ * f4 - p_180434_8_ * f4)).func_187315_a((double)f, (double)f3).func_181666_a(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).func_187314_a(j, k).endVertex();
        p_180434_1_.func_181662_b((double)(f5 - p_180434_4_ * f4 + p_180434_7_ * f4), (double)(f6 + p_180434_5_ * f4), (double)(f7 - p_180434_6_ * f4 + p_180434_8_ * f4)).func_187315_a((double)f, (double)f2).func_181666_a(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).func_187314_a(j, k).endVertex();
        p_180434_1_.func_181662_b((double)(f5 + p_180434_4_ * f4 + p_180434_7_ * f4), (double)(f6 + p_180434_5_ * f4), (double)(f7 + p_180434_6_ * f4 + p_180434_8_ * f4)).func_187315_a((double)f1, (double)f2).func_181666_a(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).func_187314_a(j, k).endVertex();
        p_180434_1_.func_181662_b((double)(f5 + p_180434_4_ * f4 - p_180434_7_ * f4), (double)(f6 - p_180434_5_ * f4), (double)(f7 + p_180434_6_ * f4 - p_180434_8_ * f4)).func_187315_a((double)f1, (double)f3).func_181666_a(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).func_187314_a(j, k).endVertex();
    }

    public static class Factory implements IParticleFactory
    {
        public Particle func_178902_a(int p_178902_1_, World p_178902_2_, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_)
        {
            int i = p_178902_15_.length > 1 ? p_178902_15_[1] : 0;
            return new ParticleBreaking(p_178902_2_, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_, Item.getItemById(p_178902_15_[0]), i);
        }
    }

    public static class SlimeFactory implements IParticleFactory
    {
        public Particle func_178902_a(int p_178902_1_, World p_178902_2_, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_)
        {
            return new ParticleBreaking(p_178902_2_, p_178902_3_, p_178902_5_, p_178902_7_, Items.SLIME_BALL);
        }
    }

    public static class SnowballFactory implements IParticleFactory
    {
        public Particle func_178902_a(int p_178902_1_, World p_178902_2_, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_)
        {
            return new ParticleBreaking(p_178902_2_, p_178902_3_, p_178902_5_, p_178902_7_, Items.SNOWBALL);
        }
    }
}
