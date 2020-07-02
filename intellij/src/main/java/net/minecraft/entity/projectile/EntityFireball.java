package net.minecraft.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityFireball extends Entity
{
    public EntityLivingBase shootingEntity;
    private int ticksAlive;
    private int ticksInAir;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;

    public EntityFireball(World p_i1759_1_)
    {
        super(p_i1759_1_);
        this.func_70105_a(1.0F, 1.0F);
    }

    protected void registerData()
    {
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getBoundingBox().getAverageEdgeLength() * 4.0D;

        if (Double.isNaN(d0))
        {
            d0 = 4.0D;
        }

        d0 = d0 * 64.0D;
        return distance < d0 * d0;
    }

    public EntityFireball(World p_i1760_1_, double p_i1760_2_, double p_i1760_4_, double p_i1760_6_, double p_i1760_8_, double p_i1760_10_, double p_i1760_12_)
    {
        super(p_i1760_1_);
        this.func_70105_a(1.0F, 1.0F);
        this.setLocationAndAngles(p_i1760_2_, p_i1760_4_, p_i1760_6_, this.rotationYaw, this.rotationPitch);
        this.setPosition(p_i1760_2_, p_i1760_4_, p_i1760_6_);
        double d0 = (double)MathHelper.sqrt(p_i1760_8_ * p_i1760_8_ + p_i1760_10_ * p_i1760_10_ + p_i1760_12_ * p_i1760_12_);
        this.accelerationX = p_i1760_8_ / d0 * 0.1D;
        this.accelerationY = p_i1760_10_ / d0 * 0.1D;
        this.accelerationZ = p_i1760_12_ / d0 * 0.1D;
    }

    public EntityFireball(World p_i1761_1_, EntityLivingBase p_i1761_2_, double p_i1761_3_, double p_i1761_5_, double p_i1761_7_)
    {
        super(p_i1761_1_);
        this.shootingEntity = p_i1761_2_;
        this.func_70105_a(1.0F, 1.0F);
        this.setLocationAndAngles(p_i1761_2_.posX, p_i1761_2_.posY, p_i1761_2_.posZ, p_i1761_2_.rotationYaw, p_i1761_2_.rotationPitch);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.field_70159_w = 0.0D;
        this.field_70181_x = 0.0D;
        this.field_70179_y = 0.0D;
        p_i1761_3_ = p_i1761_3_ + this.rand.nextGaussian() * 0.4D;
        p_i1761_5_ = p_i1761_5_ + this.rand.nextGaussian() * 0.4D;
        p_i1761_7_ = p_i1761_7_ + this.rand.nextGaussian() * 0.4D;
        double d0 = (double)MathHelper.sqrt(p_i1761_3_ * p_i1761_3_ + p_i1761_5_ * p_i1761_5_ + p_i1761_7_ * p_i1761_7_);
        this.accelerationX = p_i1761_3_ / d0 * 0.1D;
        this.accelerationY = p_i1761_5_ / d0 * 0.1D;
        this.accelerationZ = p_i1761_7_ / d0 * 0.1D;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        if (this.world.isRemote || (this.shootingEntity == null || !this.shootingEntity.removed) && this.world.isBlockLoaded(new BlockPos(this)))
        {
            super.tick();

            if (this.isFireballFiery())
            {
                this.setFire(1);
            }

            ++this.ticksInAir;
            RayTraceResult raytraceresult = ProjectileHelper.func_188802_a(this, true, this.ticksInAir >= 25, this.shootingEntity);

            if (raytraceresult != null)
            {
                this.onImpact(raytraceresult);
            }

            this.posX += this.field_70159_w;
            this.posY += this.field_70181_x;
            this.posZ += this.field_70179_y;
            ProjectileHelper.rotateTowardsMovement(this, 0.2F);
            float f = this.getMotionFactor();

            if (this.isInWater())
            {
                for (int i = 0; i < 4; ++i)
                {
                    float f1 = 0.25F;
                    this.world.func_175688_a(EnumParticleTypes.WATER_BUBBLE, this.posX - this.field_70159_w * 0.25D, this.posY - this.field_70181_x * 0.25D, this.posZ - this.field_70179_y * 0.25D, this.field_70159_w, this.field_70181_x, this.field_70179_y);
                }

                f = 0.8F;
            }

            this.field_70159_w += this.accelerationX;
            this.field_70181_x += this.accelerationY;
            this.field_70179_y += this.accelerationZ;
            this.field_70159_w *= (double)f;
            this.field_70181_x *= (double)f;
            this.field_70179_y *= (double)f;
            this.world.func_175688_a(this.func_184563_j(), this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
            this.setPosition(this.posX, this.posY, this.posZ);
        }
        else
        {
            this.remove();
        }
    }

    protected boolean isFireballFiery()
    {
        return true;
    }

    protected EnumParticleTypes func_184563_j()
    {
        return EnumParticleTypes.SMOKE_NORMAL;
    }

    /**
     * Return the motion factor for this projectile. The factor is multiplied by the original motion.
     */
    protected float getMotionFactor()
    {
        return 0.95F;
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected abstract void onImpact(RayTraceResult result);

    public static void func_189743_a(DataFixer p_189743_0_, String p_189743_1_)
    {
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        p_70014_1_.func_74782_a("direction", this.newDoubleNBTList(new double[] {this.field_70159_w, this.field_70181_x, this.field_70179_y}));
        p_70014_1_.func_74782_a("power", this.newDoubleNBTList(new double[] {this.accelerationX, this.accelerationY, this.accelerationZ}));
        p_70014_1_.putInt("life", this.ticksAlive);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        if (compound.contains("power", 9))
        {
            NBTTagList nbttaglist = compound.getList("power", 6);

            if (nbttaglist.func_74745_c() == 3)
            {
                this.accelerationX = nbttaglist.getDouble(0);
                this.accelerationY = nbttaglist.getDouble(1);
                this.accelerationZ = nbttaglist.getDouble(2);
            }
        }

        this.ticksAlive = compound.getInt("life");

        if (compound.contains("direction", 9) && compound.getList("direction", 6).func_74745_c() == 3)
        {
            NBTTagList nbttaglist1 = compound.getList("direction", 6);
            this.field_70159_w = nbttaglist1.getDouble(0);
            this.field_70181_x = nbttaglist1.getDouble(1);
            this.field_70179_y = nbttaglist1.getDouble(2);
        }
        else
        {
            this.remove();
        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return true;
    }

    public float getCollisionBorderSize()
    {
        return 1.0F;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isInvulnerableTo(source))
        {
            return false;
        }
        else
        {
            this.markVelocityChanged();

            if (source.getTrueSource() != null)
            {
                Vec3d vec3d = source.getTrueSource().getLookVec();

                if (vec3d != null)
                {
                    this.field_70159_w = vec3d.x;
                    this.field_70181_x = vec3d.y;
                    this.field_70179_y = vec3d.z;
                    this.accelerationX = this.field_70159_w * 0.1D;
                    this.accelerationY = this.field_70181_x * 0.1D;
                    this.accelerationZ = this.field_70179_y * 0.1D;
                }

                if (source.getTrueSource() instanceof EntityLivingBase)
                {
                    this.shootingEntity = (EntityLivingBase)source.getTrueSource();
                }

                return true;
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness()
    {
        return 1.0F;
    }

    public int func_70070_b()
    {
        return 15728880;
    }
}
