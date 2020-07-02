package net.minecraft.client.renderer.debug;

import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugRendererNeighborsUpdate implements DebugRenderer.IDebugRenderer
{
    private final Minecraft minecraft;
    private final Map<Long, Map<BlockPos, Integer>> lastUpdate = Maps.newTreeMap(Ordering.natural().reverse());

    DebugRendererNeighborsUpdate(Minecraft minecraftIn)
    {
        this.minecraft = minecraftIn;
    }

    public void addUpdate(long worldTime, BlockPos pos)
    {
        Map<BlockPos, Integer> map = (Map)this.lastUpdate.get(Long.valueOf(worldTime));

        if (map == null)
        {
            map = Maps.<BlockPos, Integer>newHashMap();
            this.lastUpdate.put(Long.valueOf(worldTime), map);
        }

        Integer integer = map.get(pos);

        if (integer == null)
        {
            integer = Integer.valueOf(0);
        }

        map.put(pos, Integer.valueOf(integer.intValue() + 1));
    }

    public void func_190060_a(float p_190060_1_, long p_190060_2_)
    {
        long i = this.minecraft.world.getGameTime();
        EntityPlayer entityplayer = this.minecraft.player;
        double d0 = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)p_190060_1_;
        double d1 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)p_190060_1_;
        double d2 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)p_190060_1_;
        World world = this.minecraft.player.world;
        GlStateManager.func_179147_l();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.func_187441_d(2.0F);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a(false);
        int j = 200;
        double d3 = 0.0025D;
        Set<BlockPos> set = Sets.<BlockPos>newHashSet();
        Map<BlockPos, Integer> map = Maps.<BlockPos, Integer>newHashMap();
        Iterator<Entry<Long, Map<BlockPos, Integer>>> iterator = this.lastUpdate.entrySet().iterator();

        while (iterator.hasNext())
        {
            Entry<Long, Map<BlockPos, Integer>> entry = (Entry)iterator.next();
            Long olong = entry.getKey();
            Map<BlockPos, Integer> map1 = (Map)entry.getValue();
            long k = i - olong.longValue();

            if (k > 200L)
            {
                iterator.remove();
            }
            else
            {
                for (Entry<BlockPos, Integer> entry1 : map1.entrySet())
                {
                    BlockPos blockpos = entry1.getKey();
                    Integer integer = entry1.getValue();

                    if (set.add(blockpos))
                    {
                        RenderGlobal.func_189697_a((new AxisAlignedBB(BlockPos.ZERO)).grow(0.002D).shrink(0.0025D * (double)k).offset((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ()).offset(-d0, -d1, -d2), 1.0F, 1.0F, 1.0F, 1.0F);
                        map.put(blockpos, integer);
                    }
                }
            }
        }

        for (Entry<BlockPos, Integer> entry2 : map.entrySet())
        {
            BlockPos blockpos1 = entry2.getKey();
            Integer integer1 = entry2.getValue();
            DebugRenderer.func_191556_a(String.valueOf((Object)integer1), blockpos1.getX(), blockpos1.getY(), blockpos1.getZ(), p_190060_1_, -1);
        }

        GlStateManager.func_179132_a(true);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }
}
