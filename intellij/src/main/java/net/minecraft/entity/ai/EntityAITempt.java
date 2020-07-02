package net.minecraft.entity.ai;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;

public class EntityAITempt extends EntityAIBase
{
    private final EntityCreature creature;
    private final double speed;
    private double targetX;
    private double targetY;
    private double targetZ;
    private double pitch;
    private double yaw;
    private EntityPlayer closestPlayer;
    private int delayTemptCounter;
    private boolean isRunning;
    private final Set<Item> temptItem;
    private final boolean scaredByPlayerMovement;

    public EntityAITempt(EntityCreature p_i45316_1_, double p_i45316_2_, Item p_i45316_4_, boolean p_i45316_5_)
    {
        this(p_i45316_1_, p_i45316_2_, p_i45316_5_, Sets.newHashSet(p_i45316_4_));
    }

    public EntityAITempt(EntityCreature p_i46804_1_, double p_i46804_2_, boolean p_i46804_4_, Set<Item> p_i46804_5_)
    {
        this.creature = p_i46804_1_;
        this.speed = p_i46804_2_;
        this.temptItem = p_i46804_5_;
        this.scaredByPlayerMovement = p_i46804_4_;
        this.func_75248_a(3);

        if (!(p_i46804_1_.getNavigator() instanceof PathNavigateGround))
        {
            throw new IllegalArgumentException("Unsupported mob type for TemptGoal");
        }
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        if (this.delayTemptCounter > 0)
        {
            --this.delayTemptCounter;
            return false;
        }
        else
        {
            this.closestPlayer = this.creature.world.func_72890_a(this.creature, 10.0D);

            if (this.closestPlayer == null)
            {
                return false;
            }
            else
            {
                return this.isTempting(this.closestPlayer.getHeldItemMainhand()) || this.isTempting(this.closestPlayer.getHeldItemOffhand());
            }
        }
    }

    protected boolean isTempting(ItemStack stack)
    {
        return this.temptItem.contains(stack.getItem());
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        if (this.scaredByPlayerMovement)
        {
            if (this.creature.getDistanceSq(this.closestPlayer) < 36.0D)
            {
                if (this.closestPlayer.getDistanceSq(this.targetX, this.targetY, this.targetZ) > 0.010000000000000002D)
                {
                    return false;
                }

                if (Math.abs((double)this.closestPlayer.rotationPitch - this.pitch) > 5.0D || Math.abs((double)this.closestPlayer.rotationYaw - this.yaw) > 5.0D)
                {
                    return false;
                }
            }
            else
            {
                this.targetX = this.closestPlayer.posX;
                this.targetY = this.closestPlayer.posY;
                this.targetZ = this.closestPlayer.posZ;
            }

            this.pitch = (double)this.closestPlayer.rotationPitch;
            this.yaw = (double)this.closestPlayer.rotationYaw;
        }

        return this.shouldExecute();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.targetX = this.closestPlayer.posX;
        this.targetY = this.closestPlayer.posY;
        this.targetZ = this.closestPlayer.posZ;
        this.isRunning = true;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.closestPlayer = null;
        this.creature.getNavigator().clearPath();
        this.delayTemptCounter = 100;
        this.isRunning = false;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick()
    {
        this.creature.getLookController().setLookPositionWithEntity(this.closestPlayer, (float)(this.creature.getHorizontalFaceSpeed() + 20), (float)this.creature.getVerticalFaceSpeed());

        if (this.creature.getDistanceSq(this.closestPlayer) < 6.25D)
        {
            this.creature.getNavigator().clearPath();
        }
        else
        {
            this.creature.getNavigator().tryMoveToEntityLiving(this.closestPlayer, this.speed);
        }
    }

    /**
     * @see #isRunning
     */
    public boolean isRunning()
    {
        return this.isRunning;
    }
}
