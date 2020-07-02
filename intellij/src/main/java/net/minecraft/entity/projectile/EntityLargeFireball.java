package net.minecraft.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityLargeFireball extends EntityFireball
{
    public int explosionPower = 1;

    public EntityLargeFireball(World p_i1767_1_)
    {
        super(p_i1767_1_);
    }

    public EntityLargeFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
    }

    public EntityLargeFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn, shooter, accelX, accelY, accelZ);
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onImpact(RayTraceResult result)
    {
        if (!this.world.isRemote)
        {
            if (result.field_72308_g != null)
            {
                result.field_72308_g.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 6.0F);
                this.applyEnchantments(this.shootingEntity, result.field_72308_g);
            }

            boolean flag = this.world.getGameRules().func_82766_b("mobGriefing");
            this.world.func_72885_a((Entity)null, this.posX, this.posY, this.posZ, (float)this.explosionPower, flag, flag);
            this.remove();
        }
    }

    public static void func_189744_a(DataFixer p_189744_0_)
    {
        EntityFireball.func_189743_a(p_189744_0_, "Fireball");
    }

    public void func_70014_b(NBTTagCompound p_70014_1_)
    {
        super.func_70014_b(p_70014_1_);
        p_70014_1_.putInt("ExplosionPower", this.explosionPower);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(NBTTagCompound compound)
    {
        super.readAdditional(compound);

        if (compound.contains("ExplosionPower", 99))
        {
            this.explosionPower = compound.getInt("ExplosionPower");
        }
    }
}