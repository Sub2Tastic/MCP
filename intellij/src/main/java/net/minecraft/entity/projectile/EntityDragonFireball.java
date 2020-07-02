package net.minecraft.entity.projectile;

import java.util.List;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDragonFireball extends EntityFireball
{
    public EntityDragonFireball(World p_i46774_1_)
    {
        super(p_i46774_1_);
        this.func_70105_a(1.0F, 1.0F);
    }

    public EntityDragonFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
        this.func_70105_a(1.0F, 1.0F);
    }

    public EntityDragonFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.func_70105_a(1.0F, 1.0F);
    }

    public static void func_189747_a(DataFixer p_189747_0_)
    {
        EntityFireball.func_189743_a(p_189747_0_, "DragonFireball");
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onImpact(RayTraceResult result)
    {
        if (result.field_72308_g == null || !result.field_72308_g.isEntityEqual(this.shootingEntity))
        {
            if (!this.world.isRemote)
            {
                List<EntityLivingBase> list = this.world.<EntityLivingBase>func_72872_a(EntityLivingBase.class, this.getBoundingBox().grow(4.0D, 2.0D, 4.0D));
                EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
                entityareaeffectcloud.setOwner(this.shootingEntity);
                entityareaeffectcloud.func_184491_a(EnumParticleTypes.DRAGON_BREATH);
                entityareaeffectcloud.setRadius(3.0F);
                entityareaeffectcloud.setDuration(600);
                entityareaeffectcloud.setRadiusPerTick((7.0F - entityareaeffectcloud.getRadius()) / (float)entityareaeffectcloud.getDuration());
                entityareaeffectcloud.addEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 1));

                if (!list.isEmpty())
                {
                    for (EntityLivingBase entitylivingbase : list)
                    {
                        double d0 = this.getDistanceSq(entitylivingbase);

                        if (d0 < 16.0D)
                        {
                            entityareaeffectcloud.setPosition(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ);
                            break;
                        }
                    }
                }

                this.world.func_175718_b(2006, new BlockPos(this.posX, this.posY, this.posZ), 0);
                this.world.addEntity0(entityareaeffectcloud);
                this.remove();
            }
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

    protected EnumParticleTypes func_184563_j()
    {
        return EnumParticleTypes.DRAGON_BREATH;
    }

    protected boolean isFireballFiery()
    {
        return false;
    }
}
