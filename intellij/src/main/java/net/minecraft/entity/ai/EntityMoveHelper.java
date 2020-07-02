package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.MathHelper;

public class EntityMoveHelper
{
    protected final EntityLiving mob;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected double speed;
    protected float moveForward;
    protected float moveStrafe;
    public EntityMoveHelper.Action action = EntityMoveHelper.Action.WAIT;

    public EntityMoveHelper(EntityLiving mob)
    {
        this.mob = mob;
    }

    public boolean isUpdating()
    {
        return this.action == EntityMoveHelper.Action.MOVE_TO;
    }

    public double getSpeed()
    {
        return this.speed;
    }

    /**
     * Sets the speed and location to move to
     */
    public void setMoveTo(double x, double y, double z, double speedIn)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.speed = speedIn;
        this.action = EntityMoveHelper.Action.MOVE_TO;
    }

    public void strafe(float forward, float strafe)
    {
        this.action = EntityMoveHelper.Action.STRAFE;
        this.moveForward = forward;
        this.moveStrafe = strafe;
        this.speed = 0.25D;
    }

    public void func_188487_a(EntityMoveHelper p_188487_1_)
    {
        this.action = p_188487_1_.action;
        this.posX = p_188487_1_.posX;
        this.posY = p_188487_1_.posY;
        this.posZ = p_188487_1_.posZ;
        this.speed = Math.max(p_188487_1_.speed, 1.0D);
        this.moveForward = p_188487_1_.moveForward;
        this.moveStrafe = p_188487_1_.moveStrafe;
    }

    public void tick()
    {
        if (this.action == EntityMoveHelper.Action.STRAFE)
        {
            float f = (float)this.mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
            float f1 = (float)this.speed * f;
            float f2 = this.moveForward;
            float f3 = this.moveStrafe;
            float f4 = MathHelper.sqrt(f2 * f2 + f3 * f3);

            if (f4 < 1.0F)
            {
                f4 = 1.0F;
            }

            f4 = f1 / f4;
            f2 = f2 * f4;
            f3 = f3 * f4;
            float f5 = MathHelper.sin(this.mob.rotationYaw * 0.017453292F);
            float f6 = MathHelper.cos(this.mob.rotationYaw * 0.017453292F);
            float f7 = f2 * f6 - f3 * f5;
            float f8 = f3 * f6 + f2 * f5;
            PathNavigate pathnavigate = this.mob.getNavigator();

            if (pathnavigate != null)
            {
                NodeProcessor nodeprocessor = pathnavigate.getNodeProcessor();

                if (nodeprocessor != null && nodeprocessor.getPathNodeType(this.mob.world, MathHelper.floor(this.mob.posX + (double)f7), MathHelper.floor(this.mob.posY), MathHelper.floor(this.mob.posZ + (double)f8)) != PathNodeType.WALKABLE)
                {
                    this.moveForward = 1.0F;
                    this.moveStrafe = 0.0F;
                    f1 = f;
                }
            }

            this.mob.setAIMoveSpeed(f1);
            this.mob.setMoveForward(this.moveForward);
            this.mob.setMoveStrafing(this.moveStrafe);
            this.action = EntityMoveHelper.Action.WAIT;
        }
        else if (this.action == EntityMoveHelper.Action.MOVE_TO)
        {
            this.action = EntityMoveHelper.Action.WAIT;
            double d0 = this.posX - this.mob.posX;
            double d1 = this.posZ - this.mob.posZ;
            double d2 = this.posY - this.mob.posY;
            double d3 = d0 * d0 + d2 * d2 + d1 * d1;

            if (d3 < 2.500000277905201E-7D)
            {
                this.mob.setMoveForward(0.0F);
                return;
            }

            float f9 = (float)(MathHelper.atan2(d1, d0) * (180D / Math.PI)) - 90.0F;
            this.mob.rotationYaw = this.limitAngle(this.mob.rotationYaw, f9, 90.0F);
            this.mob.setAIMoveSpeed((float)(this.speed * this.mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));

            if (d2 > (double)this.mob.stepHeight && d0 * d0 + d1 * d1 < (double)Math.max(1.0F, this.mob.field_70130_N))
            {
                this.mob.getJumpController().setJumping();
                this.action = EntityMoveHelper.Action.JUMPING;
            }
        }
        else if (this.action == EntityMoveHelper.Action.JUMPING)
        {
            this.mob.setAIMoveSpeed((float)(this.speed * this.mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));

            if (this.mob.onGround)
            {
                this.action = EntityMoveHelper.Action.WAIT;
            }
        }
        else
        {
            this.mob.setMoveForward(0.0F);
        }
    }

    /**
     * Attempt to rotate the first angle to become the second angle, but only allow overall direction change to at max
     * be third parameter
     */
    protected float limitAngle(float sourceAngle, float targetAngle, float maximumChange)
    {
        float f = MathHelper.wrapDegrees(targetAngle - sourceAngle);

        if (f > maximumChange)
        {
            f = maximumChange;
        }

        if (f < -maximumChange)
        {
            f = -maximumChange;
        }

        float f1 = sourceAngle + f;

        if (f1 < 0.0F)
        {
            f1 += 360.0F;
        }
        else if (f1 > 360.0F)
        {
            f1 -= 360.0F;
        }

        return f1;
    }

    public double getX()
    {
        return this.posX;
    }

    public double getY()
    {
        return this.posY;
    }

    public double getZ()
    {
        return this.posZ;
    }

    public static enum Action
    {
        WAIT,
        MOVE_TO,
        STRAFE,
        JUMPING;
    }
}
