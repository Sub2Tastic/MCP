package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIDefendVillage;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookAtVillager;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityIronGolem extends EntityGolem
{
    protected static final DataParameter<Byte> PLAYER_CREATED = EntityDataManager.<Byte>createKey(EntityIronGolem.class, DataSerializers.BYTE);
    private int field_70858_e;
    @Nullable
    Village field_70857_d;
    private int attackTimer;
    private int holdRoseTick;

    public EntityIronGolem(World p_i1694_1_)
    {
        super(p_i1694_1_);
        this.func_70105_a(1.4F, 2.7F);
    }

    protected void registerGoals()
    {
        this.goalSelector.addGoal(1, new EntityAIAttackMelee(this, 1.0D, true));
        this.goalSelector.addGoal(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
        this.goalSelector.addGoal(3, new EntityAIMoveThroughVillage(this, 0.6D, true));
        this.goalSelector.addGoal(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.addGoal(5, new EntityAILookAtVillager(this));
        this.goalSelector.addGoal(6, new EntityAIWanderAvoidWater(this, 0.6D));
        this.goalSelector.addGoal(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.goalSelector.addGoal(8, new EntityAILookIdle(this));
        this.targetSelector.addGoal(1, new EntityAIDefendVillage(this));
        this.targetSelector.addGoal(2, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetSelector.addGoal(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, false, true, new Predicate<EntityLiving>()
        {
            public boolean apply(@Nullable EntityLiving p_apply_1_)
            {
                return p_apply_1_ != null && IMob.field_175450_e.apply(p_apply_1_) && !(p_apply_1_ instanceof EntityCreeper);
            }
        }));
    }

    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(PLAYER_CREATED, Byte.valueOf((byte)0));
    }

    protected void updateAITasks()
    {
        if (--this.field_70858_e <= 0)
        {
            this.field_70858_e = 70 + this.rand.nextInt(50);
            this.field_70857_d = this.world.func_175714_ae().func_176056_a(new BlockPos(this), 32);

            if (this.field_70857_d == null)
            {
                this.func_110177_bN();
            }
            else
            {
                BlockPos blockpos = this.field_70857_d.func_180608_a();
                this.func_175449_a(blockpos, (int)((float)this.field_70857_d.func_75568_b() * 0.6F));
            }
        }

        super.updateAITasks();
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    /**
     * Decrements the entity's air supply when underwater
     */
    protected int decreaseAirSupply(int air)
    {
        return air;
    }

    protected void collideWithEntity(Entity entityIn)
    {
        if (entityIn instanceof IMob && !(entityIn instanceof EntityCreeper) && this.getRNG().nextInt(20) == 0)
        {
            this.setAttackTarget((EntityLivingBase)entityIn);
        }

        super.collideWithEntity(entityIn);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick()
    {
        super.livingTick();

        if (this.attackTimer > 0)
        {
            --this.attackTimer;
        }

        if (this.holdRoseTick > 0)
        {
            --this.holdRoseTick;
        }

        if (this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y > 2.500000277905201E-7D && this.rand.nextInt(5) == 0)
        {
            int i = MathHelper.floor(this.posX);
            int j = MathHelper.floor(this.posY - 0.20000000298023224D);
            int k = MathHelper.floor(this.posZ);
            IBlockState iblockstate = this.world.getBlockState(new BlockPos(i, j, k));

            if (iblockstate.getMaterial() != Material.AIR)
            {
                this.world.func_175688_a(EnumParticleTypes.BLOCK_CRACK, this.posX + ((double)this.rand.nextFloat() - 0.5D) * (double)this.field_70130_N, this.getBoundingBox().minY + 0.1D, this.posZ + ((double)this.rand.nextFloat() - 0.5D) * (double)this.field_70130_N, 4.0D * ((double)this.rand.nextFloat() - 0.5D), 0.5D, ((double)this.rand.nextFloat() - 0.5D) * 4.0D, Block.func_176210_f(iblockstate));
            }
        }
    }

    public boolean func_70686_a(Class <? extends EntityLivingBase > p_70686_1_)
    {
        if (this.isPlayerCreated() && EntityPlayer.class.isAssignableFrom(p_70686_1_))
        {
            return false;
        }
        else
        {
            return p_70686_1_ == EntityCreeper.class ? false : super.func_70686_a(p_70686_1_);
        }
    }

    public static void func_189784_b(DataFixer p_189784_0_)
    {
        EntityLiving.func_189752_a(p_189784_0_, EntityIronGolem.class);
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);
        p_70014_1_.putBoolean("PlayerCreated", this.isPlayerCreated());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);
        this.setPlayerCreated(compound.getBoolean("PlayerCreated"));
    }

    public boolean attackEntityAsMob(Entity entityIn)
    {
        this.attackTimer = 10;
        this.world.setEntityState(this, (byte)4);
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(7 + this.rand.nextInt(15)));

        if (flag)
        {
            entityIn.field_70181_x += 0.4000000059604645D;
            this.applyEnchantments(this, entityIn);
        }

        this.playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    public void handleStatusUpdate(byte id)
    {
        if (id == 4)
        {
            this.attackTimer = 10;
            this.playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        }
        else if (id == 11)
        {
            this.holdRoseTick = 400;
        }
        else if (id == 34)
        {
            this.holdRoseTick = 0;
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    public Village func_70852_n()
    {
        return this.field_70857_d;
    }

    public int getAttackTimer()
    {
        return this.attackTimer;
    }

    public void setHoldingRose(boolean holdingRose)
    {
        if (holdingRose)
        {
            this.holdRoseTick = 400;
            this.world.setEntityState(this, (byte)11);
        }
        else
        {
            this.holdRoseTick = 0;
            this.world.setEntityState(this, (byte)34);
        }
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_IRON_GOLEM_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_IRON_GOLEM_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_IRON_GOLEM_STEP, 1.0F, 1.0F);
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.field_186443_y;
    }

    public int getHoldRoseTick()
    {
        return this.holdRoseTick;
    }

    public boolean isPlayerCreated()
    {
        return (((Byte)this.dataManager.get(PLAYER_CREATED)).byteValue() & 1) != 0;
    }

    public void setPlayerCreated(boolean playerCreated)
    {
        byte b0 = ((Byte)this.dataManager.get(PLAYER_CREATED)).byteValue();

        if (playerCreated)
        {
            this.dataManager.set(PLAYER_CREATED, Byte.valueOf((byte)(b0 | 1)));
        }
        else
        {
            this.dataManager.set(PLAYER_CREATED, Byte.valueOf((byte)(b0 & -2)));
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource cause)
    {
        if (!this.isPlayerCreated() && this.attackingPlayer != null && this.field_70857_d != null)
        {
            this.field_70857_d.func_82688_a(this.attackingPlayer.func_70005_c_(), -5);
        }

        super.onDeath(cause);
    }
}
