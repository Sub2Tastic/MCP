package net.minecraft.client.particle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ParticleEmitter extends Particle
{
    private final Entity attachedEntity;
    private int age;
    private final int lifetime;
    private final EnumParticleTypes particleTypes;

    public ParticleEmitter(World p_i46279_1_, Entity p_i46279_2_, EnumParticleTypes p_i46279_3_)
    {
        this(p_i46279_1_, p_i46279_2_, p_i46279_3_, 3);
    }

    public ParticleEmitter(World p_i47219_1_, Entity p_i47219_2_, EnumParticleTypes p_i47219_3_, int p_i47219_4_)
    {
        super(p_i47219_1_, p_i47219_2_.posX, p_i47219_2_.getBoundingBox().minY + (double)(p_i47219_2_.field_70131_O / 2.0F), p_i47219_2_.posZ, p_i47219_2_.field_70159_w, p_i47219_2_.field_70181_x, p_i47219_2_.field_70179_y);
        this.attachedEntity = p_i47219_2_;
        this.lifetime = p_i47219_4_;
        this.particleTypes = p_i47219_3_;
        this.tick();
    }

    public void func_180434_a(BufferBuilder p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
    }

    public void tick()
    {
        for (int i = 0; i < 16; ++i)
        {
            double d0 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);
            double d1 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);
            double d2 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);

            if (d0 * d0 + d1 * d1 + d2 * d2 <= 1.0D)
            {
                double d3 = this.attachedEntity.posX + d0 * (double)this.attachedEntity.field_70130_N / 4.0D;
                double d4 = this.attachedEntity.getBoundingBox().minY + (double)(this.attachedEntity.field_70131_O / 2.0F) + d1 * (double)this.attachedEntity.field_70131_O / 4.0D;
                double d5 = this.attachedEntity.posZ + d2 * (double)this.attachedEntity.field_70130_N / 4.0D;
                this.world.func_175682_a(this.particleTypes, false, d3, d4, d5, d0, d1 + 0.2D, d2);
            }
        }

        ++this.age;

        if (this.age >= this.lifetime)
        {
            this.setExpired();
        }
    }

    public int func_70537_b()
    {
        return 3;
    }
}
