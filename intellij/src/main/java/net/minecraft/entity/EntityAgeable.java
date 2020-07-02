package net.minecraft.entity;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public abstract class EntityAgeable extends EntityCreature
{
    private static final DataParameter<Boolean> BABY = EntityDataManager.<Boolean>createKey(EntityAgeable.class, DataSerializers.BOOLEAN);
    protected int growingAge;
    protected int forcedAge;
    protected int forcedAgeTimer;
    private float field_98056_d = -1.0F;
    private float field_98057_e;

    public EntityAgeable(World p_i1578_1_)
    {
        super(p_i1578_1_);
    }

    @Nullable
    public abstract EntityAgeable createChild(EntityAgeable ageable);

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (itemstack.getItem() == Items.field_151063_bx)
        {
            if (!this.world.isRemote)
            {
                Class <? extends Entity > oclass = (Class)EntityList.field_191308_b.getOrDefault(ItemMonsterPlacer.func_190908_h(itemstack));

                if (oclass != null && this.getClass() == oclass)
                {
                    EntityAgeable entityageable = this.createChild(this);

                    if (entityageable != null)
                    {
                        entityageable.setGrowingAge(-24000);
                        entityageable.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
                        this.world.addEntity0(entityageable);

                        if (itemstack.hasDisplayName())
                        {
                            entityageable.func_96094_a(itemstack.func_82833_r());
                        }

                        if (!player.abilities.isCreativeMode)
                        {
                            itemstack.shrink(1);
                        }
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected boolean func_190669_a(ItemStack p_190669_1_, Class <? extends Entity > p_190669_2_)
    {
        if (p_190669_1_.getItem() != Items.field_151063_bx)
        {
            return false;
        }
        else
        {
            Class <? extends Entity > oclass = (Class)EntityList.field_191308_b.getOrDefault(ItemMonsterPlacer.func_190908_h(p_190669_1_));
            return oclass != null && p_190669_2_ == oclass;
        }
    }

    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(BABY, Boolean.valueOf(false));
    }

    /**
     * The age value may be negative or positive or zero. If it's negative, it get's incremented on each tick, if it's
     * positive, it get's decremented each tick. Don't confuse this with EntityLiving.getAge. With a negative value the
     * Entity is considered a child.
     */
    public int getGrowingAge()
    {
        if (this.world.isRemote)
        {
            return ((Boolean)this.dataManager.get(BABY)).booleanValue() ? -1 : 1;
        }
        else
        {
            return this.growingAge;
        }
    }

    /**
     * Increases this entity's age, optionally updating {@link #forcedAge}. If the entity is an adult (if the entity's
     * age is greater than or equal to 0) then the entity's age will be set to {@link #forcedAge}.
     */
    public void ageUp(int growthSeconds, boolean updateForcedAge)
    {
        int i = this.getGrowingAge();
        int j = i;
        i = i + growthSeconds * 20;

        if (i > 0)
        {
            i = 0;

            if (j < 0)
            {
                this.onGrowingAdult();
            }
        }

        int k = i - j;
        this.setGrowingAge(i);

        if (updateForcedAge)
        {
            this.forcedAge += k;

            if (this.forcedAgeTimer == 0)
            {
                this.forcedAgeTimer = 40;
            }
        }

        if (this.getGrowingAge() == 0)
        {
            this.setGrowingAge(this.forcedAge);
        }
    }

    /**
     * Increases this entity's age. If the entity is an adult (if the entity's age is greater than or equal to 0) then
     * the entity's age will be set to {@link #forcedAge}. This method does not update {@link #forcedAge}.
     */
    public void addGrowth(int growth)
    {
        this.ageUp(growth, false);
    }

    /**
     * The age value may be negative or positive or zero. If it's negative, it get's incremented on each tick, if it's
     * positive, it get's decremented each tick. With a negative value the Entity is considered a child.
     */
    public void setGrowingAge(int age)
    {
        this.dataManager.set(BABY, Boolean.valueOf(age < 0));
        this.growingAge = age;
        this.func_98054_a(this.isChild());
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);
        p_70014_1_.putInt("Age", this.getGrowingAge());
        p_70014_1_.putInt("ForcedAge", this.forcedAge);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);
        this.setGrowingAge(compound.getInt("Age"));
        this.forcedAge = compound.getInt("ForcedAge");
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (BABY.equals(key))
        {
            this.func_98054_a(this.isChild());
        }

        super.notifyDataManagerChange(key);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick()
    {
        super.livingTick();

        if (this.world.isRemote)
        {
            if (this.forcedAgeTimer > 0)
            {
                if (this.forcedAgeTimer % 4 == 0)
                {
                    this.world.func_175688_a(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (double)(this.rand.nextFloat() * this.field_70130_N * 2.0F) - (double)this.field_70130_N, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.field_70131_O), this.posZ + (double)(this.rand.nextFloat() * this.field_70130_N * 2.0F) - (double)this.field_70130_N, 0.0D, 0.0D, 0.0D);
                }

                --this.forcedAgeTimer;
            }
        }
        else
        {
            int i = this.getGrowingAge();

            if (i < 0)
            {
                ++i;
                this.setGrowingAge(i);

                if (i == 0)
                {
                    this.onGrowingAdult();
                }
            }
            else if (i > 0)
            {
                --i;
                this.setGrowingAge(i);
            }
        }
    }

    /**
     * This is called when Entity's growing age timer reaches 0 (negative values are considered as a child, positive as
     * an adult)
     */
    protected void onGrowingAdult()
    {
    }

    /**
     * If Animal, checks if the age timer is negative
     */
    public boolean isChild()
    {
        return this.getGrowingAge() < 0;
    }

    public void func_98054_a(boolean p_98054_1_)
    {
        this.func_98055_j(p_98054_1_ ? 0.5F : 1.0F);
    }

    protected final void func_70105_a(float p_70105_1_, float p_70105_2_)
    {
        boolean flag = this.field_98056_d > 0.0F;
        this.field_98056_d = p_70105_1_;
        this.field_98057_e = p_70105_2_;

        if (!flag)
        {
            this.func_98055_j(1.0F);
        }
    }

    protected final void func_98055_j(float p_98055_1_)
    {
        super.func_70105_a(this.field_98056_d * p_98055_1_, this.field_98057_e * p_98055_1_);
    }
}
