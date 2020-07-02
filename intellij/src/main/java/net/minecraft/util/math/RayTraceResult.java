package net.minecraft.util.math;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;

public class RayTraceResult
{
    private BlockPos field_178783_e;
    public RayTraceResult.Type field_72313_a;
    public EnumFacing field_178784_b;
    public Vec3d hitResult;
    public Entity field_72308_g;

    public RayTraceResult(Vec3d p_i47094_1_, EnumFacing p_i47094_2_, BlockPos p_i47094_3_)
    {
        this(RayTraceResult.Type.BLOCK, p_i47094_1_, p_i47094_2_, p_i47094_3_);
    }

    public RayTraceResult(Vec3d p_i47095_1_, EnumFacing p_i47095_2_)
    {
        this(RayTraceResult.Type.BLOCK, p_i47095_1_, p_i47095_2_, BlockPos.ZERO);
    }

    public RayTraceResult(Entity p_i2304_1_)
    {
        this(p_i2304_1_, new Vec3d(p_i2304_1_.posX, p_i2304_1_.posY, p_i2304_1_.posZ));
    }

    public RayTraceResult(RayTraceResult.Type p_i47096_1_, Vec3d p_i47096_2_, EnumFacing p_i47096_3_, BlockPos p_i47096_4_)
    {
        this.field_72313_a = p_i47096_1_;
        this.field_178783_e = p_i47096_4_;
        this.field_178784_b = p_i47096_3_;
        this.hitResult = new Vec3d(p_i47096_2_.x, p_i47096_2_.y, p_i47096_2_.z);
    }

    public RayTraceResult(Entity p_i47097_1_, Vec3d p_i47097_2_)
    {
        this.field_72313_a = RayTraceResult.Type.ENTITY;
        this.field_72308_g = p_i47097_1_;
        this.hitResult = p_i47097_2_;
    }

    public BlockPos func_178782_a()
    {
        return this.field_178783_e;
    }

    public String toString()
    {
        return "HitResult{type=" + this.field_72313_a + ", blockpos=" + this.field_178783_e + ", f=" + this.field_178784_b + ", pos=" + this.hitResult + ", entity=" + this.field_72308_g + '}';
    }

    public static enum Type
    {
        MISS,
        BLOCK,
        ENTITY;
    }
}
