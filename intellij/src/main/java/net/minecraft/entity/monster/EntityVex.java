package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityVex extends EntityMob
{
    protected static final DataParameter<Byte> VEX_FLAGS = EntityDataManager.<Byte>createKey(EntityVex.class, DataSerializers.BYTE);
    private EntityLiving owner;
    @Nullable
    private BlockPos boundOrigin;
    private boolean limitedLifespan;
    private int limitedLifeTicks;

    public EntityVex(World p_i47280_1_)
    {
        super(p_i47280_1_);
        this.field_70178_ae = true;
        this.moveController = new EntityVex.AIMoveControl(this);
        this.func_70105_a(0.4F, 0.8F);
        this.experienceValue = 3;
    }

    public void func_70091_d(MoverType p_70091_1_, double p_70091_2_, double p_70091_4_, double p_70091_6_)
    {
        super.func_70091_d(p_70091_1_, p_70091_2_, p_70091_4_, p_70091_6_);
        this.doBlockCollisions();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        this.noClip = true;
        super.tick();
        this.noClip = false;
        this.setNoGravity(true);

        if (this.limitedLifespan && --this.limitedLifeTicks <= 0)
        {
            this.limitedLifeTicks = 20;
            this.attackEntityFrom(DamageSource.STARVE, 1.0F);
        }
    }

    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal(0, new EntityAISwimming(this));
        this.goalSelector.addGoal(4, new EntityVex.AIChargeAttack());
        this.goalSelector.addGoal(8, new EntityVex.AIMoveRandom());
        this.goalSelector.addGoal(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetSelector.addGoal(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityVex.class}));
        this.targetSelector.addGoal(2, new EntityVex.AICopyOwnerTarget(this));
        this.targetSelector.addGoal(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }

    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(VEX_FLAGS, Byte.valueOf((byte)0));
    }

    public static void func_190663_b(DataFixer p_190663_0_)
    {
        EntityLiving.func_189752_a(p_190663_0_, EntityVex.class);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);

        if (compound.contains("BoundX"))
        {
            this.boundOrigin = new BlockPos(compound.getInt("BoundX"), compound.getInt("BoundY"), compound.getInt("BoundZ"));
        }

        if (compound.contains("LifeTicks"))
        {
            this.setLimitedLife(compound.getInt("LifeTicks"));
        }
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);

        if (this.boundOrigin != null)
        {
            p_70014_1_.putInt("BoundX", this.boundOrigin.getX());
            p_70014_1_.putInt("BoundY", this.boundOrigin.getY());
            p_70014_1_.putInt("BoundZ", this.boundOrigin.getZ());
        }

        if (this.limitedLifespan)
        {
            p_70014_1_.putInt("LifeTicks", this.limitedLifeTicks);
        }
    }

    public EntityLiving getOwner()
    {
        return this.owner;
    }

    @Nullable
    public BlockPos getBoundOrigin()
    {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos boundOriginIn)
    {
        this.boundOrigin = boundOriginIn;
    }

    private boolean getVexFlag(int mask)
    {
        int i = ((Byte)this.dataManager.get(VEX_FLAGS)).byteValue();
        return (i & mask) != 0;
    }

    private void setVexFlag(int mask, boolean value)
    {
        int i = ((Byte)this.dataManager.get(VEX_FLAGS)).byteValue();

        if (value)
        {
            i = i | mask;
        }
        else
        {
            i = i & ~mask;
        }

        this.dataManager.set(VEX_FLAGS, Byte.valueOf((byte)(i & 255)));
    }

    public boolean isCharging()
    {
        return this.getVexFlag(1);
    }

    public void setCharging(boolean charging)
    {
        this.setVexFlag(1, charging);
    }

    public void setOwner(EntityLiving ownerIn)
    {
        this.owner = ownerIn;
    }

    public void setLimitedLife(int limitedLifeTicksIn)
    {
        this.limitedLifespan = true;
        this.limitedLifeTicks = limitedLifeTicksIn;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_VEX_AMBIENT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_VEX_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_VEX_HURT;
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.field_191188_ax;
    }

    public int func_70070_b()
    {
        return 15728880;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness()
    {
        return 1.0F;
    }

    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, @Nullable IEntityLivingData p_180482_2_)
    {
        this.setEquipmentBasedOnDifficulty(p_180482_1_);
        this.setEnchantmentBasedOnDifficulty(p_180482_1_);
        return super.func_180482_a(p_180482_1_, p_180482_2_);
    }

    /**
     * Gives armor or weapon for entity based on given DifficultyInstance
     */
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.setDropChance(EntityEquipmentSlot.MAINHAND, 0.0F);
    }

    class AIChargeAttack extends EntityAIBase
    {
        public AIChargeAttack()
        {
            this.func_75248_a(1);
        }

        public boolean shouldExecute()
        {
            if (EntityVex.this.getAttackTarget() != null && !EntityVex.this.getMoveHelper().isUpdating() && EntityVex.this.rand.nextInt(7) == 0)
            {
                return EntityVex.this.getDistanceSq(EntityVex.this.getAttackTarget()) > 4.0D;
            }
            else
            {
                return false;
            }
        }

        public boolean shouldContinueExecuting()
        {
            return EntityVex.this.getMoveHelper().isUpdating() && EntityVex.this.isCharging() && EntityVex.this.getAttackTarget() != null && EntityVex.this.getAttackTarget().isAlive();
        }

        public void startExecuting()
        {
            EntityLivingBase entitylivingbase = EntityVex.this.getAttackTarget();
            Vec3d vec3d = entitylivingbase.getEyePosition(1.0F);
            EntityVex.this.moveController.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
            EntityVex.this.setCharging(true);
            EntityVex.this.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0F, 1.0F);
        }

        public void resetTask()
        {
            EntityVex.this.setCharging(false);
        }

        public void tick()
        {
            EntityLivingBase entitylivingbase = EntityVex.this.getAttackTarget();

            if (EntityVex.this.getBoundingBox().intersects(entitylivingbase.getBoundingBox()))
            {
                EntityVex.this.attackEntityAsMob(entitylivingbase);
                EntityVex.this.setCharging(false);
            }
            else
            {
                double d0 = EntityVex.this.getDistanceSq(entitylivingbase);

                if (d0 < 9.0D)
                {
                    Vec3d vec3d = entitylivingbase.getEyePosition(1.0F);
                    EntityVex.this.moveController.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
                }
            }
        }
    }

    class AICopyOwnerTarget extends EntityAITarget
    {
        public AICopyOwnerTarget(EntityCreature creature)
        {
            super(creature, false);
        }

        public boolean shouldExecute()
        {
            return EntityVex.this.owner != null && EntityVex.this.owner.getAttackTarget() != null && this.func_75296_a(EntityVex.this.owner.getAttackTarget(), false);
        }

        public void startExecuting()
        {
            EntityVex.this.setAttackTarget(EntityVex.this.owner.getAttackTarget());
            super.startExecuting();
        }
    }

    class AIMoveControl extends EntityMoveHelper
    {
        public AIMoveControl(EntityVex vex)
        {
            super(vex);
        }

        public void tick()
        {
            if (this.action == EntityMoveHelper.Action.MOVE_TO)
            {
                double d0 = this.posX - EntityVex.this.posX;
                double d1 = this.posY - EntityVex.this.posY;
                double d2 = this.posZ - EntityVex.this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = (double)MathHelper.sqrt(d3);

                if (d3 < EntityVex.this.getBoundingBox().getAverageEdgeLength())
                {
                    this.action = EntityMoveHelper.Action.WAIT;
                    EntityVex.this.field_70159_w *= 0.5D;
                    EntityVex.this.field_70181_x *= 0.5D;
                    EntityVex.this.field_70179_y *= 0.5D;
                }
                else
                {
                    EntityVex.this.field_70159_w += d0 / d3 * 0.05D * this.speed;
                    EntityVex.this.field_70181_x += d1 / d3 * 0.05D * this.speed;
                    EntityVex.this.field_70179_y += d2 / d3 * 0.05D * this.speed;

                    if (EntityVex.this.getAttackTarget() == null)
                    {
                        EntityVex.this.rotationYaw = -((float)MathHelper.atan2(EntityVex.this.field_70159_w, EntityVex.this.field_70179_y)) * (180F / (float)Math.PI);
                        EntityVex.this.renderYawOffset = EntityVex.this.rotationYaw;
                    }
                    else
                    {
                        double d4 = EntityVex.this.getAttackTarget().posX - EntityVex.this.posX;
                        double d5 = EntityVex.this.getAttackTarget().posZ - EntityVex.this.posZ;
                        EntityVex.this.rotationYaw = -((float)MathHelper.atan2(d4, d5)) * (180F / (float)Math.PI);
                        EntityVex.this.renderYawOffset = EntityVex.this.rotationYaw;
                    }
                }
            }
        }
    }

    class AIMoveRandom extends EntityAIBase
    {
        public AIMoveRandom()
        {
            this.func_75248_a(1);
        }

        public boolean shouldExecute()
        {
            return !EntityVex.this.getMoveHelper().isUpdating() && EntityVex.this.rand.nextInt(7) == 0;
        }

        public boolean shouldContinueExecuting()
        {
            return false;
        }

        public void tick()
        {
            BlockPos blockpos = EntityVex.this.getBoundOrigin();

            if (blockpos == null)
            {
                blockpos = new BlockPos(EntityVex.this);
            }

            for (int i = 0; i < 3; ++i)
            {
                BlockPos blockpos1 = blockpos.add(EntityVex.this.rand.nextInt(15) - 7, EntityVex.this.rand.nextInt(11) - 5, EntityVex.this.rand.nextInt(15) - 7);

                if (EntityVex.this.world.isAirBlock(blockpos1))
                {
                    EntityVex.this.moveController.setMoveTo((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);

                    if (EntityVex.this.getAttackTarget() == null)
                    {
                        EntityVex.this.getLookController().setLookPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }

                    break;
                }
            }
        }
    }
}
