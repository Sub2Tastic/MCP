package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIFollowGolem extends EntityAIBase
{
    private final EntityVillager field_75294_a;
    private EntityIronGolem field_75292_b;
    private int field_75293_c;
    private boolean field_75291_d;

    public EntityAIFollowGolem(EntityVillager p_i1656_1_)
    {
        this.field_75294_a = p_i1656_1_;
        this.func_75248_a(3);
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        if (this.field_75294_a.getGrowingAge() >= 0)
        {
            return false;
        }
        else if (!this.field_75294_a.world.isDaytime())
        {
            return false;
        }
        else
        {
            List<EntityIronGolem> list = this.field_75294_a.world.<EntityIronGolem>func_72872_a(EntityIronGolem.class, this.field_75294_a.getBoundingBox().grow(6.0D, 2.0D, 6.0D));

            if (list.isEmpty())
            {
                return false;
            }
            else
            {
                for (EntityIronGolem entityirongolem : list)
                {
                    if (entityirongolem.getHoldRoseTick() > 0)
                    {
                        this.field_75292_b = entityirongolem;
                        break;
                    }
                }

                return this.field_75292_b != null;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return this.field_75292_b.getHoldRoseTick() > 0;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.field_75293_c = this.field_75294_a.getRNG().nextInt(320);
        this.field_75291_d = false;
        this.field_75292_b.getNavigator().clearPath();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.field_75292_b = null;
        this.field_75294_a.getNavigator().clearPath();
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick()
    {
        this.field_75294_a.getLookController().setLookPositionWithEntity(this.field_75292_b, 30.0F, 30.0F);

        if (this.field_75292_b.getHoldRoseTick() == this.field_75293_c)
        {
            this.field_75294_a.getNavigator().tryMoveToEntityLiving(this.field_75292_b, 0.5D);
            this.field_75291_d = true;
        }

        if (this.field_75291_d && this.field_75294_a.getDistanceSq(this.field_75292_b) < 4.0D)
        {
            this.field_75292_b.setHoldingRose(false);
            this.field_75294_a.getNavigator().clearPath();
        }
    }
}
