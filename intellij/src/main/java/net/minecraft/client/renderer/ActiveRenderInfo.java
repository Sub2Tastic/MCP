package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.util.glu.GLU;

public class ActiveRenderInfo
{
    private static final IntBuffer field_178814_a = GLAllocation.func_74527_f(16);
    private static final FloatBuffer field_178812_b = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer field_178813_c = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer field_178810_d = GLAllocation.createDirectFloatBuffer(3);
    private static Vec3d field_178811_e = new Vec3d(0.0D, 0.0D, 0.0D);
    private static float field_74588_d;
    private static float field_74589_e;
    private static float field_74586_f;
    private static float field_74587_g;
    private static float field_74596_h;

    public static void func_74583_a(EntityPlayer p_74583_0_, boolean p_74583_1_)
    {
        GlStateManager.func_179111_a(2982, field_178812_b);
        GlStateManager.func_179111_a(2983, field_178813_c);
        GlStateManager.func_187445_a(2978, field_178814_a);
        float f = (float)((field_178814_a.get(0) + field_178814_a.get(2)) / 2);
        float f1 = (float)((field_178814_a.get(1) + field_178814_a.get(3)) / 2);
        GLU.gluUnProject(f, f1, 0.0F, field_178812_b, field_178813_c, field_178814_a, field_178810_d);
        field_178811_e = new Vec3d((double)field_178810_d.get(0), (double)field_178810_d.get(1), (double)field_178810_d.get(2));
        int i = p_74583_1_ ? 1 : 0;
        float f2 = p_74583_0_.rotationPitch;
        float f3 = p_74583_0_.rotationYaw;
        field_74588_d = MathHelper.cos(f3 * 0.017453292F) * (float)(1 - i * 2);
        field_74586_f = MathHelper.sin(f3 * 0.017453292F) * (float)(1 - i * 2);
        field_74587_g = -field_74586_f * MathHelper.sin(f2 * 0.017453292F) * (float)(1 - i * 2);
        field_74596_h = field_74588_d * MathHelper.sin(f2 * 0.017453292F) * (float)(1 - i * 2);
        field_74589_e = MathHelper.cos(f2 * 0.017453292F);
    }

    public static Vec3d func_178806_a(Entity p_178806_0_, double p_178806_1_)
    {
        double d0 = p_178806_0_.prevPosX + (p_178806_0_.posX - p_178806_0_.prevPosX) * p_178806_1_;
        double d1 = p_178806_0_.prevPosY + (p_178806_0_.posY - p_178806_0_.prevPosY) * p_178806_1_;
        double d2 = p_178806_0_.prevPosZ + (p_178806_0_.posZ - p_178806_0_.prevPosZ) * p_178806_1_;
        double d3 = d0 + field_178811_e.x;
        double d4 = d1 + field_178811_e.y;
        double d5 = d2 + field_178811_e.z;
        return new Vec3d(d3, d4, d5);
    }

    public static IBlockState func_186703_a(World p_186703_0_, Entity p_186703_1_, float p_186703_2_)
    {
        Vec3d vec3d = func_178806_a(p_186703_1_, (double)p_186703_2_);
        BlockPos blockpos = new BlockPos(vec3d);
        IBlockState iblockstate = p_186703_0_.getBlockState(blockpos);

        if (iblockstate.getMaterial().isLiquid())
        {
            float f = 0.0F;

            if (iblockstate.getBlock() instanceof BlockLiquid)
            {
                f = BlockLiquid.func_149801_b(((Integer)iblockstate.get(BlockLiquid.LEVEL)).intValue()) - 0.11111111F;
            }

            float f1 = (float)(blockpos.getY() + 1) - f;

            if (vec3d.y >= (double)f1)
            {
                iblockstate = p_186703_0_.getBlockState(blockpos.up());
            }
        }

        return iblockstate;
    }

    public static float func_178808_b()
    {
        return field_74588_d;
    }

    public static float func_178809_c()
    {
        return field_74589_e;
    }

    public static float func_178803_d()
    {
        return field_74586_f;
    }

    public static float func_178805_e()
    {
        return field_74587_g;
    }

    public static float func_178807_f()
    {
        return field_74596_h;
    }
}
