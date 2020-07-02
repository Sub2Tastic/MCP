package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAITargetNonTamed<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T>
{
    private final EntityTameable tameable;

    public EntityAITargetNonTamed(EntityTameable p_i45876_1_, Class<T> p_i45876_2_, boolean p_i45876_3_, Predicate <? super T > p_i45876_4_)
    {
        super(p_i45876_1_, p_i45876_2_, 10, p_i45876_3_, false, p_i45876_4_);
        this.tameable = p_i45876_1_;
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        return !this.tameable.isTamed() && super.shouldExecute();
    }
}
