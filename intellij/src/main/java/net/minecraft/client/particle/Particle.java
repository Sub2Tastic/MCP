package net.minecraft.client.particle;

import java.util.List;
import java.util.Random;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Particle
{
    private static final AxisAlignedBB EMPTY_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    protected World world;
    protected double prevPosX;
    protected double prevPosY;
    protected double prevPosZ;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected double motionX;
    protected double motionY;
    protected double motionZ;
    private AxisAlignedBB boundingBox;
    protected boolean onGround;
    protected boolean canCollide;
    protected boolean isExpired;
    protected float width;
    protected float height;
    protected Random rand;
    protected int field_94054_b;
    protected int field_94055_c;
    protected float field_70548_b;
    protected float field_70549_c;
    protected int age;
    protected int maxAge;
    protected float particleScale;
    protected float particleGravity;
    protected float particleRed;
    protected float particleGreen;
    protected float particleBlue;
    protected float particleAlpha;
    protected TextureAtlasSprite field_187119_C;
    protected float particleAngle;
    protected float prevParticleAngle;
    public static double field_70556_an;
    public static double field_70554_ao;
    public static double field_70555_ap;
    public static Vec3d field_190016_K;

    protected Particle(World worldIn, double posXIn, double posYIn, double posZIn)
    {
        this.boundingBox = EMPTY_AABB;
        this.width = 0.6F;
        this.height = 1.8F;
        this.rand = new Random();
        this.particleAlpha = 1.0F;
        this.world = worldIn;
        this.setSize(0.2F, 0.2F);
        this.setPosition(posXIn, posYIn, posZIn);
        this.prevPosX = posXIn;
        this.prevPosY = posYIn;
        this.prevPosZ = posZIn;
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.field_70548_b = this.rand.nextFloat() * 3.0F;
        this.field_70549_c = this.rand.nextFloat() * 3.0F;
        this.particleScale = (this.rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
        this.maxAge = (int)(4.0F / (this.rand.nextFloat() * 0.9F + 0.1F));
        this.age = 0;
        this.canCollide = true;
    }

    public Particle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.motionX = xSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        this.motionY = ySpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        this.motionZ = zSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        float f = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
        float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        this.motionX = this.motionX / (double)f1 * (double)f * 0.4000000059604645D;
        this.motionY = this.motionY / (double)f1 * (double)f * 0.4000000059604645D + 0.10000000149011612D;
        this.motionZ = this.motionZ / (double)f1 * (double)f * 0.4000000059604645D;
    }

    public Particle multiplyVelocity(float multiplier)
    {
        this.motionX *= (double)multiplier;
        this.motionY = (this.motionY - 0.10000000149011612D) * (double)multiplier + 0.10000000149011612D;
        this.motionZ *= (double)multiplier;
        return this;
    }

    public Particle multiplyParticleScaleBy(float scale)
    {
        this.setSize(0.2F * scale, 0.2F * scale);
        this.particleScale *= scale;
        return this;
    }

    public void setColor(float particleRedIn, float particleGreenIn, float particleBlueIn)
    {
        this.particleRed = particleRedIn;
        this.particleGreen = particleGreenIn;
        this.particleBlue = particleBlueIn;
    }

    /**
     * Sets the particle alpha (float)
     */
    public void setAlphaF(float alpha)
    {
        this.particleAlpha = alpha;
    }

    public boolean func_187111_c()
    {
        return false;
    }

    public float func_70534_d()
    {
        return this.particleRed;
    }

    public float func_70542_f()
    {
        return this.particleGreen;
    }

    public float func_70535_g()
    {
        return this.particleBlue;
    }

    public void setMaxAge(int particleLifeTime)
    {
        this.maxAge = particleLifeTime;
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

        this.motionY -= 0.04D * (double)this.particleGravity;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

    public void func_180434_a(BufferBuilder p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        float f = (float)this.field_94054_b / 16.0F;
        float f1 = f + 0.0624375F;
        float f2 = (float)this.field_94055_c / 16.0F;
        float f3 = f2 + 0.0624375F;
        float f4 = 0.1F * this.particleScale;

        if (this.field_187119_C != null)
        {
            f = this.field_187119_C.getMinU();
            f1 = this.field_187119_C.getMaxU();
            f2 = this.field_187119_C.getMinV();
            f3 = this.field_187119_C.getMaxV();
        }

        float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)p_180434_3_ - field_70556_an);
        float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)p_180434_3_ - field_70554_ao);
        float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)p_180434_3_ - field_70555_ap);
        int i = this.getBrightnessForRender(p_180434_3_);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        Vec3d[] avec3d = new Vec3d[] {new Vec3d((double)(-p_180434_4_ * f4 - p_180434_7_ * f4), (double)(-p_180434_5_ * f4), (double)(-p_180434_6_ * f4 - p_180434_8_ * f4)), new Vec3d((double)(-p_180434_4_ * f4 + p_180434_7_ * f4), (double)(p_180434_5_ * f4), (double)(-p_180434_6_ * f4 + p_180434_8_ * f4)), new Vec3d((double)(p_180434_4_ * f4 + p_180434_7_ * f4), (double)(p_180434_5_ * f4), (double)(p_180434_6_ * f4 + p_180434_8_ * f4)), new Vec3d((double)(p_180434_4_ * f4 - p_180434_7_ * f4), (double)(-p_180434_5_ * f4), (double)(p_180434_6_ * f4 - p_180434_8_ * f4))};

        if (this.particleAngle != 0.0F)
        {
            float f8 = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * p_180434_3_;
            float f9 = MathHelper.cos(f8 * 0.5F);
            float f10 = MathHelper.sin(f8 * 0.5F) * (float)field_190016_K.x;
            float f11 = MathHelper.sin(f8 * 0.5F) * (float)field_190016_K.y;
            float f12 = MathHelper.sin(f8 * 0.5F) * (float)field_190016_K.z;
            Vec3d vec3d = new Vec3d((double)f10, (double)f11, (double)f12);

            for (int l = 0; l < 4; ++l)
            {
                avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((double)(f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale((double)(2.0F * f9)));
            }
        }

        p_180434_1_.func_181662_b((double)f5 + avec3d[0].x, (double)f6 + avec3d[0].y, (double)f7 + avec3d[0].z).func_187315_a((double)f1, (double)f3).func_181666_a(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).func_187314_a(j, k).endVertex();
        p_180434_1_.func_181662_b((double)f5 + avec3d[1].x, (double)f6 + avec3d[1].y, (double)f7 + avec3d[1].z).func_187315_a((double)f1, (double)f2).func_181666_a(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).func_187314_a(j, k).endVertex();
        p_180434_1_.func_181662_b((double)f5 + avec3d[2].x, (double)f6 + avec3d[2].y, (double)f7 + avec3d[2].z).func_187315_a((double)f, (double)f2).func_181666_a(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).func_187314_a(j, k).endVertex();
        p_180434_1_.func_181662_b((double)f5 + avec3d[3].x, (double)f6 + avec3d[3].y, (double)f7 + avec3d[3].z).func_187315_a((double)f, (double)f3).func_181666_a(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).func_187314_a(j, k).endVertex();
    }

    public int func_70537_b()
    {
        return 0;
    }

    public void func_187117_a(TextureAtlasSprite p_187117_1_)
    {
        int i = this.func_70537_b();

        if (i == 1)
        {
            this.field_187119_C = p_187117_1_;
        }
        else
        {
            throw new RuntimeException("Invalid call to Particle.setTex, use coordinate methods");
        }
    }

    public void func_70536_a(int p_70536_1_)
    {
        if (this.func_70537_b() != 0)
        {
            throw new RuntimeException("Invalid call to Particle.setMiscTex");
        }
        else
        {
            this.field_94054_b = p_70536_1_ % 16;
            this.field_94055_c = p_70536_1_ / 16;
        }
    }

    public void func_94053_h()
    {
        ++this.field_94054_b;
    }

    public String toString()
    {
        return this.getClass().getSimpleName() + ", Pos (" + this.posX + "," + this.posY + "," + this.posZ + "), RGBA (" + this.particleRed + "," + this.particleGreen + "," + this.particleBlue + "," + this.particleAlpha + "), Age " + this.age;
    }

    /**
     * Called to indicate that this particle effect has expired and should be discontinued.
     */
    public void setExpired()
    {
        this.isExpired = true;
    }

    protected void setSize(float particleWidth, float particleHeight)
    {
        if (particleWidth != this.width || particleHeight != this.height)
        {
            this.width = particleWidth;
            this.height = particleHeight;
            AxisAlignedBB axisalignedbb = this.getBoundingBox();
            this.setBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double)this.width, axisalignedbb.minY + (double)this.height, axisalignedbb.minZ + (double)this.width));
        }
    }

    public void setPosition(double x, double y, double z)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        float f = this.width / 2.0F;
        float f1 = this.height;
        this.setBoundingBox(new AxisAlignedBB(x - (double)f, y, z - (double)f, x + (double)f, y + (double)f1, z + (double)f));
    }

    public void move(double x, double y, double z)
    {
        double d0 = y;

        if (this.canCollide)
        {
            List<AxisAlignedBB> list = this.world.func_184144_a((Entity)null, this.getBoundingBox().expand(x, y, z));

            for (AxisAlignedBB axisalignedbb : list)
            {
                y = axisalignedbb.func_72323_b(this.getBoundingBox(), y);
            }

            this.setBoundingBox(this.getBoundingBox().offset(0.0D, y, 0.0D));

            for (AxisAlignedBB axisalignedbb1 : list)
            {
                x = axisalignedbb1.func_72316_a(this.getBoundingBox(), x);
            }

            this.setBoundingBox(this.getBoundingBox().offset(x, 0.0D, 0.0D));

            for (AxisAlignedBB axisalignedbb2 : list)
            {
                z = axisalignedbb2.func_72322_c(this.getBoundingBox(), z);
            }

            this.setBoundingBox(this.getBoundingBox().offset(0.0D, 0.0D, z));
        }
        else
        {
            this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        }

        this.resetPositionToBB();
        this.onGround = y != y && d0 < 0.0D;

        if (x != x)
        {
            this.motionX = 0.0D;
        }

        if (z != z)
        {
            this.motionZ = 0.0D;
        }
    }

    protected void resetPositionToBB()
    {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        this.posX = (axisalignedbb.minX + axisalignedbb.maxX) / 2.0D;
        this.posY = axisalignedbb.minY;
        this.posZ = (axisalignedbb.minZ + axisalignedbb.maxZ) / 2.0D;
    }

    public int getBrightnessForRender(float partialTick)
    {
        BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
        return this.world.isBlockLoaded(blockpos) ? this.world.func_175626_b(blockpos, 0) : 0;
    }

    /**
     * Returns true if this effect has not yet expired. "I feel happy! I feel happy!"
     */
    public boolean isAlive()
    {
        return !this.isExpired;
    }

    public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }

    public void setBoundingBox(AxisAlignedBB bb)
    {
        this.boundingBox = bb;
    }
}
