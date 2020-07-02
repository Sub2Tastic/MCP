package net.minecraft.entity.ai;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAINearestAttackableTarget<T extends EntityLivingBase> extends EntityAITarget
{
    protected final Class<T> targetClass;
    private final int targetChance;
    protected final EntityAINearestAttackableTarget.Sorter field_75306_g;
    protected final Predicate <? super T > field_82643_g;
    protected T nearestTarget;

    public EntityAINearestAttackableTarget(EntityCreature p_i45878_1_, Class<T> p_i45878_2_, boolean p_i45878_3_)
    {
        this(p_i45878_1_, p_i45878_2_, p_i45878_3_, false);
    }

    public EntityAINearestAttackableTarget(EntityCreature p_i45879_1_, Class<T> p_i45879_2_, boolean p_i45879_3_, boolean p_i45879_4_)
    {
        this(p_i45879_1_, p_i45879_2_, 10, p_i45879_3_, p_i45879_4_, (Predicate)null);
    }

    public EntityAINearestAttackableTarget(EntityCreature p_i45880_1_, Class<T> p_i45880_2_, int p_i45880_3_, boolean p_i45880_4_, boolean p_i45880_5_, @Nullable final Predicate <? super T > p_i45880_6_)
    {
        super(p_i45880_1_, p_i45880_4_, p_i45880_5_);
        this.targetClass = p_i45880_2_;
        this.targetChance = p_i45880_3_;
        this.field_75306_g = new EntityAINearestAttackableTarget.Sorter(p_i45880_1_);
        this.func_75248_a(1);
        this.field_82643_g = new Predicate<T>()
        {
            public boolean apply(@Nullable T p_apply_1_)
            {
                if (p_apply_1_ == null)
                {
                    return false;
                }
                else if (p_i45880_6_ != null && !p_i45880_6_.apply(p_apply_1_))
                {
                    return false;
                }
                else
                {
                    return !EntitySelectors.NOT_SPECTATING.apply(p_apply_1_) ? false : EntityAINearestAttackableTarget.this.func_75296_a(p_apply_1_, false);
                }
            }
        };
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        if (this.targetChance > 0 && this.goalOwner.getRNG().nextInt(this.targetChance) != 0)
        {
            return false;
        }
        else if (this.targetClass != EntityPlayer.class && this.targetClass != EntityPlayerMP.class)
        {
            List<T> list = this.goalOwner.world.<T>getEntitiesWithinAABB(this.targetClass, this.getTargetableArea(this.getTargetDistance()), this.field_82643_g);

            if (list.isEmpty())
            {
                return false;
            }
            else
            {
                Collections.sort(list, this.field_75306_g);
                this.nearestTarget = list.get(0);
                return true;
            }
        }
        else
        {
            this.nearestTarget = (T)this.goalOwner.world.func_184150_a(this.goalOwner.posX, this.goalOwner.posY + (double)this.goalOwner.getEyeHeight(), this.goalOwner.posZ, this.getTargetDistance(), this.getTargetDistance(), new Function<EntityPlayer, Double>()
            {
                @Nullable
                public Double apply(@Nullable EntityPlayer p_apply_1_)
                {
                    ItemStack itemstack = p_apply_1_.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

                    if (itemstack.getItem() == Items.field_151144_bL)
                    {
                        int i = itemstack.getDamage();
                        boolean flag = EntityAINearestAttackableTarget.this.goalOwner instanceof EntitySkeleton && i == 0;
                        boolean flag1 = EntityAINearestAttackableTarget.this.goalOwner instanceof EntityZombie && i == 2;
                        boolean flag2 = EntityAINearestAttackableTarget.this.goalOwner instanceof EntityCreeper && i == 4;

                        if (flag || flag1 || flag2)
                        {
                            return 0.5D;
                        }
                    }

                    return 1.0D;
                }
            }, (Predicate<EntityPlayer>)this.field_82643_g);
            return this.nearestTarget != null;
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance)
    {
        return this.goalOwner.getBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.goalOwner.setAttackTarget(this.nearestTarget);
        super.startExecuting();
    }

    public static class Sorter implements Comparator<Entity>
    {
        private final Entity field_75459_b;

        public Sorter(Entity p_i1662_1_)
        {
            this.field_75459_b = p_i1662_1_;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_)
        {
            double d0 = this.field_75459_b.getDistanceSq(p_compare_1_);
            double d1 = this.field_75459_b.getDistanceSq(p_compare_2_);

            if (d0 < d1)
            {
                return -1;
            }
            else
            {
                return d0 > d1 ? 1 : 0;
            }
        }
    }
}
