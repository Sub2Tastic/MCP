package net.minecraft.entity.item;

import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityEnderPearl extends EntityThrowable
{
    private EntityLivingBase perlThrower;

    public EntityEnderPearl(World p_i46455_1_)
    {
        super(p_i46455_1_);
    }

    public EntityEnderPearl(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
        this.perlThrower = throwerIn;
    }

    public EntityEnderPearl(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public static void func_189663_a(DataFixer p_189663_0_)
    {
        EntityThrowable.func_189661_a(p_189663_0_, "ThrownEnderpearl");
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(RayTraceResult result)
    {
        EntityLivingBase entitylivingbase = this.getThrower();

        if (result.field_72308_g != null)
        {
            if (result.field_72308_g == this.perlThrower)
            {
                return;
            }

            result.field_72308_g.attackEntityFrom(DamageSource.causeThrownDamage(this, entitylivingbase), 0.0F);
        }

        if (result.field_72313_a == RayTraceResult.Type.BLOCK)
        {
            BlockPos blockpos = result.func_178782_a();
            TileEntity tileentity = this.world.getTileEntity(blockpos);

            if (tileentity instanceof TileEntityEndGateway)
            {
                TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway)tileentity;

                if (entitylivingbase != null)
                {
                    if (entitylivingbase instanceof EntityPlayerMP)
                    {
                        CriteriaTriggers.ENTER_BLOCK.trigger((EntityPlayerMP)entitylivingbase, this.world.getBlockState(blockpos));
                    }

                    tileentityendgateway.func_184306_a(entitylivingbase);
                    this.remove();
                    return;
                }

                tileentityendgateway.func_184306_a(this);
                return;
            }
        }

        for (int i = 0; i < 32; ++i)
        {
            this.world.func_175688_a(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian());
        }

        if (!this.world.isRemote)
        {
            if (entitylivingbase instanceof EntityPlayerMP)
            {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)entitylivingbase;

                if (entityplayermp.connection.func_147362_b().isChannelOpen() && entityplayermp.world == this.world && !entityplayermp.isSleeping())
                {
                    if (this.rand.nextFloat() < 0.05F && this.world.getGameRules().func_82766_b("doMobSpawning"))
                    {
                        EntityEndermite entityendermite = new EntityEndermite(this.world);
                        entityendermite.setSpawnedByPlayer(true);
                        entityendermite.setLocationAndAngles(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, entitylivingbase.rotationYaw, entitylivingbase.rotationPitch);
                        this.world.addEntity0(entityendermite);
                    }

                    if (entitylivingbase.isPassenger())
                    {
                        entitylivingbase.stopRiding();
                    }

                    entitylivingbase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                    entitylivingbase.fallDistance = 0.0F;
                    entitylivingbase.attackEntityFrom(DamageSource.FALL, 5.0F);
                }
            }
            else if (entitylivingbase != null)
            {
                entitylivingbase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                entitylivingbase.fallDistance = 0.0F;
            }

            this.remove();
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        EntityLivingBase entitylivingbase = this.getThrower();

        if (entitylivingbase != null && entitylivingbase instanceof EntityPlayer && !entitylivingbase.isAlive())
        {
            this.remove();
        }
        else
        {
            super.tick();
        }
    }

    @Nullable
    public Entity func_184204_a(int p_184204_1_)
    {
        if (this.owner.dimension != p_184204_1_)
        {
            this.owner = null;
        }

        return super.func_184204_a(p_184204_1_);
    }
}
