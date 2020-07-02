package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySmallFireball extends EntityFireball
{
    public EntitySmallFireball(World p_i1770_1_)
    {
        super(p_i1770_1_);
        this.func_70105_a(0.3125F, 0.3125F);
    }

    public EntitySmallFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.func_70105_a(0.3125F, 0.3125F);
    }

    public EntitySmallFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
        this.func_70105_a(0.3125F, 0.3125F);
    }

    public static void func_189745_a(DataFixer p_189745_0_)
    {
        EntityFireball.func_189743_a(p_189745_0_, "SmallFireball");
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
                if (!result.field_72308_g.isImmuneToFire())
                {
                    boolean flag = result.field_72308_g.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 5.0F);

                    if (flag)
                    {
                        this.applyEnchantments(this.shootingEntity, result.field_72308_g);
                        result.field_72308_g.setFire(5);
                    }
                }
            }
            else
            {
                boolean flag1 = true;

                if (this.shootingEntity != null && this.shootingEntity instanceof EntityLiving)
                {
                    flag1 = this.world.getGameRules().func_82766_b("mobGriefing");
                }

                if (flag1)
                {
                    BlockPos blockpos = result.func_178782_a().offset(result.field_178784_b);

                    if (this.world.isAirBlock(blockpos))
                    {
                        this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
                    }
                }
            }

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
}
