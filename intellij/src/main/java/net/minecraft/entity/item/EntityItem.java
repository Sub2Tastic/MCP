package net.minecraft.entity.item;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityItem extends Entity
{
    private static final Logger field_145803_d = LogManager.getLogger();
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.<ItemStack>createKey(EntityItem.class, DataSerializers.ITEMSTACK);
    private int age;
    private int pickupDelay;
    private int health;
    private String thrower;
    private String owner;
    public float hoverStart;

    public EntityItem(World worldIn, double x, double y, double z)
    {
        super(worldIn);
        this.health = 5;
        this.hoverStart = (float)(Math.random() * Math.PI * 2.0D);
        this.func_70105_a(0.25F, 0.25F);
        this.setPosition(x, y, z);
        this.rotationYaw = (float)(Math.random() * 360.0D);
        this.field_70159_w = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
        this.field_70181_x = 0.20000000298023224D;
        this.field_70179_y = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
    }

    public EntityItem(World worldIn, double x, double y, double z, ItemStack stack)
    {
        this(worldIn, x, y, z);
        this.setItem(stack);
    }

    protected boolean func_70041_e_()
    {
        return false;
    }

    public EntityItem(World p_i1711_1_)
    {
        super(p_i1711_1_);
        this.health = 5;
        this.hoverStart = (float)(Math.random() * Math.PI * 2.0D);
        this.func_70105_a(0.25F, 0.25F);
        this.setItem(ItemStack.EMPTY);
    }

    protected void registerData()
    {
        this.getDataManager().register(ITEM, ItemStack.EMPTY);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        if (this.getItem().isEmpty())
        {
            this.remove();
        }
        else
        {
            super.tick();

            if (this.pickupDelay > 0 && this.pickupDelay != 32767)
            {
                --this.pickupDelay;
            }

            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            double d0 = this.field_70159_w;
            double d1 = this.field_70181_x;
            double d2 = this.field_70179_y;

            if (!this.hasNoGravity())
            {
                this.field_70181_x -= 0.03999999910593033D;
            }

            if (this.world.isRemote)
            {
                this.noClip = false;
            }
            else
            {
                this.noClip = this.func_145771_j(this.posX, (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.posZ);
            }

            this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
            boolean flag = (int)this.prevPosX != (int)this.posX || (int)this.prevPosY != (int)this.posY || (int)this.prevPosZ != (int)this.posZ;

            if (flag || this.ticksExisted % 25 == 0)
            {
                if (this.world.getBlockState(new BlockPos(this)).getMaterial() == Material.LAVA)
                {
                    this.field_70181_x = 0.20000000298023224D;
                    this.field_70159_w = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
                    this.field_70179_y = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
                    this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
                }

                if (!this.world.isRemote)
                {
                    this.searchForOtherItemsNearby();
                }
            }

            float f = 0.98F;

            if (this.onGround)
            {
                f = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.98F;
            }

            this.field_70159_w *= (double)f;
            this.field_70181_x *= 0.9800000190734863D;
            this.field_70179_y *= (double)f;

            if (this.onGround)
            {
                this.field_70181_x *= -0.5D;
            }

            if (this.age != -32768)
            {
                ++this.age;
            }

            this.handleWaterMovement();

            if (!this.world.isRemote)
            {
                double d3 = this.field_70159_w - d0;
                double d4 = this.field_70181_x - d1;
                double d5 = this.field_70179_y - d2;
                double d6 = d3 * d3 + d4 * d4 + d5 * d5;

                if (d6 > 0.01D)
                {
                    this.isAirBorne = true;
                }
            }

            if (!this.world.isRemote && this.age >= 6000)
            {
                this.remove();
            }
        }
    }

    /**
     * Looks for other itemstacks nearby and tries to stack them together
     */
    private void searchForOtherItemsNearby()
    {
        for (EntityItem entityitem : this.world.func_72872_a(EntityItem.class, this.getBoundingBox().grow(0.5D, 0.0D, 0.5D)))
        {
            this.func_70289_a(entityitem);
        }
    }

    private boolean func_70289_a(EntityItem p_70289_1_)
    {
        if (p_70289_1_ == this)
        {
            return false;
        }
        else if (p_70289_1_.isAlive() && this.isAlive())
        {
            ItemStack itemstack = this.getItem();
            ItemStack itemstack1 = p_70289_1_.getItem();

            if (this.pickupDelay != 32767 && p_70289_1_.pickupDelay != 32767)
            {
                if (this.age != -32768 && p_70289_1_.age != -32768)
                {
                    if (itemstack1.getItem() != itemstack.getItem())
                    {
                        return false;
                    }
                    else if (itemstack1.hasTag() ^ itemstack.hasTag())
                    {
                        return false;
                    }
                    else if (itemstack1.hasTag() && !itemstack1.getTag().equals(itemstack.getTag()))
                    {
                        return false;
                    }
                    else if (itemstack1.getItem() == null)
                    {
                        return false;
                    }
                    else if (itemstack1.getItem().func_77614_k() && itemstack1.func_77960_j() != itemstack.func_77960_j())
                    {
                        return false;
                    }
                    else if (itemstack1.getCount() < itemstack.getCount())
                    {
                        return p_70289_1_.func_70289_a(this);
                    }
                    else if (itemstack1.getCount() + itemstack.getCount() > itemstack1.getMaxStackSize())
                    {
                        return false;
                    }
                    else
                    {
                        itemstack1.grow(itemstack.getCount());
                        p_70289_1_.pickupDelay = Math.max(p_70289_1_.pickupDelay, this.pickupDelay);
                        p_70289_1_.age = Math.min(p_70289_1_.age, this.age);
                        p_70289_1_.setItem(itemstack1);
                        this.remove();
                        return true;
                    }
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public void func_70288_d()
    {
        this.age = 4800;
    }

    /**
     * Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    public boolean handleWaterMovement()
    {
        if (this.world.func_72918_a(this.getBoundingBox(), Material.WATER, this))
        {
            if (!this.inWater && !this.firstUpdate)
            {
                this.doWaterSplashEffect();
            }

            this.inWater = true;
        }
        else
        {
            this.inWater = false;
        }

        return this.inWater;
    }

    /**
     * Will deal the specified amount of fire damage to the entity if the entity isn't immune to fire damage.
     */
    protected void dealFireDamage(int amount)
    {
        this.attackEntityFrom(DamageSource.IN_FIRE, (float)amount);
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
        else if (!this.getItem().isEmpty() && this.getItem().getItem() == Items.NETHER_STAR && source.isExplosion())
        {
            return false;
        }
        else
        {
            this.markVelocityChanged();
            this.health = (int)((float)this.health - amount);

            if (this.health <= 0)
            {
                this.remove();
            }

            return false;
        }
    }

    public static void func_189742_a(DataFixer p_189742_0_)
    {
        p_189742_0_.func_188258_a(FixTypes.ENTITY, new ItemStackData(EntityItem.class, new String[] {"Item"}));
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        p_70014_1_.putShort("Health", (short)this.health);
        p_70014_1_.putShort("Age", (short)this.age);
        p_70014_1_.putShort("PickupDelay", (short)this.pickupDelay);

        if (this.func_145800_j() != null)
        {
            p_70014_1_.putString("Thrower", this.thrower);
        }

        if (this.func_145798_i() != null)
        {
            p_70014_1_.putString("Owner", this.owner);
        }

        if (!this.getItem().isEmpty())
        {
            p_70014_1_.func_74782_a("Item", this.getItem().write(new NBTTagCompound()));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        this.health = compound.getShort("Health");
        this.age = compound.getShort("Age");

        if (compound.contains("PickupDelay"))
        {
            this.pickupDelay = compound.getShort("PickupDelay");
        }

        if (compound.contains("Owner"))
        {
            this.owner = compound.getString("Owner");
        }

        if (compound.contains("Thrower"))
        {
            this.thrower = compound.getString("Thrower");
        }

        NBTTagCompound nbttagcompound = compound.getCompound("Item");
        this.setItem(new ItemStack(nbttagcompound));

        if (this.getItem().isEmpty())
        {
            this.remove();
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityIn)
    {
        if (!this.world.isRemote)
        {
            ItemStack itemstack = this.getItem();
            Item item = itemstack.getItem();
            int i = itemstack.getCount();

            if (this.pickupDelay == 0 && (this.owner == null || 6000 - this.age <= 200 || this.owner.equals(entityIn.func_70005_c_())) && entityIn.inventory.addItemStackToInventory(itemstack))
            {
                entityIn.onItemPickup(this, i);

                if (itemstack.isEmpty())
                {
                    this.remove();
                    itemstack.setCount(i);
                }

                entityIn.addStat(StatList.func_188056_d(item), i);
            }
        }
    }

    public String func_70005_c_()
    {
        return this.hasCustomName() ? this.func_95999_t() : I18n.func_74838_a("item." + this.getItem().getTranslationKey());
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem()
    {
        return false;
    }

    @Nullable
    public Entity func_184204_a(int p_184204_1_)
    {
        Entity entity = super.func_184204_a(p_184204_1_);

        if (!this.world.isRemote && entity instanceof EntityItem)
        {
            ((EntityItem)entity).searchForOtherItemsNearby();
        }

        return entity;
    }

    /**
     * Gets the item that this entity represents.
     */
    public ItemStack getItem()
    {
        return (ItemStack)this.getDataManager().get(ITEM);
    }

    /**
     * Sets the item that this entity represents.
     */
    public void setItem(ItemStack stack)
    {
        this.getDataManager().set(ITEM, stack);
        this.getDataManager().func_187217_b(ITEM);
    }

    public String func_145798_i()
    {
        return this.owner;
    }

    public void func_145797_a(String p_145797_1_)
    {
        this.owner = p_145797_1_;
    }

    public String func_145800_j()
    {
        return this.thrower;
    }

    public void func_145799_b(String p_145799_1_)
    {
        this.thrower = p_145799_1_;
    }

    public int getAge()
    {
        return this.age;
    }

    public void setDefaultPickupDelay()
    {
        this.pickupDelay = 10;
    }

    public void setNoPickupDelay()
    {
        this.pickupDelay = 0;
    }

    public void setInfinitePickupDelay()
    {
        this.pickupDelay = 32767;
    }

    public void setPickupDelay(int ticks)
    {
        this.pickupDelay = ticks;
    }

    public boolean cannotPickup()
    {
        return this.pickupDelay > 0;
    }

    public void setNoDespawn()
    {
        this.age = -6000;
    }

    public void makeFakeItem()
    {
        this.setInfinitePickupDelay();
        this.age = 5999;
    }
}
