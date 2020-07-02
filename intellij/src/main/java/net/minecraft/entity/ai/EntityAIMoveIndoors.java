package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIMoveIndoors extends EntityAIBase
{
    private final EntityCreature field_75424_a;
    private VillageDoorInfo field_75422_b;
    private int field_75423_c = -1;
    private int field_75421_d = -1;

    public EntityAIMoveIndoors(EntityCreature p_i1637_1_)
    {
        this.field_75424_a = p_i1637_1_;
        this.func_75248_a(1);
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        BlockPos blockpos = new BlockPos(this.field_75424_a);

        if ((!this.field_75424_a.world.isDaytime() || this.field_75424_a.world.isRaining() && !this.field_75424_a.world.func_180494_b(blockpos).func_76738_d()) && this.field_75424_a.world.dimension.hasSkyLight())
        {
            if (this.field_75424_a.getRNG().nextInt(50) != 0)
            {
                return false;
            }
            else if (this.field_75423_c != -1 && this.field_75424_a.getDistanceSq((double)this.field_75423_c, this.field_75424_a.posY, (double)this.field_75421_d) < 4.0D)
            {
                return false;
            }
            else
            {
                Village village = this.field_75424_a.world.func_175714_ae().func_176056_a(blockpos, 14);

                if (village == null)
                {
                    return false;
                }
                else
                {
                    this.field_75422_b = village.func_179863_c(blockpos);
                    return this.field_75422_b != null;
                }
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return !this.field_75424_a.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.field_75423_c = -1;
        BlockPos blockpos = this.field_75422_b.func_179856_e();
        int i = blockpos.getX();
        int j = blockpos.getY();
        int k = blockpos.getZ();

        if (this.field_75424_a.func_174818_b(blockpos) > 256.0D)
        {
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.field_75424_a, 14, 3, new Vec3d((double)i + 0.5D, (double)j, (double)k + 0.5D));

            if (vec3d != null)
            {
                this.field_75424_a.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.0D);
            }
        }
        else
        {
            this.field_75424_a.getNavigator().tryMoveToXYZ((double)i + 0.5D, (double)j, (double)k + 0.5D, 1.0D);
        }
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.field_75423_c = this.field_75422_b.func_179856_e().getX();
        this.field_75421_d = this.field_75422_b.func_179856_e().getZ();
        this.field_75422_b = null;
    }
}
