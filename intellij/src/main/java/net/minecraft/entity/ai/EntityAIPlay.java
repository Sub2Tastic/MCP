package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.math.Vec3d;

public class EntityAIPlay extends EntityAIBase
{
    private final EntityVillager field_75262_a;
    private EntityLivingBase field_75260_b;
    private final double field_75261_c;
    private int field_75259_d;

    public EntityAIPlay(EntityVillager p_i1646_1_, double p_i1646_2_)
    {
        this.field_75262_a = p_i1646_1_;
        this.field_75261_c = p_i1646_2_;
        this.func_75248_a(1);
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        if (this.field_75262_a.getGrowingAge() >= 0)
        {
            return false;
        }
        else if (this.field_75262_a.getRNG().nextInt(400) != 0)
        {
            return false;
        }
        else
        {
            List<EntityVillager> list = this.field_75262_a.world.<EntityVillager>func_72872_a(EntityVillager.class, this.field_75262_a.getBoundingBox().grow(6.0D, 3.0D, 6.0D));
            double d0 = Double.MAX_VALUE;

            for (EntityVillager entityvillager : list)
            {
                if (entityvillager != this.field_75262_a && !entityvillager.func_70945_p() && entityvillager.getGrowingAge() < 0)
                {
                    double d1 = entityvillager.getDistanceSq(this.field_75262_a);

                    if (d1 <= d0)
                    {
                        d0 = d1;
                        this.field_75260_b = entityvillager;
                    }
                }
            }

            if (this.field_75260_b == null)
            {
                Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.field_75262_a, 16, 3);

                if (vec3d == null)
                {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return this.field_75259_d > 0;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        if (this.field_75260_b != null)
        {
            this.field_75262_a.func_70939_f(true);
        }

        this.field_75259_d = 1000;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.field_75262_a.func_70939_f(false);
        this.field_75260_b = null;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick()
    {
        --this.field_75259_d;

        if (this.field_75260_b != null)
        {
            if (this.field_75262_a.getDistanceSq(this.field_75260_b) > 4.0D)
            {
                this.field_75262_a.getNavigator().tryMoveToEntityLiving(this.field_75260_b, this.field_75261_c);
            }
        }
        else if (this.field_75262_a.getNavigator().noPath())
        {
            Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.field_75262_a, 16, 3);

            if (vec3d == null)
            {
                return;
            }

            this.field_75262_a.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, this.field_75261_c);
        }
    }
}
