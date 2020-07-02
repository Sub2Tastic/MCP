package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleDragonBreath extends Particle
{
    private final float field_187143_a;
    private boolean hasHitGround;

    protected ParticleDragonBreath(World p_i46581_1_, double p_i46581_2_, double p_i46581_4_, double p_i46581_6_, double p_i46581_8_, double p_i46581_10_, double p_i46581_12_)
    {
        super(p_i46581_1_, p_i46581_2_, p_i46581_4_, p_i46581_6_, p_i46581_8_, p_i46581_10_, p_i46581_12_);
        this.motionX = p_i46581_8_;
        this.motionY = p_i46581_10_;
        this.motionZ = p_i46581_12_;
        this.particleRed = MathHelper.nextFloat(this.rand, 0.7176471F, 0.8745098F);
        this.particleGreen = MathHelper.nextFloat(this.rand, 0.0F, 0.0F);
        this.particleBlue = MathHelper.nextFloat(this.rand, 0.8235294F, 0.9764706F);
        this.particleScale *= 0.75F;
        this.field_187143_a = this.particleScale;
        this.maxAge = (int)(20.0D / ((double)this.rand.nextFloat() * 0.8D + 0.2D));
        this.hasHitGround = false;
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
        else
        {
            this.func_70536_a(3 * this.age / this.maxAge + 5);

            if (this.onGround)
            {
                this.motionY = 0.0D;
                this.hasHitGround = true;
            }

            if (this.hasHitGround)
            {
                this.motionY += 0.002D;
            }

            this.move(this.motionX, this.motionY, this.motionZ);

            if (this.posY == this.prevPosY)
            {
                this.motionX *= 1.1D;
                this.motionZ *= 1.1D;
            }

            this.motionX *= 0.9599999785423279D;
            this.motionZ *= 0.9599999785423279D;

            if (this.hasHitGround)
            {
                this.motionY *= 0.9599999785423279D;
            }
        }
    }

    public void func_180434_a(BufferBuilder p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        this.particleScale = this.field_187143_a * MathHelper.clamp(((float)this.age + p_180434_3_) / (float)this.maxAge * 32.0F, 0.0F, 1.0F);
        super.func_180434_a(p_180434_1_, p_180434_2_, p_180434_3_, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
    }

    public static class Factory implements IParticleFactory
    {
        public Particle func_178902_a(int p_178902_1_, World p_178902_2_, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_)
        {
            return new ParticleDragonBreath(p_178902_2_, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}
