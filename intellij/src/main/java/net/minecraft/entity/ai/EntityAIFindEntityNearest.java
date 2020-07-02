package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAIFindEntityNearest extends EntityAIBase
{
    private static final Logger field_179444_a = LogManager.getLogger();
    private final EntityLiving field_179442_b;
    private final Predicate<EntityLivingBase> field_179443_c;
    private final EntityAINearestAttackableTarget.Sorter field_179440_d;
    private EntityLivingBase field_179441_e;
    private final Class <? extends EntityLivingBase > field_179439_f;

    public EntityAIFindEntityNearest(EntityLiving p_i45884_1_, Class <? extends EntityLivingBase > p_i45884_2_)
    {
        this.field_179442_b = p_i45884_1_;
        this.field_179439_f = p_i45884_2_;

        if (p_i45884_1_ instanceof EntityCreature)
        {
            field_179444_a.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }

        this.field_179443_c = new Predicate<EntityLivingBase>()
        {
            public boolean apply(@Nullable EntityLivingBase p_apply_1_)
            {
                double d0 = EntityAIFindEntityNearest.this.func_179438_f();

                if (p_apply_1_.func_70093_af())
                {
                    d0 *= 0.800000011920929D;
                }

                if (p_apply_1_.isInvisible())
                {
                    return false;
                }
                else
                {
                    return (double)p_apply_1_.getDistance(EntityAIFindEntityNearest.this.field_179442_b) > d0 ? false : EntityAITarget.func_179445_a(EntityAIFindEntityNearest.this.field_179442_b, p_apply_1_, false, true);
                }
            }
        };
        this.field_179440_d = new EntityAINearestAttackableTarget.Sorter(p_i45884_1_);
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        double d0 = this.func_179438_f();
        List<EntityLivingBase> list = this.field_179442_b.world.<EntityLivingBase>getEntitiesWithinAABB(this.field_179439_f, this.field_179442_b.getBoundingBox().grow(d0, 4.0D, d0), this.field_179443_c);
        Collections.sort(list, this.field_179440_d);

        if (list.isEmpty())
        {
            return false;
        }
        else
        {
            this.field_179441_e = list.get(0);
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        EntityLivingBase entitylivingbase = this.field_179442_b.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else if (!entitylivingbase.isAlive())
        {
            return false;
        }
        else
        {
            double d0 = this.func_179438_f();

            if (this.field_179442_b.getDistanceSq(entitylivingbase) > d0 * d0)
            {
                return false;
            }
            else
            {
                return !(entitylivingbase instanceof EntityPlayerMP) || !((EntityPlayerMP)entitylivingbase).interactionManager.isCreative();
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.field_179442_b.setAttackTarget(this.field_179441_e);
        super.startExecuting();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.field_179442_b.setAttackTarget((EntityLivingBase)null);
        super.startExecuting();
    }

    protected double func_179438_f()
    {
        IAttributeInstance iattributeinstance = this.field_179442_b.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getValue();
    }
}
