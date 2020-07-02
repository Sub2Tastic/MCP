package net.minecraft.entity.projectile;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityArrow extends Entity implements IProjectile
{
    private static final Predicate<Entity> field_184553_f = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>()
    {
        public boolean apply(@Nullable Entity p_apply_1_)
        {
            return p_apply_1_.canBeCollidedWith();
        }
    });
    private static final DataParameter<Byte> CRITICAL = EntityDataManager.<Byte>createKey(EntityArrow.class, DataSerializers.BYTE);
    private int field_145791_d;
    private int field_145792_e;
    private int field_145789_f;
    private Block field_145790_g;
    private int field_70253_h;
    protected boolean inGround;
    protected int timeInGround;
    public EntityArrow.PickupStatus pickupStatus;
    public int arrowShake;
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private double damage;
    private int knockbackStrength;

    public EntityArrow(World p_i1753_1_)
    {
        super(p_i1753_1_);
        this.field_145791_d = -1;
        this.field_145792_e = -1;
        this.field_145789_f = -1;
        this.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
        this.damage = 2.0D;
        this.func_70105_a(0.5F, 0.5F);
    }

    public EntityArrow(World p_i1754_1_, double p_i1754_2_, double p_i1754_4_, double p_i1754_6_)
    {
        this(p_i1754_1_);
        this.setPosition(p_i1754_2_, p_i1754_4_, p_i1754_6_);
    }

    public EntityArrow(World p_i46777_1_, EntityLivingBase p_i46777_2_)
    {
        this(p_i46777_1_, p_i46777_2_.posX, p_i46777_2_.posY + (double)p_i46777_2_.getEyeHeight() - 0.10000000149011612D, p_i46777_2_.posZ);
        this.shootingEntity = p_i46777_2_;

        if (p_i46777_2_ instanceof EntityPlayer)
        {
            this.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
        }
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getBoundingBox().getAverageEdgeLength() * 10.0D;

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * getRenderDistanceWeight();
        return distance < d0 * d0;
    }

    protected void registerData()
    {
        this.dataManager.register(CRITICAL, Byte.valueOf((byte)0));
    }

    public void shoot(Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy)
    {
        float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        float f1 = -MathHelper.sin(pitch * 0.017453292F);
        float f2 = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        this.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
        this.field_70159_w += shooter.field_70159_w;
        this.field_70179_y += shooter.field_70179_y;

        if (!shooter.onGround)
        {
            this.field_70181_x += shooter.field_70181_x;
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
        this.ticksInGround = 0;
    }

    /**
     * Sets a target for the client to interpolate towards over the next few ticks
     */
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
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
            this.ticksInGround = 0;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        super.tick();

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
            this.rotationYaw = (float)(MathHelper.atan2(this.field_70159_w, this.field_70179_y) * (180D / Math.PI));
            this.rotationPitch = (float)(MathHelper.atan2(this.field_70181_x, (double)f) * (180D / Math.PI));
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }

        BlockPos blockpos = new BlockPos(this.field_145791_d, this.field_145792_e, this.field_145789_f);
        IBlockState iblockstate = this.world.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (iblockstate.getMaterial() != Material.AIR)
        {
            AxisAlignedBB axisalignedbb = iblockstate.func_185890_d(this.world, blockpos);

            if (axisalignedbb != Block.field_185506_k && axisalignedbb.offset(blockpos).contains(new Vec3d(this.posX, this.posY, this.posZ)))
            {
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0)
        {
            --this.arrowShake;
        }

        if (this.inGround)
        {
            int j = block.func_176201_c(iblockstate);

            if ((block != this.field_145790_g || j != this.field_70253_h) && !this.world.func_184143_b(this.getBoundingBox().grow(0.05D)))
            {
                this.inGround = false;
                this.field_70159_w *= (double)(this.rand.nextFloat() * 0.2F);
                this.field_70181_x *= (double)(this.rand.nextFloat() * 0.2F);
                this.field_70179_y *= (double)(this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
            else
            {
                ++this.ticksInGround;

                if (this.ticksInGround >= 1200)
                {
                    this.remove();
                }
            }

            ++this.timeInGround;
        }
        else
        {
            this.timeInGround = 0;
            ++this.ticksInAir;
            Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vec3d = new Vec3d(this.posX + this.field_70159_w, this.posY + this.field_70181_x, this.posZ + this.field_70179_y);
            RayTraceResult raytraceresult = this.world.func_147447_a(vec3d1, vec3d, false, true, false);
            vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
            vec3d = new Vec3d(this.posX + this.field_70159_w, this.posY + this.field_70181_x, this.posZ + this.field_70179_y);

            if (raytraceresult != null)
            {
                vec3d = new Vec3d(raytraceresult.hitResult.x, raytraceresult.hitResult.y, raytraceresult.hitResult.z);
            }

            Entity entity = this.func_184551_a(vec3d1, vec3d);

            if (entity != null)
            {
                raytraceresult = new RayTraceResult(entity);
            }

            if (raytraceresult != null && raytraceresult.field_72308_g instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)raytraceresult.field_72308_g;

                if (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer))
                {
                    raytraceresult = null;
                }
            }

            if (raytraceresult != null)
            {
                this.onHit(raytraceresult);
            }

            if (this.getIsCritical())
            {
                for (int k = 0; k < 4; ++k)
                {
                    this.world.func_175688_a(EnumParticleTypes.CRIT, this.posX + this.field_70159_w * (double)k / 4.0D, this.posY + this.field_70181_x * (double)k / 4.0D, this.posZ + this.field_70179_y * (double)k / 4.0D, -this.field_70159_w, -this.field_70181_x + 0.2D, -this.field_70179_y);
                }
            }

            this.posX += this.field_70159_w;
            this.posY += this.field_70181_x;
            this.posZ += this.field_70179_y;
            float f4 = MathHelper.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
            this.rotationYaw = (float)(MathHelper.atan2(this.field_70159_w, this.field_70179_y) * (180D / Math.PI));

            for (this.rotationPitch = (float)(MathHelper.atan2(this.field_70181_x, (double)f4) * (180D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
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
            float f2 = 0.05F;

            if (this.isInWater())
            {
                for (int i = 0; i < 4; ++i)
                {
                    float f3 = 0.25F;
                    this.world.func_175688_a(EnumParticleTypes.WATER_BUBBLE, this.posX - this.field_70159_w * 0.25D, this.posY - this.field_70181_x * 0.25D, this.posZ - this.field_70179_y * 0.25D, this.field_70159_w, this.field_70181_x, this.field_70179_y);
                }

                f1 = 0.6F;
            }

            if (this.isWet())
            {
                this.extinguish();
            }

            this.field_70159_w *= (double)f1;
            this.field_70181_x *= (double)f1;
            this.field_70179_y *= (double)f1;

            if (!this.hasNoGravity())
            {
                this.field_70181_x -= 0.05000000074505806D;
            }

            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }
    }

    /**
     * Called when the arrow hits a block or an entity
     */
    protected void onHit(RayTraceResult raytraceResultIn)
    {
        Entity entity = raytraceResultIn.field_72308_g;

        if (entity != null)
        {
            float f = MathHelper.sqrt(this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y);
            int i = MathHelper.ceil((double)f * this.damage);

            if (this.getIsCritical())
            {
                i += this.rand.nextInt(i / 2 + 2);
            }

            DamageSource damagesource;

            if (this.shootingEntity == null)
            {
                damagesource = DamageSource.causeArrowDamage(this, this);
            }
            else
            {
                damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
            }

            if (this.isBurning() && !(entity instanceof EntityEnderman))
            {
                entity.setFire(5);
            }

            if (entity.attackEntityFrom(damagesource, (float)i))
            {
                if (entity instanceof EntityLivingBase)
                {
                    EntityLivingBase entitylivingbase = (EntityLivingBase)entity;

                    if (!this.world.isRemote)
                    {
                        entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                    }

                    if (this.knockbackStrength > 0)
                    {
                        float f1 = MathHelper.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);

                        if (f1 > 0.0F)
                        {
                            entitylivingbase.addVelocity(this.field_70159_w * (double)this.knockbackStrength * 0.6000000238418579D / (double)f1, 0.1D, this.field_70179_y * (double)this.knockbackStrength * 0.6000000238418579D / (double)f1);
                        }
                    }

                    if (this.shootingEntity instanceof EntityLivingBase)
                    {
                        EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
                        EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)this.shootingEntity, entitylivingbase);
                    }

                    this.arrowHit(entitylivingbase);

                    if (this.shootingEntity != null && entitylivingbase != this.shootingEntity && entitylivingbase instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP)
                    {
                        ((EntityPlayerMP)this.shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
                    }
                }

                this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

                if (!(entity instanceof EntityEnderman))
                {
                    this.remove();
                }
            }
            else
            {
                this.field_70159_w *= -0.10000000149011612D;
                this.field_70181_x *= -0.10000000149011612D;
                this.field_70179_y *= -0.10000000149011612D;
                this.rotationYaw += 180.0F;
                this.prevRotationYaw += 180.0F;
                this.ticksInAir = 0;

                if (!this.world.isRemote && this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y < 0.0010000000474974513D)
                {
                    if (this.pickupStatus == EntityArrow.PickupStatus.ALLOWED)
                    {
                        this.entityDropItem(this.getArrowStack(), 0.1F);
                    }

                    this.remove();
                }
            }
        }
        else
        {
            BlockPos blockpos = raytraceResultIn.func_178782_a();
            this.field_145791_d = blockpos.getX();
            this.field_145792_e = blockpos.getY();
            this.field_145789_f = blockpos.getZ();
            IBlockState iblockstate = this.world.getBlockState(blockpos);
            this.field_145790_g = iblockstate.getBlock();
            this.field_70253_h = this.field_145790_g.func_176201_c(iblockstate);
            this.field_70159_w = (double)((float)(raytraceResultIn.hitResult.x - this.posX));
            this.field_70181_x = (double)((float)(raytraceResultIn.hitResult.y - this.posY));
            this.field_70179_y = (double)((float)(raytraceResultIn.hitResult.z - this.posZ));
            float f2 = MathHelper.sqrt(this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y);
            this.posX -= this.field_70159_w / (double)f2 * 0.05000000074505806D;
            this.posY -= this.field_70181_x / (double)f2 * 0.05000000074505806D;
            this.posZ -= this.field_70179_y / (double)f2 * 0.05000000074505806D;
            this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
            this.inGround = true;
            this.arrowShake = 7;
            this.setIsCritical(false);

            if (iblockstate.getMaterial() != Material.AIR)
            {
                this.field_145790_g.func_180634_a(this.world, blockpos, iblockstate, this);
            }
        }
    }

    public void func_70091_d(MoverType p_70091_1_, double p_70091_2_, double p_70091_4_, double p_70091_6_)
    {
        super.func_70091_d(p_70091_1_, p_70091_2_, p_70091_4_, p_70091_6_);

        if (this.inGround)
        {
            this.field_145791_d = MathHelper.floor(this.posX);
            this.field_145792_e = MathHelper.floor(this.posY);
            this.field_145789_f = MathHelper.floor(this.posZ);
        }
    }

    protected void arrowHit(EntityLivingBase living)
    {
    }

    @Nullable
    protected Entity func_184551_a(Vec3d p_184551_1_, Vec3d p_184551_2_)
    {
        Entity entity = null;
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox().expand(this.field_70159_w, this.field_70181_x, this.field_70179_y).grow(1.0D), field_184553_f);
        double d0 = 0.0D;

        for (int i = 0; i < list.size(); ++i)
        {
            Entity entity1 = list.get(i);

            if (entity1 != this.shootingEntity || this.ticksInAir >= 5)
            {
                AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow(0.30000001192092896D);
                RayTraceResult raytraceresult = axisalignedbb.func_72327_a(p_184551_1_, p_184551_2_);

                if (raytraceresult != null)
                {
                    double d1 = p_184551_1_.squareDistanceTo(raytraceresult.hitResult);

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

    public static void func_189657_a(DataFixer p_189657_0_, String p_189657_1_)
    {
    }

    public static void func_189658_a(DataFixer p_189658_0_)
    {
        func_189657_a(p_189658_0_, "Arrow");
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        p_70014_1_.putInt("xTile", this.field_145791_d);
        p_70014_1_.putInt("yTile", this.field_145792_e);
        p_70014_1_.putInt("zTile", this.field_145789_f);
        p_70014_1_.putShort("life", (short)this.ticksInGround);
        ResourceLocation resourcelocation = Block.field_149771_c.getKey(this.field_145790_g);
        p_70014_1_.putString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
        p_70014_1_.putByte("inData", (byte)this.field_70253_h);
        p_70014_1_.putByte("shake", (byte)this.arrowShake);
        p_70014_1_.putByte("inGround", (byte)(this.inGround ? 1 : 0));
        p_70014_1_.putByte("pickup", (byte)this.pickupStatus.ordinal());
        p_70014_1_.putDouble("damage", this.damage);
        p_70014_1_.putBoolean("crit", this.getIsCritical());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        this.field_145791_d = compound.getInt("xTile");
        this.field_145792_e = compound.getInt("yTile");
        this.field_145789_f = compound.getInt("zTile");
        this.ticksInGround = compound.getShort("life");

        if (compound.contains("inTile", 8))
        {
            this.field_145790_g = Block.func_149684_b(compound.getString("inTile"));
        }
        else
        {
            this.field_145790_g = Block.func_149729_e(compound.getByte("inTile") & 255);
        }

        this.field_70253_h = compound.getByte("inData") & 255;
        this.arrowShake = compound.getByte("shake") & 255;
        this.inGround = compound.getByte("inGround") == 1;

        if (compound.contains("damage", 99))
        {
            this.damage = compound.getDouble("damage");
        }

        if (compound.contains("pickup", 99))
        {
            this.pickupStatus = EntityArrow.PickupStatus.getByOrdinal(compound.getByte("pickup"));
        }
        else if (compound.contains("player", 99))
        {
            this.pickupStatus = compound.getBoolean("player") ? EntityArrow.PickupStatus.ALLOWED : EntityArrow.PickupStatus.DISALLOWED;
        }

        this.setIsCritical(compound.getBoolean("crit"));
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityIn)
    {
        if (!this.world.isRemote && this.inGround && this.arrowShake <= 0)
        {
            boolean flag = this.pickupStatus == EntityArrow.PickupStatus.ALLOWED || this.pickupStatus == EntityArrow.PickupStatus.CREATIVE_ONLY && entityIn.abilities.isCreativeMode;

            if (this.pickupStatus == EntityArrow.PickupStatus.ALLOWED && !entityIn.inventory.addItemStackToInventory(this.getArrowStack()))
            {
                flag = false;
            }

            if (flag)
            {
                entityIn.onItemPickup(this, 1);
                this.remove();
            }
        }
    }

    protected abstract ItemStack getArrowStack();

    protected boolean func_70041_e_()
    {
        return false;
    }

    public void setDamage(double damageIn)
    {
        this.damage = damageIn;
    }

    public double getDamage()
    {
        return this.damage;
    }

    /**
     * Sets the amount of knockback the arrow applies when it hits a mob.
     */
    public void setKnockbackStrength(int knockbackStrengthIn)
    {
        this.knockbackStrength = knockbackStrengthIn;
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem()
    {
        return false;
    }

    public float getEyeHeight()
    {
        return 0.0F;
    }

    /**
     * Whether the arrow has a stream of critical hit particles flying behind it.
     */
    public void setIsCritical(boolean critical)
    {
        byte b0 = ((Byte)this.dataManager.get(CRITICAL)).byteValue();

        if (critical)
        {
            this.dataManager.set(CRITICAL, Byte.valueOf((byte)(b0 | 1)));
        }
        else
        {
            this.dataManager.set(CRITICAL, Byte.valueOf((byte)(b0 & -2)));
        }
    }

    /**
     * Whether the arrow has a stream of critical hit particles flying behind it.
     */
    public boolean getIsCritical()
    {
        byte b0 = ((Byte)this.dataManager.get(CRITICAL)).byteValue();
        return (b0 & 1) != 0;
    }

    public void setEnchantmentEffectsFromEntity(EntityLivingBase p_190547_1_, float p_190547_2_)
    {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, p_190547_1_);
        int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, p_190547_1_);
        this.setDamage((double)(p_190547_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.world.getDifficulty().getId() * 0.11F));

        if (i > 0)
        {
            this.setDamage(this.getDamage() + (double)i * 0.5D + 0.5D);
        }

        if (j > 0)
        {
            this.setKnockbackStrength(j);
        }

        if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, p_190547_1_) > 0)
        {
            this.setFire(100);
        }
    }

    public static enum PickupStatus
    {
        DISALLOWED,
        ALLOWED,
        CREATIVE_ONLY;

        public static EntityArrow.PickupStatus getByOrdinal(int ordinal)
        {
            if (ordinal < 0 || ordinal > values().length)
            {
                ordinal = 0;
            }

            return values()[ordinal];
        }
    }
}
