package net.minecraft.entity;

import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityCreature extends EntityLiving
{
    public static final UUID field_110179_h = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
    public static final AttributeModifier field_110181_i = (new AttributeModifier(field_110179_h, "Fleeing speed bonus", 2.0D, 2)).setSaved(false);
    private BlockPos field_70775_bC = BlockPos.ZERO;
    private float field_70772_bD = -1.0F;
    private final float field_184661_bw = PathNodeType.WATER.getPriority();

    public EntityCreature(World p_i1602_1_)
    {
        super(p_i1602_1_);
    }

    public float getBlockPathWeight(BlockPos pos)
    {
        return 0.0F;
    }

    public boolean func_70601_bi()
    {
        return super.func_70601_bi() && this.getBlockPathWeight(new BlockPos(this.posX, this.getBoundingBox().minY, this.posZ)) >= 0.0F;
    }

    /**
     * if the entity got a PathEntity it returns true, else false
     */
    public boolean hasPath()
    {
        return !this.navigator.noPath();
    }

    public boolean func_110173_bK()
    {
        return this.func_180485_d(new BlockPos(this));
    }

    public boolean func_180485_d(BlockPos p_180485_1_)
    {
        if (this.field_70772_bD == -1.0F)
        {
            return true;
        }
        else
        {
            return this.field_70775_bC.distanceSq(p_180485_1_) < (double)(this.field_70772_bD * this.field_70772_bD);
        }
    }

    public void func_175449_a(BlockPos p_175449_1_, int p_175449_2_)
    {
        this.field_70775_bC = p_175449_1_;
        this.field_70772_bD = (float)p_175449_2_;
    }

    public BlockPos func_180486_cf()
    {
        return this.field_70775_bC;
    }

    public float func_110174_bM()
    {
        return this.field_70772_bD;
    }

    public void func_110177_bN()
    {
        this.field_70772_bD = -1.0F;
    }

    public boolean func_110175_bO()
    {
        return this.field_70772_bD != -1.0F;
    }

    /**
     * Applies logic related to leashes, for example dragging the entity or breaking the leash.
     */
    protected void updateLeashedState()
    {
        super.updateLeashedState();

        if (this.getLeashed() && this.getLeashHolder() != null && this.getLeashHolder().world == this.world)
        {
            Entity entity = this.getLeashHolder();
            this.func_175449_a(new BlockPos((int)entity.posX, (int)entity.posY, (int)entity.posZ), 5);
            float f = this.getDistance(entity);

            if (this instanceof EntityTameable && ((EntityTameable)this).isSitting())
            {
                if (f > 10.0F)
                {
                    this.clearLeashed(true, true);
                }

                return;
            }

            this.onLeashDistance(f);

            if (f > 10.0F)
            {
                this.clearLeashed(true, true);
                this.goalSelector.func_188526_c(1);
            }
            else if (f > 6.0F)
            {
                double d0 = (entity.posX - this.posX) / (double)f;
                double d1 = (entity.posY - this.posY) / (double)f;
                double d2 = (entity.posZ - this.posZ) / (double)f;
                this.field_70159_w += d0 * Math.abs(d0) * 0.4D;
                this.field_70181_x += d1 * Math.abs(d1) * 0.4D;
                this.field_70179_y += d2 * Math.abs(d2) * 0.4D;
            }
            else
            {
                this.goalSelector.func_188525_d(1);
                float f1 = 2.0F;
                Vec3d vec3d = (new Vec3d(entity.posX - this.posX, entity.posY - this.posY, entity.posZ - this.posZ)).normalize().scale((double)Math.max(f - 2.0F, 0.0F));
                this.getNavigator().tryMoveToXYZ(this.posX + vec3d.x, this.posY + vec3d.y, this.posZ + vec3d.z, this.followLeashSpeed());
            }
        }
    }

    protected double followLeashSpeed()
    {
        return 1.0D;
    }

    protected void onLeashDistance(float distance)
    {
    }
}
