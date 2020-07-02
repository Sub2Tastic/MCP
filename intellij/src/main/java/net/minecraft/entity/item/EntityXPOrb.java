package net.minecraft.entity.item;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityXPOrb extends Entity
{
    public int xpColor;
    public int xpOrbAge;
    public int delayBeforeCanPickup;
    private int xpOrbHealth = 5;
    private int xpValue;
    private EntityPlayer closestPlayer;
    private int xpTargetColor;

    public EntityXPOrb(World worldIn, double x, double y, double z, int expValue)
    {
        super(worldIn);
        this.func_70105_a(0.5F, 0.5F);
        this.setPosition(x, y, z);
        this.rotationYaw = (float)(Math.random() * 360.0D);
        this.field_70159_w = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.field_70181_x = (double)((float)(Math.random() * 0.2D) * 2.0F);
        this.field_70179_y = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.xpValue = expValue;
    }

    protected boolean func_70041_e_()
    {
        return false;
    }

    public EntityXPOrb(World p_i1586_1_)
    {
        super(p_i1586_1_);
        this.func_70105_a(0.25F, 0.25F);
    }

    protected void registerData()
    {
    }

    public int func_70070_b()
    {
        float f = 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.func_70070_b();
        int j = i & 255;
        int k = i >> 16 & 255;
        j = j + (int)(f * 15.0F * 16.0F);

        if (j > 240)
        {
            j = 240;
        }

        return j | k << 16;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        super.tick();

        if (this.delayBeforeCanPickup > 0)
        {
            --this.delayBeforeCanPickup;
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (!this.hasNoGravity())
        {
            this.field_70181_x -= 0.029999999329447746D;
        }

        if (this.world.getBlockState(new BlockPos(this)).getMaterial() == Material.LAVA)
        {
            this.field_70181_x = 0.20000000298023224D;
            this.field_70159_w = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.field_70179_y = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
        }

        this.func_145771_j(this.posX, (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.posZ);
        double d0 = 8.0D;

        if (this.xpTargetColor < this.xpColor - 20 + this.getEntityId() % 100)
        {
            if (this.closestPlayer == null || this.closestPlayer.getDistanceSq(this) > 64.0D)
            {
                this.closestPlayer = this.world.func_72890_a(this, 8.0D);
            }

            this.xpTargetColor = this.xpColor;
        }

        if (this.closestPlayer != null && this.closestPlayer.isSpectator())
        {
            this.closestPlayer = null;
        }

        if (this.closestPlayer != null)
        {
            double d1 = (this.closestPlayer.posX - this.posX) / 8.0D;
            double d2 = (this.closestPlayer.posY + (double)this.closestPlayer.getEyeHeight() / 2.0D - this.posY) / 8.0D;
            double d3 = (this.closestPlayer.posZ - this.posZ) / 8.0D;
            double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
            double d5 = 1.0D - d4;

            if (d5 > 0.0D)
            {
                d5 = d5 * d5;
                this.field_70159_w += d1 / d4 * d5 * 0.1D;
                this.field_70181_x += d2 / d4 * d5 * 0.1D;
                this.field_70179_y += d3 / d4 * d5 * 0.1D;
            }
        }

        this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
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
            this.field_70181_x *= -0.8999999761581421D;
        }

        ++this.xpColor;
        ++this.xpOrbAge;

        if (this.xpOrbAge >= 6000)
        {
            this.remove();
        }
    }

    /**
     * Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    public boolean handleWaterMovement()
    {
        return this.world.func_72918_a(this.getBoundingBox(), Material.WATER, this);
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
        else
        {
            this.markVelocityChanged();
            this.xpOrbHealth = (int)((float)this.xpOrbHealth - amount);

            if (this.xpOrbHealth <= 0)
            {
                this.remove();
            }

            return false;
        }
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        p_70014_1_.putShort("Health", (short)this.xpOrbHealth);
        p_70014_1_.putShort("Age", (short)this.xpOrbAge);
        p_70014_1_.putShort("Value", (short)this.xpValue);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        this.xpOrbHealth = compound.getShort("Health");
        this.xpOrbAge = compound.getShort("Age");
        this.xpValue = compound.getShort("Value");
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityIn)
    {
        if (!this.world.isRemote)
        {
            if (this.delayBeforeCanPickup == 0 && entityIn.xpCooldown == 0)
            {
                entityIn.xpCooldown = 2;
                entityIn.onItemPickup(this, 1);
                ItemStack itemstack = EnchantmentHelper.func_92099_a(Enchantments.MENDING, entityIn);

                if (!itemstack.isEmpty() && itemstack.isDamaged())
                {
                    int i = Math.min(this.xpToDurability(this.xpValue), itemstack.getDamage());
                    this.xpValue -= this.durabilityToXp(i);
                    itemstack.func_77964_b(itemstack.getDamage() - i);
                }

                if (this.xpValue > 0)
                {
                    entityIn.func_71023_q(this.xpValue);
                }

                this.remove();
            }
        }
    }

    private int durabilityToXp(int durability)
    {
        return durability / 2;
    }

    private int xpToDurability(int xp)
    {
        return xp * 2;
    }

    /**
     * Returns the XP value of this XP orb.
     */
    public int getXpValue()
    {
        return this.xpValue;
    }

    /**
     * Returns a number from 1 to 10 based on how much XP this orb is worth. This is used by RenderXPOrb to determine
     * what texture to use.
     */
    public int getTextureByXP()
    {
        if (this.xpValue >= 2477)
        {
            return 10;
        }
        else if (this.xpValue >= 1237)
        {
            return 9;
        }
        else if (this.xpValue >= 617)
        {
            return 8;
        }
        else if (this.xpValue >= 307)
        {
            return 7;
        }
        else if (this.xpValue >= 149)
        {
            return 6;
        }
        else if (this.xpValue >= 73)
        {
            return 5;
        }
        else if (this.xpValue >= 37)
        {
            return 4;
        }
        else if (this.xpValue >= 17)
        {
            return 3;
        }
        else if (this.xpValue >= 7)
        {
            return 2;
        }
        else
        {
            return this.xpValue >= 3 ? 1 : 0;
        }
    }

    /**
     * Get a fragment of the maximum experience points value for the supplied value of experience points value.
     */
    public static int getXPSplit(int expValue)
    {
        if (expValue >= 2477)
        {
            return 2477;
        }
        else if (expValue >= 1237)
        {
            return 1237;
        }
        else if (expValue >= 617)
        {
            return 617;
        }
        else if (expValue >= 307)
        {
            return 307;
        }
        else if (expValue >= 149)
        {
            return 149;
        }
        else if (expValue >= 73)
        {
            return 73;
        }
        else if (expValue >= 37)
        {
            return 37;
        }
        else if (expValue >= 17)
        {
            return 17;
        }
        else if (expValue >= 7)
        {
            return 7;
        }
        else
        {
            return expValue >= 3 ? 3 : 1;
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
