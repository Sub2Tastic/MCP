package net.minecraft.client.renderer.debug;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

public class DebugRendererPathfinding implements DebugRenderer.IDebugRenderer
{
    private final Minecraft field_188290_a;
    private final Map<Integer, Path> pathMap = Maps.<Integer, Path>newHashMap();
    private final Map<Integer, Float> pathMaxDistance = Maps.<Integer, Float>newHashMap();
    private final Map<Integer, Long> creationMap = Maps.<Integer, Long>newHashMap();
    private EntityPlayer field_190068_e;
    private double field_190069_f;
    private double field_190070_g;
    private double field_190071_h;

    public DebugRendererPathfinding(Minecraft p_i46556_1_)
    {
        this.field_188290_a = p_i46556_1_;
    }

    public void addPath(int eid, Path pathIn, float distance)
    {
        this.pathMap.put(Integer.valueOf(eid), pathIn);
        this.creationMap.put(Integer.valueOf(eid), Long.valueOf(System.currentTimeMillis()));
        this.pathMaxDistance.put(Integer.valueOf(eid), Float.valueOf(distance));
    }

    public void func_190060_a(float p_190060_1_, long p_190060_2_)
    {
        if (!this.pathMap.isEmpty())
        {
            long i = System.currentTimeMillis();
            this.field_190068_e = this.field_188290_a.player;
            this.field_190069_f = this.field_190068_e.lastTickPosX + (this.field_190068_e.posX - this.field_190068_e.lastTickPosX) * (double)p_190060_1_;
            this.field_190070_g = this.field_190068_e.lastTickPosY + (this.field_190068_e.posY - this.field_190068_e.lastTickPosY) * (double)p_190060_1_;
            this.field_190071_h = this.field_190068_e.lastTickPosZ + (this.field_190068_e.posZ - this.field_190068_e.lastTickPosZ) * (double)p_190060_1_;
            GlStateManager.func_179094_E();
            GlStateManager.func_179147_l();
            GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.func_179131_c(0.0F, 1.0F, 0.0F, 0.75F);
            GlStateManager.func_179090_x();
            GlStateManager.func_187441_d(6.0F);

            for (Integer integer : this.pathMap.keySet())
            {
                Path path = this.pathMap.get(integer);
                float f = ((Float)this.pathMaxDistance.get(integer)).floatValue();
                this.func_190067_a(p_190060_1_, path);
                PathPoint pathpoint = path.func_189964_i();

                if (this.func_190066_a(pathpoint) <= 40.0F)
                {
                    RenderGlobal.func_189696_b((new AxisAlignedBB((double)((float)pathpoint.x + 0.25F), (double)((float)pathpoint.y + 0.25F), (double)pathpoint.z + 0.25D, (double)((float)pathpoint.x + 0.75F), (double)((float)pathpoint.y + 0.75F), (double)((float)pathpoint.z + 0.75F))).offset(-this.field_190069_f, -this.field_190070_g, -this.field_190071_h), 0.0F, 1.0F, 0.0F, 0.5F);

                    for (int j = 0; j < path.getCurrentPathLength(); ++j)
                    {
                        PathPoint pathpoint1 = path.getPathPointFromIndex(j);

                        if (this.func_190066_a(pathpoint1) <= 40.0F)
                        {
                            float f1 = j == path.getCurrentPathIndex() ? 1.0F : 0.0F;
                            float f2 = j == path.getCurrentPathIndex() ? 0.0F : 1.0F;
                            RenderGlobal.func_189696_b((new AxisAlignedBB((double)((float)pathpoint1.x + 0.5F - f), (double)((float)pathpoint1.y + 0.01F * (float)j), (double)((float)pathpoint1.z + 0.5F - f), (double)((float)pathpoint1.x + 0.5F + f), (double)((float)pathpoint1.y + 0.25F + 0.01F * (float)j), (double)((float)pathpoint1.z + 0.5F + f))).offset(-this.field_190069_f, -this.field_190070_g, -this.field_190071_h), f1, 0.0F, f2, 0.5F);
                        }
                    }
                }
            }

            for (Integer integer1 : this.pathMap.keySet())
            {
                Path path1 = this.pathMap.get(integer1);

                for (PathPoint pathpoint3 : path1.getClosedSet())
                {
                    if (this.func_190066_a(pathpoint3) <= 40.0F)
                    {
                        DebugRenderer.func_190076_a(String.format("%s", pathpoint3.nodeType), (double)pathpoint3.x + 0.5D, (double)pathpoint3.y + 0.75D, (double)pathpoint3.z + 0.5D, p_190060_1_, -65536);
                        DebugRenderer.func_190076_a(String.format("%.2f", pathpoint3.costMalus), (double)pathpoint3.x + 0.5D, (double)pathpoint3.y + 0.25D, (double)pathpoint3.z + 0.5D, p_190060_1_, -65536);
                    }
                }

                for (PathPoint pathpoint4 : path1.getOpenSet())
                {
                    if (this.func_190066_a(pathpoint4) <= 40.0F)
                    {
                        DebugRenderer.func_190076_a(String.format("%s", pathpoint4.nodeType), (double)pathpoint4.x + 0.5D, (double)pathpoint4.y + 0.75D, (double)pathpoint4.z + 0.5D, p_190060_1_, -16776961);
                        DebugRenderer.func_190076_a(String.format("%.2f", pathpoint4.costMalus), (double)pathpoint4.x + 0.5D, (double)pathpoint4.y + 0.25D, (double)pathpoint4.z + 0.5D, p_190060_1_, -16776961);
                    }
                }

                for (int k = 0; k < path1.getCurrentPathLength(); ++k)
                {
                    PathPoint pathpoint2 = path1.getPathPointFromIndex(k);

                    if (this.func_190066_a(pathpoint2) <= 40.0F)
                    {
                        DebugRenderer.func_190076_a(String.format("%s", pathpoint2.nodeType), (double)pathpoint2.x + 0.5D, (double)pathpoint2.y + 0.75D, (double)pathpoint2.z + 0.5D, p_190060_1_, -1);
                        DebugRenderer.func_190076_a(String.format("%.2f", pathpoint2.costMalus), (double)pathpoint2.x + 0.5D, (double)pathpoint2.y + 0.25D, (double)pathpoint2.z + 0.5D, p_190060_1_, -1);
                    }
                }
            }

            for (Integer integer2 : (Integer[])this.creationMap.keySet().toArray(new Integer[0]))
            {
                if (i - ((Long)this.creationMap.get(integer2)).longValue() > 20000L)
                {
                    this.pathMap.remove(integer2);
                    this.creationMap.remove(integer2);
                }
            }

            GlStateManager.func_179098_w();
            GlStateManager.func_179084_k();
            GlStateManager.func_179121_F();
        }
    }

    public void func_190067_a(float p_190067_1_, Path p_190067_2_)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);

        for (int i = 0; i < p_190067_2_.getCurrentPathLength(); ++i)
        {
            PathPoint pathpoint = p_190067_2_.getPathPointFromIndex(i);

            if (this.func_190066_a(pathpoint) <= 40.0F)
            {
                float f = (float)i / (float)p_190067_2_.getCurrentPathLength() * 0.33F;
                int j = i == 0 ? 0 : MathHelper.hsvToRGB(f, 0.9F, 0.9F);
                int k = j >> 16 & 255;
                int l = j >> 8 & 255;
                int i1 = j & 255;
                bufferbuilder.func_181662_b((double)pathpoint.x - this.field_190069_f + 0.5D, (double)pathpoint.y - this.field_190070_g + 0.5D, (double)pathpoint.z - this.field_190071_h + 0.5D).func_181669_b(k, l, i1, 255).endVertex();
            }
        }

        tessellator.draw();
    }

    private float func_190066_a(PathPoint p_190066_1_)
    {
        return (float)(Math.abs((double)p_190066_1_.x - this.field_190068_e.posX) + Math.abs((double)p_190066_1_.y - this.field_190068_e.posY) + Math.abs((double)p_190066_1_.z - this.field_190068_e.posZ));
    }
}
