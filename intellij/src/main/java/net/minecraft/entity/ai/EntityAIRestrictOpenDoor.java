package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIRestrictOpenDoor extends EntityAIBase
{
    private final EntityCreature field_75275_a;
    private VillageDoorInfo field_75274_b;

    public EntityAIRestrictOpenDoor(EntityCreature p_i1651_1_)
    {
        this.field_75275_a = p_i1651_1_;

        if (!(p_i1651_1_.getNavigator() instanceof PathNavigateGround))
        {
            throw new IllegalArgumentException("Unsupported mob type for RestrictOpenDoorGoal");
        }
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        if (this.field_75275_a.world.isDaytime())
        {
            return false;
        }
        else
        {
            BlockPos blockpos = new BlockPos(this.field_75275_a);
            Village village = this.field_75275_a.world.func_175714_ae().func_176056_a(blockpos, 16);

            if (village == null)
            {
                return false;
            }
            else
            {
                this.field_75274_b = village.func_179865_b(blockpos);

                if (this.field_75274_b == null)
                {
                    return false;
                }
                else
                {
                    return (double)this.field_75274_b.func_179846_b(blockpos) < 2.25D;
                }
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        if (this.field_75275_a.world.isDaytime())
        {
            return false;
        }
        else
        {
            return !this.field_75274_b.func_179851_i() && this.field_75274_b.func_179850_c(new BlockPos(this.field_75275_a));
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        ((PathNavigateGround)this.field_75275_a.getNavigator()).setBreakDoors(false);
        ((PathNavigateGround)this.field_75275_a.getNavigator()).func_179691_c(false);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        ((PathNavigateGround)this.field_75275_a.getNavigator()).setBreakDoors(true);
        ((PathNavigateGround)this.field_75275_a.getNavigator()).func_179691_c(true);
        this.field_75274_b = null;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick()
    {
        this.field_75274_b.func_75470_e();
    }
}
