package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleNote extends Particle
{
    float field_70585_a;

    protected ParticleNote(World p_i46353_1_, double p_i46353_2_, double p_i46353_4_, double p_i46353_6_, double p_i46353_8_, double p_i46353_10_, double p_i46353_12_)
    {
        this(p_i46353_1_, p_i46353_2_, p_i46353_4_, p_i46353_6_, p_i46353_8_, p_i46353_10_, p_i46353_12_, 2.0F);
    }

    protected ParticleNote(World p_i1217_1_, double p_i1217_2_, double p_i1217_4_, double p_i1217_6_, double p_i1217_8_, double p_i1217_10_, double p_i1217_12_, float p_i1217_14_)
    {
        super(p_i1217_1_, p_i1217_2_, p_i1217_4_, p_i1217_6_, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.009999999776482582D;
        this.motionY *= 0.009999999776482582D;
        this.motionZ *= 0.009999999776482582D;
        this.motionY += 0.2D;
        this.particleRed = MathHelper.sin(((float)p_i1217_8_ + 0.0F) * ((float)Math.PI * 2F)) * 0.65F + 0.35F;
        this.particleGreen = MathHelper.sin(((float)p_i1217_8_ + 0.33333334F) * ((float)Math.PI * 2F)) * 0.65F + 0.35F;
        this.particleBlue = MathHelper.sin(((float)p_i1217_8_ + 0.6666667F) * ((float)Math.PI * 2F)) * 0.65F + 0.35F;
        this.particleScale *= 0.75F;
        this.particleScale *= p_i1217_14_;
        this.field_70585_a = this.particleScale;
        this.maxAge = 6;
        this.func_70536_a(64);
    }

    public void func_180434_a(BufferBuilder p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        float f = ((float)this.age + p_180434_3_) / (float)this.maxAge * 32.0F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        this.particleScale = this.field_70585_a * f;
        super.func_180434_a(p_180434_1_, p_180434_2_, p_180434_3_, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
    }

    public void tick()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.age++ >= this.maxAge)
        {
            this.setExpired();
        }

        this.move(this.motionX, this.motionY, this.motionZ);

        if (this.posY == this.prevPosY)
        {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.motionX *= 0.6600000262260437D;
        this.motionY *= 0.6600000262260437D;
        this.motionZ *= 0.6600000262260437D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

    public static class Factory implements IParticleFactory
    {
        public Particle func_178902_a(int p_178902_1_, World p_178902_2_, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_)
        {
            return new ParticleNote(p_178902_2_, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}
