package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAITradePlayer extends EntityAIBase
{
    private final EntityVillager villager;

    public EntityAITradePlayer(EntityVillager p_i1658_1_)
    {
        this.villager = p_i1658_1_;
        this.func_75248_a(5);
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        if (!this.villager.isAlive())
        {
            return false;
        }
        else if (this.villager.isInWater())
        {
            return false;
        }
        else if (!this.villager.onGround)
        {
            return false;
        }
        else if (this.villager.velocityChanged)
        {
            return false;
        }
        else
        {
            EntityPlayer entityplayer = this.villager.getCustomer();

            if (entityplayer == null)
            {
                return false;
            }
            else if (this.villager.getDistanceSq(entityplayer) > 16.0D)
            {
                return false;
            }
            else
            {
                return entityplayer.openContainer != null;
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.villager.getNavigator().clearPath();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.villager.setCustomer((EntityPlayer)null);
    }
}