package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.village.Village;

public class EntityAIDefendVillage extends EntityAITarget
{
    EntityIronGolem irongolem;
    EntityLivingBase villageAgressorTarget;

    public EntityAIDefendVillage(EntityIronGolem ironGolemIn)
    {
        super(ironGolemIn, false, true);
        this.irongolem = ironGolemIn;
        this.func_75248_a(1);
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        Village village = this.irongolem.func_70852_n();

        if (village == null)
        {
            return false;
        }
        else
        {
            this.villageAgressorTarget = village.func_75571_b(this.irongolem);

            if (this.villageAgressorTarget instanceof EntityCreeper)
            {
                return false;
            }
            else if (this.func_75296_a(this.villageAgressorTarget, false))
            {
                return true;
            }
            else if (this.goalOwner.getRNG().nextInt(20) == 0)
            {
                this.villageAgressorTarget = village.func_82685_c(this.irongolem);
                return this.func_75296_a(this.villageAgressorTarget, false);
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.irongolem.setAttackTarget(this.villageAgressorTarget);
        super.startExecuting();
    }
}
