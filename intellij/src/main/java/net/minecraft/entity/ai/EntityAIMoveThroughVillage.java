package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIMoveThroughVillage extends EntityAIBase
{
    private final EntityCreature entity;
    private final double movementSpeed;
    private Path path;
    private VillageDoorInfo field_75416_d;
    private final boolean isNocturnal;
    private final List<VillageDoorInfo> doorList = Lists.<VillageDoorInfo>newArrayList();

    public EntityAIMoveThroughVillage(EntityCreature p_i1638_1_, double p_i1638_2_, boolean p_i1638_4_)
    {
        this.entity = p_i1638_1_;
        this.movementSpeed = p_i1638_2_;
        this.isNocturnal = p_i1638_4_;
        this.func_75248_a(1);

        if (!(p_i1638_1_.getNavigator() instanceof PathNavigateGround))
        {
            throw new IllegalArgumentException("Unsupported mob for MoveThroughVillageGoal");
        }
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        this.resizeDoorList();

        if (this.isNocturnal && this.entity.world.isDaytime())
        {
            return false;
        }
        else
        {
            Village village = this.entity.world.func_175714_ae().func_176056_a(new BlockPos(this.entity), 0);

            if (village == null)
            {
                return false;
            }
            else
            {
                this.field_75416_d = this.func_75412_a(village);

                if (this.field_75416_d == null)
                {
                    return false;
                }
                else
                {
                    PathNavigateGround pathnavigateground = (PathNavigateGround)this.entity.getNavigator();
                    boolean flag = pathnavigateground.getEnterDoors();
                    pathnavigateground.setBreakDoors(false);
                    this.path = pathnavigateground.getPathToPos(this.field_75416_d.func_179852_d());
                    pathnavigateground.setBreakDoors(flag);

                    if (this.path != null)
                    {
                        return true;
                    }
                    else
                    {
                        Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.entity, 10, 7, new Vec3d((double)this.field_75416_d.func_179852_d().getX(), (double)this.field_75416_d.func_179852_d().getY(), (double)this.field_75416_d.func_179852_d().getZ()));

                        if (vec3d == null)
                        {
                            return false;
                        }
                        else
                        {
                            pathnavigateground.setBreakDoors(false);
                            this.path = this.entity.getNavigator().func_75488_a(vec3d.x, vec3d.y, vec3d.z);
                            pathnavigateground.setBreakDoors(flag);
                            return this.path != null;
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        if (this.entity.getNavigator().noPath())
        {
            return false;
        }
        else
        {
            float f = this.entity.field_70130_N + 4.0F;
            return this.entity.func_174818_b(this.field_75416_d.func_179852_d()) > (double)(f * f);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.entity.getNavigator().setPath(this.path, this.movementSpeed);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        if (this.entity.getNavigator().noPath() || this.entity.func_174818_b(this.field_75416_d.func_179852_d()) < 16.0D)
        {
            this.doorList.add(this.field_75416_d);
        }
    }

    private VillageDoorInfo func_75412_a(Village p_75412_1_)
    {
        VillageDoorInfo villagedoorinfo = null;
        int i = Integer.MAX_VALUE;

        for (VillageDoorInfo villagedoorinfo1 : p_75412_1_.func_75558_f())
        {
            int j = villagedoorinfo1.func_75474_b(MathHelper.floor(this.entity.posX), MathHelper.floor(this.entity.posY), MathHelper.floor(this.entity.posZ));

            if (j < i && !this.func_75413_a(villagedoorinfo1))
            {
                villagedoorinfo = villagedoorinfo1;
                i = j;
            }
        }

        return villagedoorinfo;
    }

    private boolean func_75413_a(VillageDoorInfo p_75413_1_)
    {
        for (VillageDoorInfo villagedoorinfo : this.doorList)
        {
            if (p_75413_1_.func_179852_d().equals(villagedoorinfo.func_179852_d()))
            {
                return true;
            }
        }

        return false;
    }

    private void resizeDoorList()
    {
        if (this.doorList.size() > 15)
        {
            this.doorList.remove(0);
        }
    }
}
