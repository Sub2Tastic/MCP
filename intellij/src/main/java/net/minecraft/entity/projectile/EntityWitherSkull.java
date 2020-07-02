package net.minecraft.entity.projectile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.init.MobEffects;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityWitherSkull extends EntityFireball
{
    private static final DataParameter<Boolean> INVULNERABLE = EntityDataManager.<Boolean>createKey(EntityWitherSkull.class, DataSerializers.BOOLEAN);

    public EntityWitherSkull(World p_i1793_1_)
    {
        super(p_i1793_1_);
        this.func_70105_a(0.3125F, 0.3125F);
    }

    public EntityWitherSkull(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.func_70105_a(0.3125F, 0.3125F);
    }

    public static void func_189746_a(DataFixer p_189746_0_)
    {
        EntityFireball.func_189743_a(p_189746_0_, "WitherSkull");
    }

    /**
     * Return the motion factor for this projectile. The factor is multiplied by the original motion.
     */
    protected float getMotionFactor()
    {
        return this.isSkullInvulnerable() ? 0.73F : super.getMotionFactor();
    }

    public EntityWitherSkull(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
        this.func_70105_a(0.3125F, 0.3125F);
    }

    /**
     * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     */
    public boolean isBurning()
    {
        return false;
    }

    /**
     * Explosion resistance of a block relative to this entity
     */
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn)
    {
        float f = super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn);
        Block block = blockStateIn.getBlock();

        if (this.isSkullInvulnerable() && EntityWither.canDestroyBlock(block))
        {
            f = Math.min(0.8F, f);
        }

        return f;
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
                if (this.shootingEntity != null)
                {
                    if (result.field_72308_g.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 8.0F))
                    {
                        if (result.field_72308_g.isAlive())
                        {
                            this.applyEnchantments(this.shootingEntity, result.field_72308_g);
                        }
                        else
                        {
                            this.shootingEntity.heal(5.0F);
                        }
                    }
                }
                else
                {
                    result.field_72308_g.attackEntityFrom(DamageSource.MAGIC, 5.0F);
                }

                if (result.field_72308_g instanceof EntityLivingBase)
                {
                    int i = 0;

                    if (this.world.getDifficulty() == EnumDifficulty.NORMAL)
                    {
                        i = 10;
                    }
                    else if (this.world.getDifficulty() == EnumDifficulty.HARD)
                    {
                        i = 40;
                    }

                    if (i > 0)
                    {
                        ((EntityLivingBase)result.field_72308_g).func_70690_d(new PotionEffect(MobEffects.WITHER, 20 * i, 1));
                    }
                }
            }

            this.world.func_72885_a(this, this.posX, this.posY, this.posZ, 1.0F, false, this.world.getGameRules().func_82766_b("mobGriefing"));
            this.remove();
        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return false;
    }

    protected void registerData()
    {
        this.dataManager.register(INVULNERABLE, Boolean.valueOf(false));
    }

    /**
     * Return whether this skull comes from an invulnerable (aura) wither boss.
     */
    public boolean isSkullInvulnerable()
    {
        return ((Boolean)this.dataManager.get(INVULNERABLE)).booleanValue();
    }

    /**
     * Set whether this skull comes from an invulnerable (aura) wither boss.
     */
    public void setSkullInvulnerable(boolean invulnerable)
    {
        this.dataManager.set(INVULNERABLE, Boolean.valueOf(invulnerable));
    }

    protected boolean isFireballFiery()
    {
        return false;
    }
}
