package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityGuardian extends EntityMob
{
    private static final DataParameter<Boolean> MOVING = EntityDataManager.<Boolean>createKey(EntityGuardian.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> TARGET_ENTITY = EntityDataManager.<Integer>createKey(EntityGuardian.class, DataSerializers.VARINT);
    protected float clientSideTailAnimation;
    protected float clientSideTailAnimationO;
    protected float clientSideTailAnimationSpeed;
    protected float clientSideSpikesAnimation;
    protected float clientSideSpikesAnimationO;
    private EntityLivingBase targetedEntity;
    private int clientSideAttackTime;
    private boolean clientSideTouchedGround;
    protected EntityAIWander wander;

    public EntityGuardian(World p_i45835_1_)
    {
        super(p_i45835_1_);
        this.experienceValue = 10;
        this.func_70105_a(0.85F, 0.85F);
        this.moveController = new EntityGuardian.GuardianMoveHelper(this);
        this.clientSideTailAnimation = this.rand.nextFloat();
        this.clientSideTailAnimationO = this.clientSideTailAnimation;
    }

    protected void registerGoals()
    {
        EntityAIMoveTowardsRestriction entityaimovetowardsrestriction = new EntityAIMoveTowardsRestriction(this, 1.0D);
        this.wander = new EntityAIWander(this, 1.0D, 80);
        this.goalSelector.addGoal(4, new EntityGuardian.AIGuardianAttack(this));
        this.goalSelector.addGoal(5, entityaimovetowardsrestriction);
        this.goalSelector.addGoal(7, this.wander);
        this.goalSelector.addGoal(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.goalSelector.addGoal(8, new EntityAIWatchClosest(this, EntityGuardian.class, 12.0F, 0.01F));
        this.goalSelector.addGoal(9, new EntityAILookIdle(this));
        this.wander.func_75248_a(3);
        entityaimovetowardsrestriction.func_75248_a(3);
        this.targetSelector.addGoal(1, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 10, true, false, new EntityGuardian.GuardianTargetSelector(this)));
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
    }

    public static void func_189766_b(DataFixer p_189766_0_)
    {
        EntityLiving.func_189752_a(p_189766_0_, EntityGuardian.class);
    }

    /**
     * Returns new PathNavigateGround instance
     */
    protected PathNavigate createNavigator(World worldIn)
    {
        return new PathNavigateSwimmer(this, worldIn);
    }

    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(MOVING, Boolean.valueOf(false));
        this.dataManager.register(TARGET_ENTITY, Integer.valueOf(0));
    }

    public boolean isMoving()
    {
        return ((Boolean)this.dataManager.get(MOVING)).booleanValue();
    }

    private void setMoving(boolean moving)
    {
        this.dataManager.set(MOVING, Boolean.valueOf(moving));
    }

    public int getAttackDuration()
    {
        return 80;
    }

    private void setTargetedEntity(int entityId)
    {
        this.dataManager.set(TARGET_ENTITY, Integer.valueOf(entityId));
    }

    public boolean hasTargetedEntity()
    {
        return ((Integer)this.dataManager.get(TARGET_ENTITY)).intValue() != 0;
    }

    @Nullable
    public EntityLivingBase getTargetedEntity()
    {
        if (!this.hasTargetedEntity())
        {
            return null;
        }
        else if (this.world.isRemote)
        {
            if (this.targetedEntity != null)
            {
                return this.targetedEntity;
            }
            else
            {
                Entity entity = this.world.getEntityByID(((Integer)this.dataManager.get(TARGET_ENTITY)).intValue());

                if (entity instanceof EntityLivingBase)
                {
                    this.targetedEntity = (EntityLivingBase)entity;
                    return this.targetedEntity;
                }
                else
                {
                    return null;
                }
            }
        }
        else
        {
            return this.getAttackTarget();
        }
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        super.notifyDataManagerChange(key);

        if (TARGET_ENTITY.equals(key))
        {
            this.clientSideAttackTime = 0;
            this.targetedEntity = null;
        }
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getTalkInterval()
    {
        return 160;
    }

    protected SoundEvent getAmbientSound()
    {
        return this.isInWater() ? SoundEvents.ENTITY_GUARDIAN_AMBIENT : SoundEvents.ENTITY_GUARDIAN_AMBIENT_LAND;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return this.isInWater() ? SoundEvents.ENTITY_GUARDIAN_HURT : SoundEvents.ENTITY_GUARDIAN_HURT_LAND;
    }

    protected SoundEvent getDeathSound()
    {
        return this.isInWater() ? SoundEvents.ENTITY_GUARDIAN_DEATH : SoundEvents.ENTITY_GUARDIAN_DEATH_LAND;
    }

    protected boolean func_70041_e_()
    {
        return false;
    }

    public float getEyeHeight()
    {
        return this.field_70131_O * 0.5F;
    }

    public float getBlockPathWeight(BlockPos pos)
    {
        return this.world.getBlockState(pos).getMaterial() == Material.WATER ? 10.0F + this.world.func_175724_o(pos) - 0.5F : super.getBlockPathWeight(pos);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick()
    {
        if (this.world.isRemote)
        {
            this.clientSideTailAnimationO = this.clientSideTailAnimation;

            if (!this.isInWater())
            {
                this.clientSideTailAnimationSpeed = 2.0F;

                if (this.field_70181_x > 0.0D && this.clientSideTouchedGround && !this.isSilent())
                {
                    this.world.playSound(this.posX, this.posY, this.posZ, this.getFlopSound(), this.getSoundCategory(), 1.0F, 1.0F, false);
                }

                this.clientSideTouchedGround = this.field_70181_x < 0.0D && this.world.func_175677_d((new BlockPos(this)).down(), false);
            }
            else if (this.isMoving())
            {
                if (this.clientSideTailAnimationSpeed < 0.5F)
                {
                    this.clientSideTailAnimationSpeed = 4.0F;
                }
                else
                {
                    this.clientSideTailAnimationSpeed += (0.5F - this.clientSideTailAnimationSpeed) * 0.1F;
                }
            }
            else
            {
                this.clientSideTailAnimationSpeed += (0.125F - this.clientSideTailAnimationSpeed) * 0.2F;
            }

            this.clientSideTailAnimation += this.clientSideTailAnimationSpeed;
            this.clientSideSpikesAnimationO = this.clientSideSpikesAnimation;

            if (!this.isInWater())
            {
                this.clientSideSpikesAnimation = this.rand.nextFloat();
            }
            else if (this.isMoving())
            {
                this.clientSideSpikesAnimation += (0.0F - this.clientSideSpikesAnimation) * 0.25F;
            }
            else
            {
                this.clientSideSpikesAnimation += (1.0F - this.clientSideSpikesAnimation) * 0.06F;
            }

            if (this.isMoving() && this.isInWater())
            {
                Vec3d vec3d = this.getLook(0.0F);

                for (int i = 0; i < 2; ++i)
                {
                    this.world.func_175688_a(EnumParticleTypes.WATER_BUBBLE, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.field_70130_N - vec3d.x * 1.5D, this.posY + this.rand.nextDouble() * (double)this.field_70131_O - vec3d.y * 1.5D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.field_70130_N - vec3d.z * 1.5D, 0.0D, 0.0D, 0.0D);
                }
            }

            if (this.hasTargetedEntity())
            {
                if (this.clientSideAttackTime < this.getAttackDuration())
                {
                    ++this.clientSideAttackTime;
                }

                EntityLivingBase entitylivingbase = this.getTargetedEntity();

                if (entitylivingbase != null)
                {
                    this.getLookController().setLookPositionWithEntity(entitylivingbase, 90.0F, 90.0F);
                    this.getLookController().tick();
                    double d5 = (double)this.getAttackAnimationScale(0.0F);
                    double d0 = entitylivingbase.posX - this.posX;
                    double d1 = entitylivingbase.posY + (double)(entitylivingbase.field_70131_O * 0.5F) - (this.posY + (double)this.getEyeHeight());
                    double d2 = entitylivingbase.posZ - this.posZ;
                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    d0 = d0 / d3;
                    d1 = d1 / d3;
                    d2 = d2 / d3;
                    double d4 = this.rand.nextDouble();

                    while (d4 < d3)
                    {
                        d4 += 1.8D - d5 + this.rand.nextDouble() * (1.7D - d5);
                        this.world.func_175688_a(EnumParticleTypes.WATER_BUBBLE, this.posX + d0 * d4, this.posY + d1 * d4 + (double)this.getEyeHeight(), this.posZ + d2 * d4, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        }

        if (this.inWater)
        {
            this.setAir(300);
        }
        else if (this.onGround)
        {
            this.field_70181_x += 0.5D;
            this.field_70159_w += (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F);
            this.field_70179_y += (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F);
            this.rotationYaw = this.rand.nextFloat() * 360.0F;
            this.onGround = false;
            this.isAirBorne = true;
        }

        if (this.hasTargetedEntity())
        {
            this.rotationYaw = this.rotationYawHead;
        }

        super.livingTick();
    }

    protected SoundEvent getFlopSound()
    {
        return SoundEvents.ENTITY_GUARDIAN_FLOP;
    }

    public float getTailAnimation(float p_175471_1_)
    {
        return this.clientSideTailAnimationO + (this.clientSideTailAnimation - this.clientSideTailAnimationO) * p_175471_1_;
    }

    public float getSpikesAnimation(float p_175469_1_)
    {
        return this.clientSideSpikesAnimationO + (this.clientSideSpikesAnimation - this.clientSideSpikesAnimationO) * p_175469_1_;
    }

    public float getAttackAnimationScale(float p_175477_1_)
    {
        return ((float)this.clientSideAttackTime + p_175477_1_) / (float)this.getAttackDuration();
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.field_186440_v;
    }

    protected boolean func_70814_o()
    {
        return true;
    }

    public boolean func_70058_J()
    {
        return this.world.func_72917_a(this.getBoundingBox(), this) && this.world.func_184144_a(this, this.getBoundingBox()).isEmpty();
    }

    public boolean func_70601_bi()
    {
        return (this.rand.nextInt(20) == 0 || !this.world.canBlockSeeSky(new BlockPos(this))) && super.func_70601_bi();
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!this.isMoving() && !source.isMagicDamage() && source.getImmediateSource() instanceof EntityLivingBase)
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)source.getImmediateSource();

            if (!source.isExplosion())
            {
                entitylivingbase.attackEntityFrom(DamageSource.causeThornsDamage(this), 2.0F);
            }
        }

        if (this.wander != null)
        {
            this.wander.makeUpdate();
        }

        return super.attackEntityFrom(source, amount);
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed()
    {
        return 180;
    }

    public void func_191986_a(float p_191986_1_, float p_191986_2_, float p_191986_3_)
    {
        if (this.isServerWorld() && this.isInWater())
        {
            this.func_191958_b(p_191986_1_, p_191986_2_, p_191986_3_, 0.1F);
            this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
            this.field_70159_w *= 0.8999999761581421D;
            this.field_70181_x *= 0.8999999761581421D;
            this.field_70179_y *= 0.8999999761581421D;

            if (!this.isMoving() && this.getAttackTarget() == null)
            {
                this.field_70181_x -= 0.005D;
            }
        }
        else
        {
            super.func_191986_a(p_191986_1_, p_191986_2_, p_191986_3_);
        }
    }

    static class AIGuardianAttack extends EntityAIBase
    {
        private final EntityGuardian guardian;
        private int tickCounter;
        private final boolean isElder;

        public AIGuardianAttack(EntityGuardian guardian)
        {
            this.guardian = guardian;
            this.isElder = guardian instanceof EntityElderGuardian;
            this.func_75248_a(3);
        }

        public boolean shouldExecute()
        {
            EntityLivingBase entitylivingbase = this.guardian.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isAlive();
        }

        public boolean shouldContinueExecuting()
        {
            return super.shouldContinueExecuting() && (this.isElder || this.guardian.getDistanceSq(this.guardian.getAttackTarget()) > 9.0D);
        }

        public void startExecuting()
        {
            this.tickCounter = -10;
            this.guardian.getNavigator().clearPath();
            this.guardian.getLookController().setLookPositionWithEntity(this.guardian.getAttackTarget(), 90.0F, 90.0F);
            this.guardian.isAirBorne = true;
        }

        public void resetTask()
        {
            this.guardian.setTargetedEntity(0);
            this.guardian.setAttackTarget((EntityLivingBase)null);
            this.guardian.wander.makeUpdate();
        }

        public void tick()
        {
            EntityLivingBase entitylivingbase = this.guardian.getAttackTarget();
            this.guardian.getNavigator().clearPath();
            this.guardian.getLookController().setLookPositionWithEntity(entitylivingbase, 90.0F, 90.0F);

            if (!this.guardian.canEntityBeSeen(entitylivingbase))
            {
                this.guardian.setAttackTarget((EntityLivingBase)null);
            }
            else
            {
                ++this.tickCounter;

                if (this.tickCounter == 0)
                {
                    this.guardian.setTargetedEntity(this.guardian.getAttackTarget().getEntityId());
                    this.guardian.world.setEntityState(this.guardian, (byte)21);
                }
                else if (this.tickCounter >= this.guardian.getAttackDuration())
                {
                    float f = 1.0F;

                    if (this.guardian.world.getDifficulty() == EnumDifficulty.HARD)
                    {
                        f += 2.0F;
                    }

                    if (this.isElder)
                    {
                        f += 2.0F;
                    }

                    entitylivingbase.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this.guardian, this.guardian), f);
                    entitylivingbase.attackEntityFrom(DamageSource.causeMobDamage(this.guardian), (float)this.guardian.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue());
                    this.guardian.setAttackTarget((EntityLivingBase)null);
                }

                super.tick();
            }
        }
    }

    static class GuardianMoveHelper extends EntityMoveHelper
    {
        private final EntityGuardian entityGuardian;

        public GuardianMoveHelper(EntityGuardian guardian)
        {
            super(guardian);
            this.entityGuardian = guardian;
        }

        public void tick()
        {
            if (this.action == EntityMoveHelper.Action.MOVE_TO && !this.entityGuardian.getNavigator().noPath())
            {
                double d0 = this.posX - this.entityGuardian.posX;
                double d1 = this.posY - this.entityGuardian.posY;
                double d2 = this.posZ - this.entityGuardian.posZ;
                double d3 = (double)MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d1 = d1 / d3;
                float f = (float)(MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
                this.entityGuardian.rotationYaw = this.limitAngle(this.entityGuardian.rotationYaw, f, 90.0F);
                this.entityGuardian.renderYawOffset = this.entityGuardian.rotationYaw;
                float f1 = (float)(this.speed * this.entityGuardian.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
                this.entityGuardian.setAIMoveSpeed(this.entityGuardian.getAIMoveSpeed() + (f1 - this.entityGuardian.getAIMoveSpeed()) * 0.125F);
                double d4 = Math.sin((double)(this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.5D) * 0.05D;
                double d5 = Math.cos((double)(this.entityGuardian.rotationYaw * 0.017453292F));
                double d6 = Math.sin((double)(this.entityGuardian.rotationYaw * 0.017453292F));
                this.entityGuardian.field_70159_w += d4 * d5;
                this.entityGuardian.field_70179_y += d4 * d6;
                d4 = Math.sin((double)(this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.75D) * 0.05D;
                this.entityGuardian.field_70181_x += d4 * (d6 + d5) * 0.25D;
                this.entityGuardian.field_70181_x += (double)this.entityGuardian.getAIMoveSpeed() * d1 * 0.1D;
                EntityLookHelper entitylookhelper = this.entityGuardian.getLookController();
                double d7 = this.entityGuardian.posX + d0 / d3 * 2.0D;
                double d8 = (double)this.entityGuardian.getEyeHeight() + this.entityGuardian.posY + d1 / d3;
                double d9 = this.entityGuardian.posZ + d2 / d3 * 2.0D;
                double d10 = entitylookhelper.getLookPosX();
                double d11 = entitylookhelper.getLookPosY();
                double d12 = entitylookhelper.getLookPosZ();

                if (!entitylookhelper.getIsLooking())
                {
                    d10 = d7;
                    d11 = d8;
                    d12 = d9;
                }

                this.entityGuardian.getLookController().setLookPosition(d10 + (d7 - d10) * 0.125D, d11 + (d8 - d11) * 0.125D, d12 + (d9 - d12) * 0.125D, 10.0F, 40.0F);
                this.entityGuardian.setMoving(true);
            }
            else
            {
                this.entityGuardian.setAIMoveSpeed(0.0F);
                this.entityGuardian.setMoving(false);
            }
        }
    }

    static class GuardianTargetSelector implements Predicate<EntityLivingBase>
    {
        private final EntityGuardian parentEntity;

        public GuardianTargetSelector(EntityGuardian guardian)
        {
            this.parentEntity = guardian;
        }

        public boolean apply(@Nullable EntityLivingBase p_apply_1_)
        {
            return (p_apply_1_ instanceof EntityPlayer || p_apply_1_ instanceof EntitySquid) && p_apply_1_.getDistanceSq(this.parentEntity) > 9.0D;
        }
    }
}
