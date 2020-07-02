package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityAIVillagerMate extends EntityAIBase
{
    private final EntityVillager field_75450_b;
    private EntityVillager field_75451_c;
    private final World field_75448_d;
    private int field_75449_e;
    Village field_75452_a;

    public EntityAIVillagerMate(EntityVillager p_i1634_1_)
    {
        this.field_75450_b = p_i1634_1_;
        this.field_75448_d = p_i1634_1_.world;
        this.func_75248_a(3);
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        if (this.field_75450_b.getGrowingAge() != 0)
        {
            return false;
        }
        else if (this.field_75450_b.getRNG().nextInt(500) != 0)
        {
            return false;
        }
        else
        {
            this.field_75452_a = this.field_75448_d.func_175714_ae().func_176056_a(new BlockPos(this.field_75450_b), 0);

            if (this.field_75452_a == null)
            {
                return false;
            }
            else if (this.func_75446_f() && this.field_75450_b.func_175550_n(true))
            {
                Entity entity = this.field_75448_d.func_72857_a(EntityVillager.class, this.field_75450_b.getBoundingBox().grow(8.0D, 3.0D, 8.0D), this.field_75450_b);

                if (entity == null)
                {
                    return false;
                }
                else
                {
                    this.field_75451_c = (EntityVillager)entity;
                    return this.field_75451_c.getGrowingAge() == 0 && this.field_75451_c.func_175550_n(true);
                }
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
        this.field_75449_e = 300;
        this.field_75450_b.func_70947_e(true);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.field_75452_a = null;
        this.field_75451_c = null;
        this.field_75450_b.func_70947_e(false);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return this.field_75449_e >= 0 && this.func_75446_f() && this.field_75450_b.getGrowingAge() == 0 && this.field_75450_b.func_175550_n(false);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick()
    {
        --this.field_75449_e;
        this.field_75450_b.getLookController().setLookPositionWithEntity(this.field_75451_c, 10.0F, 30.0F);

        if (this.field_75450_b.getDistanceSq(this.field_75451_c) > 2.25D)
        {
            this.field_75450_b.getNavigator().tryMoveToEntityLiving(this.field_75451_c, 0.25D);
        }
        else if (this.field_75449_e == 0 && this.field_75451_c.func_70941_o())
        {
            this.func_75447_i();
        }

        if (this.field_75450_b.getRNG().nextInt(35) == 0)
        {
            this.field_75448_d.setEntityState(this.field_75450_b, (byte)12);
        }
    }

    private boolean func_75446_f()
    {
        if (!this.field_75452_a.func_82686_i())
        {
            return false;
        }
        else
        {
            int i = (int)((double)((float)this.field_75452_a.func_75567_c()) * 0.35D);
            return this.field_75452_a.func_75562_e() < i;
        }
    }

    private void func_75447_i()
    {
        EntityVillager entityvillager = this.field_75450_b.createChild(this.field_75451_c);
        this.field_75451_c.setGrowingAge(6000);
        this.field_75450_b.setGrowingAge(6000);
        this.field_75451_c.func_175549_o(false);
        this.field_75450_b.func_175549_o(false);
        entityvillager.setGrowingAge(-24000);
        entityvillager.setLocationAndAngles(this.field_75450_b.posX, this.field_75450_b.posY, this.field_75450_b.posZ, 0.0F, 0.0F);
        this.field_75448_d.addEntity0(entityvillager);
        this.field_75448_d.setEntityState(entityvillager, (byte)12);
    }
}
