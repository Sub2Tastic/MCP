package net.minecraft.entity.projectile;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityThrowable extends Entity implements IProjectile
{
    private int xTile;
    private int yTile;
    private int zTile;
    private Block field_174853_f;
    protected boolean inGround;
    public int throwableShake;
    protected EntityLivingBase owner;
    private String field_85053_h;
    private int field_70194_h;
    private int field_70195_i;
    public Entity ignoreEntity;
    private int ignoreTime;

    public EntityThrowable(World p_i1776_1_)
    {
        super(p_i1776_1_);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.func_70105_a(0.25F, 0.25F);
    }

    public EntityThrowable(World p_i1778_1_, double p_i1778_2_, double p_i1778_4_, double p_i1778_6_)
    {
        this(p_i1778_1_);
        this.setPosition(p_i1778_2_, p_i1778_4_, p_i1778_6_);
    }

    public EntityThrowable(World p_i1777_1_, EntityLivingBase p_i1777_2_)
    {
        this(p_i1777_1_, p_i1777_2_.posX, p_i1777_2_.posY + (double)p_i1777_2_.getEyeHeight() - 0.10000000149011612D, p_i1777_2_.posZ);
        this.owner = p_i1777_2_;
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

    /**
     * Sets throwable heading based on an entity that's throwing it
     */
    public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy)
    {
        float f = -MathHelper.sin(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
        float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
        float f2 = MathHelper.cos(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
        this.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
        this.field_70159_w += entityThrower.field_70159_w;
        this.field_70179_y += entityThrower.field_70179_y;

        if (!entityThrower.onGround)
        {
            this.field_70181_x += entityThrower.field_70181_x;
        }
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
        this.field_70194_h = 0;
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
            this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
            this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * (180D / Math.PI));
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.tick();

        if (this.throwableShake > 0)
        {
            --this.throwableShake;
        }

        if (this.inGround)
        {
            if (this.world.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.field_174853_f)
            {
                ++this.field_70194_h;

                if (this.field_70194_h == 1200)
                {
                    this.remove();
                }

                return;
            }

            this.inGround = false;
            this.field_70159_w *= (double)(this.rand.nextFloat() * 0.2F);
            this.field_70181_x *= (double)(this.rand.nextFloat() * 0.2F);
            this.field_70179_y *= (double)(this.rand.nextFloat() * 0.2F);
            this.field_70194_h = 0;
            this.field_70195_i = 0;
        }
        else
        {
            ++this.field_70195_i;
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

        Entity entity = null;
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(this.field_70159_w, this.field_70181_x, this.field_70179_y).grow(1.0D));
        double d0 = 0.0D;
        boolean flag = false;

        for (int i = 0; i < list.size(); ++i)
        {
            Entity entity1 = list.get(i);

            if (entity1.canBeCollidedWith())
            {
                if (entity1 == this.ignoreEntity)
                {
                    flag = true;
                }
                else if (this.owner != null && this.ticksExisted < 2 && this.ignoreEntity == null)
                {
                    this.ignoreEntity = entity1;
                    flag = true;
                }
                else
                {
                    flag = false;
                    AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow(0.30000001192092896D);
                    RayTraceResult raytraceresult1 = axisalignedbb.func_72327_a(vec3d, vec3d1);

                    if (raytraceresult1 != null)
                    {
                        double d1 = vec3d.squareDistanceTo(raytraceresult1.hitResult);

                        if (d1 < d0 || d0 == 0.0D)
                        {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }
        }

        if (this.ignoreEntity != null)
        {
            if (flag)
            {
                this.ignoreTime = 2;
            }
            else if (this.ignoreTime-- <= 0)
            {
                this.ignoreEntity = null;
            }
        }

        if (entity != null)
        {
            raytraceresult = new RayTraceResult(entity);
        }

        if (raytraceresult != null)
        {
            if (raytraceresult.field_72313_a == RayTraceResult.Type.BLOCK && this.world.getBlockState(raytraceresult.func_178782_a()).getBlock() == Blocks.NETHER_PORTAL)
            {
                this.setPortal(raytraceresult.func_178782_a());
            }
            else
            {
                this.onImpact(raytraceresult);
            }
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
        float f2 = this.getGravityVelocity();

        if (this.isInWater())
        {
            for (int j = 0; j < 4; ++j)
            {
                float f3 = 0.25F;
                this.world.func_175688_a(EnumParticleTypes.WATER_BUBBLE, this.posX - this.field_70159_w * 0.25D, this.posY - this.field_70181_x * 0.25D, this.posZ - this.field_70179_y * 0.25D, this.field_70159_w, this.field_70181_x, this.field_70179_y);
            }

            f1 = 0.8F;
        }

        this.field_70159_w *= (double)f1;
        this.field_70181_x *= (double)f1;
        this.field_70179_y *= (double)f1;

        if (!this.hasNoGravity())
        {
            this.field_70181_x -= (double)f2;
        }

        this.setPosition(this.posX, this.posY, this.posZ);
    }

    /**
     * Gets the amount of gravity to apply to the thrown entity with each tick.
     */
    protected float getGravityVelocity()
    {
        return 0.03F;
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected abstract void onImpact(RayTraceResult result);

    public static void func_189661_a(DataFixer p_189661_0_, String p_189661_1_)
    {
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        p_70014_1_.putInt("xTile", this.xTile);
        p_70014_1_.putInt("yTile", this.yTile);
        p_70014_1_.putInt("zTile", this.zTile);
        ResourceLocation resourcelocation = Block.field_149771_c.getKey(this.field_174853_f);
        p_70014_1_.putString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
        p_70014_1_.putByte("shake", (byte)this.throwableShake);
        p_70014_1_.putByte("inGround", (byte)(this.inGround ? 1 : 0));

        if ((this.field_85053_h == null || this.field_85053_h.isEmpty()) && this.owner instanceof EntityPlayer)
        {
            this.field_85053_h = this.owner.func_70005_c_();
        }

        p_70014_1_.putString("ownerName", this.field_85053_h == null ? "" : this.field_85053_h);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        this.xTile = compound.getInt("xTile");
        this.yTile = compound.getInt("yTile");
        this.zTile = compound.getInt("zTile");

        if (compound.contains("inTile", 8))
        {
            this.field_174853_f = Block.func_149684_b(compound.getString("inTile"));
        }
        else
        {
            this.field_174853_f = Block.func_149729_e(compound.getByte("inTile") & 255);
        }

        this.throwableShake = compound.getByte("shake") & 255;
        this.inGround = compound.getByte("inGround") == 1;
        this.owner = null;
        this.field_85053_h = compound.getString("ownerName");

        if (this.field_85053_h != null && this.field_85053_h.isEmpty())
        {
            this.field_85053_h = null;
        }

        this.owner = this.getThrower();
    }

    @Nullable
    public EntityLivingBase getThrower()
    {
        if (this.owner == null && this.field_85053_h != null && !this.field_85053_h.isEmpty())
        {
            this.owner = this.world.func_72924_a(this.field_85053_h);

            if (this.owner == null && this.world instanceof WorldServer)
            {
                try
                {
                    Entity entity = ((WorldServer)this.world).func_175733_a(UUID.fromString(this.field_85053_h));

                    if (entity instanceof EntityLivingBase)
                    {
                        this.owner = (EntityLivingBase)entity;
                    }
                }
                catch (Throwable var2)
                {
                    this.owner = null;
                }
            }
        }

        return this.owner;
    }
}
