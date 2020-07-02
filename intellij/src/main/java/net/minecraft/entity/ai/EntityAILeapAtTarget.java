package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class EntityAILeapAtTarget extends EntityAIBase
{
    EntityLiving leaper;
    EntityLivingBase leapTarget;
    float leapMotionY;

    public EntityAILeapAtTarget(EntityLiving leapingEntity, float leapMotionYIn)
    {
        this.leaper = leapingEntity;
        this.leapMotionY = leapMotionYIn;
        this.func_75248_a(5);
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        this.leapTarget = this.leaper.getAttackTarget();

        if (this.leapTarget == null)
        {
            return false;
        }
        else
        {
            double d0 = this.leaper.getDistanceSq(this.leapTarget);

            if (d0 >= 4.0D && d0 <= 16.0D)
            {
                if (!this.leaper.onGround)
                {
                    return false;
                }
                else
                {
                    return this.leaper.getRNG().nextInt(5) == 0;
                }
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return !this.leaper.onGround;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        double d0 = this.leapTarget.posX - this.leaper.posX;
        double d1 = this.leapTarget.posZ - this.leaper.posZ;
        float f = MathHelper.sqrt(d0 * d0 + d1 * d1);

        if ((double)f >= 1.0E-4D)
        {
            this.leaper.field_70159_w += d0 / (double)f * 0.5D * 0.800000011920929D + this.leaper.field_70159_w * 0.20000000298023224D;
            this.leaper.field_70179_y += d1 / (double)f * 0.5D * 0.800000011920929D + this.leaper.field_70179_y * 0.20000000298023224D;
        }

        this.leaper.field_70181_x = (double)this.leapMotionY;
    }
}
