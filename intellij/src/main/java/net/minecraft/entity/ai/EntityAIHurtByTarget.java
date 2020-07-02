package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIHurtByTarget extends EntityAITarget
{
    private final boolean entityCallsForHelp;

    /** Store the previous revengeTimer value */
    private int revengeTimerOld;
    private final Class<?>[] excludedReinforcementTypes;

    public EntityAIHurtByTarget(EntityCreature p_i45885_1_, boolean p_i45885_2_, Class<?>... p_i45885_3_)
    {
        super(p_i45885_1_, true);
        this.entityCallsForHelp = p_i45885_2_;
        this.excludedReinforcementTypes = p_i45885_3_;
        this.func_75248_a(1);
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        int i = this.goalOwner.getRevengeTimer();
        EntityLivingBase entitylivingbase = this.goalOwner.getRevengeTarget();
        return i != this.revengeTimerOld && entitylivingbase != null && this.func_75296_a(entitylivingbase, false);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.goalOwner.setAttackTarget(this.goalOwner.getRevengeTarget());
        this.target = this.goalOwner.getAttackTarget();
        this.revengeTimerOld = this.goalOwner.getRevengeTimer();
        this.unseenMemoryTicks = 300;

        if (this.entityCallsForHelp)
        {
            this.alertOthers();
        }

        super.startExecuting();
    }

    protected void alertOthers()
    {
        double d0 = this.getTargetDistance();

        for (EntityCreature entitycreature : this.goalOwner.world.func_72872_a(this.goalOwner.getClass(), (new AxisAlignedBB(this.goalOwner.posX, this.goalOwner.posY, this.goalOwner.posZ, this.goalOwner.posX + 1.0D, this.goalOwner.posY + 1.0D, this.goalOwner.posZ + 1.0D)).grow(d0, 10.0D, d0)))
        {
            if (this.goalOwner != entitycreature && entitycreature.getAttackTarget() == null && (!(this.goalOwner instanceof EntityTameable) || ((EntityTameable)this.goalOwner).getOwner() == ((EntityTameable)entitycreature).getOwner()) && !entitycreature.isOnSameTeam(this.goalOwner.getRevengeTarget()))
            {
                boolean flag = false;

                for (Class<?> oclass : this.excludedReinforcementTypes)
                {
                    if (entitycreature.getClass() == oclass)
                    {
                        flag = true;
                        break;
                    }
                }

                if (!flag)
                {
                    this.func_179446_a(entitycreature, this.goalOwner.getRevengeTarget());
                }
            }
        }
    }

    protected void func_179446_a(EntityCreature p_179446_1_, EntityLivingBase p_179446_2_)
    {
        p_179446_1_.setAttackTarget(p_179446_2_);
    }
}
