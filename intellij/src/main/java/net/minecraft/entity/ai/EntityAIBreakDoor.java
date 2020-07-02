package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.EnumDifficulty;

public class EntityAIBreakDoor extends EntityAIDoorInteract
{
    private int breakingTime;
    private int previousBreakProgress = -1;

    public EntityAIBreakDoor(EntityLiving p_i1618_1_)
    {
        super(p_i1618_1_);
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        if (!super.shouldExecute())
        {
            return false;
        }
        else if (!this.entity.world.getGameRules().func_82766_b("mobGriefing"))
        {
            return false;
        }
        else
        {
            BlockDoor blockdoor = this.field_151504_e;
            return !BlockDoor.func_176514_f(this.entity.world, this.doorPosition);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        super.startExecuting();
        this.breakingTime = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        double d0 = this.entity.func_174818_b(this.doorPosition);
        boolean flag;

        if (this.breakingTime <= 240)
        {
            BlockDoor blockdoor = this.field_151504_e;

            if (!BlockDoor.func_176514_f(this.entity.world, this.doorPosition) && d0 < 4.0D)
            {
                flag = true;
                return flag;
            }
        }

        flag = false;
        return flag;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        super.resetTask();
        this.entity.world.sendBlockBreakProgress(this.entity.getEntityId(), this.doorPosition, -1);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick()
    {
        super.tick();

        if (this.entity.getRNG().nextInt(20) == 0)
        {
            this.entity.world.func_175718_b(1019, this.doorPosition, 0);
        }

        ++this.breakingTime;
        int i = (int)((float)this.breakingTime / 240.0F * 10.0F);

        if (i != this.previousBreakProgress)
        {
            this.entity.world.sendBlockBreakProgress(this.entity.getEntityId(), this.doorPosition, i);
            this.previousBreakProgress = i;
        }

        if (this.breakingTime == 240 && this.entity.world.getDifficulty() == EnumDifficulty.HARD)
        {
            this.entity.world.func_175698_g(this.doorPosition);
            this.entity.world.func_175718_b(1021, this.doorPosition, 0);
            this.entity.world.func_175718_b(2001, this.doorPosition, Block.func_149682_b(this.field_151504_e));
        }
    }
}
