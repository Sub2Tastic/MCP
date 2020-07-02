package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class PathNavigateClimber extends PathNavigateGround
{
    /** Current path navigation target */
    private BlockPos targetPosition;

    public PathNavigateClimber(EntityLiving entityLivingIn, World worldIn)
    {
        super(entityLivingIn, worldIn);
    }

    /**
     * Returns path to given BlockPos
     */
    public Path getPathToPos(BlockPos pos)
    {
        this.targetPosition = pos;
        return super.getPathToPos(pos);
    }

    /**
     * Returns the path to the given EntityLiving. Args : entity
     */
    public Path getPathToEntity(Entity entityIn)
    {
        this.targetPosition = new BlockPos(entityIn);
        return super.getPathToEntity(entityIn);
    }

    /**
     * Try to find and set a path to EntityLiving. Returns true if successful. Args : entity, speed
     */
    public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn)
    {
        Path path = this.getPathToEntity(entityIn);

        if (path != null)
        {
            return this.setPath(path, speedIn);
        }
        else
        {
            this.targetPosition = new BlockPos(entityIn);
            this.speed = speedIn;
            return true;
        }
    }

    public void tick()
    {
        if (!this.noPath())
        {
            super.tick();
        }
        else
        {
            if (this.targetPosition != null)
            {
                double d0 = (double)(this.entity.field_70130_N * this.entity.field_70130_N);

                if (this.entity.func_174831_c(this.targetPosition) >= d0 && (this.entity.posY <= (double)this.targetPosition.getY() || this.entity.func_174831_c(new BlockPos(this.targetPosition.getX(), MathHelper.floor(this.entity.posY), this.targetPosition.getZ())) >= d0))
                {
                    this.entity.getMoveHelper().setMoveTo((double)this.targetPosition.getX(), (double)this.targetPosition.getY(), (double)this.targetPosition.getZ(), this.speed);
                }
                else
                {
                    this.targetPosition = null;
                }
            }
        }
    }
}
