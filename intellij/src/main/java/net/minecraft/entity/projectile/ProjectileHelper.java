package net.minecraft.entity.projectile;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class ProjectileHelper
{
    public static RayTraceResult func_188802_a(Entity p_188802_0_, boolean p_188802_1_, boolean p_188802_2_, Entity p_188802_3_)
    {
        double d0 = p_188802_0_.posX;
        double d1 = p_188802_0_.posY;
        double d2 = p_188802_0_.posZ;
        double d3 = p_188802_0_.field_70159_w;
        double d4 = p_188802_0_.field_70181_x;
        double d5 = p_188802_0_.field_70179_y;
        World world = p_188802_0_.world;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        Vec3d vec3d1 = new Vec3d(d0 + d3, d1 + d4, d2 + d5);
        RayTraceResult raytraceresult = world.func_147447_a(vec3d, vec3d1, false, true, false);

        if (p_188802_1_)
        {
            if (raytraceresult != null)
            {
                vec3d1 = new Vec3d(raytraceresult.hitResult.x, raytraceresult.hitResult.y, raytraceresult.hitResult.z);
            }

            Entity entity = null;
            List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(p_188802_0_, p_188802_0_.getBoundingBox().expand(d3, d4, d5).grow(1.0D));
            double d6 = 0.0D;

            for (int i = 0; i < list.size(); ++i)
            {
                Entity entity1 = list.get(i);

                if (entity1.canBeCollidedWith() && (p_188802_2_ || !entity1.isEntityEqual(p_188802_3_)) && !entity1.noClip)
                {
                    AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow(0.30000001192092896D);
                    RayTraceResult raytraceresult1 = axisalignedbb.func_72327_a(vec3d, vec3d1);

                    if (raytraceresult1 != null)
                    {
                        double d7 = vec3d.squareDistanceTo(raytraceresult1.hitResult);

                        if (d7 < d6 || d6 == 0.0D)
                        {
                            entity = entity1;
                            d6 = d7;
                        }
                    }
                }
            }

            if (entity != null)
            {
                raytraceresult = new RayTraceResult(entity);
            }
        }

        return raytraceresult;
    }

    public static final void rotateTowardsMovement(Entity projectile, float rotationSpeed)
    {
        double d0 = projectile.field_70159_w;
        double d1 = projectile.field_70181_x;
        double d2 = projectile.field_70179_y;
        float f = MathHelper.sqrt(d0 * d0 + d2 * d2);
        projectile.rotationYaw = (float)(MathHelper.atan2(d2, d0) * (180D / Math.PI)) + 90.0F;

        for (projectile.rotationPitch = (float)(MathHelper.atan2((double)f, d1) * (180D / Math.PI)) - 90.0F; projectile.rotationPitch - projectile.prevRotationPitch < -180.0F; projectile.prevRotationPitch -= 360.0F)
        {
            ;
        }

        while (projectile.rotationPitch - projectile.prevRotationPitch >= 180.0F)
        {
            projectile.prevRotationPitch += 360.0F;
        }

        while (projectile.rotationYaw - projectile.prevRotationYaw < -180.0F)
        {
            projectile.prevRotationYaw -= 360.0F;
        }

        while (projectile.rotationYaw - projectile.prevRotationYaw >= 180.0F)
        {
            projectile.prevRotationYaw += 360.0F;
        }

        projectile.rotationPitch = projectile.prevRotationPitch + (projectile.rotationPitch - projectile.prevRotationPitch) * rotationSpeed;
        projectile.rotationYaw = projectile.prevRotationYaw + (projectile.rotationYaw - projectile.prevRotationYaw) * rotationSpeed;
    }
}
