package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityEnderEye extends Entity
{
    private double targetX;
    private double targetY;
    private double targetZ;
    private int despawnTimer;
    private boolean shatterOrDrop;

    public EntityEnderEye(World p_i1757_1_)
    {
        super(p_i1757_1_);
        this.func_70105_a(0.25F, 0.25F);
    }

    protected void registerData()
    {
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getBoundingBox().getAverageEdgeLength() * 4.0D;

        if (Double.isNaN(d0))
        {
            d0 = 4.0D;
        }

        d0 = d0 * 64.0D;
        return distance < d0 * d0;
    }

    public EntityEnderEye(World worldIn, double x, double y, double z)
    {
        super(worldIn);
        this.despawnTimer = 0;
        this.func_70105_a(0.25F, 0.25F);
        this.setPosition(x, y, z);
    }

    public void moveTowards(BlockPos pos)
    {
        double d0 = (double)pos.getX();
        int i = pos.getY();
        double d1 = (double)pos.getZ();
        double d2 = d0 - this.posX;
        double d3 = d1 - this.posZ;
        float f = MathHelper.sqrt(d2 * d2 + d3 * d3);

        if (f > 12.0F)
        {
            this.targetX = this.posX + d2 / (double)f * 12.0D;
            this.targetZ = this.posZ + d3 / (double)f * 12.0D;
            this.targetY = this.posY + 8.0D;
        }
        else
        {
            this.targetX = d0;
            this.targetY = (double)i;
            this.targetZ = d1;
        }

        this.despawnTimer = 0;
        this.shatterOrDrop = this.rand.nextInt(5) > 0;
    }

    /**
     * Updates the entity motion clientside, called by packets from the server
     */
    public void setVelocity(double x, double y, double z)
    {
        this.field_70159_w = x;
        this.field_70181_x = y;
        this.field_70179_y = z;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(x * x + z * z);
            this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
            this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * (180D / Math.PI));
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.tick();
        this.posX += this.field_70159_w;
        this.posY += this.field_70181_x;
        this.posZ += this.field_70179_y;
        float f = MathHelper.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
        this.rotationYaw = (float)(MathHelper.atan2(this.field_70159_w, this.field_70179_y) * (180D / Math.PI));

        for (this.rotationPitch = (float)(MathHelper.atan2(this.field_70181_x, (double)f) * (180D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

        if (!this.world.isRemote)
        {
            double d0 = this.targetX - this.posX;
            double d1 = this.targetZ - this.posZ;
            float f1 = (float)Math.sqrt(d0 * d0 + d1 * d1);
            float f2 = (float)MathHelper.atan2(d1, d0);
            double d2 = (double)f + (double)(f1 - f) * 0.0025D;

            if (f1 < 1.0F)
            {
                d2 *= 0.8D;
                this.field_70181_x *= 0.8D;
            }

            this.field_70159_w = Math.cos((double)f2) * d2;
            this.field_70179_y = Math.sin((double)f2) * d2;

            if (this.posY < this.targetY)
            {
                this.field_70181_x += (1.0D - this.field_70181_x) * 0.014999999664723873D;
            }
            else
            {
                this.field_70181_x += (-1.0D - this.field_70181_x) * 0.014999999664723873D;
            }
        }

        float f3 = 0.25F;

        if (this.isInWater())
        {
            for (int i = 0; i < 4; ++i)
            {
                this.world.func_175688_a(EnumParticleTypes.WATER_BUBBLE, this.posX - this.field_70159_w * 0.25D, this.posY - this.field_70181_x * 0.25D, this.posZ - this.field_70179_y * 0.25D, this.field_70159_w, this.field_70181_x, this.field_70179_y);
            }
        }
        else
        {
            this.world.func_175688_a(EnumParticleTypes.PORTAL, this.posX - this.field_70159_w * 0.25D + this.rand.nextDouble() * 0.6D - 0.3D, this.posY - this.field_70181_x * 0.25D - 0.5D, this.posZ - this.field_70179_y * 0.25D + this.rand.nextDouble() * 0.6D - 0.3D, this.field_70159_w, this.field_70181_x, this.field_70179_y);
        }

        if (!this.world.isRemote)
        {
            this.setPosition(this.posX, this.posY, this.posZ);
            ++this.despawnTimer;

            if (this.despawnTimer > 80 && !this.world.isRemote)
            {
                this.playSound(SoundEvents.ENTITY_ENDER_EYE_DEATH, 1.0F, 1.0F);
                this.remove();

                if (this.shatterOrDrop)
                {
                    this.world.addEntity0(new EntityItem(this.world, this.posX, this.posY, this.posZ, new ItemStack(Items.ENDER_EYE)));
                }
                else
                {
                    this.world.func_175718_b(2003, new BlockPos(this), 0);
                }
            }
        }
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness()
    {
        return 1.0F;
    }

    public int func_70070_b()
    {
        return 15728880;
    }

    /**
     * Returns true if it's possible to attack this entity with an item.
     */
    public boolean canBeAttackedWithItem()
    {
        return false;
    }
}
