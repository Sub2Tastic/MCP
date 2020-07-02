package net.minecraft.client.particle;

import net.minecraft.world.World;

public class ParticleSimpleAnimated extends Particle
{
    private final int field_187147_a;
    private final int field_187148_G;
    private final float yAccel;
    private float baseAirFriction = 0.91F;
    private float fadeTargetRed;
    private float fadeTargetGreen;
    private float fadeTargetBlue;
    private boolean fadingColor;

    public ParticleSimpleAnimated(World p_i46578_1_, double p_i46578_2_, double p_i46578_4_, double p_i46578_6_, int p_i46578_8_, int p_i46578_9_, float p_i46578_10_)
    {
        super(p_i46578_1_, p_i46578_2_, p_i46578_4_, p_i46578_6_);
        this.field_187147_a = p_i46578_8_;
        this.field_187148_G = p_i46578_9_;
        this.yAccel = p_i46578_10_;
    }

    public void setColor(int p_187146_1_)
    {
        float f = (float)((p_187146_1_ & 16711680) >> 16) / 255.0F;
        float f1 = (float)((p_187146_1_ & 65280) >> 8) / 255.0F;
        float f2 = (float)((p_187146_1_ & 255) >> 0) / 255.0F;
        float f3 = 1.0F;
        this.setColor(f * 1.0F, f1 * 1.0F, f2 * 1.0F);
    }

    /**
     * sets a color for the particle to drift toward (20% closer each tick, never actually getting very close)
     */
    public void setColorFade(int rgb)
    {
        this.fadeTargetRed = (float)((rgb & 16711680) >> 16) / 255.0F;
        this.fadeTargetGreen = (float)((rgb & 65280) >> 8) / 255.0F;
        this.fadeTargetBlue = (float)((rgb & 255) >> 0) / 255.0F;
        this.fadingColor = true;
    }

    public boolean func_187111_c()
    {
        return true;
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

        if (this.age > this.maxAge / 2)
        {
            this.setAlphaF(1.0F - ((float)this.age - (float)(this.maxAge / 2)) / (float)this.maxAge);

            if (this.fadingColor)
            {
                this.particleRed += (this.fadeTargetRed - this.particleRed) * 0.2F;
                this.particleGreen += (this.fadeTargetGreen - this.particleGreen) * 0.2F;
                this.particleBlue += (this.fadeTargetBlue - this.particleBlue) * 0.2F;
            }
        }

        this.func_70536_a(this.field_187147_a + (this.field_187148_G - 1 - this.age * this.field_187148_G / this.maxAge));
        this.motionY += (double)this.yAccel;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= (double)this.baseAirFriction;
        this.motionY *= (double)this.baseAirFriction;
        this.motionZ *= (double)this.baseAirFriction;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

    public int getBrightnessForRender(float partialTick)
    {
        return 15728880;
    }

    protected void setBaseAirFriction(float p_191238_1_)
    {
        this.baseAirFriction = p_191238_1_;
    }
}
