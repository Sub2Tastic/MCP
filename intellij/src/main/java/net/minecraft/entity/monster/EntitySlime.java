package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySlime extends EntityLiving implements IMob
{
    private static final DataParameter<Integer> SLIME_SIZE = EntityDataManager.<Integer>createKey(EntitySlime.class, DataSerializers.VARINT);
    public float squishAmount;
    public float squishFactor;
    public float prevSquishFactor;
    private boolean wasOnGround;

    public EntitySlime(World p_i1742_1_)
    {
        super(p_i1742_1_);
        this.moveController = new EntitySlime.SlimeMoveHelper(this);
    }

    protected void registerGoals()
    {
        this.goalSelector.addGoal(1, new EntitySlime.AISlimeFloat(this));
        this.goalSelector.addGoal(2, new EntitySlime.AISlimeAttack(this));
        this.goalSelector.addGoal(3, new EntitySlime.AISlimeFaceRandom(this));
        this.goalSelector.addGoal(5, new EntitySlime.AISlimeHop(this));
        this.targetSelector.addGoal(1, new EntityAIFindEntityNearestPlayer(this));
        this.targetSelector.addGoal(3, new EntityAIFindEntityNearest(this, EntityIronGolem.class));
    }

    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(SLIME_SIZE, Integer.valueOf(1));
    }

    protected void setSlimeSize(int size, boolean resetHealth)
    {
        this.dataManager.set(SLIME_SIZE, Integer.valueOf(size));
        this.func_70105_a(0.51000005F * (float)size, 0.51000005F * (float)size);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)(size * size));
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)(0.2F + 0.1F * (float)size));

        if (resetHealth)
        {
            this.setHealth(this.getMaxHealth());
        }

        this.experienceValue = size;
    }

    /**
     * Returns the size of the slime.
     */
    public int getSlimeSize()
    {
        return ((Integer)this.dataManager.get(SLIME_SIZE)).intValue();
    }

    public static void func_189758_c(DataFixer p_189758_0_)
    {
        EntityLiving.func_189752_a(p_189758_0_, EntitySlime.class);
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);
        p_70014_1_.putInt("Size", this.getSlimeSize() - 1);
        p_70014_1_.putBoolean("wasOnGround", this.wasOnGround);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);
        int i = compound.getInt("Size");

        if (i < 0)
        {
            i = 0;
        }

        this.setSlimeSize(i + 1, false);
        this.wasOnGround = compound.getBoolean("wasOnGround");
    }

    public boolean isSmallSlime()
    {
        return this.getSlimeSize() <= 1;
    }

    protected EnumParticleTypes func_180487_n()
    {
        return EnumParticleTypes.SLIME;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL && this.getSlimeSize() > 0)
        {
            this.removed = true;
        }

        this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5F;
        this.prevSquishFactor = this.squishFactor;
        super.tick();

        if (this.onGround && !this.wasOnGround)
        {
            int i = this.getSlimeSize();

            for (int j = 0; j < i * 8; ++j)
            {
                float f = this.rand.nextFloat() * ((float)Math.PI * 2F);
                float f1 = this.rand.nextFloat() * 0.5F + 0.5F;
                float f2 = MathHelper.sin(f) * (float)i * 0.5F * f1;
                float f3 = MathHelper.cos(f) * (float)i * 0.5F * f1;
                World world = this.world;
                EnumParticleTypes enumparticletypes = this.func_180487_n();
                double d0 = this.posX + (double)f2;
                double d1 = this.posZ + (double)f3;
                world.func_175688_a(enumparticletypes, d0, this.getBoundingBox().minY, d1, 0.0D, 0.0D, 0.0D);
            }

            this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            this.squishAmount = -0.5F;
        }
        else if (!this.onGround && this.wasOnGround)
        {
            this.squishAmount = 1.0F;
        }

        this.wasOnGround = this.onGround;
        this.alterSquishAmount();
    }

    protected void alterSquishAmount()
    {
        this.squishAmount *= 0.6F;
    }

    /**
     * Gets the amount of time the slime needs to wait between jumps.
     */
    protected int getJumpDelay()
    {
        return this.rand.nextInt(20) + 10;
    }

    protected EntitySlime func_70802_j()
    {
        return new EntitySlime(this.world);
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (SLIME_SIZE.equals(key))
        {
            int i = this.getSlimeSize();
            this.func_70105_a(0.51000005F * (float)i, 0.51000005F * (float)i);
            this.rotationYaw = this.rotationYawHead;
            this.renderYawOffset = this.rotationYawHead;

            if (this.isInWater() && this.rand.nextInt(20) == 0)
            {
                this.doWaterSplashEffect();
            }
        }

        super.notifyDataManagerChange(key);
    }

    /**
     * Queues the entity for removal from the world on the next tick.
     */
    public void remove()
    {
        int i = this.getSlimeSize();

        if (!this.world.isRemote && i > 1 && this.getHealth() <= 0.0F)
        {
            int j = 2 + this.rand.nextInt(3);

            for (int k = 0; k < j; ++k)
            {
                float f = ((float)(k % 2) - 0.5F) * (float)i / 4.0F;
                float f1 = ((float)(k / 2) - 0.5F) * (float)i / 4.0F;
                EntitySlime entityslime = this.func_70802_j();

                if (this.hasCustomName())
                {
                    entityslime.func_96094_a(this.func_95999_t());
                }

                if (this.isNoDespawnRequired())
                {
                    entityslime.enablePersistence();
                }

                entityslime.setSlimeSize(i / 2, true);
                entityslime.setLocationAndAngles(this.posX + (double)f, this.posY + 0.5D, this.posZ + (double)f1, this.rand.nextFloat() * 360.0F, 0.0F);
                this.world.addEntity0(entityslime);
            }
        }

        super.remove();
    }

    /**
     * Applies a velocity to the entities, to push them away from eachother.
     */
    public void applyEntityCollision(Entity entityIn)
    {
        super.applyEntityCollision(entityIn);

        if (entityIn instanceof EntityIronGolem && this.canDamagePlayer())
        {
            this.dealDamage((EntityLivingBase)entityIn);
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityIn)
    {
        if (this.canDamagePlayer())
        {
            this.dealDamage(entityIn);
        }
    }

    protected void dealDamage(EntityLivingBase entityIn)
    {
        int i = this.getSlimeSize();

        if (this.canEntityBeSeen(entityIn) && this.getDistanceSq(entityIn) < 0.6D * (double)i * 0.6D * (double)i && entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)this.func_70805_n()))
        {
            this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.applyEnchantments(this, entityIn);
        }
    }

    public float getEyeHeight()
    {
        return 0.625F * this.field_70131_O;
    }

    /**
     * Indicates weather the slime is able to damage the player (based upon the slime's size)
     */
    protected boolean canDamagePlayer()
    {
        return !this.isSmallSlime();
    }

    protected int func_70805_n()
    {
        return this.getSlimeSize();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SLIME_HURT_SMALL : SoundEvents.ENTITY_SLIME_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SLIME_DEATH_SMALL : SoundEvents.ENTITY_SLIME_DEATH;
    }

    protected SoundEvent getSquishSound()
    {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SLIME_SQUISH_SMALL : SoundEvents.ENTITY_SLIME_SQUISH;
    }

    protected Item func_146068_u()
    {
        return this.getSlimeSize() == 1 ? Items.SLIME_BALL : null;
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return this.getSlimeSize() == 1 ? LootTableList.field_186378_ac : LootTableList.EMPTY;
    }

    public boolean func_70601_bi()
    {
        BlockPos blockpos = new BlockPos(MathHelper.floor(this.posX), 0, MathHelper.floor(this.posZ));
        Chunk chunk = this.world.getChunkAt(blockpos);

        if (this.world.getWorldInfo().getGenerator() == WorldType.FLAT && this.rand.nextInt(4) != 1)
        {
            return false;
        }
        else
        {
            if (this.world.getDifficulty() != EnumDifficulty.PEACEFUL)
            {
                Biome biome = this.world.func_180494_b(blockpos);

                if (biome == Biomes.SWAMP && this.posY > 50.0D && this.posY < 70.0D && this.rand.nextFloat() < 0.5F && this.rand.nextFloat() < this.world.getCurrentMoonPhaseFactor() && this.world.func_175671_l(new BlockPos(this)) <= this.rand.nextInt(8))
                {
                    return super.func_70601_bi();
                }

                if (this.rand.nextInt(10) == 0 && chunk.func_76617_a(987234911L).nextInt(10) == 0 && this.posY < 40.0D)
                {
                    return super.func_70601_bi();
                }
            }

            return false;
        }
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.4F * (float)this.getSlimeSize();
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed()
    {
        return 0;
    }

    /**
     * Returns true if the slime makes a sound when it jumps (based upon the slime's size)
     */
    protected boolean makesSoundOnJump()
    {
        return this.getSlimeSize() > 0;
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jump()
    {
        this.field_70181_x = 0.41999998688697815D;
        this.isAirBorne = true;
    }

    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, @Nullable IEntityLivingData p_180482_2_)
    {
        int i = this.rand.nextInt(3);

        if (i < 2 && this.rand.nextFloat() < 0.5F * p_180482_1_.getClampedAdditionalDifficulty())
        {
            ++i;
        }

        int j = 1 << i;
        this.setSlimeSize(j, true);
        return super.func_180482_a(p_180482_1_, p_180482_2_);
    }

    protected SoundEvent getJumpSound()
    {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SLIME_JUMP_SMALL : SoundEvents.ENTITY_SLIME_JUMP;
    }

    static class AISlimeAttack extends EntityAIBase
    {
        private final EntitySlime slime;
        private int growTieredTimer;

        public AISlimeAttack(EntitySlime slimeIn)
        {
            this.slime = slimeIn;
            this.func_75248_a(2);
        }

        public boolean shouldExecute()
        {
            EntityLivingBase entitylivingbase = this.slime.getAttackTarget();

            if (entitylivingbase == null)
            {
                return false;
            }
            else if (!entitylivingbase.isAlive())
            {
                return false;
            }
            else
            {
                return !(entitylivingbase instanceof EntityPlayer) || !((EntityPlayer)entitylivingbase).abilities.disableDamage;
            }
        }

        public void startExecuting()
        {
            this.growTieredTimer = 300;
            super.startExecuting();
        }

        public boolean shouldContinueExecuting()
        {
            EntityLivingBase entitylivingbase = this.slime.getAttackTarget();

            if (entitylivingbase == null)
            {
                return false;
            }
            else if (!entitylivingbase.isAlive())
            {
                return false;
            }
            else if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).abilities.disableDamage)
            {
                return false;
            }
            else
            {
                return --this.growTieredTimer > 0;
            }
        }

        public void tick()
        {
            this.slime.faceEntity(this.slime.getAttackTarget(), 10.0F, 10.0F);
            ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setDirection(this.slime.rotationYaw, this.slime.canDamagePlayer());
        }
    }

    static class AISlimeFaceRandom extends EntityAIBase
    {
        private final EntitySlime slime;
        private float chosenDegrees;
        private int nextRandomizeTime;

        public AISlimeFaceRandom(EntitySlime slimeIn)
        {
            this.slime = slimeIn;
            this.func_75248_a(2);
        }

        public boolean shouldExecute()
        {
            return this.slime.getAttackTarget() == null && (this.slime.onGround || this.slime.isInWater() || this.slime.isInLava() || this.slime.isPotionActive(MobEffects.LEVITATION));
        }

        public void tick()
        {
            if (--this.nextRandomizeTime <= 0)
            {
                this.nextRandomizeTime = 40 + this.slime.getRNG().nextInt(60);
                this.chosenDegrees = (float)this.slime.getRNG().nextInt(360);
            }

            ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setDirection(this.chosenDegrees, false);
        }
    }

    static class AISlimeFloat extends EntityAIBase
    {
        private final EntitySlime slime;

        public AISlimeFloat(EntitySlime slimeIn)
        {
            this.slime = slimeIn;
            this.func_75248_a(5);
            ((PathNavigateGround)slimeIn.getNavigator()).func_179693_d(true);
        }

        public boolean shouldExecute()
        {
            return this.slime.isInWater() || this.slime.isInLava();
        }

        public void tick()
        {
            if (this.slime.getRNG().nextFloat() < 0.8F)
            {
                this.slime.getJumpController().setJumping();
            }

            ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.2D);
        }
    }

    static class AISlimeHop extends EntityAIBase
    {
        private final EntitySlime slime;

        public AISlimeHop(EntitySlime slimeIn)
        {
            this.slime = slimeIn;
            this.func_75248_a(5);
        }

        public boolean shouldExecute()
        {
            return true;
        }

        public void tick()
        {
            ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.0D);
        }
    }

    static class SlimeMoveHelper extends EntityMoveHelper
    {
        private float yRot;
        private int jumpDelay;
        private final EntitySlime slime;
        private boolean isAggressive;

        public SlimeMoveHelper(EntitySlime slimeIn)
        {
            super(slimeIn);
            this.slime = slimeIn;
            this.yRot = 180.0F * slimeIn.rotationYaw / (float)Math.PI;
        }

        public void setDirection(float yRotIn, boolean aggressive)
        {
            this.yRot = yRotIn;
            this.isAggressive = aggressive;
        }

        public void setSpeed(double speedIn)
        {
            this.speed = speedIn;
            this.action = EntityMoveHelper.Action.MOVE_TO;
        }

        public void tick()
        {
            this.mob.rotationYaw = this.limitAngle(this.mob.rotationYaw, this.yRot, 90.0F);
            this.mob.rotationYawHead = this.mob.rotationYaw;
            this.mob.renderYawOffset = this.mob.rotationYaw;

            if (this.action != EntityMoveHelper.Action.MOVE_TO)
            {
                this.mob.setMoveForward(0.0F);
            }
            else
            {
                this.action = EntityMoveHelper.Action.WAIT;

                if (this.mob.onGround)
                {
                    this.mob.setAIMoveSpeed((float)(this.speed * this.mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));

                    if (this.jumpDelay-- <= 0)
                    {
                        this.jumpDelay = this.slime.getJumpDelay();

                        if (this.isAggressive)
                        {
                            this.jumpDelay /= 3;
                        }

                        this.slime.getJumpController().setJumping();

                        if (this.slime.makesSoundOnJump())
                        {
                            this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), ((this.slime.getRNG().nextFloat() - this.slime.getRNG().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                        }
                    }
                    else
                    {
                        this.slime.moveStrafing = 0.0F;
                        this.slime.moveForward = 0.0F;
                        this.mob.setAIMoveSpeed(0.0F);
                    }
                }
                else
                {
                    this.mob.setAIMoveSpeed((float)(this.speed * this.mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));
                }
            }
        }
    }
}
