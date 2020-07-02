package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILlamaFollowCaravan;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityLlama extends AbstractChestHorse implements IRangedAttackMob
{
    private static final DataParameter<Integer> DATA_STRENGTH_ID = EntityDataManager.<Integer>createKey(EntityLlama.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.<Integer>createKey(EntityLlama.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DATA_VARIANT_ID = EntityDataManager.<Integer>createKey(EntityLlama.class, DataSerializers.VARINT);
    private boolean didSpit;
    @Nullable
    private EntityLlama caravanHead;
    @Nullable
    private EntityLlama caravanTail;

    public EntityLlama(World p_i47297_1_)
    {
        super(p_i47297_1_);
        this.func_70105_a(0.9F, 1.87F);
    }

    private void setStrength(int strengthIn)
    {
        this.dataManager.set(DATA_STRENGTH_ID, Integer.valueOf(Math.max(1, Math.min(5, strengthIn))));
    }

    private void setRandomStrength()
    {
        int i = this.rand.nextFloat() < 0.04F ? 5 : 3;
        this.setStrength(1 + this.rand.nextInt(i));
    }

    public int getStrength()
    {
        return ((Integer)this.dataManager.get(DATA_STRENGTH_ID)).intValue();
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);
        p_70014_1_.putInt("Variant", this.getVariant());
        p_70014_1_.putInt("Strength", this.getStrength());

        if (!this.horseChest.getStackInSlot(1).isEmpty())
        {
            p_70014_1_.func_74782_a("DecorItem", this.horseChest.getStackInSlot(1).write(new NBTTagCompound()));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        this.setStrength(compound.getInt("Strength"));
        super.readAdditional(compound);
        this.setVariant(compound.getInt("Variant"));

        if (compound.contains("DecorItem", 10))
        {
            this.horseChest.setInventorySlotContents(1, new ItemStack(compound.getCompound("DecorItem")));
        }

        this.updateHorseSlots();
    }

    protected void registerGoals()
    {
        this.goalSelector.addGoal(0, new EntityAISwimming(this));
        this.goalSelector.addGoal(1, new EntityAIRunAroundLikeCrazy(this, 1.2D));
        this.goalSelector.addGoal(2, new EntityAILlamaFollowCaravan(this, 2.0999999046325684D));
        this.goalSelector.addGoal(3, new EntityAIAttackRanged(this, 1.25D, 40, 20.0F));
        this.goalSelector.addGoal(3, new EntityAIPanic(this, 1.2D));
        this.goalSelector.addGoal(4, new EntityAIMate(this, 1.0D));
        this.goalSelector.addGoal(5, new EntityAIFollowParent(this, 1.0D));
        this.goalSelector.addGoal(6, new EntityAIWanderAvoidWater(this, 0.7D));
        this.goalSelector.addGoal(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.goalSelector.addGoal(8, new EntityAILookIdle(this));
        this.targetSelector.addGoal(1, new EntityLlama.AIHurtByTarget(this));
        this.targetSelector.addGoal(2, new EntityLlama.AIDefendTarget(this));
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
    }

    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(DATA_STRENGTH_ID, Integer.valueOf(0));
        this.dataManager.register(DATA_COLOR_ID, Integer.valueOf(-1));
        this.dataManager.register(DATA_VARIANT_ID, Integer.valueOf(0));
    }

    public int getVariant()
    {
        return MathHelper.clamp(((Integer)this.dataManager.get(DATA_VARIANT_ID)).intValue(), 0, 3);
    }

    public void setVariant(int variantIn)
    {
        this.dataManager.set(DATA_VARIANT_ID, Integer.valueOf(variantIn));
    }

    protected int getInventorySize()
    {
        return this.hasChest() ? 2 + 3 * this.getInventoryColumns() : super.getInventorySize();
    }

    public void updatePassenger(Entity passenger)
    {
        if (this.isPassenger(passenger))
        {
            float f = MathHelper.cos(this.renderYawOffset * 0.017453292F);
            float f1 = MathHelper.sin(this.renderYawOffset * 0.017453292F);
            float f2 = 0.3F;
            passenger.setPosition(this.posX + (double)(0.3F * f1), this.posY + this.getMountedYOffset() + passenger.getYOffset(), this.posZ - (double)(0.3F * f));
        }
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return (double)this.field_70131_O * 0.67D;
    }

    /**
     * returns true if all the conditions for steering the entity are met. For pigs, this is true if it is being ridden
     * by a player and the player is holding a carrot-on-a-stick
     */
    public boolean canBeSteered()
    {
        return false;
    }

    protected boolean handleEating(EntityPlayer player, ItemStack stack)
    {
        int i = 0;
        int j = 0;
        float f = 0.0F;
        boolean flag = false;
        Item item = stack.getItem();

        if (item == Items.WHEAT)
        {
            i = 10;
            j = 3;
            f = 2.0F;
        }
        else if (item == Item.getItemFromBlock(Blocks.HAY_BLOCK))
        {
            i = 90;
            j = 6;
            f = 10.0F;

            if (this.isTame() && this.getGrowingAge() == 0)
            {
                flag = true;
                this.setInLove(player);
            }
        }

        if (this.getHealth() < this.getMaxHealth() && f > 0.0F)
        {
            this.heal(f);
            flag = true;
        }

        if (this.isChild() && i > 0)
        {
            this.world.func_175688_a(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double)(this.rand.nextFloat() * this.field_70130_N * 2.0F) - (double)this.field_70130_N, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.field_70131_O), this.posZ + (double)(this.rand.nextFloat() * this.field_70130_N * 2.0F) - (double)this.field_70130_N, 0.0D, 0.0D, 0.0D);

            if (!this.world.isRemote)
            {
                this.addGrowth(i);
            }

            flag = true;
        }

        if (j > 0 && (flag || !this.isTame()) && this.getTemper() < this.getMaxTemper())
        {
            flag = true;

            if (!this.world.isRemote)
            {
                this.increaseTemper(j);
            }
        }

        if (flag && !this.isSilent())
        {
            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LLAMA_EAT, this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
        }

        return flag;
    }

    /**
     * Dead and sleeping entities cannot move
     */
    protected boolean isMovementBlocked()
    {
        return this.getHealth() <= 0.0F || this.isEatingHaystack();
    }

    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, @Nullable IEntityLivingData p_180482_2_)
    {
        p_180482_2_ = super.func_180482_a(p_180482_1_, p_180482_2_);
        this.setRandomStrength();
        int i;

        if (p_180482_2_ instanceof EntityLlama.GroupData)
        {
            i = ((EntityLlama.GroupData)p_180482_2_).variant;
        }
        else
        {
            i = this.rand.nextInt(4);
            p_180482_2_ = new EntityLlama.GroupData(i);
        }

        this.setVariant(i);
        return p_180482_2_;
    }

    public boolean func_190717_dN()
    {
        return this.getColor() != null;
    }

    protected SoundEvent getAngrySound()
    {
        return SoundEvents.ENTITY_LLAMA_ANGRY;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_LLAMA_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_LLAMA_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_LLAMA_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_LLAMA_STEP, 0.15F, 1.0F);
    }

    protected void playChestEquipSound()
    {
        this.playSound(SoundEvents.ENTITY_LLAMA_CHEST, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
    }

    public void makeMad()
    {
        SoundEvent soundevent = this.getAngrySound();

        if (soundevent != null)
        {
            this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.field_191187_aw;
    }

    public int getInventoryColumns()
    {
        return this.getStrength();
    }

    public boolean wearsArmor()
    {
        return true;
    }

    public boolean isArmor(ItemStack stack)
    {
        return stack.getItem() == Item.getItemFromBlock(Blocks.field_150404_cg);
    }

    public boolean canBeSaddled()
    {
        return false;
    }

    /**
     * Called by InventoryBasic.onInventoryChanged() on a array that is never filled.
     */
    public void onInventoryChanged(IInventory invBasic)
    {
        EnumDyeColor enumdyecolor = this.getColor();
        super.onInventoryChanged(invBasic);
        EnumDyeColor enumdyecolor1 = this.getColor();

        if (this.ticksExisted > 20 && enumdyecolor1 != null && enumdyecolor1 != enumdyecolor)
        {
            this.playSound(SoundEvents.ENTITY_LLAMA_SWAG, 0.5F, 1.0F);
        }
    }

    /**
     * Updates the items in the saddle and armor slots of the horse's inventory.
     */
    protected void updateHorseSlots()
    {
        if (!this.world.isRemote)
        {
            super.updateHorseSlots();
            this.func_190702_g(this.horseChest.getStackInSlot(1));
        }
    }

    private void setColor(@Nullable EnumDyeColor color)
    {
        this.dataManager.set(DATA_COLOR_ID, Integer.valueOf(color == null ? -1 : color.func_176765_a()));
    }

    private void func_190702_g(ItemStack p_190702_1_)
    {
        if (this.isArmor(p_190702_1_))
        {
            this.setColor(EnumDyeColor.func_176764_b(p_190702_1_.func_77960_j()));
        }
        else
        {
            this.setColor((EnumDyeColor)null);
        }
    }

    @Nullable
    public EnumDyeColor getColor()
    {
        int i = ((Integer)this.dataManager.get(DATA_COLOR_ID)).intValue();
        return i == -1 ? null : EnumDyeColor.func_176764_b(i);
    }

    public int getMaxTemper()
    {
        return 30;
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    public boolean canMateWith(EntityAnimal otherAnimal)
    {
        return otherAnimal != this && otherAnimal instanceof EntityLlama && this.canMate() && ((EntityLlama)otherAnimal).canMate();
    }

    public EntityLlama createChild(EntityAgeable ageable)
    {
        EntityLlama entityllama = new EntityLlama(this.world);
        this.setOffspringAttributes(ageable, entityllama);
        EntityLlama entityllama1 = (EntityLlama)ageable;
        int i = this.rand.nextInt(Math.max(this.getStrength(), entityllama1.getStrength())) + 1;

        if (this.rand.nextFloat() < 0.03F)
        {
            ++i;
        }

        entityllama.setStrength(i);
        entityllama.setVariant(this.rand.nextBoolean() ? this.getVariant() : entityllama1.getVariant());
        return entityllama;
    }

    private void spit(EntityLivingBase target)
    {
        EntityLlamaSpit entityllamaspit = new EntityLlamaSpit(this.world, this);
        double d0 = target.posX - this.posX;
        double d1 = target.getBoundingBox().minY + (double)(target.field_70131_O / 3.0F) - entityllamaspit.posY;
        double d2 = target.posZ - this.posZ;
        float f = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2F;
        entityllamaspit.shoot(d0, d1 + (double)f, d2, 1.5F, 10.0F);
        this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LLAMA_SPIT, this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
        this.world.addEntity0(entityllamaspit);
        this.didSpit = true;
    }

    private void setDidSpit(boolean didSpitIn)
    {
        this.didSpit = didSpitIn;
    }

    public void func_180430_e(float p_180430_1_, float p_180430_2_)
    {
        int i = MathHelper.ceil((p_180430_1_ * 0.5F - 3.0F) * p_180430_2_);

        if (i > 0)
        {
            if (p_180430_1_ >= 6.0F)
            {
                this.attackEntityFrom(DamageSource.FALL, (float)i);

                if (this.isBeingRidden())
                {
                    for (Entity entity : this.getRecursivePassengers())
                    {
                        entity.attackEntityFrom(DamageSource.FALL, (float)i);
                    }
                }
            }

            IBlockState iblockstate = this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.2D - (double)this.prevRotationYaw, this.posZ));
            Block block = iblockstate.getBlock();

            if (iblockstate.getMaterial() != Material.AIR && !this.isSilent())
            {
                SoundType soundtype = block.func_185467_w();
                this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, soundtype.getStepSound(), this.getSoundCategory(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
            }
        }
    }

    public void leaveCaravan()
    {
        if (this.caravanHead != null)
        {
            this.caravanHead.caravanTail = null;
        }

        this.caravanHead = null;
    }

    public void joinCaravan(EntityLlama caravanHeadIn)
    {
        this.caravanHead = caravanHeadIn;
        this.caravanHead.caravanTail = this;
    }

    public boolean hasCaravanTrail()
    {
        return this.caravanTail != null;
    }

    public boolean inCaravan()
    {
        return this.caravanHead != null;
    }

    @Nullable
    public EntityLlama getCaravanHead()
    {
        return this.caravanHead;
    }

    protected double followLeashSpeed()
    {
        return 2.0D;
    }

    protected void followMother()
    {
        if (!this.inCaravan() && this.isChild())
        {
            super.followMother();
        }
    }

    public boolean canEatGrass()
    {
        return false;
    }

    /**
     * Attack the specified entity using a ranged attack.
     */
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
    {
        this.spit(target);
    }

    public void func_184724_a(boolean p_184724_1_)
    {
    }

    static class AIDefendTarget extends EntityAINearestAttackableTarget<EntityWolf>
    {
        public AIDefendTarget(EntityLlama llama)
        {
            super(llama, EntityWolf.class, 16, false, true, (Predicate)null);
        }

        public boolean shouldExecute()
        {
            if (super.shouldExecute() && this.nearestTarget != null && !((EntityWolf)this.nearestTarget).isTamed())
            {
                return true;
            }
            else
            {
                this.goalOwner.setAttackTarget((EntityLivingBase)null);
                return false;
            }
        }

        protected double getTargetDistance()
        {
            return super.getTargetDistance() * 0.25D;
        }
    }

    static class AIHurtByTarget extends EntityAIHurtByTarget
    {
        public AIHurtByTarget(EntityLlama llama)
        {
            super(llama, false);
        }

        public boolean shouldContinueExecuting()
        {
            if (this.goalOwner instanceof EntityLlama)
            {
                EntityLlama entityllama = (EntityLlama)this.goalOwner;

                if (entityllama.didSpit)
                {
                    entityllama.setDidSpit(false);
                    return false;
                }
            }

            return super.shouldContinueExecuting();
        }
    }

    static class GroupData implements IEntityLivingData
    {
        public int variant;

        private GroupData(int variantIn)
        {
            this.variant = variantIn;
        }
    }
}
