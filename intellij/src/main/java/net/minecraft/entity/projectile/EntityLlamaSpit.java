package net.minecraft.entity.projectile;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityLlamaSpit extends Entity implements IProjectile
{
    public EntityLlama owner;
    private NBTTagCompound ownerNbt;

    public EntityLlamaSpit(World p_i47272_1_)
    {
        super(p_i47272_1_);
    }

    public EntityLlamaSpit(World worldIn, EntityLlama p_i47273_2_)
    {
        super(worldIn);
        this.owner = p_i47273_2_;
        this.setPosition(p_i47273_2_.posX - (double)(p_i47273_2_.field_70130_N + 1.0F) * 0.5D * (double)MathHelper.sin(p_i47273_2_.renderYawOffset * 0.017453292F), p_i47273_2_.posY + (double)p_i47273_2_.getEyeHeight() - 0.10000000149011612D, p_i47273_2_.posZ + (double)(p_i47273_2_.field_70130_N + 1.0F) * 0.5D * (double)MathHelper.cos(p_i47273_2_.renderYawOffset * 0.017453292F));
        this.func_70105_a(0.25F, 0.25F);
    }

    public EntityLlamaSpit(World worldIn, double x, double y, double z, double p_i47274_8_, double p_i47274_10_, double p_i47274_12_)
    {
        super(worldIn);
        this.setPosition(x, y, z);

        for (int i = 0; i < 7; ++i)
        {
            double d0 = 0.4D + 0.1D * (double)i;
            worldIn.func_175688_a(EnumParticleTypes.SPIT, x, y, z, p_i47274_8_ * d0, p_i47274_10_, p_i47274_12_ * d0);
        }

        this.field_70159_w = p_i47274_8_;
        this.field_70181_x = p_i47274_10_;
        this.field_70179_y = p_i47274_12_;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        super.tick();

        if (this.ownerNbt != null)
        {
            this.restoreOwnerFromSave();
        }

        Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3d1 = new Vec3d(this.posX + this.field_70159_w, this.posY + this.field_70181_x, this.posZ + this.field_70179_y);
        RayTraceResult raytraceresult = this.world.func_72933_a(vec3d, vec3d1);
        vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        vec3d1 = new Vec3d(this.posX + this.field_70159_w, this.posY + this.field_70181_x, this.posZ + this.field_70179_y);

        if (raytraceresult != null)
        {
            vec3d1 = new Vec3d(raytraceresult.hitResult.x, raytraceresult.hitResult.y, raytraceresult.hitResult.z);
        }

        Entity entity = this.func_190538_a(vec3d, vec3d1);

        if (entity != null)
        {
            raytraceresult = new RayTraceResult(entity);
        }

        if (raytraceresult != null)
        {
            this.onHit(raytraceresult);
        }

        this.posX += this.field_70159_w;
        this.posY += this.field_70181_x;
        this.posZ += this.field_70179_y;
        float f = MathHelper.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
        this.rotationYaw = (float)(MathHelper.atan2(this.field_70159_w, this.field_70179_y) * (180D / Math.PI));

        for (this.rotationPitch = (float)(MathHelper.atan2(this.field_70181_x, (double)f) * (180D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        float f1 = 0.99F;
        float f2 = 0.06F;

        if (!this.world.isMaterialInBB(this.getBoundingBox(), Material.AIR))
        {
            this.remove();
        }
        else if (this.isInWater())
        {
            this.remove();
        }
        else
        {
            this.field_70159_w *= 0.9900000095367432D;
            this.field_70181_x *= 0.9900000095367432D;
            this.field_70179_y *= 0.9900000095367432D;

            if (!this.hasNoGravity())
            {
                this.field_70181_x -= 0.05999999865889549D;
            }

            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    /**
     * Updates the entity motion clientside, called by packets from the server
     */
    public void setVelocity(double x, double y, double z)
    {
        this.field_70159_w = x;
        this.field_70181_x = y;
        this.field_70179_y = z;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(x * x + z * z);
            this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * (180D / Math.PI));
            this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }

    @Nullable
    private Entity func_190538_a(Vec3d p_190538_1_, Vec3d p_190538_2_)
    {
        Entity entity = null;
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(this.field_70159_w, this.field_70181_x, this.field_70179_y).grow(1.0D));
        double d0 = 0.0D;

        for (Entity entity1 : list)
        {
            if (entity1 != this.owner)
            {
                AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow(0.30000001192092896D);
                RayTraceResult raytraceresult = axisalignedbb.func_72327_a(p_190538_1_, p_190538_2_);

                if (raytraceresult != null)
                {
                    double d1 = p_190538_1_.squareDistanceTo(raytraceresult.hitResult);

                    if (d1 < d0 || d0 == 0.0D)
                    {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        return entity;
    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    public void shoot(double x, double y, double z, float velocity, float inaccuracy)
    {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double)f;
        y = y / (double)f;
        z = z / (double)f;
        x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
        x = x * (double)velocity;
        y = y * (double)velocity;
        z = z * (double)velocity;
        this.field_70159_w = x;
        this.field_70181_x = y;
        this.field_70179_y = z;
        float f1 = MathHelper.sqrt(x * x + z * z);
        this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
        this.rotationPitch = (float)(MathHelper.atan2(y, (double)f1) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    public void onHit(RayTraceResult p_190536_1_)
    {
        if (p_190536_1_.field_72308_g != null && this.owner != null)
        {
            p_190536_1_.field_72308_g.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.owner).setProjectile(), 1.0F);
        }

        if (!this.world.isRemote)
        {
            this.remove();
        }
    }

    protected void registerData()
    {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditional(NBTTagCompound compound)
    {
        if (compound.contains("Owner", 10))
        {
            this.ownerNbt = compound.getCompound("Owner");
        }
    }

    protected void func_70014_b(NBTTagCompound p_70014_1_)
    {
        if (this.owner != null)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            UUID uuid = this.owner.getUniqueID();
            nbttagcompound.putUniqueId("OwnerUUID", uuid);
            p_70014_1_.func_74782_a("Owner", nbttagcompound);
        }
    }

    private void restoreOwnerFromSave()
    {
        if (this.ownerNbt != null && this.ownerNbt.hasUniqueId("OwnerUUID"))
        {
            UUID uuid = this.ownerNbt.getUniqueId("OwnerUUID");

            for (EntityLlama entityllama : this.world.func_72872_a(EntityLlama.class, this.getBoundingBox().grow(15.0D)))
            {
                if (entityllama.getUniqueID().equals(uuid))
                {
                    this.owner = entityllama;
                    break;
                }
            }
        }

        this.ownerNbt = null;
    }
}
