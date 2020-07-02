package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIOcelotAttack;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityOcelot extends EntityTameable
{
    private static final DataParameter<Integer> field_184757_bz = EntityDataManager.<Integer>createKey(EntityOcelot.class, DataSerializers.VARINT);
    private EntityAIAvoidEntity<EntityPlayer> field_175545_bm;
    private EntityAITempt aiTempt;

    public EntityOcelot(World p_i1688_1_)
    {
        super(p_i1688_1_);
        this.func_70105_a(0.6F, 0.7F);
    }

    protected void registerGoals()
    {
        this.sitGoal = new EntityAISit(this);
        this.aiTempt = new EntityAITempt(this, 0.6D, Items.field_151115_aP, true);
        this.goalSelector.addGoal(1, new EntityAISwimming(this));
        this.goalSelector.addGoal(2, this.sitGoal);
        this.goalSelector.addGoal(3, this.aiTempt);
        this.goalSelector.addGoal(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 5.0F));
        this.goalSelector.addGoal(6, new EntityAIOcelotSit(this, 0.8D));
        this.goalSelector.addGoal(7, new EntityAILeapAtTarget(this, 0.3F));
        this.goalSelector.addGoal(8, new EntityAIOcelotAttack(this));
        this.goalSelector.addGoal(9, new EntityAIMate(this, 0.8D));
        this.goalSelector.addGoal(10, new EntityAIWanderAvoidWater(this, 0.8D, 1.0000001E-5F));
        this.goalSelector.addGoal(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
        this.targetSelector.addGoal(1, new EntityAITargetNonTamed(this, EntityChicken.class, false, (Predicate)null));
    }

    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(field_184757_bz, Integer.valueOf(0));
    }

    public void updateAITasks()
    {
        if (this.getMoveHelper().isUpdating())
        {
            double d0 = this.getMoveHelper().getSpeed();

            if (d0 == 0.6D)
            {
                this.func_70095_a(true);
                this.setSprinting(false);
            }
            else if (d0 == 1.33D)
            {
                this.func_70095_a(false);
                this.setSprinting(true);
            }
            else
            {
                this.func_70095_a(false);
                this.setSprinting(false);
            }
        }
        else
        {
            this.func_70095_a(false);
            this.setSprinting(false);
        }
    }

    protected boolean func_70692_ba()
    {
        return !this.isTamed() && this.ticksExisted > 2400;
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
    }

    public void func_180430_e(float p_180430_1_, float p_180430_2_)
    {
    }

    public static void func_189787_b(DataFixer p_189787_0_)
    {
        EntityLiving.func_189752_a(p_189787_0_, EntityOcelot.class);
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);
        p_70014_1_.putInt("CatType", this.func_70913_u());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);
        this.func_70912_b(compound.getInt("CatType"));
    }

    @Nullable
    protected SoundEvent getAmbientSound()
    {
        if (this.isTamed())
        {
            if (this.isInLove())
            {
                return SoundEvents.ENTITY_CAT_PURR;
            }
            else
            {
                return this.rand.nextInt(4) == 0 ? SoundEvents.ENTITY_CAT_PURREOW : SoundEvents.ENTITY_CAT_AMBIENT;
            }
        }
        else
        {
            return null;
        }
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_CAT_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_CAT_DEATH;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.4F;
    }

    public boolean attackEntityAsMob(Entity entityIn)
    {
        return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
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
            if (this.sitGoal != null)
            {
                this.sitGoal.setSitting(false);
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.field_186402_J;
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (this.isTamed())
        {
            if (this.isOwner(player) && !this.world.isRemote && !this.isBreedingItem(itemstack))
            {
                this.sitGoal.setSitting(!this.isSitting());
            }
        }
        else if ((this.aiTempt == null || this.aiTempt.isRunning()) && itemstack.getItem() == Items.field_151115_aP && player.getDistanceSq(this) < 9.0D)
        {
            if (!player.abilities.isCreativeMode)
            {
                itemstack.shrink(1);
            }

            if (!this.world.isRemote)
            {
                if (this.rand.nextInt(3) == 0)
                {
                    this.setTamedBy(player);
                    this.func_70912_b(1 + this.world.rand.nextInt(3));
                    this.playTameEffect(true);
                    this.sitGoal.setSitting(true);
                    this.world.setEntityState(this, (byte)7);
                }
                else
                {
                    this.playTameEffect(false);
                    this.world.setEntityState(this, (byte)6);
                }
            }

            return true;
        }

        return super.processInteract(player, hand);
    }

    public EntityOcelot createChild(EntityAgeable ageable)
    {
        EntityOcelot entityocelot = new EntityOcelot(this.world);

        if (this.isTamed())
        {
            entityocelot.setOwnerId(this.getOwnerId());
            entityocelot.setTamed(true);
            entityocelot.func_70912_b(this.func_70913_u());
        }

        return entityocelot;
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack stack)
    {
        return stack.getItem() == Items.field_151115_aP;
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    public boolean canMateWith(EntityAnimal otherAnimal)
    {
        if (otherAnimal == this)
        {
            return false;
        }
        else if (!this.isTamed())
        {
            return false;
        }
        else if (!(otherAnimal instanceof EntityOcelot))
        {
            return false;
        }
        else
        {
            EntityOcelot entityocelot = (EntityOcelot)otherAnimal;

            if (!entityocelot.isTamed())
            {
                return false;
            }
            else
            {
                return this.isInLove() && entityocelot.isInLove();
            }
        }
    }

    public int func_70913_u()
    {
        return ((Integer)this.dataManager.get(field_184757_bz)).intValue();
    }

    public void func_70912_b(int p_70912_1_)
    {
        this.dataManager.set(field_184757_bz, Integer.valueOf(p_70912_1_));
    }

    public boolean func_70601_bi()
    {
        return this.world.rand.nextInt(3) != 0;
    }

    public boolean func_70058_J()
    {
        if (this.world.func_72917_a(this.getBoundingBox(), this) && this.world.func_184144_a(this, this.getBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(this.getBoundingBox()))
        {
            BlockPos blockpos = new BlockPos(this.posX, this.getBoundingBox().minY, this.posZ);

            if (blockpos.getY() < this.world.getSeaLevel())
            {
                return false;
            }

            IBlockState iblockstate = this.world.getBlockState(blockpos.down());
            Block block = iblockstate.getBlock();

            if (block == Blocks.GRASS || iblockstate.getMaterial() == Material.LEAVES)
            {
                return true;
            }
        }

        return false;
    }

    public String func_70005_c_()
    {
        if (this.hasCustomName())
        {
            return this.func_95999_t();
        }
        else
        {
            return this.isTamed() ? I18n.func_74838_a("entity.Cat.name") : super.func_70005_c_();
        }
    }

    protected void setupTamedAI()
    {
        if (this.field_175545_bm == null)
        {
            this.field_175545_bm = new EntityAIAvoidEntity<EntityPlayer>(this, EntityPlayer.class, 16.0F, 0.8D, 1.33D);
        }

        this.goalSelector.removeGoal(this.field_175545_bm);

        if (!this.isTamed())
        {
            this.goalSelector.addGoal(4, this.field_175545_bm);
        }
    }

    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, @Nullable IEntityLivingData p_180482_2_)
    {
        p_180482_2_ = super.func_180482_a(p_180482_1_, p_180482_2_);

        if (this.func_70913_u() == 0 && this.world.rand.nextInt(7) == 0)
        {
            for (int i = 0; i < 2; ++i)
            {
                EntityOcelot entityocelot = new EntityOcelot(this.world);
                entityocelot.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                entityocelot.setGrowingAge(-24000);
                this.world.addEntity0(entityocelot);
            }
        }

        return p_180482_2_;
    }
}
