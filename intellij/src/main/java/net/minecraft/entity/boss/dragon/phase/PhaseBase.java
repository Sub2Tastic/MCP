package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public abstract class PhaseBase implements IPhase
{
    protected final EntityDragon dragon;

    public PhaseBase(EntityDragon dragonIn)
    {
        this.dragon = dragonIn;
    }

    public boolean getIsStationary()
    {
        return false;
    }

    /**
     * Generates particle effects appropriate to the phase (or sometimes sounds).
     * Called by dragon's onLivingUpdate. Only used when worldObj.isRemote.
     */
    public void clientTick()
    {
    }

    /**
     * Gives the phase a chance to update its status.
     * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
     */
    public void serverTick()
    {
    }

    public void onCrystalDestroyed(EntityEnderCrystal crystal, BlockPos pos, DamageSource dmgSrc, @Nullable EntityPlayer plyr)
    {
    }

    /**
     * Called when this phase is set to active
     */
    public void initPhase()
    {
    }

    public void removeAreaEffect()
    {
    }

    /**
     * Returns the maximum amount dragon may rise or fall during this phase
     */
    public float getMaxRiseOrFall()
    {
        return 0.6F;
    }

    @Nullable

    /**
     * Returns the location the dragon is flying toward
     */
    public Vec3d getTargetLocation()
    {
        return null;
    }

    public float func_188656_a(MultiPartEntityPart p_188656_1_, DamageSource p_188656_2_, float p_188656_3_)
    {
        return p_188656_3_;
    }

    public float getYawFactor()
    {
        float f = MathHelper.sqrt(this.dragon.field_70159_w * this.dragon.field_70159_w + this.dragon.field_70179_y * this.dragon.field_70179_y) + 1.0F;
        float f1 = Math.min(f, 40.0F);
        return 0.7F / f1 / f;
    }
}
