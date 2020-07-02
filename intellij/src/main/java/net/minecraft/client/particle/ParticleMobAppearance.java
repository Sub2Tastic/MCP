package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleMobAppearance extends Particle
{
    private EntityLivingBase field_174844_a;

    protected ParticleMobAppearance(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.particleGravity = 0.0F;
        this.maxAge = 30;
    }

    public int func_70537_b()
    {
        return 3;
    }

    public void tick()
    {
        super.tick();

        if (this.field_174844_a == null)
        {
            EntityElderGuardian entityelderguardian = new EntityElderGuardian(this.world);
            entityelderguardian.func_190767_di();
            this.field_174844_a = entityelderguardian;
        }
    }

    public void func_180434_a(BufferBuilder p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        if (this.field_174844_a != null)
        {
            RenderManager rendermanager = Minecraft.getInstance().getRenderManager();
            rendermanager.func_178628_a(Particle.field_70556_an, Particle.field_70554_ao, Particle.field_70555_ap);
            float f = 0.42553192F;
            float f1 = ((float)this.age + p_180434_3_) / (float)this.maxAge;
            GlStateManager.func_179132_a(true);
            GlStateManager.func_179147_l();
            GlStateManager.func_179126_j();
            GlStateManager.func_187401_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            float f2 = 240.0F;
            OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, 240.0F, 240.0F);
            GlStateManager.func_179094_E();
            float f3 = 0.05F + 0.5F * MathHelper.sin(f1 * (float)Math.PI);
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, f3);
            GlStateManager.func_179109_b(0.0F, 1.8F, 0.0F);
            GlStateManager.func_179114_b(180.0F - p_180434_2_.rotationYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.func_179114_b(60.0F - 150.0F * f1 - p_180434_2_.rotationPitch, 1.0F, 0.0F, 0.0F);
            GlStateManager.func_179109_b(0.0F, -0.4F, -1.5F);
            GlStateManager.func_179152_a(0.42553192F, 0.42553192F, 0.42553192F);
            this.field_174844_a.rotationYaw = 0.0F;
            this.field_174844_a.rotationYawHead = 0.0F;
            this.field_174844_a.prevRotationYaw = 0.0F;
            this.field_174844_a.prevRotationYawHead = 0.0F;
            rendermanager.func_188391_a(this.field_174844_a, 0.0D, 0.0D, 0.0D, 0.0F, p_180434_3_, false);
            GlStateManager.func_179121_F();
            GlStateManager.func_179126_j();
        }
    }

    public static class Factory implements IParticleFactory
    {
        public Particle func_178902_a(int p_178902_1_, World p_178902_2_, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_)
        {
            return new ParticleMobAppearance(p_178902_2_, p_178902_3_, p_178902_5_, p_178902_7_);
        }
    }
}
