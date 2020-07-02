package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityVindicator extends AbstractIllager
{
    private boolean johnny;
    private static final Predicate<Entity> field_190644_c = new Predicate<Entity>()
    {
        public boolean apply(@Nullable Entity p_apply_1_)
        {
            return p_apply_1_ instanceof EntityLivingBase && ((EntityLivingBase)p_apply_1_).attackable();
        }
    };

    public EntityVindicator(World p_i47279_1_)
    {
        super(p_i47279_1_);
        this.func_70105_a(0.6F, 1.95F);
    }

    public static void func_190641_b(DataFixer p_190641_0_)
    {
        EntityLiving.func_189752_a(p_190641_0_, EntityVindicator.class);
    }

    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal(0, new EntityAISwimming(this));
        this.goalSelector.addGoal(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.goalSelector.addGoal(8, new EntityAIWander(this, 0.6D));
        this.goalSelector.addGoal(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetSelector.addGoal(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityVindicator.class}));
        this.targetSelector.addGoal(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetSelector.addGoal(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, true));
        this.targetSelector.addGoal(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
        this.targetSelector.addGoal(4, new EntityVindicator.AIJohnnyAttack(this));
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3499999940395355D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(12.0D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
    }

    protected void registerData()
    {
        super.registerData();
    }

    protected ResourceLocation getLootTable()
    {
        return LootTableList.field_191186_av;
    }

    public boolean func_190639_o()
    {
        return this.func_193078_a(1);
    }

    public void func_190636_a(boolean p_190636_1_)
    {
        this.func_193079_a(1, p_190636_1_);
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);

        if (this.johnny)
        {
            p_70014_1_.putBoolean("Johnny", true);
        }
    }

    public AbstractIllager.IllagerArmPose getArmPose()
    {
        return this.func_190639_o() ? AbstractIllager.IllagerArmPose.ATTACKING : AbstractIllager.IllagerArmPose.CROSSED;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);

        if (compound.contains("Johnny", 99))
        {
            this.johnny = compound.getBoolean("Johnny");
        }
    }

    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, @Nullable IEntityLivingData p_180482_2_)
    {
        IEntityLivingData ientitylivingdata = super.func_180482_a(p_180482_1_, p_180482_2_);
        this.setEquipmentBasedOnDifficulty(p_180482_1_);
        this.setEnchantmentBasedOnDifficulty(p_180482_1_);
        return ientitylivingdata;
    }

    /**
     * Gives armor or weapon for entity based on given DifficultyInstance
     */
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
    }

    protected void updateAITasks()
    {
        super.updateAITasks();
        this.func_190636_a(this.getAttackTarget() != null);
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isOnSameTeam(Entity entityIn)
    {
        if (super.isOnSameTeam(entityIn))
        {
            return true;
        }
        else if (entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).getCreatureAttribute() == EnumCreatureAttribute.ILLAGER)
        {
            return this.getTeam() == null && entityIn.getTeam() == null;
        }
        else
        {
            return false;
        }
    }

    public void func_96094_a(String p_96094_1_)
    {
        super.func_96094_a(p_96094_1_);

        if (!this.johnny && "Johnny".equals(p_96094_1_))
        {
            this.johnny = true;
        }
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_VINDICATOR_AMBIENT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_VINDICATOR_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_VINDICATOR_HURT;
    }

    static class AIJohnnyAttack extends EntityAINearestAttackableTarget<EntityLivingBase>
    {
        public AIJohnnyAttack(EntityVindicator vindicator)
        {
            super(vindicator, EntityLivingBase.class, 0, true, true, EntityVindicator.field_190644_c);
        }

        public boolean shouldExecute()
        {
            return ((EntityVindicator)this.goalOwner).johnny && super.shouldExecute();
        }
    }
}
