package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityFireworkRocket extends Entity
{
    private static final DataParameter<ItemStack> FIREWORK_ITEM = EntityDataManager.<ItemStack>createKey(EntityFireworkRocket.class, DataSerializers.ITEMSTACK);
    private static final DataParameter<Integer> BOOSTED_ENTITY_ID = EntityDataManager.<Integer>createKey(EntityFireworkRocket.class, DataSerializers.VARINT);
    private int fireworkAge;
    private int lifetime;
    private EntityLivingBase boostedEntity;

    public EntityFireworkRocket(World p_i1762_1_)
    {
        super(p_i1762_1_);
        this.func_70105_a(0.25F, 0.25F);
    }

    protected void registerData()
    {
        this.dataManager.register(FIREWORK_ITEM, ItemStack.EMPTY);
        this.dataManager.register(BOOSTED_ENTITY_ID, Integer.valueOf(0));
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean isInRangeToRenderDist(double distance)
    {
        return distance < 4096.0D && !this.isAttachedToEntity();
    }

    public boolean isInRangeToRender3d(double x, double y, double z)
    {
        return super.isInRangeToRender3d(x, y, z) && !this.isAttachedToEntity();
    }

    public EntityFireworkRocket(World worldIn, double x, double y, double z, ItemStack givenItem)
    {
        super(worldIn);
        this.fireworkAge = 0;
        this.func_70105_a(0.25F, 0.25F);
        this.setPosition(x, y, z);
        int i = 1;

        if (!givenItem.isEmpty() && givenItem.hasTag())
        {
            this.dataManager.set(FIREWORK_ITEM, givenItem.copy());
            NBTTagCompound nbttagcompound = givenItem.getTag();
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("Fireworks");
            i += nbttagcompound1.getByte("Flight");
        }

        this.field_70159_w = this.rand.nextGaussian() * 0.001D;
        this.field_70179_y = this.rand.nextGaussian() * 0.001D;
        this.field_70181_x = 0.05D;
        this.lifetime = 10 * i + this.rand.nextInt(6) + this.rand.nextInt(7);
    }

    public EntityFireworkRocket(World p_i47367_1_, ItemStack p_i47367_2_, EntityLivingBase p_i47367_3_)
    {
        this(p_i47367_1_, p_i47367_3_.posX, p_i47367_3_.posY, p_i47367_3_.posZ, p_i47367_2_);
        this.dataManager.set(BOOSTED_ENTITY_ID, Integer.valueOf(p_i47367_3_.getEntityId()));
        this.boostedEntity = p_i47367_3_;
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

        if (this.isAttachedToEntity())
        {
            if (this.boostedEntity == null)
            {
                Entity entity = this.world.getEntityByID(((Integer)this.dataManager.get(BOOSTED_ENTITY_ID)).intValue());

                if (entity instanceof EntityLivingBase)
                {
                    this.boostedEntity = (EntityLivingBase)entity;
                }
            }

            if (this.boostedEntity != null)
            {
                if (this.boostedEntity.isElytraFlying())
                {
                    Vec3d vec3d = this.boostedEntity.getLookVec();
                    double d0 = 1.5D;
                    double d1 = 0.1D;
                    this.boostedEntity.field_70159_w += vec3d.x * 0.1D + (vec3d.x * 1.5D - this.boostedEntity.field_70159_w) * 0.5D;
                    this.boostedEntity.field_70181_x += vec3d.y * 0.1D + (vec3d.y * 1.5D - this.boostedEntity.field_70181_x) * 0.5D;
                    this.boostedEntity.field_70179_y += vec3d.z * 0.1D + (vec3d.z * 1.5D - this.boostedEntity.field_70179_y) * 0.5D;
                }

                this.setPosition(this.boostedEntity.posX, this.boostedEntity.posY, this.boostedEntity.posZ);
                this.field_70159_w = this.boostedEntity.field_70159_w;
                this.field_70181_x = this.boostedEntity.field_70181_x;
                this.field_70179_y = this.boostedEntity.field_70179_y;
            }
        }
        else
        {
            this.field_70159_w *= 1.15D;
            this.field_70179_y *= 1.15D;
            this.field_70181_x += 0.04D;
            this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
        }

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

        if (this.fireworkAge == 0 && !this.isSilent())
        {
            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.AMBIENT, 3.0F, 1.0F);
        }

        ++this.fireworkAge;

        if (this.world.isRemote && this.fireworkAge % 2 < 2)
        {
            this.world.func_175688_a(EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY - 0.3D, this.posZ, this.rand.nextGaussian() * 0.05D, -this.field_70181_x * 0.5D, this.rand.nextGaussian() * 0.05D);
        }

        if (!this.world.isRemote && this.fireworkAge > this.lifetime)
        {
            this.world.setEntityState(this, (byte)17);
            this.dealExplosionDamage();
            this.remove();
        }
    }

    private void dealExplosionDamage()
    {
        float f = 0.0F;
        ItemStack itemstack = (ItemStack)this.dataManager.get(FIREWORK_ITEM);
        NBTTagCompound nbttagcompound = itemstack.isEmpty() ? null : itemstack.getChildTag("Fireworks");
        NBTTagList nbttaglist = nbttagcompound != null ? nbttagcompound.getList("Explosions", 10) : null;

        if (nbttaglist != null && !nbttaglist.func_82582_d())
        {
            f = (float)(5 + nbttaglist.func_74745_c() * 2);
        }

        if (f > 0.0F)
        {
            if (this.boostedEntity != null)
            {
                this.boostedEntity.attackEntityFrom(DamageSource.FIREWORKS, (float)(5 + nbttaglist.func_74745_c() * 2));
            }

            double d0 = 5.0D;
            Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);

            for (EntityLivingBase entitylivingbase : this.world.func_72872_a(EntityLivingBase.class, this.getBoundingBox().grow(5.0D)))
            {
                if (entitylivingbase != this.boostedEntity && this.getDistanceSq(entitylivingbase) <= 25.0D)
                {
                    boolean flag = false;

                    for (int i = 0; i < 2; ++i)
                    {
                        RayTraceResult raytraceresult = this.world.func_147447_a(vec3d, new Vec3d(entitylivingbase.posX, entitylivingbase.posY + (double)entitylivingbase.field_70131_O * 0.5D * (double)i, entitylivingbase.posZ), false, true, false);

                        if (raytraceresult == null || raytraceresult.field_72313_a == RayTraceResult.Type.MISS)
                        {
                            flag = true;
                            break;
                        }
                    }

                    if (flag)
                    {
                        float f1 = f * (float)Math.sqrt((5.0D - (double)this.getDistance(entitylivingbase)) / 5.0D);
                        entitylivingbase.attackEntityFrom(DamageSource.FIREWORKS, f1);
                    }
                }
            }
        }
    }

    public boolean isAttachedToEntity()
    {
        return ((Integer)this.dataManager.get(BOOSTED_ENTITY_ID)).intValue() > 0;
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    public void handleStatusUpdate(byte id)
    {
        if (id == 17 && this.world.isRemote)
        {
            ItemStack itemstack = (ItemStack)this.dataManager.get(FIREWORK_ITEM);
            NBTTagCompound nbttagcompound = itemstack.isEmpty() ? null : itemstack.getChildTag("Fireworks");
            this.world.makeFireworks(this.posX, this.posY, this.posZ, this.field_70159_w, this.field_70181_x, this.field_70179_y, nbttagcompound);
        }

        super.handleStatusUpdate(id);
    }

    public static void func_189656_a(DataFixer p_189656_0_)
    {
        p_189656_0_.func_188258_a(FixTypes.ENTITY, new ItemStackData(EntityFireworkRocket.class, new String[] {"FireworksItem"}));
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        p_70014_1_.putInt("Life", this.fireworkAge);
        p_70014_1_.putInt("LifeTime", this.lifetime);
        ItemStack itemstack = (ItemStack)this.dataManager.get(FIREWORK_ITEM);

        if (!itemstack.isEmpty())
        {
            p_70014_1_.func_74782_a("FireworksItem", itemstack.write(new NBTTagCompound()));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        this.fireworkAge = compound.getInt("Life");
        this.lifetime = compound.getInt("LifeTime");
        NBTTagCompound nbttagcompound = compound.getCompound("FireworksItem");

        if (nbttagcompound != null)
        {
            ItemStack itemstack = new ItemStack(nbttagcompound);

            if (!itemstack.isEmpty())
            {
                this.dataManager.set(FIREWORK_ITEM, itemstack);
            }
        }
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem()
    {
        return false;
    }
}
