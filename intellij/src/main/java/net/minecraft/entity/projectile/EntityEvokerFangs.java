package net.minecraft.entity.projectile;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityEvokerFangs extends Entity
{
    private int warmupDelayTicks;
    private boolean sentSpikeEvent;
    private int lifeTicks;
    private boolean clientSideAttackStarted;
    private EntityLivingBase caster;
    private UUID casterUuid;

    public EntityEvokerFangs(World p_i47275_1_)
    {
        super(p_i47275_1_);
        this.lifeTicks = 22;
        this.func_70105_a(0.5F, 0.8F);
    }

    public EntityEvokerFangs(World worldIn, double x, double y, double z, float p_i47276_8_, int p_i47276_9_, EntityLivingBase casterIn)
    {
        this(worldIn);
        this.warmupDelayTicks = p_i47276_9_;
        this.setCaster(casterIn);
        this.rotationYaw = p_i47276_8_ * (180F / (float)Math.PI);
        this.setPosition(x, y, z);
    }

    protected void registerData()
    {
    }

    public void setCaster(@Nullable EntityLivingBase p_190549_1_)
    {
        this.caster = p_190549_1_;
        this.casterUuid = p_190549_1_ == null ? null : p_190549_1_.getUniqueID();
    }

    @Nullable
    public EntityLivingBase getCaster()
    {
        if (this.caster == null && this.casterUuid != null && this.world instanceof WorldServer)
        {
            Entity entity = ((WorldServer)this.world).func_175733_a(this.casterUuid);

            if (entity instanceof EntityLivingBase)
            {
                this.caster = (EntityLivingBase)entity;
            }
        }

        return this.caster;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditional(NBTTagCompound compound)
    {
        this.warmupDelayTicks = compound.getInt("Warmup");
        this.casterUuid = compound.getUniqueId("OwnerUUID");
    }

    protected void func_70014_b(NBTTagCompound p_70014_1_)
    {
        p_70014_1_.putInt("Warmup", this.warmupDelayTicks);

        if (this.casterUuid != null)
        {
            p_70014_1_.putUniqueId("OwnerUUID", this.casterUuid);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        super.tick();

        if (this.world.isRemote)
        {
            if (this.clientSideAttackStarted)
            {
                --this.lifeTicks;

                if (this.lifeTicks == 14)
                {
                    for (int i = 0; i < 12; ++i)
                    {
                        double d0 = this.posX + (this.rand.nextDouble() * 2.0D - 1.0D) * (double)this.field_70130_N * 0.5D;
                        double d1 = this.posY + 0.05D + this.rand.nextDouble() * 1.0D;
                        double d2 = this.posZ + (this.rand.nextDouble() * 2.0D - 1.0D) * (double)this.field_70130_N * 0.5D;
                        double d3 = (this.rand.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        double d4 = 0.3D + this.rand.nextDouble() * 0.3D;
                        double d5 = (this.rand.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        this.world.func_175688_a(EnumParticleTypes.CRIT, d0, d1 + 1.0D, d2, d3, d4, d5);
                    }
                }
            }
        }
        else if (--this.warmupDelayTicks < 0)
        {
            if (this.warmupDelayTicks == -8)
            {
                for (EntityLivingBase entitylivingbase : this.world.func_72872_a(EntityLivingBase.class, this.getBoundingBox().grow(0.2D, 0.0D, 0.2D)))
                {
                    this.damage(entitylivingbase);
                }
            }

            if (!this.sentSpikeEvent)
            {
                this.world.setEntityState(this, (byte)4);
                this.sentSpikeEvent = true;
            }

            if (--this.lifeTicks < 0)
            {
                this.remove();
            }
        }
    }

    private void damage(EntityLivingBase p_190551_1_)
    {
        EntityLivingBase entitylivingbase = this.getCaster();

        if (p_190551_1_.isAlive() && !p_190551_1_.isInvulnerable() && p_190551_1_ != entitylivingbase)
        {
            if (entitylivingbase == null)
            {
                p_190551_1_.attackEntityFrom(DamageSource.MAGIC, 6.0F);
            }
            else
            {
                if (entitylivingbase.isOnSameTeam(p_190551_1_))
                {
                    return;
                }

                p_190551_1_.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, entitylivingbase), 6.0F);
            }
        }
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    public void handleStatusUpdate(byte id)
    {
        super.handleStatusUpdate(id);

        if (id == 4)
        {
            this.clientSideAttackStarted = true;

            if (!this.isSilent())
            {
                this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_EVOKER_FANGS_ATTACK, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.2F + 0.85F, false);
            }
        }
    }

    public float getAnimationProgress(float partialTicks)
    {
        if (!this.clientSideAttackStarted)
        {
            return 0.0F;
        }
        else
        {
            int i = this.lifeTicks - 2;
            return i <= 0 ? 1.0F : 1.0F - ((float)i - partialTicks) / 20.0F;
        }
    }
}
